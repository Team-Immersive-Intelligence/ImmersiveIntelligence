package pl.pabilo8.immersiveintelligence.client.util.amt;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.api.utils.upgrade_system.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.api.utils.upgrade_system.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil.Shaders;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation.IIAnimationGroup;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation.IIShaderLine;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation.IIVectorLine;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 13.07.2022
 */
@ParametersAreNonnullByDefault
public class MachineUpgradeModel implements AMTRenderable
{
	private final MachineUpgrade upgrade;
	private final IIAnimationCompiledMap animation;
	private final AMTModel model;
	private final AMT assembledModel;
	private final int steps;

	public MachineUpgradeModel(MachineUpgrade upgrade, ResourceLocation model, ResourceLocation animation)
	{
		this(upgrade, new AMTModel(DefaultVertexFormats.BLOCK, model), animation);
	}

	public MachineUpgradeModel(MachineUpgrade upgrade, AMTModel model, ResourceLocation animation)
	{
		this.upgrade = upgrade;

		IIAnimation loaded = AMTLoader.loadAnimation(animation);

		this.animation = IIAnimationCompiledMap.create(model, new IIAnimation(IIReference.RES_II.with("machine_upgrade/"+upgrade.getName()),
				Arrays.stream(loaded.groups)
						.map(g -> new IIAnimationGroup(g.groupName, g.position, g.scale, g.rotation, null, vecToAlpha(g.position), null))
						.toArray(IIAnimationGroup[]::new)));

		upgrade.setRequiredSteps(this.steps = this.animation.size());

		//get only base level models (model), contained in animation
		this.model = new AMTModel(model.stream()
				.filter(this.animation::containsKey)
				.toArray(AMT[]::new));
		this.assembledModel = this.model.batch("batched");
	}

	@Nullable
	private IIShaderLine vecToAlpha(@Nullable IIVectorLine position)
	{
		if(position==null||position.values.length < 2)
			return null;

		return new IIShaderLine(Shaders.ALPHA,
				new float[]{0f, position.timeframes[0], position.timeframes[position.timeframes.length-1], 1f},
				new Float[][]{
						{0f},
						{0f},
						{1f},
						{1f}
				}
		);
	}

	public UpgradeStage renderConstruction(IUpgradableMachine machine, Tessellator tes, BufferBuilder buf, float partialTicks)
	{
		if(machine.getCurrentlyInstalled()!=upgrade)
			return machine.hasUpgrade(upgrade)?UpgradeStage.INSTALLED: UpgradeStage.NOT_INSTALLED;

		//calculate progress per part
		final int maxProgress = IIContent.UPGRADE_INSERTER.getProgressRequired();
		double maxClientProgress = IIUtils.getMaxClientProgress(machine.getInstallProgress(), maxProgress, steps);

		double currentProgress = (int)Math.min(machine.getClientInstallProgress()+((partialTicks*(Tools.wrenchUpgradeProgress*0.5f))), maxClientProgress);
		float install = (float)MathHelper.clamp(currentProgress/maxProgress, 0, 1);


		//draw blueprint
		ShaderUtil.useBlueprint(0.35f, ClientUtils.mc().player.ticksExisted+partialTicks);
		this.assembledModel.render(tes, buf);
		ShaderUtil.releaseShader();

		model.defaultize();

		//draw construction animation
		animation.apply(install);
		model.render(tes, buf);

		return UpgradeStage.IN_PROGRESS;
	}

	@Override
	public void disposeOf()
	{
		IIAnimationUtils.disposeOf(model);
	}

	@Override
	public void defaultize()
	{
		model.defaultize();
	}

	@Override
	public void render(Tessellator tes, BufferBuilder buf)
	{
		model.render(tes, buf);
	}

	public AMT getPart(String name)
	{
		return model.getPart(name);
	}

	public enum UpgradeStage
	{
		INSTALLED,
		IN_PROGRESS,
		NOT_INSTALLED,
	}

}
