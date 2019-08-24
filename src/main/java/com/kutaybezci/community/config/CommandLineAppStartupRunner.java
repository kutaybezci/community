package com.kutaybezci.community.config;

import com.kutaybezci.community.bl.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 *
 * @author kutay.bezci
 */
@Component
@Slf4j
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Autowired
    private MemberService memberService;

    @Override
    public void run(String... args) throws Exception {
        if (memberService.doInitAdmin()) {
            log.info("Admin created");
        }
    }
}
