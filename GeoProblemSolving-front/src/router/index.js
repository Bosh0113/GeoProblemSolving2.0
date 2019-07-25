import Vue from 'vue'
import Router from 'vue-router'
// import 'mavon-editor/dist/css/index.css'
// import mavonEditor from 'mavon-editor'
Vue.use(Router)
// Vue.use(mavonEditor)


const routes = [{
  path: '/',component: resolve=>(require(["@/components/navigation"],resolve)), children: [
    { path: '', redirect: 'home'},
    { path: 'home', name: 'Home', component: resolve=>(require(["@/components/navigationContent/home"],resolve)) },
    { path: 'projectlist', name: 'Projects', component: resolve=>(require(["@/components/navigationContent/projectList"],resolve)) },
    { path: 'project/:id', name: 'ProjectDetail', component: resolve=>(require(["@/components/projects/projectDetail"],resolve)) },
    { path: 'project/:id/workspace', name: 'workspace', component: resolve=>(require(["@/components/workingSpace/moduleList"],resolve)) },
    { path: 'project/:id/subproject', name: 'subproject', component: resolve=>(require(["@/components/subProject/subprojectDetail"],resolve)) },
    { path: 'newproject', name: 'NewProject', component: resolve=>(require(["@/components/projects/newProject"],resolve)) },
    { path: 'participants', name: 'Participants', component: resolve=>(require(["@/components/navigationContent/participants"],resolve)) },
    { path: 'community', name: 'Community', component: resolve=>(require(["@/components/community/community"],resolve)) },
    { path: 'community/:id', name: 'Communityreply', component: resolve=>(require(["@/components/community/communityReply"],resolve)) },
    { path: 'help', name: 'Help', component: resolve=>(require(["@/components/navigationContent/help"],resolve)) },
    { path: 'personalPage', name: 'PersonalPage', component: resolve=>(require(["@/components/userPage/personalPage"],resolve)) },
    { path: 'notifications', name: 'Notifications', component: resolve=>(require(["@/components/usersState/notifications"],resolve)) },
    { path: 'memberPage/:id', name: 'MemberDetailPage', component: resolve=>(require(["@/components/userPage/memberDetailPage"],resolve)) },
    { path: 'resourceList', name: 'resourceList', component: resolve=>(require(["@/components/resource/resourceList"],resolve)) },
    { path: 'publicResource', name: 'PublicResource', component: resolve=>(require(["@/components/resource/publicResourceList"],resolve)) },
    { path: 'join/:id/:email', name: 'joinProject', component: resolve=>(require(["@/components/Projects/joinNewProject"],resolve)) },
    { path: 'login', name: 'Login', component: resolve=>(require(["@/components/usersState/login"],resolve)) },
    { path: 'register', name: 'Register', component: resolve=>(require(["@/components/usersState/register"],resolve)) },
    { path: 'resetPassword/:email', name: 'resetPassword', component: resolve=>(require(["@/components/UsersState/resetPwd"],resolve)) } ,
    { path: 'cmp-projectlist',name:'cmp-projectlist',component:()=>import("@/views/comparison/CmpProjectList")},
    { path: 'create-cmp-project', name:'create-cmp-project',component:()=>import("@/views/comparison/CreateProject")},
    { path: 'cmp-project/:id',name:'cmp-project-detail',component:()=>import("@/views/comparison/CmpProjectDetail")},
    { path: 'cmp-project/comprehensive/:id',name:'cmp-project-comprehensive',component:()=>import("@/views/comparison/ComprehensiveProject")},
    { path: 'cmp-project/specific/:id',name:'cmp-project-specific',component:()=>import("@/views/comparison/SpecificProject")},
    { path: 'create-cmp-item/:id',name:'create-cmp-item',component:()=>import("@/views/comparison/CreateCmpItem")}
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
  scrollBehavior(to,from,savedPosition){
    if(savedPosition){
      return savedPosition;
    }else{
      return {x:0,y:0}
    }
  }
})
