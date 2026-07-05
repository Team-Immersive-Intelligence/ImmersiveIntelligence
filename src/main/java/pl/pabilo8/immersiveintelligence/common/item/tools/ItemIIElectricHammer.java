package pl.pabilo8.immersiveintelligence.common.item.tools;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IConfigurableSides;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.RotationUtil;
import blusunrize.immersiveengineering.common.util.advancements.IEAdvancements;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemUtils;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IConstructionRequiringDevice;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;
import pl.pabilo8.modworks.annotations.item.ItemModelType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

import static blusunrize.immersiveengineering.api.Lib.TOOL_HAMMER;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 30.05.2019
 */
@IIItemProperties(category = IICategory.TOOLS)
@GeneratedItemModels(itemName = "electric_hammer", type = ItemModelType.ITEM_SIMPLE_TOOL, texturePath = "tools/electric_hammer")
public class ItemIIElectricHammer extends ItemIIElectricTool
{
	public ItemIIElectricHammer()
	{
		super("electric_hammer", "electric_hammer");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		super.addInformation(stack, world, list, flag);

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

	@Nonnull
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		TileEntity tile = world.getTileEntity(pos);
		ItemNBTHelper.setBoolean(stack, "forbidHammer", IIContent.tileEntitiesWeDontLike.stream().anyMatch(tileEntityPredicate -> tileEntityPredicate.test(tile)));
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
							b = true;
				if(!b)
					break;
				if(interdictedMultiblocks!=null)
					for(String s : interdictedMultiblocks)
						if(mb.getUniqueName().equalsIgnoreCase(s))
							b = false;
				if(!b)
					break;
				if(MultiblockHandler.fireMultiblockFormationEventPre(player, mb, pos, stack).isCanceled())
					continue;
				if(mb.createStructure(world, pos, side, player))
				{
					//Consume energy from the electric hammer when forming a multiblock
					if(stack.hasCapability(CapabilityEnergy.ENERGY, null) && !IIItemUtils.canConstructFreeOfCharge(player))
						stack.getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(Tools.electricHammerEnergyPerUseConstruction, false);

					if(player instanceof EntityPlayerMP)
						IEAdvancements.TRIGGER_MULTIBLOCK.trigger((EntityPlayerMP)player, mb, stack);
					return doAction(player, hand);
				}
			}

		if(performHammerFunctions(player, world, pos, side, hitX, hitY, hitZ, hand))
			return doAction(player, hand);

		return EnumActionResult.PASS;
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity te = world.getTileEntity(pos);
		ItemStack stack = player.getHeldItem(hand);
		IEnergyStorage cap = getEnergyStorage(stack);
		if(cap==null)
			return EnumActionResult.FAIL;

		if(!(te instanceof IConstructionRequiringDevice))
			return EnumActionResult.PASS;
		IConstructionRequiringDevice mb = ((IConstructionRequiringDevice)te).master();

		if(mb!=null&&!mb.isConstructionFinished())
		{
			int energy = IIItemUtils.canConstructFreeOfCharge(player)?999999: cap.extractEnergy(Tools.electricHammerEnergyPerUseConstruction, false);
			if(energy > 0)
			{
				if(!IIItemUtils.canConstructFreeOfCharge(player))
					cap.extractEnergy(Tools.electricHammerEnergyPerUseConstruction, false);
				mb.progressConstruction(energy);
				world.playSound(null, pos, IISounds.constructionHammer, SoundCategory.PLAYERS, 0.5f, 1);
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
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand)
	{
		return !player.world.isRemote&&RotationUtil.rotateEntity(entity, player);
	}

	//Shares code with IEn, long live II-IEn Cooperation!
	boolean performHammerFunctions(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		TileEntity tile = world.getTileEntity(pos);
		IBlockState state = world.getBlockState(pos);
		ItemStack stack = player.getHeldItem(hand);

		if(!(tile instanceof IDirectionalTile)&&!(tile instanceof IHammerInteraction)&&!(tile instanceof IConfigurableSides)&&hasEnoughEnergy(stack))
			if(RotationUtil.rotateBlock(world, pos, side))
			{
				drainEnergy(stack, Tools.electricHammerEnergyPerUse, false);
				return true;
			}

		if(tile==null)
			return false;

		if(tile instanceof IConfigurableSides&&!world.isRemote)
		{
			int iSide = player.isSneaking()?side.getOpposite().ordinal(): side.ordinal();
			return ((IConfigurableSides)tile).toggleSide(iSide, player);
		}
		else if(IIContent.tileEntitiesWeDontLike.stream().noneMatch(tileEntityPredicate -> tileEntityPredicate.test(tile))
				&&tile instanceof IDirectionalTile&&((IDirectionalTile)tile).canHammerRotate(side, hitX, hitY, hitZ, player)&&!world.isRemote)
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
		return Tools.electricHammerCapacity;
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		if(ItemNBTHelper.getBoolean(stack, "forbidHammer"))
			return ImmutableSet.of(IIReference.TOOL_ADVANCED_HAMMER);
		else
			return ImmutableSet.of(IIReference.TOOL_ADVANCED_HAMMER, Lib.TOOL_HAMMER);
	}

	@Override
	public boolean canHarvestBlock(@Nonnull IBlockState state, ItemStack stack)
	{
		if(hasEnoughEnergy(stack))
			if(state.getBlock() instanceof BlockIEBase)
				return ((BlockIEBase<?>)state.getBlock()).allowHammerHarvest(state);
			else if(state.getBlock().isToolEffective(TOOL_HAMMER, state))
				return true;
			else return state.getBlock().isToolEffective(IIReference.TOOL_ADVANCED_HAMMER, state);
		return false;
	}

	@Override
	protected int getEnergyPerUse(ItemStack stack)
	{
		return Tools.electricHammerEnergyPerUse;
	}

	private EnumActionResult doAction(EntityPlayer player, EnumHand hand)
	{
//		player.swingArm(hand);
		return EnumActionResult.SUCCESS;
	}
}
