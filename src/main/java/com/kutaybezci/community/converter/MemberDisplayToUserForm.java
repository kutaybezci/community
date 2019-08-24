package com.kutaybezci.community.converter;

import com.kutaybezci.community.types.bl.MemberDisplay;
import com.kutaybezci.community.types.fe.UserForm;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author Kutay Bezci
 */
public class MemberDisplayToUserForm implements Converter<MemberDisplay, UserForm> {

    @Override
    public UserForm convert(MemberDisplay s) {
        UserForm form = new UserForm();
        form.setMemberId(s.getMemberId());
        form.setEmail(s.getEmail());
        form.setFullname(s.getFullname());
        form.setPhone(s.getPhone());
        form.setUpdatePassword(false);
        form.setUsername(s.getUsername());
        return form;
    }

}
