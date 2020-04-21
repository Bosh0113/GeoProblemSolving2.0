<style>
@import "../../../../static/css/Control.MiniMap.css";
@import "../../../../static/css/leaflet.pm.css";
#map {
  float: right;
}
#import-data {
  background-color: white;
  width: 30px;
  height: 30px;
  border-bottom: 1px solid lightgray;
  cursor: pointer;
}
#import-data:hover {
  background-color: #f3f3f3;
  width: 30px;
  height: 30px;
  border-bottom: 1px solid lightgray;
  cursor: pointer;
}
#export-data {
  background-color: white;
  width: 30px;
  height: 30px;
  cursor: pointer;
}
#export-data:hover {
  background-color: #f3f3f3;
  width: 30px;
  height: 30px;
  cursor: pointer;
}
</style>
<template>
  <div>
    <toolStyle
      :style="{height:windowHeight+'px'}"
      :resources="resources"
      v-on:resourceUrl="selecetResource"
    ></toolStyle>
    <div id="map" class="map" :style="{height:windowHeight+'px', width:windowWidth+'px'}">
      <Modal
        v-model="modalExport"
        title="Export GeoJSON to resource center"
        @on-ok="save2Resource('formValidate')"
      >
        <Form
          ref="formValidate"
          :model="formValidate"
          :rules="ruleValidate"
          :label-width="80"
          style="margin-left:20px"
        >
          <FormItem label="Name" prop="fileName">
            <Input v-model="formValidate.fileName" placeholder="*.json" style="width: 350px" />
          </FormItem>
          <FormItem label="Description" prop="fileDescription">
            <Input
              v-model="formValidate.fileDescription"
              type="textarea"
              placeholder="Enter something..."
              style="width: 350px"
            />
          </FormItem>
        </Form>
        <p style="margin-left:30px">
          Download this data directly.
          <Button style="margin-left:50px" @click="downloadGeoJson('formValidate')">Download</Button>
        </p>
      </Modal>
      <Modal
        v-model="modalImport"
        title="Import GeoJSON data to resource center and show the data"
        @on-ok="viewData"
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
  </div>
