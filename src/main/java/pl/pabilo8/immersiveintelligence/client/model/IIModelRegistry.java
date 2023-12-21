package pl.pabilo8.immersiveintelligence.client.model;

import blusunrize.immersiveengineering.client.ImmersiveModelRegistry;
import blusunrize.immersiveengineering.client.models.ModelItemDynamicOverride;
import blusunrize.immersiveengineering.common.items.ItemIEBase;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;

import java.util.*;

/**
 * Created by Pabilo8 on 14-09-2019.
 * The bestest of best!
 * Blu, please make variables public (and available to addon makers)
 */
@SideOnly(Side.CLIENT)
public class IIModelRegistry extends ImmersiveModelRegistry
{
	public static IIModelRegistry instance = new IIModelRegistry();
	//Yes
	public final HashMap<ModelResourceLocation, ItemModelReplacement> itemModelReplacements = new HashMap<>();
	private final Map<ResourceLocation, IReloadableModelContainer<?>> reloadableModels = new HashMap<>();
	private final List<IReloadableModelContainer<?>> temporaryReloadableModels = new ArrayList<>();

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

	/**
	 * General method for registering a custom item model, handles {@link ItemIISubItemsBase}, {@link ItemIIBase} and {@link ItemIEBase}
	 *
	 * @param item        item to be registered
	 * @param modID       Mod ID / domain used for model path
	 * @param replacement replacement item model
	 */
	public void registerCustomItemModel(Item item, String modID, ItemModelReplacement replacement)
	{
		if(item instanceof ItemIIBase)
		{
			if(item instanceof ItemIISubItemsBase)
			{
				ItemIISubItemsBase<?> itemSub = (ItemIISubItemsBase<?>)item;
				for(IIItemEnum subItem : itemSub.getSubItems())
					itemModelReplacements.put(new ModelResourceLocation(
									new ResourceLocation(modID, itemSub.itemName+"/"+subItem.getName()), "inventory"),
							replacement);
			}
			else
			{
				itemModelReplacements.put(new ModelResourceLocation(
								new ResourceLocation(modID, ((ItemIIBase)item).itemName), "inventory"),
						replacement);
			}
		}
		else if(item instanceof ItemIEBase)
		{
			ResourceLocation loc = new ResourceLocation(modID, ((ItemIEBase)item).itemName);
			itemModelReplacements.put(new ModelResourceLocation(loc, "inventory"), replacement);
		}
	}

	/**
	 * Overloaded method of registering a dynamic override item model for an IE item, but only for specified meta values
	 *
	 * @param item  item to be registered
	 * @param modID Mod ID / domain used for model path
	 * @param IDs   item meta IDs for this registration
	 * @see #registerCustomItemModel(Item, String, ItemModelReplacement)
	 */
	public void registerCustomItemModel(ItemIEBase item, String modID, int... IDs)
	{
		for(int id : IDs)
			itemModelReplacements.put(new ModelResourceLocation(
							new ResourceLocation(modID, item.itemName+"/"+item.getSubNames()[id]), "inventory"),
					new ImmersiveModelRegistry.ItemModelReplacement()
					{
						@Override
						public IBakedModel createBakedModel(IBakedModel existingModel)
						{
							return new ModelItemDynamicOverride(existingModel, null);
						}
					});
	}

	/**
	 * Overloaded method of registering a dynamic override item model for an IE item using meta values
	 */
	public void registerCustomItemModel(ItemIIBase item)
	{
		registerCustomItemModel(item, ImmersiveIntelligence.MODID, new ImmersiveModelRegistry.ItemModelReplacement()
		{
			@Override
			public IBakedModel createBakedModel(IBakedModel existingModel)
			{
				return new ModelItemDynamicOverride(existingModel, null);
			}
		});
	}

	public void addReloadableModel(IReloadableModelContainer<?> model, ResourceLocation modelName)
	{
		reloadableModels.put(modelName, model);
	}

	/**
	 * Adds a temporary model to the registry, which will be removed after registering sprites
	 *
	 * @param temp the model to be added
	 */
	public void addTemporaryModel(IReloadableModelContainer<?> temp)
	{
		temporaryReloadableModels.add(temp);
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

	public void registerSprites(TextureMap map)
	{
		//Removable temporary models
		temporaryReloadableModels.forEach(mod -> mod.registerSprites(map));
		temporaryReloadableModels.clear();
		//Actual models
		reloadableModels.values().forEach(mod -> mod.registerSprites(map));
	}
}
