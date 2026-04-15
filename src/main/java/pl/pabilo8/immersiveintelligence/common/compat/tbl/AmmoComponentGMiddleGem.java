package pl.pabilo8.immersiveintelligence.common.compat.tbl;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.IEPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
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
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

public class AmmoComponentGMiddleGem extends AmmoComponent
/**
 * @author Carver (carver@iiteam.net)
 * @since 14.04.2026
 * @updated 15.04.2026
 */
{

//Middle gem circle:
// aqua_middle_gem: Chance to weaken the enemy when attacking / Chance to gain defense when attacked
// crimson_middle_gem:  Chance to gain strength when attacking / Chance to hurt the enemy when attacked
// > green_middle_gem:  Chance to heal when attacking / Chance to absorb damage when attacked
//
// Aqua Gems are stronger against Crimson Gems, but weaker against Green Gems.
//	Crimson Gems are stronger against Green Gems, but weaker against Aqua Gems.
//	Green Gems are stronger against Aqua Gems, but weaker against Crimson Gems.

// default tbl value: given it is a ground element, may as well just stun and apply something more ground-related.
// Given we are stuffing it into an explosive shell or a bullet, and gem ores are rare,and we are shooting it without return, may as well amplify.

	public AmmoComponentGMiddleGem()
	{
		super("green_middlegem", 1f, ComponentRole.SPECIAL, IIColor.fromPackedARGB(0x2b820d));
	}

	@Override

	public IngredientStack getMaterial()
	{
		Item green_middlegem = Item.REGISTRY.getObject(new ResourceLocation("thebetweenlands", "green_middle_gem"));

		return new IngredientStack(new ItemStack(green_middlegem, 1));
	}
	@Override

	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentSize, float multiplier, Entity owner)
	{
		SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(ResLoc.of("thebetweenlands:beam_activate"));
		world.playSound(null, pos.x,pos.y,pos.z,sound,  SoundCategory.NEUTRAL, 1.0F, 1.0F);

		SoundEvent sound2 = ForgeRegistries.SOUND_EVENTS.getValue(ResLoc.of("minecraft:block_glass_break"));
		world.playSound(null, pos.x,pos.y,pos.z,sound2,  SoundCategory.NEUTRAL, 1.0F, 0.5F);

		IIAmmoUtils.suppress(world, pos.x, pos.y, pos.z, 10f*multiplier, (int)(255*multiplier));

		BlockPos ppos = new BlockPos(pos);

		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(10*multiplier)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
		{
			Potion slowness = Potion.REGISTRY.getObject(ResLoc.of("minecraft:slowness"));
			Potion jump_boost = Potion.REGISTRY.getObject(ResLoc.of("minecraft:jump_boost"));
			e.addPotionEffect(new PotionEffect(slowness, 160, 4));
			e.addPotionEffect(new PotionEffect(jump_boost, 160, -4));
			//negative jump boost will not allow to jump
			e.addPotionEffect(new PotionEffect(IEPotions.stunned, 128, 4));
			e.attackEntityFrom(DamageSource.FALLING_BLOCK, 8);
		}
		Vec3d v = dir.scale(-1);

		EntityAreaEffectCloud cloud = new EntityAreaEffectCloud(world, pos.x+v.x, pos.y+v.y+1f, pos.z+v.z);
		cloud.setDuration(80);
		cloud.setParticle(EnumParticleTypes.ENCHANTMENT_TABLE);
		cloud.setParticle(EnumParticleTypes.FALLING_DUST);
		world.spawnEntity(cloud);
	}
}
