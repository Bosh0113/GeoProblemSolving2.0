<!-- records -->
<template>
  <div class>
    <Row>
      <DatePicker
        type="date"
        placeholder="Select date"
        class="date_pick"
        format="yyyy-MM-dd"
        :value="query_date"
        @on-change="queryTimeLike"
      ></DatePicker>
    </Row>
    <!-- 搜索框 -->
    <Row class="search_msg">
      <Input search placeholder="Enter something..." enter-button @on-search="queryContentLike" />
    </Row>
    <!-- 聊天记录列表 -->
    <Row class="record_list">
      <vue-scroll :ops="ops" :style="{height:panelHeight - 200 +'px'}">
        <div class="box" v-infinite-scroll="load" infinite-scroll-disabled="disabled">
          <div v-for="(list,index) in currentPageData" :key="index" class="record">
            <Row style="margin-bottom:2.5px">
              <Row>
                <div class="message_from">
                  <div class="src" v-if="list.srcUserId == srcId">{{list.srcUserName}}</div>
                  <div class="target" v-else>{{list.srcUserName}}</div>
                </div>
                <div class="create_time">{{list.createTime}}</div>
              </Row>
              <Row>
                <div style class="content">{{list.content}}</div>
              </Row>
            </Row>
          </div>
          <p v-if="loading" class="loading_icon">
            <Icon class="el-icon-loading" :size="30" />
          </p>
          <p v-if="noMore" class="no_records">No More Reocrds</p>
        </div>
      </vue-scroll>
    </Row>
    <!-- 分页 -->
    <!-- <Row class="record_pages">
      <Page
        :current="currentPage"
        :total="totalMsg"
        :page-size="pageSize"
        @on-change="changeRecordPage"
        simple
        ref="page"
      />
    </Row>-->
  </div>
</template>

<script>
import { get, del, post, put } from "@/axios";
export default {
  props: {
    showTab: {
      type: Boolean
    },
    groupId: {
      type: String
    },
    userId: {
      type: String
    }
  },
  data() {
    return {
      query_date: "",
      // 聊天记录
      msgRecords: [],
      totalMsg: 0, //数据总条数
      totalPages: 1,
      currentPage: 1, //当前页数 ，默认为1
      pageSize: 15, // 每页显示数量
      currentPageData: [], //当前页显示内容
      roomId: this.groupId,
      srcId: this.userId,
      ops: {
        vueScroll: {},
        scrollPanel: {},
        rail: {
          opacity: "0.1",
          border: "1px solid #f2f2f2",
          size: "6px"
        },
        bar: {
          size: "6px",
          background: "#999",
          keepShow: true
        }
      },
      loading: false,
      //日期选择器选择的index
      msgR_datepicker: [], //日期选择器的第一条记录
      msgindex: 0,
      msgR_prev: [],
      msgR_next: [],

      //height
      panelHeight: 0
    };
  },
  computed: {
    noMore() {
      return this.currentPage >= this.totalPages - 1;
    },
    disabled() {
      return this.loading || this.noMore;
    }
  },
  watch: {},
  methods: {
    //日期选择器
    async queryTimeLike(seledate) {
      this.currentPageData = [];
      let data = await get(
        `/GeoProblemSolving/message/timeLike/${this.roomId}/${seledate}`
      );
      this.currentPageData.push(...data);
    },

    //查找total currentPage
    async getTotalCount() {
      let data = await get(`/GeoProblemSolving/message/totalCount`);
      if (data != 0) {
        this.totalMsg = data;
        this.totalPages = Math.ceil(this.totalMsg / this.pageSize);
        console.log(this.totalPages);
      } else {
        this.totalMsg = data;
        this.$Message.warning("This is no record here!");
      }
    },

    //inifte scroll

    load() {
      this.loading = true;
      setTimeout(() => {
        this.currentPage += 1; //页数+1
        this.loadMessageRecords(); //调用接口，此时页数+1，查询下一页数据
        this.loading = false;
      }, 2000);
    },

    //按页码查询
    async loadMessageRecords() {
      let data = await get(
        `/GeoProblemSolving/message/inquiryByPage/${this.roomId}/${this.currentPage}/${this.pageSize}`
      );
      this.currentPageData.push(...data);
    },

    //聊天记录分页,没有按日期查找，倒序排列
    // changeRecordPage(index) {
    //   this.currentPage = index;
    //   this.loadMessageRecords();
    // },
    async queryContentLike(val) {
      let { data } = await get(
        `/GeoProblemSolving/message/contentLike/${this.roomId}/${val}`
      );
      this.currentPageData = data;
    },
    initSize() {
      this.panelHeight = window.innerHeight;
      console.log(this.panelHeight);
    }
  },
  created() {
    this.getTotalCount();
    this.loadMessageRecords();
  },
  mounted() {
    this.initSize();
  }
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

.record_board {
  padding: 0 15px;
}

.record {
  padding: 5px 25px;
}
// col left
.message_from {
  float: left;
  width: 150px;
  font-size: 16px;
}
//col right
.create_time {
  color: lightgray;
  float: left;
  width: 150px;
  font-size: 16px;
}

.src {
  color: #37881f;
}
.target {
  color: #2d8cf0;
  float: left;
  width: 300px;
  font-size: 16px;
}

.content {
  color: gray;
  font-size: 18px;
  word-wrap: break-word;
  width: 100%;
}
.box {
  width: 100%;
  height: 100%;
  position: absolute;
  overflow-y: auto;
}
.loading_icon {
  text-align: center;
  margin: 10px 0;
}
.no_records {
  margin-top: 10px;
  font-size: 20px;
  color: #ccc;
  text-align: center;
  font-style: italic;
  font-weight: 600;
  margin: 10px 0;
}
</style>