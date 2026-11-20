package eu.minemania.watson.render;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import fi.dy.masa.malilib.util.data.Color4f;
import fi.dy.masa.malilib.util.PositionUtils;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class RenderUtils
{
    private static final RandomSource RAND = RandomSource.create();

    public static BufferBuilder startDrawingLines(Tesselator tessellator)
    {
        RenderSystem.setShader(CoreShaders.RENDERTYPE_LINES);
        return tessellator.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
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
        buffer.addVertex(minX, minY, minZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(minX, minY, maxZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(minX, minY, maxZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(minX, maxY, maxZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(minX, maxY, maxZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(minX, maxY, minZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(minX, maxY, minZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(minX, minY, minZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        // East side
        buffer.addVertex(maxX, minY, maxZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(maxX, minY, minZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(maxX, minY, minZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(maxX, maxY, minZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(maxX, maxY, minZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(maxX, maxY, maxZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(maxX, maxY, maxZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(maxX, minY, maxZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        // North side (don't repeat the vertical lines that are done by the east/west sides)
        buffer.addVertex(maxX, minY, minZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(minX, minY, minZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(minX, maxY, minZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(maxX, maxY, minZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        // South side (don't repeat the vertical lines that are done by the east/west sides)
        buffer.addVertex(minX, minY, maxZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(maxX, minY, maxZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(maxX, maxY, maxZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(minX, maxY, maxZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
    }
    //END TEMP MALILIB

    public static void drawFullBlockOutlinesBatched(float x, float y, float z, Color4f color, BufferBuilder buffer)
    {
        buffer.addVertex(x, y, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + 1F, y, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x, y, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x, y, z + 1F).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x, y, z + 1F).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + 1F, y, z + 1F).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x + 1F, y, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + 1F, y, z + 1F).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x, y + 1F, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + 1F, y + 1F, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x, y + 1F, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x, y + 1F, z + 1F).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x, y + 1F, z + 1F).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + 1F, y + 1F, z + 1F).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x + 1F, y + 1F, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + 1F, y + 1F, z + 1F).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x, y, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x, y + 1F, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x + 1F, y, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + 1F, y + 1F, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x, y, z + 1F).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x, y + 1F, z + 1F).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x + 1F, y, z + 1F).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + 1F, y + 1, z + 1F).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
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

        buffer.addVertex(posX, posY, posZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(posX + widthX, posY, posZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(posX, posY + heightY, posZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(posX + widthX, posY + heightY, posZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(posX, posY, posZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(posX, posY + heightY, posZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(posX + widthX, posY, posZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(posX + widthX, posY + heightY, posZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(posX, posY, posZ + widthZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(posX + widthX, posY, posZ + widthZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(posX, posY + heightY, posZ + widthZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(posX + widthX, posY + heightY, posZ + widthZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(posX, posY, posZ + widthZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(posX, posY + heightY, posZ + widthZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(posX + widthX, posY, posZ + widthZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(posX + widthX, posY + heightY, posZ + widthZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(posX + widthX, posY, posZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(posX + widthX, posY, posZ + widthZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(posX, posY, posZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(posX, posY, posZ + widthZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(posX + widthX, posY + heightY, posZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(posX + widthX, posY + heightY, posZ + widthZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(posX, posY + heightY, posZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(posX, posY + heightY, posZ + widthZ).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
    }

    public static void drawBedOutlineBatched(float x, float y, float z, Color4f color, BufferBuilder buffer)
    {
        float shortLength = 0.19f;
        float reverseShortLength = 0.81f;
        float otherSide = 1f;
        float longHeight = 0.56f;

        //left front leg
        buffer.addVertex(x, y, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + shortLength, y, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x, y, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x, y, z + shortLength).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x, y, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x, y + longHeight, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x + shortLength, y, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + shortLength, y + shortLength, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x, y, z + shortLength).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x, y + shortLength, z + shortLength).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        //right front leg
        buffer.addVertex(x + reverseShortLength, y, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + otherSide, y, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x + otherSide, y, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + otherSide, y, z + shortLength).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x + otherSide, y, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + otherSide, y + longHeight, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x + reverseShortLength, y, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + reverseShortLength, y + shortLength, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x + otherSide, y, z + shortLength).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + otherSide, y + shortLength, z + shortLength).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        //left back leg
        buffer.addVertex(x, y, z + otherSide).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + shortLength, y, z + otherSide).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x, y, z + otherSide).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x, y, z + reverseShortLength).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x, y, z + otherSide).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x, y + longHeight, z + otherSide).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x + shortLength, y, z + 1).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + shortLength, y + shortLength, z + 1).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x, y, z + reverseShortLength).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x, y + shortLength, z + reverseShortLength).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        //right back leg
        buffer.addVertex(x + reverseShortLength, y, z + otherSide).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + otherSide, y, z + otherSide).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x + otherSide, y, z + reverseShortLength).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + otherSide, y, z + otherSide).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x + otherSide, y, z + otherSide).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + otherSide, y + longHeight, z + otherSide).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x + reverseShortLength, y, z + otherSide).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + reverseShortLength, y + shortLength, z + otherSide).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x + otherSide, y, z + reverseShortLength).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + otherSide, y + shortLength, z + reverseShortLength).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        //middle connections
        buffer.addVertex(x + shortLength, y + shortLength, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + reverseShortLength, y + shortLength, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x, y + shortLength, z + shortLength).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x, y + shortLength, z + reverseShortLength).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x + shortLength, y + shortLength, z + otherSide).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + reverseShortLength, y + shortLength, z + otherSide).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x + otherSide, y + shortLength, z + shortLength).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + otherSide, y + shortLength, z + reverseShortLength).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        //top connections
        buffer.addVertex(x, y + longHeight, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + otherSide, y + longHeight, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x, y + longHeight, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x, y + longHeight, z + otherSide).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x, y + longHeight, z + otherSide).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + otherSide, y + longHeight, z + otherSide).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);

        buffer.addVertex(x + otherSide, y + longHeight, z).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
        buffer.addVertex(x + otherSide, y + longHeight, z + otherSide).setColor(color.r, color.g, color.b, color.a).setNormal(0, 0, 0);
    }

    /**
     * Assumes a BufferBuilder in the GL_LINES mode has been initialized
     */
    public static void drawBlockModelOutlinesBatched(BakedModel model, BlockState state, BlockPos pos, Color4f color, BufferBuilder buffer)
    {
        for (final Direction side : PositionUtils.ALL_DIRECTIONS)
        {
            renderModelQuadOutlines(pos, color, model.getQuads(state, side, RAND), buffer);
        }

        renderModelQuadOutlines(pos, color, model.getQuads(state, null, RAND), buffer);
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
        int[] vertexData = quad.getVertices();
        Vec3i vec3i = quad.getDirection().getUnitVec3i();
        Vector3f vec3f = new Vector3f(vec3i.getX(), vec3i.getY(), vec3i.getZ());
        final int vertexSize = vertexData.length / 4;
        float[] fx = new float[4];
        float[] fy = new float[4];
        float[] fz = new float[4];

        for (int index = 0; index < 4; ++index)
        {
            fx[index] = x + Float.intBitsToFloat(vertexData[index * vertexSize]);
            fy[index] = y + Float.intBitsToFloat(vertexData[index * vertexSize + 1]);
            fz[index] = z + Float.intBitsToFloat(vertexData[index * vertexSize + 2]);
        }

        buffer.addVertex(fx[0], fy[0], fz[0]).setColor(color.r, color.g, color.b, color.a).setNormal(vec3f.x, vec3f.y, vec3f.z);
        buffer.addVertex(fx[1], fy[1], fz[1]).setColor(color.r, color.g, color.b, color.a).setNormal(vec3f.x, vec3f.y, vec3f.z);

        buffer.addVertex(fx[1], fy[1], fz[1]).setColor(color.r, color.g, color.b, color.a).setNormal(vec3f.x, vec3f.y, vec3f.z);
        buffer.addVertex(fx[2], fy[2], fz[2]).setColor(color.r, color.g, color.b, color.a).setNormal(vec3f.x, vec3f.y, vec3f.z);

        buffer.addVertex(fx[2], fy[2], fz[2]).setColor(color.r, color.g, color.b, color.a).setNormal(vec3f.x, vec3f.y, vec3f.z);
        buffer.addVertex(fx[3], fy[3], fz[3]).setColor(color.r, color.g, color.b, color.a).setNormal(vec3f.x, vec3f.y, vec3f.z);

        buffer.addVertex(fx[3], fy[3], fz[3]).setColor(color.r, color.g, color.b, color.a).setNormal(vec3f.x, vec3f.y, vec3f.z);
        buffer.addVertex(fx[0], fy[0], fz[0]).setColor(color.r, color.g, color.b, color.a).setNormal(vec3f.x, vec3f.y, vec3f.z);
    }
}