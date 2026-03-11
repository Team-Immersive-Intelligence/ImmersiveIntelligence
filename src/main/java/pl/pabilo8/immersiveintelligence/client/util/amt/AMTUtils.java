package pl.pabilo8.immersiveintelligence.client.util.amt;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.models.obj.IEOBJLoader;
import blusunrize.immersiveengineering.client.models.obj.IEOBJModel;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.MaterialLibrary;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTRenderable;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation.IIAnimationGroup;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 02.04.2022
 */
@SideOnly(Side.CLIENT)
public class AMTUtils
{
	//--- Empty OBJ Model Placeholder ---//
	private static final OBJModel EMPTY = new OBJModel(new MaterialLibrary(), ResLoc.of(IIReference.RES_BLOCK_MODEL, "empty.obj"));

	//--- Time Calculation ---//

	@SideOnly(Side.CLIENT)
	public static float getDebugProgress(float max, float partialTicks)
	{
		return (ClientUtils.mc().world.getTotalWorldTime()%(int)max+partialTicks)/max;
	}

	public static float getAnimationOffsetProgress(float current, float begin, float end, float partialTicks)
	{
		if(current < begin)
			return 0;
		if(current > end)
			return 1;
		return (current-begin+partialTicks)/(end-begin);
	}

	public static float getAnimationProgress(float current, float max, boolean invert, float partialTicks)
	{
		return MathHelper.clamp(invert?(1f-((current-partialTicks)/max)): ((current+partialTicks)/max), 0, 1);
	}

	public static float getAnimationProgress(float current, float max, boolean shouldAnimate, boolean reverse, float posStep, float negStep, float partialTicks)
	{
		return current==0?0: (current==max?1: (MathHelper.clamp((current+(shouldAnimate?((reverse?-negStep: posStep)*partialTicks): 0))/max, 0f, 1f)));
	}

	/**
	 * @param progress of the full animation
	 * @param offset   of the current animation
	 * @param duration of the current animation
	 * @return animation time 0-1
	 */
	public static float getOffset(float progress, float offset, float duration)
	{
		return MathHelper.clamp((progress-offset)/duration, 0, 1);
	}

	//--- Manual Rendering Stuff ---//

	public static BlockRendererDispatcher getBRD()
	{
		return Minecraft.getMinecraft().getBlockRendererDispatcher();
	}

	//--- AMT Based Animations ---//

	/**
	 * Applies animation to an AMT
	 *
	 * @param model the AMT
	 * @param group an animation group providing the transforms
	 * @param time  of the animation in range 0.0-1.0
	 */
	public static void setModelAnimations(AMT model, IIAnimationGroup group, float time)
	{
		if(group.visibility!=null)
			model.setVisible(group.visibility.getForTime(time));

		if(group.position!=null)
			model.setPosition(group.position.getForTime(time));
		if(group.rotation!=null)
			model.setRotation(group.rotation.getForTime(time));
		if(group.scale!=null)
			model.setScale(group.scale.getForTime(time));

		if(group.shader!=null)
			model.setShader(group.shader.getShader(), group.shader.getForTime(time));

		if(group.property!=null)
			model.setProperty(group.property.getForTime(time));

	}

	public static OBJModel modelFromRes(ResourceLocation res)
	{
		try
		{
			OBJModel model;
			if(res.getResourcePath().endsWith(".obj.ie"))
				model = ((IEOBJModel)IEOBJLoader.instance.loadModel(res));
			else
				model = (OBJModel)OBJLoader.INSTANCE.loadModel(res);
			return ((OBJModel)model.process(ImmutableMap.of("flip-v", String.valueOf(true))));
		} catch(Exception ignored)
		{
			IILogger.error("Couldn't load model for {}, either the path used is incorrect or a model file may be missing!", res);
			return EMPTY;
//			return ((OBJModel)ModelLoaderRegistry.getMissingModel());
		}
	}

	@Nonnull(when = When.NEVER)
	public static <T extends AMTRenderable> T disposeOf(@Nullable T model)
	{
		if(model!=null)
			model.disposeOf();
		return null;
	}

	public static <T extends AMTRenderable> void disposeOf(@Nullable T... model)
	{
		if(model!=null)
			Arrays.stream(model).filter(Objects::nonNull).forEach(AMTRenderable::disposeOf);
	}
}
