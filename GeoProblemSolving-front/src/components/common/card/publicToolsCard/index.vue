<template>
  <div>
    <Card class="mainBody">
      <p
        slot="title"
        class="ellipsis"
        style="width:50%;display:inline-block;"
        :title="tool.toolName"
      >{{tool.toolName}}</p>
      <div slot="extra">
        <Button
          icon="md-eye"
          shape="circle"
          class="btnHoverGray"
          title="Preview"
          size="small"
          @click="showTool(tool)"
        ></Button>
        <Button
          icon="ios-create"
          shape="circle"
          class="btnHoverBlue"
          title="Edit"
          size="small"
          @click="editToolShow(tool)"
        ></Button>
        <Button
          icon="md-close"
          shape="circle"
          class="btnHoverRed"
          title="Delete"
          size="small"
          @click="removeToolShow(tool)"
        ></Button>
        <Button
          icon="md-share-alt"
          shape="circle"
          type="success"
          title="Add to toolset"
          size="small"
          @click="addToToolsetShow(tool)"
        ></Button>
      </div>

      <Row style="margin-bottom:5px">
        <Col span="4">
          <div>
            <div align="center">
              <img
                :src="tool.toolImg"
                style="height:100%;max-height:50px;max-width:50px"
                v-if="tool.toolImg!=''"
              />
              <avatar :username="tool.toolName" :size="50" :rounded="false" v-else></avatar>
            </div>
          </div>
        </Col>
        <Col span="19" offset="1">
          <Row v-if="tool.categoryTag.length!=0">
            <Col span="18">
              <div v-for="(tag,index) in tool.categoryTag" :key="index">
                <Col span="8">
                  <Tag type="border" color="success" v-if="index < 3">
                    <div class="tags">{{tag}}</div>
                  </Tag>
                </Col>
              </div>
            </Col>
            <Col span="6" v-if="tool.categoryTag.length > 2">
              <Tag color="geekblue" :title="tool.categoryTag" type="border">More</Tag>
            </Col>
          </Row>
          <Row v-else class="noTags">
            <Tag color="default" type="border">No Tags</Tag>
          </Row>

          <Row>
            <div>{{tool.createTime}}</div>
          </Row>
        </Col>
      </Row>
      <Row>
        <div class="toolDes">
          <p class="description" :title="tool.description">{{tool.description}}</p>
        </div>
      </Row>
    </Card>
  </div>
</template>

<script>
import Avatar from "vue-avatar";
export default {
  components: { Avatar },
  props: {
    item: {
      type: Object
    }
  },
  data() {
    return { tool: this.item };
  },
  computed: {},
  watch: {},
  methods: {
    showTool() {
      this.$emit("showtool", this.tool);
    },
    editToolShow() {
      this.$emit("edittool", this.tool);
    },
    removeToolShow() {
      this.$emit("removetool", this.tool);
    },
    addToToolsetShow() {
      this.$emit("addtotoolset", this.tool);
    }
  },
  created() {},
  mounted() {}
};
</script>
<style lang='scss' scoped>
.mainBody {
  background-color: ghostwhite;
  height: 180px;
}
.toolDes {
  width: 100%;
  height: 65px;
  border: 0.5px dashed #8080804d;
  padding: 1px 3px;

  .description {
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    /* !autoprefixer: off */
    -webkit-box-orient: vertical;
    font-size: 14px;
  }
}
.tags {
  width: 40px;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
  text-align: center;
}

.noTags >>> .ivu-tag-text {
  font-weight: 600;
  color: rgb(160, 160, 160);
}
</style>