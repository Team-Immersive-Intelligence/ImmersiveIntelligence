package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTFluid;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTItem;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityChemicalBath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 17.03.2026
 * @ii-approved 0.3.1
 * @since 21.06.2019
 */
@RegisteredTileRenderer(name = "multiblock/chemical_bath", clazz = TileEntityChemicalBath.class)
public class ChemicalBathRenderer extends IIMultiblockRenderer<TileEntityChemicalBath>
{
	private AMTModel model;
	private AMTFluid fluid;
	private AMTItem item;
	private IIAnimationCompiledMap work;

	@Override
	public void drawAnimated(TileEntityChemicalBath te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Set model values
		float productionProgress = te.getProductionProgress(te.currentProcess, partialTicks);
		float tankAmount = te.tank.getFluidAmount();
		fluid.withFluid(te.tank.getFluid());
		item.setStack(ItemStack.EMPTY);

		//Calculate fluid height based on recipe progress
		if(te.currentProcess!=null)
		{
			fluid.withFluid(te.currentProcess.recipe.fluidInput);
			tankAmount += te.currentProcess.recipe.fluidInput.amount*(1f-productionProgress);
			item.setStack(te.currentProcess.recipe.itemInput.getExampleStack(), te.currentProcess.recipe.itemOutput);
		}
		fluid.withLevel(tankAmount/(float)te.tank.getCapacity());

		//Apply animation
		work.apply(productionProgress);

		//Draw
		applyStandardMirroring(te, true);
		model.render(tes, buf);
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		model.defaultize();
		model.render(tes, buf);
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		this.model = new AMTModel(state, model, header -> new AMT[]{
				this.item = new AMTItem("item", header),
				this.fluid = new AMTFluid("fluid", header)
						.withFluidLayer(0, 0, 5, 46, 36)
						.withFluidLayer(7, 0, 0, 46, 46)
						.withFluidLayer(12, 0, 0, 46, 46)
						.withFluid(new FluidStack(IIContent.fluidSulfuricAcid, 1000))
						.withLevel(1f)
		});
		this.work = IIAnimationCompiledMap.create(this.model, IIReference.RES_II.with("chemical_bath/work"));
	}

	@Override
	protected void nullifyModels()
	{
		super.nullifyModels();
		AMTUtils.disposeOf(this.model);
	}
}
