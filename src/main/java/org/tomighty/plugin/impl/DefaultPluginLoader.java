package org.tomighty.plugin.impl;

import com.google.inject.Injector;
import org.tomighty.plugin.Plugin;
import org.tomighty.plugin.PluginLoader;
import org.tomighty.plugin.PluginPack;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.util.Properties;

public class DefaultPluginLoader implements PluginLoader {

    @Inject
    private Injector injector;

    @Override
    public Plugin load(PluginPack pluginPack) {
        URLClassLoader classLoader = new URLClassLoader(pluginPack.jars());
        Class<? extends Plugin> pluginClass = loadPluginClass(classLoader);
        return injector.getInstance(pluginClass);
    }

    private Class<? extends Plugin> loadPluginClass(ClassLoader classLoader) {
        String pluginClassName = getPluginClassName(classLoader);
        try {
            return (Class<? extends Plugin>) classLoader.loadClass(pluginClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private String getPluginClassName(ClassLoader classLoader) {
        InputStream inputStream = classLoader.getResourceAsStream("tomighty-plugin.properties");
        Properties properties = loadProperties(inputStream);
        return properties.getProperty("class");
    }

    private Properties loadProperties(InputStream inputStream) {
        if(inputStream == null)
            throw new IllegalArgumentException("Input stream must not be null");

        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

}