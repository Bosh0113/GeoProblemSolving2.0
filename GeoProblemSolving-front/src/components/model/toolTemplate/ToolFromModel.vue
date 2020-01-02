<style scoped>
.modelList {
  height: 105px;
  width: 290px;
  margin-bottom: 5px;
  /* border: 1px solid #cfcfcfb6;
  border-radius: 5px; */
  /* padding: 6%; */
}
/* .modelList:hover {
  box-shadow: 0px 0px 3px 1px #909090;
} */

.modelTitle {
  margin-bottom: 5px;
}
.modelName {
  font-weight: 600;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.modelDes {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  font-style: italic;
}
.tipButton >>> .ivu-btn {
  border: 0;
  clear: both;
  /* border: none;
  outline: none; */
}

.searchTitle {
  float: left;
  font-size: 18px;
  font-weight: 600;
}
.searchModel >>> .ivu-input-group .ivu-input,
.ivu-input-group .ivu-input-inner-container {
}
</style>
<template>
  <div>
    <Row>
      <div class="title">Select the model to create your tool</div>
    </Row>
    <Row>
      <div class="searchModel">
        <!-- <div class="searchTitle">Search</div> -->
        <Input search enter-button="Search" placeholder="Model name ..." />
      </div>
    </Row>
    <Row>
      <Col span="22" offset="2">
        <Row>
          <Col span="4" offset="1" v-for="(model,index) in modelList" :key="index">
            <Card class="modelList" @click.native="getSelectModel(model)">
              <Col class="modelAvatar" span="8">
                <avatar :username="model.name" :size="70" :rounded="false"></avatar>
              </Col>
              <Col class="modelInfo" span="16">
                <Row>
                  <div class="modeTitle">
                    <Col span="22">
                      <div class="modelName">{{model.name}}</div>
                    </Col>
                    <Col span="2">
                      <Poptip placement="bottom" trigger="hover">
                        <Icon type="ios-arrow-down" size="16"></Icon>
                        <div slot="content" class="tipButton">
                          <div>
                            <Button
                              name="modelIntro"
                              @click="toModelIntro(model.relatedModelInfoId)"
                            >Model introduction</Button>
                          </div>
                          <div>
                            <Button
                              name="modelProcessIntro"
                              @click="toModelProcessIntro(model.id)"
                            >Introduction to model process</Button>
                          </div>
                        </div>
                      </Poptip>
                    </Col>
                  </div>
                </Row>
                <Row>
                  <div class="modelDes">{{model.des}}</div>
                </Row>
              </Col>
            </Card>
          </Col>
        </Row>
      </Col>
    </Row>
  </div>
</template>

<script>
import Avatar from "vue-avatar";
export default {
  components: {
    Avatar
  },
  data() {
    return {
      modelList: [],
      geoModelingModel: "http://geomodeling.njnu.edu.cn/modelItem/",
      selectModelItem: {},
      selectModelName: "",
      selectModelDes: ""
    };
  },
  methods: {
    async getAllModelItems() {
      let data = await this.axios.get(
        `/GeoProblemSolving/modelItem/getModelItems`
      );
      this.modelList = data.data.data;
    },
    toModelIntro(relatedId) {
      let url = this.geoModelingModel + `${relatedId}`;
      window.open(url);
    },
    toModelProcessIntro(modelItemId) {
      let route = `/modelItem?id=${modelItemId}`;

      let data = this.$router.resolve({ path: route });
      window.open(data.href, "_blank");
    },
    getSelectModel(model) {
      this.selectModelItem = model;
      this.$emit("getSelectModel", this.selectModelItem);
      // $(this).css("background","#f00");
    }
  },

  created() {
    this.getAllModelItems();
  }
};
</script>
