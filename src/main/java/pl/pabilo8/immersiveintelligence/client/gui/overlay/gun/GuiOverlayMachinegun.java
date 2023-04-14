package pl.pabilo8.immersiveintelligence.client.gui.overlay.gun;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Weapons.Machinegun;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.overlay.GuiOverlayBase;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMachinegun;

/**
 * @author Pabilo8
 * @since 13.09.2022
 */
public class GuiOverlayMachinegun extends GuiOverlayBase
{
	@Override
	public boolean shouldDraw(EntityPlayer player, RayTraceResult mouseOver)
	{
		return player.getRidingEntity() instanceof EntityMachinegun;
	}

	@Override
	public void draw(EntityPlayer player, RayTraceResult mouseOver, int width, int height)
	{
		EntityMachinegun mg = (EntityMachinegun)player.getRidingEntity();
		assert mg!=null;

		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.enableAlpha();
		boolean drawWater = mg.tankCapacity > 0, hasShield = mg.maxShieldStrength > 0;

		//Magazine 1
		if(mg.hasSecondMag)
		{
			ClientUtils.drawTexturedRect(width-38, height-27, 36, 25, 15/256f, (15+36)/256f, 29/256f, (29+25)/256f);
			if(mg.bullets2 > 0)
			{
				NonNullList<ItemStack> cartridge = IIContent.itemBulletMagazine.readInventory(mg.magazine2);
				for(int i = 0; i < mg.bullets2; i++)
				{
					if(cartridge.get(i).isEmpty())
						continue;
					final int x = width-38+3+((64-i)%2==0?-1: 0);
					ClientUtils.drawTexturedRect(x, height-17-(i*6), 30, 6, 51/256f, (51+30)/256f, 33/256f, (33+6)/256f);
					int cc = IIContent.itemAmmoMachinegun.getPaintColor(cartridge.get(i));
					float[] rgb;
					if(cc!=-1)
					{
						rgb = IIUtils.rgbIntToRGB(cc);
						GlStateManager.color(rgb[0], rgb[1], rgb[2]);
						ClientUtils.drawTexturedRect(x+10, height-17-(i*6), 4, 6, 61/256f, (65)/256f, 27/256f, (27+6)/256f);
					}
					rgb = IIUtils.rgbIntToRGB(IIContent.itemAmmoMachinegun.getCore(cartridge.get(i)).getColour());
					GlStateManager.color(rgb[0], rgb[1], rgb[2]);
					ClientUtils.drawTexturedRect(x+24, height-17-(i*6), 6, 6, 75/256f, (81)/256f, 27/256f, (27+6)/256f);
					GlStateManager.color(1f, 1f, 1f);

				}
			}
			drawTexturedModalRect(width-38+10, height-27+8, 51, (51+16), 16, 15);
			IIClientUtils.drawStringCentered(ClientUtils.font(),
					String.valueOf(mg.bullets2),
					width-38+12, height-27+12,
					12, 0,
					0xffffff
			);
			width -= 38;
			bindHUDTexture();
		}

		ClientUtils.drawTexturedRect(width-38, height-27, 36, 25, 15/256f, (15+36)/256f, 29/256f, (29+25)/256f);
		if(mg.bullets1 > 0)
		{
			NonNullList<ItemStack> cartridge = IIContent.itemBulletMagazine.readInventory(mg.magazine1);
			for(int i = 0; i < mg.bullets1; i++)
			{
				if(cartridge.get(i).isEmpty())
					continue;
				final int x = width-38+3+((64-i)%2==0?-1: 0);
				ClientUtils.drawTexturedRect(x, height-17-(i*6), 30, 6, 51/256f, (51+30)/256f, 33/256f, (33+6)/256f);
				int cc = IIContent.itemAmmoMachinegun.getPaintColor(cartridge.get(i));
				float[] rgb;
				if(cc!=-1)
				{
					rgb = IIUtils.rgbIntToRGB(cc);
					GlStateManager.color(rgb[0], rgb[1], rgb[2]);
					ClientUtils.drawTexturedRect(x+10, height-17-(i*6), 4, 6, 61/256f, (65)/256f, 27/256f, (27+6)/256f);
				}
				rgb = IIUtils.rgbIntToRGB(IIContent.itemAmmoMachinegun.getCore(cartridge.get(i)).getColour());
				GlStateManager.color(rgb[0], rgb[1], rgb[2]);
				ClientUtils.drawTexturedRect(x+24, height-17-(i*6), 6, 6, 75/256f, (81)/256f, 27/256f, (27+6)/256f);
				GlStateManager.color(1f, 1f, 1f);

			}
		}
		ClientUtils.drawTexturedRect(width-38+10, height-27+8, 16, 15, 51/256f, (51+16)/256f, 39/256f, (44+10)/256f);
		IIClientUtils.drawStringCentered(ClientUtils.font(),
				String.valueOf(mg.bullets1),
				width-38+12, height-27+12,
				12, 0,
				0xffffff
		);
		width -= 38;
		bindHUDTexture();

		GlStateManager.enableBlend();

		ClientUtils.drawTexturedRect(width-24, height-20, 22, 18, 0/256f, 22/256f, 62/256f, 80/256f);
		ClientUtils.drawGradientRect(width-23, height-3-(int)((mg.overheating/(float)Machinegun.maxOverheat)*16), width-20, height-3, 0xffdf9916, 0x0fba0f0f);
		ClientUtils.drawTexturedRect(width-19, height-19, 16, 16, 16/256f, 32/256f, 0, 16/256f);
		height -= 18;

		if(drawWater)
		{
			FluidStack fluid = mg.tank.getFluid();
			ClientUtils.drawTexturedRect(width-24, height-20, 22, 18, 0/256f, 22/256f, 62/256f, 80/256f);
			if(fluid!=null)
			{
				float hh = 16*((float)mg.tank.getFluidAmount()/(float)mg.tankCapacity);
				ClientUtils.drawRepeatedFluidSprite(fluid, width-23, height-3-hh, 3, hh);
			}
			bindHUDTexture();
			ClientUtils.drawTexturedRect(width-19, height-19, 16, 16, 0, 16/256f, 0, 16/256f);
			height -= 18;
		}

		if(hasShield)
		{
			ClientUtils.drawTexturedRect(width-24, height-20, 22, 18, 0/256f, 22/256f, 62/256f, 80/256f);
			IIClientUtils.drawArmorBar(width-23, height-3, 3, 16,
					mg.shieldStrength/mg.maxShieldStrength);
			ClientUtils.drawTexturedRect(width-19, height-19, 16, 16, 32/256f, 48/256f, 0, 16/256f);
		}

		GlStateManager.popMatrix();
	}
}
