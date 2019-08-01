<template>
  <div class="participants">
    <h1>{{msg}}</h1>
    <br>
    <Row>
      <Col span="22" offset="1">
        <div>
          <ve-line :data="chartData" :settings="chartSettings"></ve-line>
        </div>
        <div>
          <ve-tree :data="treeData"></ve-tree>
        </div>
        <div>
        </div>
      </Col>
    </Row>
  </div>
</template>
<script>

const treeData = {
    name: 'f',
    value: 1,
    link: 'https://ele.me',
    children: [
      {
        name: 'a',
        value: 1,
        link: 'https://ele.me',
        children: [
          {
            name: 'a-a',
            link: 'https://ele.me',
            value: 2
          },
          {
            name: 'a-b',
            link: 'https://ele.me',
            value: 2
          }
        ]
      },
      {
        name: 'b',
        value: 1,
        link: 'https://ele.me',
        children: [
          {
            name: 'b-a',
            link: 'https://ele.me',
            value: 2
          },
          {
            name: 'b-b',
            link: 'https://ele.me',
            value: 2
          }
        ]
      },
      {
        name: 'c',
        value: 3,
        link: 'https://ele.me',
        children: [
          {
            name: 'c-a',
            link: 'https://ele.me',
            value: 4
          },
          {
            name: 'c-b',
            link: 'https://ele.me',
            value: 2
          }
        ]
      },
      {
        name: 'd',
        value: 3,
        link: 'https://ele.me',
        children: [
          {
            name: 'd-a',
            link: 'https://ele.me',
            value: 4
          },
          {
            name: 'd-b',
            link: 'https://ele.me',
            value: 2
          }
        ]
      }
    ]
  }
import VCharts from "./../../utils/VCharts";
export default {
  name: "Participants",
  methods: {},
  data() {
    this.chartSettings = {
        stack: { '用户': ['访问用户', '下单用户'] },
        area: true
      }
    return {
      msg: "Welcome to Participants",
      chartData: {
        columns: ["日期", "访问用户", "下单用户", "下单率"],
        rows: [
          { 日期: "1/1", 访问用户: 1393, 下单用户: 1093, 下单率: 0.32 },
          { 日期: "1/2", 访问用户: 3530, 下单用户: 3230, 下单率: 0.26 },
          { 日期: "1/3", 访问用户: 2923, 下单用户: 2623, 下单率: 0.76 },
          { 日期: "1/4", 访问用户: 1723, 下单用户: 1423, 下单率: 0.49 },
          { 日期: "1/5", 访问用户: 3792, 下单用户: 3492, 下单率: 0.323 },
          { 日期: "1/6", 访问用户: 4593, 下单用户: 4293, 下单率: 0.78 }
        ]
      },
      treeData: {
        columns:['name','value'],
        rows: [
          {
            name: 'tree1',
            value: [treeData]
          }
        ]
      }
    };
  },
  components: {}
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1 {
  font-weight: normal;
  text-align: center;
}
#editor {
  margin: auto;
  width: 80%;
  height: 580px;
}
</style>

