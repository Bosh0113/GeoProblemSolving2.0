<template>
  <div class="main">
    <div id="collab-tool-head"></div>
    <div id="collab-tool-sidebar"></div>
    <div id="collab-tool-content" class="scrollbar" style="margin-top: 20px">
      <el-row>
        <!--        <el-col :sm="{ span: 10 }" :lg="{ span: 8 }" :xl="{ span: 6 }">-->
        <!--          <el-card class="left-card">-->
        <!--            <div style="float:left;font-size:60px;color:#409EFF;margin-left:50px">-->
        <!--              <i class="el-icon-s-tools" />-->
        <!--            </div>-->
        <!--            <div style="float:left;margin-left:40px">-->
        <!--              <div class="title">{{ dataService.name }}</div>-->
        <!--              <div style="float:left;margin:3px 15px 0 0">-->
        <!--                Contributed by-->
        <!--              </div>-->
        <!--              <div style=" font-weight: 600;font-size: 16px;float:left">-->
        <!--                {{ dataService.authorship }}-->
        <!--              </div>-->
        <!--            </div>-->

        <!--            <div style="clear:both">-->
        <!--              <el-button-->
        <!--                type="primary"-->
        <!--                @click="invokeTest"-->
        <!--                style="float:left;width:100px;margin-left:30px"-->
        <!--              >-->
        <!--                <i class="el-icon-setting"></i>&nbsp;Invoke-->
        <!--              </el-button>-->
        <!--              <el-button-->
        <!--                type="primary"-->
        <!--                @click="getRemoteData"-->
        <!--                style="float:right;width:140px;margin-right:30px"-->
        <!--              >-->
        <!--                <i class="el-icon-setting"></i>&nbsp;Remote Data-->
        <!--              </el-button>-->
        <!--            </div>-->

        <!--            <div class="des" style="clear:both;margin-top:20px">-->
        <!--              <el-divider class="left-divider"></el-divider>-->
        <!--              <div style="font-weight: 600;font-size: 16px;">Description</div>-->
        <!--              <div v-if="dataService.hasOwnProperty('metaDetail')">-->
        <!--                {{ dataService.metaDetail.description }}-->
        <!--              </div>-->
        <!--            </div>-->
        <!--            <div class="des">{{ dataService.date }}</div>-->
        <!--          </el-card>-->
        <!--        </el-col>-->

        <el-col
          v-if="dataService.hasOwnProperty('metaDetail')"
        >
          <el-divider
            direction="vertical"
            style="height:1000px;float:left"
          ></el-divider>
          <div class="_params-group">
            <div class="stateTitle">
              Input
            </div>
            <el-divider class="stateTitleDivider"></el-divider>
            <div
              v-for="(input, index) in dataService.metaDetail.input"
              :key="index"
              class="event"
            >
              <el-row>
                <el-col :span="17" class="_event-desc">
                <span class="event_name" :title="input.name">{{
                  input.name
                }}</span>
                  <p class="event_desc" :title="input.description">
                    {{ input.description }}
                  </p>
                </el-col>
                <el-col :span="6" :offset="1">
                  <div
                    v-if="
                    input.hasOwnProperty('url') &&
                      input.url != '' &&
                      input.urlName != ''
                  "
                  >
                    <div class="select-data select-data-line">
                      <div class="data-name">{{ input.urlName }}</div>
                      <el-button
                        type="success"
                        icon="el-icon-download"
                        size="mini"
                        circle
                        @click="download(input)"
                      ></el-button>
                      <el-button
                        type="warning"
                        icon="el-icon-close"
                        size="mini"
                        circle
                        @click="remove(input)"
                      ></el-button>
                    </div>
                  </div>
                  <div v-else>
                    <el-button
                      type="primary"
                      plain
                      @click="selectDataDialog(index)"
                    >
                      Select data
                    </el-button>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
          <div
            class="_params-group"
            v-if="dataService.metaDetail.parameter.length != 0"
          >
            <div class="stateTitle">
              Parameter
            </div>
            <el-divider class="stateTitleDivider"></el-divider>
            <div
              v-for="(parameter, index) in dataService.metaDetail.parameter"
              :key="index"
              class="event"
            >
              <el-row>
                <el-col :span="17" class="_event-desc">
                <span class="event_name" :title="parameter.name">{{
                  parameter.name
                }}</span>
                  <p class="event_desc" :title="parameter.description">
                    {{ parameter.description }}
                  </p>
                </el-col>
                <el-col :span="6" :offset="1">
                  <el-input
                    style="width:200px"
                    v-model="parameter.value"
                    placeholder="Enter the parameter"
                  ></el-input>
                </el-col>
              </el-row>
            </div>
          </div>
          <!--          OUTPUT 相关-->
          <div class="_params-group">
            <div class="stateTitle">
              Output
            </div>
            <el-divider class="stateTitleDivider"></el-divider>

            <div
              v-for="(output, index) in dataService.metaDetail.output"
              :key="index"
              class="event"
            >
              <el-row style="height:100px">
                <el-col :span="17" class="_event-desc">
                <span class="event_name" :title="output.name">{{
                  output.name
                }}</span>
