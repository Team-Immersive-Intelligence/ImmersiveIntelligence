package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional.Method;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoFluidTank;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage.ImageAnimationDirection;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityElectrolyzer;
import pl.pabilo8.immersiveintelligence.common.compat.jei.JEIHelper;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerElectrolyzer;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 10.07.2019
 */
@DecoTemplate(name = "electrolyzer", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiElectrolyzer extends DecoGui<TileEntityElectrolyzer, ContainerElectrolyzer>
{
	@DecoResource
	public static final ResourceLocation TEXTURE = ResLoc.of(IIReference.RES_II, "gui/electrolyzer");
	private DecoImage imageProgress;

	public GuiElectrolyzer(EntityPlayer player, TileEntityElectrolyzer tile)
	{
		super(player, tile, IIGUI.ELECTROLYZER);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.GUI_BG_STEEL_ROUGH, 0, 0, 176, 76)
				.withTitleBar(tile)
				.withInventorySlots(SlotStyle.IE_INPUT, container.slotsInput)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.slotsOutput)

				.withBox(DecoTextures.GUI_BG_WOODEN, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_ROUND_WOODEN, 0, 76, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();

		addComponents(
				//Input and Output Fluid Tanks
				new DecoFluidTank(32-1, 12)
						.withSize(24, 58)
						.withFluidTank(tile.tankInput),
				new DecoFluidTank(90-10+2, 10)
						.withSize(58, 24)
						.withFluidTank(tile.tankOutput1),
				new DecoFluidTank(90-10+2, 55-8-1)
						.withSize(58, 24)
						.withFluidTank(tile.tankOutput2),
				//Energy bar
				new DecoBar(168, 0)
						.withTemplate(DecoGuiUtils.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage)),
				//Production progress
				this.imageProgress = new DecoImage(66-10-1, 42-8)
						.withSize(60, 12)
						.withImageLocation(TEXTURE, true)
						.withUV(64, 0, 0, 60, 12),
				new DecoImage(66-10-1, 42-8)
						.withSize(60, 12)
						.withImageLocation(TEXTURE, true)
						.withUV(64, 0, 12, 60, 24)
						.withAnimation(ImageAnimationDirection.LEFT_TO_RIGHT, DecoGuiUtils.getMultiblockProductionSingleProgress(tile)),
				new DecoImage(66-10-1, 42-8)
						.withSize(23, 24)
		);
	}

	@Override
	@Method(modid = "jei")
	public void onInitJEICompat()
	{
		JEIHelper.addRecipesDecoGuiLink(this.imageProgress, "ii.electrolyzer");
	}
}
