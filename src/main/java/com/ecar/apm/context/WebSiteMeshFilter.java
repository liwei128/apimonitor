package com.ecar.apm.context;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;


public class WebSiteMeshFilter extends ConfigurableSiteMeshFilter {


    public WebSiteMeshFilter() {
    }


    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        builder.addDecoratorPath("/*", "/decorator")
                .addExcludedPath("/static/**")
                .addExcludedPath("/load_addition_monitor_logs.hb*")
                .addExcludedPath("/monitoring/statistics/*.hb*")
                .addExcludedPath("/user/reset_password/*.hb*")
                .addExcludedPath("/user/delete/*.hb*")
                .addExcludedPath("/login.hb*");

    }
}
