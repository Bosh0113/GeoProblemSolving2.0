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
    { path: 'project/:id/workspace', name: 'workspace', component: resolve => (require(["@/components/workingSpace/moduleList"], resolve)) },
    // { path: 'markdown', name: 'markDown', component: resolve => (require(["@/components/workingSpace/utils/markDown"], resolve)) },
    { path: 'project/workspace/contextdefinition/:id', name: 'contextDefinition', component: resolve => (require(["@/components/workingSpace/contextDefinition"], resolve)) },
    { path: 'project/workspace/dataprocessing/:id', name: 'dataProcessing', component: resolve => (require(["@/components/workingSpace/dataProcessing"], resolve)) },
    { path: 'project/workspace/modelprocess/:id', name: 'modelProcess', component: resolve => (require(["@/components/workingSpace/modelProcess"], resolve)) },
    { path: 'project/workspace/modelevalution/:id', name: 'modelEvalution', component: resolve => (require(["@/components/workingSpace/modelEvalution"], resolve)) },
    { path: 'project/workspace/quantitativeandqualitative/:id', name: 'quantitativeAndQualitative', component: resolve => (require(["@/components/workingSpace/quantitativeAndQualitative"], resolve)) },
    { path: 'project/workspace/simulationprediction/:id', name: 'simulationPrediction', component: resolve => (require(["@/components/workingSpace/simulationPrediction"], resolve)) },
    { path: 'project/workspace/datavisulization/:id', name: 'dataVisulization', component: resolve => (require(["@/components/workingSpace/dataVisulization"], resolve)) },
    { path: 'project/workspace/decisionmakingandmanagement/:id', name: 'decisionMakingAndManagement', component: resolve => (require(["@/components/workingSpace/decisionMakingAndManagement"], resolve)) },
    {
      path: 'project/:id/subproject/', name: 'subproject', component: resolve => (require(["@/components/subProject/subprojectNav"], resolve)), children: [
        { path: '', redirect: 'info' },
        { path: 'info', name: 'info', component: resolve => (require(["@/components/subProject/subprojectDetail"], resolve)) },
        { path: 'resource', name: 'resource', component: resolve => (require(["@/components/subProject/subResources"], resolve)) },
        { path: 'process', name: 'process', component: resolve => (require(["@/components/subProject/solvingStep"], resolve)) },
        { path: 'task', name: 'task', component: resolve => (require(["@/components/subProject/taskArrangement"], resolve)) },
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
    { path: 'resourceList', name: 'resourceList', component: resolve => (require(["@/components/resources/resourceList"], resolve)) },
    { path: 'publicResource', name: 'PublicResource', component: resolve => (require(["@/components/resources/publicResourceList"], resolve)) },
    { path: 'join/:id/:email', name: 'joinProject', component: resolve => (require(["@/components/projects/joinNewProject"], resolve)) },
    { path: 'login', name: 'Login', component: resolve => (require(["@/components/userState/login"], resolve)) },
    { path: 'register', name: 'Register', component: resolve => (require(["@/components/userState/register"], resolve)) },
    { path: 'resetPassword/:email', name: 'resetPassword', component: resolve => (require(["@/components/userState/resetPwd"], resolve)) },
    { path: 'resourceCenter', name: 'resourceCenter', component: resolve => (require(["@/components/resources/resourceCenter"], resolve)) },
    { path: 'toolCollection', name: 'toolCollection', component: resolve => (require(["@/components/tools/toolCollection"], resolve)) },    
    { path: 'createNewTool', name: 'createNewTool', component: resolve => (require(["@/components/tools/createNewTool"], resolve)) },    
    { path: 'createToolset', name: 'createToolset', component: resolve => (require(["@/components/tools/createToolset"], resolve)) }
  ]
},
{ path: '/chat', name: 'chatUtil', component: resolve=>(require(["@/components/utils/chatroom"],resolve)) },
{ path: '/draw', name: 'drawUtil', component: resolve=>(require(["@/components/utils/drawBoard"],resolve)) },
{ path: '/map', name: 'mapTool', component: resolve=>(require(["@/components/utils/mapTool"],resolve)) },
{ path: '/charts', name: 'dataCharts', component: resolve=>(require(["@/components/utils/charts"],resolve)) },
{ path: '/nc/draw', name: 'drawUtilNC', component: resolve=>(require(["@/components/utils/singleUtils/ncDrawBoard"],resolve)) },
{ path: '/nc/map', name: 'mapToolNC', component: resolve=>(require(["@/components/utils/singleUtils/ncMapTool"],resolve)) },
{ path: '/nc/charts', name: 'dataChartsNC', component: resolve=>(require(["@/components/utils/singleUtils/ncCharts"],resolve)) },
{ path: '/video', name: 'videoViewer', component: resolve=>(require(["@/components/utils/videoViewer"],resolve)) },
{ path: '/preview', name: 'pdfViewer', component: resolve=>(require(["@/components/utils/filePreview"],resolve)) },
{ path: '/tinymce', name: 'tinymce', component: resolve=>(require(["@/components/utils/singleUtils/tinymce"],resolve)) },
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
