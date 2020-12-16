<!--  -->
<template>
  <div>
    <div class="switch-label">
      <div v-if="switchValue">Private data</div>
      <div v-else>Public data</div>
    </div>
    <el-switch
      v-model="switchValue"
      active-color="#13ce66"
      inactive-color="#409eff"
    >
    </el-switch>
    <el-table
      :data="tableData"
      style="width: 100%"
      :default-sort="{ prop: 'date', order: 'descending' }"
    >
      <el-table-column prop="name" label="Name" sortable width="200">
      </el-table-column>
      <el-table-column prop="type" label="Type" width="100"> </el-table-column>
      <el-table-column prop="description" label="Description" width="300">
      </el-table-column>
      <el-table-column prop="provider" label="Provider" width="200">
      </el-table-column>
      <el-table-column prop="uploadTime" label="Upload time" width="150">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { get, del, post, put, patch } from "@/axios";
export default {
  components: {},

  watch: {},

  computed: {},

  data() {
    return { switchValue: true };
  },

  methods: {
    async getPrivateResources() {
      await get;
    }
  },

  mounted() {}
};
</script>
<style lang="scss" scoped>
.switch-label {
  width: 100px;
  float: left;
  font-weight: 600;
  font-size: 16px;
}
</style>
