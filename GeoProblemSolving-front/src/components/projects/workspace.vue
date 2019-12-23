<template>
    <Row>
        <Col span="23" offset="1" style="margin-top:20px">
        <Row>
            <Col span="22" offset="1">
                <Collapse simple v-model="unfold">
                <Panel name="tool">
                    Toolbox
                    <tool-container slot="content" :stepInfo="stepInfo" :userRole="userRole"></tool-container>
                </Panel>
                <Panel name="data">
                    Data list
                    <data-list slot="content" :stepInfo="stepInfo" :userRole="userRole"></data-list>
                </Panel>
                </Collapse>
            </Col>
        </Row>
        </Col>
    </Row>
</template>
<script>
import dataList from "./../workingSpace/functionSteps/utils/dataList";
import toolContainer from "./../workingSpace/functionSteps/utils/toolContainer";
export default {
    props: ["projectInfo", "userRole"],
    components: {
        dataList,
        toolContainer,
    },
    data(){
        return{
            stepInfo:{},
            unfold: ["tool", "data"],
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
                }
            })
            .catch(err => {});
        }
    }
}
</script>
