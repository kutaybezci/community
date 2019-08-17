/*
 * Copyright (C) 2019 Kutay Bezci
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kutaybezci.community.controller;

import com.kutaybezci.community.types.fe.InfoForm;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Kutay Bezci
 */
@Controller
@Slf4j
public class InfoController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {
        //Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        log.error("Error controller catches exception", exception);
        InfoForm infoForm = new InfoForm();
        infoForm.setCode("error");
        if (exception != null) {
            infoForm.setMessage(exception.getMessage());
        }
        infoForm.setError(true);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("infoForm", infoForm);
        modelAndView.setViewName("info");
        return modelAndView;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}
