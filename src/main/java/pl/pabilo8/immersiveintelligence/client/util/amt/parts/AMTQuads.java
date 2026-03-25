package pl.pabilo8.immersiveintelligence.client.util.amt.parts;

import blusunrize.immersiveengineering.client.models.IESmartObjModel;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.model.IIModelRegistry;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.Arrays;

/**
 * AMT type for drawing 3d models using quads
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 26.07.2022
 */
public class AMTQuads extends AMT
{
	/**
	 * Default normal of an unlit quad.
	 */
	protected static final Vec3i NO_LIGHTING_NORMAL = new Vec3i(1, 1, 1);
	/**
	 * Quads acquired from a {@link IESmartObjModel}
	 */
	protected final BakedQuad[] quads;
	/**
	 * GL CallList ID
	 */
	protected int listID = -1;
	/**
	 * Color baked onto this element's quads.
	 */
	protected IIColor bakedColor = IIColor.WHITE;

	/**
	 * Whether this element has lighting (darkening the face when viewed from another angle) enabled<br>
	 * Use on static half-transparent elements, such as dust, glass, straw, etc..<br>
	 * When set to true, tint may be darker/lighter during rotation.
	 */
	protected boolean hasLighting = true;

	public AMTQuads(String name, Vec3d originPos, BakedQuad[] quads)
	{
		super(name, originPos);
		this.quads = quads;
	}

	private static AMT recolorChild(AMT child, IIColor color)
	{
		if(child instanceof AMTQuads)
			return ((AMTQuads)child).recolor(color);
		return child;
	}

	@Override
	protected void draw(Tessellator tes, BufferBuilder buf)
	{
		if(listID!=-1)
			GlStateManager.callList(listID);
		else
		{
			listID = GLAllocation.generateDisplayLists(1);
			GL11.glNewList(listID, GL11.GL_COMPILE);

			if(quads.length > 0)
			{
				buf.begin(7, DefaultVertexFormats.ITEM);

				//Use forge's trick to put colored quads to the buffer
				//Since it's inside a GLCallList, the speed will get boosted significantly
				if(bakedColor!=IIColor.WHITE)
				{
					float[] floats = bakedColor.getFloatRGB();
					for(BakedQuad quad : quads)
					{
						buf.addVertexData(quad.getVertexData());
						buf.putColorRGB_F4(floats[0], floats[1], floats[2]);
						Vec3i vec3i = hasLighting?(quad.getFace().getDirectionVec()): NO_LIGHTING_NORMAL;
						buf.putNormal((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
					}
					tes.draw();
					buf.setTranslation(0, 0, 0);

					buf.begin(7, DefaultVertexFormats.ITEM);
					buf.addVertexData(IIModelRegistry.QUAD_EMPTY.getVertexData());
					buf.putColorRGB_F4(1, 1, 1);
					buf.putNormal((float)NO_LIGHTING_NORMAL.getX(), (float)NO_LIGHTING_NORMAL.getY(), (float)NO_LIGHTING_NORMAL.getZ());
				}
				else
					//Else use the ancient method to place them as the scripture says
					for(BakedQuad quad : quads)
					{
						buf.addVertexData(quad.getVertexData());
						buf.putColorRGB_F4(1, 1, 1);
						Vec3i vec3i = hasLighting?(quad.getFace().getDirectionVec()): NO_LIGHTING_NORMAL;
						buf.putNormal((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
					}
				tes.draw();
				buf.setTranslation(0, 0, 0);
			}

			GL11.glEndList();
		}
	}

	@Override
	public void disposeOf()
	{
		if(listID!=-1)
			GlStateManager.glDeleteLists(listID, 1);
		listID = -1;
	}

	@Override
	public void applyProperties(EasyNBT nbt)
	{
		super.applyProperties(nbt);
		nbt.checkSetBoolean("lighting", this::setLighting);
	}

	public final BakedQuad[] getQuads()
	{
		return quads;
	}

	/**
	 * @param color new color to be baked on quads
	 * @return a recolored copy of this AMTQuads
	 */
	public AMTQuads recolor(IIColor color)
	{
		//Copy and recolor this quad
		AMTQuads copy = new AMTQuads(this.name, this.originPos,
				Arrays.stream(quads)
						.map(q -> new BakedQuad(Arrays.copyOf(q.getVertexData(), q.getVertexData().length), 1, q.getFace(), q.getSprite(), q.shouldApplyDiffuseLighting(), q.getFormat()))
						.toArray(BakedQuad[]::new)
		);
		copy.bakedColor = color;

		//Copy and recolor children as well
		AMT[] children = getChildren();
		if(children!=null)
			copy.setChildren(Arrays.stream(children).map(child -> recolorChild(child, color)).toArray(AMT[]::new));
		return copy;
	}

	/**
	 * Shrink the quads by the given amount using their normals. This is useful for preventing z-fighting when two quads are in the same position, such as a block and its overlay.<br>
	 *
	 * @param shrinkAmount the amount to shrink the quads by
	 * @return a copy of this AMTQuads with shrunk quads
	 */
	public AMT shrinkByNormals(double shrinkAmount)
	{
		AMTQuads copy = new AMTQuads(this.name, this.originPos,
				Arrays.stream(quads)
						.map(q -> {
							Vec3i normal = q.getFace().getDirectionVec();
							int[] vertexData = Arrays.copyOf(q.getVertexData(), q.getVertexData().length);
							for(int i = 0; i < 4; i++)
							{
								//Extract vertex position from quad data
								int vertexIndex = i*DefaultVertexFormats.BLOCK.getIntegerSize();

								vertexData[vertexIndex+i] = Float.floatToIntBits((float)(Float.intBitsToFloat(vertexData[vertexIndex+i])-(normal.getX()*shrinkAmount)));
								vertexData[vertexIndex+i+1] = Float.floatToIntBits((float)(Float.intBitsToFloat(vertexData[vertexIndex+i+1])-normal.getY()*shrinkAmount));
								vertexData[vertexIndex+i+2] = Float.floatToIntBits((float)(Float.intBitsToFloat(vertexData[vertexIndex+i+2])-normal.getZ()*shrinkAmount));
							}
							return new BakedQuad(vertexData, q.getTintIndex(), q.getFace(), q.getSprite(), q.shouldApplyDiffuseLighting(), q.getFormat());
						})
						.toArray(BakedQuad[]::new)
		);
		copy.bakedColor = bakedColor;

		//Copy children as well
		AMT[] children = getChildren();
		if(children!=null)
			copy.setChildren(Arrays.stream(children).map(child -> {
				if(child instanceof AMTQuads)
					return ((AMTQuads)child).shrinkByNormals(shrinkAmount);
				return child;
			}).toArray(AMT[]::new));
		return copy;
	}

	public void setLighting(boolean hasLighting)
	{
		this.hasLighting = hasLighting;
	}
}
