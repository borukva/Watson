package eu.minemania.watson.network.watson.world;

import com.google.common.base.Charsets;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class PluginWorldPacket
{
    private String world;

    public PluginWorldPacket(String world, FriendlyByteBuf buf)
    {
        this.world = world;
        buf.readBytes(buf.readableBytes());
    }

    public String getWorld()
    {
        return this.world;
    }

    public record Payload(PluginWorldPacket content) implements CustomPacketPayload
    {
        public static final Type<Payload> ID = new Type<>(PluginWorldPacketHandler.CHANNEL);
        public static final StreamCodec<FriendlyByteBuf, Payload> CODEC = CustomPacketPayload.codec(null, Payload::new);

        public Payload(FriendlyByteBuf buf)
        {
            this(new PluginWorldPacket(buf.toString(Charsets.UTF_8), buf));
        }

        @Override
        public Type<Payload> type()
        {
            return ID;
        }
    }
}