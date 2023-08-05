package pl.pabilo8.immersiveintelligence.client.model.misc;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 08.11.2022
 */
public class FluidStateMapper extends StateMapperBase implements ItemMeshDefinition
{
	public final ModelResourceLocation location;

	public FluidStateMapper(Fluid fluid)
	{
		this.location = new ModelResourceLocation(ImmersiveIntelligence.MODID+":fluid_block", fluid.getName());
	}

	@Nonnull
	@Override
	protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state)
	{
		return location;
	}

	@Nonnull
	@Override
	public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack)
	{
		return location;
	}
}
