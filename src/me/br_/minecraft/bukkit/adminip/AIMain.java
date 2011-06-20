package me.br_.minecraft.bukkit.adminip;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AIMain extends JavaPlugin {
	public boolean enabled = false;
	public AIListener playerListener;

	@Override
	public void onDisable() {
		AdminIP sender = new AdminIP(getServer());
		for (String s : playerListener.opped) {
			getServer().dispatchCommand(sender, "deop " + s);
			System.out.println("[AdminIP] Deopping <" + s + ">.");
		}
		enabled = false;
		playerListener = null;
		System.out.println("[AdminIP] Disabled.");
	}

	@Override
	public void onEnable() {
		playerListener = new AIListener(this);
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener,
				Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener,
				Event.Priority.Normal, this);
		enabled = true;
		System.out.println("[AdminIP] Enabled.");
	}
}
