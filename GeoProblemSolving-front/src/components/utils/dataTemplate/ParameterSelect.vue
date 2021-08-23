<template>
  <div>
    <el-select v-model="dataTemplate.value" :disabled="disabled">
      <el-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      >
      </el-option>
    </el-select>
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
    options() {
      let options = [];
      Object.entries(this.dataTemplate.keyValue).forEach(element => {
        options.push({
          label: element[1],
          value: element[0]
        });
      });
      return options;
    }
  }
};
</script>

<style></style>
