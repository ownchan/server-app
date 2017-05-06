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

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.ownchan.server.joint.persistence.template.UserTemplate;
import org.ownchan.server.joint.persistence.valuetype.UserStatus;
import org.ownchan.server.joint.security.ContextUser;
import org.ownchan.server.persistence.model.DbUser;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

public class ContextUserImpl implements ContextUser {

  private static final long serialVersionUID = 6592451097649295983L;

  private long id;

  private UserStatus status;

  private String statusReason;

  private String alias;

  private String displayName;

  private String passwordHash;

  private Date createTime;

  private Date updateTime;

  private String email;

  private String motto;

  private String externalLink;

  private Long avatarContentId;

  private Date lastPasswordChangeTime;

  private Date beaconTime;

  private Date lastDbLoadTime;

  private Set<ContextUserAuthorityImpl> authorities;

  public ContextUserImpl(DbUser dbUser) {
    Assert.notNull(dbUser, "a persisted user is required");
    BeanUtils.copyProperties(dbUser, this, UserTemplate.class);
    this.lastDbLoadTime = new Date();

    this.authorities = new HashSet<>();
    if (CollectionUtils.isNotEmpty(dbUser.getLinkedRoles())) {
      dbUser.getLinkedRoles().stream().forEach(role -> {
        this.authorities.add(new ContextUserAuthorityImpl(role));
        if (CollectionUtils.isNotEmpty(role.getLinkedPrivileges())) {
          role.getLinkedPrivileges().stream()
              .map(ContextUserAuthorityImpl::new)
              .forEach(this.authorities::add);
        }
      });
    }
  }

  @Override
  public long getId() {
    return id;
  }

  @Override
  public UserStatus getStatus() {
    return status;
  }

  @Override
  public String getStatusReason() {
    return statusReason;
  }

  @Override
  public String getAlias() {
    return alias;
  }

  @Override
  public String getDisplayName() {
    return displayName;
  }

  @Override
  public String getPasswordHash() {
    return passwordHash;
  }

  @Override
  public Date getCreateTime() {
    return createTime;
  }

  @Override
  public Date getUpdateTime() {
    return updateTime;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public String getMotto() {
    return motto;
  }

  @Override
  public String getExternalLink() {
    return externalLink;
  }

  @Override
  public Long getAvatarContentId() {
    return avatarContentId;
  }

  @Override
  public Date getLastPasswordChangeTime() {
    return lastPasswordChangeTime;
  }

  @Override
  public Date getBeaconTime() {
    return beaconTime;
  }

  @Override
  public Date getLastDbLoadTime() {
    return lastDbLoadTime;
  }

  @Override
  public Set<ContextUserAuthorityImpl> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return passwordHash;
  }

  @Override
  public String getUsername() {
    return alias;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !Objects.equals(UserStatus.BANNED, status);
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return !Objects.equals(UserStatus.DISABLED, status);
  }

}
