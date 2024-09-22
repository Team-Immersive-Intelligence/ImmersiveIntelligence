package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityRepairCrate;

/**
 * @author Pabilo8
 * @since 2019-05-26
 */
@SideOnly(Side.CLIENT)
@RegisteredTileRenderer(name = "block/crate/repair_crate", clazz = TileEntityRepairCrate.class)
public class RepairCrateRenderer extends EffectCrateRenderer<TileEntityRepairCrate>
{
	private ItemStack STACK;
	private final ResourceLocation openAnimation = new ResourceLocation(ImmersiveIntelligence.MODID, "repair_crate_open");

	private final ResourceLocation inserterUpgrade = new ResourceLocation(ImmersiveIntelligence.MODID, "models/block/metal_device/effect_crate/upgrade/upgrade_welder.obj.ie");
	private final ResourceLocation inserterUpgradeAnimation = new ResourceLocation(ImmersiveIntelligence.MODID, "inserter_upgrade_construction");

	@Override
	public void draw(TileEntityRepairCrate te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		super.draw(te, buf, partialTicks, tes);

		if(te.lidAngle > 0)
			renderWrench();

	}

	@Override
	public void compileModels(Tuple<IBlockState, IBakedModel> sModel)
	{
		super.compileModels(sModel);

		//wrench item
		STACK = new ItemStack(IIContent.itemWrench);
	}

	//--- EffectCrateRenderer ---//

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

	//--- Custom Methods ---//

	//Renders the wrench item
	private void renderWrench()
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.425f, 0.425, 0.5f);
		GlStateManager.rotate(180, 0, 1, 0);
		GlStateManager.rotate(60, 0, 0, 1);
		GlStateManager.rotate(-45, 1, 0, 0);
		GlStateManager.rotate(90, 0, 1, 0);
		GlStateManager.scale(0.75, 0.75, 0.75);
		ClientUtils.mc().getRenderItem().renderItem(STACK, TransformType.FIXED);
		GlStateManager.popMatrix();
	}
}
