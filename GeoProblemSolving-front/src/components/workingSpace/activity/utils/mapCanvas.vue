<style scoped>
.map {
  width: -webkit-fill-available;
  height: calc(100ch - 180px);
  z-index: 10;
}
</style>
<template>
  <div>
    <div id="map" class="map"></div>
  </div>
</template>

<script>
//leaflet
import L from "leaflet";
import "leaflet/dist/leaflet.css";

export default {
  props: ["stepInfo",  "userRole"],
  data() {
    return {};
  },
  mounted() {
    window.addEventListener("resize", this.refreshMap);
    this.initMap();
    this.initLayer();
    this.initControl();
    this.refreshMap();
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.refreshMap);
  },
  methods: {
    refreshMap() {
      this.map.panTo(this.map.getCenter());
      this.map.setZoom(this.map.getZoom());
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
        zoom: 3
      });
    },
    initLayer() {
      this.drawingLayerGroup = L.layerGroup([]);
      this.drawingLayerGroup.addTo(this.map);
    },
    initControl() {
      // 图层控件
      var vectorMap = L.tileLayer("", {
        maxZoom: 20
      });
      var empty = L.layerGroup([vectorMap]);

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
        "Map container": empty,
        "Vector map": vector,
        "Satellite map": satellite,
        "Terrain map": terrain
        // "Google satellite map": googleSatellite
      };
      var overlayLayers = {};
      L.control.layers(this.baseLayers, overlayLayers).addTo(this.map);
      this.baseLayers["Satellite map"].addTo(this.map);

      // 比例尺
      L.control
        .scale({
          position: "bottomleft"
        })
        .addTo(this.map);

      //   // 鹰眼
      //   var normal = L.tileLayer(this.tdtVectorMap, { maxZoom: 18 });
      //   var miniMap = new L.Control.MiniMap(normal, {
      //     toggleDisplay: true,
      //     minimized: false,
      //     position: "bottomleft"
      //   }).addTo(this.map);

      //   // 绘图控件
      //   var options = {
      //     position: "topleft", // toolbar position, options are 'topleft', 'topright', 'bottomleft', 'bottomright'
      //     drawMarker: true, // adds button to draw markers
      //     drawPolyline: true, // adds button to draw a polyline
      //     drawRectangle: true, // adds button to draw a rectangle
      //     drawPolygon: true, // adds button to draw a polygon
      //     drawCircle: true, // adds button to draw a cricle
      //     cutPolygon: false, // adds button to cut a hole in a polygon
      //     editMode: false, // adds button to toggle edit mode for all layers
      //     dragMode: false,
      //     removalMode: true // adds a button to remove layers
      //   };
      //   this.map.pm.addControls(options);

      //   this.diyDataControl();
    }
  }
};
</script>