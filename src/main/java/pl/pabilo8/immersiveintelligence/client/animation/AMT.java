package pl.pabilo8.immersiveintelligence.client.animation;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.util.vector.Vector3f;
import pl.pabilo8.immersiveintelligence.client.ShaderUtil;

import javax.annotation.Nonnull;
import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 03.04.2022
 * <p>
 * An attempt to combine .obj and TMT<br>
 * Introducing the Animated Model Thingy(tm)<br>
 * Brace yourselves
 */
public abstract class AMT
{
	//--- Final Properties ---//

	/**
	 * The name of this ModelThingy
	 */
	public final String name;
	/**
	 * The default position and rotation, not to be modified
	 */
	protected final Vec3d originPos;
	/**
	 * Children ModelThingies, rendered after this one
	 */
	private AMT[] children;
	/**
	 * Whether this AMT is a child<br>
	 * If it is - it shouldn't be rendered on its own
	 */
	private boolean isChild = false;

	//--- Mutable Properties ---//

	protected boolean visible;
	protected Vec3d off, scale;
	protected Vector3f color;
	protected Vec3d rot;
	protected float alpha;

	public AMT(String name, Vec3d originPos)
	{
		//final variables
		this.name = name;
		this.originPos = originPos;

		defaultize();
	}

	/**
	 * Renders this AMT
	 *
	 * @param tes Tesselator to be used
	 * @param buf BufferBuilder of tes
	 */
	public final void render(Tessellator tes, BufferBuilder buf)
	{
		if(!visible||alpha==0)
			return;

		GlStateManager.pushMatrix();

		preDraw();

		if(alpha!=1f)
			ShaderUtil.alpha_static(alpha);

		draw(tes, buf);

		if(children!=null)
			for(AMT child : children)
				child.render(tes, buf);

		if(alpha!=1f)
			ShaderUtil.releaseShader();

		GlStateManager.popMatrix();
	}

	/**
	 * Rotate (YZX), Translate, Scale
	 */
	protected void preDraw()
	{
		if(off!=null)
			GlStateManager.translate(-off.x, off.y, off.z);

		GlStateManager.translate(originPos.x, originPos.y, originPos.z);

		if(rot!=null)
		{
			GlStateManager.rotate((float)rot.y, 0, 1, 0);
			GlStateManager.rotate((float)rot.z, 0, 0, 1);
			GlStateManager.rotate((float)-rot.x, 1, 0, 0);
		}

		GlStateManager.translate(-originPos.x, -originPos.y, -originPos.z);

		if(scale!=null)
			GlStateManager.scale(scale.x, scale.y, scale.z);

	}

	/**
	 * Draw this AMT
	 */
	protected abstract void draw(Tessellator tes, BufferBuilder buf);

	/**
	 * Set all variables to default values
	 */
	public void defaultize()
	{
		visible = true;
		off = scale = rot = null;
		color = null;
		alpha = 1f;
	}

	/**
	 * Remove GL CallLists so they won't waste space, when not needed
	 */
	public abstract void disposeOf();

	final void setChildren(AMT[] children)
	{
		this.children = children;
	}

	/**
	 * @return the children of this AMT
	 */
	public final ArrayList<AMT> getChildrenRecursive()
	{
		return getChildrenRecursive(new ArrayList<>());
	}

	/**
	 * Internal method for collection
	 */
	private ArrayList<AMT> getChildrenRecursive(@Nonnull ArrayList<AMT> list)
	{
		list.add(this);
		if(children!=null)
			for(AMT child : children)
				child.getChildrenRecursive(list);

		return list;
	}

	final AMT setChild()
	{
		this.isChild = true;
		return this;
	}

	final boolean isChild()
	{
		return isChild;
	}
}
