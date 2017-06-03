/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.karuslabs.commons.configuration;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class ConfigurationsTest {
    
    @Test
    public void load() {
        YamlConfiguration config = Configurations.from(getClass().getClassLoader().getResourceAsStream("util/config.yml"));
        assertNotNull(config);
    }
    
    
    @Test
    public void getOrDefault() {
        ConfigurationSection config = mock(ConfigurationSection.class);
        assertTrue(config == Configurations.getOrDefault(config, Configurations.BLANK));
    }
    
    
    @Test
    public void getOrDefault_ReturnsDefault() {
        ConfigurationSection config = mock(ConfigurationSection.class);
        assertTrue(config == Configurations.getOrDefault(null, config));
    }
    
    
    @Test
    public void getOrBlank() {
        ConfigurationSection config = mock(ConfigurationSection.class);
        assertTrue(config == Configurations.getOrBlank(config));
    }
    
    
    @Test
    public void getOrBlank_ReturnsBlank() {
        assertTrue(Configurations.BLANK == Configurations.getOrBlank(null));
    }
    
}
