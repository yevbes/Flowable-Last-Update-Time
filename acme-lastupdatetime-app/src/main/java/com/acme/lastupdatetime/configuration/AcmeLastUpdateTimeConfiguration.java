package com.acme.lastupdatetime.configuration;

import org.flowable.cmmn.api.CmmnRepositoryService;
import org.flowable.dmn.api.DmnRepositoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.form.api.FormRepositoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
