package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModelBuilder;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCrossVariantReference;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBanner;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTWire;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFlagpole;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Renders the flagpole, its selected banner, style variant, and installed upgrades.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 14.08.2026
 * @ii-approved 0.3.1
 * @since 06.09.2025
 */
@RegisteredTileRenderer(name = "multiblock/flagpole", clazz = TileEntityFlagpole.class)
public class FlagpoleRenderer extends IIMultiblockRenderer<TileEntityFlagpole>
{
	private AMTCachedModel<TileEntityFlagpole> model;
	private AMTCrossVariantReference<AMTBanner> flag;
	private final Map<Upgrade, AMTModel> upgradeModels = new LinkedHashMap<>();

	@Override
	public void drawAnimated(TileEntityFlagpole te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		applyStandardMirroring(te, true);
		model.getVariant(te, te.style);
		flag.get().setBanner(te.getFlagOrFactionFlag())
				.setIsFlag(true)
				.setProperty(AMTUtils.getDebugProgress(100, partialTicks));

		//Render all installed upgrade models in the same transform as the base model.
		for(Map.Entry<Upgrade, AMTModel> entry : upgradeModels.entrySet())
			if(te.isUpgradeInstalled(entry.getKey()))
			{
				entry.getValue().defaultize();
				entry.getValue().render(tes, buf);
			}

		model.render(tes, buf);
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		model.defaultize();
		flag.get().setBanner(ItemStack.EMPTY).setIsFlag(true);
		model.render(tes, buf);
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		ResLoc modelDir = IIReference.RES_BLOCK_MODEL.with("multiblock/flagpole/");
		this.model = AMTCachedModelBuilder.startTileEntityModel(TileEntityFlagpole.class)
				.withModel(model)
				.withHeader(IIReference.RES_BLOCK_MODEL.with("multiblock/flagpole/flagpole.obj.amt"))
				//Style Variants
				.withModel(te -> te==null||te.style.getStyle().equals("sandbags"),
						modelDir.with("variant_sandbags.obj"))
				.withModel(te -> te!=null&&te.style.getStyle().equals("bricks"),
						modelDir.with("variant_bricks.obj"))
				.withModel(te -> te!=null&&te.style.getStyle().equals("concrete"),
						modelDir.with("variant_concrete.obj"))
				.withModel(te -> te!=null&&te.style.getStyle().equals("wooden"),
						modelDir.with("variant_wooden.obj"))
				.withModel(te -> te!=null&&te.style.getStyle().equals("steel"),
						modelDir.with("variant_steel.obj"))
				.withModelProvider((te, header) -> new AMT[]{new AMTBanner("flag", header)})
				.build();

		this.flag = new AMTCrossVariantReference<>("flag", this.model);

		upgradeModels.clear();
		upgradeModels.put(IIContent.UPGRADE_FLAGPOLE_UNIT_POST,
				new AMTModel(DefaultVertexFormats.BLOCK, modelDir.with("upgrade_unit_post.obj"), header -> new AMT[0]));
		upgradeModels.put(IIContent.UPGRADE_FLAGPOLE_DISTRESS_SIGNAL,
				new AMTModel(DefaultVertexFormats.BLOCK, modelDir.with("upgrade_distress_signal.obj"), header -> new AMT[]{
						new AMTWire("distress_signal_wire", header)
				}));
		upgradeModels.put(IIContent.UPGRADE_FLAGPOLE_TASER_LOCKS,
				new AMTModel(DefaultVertexFormats.BLOCK, modelDir.with("upgrade_taser_locks.obj"), header -> new AMT[]{
						new AMTWire("taser_locks_wire", header)
				}));
		upgradeModels.put(IIContent.UPGRADE_FLAGPOLE_CAPTURE_DEFIANCE,
				new AMTModel(DefaultVertexFormats.BLOCK, modelDir.with("upgrade_capture_defiance.obj"), header -> new AMT[0]));

		UpgradeTechTree.getTreeFor(TileEntityFlagpole.class)
				.withBaseModelLocation(modelDir.with("flagpole_base.obj"))
				.withUpgradeModelLocation(IIContent.UPGRADE_FLAGPOLE_UNIT_POST, modelDir.with("upgrade_unit_post.obj"))
				.withUpgradeModelLocation(IIContent.UPGRADE_FLAGPOLE_DISTRESS_SIGNAL, modelDir.with("upgrade_distress_signal.obj"))
				.withUpgradeModelLocation(IIContent.UPGRADE_FLAGPOLE_TASER_LOCKS, modelDir.with("upgrade_taser_locks.obj"))
				.withUpgradeModelLocation(IIContent.UPGRADE_FLAGPOLE_CAPTURE_DEFIANCE, modelDir.with("upgrade_capture_defiance.obj"));
	}

	@Override
	protected void nullifyModels()
	{
		super.nullifyModels();
		model = AMTUtils.disposeOf(model);
		flag = null;
		for(AMTModel upgradeModel : upgradeModels.values())
			AMTUtils.disposeOf(upgradeModel);
		upgradeModels.clear();
	}
}
