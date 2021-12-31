package pl.pabilo8.immersiveintelligence.api.rotary;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static pl.pabilo8.immersiveintelligence.api.rotary.RotaryUtils.BELT_CATEGORY;

/**
 * @author Pabilo8
 * @since 26-12-2019
 */
public abstract class MotorBeltType extends WireType
{
	/**
	 * @return Resource location of the motor belt texture
	 */
	public abstract ResourceLocation getTexture();

	/**
	 * @return Category name, similar to how wires handle it
	 */
	public String getCategory()
	{
		return BELT_CATEGORY;
	}

	/**
	  @return Category name, similar to how wires handle it
	 */
	public abstract String getName();

	/**
	  @return Maximum length in blocks
	 */
	public abstract int getLength();

	/**
	  @return Width of the belt
	 */
	public abstract int getWidth();

	/**
	  @return Thickness of the belt
	 */
	public abstract int getThickness();

	/**
	  @return Maximal Torque
	 */
	public abstract int getMaxTorque();

	/**
	  @return Torque loss
	 */
	public abstract float getTorqueLoss();

	/**
	 * @return Motor belt's model
	 */
	@SideOnly(Side.CLIENT)
	public abstract IModelMotorBelt getModel();

	@Override
	public double getLossRatio()
	{
		return getTorqueLoss();
	}

	@Override
	public int getTransferRate()
	{
		return 0;
	}

	@Override
	public int getColour(Connection connection)
	{
		return 0xffffff;
	}

	@Override
	public double getSlack()
	{
		return 1.002;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getIcon(Connection connection)
	{
		return iconDefaultWire;
	}

	@Override
	public double getRenderDiameter()
	{
		return 0;
	}

	@Override
	public boolean isEnergyWire()
	{
		return false;
	}

	@Override
	public double getDamageRadius()
	{
		return getWidth()/16f;
	}


	@Override
	public boolean canCauseDamage()
	{
		return true;
	}

	@Override
	public String getUniqueName()
	{
		return getName();
	}

	@Override
	public int getMaxLength()
	{
		return getLength();
	}
}
