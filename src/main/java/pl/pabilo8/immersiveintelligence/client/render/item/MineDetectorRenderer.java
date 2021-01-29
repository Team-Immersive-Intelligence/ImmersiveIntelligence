package pl.pabilo8.immersiveintelligence.client.render.item;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.item.ModelMineDetector;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 28.01.2021
 */
public class MineDetectorRenderer extends TileEntityItemStackRenderer implements IReloadableModelContainer<MineDetectorRenderer>
{
	public static MineDetectorRenderer instance = new MineDetectorRenderer().subscribeToList("mine_detector");
	@SideOnly(Side.CLIENT)
	private static ModelMineDetector model;
	private static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/items/tools/mine_detector.png";

	@Override
	public void renderByItem(ItemStack itemStackIn, float partialTicks)
	{
		GlStateManager.pushMatrix();

		ClientUtils.bindTexture(TEXTURE);
		for(ModelRendererTurbo mod : model.baseModel)
			mod.render();
		for(ModelRendererTurbo mod : model.poleModel)
		{
			//mod.rotateAngleZ=-0.95993109F;
			mod.render();
		}

		GlStateManager.popMatrix();
	}

	@Override
	public void reloadModels()
	{
		model = new ModelMineDetector();
	}
}
