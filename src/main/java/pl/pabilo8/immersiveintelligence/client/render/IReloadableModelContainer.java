package pl.pabilo8.immersiveintelligence.client.render;

import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.EvenMoreImmersiveModelRegistry;

/**
 * @author Pabilo8
 * @since 23-06-2020
 */
public interface IReloadableModelContainer<T>
{
	void reloadModels();

	default T subscribeToList(String name)
	{
		return subscribeToList(new ResourceLocation(ImmersiveIntelligence.MODID, name));
	}

	default T subscribeToList(ResourceLocation modelName)
	{
		EvenMoreImmersiveModelRegistry.instance.addReloadableModel(this, modelName);
		return (T)this;
	}

	default void unsubscribeToList()
	{
		EvenMoreImmersiveModelRegistry.instance.removeReloadableModel(this);
	}
}
