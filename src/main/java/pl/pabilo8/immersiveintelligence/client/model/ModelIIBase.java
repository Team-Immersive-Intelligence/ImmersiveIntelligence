package pl.pabilo8.immersiveintelligence.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

import java.util.LinkedHashMap;

/**
 * Created by Pabilo8 on 2019-06-01.
 * Created using SMP-Toolbox 2.0 (Old stuff, probably considered dead ^^)
 * Thanks to The Flan's Mod Team for the Turbo Model Thingy used in there
 */
public class ModelIIBase extends ModelBase
{
	//Base Model Part
	public ModelRendererTurbo[] baseModel;
	//List of parts for group flipping / translation / rotation
	public LinkedHashMap<String, ModelRendererTurbo[]> parts = new LinkedHashMap<>();

	public static void getCommonConnectorModelRotation(EnumFacing facing, ModelIIBase model)
	{
		switch(facing)
		{
			case UP:
			{
				GlStateManager.rotate(180, 0, 0, 1);
				GlStateManager.translate(-1, -1, 0);
				//model.rotateAll(0f, 0f, TmtUtil.AngleToTMT(180));
			}
			break;
			case DOWN:
			{
				//model.rotateAll(0f, 0f, 0f);
			}
			break;
			case NORTH:
			{
				GlStateManager.rotate(90, 1, 0, 0);
				GlStateManager.rotate(90, 0, 1, 0);
				GlStateManager.translate(0, -1, 1);
				//model.rotateAll(TmtUtil.AngleToTMT(90), 0f, 0f);
			}
			break;
			case SOUTH:
			{
				GlStateManager.rotate(-90, 1, 0, 0);
				GlStateManager.rotate(90, 0, 1, 0);
				GlStateManager.translate(-1, 0, 1);
				//model.rotateAll(TmtUtil.AngleToTMT(270), 0f, 0f);
			}
			break;
			case EAST:
			{
				GlStateManager.rotate(90, 0, 0, 1);
				GlStateManager.translate(0, -1, 0);
				//model.rotateAll(0f, 0f, TmtUtil.AngleToTMT(90));
			}
			break;
			case WEST:
			{
				GlStateManager.rotate(-90, 0, 0, 1);
				GlStateManager.translate(-1, 0, 0);
				//model.rotateAll(0f, 0f, TmtUtil.AngleToTMT(270));
			}
			break;
		}
	}

	public void flipAll()
	{
		if(parts.isEmpty())
			parts.put("base", baseModel);
		for(ModelRendererTurbo[] mod : parts.values())
			flip(mod);
	}

	public void flipAllZ()
	{
		if(parts.isEmpty())
			parts.put("base", baseModel);
		for(ModelRendererTurbo[] mod : parts.values())
		{
			flipZ(mod);
			for(ModelRendererTurbo m : mod)
			{
				m.rotateAngleY *= -1;
			}
		}
	}

	public void flipAllX()
	{
		if(parts.isEmpty())
			parts.put("base", baseModel);
		for(ModelRendererTurbo[] mod : parts.values())
		{
			flipX(mod);
			for(ModelRendererTurbo m : mod)
			{
				m.rotateAngleY *= -1;
				m.rotateAngleZ *= -1;
			}
		}
	}

	public void flip(ModelRendererTurbo[] model)
	{
		for(ModelRendererTurbo part : model)
		{
			part.doMirror(false, true, true);
			part.setRotationPoint(part.rotationPointX, -part.rotationPointY, -part.rotationPointZ);
			part.setMirrored(!part.mirror);
		}
	}

	public void flipZ(ModelRendererTurbo[] model)
	{
		for(ModelRendererTurbo part : model)
		{
			part.doMirror(false, false, true);
			if(!part.flip)
				part.setRotationPoint(part.rotationPointX, part.rotationPointY, -part.rotationPointZ);
			else
				part.setRotationPoint(part.rotationPointX, -part.rotationPointY, part.rotationPointZ);

		}
	}

	public void flipX(ModelRendererTurbo[] model)
	{
		for(ModelRendererTurbo part : model)
		{
			part.doMirror(true, false, false);
			if(!part.flip)
				part.setRotationPoint(-part.rotationPointX, part.rotationPointY, part.rotationPointZ);
			else
				part.setRotationPoint(-part.rotationPointX, -part.rotationPointY, part.rotationPointZ);

		}
	}

	public void translateAll(float x, float y, float z)
	{
		for(ModelRendererTurbo[] mod : parts.values())
			translate(mod, x, y, z);
	}

	public void rotateAll(float x, float y, float z)
	{
		for(ModelRendererTurbo[] mod : parts.values())
			rotate(mod, x, y, z);

	}

	public void rotateAddAll(float x, float y, float z)
	{
		for(ModelRendererTurbo[] mod : parts.values())
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

	public void getBlockRotation(EnumFacing facing, boolean mirrored)
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
