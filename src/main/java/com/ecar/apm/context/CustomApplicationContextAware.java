package com.ecar.apm.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
public class CustomApplicationContextAware implements ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(CustomApplicationContextAware.class);


    public CustomApplicationContextAware() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanProvider.initialize(applicationContext);
        LOG.info("Initialed BeanProvider from ApplicationContext: {}", applicationContext);
    }
}