package com.ecar.apm.controller.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EmployeeHander{


    // 跳转进入添加员工信息页面
    // 需要查询出性别,全部部门
	@ResponseBody
    @RequestMapping(value="/emp",method=RequestMethod.GET)
    public String addPre(){
        return "get";
    }

    // 保存员工信息
	@ResponseBody
    @RequestMapping(value="/emp",method=RequestMethod.POST)
    public String add(){
        return "post";
    }

    // 删除员工信息
	@ResponseBody
    @RequestMapping(value="/emp/{id}",method=RequestMethod.DELETE)
    public String delete(@PathVariable("id") Integer id){
        return "delete id = " + id;
    }


    // 修改
	@ResponseBody
    @RequestMapping(value="/emp",method=RequestMethod.PUT)
    public String update(){
        return "put";
    }
}