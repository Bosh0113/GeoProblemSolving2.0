package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.controller;

import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.service.DataService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;


/**
 * 2021/7/1
 * Xinchang 定制的交通噪声工具
 */

/**
 * @ClassName DataController
 * @Description
 * @Author Xinchang Wang
 * @Date 2021/1/13  22:58
 * @Version 1.0.0
 */
@RestController
@RequestMapping(value = "/trafficNoiseData")
public class DataController {
    @Autowired
    DataService dataService;

    @RequestMapping(value = "/saveTempData", method = RequestMethod.POST)
    public Object saveTempRLS90Data(@RequestParam("userId") String userId,
                                    @RequestParam("tempId") String tempId,
                                    HttpServletRequest req) {
        return dataService.saveTempRLS90Data(req, userId, tempId);
    }

    // @RequestMapping(value = "/uploadRLS90Data/{userId}/{tempId}", method = RequestMethod.GET)
    // public Object uploadRLS90Data(@PathVariable("userId") String userId,@PathVariable("tempId") String tempId, HttpServletRequest req) {
    //     return dataService.uploadRLS90Data(userId, tempId, req);
    // }
}
