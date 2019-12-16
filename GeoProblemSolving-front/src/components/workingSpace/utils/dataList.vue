<style scoped>
.table table {
  table-layout: auto;
  width: 100% !important;
}
</style>
<template>
  <div>
    <div style="width:3px;height:18px;float:left;background-color:rgb(124, 126, 126)"></div>
    <h4 style="float:left;margin-left:5px">Data</h4>
    <div
      style="float:right;cursor:pointer"
      @click="dataUploadModal = !dataUploadModal"
      title="Upload data"
    >
      <Icon type="md-cloud-upload" />
    </div>
    <br />
    <div style="margin-top:10px" :style="{height:contentHeight-66+'px'}">
      <vue-scroll :ops="ops">
        <Table
          style="overflow:auto"
          :columns="tableColName"
          :data="this.stepDataList"
          class="table"
          v-show="this.stepDataList!=[] && this.stepDataList!='None'"
        >
          <template slot-scope="{ row }" slot="name">
            <strong>{{ row.name }}</strong>
          </template>
          <template slot-scope="{ row, index }" slot="action">
            <Button
              type="primary"
              size="small"
              style="margin-right: 5px"
              title="Check"
              @click="checkData(index)"
            >
              <Icon type="md-eye" />
            </Button>
          </template>
        </Table>
      </vue-scroll>
    </div>
    <Modal v-model="checkDataModal" title="Data Information" width="600">
      <Tabs>
        <TabPane label="Metadata" name="metadata" icon="md-home">
          <div style="width:100%;position:absolute;right:5%">
            <template v-if="userRole == 'Manager'">
              <Icon
                v-if="!metaDataEdit"
                type="ios-create"
                :size="25"
                style="float:right;cursor:pointer"
                title="Edit"
                @click="editMetadata"
              />
              <Icon
                v-else
                type="md-checkbox-outline"
                :size="25"
                style="float:right;cursor:pointer"
                title="Complete"
                @click="editMetadata"
              />
            </template>
          </div>
          <div
            v-if="!metaDataEdit"
            style="word-break: break-all; word-wrap: break-word;width:85%"
          >{{metaDataInfo}}</div>
          <div v-else>
            <Input
              v-model="metaDataInfo"
              type="textarea"
              :rows="5"
              :autosize="{minRows: 5,maxRows: 18}"
              placeholder="Enter something..."
              style="width:85%"
            />
          </div>
        </TabPane>
        <TabPane label="UDX Schema" name="udx" icon="md-browsers">
          <pre class="brush: html"></pre>
          <template>...</template>
        </TabPane>
      </Tabs>
      <br/>
      <Button style="margin-right:20px" @click="dataPreview">Preview</Button><Button style="margin-right:20px" @click="dataVisualize">Visualization</Button>
      <br/>
    </Modal>
    <Modal v-model="dataUploadModal" title="Upload data" width="600">
      <Form
        ref="uploadData"
        :model="uploadData"
        :rules="uploadDataRule"
        :label-width="100"
        label-position="left"
      >
        <FormItem label="Privacy" prop="privacy">
          <RadioGroup v-model="uploadData.privacy" style="width:80%">
            <Radio label="private">Private</Radio>
            <Radio label="public">Public</Radio>
          </RadioGroup>
        </FormItem>

        <FormItem label="Description" prop="description">
          <Input type="textarea" :rows="4" v-model="uploadData.description" />
        </FormItem>
      </Form>
      <Upload :max-size="1024*1024" multiple type="drag" :before-upload="gatherFile" action="-">
        <div style="padding: 20px 0">
          <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
          <p>
            Click or drag files here to upload (The file size must control in
            <span
              style="color:red"
            >1GB</span>)
          </p>
        </div>
      </Upload>
      <div style="padding:0 10px 0 10px;max-height:200px;overflow-y:auto">
        <ul v-for="(list,index) in toUploadFiles" :key="index">
          <li style="display:flex">
            File name:
            <span
              style="font-size:10px;margin: 0 5px 0 5px"
            >{{ list.name }} ( {{list.fileSize}} )</span>
            <Icon
              type="ios-close"
              size="20"
              @click="delFileList(index)"
              style="display:flex;justify-content:flex-end;cursor:pointer"
            ></Icon>
          </li>
        </ul>
      </div>
      <div slot="footer">
        <Button @click="dataUploadModal=false">Cancel</Button>
        <Button type="success" @click="folderUpload('uploadData')">Upload</Button>
      </div>
    </Modal>
    <Modal
      v-model="progressModalShow"
      title="Upload Progress"
      :mask-closable="false"
      :closable="false"
    >
      <Progress :percent="uploadProgress"></Progress>
      <div slot="footer"></div>
    </Modal>
  </div>
