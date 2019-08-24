package com.kutaybezci.community.config;

import com.kutaybezci.community.dal.MemberRepository;
import com.kutaybezci.community.types.model.Member;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author kutay.bezci
 */
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findByUsername(username);
        if (member.isPresent() && !StringUtils.isBlank(member.get().getPassword())) {
            String[] roles = member.get().getMemberRoles().stream().map(y -> y.getRoleCode()).collect(Collectors.toList()).toArray(new String[0]);
            return User.withUsername(member.get().getUsername()).password(member.get().getPassword()).roles(roles).build();
        } else {
            throw new UsernameNotFoundException("User name not found");
        }

    }

}
