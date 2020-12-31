/*
 *    Copyright (C) 2020 Camshaft54, MetalTurtle18
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.camshaft54.idlebot.events;

import static io.github.camshaft54.idlebot.events.InventoryFull.inventoryFull;
import static io.github.camshaft54.idlebot.events.LocationReached.locationReached;
import static io.github.camshaft54.idlebot.events.XPLevelReached.xpLevelReached;

public class EventManager implements Runnable {
    // TODO: add hashmaps of players to this class so it will have a single instance
    @Override
    public void run() {
        // Run "events" that are not actually events every one second
        inventoryFull();
        locationReached();
        xpLevelReached();
    }
}
