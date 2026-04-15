package pl.pabilo8.immersiveintelligence.common.compat.tbl;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.IEPotions;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
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
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityGasCloud;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

public class AmmoComponentCremains extends AmmoComponent

/**
 * @author Carver (carver@iiteam.net)
 * @updated 14.04.2026
 */
{
//TODO: add actual texture for the fluid itself or just force the fluid to be entirely of the same color by code. Color for active gas FF500000(HEX: 0x500000)
//navigation: assets, blockstates, fluid_block. And textures>blocks>fluids. Make a monocolor fluid. Flow state does not matter as this one is not supposed to be placed in world.

	public AmmoComponentCremains()
	{
		super("cremains", 1f, ComponentRole.SPECIAL, IIColor.fromPackedARGB(0x35435d));
	}
	Fluid fluid = FluidRegistry.getFluidStack("cremain_gas", 4000).getFluid();
	Block blockGasCremainGas = fluid.getBlock();

	@Override

	public IngredientStack getMaterial()
	{
		Item cremains = Item.REGISTRY.getObject(new ResourceLocation("thebetweenlands", "cremains"));

		return new IngredientStack(new ItemStack(cremains, 1));
	}

	@Override

	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentSize, float multiplier, Entity owner)
	{

		IIPacketHandler.playRangedSound(world, pos, IISounds.explosionIncendiary, SoundCategory.NEUTRAL, (int)(40*multiplier), 1f, 1f);

		BlockPos ppos = new BlockPos(pos);

		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(10*multiplier)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
		{
			e.addPotionEffect(new PotionEffect(IEPotions.flammable, 120, 2));
			e.setFire(12);
		}

		//Spawn gas cloud with custom "cremains_gas" defined in Helper. Fluid should be unobtainable by any other means.

		if(world.isRemote)
			return;

		Vec3d v = new Vec3d(0, -1, 0);
		BlockPos p = new BlockPos(pos);
		Vec3d throwerPos = new Vec3d(p.offset(EnumFacing.UP, 3));

		EntityGasCloud gasCloud = new EntityGasCloud(world, throwerPos.x+v.x*2, throwerPos.y+v.y*2,
				throwerPos.z+v.z*2, new FluidStack(fluid, (int)(multiplier*1000)));
		world.spawnEntity(gasCloud);


	}
}
