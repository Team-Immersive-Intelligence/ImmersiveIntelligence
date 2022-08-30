package pl.pabilo8.immersiveintelligence.common.entity.minecart.crate;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockTypes_WoodenDevice0;
import net.minecraft.block.Block;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.utils.minecart.EntityMinecartCrateBase;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class EntityMinecartCrateReinforced extends EntityMinecartCrateBase
{
	public EntityMinecartCrateReinforced(World worldIn)
	{
		super(worldIn);
	}

	public EntityMinecartCrateReinforced(World worldIn, Vec3d vv)
	{
		super(worldIn, vv);
	}

	@Override
	protected Block getCarriedBlock()
	{
		return IEContent.blockWoodenDevice0;
	}

	@Override
	protected int getBlockMetaID()
	{
		return BlockTypes_WoodenDevice0.REINFORCED_CRATE.getMeta();
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
		return "intelli"+":rf_crt";
	}
}
