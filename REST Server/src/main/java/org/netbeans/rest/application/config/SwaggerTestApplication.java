/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.rest.application.config;

import io.swagger.jaxrs.config.BeanConfig;
import java.util.HashSet;
import java.util.Set;
import javax.faces.application.Resource;
import javax.ws.rs.core.Application;

/**
 *
 * @author Paul
 */
public class SwaggerTestApplication extends Application {

    public SwaggerTestApplication() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setTitle("My API");
        beanConfig.setBasePath("/MavenServer");
        beanConfig.setResourcePackage("com.paths");
        beanConfig.setScan(true);
    }

    @Override
    public Set<Class<?>> getClasses() {
        HashSet<Class<?>> set = new HashSet<Class<?>>();

        set.add(Resource.class);
        set.add(com.paths.Homepage.class);
        set.add(com.paths.Search.class);
        set.add(com.paths.Stores.class);
        set.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        set.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);

        return set;
    }
}
