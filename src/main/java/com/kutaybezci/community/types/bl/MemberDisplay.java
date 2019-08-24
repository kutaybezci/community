package com.kutaybezci.community.types.bl;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;

/**
 *
 * @author Kutay Bezci
 */
@Data
public class MemberDisplay {

    private String memberId;
    private String username;
    private String email;
    private String fullname;
    private String phone;
    private Set<String> roles = new HashSet();
    private boolean canLogin;
}
