<style scoped>
.paramsCard >>> .ivu-card-body {
  padding: 10px 16px;
}
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
      class="paramsCard"
      dis-hover
      style="width: 290px; height: calc(100vh - 10px); background-color: white; margin: 5px;overflow-y: auto"
    >
      <p slot="title">Parameters of SEIR model</p>
      <div id="parameters">
        <Label>Total population:</Label>
        <InputNumber
          size="small"
          :max="1000"
          :min="1"
          v-model="total_popu"
          style="margin: 5px 0 10px 0; width:200px; display:block"
          title="Population"
        ></InputNumber>
        <Label>Number of initial exposed people:</Label>
        <InputNumber
          size="small"
          :max="1000"
          :min="0"
          v-model="exposed_popu"
          style="margin: 5px 0 10px 0; width:200px; display:block"
          title="Population"
        ></InputNumber>
        <Label>Number of initial infected people:</Label>
        <InputNumber
          size="small"
          :max="1000"
          :min="0"
          v-model="infectious_popu"
          style="margin: 5px 0 10px 0; width:200px; display:block"
          title="Population"
        ></InputNumber>
        <Label>Number of initial recovered people:</Label>
        <InputNumber
          size="small"
          :max="1000"
          :min="0"
          v-model="recovered_popu"
          style="margin: 5px 0 10px 0; width:200px; display:block"
          title="Population"
        ></InputNumber>
        <Label>Number of initial dead people:</Label>
        <InputNumber
          size="small"
          :max="1000"
          :min="0"
          v-model="death_popu"
          style="margin: 5px 0 10px 0; width:200px; display:block"
          title="Population"
        ></InputNumber>
        <Divider style="margin: 10px 0;" />
        <Label style="display:block">Infectiousness in exposed duration:</Label>
        <RadioGroup v-model="exposed_infect" style="margin: 5px 0 10px 0; width:200px">
          <Radio label="Yes"></Radio>
          <Radio label="No"></Radio>
        </RadioGroup>
        <Label style="display:block">Mean incubation period(days):</Label>
        <InputNumber
          size="small"
          :max="30"
          :min="0"
          v-model="exposedTime"
          style="margin: 5px 0 10px 0; width:200px"
        ></InputNumber>
        <Label style="display:block">The probability of transmission(person to person) (%):</Label>
        <InputNumber
          size="small"
          :max="30"
          :min="0"
          v-model="transmission_rate"
          style="margin: 5px 0 10px 0; width:200px"
        ></InputNumber>
        <Label style="display:block">The probability of recovery(%/day):</Label>
        <InputNumber
          size="small"
          :max="30"
          :min="0"
          v-model="recovery_rate"
          style="margin: 5px 0 10px 0; width:200px"
        ></InputNumber>
        <Label style="display:block">The probability of death(%/day):</Label>
        <InputNumber
          size="small"
          :max="1"
          :min="0"
          v-model="death_rate"
          style="margin: 5px 0 10px 0; width:200px"
        ></InputNumber>
        <Button @click="setSEIRParem()" style="margin-right:60px">Submit</Button>
        <Button @click="resetSEIRParem()">Reset</Button>
      </div>
    </Card>
    <div>
      <Card
        class="mapCard"
        dis-hover
        style="width: calc(100vw - 310px); height: calc(100vh - 280px); background-color: white; margin: 5px;overflow-x: auto"
      >
        <div id="map" style="width:100%; height:100%; position: absolute;"></div>
      </Card>
      <Card
        dis-hover
        class="agentConsole"
        style="width: calc(100vw - 310px); height: 265px; background-color: white; margin: 5px;"
      >
        <p slot="title">Simulation console</p>
        <div
          slot="extra"
          style="margin-top: -7px; color: grey; cursor:pointer"
          v-show="collaborative && !showMembers"
          @click="showMembers = true"
        >
          Status:
          <span v-if="simulator.Name == undefined">Wait for a simulation.</span>
          <span v-else>{{simulator.Name}} is conducting the simulation.</span>
        </div>
        <div
          slot="extra"
          style="margin-top: -7px; color: grey; cursor:pointer"
          v-show="collaborative && showMembers"
          @click="showMembers = false"
        >
          Online participants:
          <span v-for="item in participants" :key="item">
            <Icon type="md-man" :title="item.Name" />
          </span>
        </div>
        <div id="status">
          <div>
            <Label>Collaboration:</Label>
            <i-switch
              size="large"
              v-model="collaborative"
              @on-change="changeCollab"
              style="margin-right:20px"
            >
              <span slot="open">ON</span>
              <span slot="close">OFF</span>
            </i-switch>
            <Button size="small" style="margin-right:20px" @click="addAgents()">Set agents</Button>
            <Button size="small" @click="removeAgents()">Remove agents</Button>
          </div>
          <Divider style="margin: 20px 0;" />
          <div style="margin-top:10px">
            <Label>Outside activities(per day):</Label>
            <InputNumber
              size="small"
              :max="5"
              :min="0"
              v-model="numActivity"
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
            <Label>Simulation duration(days):</Label>
            <InputNumber
              size="small"
              :max="100"
              :min="1"
              v-model="totalSimuDays"
              style="margin-right:20px"
            ></InputNumber>
            <Button size="small" style="margin-right:40px" @click="submitAgents()">Submit</Button>
            <Label>Quarantine:</Label>
            <Select style="width:100px" @on-change="changeIsolate" size="small">
              <Option v-for="item in quaraObjs" :value="item" :key="item">{{ item }}</Option>
            </Select>
          </div>
          <Divider style="margin: 20px 0;" />
          <Slider v-model="simuTime" :marks="simuDays" disabled></Slider>
          <div style="margin-top:40px">
            <Button size="small" icon="md-play" style="margin-right:20px" @click="startSimu()">Start</Button>
            <Button
              size="small"
              icon="md-pause"
              style="margin-right:20px"
              :disabled="colSimu"
              @click="pauseSimu()"
            >Pause</Button>
            <Button
              size="small"
              icon="ios-play-outline"
              style="margin-right:20px"
              @click="continueSimu()"
              :disabled="colSimu"
            >Continue</Button>
            <Button
              size="small"
              icon="md-square"
              style="margin-right:60px"
              @click="endSimu()"
              :disabled="colSimu"
            >End</Button>
            <Button
              size="small"
              @click="saveSimuModal=true"
              v-show="simu_results.length > 0"
            >Save simulation data</Button>
          </div>
        </div>
      </Card>
    </div>
    <Modal
      v-model="modalImport"
      title="Select and import units/streets data (GeoJson)"
      @on-ok="addData"
      ok-text="OK"
      cancel-text="Cancel"
    >
      <div style="height:300px">
        <vue-scroll :ops="ops">
          <CheckboxGroup v-model="selectedResIds" style="margin-left:10px">
            <Checkbox
              v-for="item in dataResList"
              :key="item.index"
              :label="item.resourceId"
              style="display:block; margin-bottom:5px"
            >
              <span style="margin-left: 10px;">{{item.name}}</span>
              <div style="margin: 5px 0 5px 25px" title="Description">{{item.description}}</div>
            </Checkbox>
          </CheckboxGroup>
        </vue-scroll>
      </div>
    </Modal>
    <Modal v-model="saveSimuModal" title="Save the simulation result">
      <Label style="margin-left: 20px">File name:</Label>
      <Input
        style="margin-left: 10px;width: 300px"
        v-model="resultName"
        placeholder="Enter file name (*.csv)"
      />
      <div slot="footer">
        <Button type="primary" @click="saveSimu()">OK</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
