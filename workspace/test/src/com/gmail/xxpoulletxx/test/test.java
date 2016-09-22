package com.gmail.xxpoulletxx.test;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/*import org.json.*;
 import org.json.simple.JSONObject;*/

public class test extends JavaPlugin implements Listener {
	public static Map<String, Integer> TPlimit = new HashMap<String, Integer>();
	public static Map<String, Integer> BrL = new HashMap<String, Integer>();
	public static Map<String, Boolean> Sw = new HashMap<String, Boolean>();
	public int nbPlayerInBed = 0;
	public boolean slept = false;

	@Override
	public void onEnable() {
		// TODO Insert logic to be performed when the plugin is enabled
		getLogger().info("onEnable has been invoked!");
		getServer().getPluginManager().registerEvents(this, this);
		getCommand("block").setExecutor(new testCommandExecutor(this));
		getCommand("entity").setExecutor(new testCommandExecutor(this));
		getCommand("message").setExecutor(new testCommandExecutor(this));
		getCommand("enchantitem").setExecutor(new testCommandExecutor(this));
		getCommand("setonmoveblock").setExecutor(new testCommandExecutor(this));
		getCommand("tr").setExecutor(new testCommandExecutor(this));
		getCommand("test").setExecutor(new testCommandExecutor(this));
		getCommand("blockfind").setExecutor(new testCommandExecutor(this));
	}

	@Override
	public void onDisable() {
		// TODO Insert logic to be performed when the plugin is disabled
		getLogger().info("onDisable has been invoked!");
	}

	@EventHandler
	public void onLogin(PlayerJoinEvent event) {

	}

	@EventHandler
	public void playerEnterBedEvent(PlayerBedEnterEvent event) {
		Player p = event.getPlayer();
		int nbOnlinePlayer = Bukkit.getOnlinePlayers().length;
		nbPlayerInBed++;
		for (Player pl : Bukkit.getOnlinePlayers()) {
			if (nbPlayerInBed != nbOnlinePlayer) {
				pl.sendMessage(ChatColor.GOLD + "The player " + p
						+ " want to sleep! (" + nbPlayerInBed + "/"
						+ nbOnlinePlayer + ")");
			} else {
				pl.sendMessage(ChatColor.GREEN + "Goodnight!");
				slept = true;
			}
		}
	}

