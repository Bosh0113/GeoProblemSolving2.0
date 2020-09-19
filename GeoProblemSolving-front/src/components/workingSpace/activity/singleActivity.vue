<template>
  <div>
    <div style="display:flex; background-color: #f8f8f9;">
      <div
        style="width:250px;margin-right: 5px; border-right: 1px solid #dcdee2;; background-color: white;"
      >
        <vue-scroll :ops="scrollOps" style="height:calc(100vh - 70px)">
          <div style="padding:5px 10px;">
            <h3 style="display:inline-block">Name:</h3>
            <p style="font-size: medium; padding-top:5px">{{activityInfo.name}}</p>
            <Divider style="margin:10px 0" />
            <h3 style="display:inline-block">Purpose:</h3>
            <p style="font-size: medium; padding-top:5px">{{activityInfo.purpose}}</p>
            <Divider style="margin:10px 0" />
            <h3>Description:</h3>
            <p
              style="font-size: larger;white-space: pre-line;word-break: break-word; padding-top:5px"
            >{{activityInfo.description}}</p>
            <Divider style="margin:10px 0" />
            <h3>Members:</h3>
            <Card
              v-for="(member, index) in participants"
              :key="'online' + index"
              style="margin:5px 0"
              :padding="5"
            >
              <div
                style="display:flex;align-items:center;cursor:pointer"
                @click="gotoPersonalSpace(member.userId)"
              >
                <div class="memberImg" style="position:relative">
                  <img
                    v-if="member.avatar != '' && member.avatar!='undefined'"
                    :src="member.avatar"
                    style="width:40px;height:40px"
                  />
                  <avatar v-else :username="member.userName" :size="40" :rounded="false"></avatar>
                  <div class="onlinecircle"></div>
                </div>
                <div class="memberDetail">
                  <div class="memberName">
                    <span>{{member.name}}</span>
                  </div>
                  <div class="memberRole">
                    <span>{{member.role}}</span>
                  </div>
                </div>
              </div>
            </Card>
            <Divider style="margin:10px 0" />
          </div>
        </vue-scroll>
      </div>
      <div style="flex:1; border-left: 1px solid #dcdee2; background-color: white;">
        <Menu
          mode="horizontal"
          :active-name="activeMenu"
          style="height: 45px; line-height: 45px;"
          @on-select="changeMenuItem"
        >
          <MenuItem name="Workspace">
            <Icon type="ios-paper" />Workspace
          </MenuItem>
          <MenuItem name="Task">
            <Icon type="ios-git-network" />Task management
          </MenuItem>
        </Menu>

        <div v-show="activeMenu=='Workspace'">
          <vue-scroll :ops="scrollOps" style="height:calc(100vh - 120px); margin-top:5px">
            <context-res
              v-if="activityInfo.purpose=='Context definition & resource collection'"
              :activityInfo="activityInfo"
              :participants="participants"
            ></context-res>
            <data-processing
              v-else-if="activityInfo.purpose=='Data processing'"
              :activityInfo="activityInfo"
              :participants="participants"
            ></data-processing>
            <data-visual
              v-else-if="activityInfo.purpose=='Data visualization'"
              :activityInfo="activityInfo"
              :participants="participants"
            ></data-visual>
            <model-build
              v-else-if="activityInfo.purpose=='Geographic model construction'"
              :activityInfo="activityInfo"
              :participants="participants"
            ></model-build>
            <model-evaluation
              v-else-if="activityInfo.purpose=='Model effectiveness evaluation'"
              :activityInfo="activityInfo"
              :participants="participants"
            ></model-evaluation>
            <geo-simulation
              v-else-if="activityInfo.purpose=='Geographical simulation'"
              :activityInfo="activityInfo"
              :participants="participants"
            ></geo-simulation>
            <geo-analysis
              v-else-if="activityInfo.purpose=='Quantitative and qualitative analyses'"
              :activityInfo="activityInfo"
              :participants="participants"
            ></geo-analysis>
            <decision-making
              v-else-if="activityInfo.purpose=='Decision-making and management'"
              :activityInfo="activityInfo"
              :participants="participants"
            ></decision-making>
          </vue-scroll>
        </div>
        <div v-show="activeMenu=='Task'">
          <task-manager :activityInfo="activityInfo"></task-manager>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import taskManager from "./utils/taskManger.vue";
import contextRes from "./funcs/contextAndResource.vue";
import dataProcessing from "./funcs/dataProcessing.vue";
import dataVisual from "./funcs/dataVisualization.vue";
import modelBuild from "./funcs/modelBuild.vue";
import modelEvaluation from "./funcs/modelEvaluation.vue";
import geoSimulation from "./funcs/geoSimulation.vue";
import geoAnalysis from "./funcs/geoAnalysis.vue";
import decisionMaking from "./funcs/decisionMaking.vue";
export default {
  props: ["activityInfo"],
  components: {
    taskManager,
    contextRes,
    dataProcessing,
    dataVisual,
    modelBuild,
    modelEvaluation,
    geoSimulation,
    geoAnalysis,
    decisionMaking,
  },
  data() {
    return {
      scrollOps: {
        bar: {
          background: "lightgrey",
        },
      },
      activeMenu: "Workspace",
      userInfo: JSON.parse(sessionStorage.getItem("userInfo")),
      contentHeight: 560,
      participants: [
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
      ],
    };
  },
  mounted() {},
  methods: {
    gotoPersonalSpace(id) {
      if (id == this.$store.getters.userId) {
        this.$router.push({ name: "PersonalPage" });
      } else {
        this.$router.push({ name: "MemberDetailPage", params: { id: id } });
      }
    },
    changeMenuItem(name) {
      this.activeMenu = name;
    },
  },
};
</script>
<style scoped>
.memberImg {
  width: 40px;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.memberName {
  height: 20px;
  padding: 0px 10px;
  width: 100%;
}
.memberDetail {
  height: 100%;
  width: 100%;
  overflow: hidden;
}
.memberRole {
  height: 20px;
  padding: 0px 10px;
  width: 100%;
}
.memberName span {
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.memberRole span {
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 50%;
}
</style>