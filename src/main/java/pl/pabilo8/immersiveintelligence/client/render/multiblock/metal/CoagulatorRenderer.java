package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.crafting.CoagulatorRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.CoagulatorRecipe.DryingInformation;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTFluid;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTLocator;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTWire;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Coagulator;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockCoagulator;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityCoagulator;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.04.2026
 * @ii-approved 0.3.1
 * @since 21.06.2019
 */
@RegisteredTileRenderer(name = "multiblock/coagulator", clazz = TileEntityCoagulator.class)
public class CoagulatorRenderer extends IIMultiblockRenderer<TileEntityCoagulator>
{
	private AMTModel model;
	private IIAnimationCompiledMap work, traverse, fillBucket;
	private IIAnimationCompiledMap[] takeBucket, putBucket;
	private IIAnimationCompiledMap[] spill, timer;
	private AMTFluid fluidTank, stream1, stream2, fluidBucket;
	private AMTFluid[] bucketFluids;

	@Override
	public void drawAnimated(TileEntityCoagulator te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		model.defaultize();
		int bucketIndex = MathHelper.clamp(te.craneCurrentBucket, 0, 5);
		float roadProgress = MathHelper.clamp(te.cranePosition/6f, 0f, 1f);

		//Bucket drying animations
		for(int i = 0; i < 6; i++)
		{
			DryingInformation dryingInformation = CoagulatorRecipe.getDryingInformationFor(te.bucketStacks.get(i));
			float drying = te.getDryingProgressForSlot(i, partialTicks);

			timer[i].apply(drying);
			spill[i].apply(drying > 0?AMTUtils.getAnimationProgress(20-te.bucketProgress[i], 20f, true, -partialTicks): 0);
			if(te.bucketStacks.get(i).isEmpty())
			{
				bucketFluids[i].withFluid(null);
				bucketFluids[i].withLevel(0f);
			}
			else
			{
				bucketFluids[i].withFluid(dryingInformation.getFluid());
				bucketFluids[i].withLevel(1f);
			}
		}

		takeBucket[bucketIndex].apply(0f);
		switch(te.craneAnimation)
		{
			case NONE:
				break;
			case MOVE_TO_BUCKET:
			{
				if(te.craneCurrentBucket!=te.cranePosition)
					roadProgress += AMTUtils.getAnimationProgress(te.craneAnimationProgress, Coagulator.craneMoveTime, true, partialTicks)
							*Math.signum(te.craneCurrentBucket-te.cranePosition)*(1/6f);
			}
			break;
			case PICK_BUCKET:
			{
				takeBucket[bucketIndex].apply(AMTUtils.getAnimationProgress(te.craneAnimationProgress, Coagulator.craneBucketActionTime, true, partialTicks));
			}
			break;
			case MOVE_TO_MIXER:
			{
				takeBucket[bucketIndex].apply(1f);
				if(te.cranePosition!=1)
					roadProgress += AMTUtils.getAnimationProgress(te.craneAnimationProgress, Coagulator.craneMoveTime, true, partialTicks)
							*Math.signum(1-te.cranePosition)*(1/6f);
			}
			break;
			case FILL_BUCKET:
			{
				takeBucket[bucketIndex].apply(1f);
				fluidBucket.withFluid(CoagulatorRecipe.getDryingInformationFor(te.inventory.get(MultiblockCoagulator.SLOT_OUTPUT)).getFluid());
				fillBucket.apply(AMTUtils.getAnimationProgress(te.craneAnimationProgress, Coagulator.craneBucketActionTime, true, partialTicks));
			}
			break;
			case MOVE_BACK:
			{
				fluidBucket.withFluid(CoagulatorRecipe.getDryingInformationFor(te.inventory.get(MultiblockCoagulator.SLOT_OUTPUT)).getFluid());
				fluidBucket.withLevel(1f);
				putBucket[bucketIndex].apply(0f);
				if(te.craneCurrentBucket!=te.cranePosition)
					roadProgress += AMTUtils.getAnimationProgress(te.craneAnimationProgress, Coagulator.craneMoveTime, true, partialTicks)
							*Math.signum(te.craneCurrentBucket-te.cranePosition)*(1/6f);
			}
			break;
			case PLACE_BUCKET:
			{
				FluidStack bucketStack = CoagulatorRecipe.getDryingInformationFor(te.inventory.get(MultiblockCoagulator.SLOT_OUTPUT)).getFluid();
				fluidBucket.withFluid(bucketStack);
				fluidBucket.withLevel(1f);
				bucketFluids[bucketIndex].withFluid(bucketStack);
				bucketFluids[bucketIndex].withLevel(1f);
				putBucket[bucketIndex].apply(AMTUtils.getAnimationProgress(te.craneAnimationProgress, Coagulator.craneBucketActionTime, true, partialTicks));
			}
			break;
			default:
				break;
		}
		traverse.apply(roadProgress);

		//Set production (mixing) animation
		float productionProgress = te.getProductionProgress(te.currentProcess, partialTicks);
		//Fluid data
		stream1.withFluid(te.tankCoagulant.getFluid());
		stream2.withFluid(te.tankInput.getFluid());
		work.apply(productionProgress);


		int basinContents = te.inventory.get(MultiblockCoagulator.SLOT_OUTPUT).getCount();
		if(te.currentProcess!=null)
			basinContents += (int)(te.currentProcess.recipe.itemOutput.getCount()*productionProgress);
		fluidTank.withFluid(te.tankInput.getFluid());
		fluidTank.withLevel(basinContents/(float)te.getSlotLimit(MultiblockCoagulator.SLOT_OUTPUT));


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
		this.bucketFluids = new AMTFluid[6];
		this.model = new AMTModel(state, model, header -> new AMT[]{
				fluidTank = new AMTFluid("fluid_tank", header),
				stream1 = new AMTFluid("fuel1", header),
				stream2 = new AMTFluid("fuel2", header),
				new AMTLocator("rotate", header),
				new AMTWire("wire1", header),
				new AMTWire("wire2", header),
				fluidBucket = new AMTFluidSolidifying("fluid_bucket", header),
				bucketFluids[0] = new AMTFluidSolidifying("fluid1", header),
				bucketFluids[1] = new AMTFluidSolidifying("fluid2", header),
				bucketFluids[2] = new AMTFluidSolidifying("fluid3", header),
				bucketFluids[3] = new AMTFluidSolidifying("fluid4", header),
				bucketFluids[4] = new AMTFluidSolidifying("fluid5", header),
				bucketFluids[5] = new AMTFluidSolidifying("fluid6", header)
		});
		this.work = IIAnimationCompiledMap.create(this.model, IIReference.RES_II.with("coagulator/work"));
		this.traverse = IIAnimationCompiledMap.create(this.model, IIReference.RES_II.with("coagulator/traverse"));
		this.fillBucket = IIAnimationCompiledMap.create(this.model, IIReference.RES_II.with("coagulator/fill_bucket"));

		this.takeBucket = new IIAnimationCompiledMap[6];
		this.putBucket = new IIAnimationCompiledMap[6];
		this.spill = new IIAnimationCompiledMap[6];
		this.timer = new IIAnimationCompiledMap[6];

		for(int i = 0; i < 6; i++)
		{
			final int idx = i+1;
			this.takeBucket[i] = IIAnimationCompiledMap.create(this.model, IIReference.RES_II.with("coagulator/take_bucket/take_bucket"+idx));
			this.putBucket[i] = IIAnimationCompiledMap.create(this.model, IIReference.RES_II.with("coagulator/put_bucket/put_bucket"+idx));
			this.spill[i] = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "coagulator/spill/spill"+idx));
			this.timer[i] = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "coagulator/timer/timer"+idx));
		}
	}

	@Override
	protected void nullifyModels()
	{
		super.nullifyModels();
		AMTUtils.disposeOf(this.model);
	}

	private static class AMTFluidSolidifying extends AMTFluid
	{
		public AMTFluidSolidifying(String name, AMTModelHeader header)
		{
			super(name, header);
		}

		@Override
		@Nonnull
		public AxisAlignedBB getBoundingBox()
		{
			return null;
		}
	}
}
