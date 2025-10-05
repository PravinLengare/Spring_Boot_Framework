package com.pravin.SpringBootWeb1;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String home(){

        return "index";
    }
    @ModelAttribute("course")
    public String courseName(){
        return "JAVA";
    }
    /*
    @RequestMapping("add")
    public String add(HttpServletRequest request, HttpSession session){
        int num1 = Integer.parseInt(request.getParameter("num1"));
        int num2 = Integer.parseInt(request.getParameter("num2"));
        int result = num1 + num2;

        session.setAttribute("result",result);
        return "result.jsp";
    }

     */

    // Request parameter
    /*
    @RequestMapping("add")
    public String add(@RequestParam("num1") int num, int num2, HttpSession session){

        int result = num + num2;

        session.setAttribute("result",result);
        return "result.jsp";
    }

     */
    // Model Object -> to transfer the data from controller to jsp
    /*
    @RequestMapping("add")
    public String add(@RequestParam("num1") int num1, @RequestParam("num2") int num2, Model model){

        int result = num1 + num2;

        model.addAttribute("result",result);
        return "result";
    }

     */

    // Model And View
    /*
    @RequestMapping("add")
    public ModelAndView add(@RequestParam("num1") int num1, @RequestParam("num2") int num2, ModelAndView mv){

        int result = num1 + num2;

        mv.addObject("result",result);
        mv.setViewName("result");
        return mv;
    }

     */


    //  using the model attribute
    /*
    @RequestMapping("addAlien")
    public ModelAndView addAlien(@RequestParam("aid") int aid, @RequestParam("aname") String aname, ModelAndView mv) {
        Alien alien=new Alien();
        alien.setAid(aid);
        alien.setAname(aname);
        mv.addObject("alien",alien);
        mv.setViewName("result");

        return mv;
    }

     */

    //

    @RequestMapping("addAlien")  // to give model attribute is not necessary
    public String addAlien(@ModelAttribute("alien") Alien alien1, ModelAndView mv) {

        return "result";
    }
}
