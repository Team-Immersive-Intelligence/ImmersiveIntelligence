package pl.pabilo8.immersiveintelligence.common.compat.thaum;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityGasCloud;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

//import thaumcraft.api.aura.AuraHelper;

public class AmmoComponentBottledTaint extends AmmoComponent

{
	public AmmoComponentBottledTaint()
	{
		super("bottled taint", 1f, ComponentRole.SPECIAL, IIColor.fromPackedARGB(0x1e0026));
	}

	@Override

	public IngredientStack getMaterial()
	{
		Item bottledtaint = Item.REGISTRY.getObject(new ResourceLocation("thaumcraft", "bottle_taint"));
		return new IngredientStack(new ItemStack(bottledtaint, 1));
		//ItemBottleTaint?
	}

	@Override

	//12.04.2026 Carver: took some inspirations Thaumic Wonders Hexamite and used TC's Aurahelper to actually affect aura and flux directly.
	// Makes explosion more refined and actually shreds aura.

	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float size, float multiplier, Entity owner)
	{
		BlockPos ppos = new BlockPos(pos);

		//11.04.2026 Carver: Should spawn 1 bottle, and a gas cloud of flux goo.

		Entity e = EntityList.createEntityByIDFromName(ResLoc.of("thaumcraft:EntityBottleTaint"), world);
		e.setPosition(pos.z, pos.y, pos.z);
		world.spawnEntity(e);

		//AuraHelper.polluteAura(world, pos, 100.0F, true);
		//AuraHelper.drainVis(world, pos, 1000.0F, false);

		Fluid fluid = FluidRegistry.getFluidStack("flux_goo", 10000).getFluid();

		for(int i = 0; i < 50*multiplier; i++)
		{
			Vec3d vecDir = new Vec3d(1, 0, 0).rotateYaw(i/(50f*multiplier)*360f).add(dir);

			EntityGasCloud gasCloud = new EntityGasCloud(world, pos.x+vecDir.x, pos.y+dir.y+1f, pos.z+vecDir.z,
					new FluidStack(fluid, (int)(multiplier*5000)));
			world.spawnEntity(gasCloud);
		}
	}
}
