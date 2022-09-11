package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal.MultiblockProcess;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal.MultiblockProcessInWorld;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.CullFace;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.api.crafting.FillerRecipe;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelFiller;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFiller;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class FillerRenderer extends TileEntitySpecialRenderer<TileEntityFiller> implements IReloadableModelContainer<FillerRenderer>
{
	private static ModelFiller model, modelFlipped;
	private static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/casing_filler.png";

	@Override
	public void render(TileEntityFiller te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			ClientUtils.bindTexture(TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+1, (float)y-2, (float)z+2);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			float ff = ((te.getWorld().getTotalWorldTime())%40)/40f+(partialTicks/40f);
			double[] shift = new double[te.processQueue.size()];
			double[] fill = new double[te.processQueue.size()];
			for(int i = 0; i < shift.length; i++)
			{
				MultiblockProcess<FillerRecipe> process = te.processQueue.get(i);
				if(process==null)
					continue;
				float transportTime = 52.5f/120f;
				double fProcess = (process.processTick-(te.shouldRenderAsActive()?partialTicks: 0))/(double)process.maxTicks;

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

			//legacy filler compat, due to migration to nbt structure files
			if(te.pos==10)
				model.getLegacyBlockRotation(te.facing.getOpposite(), te.mirrored);
			else
				model.getBlockRotation(te.facing, te.mirrored);

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
				MultiblockProcess<FillerRecipe> process = te.processQueue.get(i);
				if(!(process instanceof MultiblockProcessInWorld))
					continue;
				GlStateManager.pushMatrix();
				GlStateManager.translate(-0.5, 1.125f, -3*(1f-shift[i]));
				IAmmo bullet = process.recipe.getBullet();
				if(bullet!=null)
				{
					IBulletModel iBulletModel = AmmoRegistry.INSTANCE.registeredModels.get(bullet.getName());
					if(iBulletModel!=null)
						iBulletModel.renderCasing((float)MathHelper.clamp(fill[i],0,1),-1);
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
		model = new ModelFiller();
		modelFlipped = new ModelFiller();
		modelFlipped.flipAllZ();
	}
}
