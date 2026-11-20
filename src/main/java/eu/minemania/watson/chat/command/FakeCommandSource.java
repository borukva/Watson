package eu.minemania.watson.chat.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.permissions.PermissionSet;

public class FakeCommandSource extends CommandSourceStack
{
    private static final List<String> colors = Arrays.asList("black", "darkblue", "darkgreen", "darkaqua", "darkred", "darkpurple", "gold", "grey", "gray", "darkgrey", "darkgray", "blue", "green", "aqua", "red", "lightpurple", "yellow", "white");
    private static final List<String> styles = Arrays.asList("+", "/", "_", "-", "?");

    public FakeCommandSource(LocalPlayer player)
    {
        super(CommandSource.NULL, player.position(), player.getRotationVector(), null, PermissionSet.NO_PERMISSIONS, player.getName().getString(), player.getDisplayName(), null, player);
    }

    @Override
    public Collection<String> getOnlinePlayerNames()
    {
        return Minecraft.getInstance().getConnection().getOnlinePlayers().stream().map(e -> e.getProfile().name()).collect(Collectors.toList());
    }

    public static Collection<String> getColor()
    {
        return colors;
    }

    public static Collection<String> getStyle()
    {
        return styles;
    }
}