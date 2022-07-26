package pl.pabilo8.immersiveintelligence.client.animation;

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
 * @author Pabilo8
 * @since 26.07.2022
 */
public class AMTQuads extends AMT
{
	/**
	 * Quads acquired from a {@link blusunrize.immersiveengineering.client.models.IESmartObjModel}
	 */
	private final BakedQuad[] quads;
	private int listID = -1;

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
					if(color!=null&&bakedquad.hasTintIndex())
						buf.putColorRGB_F4(color.x, color.y, color.z);
					else
						buf.putColorRGB_F4(1f, 1f, 1f);
					Vec3i vec3i = bakedquad.getFace().getDirectionVec();
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
}
