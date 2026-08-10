package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTUpgradeModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTUpgradeModel.UpgradeStage;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityEffectCrate;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 20.07.2022
 */
public abstract class EffectCrateRenderer<T extends TileEntityEffectCrate> extends IITileRenderer<T>
{
	private IIAnimationCompiledMap animationOpen = null;
	private AMTModel model = null;

	private AMTUpgradeModel modelUpgrade = null;
	private AMT partInserter, partLower, partUpper;

	@Override
	public void draw(T te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//apply animation
		animationOpen.apply(te.lid.getProgress(partialTicks));

		//apply rotation for block facing
		GlStateManager.pushMatrix();
		applyStandardRotation(te.getFacing());

		//render
		model.render(tes, buf);
		GlStateManager.popMatrix();

		//render upgrade
		if(modelUpgrade.renderProgress(te, tes, buf, partialTicks)==UpgradeStage.INSTALLED)
		{
			modelUpgrade.defaultize();
			partInserter.setRotation(new Vec3d(0, -te.calculateInserterAngle(partialTicks), 0));
			float ins = te.calculateInserterAnimation(partialTicks);
			float h = te.calculateInserterHeight(partialTicks);

			partLower.setRotation(new Vec3d(-ins*45-15+(IIMath.clampedLerp3Par(35, 0, -45, h)*ins), 0, 0));
			partUpper.setRotation(new Vec3d(-145+ins*75+(IIMath.clampedLerp3Par(75, -10, 50, h)*ins), 0, 0));
			modelUpgrade.render(tes, buf);
		}

	}

	@Override
	public final void compileModels(IBlockState state, OBJModel model)
	{
		this.model = getModel(state, model);
		animationOpen = IIAnimationCompiledMap.create(this.model, getOpenAnimationPath());

		modelUpgrade = new AMTUpgradeModel(IIContent.UPGRADE_INSERTER,
				getInserterUpgradePath(), getInserterUpgradeAnimationPath());
		partInserter = modelUpgrade.getPart("inserter");
		partUpper = modelUpgrade.getPart("upper");
		partLower = modelUpgrade.getPart("lower");
	}

	@Nonnull
	protected AMTModel getModel(IBlockState state, OBJModel model)
	{
		return new AMTModel(state, model);
	}

	@Override
	protected final void nullifyModels()
	{
		model = AMTUtils.disposeOf(model);
		modelUpgrade = AMTUtils.disposeOf(modelUpgrade);

		animationOpen = null;
	}

	//--- Abstract Methods ---//

	public abstract ResourceLocation getOpenAnimationPath();

	public abstract ResourceLocation getInserterUpgradeAnimationPath();

	public abstract ResourceLocation getInserterUpgradePath();
}
