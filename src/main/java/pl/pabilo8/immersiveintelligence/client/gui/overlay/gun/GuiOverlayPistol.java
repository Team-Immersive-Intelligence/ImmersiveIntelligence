package pl.pabilo8.immersiveintelligence.client.gui.overlay.gun;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIPistol;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * @author Pabilo8
 * @since 13.09.2022
 */
public class GuiOverlayPistol extends GuiOverlayGunBase
{
	@Override
	public boolean shouldDraw(EntityPlayer player, RayTraceResult mouseOver)
	{
		return player.getHeldItem(EnumHand.MAIN_HAND).getItem()==IIContent.itemPistol;
	}

	@Override
	public void draw(EntityPlayer player, RayTraceResult mouseOver, int width, int height)
	{
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
		EasyNBT nbt = EasyNBT.wrapNBT(stack);

		int lastMode = nbt.getInt(ItemIIPistol.LAST_FIRE_MODE);
		int mode = nbt.getInt(ItemIIPistol.FIRE_MODE);

		float modeProgress = 1f-MathHelper.clamp((nbt.getInt(ItemIIPistol.FIRE_MODE_TIMER)-ClientUtils.mc().getRenderPartialTicks())/6f, 0f, 1f);

		//Draw fire mode toggle
		IIDrawUtils draw = IIDrawUtils.startTextured();
		draw.setOffset(width-58, height-20);
		draw.setOffset(0, 0);
		draw.drawTexRect(width-59, height-28+7-((float)MathHelper.clampedLerp(lastMode, mode, modeProgress)*19), 20, 20, 44/256f, (44+20)/256f, 60/256f, (60+20)/256f);
		draw.finish();


		//Draw magazine
		drawMagazine(nbt.getItemStack(ItemIIPistol.MAGAZINE), width, height);

	}
}
