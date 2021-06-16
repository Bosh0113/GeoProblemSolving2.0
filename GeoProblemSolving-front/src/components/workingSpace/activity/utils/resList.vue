<template>
  <div>
    <Card class="resCard" dis-hover>
      <div slot="title">
        <Button
          v-if="folderIdStack.length >= 1"
          icon="md-arrow-round-back"
          shape="circle"
          size="small"
          @click="backforeFolder"
          title="Back to the parent folder"
          style="cursor: pointer"
        />
        <Button
          v-else
          icon="md-arrow-round-back"
          shape="circle"
          size="small"
          title="Back to the parent folder"
          style="color: lightgray; cursor: default"
        />
        <Label style="margin-left: 20px">Select:</Label>
        <Select
          v-model="resouceType"
          size="small"
          @on-change="changeResType"
          style="width: 150px; margin: 5px"
        >
          <Option value="all">All resources</Option>
          <Option value="data">Data</Option>
          <Option value="others">Other resources</Option>
          <!-- <Option value="toolData">Results</Option> -->
        </Select>
        <Button
          v-if="userRole != 'Visitor'"
          shape="circle"
          size="small"
          icon="md-cloud-outline"
          @click="getPersonalRes()"
          style="margin-right: 10px"
          title="Get resources from your personal space"
        ></Button>
        <Button
          v-if="userRole != 'Visitor'"
          shape="circle"
          size="small"
          icon="md-shuffle"
          @click="getPreviousRes()"
          style="margin-right: 10px"
          title="Get resources from the previous activities"
        ></Button>
        <Button
          v-if="userRole != 'Visitor'"
          shape="circle"
          size="small"
          icon="md-cloud-upload"
          @click="dataUploadModalShow"
          title="Upload resources"
        ></Button>
      </div>
      <div style="display: flex; justify-content: space-between">
        <div style="width: 100%">
          <Card class="res-content" v-if="fileList.length == 0">
            <div>There is no resource.</div>
          </Card>
          <vue-scroll :ops="ops" style="max-height: calc(100vh - 245px)" v-else>
            <Card class="res-content"
              ><Icon
                type="ios-add"
                size="80"
                title="Create a new file folder"
                @click="addFolderModalShow"
            /></Card>
            <div v-for="(item, index) in fileList" :key="index">
              <Card class="res-content">
                <div
                  class="res-content-image"
                  v-if="item.folder"
                  @click="enterFolder(item)"
                >
                  <img
                    :src="folderUrl"
                    height="42px"
                    width="42px"
                    title="Folder"
                  />
                </div>
                <div class="res-content-image" @click="checkData(item)" v-else>
                  <template
                    v-if="item.thumbnail == '' || item.thumbnail == undefined"
                  >
                    <img
                      :src="getImageUrl(item.type)"
                      height="42px"
                      width="42px"
                      :title="item.type"
                    />
                  </template>
                  <template v-else>
                    <img :src="item.thumbnail" height="42px" width="42px" />
                  </template>
                </div>
                <div>
                  <div
                    class="toolDataText"
                    :title="item.name"
                    v-if="item.folder"
                  >
                    {{ item.name }}
                  </div>
                  <div
                    class="toolDataText"
                    :title="item.name + item.suffix"
                    v-else
                  >
                    {{ item.name + item.suffix }}
                  </div>
                </div>
              </Card>
            </div>
          </vue-scroll>
        </div>
      </div>
    </Card>
    <Modal
      v-model="renameFolderModal"
      title="Rename folder"
      ok-text="Assure"
      cancel-text="Cancel"
    >
      <Form
        ref="renameValidate"
        :model="renameValidate"
        :rules="renameRuleValidate"
        :label-width="80"
      >
        <FormItem label="New name" prop="newName">
          <Input
            v-model="renameValidate.newName"
            :rows="4"
            placeholder="Enter the name for folder..."
          />
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="renameFolderModal = false">Cancel</Button>
        <Button type="success" @click="renameFolder('renameValidate')"
          >Rename
        </Button>
      </div>
    </Modal>
    <Modal v-model="newFolderModal" title="New folder">
      <Form
        ref="newValidate"
        :model="newValidate"
        :rules="newRuleValidate"
        :label-width="80"
        @submit.native.prevent
      >
        <FormItem label="Set name" prop="setName">
          <Input
            v-model="newValidate.setName"
            :rows="4"
            placeholder="Enter the name for folder..."
          />
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="newFolderModal = false">Cancel</Button>
        <Button type="success" @click="addFolder('newValidate')">New</Button>
      </div>
    </Modal>
    <Modal
      v-model="checkDataModal"
      title="Data information"
      width="600"
      footer-hide
    >
      <!-- <Tabs>
        <TabPane label="Information" name="metadata" icon="md-home"> -->
      <div style>
        <div class="dataInfo">
          <Label class="dataLabel">Name:</Label>
          <span class="dataText">{{
            selectData.name + selectData.suffix
          }}</span>
        </div>
        <div class="dataInfo">
          <Label class="dataLabel">Type:</Label>
          <span class="dataContent">{{ selectData.type }}</span>
          <Label class="dataLabel">Provider:</Label>
          <span class="dataContent">{{ selectData.uploaderName }}</span>
        </div>
        <div class="dataInfo">
          <Label class="dataLabel">File size:</Label>
          <span class="dataContent">{{ selectData.fileSize }}</span>
          <Label class="dataLabel">Created time:</Label>
          <span class="dataContent">{{ dateFormat(selectData.uploadTime) }}</span>
        </div>
        <div class="dataInfo">
          <Label class="dataLabel">Description:</Label>
          <span class="dataText">{{ selectData.description }}</span>
        </div>
      </div>
      <!-- </TabPane>
        <TabPane label="UDX Schema" name="udx" icon="md-browsers">
          <pre class="brush: html"></pre>
          <template>...</template>
        </TabPane>
      </Tabs> -->
      <br />
      <div>
        <a
          :href="selectData.address"
          :download="selectData.name + selectData.suffix"
        >
          <Button
            v-if="
              permissionIdentity(
                activityInfo.permission,
                userRole,
                'use_resource'
              )
            "
            type="info"
            size="small"
            title="Download"
            icon="md-download"
            style="margin: 10px 20px 0 0; cursor: pointer; width: 60px"
          ></Button>
        </a>
        <Button
          v-if="
            permissionIdentity(
              activityInfo.permission,
              userRole,
              'manage_resource'
            ) ||
            (permissionIdentity(
              activityInfo.permission,
              userRole,
              'upload_resource'
            ) &&
              selectData.uploaderId == userInfo.userId)
          "
          size="small"
          type="warning"
          title="Delete"
          icon="md-close"
          style="margin: 10px 20px 0 0; cursor: pointer; width: 60px"
          @click="deleteResourceModalShow(selectData.uid)"
        ></Button>
      </div>
      <!-- <Button style="margin-right:20px" @click="dataPreview(selectData)">Preview</Button>
      <Button style="margin-right:20px" @click="dataVisualize">Visualization</Button>-->
    </Modal>
    <Modal
      width="800px"
      v-model="inheritResModal"
      title="Get resources from previous activities"
      :styles="{ top: '20px' }"
      @on-ok="saveResources()"
      ok-text="Save"
      cancel-text="Cancel"
    >
      <div style="margin-left: 75px">
        <div style="font-size: 14px">Select the needed data:</div>
        <Transfer
          :data="existingResources"
          :target-keys="targetKeys"
          :list-style="listStyle"
          :render-format="resourceRender"
          :titles="['The previous activities', 'The new activity']"
          filter-placeholder="Enter key words..."
          filterable
          :filter-method="filterMethod"
          @on-change="handleChange"
        ></Transfer>
      </div>
    </Modal>
    <Modal v-model="dataUploadModal" title="Upload data" width="600">
      <Form
        ref="uploadDataInfo"
        :model="uploadDataInfo"
        :rules="uploadDataRule"
        :label-width="100"
        label-position="left"
      >
        <FormItem label="Privacy" prop="privacy">
          <RadioGroup v-model="uploadDataInfo.privacy" style="width: 80%">
            <Radio label="private">Private</Radio>
            <Radio label="public">Public</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="Type" prop="type">
          <RadioGroup v-model="uploadDataInfo.type">
            <Radio label="data"></Radio>
            <Radio label="paper"></Radio>
            <Radio label="document"></Radio>
            <Radio label="model"></Radio>
            <Radio label="image"></Radio>
            <Radio label="video"></Radio>
            <Radio label="others"></Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="Description" prop="description">
          <Input
            type="textarea"
            :rows="4"
            v-model="uploadDataInfo.description"
          />
        </FormItem>
      </Form>
      <Upload
        :max-size="1024 * 1024"
        multiple
        type="drag"
        :before-upload="gatherFile"
        action="-"
      >
        <div style="padding: 20px 0">
          <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
          <p>
            Click or drag files here to upload (The file size must control in
            <span style="color: red">1GB</span>)
          </p>
        </div>
      </Upload>
      <div style="padding: 0 10px; max-height: 200px; overflow-y: auto">
        <ul v-for="(list, index) in toUploadFiles" :key="index">
          <li style="display: flex">
            File name:
            <span style="font-size: 10px; margin: 0 5px 0 5px"
              >{{ list.name + list.suffix }} ( {{ list.fileSize }} )</span
            >
            <Icon
              type="ios-close"
              size="20"
              @click="delFileList(index)"
              style="display: flex; justify-content: flex-end; cursor: pointer"
            ></Icon>
          </li>
        </ul>
      </div>
      <div slot="footer">
        <Button @click="dataUploadModal = false">Cancel</Button>
        <Button type="success" @click="folderUpload('uploadDataInfo')"
          >Upload</Button
        >
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
    <Modal
      v-model="deleteResourceModal"
      @on-ok="deleteResource()"
      ok-text="Delete"
      cancel-text="Cancel"
    >
      <h3>Do you really want to delete this resource?</h3>
    </Modal>
  </div>
