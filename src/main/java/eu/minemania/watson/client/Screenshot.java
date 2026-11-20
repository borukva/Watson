package eu.minemania.watson.client;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.mojang.blaze3d.platform.NativeImage;
import eu.minemania.watson.Watson;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import eu.minemania.watson.config.Configs;
import eu.minemania.watson.data.DataManager;

public class Screenshot
{

    /**
     * Makes screenshot and custom directory.
     */
    public static void makeScreenshot()
    {
        Date now = new Date();
        String player2 = (String) DataManager.getEditSelection().getVariables().get("player");
        String subdirectoryName = (player2 != null && !player2.isEmpty() && Configs.Generic.SS_PLAYER_DIRECTORY.getBooleanValue()) ? player2 : new SimpleDateFormat(Configs.Generic.SS_DATE_DIRECTORY.getStringValue()).format(now);
        subdirectoryName = subdirectoryName.replaceAll(":", "-").replaceAll(" ", "-");
        Minecraft mc = Minecraft.getInstance();
        File screenshotsDir = new File(mc.gameDirectory, "screenshots");
        File subdirectory = new File(screenshotsDir, subdirectoryName);
        File file = Screenshot.getUniqueFilename(subdirectory, player2, now);
        Screenshot.save(file, mc);
    }

    public static void save(File file, Minecraft mc)
    {
        net.minecraft.client.Screenshot.takeScreenshot(mc.gameRenderer.mainRenderTarget(), nativeImage -> Util.ioPool().execute(() -> {
            try {
                nativeImage.writeToFile(file);
                MutableComponent text = Component.literal(file.getName()).withStyle(ChatFormatting.UNDERLINE).withStyle(style -> style.withClickEvent(new ClickEvent.OpenFile(file.getAbsolutePath())));
                mc.execute(() -> mc.gui.hud.getChat().addClientSystemMessage(Component.translatable("screenshot.success", text)));
            }
            catch (Exception text) {
                Watson.logger.warn("Couldn't save screenshot", (Throwable)text);
                mc.execute(() -> mc.gui.hud.getChat().addClientSystemMessage(Component.translatable("screenshot.failure", text.getMessage())));
            }
            finally {
                nativeImage.close();
            }
        }));
    }

    /**
     * Returns unique file name for screenshot, adds username if filled in.
     *
     * @param dir    Directory gets saved
     * @param player Username
     * @param now    Current date
     * @return Unique PNG file name for screenshot
     */
    public static File getUniqueFilename(File dir, String player, Date now)
    {
        String baseName = _DATE_FORMAT.format(now);

        int count = 1;
        String playerSuffix = (player == null || player.isEmpty() || !Configs.Generic.SS_PLAYER_SUFFIX.getBooleanValue()) ? "" : "-" + player;
        while (true)
        {
            File result = new File(dir, baseName + playerSuffix + (count == 1 ? "" : "-" + count) + ".png");
            if (!result.exists())
            {
                dir.mkdirs();
                return result;
            }
            ++count;
        }
    }

    // --------------------------------------------------------------------------
    /**
     * Used to format dates for making screenshot filenames.
     */
    private static final DateFormat _DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
}