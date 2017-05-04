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

import java.util.LinkedList;

import org.ownchan.server.joint.scheduler.AutowiringSpringBeanJobFactory;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {

  @Autowired
  private ApplicationConfig applicationConfig;

  @Value("${schedule.cron.group}")
  private String jobGroup;

  @Value("${schedule.cron.trigger.suffix}")
  private String triggerSuffix;

  /*@Value("${schedule.cron.dummyAction.name}")
  private String dummyActionJobName;
  
  @Value("${schedule.cron.dummyAction.pattern}")
  private String dummyActionJobPattern;
  
  @Value("${schedule.cron.dummyAction.corePoolSize}")
  private int dummyActionCorePoolSize;
  
  @Value("${schedule.cron.dummyAction.maxPoolSize}")
  private int dummyActionMaxPoolSize;
  
  @Value("${schedule.cron.dummyAction.enabled}")
  private boolean dummyActionEnabled;*/

  @Bean
  public AutowiringSpringBeanJobFactory quartzJobFactory() {
    AutowiringSpringBeanJobFactory factory = new AutowiringSpringBeanJobFactory();
    factory.setIgnoredUnknownProperties("applicationContext");
    return factory;
  }

  @Bean
  public JobDetail[] jobs() {
    LinkedList<JobDetail> jobs = new LinkedList<>();

    /*jobs.add(JobBuilder
        .newJob(DummyActionJob.class)
        .withIdentity(dummyActionJobName, jobGroup)
        .withDescription("perform a dummy action")
        .storeDurably()
        .build());*/

    return jobs.toArray(new JobDetail[] {});
  }

  @Bean
  public Trigger[] jobTrigger() {
    LinkedList<Trigger> trigger = new LinkedList<>();

    /*if (dummyActionEnabled) {
      trigger.add(TriggerBuilder.newTrigger()
          .forJob(dummyActionJobName, jobGroup)
          .withIdentity(dummyActionJobName + triggerSuffix)
          .withSchedule(CronScheduleBuilder.cronSchedule(dummyActionJobPattern))
          .build());
    }*/

    return trigger.toArray(new Trigger[] {});
  }

  @Bean
  public SchedulerFactoryBean schedulerFactoryBean() {
    SchedulerFactoryBean factory = new SchedulerFactoryBean();
    factory.setConfigLocation(new ClassPathResource("quartz.properties"));
    factory.setDataSource(applicationConfig.getPersistenceConfig().dataSource());
    factory.setTransactionManager(applicationConfig.getPersistenceConfig().transactionManager());
    factory.setJobFactory(quartzJobFactory());
    factory.setOverwriteExistingJobs(true);
    factory.setAutoStartup(true);
    factory.setSchedulerName(applicationConfig.getApplicationName() + " scheduler");
    factory.setApplicationContextSchedulerContextKey("applicationContext");
    factory.setWaitForJobsToCompleteOnShutdown(true);
    factory.setJobDetails(jobs());
    factory.setTriggers(jobTrigger());

    return factory;
  }

  /*@Bean
  public ThreadPoolTaskExecutor dummyActionExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(dummyActionCorePoolSize);
    executor.setMaxPoolSize(dummyActionMaxPoolSize);
    executor.setThreadNamePrefix(StringUtils.capitalize(dummyActionJobName) + "-");
    executor.initialize();
    return executor;
  }
  
  public String getDummyActionJobName() {
    return dummyActionJobName;
  }
  
  public String getDummyActionJobPattern() {
    return dummyActionJobPattern;
  }
  
  public int getDummyActionCorePoolSize() {
    return dummyActionCorePoolSize;
  }
  
  public int getDummyActionMaxPoolSize() {
    return dummyActionMaxPoolSize;
  }
  
  public boolean isDummyActionEnabled() {
    return dummyActionEnabled;
  }*/

}
