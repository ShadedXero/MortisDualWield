package com.mortisdevelopment.mortisdualwield.manager;

import com.mortisdevelopment.mortiscorepaper.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DualWieldCommand implements TabExecutor {

    private final Manager manager;

    public DualWieldCommand(Manager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("mortisdualwield.reload")) {
                sender.sendMessage(MessageUtils.color("&cYou don't have permission to use this"));
                return false;
            }
            manager.reload();
            sender.sendMessage(MessageUtils.color("&aMortisDualWield Reloaded"));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> arguments = new ArrayList<>();
            arguments.add("reload");
            return arguments;
        }
        return null;
    }
}
