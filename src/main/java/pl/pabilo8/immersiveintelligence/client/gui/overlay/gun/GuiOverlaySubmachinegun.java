package pl.pabilo8.immersiveintelligence.client.gui.overlay.gun;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIISubmachinegun;
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

		//Draw magazine
		drawMagazine(nbt.getItemStack(ItemIISubmachinegun.MAGAZINE), width, height);
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
}
