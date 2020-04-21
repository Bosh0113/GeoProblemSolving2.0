<style>
@import "../../../../static/css/jquery.jexcel.css";
</style>
<template>
  <Row>
    <toolStyle
      :style="{height:sidebarHeight}"
      :resources="resources"
      v-on:resourceUrl="selecetResource"
    ></toolStyle>
    <Col span="4" style="padding:30px;margin-left:60px">
      <Upload :before-upload="handleUpload" action="-" accept=".csv, .xls, .xlsx">
        <Button icon="ios-cloud-upload-outline">Upload to resource center</Button>
      </Upload>
      <RadioGroup v-model="SelectAxis">
        <Radio label="X-Axis" style="padding:20px 0 10px 0"></Radio>
        <Input v-model="SelectX" style="width:200px" readonly />
        <Radio label="Y-Axis" style="padding:20px 0 10px 0"></Radio>
        <Input v-model="SelectY" style="width:200px" readonly />
      </RadioGroup>
      <h3 style="padding-top:20px">Type of chart:</h3>
      <Select
        v-model="chooseType"
        style="width:200px;padding-top:10px"
        :placeholder="chartTypePlaceholder"
      >
        <Option v-for="item in normalChart" :value="item.value" :key="item.value">{{ item.label }}</Option>
      </Select>
      <Button @click="Visualize" style="margin-top:30px">Visualization</Button>
      <Button v-if="visualization" @click="back2Table" style="margin-top:30px">Select data</Button>
    </Col>
    <Col span="17" offset="1" style="padding-top:30px">
      <div v-if="visualization" title="Data visualization" style="padding-right:20px">
        <ve-scatter v-if="chooseType == 'scatter'" :data="chartData"></ve-scatter>
        <ve-chart v-else :data="chartData" :settings="chartSettings"></ve-chart>
      </div>
      <div v-show="!visualization">
        <div id="mytable" style="height:400px"></div>
      </div>
    </Col>
  </Row>
