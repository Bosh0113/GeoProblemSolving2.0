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
      :data="switchValue ? privateData : publicData"
      border
      style="width: 100%"
    >
      <el-table-column label="Select" sortable width="50">
        <template slot-scope="scope">
          <el-radio
            v-model="scope.row.isSelect"
            label="true"
            @change="selectChange(scope.row)"
          >
          </el-radio>
          <!-- <el-radio-group v-model="scope.row.isSelect">
            <el-radio-button label="1"></el-radio-button>
          </el-radio-group> -->
        </template>
      </el-table-column>
      <el-table-column prop="name" label="Name" sortable width="200">
      </el-table-column>
      <el-table-column prop="type" label="Type" width="100"> </el-table-column>
      <el-table-column prop="description" label="Description" width="300">
      </el-table-column>
      <el-table-column prop="uploaderName" label="Provider" width="150">
      </el-table-column>
      <el-table-column prop="uploadTime" label="Upload time" width="150">
      </el-table-column>
    </el-table>
    {{ privateData }}
  </div>
</template>

<script>
import { get, del, post, put, patch } from "@/axios";
export default {
  components: {},

  watch: {},

  computed: {},

  data() {
    return {
      switchValue: true,
      privateData: [
        {
          resourceId: "1",
          name: "1.jpg",
          description: "",
          type: "data",
          fileSize: "1.14MB",
          pathURL: "/GeoProblemSolving/resource/upload/1186921.jpg",
          uploaderId: "1bfbe06c-be1e-40dc-bf13-bdd330ad607e",
          uploaderName: "luyuchen",
          uploadTime: "2019/05/08 21:34:38",
          privacy: "private"
        },
        {
          resourceId: "22",
          name: "1.jpg",
          description: "",
          type: "data",
          fileSize: "1.14MB",
          pathURL: "/GeoProblemSolving/resource/upload/1186921.jpg",
          uploaderId: "1bfbe06c-be1e-40dc-bf13-bdd330ad607e",
          uploaderName: "luyuchen",
          uploadTime: "2019/05/08 21:34:38",
          privacy: "private"
        }
      ],
      publicData: [
        {
          resourceId: "665a1c21-f141-4342-b92d-b3357927",
          name: "空白.txt",
          description: "测试",
          belong: "平台测试",
          type: "data",
          fileSize: "0.00KB",
          pathURL: "/GeoProblemSolving/resource/upload/空白870631.txt",
          uploaderId: "172c8d44-6537-4e35-9ef7-068f1c48f67b",
          uploaderName: "zbc",
          uploadTime: "2019/05/08 14:46:45",
          organization: "NNU",
          privacy: "public"
        }
      ]
    };
  },

  methods: {
    async getPrivateResources() {
      await get();
    },
    async getPublicResources() {
      await get();
    },

    selectChange(val) {
      console.log(val);

      this.privateData.forEach(data => {
        data.isSelect = "false";
      });
      val.isSelect = "true";
      this.$forceUpdate();
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
