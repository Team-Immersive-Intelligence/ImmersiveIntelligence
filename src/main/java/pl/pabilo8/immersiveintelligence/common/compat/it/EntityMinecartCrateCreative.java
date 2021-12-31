package pl.pabilo8.immersiveintelligence.common.compat.it;

import mctmods.immersivetechnology.common.ITContent;
import mctmods.immersivetechnology.common.blocks.wooden.types.BlockType_WoodenCrate;
import net.minecraft.block.Block;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.utils.minecart.EntityMinecartCrateBase;

/**
 * @author Pabilo8
 * @since 07.11.2021
 */
public class EntityMinecartCrateCreative extends EntityMinecartCrateBase
{
	public EntityMinecartCrateCreative(World worldIn)
	{
		super(worldIn);
	}

	public EntityMinecartCrateCreative(World worldIn, Vec3d vv)
	{
		super(worldIn, vv);
	}

	@Override
	protected Block getCarriedBlock()
	{
		return ITContent.blockWoodenCrate;
	}

	@Override
	protected int getBlockMetaID()
	{
		return BlockType_WoodenCrate.CRATE.getMeta();
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
		return 1;
	}

	@Override
	public String getGuiID()
	{
		return "it"+":ct_crt";
	}
}
