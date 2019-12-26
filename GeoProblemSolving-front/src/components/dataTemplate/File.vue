<template>
  <div>
    <!-- <el-button v-show="disabled" plain type="warning" icon="el-icon-download" @click="download()"></el-button> -->
     <!-- <el-button @click="test()"></el-button> -->

    <el-upload
      action="/GeoProblemSolving/dataItem/addDataItem"
      :limit="1"
      :on-success="onSuccess"
      
    >
      <el-button size="small" type="primary" :disabled="disabled">Submit</el-button>
    </el-upload>
  </div>
</template>
<script>
export default {
  props: {
    disabled: {
      type: Boolean,
      default: false
    },
    ioflag: {
      type: String
    },
    initDataTemplate: {
      type: Object  //Object&&数组可双向绑定
    }
  },
  data() {
    return {
      dataTemplate: this.initDataTemplate,
      response: {},
      dis:this.disabled
    };
  },
  methods: {
    onSuccess( data ) {
      this.dataTemplate.value = data.id;
      this.dis = true;
      console.log(this.dis);
      // this.$emit("inputFile",true);
      // this.dis = true
      // this.$set( this.dataTemplate,"value",data.id);
      // this.$set( this.dis,true);
    },
    download() {
      let url = `/GeoProblemSolving/dataItem/download/${this.dataTemplate.value}`;
      window.open(url);
    },

  
  }
};
</script>

<style></style>
