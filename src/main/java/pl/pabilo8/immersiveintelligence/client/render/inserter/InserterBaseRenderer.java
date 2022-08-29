package pl.pabilo8.immersiveintelligence.client.render.inserter;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.animation.*;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.inserter.TileEntityInserterBase;

import java.util.function.Function;

/**
 * @author Pabilo8
 * @since 26.07.2022
 */
public abstract class InserterBaseRenderer<T extends TileEntityInserterBase> extends IITileRenderer<T>
{
	//inserter animations for all directions
	private IIAnimationCompiledMap animationFrontBack = null, animationFrontRight = null, animationFrontLeft = null, animationFrontFront = null;
	//directions + actual inserter
	private AMT[] model = null;
	//reference to model parts
	private AMT inBox, outBox, turntable;


	@Override
	public final void draw(T te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//task dependent input facing
		EnumFacing teIn = te.getCurrentInputFacing();

		//defaultize model angles
		for(AMT mod : model)
			mod.defaultize();

		//apply input box direction
		IIAnimationUtils.setModelRotation(inBox, 0, -te.defaultInputFacing.getHorizontalAngle(), 0);
		//apply output box direction
		IIAnimationUtils.setModelRotation(outBox, 0, -te.defaultOutputFacing.getHorizontalAngle(), 0);

		//if doing a task
		if(te.current!=null)
		{
			EnumFacing teOut = te.getCurrentOutputFacing();

			//calculate task duration and progress
			double duration = (double)te.getPickupSpeed()*(1+(te.current!=null?te.current.getTimeModifier(): 0));
			float totalProgress = (float)((te.pickProgress+(te.current!=null?partialTicks: 0))/duration);

			//input facing - output facing | horizontal values
			int diff = Math.abs(teIn.getHorizontalIndex()-teOut.getHorizontalIndex());
			switch(diff)
			{
				case 0:
					animationFrontFront.apply(totalProgress); //same direction
					break;
				case 1:
					//depends on side, required due to abs() making the result always positive
					(teIn.rotateY()==teOut?animationFrontRight: animationFrontLeft)
							.apply(totalProgress);
					break;
				case 2:
					animationFrontBack.apply(totalProgress); //opposite direction
					break;
			}
		}
		else
			animationFrontFront.apply(0); //apply default animation if no task is performed

		//apply inserter direction | face input
		IIAnimationUtils.addModelRotation(turntable, 0, -teIn.getHorizontalAngle(), 0);

		doAdditionalTransforms(te, buf, partialTicks, tes);

		//render
		for(AMT mod : model)
			mod.render(tes, buf);
	}

	protected abstract void doAdditionalTransforms(T te, BufferBuilder buf, float partialTicks, Tessellator tes);

	@Override
	public void compileModels(Tuple<IBlockState, IBakedModel> sModel)
	{
		model = IIAnimationUtils.getAMT(sModel, IIAnimationLoader.loadHeader(sModel.getSecond()), getAdditionalParts());
		inBox = IIAnimationUtils.getPart(model, "input");
		outBox = IIAnimationUtils.getPart(model, "output");
		turntable = IIAnimationUtils.getPart(model, "turntable");

		animationFrontBack = IIAnimationCompiledMap.create(model, new ResourceLocation(ImmersiveIntelligence.MODID, "inserter/front_back"));
		animationFrontRight = IIAnimationCompiledMap.create(model, new ResourceLocation(ImmersiveIntelligence.MODID, "inserter/front_right"));
		animationFrontLeft = IIAnimationCompiledMap.create(model, new ResourceLocation(ImmersiveIntelligence.MODID, "inserter/front_left"));
		animationFrontFront = IIAnimationCompiledMap.create(model, new ResourceLocation(ImmersiveIntelligence.MODID, "inserter/front_front"));
	}

	protected abstract Function<IIModelHeader, AMT[]> getAdditionalParts();

	@Override
	protected void nullifyModels()
	{
		model = IIAnimationUtils.disposeOf(model);
		inBox = outBox = turntable = null;
	}
}
