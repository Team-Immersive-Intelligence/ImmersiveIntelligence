package pl.pabilo8.immersiveintelligence.client.gui.overlay.gun;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.gui.overlay.GuiOverlayBase;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.IIContent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 13.09.2022
 */
public class GuiOverlayMineDetector extends GuiOverlayBase
{
	@Override
	public boolean shouldDraw(@Nonnull EntityPlayer player, @Nullable RayTraceResult mouseOver)
	{
		return player.getHeldItem(EnumHand.MAIN_HAND).getItem()==IIContent.itemMineDetector;
	}

	@Override
	public void draw(@Nonnull EntityPlayer player, @Nullable RayTraceResult mouseOver, int width, int height)
	{
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);

		float value = MathHelper.clamp(ItemNBTHelper.getFloat(stack, "distance")/Tools.mineDetectorRadius, 0, 1);
		//

		ClientUtils.bindTexture(ImmersiveIntelligence.MODID+":textures/gui/hud_elements.png");
		GlStateManager.pushMatrix();
		IIDrawUtils.startTextured()
				.drawTexRect(width-72, height-41, 62, 31, 85/256f, (85+62)/256f, 81/256f, (81+31)/256f)
				.finish();

		GlStateManager.pushMatrix();
		GlStateManager.translate(width-72+31, height-41+31-3.5, 0);
		GlStateManager.rotate(180+(180*value), 0, 0, 1);
		GlStateManager.translate(-4, -3.5f, 0);

		IIDrawUtils.startTextured()
				.drawTexRect(0, 0, 32, 7, 0/256f, (32)/256f, 152/256f, (152+7)/256f)
				.finish();
		GlStateManager.popMatrix();

		IIDrawUtils.startTextured()
				.drawTexRect(width-72-5, height-41-5, 72, 41, 85/256f, (85+72)/256f, 112/256f, (112+41)/256f)
				.finish();
		GlStateManager.popMatrix();
	}
}
