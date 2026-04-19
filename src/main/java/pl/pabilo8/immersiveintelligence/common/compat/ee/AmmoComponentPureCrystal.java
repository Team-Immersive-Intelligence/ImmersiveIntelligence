package pl.pabilo8.immersiveintelligence.common.compat.ee;

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
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Carver (carver@iiteam.net)
 * @since 18.04.2026
 * @updated 18.04.2026
 */

public class AmmoComponentPureCrystal extends AmmoComponent
{
	public AmmoComponentPureCrystal()
	{
		super("pure_crystal", 1f, ComponentRole.SPECIAL, IIColor.fromPackedARGB(0x9351b2));
	}

	@Override

	public IngredientStack getMaterial()
	{
		Item purple_crystal = Item.REGISTRY.getObject(new ResourceLocation("ee", "purple_crystal_item"));
		return new IngredientStack(new ItemStack(purple_crystal, 1));
	}

	@Override

	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentSize, float multiplier, Entity owner)
	{

		SoundEvent sound1 = ForgeRegistries.SOUND_EVENTS.getValue(ResLoc.of("ee:crystal:break"));
		world.playSound(null, pos.x, pos.y, pos.z, sound1, SoundCategory.NEUTRAL, 2.0F, 1.0F);

		BlockPos ppos = new BlockPos(pos);
		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(8*multiplier)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
		{
			//primary effect is gravity altercation
			Potion levitation = Potion.REGISTRY.getObject(ResLoc.of("minecraft:levitation"));

			e.addPotionEffect(new PotionEffect(levitation, 180, 3));
			e.attackEntityFrom(DamageSource.MAGIC, 5);
		}
	}
}
