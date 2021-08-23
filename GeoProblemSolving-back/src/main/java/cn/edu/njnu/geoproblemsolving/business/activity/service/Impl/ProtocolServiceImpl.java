package cn.edu.njnu.geoproblemsolving.business.activity.service.Impl;

import cn.edu.njnu.geoproblemsolving.business.activity.dao.Impl.LinkProtocolDaoImpl;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.LinkProtocol;
import cn.edu.njnu.geoproblemsolving.business.activity.service.ProtocolService;
import cn.edu.njnu.geoproblemsolving.business.collaboration.compute.util.CommonUtil;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ProtocolServiceImpl implements ProtocolService {
    @Autowired
    LinkProtocolDaoImpl protocolDao;

    @Override
    public LinkProtocol setProtocolService(LinkProtocol linkProtocol) {

        LinkProtocol protocol = protocolDao.setProtocol(linkProtocol);
        if (protocol == null){
            return null;
        }
        /*
        还得补一些外围的东西，如前端需要返回成员所含有的领域、组织以及特定字段（后面补充，先把核心内容补齐）
        关系设置好即可开始调度，3个步骤吧
        1.整理节点关系
        2.人员调度（涉及到一些限制性条件）
        3.资源调度（涉及到一些限制性条件）
         */
        // ProtocolType linkType = protocol.getType();
        // JSONArray nodes = protocol.getNodes();

        /*
        更改过后不动以前协议下调度的东西，只影响更改后的内容
        一个节点可能属于多个协议
        用户是动态的，资源（还需要丰富一下内涵）是静态的。
        需要的函数
        （外围一些操作）
        1. 提取相关活动中的成员
             获取成员所有的领域与组织
        2. 提取相关活动的资源
             获取资源的特定字段的所有内容

        （成员调度相关）
        1. 提取相关活动中的成员
        2. 根据限制性条件进行判断用户是否满足条件（先写一个满参数的函数，然后再进行重载，但可能会有点点问题）
        3. 根据关系将满足条件用户加入项目
        4. 节点新加入成员（不管关系间成员的流动，如果该用户能被调度到该活动，则可直接加入，若不能则申请加入，这个就涉及到的连通性的分析了）

        （资源调度相关）
        1. 提取相关活动中资源
        2. 限制性条件判断符合条件的资源（这个可能还是一个比较麻烦的操作）
        4. 自动更新模块
            似乎要相对简单一些，两种方案
            1）. 查询该节点相关联的关系，然后再查询关系中的节点的关系
            2）. 直接维护一张大图，从大图中找连通的节点[肯定是还需要加上限定条件（限定条件的组织）]

        一个大的需求，需要维护一张有向有环图（含限定条件、并且通常以子图形式出现，删除子图然后再连接子图，这个或许是最大的问题）
        1.
         */
        return null;
    }

    @Override
    public LinkProtocol findProtocolService(String pid) {
        return protocolDao.findProtocolById(pid);
    }

    @Override
    public String deleteProtocolService(String pid) {
        DeleteResult deleteResult = protocolDao.delLinkProtocol(pid);
        long deletedCount = deleteResult.getDeletedCount();
        if (deletedCount > 0){
            return "suc";
        }
        return "fail";
    }

    @Override
    public LinkProtocol updateProtocolService(String pid, HashMap<String, Object> putInfo) {
        CommonUtil commonUtil = new CommonUtil();
        Update update = commonUtil.setUpdate(putInfo);
        UpdateResult updateResult = protocolDao.updateLinkProtocol(pid, update);
        if (updateResult.getMatchedCount() > 0){
            return protocolDao.findProtocolById(pid);
        }
        return null;
    }
}
