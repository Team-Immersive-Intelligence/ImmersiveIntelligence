package pl.pabilo8.immersiveintelligence.api.ammocrate;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 11.08.2026
 */
public class ReloadContext
{
	public final TileEntityAmmunitionCrate crate;
	public final Entity owner;
	public final ItemStack target;
	public final List<ItemStack> returnedMagazines = new ArrayList<>();
	public ItemStack reserved = ItemStack.EMPTY;

	public ReloadContext(TileEntityAmmunitionCrate crate, Entity owner, ItemStack target)
	{
		this.crate = crate;
		this.owner = owner;
		this.target = target;
	}

	boolean matches(ItemStack stack)
	{
		return target==stack;
	}

	void setPreview(ItemStack stack)
	{
		EasyNBT nbt = EasyNBT.wrapNBT(target).withBoolean(AmmunitionCrateHandler.KEY_CRATE_RELOAD, true);
		if(stack.isEmpty())
			nbt.without(AmmunitionCrateHandler.KEY_CRATE_RELOAD_PREVIEW);
		else
			nbt.withItemStack(AmmunitionCrateHandler.KEY_CRATE_RELOAD_PREVIEW, stack.copy());
	}

	void clearPreview()
	{
		EasyNBT.wrapNBT(target).without(AmmunitionCrateHandler.KEY_CRATE_RELOAD, AmmunitionCrateHandler.KEY_CRATE_RELOAD_PREVIEW);
	}

	void deferReturnedMagazine(ItemStack magazine)
	{
		returnedMagazines.add(magazine.copy());
	}

	void returnReserved()
	{
		if(reserved.isEmpty())
			return;
		ItemStack remainder = crate.insertAmmunition(reserved);
		if(!remainder.isEmpty())
			IIUtils.giveOrDropCasingStack(owner, remainder);
		reserved = ItemStack.EMPTY;
	}

	void finish()
	{
		returnReserved();
		for(ItemStack magazine : returnedMagazines)
		{
			ItemStack remainder = crate.insertReturnedMagazine(magazine);
			if(!remainder.isEmpty())
				IIUtils.giveOrDropCasingStack(owner, remainder);
		}
		returnedMagazines.clear();
		clearPreview();
	}
}
