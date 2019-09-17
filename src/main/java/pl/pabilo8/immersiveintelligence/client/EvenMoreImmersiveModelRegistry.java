package pl.pabilo8.immersiveintelligence.client;

import blusunrize.immersiveengineering.client.ImmersiveModelRegistry;
import blusunrize.immersiveengineering.common.items.ItemIEBase;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pabilo8 on 14-09-2019.
 * The bestest of best!
 * Blu, please make variables public (and available to addon makers)
 */
@SuppressWarnings("deprecation")
@SideOnly(Side.CLIENT)
public class EvenMoreImmersiveModelRegistry extends ImmersiveModelRegistry
{
	public static EvenMoreImmersiveModelRegistry instance = new EvenMoreImmersiveModelRegistry();
	//Yes
	public HashMap<ModelResourceLocation, ItemModelReplacement> itemModelReplacements = new HashMap<ModelResourceLocation, ItemModelReplacement>();

	@Override
	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event)
	{
		ImmersiveIntelligence.logger.info("doing a thing!");
		for(Map.Entry<ModelResourceLocation, ItemModelReplacement> entry : itemModelReplacements.entrySet())
		{
			ImmersiveIntelligence.logger.info("adding a model");
			ImmersiveIntelligence.logger.info(entry.getKey());
			IBakedModel object = event.getModelRegistry().getObject(entry.getKey());
			if(object!=null)
			{
				try
				{
					ImmersiveIntelligence.logger.info("added a model");
					event.getModelRegistry().putObject(entry.getKey(), entry.getValue().createBakedModel(object));
				} catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	//This
	public void registerCustomItemModel(ItemStack stack, ItemModelReplacement replacement, String modname)
	{
		ImmersiveIntelligence.logger.info("Adding an Item! ("+stack.toString()+")");
		if(stack.getItem() instanceof ItemIEBase)
		{
			ResourceLocation loc;
			if(((ItemIEBase)stack.getItem()).getSubNames()!=null&&((ItemIEBase)stack.getItem()).getSubNames().length > 1)
				loc = new ResourceLocation(modname, ((ItemIEBase)stack.getItem()).itemName+"/"+((ItemIEBase)stack.getItem()).getSubNames()[stack.getMetadata()]);
			else
				loc = new ResourceLocation(modname, ((ItemIEBase)stack.getItem()).itemName);
			itemModelReplacements.put(new ModelResourceLocation(loc, "inventory"), replacement);
			ImmersiveIntelligence.logger.info(loc);
		}
	}
}
