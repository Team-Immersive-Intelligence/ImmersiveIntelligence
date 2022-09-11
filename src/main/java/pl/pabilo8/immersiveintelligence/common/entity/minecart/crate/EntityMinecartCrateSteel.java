package pl.pabilo8.immersiveintelligence.common.entity.minecart.crate;

import net.minecraft.block.Block;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.utils.minecart.EntityMinecartCrateBase;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice.IIBlockTypes_MetalDevice;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class EntityMinecartCrateSteel extends EntityMinecartCrateBase
{
	public EntityMinecartCrateSteel(World worldIn)
	{
		super(worldIn);
	}

	public EntityMinecartCrateSteel(World worldIn, Vec3d vv)
	{
		super(worldIn, vv);
	}

	@Override
	protected Block getCarriedBlock()
	{
		return IIContent.blockMetalDevice;
	}

	@Override
	protected int getBlockMetaID()
	{
		return IIBlockTypes_MetalDevice.METAL_CRATE.getMeta();
	}

	@Override
	public boolean isIECrate()
	{
		return true;
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory()
	{
		return 27;
	}

	@Override
	public String getGuiID()
	{
		return "intelli"+":s_crt";
	}
}
