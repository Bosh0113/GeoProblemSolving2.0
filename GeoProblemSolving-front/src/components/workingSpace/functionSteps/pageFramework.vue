<style scoped>
#stepType{
    width: fit-content;
    margin:0 auto;
    position:absolute;
    top:0;
    left:0;
    right:0;
    bottom:0;
    max-width: 600px;
    display: inline-block;
}
.btnHoverBlue:hover {
  background-color: #2db7f5;
  color: white;
}
.pageBackground{
    background-color:white;
    border: 1px solid #dcdee2;
}
</style>
<template>
    <div>
        <div style="position:relative;padding:10px 10px 0 10px; margin-top:10px">
            <Breadcrumb style="display: inline-block" separator=">">
                <BreadcrumbItem :to="toSubProjectPage">Subproject</BreadcrumbItem>
                <BreadcrumbItem>Work space</BreadcrumbItem>
            </Breadcrumb>
            <h1 id="stepType">{{stepInfo.type}}</h1>
            <step-change style="float:right" :step-info="stepInfo" :subproject-info="subProjectInfo"></step-change>
        </div>
        <Divider style="margin:10px 0"/>
        <div style="display:flex;background-color: #f8f8f9;padding:5px;">
            <div style="width:300px;margin:0 5px;" class="pageBackground" :style="{minHeight:contentHeight - 10 +'px'}">
                <div style="padding:15px;">
                    <h2 style="display:inline-block">Title:</h2>
                    <Button style="float:right;" class="btnHoverBlue" icon="ios-create" size="small" @click="modifyStepShow">Edit</Button>
                    <p style="font-size: medium;"> {{stepInfo.name}}</p>
                    <Divider style="margin:10px 0"/>
                    <h2>Description:</h2><span style="font-size: larger;white-space: pre-line;word-break: break-all;"> {{stepInfo.description}}</span>
                    <Divider style="margin:10px 0"/>
                    <h2>Members:</h2>
                    <online-participant :sub-project-id="stepInfo.subProjectId"></online-participant>
                    <Divider style="margin:10px 0"/>

                </div>
            </div>
            <div style="flex:1;" class="pageBackground">
                <router-view :stepInfo="stepInfo" :userRole="userRole"></router-view>
            </div>
        </div>
        <Modal v-model="modifyStep" title="Modify step name and description">
            <Form
            ref="stepForm"
            :label-width="120"
            label-position="left"
            :model="stepForm"
            :rules="stepRule">
            <FormItem label="Step Name" prop="name">
                <Input
                v-model="stepForm.name"
                :autosize="{minRows: 1,maxRows: 3}"
                />
            </FormItem>
            <FormItem label="Step Description" prop="description">
                <Input
                v-model="stepForm.description"
                type="textarea"
                :rows="5"
                />
            </FormItem>
            </Form>
            <div slot="footer">
            <Button @click="modifyStep = false;">Cancel</Button>
            <Button type="primary" @click="submitModifyStep('stepForm')">Modify</Button>
            </div>
        </Modal>
    </div>
</template>

