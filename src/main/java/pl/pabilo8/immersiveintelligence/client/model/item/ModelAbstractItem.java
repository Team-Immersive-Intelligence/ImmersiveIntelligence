package pl.pabilo8.immersiveintelligence.client.model.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.item.ModelMeasuringCup.CustomModelLoader;

/**
 * Created by Pabilo8 on 13-07-2019.
 */
public abstract class ModelAbstractItem implements IModel
{
	public ModelResourceLocation getLocation()
	{
		return new ModelResourceLocation(ImmersiveIntelligence.MODID,"nothing");
	}

	public ICustomModelLoader getInstance()
	{
		return CustomModelLoader.INSTANCE;
	}
}
