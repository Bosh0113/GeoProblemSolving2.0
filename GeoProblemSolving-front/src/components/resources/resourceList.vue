<style scoped>
.main {
  display: flex;
}
.sidebarTree {
  padding-top: 20px;
  height: 420px;
  min-width: 250px;
  margin-right: 20px;
}
.resourcePanel {
  flex: 1;
}

.btnPanel {
  flex: 1;
}
.btnPanel button {
  margin-left: 20px;
}
.menuClass {
  border: 1px solid lightgray;
  z-index: 0;
}
</style>
<template>
  <Row>
    <Col span="22" offset="1">
      <h2 style="margin-top: 20px;">Resource center</h2>
      <div class="main" :style="{height:contentHeight-60 + 'px'}">
        <div class="sidebarTree">
          <Menu
            :theme="sidebarTheme"
            active-name="image"
            width="auto"
            class="menuClass"
            style="height:100%"
            @on-select="onMenuSelect"
          >
            <MenuGroup title="Resource type">
              <MenuItem name="image">
                <Icon type="md-image"/>Images
              </MenuItem>
              <MenuItem name="video">
                <Icon type="md-videocam"/>Videos
              </MenuItem>
              <MenuItem name="data">
                <Icon type="md-analytics"/>Data
              </MenuItem>
              <MenuItem name="paper">
                <Icon type="md-paper"/>Papers
              </MenuItem>
              <MenuItem name="document">
                <Icon type="md-document"/>Documents
              </MenuItem>
              <MenuItem name="model">
                <Icon type="logo-dropbox"/>Models
              </MenuItem>
              <MenuItem name="others">
                <Icon type="logo-xbox"/>Others
              </MenuItem>
            </MenuGroup>
          </Menu>
          <div style="display:flex;margin-top:20px;justify-content:center">
              <Button type="success" title="upload resource" @click="fileUploadModalShow()">
                <Icon type="ios-cloud-upload-outline" :size="20"/>
              </Button>
            </div>
        </div>
        <div class="resourcePanel">
          <div style="height:20px"></div>
          <div class="resourcePanel">
            <Row>
              <Col span="22" offset="1">
                <Table border :columns="resourceColumn" :data="showList">
                  <template slot-scope="{ row }" slot="name">
                    <strong>{{ row.name }}</strong>
                  </template>
                  <template slot-scope="{ row, index }" slot="action">
                    <a
                      :href="showList[index].pathURL"
                      :download="showList[index].name"
                      title="Download"
                    >
                      <Icon type="md-download" :size="20" color="yellowgreen"/>
                    </a>
                    <span @click="show(index)" style="margin-left: 10px;cursor:pointer" title="Preview">
                      <Icon type="md-eye" :size="20" color="orange"/>
                    </span>
                    <span
                      @click="deleteModalShow(index)"
                      v-show="judgeDelete(index)"
                      style="margin-left: 10px;cursor:pointer"
                    >
                      <Icon type="md-close" :size="20" color="red"/>
                    </span>
                  </template>
                </Table>
              </Col>
            </Row>
            <div style="display:flex;justify-content:center">
              <Page
                :total="dataCount"
                :page-size="pageSize"
                show-total
                @on-change="changepage"
                show-elevator
                style="position: absolute;top:600px"
              />
            </div>
          </div>
        </div>
      </div>
    </Col>
    <Modal
      v-model="uploadModal"
      title="Upload resource"
      @on-ok="resourceUpload('resourceValidate')"
      ok-text="Submit"
      cancel-text="Cancel"
      :mask-closable="false"
      width="600px"
    >
      <div style="display:flex;justify-content:center">
        <Form
          ref="resourceValidate"
          :model="resourceValidate"
          :rules="resourceValidateRule"
          :label-width="80"
        >
          <FormItem label="Privacy" prop="filePrivacy">
            <RadioGroup v-model="resourceValidate.filePrivacy">
              <Radio label="private">Private</Radio>
              <Radio label="public">Public</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem label="Type" prop="fileType">
            <RadioGroup v-model="resourceValidate.fileType">
              <Radio label="image">Images</Radio>
              <Radio label="video">Videos</Radio>
              <Radio label="data">Data</Radio>
              <Radio label="paper">Papers</Radio>
              <Radio label="document">Documents</Radio>
              <Radio label="model">Models</Radio>
              <Radio label="others">Others</Radio>
            </RadioGroup>
          </FormItem>
          <div style="display:flex;align-items:center;justify-content:center">
            <span style="width:20%">Description</span>
            <Input type="textarea" :rows="2" v-model="resourceValidate.fileDescription"/>
          </div>
          <Upload
            style="margin-top:10px"
            :max-size="1024*1024"
            multiple
            type="drag"
            :show-upload-list="false"
            :before-upload="gatherFile"
            action="-"
          >
            <div style="padding: 20px 0">
              <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
              <p>Click or drag files here to upload(The file size must control in <span style="color:red">1GB</span>)</p>
            </div>
          </Upload>
          <div style="padding:0 10px 0 10px">
            <ul v-for="(list,index) in file" :key="index">
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
            </ul>
          </div>
          <!-- <h6 style="text-align:center;color:red">The file's size must control smaller than 1 GB.</h6> -->
        </Form>
      </div>
      <!-- <input type="file" @change="getFile($event)" style="margin-left:20%" multiple="multiple"> -->
    </Modal>
    <Modal
      v-model="deleteModal"
      @on-ok="deleteResource"
      @on-cancel
      ok-text="Assure"
      cancel-text="Cancel"
    >
      <h3>Do you really want to delete this resource?</h3>
    </Modal>
    <Modal
      v-model="progressModalShow"
      title="Upload Progress"
      :mask-closable="false"
      :closable="false"
    >
      <Progress :percent="uploadProgress"></Progress>
    </Modal>
  </Row>
