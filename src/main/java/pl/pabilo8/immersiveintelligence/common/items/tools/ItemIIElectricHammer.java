package pl.pabilo8.immersiveintelligence.common.items.tools;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.tool.ITool;
import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IConfigurableSides;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.EnergyHelper.IIEEnergyItem;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.RotationUtil;
import blusunrize.immersiveengineering.common.util.advancements.IEAdvancements;
import blusunrize.immersiveengineering.common.util.inventory.IEItemStackHandler;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.api.utils.IAdvancedMultiblockTileEntity;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

import static blusunrize.immersiveengineering.api.Lib.TOOL_HAMMER;

/**
 * Created by Pabilo8 on 2019-05-30.
 */
public class ItemIIElectricHammer extends ItemIIBase implements ITool, IIEEnergyItem
{
	public ItemIIElectricHammer()
	{
		super("electric_hammer", 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		String stored = this.getEnergyStored(stack)+"/"+this.getMaxEnergyStored(stack);
		list.add(I18n.format(Lib.DESC+"info.energyStored", stored));

		if(ItemNBTHelper.hasKey(stack, "multiblockPermission"))
		{
			NBTTagList tagList = stack.getTagCompound().getTagList("multiblockPermission", 8);
			String s = I18n.format(Lib.DESC_INFO+"multiblocksAllowed");
			if(!GuiScreen.isShiftKeyDown())
				list.add(s+" "+I18n.format(Lib.DESC_INFO+"holdShift"));
			else
			{
				list.add(s);
				for(int i = 0; i < tagList.tagCount(); i++)
					list.add(TextFormatting.DARK_GRAY+" "+I18n.format(Lib.DESC_INFO+"multiblocks."+tagList.getStringTagAt(i)));
			}
		}
		if(ItemNBTHelper.hasKey(stack, "multiblockInterdiction"))
		{
			NBTTagList tagList = stack.getTagCompound().getTagList("multiblockInterdiction", 8);
			String s = I18n.format(Lib.DESC_INFO+"multiblockForbidden");
			if(!GuiScreen.isShiftKeyDown())
				list.add(s+" "+I18n.format(Lib.DESC_INFO+"holdShift"));
			else
			{
				list.add(s);
				for(int i = 0; i < tagList.tagCount(); i++)
					list.add(TextFormatting.DARK_GRAY+" "+I18n.format(Lib.DESC_INFO+"multiblocks."+tagList.getStringTagAt(i)));
			}
		}
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		if(!stack.isEmpty())
			return new IEItemStackHandler(stack)
			{
				final EnergyHelper.ItemEnergyStorage energyStorage = new EnergyHelper.ItemEnergyStorage(stack);

				@Override
				public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing)
				{
					return capability==CapabilityEnergy.ENERGY||
							super.hasCapability(capability, facing);
				}

				@Override
				public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing)
				{
					if(capability==CapabilityEnergy.ENERGY)
						return (T)energyStorage;
					if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
						return (T)this;
					return null;
				}
			};
		return null;
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		TileEntity tile = world.getTileEntity(pos);
		ItemNBTHelper.setBoolean(stack, "forbidHammer", CommonProxy.tileEntitiesWeDontLike.stream().anyMatch(tileEntityPredicate -> tileEntityPredicate.test(tile)));
		String[] permittedMultiblocks = null;
		String[] interdictedMultiblocks = null;
		if(ItemNBTHelper.hasKey(stack, "multiblockPermission"))
		{
			NBTTagList list = stack.getTagCompound().getTagList("multiblockPermission", 8);
			permittedMultiblocks = new String[list.tagCount()];
			for(int i = 0; i < permittedMultiblocks.length; i++)
				permittedMultiblocks[i] = list.getStringTagAt(i);
		}
		if(ItemNBTHelper.hasKey(stack, "multiblockInterdiction"))
		{
			NBTTagList list = stack.getTagCompound().getTagList("multiblockInterdiction", 8);
			interdictedMultiblocks = new String[list.tagCount()];
			for(int i = 0; i < interdictedMultiblocks.length; i++)
				interdictedMultiblocks[i] = list.getStringTagAt(i);
		}
		for(MultiblockHandler.IMultiblock mb : MultiblockHandler.getMultiblocks())
			if(mb.isBlockTrigger(world.getBlockState(pos)))
			{
				boolean b = permittedMultiblocks==null;
				if(permittedMultiblocks!=null)
					for(String s : permittedMultiblocks)
						if(mb.getUniqueName().equalsIgnoreCase(s))
						{
							b = true;
							continue;
						}
				if(!b)
					break;
				if(interdictedMultiblocks!=null)
					for(String s : interdictedMultiblocks)
						if(mb.getUniqueName().equalsIgnoreCase(s))
						{
							b = false;
							continue;
						}
				if(!b)
					break;
				if(MultiblockHandler.fireMultiblockFormationEventPre(player, mb, pos, stack).isCanceled())
					continue;
				if(mb.createStructure(world, pos, side, player))
				{
					if(player instanceof EntityPlayerMP)
						IEAdvancements.TRIGGER_MULTIBLOCK.trigger((EntityPlayerMP)player, mb, stack);
					return doAction(player, hand);
				}
			}

		if(performHammerFunctions(player,world,pos,side,hitX,hitY,hitZ,hand))
		{
			return doAction(player, hand);
		}

