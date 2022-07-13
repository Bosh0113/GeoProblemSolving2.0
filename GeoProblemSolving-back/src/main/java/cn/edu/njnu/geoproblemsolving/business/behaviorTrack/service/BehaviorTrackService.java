package cn.edu.njnu.geoproblemsolving.business.behaviorTrack.service;

import cn.edu.njnu.geoproblemsolving.business.activity.entity.Activity;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Project;
import cn.edu.njnu.geoproblemsolving.business.behaviorTrack.entity.BehaviorDoc;

/**
 * @ClassName BehaviorTrackService
 * @Description 提供交互行为组织与追溯接口
 * @Author zhngzhng
 * @Date 2022/6/29
 **/
public interface BehaviorTrackService {
    BehaviorDoc tryAcquireLock(String bid);

    void tryReleaseLock(String bid);

    void tryRefreshLock(String bid, long expirationTime);

    void initBehaviorDoc(String pid);

    void initBehaviorDoc(String pid, Activity activity, String userId);

    /*
    活动构建
     */
    void appendAcv(String pid, Activity activity, String userId);

    /*
    过程组织
     */

    /*
    任务分配
     */

    /*
    问题定义
     */

    /*
    研究区认知
     */

    /*
    模型认知
     */

    /*
    资源共享
     */

    /*
    数据自动处理
     */

    /*
    数据半自动处理
     */

    /*
    数据手动处理
     */

    /*
    概念建模
     */

    /*
    逻辑建模
     */

    /*
    计算建模
     */

    /*
    模型配置
     */

    /*
    模型调用
     */

    /*
    结果比较
     */

    /*
    结果评估
     */

    /*
    决策支持
     */
}
