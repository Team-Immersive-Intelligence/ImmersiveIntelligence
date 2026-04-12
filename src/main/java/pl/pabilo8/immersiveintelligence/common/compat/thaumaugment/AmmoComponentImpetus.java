package pl.pabilo8.immersiveintelligence.common.compat.thaumaugment;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.ITeslaEntity;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.IEDamageSources;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
import blusunrize.immersiveengineering.common.util.IEPotions;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleProperties;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;
import pl.pabilo8.immersiveintelligence.common.item.ItemIITracerPowder;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.entity.IIEntityUtils;

import javax.vecmath.Vector2f;
//import thaumcraft.api.aura.AuraHelper;

/**
 * @author Carver (carver@iiteam.net)
 * @since 12.04.2026
 */

public class AmmoComponentImpetus extends AmmoComponent
{
	public AmmoComponentImpetus()
	{
		super("impetus", 0.25f, ComponentRole.SPECIAL, IIColor.fromPackedRGB(0x000b10));
	}

	@Override
	public int getSlotsTaken()
	{
		return 3;
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("impetus");
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentSize, float multiplier, Entity owner)
	{
		float radius = multiplier*8;
		int extracted = (int)(2000000*multiplier);

		for(int x = (int)(-radius/2); x < radius/2; x++)
			for(int y = (int)(-radius/2); y < radius/2; y++)
				for(int z = (int)(-radius/2); z < radius/2; z++)
				{
					BlockPos pp = new BlockPos(pos).add(x, y, z);
					TileEntity te = world.getTileEntity(pp);
					if(te instanceof TileEntityMultiblockPart)
						te = ((TileEntityMultiblockPart<?>)te).master();

					if(te!=null)
					{
						if(te instanceof TileEntityMultiblockMetal)
						{
							((TileEntityMultiblockMetal<?, ?>)te).energyStorage.extractEnergy(extracted, false);
						}
						else
						{
							for(EnumFacing facing : EnumFacing.values())
							{
								if((te.hasCapability(CapabilityEnergy.ENERGY, facing)))
								{
									IEnergyStorage cap = te.getCapability(CapabilityEnergy.ENERGY, facing);
									if(cap!=null)
									{
										cap.extractEnergy(extracted, false);
										break;
									}
								}
							}
						}
					}
				}

		for(EntityLivingBase e : world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.x, pos.y, pos.z, pos.x, pos.y, pos.z).grow(radius)))
			if(!(e instanceof ITeslaEntity))
			{
				ElectricDamageSource dmgsrc = IEDamageSources.causeTeslaDamage(IEConfig.Machines.teslacoil_damage, false);

				if(!world.isRemote)
				{
					if(dmgsrc.apply(e))
					{
						int prevFire = e.fire;
						e.setFire(prevFire+1);
						e.addPotionEffect(new PotionEffect(IEPotions.stunned, 128));
					}
				}

				for(ItemStack stack : e.getArmorInventoryList())
				{
					if((stack.hasCapability(CapabilityEnergy.ENERGY, null)))
					{
						IEnergyStorage cap = stack.getCapability(CapabilityEnergy.ENERGY, null);
						if(cap!=null)
							if(cap.extractEnergy(extracted, false)==0)
							{
								if(ItemNBTHelper.hasKey(stack, "Energy"))
									ItemNBTHelper.setInt(stack, "Energy", Math.max(0, ItemNBTHelper.getInt(stack, "Energy")-extracted));
								else if(ItemNBTHelper.hasKey(stack, "energy"))
									ItemNBTHelper.setInt(stack, "energy", Math.max(0, ItemNBTHelper.getInt(stack, "energy")-extracted));
								else if(ItemNBTHelper.hasKey(stack, "Power"))
									ItemNBTHelper.setInt(stack, "Power", Math.max(0, ItemNBTHelper.getInt(stack, "Power")-extracted));
								else if(ItemNBTHelper.hasKey(stack, "power"))
									ItemNBTHelper.setInt(stack, "power", Math.max(0, ItemNBTHelper.getInt(stack, "power")-extracted));

							}
					}
				}
			}

		new IIExplosion(world, owner, pos, dir,
				4*componentSize, 100*multiplier, ComponentEffectShape.ORB, false, true, false)
				.doExplosion();

		//AuraHelper.polluteAura(world, pos, 100.0F, true);
		//AuraHelper.drainVis(world, pos, 200.0F, false);

		SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(ResLoc.of("thaumicaugmentation:impulse_cannon_railgun"));
		world.playSound(null, pos.x,pos.y,pos.z,sound,  SoundCategory.NEUTRAL, 2.0F, 0.5F);

		Entity rift = EntityList.createEntityByIDFromName(ResLoc.of("thaumcraft:flux_rift"), world);
		rift.setPosition(pos.z, pos.y, pos.z);
		world.spawnEntity(rift);

		BlockPos ppos = new BlockPos(pos);

		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(5*multiplier)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
		{
			e.addPotionEffect(new PotionEffect(IEPotions.flashed, 80, 5));
			e.hurtResistantTime = 0;
			e.attackEntityFrom(IIDamageSources.RADIATION_DAMAGE, 100);
			e.addPotionEffect(new PotionEffect(IIPotions.radiation, 100, 4));
		}
	}

	@Override
	public boolean spawnParticleTrail(EntityAmmoBase<?> ammo, NBTTagCompound nbt)
	{
		IIColor color = nbt.hasKey(ItemIITracerPowder.NBT_TRACER_COLOUR)?IIColor.fromPackedRGB(nbt.getInteger(ItemIITracerPowder.NBT_TRACER_COLOUR)): IIColor.BLACK;
		ParticleRegistry.spawnParticle("ammo/tracer", ammo.getPositionVector(), IIEntityUtils.getEntityMotion(ammo),
						new Vector2f((float)Math.toRadians(ammo.rotationYaw), (float)Math.toRadians(ammo.rotationPitch+90)))
				.withProperty(ParticleProperties.COLOR, color)
				.withProperty(ParticleProperties.SIZE, ammo.getAmmoType().getCaliber()/8f)
				.withProperty(ParticleProperties.MAX_LIFETIME, 2000);
		return true;
	}

	@Override
	public IIColor getColor(NBTTagCompound nbt)
	{
		return nbt!=null&&nbt.hasKey(ItemIITracerPowder.NBT_TRACER_COLOUR)?IIColor.fromPackedRGB(nbt.getInteger(ItemIITracerPowder.NBT_TRACER_COLOUR)): IIColor.BLACK;
	}
}

