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

import com.kutaybezci.community.types.bl.CreateMemberRequest;
import com.kutaybezci.community.types.bl.CreateMemberResponse;
import com.kutaybezci.community.types.bl.DisplayMemberRequest;
import com.kutaybezci.community.types.bl.DisplayMemberResponse;
import com.kutaybezci.community.types.bl.ListMemberRequest;
import com.kutaybezci.community.types.bl.ListMemberResponse;
import com.kutaybezci.community.types.bl.UpdateMemberRequest;
import com.kutaybezci.community.types.bl.UpdateMemberResponse;
import com.kutaybezci.community.types.model.Member;
import java.util.Optional;

/**
 *
 * @author kutay.bezci
 */
public interface MemberService {

    public Optional<Member> doFindByLogin(String loginName);

    public boolean doInitAdmin();

    public CreateMemberResponse doCreateMember(CreateMemberRequest request);

    public ListMemberResponse doListMember(ListMemberRequest request);

    public DisplayMemberResponse doDisplayMember(DisplayMemberRequest request);

    public UpdateMemberResponse doUpdateMember(UpdateMemberRequest request);
}
