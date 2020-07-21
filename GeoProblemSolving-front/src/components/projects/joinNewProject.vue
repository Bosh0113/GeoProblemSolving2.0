<style scoped>
</style>

<template>
<div>
  <Row>
    <Col span="18" offset="3" :style="{height:contentHeight}">
    <div style="display:flex;justify-content:center;margin-top:100px;height:100%;padding:100px">
      <Form :label-width="120">
        <FormItem label="ProjectName">
          <Input v-model="projectName" disabled style="width:600px"></Input>
        </FormItem>
        <br>
          <FormItem label="ProjectId" style="display:none">
          <Input v-model="projectId" disabled style="width:600px"></Input>
        </FormItem>
        <FormItem label="Email" >
          <Input v-model="email" disabled style="width:600px"></Input>
        </FormItem>
        <br>
        <div v-show="registeredHintShow==true" style="margin-left:120px;display:flex;align-items:center;width:600px"><Icon type="ios-information-circle-outline" :size="30" color="yellowGreen"/><span style="font-size:10px;">{{this.registeredHint}}<br>Now you need to fill the password in the blank and then click the <strong style="font-weight:bold;color:blue">Join</strong> button later you will be a member of the project.</span></div>
        <div v-show="unregisteredHintShow==true" style="margin-left:120px;display:flex;align-items:center;width:600px"><Icon type="ios-information-circle-outline" :size="30" color="red"/><span style="font-size:10px;">{{this.unregisteredHint}}<br>If you input your own password here and press <strong style="font-weight:bold;color:blue">Sign up and Join</strong> button, your own account would be created based on the email presented above and you will join the project at the same time.</span></div>
        <FormItem label="Password" v-show="passwordInputShow" style="margin-top:20px">
          <Input v-model="password" type="password" style="width:600px"></Input>
        </FormItem>
        <br>
        <div style="display:flex;justify-content:center">
          <Button type="default" @click="joinByMail()">
            <span style="font-weight:bold;font-size:1.2em" v-show="unregisteredHintShow==true">Sign up and Join</span>
            <span style="font-weight:bold;font-size:1.2em" v-show="registeredHintShow==true">Join</span>
          </Button>
        </div>
      </Form>
    </div>
    </Col>
  </Row>
</div>

</template>
<script>
import md5 from "js-md5";
export default {
  components: { md5 },
  data() {
    return {
      headerWidth: "",
      contentHeight:"",
      email:"",
      password:"",
      projectId:"",
      passwordInputShow:false,
      registeredHintShow:false,
      unregisteredHintShow:false,
      registeredHint:"This email has been registered,you have been a member in our platform.",
      // unregisteredHint:"Sorry,you are not a user in our platform,this email will be used to create a new account for you,you only need to set a password for log in,and if you want to enrich your personal information,you can go to userSpace to enrich them.",
      unregisteredHint:"It seems that this email is not a registed user in our platform.",
      unregisteredOperate:"If you input your own password here and press Submit button,your own account would be created based on the email presented above and you will join the project at the same time.",
      // navaigation页面的变量
       //消息机制
      noticeSocket: null,
      unreadNoticeCount: 0,
      timer: null,
      // 项目信息
      projectInfo:[],
      // 项目名
      projectName:"",
    };
  },
  mounted(){
    // navigation页面的
    this.getProjectName();
    this.headerWidth = window.innerWidth + "px";
    this.contentHeight = window.innerHeight - 120 + 'px';
    this.projectId = this.$route.params.id;
    this.email = this.$route.params.email;
    this.judgeMailRegiste();
  },
  computed: {
    userState() {
      return this.$store.getters.userState;
    }
  },
  methods:{
      getProjectName(){
        this.axios.get("/PExploration/project/inquiry?key=projectId&&value=" + this.$route.params.id)
        .then(res=> {
          if(res.data!="None"){
            this.projectInfo = res.data;
            this.projectName = this.projectInfo[0].title;
          }
        })
        .catch(err=>{

        })
      },
      judgeMailRegiste(){
        this.axios
        .get(
          "/PExploration/user/isRegistered?" +
            "email=" +
            this.email
        )
        .then(res => {
          if (res.data === true) {
            this.registeredHintShow = true;
            // this.$Notice.info({
            //   desc: "This email has been registerd",
            //   top:200,
            // });
            this.$Message.info("This email has been registerd");
            this.passwordInputShow = true;
          } else if (res.data === false) {
            this.unregisteredHintShow = true;
            this.$Message.info("you have use your email to register an account");
            this.passwordInputShow = true;
          }
        })
        .catch(err => {
          this.$Message.error("Judge fail");
        });
    },
    joinByMail(){
      var passwordMD5 = md5(this.password);
      this.axios
        .get(
          "/PExploration/project/joinByMail" +
            "?projectId="+
            this.projectId+
            "&email=" +
            this.email+
            "&password="+
            passwordMD5
        )
        .then(res => {
          let gotoProjectId = this.projectId;
          if (res.data === "Success") {
            this.$Message.info("Join successfuly");
            this.autoLogin(this.email,passwordMD5,gotoProjectId);
          } else if (res.data === "Fail") {
            this.$Message.info("Fail to join in this project");
          }else if(res.data === "Exist"){
            this.$Message.info("you have been in this group,no need to apply again");
            this.autoLogin(this.email,passwordMD5,gotoProjectId);
          }else if(res.data === "None"){
            this.$Message.info("this group doesn't exist");
          }else if(res.data === "Password"){
            this.$Message.info("password might input error, try it again");
          }
        })
        .catch(err => {
          this.$Message.error("Join fail");
        });
    },
    autoLogin(email,passsword,projectId){
      this.axios
        .get(
          "/PExploration/user/login" +
            "?email=" +
            email +
            "&password=" +
            passsword
        )
        .then(res => {
          if (res.data === "Email") {
            this.$Message.error("Email does not exist.");
          } else if (res.data === "Password" || res.data === "Fail") {
            this.$Message.error("Invalid account or password.");
          } else {
            this.$store.commit("userLogin", res.data);
            window.location.href="/PExploration/projectDetail/"+projectId;
          }
        });
    }
  }
};
</script>
