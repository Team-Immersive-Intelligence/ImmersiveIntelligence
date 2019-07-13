package pl.pabilo8.immersiveintelligence.common.wire;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Wires;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;

/**
 * Created by Pabilo8 on 2019-05-31.
 */
public class IIWireType extends WireType
{
	public static IIWireType DATA;

	final int ordinal;

	public IIWireType(int ordinal)
	{
		super();
		this.ordinal = ordinal;
	}

	public static void init()
	{
		DATA = new IIWireType(0);
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
		return new ItemStack(CommonProxy.item_data_wire_coil, 1, ordinal);
	}

	@Override
	public String getUniqueName()
	{
		return "DATA";
	}

	@Override
	public double getRenderDiameter()
	{
		return 0.09375;
	}

	@Override
	public boolean isEnergyWire()
	{
		return false;
	}

	@Override
	public String getCategory()
	{
		return "DATA";
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
