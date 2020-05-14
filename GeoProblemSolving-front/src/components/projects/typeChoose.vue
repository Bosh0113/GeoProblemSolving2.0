<template>
    <div>
        <div style="margin-top:20px">
            <div style="text-align: center;">
                <div style="max-width:80%;text-align: left;display: inline-block;margin-buttom:2%">
                    <span style="margin:25px 0; font-size:28px">Here </span>
                    <span style="margin: 0px 10px;font-size: 14px;">
                        you can select different types of workspaces to support collaborative geo-problem solving. This platform provides three workspace types according to different participatory levels. (Once the workspace type selected, and you will enter the selected workspace the next time.)
                    </span>
                </div>
            </div>
            <Row type="flex" justify="space-around" class="code-row-bg" style="margin-top:20px">
                <Col span="7">
                    <Card style="min-height: 400px;">
                        <div style="text-align:center;">
                            <img src="../../../src/assets/images/subProjects.jpg" style="height:150px"/>
                        </div>
                        <div style="margin:10px 0;min-height: 85px;">
                            <p style="text-indent:2em">
                                In this workspace, you can divide a complex problem into a series of sub-problems. Different subprojects can be established for distinct sub-problems. 
                            </p>
                        </div>
                        <div style="padding: 0 5%;min-height: 85px;">
                            <ul>
                                <li>Distinct purposes for sub-problems</li>
                                <li>Complex relationship of participants</li>
                                <li>Different participatory levels</li>
                            </ul>
                        </div>
                        <div style="text-align:center;margin-top:2%" v-if="userRole != 'Visitor'">
                            <h3>
                            To: 
                            <a @click="selectTypeModalShow('type0')">Create subprojects</a>
                            </h3>
                        </div>
                    </Card>
                </Col>
                <Col span="7">
                    <Card style="min-height: 400px;">
                        <div style="text-align:center;">
                            <img src="../../../src/assets/images/designWorkflow.png" style="height:150px"/>
                        </div>
                        <div style="margin:10px 0;min-height: 85px;">
                            <p style="text-indent:2em">
                                In this workspace, you can analyze and design the process for solving a geo-problem. Several iterable steps are used to construct the process. Collaborative activities are conducted in each step.
                            </p>
                        </div>
                        <div style="padding: 0 5%;min-height: 85px;">
                            <ul>
                                <li>Clear problem-solving process</li>
                                <li>Iterable steps with different functions</li>
                                <li>Directed graph workflow based process construction</li>
                            </ul>
                        </div>
                        <div style="text-align:center;margin-top:2%" v-if="userRole != 'Visitor'">
                            <h3>
                            To: 
                            <a @click="selectTypeModalShow('type1')">Design problem-solving workflow</a>
                            </h3>
                        </div>
                    </Card>
                </Col>
                <Col span="7">
                    <Card style="min-height: 400px;">
                        <div style="text-align:center;">
                            <img src="../../../src/assets/images/startWork.png" style="height:150px"/>
                        </div>
                        <div style="margin:10px 0;min-height: 85px;">
                            <p style="text-indent:2em">
                                In this workspace, You can start your work directly. Kinds of tools are available for you to solve a simple geo-problem.
                            </p>
                        </div>
                        <div style="padding: 0 5%;min-height: 85px;">
                            <ul>
                                <li>Start working immediately</li>
                                <li>A pure workspace with many tools</li>
                            </ul>
                        </div>
                        <div style="text-align:center;margin-top:2%" v-if="userRole != 'Visitor'">
                            <h3>
                            To: 
                            <a @click="selectTypeModalShow('type2')">Do it right now</a>
                            </h3>
                        </div>
                    </Card>
                </Col>
            </Row>
        </div>
        <Modal
        v-model="selectTypeModal"
        title="Set workspace type"
        @on-ok="setType()"
        ok-text="OK"
        cancel-text="Cancel"
        >
        <h2>Are you sure to select this type?</h2>
        <small style="color:#03A9F4">* Once the workspace type selected, and you will enter the selected workspace the next time.</small>
        </Modal>
    </div>
</template>
<script>
export default {
    props: ["projectInfo", "userRole"],
    mounted(){
        $("#app").css("min-width","0");
    },
    data(){
        return {
            selectType : "type0",
            selectTypeModal:false
        };
    },
    methods:{
        selectTypeModalShow(type){
            this.selectType = type;
            this.selectTypeModal = true;
        },
        setType(){
            let Step = {};
            Step["name"] = this.projectInfo.title;
            Step["type"] = "General";
            Step["description"] = this.projectInfo.description;
            Step["projectId"] = this.projectInfo.projectId;
            Step["subProjectId"] = "";
            Step["creator"] = this.$store.getters.userId;
            Step["toolList"] = [];
            Step["toolsetList"] = [];
            Step["content"] = {};
            this.axios
            .post("/GeoProblemSolving/step/createForType", Step)
            .then(res => {
            if (res.data == "Offline") {
                parent.location.href="/GeoProblemSolving/login"
            } else if (res.data === "Fail") {
                this1.$Message.info("Fail");
            } else {
                var stepId = res.data;
                let obj = new URLSearchParams();
                obj.append("projectId", this.projectInfo.projectId);
                obj.append("type", this.selectType);
                obj.append("stepId", stepId);
                this.axios
                    .post("/GeoProblemSolving/project/update", obj)
                    .then(res => {
                        this.selectTypeModal = false;
                        if (res.data == "Offline") {
                            parent.location.href="/GeoProblemSolving/login"
                        } else if (res.data != "Fail") {
                            this.$store.commit("setProjectInfo", res.data);
                            this.$emit("changeProjectInfo", res.data);
                        } else {
                            this.$Message.error("Set type failed.");
                        }
                        })
                    .catch(err => {
                    console.log(err.data);
                    });
            }
            })
            .catch(err => {
            console.log(err.data);
            });
        }
    }
};
</script>
