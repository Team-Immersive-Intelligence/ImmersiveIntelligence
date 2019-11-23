package pl.pabilo8.immersiveintelligence.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.tmt.TmtUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pabilo8 on 2019-06-01.
 * Created using SMP-Toolbox 2.0 (Old stuff, probably considered dead ^^)
 * Thanks to The Flan's Mod Team for the Turbo Model Thingy used in there
 */
public class BaseBlockModel extends ModelBase
{
	//Base Model Part
	public ModelRendererTurbo[] baseModel;
	//List of parts for group flipping / translation / rotation
	public List<ModelRendererTurbo[]> parts = new ArrayList<>();

	public void flipAll()
	{
		if(parts.isEmpty())
			parts.add(baseModel);
		for(ModelRendererTurbo[] mod : parts)
			flip(mod);
	}

	public void flip(ModelRendererTurbo[] model)
	{
		for(ModelRendererTurbo part : model)
		{
			part.doMirror(false, true, true);
			part.setRotationPoint(part.rotationPointX, -part.rotationPointY, -part.rotationPointZ);
		}
	}

	public void translateAll(float x, float y, float z)
	{
		for(ModelRendererTurbo[] mod : parts)
			translate(mod, x, y, z);
	}

	public void rotateAll(float x, float y, float z)
	{
		for(ModelRendererTurbo[] mod : parts)
			rotate(mod, x, y, z);

	}

	public void rotateAddAll(float x, float y, float z)
	{
		for(ModelRendererTurbo[] mod : parts)
			addRotation(mod, x, y, z);
	}

	public void translate(ModelRendererTurbo[] model, float x, float y, float z)
	{
		for(ModelRendererTurbo mod : model)
		{
			mod.rotationPointX += x;
			mod.rotationPointY += y;
			mod.rotationPointZ += z;
		}
	}

	public void rotate(ModelRendererTurbo[] model, float x, float y, float z)
	{
		for(ModelRendererTurbo mod : model)
		{
			mod.rotateAngleX = x;
			mod.rotateAngleY = y;
			mod.rotateAngleZ = z;
		}
	}

	public void addRotation(ModelRendererTurbo[] model, float x, float y, float z)
	{
		for(ModelRendererTurbo mod : model)
		{
			mod.rotateAngleX += x;
			mod.rotateAngleY += y;
			mod.rotateAngleZ += z;
		}
	}

	public void render()
	{
		float f5 = 1F/16F;

		for(ModelRendererTurbo model : baseModel)
			model.render(f5);
	}

	public static void copyModelAngles(BaseBlockModel setmodel, BaseBlockModel getmodel)
	{
		for(int j = 0; j < setmodel.baseModel.length; j++)
		{
			ModelRendererTurbo mod = setmodel.baseModel[j];
			ModelRendererTurbo mod2 = getmodel.baseModel[j];
			mod.rotateAngleX = mod2.rotateAngleX;
			mod.rotateAngleY = mod2.rotateAngleY;
			mod.rotateAngleZ = mod2.rotateAngleZ;
		}
	}

	public static void copyModelPositions(BaseBlockModel setmodel, BaseBlockModel getmodel)
	{
		for(int j = 0; j < setmodel.baseModel.length; j++)
		{
			ModelRendererTurbo mod = setmodel.baseModel[j];
			ModelRendererTurbo mod2 = getmodel.baseModel[j];
			mod.rotationPointX = mod2.rotationPointX;
			mod.rotationPointY = mod2.rotationPointY;
			mod.rotationPointZ = mod2.rotationPointZ;
		}
	}

	public static void getCommonConnectorModelRotation(EnumFacing facing, BaseBlockModel model)
	{
		switch(facing)
		{
			case UP:
			{
				model.rotateAll(0f, 0f, TmtUtil.AngleToTMT(180));
			}
			break;
			case DOWN:
			{
				model.rotateAll(0f, 0f, 0f);
			}
			break;
			case NORTH:
			{
				model.rotateAll(TmtUtil.AngleToTMT(90), 0f, 0f);
			}
			break;
			case SOUTH:
			{
				model.rotateAll(TmtUtil.AngleToTMT(270), 0f, 0f);
			}
			break;
			case EAST:
			{
				model.rotateAll(0f, 0f, TmtUtil.AngleToTMT(90));
			}
			break;
			case WEST:
			{
				model.rotateAll(0f, 0f, TmtUtil.AngleToTMT(270));
			}
			break;
		}
	}

	public void getBlockRotation(EnumFacing facing, BaseBlockModel model)
	{
		switch(facing)
		{
			case NORTH:
			{
				GlStateManager.translate(-1f, 0f, 1f);
			}
			break;
			case SOUTH:
			{
				GlStateManager.rotate(180F, 0F, 1F, 0F);

			}
			break;
			case EAST:
			{
				GlStateManager.rotate(270F, 0F, 1F, 0F);
				GlStateManager.translate(0f, 0f, 1f);

			}
			break;
			case WEST:
			{
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-1f, 0f, 0f);

			}
			break;
		}
	}
}
