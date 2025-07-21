package org.example.gamelogic.lastwargamelogic.flag;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DropFlagCommand implements CommandExecutor {

    private final FlagSpawner flagSpawner;

    public DropFlagCommand(FlagSpawner flagSpawner) {
        this.flagSpawner = flagSpawner;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("§cUsage: /dropFlag <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            sender.sendMessage("§cPlayer not found or offline.");
            return true;
        }

        ItemStack item = target.getInventory().getItem(4); // usually flag is in slot 4
        if (item != null && isFlagBow(item)) {
            target.getInventory().setItem(4, null);
            Location dropLocation = target.getLocation().clone().add(0, 0.1, 0);
            flagSpawner.spawnAndReturn(dropLocation);
            sender.sendMessage("§aFlag dropped from " + target.getName());
        } else {
            sender.sendMessage("§e" + target.getName() + " does not have the flag in slot 4.");
        }

        return true;
    }

    private boolean isFlagBow(ItemStack item) {
        if (item == null || item.getType() != Material.BOW) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasDisplayName() &&
                Component.text("Flag").equals(meta.displayName());
    }
}
