<template>
  <div class="panel-body" id="concept-panel">
    <word-cloud
      :data="conceptList"
      nameKey="name"
      valueKey="frequency"
      :color="myColors"
      :showTooltip="true"
      :wordClick="wordClickHandler"
      :rotate="rotate"
    ></word-cloud>
  </div>
</template>

<script>
import wordCloud from "vue-wordcloud";
export default {
  props: {
    msgConcepts: {
      type: Array
    }
  },
  watch: {
    msgConcepts: {
      handler(val) {
        this.conceptList = val;
      },
      deep: true
    }
  },
  components: { wordCloud },
  data() {
    return {
      conceptList: this.msgConcepts,
      myColors: ["#1f77b4", "#629fc9", "#94bedb", "#c9e0ef"],
      rotate: { from: 0, to: 0, numOfOrientation: 0 }
    };
  },
  methods: {
    wordClickHandler(name, value, vm) {
      console.log("wordClickHandler", name, value, vm);
      let selectConcept = this.conceptList.filter(item => {
        return item.name == name && item.frequency == value;
      });
      window.open(
        "/GeoProblemSolving/conceptMap/getConceptMapInfoByConceptId?conceptId=" +
          selectConcept[0].conceptID,
        "_blank"
      );
    }
  }
};
</script>
<style lang='scss' scoped>
.panel-body {
  width: 400px;
  height: 500px;
}
</style>