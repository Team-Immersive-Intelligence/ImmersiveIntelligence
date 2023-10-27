package pl.pabilo8.immersiveintelligence.client.gui.overlay;

import blusunrize.immersiveengineering.api.tool.ZoomHandler;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.items.ItemChemthrower;
import blusunrize.immersiveengineering.common.items.ItemDrill;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Vehicles.Motorbike;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.entity.EntityTripodPeriscope;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityMotorbike;

/**
 * @author Pabilo8
 * @since 13.09.2022
 */
public class GuiOverlayMotorbike extends GuiOverlayBase
{
	@Override
	public boolean shouldDraw(EntityPlayer player, RayTraceResult mouseOver)
	{
		return ZoomHandler.isZooming&&player.getRidingEntity() instanceof EntityTripodPeriscope;
	}

	@Override
	public void draw(EntityPlayer player, RayTraceResult mouseOver, int width, int height)
	{
		int offset = 0;
		EnumHand[] var3 = EnumHand.values();
		EntityMotorbike motorbike = (EntityMotorbike)player.getLowestRidingEntity();

		for(EnumHand hand : var3)
			if(!player.getHeldItem(hand).isEmpty())
			{
				ItemStack equipped = player.getHeldItem(hand);
				if(equipped.getItem() instanceof ItemDrill||equipped.getItem() instanceof ItemChemthrower)
					offset -= 85;
			}

		ClientUtils.bindTexture(ImmersiveIntelligence.MODID+":textures/gui/hud_elements.png");
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		float dx = (float)(width-16);
		float dy = (float)height;
		GlStateManager.pushMatrix();
		GlStateManager.translate(dx, dy+(float)offset, 0.0D);
		double uMin = 0;
		double uMax = 53/256d;
		double vMin = 80/256d;
		double vMax = (80+72)/256d;
		ClientUtils.drawTexturedRect(-41.0F+17f, -73.0F+5f, 31, 62, 54/256d, (54+31)/256d, 80/256d, (80+62)/256d);

		int capacity = motorbike.tank.getCapacity();
		int amount = motorbike.tank.getFluidAmount();
		float cap = (float)capacity;
		float angle = 83.0F-(float)(166*amount)/cap;

		GlStateManager.pushMatrix();
		GlStateManager.translate(-23.0D, -37.0D, 0.0D);
		GlStateManager.rotate(angle, 0, 0, 1);
		ClientUtils.drawTexturedRect(-3F+8, -1.5f, 24, 5, 0/256d, 32/256d, 152/256d, (152+7)/256d);
		GlStateManager.popMatrix();

		ClientUtils.drawTexturedRect(-41.0F, -73.0F, 53, 72, uMin, uMax, vMin, vMax);

		ClientUtils.drawTexturedRect(-51.0F, -73.0F, 22, 18, 22/256f, 44/256f, 62/256f, 80/256f);
		ClientUtils.drawTexturedRect(-51.0F+5, -73.0F+1, 16, 16, 48/256f, 64/256f, 0/256f, 16/256f);
		IIClientUtils.drawArmorBar(-51+1, -73+1, 3, 16,
				motorbike.frontWheelDurability/(float)Motorbike.wheelDurability);

		ClientUtils.drawTexturedRect(-59.0F, -73.0F+18, 22, 18, 22/256f, 44/256f, 62/256f, 80/256f);
		ClientUtils.drawTexturedRect(-59.0F+5, -73.0F+18+1, 16, 16, 80/256f, 96/256f, 0/256f, 16/256f);
		IIClientUtils.drawArmorBar(-59+1, -73+18+1, 3, 16,
				motorbike.engineDurability/(float)Motorbike.engineDurability);

		ClientUtils.drawTexturedRect(-59.0F, -73.0F+18*2, 22, 18, 22/256f, 44/256f, 62/256f, 80/256f);
		ClientUtils.drawTexturedRect(-59.0F+5, -73.0F+18*2+1, 16, 16, 96/256f, 112/256f, 0/256f, 16/256f);
		IIClientUtils.drawArmorBar(-59+1, -73+18*2+1, 3, 16,
				motorbike.fuelTankDurability/(float)Motorbike.fuelTankDurability);

		ClientUtils.drawTexturedRect(-51.0F, -73.0F+18*3, 22, 18, 22/256f, 44/256f, 62/256f, 80/256f);
		ClientUtils.drawTexturedRect(-51.0F+5, -73.0F+18*3+1, 16, 16, 64/256f, 80/256f, 0/256f, 16/256f);
		IIClientUtils.drawArmorBar(-51+1, -73+18*3+1, 3, 16,
				motorbike.backWheelDurability/(float)Motorbike.wheelDurability);


		GlStateManager.popMatrix();
	}
}
