package pl.pabilo8.immersiveintelligence.client.gui.overlay.gun;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.entity.mounted_weapon.EntityMachinegun;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 13.09.2022
 */
public class GuiOverlayMachinegun extends GuiOverlayGunBase
{
	private final IIColor colorFrom = IIColor.fromPackedRGB(0xdf9916);
	private final IIColor colorTo = IIColor.fromPackedRGB(0xba0f0f);

	@Override
	public boolean shouldDraw(@Nonnull EntityPlayer player, @Nullable RayTraceResult mouseOver)
	{
		return player.getRidingEntity() instanceof EntityMachinegun;
	}

	@Override
	public void draw(@Nonnull EntityPlayer player, @Nullable RayTraceResult mouseOver, int width, int height)
	{
		final EntityMachinegun mg = (EntityMachinegun)player.getRidingEntity();
		assert mg!=null;

		//Draw loaded ammo
		if(mg.upgrades.contains(WeaponUpgrade.BELT_FED_LOADER))
			drawMagazine(mg.loadingCrate, width, height);
		else
		{
			if(mg.upgrades.contains(WeaponUpgrade.SECOND_MAGAZINE))
				drawMagazine(mg.loadingMagazine2, width, height);
			drawMagazine(mg.loadingMagazine1, width, height);
		}
		bindHUDTexture();

		//Draw Overheat
		IIDrawUtils draw = IIDrawUtils.startTexturedColored()
				.setOffset(width-38-24, height)
				.drawTexColorRect(0, -20, 22, 18, IIColor.WHITE, 0/256f, 22/256f, 62/256f, 80/256f)
				.inBetween((x, y) -> {
					IIClientUtils.drawGradientBar(x+1, y-19, 3, 16, colorFrom, colorTo, mg.recoil.getOverheat(0));
					bindHUDTexture();
				})
				.drawTexColorRect(5, -19, 16, 16, IIColor.WHITE, 16/256f, 32/256f, 0, 16/256f);
		draw.addOffset(0, -18);

		//Draw Water
		if(mg.upgrades.contains(WeaponUpgrade.WATER_COOLING))
		{
			final FluidStack fluid = mg.tank.getFluid();
			draw.drawTexColorRect(0, -20, 22, 18, IIColor.WHITE, 0/256f, 22/256f, 62/256f, 80/256f)
					.inBetween((x, y) -> {
						if(fluid==null)
							return;
						float hh = 16f*mg.tank.getFillPercentage();
						ClientUtils.drawRepeatedFluidSprite(fluid, x+1, y-3-hh, 3, hh);
						bindHUDTexture();
					})
					.drawTexColorRect(5, -19, 16, 16, IIColor.WHITE, 0, 16/256f, 0, 16/256f)
					.addOffset(0, -18);
		}
		//Draw Shield
		if(mg.upgrades.contains(WeaponUpgrade.SHIELD))
		{
			draw
					.drawTexColorRect(0, -20, 22, 18, IIColor.WHITE, 0/256f, 22/256f, 62/256f, 80/256f)
					.inBetween((x, y) -> IIClientUtils.drawArmorBar(x+1, y-19, 3, 16, (float)mg.shield.getDamageFactor()))
					.drawTexColorRect(5, -19, 16, 16, IIColor.WHITE, 32/256f, 48/256f, 0, 16/256f);
		}
		draw.finish();
	}
}
