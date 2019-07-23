package cn.edu.njnu.geoproblemsolving.View;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ReturnPageController {

    @RequestMapping(value = "/projectDetail/{projectId}",method = RequestMethod.GET)
    public String projectPage(@PathVariable String projectId){
        return "/project/"+projectId+".html";//此请求根路径在templates文件夹，以后如果考虑隐藏后缀则需要将静态界面生成的位置更换，并用此方法请求。
    }
}
