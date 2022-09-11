package pl.pabilo8.immersiveintelligence.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing.Axis;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.block.fortification.tileentity.TileEntityMineSign;

/**
 * @author Pabilo8
 * @since 27.01.2021
 */
public class MineSignRenderer extends TileEntitySpecialRenderer<TileEntityMineSign>
{
	private static final String KEY = "tile.immersiveintelligence.mine_sign.mine_sign.desc";

	@Override
	public void render(TileEntityMineSign te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		GlStateManager.pushMatrix();
		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.disableLighting();
		GlStateManager.translate(x+0.5, y+1, z+0.5);
		GlStateManager.rotate((te.facing.getAxis()==Axis.Z?te.facing.getOpposite(): te.facing).getHorizontalAngle(), 0F, 1F, 0F);
		GlStateManager.translate(0.5, 0, -0.001);
		GlStateManager.scale(-0.0625f, -0.0625f, -0.0625f);
		GlStateManager.scale(0.25, 0.25, 0.25);

		String[] str = I18n.format(KEY).split(";");
		for(int i = 0; i < str.length; i++)
			IIClientUtils.drawStringCentered(getFontRenderer(), str[i], 8, i*12, 48, 12, 0xd99747);

		GlStateManager.popMatrix();
	}
}
