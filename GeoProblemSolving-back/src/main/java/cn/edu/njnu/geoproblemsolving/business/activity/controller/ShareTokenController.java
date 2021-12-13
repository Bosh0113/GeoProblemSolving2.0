package cn.edu.njnu.geoproblemsolving.business.activity.controller;

import cn.edu.njnu.geoproblemsolving.business.CommonUtil;
import cn.edu.njnu.geoproblemsolving.business.activity.service.ShareTokenService;
import cn.edu.njnu.geoproblemsolving.business.user.entity.UserEntity;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ShareTokenController
 * @Description 临时用户
 * @Author zhngzhng
 * @Date 2021/12/13
 **/
@RestController
@RequestMapping(value = "/token")
public class ShareTokenController {
    @Autowired
    ShareTokenService tokenService;

    @RequestMapping(value = "/{inviterId}/{aid}", method = RequestMethod.GET)
    public JsonResult inviteTemp(@PathVariable String inviterId, @PathVariable String aid){
        String token = tokenService.setToken(inviterId, aid);
        if (token == null ) return ResultUtils.error(-2, "Fail");
        return ResultUtils.success(token);
    }

    @RequestMapping(value = "/{token}", method = RequestMethod.GET)
    public JsonResult getTempUserInfo(@PathVariable String token){
        String userId = CommonUtil.getTokenUserId(token);
        if (userId == null) return ResultUtils.error(-2, "Wrong token.");
        UserEntity tempUserInfo = tokenService.getTempUserInfo(userId);
        if (tempUserInfo == null) return ResultUtils.error(-2, "No such temp user, id: " + userId);
        return ResultUtils.success(tempUserInfo);
    }
}
