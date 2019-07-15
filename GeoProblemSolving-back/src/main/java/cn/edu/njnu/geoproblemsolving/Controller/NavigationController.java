package cn.edu.njnu.geoproblemsolving.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NavigationController {
    @RequestMapping("/home")
    public String navigation() {
        return "home";
    }

    @RequestMapping("/p")
    public String navigation2() {
        return "personalPage";
    }

    @RequestMapping("/nav")
    public String navigation3() {
        return "navigation";
    }


}

