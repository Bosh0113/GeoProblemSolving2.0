<template>
  <div class="project form">
    <h1>New Project</h1>
    <Form ref="formInline" :model="formInline" class="projectForm">
      <!-- 选择类别 -->
      <FormItem prop="category">
        <div style="display:flex" class="inline_style">
          <div class="hintTitle">Category</div>
          <Select v-model="formInline.category" style="width:80%">
            <Option v-for="item in cityList" :value="item.value" :key="item.value">{{ item.label }}</Option>
          </Select>
        </div>
      </FormItem>
      <FormItem prop="title">
        <div class="inline_style">
          <div class="hintTitle">title</div>
          <Input v-model="formInline.title" placeholder="Enter Title..." style="width:80%" />
        </div>
      </FormItem>
      <FormItem prop="introduction">
        <div class="inline_style">
          <div class="hintTitle">Introduction</div>
          <Input v-model="formInline.introduction" type="textarea" :autosize="{minRows: 4,maxRows: 5}" placeholder="Enter a simple description about this project..." style="width:80%" />
        </div>
      </FormItem>
      <FormItem prop="Privacy">
        <div class="inline_style">
          <div class="hintTitle">Privacy</div>
          <RadioGroup v-model="formInline.privacy" style="width:80%">
          <Radio label="Public" title="Other users can find the group and see who has membership."></Radio>
          <Radio label="Discoverable" title="Other users can find this group, but membership information is hidden."></Radio>
          <Radio label="Private" title="Other users can not find this group."></Radio>
          </RadioGroup>
        </div>
      </FormItem>
      <FormItem>
        <div class="inline_style">
          <Button type="success" @click="createProject()" class="create">Create</Button>
        </div>
      </FormItem>
    </Form>
  </div>
</template>
<style scoped>
h1 {
  text-align: center;
  margin-top: 2.5%;
}
.projectForm {
  width: 60%;
  margin-top: 2.5%;
  margin-left: 20%;
  margin-right: 20%;
}
.inline_style{
  display:flex
}
.hintTitle {
  text-align: center;
  width: 20%;
}
.create{
  width:20%;
  margin-right:40%;
  margin-left:40%
}
</style>
<script>
export default {
  data() {
    return {

      //创建的新topic应该包含标题，介绍，详情，创建者，评论数，点赞数，踩数（可以放在）
      formInline: {
        title:"",
        category:"",
        introduction:"",
        privacy:"",
        participants:[
          {name:'111',uid:'dsdadasfdsfsdf',avatar:'dsfsdfdfsg'},
          {name:'123',uid:"dsfsdfqddscsff",avatar:'djksfhuweh'}
        ]
      },
      cityList: [
        {
          value: "Society",
          label: "Society"
        },
        {
          value: "Atmosphere",
          label: "Atmosphere"
        },
        {
          value: "Ecology",
          label: "Ecology"
        },
        {
          value: "Soil",
          label: "Soil"
        },
        {
          value: "Water",
          label: "Water"
        },
        {
          value: "Others",
          label: "Others"
        }
      ],
      category: ""
    };
  },
  methods:{
    createProject(){
      var formData = new URLSearchParams();
      formData.append("category",this.formInline.category);
      formData.append("title",this.formInline.title);
      formData.append("introduction",this.formInline.introduction);
      formData.append("privacy",this.formInline.privacy);
      formData.append("participants",JSON.stringify(this.formInline.participants));
      formData.append("createTime",new Date());
      formData.append("uid","lyc");
      // formData.append("module",this.formInline.module);
      this.axios.post("/GeoProblemSolving/TeamModeling/newProjectServlet", formData)
      .then(function (res) {
        if(res.data === "ok"){
          alert("成功");
          this.router.push({name: "Project"})
        }
      })
      .catch(function (err) {
        throw err;
      })
    }
  }
};
</script>
