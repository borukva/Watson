package eu.minemania.watson.network.ledger.rollback;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record PluginRollbackPacket(boolean restore, String searchData) implements CustomPacketPayload
{
    public static final Type<PluginRollbackPacket> ID = new Type<>(PluginRollbackPacketHandler.CHANNEL);
    public static final StreamCodec<FriendlyByteBuf, PluginRollbackPacket> CODEC = CustomPacketPayload.codec(PluginRollbackPacket::write, null);

    public void write(FriendlyByteBuf output)
    {
        output.writeBoolean(restore);
        output.writeUtf(searchData);
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return ID;
    }
}
