package cn.edu.njnu.geoproblemsolving.business.activity.service;

import cn.edu.njnu.geoproblemsolving.business.CommonUtil;
import cn.edu.njnu.geoproblemsolving.business.user.dao.UserDao;
import cn.edu.njnu.geoproblemsolving.business.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * @ClassName ShareTokenService
 * @Description Todo
 * @Author zhngzhng
 * @Date 2021/12/13
 **/
@Service
public class ShareTokenService {
    @Autowired
    ProjectService projectService;
    @Autowired
    UserDao userDao;

    public String setToken(String inviterId, String aid){

        // String name = inviterName + ":Temp";
        //
        // UserEntity tempUser = new UserEntity();
        // tempUser.setUserId(uid);
        // ArrayList<String> joinedProject = new ArrayList<>();
        // joinedProject.add(aid);
        // tempUser.setJoinedProjects(joinedProject);
        // tempUser.setName(name);
        // userDao.saveLocalUser(tempUser);

        // //加入项目
        // projectService.joinProject(aid, uid);

        HashMap<String, String> tempUserInfo = new HashMap<>();
        String uid = UUID.randomUUID().toString() + inviterId;
        tempUserInfo.put("userId", uid);
        tempUserInfo.put("inviterId", inviterId);
        tempUserInfo.put("aid", aid);
        return CommonUtil.createToken(tempUserInfo);
    }

    public UserEntity getTempUserInfo(String jwtToken){
        HashMap<String, String> tempUserInfo = CommonUtil.getTempUserInfo(jwtToken);
        if (tempUserInfo == null) return null;
        String userId = tempUserInfo.get("userId");
        String aid = tempUserInfo.get("aid");

        UserEntity tempUser = userDao.findUserByIdOrEmail(userId);
        //当临时被删除时，则将其删除
        if (tempUser == null){
            tempUser = new UserEntity();
            tempUser.setUserId(userId);
            tempUser.setName("TempOperator" + (int)(Math.random() * 1000) + 1);
            userDao.saveLocalUser(tempUser);

            //加入项目
            projectService.joinProject(aid, userId);
        }
        // return CommonUtil.getTempUserInfo(jwtToken);
        return tempUser;
    }


}
