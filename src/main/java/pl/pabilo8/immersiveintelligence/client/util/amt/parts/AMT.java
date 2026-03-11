package pl.pabilo8.immersiveintelligence.client.util.amt.parts;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil.Shaders;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTRenderable;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 03.04.2022
 * <p>
 * An attempt to combine .obj and TMT<br>
 * Introducing the Advanced Model Technology(tm)<br>
 * Brace yourselves
 */
public abstract class AMT implements AMTRenderable
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
	 * Whether this AMT should be rendered
	 */
	protected boolean visible;
	/**
	 * Offset (XYZ), Scale (XYZ)
	 */
	protected Vec3d off, scale;

	//--- Basic Mutable Properties ---//
	/**
	 * Rotation (XYZ) with values in degrees
	 */
	protected Vec3d rot;
	/**
	 * Current shader type and values passed to it.<br>
	 * Only one shader is allowed to be used at the same time
	 */
	@Nullable
	protected Shaders shader;
	@Nonnull
	protected Float[] shaderValue;

	//--- Extended Mutable Properties ---//
	/**
	 * Custom Property Value, used by some AMT components
	 */
	protected float property;
	/**
	 * Children ModelThingies, rendered after this one
	 */
	private AMT[] children;
	/**
	 * Whether this AMT is a child<br>
	 * If it is - it shouldn't be rendered on its own
	 */
	private boolean isChild = false;

	public AMT(String name, AMTModelHeader header)
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
	@Override
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

	protected abstract void draw(Tessellator tes, BufferBuilder buf);

	@Override
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

	//--- Children ---//

	/**
	 * @return the children of this AMT
	 */
	public final ArrayList<AMT> getChildrenRecursive()
	{
		return getChildrenRecursive(new ArrayList<>());
	}

	/**
	 * @return children of this AMT
	 */
	@Nullable
	protected final AMT[] getChildren()
	{
		return children;
	}

	protected AMT withChildren(AMT... children)
	{
		setChildren(children);
		return this;
	}

	public final void setChildren(AMT... children)
	{
		this.children = children;
	}

	/**
	 * Internal method for collection
	 */
	private ArrayList<AMT> getChildrenRecursive(@Nonnull ArrayList<AMT> list)
	{
		list.add(this);
		AMT[] amts = getChildren();
		if(amts!=null)
			for(AMT child : amts)
				child.getChildrenRecursive(list);

		return list;
	}

	public final AMT setChild()
	{
		this.isChild = true;
		return this;
	}

	public final boolean isChild()
	{
		return isChild;
	}

	//--- Setters ---//

	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	public void setPosition(Vec3d off)
	{
		this.off = off;
	}

	public void setScale(Vec3d scale)
	{
		this.scale = scale;
	}

	public void setRotation(Vec3d rot)
	{
		this.rot = rot;
	}

	public void setShader(@Nullable Shaders shader, @Nonnull Float... shaderValue)
	{
		this.shader = shader;
		this.shaderValue = shaderValue;
	}

	public void setProperty(float property)
	{
		this.property = property;
	}

	@Override
	public void applyProperties(EasyNBT nbt)
	{
		nbt.checkSetBoolean("visible", this::setVisible);
		nbt.checkSetVec3D("position", this::setPosition);
		nbt.checkSetVec3D("scale", this::setScale);
		nbt.checkSetVec3D("rotation", this::setRotation);
		nbt.checkSetFloat("property", this::setProperty);
		if(nbt.hasKey("shader")&&nbt.hasKey("shader_value"))
		{
			Shaders shader = nbt.getEnum("shader", Shaders.class);
			Float[] shaderValues = nbt.streamList(NBTTagFloat.class, "shader_values")
					.map(NBTTagFloat::getFloat)
					.toArray(Float[]::new);
			this.setShader(shader, shaderValues);
		}
	}

	//--- Adders ---//

	public void addRotation(Vec3d rot)
	{
		this.rot = this.rot==null?rot: this.rot.add(rot);
	}

	public void addPosition(Vec3d pos)
	{
		this.off = this.off==null?pos: this.off.add(pos);
	}

	public void addScale(Vec3d scale)
	{
		this.scale = this.scale==null?scale: this.scale.add(scale);
	}
}
