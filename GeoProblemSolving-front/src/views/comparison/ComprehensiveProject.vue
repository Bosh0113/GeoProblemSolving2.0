<template>
  <div class="projectContent">
    <div class="projectInfo">
      <div class="basicInfo">
        <h3 class="cmpTitle" style="text-align:center">{{projectInfo.title}}</h3>
        <span class="infoName">Description:</span>
        <p>{{projectInfo.description}}</p>
        <div class="members">
          <span class="infoName">Members:</span>
          <span class="member">{{projectInfo.managerName}}</span>
          <span class="member" v-for="member of projectInfo.members">{{member}}</span>
        </div>
        <div class="createTime">
          <span>{{getCreatedTime(projectInfo)}}</span><span>Created Time： </span>
        </div>
      </div>

      <div class="cmpItemContent" v-show="cmpItems.length>0">
        <span class="infoName">Comparison Items:</span>
        <div class="cmpItemBox">
          <Row>
            <Col :xs="{ span: 21, offset: 1 }" :md="{ span: 11, offset: 1 }" :lg="{ span: 6 }">
              <div @click="createCmpItem">
                <Card style="height:180px;margin:10px -15px">
                  <div style="display:flex; justify-content: center;  height: 150px; align-items: center;">
                    <img style="width:80px" src="@/assets/images/comparison/add.png" alt="add comparison item">
                  </div>
                </Card>
              </div>
            </Col>

            <Col :xs="{ span: 21, offset: 1 }" :md="{ span: 11, offset: 1 }" :lg="{ span: 6 }" v-for="item of cmpItems"
              :key="item.itemId">
            <Card style="height:180px;margin:10px -15px">
              <div>
                <div class="cmpItemTitle">
                  <a href="#" @click.prevent="itemDetail(item)">{{item.title}}</a>
                </div>
                <p class="cmpItemDesc">{{item.description}}</p>
                <div id="bottom-info">
                  <div class="info">
                    <Icon type="md-body" :size="15" />Manager
                    <span style="margin-left:10px; color:#2b85e4">{{item.managerName}}</span>
                  </div>
                  <div class="info">
                    <Icon type="md-clock" :size="15" />Time
                    <span style="margin-left:10px">{{getCreatedTime(item)}}</span>
                  </div>
                </div>
              </div>
            </Card>
            </Col>
          </Row>
        </div>
      </div>

      <div class="comparisonContent" v-show="cmpItems.length==0">
        <blank-box v-bind="blankInfo" v-on:linkClicked="onLinkClick"></blank-box>
      </div>
    </div>
  </div>
</template>
<script>
import BlankBox from "@/components/comparison/BlankBox";
export default {
  name: "comprehensive-project",
  components: {
    "blank-box": BlankBox
  },
  created: function() {
    //* 获取项目信息
    this.getProjectInfo();
  },
  data() {
    return {
      animated: false,
      projectInfo: {},
      blankInfo: {
        welcomeTitle: "Welcome to Comparison!",
        welcomeInfo:
          "Comprehensive comparison of simulation capabilities from multiple perspectives improving our knowledge and understanding of models. To get started, you should",
        linkInfo: "create an comparison item."
      },
      cmpItems: []
    };
  },
  computed: {
    getCreatedTime() {
      return function(project) {
        return project.createTime ? project.createTime.split(" ")[0] : "";
      };
    }
  },
  methods: {
    getProjectInfo() {
      this.projectInfo = this.$store.state.comparison.projectInfo;
      // if (JSON.stringify(this.projectInfo) === "{}") {
      //* 请求后台数据
      let projectId = this.$route.params.id;
      this.$api.cmp_project
        .getProject("projectId", projectId)
        .then(res => {
          console.log(res);
          this.projectInfo = res[0];
          //todo 请求子项目数据
          let cmpItemIds = this.projectInfo.cmpItemIds;
          if (cmpItemIds && cmpItemIds.length > 0) {
            this.$api.cmp_item
              .getItemsByIdList(cmpItemIds)
              .then(res => {
                this.cmpItems = res;
              })
              .catch(err => {
                this.$Message.error(err);
              });
          }
        })
        .catch(err => {
          this.$Message.error(err);
        });
      // }
    },
    onLinkClick() {
      // console.log("11")
      if (!this.$store.getters.userState) {
        this.$router.push({ name: "Login" });
      } else {
        this.$router.push({
          path: `/create-cmp-item/${this.projectInfo.projectId}`
        });
      }
    },
    createCmpItem() {
      console.log("111");
      this.onLinkClick();
    },
    itemDetail(item) {
      this.$router.push({ path: `/cmp-item/${item.itemId}` });
    }
    //todo 1.基本信息编辑功能
    //todo 2.模型信息编辑功能
    //todo 3.数据信息编辑功能
    //todo 4.资源
  }
};
</script>
<style scoped>
.projectContent {
  display: flex;
  flex-wrap: wrap;
  /* justify-content: center; */
  padding: auto;
  background-color: #fff;
}

