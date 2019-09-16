<template>
  <Row id="container">
    <Col span="6" id="left">
    <img id="model_img" src="@/assets/images/comparison/model.png" alt="model img">
    <span id="model_name">{{modelInfo.modelName}}</span>
    <p id="model_desc">{{modelInfo.description}}</p>
    </Col>

    <Col span="18" id="right">
    <h2 style="text-align:center;margin-top:30px;">Model Input</h2>
    <div id="stateRoom">
      <Card v-for="(state,stateIndex) of states" :bordered="false" :key="state.stateId" class="stateCard">
        <p slot="title">
          {{state.stateName}}
        </p>
        <span slot="title">
          <Icon type="md-nutrition" />
          {{state.description}}
        </span>
        <Form label-position="right" :label-width="150" :ref="state.stateName">
          <FormItem v-for="(event,eventIndex) of getInputEvent(state.events)" :key="event.name"
            :required="!event.optional">
            <span slot="label">{{event.name}}</span>
            <Input style="width: 500px" v-model="event.fileName" placeholder="Please upload or choose input date…"
              :ref="event.name">

            <Upload :max-size="1024*1024" :before-upload="beforeUpload" slot="append" :state="state.stateName"
              :event="event.name" action="/GeoProblemSolving/cmp_model/uploadData_DC" style="display:inline-block;"
              :show-upload-list="false" :data="uploadDataInfo" :on-success="uploadSuccess" :on-error="uploadError">
              <Button>
                <Icon type="md-cloud-upload" size="18" />
              </Button>
            </Upload>

            <Button slot="append">
              <Icon type="md-cloud-done" size="18" /> </Button>

            <Button slot="append">
              <Icon type="md-folder" size="18" /> </Button>
            </Input>

            <span style="color:#6a737d">{{event.description}}</span>
          </FormItem>
        </Form>
      </Card>
    </div>
    <Button type="success" id="invokeBtn" @click="invokeModel">Invoke</Button>
    </Col>
  </Row>
</template>
<script>
import Util from "@/utils/comparison/cmpUtils";

