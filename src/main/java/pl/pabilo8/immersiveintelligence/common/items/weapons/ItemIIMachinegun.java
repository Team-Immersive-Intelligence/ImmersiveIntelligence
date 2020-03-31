package pl.pabilo8.immersiveintelligence.common.items.weapons;

import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.items.ItemUpgradeableTool;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMachinegun;

import java.util.List;

import static pl.pabilo8.immersiveintelligence.common.items.ItemIIBase.fixupItem;

/**
 * Created by Pabilo8 on 01-11-2019.
 */
public class ItemIIMachinegun extends ItemUpgradeableTool
{
	public ItemIIMachinegun()
	{
		super("machinegun", 1, "MACHINEGUN");
		//Use interfaces pls Blu
		fixupItem(this);
	}

	@Override
	public int getSlotCount(ItemStack stack)
	{
		return 3;
	}

	@Override
	public boolean canModify(ItemStack stack)
	{
		return true;
	}

	@Override
	public Slot[] getWorkbenchSlots(Container container, ItemStack stack)
	{
		IItemHandler inv = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		return new Slot[]
				{
						new IESlot.Upgrades(container, inv, 0, 80, 32, "MACHINEGUN", stack, true),
						new IESlot.Upgrades(container, inv, 1, 100, 32, "MACHINEGUN", stack, true),
						new IESlot.Upgrades(container, inv, 2, 120, 32, "MACHINEGUN", stack, true)
				};
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		float f = 1.0F;
		float f1 = playerIn.prevRotationPitch+(playerIn.rotationPitch-playerIn.prevRotationPitch)*1.0F;
		float f2 = playerIn.prevRotationYaw+(playerIn.rotationYaw-playerIn.prevRotationYaw)*1.0F;
		double d0 = playerIn.prevPosX+(playerIn.posX-playerIn.prevPosX)*1.0D;
		double d1 = playerIn.prevPosY+(playerIn.posY-playerIn.prevPosY)*1.0D+(double)playerIn.getEyeHeight();
		double d2 = playerIn.prevPosZ+(playerIn.posZ-playerIn.prevPosZ)*1.0D;
		Vec3d vec3d = new Vec3d(d0, d1, d2);
		float f3 = MathHelper.cos(-f2*0.017453292F-(float)Math.PI);
		float f4 = MathHelper.sin(-f2*0.017453292F-(float)Math.PI);
		float f5 = -MathHelper.cos(-f1*0.017453292F);
		float f6 = MathHelper.sin(-f1*0.017453292F);
		float f7 = f4*f5;
		float f8 = f3*f5;
		double d3 = 5.0D;
		Vec3d vec3d1 = vec3d.add((double)f7*5.0D, (double)f6*5.0D, (double)f8*5.0D);
		RayTraceResult raytraceresult = worldIn.rayTraceBlocks(vec3d, vec3d1, false);

		if(raytraceresult==null)
		{
			return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
		}
		else
		{
			Vec3d vec3d2 = playerIn.getLook(1.0F);
			boolean flag = false;
			List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(playerIn, playerIn.getEntityBoundingBox().expand(vec3d2.x*5.0D, vec3d2.y*5.0D, vec3d2.z*5.0D).grow(1.0D));

			for(int i = 0; i < list.size(); ++i)
			{
				Entity entity = list.get(i);

				if(entity.canBeCollidedWith())
				{
					AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().grow(entity.getCollisionBorderSize());

					if(axisalignedbb.contains(vec3d))
					{
						flag = true;
					}
				}
			}

			if(flag)
			{
				return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
			}
			else if(raytraceresult.typeOfHit!=RayTraceResult.Type.BLOCK)
			{
				return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
			}
			else
			{
				AxisAlignedBB aabb = worldIn.getBlockState(raytraceresult.getBlockPos()).getCollisionBoundingBox(playerIn.world, raytraceresult.getBlockPos());
				EnumFacing facing = EnumFacing.fromAngle(playerIn.rotationYaw);
				float yaw = facing.getHorizontalAngle();
				float pitch = 25f;

				AxisAlignedBB fence;
				switch(facing)
				{
					case SOUTH:
						fence = new AxisAlignedBB(0, 0, 0.5, 1, 1, 1);
						break;
					case WEST:
						fence = new AxisAlignedBB(0, 0, 0, 0.5, 1, 1);
						break;
					case EAST:
						fence = new AxisAlignedBB(0.5, 0, 0, 1, 1, 1);
						break;
					default:
					case NORTH:
						fence = new AxisAlignedBB(0, 0, 0, 1, 1, 0.5);
						break;
				}
				/*
				ImmersiveIntelligence.logger.info(aabb);
				ImmersiveIntelligence.logger.info(fence);
				//fence.offset(raytraceresult.getBlockPos());
				 */
				if(aabb==null)
					return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);

				boolean intersects = Utils.isAABBContained(fence, aabb);

				if(!intersects)
				{
					return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
				}

				EntityMachinegun maschinengewehr = new EntityMachinegun(worldIn, raytraceresult.getBlockPos(), yaw, pitch, itemstack);

				if(!worldIn.isRemote)
				{
					worldIn.spawnEntity(maschinengewehr);
				}

				if(!playerIn.capabilities.isCreativeMode)
				{
					itemstack.shrink(1);
				}

				playerIn.addStat(StatList.getObjectUseStats(this));
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
			}
		}
	}
}
