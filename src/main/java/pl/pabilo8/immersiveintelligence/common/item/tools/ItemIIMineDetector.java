package pl.pabilo8.immersiveintelligence.common.item.tools;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIIMine;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;

/**
 * @author Pabilo8
 * @since 28.01.2021
 */
public class ItemIIMineDetector extends ItemIIBase
{
	public ItemIIMineDetector()
	{
		super("mine_detector", 1);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

		if(isSelected)
		{
			final float blockReachDistance = 4.5f;

			Vec3d vec3d = entityIn.getPositionEyes(0);
			Vec3d vec3d1 = entityIn.getLook(0);
			Vec3d vec3d2 = vec3d.addVector(vec3d1.x*blockReachDistance, vec3d1.y*blockReachDistance, vec3d1.z*blockReachDistance);

			RayTraceResult traceResult = worldIn.rayTraceBlocks(vec3d, vec3d2, false, false, true);
			if(traceResult!=null&&traceResult.typeOfHit==Type.BLOCK)
			{
				final BlockPos dPos = new BlockPos(traceResult.getBlockPos().getX(),entityIn.posY,traceResult.getBlockPos().getZ());
				for(int x = -3; x < 4; x++)
					for(int z = -3; z < 4; z++)
						if(worldIn.getBlockState(dPos.add(x, 0, z)).getBlock() instanceof BlockIIMine)
						{
							final BlockPos pp = dPos.add(x, 0, z);
							float dist = Math.max((float)new Vec3d(pp).distanceTo(entityIn.getPositionVector()),0.125f);

							if(worldIn.getTotalWorldTime()%(int)(dist*4)==0)
								worldIn.playSound(pp.getX(), pp.getY(), pp.getZ(), IISounds.mineDetector, SoundCategory.PLAYERS, 1, 0.5f, false);
						}
			}
		}

		//if is worn as a helmet
		if(entityIn instanceof EntityPlayer&&((EntityPlayer)entityIn).getItemStackFromSlot(EntityEquipmentSlot.HEAD).equals(stack))
		{
			if(!IIUtils.hasUnlockedIIAdvancement((EntityPlayer)entityIn, "main/secret_carvers_revenge"))
				IIUtils.unlockIIAdvancement((EntityPlayer)entityIn, "main/secret_carvers_revenge");
		}
	}
}
