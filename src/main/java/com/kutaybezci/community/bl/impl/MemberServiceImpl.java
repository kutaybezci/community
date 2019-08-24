package com.kutaybezci.community.bl.impl;

import com.kutaybezci.community.bl.MemberService;
import com.kutaybezci.community.dal.MemberRepository;
import com.kutaybezci.community.types.bl.CreateMemberRequest;
import com.kutaybezci.community.types.bl.CreateMemberResponse;
import com.kutaybezci.community.types.bl.DisplayMemberRequest;
import com.kutaybezci.community.types.bl.ListMemberRequest;
import com.kutaybezci.community.types.bl.ListMemberResponse;
import com.kutaybezci.community.types.bl.MemberDisplay;
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
import org.springframework.core.convert.ConversionService;
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
    private ConversionService conversionService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final String EMAIL_AT = "@";

    @Override
    @Transactional
    public Optional<MemberDisplay> doFindByLogin(String loginName) {
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
    public MemberDisplay doDisplayMember(DisplayMemberRequest request) {
        return displayMember(request);
    }

    @Override
    @Transactional
    public UpdateMemberResponse doUpdateMember(UpdateMemberRequest request) {
        return updateMember(request);
    }

    public Optional<MemberDisplay> findByLogin(String loginName) {
        Optional<Member> member = memberRepository.findByUsername(loginName);
        if (member.isPresent()) {
            MemberDisplay response = conversionService.convert(member.get(), MemberDisplay.class);
            return Optional.of(response);
        } else {
            return Optional.empty();
        }
    }

    public boolean initAdmin() {
        try {
            if (memberRepository.count() == 0) {
                final String[] users = new String[]{"SYSTEM", "ADMIN", "USER"};
                for (int i = 0; i < users.length; i++) {
                    String user = users[i];
                    Member m = new Member();
                    m.setFullname(user);
                    m.setUsername(user);
                    m.setMemberRoles(new HashSet());
                    if (i > 0) {
                        m.setPassword(bCryptPasswordEncoder.encode(user));
                        Role role = new Role();
                        role.setRoleCode(user);
                        m.getMemberRoles().add(role);
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
        for (Member model : members) {
            MemberDisplay m = conversionService.convert(model, MemberDisplay.class);
            response.getMemberList().add(m);
        }
        return response;
    }

    public MemberDisplay displayMember(DisplayMemberRequest request) {
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
        MemberDisplay response = conversionService.convert(m, MemberDisplay.class);
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
