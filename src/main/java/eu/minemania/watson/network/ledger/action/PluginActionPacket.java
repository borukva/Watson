package eu.minemania.watson.network.ledger.action;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class PluginActionPacket
{
    private BlockPos blockPos;
    private String type;
    private ResourceLocation dim;
    private ResourceLocation oldObj;
    private ResourceLocation newObj;
    private String source;
    private long time;
    private boolean rolledBack;
    private String additionalData;

    public PluginActionPacket(BlockPos blockPos, String type, ResourceLocation dim, ResourceLocation oldObj, ResourceLocation newObj, String source, long time, boolean rolledBack, String additionalData)
    {
        this.blockPos = blockPos;
        this.type = type;
        this.dim = dim;
        this.oldObj = oldObj;
        this.newObj = newObj;
        this.source = source;
        this.time = time;
        this.rolledBack = rolledBack;
        this.additionalData = additionalData;
    }

    public BlockPos getBlockPos()
    {
        return this.blockPos;
    }

    public String getType()
    {
        return this.type;
    }

    public ResourceLocation getDimension()
    {
        return this.dim;
    }

    public ResourceLocation getOldObject()
    {
        return this.oldObj;
    }

    public ResourceLocation getNewObject()
    {
        return this.newObj;
    }

    public String getSource()
    {
        return this.source;
    }

    public long getTime()
    {
        return this.time;
    }

    public boolean isRolledBack()
    {
        return this.rolledBack;
    }

    public String getAdditionalData()
    {
        return this.additionalData;
    }

    public record Payload(PluginActionPacket content) implements CustomPacketPayload
    {
        public static final Type<Payload> ID = new Type<>(PluginActionPacketHandler.CHANNEL);
        public static final StreamCodec<FriendlyByteBuf, Payload> CODEC = CustomPacketPayload.codec(null, Payload::new);

        public Payload(FriendlyByteBuf input)
        {
            this(new PluginActionPacket(input.readBlockPos(), input.readUtf(), input.readResourceLocation(), input.readResourceLocation(), input.readResourceLocation(), input.readUtf(), input.readLong(), input.readBoolean(), input.readUtf()));
        }

        @Override
        public Type<Payload> type()
        {
            return ID;
        }
    }
}
