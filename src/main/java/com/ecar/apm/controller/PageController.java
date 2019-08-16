package com.ecar.apm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PageController {


    @RequestMapping("/decorator")
    public String decorator(ModelMap map) {
    	return "decorator";
    }

    @RequestMapping("/first")
    public String first(ModelMap map) {
    	return "first";
    }


    // 重定向
    @RequestMapping(value="/redirect",method=RequestMethod.GET)
    public String redirect(){
        System.out.println("程序运行正常");
        return "redirect:/first";
    }

    // 转发
    @RequestMapping(value="/forward",method=RequestMethod.GET)
    public String forward(){
        System.out.println("程序运行正常");
        return "forward:/second";
    }
}
