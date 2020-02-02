package pl.pabilo8.immersiveintelligence.client.tmt;

/**
 * Created by Pabilo8 on 02-11-2019.
 */
public class TmtNamedBoxGroup
{
	//Those can be set only once!
	final ModelRendererTurbo[] model;
	final String name, texturePath;

	public TmtNamedBoxGroup(String name, ModelRendererTurbo[] model, String texturePath)
	{
		this.name = name;
		this.model = model;
		this.texturePath = texturePath;
	}

	public String getName()
	{
		return name;
	}

	public String getTexturePath()
	{
		return texturePath;
	}

	public ModelRendererTurbo[] getModel()
	{
		return model;
	}

	public void render(float scale)
	{
		for(ModelRendererTurbo mod : model)
			mod.render(scale);
	}
}
