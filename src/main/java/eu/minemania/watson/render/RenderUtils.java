package eu.minemania.watson.render;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import eu.minemania.watson.Reference;
import eu.minemania.watson.Watson;
import fi.dy.masa.malilib.render.MaLiLibPipelines;
import fi.dy.masa.malilib.render.RenderContext;
import fi.dy.masa.malilib.util.data.Color4f;
import fi.dy.masa.malilib.util.position.PositionUtils;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3fc;

public class RenderUtils
{
    private static final RandomSource RAND = RandomSource.create();
    private static double cameraX;
    private static double cameraY;
    private static double cameraZ;
    private static float lineWidth = 1f;

    /**
     * Vertices are given in world coordinates and made relative to this camera position,
     * since there is no global model-view matrix to translate anymore.
     */
    public static void setCameraPos(Vec3 cameraPos)
    {
        cameraX = cameraPos.x;
        cameraY = cameraPos.y;
        cameraZ = cameraPos.z;
    }

    /**
     * Line width is a per-vertex attribute now; it applies to the vertices added after this call.
     */
    public static void setLineWidth(float width)
    {
        lineWidth = width;
    }

    public static RenderContext startDrawingLines(String name)
    {
        return new RenderContext(() -> Reference.MOD_ID + ":" + name, MaLiLibPipelines.DEBUG_LINES_MASA_SIMPLE_NO_DEPTH_NO_CULL, 0);
    }

    public static void draw(RenderContext ctx)
    {
        try
        {
            MeshData meshData = ctx.getBuilder().build();

            if (meshData != null)
            {
                ctx.draw(meshData, false, true);
                meshData.close();
            }

            ctx.close();
        }
        catch (Exception e)
        {
            Watson.logger.error("Failed to draw {}: {}", ctx.getName(), e.getMessage());
        }
    }

    public static void vertex(BufferBuilder buffer, double x, double y, double z, Color4f color)
    {
        buffer.addVertex((float) (x - cameraX), (float) (y - cameraY), (float) (z - cameraZ))
              .setColor(color.r, color.g, color.b, color.a)
              .setLineWidth(lineWidth);
    }

    //START TEMP MALILIB
    /**
     * Assumes a BufferBuilder in GL_LINES mode has been initialized
     */
    public static void drawBlockBoundingBoxOutlinesBatchedLines(BlockPos pos, Color4f color, double expand, BufferBuilder buffer)
    {
        drawBlockBoundingBoxOutlinesBatchedLines(pos, Vec3.ZERO, color, expand, buffer);
    }

    /**
     * Assumes a BufferBuilder in GL_LINES mode has been initialized.
     * The cameraPos value will be subtracted from the absolute coordinate values of the passed in BlockPos.
     */
    public static void drawBlockBoundingBoxOutlinesBatchedLines(BlockPos pos, Vec3 cameraPos, Color4f color, double expand, BufferBuilder buffer)
    {
        float minX = (float) (pos.getX() - expand - cameraPos.x);
        float minY = (float) (pos.getY() - expand - cameraPos.y);
        float minZ = (float) (pos.getZ() - expand - cameraPos.z);
        float maxX = (float) (pos.getX() + expand - cameraPos.x + 1);
        float maxY = (float) (pos.getY() + expand - cameraPos.y + 1);
        float maxZ = (float) (pos.getZ() + expand - cameraPos.z + 1);

        drawBoxAllEdgesBatchedLines(minX, minY, minZ, maxX, maxY, maxZ, color, buffer);
    }

