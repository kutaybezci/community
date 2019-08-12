/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kutaybezci.community.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 *
 * @author kutay.bezci
 */
@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    @Override
    public void run(String...args) throws Exception {
        System.out.println("STARTED");
    }
}

