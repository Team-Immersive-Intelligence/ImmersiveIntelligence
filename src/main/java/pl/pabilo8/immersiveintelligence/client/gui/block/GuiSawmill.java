package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBarGroup;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage.ImageAnimationDirection;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoResource;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySawmill;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerSawmill;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 01.07.2025
 * @ii-approved 0.3.1
 * @since 10.07.2019
 */
@DecoTemplate(name = "wooden_gearbox")
public class GuiSawmill extends DecoGui<TileEntitySawmill, ContainerSawmill>
{
	@DecoResource
	public static final ResourceLocation PROGRESS_ARROW = ResLoc.of(IIReference.RES_II, "gui/sawmill");

	public GuiSawmill(EntityPlayer player, TileEntitySawmill tile)
	{
		super(player, tile, IIGUI.SAWMILL);
	}

	@Override
	public void onInit()
	{
		syncAnimatedParts(0, true);
		startBackground()
				.withBox(IIReference.GUI_BG_WOODEN, 8, 0, 176-24, 76)
				.withStandaloneFrame(6+8, 12, 128+4-2, 64-8, IIReference.GUI_FRAME_CORNERS_BRASS, 4, true)
				.withTitleBar(tile)
				.withBox(IIReference.GUI_BG_WOODEN, 0, 76, 176, 92)
				.withInventorySlots(SlotStyle.IE_INPUT, container.slotInput)
				.withInventorySlots(SlotStyle.VANILLA, container.slotSaw)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.slotOutput)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.slotOutputTrash)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();

		addComponents(
				new DecoBarGroup(128+28-16+8, 0)
						.withBar(b -> b.withTemplate(DecoGuiUtils.BAR_MECH_TORQUE_INPUT.apply(tile.rotation)))
						.withBar(b -> b.withTemplate(DecoGuiUtils.BAR_MECH_SPEED_INPUT.apply(tile.rotation))),

				new DecoImage(66-10-1-14, 42-4)
						.withSize(49, 12)
						.withImageLocation(PROGRESS_ARROW, true)
						.withUV(64, 0, 12, 49, 24),
				new DecoImage(66-10-1-14, 42-4)
						.withSize(49, 12)
						.withImageLocation(PROGRESS_ARROW, true)
						.withUV(64, 0, 0, 49, 12)
						.withAnimation(ImageAnimationDirection.LEFT_TO_RIGHT, DecoGuiUtils.getMultiblockProductionSingleProgress(tile))
		);
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		syncAnimatedParts(0, false);
	}
}
