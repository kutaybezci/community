package com.kutaybezci.community.dal;

import com.kutaybezci.community.types.model.Member;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author kutay.bezci
 */
public interface MemberRepository extends CrudRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    Optional<Member> findByEmail(String email);

}
