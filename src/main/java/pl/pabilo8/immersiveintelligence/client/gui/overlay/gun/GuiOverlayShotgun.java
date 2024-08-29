package pl.pabilo8.immersiveintelligence.client.gui.overlay.gun;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.RayTraceResult;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIShotgun;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

public class GuiOverlayShotgun extends GuiOverlayGunBase
{
	@Override
	public boolean shouldDraw(EntityPlayer player, RayTraceResult mouseOver)
	{
		return player.getHeldItem(EnumHand.MAIN_HAND).getItem()==IIContent.itemShotgun;
	}

	@Override
	public void draw(EntityPlayer player, RayTraceResult mouseOver, int width, int height)
	{
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
		EasyNBT nbt = EasyNBT.wrapNBT(stack);

		//if(IIContent.itemShotgun.hasIIUpgrade(stack, WeaponUpgrade.SEMI_AUTOMATIC))
			//drawMagazine(nbt.getItemStack(ItemIIShotgun.MAGAZINE), width, height);
		//else
		//{
			NonNullList<ItemStack> ammo = NonNullList.create();
			for(NBTBase nbtBase : IIContent.itemShotgun.getAmmoList(stack))
				ammo.add(new ItemStack(((NBTTagCompound)nbtBase)));
			drawMagazine(width, height, ammo);
		//}

	}
}

