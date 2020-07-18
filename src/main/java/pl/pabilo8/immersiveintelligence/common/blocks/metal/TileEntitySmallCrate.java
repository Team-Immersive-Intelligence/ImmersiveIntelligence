package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IComparatorOverride;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ITileDrop;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
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
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_SmallCrate;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class TileEntitySmallCrate extends TileEntityIEBase implements IIEInventory, IGuiTile, ITileDrop, IComparatorOverride, ILootContainer, IDirectionalTile
{
	NonNullList<ItemStack> inventory = NonNullList.withSize(27, ItemStack.EMPTY);
	public ResourceLocation lootTable;
	public String name;
	private NBTTagList enchantments;
	public EnumFacing facing = EnumFacing.NORTH;

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
				inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 27);
		}
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

	@Override
	@Nullable
	public ITextComponent getDisplayName()
	{
		return name!=null?new TextComponentString(name): new TextComponentTranslation(ImmersiveIntelligence.proxy.BLOCK_KEY+"small_crate."+IIBlockTypes_SmallCrate.values()[getBlockMetadata()].getName()+".name");
	}

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_SMALL_CRATE.ordinal();
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
				contextBuilder.withLuck(player.getLuck()).withPlayer(player);
			LootContext context = contextBuilder.build();
			Random rand = new Random();

			List<ItemStack> list = loottable.generateLootForPools(rand, context);
			List<Integer> listSlots = Lists.newArrayList();
			for(int i = 0; i < inventory.size(); i++)
				if(inventory.get(i).isEmpty())
					listSlots.add(Integer.valueOf(i));
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
		return IEApi.isAllowedInCrate(stack);
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

	IItemHandler insertionHandler = new IEInventoryHandler(27, this);

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
		return false;
	}

	@Override
	public boolean canRotate(EnumFacing axis)
	{
		return false;
	}
}
