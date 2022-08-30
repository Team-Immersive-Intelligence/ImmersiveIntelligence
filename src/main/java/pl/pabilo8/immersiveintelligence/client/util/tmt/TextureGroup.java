package pl.pabilo8.immersiveintelligence.client.util.tmt;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

public class TextureGroup
{
	public TextureGroup()
	{
		poly = new ArrayList<>();
		texture = "";
	}

	public void addPoly(TexturedPolygon polygon)
	{
		poly.add(polygon);
	}

	public void loadTexture()
	{
		loadTexture(-1);
	}

	public void loadTexture(int defaultTexture)
	{
		if(!texture.equals(""))
		{
			TextureManager renderengine = ClientUtils.mc().renderEngine;
			renderengine.bindTexture(new ResourceLocation("", texture)); //TODO : Check. Not sure about this one
		}
		else if(defaultTexture > -1)
		{
			ClientUtils.mc().renderEngine.bindTexture(new ResourceLocation("", ""));
		}
	}

	public ArrayList<TexturedPolygon> poly;
	public String texture;
}
