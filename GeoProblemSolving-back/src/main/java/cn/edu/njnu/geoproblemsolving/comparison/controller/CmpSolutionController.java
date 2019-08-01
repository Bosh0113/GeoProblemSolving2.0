package cn.edu.njnu.geoproblemsolving.comparison.controller;

import cn.edu.njnu.geoproblemsolving.comparison.bean.JsonResult;
import cn.edu.njnu.geoproblemsolving.comparison.dao.computablemodel.ComputableModelImpl;
import cn.edu.njnu.geoproblemsolving.comparison.entity.ComputableModel;
import cn.edu.njnu.geoproblemsolving.comparison.enums.ResultEnum;
import cn.edu.njnu.geoproblemsolving.comparison.utils.ResultUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;

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


    @RequestMapping(value = "/createSolution",method = RequestMethod.POST)
    public JsonResult createSolution(){

        return null;
    }


    @RequestMapping(value = "/deployModel",method = RequestMethod.POST)
    public JsonResult deployModel(HttpServletRequest request){
        System.out.println(request);
        try {
            //todo 获取MD5值

            //todo 获取文件信息
            Part file = request.getPart("file");

            //todo  部署

            //todo 信息存库

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return ResultUtils.success();
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