</template>
<script>
import Avatar from "vue-avatar";
export default {
  props: ["activityInfo"],
  components: {
    Avatar,
  },
  data() {
    return {
      ops: {
        bar: {
          background: "#808695",
        },
      },
      userInfo: this.$store.getters.userInfo,
      userRole: this.roleIdentity(this.activityInfo),
      resouceType: "all",
      // 文件夹
      newFolderModal: false,
      newValidate: {
        setName: "",
      },
      newRuleValidate: {
        setName: [
          {
            required: true,
            message: "The name can't be null.",
            trigger: "blur",
          },
        ],
      },
      renameForeInfo: {},
      renameFolderModal: false,
      renameValidate: {
        newName: "",
      },
      renameRuleValidate: {
        newName: [
          {
            required: true,
            message: "The name can't be null.",
            trigger: "blur",
          },
        ],
      },
      //资源继承
      preActivities: [],
      existingResources: [],
      targetKeys: [],
      inheritResModal: false,
      listStyle: { width: "280px", height: "375px" },
      // 上传资源
      dataUploadModal: false,
      uploadDataInfo: {
        privacy: "private",
        type: "data",
        description: "",
      },
      uploadDataRule: {
        privacy: [
          {
            required: true,
            message: "file privacy cannot be empty",
            trigger: "blur",
          },
        ],
        type: [
          {
            required: true,
            message: "file type cannot be empty",
            trigger: "blur",
          },
        ],
        description: [
          {
            required: true,
            message: "file description cannot be empty",
            trigger: "blur",
          },
        ],
      },
      toUploadFiles: [],
      fileCountTimer: null,
      progressModalShow: false,
      uploadProgress: 0,
      folderStack: [{ uid: 0, name: "Home" }], //folder level
      folderIdStack: [], // folder level
      fileList: [], //showed resources
      activityResList: [], //resources in the activity
      activityDataList: [], //data in the activity
      relatedResList: [], //related materials in the activity
      // toolDataList: [], //data from tools in the activity
      dataListStyle: false, // 数据列表展示样式
      // 资源avatar 图片路径
      dataUrl: require("@/assets/images/data.png"),
      modelUrl: require("@/assets/images/model.png"),
      paperUrl: require("@/assets/images/paper.png"),
      documentUrl: require("@/assets/images/document.png"),
      imageUrl: require("@/assets/images/image.png"),
      videoUrl: require("@/assets/images/video.png"),
      otherUrl: require("@/assets/images/otherfile.png"),
      folderUrl: require("@/assets/images/folder.png"),
      //
      checkDataModal: false,
      tableColName: [
        {
          title: "Name",
          key: "name",
          minWidth: 30,
          tooltip: true,
          sortable: true,
        },
        // {
        //   title: "Description",
        //   key: "description",
        //   tooltip: true,
        // },
        {
          title: "Action",
          slot: "action",
          width: 120,
          align: "center",
        },
      ],
      selectData: {},
      // 编辑data描述信息
      metaDataEdit: false,
      // 元数据信息，待完善
      metaDataInfo: "",
      // 删除资源
      deleteResourceModal: false,
      deleteResourceId: "",
      panel: null,
    };
  },
  watch: {
    checkDataModal(value) {
      // if (!value) {
      //   this.panel.close();
      // }
    },
    activityInfo() {
      this.getResList();
    },
  },
  created() {},
  mounted() {
    this.getResList();

    Date.prototype.Format = function (fmt) {
      var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S+": this.getMilliseconds(), //毫毛
      };
      if (/(y+)/.test(fmt))
        fmt = fmt.replace(
          RegExp.$1,
          (this.getFullYear() + "").substr(4 - RegExp.$1.length)
        );
      for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
          fmt = fmt.replace(
            RegExp.$1,
            RegExp.$1.length == 1
              ? o[k]
              : ("00" + o[k]).substr(("" + o[k]).length)
          );
      return fmt;
    };
  },
  methods: {
    roleIdentity(activity) {
      this.userInfo = this.$store.getters.userInfo;
      return this.userRoleApi.roleIdentify(
        activity.members,
        this.userInfo.userId
      );
    },
    permissionIdentity(permission, role, operation) {
      if (permission == undefined)
        permission = JSON.stringify(this.userRoleApi.getDefault());
      if (operation == "auto_join") {
        if (JSON.parse(permission).auto_join.visitor == "Yes") return true;
        else if (JSON.parse(permission).auto_join.visitor == "No") return false;
        else {
          return this.getParentPermission();
        }
      } else {
        return this.userRoleApi.permissionIdentity(
          JSON.parse(permission),
          role,
          operation
        );
      }
    },
    dateFormat(date) {
      let time = new Date(date);
      return time.Format("yyyy-MM-dd HH:mm:ss")
    },
    getImageUrl(type) {
      let url;
      switch (type) {
        case 'data': {
          url = this.dataUrl;
          break;
        }
        case 'model': {
          url = this.modelUrl;
          break;
        }
        case 'paper': {
          url = this.paperUrl;
          break;
        }
        case 'document': {
          url = this.documentUrl;
          break;
        }
        case 'image': {
          url = this.imageUrl;
          break;
        }
        case 'video': {
          url = this.videoUrl;
          break;
        }
        case 'others': {
          url = this.otherUrl;
          break;
        }
      }
      return url;
    },
    getResList() {
      if (this.activityInfo.aid != "" && this.activityInfo.aid != undefined) {
        this.axios
          .get("/GeoProblemSolving/rip/" + this.activityInfo.aid + "/0")
          .then((res) => {
            if (res.data == "Offline") {
              confirm("You are offline, please login again.");
            } else if (res.data.code == 0) {
              let list = res.data.data;
              this.$set(this, "activityResList", list);
              this.filterData();
              this.filterRelatedRes();
              // this.filterToolData();

              // show resources
              this.$set(this, "fileList", list);
            }
          })
          .catch((err) => {
            throw err;
          });
      }
    },
    filterData() {
      var filterdata = this.activityResList.filter((item) => {
        return item.folder || item.type === "data";
      });
      this.$set(this, "activityDataList", filterdata);
    },
    filterRelatedRes() {
      var filterdata = this.activityResList.filter((item) => {
        return item.folder || item.type !== "data";
      });
      this.$set(this, "relatedResList", filterdata);
    },
    // filterToolData() {
    //   var filterdata = this.fileList.filter((item) => {
    //     if (item.type.indexOf("toolData") != -1) {
    //       return item;
    //     }
    //   });
    //   this.$set(this, "toolDataList", filterdata);
    // },

    changeResType(value) {
      if (value == "all") {
        this.fileList = this.activityResList;
      } else if (value == "data") {
        this.fileList = this.activityDataList;
      } else if (value == "others") {
        this.fileList = this.relatedResList;
      } else if (value == "toolData") {
        // this.fileList = this.toolDataList;
      }
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
    dataUploadModalShow() {
      this.uploadDataInfo = {
        privacy: "private",
        type: "data",
        description: "",
      };
      this.dataUploadModal = true;
    },
    folderUpload(name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          var uploadFiles = this.toUploadFiles;
          if (uploadFiles.length > 0) {
            this.dataUploadModal = false;
            var formData = new FormData();
            for (var i = 0; i < uploadFiles.length; i++) {
              formData.append("file", uploadFiles[i]);
            }
            let temp = this.folderIdStack;
            if (temp.length == 0) {
              temp = ["0"];
            }
            formData.append("description", this.uploadDataInfo.description);
            formData.append("type", this.uploadDataInfo.type);
            formData.append("privacy", this.uploadDataInfo.privacy);
            formData.append("aid", this.activityInfo.aid);
            formData.append("paths", temp.toString());
            this.progressModalShow = true;

            if (
              this.activityInfo.aid != "" ||
              this.activityInfo.aid != undefined
            ) {
              this.axios({
                url: "/GeoProblemSolving/rip/file/upload",
                method: "post",
                onUploadProgress: (progressEvent) => {
                  this.uploadProgress =
                    ((progressEvent.loaded / progressEvent.total) * 100) | 0;
                },
                data: formData,
              })
                .then((res) => {
                  if (res.data != "Fail") {
                    var uploadedList = res.data.uploaded;
                    var failedList = res.data.failed;
                    var sizeOverList = res.data.sizeOver;

                    for (var i = 0; i < uploadedList.length; i++) {
                      this.fileList.push(uploadedList[i]);

                      this.activityResList.push(uploadedList[i]);
                      if (this.uploadDataInfo.type == "data") {
                        this.activityDataList.push(uploadedList[i]);
                      } else if (this.uploadDataInfo.type == "others") {
                        this.relatedResList.push(uploadedList[i]);
                      }
                    }
                    if (sizeOverList.length > 0) {
                      this.$Notice.warning({
                        title: "Files too large.",
                        render: (h) => {
                          return h("span", sizeOverList.join(";"));
                        },
                      });
                    }
                    if (failedList.length > 0) {
                      this.$Notice.error({
                        title: "Upload fail.",
                        render: (h) => {
                          return h("span", failedList.join(";"));
                        },
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
                .catch((err) => {
                  this.progressModalShow = false;
                  this.$Message.warning("Upload fail.");
                  this.uploadProgress = 0;
                });
            } else {
              this.$Notice.info({
                desc: "upload data failed!",
              });
            }
          } else {
            this.$Message.warning("Data is empty.");
          }
        }
      });
    },
    deleteResourceModalShow(id) {
      this.deleteResourceModal = true;
      this.deleteResourceId = id;
    },
    deleteResource() {
      if (this.deleteResourceId != "") {
        let temp = this.folderIdStack;
        if (temp.length == 0) {
          //重新开辟内存空间的temp,如果直接使用push的话，地址还是指向原数据的地址
          temp = ["0"];
        }
        let formData = new FormData();
        formData.append("uids", this.deleteResourceId);
        formData.append("aid", this.activityInfo.aid);
        formData.append("paths", temp.toString());
        this.axios
          .post("/GeoProblemSolving/rip/del", formData)
          .then((res) => {
            if (res.data == "Offline") {
              confirm("You are offline, please login again.");
            } else if (res.data.code == 0) {
              this.$Notice.success({
                title: "Process result",
                desc: "Delete successfully",
              });

              //从列表中删除
              let deleteResType = "";
              // for (let i = 0; i < this.fileList.length; i++) {
              //   if (this.fileList[i].uid == this.deleteResourceId) {
              //     // 记录信息
              //     try {
              //       deleteResType = this.fileList[i].type;
              //       this.fileList.splice(i, 1);
              //     } catch (err) {
              //       throw err;
              //     }
              //   }
              // }
              // 同步删除其他数组内的资源
              for (var i = 0; i < this.activityResList.length; i++) {
                if (
                  this.activityResList[i].uid == this.deleteResourceId
                ) {
                  this.activityResList.splice(i, 1);
                }
              }
              if (deleteResType == "data") {
                for (var i = 0; i < this.activityDataList.length; i++) {
                  if (
                    this.activityDataList[i].uid == this.deleteResourceId
                  ) {
                    this.activityDataList.splice(i, 1);
                  }
                }
              } else if (deleteResType == "others") {
                for (var i = 0; i < this.relatedResList.length; i++) {
                  if (
                    this.relatedResList[i].uid == this.deleteResourceId
                  ) {
                    this.relatedResList.splice(i, 1);
                  }
                }
              }
              // // 如果删除的是ToolData, 同步删除
              // for (var i = 0; i < this.toolDataList.length; i++) {
              //   if (this.toolDataList[i].uid == this.deleteResourceId) {
              //     this.toolDataList.splice(i, 1);
              //   }
              // }
            }
          })
          .catch((err) => {
            console.log(err.data);
          });
      }
    },
    // addHistoryEvent(scopeId, record) {
    //   let form = {};
    //   form["description"] = JSON.stringify(record);
    //   form["scopeId"] = scopeId;
    //   form["eventType"] = "activity";
    //   form["userId"] = this.$store.getters.userId;
    //   this.axios
    //     .post("/GeoProblemSolving/history/save", form)
    //     .then(res => {
    //       console.log(res.data);
    //     })
    //     .catch(err => {
    //       console.log(err.data);
    //     });
    // },
    checkData(item) {
      this.selectData = item;
      this.checkDataModal = true;
    },
    editMetadata() {
      if (this.metaDataEdit) {
        this.metaDataEdit = false;
      } else {
        this.metaDataEdit = true;
      }
    },
    dataPreview(res) {
      let name = res.name;
      if (/\.(doc|docx|xls|xlsx|ppt|pptx)$/.test(name.toLowerCase())) {
        this.$Modal.confirm({
          title: "Note",
          content:
            "<p>You selected file will be previewed through</p><p style='font-size:16px;font-weight:bold'>Microsoft office online service</p>",
          onOk: () => {
            if (this.panel != null) {
              this.panel.close();
            }
            let url =
              "http://view.officeapps.live.com/op/view.aspx?src=" +
              res.address;
            let toolURL =
              "<iframe src=" +
              url +
              ' style="width: 100%;height:100%" frameborder="0"></iframe>';
            // var demoPanelTimer = null;
            this.panel = jsPanel.create({
              headerControls: {
                smallify: "remove",
              },
              theme: "primary",
              footerToolbar: '<p style="height:5px"></p>',
              headerTitle: "Preview",
              contentSize: "800 600",
              content: toolURL,
              disableOnMaximized: true,
              dragit: {
                containment: 5,
              },
              closeOnEscape: true,
              // callback: function() {
              //   var that = this;
              //   demoPanelTimer = window.setInterval(function() {
              //     that.style.zIndex = "9999";
              //   }, 1);
              // }
            });
            $(".jsPanel-content").css("font-size", "0");
          },
          onCancel: () => {
            return;
          },
        });
      } else if (/\.(mp4)$/.test(name.toLowerCase())) {
        if (this.panel != null) {
          this.panel.close();
        }
        var toolURL =
          "<video src=" +
          res.address +
          ' style="width: 100%;height:100%" controls></video>';
        // var demoPanelTimer = null;
        this.panel = jsPanel.create({
          headerControls: {
            smallify: "remove",
          },
          theme: "primary",
          footerToolbar: '<p style="height:10px"></p>',
          headerTitle: "Preview",
          contentSize: "800 600",
          content: toolURL,
          disableOnMaximized: true,
          dragit: {
            containment: 5,
          },
          closeOnEscape: true,
          // callback: function() {
          //   var that = this;
          //   demoPanelTimer = window.setInterval(function() {
          //     that.style.zIndex = "9999";
          //   }, 1);
          // }
        });
        $(".jsPanel-content").css("font-size", "0");
      } else if (/\.(pdf|json|md|gif|jpg|png)$/.test(name.toLowerCase())) {
        if (this.panel != null) {
          this.panel.close();
        }
        var toolURL =
          "<iframe src=" +
          res.address +
          ' style="width: 100%;height:100%" frameborder="0" controls></iframe>';
        // var demoPanelTimer = null;
        this.panel = jsPanel.create({
          headerControls: {
            smallify: "remove",
          },
          theme: "primary",
          footerToolbar: '<p style="height:10px"></p>',
          headerTitle: "Preview",
          contentSize: "800 600",
          content: toolURL,
          disableOnMaximized: true,
          dragit: {
            containment: 5,
          },
          closeOnEscape: true,
          // callback: function() {
          //   var that = this;
          //   demoPanelTimer = window.setInterval(function() {
          //     that.style.zIndex = "9999";
          //   }, 1);
          // }
        });
        $(".jsPanel-content").css("font-size", "0");
      } else {
        this.$Notice.error({
          title: "Open failed",
          desc: "Sorry. Unsupported file format.",
        });
        return false;
      }
    },
    getPersonalRes() {

    },
    getPreviousRes() {
      // 获取可继承的资源
      this.getPreActivities();
      this.inheritResModal = true;
    },
    getPreActivities() {
      this.preActivities = [];
      // parent
      if (
        this.activityInfo.parent != undefined &&
        this.activityInfo.parent != ""
      ) {
        this.preActivities.push({ aid: this.activityInfo.parent });
      }
      // last activities
      if (this.activityInfo.last != undefined) {
        for (var i = 0; i < this.activityInfo.last.length; i++) {
          this.preActivities.push(this.activityInfo.last[i]);
        }
        this.getInheritResource();
      }
    },
    getInheritResource() {
      this.existingResources = this.getMockData();
    },
    getMockData() {
      let mockData = [];
      let selectedRes = [];

      // 前驱步骤的资源
      for (var i = 0; i < this.preActivities.length; i++) {
        let activityId = this.preActivities[i].aid;
        let activityName = this.preActivities[i].name;

        this.axios
          .get("/GeoProblemSolving/rip/" + activityId + "/0")
          .then((res) => {
            if (res.data == "Offline") {
              confirm("You are offline, please login again.");
            } else if (res.data.code == 0) {
              selectedRes = res.data.data;
              for (var j = 0; j < selectedRes.length; j++) {
                mockData.push({
                  key: mockData.length.toString(),
                  name: selectedRes[j].name,
                  type: selectedRes[j].type,
                  uid: selectedRes[j].uid,
                  source: activityName,
                });
              }
            } else {
              selectedRes = [];
            }
          })
          .catch((err) => {
            selectedRes = [];
            throw err;
          });
      }
      return mockData;
    },
    getTargetKeys() {
      let mockData = [];
      if (this.existingResources.length > 0) {
        for (var i = 0; i < this.targetKeys.length; i++) {
          mockData.push({
            key: this.targetKeys[i],
            name: this.existingResources[this.targetKeys[i]].name,
            type: this.existingResources[this.targetKeys[i]].type,
            uid: this.existingResources[this.targetKeys[i]].uid,
            source: this.existingResources[this.targetKeys[i]].source,
          });
        }
      }
      return mockData;
    },
    handleChange(newTargetKeys) {
      this.targetKeys = newTargetKeys;
    },
    resourceRender(item) {
      // return item.type + " - " + item.name;
      return `<span title="${item.type} - ${item.source}">${item.name}</span>`;
    },
    filterMethod(data, query) {
      return data.type.indexOf(query) > -1;
    },
    saveResources() {
      let selectResource = this.getTargetKeys();
      let addFileList = [];
      for (var i = 0; i < selectResource.length; i++) {
        addFileList.push(selectResource[i].uid);
      }

      let tempPath = this.folderIdStack;
      if (tempPath.length == 0) {
        tempPath = ["0"];
      }
      this.axios
        .get(
          "/GeoProblemSolving/rip/shareToProject/" +
            this.activityInfo.aid +
            "/" +
            addFileList.toString() +
            "/" +
            tempPath.toString()
        )
        .then((res) => {
          this.shareModal = false;
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data.code == 0) {
            this.getResList();
          } else {
            this.$Message.error(
              "Failed to get resources from previous activities."
            );
          }
        })
        .catch((err) => {
          this.$Message.error(
            "Failed to get resources from previous activities."
          );
        });
    },
    enterFolder(currentFolder) {
      this.folderIdStack.unshift(currentFolder.uid);
      this.changeFolder(currentFolder, "enter");
    },
    backforeFolder() {
      if (this.folderStack.length > 1) {
        //folderStack 倒数第二个就是其的父文件夹
        this.folderIdStack.splice(0, 1);
        this.changeFolder(
          this.folderStack[this.folderStack.length - 2],
          "back"
        );
      } else {
        this.$Message.warning("This is the root folder.");
      }
    },
    changeFolder(folder, operationType) {
      let temp = this.folderIdStack;
      console.log(this.folderIdStack);
      if (temp.length == 0) {
        temp = ["0"];
      }
      this.axios
        .get(
          "/GeoProblemSolving/rip/" +
            this.activityInfo.aid +
            "/" +
            temp.toString()
        )
        .then((res) => {
          if (res.data == "Offline") {
            confirm("You are offline, please login again.");
          } else if (res.data.code == 0) {
            let list = res.data.data;

            this.$set(this, "activityResList", list);
            this.filterData();
            this.filterRelatedRes();
            // this.filterToolData();

            // show resources
            this.$set(this, "fileList", list);

            if (operationType == "enter") {
              this.folderStack.push({ uid: folder.uid, name: folder.name });
            } else if (operationType == "back") {
              this.folderStack.pop();
            }
          } else {
            this.$Message.warning("Get folder info fail.");
          }
        })
        .catch((err) => {
          this.$Message.warning("Get folder info fail.");
        });
    },
    addFolderModalShow() {
      this.newValidate.setName = "";
      this.newFolderModal = true;
    },
    addFolder(name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          let pathArray = this.folderIdStack;
          if (pathArray.length == 0) {
            pathArray = ["0"];
          }
          let formData = new FormData();
          formData.append("folderName", this.newValidate.setName);
          formData.append("paths", pathArray.toString());
          formData.append("aid", this.activityInfo.aid);
          this.axios
            .post("/GeoProblemSolving/rip/folder", formData)
            .then((res) => {
              if (res.data == "Offline") {
                confirm("You are offline, please login again.");
              } else if (res.data.code == 0) {
                this.activityResList.push(res.data.data);
                this.activityDataList.push(res.data.data);
                this.relatedResList.push(res.data.data);

                this.newFolderModal = false;
              } else {
                this.$Message.warning("New folder fail.");
              }
            })
            .catch((err) => {
              this.$Message.warning("New folder fail.");
            });
        }
      });
    },
    deleteFolder(folder) {
      if (confirm("Are you sure to delete this folder?")) {
        let folderId = folder.uid;
        let temp = this.folderIdStack;
        if (temp.length == 0) {
          //重新开辟内存空间的temp,如果直接使用push的话，地址还是指向原数据的地址
          temp = ["0"];
        }
        let formData = new FormData();
        formData.append("uids", folderId);
        formData.append("aid", this.activityInfo.aid);
        formData.append("paths", temp.toString());
        this.axios
          .post("/GeoProblemSolving/rip/del", formData)
          .then((res) => {
            if (res.data == "Offline") {
              confirm("You are offline, please login again.");
            } else if (res.data.code == 0) {
              //删除用于显示的数据中对应的内容
              for (let i = 0; i < this.currentFolder.folders.length; i++) {
                if (this.currentFolder.folders[i].uid == folderId) {
                  this.currentFolder.folders.splice(i, 1);
                  break;
                }
              }
            } else {
              this.$Message.warning("Delete folder fail.");
            }
          })
          .catch((err) => {
            this.$Message.warning("Delete folder fail.");
          });
      }
    },
    renameFolderModalShow(folder) {
      this.renameForeInfo = folder;
      this.renameValidate.newName = "";
      this.renameFolderModal = true;
    },
    renameFolder(name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          let aid = this.activityInfo.aid;
          let folderId = this.renameForeInfo.uid;
          let newFolderName = this.renameValidate.newName;
          let temp = this.folderIdStack;
          if (temp.length == 0) {
            temp = ["0"];
          }
          let formData = new FormData();
          formData.append("aid", aid);
          formData.append("folderId", folderId);
          formData.append("newFolderName", newFolderName);
          formData.append("paths", temp.toString());
          this.axios
            .put("/GeoProblemSolving/rip/folder", formData)
            .then((res) => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({ name: "Login" });
              } else if (res.data.code == 0) {
                let newNameFolder = {
                  uid: folderId,
                  name: newFolderName,
                };
                for (var i = 0; i < this.currentFolder.folders.length; i++) {
                  if (this.currentFolder.folders[i].uid == folderId) {
                    this.currentFolder.folders.splice(i, 1, newNameFolder);
                    break;
                  }
                }
              } else {
                this.$Message.warning("Rename fail.");
              }
            })
            .catch((err) => {
              this.$Message.warning("Rename fail.");
            });
          this.renameFolderModal = false;
        }
      });
    },
    dataVisualize() {},
  },
};
</script>
<style scoped>
.res-content {
  width: 90px;
  height: 85px;
  float: left;
  margin: 5px;
  cursor: pointer;
}
.res-content-image {
  margin-left: 18px;
  padding-top: 5px;
}
.fileBtnHoverGreen:hover {
  background-color: #19be6b;
  color: white;
}
.fileBtnHoverBlue:hover {
  background-color: dodgerblue;
  color: white;
}
.fileBtnHoverRed:hover {
  background-color: #ed4014;
  color: white;
}
.dataInfo {
  margin: 5px 0;
}
.dataLabel {
  width: 90px;
  display: inline-block;
  font-size: 13px;
  font-weight: bold;
  vertical-align: top;
  color: dodgerblue;
}
.dataContent {
  width: 180px;
  display: inline-block;
  padding-left: 10px;
}
.dataText {
  padding-left: 10px;
  display: inline-block;
  word-break: break-word;
  width: 470px;
}
.toolDataLabel {
  font-weight: bold;
  vertical-align: top;
  color: dodgerblue;
}
.toolDataText {
  font-size: 12px;
  width: 80px;
  word-break: break-word;
  /* display: inline-block; */
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
  text-align: center;
}
#toolDataHeader {
  font-size: 12px;
  font-weight: bold;
  padding: 10px 15px;
  border-bottom: 1px solid #e8eaec;
  background-color: #f8f8f9;
}
.resCard {
  max-height: calc(100vh - 165px);
}
.resCard >>> .ivu-card-head {
  padding: 6px 16px;
}
.resCard >>> .ivu-card-body {
  padding: 5px;
}
</style>
