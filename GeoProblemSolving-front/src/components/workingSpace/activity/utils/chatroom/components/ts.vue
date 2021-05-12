<!-- records -->
<template>
  <div class>
    <Row class="record_list">
      <my-scroll :on-reach-bottom="handleReachBottom" >
        <el-card
          v-for="(item, index) in list1"
          :key="index"
          style="margin: 32px 0"
        >Content {{ item }}</el-card>
      </my-scroll>

      <!-- </div> -->
    </Row>
  </div>
</template>

<script>
import MyScroll from "@/components/common/infiniteTopScroll";
export default {
  props: {},
  components: { MyScroll },
  data() {
    return {
      list1: [
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        10,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        10,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        10,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        10,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        10,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        10,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        10,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        10,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        10,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        10,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        10
      ]
    };
  },
  computed: {},
  watch: {},
  methods: {
    //日期选择器

    handleReachBottom() {
      return new Promise(resolve => {
        setTimeout(() => {
          const last = this.list1[this.list1.length - 1];
          for (let i = 1; i < 11; i++) {
            this.list1.push(last + i);
          }
          resolve();
        }, 2000);
      });
    }
  },
  created() {},
  mounted() {},
  
};
</script>
<style lang='scss' scoped>
.date_pick {
  padding: 10px;
  width: 60%;
  margin-left: 2.5%;
  margin-right: 2.5%;
}

.date_pick_btn {
  width: 20%;
  margin-left: 5%;
  margin-right: 5%;
  height: 32px;
  margin-top: 10px;
}

.search_msg {
  padding: 10px;
  /* background-color: lightblue; */
  width: 90%;
  // margin-left: 2.5%;
}

.record_pages {
  height: 25px;
  // padding: 10px;
  /* line-height: 15px; */
  text-align: center;
}

.record_board {
  padding: 0 15px;
}

.record {
  padding: 5px;
}
.src {
  color: #37881f;
  margin-right: 2%;
  float: left;
  width: 250px;
  font-size: 16px;
}
.target {
  color: #2d8cf0;
  margin-right: 2%;
  float: left;
  width: 300px;
  font-size: 16px;
}
.create_time {
  color: lightgray;
  float: left;
  width: 250px;
  font-size: 16px;
}
.content {
  color: gray;
  font-size: 18px;
  word-wrap: break-word;
  width: 460px;
}
</style>
