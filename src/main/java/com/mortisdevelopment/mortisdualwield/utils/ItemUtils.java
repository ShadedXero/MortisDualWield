package com.mortisdevelopment.mortisdualwield.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ItemUtils {

    public static String serialize(ItemStack item) {
        if (item == null) {
            return null;
        }
        try {
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);
            os.writeObject(item);
            os.flush();
            return new String(Base64.getEncoder().encode(io.toByteArray()));
        }catch (IOException exp) {
            return null;
        }
    }

    public static ItemStack deserialize(String rawItem) {
        if (rawItem == null) {
            return null;
        }
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(rawItem));
            BukkitObjectInputStream is = new BukkitObjectInputStream(in);
            return (ItemStack) is.readObject();
        }catch (IOException | ClassNotFoundException exp) {
            return null;
        }
    }
}
