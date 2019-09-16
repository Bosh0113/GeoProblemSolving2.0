<style scoped>
</style>
<template>
  <div>
    <Row>
      <Col span="21" style="margin:10px 0 0 100px">
        <div style="padding-top: 20px">
          <folder-tree
            :rootFolderId="subProjectInfo.subProjectId"
            :role="userRole"
            ref="folderTreeEle"
          ></folder-tree>
        </div>
      </Col>
    </Row>
  </div>
</template>
<script>
import folderTree from "../resources/folderTree";
export default {
  components: {
    folderTree
  },
  props: ["subProjectInfo", "userRole"],
  data() {
    return {};
  },
  created() {
  },
  mounted() {
  },
  methods: {
  }
};
</script>
