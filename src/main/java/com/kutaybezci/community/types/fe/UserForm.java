package com.kutaybezci.community.types.fe;

import lombok.Data;

/**
 *
 * @author kutay.bezci
 */
@Data
public class UserForm {

    private String memberId;
    private String username;
    private String password;
    private String password2;
    private String fullname;
    private String phone;
    private String email;
    private boolean updatePassword;
}
