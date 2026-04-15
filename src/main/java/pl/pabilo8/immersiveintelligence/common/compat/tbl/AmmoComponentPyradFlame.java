package pl.pabilo8.immersiveintelligence.common.compat.tbl;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nullable;

public class AmmoComponentPyradFlame extends AmmoComponent

/**
 * @author Carver (carver@iiteam.net)
 * @since 15.04.2026
 * @updated 15.04.2026
 */
{

	public AmmoComponentPyradFlame()
	{
		super("pyrad_flame", 1f, ComponentRole.SPECIAL, IIColor.fromPackedARGB(0xecad2f));
	}

	@Override

	public IngredientStack getMaterial()
	{
		Item pyrad_flame = Item.REGISTRY.getObject(new ResourceLocation("thebetweenlands", "pyrad_flame"));

		return new IngredientStack(new ItemStack(pyrad_flame, 1));
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float size, float multiplier, @Nullable Entity owner)
	{
		new IIExplosion(world, owner, pos, dir,
				8, 0, ComponentEffectShape.ORB, true, false, false)
				.doExplosion();

		Entity e1 = EntityList.createEntityByIDFromName(ResLoc.of("thebetweenlands:EntityPyradFlame"), world);
		assert e1!=null;
		e1.setPosition(pos.z, pos.y, pos.z);
		world.spawnEntity(e1);
		world.spawnEntity(e1);
		world.spawnEntity(e1);
		world.spawnEntity(e1);

		BlockPos ppos = new BlockPos(pos);
		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(10*multiplier)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
		{
			e.setFire(120);
		}
	}
}
