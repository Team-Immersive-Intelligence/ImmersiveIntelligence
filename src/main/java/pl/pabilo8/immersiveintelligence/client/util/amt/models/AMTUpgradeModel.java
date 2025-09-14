package pl.pabilo8.immersiveintelligence.client.util.amt.models;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.api.upgrade.IUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 13.07.2022
 */
@ParametersAreNonnullByDefault
public class AMTUpgradeModel extends AMTProgressModel<IUpgradableDevice, AMTUpgradeModel.UpgradeStage>
{
	private final Upgrade upgrade;

	public AMTUpgradeModel(Upgrade upgrade, ResourceLocation model, ResourceLocation animation)
	{
		super(model, animation);
		this.upgrade = upgrade;
	}

	public AMTUpgradeModel(Upgrade upgrade, AMTModel model, ResourceLocation animation)
	{
		super(model, animation);
		this.upgrade = upgrade;
	}

	@Override
	public UpgradeStage renderProgress(IUpgradableDevice machine, Tessellator tes, BufferBuilder buf, float partialTicks)
	{
		if(machine.getCurrentUpgrade()!=upgrade)
			return machine.isUpgradeInstalled(upgrade)?UpgradeStage.INSTALLED: UpgradeStage.NOT_INSTALLED;

		final int maxProgress = pl.pabilo8.immersiveintelligence.common.IIContent.UPGRADE_INSERTER.getProgressRequired();
		double maxClientProgress = UpgradeUtils.getMaxClientProgress(machine.getUpgradeInstallProgress(false), upgrade);
		double currentProgress = Math.min(machine.getUpgradeInstallProgress(true)+((partialTicks*(pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools.wrenchUpgradeProgress*0.5f))), maxClientProgress);
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

	public enum UpgradeStage
	{
		INSTALLED,
		IN_PROGRESS,
		NOT_INSTALLED,
	}
}
