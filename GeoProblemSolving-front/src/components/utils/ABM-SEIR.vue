<style scoped>
.mapCard >>> .ivu-card-body {
  padding: 0px;
}
.agentConsole >>> .ivu-card-head {
  padding: 6px 16px;
}
</style>
<template>
  <div style="display: flex;background-color:lightgrey">
    <Card
      dis-hover
      style="width: 290px; height: calc(100vh - 10px); background-color: white; margin: 5px;"
    >
      <p slot="title">Parameters of SEIR model</p>
      <div id="parameters">
        <Label style="display:block">Infectiousness in exposed duration:</Label>
        <RadioGroup v-model="exposedInfect" style="margin: 5px 0 20px 0; width:200px">
          <Radio label="Yes"></Radio>
          <Radio label="No"></Radio>
        </RadioGroup>
        <Label style="display:block">Mean exposed time(days):</Label>
        <InputNumber
          size="small"
          :max="30"
          :min="0"
          v-model="exposedTime"
          style="margin: 5px 0 20px 0; width:200px"
        ></InputNumber>
        <Label style="display:block">Mean infected time(days):</Label>
        <InputNumber
          size="small"
          :max="30"
          :min="0"
          v-model="infectedTime"
          style="margin: 5px 0 20px 0; width:200px"
        ></InputNumber>
        <Label style="display:block">Basic reproduction number:</Label>
        <InputNumber
          size="small"
          :max="30"
          :min="0"
          v-model="R0"
          style="margin: 5px 0 20px 0; width:200px"
        ></InputNumber>
        <Label style="display:block">Mortality rate:</Label>
        <InputNumber
          size="small"
          :max="1"
          :min="0"
          v-model="mortality"
          style="margin: 5px 0 20px 0; width:200px"
        ></InputNumber>
        <Button @click="setSEIRParem">Submit</Button>
        <Button @click="resetSEIRParem">Reset</Button>
      </div>
    </Card>
    <div>
      <Card
        class="mapCard"
        dis-hover
        style="width: calc(100vw - 310px); height: calc(100vh - 250px); background-color: white; margin: 5px;"
      >
        <div id="map" style="width:100%; height:100%; position: absolute;"></div>
      </Card>
      <Card
        dis-hover
        class="agentConsole"
        style="width: calc(100vw - 310px); height: 235px; background-color: white; margin: 5px;"
      >
        <p slot="title">Agent console</p>
        <div id="status">
          <div style="margin-bottom:10px">
            <Label>Total humans:</Label>
            <InputNumber
              size="small"
              :max="1000"
              :min="1"
              v-model="totalNum"
              style="margin-right:20px"
            ></InputNumber>
            <Label>Infected humans:</Label>
            <InputNumber
              size="small"
              :max="1000"
              :min="0"
              v-model="infectedNum"
              style="margin-right:20px"
            ></InputNumber>
            <Label>Human activity(km):</Label>
            <InputNumber
              size="small"
              :max="5"
              :min="0"
              v-model="activity"
              style="margin-right:20px"
            ></InputNumber>
            <Label>Infective distance(m):</Label>
            <InputNumber
              size="small"
              :max="5"
              :min="0"
              v-model="infectiveDist"
              style="margin-right:20px"
            ></InputNumber>
            <Button size="small" style="margin-right:20px" @click="addAgents">Add agents</Button>
            <Button
              size="small"
              icon="md-refresh"
              style="margin-right:20px"
              @click="resetAgents"
            >Reset</Button>
          </div>
          <Divider style="margin: 20px 0;" />
          <Slider v-model="simuTime" :marks="simuDays"></Slider>
          <div style="margin-top:40px">
            <Label>Simulation duration(days):</Label>
            <InputNumber
              size="small"
              :max="100"
              :min="1"
              v-model="totalSimuDays"
              style="margin-right:20px"
            ></InputNumber>
            <Button size="small" style="margin-right:20px" @click="setDuration">Set duration</Button>
          </div>
          <div style="margin-top:10px">
            <Button size="small" icon="md-play" style="margin-right:20px" @click="startSimu">Start</Button>
            <Button size="small" icon="md-pause" style="margin-right:20px" @click="pauseSimu">Pause</Button>
            <Button size="small" icon="md-square" style="margin-right:20px" @click="endSimu">End</Button>
            <Button size="small" style="margin:0 12px" @click="saveSimu">Save simulation data</Button>
          </div>
        </div>
      </Card>
    </div>
    <Modal
      v-model="modalImport"
      title="Import GeoJSON data"
      @on-ok="addData"
      ok-text="OK"
      cancel-text="Cancel"
    >
      <Upload type="drag" :before-upload="handleUpload" action="-" accept=".json, .zip">
        <div style="padding: 20px 0">
          <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
          <p>
            Click or drag files here to upload(The file size must control in
            <span
              style="color:red"
            >1GB</span>)
          </p>
        </div>
      </Upload>
      <div v-show="showFile">
        <span id="show-file">
          <i class="ivu-icon ivu-icon-md-document"></i>
          {{uploadDataName}}
        </span>
      </div>
      <br />
    </Modal>
  </div>
