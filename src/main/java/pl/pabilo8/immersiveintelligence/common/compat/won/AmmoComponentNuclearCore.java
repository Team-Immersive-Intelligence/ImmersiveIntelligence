package pl.pabilo8.immersiveintelligence.common.compat.won;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.IEPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;

/**
 * @author Carver (carver@iiteam.net)
 * @updated 13.04.2026
 */


//This component is an EXTREME irradiator. Due to its relation to "Demon cores", does not pollute the area with biome.

//Ideally, it should also make a bright blue flash.

public class AmmoComponentNuclearCore extends AmmoComponent
{
	public AmmoComponentNuclearCore()
	{
		super("nuclearcore", 1f, ComponentRole.SPECIAL, IIColor.fromPackedARGB(0xf2ff92));
	}

	@Override

	public IngredientStack getMaterial()
	{
		Item nuclearcore = Item.REGISTRY.getObject(new ResourceLocation("wyrmsofnyrus", "nuclearcore"));
		return new IngredientStack(new ItemStack(nuclearcore, 1));
	}

	@Override
	public int getSlotsTaken()
	{
		return 3;
	}

	@Override

	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentSize, float multiplier, Entity owner)
	{

		BlockPos ppos = new BlockPos(pos);

		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(16*multiplier)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
		{
			e.addPotionEffect(new PotionEffect(IIPotions.nuclearHeat, 40, 0));
			e.hurtResistantTime = 0;
			e.getArmorInventoryList().forEach(stack -> stack.damageItem(stack.getMaxDamage(), e));
			e.attackEntityFrom(IIDamageSources.RADIATION_DAMAGE, 2000);
		}
		entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(32*multiplier)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
			e.addPotionEffect(new PotionEffect(IIPotions.radiation, 8000, 10));
		}
	}
