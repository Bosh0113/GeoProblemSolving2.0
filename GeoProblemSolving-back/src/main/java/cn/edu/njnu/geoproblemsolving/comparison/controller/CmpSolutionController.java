package cn.edu.njnu.geoproblemsolving.comparison.controller;

import cn.edu.njnu.geoproblemsolving.comparison.bean.JsonResult;
import cn.edu.njnu.geoproblemsolving.comparison.dao.computablemodel.ComputableModelImpl;
import cn.edu.njnu.geoproblemsolving.comparison.dao.project.CmpProjectDaoImpl;
import cn.edu.njnu.geoproblemsolving.comparison.dao.solution.SolutionImpl;
import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpSolution;
import cn.edu.njnu.geoproblemsolving.comparison.entity.ComputableModel;
import cn.edu.njnu.geoproblemsolving.comparison.enums.ResultEnum;
import cn.edu.njnu.geoproblemsolving.comparison.utils.ResultUtils;
import jdk.internal.util.xml.impl.Input;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 20:16 2019/7/30
 * @Modified By:
 **/
@CrossOrigin
@RestController
@RequestMapping("/cmp_solution")
public class CmpSolutionController {

    @Resource
    private MongoTemplate mongoTemplate;


    @RequestMapping(value = "/createSolution",produces = {"application/json;charset=UTF-8"},method = RequestMethod.POST)
    public JsonResult createSolution(@RequestBody CmpSolution solution){
        SolutionImpl solutionImpl = new SolutionImpl(mongoTemplate);
        try{
            CmpSolution cmpSolution = solutionImpl.addSolution(solution);
            return ResultUtils.success(cmpSolution);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }


//    @RequestMapping(value = "/deployModel",method = RequestMethod.POST)
//    public JsonResult deployModel(HttpServletRequest request){
//        System.out.println(request);
//        try {
//            //todo 获取MD5值
//            String md5 = request.getParameter("md5");
//            String ownerName = request.getParameter("ownerName");
//            String ownerId = request.getParameter("ownerId");
//            //todo 获取文件信息
//            Part file = request.getPart("file");
//            String fileFullName = file.getSubmittedFileName();
//            String fileName = fileFullName.substring(0,fileFullName.lastIndexOf('.'));
//
//            Date date = new Date();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//            String createTime = dateFormat.format(date);
//
//            String oid = UUID.randomUUID().toString();
//            //todo  发送部署请求  此处假设部署成功
//            Thread.sleep(5000);
//            //todo 上传成功 解析MDL 更新computableModelMDL信息
//
//
//            //todo 信息存库
//            ComputableModel computableModel = new ComputableModel(oid, fileName, ownerName, ownerId, md5, createTime);
//            ComputableModelImpl computableModelImpl = new ComputableModelImpl(mongoTemplate);
//            computableModelImpl.addComputableModel(computableModel);
//            return ResultUtils.success(computableModel);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResultUtils.error(ResultEnum.FAILED);
//        }
//    }
//
//
//        @RequestMapping(value = "/matchMd5",method = RequestMethod.GET)
//    public JsonResult matchMd5(@RequestParam("md5") String md5){
//        try{
//            ComputableModelImpl computableModelDao = new ComputableModelImpl(mongoTemplate);
//            ComputableModel cm = computableModelDao.findItemByMd5(md5);
//            if(cm==null){
//                return ResultUtils.success();
//            }else{
//                return ResultUtils.success(cm.getOid());
//            }
//        }catch (Exception e){
//            return ResultUtils.error(ResultEnum.FAILED);
//        }
//    }
}
