package pl.pabilo8.immersiveintelligence.common.compat.srp;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

public class AmmoComponentSerratedPayload extends AmmoComponent
{
	public AmmoComponentSerratedPayload()
	{
		super("blade_fragmenter", 0.5f, ComponentRole.SPECIAL, IIColor.fromPackedRGB(0x5a3933));
	}

	@Override
	public IngredientStack getMaterial()
	{
		Item infectious_blade_fragment = Item.REGISTRY.getObject(new ResourceLocation("srparasites", "infectious_blade_fragment"));

		return new IngredientStack(new ItemStack(infectious_blade_fragment, 1));
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentSize, float multiplier, Entity owner)
	{
		new IIExplosion(world, owner, pos, dir,
				7*componentSize, 0, shape, false, false, false)
				.doExplosion();

		SoundEvent sound1 = ForgeRegistries.SOUND_EVENTS.getValue(ResLoc.of("srparasites:mob_explotion"));
		//for summoning sound buthol_explotion / mob_explotion general small explosion / rathol_explotion - big flesh explosion.

		world.playSound(null, pos.x, pos.y, pos.z, sound1, SoundCategory.NEUTRAL, 2.0F, 1.0F);

		IIAmmoUtils.suppress(world, pos.x, pos.y, pos.z, 5f*multiplier, (int)(255*multiplier));

		BlockPos ppos = new BlockPos(pos);
		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(7)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
		{
			Potion bleed = Potion.REGISTRY.getObject(ResLoc.of("srparasites:bleed"));
			e.attackEntityFrom(DamageSource.CACTUS, 8);
			e.addPotionEffect(new PotionEffect(bleed, 380,4));
		}

	}
}
