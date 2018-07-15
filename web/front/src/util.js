// 获取cookie
export function getCookie (name) {
  let cookies = document.cookie.replace(/\s+/g, ''); // remove space
  let arr = cookies.split(';');
  for (let i = 0; i < arr.length; i++) {
    let a = arr[i].split('=');
    if (a[0] === name) {
      return a[1];
    }
  }
  return null;
}

// 设置cookie 增加到vue实例方便全局调用
export function setCookie (name, value, expireDays) {
  let expireDate = new Date();
  expireDate.setDate(expireDate.getDate() + expireDays);
  document.cookie = name + '=' + escape(value) + ((expireDays == null) ? '' : ';expires=' + expireDate.toUTCString());
}

// 删除cookie
export function delCookie (name, expireDays) {
  let expireDate = new Date();
  expireDate.setDate(expireDate.getDate() + expireDays);
  document.cookie = name + '=' + escape('null') + ((expireDays == null) ? '' : ';expires=' + expireDate.toUTCString());
}
