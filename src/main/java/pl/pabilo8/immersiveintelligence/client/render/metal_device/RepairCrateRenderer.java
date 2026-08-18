package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTItem;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityRepairCrate;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 26.05.2019
 */
@SideOnly(Side.CLIENT)
@RegisteredTileRenderer(name = "block/crate/repair_crate", clazz = TileEntityRepairCrate.class)
public class RepairCrateRenderer extends EffectCrateRenderer<TileEntityRepairCrate>
{
	private final ResourceLocation openAnimation = new ResourceLocation(ImmersiveIntelligence.MODID, "repair_crate_open");
	private final ResourceLocation inserterUpgrade = new ResourceLocation(ImmersiveIntelligence.MODID, "models/block/metal_device/effect_crate/upgrade/upgrade_welder.obj.ie");
	private final ResourceLocation inserterUpgradeAnimation = new ResourceLocation(ImmersiveIntelligence.MODID, "inserter_upgrade_construction");

	//--- EffectCrateRenderer ---//

	@Nonnull
	@Override
	protected AMTModel getModel(IBlockState state, OBJModel model)
	{
		return new AMTModel(state, model, header -> new AMT[]{
				new AMTItem("wrench", header)
						.setStack(IIContent.itemWrench.getStack(1))
		});
	}

	@Override
	public ResourceLocation getOpenAnimationPath()
	{
		return openAnimation;
	}

	@Override
	public ResourceLocation getInserterUpgradeAnimationPath()
	{
		return inserterUpgradeAnimation;
	}

	@Override
	public ResourceLocation getInserterUpgradePath()
	{
		return inserterUpgrade;
	}

}
