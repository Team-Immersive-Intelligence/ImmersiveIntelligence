package pl.pabilo8.immersiveintelligence.client.render.mechanical_device;

import net.minecraft.client.renderer.texture.TextureMap;
import pl.pabilo8.immersiveintelligence.api.rotary.IIRotaryUtils;
import pl.pabilo8.immersiveintelligence.api.rotary.MotorBeltType;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTQuads;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationUtils;
import pl.pabilo8.immersiveintelligence.common.IILogger;

import java.util.HashMap;

public class BeltModelStorage implements IReloadableModelContainer<BeltModelStorage>
{
	private static final HashMap<MotorBeltType, AMTQuads> beltModels = new HashMap<>();

	public static AMTQuads getModelForBelt(MotorBeltType belt)
	{
		return beltModels.get(belt);
	}

	@Override
	public void reloadModels()
	{
		//Start reload
		beltModels.values().forEach(AMT::disposeOf);
		beltModels.clear();

		//Load all motor belt textures
		for(MotorBeltType value : IIRotaryUtils.getAllMotorBelts())
			try
			{
				beltModels.put(value, (AMTQuads)IIAnimationUtils.getAMTFromRes(value.getModelPath(), null)[0]);
			} catch(ArrayIndexOutOfBoundsException e)
			{
				IILogger.error("No AMT found for motor belt \""+value.getUniqueName()+"\". Is the model missing?");
			}
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		IIRotaryUtils.getAllMotorBelts().forEach(m ->
				IIAnimationLoader.preloadTexturesFromOBJ(m.getModelPath(), map));
	}
}
