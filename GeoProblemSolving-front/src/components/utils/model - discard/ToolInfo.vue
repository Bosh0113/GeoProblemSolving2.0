<style scoped>
.inline_style {
  display: flex;
}
/* 上传图片 */
.demo-upload-list {
  display: inline-block;
  width: 60px;
  height: 60px;
  text-align: center;
  line-height: 60px;
  border: 1px solid transparent;
  border-radius: 4px;
  /* overflow-x: hidden; */
  /* overflow-y: scroll; */
  background: #fff;
  position: relative;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.2);
  margin-right: 4px;
}
.demo-upload-list img {
  width: 100%;
  height: 100%;
}
.demo-upload-list-cover {
  display: none;
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.6);
}
.demo-upload-list:hover .demo-upload-list-cover {
  display: block;
}
.demo-upload-list-cover i {
  color: #fff;
  font-size: 20px;
  cursor: pointer;
  margin: 0 2px;
}
.uploadAvatar {
  position: relative;
  width: 58px;
  height: 58px;
  top: 0;
  left: 0;
  outline: none;
  background-color: transparent;
  opacity: 0;
}
.uploadBox {
  display: inline-block;
  width: 58px;
  height: 58px;
  line-height: 58px;
  border-width: 0.75px;
  border-style: dashed;
  border-color: lightslategray;
}
</style>
<template>
  <div>
    <Form ref="toolInfo" :model="toolInfo" :label-width="80" class="toolForm">
      <FormItem label="Tool name:" prop="name" :label-width="140">
        <Input v-model="toolInfo.name" placeholder="Enter the name of your tool"></Input>
      </FormItem>
      <FormItem label="Tool description:" prop="description" :label-width="140">
        <Input
          v-model="toolInfo.description"
          type="textarea"
          placeholder="Enter description of your tool"
        />
      </FormItem>

      <FormItem label="Categroy tag:" prop="categoryTag" :label-width="140">
        <Input
          v-model="inputToolTag"
          placeholder="Enter some tag to classify your tools"
          style="width: 400px"
          @keyup.enter.native="addCreateToolTag(inputToolTag)"
        />
        <Button
          icon="ios-add"
          type="dashed"
          size="small"
          @click="addCreateToolTag(inputToolTag)"
          style="margin-left:2.5%"
        >Add tag</Button>
        <div>
          <Tag
            color="primary"
            v-for="(item,index) in this.toolInfo.categoryTag"
            :key="index"
            closable
            @on-close="deleteCreateToolTag(index)"
          >{{item}}</Tag>
        </div>
        <div>
          <span>Example:</span>
          <Tag style="cursor:default">vector</Tag>
          <Tag style="cursor:default">raster</Tag>
          <Tag style="cursor:default">evaluation</Tag>
        </div>
      </FormItem>
      <FormItem label="Image:" prop="toolImg" :label-width="140">
        <div class="inline_style">
          <div class="demo-upload-list" v-if="image!=''">
            <template>
              <img :src="image" />
              <div class="demo-upload-list-cover">
                <Icon type="ios-eye-outline" @click.native="handleView()"></Icon>
                <Icon type="ios-trash-outline" @click.native="handleRemove()"></Icon>
              </div>
            </template>
          </div>
          <div class="uploadBox">
            <Icon type="ios-camera" size="20" style="position:absolute;margin:18px;"></Icon>
            <input
              id="choosePicture"
              @change="uploadPhoto($event)"
              type="file"
              class="uploadAvatar"
              accept="image/*"
            />
          </div>
          <br />
          <Modal title="View Image" v-model="visible">
            <img :src="image" v-if="visible" style="width: 100%" />
          </Modal>
        </div>
      </FormItem>
      <FormItem label="Privacy:" prop="privacy" :label-width="140">
        <RadioGroup v-model="toolInfo.privacy">
          <Radio label="Public">Public</Radio>
          <Radio label="Private">Private</Radio>
        </RadioGroup>
      </FormItem>
      <Button type="success" @click="createTool('toolInfo')" class="create">Create</Button>
    </Form>
  </div>
</template>
<script>
export default {
  data() {
    return {
      selectModelRoute: this.modelRoute,
      selectModel: this.modelItem,
      selectModelName: "",
      selectModelDes: "",
      selectModelUrl: "",
      toolInfo: {
        name: "",
        description: "",
        tool_url: "",
        recomStep: [],
        categoryTag: [],
        toolImg: "",
        privacy: "Private"
      },
      userId: "testUserId",
      inputToolTag: "",
      visible: false,
      //表示图片
      image: ""
    };
  },

  methods: {
    createTool(tool) {
      let createToolForm = {};
      createToolForm["toolName"] = this.modelItem.name;
      createToolForm["toolUrl"] = this.selectModelRoute;
      createToolForm["description"] = this.modelItem.des;
      createToolForm["provider"] = this.userId;
      createToolForm["privacy"] = "Private";
      this.axios
        .post("/GeoProblemSolving/tool/create", createToolForm)
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data === "Fail") {
            this.$Notice.error({ desc: "Create tool fail." });
          } else if (res.data === "Duplicate naming") {
            this.$Notice.error({ desc: "The name already exists." });
          } else {
            this.createToolModal = false;
            this.$Notice.info({ desc: "Create successfully" });
            this.personalTools.push(res.data);
            if (this.toolInfo.privacy == "Public") {
              this.publicTools.push(res.data);
            }
            this.filterShowListByType();
          }
        });
    },

    getModelDetail() {
      let modelItemId = this.selectModel.id;
      let route = `/modelItem?id=${modelItemId}`;

      let url = window.location.href;
      let index = url.indexOf("toolTemplate");
      let urlPrefix = url.slice(0, index - 1);
      this.selectModelUrl = `${urlPrefix}${route}`;

      this.toolInfo.tool_url = this.selectModelUrl;
      this.toolInfo.name = this.selectModel.name;
      this.toolInfo.description = this.selectModel.des;
      console.log(this.toolInfo);
    },

    addCreateToolTag(tag) {
      if (tag != "") {
        this.toolInfo.categoryTag.push(tag);
        this.inputToolTag = "";
      }
    },
    deleteCreateToolTag(index) {
      this.toolInfo.categoryTag.splice(index, 1);
    },
    handleView() {
      this.visible = true;
    },
    handleRemove() {
      this.image = "";
      this.toolInfo.toolImg = "";
    }
  },
  props: {
    modelItem: {
      type: Object
    }
  },

  watch: {
    //   modelRoute(val) {
    //     this.selectModelRoute = val; //监听选择的model的route
    //     // console.log(val);
    //   },
    modelItem(val) {
      this.selectModel = val;
      this.getModelDetail();
    }
  }
};
</script>
