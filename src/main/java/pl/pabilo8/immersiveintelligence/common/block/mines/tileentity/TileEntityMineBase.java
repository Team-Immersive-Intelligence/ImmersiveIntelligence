package pl.pabilo8.immersiveintelligence.common.block.mines.tileentity;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
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

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 05.03.2024
 */
public abstract class TileEntityMineBase extends TileEntityImmersiveConnectable implements IPlayerInteraction, ITileDrop
{
	protected ItemStack mineStack = ItemStack.EMPTY;
	protected boolean armed = true;

	public void explode()
	{
		if(!armed)
			return;
		if(!world.isRemote&&!mineStack.isEmpty())
			new AmmoFactory<>(world)
					.setStack(mineStack)
					.setPosition(pos)
					.setDirection(EnumFacing.UP)
					.detonate();
		world.setBlockToAir(this.getPos());
	}

	public void readCustomNBT(NBTTagCompound nbtTagCompound, boolean b)
	{
		armed = nbtTagCompound.getBoolean("armed");
		this.readOnPlacement(null, new ItemStack(nbtTagCompound.getCompoundTag("mineStack")));
	}

	public void writeCustomNBT(NBTTagCompound nbtTagCompound, boolean b)
	{
		nbtTagCompound.setBoolean("armed", armed);
		nbtTagCompound.setTag("mineStack", mineStack.serializeNBT());
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		if(armed&&heldItem.getItem().getToolClasses(heldItem).contains(Lib.TOOL_WIRECUTTER))
		{
			heldItem.damageItem(8, player);
			world.playSound(pos.getX(), pos.getY()+1, pos.getZ(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1f, 1f, false);

			armed = false;

			// Update the NBT tag to reflect the disarmed state
			NBTTagCompound nbt = new NBTTagCompound();
			this.writeCustomNBT(nbt, false);  // This method will update the NBT with the current armed status

			return true;  // Return true to indicate the interaction was successful
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
}
