package ru.softshaper.conf;

import java.io.IOException;

import javax.sql.DataSource;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;

/**
 *
 * CommundaConfig for BPNM Engine
 *
 * @author ashek
 *
 */
@Configuration
@Import({ JooqConfig.class })
@ComponentScan("ru.softshaper.services.workflow")
public class CommundaConfig {
  /**
   * DataSource
   */
  @Autowired
  private DataSource dataSource;
  /**
   * PlatformTransactionManager
   */
  @Autowired
  private PlatformTransactionManager transactionManager;

  /**
   * load process bpnm
   *
   * @return SpringProcessEngineConfiguration
   * @throws IOException
   */
  @Bean
  public SpringProcessEngineConfiguration springProcessEngineConfiguration() throws IOException {
    SpringProcessEngineConfiguration springProcessEngineConfiguration = new SpringProcessEngineConfiguration();
    springProcessEngineConfiguration.setProcessEngineName("engine_communda");
    springProcessEngineConfiguration.setDataSource(dataSource);
    springProcessEngineConfiguration.setTransactionManager(transactionManager);
    springProcessEngineConfiguration.setDatabaseSchemaUpdate("true");
    springProcessEngineConfiguration.setJobExecutorActivate(false);
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    springProcessEngineConfiguration.setDeploymentResources(resolver.getResources("classpath*:processes/*.bpmn"));
    return springProcessEngineConfiguration;
  }

  /**
   * @param springProcessEngineConfiguration
   * @return
   */
  @Bean
  public ProcessEngineFactoryBean processEngineFactoryBean(SpringProcessEngineConfiguration springProcessEngineConfiguration) {
    ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
    processEngineFactoryBean.setProcessEngineConfiguration(springProcessEngineConfiguration);
    return processEngineFactoryBean;
  }

  /**
   * Service providing access to the repository of process definitions and deployments.
   *
   * @param processEngine
   * @return
   */
  @Bean
  public RepositoryService repositoryService(ProcessEngine processEngine) {
    return processEngine.getRepositoryService();
  }

  /**
   * Service which provides access to {@link Deployment}s, {@link ProcessDefinition}s and {@link ProcessInstance}s
   *
   * @param processEngine
   * @return
   */
  @Bean
  public RuntimeService runtimeService(ProcessEngine processEngine) {
    return processEngine.getRuntimeService();
  }

  /**
   * Service which provides access to {@link Task} and form related operations.
   *
   * @param processEngine
   * @return
   */
  @Bean
  public TaskService taskService(ProcessEngine processEngine) {
    return processEngine.getTaskService();
  }

  /**
   * Service exposing information about ongoing and past process instances. This is different from the runtime information in the sense that this runtime information only contains
   * the actual runtime state at any given moment and it is optimized for runtime process execution performance. The history information is optimized for easy querying and remains
   * permanent in the persistent storage.
   *
   * @param processEngine
   * @return
   */
  @Bean
  public HistoryService historyService(ProcessEngine processEngine) {
    return processEngine.getHistoryService();
  }

  /**
   * Service for admin and maintenance operations on the process engine.
   *
   * @param processEngine
   * @return
   */
  @Bean
  public ManagementService managementService(ProcessEngine processEngine) {
    return processEngine.getManagementService();
  }

  /**
   * Access to form data and rendered forms for starting new process instances and completing tasks.
   *
   * @param processEngine Access to form data and rendered forms for starting new process instances and completing tasks.
   * @return
   */
  @Bean
  public FormService formService(ProcessEngine processEngine) {
    return processEngine.getFormService();
  }
}
