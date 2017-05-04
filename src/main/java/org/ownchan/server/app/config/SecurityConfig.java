package org.ownchan.server.app.config;

import org.apache.commons.lang3.StringUtils;
import org.ownchan.server.app.service.ContextUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
public class SecurityConfig {

  @Autowired
  private ContextUserService contextUserService;

  @Autowired
  private ServerAppConfig serverAppConfig;

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(serverAppConfig.getPasswordHashStrength());
    return bCryptPasswordEncoder;
  }

  @Bean
  public DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher() {
    return new DefaultAuthenticationEventPublisher();
  }

  @Bean
  public TokenBasedRememberMeServices tokenBasedRememberMeServices() {
    TokenBasedRememberMeServices rememberMe = new TokenBasedRememberMeServices(serverAppConfig.getApplicationSecret(), contextUserService);
    rememberMe.setCookieName("oh-hi-mark");
    rememberMe.setAlwaysRemember(true);
    if (StringUtils.isNotEmpty(serverAppConfig.getCookieDomain())) {
      rememberMe.setCookieDomain(serverAppConfig.getCookieDomain());
    }
    rememberMe.setTokenValiditySeconds(serverAppConfig.getLoginValiditySeconds());
    rememberMe.setUseSecureCookie(serverAppConfig.isUseSecureCookies());

    return rememberMe;
  }

}
