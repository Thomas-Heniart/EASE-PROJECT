package com.Ease.backend.common.configuration;

import com.Ease.API.Rest.GetAppInformationRE;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class RestEasyCoreConfiguration extends Application {
    private Set<Object> singletons = new HashSet<Object>();

    public RestEasyCoreConfiguration() {
        singletons.add(new GetAppInformationRE());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
