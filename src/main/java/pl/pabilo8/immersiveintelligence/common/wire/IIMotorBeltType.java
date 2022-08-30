package pl.pabilo8.immersiveintelligence.common.wire;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.api.rotary.IModelMotorBelt;
import pl.pabilo8.immersiveintelligence.api.rotary.MotorBeltType;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.mechanical.ItemIIMotorBelt.MotorBelt;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 29-12-2019
 */
public class IIMotorBeltType extends MotorBeltType
{
	static ArrayList<MotorBelt> belts = new ArrayList<>();
	MotorBelt type;

	public IIMotorBeltType(MotorBelt type)
	{
		this.type = type;
		belts.add(type);
	}

	@Override
	public ResourceLocation getTexture()
	{
		return type.res;
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
	public int getWidth()
	{
		return type.width;
	}

	@Override
	public int getThickness()
	{
		return type.thickness;
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

	@Override
	public IModelMotorBelt getModel()
	{
		return type.getModel();
	}

	@Override
	public ItemStack getWireCoil()
	{
		return new ItemStack(IIContent.itemMotorBelt, 1, type.ordinal());
	}
}
