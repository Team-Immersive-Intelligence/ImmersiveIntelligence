package pl.pabilo8.immersiveintelligence.common.compat.thaum;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityGasCloud;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import java.util.Set;

/**
 * @author Carver (carver@iiteam.net)
 * @updated 11.04.2026
 * @since 08.04.2026
 */
public class AmmoComponentBottledTaint extends AmmoComponent
{
	public AmmoComponentBottledTaint()
	{
		super("bottled_taint", 1f, ComponentRole.SPECIAL, IIColor.fromPackedARGB(0x1e0026), 1);
	}

	@Override

	public IngredientStack getMaterial()
	{
		Item bottledtaint = Item.REGISTRY.getObject(ThaumcraftHelper.RES_TC.with("bottle_taint"));
		return new IngredientStack(new ItemStack(bottledtaint, 1));
	}


	//12.04.2026 Carver: took some inspirations Thaumic Wonders Hexamite and used TC's Aurahelper to actually affect aura and flux directly.
	// Makes explosion more refined and actually shreds aura.
	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float size, float multiplier, Entity owner)
	{
		BlockPos ppos = new BlockPos(pos);

		//AuraHelper.polluteAura(world, pos, 100.0F, true);
		//AuraHelper.drainVis(world, pos, 1000.0F, false);

		Set<BlockPos> blocks = IIUtils.getBlocksInOrb(world, new BlockPos(pos), 6*size);
		Fluid fluid = FluidRegistry.getFluidStack("flux_goo", 10000).getFluid();
		IBlockState state = Block.REGISTRY.getObject(ThaumcraftHelper.RES_TC.with("taint_fibre")).getDefaultState();
		for(BlockPos firePos : blocks)
		{
			if(world.isAirBlock(firePos)&&world.getBlockState(firePos.down()).isTopSolid())
				world.setBlockState(firePos, state);
		}
		world.setBlockState(ppos.up(), fluid.getBlock().getDefaultState());
		for(EnumFacing horizontal : EnumFacing.HORIZONTALS)
			world.setBlockState(ppos.up().offset(horizontal), fluid.getBlock().getDefaultState());

		Vec3d vecDir = new Vec3d(1, 0, 0).add(dir);
		EntityGasCloud gasCloud = new EntityGasCloud(world, pos.x+vecDir.x, pos.y+dir.y+1f, pos.z+vecDir.z,
				new FluidStack(fluid, (int)(multiplier*2000)));
		world.spawnEntity(gasCloud);
	}
}
