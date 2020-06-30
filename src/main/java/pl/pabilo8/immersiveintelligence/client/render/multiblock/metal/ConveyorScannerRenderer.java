package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelConveyorScanner;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.TileEntityConveyorScanner;

/**
 * Created by Pabilo8 on 21-06-2019.
 */
public class ConveyorScannerRenderer extends TileEntitySpecialRenderer<TileEntityConveyorScanner> implements IReloadableModelContainer<ConveyorScannerRenderer>
{
	static RenderItem renderItem = ClientUtils.mc().getRenderItem();
	private static ModelConveyorScanner model;

	private static String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/conveyor_scanner.png";

	@Override
	public void render(TileEntityConveyorScanner te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+1, (float)y-2, (float)z+2);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 1f, 1f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
			}

			model.getBlockRotation(te.facing, false);
			model.render();

			GlStateManager.translate(0f, 1f, -1f);

			ClientUtils.bindTexture("textures/atlas/blocks.png");
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", EnumFacing.NORTH);

			GlStateManager.translate(-0.5f, 0.25f, 1f);
			GlStateManager.translate(1f, 0f, -(float)te.processTime/(float)te.processTimeMax);

			renderItem.renderItem(te.inventory.get(0), TransformType.GROUND);


			GlStateManager.popMatrix();

			//ImmersiveIntelligence.logger.info(ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", te.facing));
		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelConveyorScanner();
	}
}