.projectInfo {
  /* border-bottom: 1px solid #d1d5da; */
  border-top: 1px solid #d1d5da;
  padding-left: 20px;
  padding-right: 20px;
  width: 1200px;
  margin-left: auto;
  margin-right: auto;
  /* margin-top: 10px; */
  background-color: #fff;
}

.basicInfo {
  border-bottom: 1px solid #d1d5da;
}

.cmpTitle {
  color: #0366d6;
  text-decoration: none;
  font-weight: 600;
  margin-top: 10px;
  margin-bottom: 10px;
  font-size: 20px;
}

.infoName {
  color: #0366d6;
  font-size: 16px;
}

.basicInfo p {
  margin-left: 20px;
  margin-top: 5px;
  margin-bottom: 5px;
  font-size: 16px;
}

.members {
  display: flex;
  width: 100%;
  flex-wrap: wrap;
  margin-top: 5px;
}

.member {
  font-size: 12px;
  border: 1px solid #d1d5da;
  border-radius: 8px;
  padding: 3px;
  margin-right: 5px;
  margin-left: 5px;
  margin-bottom: 5px;
}

.constrainsContent {
  display: flex;
  margin-top: 10px;
  margin-bottom: 10px;
  border-bottom: 1px solid #d1d5da;
}

.resourceContent {
  flex: 3 0 auto;
  /* flex: auto; */
  border-right: 1px solid #d1d5da;
  margin-top: 5px;
  margin-bottom: 5px;
  margin-right: 10px;
}

.constrainsInfo {
  flex: 1 0 auto;
}

.constrain_tb_name {
  width: 45%;
  font-size: 14px;
}

.constrain_tb_value {
  width: 50%;
  font-size: 14px;
}

.box {
  background-color: #fff;
  border: 1px solid #d1d5da;
  border-radius: 3px;
}

.blankslate {
  padding: 80px 40px;
  background-color: #fafbfc;
  border-radius: 3px;
  box-shadow: inset 0 0 10px rgba(27, 31, 35, 0.05);
  text-align: center;
}

.blankslate-icon {
  color: #a3aab1;
  margin-bottom: 8px;
  margin-left: 4px;
  margin-right: 4px;
  vertical-align: text-bottom;
  display: inline-block;
  fill: currentColor;
}

.blankslate h3 {
  font-size: 20px;
  margin: 16px 0;
  font-weight: 600;
}

.blankslate a {
  color: #0366d6;
  text-decoration: none;
}

.blankslate p {
  font-size: 16px;
  margin: 0px 50px;
}

.comparisonContent {
  margin-bottom: 10px;
  margin-top: 10px;
}

.createTime {
  display: flex;
  flex-direction: row-reverse;
}

.createTime span {
  color: #586069;
  font-size: 14px;
  line-height: 20px;
}

.cmpItemTitle {
  height: 3em;
  overflow: hidden;
  /* text-overflow: ellipsis; */
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
.cmpItemTitle a {
  color: #0366d6;
  text-decoration: none;
  font-weight: 600;
  font-size: 18px;
  line-height: 1em;
}

.cmpItemDesc {
  font-size: 14px;
  height: 80px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
}

#bottom-info {
  display: flex;
  margin-top: 5px;
  justify-content: space-between;
}

.info {
  display: flex;
  align-items: center;
  margin-right: 10px;
}

.cmpItemContent {
  margin-top: 10px;
}
</style>