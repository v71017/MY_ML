package edu.rtu.stl.util;

public interface WithDefaultProperties {

    PropertyResolver RESOLVER = new PropertyResolver("src/main/resources/analyzer.properties");

    default String getProperty(String name) {
        return RESOLVER.configuration.getString(name);
    }

    default Long getLongProperty(String name, long defaultValue) {
        return RESOLVER.configuration.getLong(name, defaultValue);
    }
    default Integer getIntegerProperty(String name, int defaultValue) {
        return RESOLVER.configuration.getInt(name, defaultValue);
    }


}
