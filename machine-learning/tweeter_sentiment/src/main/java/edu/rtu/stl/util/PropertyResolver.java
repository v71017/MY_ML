package edu.rtu.stl.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

public class PropertyResolver {

    public final PropertiesConfiguration configuration;

    public PropertyResolver(String name) {
        try {
            configuration = new PropertiesConfiguration(name);
            configuration.setReloadingStrategy(new FileChangedReloadingStrategy());
            configuration.setThrowExceptionOnMissing(true);
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

}
