<style>
@import "../../../../../static/css/jquery.jexcel.css";
</style>
<template>
  <Row>
    <toolStyle
      :participants="participants"
      :resources="resources"
      v-on:resourceUrl="selecetResource"
    ></toolStyle>
    <div style="width: 300px; padding: 30px; margin-left: 60px; float: left">
      <h3>Select data:</h3>
      <RadioGroup v-model="SelectAxis">
        <Radio label="Name" style="padding: 20px 0 10px 0"></Radio>
        <Input v-model="SelectX" style="width: 200px" readonly />
        <Radio label="Value" style="padding: 20px 0 10px 0"></Radio>
        <Input v-model="SelectY" style="width: 200px" readonly />
      </RadioGroup>
      <RadioGroup v-model="sort">
        <Radio label="ascending" style="padding: 20px 20px 10px 0"></Radio>
        <Radio label="descending" style="padding: 20px 20px 10px 0"></Radio>
      </RadioGroup>
      <Button @click="Visualize" style="margin-top: 30px">Visualization</Button>
      <Button v-if="visualization" @click="back2Table" style="margin-top: 30px"
        >Select data</Button
      >
    </div>
    <div style="padding-top: 30px; float: left; width: calc(100vw - 400px)">
      <div
        v-show="visualization"
        id="visualization"
        style="
          width: calc(100vw - 400px);
          height: calc(90vh);
          background-color: #f8f8f9;
        "
      ></div>
      <div v-show="!visualization">
        <div id="mytable" style="height: calc(90vh)"></div>
      </div>
    </div>
  </Row>
