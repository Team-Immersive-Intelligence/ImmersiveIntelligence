package pl.pabilo8.immersiveintelligence.client.util.amt;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil.Shaders;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

	//--- Basic Mutable Properties ---//
	/**
	 * Whether this AMT should be rendered
	 */
	protected boolean visible;
	/**
	 * Offset (XYZ), Scale (XYZ)
	 */
	protected Vec3d off, scale;
	/**
	 * Rotation (XYZ) with values in degrees
	 */
	protected Vec3d rot;

	//--- Extended Mutable Properties ---//
	/**
	 * Current shader type and values passed to it.<br>
	 * Only one shader is allowed to be used at the same time
	 */
	@Nullable
	protected Shaders shader;
	@Nonnull
	protected Float[] shaderValue;
	/**
	 * Custom Property Value, used by some AMT components
	 */
	protected float property;

	public AMT(String name, IIModelHeader header)
	{
		this(name, header.getOffset(name));
	}

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
		if(!visible)
			return;

		GlStateManager.pushMatrix();

		//Translation, Rotation, Scaling, overridden by AMT subclasses
		preDraw();

		//Draw current element
		preShaders();
		draw(tes, buf);
		postShaders();

		//Render child elements, shader values should be passed individually to them
		if(children!=null)
			for(AMT child : children)
				child.render(tes, buf);

		GlStateManager.popMatrix();
	}

	/**
	 * Used to enable shaders before drawing this AMT
	 */
	private void preShaders()
	{
		if(shader!=null)
			ShaderUtil.useShader(shader, shaderValue);
	}

	/**
	 * Used to enable shaders for drawing this AMT
	 */
	private void postShaders()
	{
		if(shader!=null)
			ShaderUtil.releaseShader();
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
		shader = null;
		shaderValue = new Float[0];
		property = 0f;

		if(children!=null)
			for(AMT mod : children)
				mod.defaultize();
	}

	/**
	 * Remove GL CallLists so they won't waste space, when not needed
	 */
	public abstract void disposeOf();

	public final void setChildren(AMT[] children)
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
