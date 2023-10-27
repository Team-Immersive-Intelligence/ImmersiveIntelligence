package pl.pabilo8.immersiveintelligence.common.item;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDevice0;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockTypes_WoodenDevice0;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.utils.minecart.IMinecartBlockPickable;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice.IIBlockTypes_MetalDevice;
import pl.pabilo8.immersiveintelligence.common.entity.minecart.barrel.EntityMinecartBarrelSteel;
import pl.pabilo8.immersiveintelligence.common.entity.minecart.barrel.EntityMinecartBarrelWooden;
import pl.pabilo8.immersiveintelligence.common.entity.minecart.capacitor.EntityMinecartCapacitorCreative;
import pl.pabilo8.immersiveintelligence.common.entity.minecart.capacitor.EntityMinecartCapacitorHV;
import pl.pabilo8.immersiveintelligence.common.entity.minecart.capacitor.EntityMinecartCapacitorLV;
import pl.pabilo8.immersiveintelligence.common.entity.minecart.capacitor.EntityMinecartCapacitorMV;
import pl.pabilo8.immersiveintelligence.common.entity.minecart.crate.EntityMinecartCrateReinforced;
import pl.pabilo8.immersiveintelligence.common.entity.minecart.crate.EntityMinecartCrateSteel;
import pl.pabilo8.immersiveintelligence.common.entity.minecart.crate.EntityMinecartCrateWooden;
import pl.pabilo8.immersiveintelligence.common.item.ItemIIMinecart.Minecarts;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class ItemIIMinecart extends ItemIISubItemsBase<Minecarts>
{
	public ItemIIMinecart()
	{
		super("minecart", 1, Minecarts.values());
	}

	@GeneratedItemModels(itemName = "minecart")
	public enum Minecarts implements IIItemEnum
	{
		WOODEN_CRATE(EntityMinecartCrateWooden::new,
				() -> new ItemStack(IEContent.blockWoodenDevice0, 1, BlockTypes_WoodenDevice0.CRATE.getMeta())),
		REINFORCED_CRATE(EntityMinecartCrateReinforced::new,
				() -> new ItemStack(IEContent.blockWoodenDevice0, 1, BlockTypes_WoodenDevice0.REINFORCED_CRATE.getMeta())),
		STEEL_CRATE(EntityMinecartCrateSteel::new,
				() -> new ItemStack(IIContent.blockMetalDevice, 1, IIBlockTypes_MetalDevice.METAL_CRATE.getMeta())),

		WOODEN_BARREL(EntityMinecartBarrelWooden::new,
				() -> new ItemStack(IEContent.blockWoodenDevice0, 1, BlockTypes_WoodenDevice0.BARREL.getMeta())),
		METAL_BARREL(EntityMinecartBarrelSteel::new,
				() -> new ItemStack(IEContent.blockMetalDevice0, 1, BlockTypes_MetalDevice0.BARREL.getMeta())),

		CAPACITOR_LV(EntityMinecartCapacitorLV::new,
				() -> new ItemStack(IEContent.blockMetalDevice0, 1, BlockTypes_MetalDevice0.CAPACITOR_LV.getMeta())),
		CAPACITOR_MV(EntityMinecartCapacitorMV::new,
				() -> new ItemStack(IEContent.blockMetalDevice0, 1, BlockTypes_MetalDevice0.CAPACITOR_MV.getMeta())),
		CAPACITOR_HV(EntityMinecartCapacitorHV::new,
				() -> new ItemStack(IEContent.blockMetalDevice0, 1, BlockTypes_MetalDevice0.CAPACITOR_HV.getMeta())),
		CAPACITOR_CREATIVE(EntityMinecartCapacitorCreative::new,
				() -> new ItemStack(IEContent.blockMetalDevice0, 1, BlockTypes_MetalDevice0.CAPACITOR_CREATIVE.getMeta()));

		public final BiFunction<World, Vec3d, EntityMinecart> minecart;
		public final Supplier<ItemStack> stack;

		Minecarts(BiFunction<World, Vec3d, EntityMinecart> minecart, Supplier<ItemStack> stack)
		{
			this.minecart = minecart;
			this.stack = stack;
		}
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		IBlockState iblockstate = world.getBlockState(pos);

		if(!BlockRailBase.isRailBlock(iblockstate))
		{
			return EnumActionResult.FAIL;
		}
		else
		{
			ItemStack stack = player.getHeldItem(hand);

			if(!world.isRemote)
			{
				BlockRailBase.EnumRailDirection dir = iblockstate.getBlock() instanceof BlockRailBase?((BlockRailBase)iblockstate.getBlock()).getRailDirection(world, pos, iblockstate, null): BlockRailBase.EnumRailDirection.NORTH_SOUTH;
				double d0 = dir.isAscending()?0.5D: 0.0D;
				EntityMinecart ent = null;
				ItemStack blockStack = ItemStack.EMPTY;

				if(stack.getMetadata() < Minecarts.values().length)
				{
					Minecarts cart = Minecarts.values()[stack.getMetadata()];
					ent = cart.minecart.apply(world, new Vec3d(pos.getX()+0.5D, pos.getY()+0.0625D+d0, pos.getZ()+0.5D));
					blockStack = cart.stack.get();
				}

				if(ent!=null)
				{
					blockStack.setTagCompound(stack.getTagCompound());
					((IMinecartBlockPickable)ent).setMinecartBlock(blockStack);
					world.spawnEntity(ent);
				}
			}

			stack.shrink(1);
			return EnumActionResult.SUCCESS;
		}
	}
}
