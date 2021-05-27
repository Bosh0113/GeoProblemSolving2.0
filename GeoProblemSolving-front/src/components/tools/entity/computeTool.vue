<template>
<!--  全部改用 iview 来做-->
  <div>
    <div id="collab-tool-head"></div>
    <div id="collab-tool-sidebar"></div>
    <div id="collab-tool-content" class="scrollbar">

    </div>

    <Modal v-model="selectDataModal" title="File Info">
<!--      <resource-list-->
<!--        :activityResource="pageParams"-->
<!--        @selectData="selectData"-->
<!--      ></resource-list>-->
    </Modal>
  </div>
</template>

<script>
  import resourceList from "../../common/resource/resourceList-copy";
  export default {
    components: {
      resourceList
    },
    data() {
      return {
        toolId: '',
        toolInfo: "",
        toolBackendType: "",
        dataMethodMeta: {},
        selectDataModal: false,
        activityResource: []
      }
    },
    mounted() {
      this.toolId = window.frameElement.id;
      loadCollabComponent();
      this.loadToolInfo();
      this.activityResource = resources;
      console.log("acResources", resources)
    },
    methods: {
      loadToolInfo: function () {
        this.$axios.get("/GeoProblemSolving/tool/" + this.toolId)
          .then(res => {
            if (res.data.code == 0) {
              this.toolInfo = res.data.data;
              this.toolBackendType = this.toolInfo.backendType;
              this.dataMethodMeta = this.toolInfo.dataMethodMeta.data;
              console.log(this.dataMethodMeta)
            } else {
              this.$Notice.error({title: "Fail", desc: "Loading compute tool fail."})
            }
          })
          .catch(err => {
            this.$Notice.error({title: "Fail", desc: "Loading compute tool fail."})
          })
      },

    }
  }
</script>

<style scoped>
  .main {
    position: relative;
    padding: 20px;
    min-width: 1200px;
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
  .left-divider /deep/.el-divider {
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