export default {
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      $.ajax({
        url: "/GeoProblemSolving/user/state",
        type: "GET",
        async: false,
        success: function(data) {
          if (!data) {
            vm.$store.commit("userLogout");
            vm.$router.push({ name: "Login" });
          }
        },
        error: function(err) {
          console.log("Get user state fail.");
        }
      });
    });
  },
  created: function() {
    //* 获取路由信息
    // this.modelInfo = this.$route.params.modelInfo;
    this.modelId = this.$route.params.id;
    // this.modelInfo = sessionStorage.getItem("modelInfo");
    this.modelInfo = JSON.parse(sessionStorage.getItem("modelInfo"));
    console.log("modelInfo: ", this.modelInfo);

    //* 获取计算模型信息
    this.getComputableModelInfo(this.modelId);
  },
  data() {
    return {
      modelInfo: {},
      modelId: "",
      computableModelInfo: {},
      states: [],
      uploadDataInfo: {
        stateName: "",
        eventName: "",
        md5: "",
        userName: this.$store.getters.userName,
        fileName: "",
        sourceStoreId: ""
      },
      inputData: [],
      inputText: {}
    };
  },
  methods: {
    getComputableModelInfo(modelId) {
      this.$api.cmp_model
        .getComputableModelInfo(modelId)
        .then(res => {
          this.computableModelInfo = res;
          this.states = res.states;
        })
        .catch(err => {
          this.$Message.error(err);
        });
    },

    beforeUpload(file, $event) {
      let infoDiv = event.currentTarget.parentElement.parentElement;
      let stateName = infoDiv.getAttribute("state");
      let eventName = infoDiv.getAttribute("event");
      this.uploadDataInfo.stateName = stateName;
      this.uploadDataInfo.eventName = eventName;
      this.uploadDataInfo.fileName = file.name;
      this.uploadDataInfo.sourceStoreId = "";
      this.uploadDataInfo.md5 = "";
      //计算 md5 值
      return new Promise((resolve, reject) => {
        //* 计算 md5
        Util.getFileMD5(file, md5 => {
          console.log(md5);
          this.uploadDataInfo.md5 = md5;
          //* 后台查询是否已经上传过。
          this.$api.cmp_model
            .matchDataMd5(md5)
            .then(res => {
              if (res.code == "-9999") {
                //* 没有该资源  需要上传
                resolve();
              } else {
                //* 已有该资源
                this.uploadDataInfo.sourceStoreId = res.data;
                file = null;
                resolve();
                // let formData = new FormData();
                // // formData.push("userName", this.$store.getters.userName);
                // formData.push("stateName", stateName);
                // formData.push("eventName", eventName);
                // formData.push("fileName", this.uploadDataInfo.fileName);
                // formData.push(
                //   "sourceStoreId",
                //   this.uploadDataInfo.sourceStoreId
                // );
                // formData.push("md5", this.uploadDataInfo.md5);
                // this.$api.cmp_model
                //   .uploadData_DC_Fast(formData)
                //   .then(res => {
                //     console.log("uploadData_DC_Fast:", res);
                //     // return false;
                //   })
                //   .catch(err => {
                //     this.$Message.error(err);
                //     // return false;
                //   });
              }
              console.log("md5:", res);
              // if (res == null) {
              //   console.log("第一次上传");
              //   resolve();
              // } else {
              //   console.log("该文件已存在：", res);
              //   this.computableModelOid = res;
              //   this.modelInfo.computableModels.push(res);
              //   this.creatable = true;
              //   reject();
              // }
            })
            .catch(err => {
              this.$Message.error(err);
              reject();
            });
        });

        // // *获取任务服务器
        // this.$api.cmp_model
        //   .getTaskServer(this.computableModelInfo.md5)
        //   .then(res => {
        //     this.taskServerNode.host = res.host;
        //     this.taskServerNode.port = res.port;
        //     console.log("测试：taskNodeRes", res);

        //     //* 获取数据服务器地址
        //     let formData = new FormData();
        //     formData.set("host", this.taskServerNode.host);
        //     formData.set("port", this.taskServerNode.port);
        //     formData.set("pid", this.computableModelInfo.md5);
        //     formData.set("username", this.$store.getters.userName);
        //     this.$api.cmp_model
        //       .getDataServerNode(formData)
        //       .then(res => {
        //         console.log("测试：dataNodeRes", res);
        //         this.dataServerNode = res;
        //         this.dataServerNode.stateName = stateName;
        //         this.dataServerNode.eventName = eventName;
        //         this.dataServerNode.fileName = file.name;
        //         resolve();
        //       })
        //       .catch(err => {
        //         this.$Message.error(err);
        //         reject();
        //       });
        //   })
        //   .catch(err => {
        //     this.$Message.error(err);
        //     reject();
        //   });
      });
    },
    // 弃用 任务服务器
    uploadSuccess(response) {
      if (response.code == 0) {
        let data = response.data;
        let stateName = data.statename;
        let eventName = data.event;
        let fileName = data.fileName;
        let url = data.url;

        // let input = {"statename":stateName,"event":eventName,"url":url,"tag":""};
        // this.inputData.push(input);
        // console.log("inputData:",this.inputData);

        let self = this;
        self.states.map(state => {
          if (state.stateName === stateName) {
            state.events.map(event => {
              if (event.name === eventName) {
                self.$set(event, "fileName", fileName);
                self.$set(event, "url", url);
              }
            });
          }
        });
        console.log(this.states);
      } else {
        this.$Message.error(response.msg);
      }
    },
    // 弃用 任务服务器
    uploadError() {
      this.$Message.error("upload data failed!");
    },
    // 弃用 任务服务器
    invokeModel() {
      //* 检查数据是否准备完毕
      let dataReady = this.checkInputData();
      if (!dataReady) {
        this.$Message.error("data not ready");
        return;
      }
      this.inputData = [];
      this.states.forEach(state => {
        return state.events.forEach(event => {
          if (event.url) {
            this.inputData.push({
              statename: state.stateName,
              event: event.name,
              url: event.url,
              tag: ""
            });
          }
        });
      });

      let formData = new FormData();
      formData.set("ip", this.taskServerNode.host);
      formData.set("port", this.taskServerNode.port);
      formData.set("pid", this.computableModelInfo.md5);
      formData.set("username", this.$store.getters.userName);
      formData.set("inputs", JSON.stringify(this.inputData));
      this.$api.cmp_model
        .invokeModel(formData)
        .then(res => {
          console.log(res);
        })
        .catch(err => {
          this.$Message.error(err);
        });

      console.log("inputData:", this.inputData);
    },
    // 弃用 任务服务器
    checkInputData() {
      return this.states.every(state => {
        return state.events.every(event => {
          if (event.type === "response" && !event.optional && !event.url) {
            return false;
          } else {
            return true;
          }
        });
      });
    }
  },
  computed: {
    getInputEvent() {
      return function(events) {
        return events.filter(event => {
          return event.type === "response";
        });
      };
    }
  }
};
</script>
<style scoped>
#container {
  width: 1200px;
  margin-left: auto;
  margin-right: auto;
}
#left {
  margin-top: 50px;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-right: 1px solid #e1e4e8;
}
#right {
  padding-left: 20px;
}
#model_img {
  width: 80px;
}
#model_name {
  font-size: 28px;
  color: #0366d6;
  margin-top: 10px;
}
#model_desc {
  font-size: 18px;
  margin-top: 5px;
  margin-left: 10px;
  color: #666;
}

#invokeBtn {
  float: right;
  margin: 20px;
}

.stateCard {
  margin-top: 20px;
  width: 90%;
  margin-left: auto;
  margin-right: auto;
}

.file Icon {
  cursor: pointer;
}

.file input {
  position: absolute;
  margin-left: -30px;
  width: 30px;
  opacity: 0;
  cursor: pointer;
}
</style>