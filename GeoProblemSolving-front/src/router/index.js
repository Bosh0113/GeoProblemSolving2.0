import Vue from 'vue'
import Router from 'vue-router'
// import 'mavon-editor/dist/css/index.css'
// import mavonEditor from 'mavon-editor'
Vue.use(Router)
// Vue.use(mavonEditor)


const routes = [{
  path: '/', name: 'Navigation', component: resolve => (require(["@/components/navigation"], resolve)), children: [
    { path: '', redirect: 'staticPage' },
    { path: 'staticPage', name: 'StaticPage', component: resolve => (require(["@/components/navigationContent/staticPage"], resolve)) },
    // { path: '', redirect: 'home' },
    // { path: 'home', name: 'Home', component: resolve => (require(["@/components/navigationContent/home"], resolve)) },
    // { path: 'projectlist', name: 'Projects', component: resolve => (require(["@/components/navigationContent/projectList"], resolve)) },
    // { path: 'project/:id', name: 'ProjectDetail', component: resolve => (require(["@/components/projects/projectDetail"], resolve)) },
    // { path: 'project/:id/workspace', name: 'workspace', component: resolve => (require(["@/components/workingSpace/moduleList"], resolve)) },
    // { path: 'markdown', name: 'markDown', component: resolve => (require(["@/components/workingSpace/utils/markDown"], resolve)) },

    // { path: 'project/workspace/contextdefinition/:id', name: 'contextDefinition', component: resolve => (require(["@/components/workingSpace/contextDefinition"], resolve)) },
    // { path: 'project/workspace/dataprocessing/:id', name: 'dataProcessing', component: resolve => (require(["@/components/workingSpace/dataProcessing"], resolve)) },
    // { path: 'project/workspace/modelprocess/:id', name: 'modelProcess', component: resolve => (require(["@/components/workingSpace/modelProcess"], resolve)) },
    // { path: 'project/workspace/modelevaluation/:id', name: 'modelEvaluation', component: resolve => (require(["@/components/workingSpace/modelEvaluation"], resolve)) },
    // { path: 'project/workspace/quantitativeandqualitative/:id', name: 'quantitativeAndQualitative', component: resolve => (require(["@/components/workingSpace/quantitativeAndQualitative"], resolve)) },
    // { path: 'project/workspace/simulationprediction/:id', name: 'simulationPrediction', component: resolve => (require(["@/components/workingSpace/simulationPrediction"], resolve)) },
    // { path: 'project/workspace/datavisualization/:id', name: 'dataVisualization', component: resolve => (require(["@/components/workingSpace/dataVisualization"], resolve)) },
    // { path: 'project/workspace/decisionmakingandmanagement/:id', name: 'decisionMakingAndManagement', component: resolve => (require(["@/components/workingSpace/decisionMakingAndManagement"], resolve)) },
     { path: 'project/:id/permission', name: 'permission', component: resolve => (require(["@/components/projects/permissionManager"], resolve)) },
    {
      path: 'project/:id/subproject/', name: 'subproject', component: resolve => (require(["@/components/subProject/subprojectNav"], resolve)), children: [
        { path: '', redirect: 'overview' },
        { path: 'overview', name: 'overview', component: resolve => (require(["@/components/subProject/subprojectView"], resolve)) },
        { path: 'info', name: 'info', component: resolve => (require(["@/components/subProject/subprojectDetail"], resolve)) },
        { path: 'resource', name: 'resource', component: resolve => (require(["@/components/subProject/subResources"], resolve)) },
        { path: 'process', name: 'process', component: resolve => (require(["@/components/subProject/toWork"], resolve)) },
        { path: 'task', name: 'task', component: resolve => (require(["@/components/subProject/taskArrangement"], resolve)) },
      ],
    },
    {
      path: 'workspace/:stepId/', name: 'stepFramework', component: resolve => (require(["@/components/workingSpace/functionSteps/pageFramework"], resolve)), children: [
        { path: 'contextDefinition', name: 'contextDefinition', component: resolve => (require(["@/components/workingSpace/functionSteps/contextDefinitionContent"], resolve)) },
        { path: 'dataProcessing', name: 'dataProcessing', component: resolve => (require(["@/components/workingSpace/functionSteps/dataProcessingContent"], resolve)) },
        { path: 'modelProcess', name: 'modelProcess', component: resolve => (require(["@/components/workingSpace/functionSteps/modelProcessContent"], resolve)) },
        { path: 'modelEvaluation', name: 'modelEvaluation', component: resolve => (require(["@/components/workingSpace/functionSteps/modelEvaluationContent"], resolve)) },
        { path: 'analysis', name: 'analysis', component: resolve => (require(["@/components/workingSpace/functionSteps/analysisContent"], resolve)) },
        { path: 'simulation', name: 'simulation', component: resolve => (require(["@/components/workingSpace/functionSteps/simulationContent"], resolve)) },
        { path: 'visualization', name: 'visualization', component: resolve => (require(["@/components/workingSpace/functionSteps/visualizationContent"], resolve)) },
        { path: 'decisionMaking', name: 'decisionMaking', component: resolve => (require(["@/components/workingSpace/functionSteps/decisionMakingContent"], resolve)) },
      ],
    },
    {
      path: '/workspaceP/:stepId/', name: 'stepFrameworkP', component: resolve => (require(["@/components/projects/pageFramework"], resolve)), children: [
        { path: 'contextDefinition', name: 'contextDefinitionP', component: resolve => (require(["@/components/workingSpace/functionSteps/contextDefinitionContent"], resolve)) },
        { path: 'dataProcessing', name: 'dataProcessingP', component: resolve => (require(["@/components/workingSpace/functionSteps/dataProcessingContent"], resolve)) },
        { path: 'modelProcess', name: 'modelProcessP', component: resolve => (require(["@/components/workingSpace/functionSteps/modelProcessContent"], resolve)) },
        { path: 'modelEvaluation', name: 'modelEvaluationP', component: resolve => (require(["@/components/workingSpace/functionSteps/modelEvaluationContent"], resolve)) },
        { path: 'analysis', name: 'analysisP', component: resolve => (require(["@/components/workingSpace/functionSteps/analysisContent"], resolve)) },
        { path: 'simulation', name: 'simulationP', component: resolve => (require(["@/components/workingSpace/functionSteps/simulationContent"], resolve)) },
        { path: 'visualization', name: 'visualizationP', component: resolve => (require(["@/components/workingSpace/functionSteps/visualizationContent"], resolve)) },
        { path: 'decisionMaking', name: 'decisionMakingP', component: resolve => (require(["@/components/workingSpace/functionSteps/decisionMakingContent"], resolve)) },
      ],
    },
    { path: 'newproject', name: 'NewProject', component: resolve => (require(["@/components/projects/newProject"], resolve)) },
    { path: 'participants', name: 'Participants', component: resolve => (require(["@/components/navigationContent/participants"], resolve)) },
    { path: 'community', name: 'Community', component: resolve => (require(["@/components/community/community"], resolve)) },
    { path: 'community/:id', name: 'Communityreply', component: resolve => (require(["@/components/community/communityReply"], resolve)) },
    { path: 'help', name: 'Help', component: resolve => (require(["@/components/navigationContent/help"], resolve)) },
    { path: 'personalPage', name: 'PersonalPage', component: resolve => (require(["@/components/userPage/personalPage"], resolve)) },
    { path: 'notifications', name: 'Notifications', component: resolve => (require(["@/components/userState/notifications"], resolve)) },
    { path: 'memberPage/:id', name: 'MemberDetailPage', component: resolve => (require(["@/components/userPage/memberDetailPage"], resolve)) },
    // { path: 'resourceList', name: 'resourceList', component: resolve => (require(["@/components/resources/resourceList"], resolve)) },
    { path: 'publicResource', name: 'PublicResource', component: resolve => (require(["@/components/resources/publicResourceList"], resolve)) },
    { path: 'join/:id/:email', name: 'joinProject', component: resolve => (require(["@/components/projects/joinNewProject"], resolve)) },
    { path: 'login', name: 'Login', component: resolve => (require(["@/components/userState/login"], resolve)) },
    { path: 'register', name: 'Register', component: resolve => (require(["@/components/userState/register"], resolve)) },
    { path: 'resetPassword/:email', name: 'resetPassword', component: resolve => (require(["@/components/userState/resetPwd"], resolve)) },
    { path: 'resourceCenter', name: 'resourceCenter', component: resolve => (require(["@/components/resources/resourceCenter"], resolve)) },
    { path: 'toolsCenter', name: 'toolsCenter', component: resolve => (require(["@/components/tools/toolsCenter"], resolve)) },
    { path: 'share', name: 'shareContainer', component: resolve => (require(["@/components/workingSpace/share/shareContainer"], resolve)) }, //share 
  ]
},
{ path: '/projectTypeContent/:projectId', name: 'projectTypeContent', component: resolve => (require(["@/components/projects/toWork"], resolve)) },
{ path: '/chat', name: 'chatUtil', component: resolve => (require(["@/components/utils/chatroom"], resolve)) },
{ path: '/draw', name: 'drawUtil', component: resolve => (require(["@/components/utils/drawBoard"], resolve)) },
{ path: '/map', name: 'mapTool', component: resolve => (require(["@/components/utils/mapTool"], resolve)) },
{ path: '/lineChart', name: 'lineChart', component: resolve => (require(["@/components/utils/charts/lineChart"], resolve)) },
{ path: '/basicScatter', name: 'basicScatter', component: resolve => (require(["@/components/utils/charts/basicScatter"], resolve)) },
{ path: '/mapScatter', name: 'mapScatter', component: resolve => (require(["@/components/utils/charts/mapScatter"], resolve)) },
{ path: '/histogram', name: 'histogram', component: resolve => (require(["@/components/utils/charts/histogram"], resolve)) },
{ path: '/pieChart', name: 'pieChart', component: resolve => (require(["@/components/utils/charts/pieChart"], resolve)) },
{ path: '/radarChart', name: 'radarChart', component: resolve => (require(["@/components/utils/charts/radarChart"], resolve)) },
{ path: '/funnelChart', name: 'funnelChart', component: resolve => (require(["@/components/utils/charts/funnelChart"], resolve)) },
{ path: '/nc/draw', name: 'drawUtilNC', component: resolve => (require(["@/components/utils/singleUtils/ncDrawBoard"], resolve)) },
{ path: '/nc/map', name: 'mapToolNC', component: resolve => (require(["@/components/utils/singleUtils/ncMapTool"], resolve)) },
{ path: '/nc/charts', name: 'dataChartsNC', component: resolve => (require(["@/components/utils/singleUtils/ncCharts"], resolve)) },
{ path: '/nc/taskManager', name: 'taskManager', component: resolve => (require(["@/components/utils/singleUtils/taskManager"], resolve)) },
{ path: '/video', name: 'videoViewer', component: resolve => (require(["@/components/utils/videoViewer"], resolve)) },
{ path: '/preview', name: 'pdfViewer', component: resolve => (require(["@/components/utils/filePreview"], resolve)) },
{ path: '/abseir', name: 'abSeir', component: resolve => (require(["@/components/utils/ABM-SEIR"], resolve)) },
{ path: '/tinymce', name: 'tinymce', component: resolve => (require(["@/components/utils/singleUtils/tinymce"], resolve)) }
]

export default new Router({
  routes,
  mode: 'history',
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition;
    } else {
      return { x: 0, y: 0 }
    }
  }
})
