package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.ConveyorDirection;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorBelt;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.models.ModelConveyor;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal.MultiblockProcess;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal.MultiblockProcessInWorld;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.CullFace;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.IBullet;
import pl.pabilo8.immersiveintelligence.api.crafting.CasingFillerRecipe;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelCasingFiller;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityCasingFiller;

import java.util.List;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class CasingFillerRenderer extends TileEntitySpecialRenderer<TileEntityCasingFiller> implements IReloadableModelContainer<CasingFillerRenderer>
{
	private static ModelCasingFiller model, modelFlipped;
	private static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/casing_filler.png";

	@Override
	public void render(TileEntityCasingFiller te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			ClientUtils.bindTexture(TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+1, (float)y-2, (float)z+2);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			float ff = ((te.getWorld().getTotalWorldTime()+partialTicks)%40)/40f;
			float shift[] = new float[te.processQueue.size()];
			float fill[] = new float[te.processQueue.size()];
			for(int i = 0; i < shift.length; i++)
			{
				MultiblockProcess process = te.processQueue.get(i);
				if(process==null)
					continue;
				float transportTime = 52.5f/120f;
				float fProcess = (process.processTick+(te.shouldRenderAsActive()?partialTicks: 0))/(float)process.maxTicks;

				if(fProcess < transportTime)
					shift[i] = fProcess/transportTime*.5f;
				else if(fProcess < (1-transportTime))
					shift[i] = .5f;
				else
					shift[i] = .5f+(fProcess-(1-transportTime))/transportTime*.5f;

				fill[i] = fProcess>0.45?Math.min((fProcess-0.45f)/0.1f,1f):0f;
			}
			if(te.hasWorld())
			{
				Vec3i offset = (te.facing.rotateYCCW()).getDirectionVec();
				if(te.mirrored)
				{
					GlStateManager.scale(-1f,1f,1f);
					GlStateManager.cullFace(CullFace.FRONT);
				}
				else
					GlStateManager.translate(offset.getX(), 0, offset.getZ());
			}

			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 1f, 1f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
			}

			model.getBlockRotation(te.facing.getOpposite(), te.mirrored);
			for(ModelRendererTurbo mod : model.baseModel)
				mod.render();
			GlStateManager.pushMatrix();
			GlStateManager.translate(26F/16f, 38F/16f, -43F/16f);
			GlStateManager.rotate(360*ff,0,0,1);
			for(ModelRendererTurbo mod : model.fanModel)
				mod.render();
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			ClientUtils.bindTexture("textures/atlas/blocks.png");
			GlStateManager.translate(0f, 1f, -1f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", EnumFacing.SOUTH);
			GlStateManager.translate(0f, 0f, -1f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", EnumFacing.SOUTH);
			GlStateManager.translate(0f, 0f, -1f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", EnumFacing.SOUTH);

			GlStateManager.popMatrix();
			GlStateManager.scale(-1,1,1);
			GlStateManager.cullFace(CullFace.FRONT);
			for(int i = 0; i < shift.length; i++)
			{
				MultiblockProcess<CasingFillerRecipe> process = te.processQueue.get(i);
				if(!(process instanceof MultiblockProcessInWorld))
					continue;
				GlStateManager.pushMatrix();
				GlStateManager.translate(-0.5, 1, -3*(1f-shift[i]));
				IBullet bullet = process.recipe.getBullet();
				if(bullet!=null)
				{
					IBulletModel iBulletModel = BulletRegistry.INSTANCE.registeredModels.get(bullet.getName());
					if(iBulletModel!=null)
						iBulletModel.renderCasing(MathHelper.clamp(fill[i],0,1),-1);
				}
				else
				{
					ItemStack stack = process.recipe.getItemInputs().get(0).getExampleStack();
					if(!stack.isEmpty())
					{
						GlStateManager.rotate(-90, 1, 0, 0);
						float scale = .625f;
						GlStateManager.scale(scale, scale, 1);
						ClientUtils.mc().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
						GlStateManager.popMatrix();
					}
				}

				GlStateManager.popMatrix();
			}
			GlStateManager.cullFace(CullFace.BACK);

			GlStateManager.popMatrix();
		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelCasingFiller();
		modelFlipped = new ModelCasingFiller();
		modelFlipped.flipAllZ();
	}
}
