<template>
  <div class="main">
    <div class="cards" v-for="(item,index) in recordList" :key="index">
      <Card class="cardItem">
        <!-- <Button @click="detailModal">detail</Button> -->
        <Button @click="postModal(item.id)">Post</Button>
        <div class="cardContent" @click="showDetail">
          <div>{{item.name}}</div>
          <div>{{item.createTime}}</div>
          <div>{{item.modelItem.des}}</div>
        </div>
      </Card>
    </div>
    <div>
      <Modal v-model="openPostModal" class="modal">
        <p slot="header" class="modalHead">
          <span>Post Model</span>
        </p>
        <post
          :instanceId="selectInstanceId"
          :openModal="openPostModal"
          @changeModalState="changeModalState"
        ></post>
        <div slot="footer">
          <!-- <Button type="error" size="large" long :loading="modal_loading" @click="del">Delete</Button> -->
        </div>
      </Modal>

      <Modal v-model="openDetailModal" v-for="(item,index) in recordList" :key="index">
        <p slot="header" class="modalHead">
          <span>Tool Detail</span>
        </p>
        <div>
          <div style="float:left">Tool Name:</div>
          <div>{{item.name}}</div>
          <div style="float:left">Create Time:</div>
          <div>{{item.createTime}}</div>

          <br />
          <div style="float:left">Model Item:</div>
          <div>{{item.modelItem.name}}</div>
          <!-- <Collapse simple>
            <Panel name="1">
              Model Item:
              <p slot="content">{{item.modelItem}}</p>
            </Panel>
          </Collapse>-->
        </div>
      </Modal>
    </div>
  </div>
</template>

<script>
import modal_post from "./ModalPost";

export default {
  data() {
    return {
      modelItem: {},
      modelInstance: {},
      userId: "testUserId1234",
      stepId: "testStepId1234",
      recordList: [],
      openPostModal: false,
      openDetailModal: false,
      selectInstanceId: ""
    };
  },

  methods: {
    async getAllRecords() {
      let stepId = this.stepId;
      let data = await this.axios.get(
        `/GeoProblemSolving/modelItem/getAllRecords/${stepId}`
      );
      if (data.status == "200") {
        console.log("返回记录成功");
        let recordList = data.data.data;
        for (let i = 0; i < recordList.length; i++) {
          let modelInstanceId = recordList[i].modelInstanceId;
          let item = await this.axios.get(
            `/GeoProblemSolving/modelItem/getModelInstance/${modelInstanceId}`
          );
          this.recordList.push(item.data.data);
          console.log(this.recordList);
        }
      } else {
        console.log("返回记录失败");
      }
    },

    postModal(instanceId) {
      console.log(instanceId);
      this.openPostModal = true;
      this.selectInstanceId = instanceId;
    },
    changeModalState(modalOpen) {
      this.openPostModal = modalOpen;
    },
    showDetail() {
      //show the model detail
      this.openDetailModal = true;
    }
  },

  created() {
    this.getAllRecords();
    console.log(window.location.href);
  },

  components: {
    post: modal_post
  }
};
</script>
<style scoped>
.cards {
  margin-top: 10px;
}

.modal >>> .ivu-modal-footer {
  border-top: 0;
  padding: 0;
}

.cardContent:hover {
  cursor: pointer;
}
</style>

