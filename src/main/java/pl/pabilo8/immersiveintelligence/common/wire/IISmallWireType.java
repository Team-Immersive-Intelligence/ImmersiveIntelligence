package pl.pabilo8.immersiveintelligence.common.wire;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Wires;
import pl.pabilo8.immersiveintelligence.common.IIContent;

/**
 * @author Pabilo8
 * @since 2019-05-31
 */
public class IISmallWireType extends WireType
{
	public static IISmallWireType SMALL_REDSTONE;
	public static IISmallWireType SMALL_DATA;

	public String name;
	public int meta;

	public IISmallWireType(String name, int meta)
	{
		this.name = name;
		this.meta = meta;
	}

	public static void init()
	{
		SMALL_REDSTONE = new IISmallWireType("redstone", 0);
		SMALL_DATA = new IISmallWireType("data", 1);
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
		return Wires.dataWireColouration;
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
		return Wires.dataWireLength;
	}

	@Override
	public ItemStack getWireCoil()
	{
		return new ItemStack(IIContent.itemDataWireCoil, 1, meta);
	}

	@Override
	public String getUniqueName()
	{
		return name;
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
	public String getCategory()
	{
		return name.toUpperCase();
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