</template>
<script>
import csv from "../../../../static/js/jquery.csv.min.js";
import jexcel from "../../../../static/js/jquery.jexcel.js";
import XLSX from "xlsx";
import VCharts from "./../../../utils/VCharts";
import toolStyle from "./toolStyle";
export default {
  components: { toolStyle },
  data() {
    return {
      sidebarHeight: 0,
      visualization: false,
      testData: [],
      columnHeader: [],
      excelData: [],
      columnNameList: [],
      // 选中的参考值
      selectName: [],
      normalChart: [
        { value: "line", label: "line" },
        { value: "histogram", label: "histogram" },
        { value: "bar", label: "bar" },
        { value: "pie", label: "pie" },
        { value: "ring", label: "ring" },
        { value: "waterfall", label: "waterfall" },
        { value: "radar", label: "radar" },
        { value: "funnel", label: "funnel" },
        { value: "scatter", label: "scatter" }
      ],
      chartTypePlaceholder: "Choose one type of charts",
      DataX: [],
      DataY: [],
      SelectX: "",
      SelectY: "",
      SelectAxis: "",
      chartData: [],
      chooseType: "",
      chartSettings: {},
      pageParams: { pageId: "", userId: "", userName: "" },
      userInfo: {}
    };
  },
  beforeDestroy() {},
  mounted() {
    this.init();
    this.getStepInfo();
    this.getUserInfo();
    this.getResources();
  },
  methods: {
    init() {
      $("#app").css("min-width", "0");
      $("#app").css("min-height", "0");
      this.sidebarHeight = window.innerHeight + "px";
      $("#mytable").jexcel({
        data: this.testData,
        minDimensions: [20, 20],
        onselection: this.selectData
      });
    },
    getStepInfo() {
      if (
        this.$route.params.groupID == undefined ||
        this.$route.params.groupID == ""
      ) {
        var href = window.location.href;
        var url = href.split("&");

        for (var i = 0; i < url.length; i++) {
          if (/groupID/.test(url[i])) {
            this.pageParams.pageId = url[i].match(/groupID=(\S*)/)[1];
            continue;
          }

          if (/userID/.test(url[i])) {
            this.pageParams.userId = url[i].match(/userID=(\S*)/)[1];
            continue;
          }

          if (/userName/.test(url[i])) {
            this.pageParams.userName = url[i].match(/userName=(\S*)/)[1];
            continue;
          }
        }
      } else {
        this.pageParams.pageId = this.$route.params.groupID;
        this.pageParams.userId = this.$route.params.userID;
        this.pageParams.userName = this.$route.params.userName;
      }
    },
    getUserInfo() {
      this.userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
      if (this.userInfo == {}) {
        this.axios
          .get(
            "/GeoProblemSolving/user/inquiry" +
              "?key=" +
              "userId" +
              "&value=" +
              this.pageParams.userId
          )
          .then(res => {
            if (res.data != "Fail" && res.data != "None") {
              this.$set(this, "userInfo", res.data);
            }
          })
          .catch(err => {});
      }
    },
    handleUpload(file) {
      if (this.pageParams.pageId == undefined || this.pageParams.pageId == "") {
        this.$Message.error("Lose the information of current step.");
        return false;
      }

      if (!/\.(xls|xlsx|csv)$/.test(file.name.toLowerCase())) {
        this.$Message.error("Wrong format");
        return false;
      }

      //上传数据
      let formData = new FormData();
      formData.append("file", file);
      formData.append("description", "Data chart tool");
      formData.append("type", "data");
      formData.append("uploaderId", this.userInfo.userId);
      formData.append("privacy", "private");
      formData.append("folderId", this.pageParams.pageId);
      this.axios
        .post("/GeoProblemSolving/folder/uploadToFolder", formData)
        .then(res => {
          if (
            res.data.sizeOver.length > 0 ||
              res.data.failed.length > 0 ||
              res.data == "Offline"
            ) {
              console.log(res.data);
            } else if (res.data.uploaded.length > 0) {
            let dataName = res.data[0].fileName;

            this.dataUrl = "/GeoProblemSolving/resource/upload/" + dataName;

            let dataItem = {
              name: file.name,
              description: "charts tool data",
              pathURL: "/GeoProblemSolving/resource/upload/" + dataName
            };
            this.resources.push(dataItem);
          }
        })
        .catch(err => {});

      // 渲染表格
      this.fillTable(file);

      return false;
    },
    fillTable(file) {
      var that = this;
      var fileReader = new FileReader();
      fileReader.readAsBinaryString(file);
      fileReader.onload = ev => {
        try {
          const data = ev.target.result;
          const workbook = XLSX.read(data, {
            type: "binary"
          });
          const wsname = workbook.SheetNames[0]; //取第一张表
          const ws = XLSX.utils.sheet_to_json(workbook.Sheets[wsname]);
          let arr = Object.keys(ws[0]);
          that.columnNameList = arr;
          //生成json表格内容
          let list1 = [];
          let list2 = [];
          for (var i = 0; i < ws.length; i++) {
            for (var key in ws[i]) {
              list1.push(ws[i][key]);
            }
            list2[i] = list1;
            list1 = [];
          }
          that.columnHeader = arr;
          that.excelData = list2;

          $("#mytable").jexcel({
            data: that.excelData,
            colHeaders: that.columnHeader,
            tableOverflow: false,
            minDimensions: [20, 20],
            csvHeaders: true,
            onselection: that.selectData
          });
          that.$refs.upload.value = "";
        } catch (e) {
          return false;
        }
      };
      fileReader.onerror = function() {
        this.$Message.error("Input data error.");
        that.showFile = false;
        that.uploadGeoJson = null;
      };
    },
    selectData(obj, startCell, endCell) {
      let start = startCell[0].id;
      let end = endCell[0].id;
      let startXY = start.split("-");
      let endXY = end.split("-");
      if (this.SelectAxis == "") {
        return;
      }

      try {
        start =
          String.fromCharCode(parseInt(startXY[0]) + "A".charCodeAt()) +
          (parseInt(startXY[1]) + 1);
        end =
          String.fromCharCode(parseInt(endXY[0]) + "A".charCodeAt()) +
          (parseInt(endXY[1]) + 1);
      } catch (e) {}

      if (this.SelectAxis == "X-Axis") {
        this.SelectX = start + "->" + end;
        this.DataX = this.getData(startXY, endXY);
      } else if (this.SelectAxis == "Y-Axis") {
        this.SelectY = start + "->" + end;
        this.DataY = this.getData(startXY, endXY);
      }
    },
    getData(start, end) {
      //取值
      let data = [];
      let minx =
        parseInt(start[0]) <= parseInt(end[0])
          ? parseInt(start[0])
          : parseInt(end[0]);
      let maxx =
        parseInt(start[0]) >= parseInt(end[0])
          ? parseInt(start[0])
          : parseInt(end[0]);
      let miny =
        parseInt(start[1]) <= parseInt(end[1])
          ? parseInt(start[1])
          : parseInt(end[1]);
      let maxy =
        parseInt(start[1]) >= parseInt(end[1])
          ? parseInt(start[1])
          : parseInt(end[1]);
      for (let i = minx; i <= maxx; i++) {
        let colData = [];
        for (let j = miny; j <= maxy; j++) {
          let value = $("#mytable").jexcel("getValue", i + "-" + j);
          colData.push(value);
        }
        data.push(colData);
      }
      for (let i = 0; i < data.length; i++) {
        let header = $("#mytable").jexcel("getHeader", i + minx);
        data[i].unshift(header);
      }
      return data;
    },
    back2Table() {
      this.visualization = false;
    },
    showCharts() {
      this.visualization = true;

      //数据标准化
      let dimension = [],
        metrics = [],
        columns = [],
        rows = [];
      dimension.push(this.DataX[0][0]);
      columns.push(this.DataX[0][0]);
      let dataLength =
        this.DataX[0].length <= this.DataY[0].length
          ? this.DataX[0].length
          : this.DataY[0].length;

      if (
        this.chooseType === "pie" ||
        this.chooseType === "ring" ||
        this.chooseType === "waterfall"
      ) {
        metrics.push(this.DataY[0][0]);
        columns.push(this.DataY[0][0]);
        for (let i = 1; i < dataLength; i++) {
          let row = {};
          row[this.DataX[0][0]] = this.DataX[0][i];
          row[this.DataY[0][0]] = this.DataY[0][i];
          rows.push(row);
        }
        //排序
        var that = this;
        rows.sort(function(a, b) {
          return a[that.DataX[0][0]] - b[that.DataX[0][0]];
        });

        if (this.chooseType == "funnel") {
          this.chartSettings = {
            type: this.chooseType,
            metrics: metrics,
            dimension: dimension,
            ascending: true,
            useDefaultOrder: true
          };
          this.chartData = {
            columns: columns,
            rows: rows
          };
        } else {
          this.chartSettings = {
            type: this.chooseType,
            metrics: metrics,
            dimension: dimension
          };
          this.chartData = {
            columns: columns,
            rows: rows
          };
        }
      } else {
        for (let i = 0; i < this.DataY.length; i++) {
          metrics.push(this.DataY[i][0]);
        }
        for (let i = 0; i < this.DataY.length; i++) {
          columns.push(this.DataY[i][0]);
        }
        for (let i = 1; i < dataLength; i++) {
          let row = {};
          row[this.DataX[0][0]] = this.DataX[0][i];
          for (let j = 0; j < this.DataY.length; j++) {
            row[this.DataY[j][0]] = this.DataY[j][i];
          }
          rows.push(row);
        }
        //排序
        var that = this;
        rows.sort(function(a, b) {
          return a[that.DataX[0][0]] - b[that.DataX[0][0]];
        });

        if (this.chooseType == "scatter") {
          this.chartSettings = {
            type: this.chooseType,
            metrics: metrics,
            dimension: dimension
          };
          this.chartData = {
            columns: columns,
            rows: rows
          };
        } else {
          this.chartSettings = {
            type: this.chooseType,
            metrics: metrics,
            dimension: dimension
          };
          this.chartData = {
            columns: columns,
            rows: rows
          };
        }
      }
    },
    Visualize() {
      if (this.DataX.length == 0 || this.DataY.length == 0) {
        return;
      }
      this.showCharts();
    },
    getResources() {
      if (this.pageParams.pageId == undefined || this.pageParams.pageId == "") {
        this.$Message.error("Lose the information of current step.");
        return false;
      }

      this.resources = [];
      this.axios
        .get(
          "/GeoProblemSolving/folder/inquiry?folderId=" + this.pageParams.pageId
        )
        .then(res => {
          // 写渲染函数，取到所有资源
          if (res.data !== "None") {
            for (let i = 0; i < res.data.files.length; i++) {
              if (
                res.data.files[i].type == "data" &&
                /\.(xls|xlsx|csv)$/.test(res.data.files[i].name.toLowerCase())
              ) {
                this.resources.push(res.data.files[i]);
              }
            }
          } else {
            this.resources = [];
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    selecetResource(url) {
      this.dataUrl = url;
      this.viewData();
    },
    viewData() {
      if (/\.(csv|xls|xlsx)$/.test(this.dataUrl.toLowerCase())) {
        var that = this;
        var xhr = new XMLHttpRequest();
        xhr.open("GET", this.dataUrl, true);
        xhr.responseType = "blob";
        xhr.onload = function(e) {
          if (this.status == 200) {
            var file = this.response;
            that.fillTable(file);
          }
        };
        xhr.send();
      } else {
        this.$Message.error("Worry data format!");
      }
      this.showFile = false;
    }
  }
};
</script>



