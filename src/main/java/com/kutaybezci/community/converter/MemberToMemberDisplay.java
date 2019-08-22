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
