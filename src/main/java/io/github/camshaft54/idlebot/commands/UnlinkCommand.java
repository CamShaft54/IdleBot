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

package io.github.camshaft54.idlebot.commands;

import io.github.camshaft54.idlebot.util.PersistentDataHandler;
import io.github.camshaft54.idlebot.util.IdleBotCommand;
import org.bukkit.entity.Player;

public class UnlinkCommand extends IdleBotCommand {
    @Override
    public String getCommandName() {
        return "unlink";
    }

    @Override
    public void runCommand(Player player, String[] args) {
        if (!playerIsLinked(player)) {
            // Send a blurb about how they aren't linked so they can't unlink
            return;
        }
        PersistentDataHandler.removeData(player, "discordID");
    }

    private boolean playerIsLinked(Player player) {
        return PersistentDataHandler.getStringData(player, "discordID") != null; // Returns true if the player already has an account linked
    }
}