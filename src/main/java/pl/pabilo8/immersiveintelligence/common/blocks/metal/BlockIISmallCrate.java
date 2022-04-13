package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_SmallCrate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class BlockIISmallCrate extends BlockIITileProvider<IIBlockTypes_SmallCrate>
{
	public BlockIISmallCrate()
	{
		super("small_crate", Material.IRON, PropertyEnum.create("type", IIBlockTypes_SmallCrate.class), ItemBlockIEBase.class, IEProperties.FACING_HORIZONTAL);
		setHardness(3.0F);
		setResistance(15.0F);
		lightOpacity = 0;
		setOpaque(false);
		this.setAllNotNormalBlock();
	}

	@Override
	public boolean useCustomStateMapper()
	{
		return true;
	}

	@Override
	public String getCustomStateMapping(int meta, boolean itemBlock)
	{
		return null;
	}

	@Nonnull
	@SuppressWarnings("Deprecation")
	@Override
	public Material getMaterial(@Nonnull IBlockState state)
	{
		return disableStats().getMetaFromState(state) > 2?Material.IRON: Material.WOOD;
	}

	@Override
	public TileEntity createBasicTE(World world, IIBlockTypes_SmallCrate type)
	{
		return new TileEntitySmallCrate();
	}

	@Override
	public boolean allowHammerHarvest(@Nonnull IBlockState state)
	{
		return true;
	}

	@Override
	public boolean canIEBlockBePlaced(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState newState, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ, @Nonnull EntityPlayer player, @Nonnull ItemStack stack)
	{
		return true;
	}
}