    /**
     * Assumes a BufferBuilder in GL_LINES mode has been initialized
     */
    public static void drawBoxAllEdgesBatchedLines(float minX, float minY, float minZ, float maxX, float maxY, float maxZ,
                                                   Color4f color, BufferBuilder buffer)
    {
        // West side
        vertex(buffer, minX, minY, minZ, color);
        vertex(buffer, minX, minY, maxZ, color);

        vertex(buffer, minX, minY, maxZ, color);
        vertex(buffer, minX, maxY, maxZ, color);

        vertex(buffer, minX, maxY, maxZ, color);
        vertex(buffer, minX, maxY, minZ, color);

        vertex(buffer, minX, maxY, minZ, color);
        vertex(buffer, minX, minY, minZ, color);

        // East side
        vertex(buffer, maxX, minY, maxZ, color);
        vertex(buffer, maxX, minY, minZ, color);

        vertex(buffer, maxX, minY, minZ, color);
        vertex(buffer, maxX, maxY, minZ, color);

        vertex(buffer, maxX, maxY, minZ, color);
        vertex(buffer, maxX, maxY, maxZ, color);

        vertex(buffer, maxX, maxY, maxZ, color);
        vertex(buffer, maxX, minY, maxZ, color);

        // North side (don't repeat the vertical lines that are done by the east/west sides)
        vertex(buffer, maxX, minY, minZ, color);
        vertex(buffer, minX, minY, minZ, color);

        vertex(buffer, minX, maxY, minZ, color);
        vertex(buffer, maxX, maxY, minZ, color);

        // South side (don't repeat the vertical lines that are done by the east/west sides)
        vertex(buffer, minX, minY, maxZ, color);
        vertex(buffer, maxX, minY, maxZ, color);

        vertex(buffer, maxX, maxY, maxZ, color);
        vertex(buffer, minX, maxY, maxZ, color);
    }
    //END TEMP MALILIB

    public static void drawFullBlockOutlinesBatched(float x, float y, float z, Color4f color, BufferBuilder buffer)
    {
        vertex(buffer, x, y, z, color);
        vertex(buffer, x + 1F, y, z, color);

        vertex(buffer, x, y, z, color);
        vertex(buffer, x, y, z + 1F, color);

        vertex(buffer, x, y, z + 1F, color);
        vertex(buffer, x + 1F, y, z + 1F, color);

        vertex(buffer, x + 1F, y, z, color);
        vertex(buffer, x + 1F, y, z + 1F, color);

        vertex(buffer, x, y + 1F, z, color);
        vertex(buffer, x + 1F, y + 1F, z, color);

        vertex(buffer, x, y + 1F, z, color);
        vertex(buffer, x, y + 1F, z + 1F, color);

        vertex(buffer, x, y + 1F, z + 1F, color);
        vertex(buffer, x + 1F, y + 1F, z + 1F, color);

        vertex(buffer, x + 1F, y + 1F, z, color);
        vertex(buffer, x + 1F, y + 1F, z + 1F, color);

        vertex(buffer, x, y, z, color);
        vertex(buffer, x, y + 1F, z, color);

        vertex(buffer, x + 1F, y, z, color);
        vertex(buffer, x + 1F, y + 1F, z, color);

        vertex(buffer, x, y, z + 1F, color);
        vertex(buffer, x, y + 1F, z + 1F, color);

        vertex(buffer, x + 1F, y, z + 1F, color);
        vertex(buffer, x + 1F, y + 1, z + 1F, color);
    }

