package it.uniroma3.siw.festival.controller;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class GlobalBindingConfig {

   // toglie gli spazi all'inizio e alla fine di ogni stringa delle form; true = stringa vuota diventa null
   @InitBinder
   public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
   }
}
