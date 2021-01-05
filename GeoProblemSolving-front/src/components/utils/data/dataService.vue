<template>
  <div class="main">
    <!-- <el-button @click="getAllRecords">getAllRecords</el-button>
    <el-button @click="loadingClose">close</el-button>-->
    <el-row class="title">
      <el-col>{{ dataService.name }}</el-col>
    </el-row>

    <el-row>
      <el-col :span="6">
        <p class="des">{{ dataService.description }}</p>
      </el-col>
      <!-- <el-col :span="1" :offset="11"> -->
      <el-button
        type="primary"
        @click="invokeTest"
        style="float:right;width:110px;margin-right:15%"
      >
        <i class="el-icon-setting"></i>&nbsp;Invoke
      </el-button>
      <el-button
        type="warning"
        @click="bindAll"
        v-if="record.status == 2"
        style="float:right;width:160px;margin-right:20px"
      >
        <i class="el-icon-paperclip"></i>&nbsp;Bind all outputs
      </el-button>
    </el-row>
    <el-divider></el-divider>
  </div>
</template>

<script>
import { get } from "@/axios";

export default {
  data() {
    return {
      id: "bb665d6a-5643-4608-92e5-9245039d8d04",
      dataService: {},
      method: {},
      record: {}
    };
  },

  methods: {
    async getData() {
      let data = await get(
        `/GeoProblemSolving/dataContainer/dataService/${this.id}/Processing`
      );
      this.dataService = data;

      console.log(data);
    },

    invokeTest() {}
  },
  created() {
    this.getData();
  }
};
</script>

<style>
.main {
  position: relative;
}
.title {
  position: relative;
  font-size: 18px;
  padding: 0px 0px 30px 0px;
}
.title i {
  font-size: 30px !important;
}

.el-button {
  padding: 12px;
}
.params-group {
  position: relative;
  padding-bottom: 30px;
}
.params-group > .title {
  font-style: italic;
  font-size: 16px;
  padding-bottom: 10px;
  border-bottom: solid 2px #999;
}
.params-group > .items {
  padding: 10px 0px 6px 50px;
}
.params-group > .items > .item {
  padding: 20px 10px 20px 0px;
  border-bottom: dotted 1px #999999;
  line-height: 2;
}
.invoke-btn {
  margin-top: 20px;
}
.state-desc {
  margin: 0px 0px 35px 0px;
  padding: 4px 0px;
  line-height: 2;
  background-color: #f3f3f3;
  font-size: 16px;
  font-style: italic;
}
</style>
