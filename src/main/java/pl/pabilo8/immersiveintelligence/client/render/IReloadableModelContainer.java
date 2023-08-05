package pl.pabilo8.immersiveintelligence.client.render;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.IIModelRegistry;

/**
 * @author Pabilo8
 * @since 23-06-2020
 */
public interface IReloadableModelContainer<T>
{
	/**
	 * Called when model is to be reloaded/refreshed
	 */
	void reloadModels();

	/**
	 * Called on {@link net.minecraftforge.client.event.TextureStitchEvent.Pre}<br>
	 * Used for registering sprites for the texture sticher's atlas
	 */
	default void registerSprites(TextureMap map)
	{

	}

	/**
	 * Adds the model container to the II Model Registry
	 * @param name name of the model in II namespace
	 * @return this
	 */
	default T subscribeToList(String name)
	{
		return subscribeToList(new ResourceLocation(ImmersiveIntelligence.MODID, name));
	}

	/**
	 * Adds the model container to the II Model Registry
	 * @param modelName full name of this model
	 * @return this
	 */
	default T subscribeToList(ResourceLocation modelName)
	{
		IIModelRegistry.instance.addReloadableModel(this, modelName);
		return (T)this;
	}

	/**
	 * Removes this model from the II Model Registry<br>
	 * Used mostly by containers that are also models themselves
	 */
	default void unsubscribeToList()
	{
		IIModelRegistry.instance.removeReloadableModel(this);
	}
}
