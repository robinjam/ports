package net.robinjam.bukkit.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

public class EconomyUtils {
	
	/**
	 * Checks if Vault is installed and returns a reference to the economy API.
	 * 
	 * @return A reference to the economy API, or null if Vault and a compatible economy plugin are not installed.
	 */
	public static Economy getEconomy() {
		if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null)
			return null;
		
		RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
		if (economyProvider == null)
			return null;
		
		return economyProvider.getProvider();
	}
	
}
