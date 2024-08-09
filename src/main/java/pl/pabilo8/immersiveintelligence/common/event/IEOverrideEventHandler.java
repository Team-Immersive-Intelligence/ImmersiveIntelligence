package pl.pabilo8.immersiveintelligence.common.event;

import blusunrize.immersiveengineering.common.EventHandler;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IEntityProof;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration2;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityRazorWire;
import blusunrize.immersiveengineering.common.items.ItemDrill;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

/**
 * @author GabrielV (gabriel@iiteam.net)
 * @since 21/04/2024 - 7:17 PM
 * Class made to override defaule IE <code>digSpeedEvent</code><br>
 * in order to make our electric wirecutter cut the razor wire ^^
 */
public class IEOverrideEventHandler extends EventHandler
{
	@Override
	public void digSpeedEvent(BreakSpeed event)
	{
		ItemStack current = event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND);
		//Stop the combustion drill from working underwater
		if(!current.isEmpty()&&current.getItem().equals(IEContent.itemDrill)&&current.getItemDamage()==0&&event.getEntityPlayer().isInsideOfMaterial(Material.WATER))
			if(((ItemDrill)IEContent.itemDrill).getUpgrades(current).getBoolean("waterproof"))
				event.setNewSpeed(event.getOriginalSpeed()*5);
			else
				event.setCanceled(true);
		if(event.getState().getBlock()==IEContent.blockMetalDecoration2&&IEContent.blockMetalDecoration2.getMetaFromState(event.getState())==BlockTypes_MetalDecoration2.RAZOR_WIRE.getMeta())
			if(!Utils.isWirecutter(current))
			{
				event.setCanceled(true);
				TileEntityRazorWire.applyDamage(event.getEntityLiving());
			}
		TileEntity te = event.getEntityPlayer().getEntityWorld().getTileEntity(event.getPos());
		if(te instanceof IEntityProof&&!((IEntityProof)te).canEntityDestroy(event.getEntityPlayer()))
			event.setCanceled(true);
	}
}
