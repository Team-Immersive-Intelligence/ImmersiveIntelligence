package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoFluidTank;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoItemStackDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityCoagulator;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerCoagulator;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Avalon (avalon@iiteam.net)
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 12.12.2025
 */
@DecoTemplate(name = "coagulator", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiCoagulator extends DecoTileGui<TileEntityCoagulator, ContainerCoagulator>
{
	@DecoResource
	public static final ResourceLocation TEXTURE = IIReference.RES_II.with("gui/coagulator");

	public GuiCoagulator(EntityPlayer player, TileEntityCoagulator tile)
	{
		super(player, tile, IIGUI.COAGULATOR);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.BG_STEEL, 176-24, 0, 24, 76+24)
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 0, 76, 176-24, 24)
				.withInventorySlots(SlotStyle.IE_INPUT, container.slotBucketIn)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.slotBucketOut)

				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 76+24, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();

		addComponents(
				//Energy bar
				new DecoBar(145+5+8, 0)
						.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage))
						.withHeight(76+20-4+2),

				//Tank bottom
				new DecoImage(0, 76-16-3)
						.withSize(24, 16)
						.withImageLocation(TEXTURE, true)
						.withUV(32, 0, 0, 24, 16),
				new DecoImage(24, 76-16-3)
						.withSize(24, 16)
						.withImageLocation(TEXTURE, true)
						.withUV(32, 0, 16, 24, 32),

				new DecoImage(176-64-32, 76-16-3)
						.withSize(24, 16)
						.withImageLocation(TEXTURE, true)
						.withUV(32, 0, 0, 24, 16),
				new DecoImage(176-64-32+24, 76-16-3)
						.withSize(24, 16)
						.withImageLocation(TEXTURE, true)
						.withUV(32, 0, 16, 24, 32),

				new DecoFluidTank(0, 2+6)
						.withSize(48, 58-8)
						.withFluidTank(tile.tankCoagulant),
				new DecoFluidTank(176-64-32, 2+6)
						.withSize(48, 58-8)
						.withFluidTank(tile.tankInput)

		);


		for(int i = 0; i < 6; i++)
		{
			int slotIndex = i;
			final DecoItemStackDisplay stackDisplay = addComponent(new DecoItemStackDisplay(10-4-2+i*26, 20+40+16+2-2))
					.withBackgroundTexture(DecoSprite.atlasSprite(DecoTextures.SLOT_IE, 32, true))
					.withStack(tile.bucketStacks.get(slotIndex))
					.withProgressBar(partialTicks -> tile.getDryingProgressForSlot(slotIndex, partialTicks),
							IIReference.COLOR_GUI_BRASS, IIReference.COLOR_IMMERSIVE_ORANGE);
			addValueListener(() -> tile.bucketStacks.get(slotIndex))
					.addObserver(stackDisplay::withStack);
		}


	}
}

