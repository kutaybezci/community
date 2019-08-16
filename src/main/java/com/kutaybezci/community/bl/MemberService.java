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
import com.kutaybezci.community.types.bl.CreateMemberRequest;
import com.kutaybezci.community.types.bl.CreateMemberResponse;
import com.kutaybezci.community.types.model.Member;
import com.kutaybezci.community.types.model.Role;
import java.util.HashSet;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Transactional
    public Optional<Member> findByLogin(String loginName) {
        Member member;
        if (StringUtils.contains(loginName, "@")) {
            member = memberRepository.findByEmail(loginName);
        } else {
            member = memberRepository.findByUsername(loginName);
        }
        if (member == null) {
            return Optional.empty();
        } else {
            return Optional.of(member);
        }
    }

    public boolean initAdmin() {
        try {
            if (memberRepository.count() == 0) {
                val admin = "ADMIN";
                CreateMemberRequest request = new CreateMemberRequest();
                request.setFullname(admin);
                request.setPassword(admin);
                request.setUsername(admin);
                request.setRoles(new HashSet<>());
                request.getRoles().add(admin);
                createMember(request);
                return true;
            }
        } catch (Exception ex) {
            log.error("Error on creating admin", ex);
        }
        return false;
    }

    @Transactional
    public CreateMemberResponse createMember(CreateMemberRequest request) {
        if (StringUtils.isAllBlank(request.getUsername())) {
            throw new RuntimeException("Username cannot be empty");
        }
        String emailAt = "@";
        if (StringUtils.containsAny(request.getUsername(), emailAt)) {
            throw new RuntimeException(String.format("Username cannot contain:%s", emailAt));
        }
        if (StringUtils.isNotBlank(request.getEmail())) {
            if (StringUtils.containsNone(request.getEmail(), emailAt)) {
                throw new RuntimeException("Email is not valid");
            }
            Member m = memberRepository.findByEmail(request.getEmail());
            if (m != null) {
                throw new RuntimeException("Username is already taken");
            }
        }
        Member m = memberRepository.findByUsername(request.getUsername());
        if (m != null) {
            throw new RuntimeException("Username already exists");
        }
        Member member = new Member();
        member.setUsername(request.getUsername());
        member.setEmail(request.getEmail());
        member.setFullname(request.getFullname());
        if (StringUtils.isNotBlank(request.getPassword())) {
            member.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        }
        member.setPhone(request.getPhone());
        member.setMemberRoles(new HashSet<>());
        request.getRoles().stream().map((role) -> {
            Role r = new Role();
            r.setRoleCode(role);
            return r;
        }).forEach((r) -> {
            member.getMemberRoles().add(r);
        });
        memberRepository.save(member);
        CreateMemberResponse response = new CreateMemberResponse();
        response.setMemberId(member.getId().toString());
        return response;
    }
}
