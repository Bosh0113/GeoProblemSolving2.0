<!--  -->
<template>
  <div>
    <el-row style="margin:5px 0 ;line-height:40px">
      <div class="switch-label">
        <div v-if="switchValue">Private data</div>
        <div v-else>Public data</div>
      </div>
      <div class="switch-label">
        <el-switch
          v-model="switchValue"
          active-color="#13ce66"
          inactive-color="#409eff"
        >
        </el-switch>
      </div>
      <div style="float:right">
        <!-- <el-button @click="uploadNewData">Upload new data</el-button> -->
        <el-upload
          action
          :auto-upload="true"
          :show-file-list="false"
          ref="upload"
          :http-request="uploadNewData"
        >
          <el-button type="primary" size="mini" style="font-size: 14px"
            >Upload {{ switchValue ? "private" : "public" }} data</el-button
          >
        </el-upload>
      </div>
    </el-row>

    <el-table
      border
      style="width: 100%"
      max-height="400"
      highlight-current-row
      :data="switchValue ? privateData : publicData"
      @row-click="handleCurrentChange"
      @row-dblclick="handleCurrentChangeSubmit"
    >
      <el-table-column prop="name" label="Name" sortable width="200">
      </el-table-column>

      <el-table-column prop="description" label="Description" width="269">
      </el-table-column
      ><el-table-column prop="type" label="Type" width="80"> </el-table-column>
      <el-table-column prop="privacy" label="Privacy" width="80">
      </el-table-column>
      <el-table-column prop="uploaderName" label="Provider" width="150">
      </el-table-column>
      <el-table-column prop="uploadTime" label="Upload time" width="180">
      </el-table-column>
    </el-table>
    <div v-if="Object.keys(selectData).length === 0">
      <div class="data-name select-title" style="width:300px">
        You haven't selected any data!
      </div>
    </div>
    <div v-else style="line-height:40px">
      <div class="data-name select-title">
        Data you have selected:
      </div>
      <div class="select-data select-data-line">
        <div class="data-name">{{ selectData.name }}</div>
        <i class="el-icon-close" @click="remove(selectData)"></i>
      </div>
      <div style="float:right" class="select-data-line">
        <el-button @click="submit" size="small" type="warning" plain
          >OK</el-button
        >
      </div>
    </div>
    <div style="clear:both"></div>
  </div>
</template>

<script>
import { get, del, post, put, patch } from "@/axios";
export default {
  props: ["pageParams"],
  components: {},

  watch: {
    pageParams: {
      async handler(val) {
        // console.log(val);
      },
      deep: true
    }
    // switchValue: {
    //   handler(val) {
    //     console.log(val);
    //     // this.selectData = {};
    //     // this.handleCurrentChange("");
    //     if (val) {
    //       this.tableData = this.privateData;
    //     } else {
    //       this.tableData = this.publicData;
    //     }
    //   },
    //   deep: true
    // }
  },

  computed: {},

  data() {
    return {
      switchValue: true,
      privateData: [],
      publicData: [],
      selectData: {},
      // tableData: []
      fileList: [], //el-upload上传的文件列表,
      file: {}
    };
  },

  methods: {
    async init() {
      await this.getPrivateResources();
      await this.getPublicResources();
    },
    async getPrivateResources() {
      let data = await get(
        `/GeoProblemSolving/resource/${this.pageParams.userId}?type=data`
      );
      // data.forEach(e => (e.isSelect = "false"));
      this.privateData = data;
    },
    async getPublicResources() {
      let data = await get(`/GeoProblemSolving/resource?privacy=public`);
      this.publicData = data;
    },
    handleCurrentChange(val) {
      this.selectData = val;
    },
    handleCurrentChangeSubmit(val) {
      this.selectData = val;
      this.submit();
    },
    remove() {
      this.selectData = {};
    },

    async uploadNewData({ file }) {
      let formData = new FormData();
      formData.append("file", file);
      formData.append("type", "data");
      formData.append("uploaderId", this.pageParams.userId);

      let flag = this.switchValue ? "private" : "public";
      // console.log(flag);

      formData.append("privacy", flag);

      let { data } = await this.axios.post(
        `/GeoProblemSolving/resource/upload`,
        formData
      );
      if (flag == "private") {
        await this.getPrivateResources();
      } else {
        await this.getPublicResources();
      }
    },

    submit() {
      this.$emit("selectData", this.selectData);
    }
  },

  mounted() {
    this.init();
  }
};
</script>
<style lang="scss" scoped>
.switch-label {
  width: 100px;
  float: left;
  font-weight: 600;
  font-size: 16px;
}
.data-name {
  float: left;
  width: 200px;
  font-weight: 600;
  font-size: 16px;
  // padding-left: 5px;
}
.select-data-line {
  margin: 15px 0 0 0;
  line-height: 30px;
  .el-button {
    width: 138px;
  }
}
.select-data {
  background-color: #f0f9eb;
  display: inline-block;
  height: 32px;
  padding: 0 10px;

  font-size: 12px;
  color: #67c23a;
  border: 1px solid #e1f3d8;
  border-radius: 4px;
  box-sizing: border-box;
  white-space: nowrap;

  //   background-color: #f0f9eb;
  // border-color: #e1f3d8;
  // color: #67c23a;
}
.select-data:hover {
  // width: 250px;
  cursor: pointer;
  transition: all 0.2s ease-out;
  color: #60a83c;
  background-color: #c8f3d3;
}
.select-title {
  width: 200px;
  float: left;
  line-height: 30px;
  margin: 18px 0 0 0;
}
</style>
