package com.toy.webshop.controlloer;


import com.toy.webshop.config.Login;
import com.toy.webshop.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@Slf4j
public class HomeController {

    @RequestMapping("/")
    public String home(@AuthenticationPrincipal User principal, Model model) {
        log.info("home controller");
        if(principal!=null)
            model.addAttribute("loginUsername", principal.getName());
        return "home";
    }

    @ResponseBody
    @GetMapping("/auth")
    public Authentication auth(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/access-denied")
    public String accessDenied(){
        return "AccessDenied";
    }
}
