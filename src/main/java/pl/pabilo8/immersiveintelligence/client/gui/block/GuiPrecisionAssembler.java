package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage.ImageAnimationDirection;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerPrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;


/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @updated 30.80.2025
 * @since 10.07.2019
 */

@DecoTemplate(name = "precision_assembler", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiPrecisionAssembler extends DecoGui<TileEntityPrecisionAssembler, ContainerPrecisionAssembler>
{
	private final TileEntityPrecisionAssembler tile;

	@DecoResource
	public static final ResourceLocation TEXTURE_PRE = IIReference.RES_II.with("gui/precision_assembler");

	public GuiPrecisionAssembler(EntityPlayer player, TileEntityPrecisionAssembler tile)
	{
		super(player, tile, IIGUI.PRECISION_ASSEMBLER);
		this.tile = tile;

	}

	@Override
	public void onInit()
	{

		startBackground()
				.withBox(null, 0, 0, 176, 76)
				.withBox(DecoTextures.GUI_BG_STEEL, 152, 0, 24, 76)
				.withBox(DecoTextures.GUI_BG_WOODEN, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_ROUND_WOODEN, 0, 76, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventorySlots(SlotStyle.IE_INPUT, container.ingredientSlot)
				.withInventorySlots(SlotStyle.IE_INPUT, container.schemeSlot)
				.withInventorySlots(SlotStyle.IE_INPUT, container.toolSlot)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.outputSlot)
				.withInventoryTitleBar()
				.build();

		addComponents(
				new DecoImage(0, 0)
						.withSize(175, 78)
						.withImageLocation(TEXTURE_PRE, true)
						.withUV(256, 0, 0, 175, 78),
				new DecoBar(161-4, 5)
						.withTemplate(DecoGuiUtils.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage))



		);
	}

}

