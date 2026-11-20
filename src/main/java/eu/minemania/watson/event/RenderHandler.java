package eu.minemania.watson.event;

import eu.minemania.watson.config.Configs;
import eu.minemania.watson.render.OverlayRenderer;
import eu.minemania.watson.render.WatsonRenderer;
import fi.dy.masa.malilib.interfaces.IRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.util.profiling.ProfilerFiller;
import org.joml.Matrix4f;

public class RenderHandler implements IRenderer
{
    private static final RenderHandler INSTANCE = new RenderHandler();

    public static RenderHandler getInstance()
    {
        return INSTANCE;
    }

    @Override
    public void onRenderWorldLastAdvanced(Matrix4f posMatrix, Matrix4f projMatrix, Frustum frustum, Camera camera, FogParameters fog, ProfilerFiller profiler)
    {
        Minecraft mc = Minecraft.getInstance();

        if (Configs.Generic.ENABLED.getBooleanValue() && mc.level != null && mc.player != null)
        {
            OverlayRenderer.renderOverlays(mc);
            WatsonRenderer.getInstance().piecewiseRenderEntities(mc, profiler, fog);
        }
    }
}