<!--                  <p class="event_desc" :title="output.description">-->
<!--                    {{ output.description }}-->
<!--                  </p>-->
                </el-col>
                <div>
                  <div class="_btn-group" v-if="type == 'Processing'">
                    <el-button
                      plain
                      round
                      type="warning"
                      @click="download(output)"
                      v-if="output.hasOwnProperty('url') && output.url != ''"
                    >Download
                    </el-button
                    >
                    <el-button
                      plain
                      round
                      type="warning"
                      @click="bind(output)"
                      :class="{ bindClass: output.bind }"
                      v-if="output.hasOwnProperty('url') && output.url != ''"
                    >Bind
                    </el-button
                    >
                  </div>
                  <div
                    v-else-if="
                    type == 'Visualization' &&
                      output.hasOwnProperty('url') &&
                      output.url != ''
                  "
                  >
                    <div class="output-pic" @mouseenter="maskVisible">
                      <el-avatar
                        shape="square"
                        :size="80"
                        fit="fill"
                        :src="output.url"
                      ></el-avatar>
                    </div>
                    <div v-show="showMask" @mouseout="maskInvisible">
                      <div class="mask">
                        <i
                          class="el-icon-view mask-icon"
                          style="margin-left:20px"
                          @click="zoomOutPicDialog(output.url)"
                        />
                        <i
                          class="el-icon-download mask-icon"
                          @click="download(output)"
                        />
                      </div>
                    </div>

                    <!-- <img
                        src="https://bkimg.cdn.bcebos.com/pic/0e2442a7d933c89566f98647d91373f082020002?x-bce-process=image/resize,m_lfit,w_480,limit_1"
                        height="90"
                        width="200"
                      /> -->
                  </div>
                </div>
              </el-row>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-button
          type="primary"
          @click="invokeTest"
          style="float: right; width: 110px; margin-right: 15%"
        >
          <i class="el-icon-setting"></i>&nbsp;Invoke
        </el-button>
      </el-row>

      <el-dialog
        :visible.sync="selectDataDialogShow"
        width="1000px"
        title="Select data from Resource Center or Upload"
        :close-on-click-modal="false"
        :destroy-on-close="true"
      >
        <resource-list
          :pageParams="pageParams"
          @selectData="selectData"
        ></resource-list>
      </el-dialog>
      <el-dialog
        :visible.sync="zoomOutPicDialogShow"
        width="1000px"
        title="Output Picture"
        :close-on-click-modal="false"
        :destroy-on-close="true"
      >
        <img :src="zoomUrl" width="950"/>
      </el-dialog>
    </div>
  </div>
</template>

