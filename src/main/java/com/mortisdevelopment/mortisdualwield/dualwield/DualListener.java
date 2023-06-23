package com.mortisdevelopment.mortisdualwield.dualwield;

import me.deecaad.weaponmechanics.WeaponMechanics;
import me.deecaad.weaponmechanics.utils.CustomTag;
import me.deecaad.weaponmechanics.weapon.weaponevents.WeaponEquipEvent;
import me.deecaad.weaponmechanics.wrappers.EntityWrapper;
import me.deecaad.weaponmechanics.wrappers.HandData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class DualListener implements Listener {

    private final DualManager dualManager;

    public DualListener(DualManager dualManager) {
        this.dualManager = dualManager;
    }

    @EventHandler
    public void onEquip(WeaponEquipEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) e.getEntity();
        if (e.getHand() == null || !e.getHand().equals(EquipmentSlot.HAND)) {
            return;
        }
        String weaponId = e.getWeaponTitle();
        if (!dualManager.getWeaponIds().contains(weaponId)) {
            return;
        }
        ItemStack weapon = e.getWeaponStack();
        ItemStack generated = WeaponMechanics.getWeaponHandler().getInfoHandler().generateWeapon(weaponId, 1);
        DualWield dualWield = dualManager.getDualWieldByUUID().get(player.getUniqueId());
        if (dualWield == null) {
            System.out.println(CustomTag.AMMO_MAGAZINE.getInteger(generated));
            dualManager.getDualWieldByUUID().put(player.getUniqueId(), new DualWield(player.getInventory().getItemInOffHand(), generated, CustomTag.AMMO_MAGAZINE.getInteger(generated)));
        }else {
            System.out.println(dualWield);
        }
        EntityWrapper wrapper = WeaponMechanics.getEntityWrapper(player);
        System.out.println(wrapper);
        HandData handData = wrapper.getMainHandData();
        System.out.println(handData);
    }
}
