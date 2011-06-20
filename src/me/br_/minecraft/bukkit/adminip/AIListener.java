package me.br_.minecraft.bukkit.adminip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.config.Configuration;

public class AIListener extends PlayerListener {
	public AdminIP sender;
	public static AIMain plugin;
	public Configuration config;
	public List<String> opped = new ArrayList<String>();
	public List<String> def = List("127.0.0.1");

	public AIListener(AIMain instance) {
		plugin = instance;
		config = plugin.getConfiguration();
		config.setProperty("adminIPs", config.getStringList("adminIPs", def));
		config.save();
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (plugin.enabled) {
			String ip = event.getPlayer().getAddress().getAddress()
					.getHostAddress();
			if (event.getPlayer().isOp()) {
				System.out.println("[AdminIP] Not opping <"
						+ event.getPlayer().getDisplayName() + "> from IP <"
						+ ip + "> because he is already an op.");
			} else {
				config.load();
				config.setProperty("adminIPs",
						config.getStringList("adminIPs", def));
				config.save();
				for (String testip : config.getStringList("adminIPs", def)) {
					if (testip.equals(ip)) {
						opped.add(event.getPlayer().getName());
						System.out.println("[AdminIP] Opping <"
								+ event.getPlayer().getDisplayName()
								+ "> from IP <" + ip + ">.");
						event.getPlayer().sendMessage(
								"[AdminIP] You have been opped.");
						plugin.getServer().dispatchCommand(sender,
								"op " + event.getPlayer().getName());
						return;
					}
				}
				System.out.println("[AdminIP] Not opping <"
						+ event.getPlayer().getDisplayName() + "> from IP <"
						+ ip + "> because his IP was not recognized.");
			}
		}
	}

	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (plugin.enabled) {
			if (opped.contains(event.getPlayer().getName())) {
				plugin.getServer().dispatchCommand(sender,
						"deop " + event.getPlayer().getName());
				System.out.println("[AdminIP] Deopping <"
						+ event.getPlayer().getDisplayName()
						+ "> from IP <"
						+ event.getPlayer().getAddress().getAddress()
								.getHostAddress() + ">.");
				opped.remove(event.getPlayer().getName());
			}
		}
	}

	public static <T> List<T> List(T... elems) {
		return Arrays.asList(elems);
	}
}
