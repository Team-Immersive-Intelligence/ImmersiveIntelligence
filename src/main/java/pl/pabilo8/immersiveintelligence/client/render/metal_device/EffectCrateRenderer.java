package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIMachineUpgradeModel.UpgradeStage;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityEffectCrate;

/**
 * @author Pabilo8
 * @since 20.07.2022
 */
public abstract class EffectCrateRenderer<T extends TileEntityEffectCrate> extends IITileRenderer<T>
{
	private IIAnimationCompiledMap animationOpen = null;
	private AMT[] model = null;

	private IIMachineUpgradeModel modelUpgrade = null;
	private AMT partInserter, partLower, partUpper;


	@Override
	public void draw(T te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		float progress = Math.min(1.5f, Math.max(te.lidAngle+(te.open?0.2f*partialTicks: -0.3f*partialTicks), 0f))/1.5f;

		//apply animation
		animationOpen.apply(progress);

		//apply rotation for block facing
		GlStateManager.pushMatrix();
		applyStandardRotation(te.getFacing());

		//render
		for(AMT mod : model)
			mod.render(tes, buf);
		GlStateManager.popMatrix();

		//render upgrade
		if(modelUpgrade.renderConstruction(te, tes, buf, partialTicks)==UpgradeStage.INSTALLED)
		{
			modelUpgrade.defaultize();
			IIAnimationUtils.setModelRotation(partInserter, 0, -te.calculateInserterAngle(partialTicks), 0);
			float ins = te.calculateInserterAnimation(partialTicks);
			float h = te.calculateInserterHeight(partialTicks);

			IIAnimationUtils.setModelRotation(partLower, -ins*45-15, 0, 0);
			IIAnimationUtils.setModelRotation(partUpper, -145+ins*75, 0, 0);

			IIAnimationUtils.addModelRotation(partLower, IIUtils.clampedLerp3Par(35,0,-45,h)*ins, 0, 0);
			IIAnimationUtils.addModelRotation(partUpper, IIUtils.clampedLerp3Par(75,-10,50,h)*ins, 0, 0);


			modelUpgrade.render(tes, buf);
		}

	}

	@Override
	public void compileModels(Tuple<IBlockState, IBakedModel> sModel)
	{
		model = IIAnimationUtils.getAMT(sModel, IIAnimationLoader.loadHeader(sModel.getSecond()));
		animationOpen = IIAnimationCompiledMap.create(model, getOpenAnimationPath());

		modelUpgrade = new IIMachineUpgradeModel(
				IIContent.UPGRADE_INSERTER, getInserterUpgradePath(), getInserterUpgradeAnimationPath()
		);

		partInserter = modelUpgrade.getPart("inserter");
		partUpper = modelUpgrade.getPart("upper");
		partLower = modelUpgrade.getPart("lower");
	}

	@Override
	protected void nullifyModels()
	{
		model = IIAnimationUtils.disposeOf(model);
		modelUpgrade = modelUpgrade==null?null: modelUpgrade.disposeOf();

		animationOpen = null;
	}

	public static void renderWithUpgrade(MachineUpgrade... upgrades)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(-0.5, 0, 0.5);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		//model.getBlockRotation(EnumFacing.NORTH, false);

		for(MachineUpgrade upgrade : upgrades)
		{
			if(upgrade==IIContent.UPGRADE_INSERTER)
			{
				GlStateManager.pushMatrix();
				GlStateManager.popMatrix();
			}
		}

		GlStateManager.popMatrix();
	}

	//--- Abstract Methods ---//

	public abstract ResourceLocation getOpenAnimationPath();

	public abstract ResourceLocation getInserterUpgradeAnimationPath();

	public abstract ResourceLocation getInserterUpgradePath();
}
