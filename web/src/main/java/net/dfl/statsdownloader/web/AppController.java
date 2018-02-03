package net.dfl.statsdownloader.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppController {
	
    @RequestMapping(value = "/")
    public String something(Model model){
        System.out.println("Hit root path");
        model.addAttribute("text", "It's Working");
        return "index";
    }
}
