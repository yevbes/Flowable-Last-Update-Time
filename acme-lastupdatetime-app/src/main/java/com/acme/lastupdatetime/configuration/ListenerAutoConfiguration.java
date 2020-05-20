package com.acme.lastupdatetime.configuration;

import com.acme.lastupdatetime.listeners.LastUpdateTimeEventListener;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Collections;

/**
 * @author Ievgenii Bespal
 */

@Configuration
public class ListenerAutoConfiguration {

    @Bean
    public LastUpdateTimeEventListener lastUpdateTimeEventListener(DataSource dataSource) {
        return new LastUpdateTimeEventListener(dataSource);
    }

    @Bean
    @ConditionalOnClass(SpringProcessEngineConfiguration.class)
    public EngineConfigurationConfigurer<SpringProcessEngineConfiguration> customizeSpringProcessEngineConfiguration(DataSource dataSource) {
        return processEngineConfiguration -> {
            processEngineConfiguration.setEventListeners(Collections.singletonList(new LastUpdateTimeEventListener(dataSource)));
        };
    }
}