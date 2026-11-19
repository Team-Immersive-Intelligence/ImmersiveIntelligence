package pl.pabilo8.immersiveintelligence.common.block.simple.tileentity.backpack;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ITileDrop;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIBackpack.BackpackType;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.raytracer.AxisAlignedFacingBB;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional.FacingLimitation;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional.FacingSettings;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectionalConnectable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Provides a backpack with one wire socket.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 18.11.2026
 */
public abstract class TileEntityBackpack extends TileEntityIIDirectionalConnectable implements IBlockBounds, IDirectionalTile, IPlayerInteraction, ITileDrop
{
	private static final FacingSettings FACING_SETTINGS = new FacingSettings(FacingLimitation.HORIZONTAL);
	private static final AxisAlignedFacingBB BOUNDS = new AxisAlignedFacingBB(0.0625f, 0, 0.15625f, 0.9375f, 0.875f, 0.84375f);

	@SyncNBT(events = SyncEvents.TILE_CUSTOM1)
	public ItemStack backpack;

	public TileEntityBackpack(BackpackType type)
	{
		this.backpack = new ItemStack(type.getItem());
	}

	@Nonnull
	@Override
	protected final FacingSettings getFacingSettings()
	{
		return FACING_SETTINGS;
	}

	@Override
	public boolean acceptsWireType(WireType category)
	{
		return false;
	}

	@Override
	public final ITextComponent getDisplayName()
	{
		return new TextComponentString(backpack.getDisplayName());
	}

	@Override
	public final boolean isRelay()
	{
		return false;
	}

	@Override
	public final Vec3d getConnectionOffset(Connection connection)
	{
		return new Vec3d(0.5, 0.875+connection.cableType.getRenderDiameter()/2, 0.5);
	}

	@Nonnull
	@Override
	public float[] getBlockBounds()
	{
		return BOUNDS.getFacingBounds(getFacing(), false);
	}

	@Override
	public boolean preventInventoryDrop()
	{
		return true;
	}

	@Nonnull
	@Override
	public ItemStack getTileDrop(@Nullable EntityPlayer player, @Nonnull IBlockState state)
	{
		return backpack.copy();
	}

	@Nonnull
	@Override
	public ItemStack getPickBlock(@Nullable EntityPlayer player, @Nonnull IBlockState state, @Nonnull RayTraceResult hit)
	{
		return new ItemStack(backpack.getItem());
	}

	@Override
	public void readOnPlacement(@Nullable EntityLivingBase placer, ItemStack stack)
	{
		if(stack.getItem()==backpack.getItem())
		{
			ItemStack placed = stack.copy();
			placed.setCount(1);
			this.backpack = placed;
			updateTileForEvent(SyncEvents.TILE_CUSTOM1);
		}
	}

	@Override
	public boolean interact(@Nonnull EnumFacing side, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		return onInteract(side, player, hand, heldItem);
	}

	boolean onInteract(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem)
	{
		if(!player.isSneaking()||!heldItem.isEmpty())
			return false;

		World world = getWorld();
		BlockPos pos = getPos();
		if(!world.isRemote&&world.isBlockModifiable(player, pos)&&player.canPlayerEdit(pos, side, heldItem))
		{
			ItemStack drop = getTileDrop(player, world.getBlockState(pos));
			if(world.setBlockToAir(pos))
				ItemHandlerHelper.giveItemToPlayer(player, drop);
		}
		return true;
	}
}
