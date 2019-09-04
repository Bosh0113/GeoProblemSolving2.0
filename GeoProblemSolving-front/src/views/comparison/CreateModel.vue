<template>
  <div>
    <h1>Create Model</h1>
    <Form ref="modelInfo" :rules="rules" :model="modelInfo" class="projectForm">
      <FormItem prop="modelName" label="Name" :label-width="150">
        <Input v-model="modelInfo.modelName" placeholder="Enter Model Name ..." />
      </FormItem>

      <FormItem prop="description" label="Description" :label-width="150">
        <div>
          <Input type="textarea" v-model="modelInfo.description"
            placeholder="Enter detailed introduction about this model" />
        </div>
      </FormItem>

      <!-- <FormItem prop="startTime" label="Start Time" :label-width="150" :style="{display:'inline-block'}">
        <Input v-model="modelInfo.startTime" placeholder="Enter Start Time ..." />
      </FormItem>

      <FormItem prop="endTime" label="End Time" :label-width="150" :style="{display:'inline-block'}">
        <Input v-model="modelInfo.endTime" placeholder="Enter End Time ..." />
      </FormItem>

      <FormItem prop="scale" label="Scale" :label-width="150" :style="{display:'inline-block'}">
        <Input v-model="modelInfo.scale" placeholder="Enter Scale ..." />
      </FormItem>

      <FormItem prop="spatialDimension" label="Spatial Dimension" :label-width="150">
        <Select v-model="modelInfo.spatialDimension">
          <Option value="2">2-dimensional</Option>
          <Option value="3">3-dimensional</Option>
        </Select>
      </FormItem>

      <FormItem label="Resolution" :label-width="150">

      </FormItem> -->

      <FormItem label="Model" :label-width="150">
        <Upload :max-size="1024*1024" :before-upload="beforeUpload" :data="deployRequestInfo"
          accept="application/zip,application/x-zip,application/x-zip-compressed"
          action="/GeoProblemSolving/cmp_model/deployModel" :disabled="creatable" :on-remove="removeFile"
          :on-success="uploadSuccess" :on-error="uploadError">
          <Button icon="ios-cloud-upload-outline">Upload Model</Button>
        </Upload>
        <div>
          <span v-show="creatable" size="52" style="color: #3399ff">Model has been uploaded.</span>
        </div>
      </FormItem>

      <FormItem>
        <div class="inline_style">
          <Button class="create" type="success" @click='createModel("modelInfo")' :disabled="!creatable">Create</Button>
        </div>
      </FormItem>

    </Form>
  </div>
</template>
<script>
import Util from "@/utils/comparison/cmpUtils";
import base from "@/request/api/base";
export default {
  created() {
    this.baseUrl = base.cmpSolution;
    let projectId = this.$route.params.id;
    this.modelInfo.projectId = projectId;
  },
  data() {
    return {
      modelInfo: {
        ownerId: this.$store.getters.userId,
        ownerName: this.$store.getters.userName,
        modelName: "",
        description: "",
        privacy: "Public",
        computableModels:[],
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
      rules: {
        name: [
          {
            required: true,
            message: "The name cannot be empty and no more than 100 characters",
            trigger: "blur",
            max: 100
          }
        ],
        description: [
          {
            required: true,
            message:
              "The description cannot be empty and no more than 600 characters",
            trigger: "blur",
            max: 600
          }
        ]
      },
      deployRequestInfo: {
        md5: "",
        ownerId: this.$store.getters.userId,
        ownerName: this.$store.getters.userName
      },
      baseUrl: ""
    };
  },
  methods: {
    createModel(model) {
      //todo 进度条
      this.$refs[model].validate(valid => {
        if (valid) {
          //todo cmp_model api
          this.$api.cmp_model
            .create(this.modelInfo)
            .then(res=>{
              this.$router.back(-1);
              this.$Message.success("success");
              // console.log(res);
            })
            .catch(err=>{
              this.$Message.error(err);
            });
          // this.$api.cmp_solution
          //   .create(model)
          //   .then(res => {
          //     console.log(res);
          //   })
          //   .catch(err => {
          //     this.$Message.error(err);
          //   });
        }
      });
    },
    beforeUpload(file) {
      this.creatable = false;
      this.computableModelOid = "";
      console.log("getMD5");
      this.fileMd5 = "";
      this.deployRequestInfo.md5 = "";

      return new Promise((resolve, reject) => {
        //* 获取 MD5 值
        Util.getFileMD5(file, md5 => {
          console.log(md5);
          this.fileMd5 = md5;
          this.deployRequestInfo.md5 = md5;
          //* 后台查询是否已经上传过。
          this.$api.cmp_model
            .matchMd5(md5)
            .then(res => {
              if (res == null) {
                console.log("第一次上传");
                this.modelFile = file;
                this.modelFile["fileSize"] =
                  Math.round((this.modelFile.size / 1024) * 100) / 100;
                resolve();
              } else {
                console.log("该文件已存在：", res);
                this.computableModelOid = res;
                this.modelInfo.computableModels.push(res);
                this.creatable = true;
                reject();
              }
            })
            .catch(err => {
              this.$Message.error(err);
              reject();
            });
        });
      });
    },
    delFile() {
      this.modelFile = "";
      this.creatable = false;
    },
    removeFile() {
      console.log("delete");
      this.creatable = false;
    },
    uploadSuccess(response) {
      // console.log(response);
      if(response.code==0){
        this.creatable = true;
        this.modelInfo.computableModels.push(response.data.oid);
      }else{
        this.$Message.error(response.data.msg);
      }
    },
    uploadError() {
      this.$Message.error("upload model failed!");
    }
  },
  computed: {
    getFileSizeStr() {
      let str = "";
      let tmp = this.modelFile.size / 1024;
      if (this.modelFile.size < Math.pow(1024, 2)) {
        str = tmp.toFixed(2) + "KB";
      } else {
        tmp = tmp / 1024;
        str = tmp.toFixed(2) + "MB";
      }
      return str;
    },
    getDeployModelUrl() {
      return `${this.baseUrl}/deployModel`;
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