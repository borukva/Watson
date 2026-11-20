package eu.minemania.watson.render;

import eu.minemania.watson.config.Configs;
import eu.minemania.watson.data.DataManager;
import eu.minemania.watson.db.BlockEditSet;
import eu.minemania.watson.selection.EditSelection;
import net.minecraft.client.Minecraft;
import net.minecraft.util.profiling.ProfilerFiller;

public class WatsonRenderer
{
    private static final WatsonRenderer INSTANCE = new WatsonRenderer();

    private Minecraft mc;

    public static WatsonRenderer getInstance()
    {
        return INSTANCE;
    }

    public void piecewiseRenderEntities(Minecraft mc, ProfilerFiller profiler)
    {
        if (this.mc == null)
        {
            this.mc = mc;
        }
        if (Configs.Generic.DISPLAYED.getBooleanValue() && this.mc.getCameraEntity() != null && Configs.Outlines.OUTLINE_SHOWN.getBooleanValue())
        {
            profiler.push(() -> "watson_entities");
            EditSelection selection = DataManager.getEditSelection();
            BlockEditSet edits = selection.getBlockEditSet();

            RenderUtils.setCameraPos(fi.dy.masa.malilib.render.RenderUtils.camPos());
            edits.drawOutlines();
            edits.drawVectors();
            selection.drawSelection();
            profiler.pop();
        }
    }
}