    public static void drawSpecialOutlinesBatched(float x, float y, float z, Color4f color, BufferBuilder buffer, boolean sign)
    {
        float posX = x + 0.25F / 2;
        float posY = y + 0.25F / 2;
        float posZ = z + 0.015F;
        float widthX = (12 / 32.0F) * 2;
        float heightY = (12 / 32.0F) * 2;
        float widthZ = (1.0F / 32.0F) * 2;

        if (sign)
        {
            posX = posX - 0.1F;
            posY = posY + 0.2F;
            widthX = widthX + 0.2F;
            heightY = heightY - 0.3F;
        }

        vertex(buffer, posX, posY, posZ, color);
        vertex(buffer, posX + widthX, posY, posZ, color);

        vertex(buffer, posX, posY + heightY, posZ, color);
        vertex(buffer, posX + widthX, posY + heightY, posZ, color);

        vertex(buffer, posX, posY, posZ, color);
        vertex(buffer, posX, posY + heightY, posZ, color);

        vertex(buffer, posX + widthX, posY, posZ, color);
        vertex(buffer, posX + widthX, posY + heightY, posZ, color);

        vertex(buffer, posX, posY, posZ + widthZ, color);
        vertex(buffer, posX + widthX, posY, posZ + widthZ, color);

        vertex(buffer, posX, posY + heightY, posZ + widthZ, color);
        vertex(buffer, posX + widthX, posY + heightY, posZ + widthZ, color);

        vertex(buffer, posX, posY, posZ + widthZ, color);
        vertex(buffer, posX, posY + heightY, posZ + widthZ, color);

        vertex(buffer, posX + widthX, posY, posZ + widthZ, color);
        vertex(buffer, posX + widthX, posY + heightY, posZ + widthZ, color);

        vertex(buffer, posX + widthX, posY, posZ, color);
        vertex(buffer, posX + widthX, posY, posZ + widthZ, color);

        vertex(buffer, posX, posY, posZ, color);
        vertex(buffer, posX, posY, posZ + widthZ, color);

        vertex(buffer, posX + widthX, posY + heightY, posZ, color);
        vertex(buffer, posX + widthX, posY + heightY, posZ + widthZ, color);

        vertex(buffer, posX, posY + heightY, posZ, color);
        vertex(buffer, posX, posY + heightY, posZ + widthZ, color);
    }

    public static void drawBedOutlineBatched(float x, float y, float z, Color4f color, BufferBuilder buffer)
    {
        float shortLength = 0.19f;
        float reverseShortLength = 0.81f;
        float otherSide = 1f;
        float longHeight = 0.56f;

        //left front leg
        vertex(buffer, x, y, z, color);
        vertex(buffer, x + shortLength, y, z, color);

        vertex(buffer, x, y, z, color);
        vertex(buffer, x, y, z + shortLength, color);

        vertex(buffer, x, y, z, color);
        vertex(buffer, x, y + longHeight, z, color);

        vertex(buffer, x + shortLength, y, z, color);
        vertex(buffer, x + shortLength, y + shortLength, z, color);

        vertex(buffer, x, y, z + shortLength, color);
        vertex(buffer, x, y + shortLength, z + shortLength, color);

        //right front leg
        vertex(buffer, x + reverseShortLength, y, z, color);
        vertex(buffer, x + otherSide, y, z, color);

        vertex(buffer, x + otherSide, y, z, color);
        vertex(buffer, x + otherSide, y, z + shortLength, color);

        vertex(buffer, x + otherSide, y, z, color);
        vertex(buffer, x + otherSide, y + longHeight, z, color);

        vertex(buffer, x + reverseShortLength, y, z, color);
        vertex(buffer, x + reverseShortLength, y + shortLength, z, color);

        vertex(buffer, x + otherSide, y, z + shortLength, color);
        vertex(buffer, x + otherSide, y + shortLength, z + shortLength, color);

        //left back leg
        vertex(buffer, x, y, z + otherSide, color);
        vertex(buffer, x + shortLength, y, z + otherSide, color);

        vertex(buffer, x, y, z + otherSide, color);
        vertex(buffer, x, y, z + reverseShortLength, color);

        vertex(buffer, x, y, z + otherSide, color);
        vertex(buffer, x, y + longHeight, z + otherSide, color);

        vertex(buffer, x + shortLength, y, z + 1, color);
        vertex(buffer, x + shortLength, y + shortLength, z + 1, color);

        vertex(buffer, x, y, z + reverseShortLength, color);
        vertex(buffer, x, y + shortLength, z + reverseShortLength, color);

        //right back leg
        vertex(buffer, x + reverseShortLength, y, z + otherSide, color);
        vertex(buffer, x + otherSide, y, z + otherSide, color);

        vertex(buffer, x + otherSide, y, z + reverseShortLength, color);
        vertex(buffer, x + otherSide, y, z + otherSide, color);

        vertex(buffer, x + otherSide, y, z + otherSide, color);
        vertex(buffer, x + otherSide, y + longHeight, z + otherSide, color);

        vertex(buffer, x + reverseShortLength, y, z + otherSide, color);
        vertex(buffer, x + reverseShortLength, y + shortLength, z + otherSide, color);

        vertex(buffer, x + otherSide, y, z + reverseShortLength, color);
        vertex(buffer, x + otherSide, y + shortLength, z + reverseShortLength, color);

        //middle connections
        vertex(buffer, x + shortLength, y + shortLength, z, color);
        vertex(buffer, x + reverseShortLength, y + shortLength, z, color);

        vertex(buffer, x, y + shortLength, z + shortLength, color);
        vertex(buffer, x, y + shortLength, z + reverseShortLength, color);

        vertex(buffer, x + shortLength, y + shortLength, z + otherSide, color);
        vertex(buffer, x + reverseShortLength, y + shortLength, z + otherSide, color);

        vertex(buffer, x + otherSide, y + shortLength, z + shortLength, color);
        vertex(buffer, x + otherSide, y + shortLength, z + reverseShortLength, color);

        //top connections
        vertex(buffer, x, y + longHeight, z, color);
        vertex(buffer, x + otherSide, y + longHeight, z, color);

        vertex(buffer, x, y + longHeight, z, color);
        vertex(buffer, x, y + longHeight, z + otherSide, color);

        vertex(buffer, x, y + longHeight, z + otherSide, color);
        vertex(buffer, x + otherSide, y + longHeight, z + otherSide, color);

        vertex(buffer, x + otherSide, y + longHeight, z, color);
        vertex(buffer, x + otherSide, y + longHeight, z + otherSide, color);
    }

