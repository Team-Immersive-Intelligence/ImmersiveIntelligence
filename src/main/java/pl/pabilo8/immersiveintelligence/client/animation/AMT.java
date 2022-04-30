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
import org.lwjgl.util.vector.Vector3f;

/**
 * @author Pabilo8
 * @since 03.04.2022
 * <p>
 * An attempt to combine .obj and TMT<br>
 * Introducing the Animated Model Thingy(tm)<br>
 * Brace yourselves
 */
public class AMT
{
	// --- FINAL PROPERTIES ---//

	/**
	 * The name of this ModelThingy
	 */
	public final String name;
	/**
	 * The default position and rotation, not to be modified
	 */
	private final Vec3d originPos;
	/**
	 * Quads acquired from a {@link blusunrize.immersiveengineering.client.models.IESmartObjModel}
	 */
	private final BakedQuad[] quads;
	private int listID = -1;
	/**
	 * Children ModelThingies, rendered after this one
	 */
	private AMT[] children;

	// --- MUTABLE PROPERTIES ---//

	boolean visible;
	Vec3d off, scale;
	Vector3f color;
	Vec3d rot;

	public AMT(String name, Vec3d originPos, BakedQuad[] quads)
	{
		//final variables
		this.name = name;
		this.originPos = originPos;
		this.quads = quads;

		defaultize();
	}

	/**
	 * Renders this AMT
	 *
	 * @param tes Tesselator to be used
	 * @param buf BufferBuilder of tes
	 */
	public void render(Tessellator tes, BufferBuilder buf)
	{
		if(!visible)
			return;

		GlStateManager.pushMatrix();

		if(off!=null)
			GlStateManager.translate(-off.x, off.y, off.z);


		GlStateManager.translate(-originPos.x, originPos.y, -originPos.z);

		if(rot!=null)
		{
			GlStateManager.rotate((float)rot.z, 0, 0, 1);
			GlStateManager.rotate((float)rot.y, 0, 1, 0);
			GlStateManager.rotate((float)rot.x, 1, 0, 0);
		}

		GlStateManager.translate(originPos.x, -originPos.y, originPos.z);

		if(scale!=null)
			GlStateManager.scale(scale.x, scale.y, scale.z);

		if(listID!=-1)
			GlStateManager.callList(listID);
		else
		{
			listID = GLAllocation.generateDisplayLists(1);
			GL11.glNewList(listID, GL11.GL_COMPILE);

			for(BakedQuad bakedquad : quads)
			{
				buf.begin(7, DefaultVertexFormats.ITEM);
				buf.addVertexData(bakedquad.getVertexData());
				if(color!=null&&bakedquad.hasTintIndex())
					buf.putColorRGB_F4(color.x, color.y, color.z);
				else
					buf.putColorRGB_F4(1f, 1f, 1f);
				Vec3i vec3i = bakedquad.getFace().getDirectionVec();
				buf.putNormal((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
				tes.draw();
				buf.setTranslation(0, 0, 0);
			}

			GL11.glEndList();
		}

		if(children!=null)
			for(AMT child : children)
				child.render(tes, buf);
		GlStateManager.popMatrix();
	}

	/**
	 * Set all variables to default values
	 */
	public void defaultize()
	{
		visible = true;
		off = scale = rot = null;
		color = null;
	}

	/**
	 * Remove GL CallLists so they won't waste space, when not needed
	 */
	public void disposeOf()
	{
		if(listID!=-1)
			GlStateManager.glDeleteLists(listID, 1);
	}

	public void setChildren(AMT[] children)
	{
		this.children = children;
	}
}
