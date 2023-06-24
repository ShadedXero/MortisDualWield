package com.mortisdevelopment.mortisdualwield.data;

import com.mortisdevelopment.mortiscorepaper.databases.H2Database;
import com.mortisdevelopment.mortisdualwield.MortisDualWield;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DataManager {

    private final MortisDualWield plugin = MortisDualWield.getInstance();
    private final H2Database database;
    private final HashMap<UUID, String> itemByUUID;
    private final Set<UUID> dualWielding;

    public DataManager() {
        this.database = new H2Database(getFile(), "username", "password");
        this.itemByUUID = new HashMap<>();
        this.dualWielding = new HashSet<>();
        initialize();
        loadDatabase();
        loadDatabase2();
    }

    private File getFile() {
        return new File(plugin.getDataFolder(), "database");
    }

    private void initialize() {
        database.execute("CREATE TABLE IF NOT EXISTS MortisDualWield(uuid varchar(36) primary key, item mediumtext)");
        database.execute("CREATE TABLE IF NOT EXISTS MortisDualWieldEquiped(uuid varchar(36))");
    }

    private void loadDatabase() {
        try {
            ResultSet result = database.query("SELECT * FROM MortisDualWield");
            while (result.next()) {
                UUID uuid = UUID.fromString(result.getString("uuid"));
                String item = result.getString("item");
                itemByUUID.put(uuid, item);
            }
        }catch (SQLException exp) {
            exp.printStackTrace();
        }
    }

    private void loadDatabase2() {
        try {
            ResultSet result = database.query("SELECT * FROM MortisDualWieldEquiped");
            while (result.next()) {
                UUID uuid = UUID.fromString(result.getString("uuid"));
                dualWielding.add(uuid);
            }
        }catch (SQLException exp) {
            exp.printStackTrace();
        }
    }

    public void addItem(@NotNull UUID uuid, @NotNull String item) {
        database.update("INSERT INTO MortisDualWield(uuid, item) VALUES (?, ?)", uuid, item);
        itemByUUID.put(uuid, item);
    }

    public void removeItem(@NotNull UUID uuid) {
        database.update("DELETE FROM MortisDualWield WHERE uuid = ?", uuid);
        itemByUUID.remove(uuid);
    }

    public String getItem(@NotNull UUID uuid) {
        return itemByUUID.get(uuid);
    }

    public void addEquiped(@NotNull UUID uuid) {
        database.update("INSERT INTO MortisDualWieldEquiped(uuid) VALUES (?)", uuid);
        dualWielding.add(uuid);
    }

    public boolean hasEquiped(@NotNull UUID uuid) {
        return dualWielding.contains(uuid);
    }

    public void removeEquiped(@NotNull UUID uuid) {
        database.update("DELETE FROM MortisDualWieldEquiped WHERE uuid = ?", uuid);
        dualWielding.remove(uuid);
    }

    public H2Database getDatabase() {
        return database;
    }

    public HashMap<UUID, String> getItemByUUID() {
        return itemByUUID;
    }

    public Set<UUID> getDualWielding() {
        return dualWielding;
    }
}
