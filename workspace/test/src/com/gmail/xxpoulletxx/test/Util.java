package com.gmail.xxpoulletxx.test;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Function;

public final class Util {
	private Util() {
	} // Prevent the class from being constructed

	public static boolean isPlayerSender(CommandSender cms) {
		if (cms instanceof Player) {
			return true;
		} else {
			return false;
		}
	}

	public static List<Entity> getNearlyEntity(Location loc, double radius) {
		List<Entity> entity = new ArrayList<Entity>();
		List<Entity> near = loc.getWorld().getEntities();
		for (Entity e : near) {
			if (e.getLocation().distance(loc) <= radius) {
				entity.add(e);
			}
		}
		return entity;
	}

	public static List<Enchantment> getAllEnchantment() {
		List<Enchantment> ench = new ArrayList<Enchantment>();
		for (int i = 0; i < 63; i++) {
			if (Enchantment.getById(i) != null) {
				ench.add(Enchantment.getById(i));
			}
		}
		return ench;
	}

	public static List<Block> getNearlyBlock(Location loc, int radius) {
		List<Block> blocs = new ArrayList<Block>();
		Location locMin = loc.clone().subtract(radius, radius, radius);
		Location locMax = loc.clone().add(radius, radius, radius);
		for (double x = locMin.getX(); x <= locMax.getX(); x++) {
			for (double y = locMin.getY(); y <= locMax.getY(); y++) {
				for (double z = locMin.getZ(); z <= locMax.getZ(); z++) {
					Location locBis = new Location(loc.getWorld(), x, y, z);
					blocs.add(locBis.getBlock());
				}
			}
		}
		return blocs;
	}

	public static boolean isError(String value, String type) {
		if (type.toLowerCase().equals("double")) {
			try {
				Double.valueOf(value);
			} catch (NumberFormatException e) {
				return true;
			}
		} else if (type.toLowerCase().equals("integer")) {
			try {
				Integer.parseInt(value);
			} catch (NumberFormatException e) {
				return true;
			}
		} else if (type.toLowerCase().equals("material")) {
			try {
				new ItemStack(Material.getMaterial(value.toUpperCase()));
			} catch (NullPointerException e) {
				return true;
			}
		} else if (type.toLowerCase().equals("entitytype")) {
			try {
				EntityType.valueOf(value.toUpperCase());
			} catch (IllegalArgumentException e) {
				return true;
			}
		} else {
			Exception e = new Exception();
			StackTraceElement ste = e.getStackTrace()[1];
			ConsoleCommandSender cms = Bukkit.getConsoleSender();
			cms.sendMessage(ChatColor.RED + "ERREUR: La demande Util.isError("
					+ value + "," + type + ") n'a pas un type valide!\n"
					+ "(Util.isError(String value,String type) at line "
					+ ste.getLineNumber() + " in class " + ste.getClassName()
					+ " with file name " + ste.getFileName() + ")");
		}
		return false;
	}
}
