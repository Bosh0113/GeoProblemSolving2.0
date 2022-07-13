package cn.edu.njnu.geoproblemsolving.business.behaviorTrack.controller;

import cn.edu.njnu.geoproblemsolving.business.activity.entity.Activity;
import cn.edu.njnu.geoproblemsolving.business.behaviorTrack.service.BehaviorTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName TestController
 * @Description Todo
 * @Author zhngzhng
 * @Date 2022/7/12
 **/
@RestController
@RequestMapping(value = "/ts")
public class TestController {
    @Autowired
    BehaviorTrackService behaviorTrackService;

    @GetMapping(value = "/test/{pid}")
    public void test(@PathVariable String pid) {
        behaviorTrackService.initBehaviorDoc(pid);
    }

    @PostMapping(value = "/t2/{pid}/{userId}")
    public void createActivity(@RequestBody Activity activity,@PathVariable String pid, @PathVariable String userId) {
        behaviorTrackService.appendAcv(pid, activity, userId);
    }
}
