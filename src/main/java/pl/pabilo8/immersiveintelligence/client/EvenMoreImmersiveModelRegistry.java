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
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Pabilo8 on 14-09-2019.
 * The bestest of best!
 * Blu, please make variables public (and available to addon makers)
 */
@SideOnly(Side.CLIENT)
public class EvenMoreImmersiveModelRegistry extends ImmersiveModelRegistry
{
	public static EvenMoreImmersiveModelRegistry instance = new EvenMoreImmersiveModelRegistry();
	//Yes
	public final HashMap<ModelResourceLocation, ItemModelReplacement> itemModelReplacements = new HashMap<>();
	private final Map<ResourceLocation, IReloadableModelContainer<?>> reloadableModels = new HashMap<>();

	@Override
	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event)
	{
		for(Map.Entry<ModelResourceLocation, ItemModelReplacement> entry : itemModelReplacements.entrySet())
		{
			IBakedModel object = event.getModelRegistry().getObject(entry.getKey());
			if(object!=null)
			{
				try
				{
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
		if(stack.getItem() instanceof ItemIEBase)
		{
			ResourceLocation loc;
			if(((ItemIEBase)stack.getItem()).getSubNames()!=null&&((ItemIEBase)stack.getItem()).getSubNames().length > 1)
				loc = new ResourceLocation(modname, ((ItemIEBase)stack.getItem()).itemName+"/"+((ItemIEBase)stack.getItem()).getSubNames()[stack.getMetadata()]);
			else
				loc = new ResourceLocation(modname, ((ItemIEBase)stack.getItem()).itemName);
			itemModelReplacements.put(new ModelResourceLocation(loc, "inventory"), replacement);
		}
	}

	public void addReloadableModel(IReloadableModelContainer<?> model, ResourceLocation modelName)
	{
		reloadableModels.put(modelName, model);
	}

	public void removeReloadableModel(IReloadableModelContainer<?> model)
	{
		reloadableModels.remove(model);
	}

	public void reloadRegisteredModels()
	{
		reloadableModels.values().forEach(IReloadableModelContainer::reloadModels);
	}

	public boolean reloadModel(ResourceLocation modelName)
	{
		reloadableModels.forEach((s, iReloadableModelContainer) ->
		{
			if(s.equals(modelName))
				iReloadableModelContainer.reloadModels();
		});
		return reloadableModels.keySet().stream().anyMatch(resourceLocation -> resourceLocation.equals(modelName));
	}

	public Set<ResourceLocation> getReloadableModels()
	{
		return reloadableModels.keySet();
	}
}
