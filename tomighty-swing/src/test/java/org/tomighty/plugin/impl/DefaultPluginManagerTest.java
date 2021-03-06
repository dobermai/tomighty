/*
 * Copyright (c) 2010-2012 Célio Cidral Junior, Dominik Obermaier.
 *
 *       Licensed under the Apache License, Version 2.0 (the "License");
 *       you may not use this file except in compliance with the License.
 *       You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 *       Unless required by applicable law or agreed to in writing, software
 *       distributed under the License is distributed on an "AS IS" BASIS,
 *       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *       See the License for the specific language governing permissions and
 *       limitations under the License.
 */

package org.tomighty.plugin.impl;

import org.junit.Test;
import org.tomighty.io.Directory;
import org.tomighty.io.FileSystemDirectory;
import org.tomighty.plugin.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author dobermai
 */
public class DefaultPluginManagerTest {

    private class PluginStub implements Plugin {

        @Override
        public String getPluginName() {
            return "stub";
        }

        @Override
        public PluginVersion getPluginVersion() {
            return new PluginVersion(1, 0, 0);
        }

        @Override
        public MenuItem getMenuItem() {
            return null;
        }
    }

    @Test
    public void TestGetLoadedPluginsWhenPluginsAreLoaded() {

        //Initialize Stubs so we can test the relevant methods without getting NPEs ;)
        PluginPackFactory pluginPackFactoryMock = mock(PluginPackFactory.class);

        Directory directoryMock = mock(Directory.class);
        when(directoryMock.subdirs()).thenReturn(new ArrayList<Directory>() {{
            add(new FileSystemDirectory(null));
        }});

        PluginLoader loaderMock = mock(PluginLoader.class);
        when(loaderMock.load(any(PluginPack.class))).thenReturn(new PluginStub());

        DefaultPluginManager defaultPluginManager = new DefaultPluginManager(loaderMock, pluginPackFactoryMock);
        defaultPluginManager.loadPluginsFrom(directoryMock);


        Set<Plugin> loadedPlugins = defaultPluginManager.getLoadedPlugins();

        assertEquals(1, loadedPlugins.size());
    }
}
