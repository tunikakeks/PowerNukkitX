package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.entity.IHuman;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.InventoryContentPacket;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.network.protocol.types.inventory.FullContainerName;
import cn.nukkit.network.protocol.types.itemstack.ContainerSlotType;
import com.google.common.collect.BiMap;

import java.util.Map;

public class HumanOffHandInventory extends BaseInventory {
    public HumanOffHandInventory(IHuman holder) {
        super(holder, InventoryType.INVENTORY, 1);
    }

    @Override
    public void init() {
        BiMap<Integer, Integer> map = super.networkSlotMap();
        map.put(0, 1);

        Map<Integer, ContainerSlotType> map2 = super.slotTypeMap();
        map2.put(0, ContainerSlotType.OFFHAND);
    }

    public void setItem(Item item) {
        setItem(0, item);
    }

    public void setItem(Item item, boolean send) {
        setItem(0, item, send);
    }

    @Override
    public boolean setItem(int index, Item item) {
        return super.setItem(0, item);
    }

    @Override
    public boolean setItem(int index, Item item, boolean send) {
        return super.setItem(0, item, send);
    }

    @Override
    public void onSlotChange(int index, Item before, boolean send) {
        IHuman holder = this.getHolder();
        if (holder instanceof Player player) {
            if (!player.spawned) return;
            if (send) {
                this.sendContents(this.getViewers());
                this.sendContents(holder.getEntity().getViewers().values());
            }
        }
    }

    @Override
    public void sendContents(Player... players) {
        Item item = this.getItem(0);
        MobEquipmentPacket pk = this.createMobEquipmentPacket(item);

        for (Player player : players) {
            if (player == this.getHolder()) {
                InventoryContentPacket pk2 = new InventoryContentPacket();
                pk2.inventoryId = SpecialWindowId.OFFHAND.getId();
                pk2.slots = new Item[]{item};
                pk2.fullContainerName = new FullContainerName(
                        ContainerSlotType.OFFHAND,
                        0
                );
                pk2.dynamicContainerSize=player.getOffhandInventory().getSize();
                player.dataPacket(pk2);
                player.dataPacket(pk);
            } else {
                player.dataPacket(pk);
            }
        }
    }

    @Override
    public void sendSlot(int index, Player... players) {
        sendContents(players);
    }

    private MobEquipmentPacket createMobEquipmentPacket(Item item) {
        MobEquipmentPacket pk = new MobEquipmentPacket();
        pk.eid = this.getHolder().getEntity().getId();
        pk.item = item;
        pk.inventorySlot = 1;
        pk.windowId = SpecialWindowId.OFFHAND.getId();
        return pk;
    }

    @Override
    public IHuman getHolder() {
        return (IHuman) super.getHolder();
    }
}
