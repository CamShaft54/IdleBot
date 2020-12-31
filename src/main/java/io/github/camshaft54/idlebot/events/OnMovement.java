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

/*
 *    Copyright 2020 Benjamin Martin
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.camshaft54.idlebot.events;

import io.github.camshaft54.idlebot.IdleBot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


public class OnMovement implements Listener {

    // The following method is a derivative work of AFKPlus by Dart2112 (https://github.com/LapisPlugins/AFKPlus) under the Apache License
    // If player has moved and looked in a different direction, reset playersIdling and the other condition hashmaps
    @EventHandler
    public static void onMovement(PlayerMoveEvent e) {
        Location from = e.getFrom();
        Location to = e.getTo();
        if (to == null)
            return;
        if (to.getPitch() != from.getPitch() || to.getYaw() != from.getYaw()) {
            Player player = e.getPlayer();
            // Send a message in the console
            Bukkit.getLogger().info(ChatColor.DARK_PURPLE + "[IdleBot] " + ChatColor.AQUA + player.getDisplayName() + " stopped being idle.");
            // Player is no longer idle, so they are removed from the HashMap of idle players
            IdleBot.idlePlayers.remove(player);
        }
    }
}

