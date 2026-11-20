package eu.minemania.watson.mixin;

import com.mojang.brigadier.StringReader;
import eu.minemania.watson.chat.command.ClientCommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.brigadier.CommandDispatcher;
import eu.minemania.watson.chat.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundCommandsPacket;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPlayNetworkHandler
{
    @Shadow
    private CommandDispatcher<CommandSourceStack> commands;

    @SuppressWarnings("unchecked")
    @Inject(method = "<init>", at = @At("RETURN"))
    public void onInit(Minecraft client, Connection clientConnection, CommonListenerCookie clientConnectionState, CallbackInfo ci)
    {
        Command.registerCommands((CommandDispatcher<CommandSourceStack>) (Object) commands);
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "handleCommands", at = @At("TAIL"))
    public void onOnCommandTree(ClientboundCommandsPacket packet, CallbackInfo ci)
    {
        Command.registerCommands((CommandDispatcher<CommandSourceStack>) (Object) commands);
    }

    @ModifyVariable(method = "sendCommand", at = @At("HEAD"), argsOnly = true)
    private String onSendCommand(String message)
    {
        if ((message.startsWith("pr l") || message.startsWith("pr i")) && !message.contains("-extended"))
        {
            return message + " -extended";
        }

        return message;
    }

    @Inject(method = "sendCommand", at = @At("HEAD"), cancellable = true)
    private void sendCommand(String message, CallbackInfo ci)
    {
        StringReader reader = new StringReader(message);
        int cursor = reader.getCursor();
        String commandName = reader.canRead() ? reader.readUnquotedString() : "";
        reader.setCursor(cursor);
        if (ClientCommandManager.isClientSideCommand(commandName))
        {
            ClientCommandManager.executeCommand(reader, message);
            ci.cancel();
        }
    }
}
