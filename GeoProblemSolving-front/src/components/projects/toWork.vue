<template>
    <div>
        <type-choose :projectInfo="projectInfo" :userRole="userRole" v-if="projectInfo.type==''" @changeProjectInfo = "changeProjectInfo"></type-choose>
        <h1 v-else-if="projectInfo.type=='type1'">工作流页面</h1>
        <h1 v-else-if="projectInfo.type=='type2'">工作空间</h1>
    </div>
</template>
<script>
import typeChoose from "./typeChoose.vue"
export default {
    components:{
        typeChoose,
    },
    created(){
        this.getUserInfo();
        this.getProjectInfo();
    },
    data(){
        return{
            projectId: this.$route.params.projectId,
            userInfo: JSON.parse(sessionStorage.getItem("userInfo")),
            projectInfo: JSON.parse(sessionStorage.getItem("projectInfo")),
            userRole: 'visitor',
        }
    },
    methods:{
        getUserInfo(){
            if(this.userInfo=={}||this.userInfo==undefined){
                $.ajax({
                    url: "/GeoProblemSolving/user/state",
                    type: "POST",
                    async: false,
                    success: data=>{
                        if (data) {
                            var userInfo = data;
                            userInfo.userState = true;
                            this.userInfo = userInfo;
                        }
                    },
                    error: function (err) {
                        console.log("Get user info fail.");
                    }
                });
            }
        },
        getProjectInfo(){
            if(this.projectInfo=={}||this.projectInfo==undefined){
                $.ajax({
                url:
                    "/GeoProblemSolving/project/inquiry" +
                    "?key=projectId" +
                    "&value=" +
                    this.projectId,
                type: "GET",
                async: false,
                success: data => {
                    if (data != "None" && data != "Fail") {
                    this.projectInfo = data[0];
                    this.identifyUserRole();
                    this.$store.commit("setProjectInfo", data[0]);
                    } else {
                    console.log(data);
                    }
                }
                });
            }
        },
        identifyUserRole(){
            //Manager|Member|Visitor
            var thisUserId = this.userInfo.userId;
            var projectInfo = this.projectInfo;
            var members = projectInfo.members;
            var managerId = projectInfo.managerId;
            if(managerId == thisUserId){
                this.userRole = "Manager";
            }
            else{
                for(var i=0;i< members.length;i++){
                    var userId = members[i].userId;
                    if(userId == thisUserId){
                        this.userRole = "Member";
                    }
                }
            }
        },
        changeProjectInfo(newProjectInfo){
            if(newProjectInfo.type!="type0"){
                this.projectInfo = newProjectInfo;
            }else{
                parent.projectInfo.type = "type0";
            }
        }
    }
}
</script>
