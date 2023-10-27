package pl.pabilo8.immersiveintelligence.client.util.amt;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil.Shaders;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimation.IIAnimationGroup;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimation.IIShaderLine;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimation.IIVectorLine;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 13.07.2022
 */
public class IIMachineUpgradeModel
{
	private final MachineUpgrade upgrade;
	private final IIAnimationCompiledMap animation;
	private final AMT[] model;
	private final AMT assembledModel;
	private final int steps;

	public IIMachineUpgradeModel(MachineUpgrade upgrade, ResourceLocation model, ResourceLocation animation)
	{
		this(upgrade,
				IIAnimationUtils.getAMTFromRes(model, new ResourceLocation(model.getResourceDomain(),
						model.getResourcePath().replace(".obj.ie", ".obj.amt"))),
				animation);
	}

	public IIMachineUpgradeModel(MachineUpgrade upgrade, AMT[] model, ResourceLocation animation)
	{
		this.upgrade = upgrade;

		IIAnimation loaded = IIAnimationLoader.loadAnimation(animation);

		this.animation = IIAnimationCompiledMap.create(model, new IIAnimation(new ResourceLocation(""),
				Arrays.stream(loaded.groups)
						.map(g -> new IIAnimationGroup(g.groupName, g.position, g.scale, g.rotation, null, vecToAlpha(g.position), null))
						.toArray(IIAnimationGroup[]::new)));

		upgrade.setRequiredSteps(this.steps = this.animation.size());

		//get only base level models (model), contained in animation
		this.model = Arrays.stream(model)
				.filter(this.animation::containsKey)
				.toArray(AMT[]::new);
		this.assembledModel = IIAnimationUtils.batchMultipleAMTQuads(this.model, "batched");
	}

	@Nullable
	private IIShaderLine vecToAlpha(IIVectorLine position)
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

		for(AMT mod : model)
			mod.defaultize();

		//draw construction animation
		animation.apply(install);
		for(AMT mod : model)
			mod.render(tes, buf);

		return UpgradeStage.IN_PROGRESS;
	}

	public IIMachineUpgradeModel disposeOf()
	{
		IIAnimationUtils.disposeOf(model);
		return null;
	}

	public void defaultize()
	{
		for(AMT mod : model)
			mod.defaultize();
	}

	public void render(Tessellator tes, BufferBuilder buf)
	{
		for(AMT amt : model)
			amt.render(tes, buf);
	}

	@Nonnull
	public AMT getPart(String name)
	{
		return IIAnimationUtils.getPart(model, name);
	}

	public enum UpgradeStage
	{
		INSTALLED,
		IN_PROGRESS,
		NOT_INSTALLED,
	}

}
