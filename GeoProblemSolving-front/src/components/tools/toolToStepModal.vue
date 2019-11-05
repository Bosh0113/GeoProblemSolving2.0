<style scoped>
/* import { try } from 'q'; */
.selector {
  width: 250px;
}
.fileBtnHoverGreen:hover {
  background-color: #19be6b;
  color: white;
}
.fileBtnHoverGray:hover {
  background-color: #808695;
  color: white;
}
.leftMenuItem {
  margin: 0 0 10px 0;
}
.changeGreenColor:hover{
    background-color: #19be6b;
    color: white;
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
      <Button shape="circle" icon="md-cog" @click="stepToolModalShow"></Button>
      <Modal v-model="stepToolModal" title="Manage toolset and tools" width="800" :mask-closable="false">
        <Row>
          <Col span="16">
          <div span="2" style="height: inherit;width: 90px;position: absolute;">
            <Menu
              active-name="allToolsets"
              @on-select="changeMenuItem"
              style="height: inherit;width: fit-content;z-index:1"
            >
              <MenuItem name="allToolsets" class="leftMenuItem">
                <Tooltip content="All toolsets" placement="right">
                <Icon type="ios-briefcase" size="25"></Icon>
                </Tooltip>
              </MenuItem>
              <MenuItem name="allTools" class="leftMenuItem">
                <Tooltip content="All tools" placement="right">
                <Icon type="ios-hammer" size="25"></Icon>
                </Tooltip>
              </MenuItem>
            </Menu>
          </div>
            <Card dis-hover style="margin-left: 80px">
              <h2 slot="title" style="padding-top:5px;color: #2d8cf099" v-if="isPublic&&showMenuItem=='allToolsets'">Public toolsets</h2>
              <h2 slot="title" style="padding-top:5px;color: #2d8cf099" v-if="isPublic&&showMenuItem=='allTools'">Public tools</h2>
              <h2 slot="title" style="padding-top:5px;color: #2d8cf099" v-if="!isPublic&&showMenuItem=='allToolsets'">Personal toolsets</h2>
              <h2 slot="title" style="padding-top:5px;color: #2d8cf099" v-if="!isPublic&&showMenuItem=='allTools'">Personal tools</h2>
              <div slot="extra">
                <i-switch v-model="isPublic">
                  <Icon type="logo-dropbox" slot="open" title="Public"></Icon>
                  <Icon type="ios-cube" slot="close" title="Personal"></Icon>
                </i-switch>
              </div>
              <div v-if="isPublic&&showMenuItem=='allToolsets'" style="height: 400px;">
                  <vue-scroll :ops="ops" style="height:400px;">
                  <Row>
                    <Col span="8" v-for="toolset in publicToolsets" :key="toolset.index">
                    <Card style="background-color: ghostwhite;margin: 0 5px 10px 5px">
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
                        <h4 :title="toolset.toolsetName" style="display:block;width:90px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">{{toolset.toolsetName}}</h4>
                      </div>
                    </Card>
                    </Col>
                  </Row>
                  </vue-scroll>
              </div>
              <div v-if="!isPublic&&showMenuItem=='allToolsets'" style="height: 400px;">
                  <vue-scroll :ops="ops" style="height:400px;">
                  <Row>
                    <Col span="8" v-for="toolset in personalToolsets" :key="toolset.index">
                    <Card style="background-color: #faebd794;margin: 0 5px 10px 5px">
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
                        <h4 :title="toolset.toolsetName" style="width:90px;" class="ellipsis">{{toolset.toolsetName}}</h4>
                      </div>
                    </Card>
                    </Col>
                  </Row>
                  </vue-scroll>
              </div>
              <div v-if="isPublic&&showMenuItem=='allTools'" style="height: 400px;">
                  <h3>All Public tools</h3>
              </div>
              <div v-if="!isPublic&&showMenuItem=='allTools'" style="height: 400px;">
                  <h3>All Personal tools</h3>
              </div>
            </Card>
          </Col>
          <Col span="8">
            <div style="padding: 0 5px;margin-left: 15px;">
              <Card dis-hover>
                <h2 slot="title" style="padding-top:5px" v-if="showMenuItem=='allToolsets'">Toolsets in step</h2>
                <h2 slot="title" style="padding-top:5px" v-if="showMenuItem=='allTools'">Tools in step</h2>
                <div style="height: 400px;" v-if="showMenuItem=='allToolsets'">
                  <Card v-for="toolset in stepToolsetsShow" :key="toolset.index">
                    <div class="ellipsis" style="width: 100px;">{{toolset.toolsetName}}</div>
                  </Card>
                </div>
                <div style="height: 400px;" v-if="showMenuItem=='allTools'">
                  <h1>Tools List</h1>
                </div>
              </Card>
            </div>
          </Col>
        </Row>
      <div slot="footer">
        <Button @click="stepToolModal=false">Cancel</Button>
        <Button
          type="success"
          @click="confirmToolset()"
        >OK</Button>
      </div>
      </Modal>
  </div>
</template>
<script>
import Avatar from "vue-avatar";
export default {
  components: {
    Avatar
  },
  mounted() {
    this.resizeContent();
    this.getPublicToolsets();
    this.getPersonalToolsets();
    this.getPersonalTools();
    this.getStepToolsets();
  },
  data() {
    return {
      stepToolsets:["168ed2bb-895e-4085-9bf0-3f3a6684200d","c1ba9d27-616e-44cf-b887-d278a42f2a49","380d9f07-695a-48c3-9f06-e6bd925ee5a0"],
      stepTools:[],
      stepToolsetsShow:[],
      stepToolsShow:[],
      contentHeight: "",
      userInfo: this.$store.getters.userInfo,
      showMenuItem: "allToolsets",
      publicToolsets:[],
      personalToolsets:[],
      publicTools:[],
      personalTools:[],
      stepToolModal:false,
      isPublic:true,
      ops: {
        bar: {
          background: "#808695"
        }
      },
    };
  },
  methods: {
    resizeContent() {
      if (window.innerHeight > 675) {
        this.contentHeight = window.innerHeight - 120;
      } else {
        this.contentHeight = 555;
      }
      window.onresize = () => {
        return (() => {
          this.resizeContent();
        })();
      };
    },
    stepToolModalShow(){
      this.stepToolModal = true;
    },
    getStepToolsets(){
      var stepToolsetIds = this.stepToolsets;
      var toolsetsCount = this.stepToolsets.length;
      var flagCount = toolsetsCount;
      var stepToolsetInfos = [];
      for(var i=0;i<toolsetsCount;i++){
        this.axios
          .get(
            "/GeoProblemSolving/toolset/inquiry" +
              "?key="+"tsId" +
              "&value="+stepToolsetIds[i]
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
              stepToolsetInfos.push(res.data[0]);
              if(--flagCount<1){
                var sortToolsets = [];
                for(var j=0;j<toolsetsCount;j++){
                  for(var k=0;k<toolsetsCount;k++){
                    if(stepToolsetIds[j]==stepToolsetInfos[k].tsId){
                      sortToolsets.push(stepToolsetInfos[k]);
                      break;
                    }
                  }
                }
                this.$set(this, "stepToolsetsShow", sortToolsets);
              }
            }
          })
          .catch(err => {
            console.log(err);
          });
      }
    },
    getPublicToolsets() {
      this.axios
        .get(
          "/GeoProblemSolving/toolset/inquiry" +
            "?key="+"privacy" +
            "&value="+"Public"
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
            this.$set(this, "publicToolsets", res.data);
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    getPersonalToolsets() {
      this.axios
        .get(
          "/GeoProblemSolving/toolset/inquiryAll" +
            "?provider=" +
            this.userInfo.userId
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
            this.$set(this, "personalToolsets", res.data);
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    getPersonalTools() {
      this.axios
        .get(
          "/GeoProblemSolving/tool/inquiryAll" +
            "?provider=" +
            this.userInfo.userId
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data === "Fail") {
            this.$Notice.error({ desc: "Loading tool fail." });
          } else if (res.data === "None") {
            this.$Notice.error({ desc: "There is no existing tool" });
          } else {
            console.log(res.data);
            this.$set(this, "personalTools", res.data);
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    changeMenuItem(name) {
      this.showMenuItem = name;
    },
    confirmToolset(){
      console.log('update toolset.');
      this.stepToolModal = false;
    }
  }
};
</script>