</template>
<script>
import minimap from "../../../../static/js/Control.MiniMap.min.js";
import pm from "../../../../static/js/leaflet.pm.min.js";
import imIcon from "../../../../static/Images/import.png";
import exIcon from "../../../../static/Images/export.png";
//leaflet
import L from "leaflet";
import shp from "shpjs";
import "leaflet/dist/leaflet.css";
import toolStyle from "./toolStyle";
// this part resolve an issue where the markers would not appear
delete L.Icon.Default.prototype._getIconUrl;
//请求带上cookie以防session丢失
L.Icon.Default.mergeOptions({
  iconRetinaUrl: require("leaflet/dist/images/marker-icon-2x.png"),
  iconUrl: require("leaflet/dist/images/marker-icon.png"),
  shadowUrl: require("leaflet/dist/images/marker-shadow.png")
});
export default {
  components: { toolStyle },
  data() {
    return {
      windowHeight: window.innerHeight,
      windowWidth: window.innerWidth - 60,
      modalExport: false,
      modalImport: false,
      map: null,
      baseLayers: null,
      traces: {},
      send_content: {},
      //geojson blob
      geojsonBlob: null,
      showFile: false,
      uploadDataName: "",
      //存储绘制的图像layer
      drawingLayerGroup: null,
      resources: [],
      dataUrl: "",
      formValidate: {
        fileName: "",
        fileDescription: ""
      },
      ruleValidate: {
        fileName: [
          { required: true, message: "Please select type...", trigger: "blur" }
        ],
        fileDescription: [
          { required: false, message: "Drawing tool", trigger: "blur" }
        ]
      },
      pageParams: { pageId: "", userId: "", userName: "" },
      userInfo: {}
    };
  },
  mounted() {
    window.addEventListener("resize", this.initSize);
    this.initSize();
    this.getStepInfo();
    this.getUserInfo();
    this.initMap();
    this.initLayer();
    this.initControl();
    this.getResources();
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.initSize);
  },
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      if (!vm.$store.getters.userState || vm.$store.getters.userId == "") {
        vm.$router.push({ name: "Login" });
      } else {
      }
    });
  },
  methods: {
    initSize() {
      $("#app").css("min-width", "0");
      $("#app").css("min-height", "0");
      this.windowHeight = window.innerHeight;
      this.windowWidth = window.innerWidth - 60;
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
    initMap() {
      this.tdtVectorMap =
        "http://t0.tianditu.gov.cn/vec_w/wmts?tk=d6b0b78f412853967d91042483385d2c" +
        "&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}";
      this.tdtVectorAno =
        "http://t0.tianditu.gov.cn/cva_w/wmts?tk=d6b0b78f412853967d91042483385d2c" +
        "&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}";
      this.tdtImgMap =
        "http://t0.tianditu.gov.cn/img_w/wmts?tk=d6b0b78f412853967d91042483385d2c" +
        "&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}";
      this.tdtImgAno =
        "http://t0.tianditu.gov.cn/cia_w/wmts?tk=d6b0b78f412853967d91042483385d2c" +
        "&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}";
      this.tdtTerrMap =
        "http://t0.tianditu.com/ter_w/wmts?tk=d6b0b78f412853967d91042483385d2c" +
        "&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=ter&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}";
      this.tdtTerrAno =
        "http://t0.tianditu.com/cta_w/wmts?tk=d6b0b78f412853967d91042483385d2c" +
        "&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cta&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}";

      this.map = L.map("map", {
        crs: L.CRS.EPSG3857,
        center: L.latLng(32.07, 118.78),
        zoom: 13
      });
    },
    initLayer() {
      this.drawingLayerGroup = L.layerGroup([]);
      this.drawingLayerGroup.addTo(this.map);
    },
    initControl() {
      // 图层控件
      var vectorMap = L.tileLayer(this.tdtVectorMap, {
        maxZoom: 20,
        attribution:
          '&copy; <a href="http://map.tianditu.gov.cn/">tianditu</a> contributors'
      });
      var vectorAno = L.tileLayer(this.tdtVectorAno, { maxZoom: 18 });
      var vector = L.layerGroup([vectorMap, vectorAno]);

      var satelliteMap = L.tileLayer(this.tdtImgMap, {
        maxZoom: 18,
        attribution:
          '&copy; <a href="http://map.tianditu.gov.cn/">tianditu</a> contributors'
      });
      var satelliteAno = L.tileLayer(this.tdtImgAno, { maxZoom: 18 });
      var satellite = L.layerGroup([satelliteMap, satelliteAno]);

      var terrainMap = L.tileLayer(this.tdtTerrMap, {
        maxZoom: 18,
        attribution:
          '&copy; <a href="http://map.tianditu.gov.cn/">tianditu</a> contributors'
      });
      var terrainAno = L.tileLayer(this.tdtTerrAno, { maxZoom: 18 });
      var terrain = L.layerGroup([terrainMap, terrainAno]);

      this.baseLayers = {
        "Vector map": vector,
        "Satellite map": satellite,
        "Terrain map": terrain
        // "Google satellite map": googleSatellite
      };
      var overlayLayers = {};
      L.control.layers(this.baseLayers, overlayLayers).addTo(this.map);
      this.baseLayers["Vector map"].addTo(this.map);

      // 比例尺
      L.control
        .scale({
          position: "bottomleft"
        })
        .addTo(this.map);

      // 鹰眼
      var normal = L.tileLayer(this.tdtVectorMap, { maxZoom: 18 });
      var miniMap = new L.Control.MiniMap(normal, {
        toggleDisplay: true,
        minimized: false,
        position: "bottomleft"
      }).addTo(this.map);

      // 绘图控件
      var options = {
        position: "topleft", // toolbar position, options are 'topleft', 'topright', 'bottomleft', 'bottomright'
        drawMarker: true, // adds button to draw markers
        drawPolyline: true, // adds button to draw a polyline
        drawRectangle: true, // adds button to draw a rectangle
        drawPolygon: true, // adds button to draw a polygon
        drawCircle: true, // adds button to draw a cricle
        cutPolygon: true, // adds button to cut a hole in a polygon
        editMode: true, // adds button to toggle edit mode for all layers
        dragMode: true,
        removalMode: true // adds a button to remove layers
      };
      this.map.pm.addControls(options);

      this.diyDataControl();
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
            "border:2px solid rgba(128,128,128,0.5);border-radius:6px";

          let importData = document.createElement("div");
          importData.id = "import-data";
          importData.title = "Import data";
          importData.onclick = this._importData;
          let iconImport = document.createElement("img");
          iconImport.src = imIcon;
          iconImport.style = "margin-left: 3.5px;margin-top: 3px";
          importData.appendChild(iconImport);

          let exportData = document.createElement("div");
          exportData.id = "export-data";
          exportData.title = "Export GeoJSON";
          exportData.onclick = this._exportData;
          let iconExport = document.createElement("img");
          iconExport.src = exIcon;
          iconExport.style = "margin-left: 3.5px;margin-top: 3px";
          exportData.appendChild(iconExport);

          this._container.appendChild(importData);
          this._container.appendChild(exportData);
          return this._container;
        },
        _exportData() {
          var featuresSet = { type: "FeatureCollection", features: [] };
          that.map.eachLayer(function(layer) {
            try {
              var json = layer.toGeoJSON();
              if (json.type == "Feature") {
                featuresSet.features.push(json);
              }
            } catch (e) {}
          });
          if (featuresSet.features.length > 0) {
            that.geojsonBlob = new Blob(
              [JSON.stringify(featuresSet, null, 2)],
              { type: "application/json" }
            );
            that.modalExport = true;
          }
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
    downloadGeoJson(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          var reader = new FileReader();
          if (this.geojsonBlob != null) {
            let filename = "";
            if (!/\.(json)$/.test(this.formValidate.fileName.toLowerCase())) {
              filename = this.formValidate.fileName + ".json";
            } else {
              filename = this.formValidate.fileName;
            }
            reader.readAsDataURL(this.geojsonBlob);
            reader.onload = function(e) {
              var a = document.createElement("a");
              a.download = filename;
              a.href = e.target.result;
              $("body").append(a);
              a.click();
              $(a).remove();
            };
          }
        } else {
          this.$Message.error("Please enter the necessary information!");
        }
      });
    },
    save2Resource(name) {
      if (this.pageParams.pageId == undefined || this.pageParams.pageId == "") {
        this.$Message.error("Lose the information of current step.");
        return false;
      }

      this.$refs[name].validate(valid => {
        if (valid) {
          if (this.geojsonBlob != null) {
            // 完善文件信息
            let filename = "";
            if (!/\.(json)$/.test(this.formValidate.fileName.toLowerCase())) {
              filename = this.formValidate.fileName + ".json";
            } else {
              filename = this.formValidate.fileName;
            }
            let description = "";
            if (this.formValidate.fileDescription == "") {
              description = "from Map tool";
            } else {
              description = this.formValidate.fileDescription;
            }

            //上传数据
            let fileOfBlob = new File([this.geojsonBlob], filename);
            let formData = new FormData();
            formData.append("file", fileOfBlob);
            formData.append("description", description);
            formData.append("type", "data");
            formData.append("uploaderId", this.userInfo.userId);
            formData.append("privacy", "private");
            formData.append("folderId", this.pageParams.pageId);
            this.axios
              .post("/GeoProblemSolving/folder/uploadToFolder", formData)
              .then(res => {
                if (res.data.uploaded.length > 0) {
                  this.showFile = true;
                  this.uploadDataName = filename;

                  this.$Notice.open({
                    title: "Save to resource center",
                    desc: "Data saved successfully",
                    duration: 0
                  });

                  // 文件列表更新
                  let dataName = res.data[0].fileName;
                  let dataItem = {
                    name: filename,
                    description: "map tool data",
                    pathURL: "/GeoProblemSolving/resource/upload/" + dataName
                  };
                  this.resources.push(dataItem);

                  // 初始化formValidation
                  this.formValidate = {
                    fileName: "",
                    fileDescription: ""
                  };
                }
              })
              .catch(err => {});
          }
        } else {
          this.$Message.error("Please enter the necessary information!");
        }
      });
    },
    handleUpload(file) {
      if (this.pageParams.pageId == undefined || this.pageParams.pageId == "") {
        this.$Message.error("Lose the information of current step.");
        return false;
      }
      if (!/\.(json)$/.test(file.name.toLowerCase())) {
        this.$Message.error("Worry format");
        return false;
      }

      //上传数据
      let formData = new FormData();
      formData.append("file", file);
      formData.append("description", "Map tool data");
      formData.append("type", "data");
      formData.append("uploaderId", this.userInfo.userId);
      formData.append("privacy", "private");
      formData.append("folderId", this.pageParams.pageId);
      this.axios
        .post("/GeoProblemSolving/folder/uploadToFolder", formData)
        .then(res => {
          if (res.data.uploaded.length > 0) {
            this.showFile = true;
            this.uploadDataName = file.name;

            let dataName = res.data[0].fileName;
            this.dataUrl = "/GeoProblemSolving/resource/upload/" + dataName;

            let dataItem = {
              name: dataName,
              description: "map tool data",
              pathURL: "/GeoProblemSolving/resource/upload/" + dataName
            };
            this.resources.push(dataItem);
          }
        })
        .catch(err => {});
      return false;
    },
    viewData() {
      if (/\.(json)$/.test(this.dataUrl.toLowerCase())) {
        //从url获取GeoJSON数据
        var that = this;
        var xhr = new XMLHttpRequest();
        xhr.open("GET", this.dataUrl, true);
        xhr.onload = function(e) {
          if (this.status == 200) {
            var file = JSON.parse(this.response);

            let geoJsonLayer = L.geoJSON(file, {
              style: function(feature) {
                return { color: "green" };
              }
            }).bindPopup(function(layer) {
              return layer.feature.properties.description;
            });
            that.drawingLayerGroup.addLayer(geoJsonLayer);
            //平移至数据位置
            that.map.fitBounds(geoJsonLayer.getBounds());
          }
        };
        xhr.send();
      } else if (/\.(zip)$/.test(this.dataUrl.toLowerCase())) {
        try {
          var that = this;
          shp(this.dataUrl).then(function(file) {
            let geoJsonLayer = L.geoJSON(file, {
              style: function(feature) {
                return { color: "orange" };
              }
            }).bindPopup(function(layer) {
              return layer.feature.properties.description;
            });
            that.drawingLayerGroup.addLayer(geoJsonLayer);
            that.map.fitBounds(geoJsonLayer.getBounds());
          });
        } catch (res) {
          this.$Message.error("Worry data format!");
        }
      } else {
        this.$Message.error("Worry data format!");
      }

      this.showFile = false;
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
                /\.(json|zip)$/.test(res.data.files[i].name.toLowerCase())
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
    }
  }
};
</script>
