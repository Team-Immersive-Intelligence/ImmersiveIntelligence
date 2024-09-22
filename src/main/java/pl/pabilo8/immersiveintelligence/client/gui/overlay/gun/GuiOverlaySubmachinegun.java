package pl.pabilo8.immersiveintelligence.client.gui.overlay.gun;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.RayTraceResult;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIISubmachinegun;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * @author Pabilo8
 * @since 13.09.2022
 */
public class GuiOverlaySubmachinegun extends GuiOverlayGunBase
{
	@Override
	public boolean shouldDraw(EntityPlayer player, RayTraceResult mouseOver)
	{
		return player.getHeldItem(EnumHand.MAIN_HAND).getItem()==IIContent.itemSubmachinegun;
	}

	@Override
	public void draw(EntityPlayer player, RayTraceResult mouseOver, int width, int height)
	{
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
		EasyNBT nbt = EasyNBT.wrapNBT(stack);
		ItemStack magazine = nbt.getItemStack(ItemIISubmachinegun.MAGAZINE);

		//Draw magazine
		if(IIContent.itemBulletMagazine.stackToSub(magazine)==Magazines.SUBMACHINEGUN_DRUM)
			drawRoundMagazine(IIContent.itemBulletMagazine.readInventory(magazine), width, height);
		else
			drawMagazine(magazine, width, height);
		//TODO: 14.09.2024 drum magazines

		//for drums
		/*if(magazine.getMetadata()==3)
		{
			int bullets = IIContent.itemBulletMagazine.getRemainingBulletCount(magazine);
			ClientUtils.drawTexturedRect(width-38, height-27, 36, 25, 15/256f, (15+36)/256f, 29/256f, (29+25)/256f);
			if(bullets > 0)
			{
				NonNullList<ItemStack> cartridge = IIContent.itemBulletMagazine.readInventory(magazine);
				GlStateManager.pushMatrix();
				GlStateManager.translate(width-38+3, height-17, 0);
				for(int i = 0; i < bullets; i++)
				{
					if(cartridge.get(i).isEmpty())
						continue;
					final int x = ((bullets-i)%2==0?-1: 0);
					ClientUtils.drawTexturedRect(x, 0, 30, 6, 51/256f, (51+30)/256f, 33/256f, (33+6)/256f);
					IIColor rgb = IIContent.itemAmmoMachinegun.getPaintColor(cartridge.get(i));

					if(rgb!=null)
					{
						rgb.glColor();
						ClientUtils.drawTexturedRect(10, 0, 4, 6, 61/256f, (65)/256f, 27/256f, (27+6)/256f);
					}
					IIContent.itemAmmoMachinegun.getCore(cartridge.get(i)).getColor().glColor();
					ClientUtils.drawTexturedRect(24, 0, 6, 6, 75/256f, (81)/256f, 27/256f, (27+6)/256f);
					GlStateManager.color(1f, 1f, 1f);
					if(i < 6)
					{
						GlStateManager.translate(0, -6, 0);
					}
					else if(i < 16)
					{
						GlStateManager.rotate(-9f, 0, 0, 1);
						GlStateManager.translate(0, -4, 0);
					}
					else if(i < 20)
					{
						GlStateManager.translate(0, -5, 0);
					}
					else if(i < 40)
					{
						GlStateManager.rotate(-9f, 0, 0, 1);
						GlStateManager.translate(0, -4, 0);
					}
					else if(i < 52)
					{
						GlStateManager.rotate(-9f, 0, 0, 1);
						GlStateManager.translate(0, -3, 0);
					}
					else if(i < 65)
					{
						GlStateManager.rotate(-15f, 0, 0, 1);
						GlStateManager.translate(0, -3, 0);
					}
				}
				GlStateManager.popMatrix();
			}
			ClientUtils.drawTexturedRect(width-38+10, height-27+8, 16, 15, 51/256f, (51+16)/256f, 39/256f, (44+10)/256f);
			IIClientUtils.drawStringCentered(ClientUtils.font(),
					String.valueOf(bullets),
					width-38+12, height-27+12,
					12, 0,
					0xffffff
			);
		}*/

	}

	private void drawRoundMagazine(NonNullList<ItemStack> ammo, int width, int height)
	{
		IIDrawUtils draw = IIDrawUtils.startTexturedColored();
		draw.drawTexColorRect(width-38, height-27, 36, 25, IIColor.WHITE, 15/256f, (15+36)/256f, 29/256f, (29+25)/256f);

		//Draw bullets
		int bullets = 0;
		boolean offset = false;
		IAmmoTypeItem<?, ?> ammoItem = null;
		GlStateManager.pushMatrix();
		if(!ammo.isEmpty())
		{
			draw.setOffset(width-38+3, height-17);
			int i = 0;
			for(ItemStack bullet : ammo)
			{
				if(bullet.isEmpty())
					break;
				if(ammoItem==null)
					ammoItem = (IAmmoTypeItem<?, ?>)bullet.getItem();

				bullets++;
				draw.drawTexColorRect(offset?1: 0, 0, 30, 6, IIColor.WHITE, 51/256f, (51+30)/256f, 33/256f, (33+6)/256f);
				IIColor paintColor = ammoItem.getPaintColor(bullet);

				if(paintColor!=null)
					draw.drawTexColorRect(offset?11: 10, 0, 4, 6, paintColor, 61/256f, (65)/256f, 27/256f, (27+6)/256f);
				draw.drawTexColorRect(offset?25: 24, 0, 6, 6, ammoItem.getCore(bullet).getColor(),
						75/256f, (81)/256f, 27/256f, (27+6)/256f);

				if(i < 6)
					draw.addOffset(0, -6);
				else if(i < 20)
					draw.addOffset(0, -5);
				else if(i < 40)
					draw.addRotation(-9).addOffset(0, -4);
				else if(i < 52)
					draw.addRotation(-9).addOffset(0, -3);
				else if(i < 65)
					draw.addRotation(-15f).addOffset(0, -3);
				i++;

				offset ^= true;
			}
		}
		GlStateManager.popMatrix();
		draw.setOffset(0, 0);

		//Draw ammo count
		draw.drawTexColorRect(width-38+10, height-27+8, 16, 15, IIColor.WHITE, 51/256f, (51+16)/256f, 39/256f, (44+10)/256f);
		draw.finish();

		IIClientUtils.drawStringCentered(ClientUtils.font(),
				String.valueOf(bullets),
				width-38+12, height-27+12,
				12, 0,
				0xffffff
		);
	}
}
