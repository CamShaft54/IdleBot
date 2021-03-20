/*
 *    Copyright (C) 2020-2021 Camshaft54, MetalTurtle18
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

package io.github.idlebotdevelopment.idlebot;

import github.scarsz.configuralize.ParseException;
import github.scarsz.discordsrv.DiscordSRV;
import io.github.idlebotdevelopment.idlebot.commands.IdleBotCommandManager;
import io.github.idlebotdevelopment.idlebot.commands.IdleBotTabCompleter;
import io.github.idlebotdevelopment.idlebot.discord.DiscordAPIManager;
import io.github.idlebotdevelopment.idlebot.events.*;
import io.github.idlebotdevelopment.idlebot.util.ConfigManager;
import io.github.idlebotdevelopment.idlebot.util.MessageHelper;
import io.github.idlebotdevelopment.idlebot.util.UpdateChecker;
import io.github.idlebotdevelopment.idlebot.util.enums.MessageLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Callable;

public class IdleBot extends JavaPlugin {

    @Getter private static  ConfigManager configManager;
    @Getter private static final EventManager eventManager = new EventManager();
    @Getter private static IdleBot plugin;
    @Getter private static DiscordAPIManager discordAPIManager;
    @Getter private static String localVersion;
    @Getter private static String latestVersion;
    public static final HashMap<Integer, Player> linkCodes = new HashMap<>();
    public static final HashMap<Player, Integer> idlePlayers = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;
        try {
            configManager = new ConfigManager(this);
        }
        catch (IOException | ParseException e) {
            MessageHelper.sendMessage("Plugin configuration load failed! Plugin disabled. Try to fix the configuration file and try again or get support!", MessageLevel.FATAL_ERROR);
            e.printStackTrace();
            disablePlugin();
        }
        if (isEnabled()) {
            BukkitScheduler scheduler = getServer().getScheduler();
            PluginManager pluginManager = getServer().getPluginManager();
            PluginCommand idleBotCommand = Objects.requireNonNull(this.getCommand("idlebot"));
            idleBotCommand.setExecutor(new IdleBotCommandManager());
            idleBotCommand.setTabCompleter(new IdleBotTabCompleter());
            scheduler.runTaskTimer(this, new IdleChecker(), 20L, 20L); // Execute the idle checker every 20 ticks (1 second)
            scheduler.runTaskTimer(this, eventManager, 20L, 20L); // Check for all extra events (events that don't have official Bukkit events) every 20 ticks (1 second)
            pluginManager.registerEvents(new OnMovement(), this); // Register movement event
            pluginManager.registerEvents(new OnDamage(), this); // Register damage event
            pluginManager.registerEvents(new OnDeath(), this); // Register death event
            pluginManager.registerEvents(new OnPlayerQuit(), this); // Register player quit event
            pluginManager.registerEvents(new OnPlayerJoin(), this); // Register player join event
            localVersion = this.getDescription().getVersion();
            new UpdateChecker(this).getVersion(version -> {
                latestVersion = version;
                if (localVersion.equals(latestVersion))
                    MessageHelper.sendMessage("You are running the latest version! (" + localVersion + ")", MessageLevel.INFO);
                else
                    MessageHelper.sendMessage("You are running an outdated version! (You are running version " + localVersion + " but the latest version is " + latestVersion + ")\nGo to https://www.spigotmc.org/resources/idlebot-step-up-your-afk-game.88778/ to download a new version", MessageLevel.IMPORTANT);
            });
            // Load JDA
            if (configManager.DISCORDSRV_MODE) {
                MessageHelper.sendMessage("Connecting to DiscordSRV plugin", MessageLevel.INFO);
                DiscordSRV.api.subscribe(new DiscordSRVEvents(this));
            } else {
                MessageHelper.sendMessage("Starting to load JDA", MessageLevel.INFO);
                discordAPIManager = new DiscordAPIManager(this, false);
                MessageHelper.sendMessage("Plugin successfully loaded", MessageLevel.INFO);
            }
        }
    }

    @Override
    public void onDisable() {
        MessageHelper.sendMessage("All data saved. Plugin safely closed!", MessageLevel.INFO);
    }

    public void disablePlugin() {
        //Bukkit.getPluginManager().disablePlugin(plugin);
        //Bukkit.getScheduler().callSyncMethod(this, new DisablePlugin());
        Bukkit.getScheduler().callSyncMethod(this, new Callable<>() {
            @Override
            public Object call() {
                Bukkit.getPluginManager().disablePlugin(IdleBot.getPlugin());
                return this;
            }
        });
    }

    public void setDiscordAPIManager(DiscordAPIManager api) {
        discordAPIManager = api;
    }
}

