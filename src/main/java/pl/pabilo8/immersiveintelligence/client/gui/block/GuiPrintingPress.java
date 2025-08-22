package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoFluidTank;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage.ImageAnimationDirection;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPrintingPress;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerPrintingPress;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 13.12.2023
 * @updated 15.06.2025
 * @ii-approved 0.3.1
 * @since 10.07.2019
 */
@DecoTemplate(name = "printing_press", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiPrintingPress extends DecoGui<TileEntityPrintingPress, ContainerPrintingPress>
{
	@DecoResource
	public static ResourceLocation BACKGROUND = ResLoc.of(IIReference.RES_II, "gui/printing_press");
	private final TileEntityPrintingPress tile;

	public GuiPrintingPress(EntityPlayer player, TileEntityPrintingPress tile)
	{
		super(player, tile, IIGUI.PRINTING_PRESS);
		this.ySize = 176;
		this.tile = tile;
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.GUI_BG_STEEL_ROUGH, 0, 0, 176, 76)
				.withTitleBar(tile)
				.withInventorySlots(SlotStyle.IE_INPUT, container.slotInput)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.slotOutput)
				.withInventorySlots(SlotStyle.IE_INPUT, container.slotBucketIn)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.slotBucketOut)

				.withBox(DecoTextures.GUI_BG_WOODEN, 0, 76, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();

		addComponents(
				new DecoBar(168, 0)
						.withTemplate(DecoGuiUtils.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage)),
				new DecoFluidTank(118, 8+4-2)
						.withSize(24, 58)
						.withFluidTank(tile.tank),
				new DecoImage(13-4+16+10, 39-8-1)
						.withSize(51, 18)
						.withImageLocation(BACKGROUND, true)
						.withUV(64, 0, 0, 51, 18),
				new DecoImage(13-4+16+10, 39-8-1)
						.withSize(51, 18)
						.withImageLocation(BACKGROUND, true)
						.withUV(64, 0, 18, 51, 18+18)
						.withAnimation(ImageAnimationDirection.LEFT_TO_RIGHT, DecoGuiUtils.getMultiblockProductionMultiProgress(tile))
		);
	}
}
