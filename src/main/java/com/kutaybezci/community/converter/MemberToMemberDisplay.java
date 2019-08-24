package com.kutaybezci.community.converter;

import com.kutaybezci.community.types.bl.MemberDisplay;
import com.kutaybezci.community.types.model.Member;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author Kutay Bezci
 */
public class MemberToMemberDisplay implements Converter<Member, MemberDisplay> {

    @Override
    public MemberDisplay convert(Member member) {
        MemberDisplay response = new MemberDisplay();
        response.setMemberId(member.getId().toString());
        response.setEmail(member.getEmail());
        response.setFullname(member.getFullname());
        response.setMemberId(member.getId().toString());
        response.setPhone(member.getPhone());
        response.setUsername(member.getUsername());
        response.setCanLogin(!StringUtils.isBlank(member.getPassword()));
        member.getMemberRoles().stream().map(y -> y.getRoleCode()).forEach(y -> response.getRoles().add(y));
        return response;
    }

}
