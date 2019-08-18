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
package com.kutaybezci.community.bl.impl;

import com.kutaybezci.community.bl.MemberService;
import com.kutaybezci.community.dal.MemberRepository;
import com.kutaybezci.community.types.bl.CreateMemberRequest;
import com.kutaybezci.community.types.bl.CreateMemberResponse;
import com.kutaybezci.community.types.bl.DisplayMemberRequest;
import com.kutaybezci.community.types.bl.DisplayMemberResponse;
import com.kutaybezci.community.types.bl.ListMemberRequest;
import com.kutaybezci.community.types.bl.ListMemberResponse;
import com.kutaybezci.community.types.bl.MemberListItem;
import com.kutaybezci.community.types.bl.UpdateMemberRequest;
import com.kutaybezci.community.types.bl.UpdateMemberResponse;
import com.kutaybezci.community.types.model.Member;
import com.kutaybezci.community.types.model.Role;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Kutay Bezci
 */
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final String EMAIL_AT = "@";

    @Override
    @Transactional
    public Optional<Member> doFindByLogin(String loginName) {
        return findByLogin(loginName);
    }

    @Override
    @Transactional
    public boolean doInitAdmin() {
        return initAdmin();
    }

    @Override
    @Transactional
    public CreateMemberResponse doCreateMember(CreateMemberRequest request) {
        return createMember(request);
    }

    @Override
    @Transactional
    public ListMemberResponse doListMember(ListMemberRequest request) {
        return listMember(request);
    }

    @Override
    @Transactional
    public DisplayMemberResponse doDisplayMember(DisplayMemberRequest request) {
        return displayMember(request);
    }

    @Override
    @Transactional
    public UpdateMemberResponse doUpdateMember(UpdateMemberRequest request) {
        return updateMember(request);
    }

    public Optional<Member> findByLogin(String loginName) {
        /*if (StringUtils.contains(loginName, EMAIL_AT)) {
            return memberRepository.findByEmail(loginName);
        } else {*/
        return memberRepository.findByUsername(loginName);
        //}
    }

    public boolean initAdmin() {
        try {
            if (memberRepository.count() == 0) {
                final String[] users = new String[]{"ADMIN", "SYSTEM"};
                for (String user : users) {
                    Member m = new Member();
                    m.setFullname(user);
                    m.setUsername(user);
                    if (StringUtils.equals(users[0], user)) {
                        m.setPassword(bCryptPasswordEncoder.encode(users[0]));
                    }
                    memberRepository.save(m);
                }
                return true;
            }
        } catch (Exception ex) {
            log.error("Error on creating admin", ex);
        }
        return false;
    }

    public CreateMemberResponse createMember(CreateMemberRequest request) {
        if (StringUtils.isAllBlank(request.getUsername())) {
            throw new RuntimeException("username.not.empty");
        }
        if (StringUtils.containsAny(request.getUsername(), EMAIL_AT)) {
            throw new RuntimeException("username.not.valid");
        }
        if (StringUtils.isNotBlank(request.getEmail())) {
            if (StringUtils.containsNone(request.getEmail(), EMAIL_AT)) {
                throw new RuntimeException("email.not.valid");
            }
            Optional<Member> m = memberRepository.findByEmail(request.getEmail());
            if (m.isPresent()) {
                throw new RuntimeException("email.in.use");
            }
        }
        Optional<Member> m = memberRepository.findByUsername(request.getUsername());
        if (m.isPresent()) {
            throw new RuntimeException("username.in.use");
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

    public ListMemberResponse listMember(ListMemberRequest request) {
        Iterable<Member> members = memberRepository.findAll();
        ListMemberResponse response = new ListMemberResponse();
        response.setMemberList(new ArrayList<>());
        for (Member m : members) {
            MemberListItem mi = new MemberListItem();
            mi.setEmail(m.getEmail());
            mi.setFullname(m.getFullname());
            mi.setId(m.getId().toString());
            mi.setPhone(m.getPhone());
            mi.setUsername(m.getUsername());
            response.getMemberList().add(mi);
        }
        return response;
    }

    public DisplayMemberResponse displayMember(DisplayMemberRequest request) {
        long id = -1;
        try {
            id = Long.valueOf(request.getMemberId());
        } catch (NumberFormatException ex) {
            log.info("Wrong format for member id", ex);
        }
        Optional<Member> member = memberRepository.findById(id);
        if (!member.isPresent()) {
            throw new RuntimeException("member.not.found");
        }
        Member m = member.get();
        DisplayMemberResponse response = new DisplayMemberResponse();
        response.setEmail(m.getEmail());
        response.setFullname(m.getFullname());
        response.setMemberId(m.getId().toString());
        response.setPhone(m.getPhone());
        response.setUsername(m.getUsername());
        return response;
    }

    public UpdateMemberResponse updateMember(UpdateMemberRequest request) {
        long id = -1;
        try {
            id = Long.valueOf(request.getMemberId());
        } catch (NumberFormatException ex) {
            log.info("Wrong format for member id", ex);
        }
        if (StringUtils.contains(request.getUsername(), EMAIL_AT)) {
            throw new RuntimeException("username.not.valid");
        }
        if (StringUtils.isNotBlank(request.getEmail())) {
            if (StringUtils.containsNone(request.getEmail(), EMAIL_AT)) {
                throw new RuntimeException("email.not.valid");
            }
        }
        Optional<Member> member = memberRepository.findById(id);
        if (!member.isPresent()) {
            throw new RuntimeException("member.not.found");
        } else {
            Member m = member.get();
            if (!StringUtils.equals(request.getUsername(), m.getUsername())) {
                Optional<Member> other = memberRepository.findByUsername(request.getUsername());
                if (other.isPresent()) {
                    throw new RuntimeException("username.in.use");
                }
                m.setUsername(request.getUsername());
            }
            if (!StringUtils.isBlank(request.getEmail())
                    && !StringUtils.equals(request.getEmail(), m.getEmail())) {
                Optional<Member> other = memberRepository.findByEmail(request.getEmail());
                if (other.isPresent()) {
                    throw new RuntimeException("email.in.use");
                }
            }
            m.setEmail(request.getEmail());
            m.setFullname(request.getFullname());
            m.setPhone(request.getPhone());
            if (request.isUpdatePassword()) {
                if (!StringUtils.isBlank(request.getPassword())) {
                    m.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
                } else {
                    m.setPassword(null);
                }
            }
            memberRepository.save(m);
            return new UpdateMemberResponse();
        }
    }

}
