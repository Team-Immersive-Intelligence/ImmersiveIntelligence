package pl.pabilo8.immersiveintelligence.common.items.tools;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBase;

/**
 * @author Pabilo8
 * @since 28.01.2021
 */
public class ItemIIMineDetector extends ItemIIBase
{
	public ItemIIMineDetector()
	{
		super("mine_detector", 1);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		//if is worn as a helmet
		if(entityIn instanceof EntityPlayer&&((EntityPlayer)entityIn).getItemStackFromSlot(EntityEquipmentSlot.HEAD).equals(stack))
		{
			if(!Utils.hasUnlockedIIAdvancement((EntityPlayer)entityIn,"main/secret_carvers_revenge"))
				Utils.unlockIIAdvancement((EntityPlayer)entityIn,"main/secret_carvers_revenge");
		}
	}
}
