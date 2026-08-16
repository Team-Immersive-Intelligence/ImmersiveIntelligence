package pl.pabilo8.immersiveintelligence.common.ammo.components.incendiary;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.IEPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityWhitePhosphorus;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageExplosion;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @updated 06.03.2024
 * @updated 14.9.2024
 * @since 10.07.2021
 */
public class AmmoComponentWhitePhosphorus extends AmmoComponent
{
	public AmmoComponentWhitePhosphorus()
	{
		super("white_phosphorus", 1f, ComponentRole.INCENDIARY, IIColor.fromPackedRGB(0xb8afa3), 2);
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("dustWhitePhosphorus");
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float size, float multiplier, @Nullable Entity owner)
	{
		if(world.isRemote)
			return;

		BlockPos blockPos = new BlockPos(pos);
		if(size > 0.2)
			IIPacketHandler.playRangedSound(world, pos, IISounds.explosionIncendiary, SoundCategory.NEUTRAL, (int)(40*multiplier), 1f, 1f);
		else
			world.playSound(null, blockPos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 1f, 1f);

		IIPacketHandler.sendToClient(MessageExplosion.createWhitePhosphorusMessage(world, pos, dir, shape, size));

		//Spawn main phosphorus entity, shared between all modes
		EntityWhitePhosphorus main = new EntityWhitePhosphorus(world, pos.x-dir.x, pos.y-dir.y, pos.z-dir.z, 0, 0, 0);
		main.motionX = -dir.x*0.75;
		main.motionY = -dir.y*0.75;
		main.motionZ = -dir.z*0.75;

		if(size > 0.2f)
		{
			//Set fire to surrounding blocks in a radius
			Set<BlockPos> blocks = IIUtils.getBlocksInOrb(world, blockPos, size*4f);
			for(BlockPos block : blocks)
				if(world.isAirBlock(block))
					world.setBlockState(block, Blocks.FIRE.getDefaultState());
		}

		//Deal fire damage to all entities around
		for(Entity entity : world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos).grow(4f)))
		{
			if(entity instanceof EntityLivingBase)
				((EntityLivingBase)entity).addPotionEffect(new PotionEffect(IEPotions.flammable, 80, 1));
			entity.attackEntityFrom(DamageSource.IN_FIRE, 10f*multiplier);
			entity.setFire(40);
		}

		switch(shape)
		{
			case ORB:
			case STAR:
			case CONE:
				main.motionX = 0;
				main.motionY = 0.4;
				main.motionZ = 0;

				float particlesInLayer = 10f*size;
				// Spawn shrapnel in a wider, explosive pattern
				for(int i = 0; i < 2*particlesInLayer; i++)
				{
					Vec3d vecDir = dir.scale(i < size*10?-1: -2).rotateYaw(i/(particlesInLayer)*360f);
					EntityWhitePhosphorus shrap = new EntityWhitePhosphorus(world, pos.x+vecDir.x, pos.y-dir.y, pos.z+vecDir.z, 0, 0, 0);

					vecDir = vecDir.scale(2);
					shrap.motionX = vecDir.x*0.125f;
					shrap.motionY = (vecDir.y+(i < size*10?2: 0.5))*0.125f*2;
					shrap.motionZ = vecDir.z*0.125f;
					world.spawnEntity(shrap);
				}
				break;
			case LINE:
				IILogger.info("Line!");
				Set<BlockPos> blocks = IIUtils.getBlocksInOrb(world, blockPos, 1f);
				for(BlockPos block : blocks)
					if(world.isAirBlock(block))
						world.setBlockState(block, Blocks.FIRE.getDefaultState());
				break;
		}
		world.spawnEntity(main);
	}
}
