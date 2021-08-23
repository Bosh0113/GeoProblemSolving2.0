<template>
  <div>
    <el-input v-model="dataTemplate.value" :disabled="disabled"></el-input>
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