		return EnumActionResult.PASS;
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity instanceof IAdvancedMultiblockTileEntity)
		{
			IAdvancedMultiblockTileEntity mb = (IAdvancedMultiblockTileEntity)tileEntity;
			if(!mb.isConstructionFinished())
			{
				int energy = player.getHeldItem(hand).getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(Tools.electric_hammer_energy_per_use_construction, false);
				if(energy > 0)
				{
					mb.setCurrentConstruction(mb.getCurrentConstruction()+energy);
					return doAction(player, hand);
				}
				else
					return EnumActionResult.PASS;
			}
		}
		return EnumActionResult.PASS;

	}

	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState)
	{
		if(getToolClasses(stack).contains(toolClass)&&hasEnoughEnergy(stack))
			return 4;
		else
			return -1;
	}

	@Override
	public boolean isDamaged(ItemStack stack)
	{
		return false;
	}

	@Override
	public boolean isTool(ItemStack item)
	{
		return true;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand)
	{
		return !player.world.isRemote&&RotationUtil.rotateEntity(entity, player);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return 1f-(this.getEnergyStored(stack)/(float)this.getMaxEnergyStored(stack));
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return this.getEnergyStored(stack) < this.getMaxEnergyStored(stack);
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack)
	{
		return 0xff0000;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state)
	{
		if(hasEnoughEnergy(stack))
		{
			for(String type : this.getToolClasses(stack))
				if(state.getBlock().isToolEffective(type, state))
					return 16;
		}
		return super.getDestroySpeed(stack, state);
	}

	//Shares code with IEn, long live II-IEn Cooperation!
	boolean performHammerFunctions(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		TileEntity tile = world.getTileEntity(pos);
		IBlockState state = world.getBlockState(pos);
		if(tile==null)
			return false;


		if(!(tile instanceof IDirectionalTile)&&!(tile instanceof IHammerInteraction)&&!(tile instanceof IConfigurableSides)&&hasEnoughEnergy(player.getHeldItem(hand)))
		{
			if(RotationUtil.rotateBlock(world, pos, side))
			{
				player.getHeldItem(hand).getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(Tools.electric_hammer_energy_per_use, false);
				return true;
			}
		}
		else if(tile instanceof IConfigurableSides&&!world.isRemote)
		{
			int iSide = player.isSneaking()?side.getOpposite().ordinal(): side.ordinal();
			if(((IConfigurableSides)tile).toggleSide(iSide, player))
				return true;
		}
		else if(CommonProxy.tileEntitiesWeDontLike.stream().noneMatch(tileEntityPredicate -> tileEntityPredicate.test(tile))
				&& tile instanceof IDirectionalTile&&((IDirectionalTile)tile).canHammerRotate(side, hitX, hitY, hitZ, player)&&!world.isRemote)
		{
			EnumFacing f = ((IDirectionalTile)tile).getFacing();
			EnumFacing oldF = f;
			int limit = ((IDirectionalTile)tile).getFacingLimitation();

			if(limit==0)
				f = EnumFacing.VALUES[(f.ordinal()+1)%EnumFacing.VALUES.length];
			else if(limit==1)
				f = player.isSneaking()?f.rotateAround(side.getAxis()).getOpposite(): f.rotateAround(side.getAxis());
			else if(limit==2||limit==5)
				f = player.isSneaking()?f.rotateYCCW(): f.rotateY();
			((IDirectionalTile)tile).setFacing(f);
			((IDirectionalTile)tile).afterRotation(oldF, f);
			tile.markDirty();
			world.notifyBlockUpdate(pos, state, state, 3);
			world.addBlockEvent(tile.getPos(), tile.getBlockType(), 255, 0);
			return true;
		}
		else if(tile instanceof IHammerInteraction&&!world.isRemote)
		{
			boolean b = ((IHammerInteraction)tile).hammerUseSide(side, player, hitX, hitY, hitZ);
			if(b)
				return b;
		}

		return false;
	}

	@Override
	public int getMaxEnergyStored(ItemStack container)
	{
		return Tools.electric_hammer_capacity;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
	{
		stack.getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(Tools.electric_hammer_energy_per_use, false);
		return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		if(ItemNBTHelper.getBoolean(stack, "forbidHammer"))
			return ImmutableSet.of(CommonProxy.TOOL_ADVANCED_HAMMER);
		else
			return ImmutableSet.of(CommonProxy.TOOL_ADVANCED_HAMMER, Lib.TOOL_HAMMER);
	}

	@Override
	public boolean canHarvestBlock(@Nonnull IBlockState state, ItemStack stack)
	{
		if(hasEnoughEnergy(stack))
		{
			if(state.getBlock() instanceof BlockIEBase)
			{
				if(((BlockIEBase)state.getBlock()).allowHammerHarvest(state))
					return true;
			}
			else if(state.getBlock().isToolEffective(TOOL_HAMMER, state))
				return true;
			else if(state.getBlock().isToolEffective(CommonProxy.TOOL_ADVANCED_HAMMER, state))
				return true;
		}
		return false;
	}

	public boolean hasEnoughEnergy(ItemStack stack)
	{
		return stack.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored() >= Tools.electric_hammer_energy_per_use;
	}

	private EnumActionResult doAction(EntityPlayer player, EnumHand hand)
	{
		player.swingArm(hand);
		return EnumActionResult.SUCCESS;
	}
}
