package pl.pabilo8.immersiveintelligence.common.ammo.components.factory;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.Shrapnel;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityShrapnel;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageExplosion;

/**
 * An ammo component that spawns {@link EntityShrapnel shrapnel fragments} around.
 *
 * @author Pabilo8(pabilo.iiteam.net)
 * @updated 06.08.2026
 * @ii-approved 0.3.1
 * @since 30.08.2019
 */
public class AmmoComponentShrapnel extends AmmoComponent
{
	private final Shrapnel shrapnel;
	private final IngredientStack stack;

	public AmmoComponentShrapnel(String material)
	{
		super("shrapnel_"+material, 1f, ComponentRole.SHRAPNEL, ShrapnelHandler.registry.get(material).color);
		shrapnel = ShrapnelHandler.registry.get(material);
		stack = new IngredientStack("dust"+Character.toUpperCase(material.charAt(0))+material.substring(1));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getTranslatedName()
	{
		return I18n.format("ie.manual.entry.bullet_component."+getName());
	}

	@Override
	public IngredientStack getMaterial()
	{
		return stack;
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag,
	                     float size, float multiplier, Entity owner)
	{
		if(world.isRemote)
			return;

		IIPacketHandler.sendToClient(MessageExplosion.createShrapnelMessage(
				world, pos, shrapnel.color, size, shrapnel.fallsSlowly
		));

		int fragmentCount = Math.max(1, (int)(20*size));
		if(shrapnel.fallsSlowly)
			spawnSlowFragments(world, pos, fragmentCount);
		else
			spawnBurstFragments(world, pos, fragmentCount);
	}

	private void spawnBurstFragments(World world, Vec3d pos, int fragmentCount)
	{
		Vec3d baseDirection = new Vec3d(0, -1, 0);
		Vec3d throwerPos = pos.addVector(0, 3, 0);
		for(int i = 0; i < fragmentCount; i++)
		{
			Vec3d direction = baseDirection.addVector(
					Utils.RAND.nextGaussian()*.25f,
					Utils.RAND.nextGaussian()*.25f,
					Utils.RAND.nextGaussian()*.25f
			);
			EntityShrapnel fragment = new EntityShrapnel(world,
					throwerPos.x+baseDirection.x*2,
					throwerPos.y+baseDirection.y*2,
					throwerPos.z+baseDirection.z*2,
					0, 0, 0, shrapnel
			);
			fragment.motionX = direction.x*2;
			fragment.motionY = direction.y*0.05f;
			fragment.motionZ = direction.z*2;
			world.spawnEntity(fragment);
		}
	}

	private void spawnSlowFragments(World world, Vec3d pos, int fragmentCount)
	{
		for(int i = 0; i < fragmentCount; i++)
		{
			EntityShrapnel fragment = new EntityShrapnel(world,
					pos.x+Utils.RAND.nextGaussian()*0.25,
					pos.y+0.25,
					pos.z+Utils.RAND.nextGaussian()*0.25,
					0, 0, 0, shrapnel
			);
			fragment.motionX = Utils.RAND.nextGaussian()*0.18;
			fragment.motionY = 0.65+Math.abs(Utils.RAND.nextGaussian())*0.18;
			fragment.motionZ = Utils.RAND.nextGaussian()*0.18;
			world.spawnEntity(fragment);
		}
	}
}
