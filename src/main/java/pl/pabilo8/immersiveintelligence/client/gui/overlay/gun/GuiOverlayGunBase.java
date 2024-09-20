package pl.pabilo8.immersiveintelligence.client.gui.overlay.gun;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.overlay.GuiOverlayBase;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @since 03.03.2023
 */
public abstract class GuiOverlayGunBase extends GuiOverlayBase
{
	void drawMagazine(ItemStack magazine, int width, int height)
	{
		drawMagazine(width, height,
				IIContent.itemBulletMagazine.readInventory(magazine)
		);
	}

	public void drawMagazine(int width, int height, NonNullList<ItemStack> ammo)
	{
		IIDrawUtils draw = IIDrawUtils.startTexturedColored();
		draw.drawTexColorRect(width-38, height-27, 36, 25, IIColor.WHITE, 15/256f, (15+36)/256f, 29/256f, (29+25)/256f);

		//Draw bullets
		int bullets = 0;
		boolean offset = false;
		IAmmoTypeItem<?, ?> ammoItem = null;
		if(!ammo.isEmpty())
		{
			draw.setOffset(width-38+3, height-17);
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

				draw.addOffset(0, -6);
				offset ^= true;
			}
		}
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
