package pl.pabilo8.immersiveintelligence.client.util.amt.models;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IConstructionRequiringDevice;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Construction model for machines requiring construction progress.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 31.08.2025
 */
@ParametersAreNonnullByDefault
public class AMTConstructionModel extends AMTProgressModel<IConstructionRequiringDevice, AMTConstructionModel.ConstructionStage>
{
	public AMTConstructionModel(ResourceLocation model, ResourceLocation animation)
	{
		super(model, animation);
	}

	public AMTConstructionModel(AMTModel model, ResourceLocation animation)
	{
		super(model, animation);
	}

	@Override
	public ConstructionStage renderProgress(IConstructionRequiringDevice device, Tessellator tes, BufferBuilder buf, float partialTicks)
	{
		final int maxProgress = device.getConstructionCost();
		int currentProgress = device.getCurrentConstruction(partialTicks);
		float progress = MathHelper.clamp((float)currentProgress/(float)maxProgress, 0, 1);

		if(progress >= 1f)
			return ConstructionStage.FINISHED;
		else
		{
			//draw blueprint
			ShaderUtil.useBlueprint(0.35f, ClientUtils.mc().player.ticksExisted+partialTicks);
			this.assembledModel.render(tes, buf);
			ShaderUtil.releaseShader();

			//draw construction animation
			model.defaultize();
			animation.apply(progress);
			model.render(tes, buf);

			if(progress > 0f)
				return ConstructionStage.IN_PROGRESS;
			else
				return ConstructionStage.NOT_STARTED;
		}
	}

	public enum ConstructionStage
	{
		FINISHED,
		IN_PROGRESS,
		NOT_STARTED
	}
}
