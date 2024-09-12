package pl.pabilo8.immersiveintelligence.client.gui.overlay.gun;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Machinegun;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMachinegun;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @since 13.09.2022
 */
public class GuiOverlayMachinegun extends GuiOverlayGunBase
{
	private IIColor colorFrom = IIColor.fromPackedRGB(0xdf9916), colorTo = IIColor.fromPackedRGB(0xba0f0f);

	@Override
	public boolean shouldDraw(EntityPlayer player, RayTraceResult mouseOver)
	{
		return player.getRidingEntity() instanceof EntityMachinegun;
	}

	@Override
	public void draw(EntityPlayer player, RayTraceResult mouseOver, int width, int height)
	{
		final EntityMachinegun mg = (EntityMachinegun)player.getRidingEntity();
		assert mg!=null;

		IIDrawUtils draw;

		if(mg.hasSecondMag)
			drawMagazine(mg.magazine2, width, height);

		drawMagazine(mg.magazine1, width, height);

		bindHUDTexture();
		draw = IIDrawUtils.startTexturedColored()
				.setOffset(width-38-24, height)
				.drawTexColorRect(0, -20, 22, 18, IIColor.WHITE, 0/256f, 22/256f, 62/256f, 80/256f)
				.inBetween((x, y) -> {
					IIClientUtils.drawGradientBar(x+1, y-19, 3, 16, colorFrom, colorTo, mg.overheating/(float)Machinegun.maxOverheat);
					bindHUDTexture();
				})
				.drawTexColorRect(5, -19, 16, 16, IIColor.WHITE, 16/256f, 32/256f, 0, 16/256f);
		draw.addOffset(0, -18);

		//Draw Water
		if(mg.tankCapacity > 0)
		{
			final FluidStack fluid = mg.tank.getFluid();
			draw.drawTexColorRect(0, -20, 22, 18, IIColor.WHITE, 0/256f, 22/256f, 62/256f, 80/256f)
					.inBetween((x, y) -> {
						if(fluid==null)
							return;

						float hh = 16*((float)mg.tank.getFluidAmount()/(float)mg.tankCapacity);
						ClientUtils.drawRepeatedFluidSprite(fluid, x+1, y-3-hh, 3, hh);
						bindHUDTexture();
					})
					.drawTexColorRect(5, -19, 16, 16, IIColor.WHITE, 0, 16/256f, 0, 16/256f)
					.addOffset(0, -18);
		}
		//Draw Shield
		if(mg.maxShieldStrength > 0)
		{
			draw
					.drawTexColorRect(0, -20, 22, 18, IIColor.WHITE, 0/256f, 22/256f, 62/256f, 80/256f)
					.inBetween((x, y) -> IIClientUtils.drawArmorBar(x+1, y-19, 3, 16, mg.shieldStrength/mg.maxShieldStrength))
					.drawTexColorRect(5, -19, 16, 16, IIColor.WHITE, 32/256f, 48/256f, 0, 16/256f);
		}
		draw.finish();
	}
}
