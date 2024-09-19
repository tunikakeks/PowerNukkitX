package cn.nukkit.network.protocol;

import cn.nukkit.camera.data.CameraPreset;
import cn.nukkit.network.connection.util.HandleByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


@ToString
@NoArgsConstructor
public class CameraPresetsPacket extends DataPacket {
    public final List<CameraPreset> presets = new ObjectArrayList<>();

    @Override
    public int pid() {
        return ProtocolInfo.CAMERA_PRESETS_PACKET;
    }

    @Override
    public void decode(HandleByteBuf byteBuf) {
    }

    @Override
    public void encode(HandleByteBuf byteBuf) {
        byteBuf.writeUnsignedVarInt(presets.size());
        for (var p : presets) {
            writePreset(byteBuf, p);
        }
    }

    public void writePreset(HandleByteBuf byteBuf, CameraPreset preset) {
        byteBuf.writeString(preset.getIdentifier());
        byteBuf.writeString(preset.getInheritFrom());
        byteBuf.writeNotNull(preset.getPos(), (v) -> byteBuf.writeFloatLE(v.getX()));
        byteBuf.writeNotNull(preset.getPos(), (v) -> byteBuf.writeFloatLE(v.getY()));
        byteBuf.writeNotNull(preset.getPos(), (v) -> byteBuf.writeFloatLE(v.getZ()));
        byteBuf.writeNotNull(preset.getPitch(), byteBuf::writeFloatLE);
        byteBuf.writeNotNull(preset.getYaw(), byteBuf::writeFloatLE);
        byteBuf.writeNotNull(preset.getRotationSpeed(), byteBuf::writeFloatLE);
        byteBuf.writeOptional(preset.getSnapToTarget(), byteBuf::writeBoolean);
        byteBuf.writeNotNull(preset.getViewOffset(), byteBuf::writeVector2f);
        byteBuf.writeNotNull(preset.getEntityOffset(), byteBuf::writeVector3f);
        byteBuf.writeNotNull(preset.getRadius(), byteBuf::writeFloatLE);
        byteBuf.writeNotNull(preset.getListener(), (l) -> byteBuf.writeByte((byte) l.ordinal()));
        byteBuf.writeOptional(preset.getPlayEffect(), byteBuf::writeBoolean);
    }

    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}
