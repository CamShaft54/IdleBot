package io.github.camshaft54.idlebot;

import io.github.camshaft54.idlebot.events.IdleBotEvents;
import io.github.camshaft54.idlebot.events.IdleChecker;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.ServerTextChannel;

public class IdleBot extends JavaPlugin {
    public static ServerTextChannel channel;
    FileConfiguration config = this.getConfig();
    private static String botToken;
    private static String channelId;

    @Override
    public void onEnable() {
        configSetup();
        // Connect to Discord
        DiscordApi api = new DiscordApiBuilder()
                .setToken(botToken) // Set the token of the bot here
                .login() // Log the bot in
                .join(); // Call #onConnectToDiscord(...) after a successful login
        getLogger().info("Connected to Discord as " + api.getYourself().getDiscriminatedName());
        getLogger().info("Open the following url to invite the bot: " + api.createBotInvite());

        if (api.getServerTextChannelById(channelId).isPresent()) {
            channel = api.getServerTextChannelById(channelId).get();
        }
        else {
            getLogger().info("Channel not present");
        }
        getServer().getScheduler().runTaskTimer(this, new IdleChecker(), 20L, 20L); // Code in task should execute every 20 ticks (1 second)
        getServer().getPluginManager().registerEvents(new IdleBotEvents(), this);
    }

    private void configSetup() {
        config.addDefault("botToken", "<Bot Token Here>");
        config.addDefault("channelId", "<Channel Id Here>");
        config.options().copyDefaults(true);
        saveConfig();
        botToken = config.getString("botToken");
        channelId = config.getString("channelId");
    }
}
