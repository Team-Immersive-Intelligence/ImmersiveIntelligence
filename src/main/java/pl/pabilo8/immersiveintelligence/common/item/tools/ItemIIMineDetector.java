package pl.pabilo8.immersiveintelligence.common.item.tools;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Detects configured blocks while the player holds a powered detector.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.01.2021
 * @updated 04.10.2026
 */
@IIItemProperties(category = IICategory.WARFARE)
public class ItemIIMineDetector extends ItemIIElectricTool
{
	public static final List<IngredientStack> detectableBlocks = new ArrayList<>();

	public ItemIIMineDetector()
	{
		super("mine_detector", "mine_detector");
	}

	@Override
	public int getMaxEnergyStored(ItemStack stack)
	{
		return Tools.mineDetectorCapacity;
	}

	@Override
	protected int getEnergyPerUse(ItemStack stack)
	{
		return Tools.mineDetectorEnergyPerTick;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		if(!(entityIn instanceof EntityPlayer))
			return;

		EntityPlayer player = (EntityPlayer)entityIn;
		//Unlock the advancement when the detector is worn as a helmet.
		if(!worldIn.isRemote&&player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).equals(stack))
			if(!IIUtils.hasUnlockedIIAdvancement(player, "main/secret_carvers_revenge"))
				IIUtils.unlockIIAdvancement(player, "main/secret_carvers_revenge");

		if(!isSelected&&player.getHeldItemOffhand()!=stack)
			return;

		ItemNBTHelper.setFloat(stack, "distance", 0);
		//Drain energy on the server. The client checks charge for the local scan and sound.
		boolean powered = worldIn.isRemote?hasEnoughEnergy(stack): drainEnergy(stack, getEnergyPerUse(stack), false);
		if(!powered)
			return;

		final float blockReachDistance = 4.5f;
		Vec3d vec3d = entityIn.getPositionEyes(0);
		Vec3d vec3d1 = entityIn.getLook(0);
		Vec3d vec3d2 = vec3d.addVector(vec3d1.x*blockReachDistance, vec3d1.y*blockReachDistance, vec3d1.z*blockReachDistance);

		RayTraceResult traceResult = worldIn.rayTraceBlocks(vec3d, vec3d2, false, false, true);
		if(traceResult!=null&&traceResult.typeOfHit==Type.BLOCK)
		{
			final BlockPos dPos = new BlockPos(traceResult.getBlockPos().getX(), traceResult.getBlockPos().getY(), traceResult.getBlockPos().getZ());
			for(int y = 0; y > -Tools.mineDetectorRadius+1; y--)
				for(int x = -Tools.mineDetectorRadius+1; x < Tools.mineDetectorRadius; x++)
					for(int z = -Tools.mineDetectorRadius+1; z < Tools.mineDetectorRadius; z++)
					{
						IBlockState state = worldIn.getBlockState(dPos.add(x, y+1, z));
						if(state.getBlock()!=Blocks.AIR&&shouldBeDetected(new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state))))
						{
							final BlockPos pp = dPos.add(x, 0, z);
							float dist = Math.max((float)new Vec3d(pp).distanceTo(entityIn.getPositionVector()), 0.125f);

							ItemNBTHelper.setFloat(stack, "distance", Math.max(0, Tools.mineDetectorRadius+1-dist));
							if(worldIn.getTotalWorldTime()%Math.max(1, (int)(dist*4))==0)
								worldIn.playSound(pp.getX(), pp.getY(), pp.getZ(), IISounds.mineDetector, SoundCategory.PLAYERS, 1, 0.5f, false);
							return;
						}
					}
		}
	}

	private boolean shouldBeDetected(ItemStack stateStack)
	{
		for(IngredientStack detectableBlock : detectableBlocks)
			if(detectableBlock.matches(stateStack))
				return true;
		return false;
	}
}
