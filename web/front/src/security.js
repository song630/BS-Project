// RSA, a suite of routines for performing RSA public-key computations in JavaScript.
// Copyright 1998-2005 David Shapiro.
// Dave Shapiro
// changed by Fuchun, 2010-05-06
// fcrpg2005@gmail.com

(function ($w) {
  let RSAUtils;
  if (typeof $w.RSAUtils === 'undefined') {
    RSAUtils = $w.RSAUtils = {};
  }
  // var biRadixBase = 2
  let biRadixBits = 16;
  let bitsPerDigit = biRadixBits;
  let biRadix = 1 << 16; // = 2^16 = 65536
  let biHalfRadix = biRadix >>> 1;
  let biRadixSquared = biRadix * biRadix;
  let maxDigitVal = biRadix - 1;
  // var maxInteger = 9999999999999998

  // maxDigits:
  // Change this to accommodate your largest number size. Use setMaxDigits()
  // to change it!
  //
  // In general, if you're working with numbers of size N bits, you'll need 2*N
  // bits of storage. Each digit holds 16 bits. So, a 1024-bit key will need
  //
  // 1024 * 2 / 16 = 128 digits of storage.
  //
  let maxDigits;
  let ZERO_ARRAY;
  let bigZero, bigOne;

  let BigInt = $w.BigInt = function (flag) {
    if (typeof flag === 'boolean' && flag === true) {
      this.digits = null;
    } else {
      this.digits = ZERO_ARRAY.slice(0);
    }
    this.isNeg = false;
  };

  RSAUtils.setMaxDigits = function (value) {
    maxDigits = value;
    ZERO_ARRAY = new Array(maxDigits);
    for (let iza = 0; iza < ZERO_ARRAY.length; iza++) ZERO_ARRAY[iza] = 0;
    bigZero = new BigInt();
    bigOne = new BigInt();
    bigOne.digits[0] = 1;
  };
  RSAUtils.setMaxDigits(20);

  // The maximum number of digits in base 10 you can convert to an
  // integer without JavaScript throwing up on you.
  let dpl10 = 15;
  RSAUtils.biFromNumber = function (i) {
    let result = new BigInt();
    result.isNeg = i < 0;
    i = Math.abs(i);
    let j = 0;
    while (i > 0) {
      result.digits[j++] = i & maxDigitVal;
      i = Math.floor(i / biRadix);
    }
    return result;
  };

  // lr10 = 10 ^ dpl10
  let lr10 = RSAUtils.biFromNumber(1000000000000000);

  RSAUtils.biFromDecimal = function (s) {
    let isNeg = s.charAt(0) === '-';
    let i = isNeg ? 1 : 0;
    let result;
    // Skip leading zeros.
    while (i < s.length && s.charAt(i) === '0') ++i;
    if (i === s.length) {
      result = new BigInt();
    } else {
      let digitCount = s.length - i;
      let fgl = digitCount % dpl10;
      if (fgl === 0) fgl = dpl10;
      result = RSAUtils.biFromNumber(Number(s.substr(i, fgl)));
      i += fgl;
      while (i < s.length) {
        result = RSAUtils.biAdd(RSAUtils.biMultiply(result, lr10), RSAUtils.biFromNumber(Number(s.substr(i, dpl10))));
        i += dpl10;
      }
      result.isNeg = isNeg;
    }
    return result;
  };

  RSAUtils.biCopy = function (bi) {
    let result = new BigInt(true);
    result.digits = bi.digits.slice(0);
    result.isNeg = bi.isNeg;
    return result;
  };

  RSAUtils.reverseStr = function (s) {
    let result = '';
    for (let i = s.length - 1; i > -1; --i) {
      result += s.charAt(i);
    }
    return result;
  };

  let hexatrigesimalToChar = [
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
    'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
    'u', 'v', 'w', 'x', 'y', 'z'
  ];

  RSAUtils.biToString = function (x, radix) { // 2 <= radix <= 36
    let b = new BigInt();
    b.digits[0] = radix;
    let qr = RSAUtils.biDivideModulo(x, b);
    let result = hexatrigesimalToChar[qr[1].digits[0]];
    while (RSAUtils.biCompare(qr[0], bigZero) === 1) {
      qr = RSAUtils.biDivideModulo(qr[0], b);
      // digit = qr[1].digits[0]
      result += hexatrigesimalToChar[qr[1].digits[0]];
    }
    return (x.isNeg ? '-' : '') + RSAUtils.reverseStr(result);
  };

  RSAUtils.biToDecimal = function (x) {
    let b = new BigInt();
    b.digits[0] = 10;
    let qr = RSAUtils.biDivideModulo(x, b);
    let result = String(qr[1].digits[0]);
    while (RSAUtils.biCompare(qr[0], bigZero) === 1) {
      qr = RSAUtils.biDivideModulo(qr[0], b);
      result += String(qr[1].digits[0]);
    }
    return (x.isNeg ? '-' : '') + RSAUtils.reverseStr(result);
  };

  let hexToChar = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    'a', 'b', 'c', 'd', 'e', 'f'];

  RSAUtils.digitToHex = function (n) {
    let mask = 0xf;
    let result = '';
    for (let i = 0; i < 4; ++i) {
      result += hexToChar[n & mask];
      n >>>= 4;
    }
    return RSAUtils.reverseStr(result);
  };

  RSAUtils.biToHex = function (x) {
    let result = '';
    // var n = RSAUtils.biHighIndex(x)
    for (let i = RSAUtils.biHighIndex(x); i > -1; --i) {
      result += RSAUtils.digitToHex(x.digits[i]);
    }
    return result;
  };

  RSAUtils.charToHex = function (c) {
    let ZERO = 48;
    let NINE = ZERO + 9;
    let littleA = 97;
    let littleZ = littleA + 25;
    let bigA = 65;
    let bigZ = 65 + 25;
    let result;

    if (c >= ZERO && c <= NINE) {
      result = c - ZERO;
    } else if (c >= bigA && c <= bigZ) {
      result = 10 + c - bigA;
    } else if (c >= littleA && c <= littleZ) {
      result = 10 + c - littleA;
    } else {
      result = 0;
    }
    return result;
  };

  RSAUtils.hexToDigit = function (s) {
    let result = 0;
    let sl = Math.min(s.length, 4);
    for (let i = 0; i < sl; ++i) {
      result <<= 4;
      result |= RSAUtils.charToHex(s.charCodeAt(i));
    }
    return result;
  };

  RSAUtils.biFromHex = function (s) {
    let result = new BigInt();
    let sl = s.length;
    for (let i = sl, j = 0; i > 0; i -= 4, ++j) {
      result.digits[j] = RSAUtils.hexToDigit(s.substr(Math.max(i - 4, 0), Math.min(i, 4)));
    }
    return result;
  };

  RSAUtils.biFromString = function (s, radix) {
    let isNeg = s.charAt(0) === '-';
    let istop = isNeg ? 1 : 0;
    let result = new BigInt();
    let place = new BigInt();
    place.digits[0] = 1; // radix^0
    for (let i = s.length - 1; i >= istop; i--) {
      let c = s.charCodeAt(i);
      let digit = RSAUtils.charToHex(c);
      let biDigit = RSAUtils.biMultiplyDigit(place, digit);
      result = RSAUtils.biAdd(result, biDigit);
      place = RSAUtils.biMultiplyDigit(place, radix);
    }
    result.isNeg = isNeg;
    return result;
  };

  RSAUtils.biDump = function (b) {
    return (b.isNeg ? '-' : '') + b.digits.join(' ');
  };

  RSAUtils.biAdd = function (x, y) {
    let result;
    if (x.isNeg !== y.isNeg) {
      y.isNeg = !y.isNeg;
      result = RSAUtils.biSubtract(x, y);
      y.isNeg = !y.isNeg;
    } else {
      result = new BigInt();
      let c = 0;
      let n;
      for (let i = 0; i < x.digits.length; ++i) {
        n = x.digits[i] + y.digits[i] + c;
        result.digits[i] = n % biRadix;
        c = Number(n >= biRadix);
      }
      result.isNeg = x.isNeg;
    }
    return result;
  };

  RSAUtils.biSubtract = function (x, y) {
    let result;
    if (x.isNeg !== y.isNeg) {
      y.isNeg = !y.isNeg;
      result = RSAUtils.biAdd(x, y);
      y.isNeg = !y.isNeg;
    } else {
      result = new BigInt();
      let n, c;
      c = 0;
      for (let i = 0; i < x.digits.length; ++i) {
        n = x.digits[i] - y.digits[i] + c;
        result.digits[i] = n % biRadix;
        // Stupid non-conforming modulus operation.
        if (result.digits[i] < 0) result.digits[i] += biRadix;
        c = 0 - Number(n < 0);
      }
      // Fix up the negative sign, if any.
      if (c === -1) {
        c = 0;
        for (let i = 0; i < x.digits.length; ++i) {
          n = 0 - result.digits[i] + c;
          result.digits[i] = n % biRadix;
          // Stupid non-conforming modulus operation.
          if (result.digits[i] < 0) result.digits[i] += biRadix;
          c = 0 - Number(n < 0);
        }
        // Result is opposite sign of arguments.
        result.isNeg = !x.isNeg;
      } else {
        // Result is same sign.
        result.isNeg = x.isNeg;
      }
    }
    return result;
  };

  RSAUtils.biHighIndex = function (x) {
    let result = x.digits.length - 1;
    while (result > 0 && x.digits[result] === 0) --result;
    return result;
  };

  RSAUtils.biNumBits = function (x) {
    let n = RSAUtils.biHighIndex(x);
    let d = x.digits[n];
    let m = (n + 1) * bitsPerDigit;
    let result;
    for (result = m; result > m - bitsPerDigit; --result) {
      if ((d & 0x8000) !== 0) break;
      d <<= 1;
    }
    return result;
  };

  RSAUtils.biMultiply = function (x, y) {
    let result = new BigInt();
    let c;
    let n = RSAUtils.biHighIndex(x);
    let t = RSAUtils.biHighIndex(y);
    let uv, k;

    for (let i = 0; i <= t; ++i) {
      c = 0;
      k = i;
      for (let j = 0; j <= n; ++j, ++k) {
        uv = result.digits[k] + x.digits[j] * y.digits[i] + c;
        result.digits[k] = uv & maxDigitVal;
        c = uv >>> biRadixBits;
        // c = Math.floor(uv / biRadix);
      }
      result.digits[i + n + 1] = c;
    }
    // Someone give me a logical xor, please.
    result.isNeg = x.isNeg !== y.isNeg;
    return result;
  };

  RSAUtils.biMultiplyDigit = function (x, y) {
    let n, c, uv;

    let result = new BigInt();
    n = RSAUtils.biHighIndex(x);
    c = 0;
    for (let j = 0; j <= n; ++j) {
      uv = result.digits[j] + x.digits[j] * y + c;
      result.digits[j] = uv & maxDigitVal;
      c = uv >>> biRadixBits;
      // c = Math.floor(uv / biRadix);
    }
    result.digits[1 + n] = c;
    return result;
  };

  RSAUtils.arrayCopy = function (src, srcStart, dest, destStart, n) {
    let m = Math.min(srcStart + n, src.length);
    for (let i = srcStart, j = destStart; i < m; ++i, ++j) {
      dest[j] = src[i];
    }
  };

  let highBitMasks = [0x0000, 0x8000, 0xC000, 0xE000, 0xF000, 0xF800,
    0xFC00, 0xFE00, 0xFF00, 0xFF80, 0xFFC0, 0xFFE0,
    0xFFF0, 0xFFF8, 0xFFFC, 0xFFFE, 0xFFFF];

  RSAUtils.biShiftLeft = function (x, n) {
    let digitCount = Math.floor(n / bitsPerDigit);
    let result = new BigInt();
    RSAUtils.arrayCopy(x.digits, 0, result.digits, digitCount,
      result.digits.length - digitCount);
    let bits = n % bitsPerDigit;
    let rightBits = bitsPerDigit - bits;
    let i, i1;
    for (i = result.digits.length - 1, i1 = i - 1; i > 0; --i, --i1) {
      result.digits[i] = ((result.digits[i] << bits) & maxDigitVal) |
                         ((result.digits[i1] & highBitMasks[bits]) >>>
                          (rightBits));
    }
    result.digits[0] = ((result.digits[i] << bits) & maxDigitVal);
    result.isNeg = x.isNeg;
    return result;
  };

  let lowBitMasks = [0x0000, 0x0001, 0x0003, 0x0007, 0x000F, 0x001F,
    0x003F, 0x007F, 0x00FF, 0x01FF, 0x03FF, 0x07FF,
    0x0FFF, 0x1FFF, 0x3FFF, 0x7FFF, 0xFFFF];

  RSAUtils.biShiftRight = function (x, n) {
    let digitCount = Math.floor(n / bitsPerDigit);
    let result = new BigInt();
    RSAUtils.arrayCopy(x.digits, digitCount, result.digits, 0,
      x.digits.length - digitCount);
    let bits = n % bitsPerDigit;
    let leftBits = bitsPerDigit - bits;
    for (let i = 0, i1 = i + 1; i < result.digits.length - 1; ++i, ++i1) {
      result.digits[i] = (result.digits[i] >>> bits) |
                         ((result.digits[i1] & lowBitMasks[bits]) << leftBits);
    }
    result.digits[result.digits.length - 1] >>>= bits;
    result.isNeg = x.isNeg;
    return result;
  };

  RSAUtils.biMultiplyByRadixPower = function (x, n) {
    let result = new BigInt();
    RSAUtils.arrayCopy(x.digits, 0, result.digits, n, result.digits.length - n);
    return result;
  };

  RSAUtils.biDivideByRadixPower = function (x, n) {
    let result = new BigInt();
    RSAUtils.arrayCopy(x.digits, n, result.digits, 0, result.digits.length - n);
    return result;
  };

  RSAUtils.biModuloByRadixPower = function (x, n) {
    let result = new BigInt();
    RSAUtils.arrayCopy(x.digits, 0, result.digits, 0, n);
    return result;
  };

  RSAUtils.biCompare = function (x, y) {
    if (x.isNeg !== y.isNeg) {
      return 1 - 2 * Number(x.isNeg);
    }
    for (let i = x.digits.length - 1; i >= 0; --i) {
      if (x.digits[i] !== y.digits[i]) {
        if (x.isNeg) {
          return 1 - 2 * Number(x.digits[i] > y.digits[i]);
        } else {
          return 1 - 2 * Number(x.digits[i] < y.digits[i]);
        }
      }
    }
    return 0
  };

  RSAUtils.biDivideModulo = function (x, y) {
    let nb = RSAUtils.biNumBits(x);
    let tb = RSAUtils.biNumBits(y);
    let origYIsNeg = y.isNeg;
    let q, r;
    if (nb < tb) {
      // |x| < |y|
      if (x.isNeg) {
        q = RSAUtils.biCopy(bigOne);
        q.isNeg = !y.isNeg;
        x.isNeg = false;
        y.isNeg = false;
        r = RSAUtils.biSubtract(y, x);
        // Restore signs, 'cause they're references.
        x.isNeg = true;
        y.isNeg = origYIsNeg;
      } else {
        q = new BigInt();
        r = RSAUtils.biCopy(x);
      }
      return [q, r];
    }

    q = new BigInt();
    r = x;

    // Normalize Y.
    let t = Math.ceil(tb / bitsPerDigit) - 1;
    let lambda = 0;
    while (y.digits[t] < biHalfRadix) {
      y = RSAUtils.biShiftLeft(y, 1);
      ++lambda;
      ++tb;
      t = Math.ceil(tb / bitsPerDigit) - 1;
    }
    // Shift r over to keep the quotient constant. We'll shift the
    // remainder back at the end.
    r = RSAUtils.biShiftLeft(r, lambda);
    nb += lambda; // Update the bit count for x.
    let n = Math.ceil(nb / bitsPerDigit) - 1;

    let b = RSAUtils.biMultiplyByRadixPower(y, n - t);
    while (RSAUtils.biCompare(r, b) !== -1) {
      ++q.digits[n - t];
      r = RSAUtils.biSubtract(r, b);
    }
    for (let i = n; i > t; --i) {
      let ri = (i >= r.digits.length) ? 0 : r.digits[i];
      let ri1 = (i - 1 >= r.digits.length) ? 0 : r.digits[i - 1];
      let ri2 = (i - 2 >= r.digits.length) ? 0 : r.digits[i - 2];
      let yt = (t >= y.digits.length) ? 0 : y.digits[t];
      let yt1 = (t - 1 >= y.digits.length) ? 0 : y.digits[t - 1];
      if (ri === yt) {
        q.digits[i - t - 1] = maxDigitVal;
      } else {
        q.digits[i - t - 1] = Math.floor((ri * biRadix + ri1) / yt);
      }

      let c1 = q.digits[i - t - 1] * ((yt * biRadix) + yt1);
      let c2 = (ri * biRadixSquared) + ((ri1 * biRadix) + ri2);
      while (c1 > c2) {
        --q.digits[i - t - 1];
        c1 = q.digits[i - t - 1] * ((yt * biRadix) | yt1);
        c2 = (ri * biRadix * biRadix) + ((ri1 * biRadix) + ri2);
      }

      b = RSAUtils.biMultiplyByRadixPower(y, i - t - 1);
      r = RSAUtils.biSubtract(r, RSAUtils.biMultiplyDigit(b, q.digits[i - t - 1]));
      if (r.isNeg) {
        r = RSAUtils.biAdd(r, b);
        --q.digits[i - t - 1];
      }
    }
    r = RSAUtils.biShiftRight(r, lambda);
    // Fiddle with the signs and stuff to make sure that 0 <= r < y.
    q.isNeg = x.isNeg !== origYIsNeg;
    if (x.isNeg) {
      if (origYIsNeg) {
        q = RSAUtils.biAdd(q, bigOne);
      } else {
        q = RSAUtils.biSubtract(q, bigOne);
      }
      y = RSAUtils.biShiftRight(y, lambda);
      r = RSAUtils.biSubtract(y, r);
    }
    // Check for the unbelievably stupid degenerate case of r == -0.
    if (r.digits[0] === 0 && RSAUtils.biHighIndex(r) === 0) r.isNeg = false;

    return [q, r];
  };

  RSAUtils.biDivide = function (x, y) {
    return RSAUtils.biDivideModulo(x, y)[0];
  };

  RSAUtils.biModulo = function (x, y) {
    return RSAUtils.biDivideModulo(x, y)[1];
  };

  RSAUtils.biMultiplyMod = function (x, y, m) {
    return RSAUtils.biModulo(RSAUtils.biMultiply(x, y), m);
  };

  RSAUtils.biPow = function (x, y) {
    let result = bigOne;
    let a = x;
    while (true) {
      if ((y & 1) !== 0) result = RSAUtils.biMultiply(result, a);
      y >>= 1;
      if (y === 0) break;
      a = RSAUtils.biMultiply(a, a);
    }
    return result;
  };

  RSAUtils.biPowMod = function (x, y, m) {
    let result = bigOne;
    let a = x;
    let k = y;
    while (true) {
      if ((k.digits[0] & 1) !== 0) result = RSAUtils.biMultiplyMod(result, a, m);
      k = RSAUtils.biShiftRight(k, 1);
      if (k.digits[0] === 0 && RSAUtils.biHighIndex(k) === 0) break;
      a = RSAUtils.biMultiplyMod(a, a, m);
    }
    return result;
  };

  $w.BarrettMu = function (m) {
    this.modulus = RSAUtils.biCopy(m);
    this.k = RSAUtils.biHighIndex(this.modulus) + 1;
    let b2k = new BigInt();
    b2k.digits[2 * this.k] = 1; // b2k = b^(2k)
    this.mu = RSAUtils.biDivide(b2k, this.modulus);
    this.bkplus1 = new BigInt();
    this.bkplus1.digits[this.k + 1] = 1; // bkplus1 = b^(k+1)
    this.modulo = BarrettMuModulo;
    this.multiplyMod = BarrettMuMultiplyMod;
    this.powMod = BarrettMuPowMod;
  };

  function BarrettMuModulo (x) {
    let $dmath = RSAUtils;
    let q1 = $dmath.biDivideByRadixPower(x, this.k - 1);
    let q2 = $dmath.biMultiply(q1, this.mu);
    let q3 = $dmath.biDivideByRadixPower(q2, this.k + 1);
    let r1 = $dmath.biModuloByRadixPower(x, this.k + 1);
    let r2term = $dmath.biMultiply(q3, this.modulus);
    let r2 = $dmath.biModuloByRadixPower(r2term, this.k + 1);
    let r = $dmath.biSubtract(r1, r2);
    if (r.isNeg) {
      r = $dmath.biAdd(r, this.bkplus1);
    }
    let rgtem = $dmath.biCompare(r, this.modulus) >= 0;
    while (rgtem) {
      r = $dmath.biSubtract(r, this.modulus);
      rgtem = $dmath.biCompare(r, this.modulus) >= 0;
    }
    return r;
  }

  function BarrettMuMultiplyMod (x, y) {
    /*
    x = this.modulo(x);
    y = this.modulo(y);
    */
    let xy = RSAUtils.biMultiply(x, y);
    return this.modulo(xy);
  }

  function BarrettMuPowMod (x, y) {
    let result = new BigInt();
    result.digits[0] = 1;
    let a = x;
    let k = y;
    while (true) {
      if ((k.digits[0] & 1) !== 0) result = this.multiplyMod(result, a);
      k = RSAUtils.biShiftRight(k, 1);
      if (k.digits[0] === 0 && RSAUtils.biHighIndex(k) === 0) break;
      a = this.multiplyMod(a, a);
    }
    return result;
  }

  let RSAKeyPair = function (encryptionExponent, decryptionExponent, modulus) {
    let $dmath = RSAUtils;
    this.e = $dmath.biFromHex(encryptionExponent);
    this.d = $dmath.biFromHex(decryptionExponent);
    this.m = $dmath.biFromHex(modulus);
    // We can do two bytes per digit, so
    // chunkSize = 2 * (number of digits in modulus - 1).
    // Since biHighIndex returns the high index, not the number of digits, 1 has
    // already been subtracted.
    this.chunkSize = 2 * $dmath.biHighIndex(this.m);
    this.radix = 16;
    this.barrett = new $w.BarrettMu(this.m);
  };

  RSAUtils.getKeyPair = function (encryptionExponent, decryptionExponent, modulus) {
    return new RSAKeyPair(encryptionExponent, decryptionExponent, modulus)
  };

  if (typeof $w.twoDigit === 'undefined') {
    $w.twoDigit = function (n) {
      return (n < 10 ? '0' : '') + String(n)
    }
  }

  // Altered by Rob Saunders (rob@robsaunders.net). New routine pads the
  // string after it has been converted to an array. This fixes an
  // incompatibility with Flash MX's ActionScript.
  RSAUtils.encryptedString = function (key, s) {
    let a = [];
    let sl = s.length;
    let i = 0;
    while (i < sl) {
      a[i] = s.charCodeAt(i);
      i++;
    }

    while (a.length % key.chunkSize !== 0) {
      a[i++] = 0;
    }

    let al = a.length;
    let result = '';
    let j, k, block;
    for (i = 0; i < al; i += key.chunkSize) {
      block = new BigInt();
      j = 0;
      for (k = i; k < i + key.chunkSize; ++j) {
        block.digits[j] = a[k++];
        block.digits[j] += a[k++] << 8;
      }
      let crypt = key.barrett.powMod(block, key.e);
      let text = key.radix === 16 ? RSAUtils.biToHex(crypt) : RSAUtils.biToString(crypt, key.radix);
      result += text + ' ';
    }
    return result.substring(0, result.length - 1); // Remove last space.
  };

  RSAUtils.decryptedString = function (key, s) {
    let blocks = s.split(' ');
    let result = '';
    let i, j, block;
    for (i = 0; i < blocks.length; ++i) {
      let bi;
      if (key.radix === 16) {
        bi = RSAUtils.biFromHex(blocks[i]);
      } else {
        bi = RSAUtils.biFromString(blocks[i], key.radix);
      }
      block = key.barrett.powMod(bi, key.d);
      for (j = 0; j <= RSAUtils.biHighIndex(block); ++j) {
        result += String.fromCharCode(block.digits[j] & 255,
          block.digits[j] >> 8);
      }
    }
    // Remove trailing null, if any.
    if (result.charCodeAt(result.length - 1) === 0) {
      result = result.substring(0, result.length - 1);
    }
    return result;
  };

  RSAUtils.setMaxDigits(130);
})(window);
