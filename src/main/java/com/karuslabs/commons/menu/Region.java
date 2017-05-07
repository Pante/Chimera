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
package com.karuslabs.commons.menu;

import java.util.*;

import org.bukkit.event.inventory.*;


public class Region {
    
    protected Set<Region> regions;
    protected Map<Integer, Button> buttons;
    
    
    public Region(Set<Region> regions, Map<Integer, Button> buttons) {
        this.regions = regions;
        this.buttons = buttons;
    }
    
    
    public boolean contains(int slot) {
        return buttons.containsKey(slot);
    }

    public void click(Menu menu, InventoryClickEvent event) {
        Button button = buttons.get(event.getRawSlot());
        if (button != null) {
            button.click(menu, event);
        }
    }

    public void drag(Menu menu, InventoryDragEvent event) {
        event.getRawSlots().forEach(slot -> {
            Button button = buttons.get(slot);
            if (button != null) {
                button.drag(menu, event);
            }
        });
    }
    
    
    public Set<Region> getNestedRegions() {
        return regions;
    }
    
    public Map<Integer, Button> getButtons() {
        return buttons;
    }
    
}