package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;
import cn.nukkit.network.connection.util.HandleByteBuf;
import cn.nukkit.network.protocol.types.inventory.FullContainerName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import cn.nukkit.network.connection.util.HandleByteBuf;
import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InventorySlotPacket extends DataPacket {
    public static final int NETWORK_ID = ProtocolInfo.INVENTORY_SLOT_PACKET;

    public int inventoryId;
    public int slot;
    public Item item;
    public FullContainerName fullContainerName;
    public int dynamicContainerSize;


    @Override
    public int pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode(HandleByteBuf byteBuf) {
        this.inventoryId = byteBuf.readUnsignedVarInt();
        this.slot = byteBuf.readUnsignedVarInt();
        this.fullContainerName = byteBuf.readFullContainerName();
        this.dynamicContainerSize = byteBuf.readUnsignedVarInt();
        this.item = byteBuf.readSlot();
    }

    @Override
    public void encode(HandleByteBuf byteBuf) {
        byteBuf.writeUnsignedVarInt(this.inventoryId);
        byteBuf.writeUnsignedVarInt(this.slot);
        byteBuf.writeFullContainerName(this.fullContainerName);
        byteBuf.writeUnsignedVarInt(this.dynamicContainerSize);
        byteBuf.writeSlot(this.item);
    }

    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}
