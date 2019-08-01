<template>
  <div>
    <h1>Create Solution</h1>
    <Form ref="solutionInfo" :rules="rules" :model="solutionInfo" class="projectForm">
      <FormItem prop="name" label="Name" :label-width="150">
        <Input v-model="solutionInfo.name" placeholder="Enter Name ..." />
      </FormItem>

      <FormItem prop="description" label="Description" :label-width="150">
        <div>
          <Input type="textarea" v-model="solutionInfo.description"
            placeholder="Enter detailed introduction about this solution" />
        </div>
      </FormItem>

      <!-- <FormItem prop="startTime" label="Start Time" :label-width="150" :style="{display:'inline-block'}">
        <Input v-model="solutionInfo.startTime" placeholder="Enter Start Time ..." />
      </FormItem>

      <FormItem prop="endTime" label="End Time" :label-width="150" :style="{display:'inline-block'}">
        <Input v-model="solutionInfo.endTime" placeholder="Enter End Time ..." />
      </FormItem>

      <FormItem prop="scale" label="Scale" :label-width="150" :style="{display:'inline-block'}">
        <Input v-model="solutionInfo.scale" placeholder="Enter Scale ..." />
      </FormItem>

      <FormItem prop="spatialDimension" label="Spatial Dimension" :label-width="150">
        <Select v-model="solutionInfo.spatialDimension">
          <Option value="2">2-dimensional</Option>
          <Option value="3">3-dimensional</Option>
        </Select>
      </FormItem>

      <FormItem label="Resolution" :label-width="150">

      </FormItem> -->

      <FormItem label="Model" :label-width="150">
        <Upload :max-size="1024*1024" type="drag" :before-upload="getMd5" action="-">
          <div style="padding: 20px 0">
            <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
            <p>
              Click or drag file here to upload(The file size must control in
              <span style="color:red">1GB</span>)
            </p>
          </div>
        </Upload>
        <div style="padding:0 10px 0 10px" v-show="modelFile!=''">
          <div style="display:flex">
            File Name: <span style="font-size:10px;margin: 0 5px 0 5px"
              :title="modelFile.name">{{ modelFile.name }}</span>
            (Size:
            <span style="font-size:10px;margin-right:10px">{{getFileSizeStr}})</span>
            <Icon type="ios-close" size="20" @click="delFile()"
              style="display:flex;justify-content:flex-end;cursor:pointer" title="Cancel"></Icon>
          </div>
          <!-- <ul v-for="(list,index) in file" :key="index">
              <li style="display:flex">
                File name:
                <span
                  style="font-size:10px;margin: 0 5px 0 5px"
                  :title="list.name"
                >{{ list.name }}</span>
                (Size:
                <span
                  v-if="list.size<(1024*1024)"
                  style="font-size:10px;margin-right:10px"
                >{{(list.size/1024).toFixed(2)}}KB)</span>
                <span
                  v-else
                  style="font-size:10px;margin-right:10px"
                >{{(list.size/1024/1024).toFixed(2)}}MB)</span>
                <Icon
                  type="ios-close"
                  size="20"
                  @click="delFileList(index)"
                  style="display:flex;justify-content:flex-end;cursor:pointer"
                  title="Cancel"
                ></Icon>
              </li>
            </ul> -->
        </div>
      </FormItem>

      <FormItem>
        <div class="inline_style">
          <Button class="create" type="success" @click='createSolution("solution")'
            :disabled="!creatable">Create</Button>
        </div>
      </FormItem>

    </Form>
  </div>
</template>
<script>
import Util from "@/utils/comparison/cmpUtils";

export default {
  data() {
    return {
      solutionInfo: {
        name: "",
        description: ""
        // spatialDimension: 2,
        // scale: "",
        // spatialReference: "",
        // resolutionConstraint: {
        //   x: { value: "", unit: "" },
        //   y: { value: "", unit: "" },
        //   z: { value: "", unit: "" }
        // },
        // spatialExtents: {
        //   type: "",
        //   value: "",
        //   envelopeValue: []
        // },
        // timeStep: { value: "", unit: "" },
        // startTime: "",
        // endTime: "",
        // programmingLang: []
      },
      modelFile: "",
      fileMd5: "",
      computableModelOid: "",
      creatable: false,
      rules: {}
    };
  },
  methods: {
    createSolution(solution) {
      let formData = new FormData();
      formData.append("file",this.modelFile);
      formData.append("md5",fileMd5);
      this.$api.cmp_solution.deployModel(formData)
        .then(res=>{
          console.log(res);
        })
        .catch(err=>{
          this.$Message.error(err);
        })
    },
    getMd5(file) {
      this.creatable = false;
      this.computableModelOid = "";
      console.log("getMD5");
      this.fileMd5 = "";
      Util.getFileMD5(file, md5 => {
        console.log(md5);
        this.fileMd5 = md5;

        //* 后台查询是否已经上传过。
        this.$api.cmp_solution
          .matchMd5(md5)
          .then(res => {
            this.creatable = true;
            if (res == null) {
              console.log("第一次上传");
              this.modelFile = file;
              this.modelFile["fileSize"] =
                Math.round((this.modelFile.size / 1024) * 100) / 100;
            } else {
              console.log("该文件已存在：", res);
              computableModelOid = res;
            }
          })
          .catch(err => {
            this.$Message.error(err);
          });
      });
      return false;
    },
    delFile(){
      this.modelFile = "";
      this.creatable = false;
    }
  },
  computed: {
    getFileSizeStr() {
      let str = "";
      let tmp = this.modelFile.size / 1024;
      if (this.modelFile.size < Math.pow(1024, 2)) {
        str = tmp.toFixed(2) + "KB";
      } else {
        tmp = tmp/1024;
        str = tmp.toFixed(2) + "MB";
      }
      return str;
    }
  }
};
</script>
<style scoped>
h1 {
  margin-top: 2.5%;
  text-align: center;
}
.projectForm {
  width: 60%;
  margin: auto;
  margin-top: 20px;
}

.create {
  width: 20%;
  margin-right: 40%;
  margin-left: 40%;
}
</style>