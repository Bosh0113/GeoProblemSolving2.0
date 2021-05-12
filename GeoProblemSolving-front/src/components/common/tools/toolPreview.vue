<template>
  <div class="previewBody">
    <Row>
      <Col :span="2">
        <img :src="tool.toolImg" v-if="tool.toolImg!=''" style="height:100%;max-height:50px;" />
        <avatar
          :username="tool.toolName"
          :size="120"
          style="margin-bottom:6px"
          :rounded="false"
          v-else
        ></avatar>
      </Col>
      <Col :span="21" :offset="1" class="basicInfo">
        <Row>
          <div class="toolName">{{tool.toolName}}</div>
        </Row>
        <Row></Row>
        <Row>
          <div style="float:left">
            <Tag color="green" size="large" v-if="tool.privacy == 'Private'">{{tool.privacy}}</Tag>
            <Tag color="warning" size="large" v-else>{{tool.privacy}}</Tag>
          </div>

          <Divider type="vertical" />
          <div style="float:left;">
            <div>{{tool.createTime}}</div>
          </div>
        </Row>
        <Row>
          <Col v-for="(item,index) in tool.categoryTag" :key="index">
            <Tag type="border" color="red">{{item}}</Tag>
          </Col>
        </Row>
      </Col>
    </Row>
    <Row class="detail">
      <div class="title">Details</div>
      <Divider />
      <!-- <vue-scroll :ops="ops"> -->
      <div v-html="tool.detail" class="detailHtml"></div>
      <!-- </vue-scroll> -->
    </Row>
  </div>
</template>

<script>
import Avatar from "vue-avatar";
export default {
  components: { Avatar },
  props: {
    selectTool: {
      type: Object
    }
  },
  data() {
    return {
      tool: this.selectTool,
      ops: {
        bar: {
          background: "#808695"
        }
      }
    };
  },
  computed: {},
  watch: {
    selectTool: {
      handler(val) {
        this.tool = val;
      },
      deep: true
    }
  },
  methods: {},
  created() {},
  mounted() {}
};
</script>
<style lang='scss' scope>
.previewBody {
  width: 100%;
  height: 100%;  
}

.basicInfo {
  font-size: 18px;
  .toolName {
    font-size: 28px;
    font-weight: 600;
    margin-bottom: 5px;
  }
  .ivu-divider {
    background-color: rgb(192, 192, 192);
    margin: 0;
    float: left;
    margin-right: 5px;
    width: 2px;
    height: 24px;
    top: 2px;
    margin: 0 10px;
  }
}
.detail {
  margin-top: 3%;
  .title {
    font-weight: 600;
    font-size: 22px;
    font-style: italic;
  }
  .ivu-divider {
    background-color: rgb(26, 26, 26);
    margin: 0;
  }
  .detailHtml {
    width: 100%;
    height: auto;
  }
}
</style>