</template>
<script>
import csv from "@static/js/jquery.csv.min.js";
import jexcel from "@static/js/jquery.jexcel.js";
import XLSX from "xlsx";
import echarts from "echarts";
import toolStyle from "../toolStyle";
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
      //data and params
      DataX: [],
      DataY: [],
      SelectX: "",
      SelectY: "",
      SelectAxis: "",
      chartData: [],
      chooseType: "",
      Charts: null,
      chartSettings: {},
      sort: "descending",
      // page info
      pageParams: { pageId: "", userId: "", userName: "" },
      userInfo: {},
      //协同消息
      send_content: {},
      participants: [],
      resources: [],
      loadData: "",
    };
  },
  beforeDestroy() {
    // this.socketApi.close();
  },
  mounted() {
    // 加载协同组件
    loadCollabComponent();
    this.getStepInfo();

    this.init();
  },
  methods: {
    getStepInfo() {
      if (componentStatus) {
        // 获取数据
        this.activityInfo = activityInfo;
        this.userInfo = userInfo;
        this.resources = resources.files;

        this.participants.push(userInfo);
        this.pageParams.pageId = activityInfo.aid;
        this.pageParams.userId = userInfo.userId;
        this.pageParams.userName = userInfo.userName;

        // 绑定函数
        buildSocketChannel(
          this.getSocketOperation,
          null,
          null,
          this.getSocketParticipants
        );
      } else {
        let _this = this;
        setTimeout(function () {
          _this.getStepInfo();
        }, 1000);
      }
    },
    init() {
      $("#app").css("min-width", "0");
      $("#app").css("min-height", "0");
      this.sidebarHeight = window.innerHeight + "px";
      $("#mytable").jexcel({
        data: this.testData,
        minDimensions: [20, 20],
        onselection: this.selectData,
      });
    },
    fillTable(file) {
      var that = this;
      var fileReader = new FileReader();
      fileReader.readAsBinaryString(file);
      fileReader.onload = (ev) => {
        try {
          const data = ev.target.result;
          const workbook = XLSX.read(data, {
            type: "binary",
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
            onselection: that.selectData,
          });
          that.$refs.upload.value = "";
        } catch (e) {
          return false;
        }
      };
      fileReader.onerror = function () {
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

      if (this.SelectAxis == "Name") {
        this.send_content["startX"] = startXY;
        this.send_content["endX"] = endXY;
        this.SelectX = start + "->" + end;
        this.DataX = this.getData(startXY, endXY);
      } else if (this.SelectAxis == "Value") {
        this.send_content["startY"] = startXY;
        this.send_content["endY"] = endXY;
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
      let dataLength =
        this.DataX[0].length <= this.DataY[0].length
          ? this.DataX[0].length
          : this.DataY[0].length;
      var option = {
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b} : {c}%",
        },
        toolbox: {
          feature: {
            saveAsImage: {},
          },
        },
        legend: {
          data: [],
        },
        series: [
          {
            data: [],
            type: "funnel",
            sort: this.sort,
            label: {
              show: true,
              position: "inside",
            },
            itemStyle: {
              borderColor: "#fff",
              borderWidth: 1,
            },
            emphasis: {
              label: {
                fontSize: 20,
              },
            },
          },
        ],
      };
      for (let i = 1; i < dataLength; i++) {
        option.legend.data.push(this.DataX[0][i]);
        option.series[0].data.push({
          value: this.DataY[0][i],
          name: this.DataX[0][i],
        });
      }
      if (this.Charts == null) {
        this.Charts = echarts.init(document.getElementById("visualization"));
      }
      this.Charts.setOption(option);
    },
    Visualize() {
      if (this.DataX.length == 0 || this.DataY.length == 0) {
        return;
      }

      this.send_content = {
        type: "operation",
        sender: this.userInfo.userId,
        behavior: "visualize",
        content: {
          chartType: this.chooseType,
        },
      };
      sendCustomOperation(this.send_content);

      this.showCharts();
    },
    getSocketParticipants(data) {
      if (data.type === "members") {
        if (data.behavior == "on") {
          this.participants = [];
          for (let i = 0; i < data.participants.length; i++) {
            this.participants.push(data.participants[i]);
          }
        } else if (data.behavior == "off") {
           for(let i = 0; i < this.participants.length; i++) {
             if(this.participants[i].userId === data.activeUser.userId) {
               this.participants.splice(i,1);
             }
           }
        }
      }
    },
    getSocketOperation(data) {
      let socketData = data;

      if (socketData.type === "operation") {
        if (socketData.behavior === "visualize") {
          this.chooseType = socketData.content.chartType;
          try {
            let start =
              String.fromCharCode(
                parseInt(socketData.content.startX[0]) + "A".charCodeAt()
              ) +
              (parseInt(socketData.content.startX[1]) + 1);
            let end =
              String.fromCharCode(
                parseInt(socketData.content.endX[0]) + "A".charCodeAt()
              ) +
              (parseInt(socketData.content.endX[1]) + 1);
            this.SelectX = start + "->" + end;
            this.DataX = this.getData(
              socketData.content.startX,
              socketData.content.endX
            );

            start =
              String.fromCharCode(
                parseInt(socketData.content.startY[0]) + "A".charCodeAt()
              ) +
              (parseInt(socketData.content.startY[1]) + 1);
            end =
              String.fromCharCode(
                parseInt(socketData.content.endY[0]) + "A".charCodeAt()
              ) +
              (parseInt(socketData.content.endY[1]) + 1);
            this.SelectY = start + "->" + end;
            this.DataY = this.getData(
              socketData.content.startY,
              socketData.content.endY
            );
          } catch (e) {}
          this.showCharts();
        } else if (socketData.behavior === "selectdata") {
          this.loadData = socketData.content.data;
          this.viewData();
        }
      }
    },
    selecetResource(data) {
      this.loadData = data;

      // 协同
      this.send_content = {
        type: "operation",
        sender: this.userInfo.userId,
        behavior: "selectdata",
        content: {
          data: this.loadData,
        },
      };
      sendCustomOperation(this.send_content);

      this.viewData();
    },
    viewData() {
      if (/\.(csv|xls|xlsx)$/.test(this.loadData.suffix.toLowerCase())) {
        var that = this;
        var xhr = new XMLHttpRequest();
        xhr.open("GET", this.loadData.address, true);
        xhr.responseType = "blob";
        xhr.onload = function (e) {
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
    },
  },
};
</script>



