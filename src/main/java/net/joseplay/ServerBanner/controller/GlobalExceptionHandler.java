package net.joseplay.ServerBanner.controller;


import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NoHandlerFoundException ex, Model model) {
        model.addAttribute("error", "Página não encontrada");
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        return "error/404"; // Arquivo HTML na pasta src/main/resources/templates/error
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleInternalServerError(Exception ex, Model model) {
        model.addAttribute("error", "Erro interno do servidor");
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return "error/500"; // Arquivo HTML na pasta src/main/resources/templates/error
    }
}

