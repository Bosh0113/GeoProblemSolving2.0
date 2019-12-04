package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.ComputerModel.ComputableModelDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.Model.JsonResult;
import cn.edu.njnu.geoproblemsolving.Service.TaskService;
import cn.edu.njnu.geoproblemsolving.Utils.ResultUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping(value = "/task")
public class TaskRestController {
    @Autowired
    TaskService taskService;

    @RequestMapping(value = "/getModelItem/{oid}",method = RequestMethod.GET)
    public Object readProject(@PathVariable("oid") String oid) {
        return taskService.getComputeModel(oid);
    }

    @RequestMapping(value = "/createTask/{pid}", method = RequestMethod.GET)
    JsonResult createTask(@PathVariable("pid") String pid){
        return ResultUtils.success(taskService.createTask(pid));
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public JsonResult uploadData(HttpServletRequest request) throws IOException, ServletException {
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        Part part=multiRequest.getPart("file");

        String header = part.getHeader("Content-Disposition");
        String filename2 = header.substring(header.indexOf("filename=\"") + 10,header.lastIndexOf("\""));//filename=" (整个字符串长度为10，所以要加10)
        // 获取文件名
        String fileName = part.getName();
        //  获取文件后缀名
        String suffix ="." + FilenameUtils.getExtension(filename2);

        File file=File.createTempFile(part.getName(),suffix);//创建临时文件
        FileUtils.copyInputStreamToFile(multiRequest.getPart("file").getInputStream(),file);

        String ip = multiRequest.getParameter("ip");//获得ip&port
        String port = multiRequest.getParameter("port");
        String url = taskService.upload(file,ip,port);


        return ResultUtils.success(taskService.upload(file,ip,port));
    }

    @RequestMapping(value = "/invoke", method = RequestMethod.POST)
    JsonResult invoke(@RequestBody JSONObject obj){
        return ResultUtils.success(taskService.slefInvoke(obj));
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    JsonResult getResult(@RequestBody JSONObject data) {
        return ResultUtils.success(taskService.refresh(data));
    }

//    @RequestMapping(value = "/updataModelItem",method = RequestMethod.POST)
//    JsonResult updataModelItem(@RequestBody JSONObject obj) {
//        return ResultUtils.success(taskService.updataModelItem(obj));
//    }

    @RequestMapping(value = "/addCModel",method = RequestMethod.POST)
    JsonResult addCModel(HttpServletRequest request){
        return  ResultUtils.success(taskService.add());
    }


}
