package eu.minemania.watson.chat.command;

import java.util.HashSet;
import java.util.Set;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;

/**
 * @author Earthcomputer
 */
public class ClientCommandManager
{
    private static Set<String> clientSideCommands = new HashSet<>();

    public static void clearClientSideCommands()
    {
        clientSideCommands.clear();
    }

    public static Set<String> getClientSideCommands()
    {
        return clientSideCommands;
    }

    public static void addClientSideCommand(String name)
    {
        clientSideCommands.add(name);
    }

    public static boolean isClientSideCommand(String name)
    {
        return clientSideCommands.contains(name);
    }

    public static void sendError(Component error)
    {
        sendFeedback(Component.literal("").append(error).withStyle(ChatFormatting.RED));
    }

    public static void sendFeedback(String message)
    {
        sendFeedback(Component.translatable(message));
    }

    public static void sendFeedback(Component message)
    {
        Minecraft.getInstance().gui.getChat().addMessage(message);
    }

    public static int executeCommand(StringReader reader, String command)
    {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        try
        {
            return player.connection.getCommands().execute(reader, new FakeCommandSource(player));
        }
        catch (CommandSyntaxException e)
        {
            ClientCommandManager.sendError(ComponentUtils.fromMessage(e.getRawMessage()));
            if (e.getInput() != null && e.getCursor() >= 0)
            {
                int cursor = Math.min(e.getCursor(), e.getInput().length());
                MutableComponent text = Component.literal("").withStyle(ChatFormatting.GRAY).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
                if (cursor > 10)
                {
                    text.append("...");
                }
                text.append(e.getInput().substring(Math.max(0, cursor - 10), cursor));
                if (cursor < e.getInput().length())
                {
                    text.append((Component.literal(e.getInput().substring(cursor)).withStyle(ChatFormatting.RED, ChatFormatting.UNDERLINE)));
                }

                text.append((Component.translatable("command.context.here").withStyle(ChatFormatting.RED, ChatFormatting.ITALIC)));
                ClientCommandManager.sendError(text);
            }
        }
        catch (Exception e)
        {
            MutableComponent error = Component.literal(e.getMessage() == null ? e.getClass().getName() : e.getMessage());
            ClientCommandManager.sendError(Component.translatable("command.failed").withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, error))));
            e.printStackTrace();
        }
        return 1;
    }
}