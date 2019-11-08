<style scoped>
.modelToolBtn {
  height: 60px;
  width: 60px;
  float: left;
  padding: 0;
  color: #00c0ff;
}
.modelToolsetBtn {
  height: 60px;
  width: 60px;
  float: left;
  padding: 0;
  color: #19be6b;
}
.ellipsis{
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  vertical-align: top;
}
</style>
<template>
  <div>
    <div>
      <div style="width:3px;height:18px;float:left;background-color:rgb(124, 126, 126)"></div>
      <h4 style="float:left;margin-left:5px">Toolbox</h4>
      <div style="float:right;" title="Manage tools and toolsets">
        <manage-tools :step-info="stepInfo" @updateStepTools="stepToolListChanged" :key="toolModal"></manage-tools>
      </div>
    </div>
    <div :style="{height:contentHeight-66+'px'}">
      <vue-scroll :ops="ops">
        <Row>
          <Col span="8" v-for="toolset in toolsetList" :key="toolset.index" style="margin-top:15px">
            <Card style="background-color: #3f51b530;margin: 0 5px 10px 5px">
              <div style="text-align:center">
                <Tooltip placement="bottom" max-width="600">
                  <img :src="toolset.toolsetImg" v-if="toolset.toolsetImg!=''" style="height:100%;max-height:50px;">
                  <avatar
                  :username="toolset.toolsetName"
                  :size="50"
                  style="margin-bottom:6px"
                  v-else
                ></avatar>
                <div slot="content">
                  <span>{{toolset.description}}</span>
                  <br v-if="toolset.categoryTag.length>0"/>
                  <p><i>{{toolset.categoryTag.join(',')}}</i></p>
                </div>
                </Tooltip>
                <h4 :title="toolset.toolsetName" style="width:75px;" class="ellipsis">{{toolset.toolsetName}}</h4>
              </div>
            </Card>
          </Col>
          <Col span="8" v-for="tool in toolList" :key="tool.index"  style="margin-top:15px">
            <Card style="background-color: ghostwhite;margin: 0 5px 10px 5px">
              <div style="text-align:center">
                <Tooltip placement="bottom" max-width="600">
                  <img :src="tool.toolImg" v-if="tool.toolImg!=''" style="height:100%;max-height:50px;">
                  <avatar
                  :username="tool.toolName"
                  :size="50"
                  style="margin-bottom:6px"
                  v-else
                ></avatar>
                <div slot="content">
                  <span>{{tool.description}}</span>
                  <br v-if="tool.categoryTag.length>0"/>
                  <span><i>{{tool.categoryTag.join(',')}}</i></span>
                </div>
                </Tooltip>
                <h4 :title="tool.toolName" style="display:block;width:90px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">{{tool.toolName}}</h4>
              </div>
            </Card>
          </Col>
        </Row>
      </vue-scroll>
    </div>
  </div>
</template>
<script>
import manageTools from "./../../tools/toolToStepModal";
import Avatar from "vue-avatar";
export default {
  props: ["stepInfo", "contentHeight", "userRole"],
  components: {
    manageTools,
    Avatar
  },
  data() {
    return {
      ops: {
        bar: {
          background: "#808695"
        }
      },
      toolList: [],
      toolsetList: [],
      toolModal:0
    };
  },
  mounted() {
    this.getAllTools(this.stepInfo.toolList,this.stepInfo.toolsetList);
  },
  methods: {
    getAllTools(toolIds,toolsetIds) {
      this.getToolInfos(toolIds);
      this.getToolsetInfos(toolsetIds);
    },
    getToolInfos(toolIds){
      var toolsCount = toolIds.length;
      var flagCount = toolsCount;
      var ToolInfos = [];
      for(var i=0;i<toolsCount;i++){
        this.axios
          .get(
            "/GeoProblemSolving/tool/inquiry" +
              "?key="+"tId" +
              "&value="+toolIds[i]
          )
          .then(res => {
            if (res.data == "Offline") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (res.data === "Fail") {
              this.$Notice.error({ desc: "Loading tools fail." });
            } else if (res.data === "None") {
              this.$Notice.error({ desc: "There is no existing tool" });
            } else {
              ToolInfos.push(res.data[0]);
              if(--flagCount<1){
                var sortTools = [];
                for(var j=0;j<toolsCount;j++){
                  for(var k=0;k<toolsCount;k++){
                    if(toolIds[j]==ToolInfos[k].tId){
                      sortTools.push(ToolInfos[k]);
                      break;
                    }
                  }
                }
                this.$set(this, "toolList", sortTools);
              }
            }
          })
          .catch(err => {
            console.log(err);
          });
      }
    },
    getToolsetInfos(toolsetIds){
      var toolsetsCount = toolsetIds.length;
      var flagCount = toolsetsCount;
      var toolsetInfos = [];
      for(var i=0;i<toolsetsCount;i++){
        this.axios
          .get(
            "/GeoProblemSolving/toolset/inquiry" +
              "?key="+"tsId" +
              "&value="+toolsetIds[i]
          )
          .then(res => {
            if (res.data == "Offline") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (res.data === "Fail") {
              this.$Notice.error({ desc: "Loading toolsets fail." });
            } else if (res.data === "None") {
              this.$Notice.error({ desc: "There is no existing toolset" });
            } else {
              toolsetInfos.push(res.data[0]);
              if(--flagCount<1){
                var sortToolsets = [];
                for(var j=0;j<toolsetsCount;j++){
                  for(var k=0;k<toolsetsCount;k++){
                    if(toolsetIds[j]==toolsetInfos[k].tsId){
                      sortToolsets.push(toolsetInfos[k]);
                      break;
                    }
                  }
                }
                this.$set(this, "toolsetList", sortToolsets);
              }
            }
          })
          .catch(err => {
            console.log(err);
          });
      }
    },
    stepToolListChanged(tools,toolsets){
      this.stepInfo.toolList = tools;
      this.stepInfo.toolsetList = toolsets;
      this.toolModal++;
      this.getAllTools(tools,toolsets);
    }
  }
};
</script>