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
package org.ownchan.server.app.security;

import org.ownchan.server.joint.security.ContextUserAuthority;
import org.ownchan.server.joint.security.ContextUserAuthorityType;
import org.ownchan.server.persistence.model.DbPrivilege;
import org.ownchan.server.persistence.model.DbRole;
import org.springframework.util.Assert;

public class ContextUserAuthorityImpl implements ContextUserAuthority {

  private static final long serialVersionUID = -4059289708939218670L;

  private static final String MSG_AUTHORITY_REQUIRED = "a granted authority textual representation is required";

  private ContextUserAuthorityType type;

  private long typeBasedId;

  private String typeBasedName;

  private Long msgIdName;

  private Long msgIdDescription;

  private String authority;

  public ContextUserAuthorityImpl(DbPrivilege dbPrivilege) {
    Assert.notNull(dbPrivilege, MSG_AUTHORITY_REQUIRED);
    this.type = ContextUserAuthorityType.PRIVILEGE;
    this.typeBasedId = dbPrivilege.getId();
    this.typeBasedName = dbPrivilege.getName();
    this.msgIdName = dbPrivilege.getMsgIdName();
    this.msgIdDescription = dbPrivilege.getMsgIdDescription();
    this.authority = this.type.getNamespace() + "_" + this.typeBasedName;
  }

  public ContextUserAuthorityImpl(DbRole dbRole) {
    Assert.notNull(dbRole, MSG_AUTHORITY_REQUIRED);
    this.type = ContextUserAuthorityType.ROLE;
    this.typeBasedId = dbRole.getId();
    this.typeBasedName = dbRole.getName();
    this.msgIdName = dbRole.getMsgIdName();
    this.msgIdDescription = dbRole.getMsgIdDescription();
    this.authority = this.type.getNamespace() + "_" + this.typeBasedName;
  }

  @Override
  public ContextUserAuthorityType getType() {
    return type;
  }

  @Override
  public long getTypeBasedId() {
    return typeBasedId;
  }

  @Override
  public String getTypeBasedName() {
    return typeBasedName;
  }

  @Override
  public Long getMsgIdName() {
    return msgIdName;
  }

  @Override
  public Long getMsgIdDescription() {
    return msgIdDescription;
  }

  @Override
  public String getAuthority() {
    return authority;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj instanceof ContextUserAuthorityImpl) {
      return authority.equals(((ContextUserAuthorityImpl) obj).authority);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return this.authority.hashCode();
  }

  @Override
  public String toString() {
    return this.authority;
  }

}
