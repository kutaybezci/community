package com.kutaybezci.community.types.bl;

import lombok.Data;

/**
 *
 * @author Kutay Bezci
 */
@Data
public class UpdateMemberRequest {

    private String memberId;
    private String username;
    private String email;
    private String password;
    private String phone;
    private String fullname;
    private boolean updatePassword;

}
