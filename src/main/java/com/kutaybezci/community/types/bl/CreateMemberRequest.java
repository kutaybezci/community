package com.kutaybezci.community.types.bl;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;

/**
 *
 * @author Kutay Bezci
 */
@Data
public class CreateMemberRequest {

    private String username;
    private String password;
    private String fullname;
    private String email;
    private String phone;
    private Set<String> roles = new HashSet<>();
}
