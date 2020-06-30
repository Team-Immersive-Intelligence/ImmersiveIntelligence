package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelSmallCrates;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntitySmallCrate;

/**
 * @author Pabilo8
 * @since 2019-05-26
 */
@SideOnly(Side.CLIENT)
public class SmallCrateRenderer extends TileEntitySpecialRenderer<TileEntitySmallCrate>
{
	private static ModelSmallCrates model = new ModelSmallCrates();

	private static String texture_wooden = ImmersiveIntelligence.MODID+":textures/blocks/small_crates/wooden";
	private static String texture_metal = ImmersiveIntelligence.MODID+":textures/blocks/small_crates/metal";

	@Override
	public void render(TileEntitySmallCrate te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(x+1, y, z);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			renderWithMeta(te.getBlockMetadata(), te.facing);

			GlStateManager.popMatrix();
			return;

		}
	}

	public static void renderWithMeta(int meta, EnumFacing facing)
	{
		ModelRendererTurbo[] mod = (meta==0||meta==3)?model.boxCrateModel: ((meta==1||meta==4)?model.cubeCrateModel: model.wideCrateModel);
		ClientUtils.bindTexture(meta < 3?(texture_wooden+meta+".png"): (texture_metal+(meta-3)+".png"));
		model.getBlockRotation(facing, false);
		for(ModelRendererTurbo m : mod)
			m.render(0.0625f);
	}
}
