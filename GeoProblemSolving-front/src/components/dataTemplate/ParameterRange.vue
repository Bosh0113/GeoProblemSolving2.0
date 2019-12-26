<template>
  <div>
    <el-row>
      <el-col :span="6">
        <el-input
          v-model="from"
          :disabled="disabled"
        ></el-input>
      </el-col>
      <el-col :span="3" :offset="2"> -</el-col>
      <el-col :span="6" >
        <el-input
          v-model="to"
          :disabled="disabled"
        ></el-input>
      </el-col>

    </el-row>

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
    from: {
      get() {
        return this.dataTemplate.value.split("-")[0];
      },
      set(newValue) {
        let toValue = this.dataTemplate.value.split("-")[1];
        this.dataTemplate.value = `${newValue}-${toValue}`;
        return newValue;
      }
    },
    to: {
      get() {
        return this.dataTemplate.value.split("-")[1];
      },
      set(newValue) {
        let fromValue = this.dataTemplate.value.split("-")[0];
        this.dataTemplate.value = `${fromValue}-${newValue}`;
        return newValue;
      }
    }
  },
  methods: {}
};
</script>

<style></style>
