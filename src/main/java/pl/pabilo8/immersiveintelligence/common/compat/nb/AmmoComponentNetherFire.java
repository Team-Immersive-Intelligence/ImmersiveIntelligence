package pl.pabilo8.immersiveintelligence.common.compat.nb;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import java.util.Set;

public class AmmoComponentNetherFire extends AmmoComponent
{
	public AmmoComponentNetherFire()
	{
		super("nether_fire", 1f, ComponentRole.SPECIAL, IIColor.fromPackedRGB(0x36D9E6));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("soul_lantern");
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentSize, float multiplier, Entity owner)
	{
		IIPacketHandler.playRangedSound(world, pos, IISounds.explosionIncendiary, SoundCategory.NEUTRAL, (int)(20*multiplier), 1f, 1f);

		/*new IIExplosion(world, owner, pos, dir,
				4*componentSize, 4*multiplier, shape, false, componentSize > 0.125f, false)
				.doExplosion();*/

		BlockPos ppos = new BlockPos(pos);
		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(5*multiplier)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
			e.setFire(800);

		Block soulFireBlock = Block.REGISTRY.getObject(NetherBackportHelper.RES_NB.with("soul_fire"));

		Set<BlockPos> blocks = IIUtils.getBlocksInOrb(world, new BlockPos(pos), 6*componentSize);
		for(BlockPos firePos : blocks)
		{
			IBlockState placed = (Utils.RAND.nextGaussian() <= 0.01?Blocks.FIRE: soulFireBlock).getDefaultState();

			if(world.isAirBlock(firePos)&&world.getBlockState(firePos.down()).isTopSolid())
				world.setBlockState(firePos, placed);
			if(Utils.isOreBlockAt(world, firePos, "wood")||Utils.isOreBlockAt(world, firePos, "logWood"))
				world.setBlockState(firePos, placed);
		}
	}
}
