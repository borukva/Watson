package eu.minemania.watson.network.ledger.response;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class PluginResponsePacket
{
    private ResourceLocation identifier;
    private int responseCode;

    public PluginResponsePacket(ResourceLocation identifier, int responseCode)
    {
        this.identifier = identifier;
        this.responseCode = responseCode;
    }

    public ResourceLocation getIdentifier()
    {
        return this.identifier;
    }

    public int getResponseCode()
    {
        return this.responseCode;
    }

    public record Payload(PluginResponsePacket content) implements CustomPacketPayload
    {
        public static final Type<Payload> ID = new Type<>(PluginResponsePacketHandler.CHANNEL);
        public static final StreamCodec<FriendlyByteBuf, Payload> CODEC = CustomPacketPayload.codec(null, Payload::new);

        public Payload(FriendlyByteBuf input)
        {
            this(new PluginResponsePacket(input.readResourceLocation(), input.readInt()));
        }

        @Override
        public Type<Payload> type()
        {
            return ID;
        }
    }
}
