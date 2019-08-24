package com.kutaybezci.community.bl;

import com.kutaybezci.community.types.bl.CreateMemberRequest;
import com.kutaybezci.community.types.bl.CreateMemberResponse;
import com.kutaybezci.community.types.bl.DisplayMemberRequest;
import com.kutaybezci.community.types.bl.ListMemberRequest;
import com.kutaybezci.community.types.bl.ListMemberResponse;
import com.kutaybezci.community.types.bl.MemberDisplay;
import com.kutaybezci.community.types.bl.UpdateMemberRequest;
import com.kutaybezci.community.types.bl.UpdateMemberResponse;
import java.util.Optional;

/**
 *
 * @author kutay.bezci
 */
public interface MemberService {

    public Optional<MemberDisplay> doFindByLogin(String loginName);

    public boolean doInitAdmin();

    public CreateMemberResponse doCreateMember(CreateMemberRequest request);

    public ListMemberResponse doListMember(ListMemberRequest request);

    public MemberDisplay doDisplayMember(DisplayMemberRequest request);

    public UpdateMemberResponse doUpdateMember(UpdateMemberRequest request);
}
