<style scoped>
.btnHoverRed:hover {
  background-color: #ed4014;
  color: white;
}
</style>

<template>
    <div>
        <Card dis-hover>
            <h1 slot="title">Workspace</h1>
            <div slot="extra">
            </div>
            <!-- <Row>
                <Col span="5">
                <Card style="margin-top: 20px 0 0 10px" dis-hover>
                    <h2 slot="title">Members:</h2>
                <online-participant
                    :online-participants="onlineParticipants"
                    :offline-participants="offlineParticipants"
                ></online-participant>
                </Card>
                </Col>
                <Col span="19" style="margin-top:20px">
                <Row>
                    <Col span="22" offset="1">
                        <Collapse simple v-model="unfold">
                        <Panel name="data">
                            Data list
                            <data-list slot="content" :stepInfo="stepInfo" :userRole="userRole"></data-list>
                        </Panel>
                        <Panel name="tool">
                            Toolbox
                            <tool-container slot="content" :stepInfo="stepInfo" :userRole="userRole"></tool-container>
                        </Panel>
                        </Collapse>
                    </Col>
                </Row>
                </Col>
            </Row> -->
            <data-list  :stepInfo="stepInfo" :userRole="userRole"></data-list>
            <div style="margin:5px 0 5px 80px;text-align:center">
                <Button class="btnHoverRed" @click="resetProjectTypeModalShow()">Reset project's type</Button>
            </div>
        </Card>
        <Modal
        v-model="resetProjectTypeModel"
        title="Reset project's type"
        >
        <h2>Are you sure you want to reset the project type?</h2>
        <small style="color:red">* All operation and data in this page can't be restore any more.</small>
        <div slot="footer">
            <Button type="primary" @click="resetProjectType()">Submit</Button>
        </div>
        </Modal>
    </div>
</template>
<script>
import dataList from "./../workingSpace/noStep/dataList";
import toolContainer from "./../workingSpace/functionSteps/utils/toolContainer";
import onlineParticipant from "./../workingSpace/functionSteps/utils/onlineParticipants";
export default {
    props: ["projectInfo", "userRole"],
    components: {
        dataList,
        toolContainer,
        onlineParticipant
    },
    created(){
        this.getStepInfo();
    },
    data(){
        return{
            stepInfo:{},
            unfold: ["tool", "data"],
            participants: [],
            // onlineParticipants.vue
            onlineUserIdList: [],
            onlineParticipants: [],
            offlineParticipants: [],
            // messagePanel.vue -- chat
            receivedChatMsgs: [],
            resetProjectTypeModel:false
        }
    },
    methods:{
        getStepInfo(){
            this.axios
            .get("/GeoProblemSolving/step/inquiry?key=stepId&value=" + this.projectInfo.stepId)
            .then(res => {
                if (res.data == "Offline") {
                    parent.location.href="/GeoProblemSolving/login"
                } else if (res.data != "None" && res.data != "Fail") {
                    this.$set(this, "stepInfo", res.data[0]);
                    this.getParticipants();
                }
            })
            .catch(err => {});
        },
        resetProjectTypeModalShow(){
            this.resetProjectTypeModel = true;
        },
        resetProjectType(){
        let obj = new URLSearchParams();
        obj.append("projectId", this.stepInfo.projectId);
        obj.append("type", "");
        obj.append("stepId", "");
        this.axios
            .post("/GeoProblemSolving/project/update", obj)
            .then(res => {
                this.resetProjectTypeModel = false;
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
        },
        getParticipants() {
            let members = this.projectInfo.members;
            let manager = [
                {
                userId: this.projectInfo["managerId"],
                userName: this.projectInfo["managerName"]
                }
            ];
            let membersList = manager.concat(members);

            let participantsTemp = [];
            let count = membersList.length;
            for (var i = 0; i < membersList.length; i++) {
                $.ajax({
                url:
                    "/GeoProblemSolving/user/inquiry" +
                    "?key=" +
                    "userId" +
                    "&value=" +
                    membersList[i].userId,
                type: "GET",
                async: false,
                success: function(data) {
                    participantsTemp.push(data);
                }
                });
            }
            this.$set(this, "participants", participantsTemp);
            this.startWebSocket();
        },
        // websocket
        startWebSocket() {
            this.socketApi.initWebSocket(
                "ChatServer/" + this.stepInfo.stepId,
                this.$store.state.IP_Port
            );
            this.send_msg = {
                type: "test",
                from: "Test",
                content: "TestChat"
            };
            this.socketApi.sendSock(this.send_msg, this.getSocketConnect);
        },

        getSocketConnect(data) {
            this.receivedChatMsgs = [];
            var chatMsg = data; //data传回onopen方法里的值
            if (data.type === "members") {
                let members = data.content
                .replace("[", "")
                .replace("]", "")
                .replace(/\s/g, "")
                .split(",");
                this.onlineUserIdList = members;
                this.judgeonlineParticipant();
            } else if (data.type === "message") {
                //判断消息的发出者
                if (chatMsg.content != "") {
                this.receivedChatMsgs.push(chatMsg);
                }
            } else if (data.type === "notice") {
                //上线下线提示
                if (chatMsg.behavior != "" && chatMsg.userId != "") {
                this.receivedChatMsgs.push(chatMsg);
                }
            } else if (chatMsg.type == undefined && chatMsg.length > 0) {
                for (let i = 0; i < chatMsg.length; i++) {
                if (chatMsg[i].content != "") {
                    this.receivedChatMsgs.push(chatMsg[i]);
                }
                }
            }
        },
        judgeonlineParticipant(msg) {
            if (msg == undefined) {
                // initial
                this.onlineParticipants = [];
                this.offlineParticipants = [];
                for (let i = 0; i < this.participants.length; i++) {
                if (this.onlineUserIdList.includes(this.participants[i].userId)) {
                    this.onlineParticipants.push(this.participants[i]);
                } else {
                    this.offlineParticipants.push(this.participants[i]);
                }
                }
            } else {
                if (msg.behavior == "off") {
                // offline
                for (let i = 0; i < this.onlineParticipants.length; i++) {
                    if (msg.userId == this.onlineParticipants[i].userId) {
                    let offperson = this.onlineParticipants.splice(i, 1);
                    this.offlineParticipants.push(offperson);
                    }
                }
                } else if (msg.behavior == "on") {
                // online
                for (let i = 0; i < this.offlineParticipants.length; i++) {
                    if (msg.userId == this.offlineParticipants[i].userId) {
                    let onperson = this.offlineParticipants.splice(i, 1);
                    this.onlineParticipants.push(onperson);
                    }
                }
                }
            }
        }
    }
}
</script>
