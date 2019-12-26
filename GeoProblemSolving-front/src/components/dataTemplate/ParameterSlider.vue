<template>
  <div>
    <el-slider
      :disabled="disabled"
      v-model="dataTemplate.value"
      :step="new Number(dataTemplate.step)"
      :max="new Number(dataTemplate.max)"
      :min="new Number(dataTemplate.min)"
    ></el-slider>
  </div>
</template>
<script>
export default {
  props: {
    disabled: {
      type: Boolean,
      default: false
    },
    initDataTemplate: {
      type: Object
    }
  },
  data() {
    return {
      dataTemplate: this.initDataTemplate
    };
  },
  methods: {}
};
</script>

<style></style>
