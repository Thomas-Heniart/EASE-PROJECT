package com.Ease.API.RestEasy;

import com.Ease.API.Rest.GetAppInformationRE;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class RestEasyApplication extends Application {
    private Set<Object> singletons = new HashSet<Object>();

    public RestEasyApplication() {
        singletons.add(new GetAppInformationRE());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}