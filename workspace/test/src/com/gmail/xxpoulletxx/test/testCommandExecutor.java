package com.gmail.xxpoulletxx.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class testCommandExecutor implements CommandExecutor {
	public static Map<String, Integer> value = new HashMap<>();
	public static Map<String, Boolean> enable = new HashMap<>();
	public static Map<String, String> name = new HashMap<>();
	private test plugin;

	public testCommandExecutor(test plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		// ---------------------------------------------------------------------------------------------------------------------------------------\\

		if (cmd.getName().equalsIgnoreCase("test")) {
			if (args.length == 0) {
				return false;
			} else if (Util.isError(args[0], "boolean")) {
				sender.sendMessage("boolean error");
				return true;
			}
			return true;
		}

		// ---------------------------------------------------------------------------------------------------------------------------------------\\

		if (cmd.getName().equalsIgnoreCase("blockfind")) {
			// Test si tous est en ordre
			if (!Util.isPlayerSender(sender)) {
				sender.sendMessage(ChatColor.RED
						+ "This command can only be run by a player!");
				return true;
			}
			if (args.length != 2) {
				return false;
			}
			if (Util.isError(args[0], "material")) {
				sender.sendMessage(ChatColor.RED + args[0]
						+ " is not a valid Material!");
				return false;
			}
			if (Util.isError(args[1], "integer")) {
				sender.sendMessage(ChatColor.RED + args[1]
						+ " is not a valid number!");
				return false;
			}
			// Création des variables
			Material mat = Material.getMaterial(args[0].toUpperCase());
			int radius = Integer.parseInt(args[1]);
			Player p = (Player) sender;
			Location loc = p.getLocation();
			int compt = 0;
			List<Location> ListLoc = new ArrayList<Location>();
			String SLoc = "";
			// Condition supérieur
			if (radius < 1 || radius > 15) {
				p.sendMessage(ChatColor.RED
						+ "The radius can't be more than 15 and less than 1!");
				return true;
			}
			if (mat.equals(Material.DIRT) || mat.equals(Material.GRASS)
					|| mat.equals(Material.STONE)
					|| mat.equals(Material.COBBLESTONE)
					|| mat.equals(Material.AIR)) {
				p.sendMessage(ChatColor.RED + "You can't use this function on "
						+ mat.toString());
				return true;
			}
			// Programme
			for (Block e : Util.getNearlyBlock(loc, radius)) {
				if (e.getType().equals(mat)) {
					compt++;
					ListLoc.add(e.getLocation());
				}
			}
			for (int i = 0; i < ListLoc.size(); i++) {
				Location locBis = ListLoc.get(i);
				SLoc += "\n" + (i + 1) + ". (" + locBis.getX() + ", "
						+ locBis.getY() + ", " + locBis.getZ() + ")";
			}
			if (compt == 0)
				p.sendMessage(ChatColor.GOLD + "Il n'y a aucun bloc de "
						+ mat.toString() + " a une distance de " + radius);
			else
				p.sendMessage(ChatColor.GREEN + "Il y a " + compt
						+ " blocs de " + mat.toString() + " à une distance de "
						+ radius + "!\n" + ChatColor.WHITE
						+ "List des locations: " + SLoc);
			return true;
		}

		// ---------------------------------------------------------------------------------------------------------------------------------------\\

		if (cmd.getName().equalsIgnoreCase("tr")) {
			if (!Util.isPlayerSender(sender)) {
				sender.sendMessage(ChatColor.RED
						+ "This command can only be run by a player.");
				return true;
			}
			if (args.length != 1) {
				return false;
			}

			Player p = (Player) sender;
			ItemStack is = p.getItemInHand();
			Player pl = Bukkit.getPlayer(args[0]);
			if (pl == null) {
				p.sendMessage(ChatColor.RED + "The player " + args[0]
						+ " is not online!");
				return true;
			}
			if (pl == p) {
				p.sendMessage(ChatColor.RED
						+ "You can't send items to yourself!");
				return true;
			}
			// Begining of the simple function

			int radius = 10;
			Location loc = p.getLocation();
			if (pl.getLocation().distance(loc) <= radius) {
				p.getInventory().remove(is);
				p.sendMessage(ChatColor.RED + "Send: " + ChatColor.WHITE
						+ is.getAmount() + "x " + is.getType() + " // "
						+ ChatColor.AQUA + "Recipient: " + ChatColor.WHITE
						+ pl.getName() + "!");
				pl.getInventory().addItem(is);
				pl.sendMessage(ChatColor.GREEN + "Receive: " + ChatColor.WHITE
						+ is.getAmount() + "x " + is.getType() + " // "
						+ ChatColor.AQUA + "Sender: " + ChatColor.WHITE
						+ pl.getName() + "!");
			} else {
				p.sendMessage(ChatColor.RED + "The player " + pl.getName()
						+ " is to far! (+10 blocks)");
				return true;
			}
			// End of the simple function
			// **************************\\
			// Begining of he hardest function of the dead!
			// end of this shitty function
			return true;
		}

		// ---------------------------------------------------------------------------------------------------------------------------------------\\

		if (cmd.getName().equalsIgnoreCase("block")) {
			if (!Util.isPlayerSender(sender)) {
				sender.sendMessage(ChatColor.RED
						+ "This command can only be run by a player.");
				return true;
			}
			if(args.length == 0){
				return false;
			}
			Player p = Bukkit.getServer().getPlayer(sender.getName());
			Block b = p.getTargetBlock(null, 100);
			if ("break".equals(args[0].toString()) && args.length == 1) {
				b.breakNaturally();
			} else if ("break".equals(args[0].toString()) && args.length == 2) {
				if (Util.isError(args[1], "integer")) {
					sender.sendMessage(ChatColor.RED + args[1]
							+ " is not a number!");
					return true;
				}
				int radius = Integer.parseInt(args[1]);
				Location loc = b.getLocation();
				int compt = 0;
				List<Block> blocs = Util.getNearlyBlock(loc, radius);
				for (Block e : blocs) {
					if (!e.getType().equals(Material.AIR)) {
						e.breakNaturally();
						compt++;
					}
				}
				if (compt == 0)
					p.sendMessage(ChatColor.GOLD
							+ "Aucun bloc n'a été détruit!");
				else
					p.sendMessage(ChatColor.GREEN
							+ "Vous avez detruit avec succées " + compt
							+ " blocs dans un rayon de " + (int) radius
							+ " autour du bloc que vous visiez!");
			} else if ("change".equals(args[0].toString()) && args.length == 2) {
				if (Util.isError(args[1], "material")) {
					sender.sendMessage(ChatColor.RED + args[1]
							+ " isn't a valid material!");
					return true;
				}
				Material mat = Material.getMaterial(args[1].toUpperCase());
				b.setType(mat);
			} else if ("add".equals(args[0].toString()) && args.length == 2) {
				if (Util.isError(args[1], "material")) {
					sender.sendMessage(ChatColor.RED + args[1]
							+ " is not a valid material!");
					return true;
				}
				Location locB = b.getLocation();
				Location locP = p.getLocation();
				Location locBis;
				if (!b.getType().equals(Material.AIR)
						&& locP.distance(locB) <= 10) {
					List<Block> blocks = p.getLastTwoTargetBlocks(null, 10);
					BlockFace face = blocks.get(1).getFace(blocks.get(0));
					int x = face.getModX();
					int y = face.getModY();
					int z = face.getModZ();
					locBis = locB.add(x, y, z);
					locBis.getBlock().setType(
							Material.getMaterial(args[1].toUpperCase()));
				} else {
					p.sendMessage(ChatColor.RED
							+ "You have to focus on a non-air block in a radius of 10!");
				}
			} else if ("get".equals(args[0].toString()) && args.length == 2) {
				if (Util.isError(args[1], "integer")) {
					sender.sendMessage(ChatColor.RED + args[1]
							+ " is not a number!");
					return true;
				}
				Material mat = b.getType();
				PlayerInventory inv = p.getInventory();
				ItemStack im = new ItemStack(mat, Integer.valueOf(args[1]));
				inv.addItem(im);
			} else {
				return false;
			}
			return true;
		}

		// ---------------------------------------------------------------------------------------------------------------------------------------\\

		if (cmd.getName().equalsIgnoreCase("entity")) {
			if (!Util.isPlayerSender(sender)) {
				sender.sendMessage(ChatColor.RED
						+ "This command can only be run by a player.");
				return true;
			}
			Player p = Bukkit.getServer().getPlayer(sender.getName());
			Location loc = p.getLocation();
			int compt = 0;
			if (args.length == 0) {
				return false;
			} else {

				if ("kill".equals(args[0].toString()) && args.length == 2) {
					if (Util.isError(args[1], "double")) {
						sender.sendMessage(ChatColor.RED + args[1]
								+ " is not a number!");
						return true;
					}
					double radius = Double.valueOf(args[1]);
					List<Entity> entity = Util.getNearlyEntity(loc, radius);
					for (Entity e : entity) {
						if (!(e instanceof Player)) {
							compt++;
							e.remove();
						}
					}
					if (compt == 0) {
						sender.sendMessage(ChatColor.GOLD
								+ "Il n'y a aucune entité à une distance de "
								+ args[1]);
					} else {
						sender.sendMessage(ChatColor.GREEN
								+ String.valueOf(compt)
								+ " entité(s) ont étée(s) supprimer dans un rayon de "
								+ args[1]);
					}
				} else if ("add".equals(args[0].toString()) && args.length == 3) {
					if (Util.isError(args[2], "double")
							|| Util.isError(args[1], "entitytype")) {
						sender.sendMessage(ChatColor.RED + args[1]
								+ " is not a valid entity or " + args[2]
								+ " is not a number!");
						return true;
					}
					Block b = p.getTargetBlock(null, 100);
					Location spawn = b.getLocation().add(0, 1, 0);
					World w = b.getWorld();
					EntityType ent = EntityType.valueOf(args[1].toUpperCase());
					for (int i = 0; i < Double.valueOf(args[2]); i++) {
						w.spawnEntity(spawn, ent);
					}
					sender.sendMessage(ChatColor.GREEN + args[2] + " "
							+ args[1] + " ont été crée(s)!");
				} else if (args.length == 1) {
					if (Util.isError(args[0], "double")) {
						sender.sendMessage(ChatColor.RED + args[0]
								+ " is not a number!");
						return true;
					}
					double radius = Double.valueOf(args[0]);
					List<Entity> entity = Util.getNearlyEntity(loc, radius);
					for (Entity e : entity) {
						if (!(e instanceof Player)) {
							compt++;
						}
					}
					sender.sendMessage(ChatColor.GREEN + "Il y'a "
							+ String.valueOf(compt)
							+ " entités a une distance de " + args[0]);
				}
			}
			return true;
		}

		// ---------------------------------------------------------------------------------------------------------------------------------------\\

		if (cmd.getName().equalsIgnoreCase("setonmoveblock")) {
			if (!Util.isPlayerSender(sender)) {
				sender.sendMessage(ChatColor.RED
						+ "This command can only be run by a player.");
				return true;
			}
			if (args.length < 1) {
				return false;
			}
			if ("on".equals(args[0].toString())) {
				if (args.length != 3) {
					return false;
				}
				if (Util.isError(args[1], "material")) {
					sender.sendMessage(ChatColor.RED + args[1]
							+ " is not a valid material!");
					return true;
				}
				if (Util.isError(args[2], "integer")) {
					sender.sendMessage(ChatColor.RED + args[2]
							+ " is not a number!");
					return true;
				}
				value.put(sender.getName(),
						Integer.parseInt(args[2].toString()));
				name.put(sender.getName(), args[1].toString().toUpperCase());
				enable.put(sender.getName(), true);
				sender.sendMessage(ChatColor.GREEN + "Bloc activée!");
			} else if ("off".equals(args[0].toString())) {
				if (args.length > 1) {
					return false;
				} else {
					enable.put(sender.getName(), false);
					sender.sendMessage(ChatColor.GOLD + "Bloc désactivée!");
				}
			}
			return true;
		}

		// ---------------------------------------------------------------------------------------------------------------------------------------\\

		if (cmd.getName().equalsIgnoreCase("message")) {
			if (!Util.isPlayerSender(sender)) {
				sender.sendMessage(ChatColor.RED
						+ "This command can only be run by a player.");
				return true;
			}
			if (args.length < 2) {
				return false;
			} else {
				Player p = Bukkit.getServer().getPlayer(args[0].toString());
				Player pOnline = (Bukkit.getServer().getPlayer(args[0]));
				if (pOnline == null) {
					sender.sendMessage(ChatColor.RED + "Le joueur " + args[0]
							+ " n'est pas en ligne");
				} else {
					String finalString = "";
					for (int i = 1; i < args.length; i++) {
						finalString = finalString + args[i] + " ";
					}
					p.sendMessage(ChatColor.GRAY + sender.getName()
							+ " -> Vous: " + ChatColor.WHITE + finalString);
					sender.sendMessage(ChatColor.GRAY + "Vous -> "
							+ args[0].toString() + ": " + ChatColor.WHITE
							+ finalString);
				}
				return true;
			}
		}

		// ---------------------------------------------------------------------------------------------------------------------------------------\\

		if (cmd.getName().equalsIgnoreCase("enchantitem") && args.length != 0) {
			if (!Util.isPlayerSender(sender)) {
				sender.sendMessage(ChatColor.RED
						+ "This command can only be run by a player.");
				return true;
			}
			Player p = (Player) sender;
			PlayerInventory inventory = p.getInventory();
			ItemStack item = inventory.getItemInHand();
			ItemMeta im = item.getItemMeta();
			short dura = item.getDurability();
			if ("list".equalsIgnoreCase(args[0]) && args.length == 1) {
				List<Enchantment> ench = Util.getAllEnchantment();
				String str = "";
				for (Enchantment e : ench) {
					// str+=e.getId()+". "+e.getName()+"\n";
					str += e.getId() + " = " + e.getName() + "\n";
				}
				p.sendMessage(str);
				return true;
			}
			if ("remove".equalsIgnoreCase(args[0].toString())
					&& args.length == 2) {
				if ("enchantment".equalsIgnoreCase(args[1].toString())) {
					if (im != null) {
						inventory.remove(item);
						for (Entry<Enchantment, Integer> e : item
								.getEnchantments().entrySet()) {
							item.removeEnchantment(e.getKey());
						}
						item.setDurability(dura);
						inventory.setItemInHand(item);
						sender.sendMessage(ChatColor.GOLD
								+ "Enchantments removed!!");
					} else {
						sender.sendMessage(ChatColor.RED
								+ "Vous devez tenir un objet dans votre main!");
					}
				} else if ("lore".equalsIgnoreCase(args[1].toString())) {
					if (im != null) {
						inventory.remove(item);
						List<String> lores = new ArrayList<String>();
						im.setLore(lores);
						item.setDurability(dura);
						item.setItemMeta(im);
						inventory.setItemInHand(item);
						sender.sendMessage(ChatColor.GOLD + "Lore removed!!");
					} else {
						sender.sendMessage(ChatColor.RED
								+ "Vous devez tenir un objet dans votre main!");
					}
				} else if ("all".equalsIgnoreCase(args[1].toString())) {
					if (im != null) {
						inventory.remove(item);
						List<String> lores = new ArrayList<String>();
						im.setLore(lores);
						item.setItemMeta(im);
						for (Entry<Enchantment, Integer> e : item
								.getEnchantments().entrySet()) {
							item.removeEnchantment(e.getKey());
						}
						item.setDurability(dura);
						inventory.setItemInHand(item);
						sender.sendMessage(ChatColor.GOLD
								+ "Everything removed!!");
					} else {
						sender.sendMessage(ChatColor.RED
								+ "Vous devez tenir un objet dans votre main!");
					}
				} else {
					sender.sendMessage(ChatColor.RED
							+ "/enchantitem remove [enchantment/lore/all]");
				}
				return true;
			} else if (args.length > 2) {
				Material mat = Material.getMaterial(args[0].toString()
						.toUpperCase());
				if (Util.isError(args[1], "integer")
						|| Util.isError(args[2], "integer")) {
					sender.sendMessage(ChatColor.RED + args[1] + " or "
							+ args[2] + " is not a number!");
					return true;
				}
				int id = Integer.parseInt(args[1]);
				int level = Integer.parseInt(args[2]);
				if (mat != null) {
					ItemStack itemMat = new ItemStack(mat);
					Enchantment myEnchantment = new EnchantmentWrapper(id);
					if (args.length >= 4) {
						im.setDisplayName(args[3].toString());
					}
					if (args.length > 4) {
						List<String> lores = new ArrayList<String>();
						String lore = "";
						for (int i = 4; i < args.length; i++) {
							lore = lore + args[i] + " ";
							if ((i - 3) % 5 == 0) {
								lores.add(lore);
								lore = "";
							}
						}
						lores.add(lore);
						im.setLore(lores);
					}
					itemMat.setItemMeta(im);
					itemMat.addUnsafeEnchantment(myEnchantment, level);
					inventory.addItem(itemMat);
					return true;
				} else {
					if ("add".equalsIgnoreCase(args[0].toString())) {
						Enchantment myEnchantment = new EnchantmentWrapper(id);
						if (im != null) {
							if (args.length >= 4) {
								im.setDisplayName(args[3].toString());
							}
							if (args.length > 4) {
								List<String> lores = new ArrayList<String>();
								String lore = "";
								for (int i = 4; i < args.length; i++) {
									lore = lore + args[i] + " ";
									if ((i - 3) % 5 == 0) {
										lores.add(lore);
										lore = "";
									}
								}
								lores.add(lore);
								im.setLore(lores);
							}
							item.setItemMeta(im);
							item.addUnsafeEnchantment(myEnchantment, level);
							inventory.setItemInHand(item);
							return true;
						} else {
							sender.sendMessage(ChatColor.RED
									+ "Vous devez tenir un objet dans votre main!");
						}
					} else {
						sender.sendMessage(ChatColor.RED + "Le materiel <"
								+ args[0].toString() + "> n'est pas valable!");
					}
				}
			} else {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}
}
