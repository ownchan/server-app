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

import static org.ownchan.server.joint.security.Role.ROLE_ADMIN_BASE;
import static org.ownchan.server.joint.security.Role.ROLE_CONTRIBUTOR_BASE;
import static org.ownchan.server.joint.security.Role.ROLE_SUPER_BASE;

import org.ownchan.server.app.service.ContextUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityAdapter extends WebSecurityConfigurerAdapter {

  @Autowired
  private ServerAppConfig serverAppConfig;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  private ContextUserService contextUserService;

  @Autowired
  private DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher;

  @Autowired
  private TokenBasedRememberMeServices tokenBasedRememberMeServices;

  @Override
  protected void configure(AuthenticationManagerBuilder auth)
      throws Exception {
    auth
        .authenticationEventPublisher(defaultAuthenticationEventPublisher)
        .userDetailsService(contextUserService)
        .passwordEncoder(bCryptPasswordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlRegistry = http
        .authorizeRequests()
        .antMatchers(
            "/login",
            "/reg")
        .permitAll()
        // TODO registration should be able to be disabled - but from inside the controller / template (configurable via DB)
        .antMatchers("/reg").permitAll()
        // restricted administration functionality
        .antMatchers("/r/a/**").hasAuthority(ROLE_ADMIN_BASE)
        // restricted super user (moderation, ...) functionality
        .antMatchers("/r/s/**").hasAnyAuthority(ROLE_SUPER_BASE, ROLE_ADMIN_BASE)
        // restricted authenticated user ("contributor") functionality
        .antMatchers("/r/c/**").hasAnyAuthority(ROLE_CONTRIBUTOR_BASE, ROLE_SUPER_BASE, ROLE_ADMIN_BASE);

    if (serverAppConfig.isAllowPublicAccess()) {
      urlRegistry = urlRegistry.anyRequest().anonymous();
    } else {
      urlRegistry = urlRegistry.anyRequest().authenticated();
    }

    urlRegistry
        .and().csrf()
        .disable()
        .formLogin()
        .loginPage("/login").failureUrl("/login?error=true")
        // per default redirect to the simple images-overview page
        .defaultSuccessUrl("/i/o/s")
        .usernameParameter("alias")
        .passwordParameter("password")
        .and().logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/")
        .and().exceptionHandling()
        .accessDeniedPage("/you-mad")
        .and().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().rememberMe()
        .rememberMeServices(tokenBasedRememberMeServices);
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web
        .ignoring()
        .antMatchers(
            "/resources/**",
            "/static/**",
            "/css/**",
            "/js/**",
            "/images/**");
  }

}