<script>
import onlineParticipant from "./utils/onlineParticipants";
import stepChange from "./utils/stepChange";
export default {
    components: {
        onlineParticipant,
        stepChange
    },
    created(){
        this.init();
    },
    mounted() {
        window.addEventListener("resize", this.initSize);
    },
    beforeRouteEnter: (to, from, next) => {
        next(vm => {
        if (!vm.$store.getters.userState) {
            next("/login");
        } else {
            if (
            !(
                vm.userRole == "Manager" ||
                vm.userRole == "Member" ||
                vm.userRole == "PManager"
            )
            ) {
            vm.$Message.error("You have no property to access it");
            // next(`/project/${vm.$store.getters.currentProjectId}`);
            vm.$router.go(-1);
            } else {
            next();
            }
        }
        });
    },
    data(){
        return{
            contentHeight:560,
            split:"400px",
            stepInfo:{},
            toSubProjectPage:"",
            subProjectInfo:{},
            projectInfo:{},
            userRole : "Visitor",
            modifyStep: false,
            stepForm: {
                name: "",
                description: ""
            },
            stepRule:{
                name:[{
                    required: true,
                    message: 'The title cannot be empty',
                    trigger: 'blur'
                }],
                description:[
                    { required: false, message: 'Please enter description', trigger: 'blur' },
                    { type: 'string', max: 150, message: 'Descript less than 150 words', trigger: 'blur' }]
            }
        }
    },
    methods:{
        init(){
            this.initSize();
            this.getStepInfo();
            this.getSubprojectInfo();
            this.getProjectInfo();
            this.userRoleIdentity();
        },
        initSize() {
            if (window.innerHeight > 675) {
                this.contentHeight = window.innerHeight - 175;
            } else {
                this.contentHeight = 490;
            }
        },
        getStepInfo(){
            if (
                this.stepInfo.stepId == "" ||
                this.stepInfo.stepId == undefined
            ) {
                $.ajax({
                url:
                    "/GeoProblemSolving/step/inquiry/" +
                    "?key=stepId" +
                    "&value=" +
                    this.$route.params.stepId,
                type: "GET",
                async: false,
                success: data => {
                    if (data == "Offline") {
                        this.$store.commit("userLogout");
                        this.$router.push({ name: "Login" });
                    }
                    else if (data != "None" && data != "Fail") {
                        this.stepInfo = data[0];
                        this.toSubProjectPage =  "/project/" + data[0].subProjectId + "/subproject";
                        } else {
                        this.$Notice.info({
                            desc: "Get the description failed!"
                        });
                    }
                }
                });
            } else {
                this.$Notice.info({
                desc: "Get step information failed!"
                });
            }
        },
        getSubprojectInfo() {
            let subProjectInfo = this.$store.getters.subProject;
            if (
                JSON.stringify(subProjectInfo) != "{}" &&
                subProjectInfo.subProjectId == sessionStorage.getItem("subProjectInfo")
            ) {
                this.$set(this, "subProjectInfo", subProjectInfo);
            } else {
                $.ajax({
                url:
                    "/GeoProblemSolving/subProject/inquiry" +
                    "?key=subProjectId" +
                    "&value=" +
                    this.stepInfo.subProjectId,
                type: "GET",
                async: false,
                success: data => {
                    if (data == "Offline") {
                        this.$store.commit("userLogout");
                        this.$router.push({ name: "Login" });
                    } else if (data != "None" && data != "Fail") {
                        subProjectInfo = data[0];
                        this.$set(this, "subProjectInfo", subProjectInfo);
                        this.$store.commit("setSubProjectInfo", subProjectInfo);
                    } else {
                        console.log(data);
                    }
                },
                error: function(err) {
                    console.log("Get manager name fail.");
                }
                });
            }
        },
        getProjectInfo() {
            let projectInfo = this.$store.getters.project;
            if (
                JSON.stringify(projectInfo) != "{}" &&
                projectInfo.projectId == this.subProjectInfo.projectId
            ) {
                this.projectInfo = projectInfo;
            } else {
                $.ajax({
                url:
                    "/GeoProblemSolving/project/inquiry" +
                    "?key=projectId" +
                    "&value=" +
                    this.subProjectInfo.projectId,
                type: "GET",
                async: false,
                success: data => {
                    if (data == "Offline") {
                        this.$store.commit("userLogout");
                        this.$router.push({ name: "Login" });
                    } else if (data != "None" && data != "Fail") {
                        this.projectInfo = data[0];
                        this.$store.commit("setProjectInfo", data[0]);
                    } else {
                        console.log(data);
                    }
                }
                });
            }
        },
        userRoleIdentity() {
            this.userRole = "Visitor";
            if (this.$store.getters.userState) {
                // 是否是子项目管理员
                if (this.subProjectInfo.managerId === this.$store.getters.userId) {
                    this.userRole = "Manager";
                }
                // 是否是子项目成员
                else {
                for (let i = 0; i < this.subProjectInfo.members.length; i++) {
                    if (
                        this.subProjectInfo.members[i].userId ===
                        this.$store.getters.userId
                    ) {
                        this.userRole = "Member";
                        break;
                    }
                }
                }
                // 是否是项目管理员
                if (this.userRole != "Manager") {
                    if (this.projectInfo.managerId === this.$store.getters.userId) {
                        this.userRole = "PManager";
                    }
                }
            } else {
                this.userRole = "Visitor";
            }
        },
        modifyStepShow(){
            this.stepForm.name = this.stepInfo.name;
            this.stepForm.description = this.stepInfo.description;
            this.modifyStep = true;
        },
        submitModifyStep(name) {
        this.$refs[name].validate(valid => {
            if (valid) {
                let obj = new URLSearchParams();
                obj.append("name", this.stepForm.name);
                obj.append("description", this.stepForm.description);
                obj.append("stepId", this.stepInfo.stepId);

                this.axios
                .post("/GeoProblemSolving/step/update", obj)
                .then(res => {
                if (res.data == "Offline") {
                    this.$store.commit("userLogout");
                    this.$router.push({
                    name: "Login"
                    });
                } else if (res.data != "Fail") {
                    this.$Notice.info({
                    desc: "Update successfully!"
                    });
                    this.stepInfo.name = this.stepForm.name;
                    this.stepInfo.description = this.stepForm.description;
                } else {
                    this.$Message.error("Update step failed.");
                }
                })
                .catch(err => {
                console.log(err.data);
                });
                this.modifyStep = false;
            }});
        },
    }
}
</script>
