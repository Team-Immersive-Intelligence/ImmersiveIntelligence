package pl.pabilo8.immersiveintelligence.common.compat.dd;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.IEPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageFireworks;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Carver (carver@iiteam.net)
 * @updated 11.04.2026
 * @since 08.04.2026
 */

public class AmmoComponentWindCharge extends AmmoComponent

{
	public AmmoComponentWindCharge()
	{
		super("windcharge", 1f, ComponentRole.SPECIAL, IIColor.fromPackedARGB(0xc8dbfa));
	}

	@Override

	public IngredientStack getMaterial()
	{
		Item windcharge1 = Item.REGISTRY.getObject(new ResourceLocation("deeperdepths", "wind_charge"));
		return new IngredientStack(new ItemStack(windcharge1, 1));
	}

	@Override
	//10.04.2026 Carver: properly implemented the desired effects for the ammo component

	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float size, float multiplier, Entity owner)
	{
		IIAmmoUtils.suppress(world, pos.x, pos.y, pos.z, 10f*multiplier, (int)(255*multiplier));

		BlockPos ppos = new BlockPos(pos);
		new IIExplosion(world, owner, pos, null, 2*multiplier, 0, ComponentEffectShape.ORB, false, false, true)
				.doExplosion();

		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(20*multiplier)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
		{
			e.addPotionEffect(new PotionEffect(IEPotions.stunned, 60, 2));
			Potion SLOW = Potion.REGISTRY.getObject(ResLoc.of("minecraft:slowness"));
			e.addPotionEffect(new PotionEffect(SLOW, 40, 2));

		}
		Entity e = EntityList.createEntityByIDFromName(ResLoc.of("deeperdepths:wind_charge"), world);
		e.setPosition(pos.z, pos.y, pos.z);
		world.spawnEntity(e);
	}
}