import L from "leaflet";
import A from "../../../static/js/agentmaps.js";
import imIcon from "../../../static/Images/import.png";
import "leaflet/dist/leaflet.css";
export default {
  data() {
    return {
      //样式
      ops: {
        bar: {
          background: "#808695"
        }
      },
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
      selectedResIds: [],
      dataResList: [],
      dataUrl: "",
      unit_layers: null,
      street_layers: null,
      //SEIR
      exposed_infect: "No",
      exposedTime: 10,
      recovery_rate: 10,
      transmission_rate: 5,
      death_rate: 0,
      total_popu: 100,
      exposed_popu: 0,
      infectious_popu: 1,
      recovered_popu: 0,
      death_popu: 0,
      seirParams: {
        exposedInfect: "No",
        exposedRate: 0,
        infectedRate: 0,
        recoveredRate: 0,
        mortalityRate: 0,
        totalNum: 100,
        exposedNum: 0,
        infectiousNum: 1,
        recoveredNum: 0,
        deathNum: 0
      },
      //agent 数据
      agentState: "stop",
      numActivity: 2, //每天外出次数
      infectiveDist: 1, //传染距离
      totalSimuDays: 50, //模拟天数
      agentInteractDist: 1,
      simuTime: 0, //当前的时间
      timeClick: 0,
      simuDays: {
        0: "0",
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
      contact_agentId_list: [],
      // 模拟数据
      simu_results: [],
      currentState: {},
      saveSimuModal: false,
      resultName: "",
      // socket
      collaborative: false,
      showMembers: false,
      quarantine: "No",
      quaraObjs: ["No", "Infected people", "Exposed and infected people"],
      participants: [],
      abseirSocket: null,
      simulator: {},
      colSimu: false
    };
  },
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      if (!vm.$store.getters.userState || vm.$store.getters.userId == "") {
        vm.$router.push({ name: "Login" });
      }
    });
  },
  mounted() {
    this.getStepInfo();
    this.getUserInfo();
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
          that.getMapData();
        }
      });
      L.control.data = function() {
        return new L.Control.Data();
      };
      L.control.data().addTo(this.map);
    },
    getMapData() {
      if (this.pageParams.pageId == undefined || this.pageParams.pageId == "") {
        this.$Message.error("Lost the information of current step.");
        return false;
      }

      this.dataResList = [];
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
                /\.(json)$/.test(res.data.files[i].name.toLowerCase())
              ) {
                this.dataResList.push(res.data.files[i]);
              }
            }
          } else {
            this.dataResList = [];
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    addData() {
      for (var i = 0; i < this.selectedResIds.length; i++) {
        for (var j = 0; j < this.dataResList.length; j++) {
          if (this.selectedResIds[i] == this.dataResList[j].resourceId) {
            if (/\.(json)$/.test(this.dataResList[j].name)) {
              let dataUrl = this.dataResList[j].pathURL;
              // 获取数据
              this.loadMapData(dataUrl);
              // websocket 同步
              if (this.collaborative) {
                let messageJson = {
                  type: "operation",
                  operation: "import_data",
                  data: dataUrl
                };
                this.sendMessage(JSON.stringify(messageJson));
              }
            } else {
              this.$Message.error("Only GeoJson data format supported.");
            }
            break;
          }
        }
      }
    },
    loadMapData(dataUrl) {
      if (this.agentMap.streets != null) {
        this.agentMap.streets.clearLayers();
      }
      if (this.agentMap.units != null) {
        this.agentMap.units.clearLayers();
      }
      if (this.agentMap.agents != null) {
        this.agentMap.agents.clearLayers();
      }
      this.agentMap = L.A.agentmap(this.map);

      this.axios.get(dataUrl).then(res => {
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
        // 为了提高效率，将street 和 unit 分开
        // this.agentMap.buildingify(geoJsonLayer.getBounds(), mapdata, style_option, this.unit_layers, this.street_layers);
        this.agentMap.buildingify(geoJsonLayer.getBounds(), mapdata);
      });
    },
    setSEIRParem(origin) {
      if (this.agentState == "stop") {
        // 传染率
        this.seirParams.totalNum = this.total_popu;
        this.seirParams.exposedNum = this.exposed_popu;
        this.seirParams.infectiousNum = this.infectious_popu;
        this.seirParams.recoveredNum = this.recovered_popu;
        this.seirParams.deathNum = this.death_popu;
        this.seirParams.exposedInfect = this.exposed_infect;
        this.seirParams.exposedRate = this.transmission_rate / 100;
        this.seirParams.infectedRate = 1 / this.exposedTime;
        this.seirParams.recoveredRate = this.recovery_rate / 100;
        this.seirParams.mortalityRate = this.death_rate / 100;

        // websocket 同步
        if (this.collaborative && origin == undefined) {
          let messageJson = {
            type: "operation",
            operation: "SEIR_params",
            data: this.seirParams
          };
          this.sendMessage(JSON.stringify(messageJson));
        }
      } else {
        this.$Notice.open({
          title:
            "A simulation has already been run. Please stop the simulation before."
        });
      }
    },
    resetSEIRParem() {
      if (this.agentState == "stop") {
        // 用户输入参数
        this.total_popu = 100;
        this.exposed_popu = 0;
        this.infectious_popu = 1;
        this.recovered_popu = 0;
        this.death_popu = 0;
        this.exposed_infect = "No";
        this.exposedTime = 10;
        this.recovery_rate = 10;
        this.transmission_rate = 5;
        this.death_rate = 0;

        // seir模型参数
        this.seirParams = {
          exposedInfect: "No",
          exposedRate: 0,
          infectedRate: 0,
          recoveredRate: 0,
          mortalityRate: 0,
          totalNum: 100,
          exposedNum: 0,
          infectiousNum: 1,
          recoveredNum: 0,
          deathNum: 0
        };
        // 易感转为暴露、暴露转为感染、感染转为康复(个体)
        this.seirParams.exposedRate = this.transmission_rate / 100;
        this.seirParams.infectedRate = 1 / this.exposedTime;
        this.seirParams.recoveredRate = this.recovery_rate / 100;
        this.seirParams.mortalityRate = this.death_rate / 100;

        // websocket 同步
        if (this.collaborative) {
          let messageJson = {
            type: "operation",
            operation: "SEIR_params",
            data: this.seirParams
          };
          this.sendMessage(JSON.stringify(messageJson));
        }
      } else {
        this.$Notice.open({
          title:
            "A simulation has already been run. Please stop the simulation before."
        });
      }
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
          infect_state: "Susceptible",
          contact: false
        },
        geometry: {
          type: "Point",
          coordinates: agent_coords
        }
      };

      return feature;
    },
    // agents for exposed human
    exposedAgentMaker(id) {
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
            color: "orange",
            radius: 0.5
          },
          infect_state: "Exposed"
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
    // agents for recovered human
    recoveredAgentMaker(id) {
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
            color: "blue",
            radius: 0.5
          },
          infect_state: "Recovered"
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
      // 移动距离（每个trick）
      let distPerTrick = 1.2;

      if (this.agentMap.state.ticks % 800 === 0) {
        //计时器
        this.timeClick++;

        // 每一天
        let activitiesPerDay = this.numActivity; //每天活动次数
        let offset = 100 / this.totalSimuDays;
        if (this.timeClick % activitiesPerDay === 0 && this.simuTime < 100) {
          this.simuTime = this.simuTime + offset;
          if (!this.colSimu) {
            this.simu_results.push({
              susceptible: this.currentState.susceptible,
              exposed: this.currentState.exposed,
              infectious: this.currentState.infectious,
              recovered: this.currentState.recovered,
              death: this.currentState.death
            });
          }
        }

        //智能体状态
        this.agentMap.agents.eachLayer(agent => {
          let random_index = Math.floor(
              _this.agentMap.units.count() * Math.random()
            ),
            random_unit = _this.agentMap.units.getLayers()[random_index],
            random_unit_id = _this.agentMap.units.getLayerId(random_unit),
            random_unit_center = random_unit.getBounds().getCenter();

          //是否隔离感染者
          if (
            _this.quarantine == "Infected people" &&
            agent.infect_state == "Infected"
          ) {
            let agent_coords = [random_unit_center.lng, random_unit_center.lat];
            agent.geometry = {
              type: "Point",
              coordinates: agent_coords
            };
          } else if (
            _this.quarantine == "Exposed and infected people" &&
            (agent.infect_state == "Exposed" ||
              agent.infect_state == "Infected")
          ) {
            let agent_coords = [random_unit_center.lng, random_unit_center.lat];
            agent.geometry = {
              type: "Point",
              coordinates: agent_coords
            };
          } else {
            // 智能体运动

            agent.scheduleTrip(
              random_unit_center,
              { type: "unit", id: random_unit_id },
              distPerTrick,
              false,
              true
            );
          }

          // 计算患病状态
          if (agent.infect_state == "Exposed") {
            // 暴露期转为发病期
            let probability = Math.random().toFixed(3);
            if (probability < _this.seirParams.infectedRate) {
              agent.infect_state = "Infected";
              agent.setStyle({
                color: "red",
                radius: 0.5
              });
              this.currentState.infectious++;
              this.currentState.exposed--;
            }
          } else if (agent.infect_state == "Infected") {
            // 发病个体转为康复个体
            let probability = Math.random().toFixed(3);
            if (probability < _this.seirParams.recoveredRate) {
              agent.infect_state = "Recovered";
              agent.setStyle({
                color: "blue",
                radius: 0.5
              });
              this.currentState.recovered++;
              this.currentState.infectious--;
            }
          }
        });
        // 确定暴露个体(有效接触个体)
        for (let i = 0; i < this.contact_agentId_list.length; i++) {
          let probability = Math.random().toFixed(3);
          if (probability < this.seirParams.exposedRate / activitiesPerDay) {
            this.agentMap.agents.getLayer(
              this.contact_agentId_list[i]
            ).infect_state = "Exposed";
            this.agentMap.agents
              .getLayer(this.contact_agentId_list[i])
              .setStyle({
                color: "orange",
                radius: 0.5
              });
            this.currentState.exposed++;
            this.currentState.susceptible--;
          } else {
            this.agentMap.agents.getLayer(
              this.contact_agentId_list[i]
            ).contact = false;
          }
        }
        this.contact_agentId_list = [];
      } else {
        this.agentMap.agents.eachLayer(agent => {
          // 如果未被隔离
          // 接触感染个体
          // 如果暴露期(潜伏期)具有传染性，接触暴露期个体
          if (
            (agent.infect_state == "Infected" && _this.quarantine == "No") ||
            (_this.seirParams.exposedInfect == "Yes" &&
              _this.quarantine != "Exposed and infected people" &&
              agent.infect_state == "Exposed")
          ) {
            _this.agentMap.agents.eachLayer(_agent => {
              if (_agent.infect_state == "Susceptible") {
                let dist = agent._latlng.distanceTo(_agent._latlng);
                if (
                  dist <= _this.agentInteractDist &&
                  _agent.contact == false
                ) {
                  _this.contact_agentId_list.push(_agent._leaflet_id);
                  _agent.contact = true;
                }
              }
            });
          }
          agent.moveIt();
        });
      }
    },
    changeCollab(status) {
      if (status) {
        this.openSocket();
      } else {
        if (this.agentState == "run") {
          // 如果主控者断开
          if (this.simulator.Id == this.userInfo.userId) {
            this.simulator = {};
            let messageJson = {
              type: "operation",
              operation: "end",
              simulator: this.simulator
            };
            this.sendMessage(JSON.stringify(messageJson));
          } else {
            this.endSimu(1);
            this.simulator = {};
            this.colSimu = false;
          }
        }
        this.closeSocket();
      }
    },
    addAgents(origin) {
      if (this.agentState == "stop") {
        if (this.agentMap.units == null || this.agentMap.streets == null)
          return;
        // agent重置
        if (this.agentMap.agents != null && this.agentMap.agents != undefined) {
          this.agentMap.agents.clearLayers();
        }
        // 设置agents
        this.agentMap.agentify(
          this.seirParams.totalNum -
            this.seirParams.exposedNum -
            this.seirParams.infectiousNum -
            this.seirParams.recoveredNum,
          this.susceptibleAgentMaker
        );
        this.agentMap.agentify(
          this.seirParams.exposedNum,
          this.exposedAgentMaker
        );
        this.agentMap.agentify(
          this.seirParams.infectiousNum,
          this.infectedAgentMaker
        );
        this.agentMap.agentify(
          this.seirParams.recoveredNum,
          this.recoveredAgentMaker
        );
        this.agentMap.controller = this.agentController;

        // websocket 同步
        if (this.collaborative && origin == undefined) {
          let messageJson = {
            type: "operation",
            operation: "add_agents"
          };
          this.sendMessage(JSON.stringify(messageJson));
        }
      } else {
        this.$Notice.open({
          title:
            "A simulation has already been run. Please stop the simulation before."
        });
      }
    },
    removeAgents(origin) {
      if (this.agentState == "stop") {
        if (this.agentMap.agents != null && this.agentMap.agents != undefined) {
          this.agentMap.agents.clearLayers();

          // websocket 同步
          if (this.collaborative && origin == undefined) {
            let messageJson = {
              type: "operation",
              operation: "remove_agents"
            };
            this.sendMessage(JSON.stringify(messageJson));
          }
        }
      } else {
        this.$Notice.open({
          title:
            "A simulation has already been run. Please stop the simulation before."
        });
      }
    },
    submitAgents(origin) {
      if (this.agentState == "stop") {
        this.agentInteractDist = this.infectiveDist;
        let offset = this.totalSimuDays / 10;
        this.simuDays = {
          0: offset * 0 + " days",
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

        // websocket 同步
        if (this.collaborative && origin == undefined) {
          let messageJson = {
            type: "operation",
            operation: "agent_params",
            data: {
              numActivity: this.numActivity,
              agentInteractDist: this.agentInteractDist,
              totalSimuDays: this.totalSimuDays
            }
          };
          this.sendMessage(JSON.stringify(messageJson));
        }
      } else {
        this.$Notice.open({
          title:
            "A simulation has already been run. Please stop the simulation before."
        });
      }
    },
    changeIsolate(status) {
      this.quarantine = status;

      // websocket 同步
      if (this.collaborative) {
        let messageJson = {
          type: "operation",
          operation: "quarantine",
          data: status
        };
        this.sendMessage(JSON.stringify(messageJson));
      }
    },
    startSimu(origin) {
      if (this.agentMap.agents == null) return;
      if (this.agentState == "stop") {
        this.agentState = "run";

        // 模拟参数重置
        this.simuTime = 0;
        this.timeClick = 0;
        // 模拟数据初始化
        this.simu_results = [];
        this.currentState = {
          susceptible:
            this.seirParams.totalNum -
            this.seirParams.exposedNum -
            this.seirParams.infectiousNum -
            this.seirParams.recoveredNum,
          exposed: this.seirParams.exposedNum,
          infectious: this.seirParams.infectiousNum,
          recovered: this.seirParams.recoveredNum,
          death: this.seirParams.deathNum
        };

        this.agentMap.run();

        // websocket 同步
        if (this.collaborative && origin == undefined) {
          // 模拟者
          this.simulator = {
            Id: this.pageParams.userId,
            Name: this.pageParams.userName
          };

          let messageJson = {
            type: "operation",
            operation: "run",
            simulator: this.simulator
          };
          this.sendMessage(JSON.stringify(messageJson));
        }
      } else {
        this.$Notice.open({
          title:
            "A simulation has already been run. Please stop the simulation before."
        });
      }
    },
    pauseSimu(origin) {
      if (this.agentMap.agents == null) return;
      if (this.agentState == "run") {
        this.agentMap.pause();

        // websocket 同步
        if (this.collaborative && origin == undefined) {
          let messageJson = {
            type: "operation",
            operation: "pause"
          };
          this.sendMessage(JSON.stringify(messageJson));
        }
      }
    },
    continueSimu(origin) {
      if (this.agentMap.agents == null) return;
      if (this.agentState == "run") {
        this.agentMap.run();

        // websocket 同步
        if (this.collaborative && origin == undefined) {
          let messageJson = {
            type: "operation",
            operation: "continue"
          };
          this.sendMessage(JSON.stringify(messageJson));
        }
      }
    },
    endSimu(origin) {
      if (this.agentMap.agents == null) return;
      if (this.agentState == "run") {
        this.agentState = "stop";

        // 模拟参数重置
        this.simuTime = 0;
        this.timeClick = 0;

        // 代理地图
        if (this.agentMap.agents != null && this.agentMap.agents != undefined) {
          this.agentMap.agents.clearLayers();
        }
        this.agentMap.clear();
        this.agentMap = null;
        this.agentMap = L.A.agentmap(this.map);

        // 接触的agents
        this.contact_agentId_list = [];

        // websocket 同步
        if (this.collaborative && origin == undefined) {
          // 模拟者
          this.simulator = {};
          let messageJson = {
            type: "operation",
            operation: "end",
            simulator: this.simulator
          };
          this.sendMessage(JSON.stringify(messageJson));
        }
      }
    },
    saveSimu() {
      if (this.simu_results.length > 0) {
        if (this.resultName != "") {
          this.saveSimuModal = false;

          //格式化
          let result = "Days,Susceptible,Exposed,Infectious,Rrecovered,Death\n";
          for (var i = 0; i < this.simu_results.length; i++) {
            result += i.toString() + ",";
            result += this.simu_results[i].susceptible + ",";
            result += this.simu_results[i].exposed + ",";
            result += this.simu_results[i].infectious + ",";
            result += this.simu_results[i].recovered + ",";
            result += this.simu_results[i].death + "\n";
          }

          //创建blob
          let blob = new Blob(["\ufeff" + result], {
            type: "text/csv,charset=UTF-8"
          });
          var resultBlob = new File([blob], this.resultName + ".csv");

          let resultForm = new FormData();
          resultForm.append("file", resultBlob);
          resultForm.append(
            "description",
            "The Agnet-based SEIR simulation result"
          );
          resultForm.append("type", "data");
          resultForm.append("uploaderId", this.userInfo.userId);
          resultForm.append("privacy", "private");
          resultForm.append("folderId", this.pageParams.pageId);

          this.axios
            .post("/GeoProblemSolving/folder/uploadToFolder", resultForm)
            .then(res => {
              if (
                res.data.sizeOver.length > 0 ||
                res.data.failed.length > 0 ||
                res.data == "Offline"
              ) {
                console.log(res.data);
              } else if (res.data.uploaded.length > 0) {
                this.$Notice.open({
                  title: "Save the simulation result successfully."
                });
              }
            })
            .catch(err => {});
        } else {
          this.$Message.error("Please fill in the file name.");
        }
      } else {
        this.saveSimuModal = false;
        this.$Message.error("There is no simulation result.");
      }
    },
    // socket
    closeSocket() {
      if (this.abseirSocket != null) {
        this.removeTimer();
        this.abseirSocket.close();
      }
    },
    openSocket() {
      if (this.abseirSocket != null) {
        this.abseirSocket = null;
      }
      var abseirSocketURL =
        "ws://" +
        this.$store.state.IP_Port +
        "/GeoProblemSolving/Abseir/" +
        this.pageParams.pageId;
      if (this.$store.state.IP_Port == "localhost:8080") {
        abseirSocketURL =
          "ws://localhost:8081/GeoProblemSolving/Abseir/" +
          this.pageParams.pageId;
      }
      this.abseirSocket = new WebSocket(abseirSocketURL);
      this.abseirSocket.onopen = this.onOpen;
      this.abseirSocket.onmessage = this.onMessage;
      this.abseirSocket.onclose = this.onClose;
      this.abseirSocket.onerror = this.onError;
      this.setTimer();
    },
    onOpen() {
      console.log("Socket连接成功！");
    },
    onMessage(e) {
      let msg = JSON.parse(e.data);
      if (msg.type == "operation") {
        switch (msg.operation) {
          case "import_data": {
            this.loadMapData(msg.data);
            break;
          }
          case "SEIR_params": {
            this.seirParams = msg.data;
            this.setSEIRParem(1);
            break;
          }
          case "add_agents": {
            this.addAgents(1);
            break;
          }
          case "remove_agents": {
            this.removeAgents(1);
            break;
          }
          case "agent_params": {
            this.numActivity = msg.data.numActivity;
            this.agentInteractDist = msg.data.agentInteractDist;
            this.totalSimuDays = msg.data.totalSimuDays;
            this.submitAgents(1);
            break;
          }
          case "run": {
            this.startSimu(1);
            this.simulator = {
              Id: msg.simulator.Id,
              Name: msg.simulator.Name
            };
            this.colSimu = true;
            break;
          }
          case "pause": {
            this.pauseSimu(1);
            break;
          }
          case "continue": {
            this.continueSimu(1);
            break;
          }
          case "end": {
            this.endSimu(1);
            this.simulator = {};
            this.colSimu = false;
            break;
          }
          case "members": {
            this.participants = JSON.parse(msg.userList);
            break;
          }
          case "quarantine": {
            this.quarantine = msg.data;
          }
        }
      }
    },
    onClose(e) {
      this.removeTimer();
      this.collaborative = false;
      console.log("Socket连接断开！");
    },
    onError(e) {
      this.removeTimer();
      this.collaborative = false;
      console.log("Socket连接错误！");
    },
    sendMessage(socketContent) {
      this.abseirSocket.send(socketContent);
    },
    setTimer() {
      var that = this;
      this.timer = setInterval(() => {
        var messageJson = {};
        messageJson["type"] = "ping";
        messageJson["message"] = "ping";
        if (
          this.collaborative &&
          that.abseirSocket != null &&
          that.abseirSocket != undefined
        ) {
          that.sendMessage(JSON.stringify(messageJson));
        }
      }, 20000);
    },
    removeTimer() {
      clearInterval(this.timer);
    }
  }
};
</script>