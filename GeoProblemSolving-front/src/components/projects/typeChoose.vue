<template>
    <div>
        <div style="margin-top:20px">
            <div style="text-align: center;">
                <div style="max-width:80%;text-align: left;display: inline-block;margin-buttom:2%">
                    <span style="margin:25px 0; font-size:28px">This content</span>
                    <span style="margin: 0px 10px;font-size: 14px;">
                        can be customized to support collaborative geo-problem solving. And it provided by this platform can be classified to three types, each designed for different levels of needs. The type of this page will be determined after selection, and the selection will no longer be displayed when you open this page again.
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
                                In this kind of workspace, you can divide the problem into a series of small problems. Different subprojects are for distinct purposes. 
                            </p>
                        </div>
                        <div style="padding: 0 5%;min-height: 85px;">
                            <ul>
                                <li>Clear problem boundaries</li>
                                <li>Management by department</li>
                                <li>More levels of workspace</li>
                            </ul>
                        </div>
                        <div style="text-align:center;margin-top:2%">
                            <h3>
                            To: 
                            <a @click="selectTypeModalShow('type0')">Create sub-projects</a>
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
                                In this kind of workspace, you can design the process of solving a geographic problem into a workflow that uses iterable functional steps. Then collaborate together in each active node. 
                            </p>
                        </div>
                        <div style="padding: 0 5%;min-height: 85px;">
                            <ul>
                                <li>Clear process boundaries</li>
                                <li>Iterable steps for different functions</li>
                                <li>Directed graph workflow editing</li>
                                <li>Provides a large number of tools</li>
                            </ul>
                        </div>
                        <div style="text-align:center;margin-top:2%">
                            <h3>
                            To: 
                            <a @click="selectTypeModalShow('type0')">Design workflow graph</a>
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
                                In this kind of workspace, You can quickly enter the working state and easily use any resources. You can directly regard this type as a node of the previous type.
                            </p>
                        </div>
                        <div style="padding: 0 5%;min-height: 85px;">
                            <ul>
                                <li>Start working immediately</li>
                                <li>Provides a large number of tools</li>
                            </ul>
                        </div>
                        <div style="text-align:center;margin-top:2%">
                            <h3>
                            To: 
                            <a @click="selectTypeModalShow('type1')">Do it right now</a>
                            </h3>
                        </div>
                    </Card>
                </Col>
            </Row>
        </div>
        <Modal
        v-model="selectTypeModal"
        title="Set type of this content"
        @on-ok="setType()"
        ok-text="OK"
        cancel-text="Cancel"
        >
        <h2>Do you sure to select this type?</h2>
        <small style="color:#03A9F4">* The type of this page will be determined after selection, and the selection will no longer be displayed when you open this page again.</small>
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
            .post("/GeoProblemSolving/step/create", Step)
            .then(res => {
            if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({ name: "Login" });
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
                            this.$store.commit("userLogout");
                            this.$router.push({ name: "Login" });
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