</template>
<script>
export default {
  data() {
    return {
      searchResourceInput: "",
      // 侧边栏的颜色主题
      sidebarTheme: "light",
      resourceColumn: [
        {
          type: "index",
          maxWidth: 50,
          align: "center"
        },
        {
          title: "Name",
          key: "name",
          minWidth: 10,
          tooltip: true,
          sortable: true
        },
        {
          title: "Type",
          key: "type",
          width: 100,
          sortable: true
        },
        {
          title: "Size",
          key: "fileSize",
          width: 100,
          sortable: true
        },
        {
          title: "Description",
          key: "description",
          minWidth: 30,
          tooltip: true
        },
        {
          title:"Provider",
          key:"uploaderName",
          width: 150,
          align: "center"
        },
        {
          title: "Upload time",
          key: "uploadTime",
          width: 150,
          sortable: true
        },
        {
          title: "Action",
          slot: "action",
          width: 125,
          align: "center"
        }
      ],
      uploaderArray: [],
      specifiedResourceList: [],
      // 上传文件的模态框
      uploadModal: false,
      resourceValidate: {
        fileType: "image",
        filePrivacy: "private",
        resourceValidate:"",
      },
      resourceValidateRule: {
        fileType: [
          { required: true, message: "Please select type...", trigger: "blur" }
        ],
        filePrivacy: [
          { required: true, message: "Please set privacy...", trigger: "blur" }
        ]
      },
      // 上传的文件
      file: [],
      // 上传文件个数控制器
      fileCountTimer: null,
      // 上传文件的进度
      uploadProgress: 0,
      // 显示进度条的模态框
      progressModalShow: false,
      contentHeight: "",
      panel: null,
      // 分页
      allResourceList: [],
      allSelectedList: [],
      showList: [],
      dataCount: 0,
      pageSize: 10,
      // 控制删除资源按钮的模态框
      deleteModal: false,
      // 删除资源的Id
      deleteResourceId: "",
      // 删除资源的位置索引
      deletePosition:"",
      // 侧边栏刚才选中的选项卡
      justSelectedItem:"image"
    };
  },
  mounted() {
    this.initLayout();
    this.onMenuSelect("image");
  },
  beforeRouteLeave(to, from, next) {
    if (this.panel != null) {
      this.panel.close();
    }
    next();
  },
  methods: {
    initLayout() {
      this.contentHeight = window.innerHeight - 120;
    },
    onMenuSelect(name) {
      this.resourceValidate.fileType = name;
      this.uploaderArray = [];
      this.justSelectedItem = name;
      this.specifiedResourceList = [];
      this.axios
        .get(
          "/GeoProblemSolving/resource/inquiry" + "?key=type" + "&value=" + name
        )
        .then(res => {
          if (res.data != "None") {
            let specifiedResourceListPre = res.data;
            specifiedResourceListPre.forEach(function(list) {
              list["uploader"] = list.uploaderName;
            });
            this.dataCount = specifiedResourceListPre.length;
            this.$set(this, "specifiedResourceList", specifiedResourceListPre);
            this.sliceList();
          }
        });
    },
    sliceList() {
      var tempResourceList = this.specifiedResourceList;
      if (this.dataCount < this.pageSize) {
        this.$set(this, "showList", tempResourceList);
      } else {
        this.$set(this, "showList", tempResourceList.slice(0, this.pageSize));
      }
    },
    changepage(index) {
      var _start = (index - 1) * this.pageSize;
      var _end = index * this.pageSize;
      this.showList = this.specifiedResourceList.slice(_start, _end);
    },
    handleSelectAll(status) {
      this.$refs.selection.selectAll(status);
    },
    fileUploadModalShow() {
      if (this.$store.getters.userState) {
        this.uploadModal = true;
      } else {
        this.$Notice.open({
          title: "Login Please",
          desc: "If you want to upload a file here, please login first."
        });
      }
    },
    // 阻止iview默认上传的方法
    gatherFile(file) {
      let that = this;
      if (that.file.length >= 5) {
        if (this.fileCountTimer != null) {
          clearTimeout(this.fileCountTimer);
        }
        this.fileCountTimer = setTimeout(() => {
          this.$Message.info("you can upload 5 files one time!");
        }, 500);
      } else {
        that.file.push(file);
        that.file.map(element => {
          element["fileSize"] = Math.round((element.size / 1024) * 100) / 100;
        });
      }
    },
    delFileList(index) {
      let that = this;
      that.file.splice(index, 1);
    },
    resourceUpload(form) {
      this.uploadProgress = 0;
      this.$refs[form].validate(valid => {
        if (valid) {
          let that = this;
          if (that.file.length != 0) {
            var formData = new FormData();
            var total = 0;
            for (var i = 0; i < that.file.length; i++) {
              if (that.file[i].fileSize < Math.pow(1024, 2)) {
                formData.append("file", that.file[i]); // 文件对象
              } else {
              }
              total += that.file[i].fileSize;
            }
            if (total < Math.pow(1024, 2)) {
              let userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
              formData.append("description", this.resourceValidate.fileDescription);
              formData.append("type", this.resourceValidate.fileType);
              formData.append(
                "uploaderId",
                this.$store.getters.userInfo.userId
              );
              formData.append("belong", this.$store.getters.userName);
              let scopeObject = {
                projectId: "",
                subprojectId: "",
                moduleId: ""
              };
              formData.append("scope", JSON.stringify(scopeObject));
              formData.append("privacy", this.resourceValidate.filePrivacy);
              this.progressModalShow = true;
              this.axios({
                url: "/GeoProblemSolving/resource/upload",
                method: "post",
                onUploadProgress: progressEvent => {
                  this.uploadProgress =
                    ((progressEvent.loaded / progressEvent.total) * 100) | 0;
                },
                data: formData
              })
                .then(res => {
                  if(res.data == "Offline"){
                    this.$store.commit("userLogout");
                    this.$router.push({ name: "Login" });
                  }else if (res.data != "Size over" && res.data.length > 0) {
                    this.$Notice.open({
                      title: "Upload notification title",
                      desc: "File uploaded successfully",
                    });
                    this.onMenuSelect(this.justSelectedItem);
                    this.file = [];
                    this.resourceValidate.fileDescription = "";
                    this.resourceValidate.filePrivacy = "private";
                    this.resourceValidate.fileType = "data";
                    // 创建一个函数根据pid去后台查询该项目下的资源
                  }
                  this.progressModalShow = false;
                  this.uploadProgress = 0;
                })
                .catch(err => {
                  this.progressModalShow = false;
                  this.uploadProgress = 0;
                });
            } else {
              this.$Message.error(
                "size over,all the file size must smaller than 1 GB one time!"
              );
            }
          }
        } else {
           this.$Message.error("Please check your form again!");
        }
      });
    },
    judgeDelete(index) {
      if (
        this.showList[index].uploaderId ==
        this.$store.getters.userId
      ) {
        return true;
      } else {
        return false;
      }
    },
    show(index) {
      let name = this.showList[index].name;

      if (
        /\.(doc|docx|xls|xlsx|csv|ppt|pptx|zip)$/.test(name.toLowerCase())
      ) {
        if (this.panel != null) {
          this.panel.close();
        }
        var url =
          "http://172.21.212.72:8012/previewFile?url=" +
          "http://" +
          this.$store.state.IP_Port +
          this.specifiedResourceList[index].pathURL;
        var toolURL =
          "<iframe src=" + url + ' style="width: 100%;height:100%"></iframe>';
        this.panel = jsPanel.create({
          headerControls: {
            smallify: "remove"
          },
          theme: "primary",
          footerToolbar: '<p style="height:10px"></p>',
          headerTitle: "Preview",
          contentSize: "800 600",
          content: toolURL,
          disableOnMaximized: true,
          dragit: {
            containment: 5
          },
          closeOnEscape: true
        });
        $(".jsPanel-content").css("font-size", "0");
      } else if (/\.(mp4)$/.test(name.toLowerCase())) {
        if (this.panel != null) {
          this.panel.close();
        }
        var url =
          "http://" +
          this.$store.state.IP_Port +
          this.specifiedResourceList[index].pathURL;
        var toolURL =
          "<video src=" +
          url +
          ' style="width: 100%;height:100%" controls></video>';
        this.panel = jsPanel.create({
          headerControls: {
            smallify: "remove"
          },
          theme: "primary",
          footerToolbar: '<p style="height:10px"></p>',
          headerTitle: "Preview",
          contentSize: "800 600",
          content: toolURL,
          disableOnMaximized: true,
          dragit: {
            containment: 5
          },
          closeOnEscape: true
        });
        $(".jsPanel-content").css("font-size", "0");
      } else if (/\.(pdf|xml|json|md|gif|jpg|png)$/.test(name.toLowerCase())) {
        if (this.panel != null) {
          this.panel.close();
        }
        var url =
          "http://" +
          this.$store.state.IP_Port +
          this.specifiedResourceList[index].pathURL;
        var toolURL =
          "<iframe src=" +
          url +
          ' style="width: 100%;height:100%" controls></iframe>';
        this.panel = jsPanel.create({
          headerControls: {
            smallify: "remove"
          },
          theme: "primary",
          footerToolbar: '<p style="height:10px"></p>',
          headerTitle: "Preview",
          contentSize: "800 600",
          content: toolURL,
          disableOnMaximized: true,
          dragit: {
            containment: 5
          },
          closeOnEscape: true
        });
        $(".jsPanel-content").css("font-size", "0");
      } else {
        this.$Notice.error({
          title: "Open failed",
          desc: "Not supported file format."
        });
        return false;
      }
    },
    // 删除资源的模态框提醒
    deleteModalShow(index) {
      this.deleteModal = true;
      this.deletePosition = index;
      this.deleteResourceId = this.showList[index].resourceId;
    },
    deleteResource() {
      if (this.deleteResourceId != "") {
        this.axios
          .get(
            "/GeoProblemSolving/resource/delete?" +
              "resourceId=" +
              this.deleteResourceId
          )
          .then(res => {
            if (res.data == "Success") {
              this.$Notice.success({
                title: "Notification title",
                desc: "Delete successfully"
              });
              this.showList.splice(this.deletePosition, 1);
            } else if (res.data == "Fail") {
              this.$Message.info("Failure");
            }
          })
          .catch(err => {
            console.log(err.data);
          });
      }
    }
  }
};
</script>