<script>
  import {get, post} from "@/axios";
  import resourceList from "../../common/resource/resourceList-copy";

  export default {
    components: {
      resourceList
    },
    data() {
      return {
        //toolId
        id: "",
        dataToken: "",
        type: "", ///Processing,Visualization,Data

        dataService: {},
        dataServiceDetail: {},
        record: {},
        selectDataDialogShow: false,
        url: "",
        /*
         page info
         pageId: aid
        */
        pageParams: {pageId: "", userId: "", userName: ""},
        inputIndex: "",
        input2: "",
        showMask: false,
        zoomOutPicDialogShow: false,
        zoomUrl: "",


        //zhngzhng
        toolId: "",
        aid: "",
        toolInfo: {}
      };
    },
    mounted() {
      this.toolId = window.frameElement.id;
      loadCollabComponent();
      this.initTool();
    },

    methods: {
      initTool: function () {
        this.initLoading();
        this.getStepInfo();
        this.getToolInfo();
        this.fullscreenLoading.close();
      },
      initLoading: function () {
        this.fullscreenLoading = this.$loading({
          lock: true,
          text: "Initialization",
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, .7)"
        });
      },
      getStepInfo: function () {
        if (componentStatus) {
          //读取信息
          this.aid = activityInfo.aid;
          this.userInfo = userInfo;
          this.pageParams.pageId = this.aid;
          this.pageParams.userId = userInfo.userId;
          this.pageParams.userName = userInfo.name;
          console.log(this.pageParams)

        } else {
          setTimeout(() => {
            this.getStepInfo();
          }, 1000)
        }
      },
      /*
      toolId 从iframe id 中获得
      不用等待其他内容加载完成

       */
      getToolInfo: function () {
        this.$axios.get("/GeoProblemSolving/tool/" + this.toolId)
          .then(res => {
            if (res.data.code == 0) {
              // this.$set(this, "toolInfo", res.data.data);
              /*
              获取到 toolInfo 读取token,查看服务是否在线
               */
              let tool = res.data.data;
              let token = tool.token;
              this.$axios.get("/GeoProblemSolving/dataContainer/" + token)
                .then(res => {
                  if (res.data.code == 0) {
                    //这里需要那么多参数吗
                    this.$set(this, "toolInfo", tool);
                    //数据处理方法 id
                    this.id = tool.dataMethodId;
                    this.dataToken = tool.token;
                    this.type = tool.dataMethodType;
                    this.dataService = tool.dataMethodMeta.data;
                  } else {
                    this.$Notice.error({title: "Fail", desc: "Loading tool fail, Node offline."});
                  }
                })
                .catch(err => {
                  this.$Notice.error({title: "Fail", desc: "Loading tool fail."});
                })
            }
          })
          .catch(err => {
            this.$Notice.error({title: "Fail", desc: "Loading tool fail."});
          })
      },

      async getRemoteData() {
        let json = {
          serverId: this.id,
          token: this.dataToken
        };
        let {id} = await post(`/GeoProblemSolving/dataContainer/remote`, json);
        let remoteDataList = id;
        let inputList = this.dataService.metaDetail.input;

        inputList.forEach(input => {
          console.log(remoteDataList);
          let selectIdUrl = remoteDataList.filter(
            el => el.file_name == input.name
          );
          input.url = selectIdUrl[0].url;
          input.urlName = input.name;
        });

        this.$forceUpdate();
      },

      async invokeTest() {
        let inputs = this.dataService.metaDetail.input;
        let urls = {};
        inputs.forEach((input)=>{
          urls[input.name] = input.url
        })

        let paramList = "";
        let params = this.dataService.metaDetail.parameter;
        for (let index = 0; index < params.length; index++) {
          paramList += params[index].value;
          if (index != params.length - 1) {
            paramList += ",";
          }
        }

        console.log("urlList", urls)
        console.log("paramList", paramList)

        sendDataOperation(this.aid, this.id, this.dataToken, urls, paramList, this.getSocketComputation)


        // let json = {
        //   token: this.dataToken,
        //   pcsId: this.id,
        //   urls: urlList,
        //   params: paramList
        // };
        // socket
        // sendDataOperation(this.aid, this.id, this.dataToken, urlList, paramList, this.getSocketComputation());

        // let data = await post(`/GeoProblemSolving/dataContainer/invoke`, json);
        // this.dataService.metaDetail.output[0].url = data;
        // this.$forceUpdate();
      },
      // webSocket 回调函数
      getSocketComputation: function (data) {
        console.log(this.dataService.metaDetail.outputs)
        let outputs =  data.computeOutputs;
        let output = {};
        let keys = Object.keys(outputs);
        let values = Object.values(outputs);
        for (let i = 0; i < keys.length-1; i++){
          // this.dataService.metaDetail.output[0].url = data;
          this.$set(this.dataService.metaDetail.output[i], "url", values[i]);
          // this.dataService.metaDetail.output[i].url = values[i];
        }
        console.log(this.dataService.metaDetail.output)
      },

      async bind(event) {
        // await this.dataURItoBlob(event);
        let json = {
          name: event.name,
          description: event.description,
          type: "data",
          userUpload: false,
          address: event.url,
          uploaderId: this.pageParams.userId,
          uploaderName: this.pageParams.userName,
          privacy: "private"
        };


        let data = await post("/GeoProblemSolving/rip/file/bind/" + this.pageParams.pageId, json);

        // let data = await post("/GeoProblemSolving/resource/bind", json);
        event.bind = true;
        this.$forceUpdate();
        this.$message({
          message: "You have bind to the resource center successfully!",
          type: "success"
        });
      },

      selectDataDialog(val) {
        this.inputIndex = val;
        this.selectDataDialogShow = true;
      },

      selectData(val) {
        let index = this.inputIndex;
        this.dataService.metaDetail.input[index].url = val.address;
        this.dataService.metaDetail.input[index].urlName = val.name;
        this.selectDataDialogShow = false;
      },

      download(event) {
        window.open(event.url);
      },
      remove(event) {
        event.url == "";
        event.urlName = "";
      },
      maskVisible() {
        this.showMask = true;
      },
      maskInvisible() {
        this.showMask = false;
      },
      zoomOutPicDialog(url) {
        this.zoomUrl = url;
        this.zoomOutPicDialogShow = true;
      }
    },

  };
