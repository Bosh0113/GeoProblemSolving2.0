<template>
  <div>
    <el-input
      :disabled="disabled"
      type="textarea"
      :rows="3"
      :placeholder="columnsFeilds"
      v-model="dataTemplate.value"
    >
    </el-input>
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
  computed: {
    columnsFeilds() {
      let str = "columns must be like '";
      this.dataTemplate.fields.forEach(element => {
        str += `${element},`;
      });
      str += "'";
      return str;
    }
  }
};
</script>

<style></style>
