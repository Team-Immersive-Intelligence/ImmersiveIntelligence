package pl.pabilo8.immersiveintelligence.api.utils.minecart;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 07.11.2021
 */
public abstract class EntityMinecartCrateBase extends EntityMinecartContainer implements IMinecartBlockPickable
{
	public EntityMinecartCrateBase(World worldIn)
	{
		super(worldIn);
	}

	public EntityMinecartCrateBase(World worldIn, Vec3d vv)
	{
		super(worldIn, vv.x, vv.y, vv.z);
	}

	/**
	 * Override, when you need a custom ItemHandler
	 */
	@Override
	protected void entityInit()
	{
		super.entityInit();
		itemHandler = new net.minecraftforge.items.wrapper.InvWrapper(this)
		{
			@Override
			public boolean isItemValid(int slot, @Nonnull ItemStack stack)
			{
				return !isIECrate()||IEApi.isAllowedInCrate(stack);
			}
		};
	}

	protected abstract Block getCarriedBlock();

	protected abstract int getBlockMetaID();

	public abstract boolean isIECrate();

	@Override
	public Type getType()
	{
		return Type.CHEST;
	}

	@Override
	public IBlockState getDefaultDisplayTile()
	{
		return getCarriedBlock().getStateFromMeta(getBlockMetaID());
	}

	@Override
	public int getDefaultDisplayTileOffset()
	{
		return 8;
	}

	@Override
	public void setDead()
	{
		this.isDead = true;
	}

	@Override
	public void killMinecart(DamageSource source)
	{
		if(!world.isRemote&&this.world.getGameRules().getBoolean("doEntityDrops"))
		{
			ItemStack cart = new ItemStack(Items.MINECART, 1);
			ItemStack drop2 = new ItemStack(getCarriedBlock(), 1, getBlockMetaID());
			NBTTagCompound nbt = new NBTTagCompound();

			if(this.hasCustomName())
				nbt.setString("name", getCustomNameTag());

			writeInventoryNBT(nbt);
			drop2.setTagCompound(nbt);

			entityDropItem(drop2, 1);
			entityDropItem(cart, 1);
			this.setDead();
		}
	}

	@Override
	public Tuple<ItemStack, EntityMinecart> getBlockForPickup()
	{
		ItemStack drop2 = new ItemStack(getCarriedBlock(), 1, getBlockMetaID());
		NBTTagCompound nbt = new NBTTagCompound();

		writeInventoryNBT(nbt);
		drop2.setTagCompound(nbt);

		NBTTagCompound nbt2 = new NBTTagCompound();
		this.writeEntityToNBT(nbt2);

		this.setDead();

		EntityMinecart ent = new EntityMinecartEmpty(this.world);
		ent.readFromNBT(nbt2);
		world.spawnEntity(ent);
		ent.readFromNBT(nbt2);

		return new Tuple<>(drop2, ent);
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target)
	{
		ItemStack stack = new ItemStack(getCarriedBlock(), 1, getBlockMetaID());
		NBTTagCompound nbt = new NBTTagCompound();

		if(this.hasCustomName())
			nbt.setString("name", getCustomNameTag());

		writeInventoryNBT(nbt);
		stack.setTagCompound(nbt);

		return stack;
	}

	@Override
	public void setMinecartBlock(ItemStack stack)
	{
		if(stack.hasTagCompound())
		{
			assert stack.getTagCompound()!=null;
			readInventoryNBT(stack.getTagCompound());

			String s = ItemNBTHelper.getString(stack, "name");
			if(!s.isEmpty())
				setCustomNameTag(s);
		}
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		this.addLoot(playerIn);
		return new ContainerChest(playerInventory, this, playerIn);
	}

	private void writeInventoryNBT(NBTTagCompound nbt)
	{
		NBTTagList invList = new NBTTagList();
		for(int i = 0; i < itemHandler.getSlots(); i++)
		{
			ItemStack stack = itemHandler.extractItem(i, itemHandler.getStackInSlot(i).getCount(), true);
			if(!stack.isEmpty())
			{
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setByte("Slot", (byte)i);
				stack.writeToNBT(itemTag);
				invList.appendTag(itemTag);
			}
		}
		nbt.setTag("inventory", invList);
	}

	void readInventoryNBT(NBTTagCompound nbt)
	{
		NonNullList<ItemStack> stacks = Utils.readInventory(nbt.getTagList("inventory", 10), getSizeInventory());
		for(int i = 0; i < stacks.size(); i += 1)
		{
			itemHandler.extractItem(i, Integer.MAX_VALUE, false);
			itemHandler.insertItem(i, stacks.get(i), false);
		}
	}
}
