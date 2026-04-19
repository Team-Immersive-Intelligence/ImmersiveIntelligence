package pl.pabilo8.immersiveintelligence.common.compat.srp;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
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
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

public class AmmoComponentInfester extends AmmoComponent
{
	public AmmoComponentInfester()
	{
		super("infestor_bomb", 1f, ComponentRole.SPECIAL, IIColor.fromPackedRGB(0x696244));
	}
	//Todo: find color for hex

	@Override
	public IngredientStack getMaterial()
	{
		Item parasitecanister = Item.REGISTRY.getObject(new ResourceLocation("srparasites", "parasitecanister"));

		return new IngredientStack(new ItemStack(parasitecanister, 1, 2)); //grotesque lump. Meta 0 is unharvestable/does not yield compatible loot.
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentSize, float multiplier, Entity owner)
	{
		new IIExplosion(world, owner, pos, dir,
				7*componentSize, 4*multiplier, ComponentEffectShape.ORB, false, componentSize > 0.125f, false)
				.doExplosion();

		SoundEvent sound1 = ForgeRegistries.SOUND_EVENTS.getValue(ResLoc.of("srparasites:rathol_explotion"));
		//for summoning sound buthol_explotion / mob_explotion general small explosion / rathol_explotion - big flesh explosion.

		world.playSound(null, pos.x, pos.y, pos.z, sound1, SoundCategory.NEUTRAL, 2.0F, 1.0F);


		BlockPos ppos = new BlockPos(pos);
		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(5)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
		{
			Potion coth = Potion.REGISTRY.getObject(ResLoc.of("srparasites:coth"));
			Potion viral = Potion.REGISTRY.getObject(ResLoc.of("srparasites:viral"));
			Potion corrosive = Potion.REGISTRY.getObject(ResLoc.of("srparasites:corrosive"));
			Potion conta = Potion.REGISTRY.getObject(ResLoc.of("srparasites:conta"));

			e.addPotionEffect(new PotionEffect(coth, 380,1));
			e.addPotionEffect(new PotionEffect(viral, 380,2));
			e.addPotionEffect(new PotionEffect(corrosive, 380,3));
			e.addPotionEffect(new PotionEffect(conta, 500,0));
		}

		Entity e1 = EntityList.createEntityByIDFromName(ResLoc.of("srparasites:ata"), world);
		e1.setPosition(pos.z, pos.y, pos.z);
		world.spawnEntity(e1);
		world.spawnEntity(e1);
		world.spawnEntity(e1);
		world.spawnEntity(e1);
		world.spawnEntity(e1);

		//TODO: add block infestation block placer.

	}
}
