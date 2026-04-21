package pl.pabilo8.immersiveintelligence.common.compat.srp;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityGasCloud;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

public class AmmoComponentParasiticgas extends AmmoComponent
{
	public AmmoComponentParasiticgas()
	{
		super("virulent_gas", 1f, ComponentRole.SPECIAL, IIColor.fromPackedARGB(0x1b2b11));
	}

	@Override
	public IngredientStack getMaterial()
	{
		Item diseased_sponge = Item.REGISTRY.getObject(new ResourceLocation("srparasites", "diseased_sponge"));

		return new IngredientStack(new ItemStack(diseased_sponge, 1));
	}

	Fluid fluid = FluidRegistry.getFluidStack("deadblood", 4000).getFluid();

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float size, float multiplier, Entity owner)
	{

		new IIExplosion(world, owner, pos, dir,
				4, 0, ComponentEffectShape.ORB, false, false, false)
				.doExplosion();

		BlockPos ppos = new BlockPos(pos);
		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(6)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
		{

			Potion negativesaturation = Potion.REGISTRY.getObject(ResLoc.of("minecraft:saturation"));
			Potion hunger = Potion.REGISTRY.getObject(ResLoc.of("minecraft:hunger"));

			Potion viral = Potion.REGISTRY.getObject(ResLoc.of("srparasites:viral"));
			Potion corrosive = Potion.REGISTRY.getObject(ResLoc.of("srparasites:corrossive"));
			Potion conta = Potion.REGISTRY.getObject(ResLoc.of("srparasites:conta"));

			e.addPotionEffect(new PotionEffect(IIPotions.corrosion, 380, 2));
			e.addPotionEffect(new PotionEffect(viral, 380, 1));
			e.addPotionEffect(new PotionEffect(corrosive, 380, 2));
			e.addPotionEffect(new PotionEffect(conta, 480, 2));

			e.addPotionEffect(new PotionEffect(negativesaturation, 180, -5));
			e.addPotionEffect(new PotionEffect(hunger, 180, 3));
		}

		if(world.isRemote)
			return;

		Vec3d v = new Vec3d(0, -1, 0);
		BlockPos p = new BlockPos(pos);
		Vec3d throwerPos = new Vec3d(p.offset(EnumFacing.UP, 3));

		EntityGasCloud gasCloud = new EntityGasCloud(world, throwerPos.x+v.x*2, throwerPos.y+v.y*2,
				throwerPos.z+v.z*2, new FluidStack(fluid, (int)(multiplier*50000)));
		world.spawnEntity(gasCloud);

		//longlasting contaminant cloud

		SoundEvent sound1 = ForgeRegistries.SOUND_EVENTS.getValue(ResLoc.of("srparasites:mob_explotion"));
		//for summoning sound buthol_explotion / mob_explotion general small explosion / rathol_explotion - big flesh explosion.

		IIPacketHandler.playRangedSound(world, pos, IISounds.explosionIncendiary, SoundCategory.NEUTRAL, (int)(30*multiplier), 1f, 2f);

		world.playSound(null, pos.x, pos.y, pos.z, sound1, SoundCategory.NEUTRAL, 2.0F, 1.0F);

	}
}