	@EventHandler
	public void playerLeaveBedEvent(PlayerBedLeaveEvent event) {
		int nbOnlinePlayer = Bukkit.getOnlinePlayers().length;
		nbPlayerInBed--;
		if (!slept) {
			for (Player pl : Bukkit.getOnlinePlayers()) {
				pl.sendMessage(ChatColor.RED + "The player "
						+ event.getPlayer() + " has left his bed! ("
						+ nbPlayerInBed + "/" + nbOnlinePlayer + ")");
			}
		}
		if (nbPlayerInBed == 0 && slept) {
			slept = false;
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event) {
		String playerName = event.getPlayer().getName();
		if (testCommandExecutor.enable.get(playerName) == null) {
			testCommandExecutor.enable.put(event.getPlayer().getName(), false);
		}
		if (testCommandExecutor.enable.get(playerName)) {
			Material bloc = Material.getMaterial(testCommandExecutor.name
					.get(playerName));
			Location loc = event.getPlayer().getLocation();
			loc.setY(loc.getY() + testCommandExecutor.value.get(playerName));
			World w = loc.getWorld();
			Block b = w.getBlockAt(loc);
			b.setType(bloc);
		}
	}

	@EventHandler
	public void BreakBlock(BlockBreakEvent event) {
		/*
		 * event.getPlayer(); event.setCancelled(true); event.isCancelled();
		 */
	}

	@EventHandler
	public void BuildBlock(BlockPlaceEvent event) {
		/*
		 * event.isCancelled(); event.setCancelled(true); event.getPlayer();
		 * event.getBlockPlaced(); event.getBlockAgainst();
		 * event.getItemInHand(); event.canBuild(); event.setBuild(true);
		 */
	}

	@EventHandler
	public void DamageBlock(BlockDamageEvent event) {
		/*
		 * event.getPlayer(); event.getInstaBreak(); event.setInstaBreak(true);
		 * event.getItemInHand(); event.isCancelled(); event.setCancelled(true);
		 */
		Block b = event.getBlock();
		Player p = event.getPlayer();
		final String pN = p.getName();

		if (b.getType() == Material.DIRT || b.getType() == Material.STONE
				|| b.getType() == Material.COBBLESTONE
				|| b.getType() == Material.GRASS) {
			if (BrL.get(pN) == null)
				BrL.put(pN, 0);
			int Broke = BrL.get(pN);
			if (Broke < 10) {
				Sw.put(pN, true);
				p.sendMessage(ChatColor.GREEN
						+ "Bloc détruit instentanement! (" + (9 - Broke) + ")");
				event.setInstaBreak(true);
				Broke++;
				BrL.put(pN, Broke);
			} else {
				if (Sw.get(pN)) {
					Sw.put(pN, false);
					Bukkit.getScheduler().scheduleSyncDelayedTask(this,
							new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									BrL.put(pN, 0);
								}
							}, 100L);
					p.sendMessage(ChatColor.RED
							+ "Attendez 5 secondes avant de pouvoir détruire instentanément des blocks!");
				}
			}
		}
	}

	@EventHandler
	public void PlayerBreakItem(PlayerItemBreakEvent event) {
		/*
		 * event.getBrokenItem(); event.getPlayer();
		 */
		final Player p = event.getPlayer();
		Inventory inv = p.getInventory();
		ItemStack Im = event.getBrokenItem();
		Material ImM = Im.getType();

		for (final ItemStack i : inv.getContents()) {
			boolean accept = true;
			try {
				i.getType();
			} catch (NullPointerException e) {
				accept = false;
			}
			if (accept && i.getType() == ImM && i.hashCode() != Im.hashCode()) {
				inv.remove(i);
				Bukkit.getScheduler().scheduleSyncDelayedTask(this,
						new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								p.setItemInHand(i);
							}
						}, 1L);
				break;
			}
		}
	}

	@EventHandler
	public void PlayerPickup(PlayerPickupItemEvent event) {
		/*
		 * event.getPlayer(); event.getItem(); event.getRemaining();
		 * event.isCancelled(); event.setCancelled(true);
		 */

	}

	@EventHandler
	public void PlayerRespawn(PlayerRespawnEvent event) {
		/*
		 * event.getPlayer(); event.getRespawnLocation(); event.isBedSpawn();
		 * event.setRespawnLocation(Location);
		 */
		Location loc = event.getRespawnLocation();
		Player pl = event.getPlayer();
		if (event.isBedSpawn()) {
			pl.sendMessage(ChatColor.GREEN
					+ "Vous avez réapparu dans votre lit.");
		} else {
			loc = pl.getWorld().getSpawnLocation();
			event.setRespawnLocation(loc);
			pl.sendMessage(ChatColor.GREEN + "Vous êtes réapparu au spawn ("
					+ loc.getBlockX() + ", " + loc.getBlockY() + ", "
					+ loc.getBlockZ() + ")");
		}
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(ChatColor.GOLD + pl.getName() + " a réapparu en X="
					+ loc.getBlockX() + " // Y=" + loc.getBlockY() + " // Z="
					+ loc.getBlockZ());
		}
	}

	@EventHandler
	public void PlayerTeleport(PlayerTeleportEvent event) {
		/*
		 * event.getCause(); event.getFrom(); event.getPlayer(); event.getTo();
		 * event.isCancelled(); event.setCancelled(true); event.setTo(Location);
		 */
		/*
		 * String str="{\"Data\":{\"Nom\":\"Darcey\",\"Prenom\":\"Samuel\"}}";
		 * JSONObject obj = new JSONObject(str);
		 */
		TeleportCause TPc = event.getCause();
		Player pl = event.getPlayer();
		if (TPc.equals(TeleportCause.COMMAND)) {
			final String Sp = pl.getName();
			if (TPlimit.isEmpty()) {
				TPlimit.put(Sp, 0);
			}
			int Limite = TPlimit.get(Sp);
			if (Limite != 0) {
				pl.sendMessage(ChatColor.RED
						+ "Attendez 5 secondes avant de pouvoir vous téléportez à nouveau!");
				event.setCancelled(true);
			} else {
				TPlimit.put(Sp, 1);
				Bukkit.getScheduler().scheduleSyncDelayedTask(this,
						new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								TPlimit.put(Sp, 0);
							}
						}, 100L);
			}
		}
	}
}