</template>
<script>
import L from "leaflet";
import A from "agentmaps";
import * as socketApi from "./../../api/socket.js";
import imIcon from "../../../static/Images/import.png";
import "leaflet/dist/leaflet.css";
export default {
  data() {
    return {
      //样式
      split: 0.5,
      // page info
      pageParams: { pageId: "", userId: "", userName: "" },
      userInfo: {},
      // map
      map: null,
      agentMap: null,
      modalImport: false,
      //协同
      send_content: {},
      //map数据
      showFile: false,
      uploadDataName: "",
      resources: [],
      dataUrl: "",
      unit_layers: null,
      street_layers: null,
      //SEIR
      exposedInfect: "No",
      exposedTime: 10,
      infectedTime: 10,
      R0: 1,
      mortality: 0,
      exposedRate: 0.1,
      infectedRate: 0.1,
      recoveredRate: 0.1,
      //agent 数据
      totalNum: 100,
      infectedNum: 1,
      activity: 0.5,
      infectiveDist: 2,
      totalSimuDays: 50,
      simuTime: 0,
      timeClick: 0,
      simuDays: {
        10: "5 days",
        20: "10 days",
        30: "15 days",
        40: "20 days",
        50: "25 days",
        60: "30 days",
        70: "35 days",
        80: "40 days",
        90: "45 days"
      },      
      contactCount: 0,
      agentIdList: [],
    };
  },
  mounted() {
    // this.getStepInfo();
    // this.getUserInfo();
    this.initMap();
  },
  methods: {
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
    initMap() {
      this.map = L.map("map", {
        crs: L.CRS.EPSG3857,
        center: L.latLng(32.07, 118.78),
        zoom: 13
      });

      let tdtVectorMap =
        "http://t0.tianditu.gov.cn/vec_w/wmts?tk=d6b0b78f412853967d91042483385d2c" +
        "&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}";
      let tdtVectorAno =
        "http://t0.tianditu.gov.cn/eva_w/wmts?tk=d6b0b78f412853967d91042483385d2c" +
        "&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=eva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}";
      // "http://t0.tianditu.gov.cn/cva_w/wmts?tk=d6b0b78f412853967d91042483385d2c" +
      // "&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}";

      var vectorMap = L.tileLayer(tdtVectorMap, {
        maxZoom: 20,
        attribution:
          '&copy; <a href="http://map.tianditu.gov.cn/">tianditu</a> contributors'
      });
      var vectorAno = L.tileLayer(tdtVectorAno, { maxZoom: 20 });
      var vector = L.layerGroup([vectorMap, vectorAno]).addTo(this.map);

      // 比例尺
      L.control
        .scale({
          position: "bottomleft"
        })
        .addTo(this.map);

      // diy控件
      this.diyDataControl();

      // 代理地图
      this.agentMap = L.A.agentmap(this.map);
    },
    diyDataControl() {
      var that = this;
      L.Control.Data = L.Control.extend({
        //在此定义参数
        options: {
          position: "topright"
        },
        //在此初始化
        initialize: function(map) {},
        onAdd: function(map) {
          this._container = L.DomUtil.create("div", "leaflet-exportData");
          this._container.style =
            "border:2px solid rgba(128,128,128,0.5);border-radius:6px;background-color:white";

          let importData = document.createElement("div");
          importData.id = "import-data";
          importData.title = "Import data";
          importData.onclick = this._importData;
          let iconImport = document.createElement("img");
          iconImport.src = imIcon;
          iconImport.style = "margin-left: 3.5px;margin-top: 3px";
          importData.appendChild(iconImport);

          this._container.appendChild(importData);
          return this._container;
        },
        _importData() {
          that.modalImport = true;
        }
      });
      L.control.data = function() {
        return new L.Control.Data();
      };
      L.control.data().addTo(this.map);
    },
    handleUpload(file) {
      // if (this.pageParams.pageId == undefined || this.pageParams.pageId == "") {
      //   this.$Message.error("Lose the information of current step.");
      //   return false;
      // }

      if (!/\.(json)$/.test(file.name.toLowerCase())) {
        this.$Message.error("Worry format");
        return false;
      }

      // //上传数据
      // let formData = new FormData();
      // formData.append("file", file);
      // formData.append("description", "for Agent-based SEIR model");
      // formData.append("type", "data");
      // formData.append("uploaderId", this.userInfo.userId);
      // formData.append("privacy", "private");
      // formData.append("folderId", this.pageParams.pageId);
      // this.axios
      //   .post("/GeoProblemSolving/folder/uploadToFolder", formData)
      //   .then(res => {
      //     if (
      //       res.data == "Size over" ||
      //       res.data == "Fail" ||
      //       res.data == "Offline"
      //     ) {
      //       console.log(res.data);
      //     } else if (res.data.length > 0) {
      //       this.showFile = true;
      //       this.uploadDataName = file.name;

      //       let dataName = res.data[0].fileName;
      //       this.dataUrl = "/GeoProblemSolving/resource/upload/" + dataName;

      //       let dataItem = {
      //         name: dataName,
      //         description: "map tool data",
      //         pathURL: "/GeoProblemSolving/resource/upload/" + dataName
      //       };
      //       this.resources.push(dataItem);

      //       // //文件列表协同
      //       // this.send_content = {
      //       //   type: "resourcesUpdate",
      //       //   name: dataName,
      //       //   description: "map tool data",
      //       //   pathURL: "/GeoProblemSolving/resource/upload/" + dataName
      //       // };
      //       // this.socketApi.sendSock(this.send_content, this.getSocketConnect);
      //     }
      //   })
      //   .catch(err => {});
      return false;
    },
    addData() {
      this.axios.get("../../static/testdata.json").then(res => {
        let mapdata = res.data;
        let geoJsonLayer = L.geoJSON(mapdata, {
          style: function(feature) {
            return { color: "green" };
          }
        }).bindPopup(function(layer) {
          return layer.feature.properties.description;
        });
        //平移至数据位置
        this.map.fitBounds(geoJsonLayer.getBounds());
        this.agentMap.buildingify(geoJsonLayer.getBounds(), mapdata);

        // 为了提高效率，将street 和 unit 分开
        // this.agentMap.buildingify(geoJsonLayer.getBounds(), mapdata, style_option, this.unit_layers, this.street_layers);
      });

      // if (/\.(json)$/.test(this.dataUrl.toLowerCase())) {
      //   //从url获取GeoJSON数据
      //   var that = this;
      //   var xhr = new XMLHttpRequest();
      //   xhr.open("GET", this.dataUrl, true);
      //   xhr.onload = function(e) {
      //     if (this.status == 200) {
      //       var file = JSON.parse(this.response);

      //       let geoJsonLayer = L.geoJSON(file, {
      //         style: function(feature) {
      //           return { color: "green" };
      //         }
      //       }).bindPopup(function(layer) {
      //         return layer.feature.properties.description;
      //       });
      //       that.agentMap.addLayer(geoJsonLayer);
      //       //平移至数据位置
      //       that.map.fitBounds(geoJsonLayer.getBounds());
      //     }
      //   };
      //   xhr.send();
      // } else {
      //   this.$Message.error("GeoJson data supported!");
      // }
      // this.showFile = false;
    },
    setSEIRParem() {
      this.infectedRate = 1 / this.exposedTime;
      this.recoveredRate = 1 / this.infectedTime;
      this.exposedRate = this.R0 * this.recoveredRate;
    },
    resetSEIRParem() {
      this.exposedInfect = "No";
      this.exposedTime = 1;
      this.infectedTime = 1;
      this.R0 = 1;
      this.mortality = 0;
      this.exposedRate = 0.1;
      this.infectedRate = 0.1;
      this.recoveredRate = 0.1;
    },
    // agents for susceptible human
    susceptibleAgentMaker(id) {
      let random_index = Math.floor(
          this.agentMap.units.count() * Math.random()
        ),
        random_unit = this.agentMap.units.getLayers()[random_index],
        random_unit_id = this.agentMap.units.getLayerId(random_unit),
        random_unit_center = random_unit.getBounds().getCenter();
      let agent_coords = [random_unit_center.lng, random_unit_center.lat];
      let feature = {
        type: "Feature",
        properties: {
          place: {
            type: "unit",
            id: random_unit_id
          },
          layer_options: {
            color: "green",
            radius: 0.5
          },
          infect_state: "Susceptible"
        },
        geometry: {
          type: "Point",
          coordinates: agent_coords
        }
      };

      return feature;
    },
    // agents for infected human
    infectedAgentMaker(id) {
      let random_index = Math.floor(
          this.agentMap.units.count() * Math.random()
        ),
        random_unit = this.agentMap.units.getLayers()[random_index],
        random_unit_id = this.agentMap.units.getLayerId(random_unit),
        random_unit_center = random_unit.getBounds().getCenter();
      let agent_coords = [random_unit_center.lng, random_unit_center.lat];
      let feature = {
        type: "Feature",
        properties: {
          place: {
            type: "unit",
            id: random_unit_id
          },
          layer_options: {
            color: "red",
            radius: 0.5
          },
          infect_state: "Infected"
        },
        geometry: {
          type: "Point",
          coordinates: agent_coords
        }
      };

      return feature;
    },
    agentController() {
      let _this = this;

      let distPerTrick = (this.activity * this.totalSimuDays * 4) / 5;
      if (this.agentMap.state.ticks % 300 === 0) {
        //计时器
        this.timeClick++;
        let day = Math.floor(100 / this.totalSimuDays);
        if (this.timeClick % day === 0 && this.simuTime < 100) {
          this.simuTime++;
        }
        //智能体状态
        this.agentMap.agents.eachLayer(agent => {
          // 智能体运动
          let random_index = Math.floor(
              _this.agentMap.units.count() * Math.random()
            ),
            random_unit = _this.agentMap.units.getLayers()[random_index],
            random_unit_id = _this.agentMap.units.getLayerId(random_unit),
            random_unit_center = random_unit.getBounds().getCenter();

          agent.scheduleTrip(
            random_unit_center,
            { type: "unit", id: random_unit_id },
            distPerTrick,
            false,
            true
          );

          // 计算患病状态
          // 统计接触人数
          if (agent.infect_state == "Exposed") {
            // 暴露期转为发病期
            let probability = Math.random().toFixed(3);
            if (probability < _this.infectedRate) {
              agent.infect_state = "Infected";
              agent.setStyle({
                color: "red",
                radius: 0.5
              });
            }
          } else if (agent.infect_state == "Infected") {
            // 发病个体转为康复个体
            let probability = Math.random().toFixed(3);
            if (probability < _this.recoveredRate) {
              agent.infect_state = "Recovered";
              agent.setStyle({
                color: "blue",
                radius: 0.5
              });
            }
          }
        });
        // 确定暴露个体(有效接触个体)--------------------??????????????????传染率如何确定+收治发病患者
        for (let i = 0; i < this.agentIdList.length; i++) {
          let transmission_probability = this.exposedRate / this.contactCount;
          let probability = Math.random().toFixed(3);
          if (probability < transmission_probability) {
            this.agentMap.agents.getLayer(this.agentIdList[i]).infect_state =
              "Exposed";
            this.agentMap.agents.getLayer(this.agentIdList[i]).setStyle({
              color: "orange",
              radius: 0.5
            });
          }
        }
        this.contactCount = 0;
        this.agentIdList = [];
      } else {
        this.agentMap.agents.eachLayer(agent => {
          // 如果暴露期(潜伏期)具有传染性，接触暴露期个体
          if (
            agent.infect_state == "Infected" ||
            (_this.exposedInfect == "Yes" && agent.infect_state == "Exposed")
          ) {
            _this.agentMap.agents.eachLayer(_agent => {
              if (_agent.infect_state == "Susceptible") {
                let dist = agent._latlng.distanceTo(_agent._latlng);
                if (dist <= _this.infectiveDist) {
                  _this.agentIdList.push(_agent._leaflet_id);
                  _this.contactCount++;
                }
              }
            });
          }
          agent.moveIt();
        });
      }
    },
    addAgents() {
      this.agentMap.agentify(this.infectedNum, this.infectedAgentMaker);
      this.agentMap.agentify(
        this.totalNum - this.infectedNum,
        this.susceptibleAgentMaker
      );
      this.agentMap.controller = this.agentController;
    },
    resetAgents() {
      this.totalNum = 100;
      this.infectedNum = 1;
      this.activity = 0.5;
      this.agentMap.agents.clearLayers();
      this.simuTime = 0;
      this.timeClick = 0;
    },
    startSimu() {
      this.agentMap.run();
    },
    pauseSimu() {
      this.agentMap.pause();
    },
    endSimu() {},
    setDuration() {
      let offset = this.totalSimuDays / 10;
      this.simuDays = {
        10: offset * 1 + " days",
        20: offset * 2 + " days",
        30: offset * 3 + " days",
        40: offset * 4 + " days",
        50: offset * 5 + " days",
        60: offset * 6 + " days",
        70: offset * 7 + " days",
        80: offset * 8 + " days",
        90: offset * 9 + " days"
      };
    },
    saveSimu() {}
  }
};
</script>