package pl.pabilo8.immersiveintelligence.client.util.amt;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.lwjgl.opengl.GL11;

/**
 * AMT type for drawing 3d models using quads
 *
 * @author Pabilo8
 * @since 26.07.2022
 */
public class AMTQuads extends AMT
{
	/**
	 * Default normal of an unlit quad.
	 */
	private static final Vec3i NO_LIGHTING_NORMAL = new Vec3i(1, 1, 1);

	/**
	 * Quads acquired from a {@link blusunrize.immersiveengineering.client.models.IESmartObjModel}
	 */
	protected final BakedQuad[] quads;
	/**
	 * GL CallList ID
	 */
	private int listID = -1;

	/**
	 * Whether this element has lighting (darkening the face when viewed from another angle) enabled<br>
	 * Use on static half-transparent elements, such as dust, glass, straw, etc..<br>
	 * When set to true, tint may be darker/lighter during rotation.
	 */
	private boolean hasLighting = true;

	public AMTQuads(String name, Vec3d originPos, BakedQuad[] quads)
	{
		super(name, originPos);
		this.quads = quads;
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

				for(BakedQuad bakedquad : quads)
				{
					buf.addVertexData(bakedquad.getVertexData());
					buf.putColorRGB_F4(1f, 1f, 1f);
					Vec3i vec3i = hasLighting?(bakedquad.getFace().getDirectionVec()): NO_LIGHTING_NORMAL;
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
	}

	public void setLighting(boolean hasLighting)
	{
		this.hasLighting = hasLighting;
	}
}
