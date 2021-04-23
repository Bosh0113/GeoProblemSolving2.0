package cn.edu.njnu.geoproblemsolving.business.resource.controller;

import cn.edu.njnu.geoproblemsolving.Dao.Resource.ResourceDaoImpl;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.resource.service.IResourceService;
import cn.edu.njnu.geoproblemsolving.business.resource.service.IResourceServiceImpl;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

/**
 * @ClassName ResourceController
 * @Description 注意GSM平台字段同步
 * 这种写法感觉就算正确的，接口返回方式只在 controller 层进行统一
 * 😊😊😊😊😊
 * 感觉在多参数情况下，还是使用 form-data 进行数据传输更美观一些
 * 但是又多种风格混用，真麻烦
 * @Author zhngzhng
 * @Date 2021/4/12
 **/

@RestController
@RequestMapping(value = "/res")
public class ResController {
    @Autowired
    IResourceServiceImpl resService;


    @RequestMapping(value = "/getFolderAndRes", method = RequestMethod.GET)
    public JsonResult getFolderAndResource() {
        return null;
    }

    /**
     * 根据 id 查询内容
     *
     * @param paths 路径
     * @return 返回单个内容，不包含 children 内容，这个在后台处理
     */
    @RequestMapping(value = "/{paths}", method = RequestMethod.GET)
    public JsonResult getResByPath(@PathVariable String paths, HttpServletRequest req) {
        String userId = (String) req.getSession().getAttribute("userId");
        ArrayList<ResourceEntity> getResResult = resService.getResPath(userId, paths);
        if (getResResult == null) {
            return ResultUtils.error(-2, "fail: user server");
        }
        return ResultUtils.success(getResResult);
    }

    @RequestMapping(value = "/{rid}", method = RequestMethod.DELETE)
    public JsonResult delResById(@PathVariable String rid, HttpServletRequest req) {
        String userId = (String) req.getSession().getAttribute("userId");
        resService.deleteRemote(userId, rid);
        return null;
    }

    /**
     * 根据路径删除
     *
     * @param delFileInfo
     * 包含以下内容
     * uid address paths folder
     * paths为“”的时候表示仅使用uid进行删除
     * 不为“”则表示使用path和uid进行删除，效率相对更高folder
     * true则为folder，false为 file 需要删除数据容器中的内容
     * @param req
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    public JsonResult delRes(@RequestBody JSONObject delFileInfo, HttpServletRequest req) {
        //真的丑
        //用 session 内的内容相较于自己传数据更加稳定一些，不必担心是否输入错误
        String userId = (String) req.getSession().getAttribute("userId");
        String uid = delFileInfo.getString("uid");
        String paths = delFileInfo.getString("paths");
        Boolean isFolder = delFileInfo.getBoolean("folder");
        String address = delFileInfo.getString("address");
        Integer resultCode = resService.delResPath(userId, address, uid, paths, isFolder);
        if (resultCode == 0) {
            return ResultUtils.success();
        }
        return ResultUtils.error(-2, "Fail: user server");
    }

    /**
     * 更新文件资源
     *
     * @param res
     * @param paths 路径
     * @param req
     * @return
     */
    @RequestMapping(value = "/{paths}", method = RequestMethod.PUT)
    public JsonResult updateRes(@RequestBody ResourceEntity res, @PathVariable String paths, HttpServletRequest req) {
        String userId = (String) req.getSession().getAttribute("userId");
        String putResult = resService.putResService(userId, res, paths, false);
        if (putResult.equals("suc")) {
            return ResultUtils.success();
        } else {
            return ResultUtils.error(-2, "fail");
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public JsonResult putRes(@RequestBody ResourceEntity res, HttpServletRequest req) {
        String userId = (String) req.getSession().getAttribute("userId");
        String putResult = resService.putResService(userId, res, "", false);
        if (putResult.equals("suc")) {
            return ResultUtils.success();
        } else {
            return ResultUtils.error(-2, "fail");
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public JsonResult getAllRes(HttpServletRequest req) {
        HttpSession session = req.getSession();
        String userId = (String) session.getAttribute("userId");
        ArrayList<ResourceEntity> resList = resService.getAllResFromService(userId);
        if (resList == null) {
            return ResultUtils.error(-2, "Fail");
        }
        return ResultUtils.success(resList);
    }

    /**
     * 文件夹和资源因为共用数据结构本可以公用一个接口
     * 但是由于数据涉及到上传到数据容器部分内容
     * 所以最好在接口层将两者分开
     * 在业务层进行合并即可
     *
     * @param paths
     * @param req
     * @return
     */
    @RequestMapping(value = "/folder/{paths}", method = RequestMethod.POST)
    public JsonResult createFolderOrUploadData(@RequestBody ResourceEntity res, @PathVariable String paths, HttpServletRequest req) {
        String userId = (String) req.getSession().getAttribute("userId");
        res.setFolder(true);
        JSONObject createResult = resService.createResService(userId, res, paths);
        if (createResult == null) {
            return ResultUtils.error(-2, "fail");
        }
        return ResultUtils.success(createResult);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object creatFolderOrUploadData(HttpServletRequest req) {
        return resService.upRemote(req);
    }


    @RequestMapping(value = "/folder", method = RequestMethod.PUT)
    public JsonResult updateFolderName(@RequestBody ResourceEntity resource, HttpServletRequest req) {
        String userId = (String) req.getSession().getAttribute("userId");
        String putRes = resService.putResService(userId, resource, "", true);
        if (putRes.equals("suc")) {
            return ResultUtils.success();
        } else {
            return ResultUtils.error(-2, "fail");
        }
    }

    @RequestMapping(value = "/folder/{uid}/{paths}", method = RequestMethod.DELETE)
    public JsonResult delFolder(@PathVariable String uid, @PathVariable String paths, HttpServletRequest req){
        String userId = (String) req.getSession().getAttribute("userId");
        Integer delResult = resService.delResPath(userId, "", uid, paths, true);
        if (delResult == 0){
            return ResultUtils.success();
        }
        return ResultUtils.error(-2, "fail");
    }

    @RequestMapping(value = "/folder/{uid}", method = RequestMethod.DELETE)
    public JsonResult delFolderByUid(@PathVariable String uid, HttpServletRequest req){
        String userId = (String) req.getSession().getAttribute("userId");
        Integer delResult = resService.delResPath(userId, "", uid, "", true);
        if (delResult == 0){
            return ResultUtils.success();
        }
        return ResultUtils.error(-2, "fail");
    }

    @RequestMapping(value = "/file/copyToCenter", method = RequestMethod.POST)
    public JsonResult copyFromProject2PerCenter(@RequestBody ResourceEntity res, HttpServletRequest req){
        String userId = (String) req.getSession().getAttribute("userId");
        String copyResult = resService.copyFromProject2Center(userId, res);
        if (copyResult.equals("suc")){
            return ResultUtils.success();
        }
        return ResultUtils.error(-2, "fail");
    }

    @RequestMapping(value = "/file/all", method = RequestMethod.GET)
    public JsonResult getAllFileList(HttpServletRequest req){
        String userId = (String) req.getSession().getAttribute("userId");
        JSONArray allFileList = resService.getAllFileList(userId);
        return ResultUtils.success(allFileList);
    }


}
