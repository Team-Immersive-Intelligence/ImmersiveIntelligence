package pl.pabilo8.immersiveintelligence.common.block.mines.tileentity;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ITileDrop;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoFactory;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIConnectable;

import javax.annotation.Nullable;

/**
 * Stores mine ammunition and controls arming and detonation.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 04.10.2026
 * @since 05.03.2024
 */
public abstract class TileEntityMineBase extends TileEntityIIConnectable implements IPlayerInteraction, ITileDrop
{
	protected ItemStack mineStack = ItemStack.EMPTY;
	protected boolean armed = true;
	private boolean detonating = false;

	/**
	 * Detonates an armed mine on the server.
	 */
	public void explode()
	{
		if(world==null||world.isRemote||!armed||detonating)
			return;
		detonating = true;
		if(!mineStack.isEmpty())
			new AmmoFactory<>(world)
					.setStack(mineStack)
					.setPosition(pos)
					.setDirection(EnumFacing.UP)
					.detonate();
		world.setBlockToAir(this.getPos());
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbtTagCompound, boolean b)
	{
		super.readCustomNBT(nbtTagCompound, b);
		armed = !nbtTagCompound.hasKey("armed")||nbtTagCompound.getBoolean("armed");
		mineStack = ItemStack.EMPTY;
		this.readOnPlacement(null, new ItemStack(nbtTagCompound.getCompoundTag("mineStack")));
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbtTagCompound, boolean b)
	{
		super.writeCustomNBT(nbtTagCompound, b);
		nbtTagCompound.setBoolean("armed", armed);
		nbtTagCompound.setTag("mineStack", mineStack.serializeNBT());
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		if(armed&&heldItem.getItem().getToolClasses(heldItem).contains(Lib.TOOL_WIRECUTTER))
		{
			if(world.isRemote)
				return true;
			heldItem.damageItem(8, player);
			world.playSound(pos.getX(), pos.getY()+1, pos.getZ(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1f, 1f, false);

			armed = false;

			markDirty();
			IBlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 3);
			return true;
		}
		return false;
	}

	@Override
	public void readOnPlacement(EntityLivingBase placer, ItemStack stack)
	{
		Item item = stack.getItem();
		if(item instanceof IAmmoTypeItem)
			this.mineStack = Utils.copyStackWithAmount(stack, 1);
	}

	@Override
	public ItemStack getTileDrop(@Nullable EntityPlayer player, IBlockState state)
	{
		return mineStack;
	}

	@Override
	public NonNullList<ItemStack> getTileDrops(@Nullable EntityPlayer player, IBlockState state)
	{
		explode();
		return NonNullList.from(armed?ItemStack.EMPTY: mineStack);
	}

	public ItemStack getMineStack()
	{
		return mineStack;
	}

	@Override
	public boolean acceptsWireType(WireType cableType)
	{
		return false;
	}

	@Override
	public boolean isRelay()
	{
		return false;
	}

	@Override
	public boolean allowEnergyToPass(@Nullable Connection connection)
	{
		return false;
	}
}
