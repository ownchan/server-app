/*******************************************************************************
 * @author Stefan Gündhör <stefan@guendhoer.com>
 *
 * @copyright Copyright (c) 2017, Stefan Gündhör <stefan@guendhoer.com>
 * @license AGPL-3.0
 *
 * This code is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License, version 3,
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *******************************************************************************/
package org.ownchan.server.app.service;

import org.ownchan.server.app.security.ContextUserImpl;
import org.ownchan.server.persistence.dao.UserDao;
import org.ownchan.server.persistence.model.DbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ContextUserService implements UserDetailsService {

  @Autowired
  private UserDao userDao;

  // TODO this method should be cacheable for x minutes, so we don't have to load the user from the db for every performed click
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    DbUser dbUser = userDao.getByAlias(username);

    if (dbUser != null) {
      return new ContextUserImpl(dbUser);
    }

    throw new UsernameNotFoundException(String.format("no user with alias %s found in database", username));
  }

}
