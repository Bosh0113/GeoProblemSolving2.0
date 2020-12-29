import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

const routes = [
  {
    path: "/",
    name: "Navigation",
    component: resolve => require(["@/components/navigation"], resolve),
    children: [
      { path: "", redirect: "staticPage" },
      {
        path: "staticPage",
        name: "StaticPage",
        component: resolve =>
          require(["@/components/navigationContent/staticPage"], resolve)
      },
      //add level
      {
        path: "permission/:level/:id",
        name: "permission",
        component: resolve =>
          require(["@/components/workingSpace/permissionManager"], resolve)
      },
      // {
      //   path: 'project/:id/subproject/', name: 'subproject', component: resolve => (require(["@/components/subProject/subprojectNav"], resolve)), children: [
      //     { path: '', redirect: 'overview' },
      //     { path: 'overview', name: 'overview', component: resolve => (require(["@/components/subProject/subprojectView"], resolve)) },
      //     { path: 'info', name: 'info', component: resolve => (require(["@/components/subProject/subprojectDetail"], resolve)) },
      //     { path: 'resource', name: 'resource', component: resolve => (require(["@/components/subProject/subResources"], resolve)) },
      //     { path: 'process', name: 'process', component: resolve => (require(["@/components/subProject/toWork"], resolve)) },
      //     { path: 'task', name: 'task', component: resolve => (require(["@/components/subProject/taskArrangement"], resolve)) },
      //   ],
      // },
      // {
      //   path: 'workspace/:stepId/', name: 'stepFramework', component: resolve => (require(["@/components/subProject/pageFramework"], resolve)), children: [
      //     { path: 'contextDefinition', name: 'contextDefinition', component: resolve => (require(["@/components/workingSpace/functionSteps/contextDefinitionContent"], resolve)) },
      //     { path: 'dataProcessing', name: 'dataProcessing', component: resolve => (require(["@/components/workingSpace/functionSteps/dataProcessingContent"], resolve)) },
      //     { path: 'modelBuild', name: 'modelBuild', component: resolve => (require(["@/components/workingSpace/functionSteps/modelBuildContent"], resolve)) },
      //     { path: 'modelEvaluation', name: 'modelEvaluation', component: resolve => (require(["@/components/workingSpace/functionSteps/modelEvaluationContent"], resolve)) },
      //     { path: 'analysis', name: 'analysis', component: resolve => (require(["@/components/workingSpace/functionSteps/analysisContent"], resolve)) },
      //     { path: 'simulation', name: 'simulation', component: resolve => (require(["@/components/workingSpace/functionSteps/simulationContent"], resolve)) },
      //     { path: 'visualization', name: 'visualization', component: resolve => (require(["@/components/workingSpace/functionSteps/visualizationContent"], resolve)) },
      //     { path: 'decisionMaking', name: 'decisionMaking', component: resolve => (require(["@/components/workingSpace/functionSteps/decisionMakingContent"], resolve)) },
      //   ],
      // },
      // {
      //   path: '/workspaceP/:stepId/', name: 'stepFrameworkP', component: resolve => (require(["@/components/projects/pageFramework"], resolve)), children: [
      //     { path: 'contextDefinition', name: 'contextDefinitionP', component: resolve => (require(["@/components/workingSpace/functionSteps/contextDefinitionContent"], resolve)) },
      //     { path: 'dataProcessing', name: 'dataProcessingP', component: resolve => (require(["@/components/workingSpace/functionSteps/dataProcessingContent"], resolve)) },
      //     { path: 'modelBuild', name: 'modelBuildP', component: resolve => (require(["@/components/workingSpace/functionSteps/modelBuildContent"], resolve)) },
      //     { path: 'modelEvaluation', name: 'modelEvaluationP', component: resolve => (require(["@/components/workingSpace/functionSteps/modelEvaluationContent"], resolve)) },
      //     { path: 'analysis', name: 'analysisP', component: resolve => (require(["@/components/workingSpace/functionSteps/analysisContent"], resolve)) },
      //     { path: 'simulation', name: 'simulationP', component: resolve => (require(["@/components/workingSpace/functionSteps/simulationContent"], resolve)) },
      //     { path: 'visualization', name: 'visualizationP', component: resolve => (require(["@/components/workingSpace/functionSteps/visualizationContent"], resolve)) },
      //     { path: 'decisionMaking', name: 'decisionMakingP', component: resolve => (require(["@/components/workingSpace/functionSteps/decisionMakingContent"], resolve)) },
      //   ],
      // },
      {
        path: "newPersonalPage",
        name: "newPersonalPage",
        component: resolve =>
          require(["@/components/user/userPage/newPersonalPage"], resolve)
      },
      {
        path: "newproject",
        name: "NewProject",
        component: resolve =>
          require(["@/components/projects/newProject"], resolve)
      },
      {
        path: "community",
        name: "Community",
        component: resolve =>
          require(["@/components/community/community"], resolve)
      },
      {
        path: "community/:id",
        name: "Communityreply",
        component: resolve =>
          require(["@/components/community/communityReply"], resolve)
      },
      {
        path: "help",
        name: "Help",
        component: resolve =>
          require(["@/components/navigationContent/help"], resolve)
      },
      {
        path: "personalPage",
        name: "PersonalPage",
        component: resolve =>
          require(["@/components/user/userPage/personalPage"], resolve)
      },
      {
        path: "notifications",
        name: "Notifications",
        component: resolve =>
          require(["@/components/user/userState/notifications"], resolve)
      },
      {
        path: "memberPage/:id",
        name: "MemberDetailPage",
        component: resolve =>
          require(["@/components/user/userPage/memberDetailPage"], resolve)
      },
      // { path: 'resourceList', name: 'resourceList', component: resolve => (require(["@/components/resources/resourceList"], resolve)) },
      {
        path: "publicResource",
        name: "PublicResource",
        component: resolve =>
          require(["@/components/resources/publicResourceList"], resolve)
      },
      {
        path: "join/:id/:email",
        name: "joinProject",
        component: resolve =>
          require(["@/components/projects/joinNewProject"], resolve)
      },
      {
        path: "login",
        name: "Login",
        component: resolve =>
          require(["@/components/user/userState/login"], resolve)
      },
      {
        path: "register",
        name: "Register",
        component: resolve =>
          require(["@/components/user/userState/register"], resolve)
      },
      {
        path: "resetPassword/:email",
        name: "resetPassword",
        component: resolve =>
          require(["@/components/user/userState/resetPwd"], resolve)
      },
      {
        path: "resourceCenter",
        name: "resourceCenter",
        component: resolve =>
          require(["@/components/resources/resourceCenter"], resolve)
      },
      {
        path: "toolsCenter",
        name: "toolsCenter",
        component: resolve =>
          require(["@/components/tools/toolsCenter"], resolve)
      },
      {
        path: "share",
        name: "shareContainer",
        component: resolve =>
          require(["@/components/workingSpace/share/shareContainer"], resolve)
      } //share
    ]
  },
  {
    path: "/activity/:projectId",
    name: "workspaceContent",
    component: resolve =>
      require(["@/components/workingSpace/workspaceFrame.vue"], resolve),
    children: [
      {
        path: "type",
        name: "activityType",
        component: resolve =>
          require(["@/components/workingSpace/activity/typeChoose"], resolve)
      },
      {
        path: "work",
        name: "workSpace",
        component: resolve =>
          require([
            "@/components/workingSpace/activity/singleActivity"
          ], resolve)
      },
      {
        path: "info",
        name: "activityInfo",
        component: resolve =>
          require(["@/components/workingSpace/activity/multiActivity"], resolve)
      }
    ]
  },
  {
    path: "/dataService/:id",
    name: "dataService",
    component: resolve =>
      require(["@/components/utils/data/dataService"], resolve)
  },
  {
    path: "/chat",
    name: "chatUtil",
    component: resolve =>
      require(["@/components/tools/entity/chatroom"], resolve)
  },
  {
    path: "/draw",
    name: "drawUtil",
    component: resolve =>
      require(["@/components/tools/entity/drawBoard"], resolve)
  },
  {
    path: "/map",
    name: "mapTool",
    component: resolve =>
      require(["@/components/tools/entity/mapTool"], resolve)
  },
  {
    path: "/lineChart",
    name: "lineChart",
    component: resolve =>
      require(["@/components/tools/entity/charts/lineChart"], resolve)
  },
  {
    path: "/basicScatter",
    name: "basicScatter",
    component: resolve =>
      require(["@/components/tools/entity/charts/basicScatter"], resolve)
  },
  {
    path: "/mapScatter",
    name: "mapScatter",
    component: resolve =>
      require(["@/components/tools/entity/charts/mapScatter"], resolve)
  },
  {
    path: "/histogram",
    name: "histogram",
    component: resolve =>
      require(["@/components/tools/entity/charts/histogram"], resolve)
  },
  {
    path: "/pieChart",
    name: "pieChart",
    component: resolve =>
      require(["@/components/tools/entity/charts/pieChart"], resolve)
  },
  {
    path: "/radarChart",
    name: "radarChart",
    component: resolve =>
      require(["@/components/tools/entity/charts/radarChart"], resolve)
  },
  {
    path: "/funnelChart",
    name: "funnelChart",
    component: resolve =>
      require(["@/components/tools/entity/charts/funnelChart"], resolve)
  },
  {
    path: "/nc/draw",
    name: "drawUtilNC",
    component: resolve =>
      require(["@/components/tools/entity/singleUtils/ncDrawBoard"], resolve)
  },
  {
    path: "/nc/map",
    name: "mapToolNC",
    component: resolve =>
      require(["@/components/tools/entity/singleUtils/ncMapTool"], resolve)
  },
  {
    path: "/nc/charts",
    name: "dataChartsNC",
    component: resolve =>
      require(["@/components/tools/entity/singleUtils/ncCharts"], resolve)
  },
  {
    path: "/nc/taskManager",
    name: "taskManager",
    component: resolve =>
      require(["@/components/tools/entity/singleUtils/taskManager"], resolve)
  },
  {
    path: "/video",
    name: "videoViewer",
    component: resolve =>
      require(["@/components/tools/entity/videoViewer"], resolve)
  },
  {
    path: "/preview",
    name: "pdfViewer",
    component: resolve =>
      require(["@/components/tools/entity/filePreview"], resolve)
  },
  {
    path: "/abseir",
    name: "abSeir",
    component: resolve =>
      require(["@/components/tools/entity/ABM-SEIR"], resolve)
  },
  {
    path: "/tinymce",
    name: "tinymce",
    component: resolve =>
      require(["@/components/tools/entity/singleUtils/tinymce"], resolve)
  },
  {
    path: "/modelItem/:doi",
    name: "Model",
    component: resolve => require(["@/components/utils/model/Model"], resolve)
  },
  {
    path: "/toolShow",
    name: "toolTemplate",
    component: resolve => require(["@/components/tools/toolPreview"], resolve)
  },

  //  个人空间路由内容
  {
    path: "/test",
    component: resolve =>
      require(["@/components/user/subPage/project"], resolve)
  }
];

export default new Router({
  routes,
  mode: "history",
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition;
    } else {
      return { x: 0, y: 0 };
    }
  }
});
