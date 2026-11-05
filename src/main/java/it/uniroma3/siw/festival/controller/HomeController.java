package it.uniroma3.siw.festival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller //spring lo registra come controller MVC 
public class HomeController {

    @GetMapping("/")  //il metodo risponde alla radice del sito 
    public String getHome() {
        return "index.html";
    }
}
