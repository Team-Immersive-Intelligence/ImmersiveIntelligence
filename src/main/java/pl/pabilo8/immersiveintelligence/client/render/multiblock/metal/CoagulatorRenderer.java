package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Coagulator;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.api.crafting.CoagulatorRecipe;
import pl.pabilo8.immersiveintelligence.client.model.metal_device.ModelCraneElectric;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelCoagulator;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityCoagulator;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityCoagulator.CraneAnimation;

import javax.annotation.Nullable;
import java.util.Arrays;

import static pl.pabilo8.immersiveintelligence.client.IIClientUtils.drawRope;

/**
 * @author Pabilo8
 * @since 21-06-2019
 */
public class CoagulatorRenderer extends TileEntitySpecialRenderer<TileEntityCoagulator> implements IReloadableModelContainer<CoagulatorRenderer>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation("immersiveintelligence:textures/blocks/multiblock/coagulator.png");
	private static final ResourceLocation TEXTURE_CRANE = new ResourceLocation("immersiveintelligence:textures/blocks/multiblock/emplacement/crane.png");
	private static ModelCoagulator model;
	private static ModelCoagulator modelFlipped;
	private static ModelCraneElectric modelCrane;

	@Override
	public void render(@Nullable TileEntityCoagulator te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			IIClientUtils.bindTexture(TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x, (float)y, (float)z);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			partialTicks *= te.energyStorage.getEnergyStored() > 0?1: 0;

			float f = (getWorld().getTotalWorldTime()%80+partialTicks)/80f;
			float mixerProgress = 0, cranePickup = 0;
			float craneRotate = ((te.craneAnimation.ordinal() > CraneAnimation.ROTATE_IN.ordinal()&&te.craneAnimation.ordinal() < CraneAnimation.ROTATE_OUT.ordinal())?1: 0);
			float bucketHandle = 1;
			double cartProgress = te.cranePosition/6d;
			float tankProgressLeft = 0, tankProgressRight = 0;
			float[] bucketProgress = new float[6];

			for(int i = 0; i < 6; i++)
				bucketProgress[i] = te.bucketStacks.get(i).isEmpty()?0: ((te.bucketProgress[i]+partialTicks)/(float)CoagulatorRecipe.getBucketProgressForStack(te.bucketStacks.get(i)));

			if(!te.processQueue.isEmpty())
			{
				double p = (te.processQueue.get(0).processTick+partialTicks)/(float)te.processQueue.get(0).maxTicks;

				if(p > 0.7f)
					tankProgressLeft = 1f-(float)MathHelper.clamp((p-0.7)/0.1, 0, 1);
				else
					tankProgressLeft = (float)MathHelper.clamp(p/0.2, 0, 1);

				if(p > 0.8f)
					tankProgressRight = 1f-(float)MathHelper.clamp((p-0.8)/0.1, 0, 1);
				else if(p > 0.15f)
					tankProgressRight = (float)MathHelper.clamp((p-0.15)/0.2, 0, 1);

				mixerProgress = p > 0.65?f: 0;
			}
			if(te.effect.get(0).getCount() > 0)
				mixerProgress = f;

			switch(te.craneAnimation)
			{
				default:
				case NONE:
					break;
				case MOVE_BUCKET:
				case MOVE_BACK:
				{
					cranePickup = 0;
					cartProgress = (te.cranePosition-Integer.compare(te.cranePosition, te.craneBucket)+
							((((te.craneProgress-partialTicks)/(double)Coagulator.craneMoveTime))*Integer.compare(te.cranePosition, te.craneBucket))
					)/6d;
				}
				break;
				case MOVE_MIXER:
				{
					cartProgress = (te.cranePosition-Integer.compare(te.cranePosition, 2)+
							((((te.craneProgress-partialTicks)/(double)Coagulator.craneMoveTime))*Integer.compare(te.cranePosition, 2))
					)/6d;
				}
				break;
				case PICK:
				case PULL:
				case RETURN:
				{
					cranePickup = (te.craneProgress-partialTicks)/(float)Coagulator.craneGrabTime;
				}
				break;
				case ROTATE_IN:
					craneRotate = 1f-((te.craneProgress-partialTicks)/(float)Coagulator.craneMoveTime);
					break;
				case ROTATE_OUT:
					craneRotate = 1f-(1f-((te.craneProgress-partialTicks)/(float)Coagulator.craneMoveTime));
					break;
				case PUT:
				case REACH:
				case PLACE:
				{
					cranePickup = 1f-((te.craneProgress-partialTicks)/(float)Coagulator.craneGrabTime);
				}
				break;
			}

			if(te.craneAnimation==CraneAnimation.PICK)
				bucketHandle = 1f-MathHelper.clamp(((cranePickup)-0.5f)/0.5f, 0, 1);
			else if(te.craneAnimation==CraneAnimation.RETURN)
				bucketHandle = cranePickup;


			model.getBlockRotation(te.facing, te.mirrored);
			ModelCoagulator modelCurrent = te.mirrored?modelFlipped: model;

			for(ModelRendererTurbo mod : modelCurrent.baseModel)
				mod.render();
			for(ModelRendererTurbo mod : modelCurrent.starterSlideModel)
				mod.render();

			GlStateManager.pushMatrix();
			GlStateManager.translate(te.mirrored?3.5f: -3.5f, 0, -2.5625f);
			GlStateManager.rotate(mixerProgress*360, 0, 1, 0);
			for(ModelRendererTurbo mod : modelCurrent.mixerModel)
				mod.render();
			GlStateManager.popMatrix();

			drawTank(true, te.mirrored, tankProgressRight, modelCurrent, te.tanks[0].getFluid());
			drawTank(false, te.mirrored, tankProgressLeft, modelCurrent, te.tanks[1].getFluid());

			GlStateManager.pushMatrix();
			GlStateManager.translate(te.mirrored?0.5: -0.5, 0, 0);

			for(int i = 0; i < 6; i++)
			{
				float pp = bucketProgress[i];

				GlStateManager.pushMatrix();
				for(ModelRendererTurbo mod : modelCurrent.bucketRailModel)
					mod.render();
				for(ModelRendererTurbo mod : modelCurrent.bucketTimerModel)
				{
					mod.rotateAngleZ = pp*6.14f;
					mod.render();
				}


				if(te.bucketProgress[i] > 0&&te.bucketProgress[i] < 80)
				{
					double bp = 1d-Math.abs((((te.bucketProgress[i]-partialTicks)/80d))-0.5d)*2;
					GlStateManager.translate(0.15625f, -0.1875f, 0.59375f+(bp*0.5f));
					GlStateManager.rotate((float)(Math.max(bp-0.5d, 0)/0.5d)*125, 1, 0, 0);
					GlStateManager.translate(-0.15625f, 0.1875f, -0.59375f);
				}

				GlStateManager.pushMatrix();
				for(ModelRendererTurbo mod : modelCurrent.bucketHolderModel)
					mod.render();
				GlStateManager.translate(0f, 0, -0.09375);
				if(te.craneAnimation.ordinal() < CraneAnimation.PICK.ordinal()||te.craneAnimation.ordinal() > CraneAnimation.PLACE.ordinal()||te.craneBucket!=i)
				{
					for(ModelRendererTurbo mod : modelCurrent.bucketModel)
						mod.render();
					GlStateManager.translate(0, 9f/16f, 11.5f/16f);

					if((te.craneAnimation==CraneAnimation.RETURN||te.craneAnimation==CraneAnimation.PICK)&&te.craneBucket==i)
					{
						GlStateManager.pushMatrix();
						GlStateManager.rotate(-124*bucketHandle, 1, 0, 0);
						for(ModelRendererTurbo mod : modelCurrent.bucketHandleModel)
							mod.render();
						GlStateManager.popMatrix();
					}
					else
						for(ModelRendererTurbo mod : modelCurrent.bucketHandleModel)
							mod.render();

				}
				GlStateManager.popMatrix();


				GlStateManager.popMatrix();

				GlStateManager.translate(te.mirrored?1: -1, 0, 0);
			}
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate((cartProgress*(te.mirrored?6: -6)), 0, 0);
			for(ModelRendererTurbo mod : modelCurrent.cartModel)
				mod.render();
			for(ModelRendererTurbo mod : modelCurrent.cartWheelsModel)
			{
				mod.rotateAngleZ = ((float)(cartProgress%0.2f)/0.2f)*6.28f;
				mod.render();
			}
			GlStateManager.translate(te.mirrored?0.5: -0.5, 1.0625, -0.4375);
			final float bh = bucketHandle;
			modelCrane.renderCrane(TEXTURE_CRANE, craneRotate*-180, 0.5f+(0.125f*bh), -0.25f+cranePickup, 0,
					(te.craneAnimation.ordinal() < CraneAnimation.PICK.ordinal()||te.craneAnimation.ordinal() > CraneAnimation.PLACE.ordinal())?
							() -> {
							}:
							() -> {
								IIClientUtils.bindTexture(TEXTURE);
								GlStateManager.translate(0, -0.125f+(bh*-0.25f), bh*0.25);
								GlStateManager.rotate(180f, 0, 1, 0);
								for(ModelRendererTurbo mod : modelFlipped.bucketModel)
									mod.render();

								GlStateManager.pushMatrix();
								GlStateManager.translate(0, 9f/16f, 11.5f/16f);
								GlStateManager.rotate(-124*bh, 1, 0, 0);
								for(ModelRendererTurbo mod : modelCurrent.bucketHandleModel)
									mod.render();
								GlStateManager.popMatrix();

								if(te.craneAnimation.ordinal() > CraneAnimation.PUT.ordinal())
								{
									GlStateManager.translate(-0.5, 0.5625f, 0.09375);
									GlStateManager.rotate(90, 1f, 0f, 0f);
									GlStateManager.scale(0.0625, 0.0625, 0.0625);
									ClientUtils.drawRepeatedFluidSprite(CoagulatorRecipe.getFluidForOutputStack(te.effect.get(0)), 4.5f, 5.5f, 8, 8);
								}

							}
			);

			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			GlStateManager.disableColorMaterial();
			GlStateManager.disableLighting();

			GlStateManager.popMatrix();

			ItemStack effect = te.effect.get(0);
			if(!effect.isEmpty())
			{
				GlStateManager.pushMatrix();

				GlStateManager.rotate(90, 1f, 0f, 0f);
				GlStateManager.translate(te.mirrored?2: -5, -4, 0);
				GlStateManager.scale(0.0625, 0.0625, 0.0625);

				float tfluid = (((float)effect.getCount())/effect.getMaxStackSize());

				GlStateManager.translate(0, 0f, -1-tfluid*24f);

				ClientUtils.drawRepeatedFluidSprite(CoagulatorRecipe.getFluidForOutputStack(effect),
						2, 2, 44, 44
				);
				GlStateManager.popMatrix();
			}

			GlStateManager.pushMatrix();

			GlStateManager.translate(0, 0.5625f, 0);
			GlStateManager.rotate(90, 1f, 0f, 0f);
			GlStateManager.translate(te.mirrored?0: -1, 0, 0);
			GlStateManager.scale(0.0625, 0.0625, 0.0625);

			for(int i = 0; i < 6; i++)
			{
				boolean b = (te.craneBucket==i&&te.craneAnimation==CraneAnimation.RETURN);
				if((te.craneBucket!=i&&!te.bucketStacks.get(i).isEmpty())||b)
				{
					GlStateManager.pushMatrix();

					if(te.bucketProgress[i] > 0&&te.bucketProgress[i] < 80)
					{
						double bp = 1d-Math.abs((((te.bucketProgress[i]+partialTicks)/80d))-0.5d)*2;
						GlStateManager.translate(2.5f, -3.0f, 9.5f+(bp*8));
						GlStateManager.rotate((float)(Math.max(bp-0.5d, 0)/0.5d)*125, 1, 0, 0);
						GlStateManager.translate(-2.5f, 3f, -9.5f);
					}

					ClientUtils.drawRepeatedFluidSprite(CoagulatorRecipe.getFluidForOutputStack(b?te.effect.get(0): te.bucketStacks.get(i)), 4.5f, 5.5f, 8, 8);
					GlStateManager.popMatrix();
				}

				GlStateManager.translate(te.mirrored?16: -16, 0, 0);
			}
			GlStateManager.popMatrix();
			GlStateManager.disableBlend();

			drawPowerline(te.mirrored);

			GlStateManager.popMatrix();
		}
		else if(te==null)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(x-0.25, y-0.25, z);
			GlStateManager.rotate(7.5f, 0, 0, 1);
			GlStateManager.rotate(-7.5f, 1, 0, 0);
			GlStateManager.scale(0.23, 0.23, 0.23);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			IIClientUtils.bindTexture(TEXTURE);

			// TODO: 31.10.2021 actually render
			for(ModelRendererTurbo mod : model.baseModel)
				mod.render();

			GlStateManager.popMatrix();
		}
	}

	private void drawTank(boolean rightSide, boolean mirrored, float progress, ModelCoagulator modelCurrent, @Nullable FluidStack fs)
	{
		float tankProgress, valveProgress, fluidProgress;
		tankProgress = Math.min(progress/0.7f, 1f);
		valveProgress = MathHelper.clamp((progress-0.7f)/0.2f, 0f, 1f);
		fluidProgress = MathHelper.clamp((progress-0.9f)/0.1f, 0f, 1f);

		GlStateManager.pushMatrix();
		for(ModelRendererTurbo mod : rightSide?modelCurrent.pistonsRightModel: modelCurrent.pistonsLeftModel)
			mod.render();

		if(rightSide)
			GlStateManager.translate((mirrored?0.875f: -0.875f), 3.625f, 0);
		else
			GlStateManager.translate((mirrored?5.8125f: -5.8125f), 3.625f, 0);

		GlStateManager.rotate((rightSide^mirrored?25: -25)*tankProgress, 0, 0, 1);
		for(ModelRendererTurbo mod : rightSide?modelCurrent.tankRightModel: modelCurrent.tankLeftModel)
			mod.render();

		if(rightSide)
			GlStateManager.translate((mirrored?1.34375: -1.34375), 0.1875, 0);
		else
			GlStateManager.translate((mirrored?-1: 1), 0.1875, 0);

		GlStateManager.pushMatrix();
		GlStateManager.translate(0, -0.4375, -3.25+0.0625f);
		GlStateManager.scale(0.0625f, 0.0625f, 0.0625f);

		if(!rightSide)
		{
			GlStateManager.rotate(180, 0, 1, 0);
			GlStateManager.translate(0, 0, -6);
		}
		drawFluidStream(fs, fluidProgress);

		IIClientUtils.bindTexture(TEXTURE);
		GlStateManager.popMatrix();

		GlStateManager.rotate((rightSide^mirrored?-95: 95)*valveProgress, 0, 0, 1);
		for(ModelRendererTurbo mod : rightSide?modelCurrent.valveRightModel: modelCurrent.valveLeftModel)
			mod.render();
		GlStateManager.popMatrix();
	}

	private void drawFluidStream(@Nullable FluidStack fs, float v)
	{
		if(v==0||fs==null)
			return;

		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		GlStateManager.disableCull();

		for(int i = 0; i < 2*v; i++)
		{
			GlStateManager.rotate(30, 0, 0, 1);
			GlStateManager.translate(-11, 0, 0);
			GlStateManager.pushMatrix();

			GlStateManager.rotate(-90, 0, 0, 1);
			GlStateManager.translate(-6, 0, 0);
			IIClientUtils.drawFluidBlock(fs, true, 0, -1-(v*4), 0, 6, 16-(v*2), 6, true);

			GlStateManager.popMatrix();
			GlStateManager.translate(-4, -1, 0.05);
		}

		GlStateManager.enableCull();
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
	}

	private void drawPowerline(boolean mirrored)
	{
		float yMod = mirrored?-1: 1;

		GlStateManager.translate(0, 1.425, 0.75);
		GlStateManager.rotate(90, 0, 0, 1);
		ClientUtils.mc().getTextureManager().bindTexture(new ResourceLocation("immersiveengineering:textures/blocks/wire.png"));
		GlStateManager.disableCull();
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		GlStateManager.color(0.7721569f, 0.46156865f, 0.27176473f);

		drawRope(buffer, 0, 0, -1.59375, -0.03125, 1.75*yMod, -1.59375, 0.046875, 0);
		drawRope(buffer, 0, 0, -1.59375, -0.03125, 1.75*yMod, -1.59375, 0, 0.046875);
		drawRope(buffer, -0.03125, 1.75*yMod, -1.59375, 0, 3.5*yMod, -1.59375, 0.046875, 0);
		drawRope(buffer, -0.03125, 1.75*yMod, -1.59375, 0, 3.5*yMod, -1.59375, 0, 0.046875);

		drawRope(buffer, 0, 3.5*yMod, -1.59375, -0.03125, 4.75*yMod, -1.59375, 0.046875, 0);
		drawRope(buffer, 0, 3.5*yMod, -1.59375, -0.03125, 4.75*yMod, -1.59375, 0, 0.046875);
		drawRope(buffer, -0.03125, 4.75*yMod, -1.59375, 0, 6*yMod, -1.59375, 0.046875, 0);
		drawRope(buffer, -0.03125, 4.75*yMod, -1.59375, 0, 6*yMod, -1.59375, 0, 0.046875);


		Tessellator.getInstance().draw();
		GlStateManager.enableCull();
		GlStateManager.color(1f, 1f, 1f);
	}

	@Override
	public void reloadModels()
	{
		model = new ModelCoagulator();
		for(ModelRendererTurbo mod : new ModelRendererTurbo[]{model.baseModel[156], model.baseModel[157], model.baseModel[158], model.baseModel[159]})
		{
			mod.doMirror(true, false, false);
			mod.rotationPointZ = -mod.rotationPointZ-80;
		}
		model.flipAllX();
		//ah, yes, the l a m b d a s
		model.parts.values().forEach(mods -> Arrays.stream(mods).filter(mm -> mm.flip).forEach(e -> e.rotationPointY *= -1));

		modelFlipped = new ModelCoagulator();

		modelCrane = new ModelCraneElectric();
	}


}
