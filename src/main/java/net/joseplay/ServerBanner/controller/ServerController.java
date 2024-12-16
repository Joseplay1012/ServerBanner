package net.joseplay.ServerBanner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ServerController {

    @GetMapping("/")
    public String index(){
        return "index";  // Isso corresponde a um arquivo index.html na pasta resources/templates
    }
}