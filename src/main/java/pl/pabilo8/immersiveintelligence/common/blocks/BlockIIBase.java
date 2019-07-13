package pl.pabilo8.immersiveintelligence.common.blocks;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.item.Item;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pabilo8 on 2019-05-07.
 * Huge thanks to AntiBlueQuirk, the author of Alternating Flux (https://github.com/AntiBlueQuirk/alternatingflux/)
 * for creating this really useful (and shortened) piece of code ^^
 */
public class BlockIIBase<E extends Enum<E> & BlockIEBase.IBlockEnum> extends BlockIEBase<E>
{

	public Map<Integer, String> tesrMap = new HashMap<>();

	public BlockIIBase(String name, Material material, PropertyEnum<E> mainProperty, Class<? extends ItemBlockIEBase> itemBlock, Object... additionalProperties)
	{
		super(name, material, mainProperty, itemBlock, additionalProperties);
		fixupBlock(this, itemBlock);
	}

	@Override
	public String createRegistryName()
	{
		return ImmersiveIntelligence.MODID+":"+name;
	}

	//This function allows us to use BlockIEBase class, by fixing things up so they come from our mod.
	//It should be called right after the super call in any constructor of a class that derives from BlockIEBase
	//This is kind of hacky, but allows us to avoid copying a lot of code.
	@SuppressWarnings("rawtypes")
	public static void fixupBlock(BlockIEBase block, Class<? extends ItemBlockIEBase> itemBlock)
	{
		//First, get the block out of IE's registries.
		Block rBlock = IEContent.registeredIEBlocks.remove(IEContent.registeredIEBlocks.size()-1);
		if(rBlock!=block)
			throw new IllegalStateException("fixupBlock was not called at the appropriate time, removed block did not match");

		Item rItem = IEContent.registeredIEItems.remove(IEContent.registeredIEItems.size()-1);
		if(rItem.getClass()!=itemBlock)
			throw new IllegalStateException("fixupBlock was not called at the appropriate time");

		//Now, reconfigure the block to match our mod.
		block.setCreativeTab(ImmersiveIntelligence.creativeTab);

		//And add it to our registries.
		CommonProxy.blocks.add(block);
		try
		{
			ItemBlockIEBase item = itemBlock.getConstructor(Block.class).newInstance(block);
			CommonProxy.items.add(item);
			//if (creative)
			//CommonProxy.items.add(item);
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}