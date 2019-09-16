<template>

  <div class="modelContent">
    <div class="topContent">
      <div class="modelInfo">
        <div class="leftInfo">
          <div id="titleInfo">
            <h1 class="public">
              <span style="display:flex">
                <Icon type="md-planet" class="titleIcon" color="#959da5" size="22" />
                <span id="projectName"><a href="#">{{projectTitle}}</a></span>
                <span style="margin:0 .25em">/</span>
                <strong><a href="#">{{modelInfo.modelName}}</a></strong>
              </span>
              <span class="fromFlag">
                <span>
                  upload by <a href="#">{{modelInfo.ownerName}}</a>
                </span>
                <span>{{getCreatedTime(modelInfo)}}</span>
              </span>
            </h1>
          </div>
          <div id="desc">
            <h3>Description:</h3>
            <p>{{modelInfo.description}}</p>
          </div>
        </div>
        <div class="rightInfo">
          <Tabs class="tabContent" value="MetaInfo" :animated="false">
            <Button @click="runModel" slot="extra">Run Model</Button>
            <TabPane label="Meta Info" name="MetaInfo">Meta Info</TabPane>
            <TabPane label="Input Data" name="InputData">Input Data</TabPane>
            <TabPane label="Output Data" name="OutputData">Output Data</TabPane>
          </Tabs>
        </div>
      </div>

    </div>

    <div class="instanceContent">
      <h2>Instance:</h2>
      <div class="item">

      </div>
    </div>

  </div>
</template>
<script>
export default {
  created: function() {
    //* 获取路由信息
    this.projectTitle = this.$route.params.projectTitle;
    this.modelId = this.$route.params.id;
    // console.log("parent: ",this.projectInfo);
    //* 获取模型信息
    this.getModelInfo(this.modelId);
  },
  data() {
    return {
      modelInfo: {
        modelName: ""
      },
      projectTitle: "",
      modelId: ""
    };
  },
  computed: {
    getCreatedTime() {
      return function(model) {
        return model.createTime ? model.createTime.split(" ")[0] : "";
      };
    }
  },
  methods: {
    getModelInfo(modelId) {
      this.$api.cmp_model
        .getModelInfo(modelId)
        .then(res => {
          console.log(res);
          this.modelInfo = res.model;
        })
        .catch(err => {
          this.$Message.error(err);
        });
    },
    runModel() {
      sessionStorage.setItem("modelInfo",JSON.stringify(this.modelInfo));
      this.$router.push({
        path: `/cmp-invoke-model`,
        name: "cmp-invoke-model",
        params: {
          id: this.modelId
        }
      });
    }
  }
};
</script>
<style scoped>
.modelContent {
}

.topContent {
  display: flex;
  flex-wrap: wrap;
  background-color: #fafbfc;
}

.modelInfo {
  padding-left: 20px;
  padding-right: 20px;
  width: 1060px;
  margin-top: 20px;
  margin-left: auto;
  margin-right: auto;

  padding: auto;
  background-color: #fafbfc;
  display: flex;
  flex-wrap: wrap;
}

.leftInfo {
  flex: 1 0 400px;
  border-right: 1px solid #d1d5da;
  margin-right: 20px;
  margin-bottom: 20px;
}

.rightInfo {
  flex: 1 0 500px;
}

.instanceContent {
  width: 1060px;
  margin: auto;
  margin-top: 20px;
}

#projectName {
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
}

#titleInfo {
  display: inline-block;
}

#desc {
  margin-top: 10px;
}

#desc p {
  margin-top: 5px;
  margin-left: 20px;
  margin-bottom: 20px;
}

.public {
  color: #586069;
  float: left;
  font-size: 18px;
  line-height: 26px;
  max-width: 635px;
  padding-left: 18px;
  position: relative;
  font-weight: 400;
  margin-bottom: 0;
  margin-top: 0;
}

.titleIcon {
  color: #959da5;
  left: -10px;
  margin-bottom: 5px;
  position: absolute;
  top: 0;
}

.tabContent {
  /* width: 960px; */
  margin-left: auto;
  margin-right: auto;
  max-height: 300px;
}

.fromFlag {
  display: flex;
  justify-content: space-between;
  font-size: 11px;
  line-height: 10px;
  width: 200px;
  white-space: nowrap;
}
</style>