package eu.minemania.watson.render;

import com.mojang.blaze3d.systems.RenderSystem;

import eu.minemania.watson.config.Configs;
import eu.minemania.watson.data.DataManager;
import eu.minemania.watson.db.BlockEditSet;
import eu.minemania.watson.selection.EditSelection;
import fi.dy.masa.malilib.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4fStack;

public class WatsonRenderer
{
    private static final WatsonRenderer INSTANCE = new WatsonRenderer();

    private Minecraft mc;

    public static WatsonRenderer getInstance()
    {
        return INSTANCE;
    }

    public void piecewiseRenderEntities(Minecraft mc, ProfilerFiller profiler, FogParameters fog)
    {
        if (this.mc == null)
        {
            this.mc = mc;
        }
        if (Configs.Generic.DISPLAYED.getBooleanValue() && this.mc.getCameraEntity() != null && Configs.Outlines.OUTLINE_SHOWN.getBooleanValue())
        {
            profiler.push(() -> "watson_entities");
            RenderSystem.setShaderFog(FogParameters.NO_FOG);
            EditSelection selection = DataManager.getEditSelection();
            BlockEditSet edits = selection.getBlockEditSet();
            Matrix4fStack matrixStack = RenderSystem.getModelViewStack();
            matrixStack.pushMatrix();
            RenderSystem.disableCull();

            RenderUtils.setupBlend();

            RenderUtils.color(1f, 1f, 1f, 1f);
            RenderSystem.depthMask(false);

            RenderSystem.disableDepthTest();

            Vec3 cameraPos = this.mc.gameRenderer.getMainCamera().getPosition();

            matrixStack.translate((float) -cameraPos.x(), (float) -cameraPos.y(), (float) -cameraPos.z());
            edits.drawOutlines();
            edits.drawVectors();
            selection.drawSelection();

            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(true);

            RenderSystem.disableBlend();

            RenderSystem.enableCull();

            matrixStack.popMatrix();
            RenderSystem.setShaderFog(fog);
            profiler.pop();
        }
    }
}