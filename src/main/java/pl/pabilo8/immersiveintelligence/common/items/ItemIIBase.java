package pl.pabilo8.immersiveintelligence.common.items;

/*
  Created by Pabilo8 on 2019-05-07.
  Huge thanks to AntiBlueQuirk, the author of Alternating Flux (https://github.com/AntiBlueQuirk/alternatingflux/)
  for creating this really useful (and shortened) piece of code ^^
 */

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.items.ItemIEBase;
import net.minecraft.item.Item;
import org.apache.commons.lang3.ArrayUtils;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.item.ModelAbstractItem;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;

import java.util.HashMap;
import java.util.Map;

public class ItemIIBase extends ItemIEBase
{
	public Map<Integer, ModelAbstractItem> specialModelMap = new HashMap<>();

	public ItemIIBase(String name, int stackSize, String... subNames)
	{
		super(name, stackSize, subNames);
		fixupItem();
	}

	//This function allows us to use IEBase classes, by fixing things up so they come from our mod.
	//It should be called right after the super call in any constructor of a class that derives from ItemIEBase
	//This is kind of hacky, but allows us to avoid copying a lot of code.
	@SuppressWarnings("rawtypes")
	public void fixupItem()
	{
		//First, get the item out of IE's registries.
		Item rItem = IEContent.registeredIEItems.remove(IEContent.registeredIEItems.size()-1);
		if(rItem!=this) throw new IllegalStateException("fixupItem was not called at the appropriate time");

		//Now, reconfigure the block to match our mod.
		this.setUnlocalizedName(ImmersiveIntelligence.MODID+"."+this.itemName);
		this.setCreativeTab(ImmersiveIntelligence.creativeTab);

		//And add it to our registries.
		CommonProxy.items.add(this);
	}

	public int getMetaBySubname(String subname)
	{
		if(ArrayUtils.contains(subNames, subname))
		{
			return ArrayUtils.indexOf(subNames, subname);
		}
		//Not the best option, but it will actually always get the correct meta (unless I mess something up)
		return 0;
	}
}