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

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.ownchan.server.persistence.config.MaintenanceDataSourceProperties;
import org.ownchan.server.persistence.config.PersistenceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class ApplicationConfig {

  @Autowired
  private PersistenceConfig persistenceConfig;

  @Value("${spring.application.name}")
  private String applicationName;

  @Value("${confDir}")
  private String confDir;

  @Value("${logDir}")
  private String logDir;

  @Bean
  public Flyway flyway(MaintenanceDataSourceProperties maintenanceDataSourceProperties) {

    DataSource maintenanceDataSource = new HikariDataSource(maintenanceDataSourceProperties);

    Flyway flyway = new Flyway();
    flyway.setDataSource(maintenanceDataSource);
    flyway.setLocations("classpath:db/migration/server");
    flyway.setTable("ocn_flyway");
    flyway.setBaselineOnMigrate(true);
    flyway.migrate();

    ((HikariDataSource) maintenanceDataSource).close();
    flyway.setDataSource(persistenceConfig.dataSource());

    return flyway;
  }

  /*@Bean
  public Executor dummyActionAsyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(7);
    executor.setMaxPoolSize(42);
    executor.setQueueCapacity(11);
    executor.setThreadNamePrefix("DummyActionAsyncExecutor-");
    executor.initialize();
    return executor;
  }*/

  public String getApplicationName() {
    return applicationName;
  }

  public String getApplicationVersion() {
    return org.ownchan.server.app.ScanBaseMarker.class.getPackage().getImplementationVersion();
  }

  public String getConfDir() {
    return confDir;
  }

  public String getLogDir() {
    return logDir;
  }

  public PersistenceConfig getPersistenceConfig() {
    return persistenceConfig;
  }

}
