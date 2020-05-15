package com.acme.lastupdatetime.configuration;

import com.acme.lastupdatetime.listeners.LastUpdateTimeEventListener;
import org.flowable.cmmn.api.CmmnRepositoryService;
import org.flowable.dmn.api.DmnRepositoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Collections;

@Configuration
public class AcmeLastUpdateTimeConfiguration {

    @Bean
    public CommandLineRunner init(final RepositoryService processRepositoryService,
                                  final CmmnRepositoryService caseRepositoryService,
                                  final DmnRepositoryService dmnRepositoryService,
                                  final FormRepositoryService formRepositoryService) {

        return strings -> {
            System.out.println("Number of process definitions : " + processRepositoryService.createProcessDefinitionQuery().count());
            System.out.println("Number of case definitions : " + caseRepositoryService.createCaseDefinitionQuery().count());
            System.out.println("Number of dmn definitions : " + dmnRepositoryService.createDecisionTableQuery().count());
            System.out.println("Number of form definitions : " + formRepositoryService.createFormDefinitionQuery().count());
        };
    }

    @Bean
    @ConditionalOnClass(SpringProcessEngineConfiguration.class)
    public EngineConfigurationConfigurer<SpringProcessEngineConfiguration> customizeSpringProcessEngineConfiguration(DataSource dataSource) {
        return processEngineConfiguration -> {
            processEngineConfiguration.setEventListeners(Collections.singletonList(new LastUpdateTimeEventListener(dataSource)));
        };
    }
}
