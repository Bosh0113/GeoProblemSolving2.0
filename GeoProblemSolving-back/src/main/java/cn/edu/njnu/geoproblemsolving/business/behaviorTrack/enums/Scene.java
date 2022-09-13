package cn.edu.njnu.geoproblemsolving.business.behaviorTrack.enums;
//一级情景，用于限制
public enum Scene {
    problemDefinition,
    studyAreaCognition,
    // modelCognition,
    // taskAssignment,
    // resourceShare,
    autoDataProcessing,
    semiAutoDataProcessing,
    manualDataProcessing,
    conceptModelling,
    logicModelling,
    computationalModelling,
    modelConfig,
    modelInvoke,
    resultCompare,
    resultAssessment,
    // decisionSupport,
    activityStructure,
    processOrganization,



//    新的情景
    problemAndStudyAreaCognition,
    modelCognition,
    //模型配置:包含建模与模型输入配置
    modelConfiguration,
    resultAssessmentAndCompare,
    decisionSupport,
    dataProcessing,
    //一下是通用情景，贯穿整个流程
    resourceShare,
    dataVisualization,
    taskAssignment,
    activityStructureAndOrganization

}
