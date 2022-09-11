package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_Connector;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelRedstoneInterface;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRedstoneInterface;

/**
 * @author Pabilo8
 * @since 21-06-2019
 */
public class RedstoneInterfaceRenderer extends TileEntitySpecialRenderer<TileEntityRedstoneInterface> implements IReloadableModelContainer<RedstoneInterfaceRenderer>
{
	static RenderItem renderItem = ClientUtils.mc().getRenderItem();
	private static ModelRedstoneInterface model;
	private static ModelRedstoneInterface modelFlipped;

	@Override
	public void render(TileEntityRedstoneInterface te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/redstone_interface.png";
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x, (float)y, (float)z);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 1f, 1f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
			}

			GlStateManager.enableBlend();
			ModelRedstoneInterface modelCurrent = te.mirrored?modelFlipped: model;
			modelCurrent.getBlockRotation(te.facing, te.mirrored);
			modelCurrent.render();
			GlStateManager.disableBlend();

			GlStateManager.rotate(te.mirrored?270: 90, 0, 1, 0);
			GlStateManager.translate(2, 0, te.mirrored?0: 1);
			ClientUtils.bindAtlas();
			ClientUtils.mc().getBlockRendererDispatcher().renderBlockBrightness(IEContent.blockConnectors.getStateFromMeta(BlockTypes_Connector.CONNECTOR_REDSTONE.getMeta()), 8);

			GlStateManager.popMatrix();
		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelRedstoneInterface();
		modelFlipped = new ModelRedstoneInterface();
		modelFlipped.flipAllZ();
	}
}
