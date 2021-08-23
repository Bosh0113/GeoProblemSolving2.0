package cn.edu.njnu.geoproblemsolving.business.tool.support.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", allowCredentials = "true")
@Controller
@RequestMapping(value = "/toolApp")
public class ToolApplicationController {

    @RequestMapping(value = "/IntegrationApp",method = RequestMethod.GET)
    ModelAndView integratedModel(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("integratedModeling_new");
        modelAndView.addObject("computableModelList", null);

        return modelAndView;

//        return "integratedModeling_new";
    }


}
