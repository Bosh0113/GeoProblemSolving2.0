<template>
  <div class="basicInfo">
    <h3 class="cmpTitle">{{projectTitle}} <span v-show="projectTitle!==''&&subprojectTitle&&subprojectTitle!==''">——</span> <span v-show="subprojectTitle&&subprojectTitle!==''" style="color:#515a6e">{{subprojectTitle}}</span></h3>
    <!-- <h3 class="subTitle">{{subprojectTitle}}</h3> -->
    <span class="infoName">Description:</span>
    <p>{{description}}</p>
    <span class="infoName">Evaluation Rules:</span>
    <p>{{rules}}</p>

    <div class="members">
      <span class="infoName">Members:</span>
      <span class="member">{{managerName}}</span>
      <span class="member" v-for="member of members">{{member}}</span>
    </div>
    <div class="createTime">
      <span>{{getCreatedTime(createTime)}}</span><span>Created Time： </span>
    </div>
  </div>
</template>
<script>
export default {
  name: "basic-info",
  data() {
    return {};
  },
  props: {
    projectTitle: {
      type: String,
      required: true
    },
    subprojectTitle: {
      type: String,
      required: false
    },
    description: {
      type: String,
      required: true
    },
    rules: {
      type: String,
      required: true
    },
    managerName: {
      type: String,
      required: true
    },
    members: {
      type: Array,
      required: true
    },
    createTime: {
      type: String,
      required: true
    }
  },
  computed: {
    getCreatedTime() {
      return function(createTime) {
        return createTime.split(" ")[0];
      };
    }
  }
};
</script>
<style scoped>

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
  text-align:center
}
.subTitle{

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

.createTime {
    display: flex;
    flex-direction: row-reverse;
}

.createTime span {
    color: #586069;
    font-size: 14px;
    line-height: 20px;
}
</style>