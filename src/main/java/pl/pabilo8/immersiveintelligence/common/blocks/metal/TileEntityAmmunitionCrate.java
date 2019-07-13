package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.api.energy.IRotationAcceptor;
import blusunrize.immersiveengineering.api.tool.BulletHandler;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.*;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.items.ItemBullet;
import blusunrize.immersiveengineering.common.items.ItemRevolver;
import blusunrize.immersiveengineering.common.items.ItemSpeedloader;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageChestSync;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Pabilo8 on 2019-05-17.
 */
public class TileEntityAmmunitionCrate extends TileEntityIEBase implements IIEInventory, IGuiTile, ITileDrop, IComparatorOverride, ILootContainer, IPlayerInteraction, ITickable, IRotationAcceptor, IBlockBounds, IDirectionalTile
{
	NonNullList<ItemStack> inventory = NonNullList.withSize(38, ItemStack.EMPTY);

	public ResourceLocation lootTable;
	public String name;
	private NBTTagList enchantments;
	public boolean open = false;
	public float lidAngle = 0;

	public EnumFacing facing = EnumFacing.NORTH;

	@Override
	public void inputRotation(double rotation, @Nonnull EnumFacing side)
	{
		if(side!=this.facing.getOpposite())
			return;
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		if(nbt.hasKey("name"))
			this.name = nbt.getString("name");
		if(nbt.hasKey("enchantments"))
			this.enchantments = nbt.getTagList("enchantments", 10);
		if(!descPacket)
		{
			if(nbt.hasKey("lootTable", 8))
				this.lootTable = new ResourceLocation(nbt.getString("lootTable"));
			else
				inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 38);
		}
		open = !insertionHandler.getStackInSlot(37).isEmpty();
		facing = EnumFacing.getFront(nbt.getInteger("facing"));
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		if(this.name!=null)
			nbt.setString("name", this.name);
		if(this.enchantments!=null&&this.enchantments.tagCount() > 0)
			nbt.setTag("enchantments", this.enchantments);
		if(!descPacket)
		{
			if(lootTable!=null)
				nbt.setString("lootTable", lootTable.toString());
			else
				writeInv(nbt, false);
		}
		nbt.setInteger("facing", facing.ordinal());
	}

	public void writeInv(NBTTagCompound nbt, boolean toItem)
	{
		boolean write = false;
		NBTTagList invList = new NBTTagList();
		for(int i = 0; i < this.inventory.size(); i++)
			if(!this.inventory.get(i).isEmpty())
			{
				if(toItem)
					write = true;
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setByte("Slot", (byte)i);
				this.inventory.get(i).writeToNBT(itemTag);
				invList.appendTag(itemTag);
			}
		if(!toItem||write)
			nbt.setTag("inventory", invList);
	}

	/**
	 * Get the formatted ChatComponent that will be used for the sender's username in chat
	 */
	@Override
	@Nullable
	public ITextComponent getDisplayName()
	{
		return name!=null?new TextComponentString(name): new TextComponentTranslation("tile."+ImmersiveIntelligence.MODID+".metal_device.metal_crate.name");
	}

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_AMMUNITION_CRATE;
	}

	@Override
	public TileEntity getGuiMaster()
	{
		return this;
	}

	@Override
	public void onGuiOpened(EntityPlayer player, boolean clientside)
	{
		if(this.lootTable!=null&&!clientside)
		{
			LootTable loottable = this.world.getLootTableManager().getLootTableFromLocation(this.lootTable);
			this.lootTable = null;
			LootContext.Builder contextBuilder = new LootContext.Builder((WorldServer)this.world);
			if(player!=null)
				contextBuilder.withLuck(player.getLuck());
			LootContext context = contextBuilder.build();
			Random rand = new Random();

			List<ItemStack> list = loottable.generateLootForPools(rand, context);
			List<Integer> listSlots = Lists.newArrayList();
			for(int i = 0; i < inventory.size(); i++)
				if(inventory.get(i).isEmpty())
					listSlots.add(i);
			Collections.shuffle(listSlots, rand);
			if(listSlots.isEmpty())
				return;
			Utils.shuffleLootItems(list, listSlots.size(), rand);
			for(ItemStack itemstack : list)
			{
				int slot = listSlots.remove(listSlots.size()-1).intValue();
				inventory.set(slot, itemstack);
			}
			this.markDirty();
		}
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return inventory;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		if(slot < 20)
		{
			return stack.getItem() instanceof ItemBullet&&!(stack.isItemEqual(BulletHandler.emptyCasing)||stack.isItemEqual(BulletHandler.emptyShell));
		}
		if(slot >= 20&&slot < 29)
		{
			return stack.getItem() instanceof ItemBullet&&(stack.isItemEqual(BulletHandler.emptyCasing))||stack.isItemEqual(BulletHandler.emptyShell)&&!stack.hasTagCompound();
		}
		if(slot >= 29&&slot < 37)
		{
			return stack.getItem() instanceof ItemBullet&&!(stack.equals(BulletHandler.emptyCasing)||stack.equals(BulletHandler.emptyShell));
		}
		if(slot==37)
			return stack.getItem() instanceof ItemRevolver||stack.getItem() instanceof ItemSpeedloader;
		return false;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 64;
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{
		this.markDirty();
	}

	@Override
	public ItemStack getTileDrop(EntityPlayer player, IBlockState state)
	{
		ItemStack stack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
		NBTTagCompound tag = new NBTTagCompound();
		writeInv(tag, true);
		if(!tag.hasNoTags())
			stack.setTagCompound(tag);
		if(this.name!=null)
			stack.setStackDisplayName(this.name);
		if(enchantments!=null&&enchantments.tagCount() > 0)
			ItemNBTHelper.getTag(stack).setTag("ench", enchantments);
		return stack;
	}

	@Override
	public void readOnPlacement(EntityLivingBase placer, ItemStack stack)
	{
		if(stack.hasTagCompound())
		{
			readCustomNBT(stack.getTagCompound(), false);
			if(stack.hasDisplayName())
				this.name = stack.getDisplayName();
			enchantments = stack.getEnchantmentTagList();
		}
	}

	@Override
	public boolean preventInventoryDrop()
	{
		return true;
	}

	@Override
	public int getComparatorInputOverride()
	{
		return Utils.calcRedstoneFromInventory(this);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	IItemHandler insertionHandler = new IEInventoryHandler(38, this);

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T)insertionHandler;
		return super.getCapability(capability, facing);
	}

	@Override
	public ResourceLocation getLootTable()
	{
		return this.lootTable;
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{

		if(!world.isRemote)
		{
			if(inventory.get(37).isEmpty())
			{
				if(heldItem.isItemEqual(new ItemStack(IEContent.itemSpeedloader))||heldItem.isItemEqual(new ItemStack(IEContent.itemRevolver)))
				{
					heldItem = insertionHandler.insertItem(37, heldItem, false);
					EntityEquipmentSlot truehand = hand==EnumHand.MAIN_HAND?EntityEquipmentSlot.MAINHAND: EntityEquipmentSlot.OFFHAND;
					player.setItemStackToSlot(truehand, heldItem);
					player.inventoryContainer.detectAndSendChanges();
					//player.sendMessage(new TextComponentString(player.getName()+""));
					open = true;
					IIPacketHandler.INSTANCE.sendToDimension(new MessageChestSync(open, this.getPos()), this.world.provider.getDimension());
					return true;
				}
			}
			else
			{
				if(heldItem==ItemStack.EMPTY)
				{
					if(inventory.get(37).getItem() instanceof ItemRevolver)
						ItemNBTHelper.setInt(inventory.get(37), "reload", Math.round(30*Tools.ammunition_crate_resupply_time));

					IItemHandler bullethandler = inventory.get(37).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);

					for(int i = 0; i < 8; i++)
					{
						ItemStack s = bullethandler.extractItem(i, 1, false);
						if(s.isItemEqual(BulletHandler.emptyCasing)||s.isItemEqual(BulletHandler.emptyShell))
						{
							for(int j = 20; j < 29; j++)
							{
								s = insertionHandler.insertItem(j, s, false);
								if(s.isEmpty())
									break;
							}
						}
						else
						{
							for(int j = 0; j < 20; j++)
							{
								s = insertionHandler.insertItem(j, s, false);
								if(s.isEmpty())
									break;
							}
						}
					}

					for(int j = 0; j < 8; j++)
					{
						ItemStack required = inventory.get(29+j);
						if(!required.isEmpty())
						{
							ItemStack s = ItemStack.EMPTY;
							for(int k = 0; k < 20; k++)
							{
								if(!insertionHandler.getStackInSlot(k).isEmpty()&&insertionHandler.getStackInSlot(k).getTagCompound().equals(required.getTagCompound()))
								{
									s = insertionHandler.extractItem(k, 1, false);
									break;
								}
							}

							bullethandler.insertItem(j, s, false);
						}
					}

					player.addItemStackToInventory(insertionHandler.extractItem(37, 1, false));
					open = false;
					IIPacketHandler.INSTANCE.sendToDimension(new MessageChestSync(open, this.getPos()), this.world.provider.getDimension());
					//player.sendMessage(new TextComponentString("Noone is using the Ammunition Crate now!"));
					return true;
				}
			}
			return false;
		}
		return false;
	}


	@Override
	public void update()
	{
		if(open&&lidAngle < 1.5f)
			lidAngle = Math.min(lidAngle+0.2f, 1.5f);
		else if(!open&&lidAngle > 0f)
			lidAngle = Math.max(lidAngle-0.3f, 0f);
	}

	@Override
	public float[] getBlockBounds()
	{
		if(facing==EnumFacing.NORTH||facing==EnumFacing.SOUTH)
			return new float[]{0f, 0, .25f, 1f, .58f, .75f};
		else
			return new float[]{.25f, 0, 0f, .75f, .58f, 1f};
	}

	@Override
	public EnumFacing getFacing()
	{
		return facing;
	}

	@Override
	public void setFacing(EnumFacing facing)
	{
		this.facing = facing;
	}

	@Override
	public int getFacingLimitation()
	{
		return 2;
	}

	@Override
	public boolean mirrorFacingOnPlacement(EntityLivingBase placer)
	{
		return false;
	}

	@Override
	public boolean canHammerRotate(EnumFacing side, float hitX, float hitY, float hitZ, EntityLivingBase entity)
	{
		return !entity.isSneaking();
	}

	@Override
	public boolean canRotate(EnumFacing axis)
	{
		return !axis.getAxis().isVertical();
	}

}
