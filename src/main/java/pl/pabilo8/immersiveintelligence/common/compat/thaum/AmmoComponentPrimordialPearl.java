package pl.pabilo8.immersiveintelligence.common.compat.thaum;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityIIChemthrowerShot;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

//import thaumcraft.api.aura.AuraHelper;

/**
 * @author Carver (carver@iiteam.net)
 * @updated 11.04.2026
 * @since 08.04.2026
 */
public class AmmoComponentPrimordialPearl extends AmmoComponent
{
	public AmmoComponentPrimordialPearl()
	{
		super("primordial_pearl", 1f, ComponentRole.SPECIAL, IIColor.fromPackedARGB(0xff3dae));
	}

	@Override

	public IngredientStack getMaterial()
	{
		Item primordialpearl = Item.REGISTRY.getObject(ThaumcraftHelper.RES_TC.with("itemeldritchobject"));
		return new IngredientStack(new ItemStack(primordialpearl, 1, 3));

		//itemeldritchobject:3 is the true name, other sources have it as primordial_pearl
	}

	@Override
	public int getSlotsTaken()
	{
		return 3;
	}

	@Override

	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentSize, float multiplier, Entity owner)
	{
		float radius = multiplier*10;
		IIAmmoUtils.applyEMPEffect(world, new BlockPos(pos), radius, (int)(2000000*multiplier));

		BlockPos ppos = new BlockPos(pos);
		new IIExplosion(world, owner, pos, dir, 30*componentSize, 50*multiplier, shape, false, componentSize > 0.125f, false)
				.doExplosion();

		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(50*multiplier)).toArray(new EntityLivingBase[0]);
		Potion fluxtaint = Potion.REGISTRY.getObject(ResLoc.of("thaumcraft:fluxtaint"));
		Potion fluxexhaust = Potion.REGISTRY.getObject(ResLoc.of("thaumcraft:visexhaust"));
		Potion fluxexhaustinfect = Potion.REGISTRY.getObject(ResLoc.of("thaumcraft:visexhaust"));
		Potion unhunger = Potion.REGISTRY.getObject(ResLoc.of("thaumcraft:unnaturalhunger"));
		Potion thaummarhia = Potion.REGISTRY.getObject(ResLoc.of("thaumcraft:thaumarhia"));
		for(EntityLivingBase e : entities)
		{

			e.addPotionEffect(new PotionEffect(fluxtaint, 460, 4));
			e.addPotionEffect(new PotionEffect(fluxexhaust, 4000, 10));
			e.addPotionEffect(new PotionEffect(fluxexhaustinfect, 4000, 10));
			e.addPotionEffect(new PotionEffect(unhunger, 460, 0));
			e.addPotionEffect(new PotionEffect(thaummarhia, 120, 8));

			e.hurtResistantTime = 0;
		}

		Entity e = EntityList.createEntityByIDFromName(ResLoc.of("thaumcraft:fluxrift"), world);
		e.setPosition(pos.z, pos.y, pos.z);
		world.spawnEntity(e);
		world.spawnEntity(e);
		world.spawnEntity(e);
		world.spawnEntity(e);
		world.spawnEntity(e);

		Fluid fluid = FluidRegistry.getFluidStack("flux_goo", 10000).getFluid();
		Block fluidBlock = fluid.getBlock();

		if(world.isRemote)
			return;

		Vec3d v = new Vec3d(0, -1, 0);
		BlockPos p = new BlockPos(pos);
		Vec3d throwerPos = new Vec3d(p.offset(EnumFacing.UP, 3));

		if(multiplier >= 0.5&&fluid.canBePlacedInWorld())
			for(int i = 0; i < 5; i++)
				if(world.isAirBlock(p.up(i)))
					world.setBlockState(p.up(i), fluid.getBlock().getDefaultState());
		for(int i = 0; i < 100*multiplier; i++)
		{
			Vec3d vecDir = v.addVector(Utils.RAND.nextGaussian()*.25f, Utils.RAND.nextGaussian()*.25f, Utils.RAND.nextGaussian()*.25f);

			world.spawnEntity(
					new EntityIIChemthrowerShot(world, throwerPos.x+v.x*2, throwerPos.y+v.y*2,
							throwerPos.z+v.z*2, 0, 0, 0, new FluidStack(fluid, (int)(multiplier*1000)))
							.withMotion(vecDir.x*2, vecDir.y*0.05f, vecDir.z*2)
			);
			EntityIIChemthrowerShot shot = new EntityIIChemthrowerShot(world, throwerPos.x+v.x*2, throwerPos.y+v.y*2,
					throwerPos.z+v.z*2, 0, 0, 0, new FluidStack(fluid, (int)(multiplier*1000)));
			shot.motionX = vecDir.x*2;
			shot.motionY = vecDir.y*0.05f;
			shot.motionZ = vecDir.z*2;
			world.spawnEntity(shot);

			//12.04.2026 Carver: made use of TC's own Aurahelper to affect aura.

			//	AuraHelper.polluteAura(world, pos, 500.0F, true);
			//	AuraHelper.drainVis(world, pos, 10000.0F, false);
		}
	}
}