</script>

<style scoped>
  .main {
    position: relative;
  }

  .state-name {
    line-height: 2;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }

  .state-desc {
    margin: 0px 0px 15px 0px;
    padding: 4px 0px;
    line-height: 2;
    background-color: #f3f3f3;
    font-size: 16px;
    font-style: italic;
  }

  .el-tabs__item {
    font-size: 16px;
  }

  .el-tabs__item:hover {
    color: #00bbd8;
    background-color: #b5dce244;
  }

  .el-tabs__item.is-active {
    color: #00bbd8;
  }

  .el-tabs__active-bar {
    background-color: #00bbd8;
  }

  .leftContainer {
    background-color: rgba(142, 200, 255, 0.2);
    border-radius: 5px;
    box-shadow: 0px 0px 4px rgb(203, 207, 212);
    padding: 20px 0;
  }

  .modelState {
    color: rgb(37, 44, 66);
    font-size: 18px;
    font-family: "微软雅黑";
    margin: 1% 0;
  }

  .stateTitle {
    font-size: 20px;
    font-weight: 600;
    color: rgb(87, 173, 253);
    font-style: italic;
  }

  .stateTitleDivider.el-divider--horizontal {
    height: 2px;
    margin: 10px 0;
  }

  .stateTitleDivider.el-divider {
    background-color: rgb(140, 144, 148);
  }

  .event {
    padding: 15px 0 0 50px;
  }

  .event:hover {
    background-color: #c4d9f734;
  }

  .event_name {
    font-size: 16px;
    font-weight: 600;
    /* padding: 10px 0; */
  }

  .event_desc {
    font-size: 14px;
    font-style: italic;
    margin: 10px 0;
    color: rgb(94, 94, 94);
    word-wrap: break-word;
  }

  .eventDivider.el-divider--horizontal {
    margin: 10px 0;
  }

  .des {
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    /* !autoprefixer: off */
    -webkit-box-orient: vertical;
    font-size: 14px;
  }

  .title {
    font-weight: 600;
    font-size: 20px;
    margin: 8px 0 10px 0;
  }

  .data-name {
    float: left;
    width: 200px;
    font-weight: 600;
    font-size: 16px;
  }

  .select-data-line {
    margin: 0 0 0 0;
    line-height: 40px;
  }

  .select-data {
    background-color: #f0f9eb;
    display: inline-block;
    height: 40px;
    padding: 0 10px;

    font-size: 12px;
    color: #3a701f;
    border: 1px solid #e1f3d8;
    border-radius: 4px;
    box-sizing: border-box;
    white-space: nowrap;
  }

  .select-data:hover {
    cursor: pointer;
    transition: all 0.2s ease-out;
    background-color: #cffab8;
  }

  .bindClass {
    background-color: rgb(255 231 231 / 72%);
    size: 20px;
    border-color: #8f2727;
    color: #8f2727;
  }

  .left-divider /deep/ .el-divider {
    background-color: #27298f;
  }

  .el-card__body {
    padding: 0;
  }

  .output-pic {
    position: absolute;
    right: 18%;
  }

  .mask {
    position: absolute;
    width: 80px;
    height: 80px;
    top: 0;
    backdrop-filter: blur(5px);
    background: rgba(230, 230, 230, 0.3);
    right: 18%;
  }

  /* .mask:hover {
    top: 0;
  } */
  .mask-icon {
    font-size: 20px;
    color: white;
    text-align: center;
    vertical-align: middle;
    line-height: 80px;
    font-weight: 600;
  }

  .mask:hover {
    cursor: pointer;
  }
</style>
