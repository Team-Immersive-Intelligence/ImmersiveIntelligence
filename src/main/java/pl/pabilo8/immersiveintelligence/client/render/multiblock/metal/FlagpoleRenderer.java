package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModelBuilder;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCrossVariantReference;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBanner;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFlagpole;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 06.09.2025
 */
@RegisteredTileRenderer(name = "multiblock/flagpole", clazz = TileEntityFlagpole.class)
public class FlagpoleRenderer extends IIMultiblockRenderer<TileEntityFlagpole>
{
	private AMTCachedModel<TileEntityFlagpole> model;
	private AMTCrossVariantReference<AMTBanner> flag;

	@Override
	public void drawAnimated(TileEntityFlagpole te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		applyStandardMirroring(te, true);
		model.getVariant(te, te.style);
		flag.get().setBanner(te.flag)
				.setIsFlag(true)
				.setProperty(AMTUtils.getDebugProgress(100, partialTicks));
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
		UpgradeTechTree.getTreeFor(TileEntityFlagpole.class)
				.withBaseModelLocation(modelDir.with("flagpole_base.obj"));
	}
}
