package pl.pabilo8.immersiveintelligence.client.animation;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.client.ShaderUtil;
import pl.pabilo8.immersiveintelligence.client.animation.IIAnimation.IIAnimationGroup;
import pl.pabilo8.immersiveintelligence.client.animation.IIAnimation.IIFloatLine;
import pl.pabilo8.immersiveintelligence.client.animation.IIAnimation.IIVectorLine;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.ArraylistJoinCollector;

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
	private final IIAnimationCompiledMap animation, alpha;
	private final AMT[] model;

	public IIMachineUpgradeModel(MachineUpgrade upgrade, ResourceLocation model, ResourceLocation animation)
	{
		this.upgrade = upgrade;
		this.model = IIAnimationUtils.getAMTFromRes(model, new ResourceLocation(model.getResourceDomain(),
				model.getResourcePath().replace(".obj.ie", ".obj.amt")));

		IIAnimation anim = IIAnimationLoader.loadAnimation(animation), alpha;

		alpha = new IIAnimation(new ResourceLocation(""),
				Arrays.stream(anim.groups)
						.map(g -> new IIAnimationGroup(g.groupName, null, null, null, null, null, vecToAlpha(g.position)))
						.toArray(IIAnimationGroup[]::new));

		this.animation = IIAnimationCompiledMap.create(this.model, anim);
		this.alpha = IIAnimationCompiledMap.create(this.model, alpha);

		upgrade.setRequiredSteps(this.model.length);
	}

	@Nullable
	private IIFloatLine vecToAlpha(IIVectorLine position)
	{
		if(position==null||position.values.length < 2)
			return null;

		return new IIFloatLine(new float[]{
				0f, position.timeframes[0], position.timeframes[position.timeframes.length-1], 1f
		}, new Float[]{
				0f, 0f, 1f, 1f
		});
	}

	public boolean renderConstruction(IUpgradableMachine machine, Tessellator tes, BufferBuilder buf, float partialTicks)
	{
		if(machine.getCurrentlyInstalled()!=upgrade)
			return machine.hasUpgrade(upgrade);

		//calculate progress per part
		final int maxProgress = IIContent.UPGRADE_INSERTER.getProgressRequired();
		double maxClientProgress = Utils.getMaxClientProgress(machine.getInstallProgress(), maxProgress, model.length);

		double currentProgress = (int)Math.min(machine.getClientInstallProgress()+((partialTicks*(Tools.wrench_upgrade_progress*0.5f))), maxClientProgress);
		float install = (float)MathHelper.clamp(currentProgress/maxProgress, 0, 1);

		for(AMT mod : model)
			mod.defaultize();

		//draw blueprint
		ShaderUtil.blueprint_static(0.35f, ClientUtils.mc().player.ticksExisted+partialTicks);
		//alpha.apply(1f-install);
		for(AMT mod : model)
			mod.render(tes, buf);
		ShaderUtil.releaseShader();

		for(AMT mod : model)
			mod.defaultize();

		//draw construction animation
		alpha.apply(install);
		animation.apply(install);
		for(AMT mod : model)
			mod.render(tes, buf);

		return false;
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

}
