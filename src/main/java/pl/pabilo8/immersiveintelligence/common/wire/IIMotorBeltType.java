package pl.pabilo8.immersiveintelligence.common.wire;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.rotary.IIRotaryUtils;
import pl.pabilo8.immersiveintelligence.api.rotary.MotorBeltType;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.item.mechanical.ItemIIMotorBelt.MotorBelt;
import pl.pabilo8.immersiveintelligence.common.util.AdvancedSounds.MultiSound;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8
 * @since 29-12-2019
 */
public class IIMotorBeltType extends MotorBeltType
{
	private final MotorBelt type;

	public IIMotorBeltType(MotorBelt type)
	{
		this.type = type;
	}

	@Override
	public String getBeltCategory()
	{
		return type.category;
	}

	@Override
	public String getName()
	{
		return type.getName();
	}

	@Override
	public int getLength()
	{
		return type.length;
	}

	@Override
	public int getMaxTorque()
	{
		return type.maxTorque;
	}

	@Override
	public float getTorqueLoss()
	{
		return type.torqueLoss;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ResourceLocation getModelPath()
	{
		return IIReference.RES_BLOCK_MODEL.with("motor_belt/%1$s_%2$s")
				.with(getBeltCategory().toLowerCase(), getName().toLowerCase())
				.withExtension(ResLoc.EXT_OBJ);
	}

	@Override
	public SoundEvent getBreakSound()
	{
		return getBeltCategory().equals(IIRotaryUtils.BELT_CATEGORY)?IISounds.motorBeltBreak: IISounds.trackBreak;
	}

	@Override
	public MultiSound getLoopSound()
	{
		return getBeltCategory().equals(IIRotaryUtils.BELT_CATEGORY)?IISounds.motorBeltRunning: IISounds.trackRunning;
	}

	@Override
	public ItemStack getBrokenDrop()
	{
		return type.dropItem.getExampleStack();
	}

	@Override
	public int getWidth()
	{
		return type.width;
	}

	@Override
	public ItemStack getWireCoil()
	{
		return new ItemStack(IIContent.itemMotorBelt, 1, type.ordinal());
	}
}
