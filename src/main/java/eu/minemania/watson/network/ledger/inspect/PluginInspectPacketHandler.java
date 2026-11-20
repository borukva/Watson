package eu.minemania.watson.network.ledger.inspect;

import eu.minemania.watson.Watson;
import eu.minemania.watson.config.Configs;
import fi.dy.masa.malilib.network.IPluginClientPlayHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public abstract class PluginInspectPacketHandler<T extends CustomPacketPayload> implements IPluginClientPlayHandler<T>
{
    public static final Identifier CHANNEL = Identifier.fromNamespaceAndPath("ledger","inspect");
    private boolean registered = false;
    private int failures = 0;

    private final static PluginInspectPacketHandler<PluginInspectPacket> INSTANCE = new PluginInspectPacketHandler<>()
    {
        @Override
        public void receive(PluginInspectPacket payload, ClientPlayNetworking.Context context)
        {
            this.receivePlayPayload(payload, context);
        }
    };

    public static PluginInspectPacketHandler<PluginInspectPacket> getInstance()
    {
        return INSTANCE;
    }

    @Override
    public Identifier getPayloadChannel()
    {
        return CHANNEL;
    }

    @Override
    public void tickFailures()
    {
        ++this.failures;
    }

    @Override
    public boolean checkFailures()
    {
        return this.failures <= this.maxFailures();
    }


    @Override
    public boolean isPlayRegistered(Identifier channel)
    {
        return this.registered;
    }

    @Override
    public void setPlayRegistered(Identifier channel)
    {
        if (channel.equals(CHANNEL))
        {
            this.registered = true;
            this.failures = 0;
        }
    }

    @Override
    public void reset(Identifier channel)
    {
        if (!channel.equals(CHANNEL))
        {
            return;
        }

        INSTANCE.unregisterPlayReceiver();
    }

    public void encodePayload(BlockPos pos, int pages)
    {
        if(Configs.Generic.DEBUG.getBooleanValue())
        {
            Watson.logger.info("blockpos: " + pos.toShortString());
            Watson.logger.info("pages: " + pages);
            Watson.logger.info(CHANNEL);
        }

        INSTANCE.sendPlayPayload(new PluginInspectPacket(pos, pages));
    }

    @Override
    public void encodeWithSplitter(FriendlyByteBuf buf, ClientPacketListener handler)
    {
        // NO-OP
    }

    @Override
    public void receivePlayPayload(T payload, ClientPlayNetworking.Context context)
    {
        // NO-OP
    }

}
