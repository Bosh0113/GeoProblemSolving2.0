package cn.edu.njnu.geoproblemsolving.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping (value = "/common")
public class CommonRestController {


    @RequestMapping(value = "/integratedModelEditor", method = RequestMethod.GET)
    public ModelAndView mxGraph_integrated() {

        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("integratedModelEditor");

        return modelAndView;
    }


}