    /**
     * Assumes a BufferBuilder in the GL_LINES mode has been initialized
     */
    public static void drawBlockModelOutlinesBatched(BlockStateModel model, BlockPos pos, Color4f color, BufferBuilder buffer)
    {
        List<BlockStateModelPart> parts = new ArrayList<>();
        model.collectParts(RAND, parts);

        for (BlockStateModelPart part : parts)
        {
            for (final Direction side : PositionUtils.ALL_DIRECTIONS)
            {
                renderModelQuadOutlines(pos, color, part.getQuads(side), buffer);
            }

            renderModelQuadOutlines(pos, color, part.getQuads(null), buffer);
        }
    }

    private static void renderModelQuadOutlines(BlockPos pos, Color4f color, List<BakedQuad> quads, BufferBuilder buffer)
    {
        for (BakedQuad quad : quads)
        {
            renderQuadOutlinesBatched(pos, color, quad, buffer);
        }
    }

    private static void renderQuadOutlinesBatched(BlockPos pos, Color4f color, BakedQuad quad, BufferBuilder buffer)
    {
        final int x = pos.getX();
        final int y = pos.getY();
        final int z = pos.getZ();
        float[] fx = new float[BakedQuad.VERTEX_COUNT];
        float[] fy = new float[BakedQuad.VERTEX_COUNT];
        float[] fz = new float[BakedQuad.VERTEX_COUNT];

        for (int index = 0; index < BakedQuad.VERTEX_COUNT; ++index)
        {
            Vector3fc position = quad.position(index);
            fx[index] = x + position.x();
            fy[index] = y + position.y();
            fz[index] = z + position.z();
        }

        vertex(buffer, fx[0], fy[0], fz[0], color);
        vertex(buffer, fx[1], fy[1], fz[1], color);

        vertex(buffer, fx[1], fy[1], fz[1], color);
        vertex(buffer, fx[2], fy[2], fz[2], color);

        vertex(buffer, fx[2], fy[2], fz[2], color);
        vertex(buffer, fx[3], fy[3], fz[3], color);

        vertex(buffer, fx[3], fy[3], fz[3], color);
        vertex(buffer, fx[0], fy[0], fz[0], color);
    }
}