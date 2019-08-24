package com.kutaybezci.community.types.bl;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Kutay Bezci
 */
@Data
public class ListMemberResponse {

    List<MemberDisplay> memberList = new ArrayList<>();
}
