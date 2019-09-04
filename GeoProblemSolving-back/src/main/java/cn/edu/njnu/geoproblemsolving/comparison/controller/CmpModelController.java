package cn.edu.njnu.geoproblemsolving.comparison.controller;

import cn.edu.njnu.geoproblemsolving.comparison.bean.JsonResult;
import cn.edu.njnu.geoproblemsolving.comparison.dao.computablemodel.ComputableModelImpl;
import cn.edu.njnu.geoproblemsolving.comparison.dao.modelresource.ModelResourceDaoImpl;
import cn.edu.njnu.geoproblemsolving.comparison.dao.solution.SolutionImpl;
import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpSolution;
import cn.edu.njnu.geoproblemsolving.comparison.entity.ComputableModel;
import cn.edu.njnu.geoproblemsolving.comparison.entity.ModelResource;
import cn.edu.njnu.geoproblemsolving.comparison.enums.ResultEnum;
import cn.edu.njnu.geoproblemsolving.comparison.utils.ResultUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 21:51 2019/8/8
 * @Modified By:
 **/
@CrossOrigin
@RestController
@RequestMapping("/cmp_model")
public class CmpModelController {

    @Resource
    private MongoTemplate mongoTemplate;


    @RequestMapping(value = "/getModelInfo",method = RequestMethod.GET)
    public JsonResult getModelInfo(@RequestParam("modelId") String modelId){
        ModelResourceDaoImpl modelResourceDao = new ModelResourceDaoImpl(mongoTemplate);
        try {
            ModelResource model = modelResourceDao.findModelByOid(modelId);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("model",model);
            //MDL信息
            return ResultUtils.success(jsonObject);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtils.error(ResultEnum.FAILED);
        }

    }


    @RequestMapping(value = "/createModel",produces = {"application/json;charset=UTF-8"},method = RequestMethod.POST)
    public JsonResult createModel(@RequestBody ModelResource model){
        ModelResourceDaoImpl modelResourceDao = new ModelResourceDaoImpl(mongoTemplate);
        try{
            model = modelResourceDao.createModel(model);
            return ResultUtils.success(model);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }



    @RequestMapping(value = "/deployModel",method = RequestMethod.POST)
    public JsonResult deployModel(HttpServletRequest request){
        System.out.println(request);
        try {
            // 获取MD5值
            String md5 = request.getParameter("md5");
            String ownerName = request.getParameter("ownerName");
            String ownerId = request.getParameter("ownerId");
            // 获取文件信息
            Part file = request.getPart("file");
            String fileFullName = file.getSubmittedFileName();
            String fileName = fileFullName.substring(0,fileFullName.lastIndexOf('.'));

            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String createTime = dateFormat.format(date);

            String oid = UUID.randomUUID().toString();
            //todo  发送部署请求  此处假设部署成功
            Thread.sleep(5000);
            //todo 上传成功 解析MDL 更新computableModelMDL信息


            //todo 信息存库
            ComputableModel computableModel = new ComputableModel(oid, fileName, ownerName, ownerId, md5, createTime);
            ComputableModelImpl computableModelImpl = new ComputableModelImpl(mongoTemplate);
            computableModelImpl.addComputableModel(computableModel);
            return ResultUtils.success(computableModel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }


    @RequestMapping(value = "/matchMd5",method = RequestMethod.GET)
    public JsonResult matchMd5(@RequestParam("md5") String md5){
        try{
            ComputableModelImpl computableModelDao = new ComputableModelImpl(mongoTemplate);
            ComputableModel cm = computableModelDao.findItemByMd5(md5);
            if(cm==null){
                return ResultUtils.success();
            }else{
                return ResultUtils.success(cm.getOid());
            }
        }catch (Exception e){
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }
}
