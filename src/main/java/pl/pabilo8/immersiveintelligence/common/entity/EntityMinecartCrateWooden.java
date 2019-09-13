package pl.pabilo8.immersiveintelligence.common.entity;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockTypes_WoodenDevice0;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.IMinecartBlockPickable;

/**
 * Created by Pabilo8 on 2019-06-01.
 */
public class EntityMinecartCrateWooden extends EntityMinecartContainer implements IMinecartBlockPickable
{
	public EntityMinecartCrateWooden(World worldIn)
	{
		super(worldIn);
	}

	public EntityMinecartCrateWooden(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	public static void registerFixesMinecartChest(DataFixer fixer)
	{
		EntityMinecartContainer.addDataFixers(fixer, net.minecraft.entity.item.EntityMinecartChest.class);
	}

	@Override
	public void killMinecart(DamageSource source)
	{
		if(!world.isRemote&&this.world.getGameRules().getBoolean("doEntityDrops"))
		{
			ItemStack cart = new ItemStack(Items.MINECART, 1);
			Item drop = Item.getItemFromBlock(IEContent.blockWoodenDevice0);
			ItemStack drop2 = new ItemStack(drop, 1, BlockTypes_WoodenDevice0.CRATE.getMeta());
			NBTTagCompound nbt = new NBTTagCompound();

			NBTTagList invList = new NBTTagList();

			for(int i = 0; i < itemHandler.getSlots(); i++)
			{
				ItemStack stack = itemHandler.extractItem(i, itemHandler.getStackInSlot(i).getCount(), false);

				if(!stack.isEmpty())
				{
					NBTTagCompound itemTag = new NBTTagCompound();
					itemTag.setByte("Slot", (byte)i);
					stack.writeToNBT(itemTag);
					invList.appendTag(itemTag);
				}
			}

			if(this.hasCustomName())
			{
				nbt.setString("name", getCustomNameTag());
			}

			nbt.setTag("inventory", invList);
			drop2.setTagCompound(nbt);

			entityDropItem(drop2, 1);
			entityDropItem(cart, 1);
			this.setDead();
		}
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
	public EntityMinecart.Type getType()
	{
		return EntityMinecart.Type.CHEST;
	}

	@Override
	public IBlockState getDefaultDisplayTile()
	{
		return IEContent.blockWoodenDevice0.getStateFromMeta(BlockTypes_WoodenDevice0.CRATE.getMeta());
	}

	@Override
	public int getDefaultDisplayTileOffset()
	{
		return 8;
	}

	@Override
	public String getGuiID()
	{
		return ImmersiveIntelligence.MODID+":wooden_crate";
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		this.addLoot(playerIn);
		return new ContainerChest(playerInventory, this, playerIn);
	}

	@Override
	public void setDead()
	{
		this.isDead = true;
	}

	@Override
	public ItemStack getBlockForPickup()
	{
		Item drop = Item.getItemFromBlock(IEContent.blockWoodenDevice0);
		ItemStack drop2 = new ItemStack(drop, 1, BlockTypes_WoodenDevice0.CRATE.getMeta());
		NBTTagCompound nbt = new NBTTagCompound();

		NBTTagList invList = new NBTTagList();

		for(int i = 0; i < itemHandler.getSlots(); i++)
		{
			ItemStack stack = itemHandler.extractItem(i, itemHandler.getStackInSlot(i).getCount(), false);

			if(!stack.isEmpty())
			{
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setByte("Slot", (byte)i);
				stack.writeToNBT(itemTag);
				invList.appendTag(itemTag);
			}
		}

		if(this.hasCustomName())
		{
			nbt.setString("name", getCustomNameTag());
		}

		nbt.setTag("inventory", invList);
		drop2.setTagCompound(nbt);

		NBTTagCompound nbt2 = new NBTTagCompound();
		this.writeEntityToNBT(nbt2);

		this.setDead();

		EntityMinecart ent = new EntityMinecartEmpty(this.world);
		ent.readFromNBT(nbt2);
		world.spawnEntity(ent);
		ent.readFromNBT(nbt2);

		return drop2;
	}
}
