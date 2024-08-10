package pl.pabilo8.immersiveintelligence.api.rotary;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.util.AdvancedSounds.MultiSound;

import static pl.pabilo8.immersiveintelligence.api.rotary.IIRotaryUtils.BELT_GENERAL_CATEGORY;

/**
 * Base class for an II motor belt. Extend to make your own.
 *
 * @author Pabilo8
 * @updated 02.08.2024
 * @ii-approved 0.3.1
 * @since 26-12-2019
 */
public abstract class MotorBeltType extends WireType
{
	/**
	 * @return <b>Wire</b> category, not to be confused with {@link WireType#getCategory()}
	 */
	public String getCategory()
	{
		return BELT_GENERAL_CATEGORY;
	}

	/**
	 * @return Category of the belt, either {@link IIRotaryUtils#BELT_CATEGORY} or {@link IIRotaryUtils#TRACK_CATEGORY}
	 */
	public abstract String getBeltCategory();

	/**
	 * @return Unique name of the motor belt
	 */
	public abstract String getName();

	@Override
	public String getUniqueName()
	{
		return getName();
	}

	/**
	 * @return Maximum length in blocks
	 */
	public abstract int getLength();

	/**
	 * @return Maximal Torque
	 */
	public abstract int getMaxTorque();

	/**
	 * @return Torque loss
	 */
	public abstract float getTorqueLoss();

	/**
	 * @return Motor belt's model
	 */
	@SideOnly(Side.CLIENT)
	public abstract ResourceLocation getModelPath();

	/**
	 * @return Sound played when the belt is broken due to overload
	 */
	public abstract SoundEvent getBreakSound();

	/**
	 * @return Sound played when the belt is broken due to overload
	 */
	public abstract MultiSound getLoopSound();

	/**
	 * @return Item dropped when the belt is broken
	 */
	public abstract ItemStack getBrokenDrop();

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
		return getWidth()/32f;
	}

	public abstract int getWidth();

	@Override
	public boolean canCauseDamage()
	{
		return true;
	}

	@Override
	public int getMaxLength()
	{
		return getLength();
	}
}
