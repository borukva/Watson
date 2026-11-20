package eu.minemania.watson.network.ledger.inspect;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;


public record PluginInspectPacket(BlockPos pos, int pages) implements CustomPacketPayload
{
    public static final Type<PluginInspectPacket> ID = new Type<>(PluginInspectPacketHandler.CHANNEL);
    public static final StreamCodec<FriendlyByteBuf, PluginInspectPacket> CODEC = CustomPacketPayload.codec(PluginInspectPacket::write, null);

    public void write(FriendlyByteBuf output)
    {
        output.writeBlockPos(pos);
        output.writeInt(pages);
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return ID;
    }
}
