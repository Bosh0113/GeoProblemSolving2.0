<template>
    <div>
        <type-choose :subProjectInfo="subProjectInfo" :userRole="userRole" :projectInfo="projectInfo" v-if="subProjectInfo.type==''" @changeSubProjectInfo = "changeSubProjectInfo"></type-choose>
        <solving-step :userRole="userRole"  :scopeInfo="scopeInfo" v-else-if="subProjectInfo.type=='type0'"></solving-step>
        <workspace :subProjectInfo="subProjectInfo" :userRole="userRole" :projectInfo="projectInfo" v-else-if="subProjectInfo.type=='type1'"></workspace>
        <Row v-else>
            <Col span="23" offset="1" style="margin-top:20px">
                <h1>Worry</h1>
            </Col>
        </Row>
    </div>
</template>
<script>
import solvingStep from "./solvingStep.vue"
import typeChoose from "./typeChoose.vue"
import workspace from "./workspace.vue"
export default {
    props: ["subProjectInfo", "userRole", "projectInfo", "scopeInfo"],
    components:{
        typeChoose,
        solvingStep,
        workspace
    },
    methods:{
        changeSubProjectInfo(subProjectInfo){
            this.subProjectInfo = subProjectInfo;
        }
    }
}
</script>
