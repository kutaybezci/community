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
package com.kutaybezci.community.dal;

import com.kutaybezci.community.types.model.Member;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author kutay.bezci
 */
public interface MemberRepository extends CrudRepository<Member, Long> {

    Member findByUsername(String username);

    Member findByEmail(String email);

}
