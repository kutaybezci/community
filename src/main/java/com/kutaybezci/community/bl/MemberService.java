/*
 * Copyright (C) 2019 kutay.bezci
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
package com.kutaybezci.community.bl;

import com.kutaybezci.community.dal.MemberRepository;
import javax.transaction.Transactional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kutaybezci.community.types.model.Member;
import com.kutaybezci.community.types.model.Role;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author kutay.bezci
 */
@Service
@Slf4j
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Member save(Member member) {
        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
        return memberRepository.save(member);
    }

    @Transactional
    public Member register(Member member) {
        return save(member);
    }
    @Transactional
    public Optional<Member> findByUsername(String username){
        Member member=memberRepository.findByUsername(username);
        if(member==null){
            return Optional.empty();
        }else{
            return Optional.of(member);
        }
    }
    
    
    public boolean initAdmin() {
        try {
            if (memberRepository.count() == 0) {
                val admin = "ADMIN";
                Member member = new Member();
                member.setPassword(admin);
                member.setUsername(admin);
                Role memberRoles=new Role();
                memberRoles.setMember(member);
                memberRoles.setRoleCode(admin);
                member.getMemberRoles().add(memberRoles);
                register(member);
                return true;
            }
            
        } catch (Exception ex) {
            log.error("Error on creating admin",ex);
        }
        return false;
    }
}
