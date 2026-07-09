package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTConstructionModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTUpgradeModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTUpgradeModel.UpgradeStage;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRadar;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 31.08.2025
 * @ii-approved 0.3.1
 * @since 21.06.2019
 */
@RegisteredTileRenderer(name = "multiblock/radar", clazz = TileEntityRadar.class)
public class RadarRenderer extends IIMultiblockRenderer<TileEntityRadar>
{
	private AMTModel model;
	private AMTConstructionModel constructionModel;
	private AMTUpgradeModel radioLocatorsUpgrade;
	private IIAnimationCompiledMap animationDish;

	@Override
	public void drawAnimated(TileEntityRadar te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Reset model to default state + apply rotation and mirroring
		model.defaultize();
		applyStandardMirroring(te, true);

		//Draw construction model or finished one
		if(constructionModel.renderProgress(te, tes, buf, partialTicks)==AMTConstructionModel.ConstructionStage.FINISHED)
		{
			if(radioLocatorsUpgrade.renderProgress(te, tes, buf, partialTicks)==UpgradeStage.INSTALLED)
			{
				radioLocatorsUpgrade.defaultize();
				radioLocatorsUpgrade.render(tes, buf);
			}

			float progress = (((te.dishRotation+(te.active?partialTicks: 0))%360)/360f);
			animationDish.apply(te.mirrored?progress: (1f-progress));
			model.render(tes, buf);
		}
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Reset model to default state and render
		model.defaultize();
		animationDish.apply(0.5f);
		model.render(tes, buf);
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		this.model = new AMTModel(state, model);
		ResLoc modelDir = IIReference.RES_BLOCK_MODEL.with("multiblock/radar/");
		this.constructionModel = new AMTConstructionModel(
				modelDir.with("radar_construction.obj.ie"),
				new ResourceLocation(ImmersiveIntelligence.MODID, "radar/construction")
		);
		this.radioLocatorsUpgrade = new AMTUpgradeModel(
				IIContent.UPGRADE_RADIO_LOCATORS,
				modelDir.with("upgrade_traingulators.obj.ie"),
				new ResourceLocation(ImmersiveIntelligence.MODID, "radar/upgrade_triangulators")
		);

		this.animationDish = IIAnimationCompiledMap.create(this.model, IIReference.RES_II.with("radar/dish"));

		UpgradeTechTree.getTreeFor(TileEntityRadar.class)
				.withBaseModelLocation(modelDir.with("radar_inv.obj"))
				.withUpgradeModelLocation(IIContent.UPGRADE_RADIO_LOCATORS, modelDir.with("upgrade_traingulators.obj.ie"));

	}
}
