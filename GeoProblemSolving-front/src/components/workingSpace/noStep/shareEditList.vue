<style scoped>
</style>
<template>
  <div>
    <h4>Historical edits</h4>
    <Table
      :columns="tableColName"
      :data="fileList"
      class="table"
      v-show="fileList!=[] && fileList!='None'"
      height="400"
      size="small"
    >
      <template slot-scope="{ row }" slot="name">
        <strong>{{ row.name }}</strong>
      </template>
      <template slot-scope="{ row }" slot="name">
        <strong>{{ row.uploaderName }}</strong>
      </template>
      <template slot-scope="{ row }" slot="name">
        <strong>{{ row.uploadTime }}</strong>
      </template>
      <template slot-scope="{ row }" slot="action">
        <Button size="small" title="Check" icon="md-eye" type="primary" @click="OpenData(row)"></Button>
      </template>
    </Table>
  </div>
</template>
<script>
export default {
  props: ["stepInfo", "userRole"],
  watch: {
    stepInfo() {
      this.getResList();
    }
  },
  data() {
    return {
      fileList: [], //showed resources
      userInfo: this.$store.getters.userInfo,
      tableColName: [
        {
          title: "Name",
          key: "name",
          minWidth: 30,
          tooltip: true,
          sortable: true
        },
        {
          title: "Editor",
          key: "uploaderName",
          width: 200,
          tooltip: true,
          sortable: true
        },
        {
          title: "Edit time",
          key: "uploadTime",
          width: 200,
          tooltip: true,
          sortable: true
        },
        {
          title: "Action",
          slot: "action",
          width: 200,
          align: "center"
        }
      ]
    };
  },
  mounted() {
    this.getResList();
  },
  methods: {
    getResList() {
      var list = [];
      if (this.stepInfo.stepId != "" && this.stepInfo.stepId != undefined) {
        $.ajax({
          url:
            "/GeoProblemSolving/folder/inquiry" +
            "?folderId=" +
            this.stepInfo.stepId,
          type: "GET",
          async: false,
          success: data => {
            if (data !== "None") {
              list = data.files;
            } else {
              list = [];
            }
            //filte
            this.fileList = this.filterToolData(list);
          }
        });
      }
    },
    filterToolData(fileList) {
      var filterdata = fileList.filter(item => {
        if (item.type.indexOf("toolData:Mindmap") != -1) {
          return item;
        }
      });
      return filterdata;
    },
    OpenData(item) {
      let toolInfo = {};
      let toolURL = "";
      // 检测是否有toolInfo信息
      try {
        toolInfo = JSON.parse(item.editToolInfo);
      } catch (err) {
        this.$Notice.info({ desc: "There is no record of using tools." });
        return;
      }
      if (toolInfo == undefined || toolInfo == null || toolInfo == {}) {
        this.$Notice.info({ desc: "There is no record of using tools." });
        return;
      }
      if (this.panel != null) {
        this.panel.close();
      }
      if (this.userRole != "Visitor" && this.userRole != "Token") {
        toolURL =
          '<iframe src="/GeoProblemSolving/Collaborative/Mindmap/version/mindmap.html?userName=' +
          this.userInfo.userName +
          "&userID=" +
          this.userInfo.userId +
          "&groupID=" +
          this.stepInfo.stepId +
          "&resourceID=" +
          item.resourceId +
          '" style="width: 100%;height:100%;"></iframe>';
      } else {
        toolURL =
          '<iframe src="/GeoProblemSolving/Collaborative/Mindmap/share/mindmap.html?userName=&userID=&groupID=' +
          this.stepInfo.stepId +
          "&resourceID=" +
          item.resourceId +
          '" style="width: 100%;height:100%;"></iframe>';
      }
      var demoPanelTimer = null;
      this.panel = jsPanel.create({
        theme: "success",
        headerTitle: toolInfo.toolName,
        footerToolbar: '<p style="height:10px"></p>',
        contentSize: "800 400",
        content: toolURL,
        disableOnMaximized: true,
        dragit: {
          containment: 5
        },
        closeOnEscape: true,
        onclosed: function() {
          window.clearTimeout(demoPanelTimer);
        },
        // callback: function() {
        //   var that = this;
        //   demoPanelTimer = window.setInterval(function() {
        //     that.style.zIndex = "9999";
        //   }, 1);
        // }
      });
      $(".jsPanel-content").css("font-size", "0");
    }
  }
};
</script>