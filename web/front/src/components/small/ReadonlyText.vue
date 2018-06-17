<template>
  <div class="form-group">
    <label class="label-text" for="label-text1">{{ msg }}</label>
    <div><input type="text" class="input-text" id="label-text1" readonly :value="display_value"></div>
  </div>
</template>

<script>
export default {
  name: 'ReadonlyText',
  data () {
    return { display_value: '' }
  },
  props: {
    msg: { type: String, default: '' },
    input_value: { type: String, default: 'xxx' }
  },
  methods: {
    getNewValue (val) {
      this.display_value = val
    }
  }
}
</script>

<style scoped>
.label-text {
  font-weight: normal;
  font-family:"Microsoft YaHei", "Microsoft JhengHei Light", Helvetica, sans-serif;
}
.input-text {
  color: #939192;
  background: #f5f5f5!important;
  border: 1px solid #d5d5d5;
  margin-left: 0; /* away from label */
  margin-bottom: 20px;
  padding: 5px 4px 6px;
  font-size: 14px;
  width: 156px;
}
.input-text:hover {
  border: 1px solid #aaaaaa;
}
</style>
