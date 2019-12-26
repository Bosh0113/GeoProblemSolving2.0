<template>
  <div>
    <Row>
      <Form ref="postForm" :model="postForm" label-position="left">
        <FormItem :label-width="70" label="name:">
          <Input v-model="postForm.name"></Input>
        </FormItem>

        <FormItem :label-width="70" label="Des:">
          <Input
            v-model="postForm.des"
            :rows="1"
            type="textarea"
            autosize
            placeholder="Please enter the des"
          />
        </FormItem>

        <!-- <FormItem :label-width="70" label="Process Id:">
        <Input v-model="postForm.pid" :disabled="pidDisabled" placeholder="Please enter the pid" />
        </FormItem>-->

        <!-- <FormItem :label-width="70" label="activityEnum:">
        <Select v-model="postForm.activityEnum" placeholder="请选择" disabled>
          <Option
            v-for="item in activityEnums"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></Option>
        </Select>
        </FormItem>-->

        <!-- <FormItem :label-width="70" label="categoryEnum:">
        <Select v-model="postForm.categoryEnum" placeholder="请选择">
          <Option
            v-for="item in categoryEnums"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></Option>
        </Select>
        </FormItem>-->

        <FormItem :label-width="70" label="topicList:">
          <Select v-model="postForm.topicList" multiple placeholder="请选择">
            <Option
              v-for="item in topicList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></Option>
          </Select>
        </FormItem>

        <!-- <el-form-item style="margin-bottom: 40px;" label-width="70px">
          <Tinymce ref="editor" v-model="postForm.content" :height="400" />
        </el-form-item>-->
      </Form>
    </Row>
    <Row>
        <Col span="2" offset="20">
      <Button @click="submitForm"  type="primary" >POST</Button>
    </Col>
    </Row>
  </div>
</template>

<script>
export default {
  //   components: { Tinymce,tinymceVue },
  data() {
    return {
      pidDisabled: false,
      modelList:[]
    };
  },
  methods: {
   

    async getAllModel() {      
        let data = await this.axios.get(
          "/GeoProblemSolving/modelItem/activity",
          this.postForm
        );
        if (data.status == 200) {
          this.$Notice.success({
            title: "Success",
            desc: "Post the model successfully "
          });
        } else {
          this.$Notice.success({
            title: "Fail",
            desc: "Post the model failed "
          });
        }
        console.log(data);
        //   this.openModal = false;
        let modalOpen = false; 
  },

  created() {

  },

  props: {
    instanceId: {
      type: String
    },
    openModal: {
      type: Boolean
    }
  },
 
};
</script>
