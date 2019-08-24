package com.kutaybezci.community.controller;

import com.kutaybezci.community.types.fe.InfoForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Kutay Bezci
 */
@Controller
@Slf4j
public class InfoController implements ErrorController {

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    public ModelAndView handleError(WebRequest webRequest) {
        final Throwable exception = errorAttributes.getError(webRequest);
        log.error("Error controller catches exception", exception);
        InfoForm infoForm = new InfoForm();
        infoForm.setCode("error");
        if (exception != null) {
            infoForm.setMessage(exception.getMessage());
        }
        infoForm.setError(true);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("info");
        modelAndView.addObject("infoForm", infoForm);
        return modelAndView;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}
