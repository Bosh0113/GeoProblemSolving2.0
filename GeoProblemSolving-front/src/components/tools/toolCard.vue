<template>
  <div>
    <Card style="background-color: #faebd75c">
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
      <div>
        <div style="display: inline-block;algin:left;">
          <img :src="tool.toolImg" v-if="tool.toolImg!=''" style="height:100%;max-height:50px;" />
          <avatar :username="tool.toolName" :size="50" style="margin-bottom:6px" v-else></avatar>
        </div>
        <div style="display: inline-block;vertical-align: top;width: 70%;" class="ellipsis">
          <strong :title="tool.toolName">{{tool.toolName}}</strong>
          <br />
          <span :title="tool.categoryTag.join('|')">
            <i>{{tool.categoryTag.join('|')}}</i>
          </span>
        </div>
        <div style="width:100%;height: 65px;border: 0.5px dashed #8080804d;padding: 1px 3px;">
          <p class="toolDescription" :title="tool.description">{{tool.description}}</p>
        </div>
      </div>
    </Card>
  </div>
</template>

<script>
export default {
  components: {},
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
  methods: {},
  created() {},
  mounted() {}
};
</script>
<style lang='scss' scoped>
//@import url(); 引入公共css类
</style>