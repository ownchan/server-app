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
package org.ownchan.server.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = ServerAppConfig.CONFIG_PREFIX, ignoreUnknownFields = true)
public class ServerAppConfig {

  public static final String CONFIG_PREFIX = "ownchan.server.app";

  private int passwordHashStrength;

  private boolean allowPublicAccess;

  // TODO if missing, this should be generated automatically and written to the config file afterwards
  private String applicationSecret;

  private String cookieDomain;

  private int loginValiditySeconds;

  private boolean useSecureCookies;

  public int getPasswordHashStrength() {
    return passwordHashStrength;
  }

  public void setPasswordHashStrength(int passwordHashStrength) {
    this.passwordHashStrength = passwordHashStrength;
  }

  public boolean isAllowPublicAccess() {
    return allowPublicAccess;
  }

  public void setAllowPublicAccess(boolean allowPublicAccess) {
    this.allowPublicAccess = allowPublicAccess;
  }

  public String getApplicationSecret() {
    return applicationSecret;
  }

  public void setApplicationSecret(String applicationSecret) {
    this.applicationSecret = applicationSecret;
  }

  public String getCookieDomain() {
    return cookieDomain;
  }

  public void setCookieDomain(String cookieDomain) {
    this.cookieDomain = cookieDomain;
  }

  public int getLoginValiditySeconds() {
    return loginValiditySeconds;
  }

  public void setLoginValiditySeconds(int loginValiditySeconds) {
    this.loginValiditySeconds = loginValiditySeconds;
  }

  public boolean isUseSecureCookies() {
    return useSecureCookies;
  }

  public void setUseSecureCookies(boolean useSecureCookies) {
    this.useSecureCookies = useSecureCookies;
  }

}
