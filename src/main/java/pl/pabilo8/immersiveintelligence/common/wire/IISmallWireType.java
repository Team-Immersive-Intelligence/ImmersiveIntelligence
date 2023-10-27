package pl.pabilo8.immersiveintelligence.common.wire;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIISmallWireCoil.SmallWires;

/**
 * @author Pabilo8
 * @since 2019-05-31
 */
public class IISmallWireType extends WireType
{
	private final SmallWires wire;

	public IISmallWireType(SmallWires wire)
	{
		this.wire = wire;
	}

	/**
	 * In this case, this does not return the loss RATIO but the loss PER BLOCK
	 */
	@Override
	public double getLossRatio()
	{
		return 0;
	}

	@Override
	public int getTransferRate()
	{
		return 0;
	}

	@Override
	public int getColour(Connection connection)
	{
		return wire.colour;
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
	public int getMaxLength()
	{
		return wire.length;
	}

	@Override
	public ItemStack getWireCoil()
	{
		return IIContent.itemSmallWireCoil.getStack(wire);
	}

	@Override
	public String getUniqueName()
	{
		return "small_"+wire.getName();
	}

	@Override
	public double getRenderDiameter()
	{
		return .03125;
	}

	@Override
	public boolean isEnergyWire()
	{
		return false;
	}

	@Override
	public double getDamageRadius()
	{
		return 0;
	}

	@Override
	public boolean canCauseDamage()
	{
		return false;
	}
}