</template>
<script>
export default {
  data() {
    return {
      ops: {
        bar: {
          background: "#808695"
        }
      },
      userInfo: this.$store.getters.userInfo,
      dataUploadModal: false,
      uploadData: {
        privacy: "private",
        type: "data",
        description: ""
      },
      uploadDataRule: {
        privacy: [
          {
            required: true,
            message: "file privacy cannot be empty",
            trigger: "blur"
          }
        ],
        description: [
          {
            required: true,
            message: "file description cannot be empty",
            trigger: "blur"
          }
        ]
      },
      toUploadFiles: [],
      fileCountTimer: null,
      progressModalShow: false,
      uploadProgress: 0,
      fileList: [],
      stepDataList: [],
      checkDataModal: false,
      tableColName: [
        {
          title: "Name",
          key: "name",
          sortable: true,
          tooltip: true
        },
        {
          title: "Action",
          slot: "action",
          align: "center",
          width: 90
        }
      ],
      // 编辑data描述信息
      metaDataEdit: false,
      // 元数据信息，待完善
      metaDataInfo:""
    };
  },
  props: ["stepInfo", "contentHeight", "userRole"],
  created() {},
  mounted() {
    this.getDataList();
    $(".__view").css("width", "inherit");
  },
  methods: {
    getDataList() {
      let list = [];
      if (this.stepInfo.stepId != "" && this.stepInfo.stepId != undefined) {
        $.ajax({
          url:
            "/GeoProblemSolving/folder/inquiry" +
            "?folderId=" +
            this.stepInfo.stepId,
          type: "GET",
          async: false,
          success: data => {
            if (data !== "None") {
              this.$set(this, "fileList", data.files);
            } else {
              this.fileList = [];
            }
            // this.$set(this, "stepResourceList", list);
            this.filterData();
          }
        });
      }
    },
    filterData() {
      let filterdata = this.fileList.filter(item => {
        return item.type === "data";
      });
      this.$set(this, "stepDataList", filterdata);
    },
    gatherFile(file) {
      if (this.toUploadFiles.length >= 5) {
        if (this.fileCountTimer != null) {
          clearTimeout(this.fileCountTimer);
        }
        this.fileCountTimer = setTimeout(() => {
          this.$Message.info("最多只能上传5个文件");
        }, 500);
      } else {
        var fileSize = file.size;
        if (fileSize < 1024) {
          file.fileSize = fileSize + "b";
        } else if (fileSize < 1024 * 1024) {
          file.fileSize = Math.round((fileSize / 1024) * 100) / 100 + "Kb";
        } else {
          file.fileSize =
            Math.round((fileSize / (1024 * 1024)) * 100) / 100 + "Mb";
        }
        this.toUploadFiles.push(file);
      }
      return false;
    },
    delFileList(index) {
      this.toUploadFiles.splice(index, 1);
    },
    folderUpload(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          var uploadFiles = this.toUploadFiles;
          if (uploadFiles.length > 0) {
            this.dataUploadModal = false;
            var formData = new FormData();
            for (var i = 0; i < uploadFiles.length; i++) {
              formData.append("file", uploadFiles[i]);
            }
            formData.append("description", this.uploadData.description);
            formData.append("type", this.uploadData.type);
            formData.append("uploaderId", this.userInfo.userId);
            formData.append("privacy", this.uploadData.privacy);
            formData.append("folderId", this.stepInfo.stepId);

            this.progressModalShow = true;

            if (
              this.stepInfo.stepId != "" ||
              this.stepInfo.stepId != undefined
            ) {
              this.axios({
                url: "/GeoProblemSolving/folder/uploadToFolder",
                method: "post",
                onUploadProgress: progressEvent => {
                  this.uploadProgress =
                    ((progressEvent.loaded / progressEvent.total) * 100) | 0;
                },
                data: formData
              })
                .then(res => {
                  if (res.data != "Fail") {
                    var uploadedList = res.data.uploaded;
                    var failedList = res.data.failed;
                    var sizeOverList = res.data.sizeOver;
                    for (var i = 0; i < uploadedList.length; i++) {
                      this.stepDataList.push(uploadedList[i]);
                    }
                    if (sizeOverList.length > 0) {
                      this.$Notice.warning({
                        title: "Files too large.",
                        render: h => {
                          return h("span", sizeOverList.join(";"));
                        }
                      });
                    }
                    if (failedList.length > 0) {
                      this.$Notice.error({
                        title: "Upload fail.",
                        render: h => {
                          return h("span", failedList.join(";"));
                        }
                      });
                    }
                    // 初始化上传数据列表
                    this.toUploadFiles = [];
                  } else {
                    this.$Message.warning("Upload fail.");
                  }
                  this.progressModalShow = false;
                  this.uploadProgress = 0;
                })
                .catch(err => {
                  this.progressModalShow = false;
                  this.$Message.warning("Upload fail.");
                  this.uploadProgress = 0;
                });
            } else {
              this.$Notice.info({
                desc: "upload data failed!"
              });
            }
          } else {
            this.$Message.warning("Upload data is null.");
          }
        }
      });
    },
    checkData() {
      this.checkDataModal = true;
    },
    editMetadata() {
        if (this.metaDataEdit) {
          this.metaDataEdit = false;
      //     let obj = new URLSearchParams();
      //     obj.append("stepId", this.stepId);
      //     obj.append("content.metaDataInfo", this.metaDataInfo);
      //     this.axios
      //       .post("/GeoProblemSolving/step/update", obj)
      //       .then(res => {
      //         if (res.data == "Offline") {
      //           this.$store.commit("userLogout");
      //           this.$router.push({ name: "Login" });
      //         } else if (res.data != "Fail") {
      //           this.$Notice.info({
      //             desc: "Update successfully!"
      //           });
      //         } else {
      //           this.$Message.error("Update step failed.");
      //         }
      //       })
      //       .catch(err => {
      //         console.log(err.data);
      //       });
        } else {
          this.metaDataEdit = true;
        }
    },
    dataPreview(){},
    dataVisualize(){}
  }
};
</script>