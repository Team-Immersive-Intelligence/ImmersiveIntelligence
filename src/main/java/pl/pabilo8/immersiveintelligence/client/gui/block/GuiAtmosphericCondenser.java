package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoFluidTank;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityAtmosphericCondenser;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerAtmosphericCondenser;

import static pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures.FRAME_STEEL;

/**
 * @author Avalon (avalon@iiteam.net)
 * @since 04.11.2026
 */
@DecoTemplate(name = "atmospheric_condenser", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiAtmosphericCondenser extends DecoTileGui<TileEntityAtmosphericCondenser, ContainerAtmosphericCondenser>
{
	public GuiAtmosphericCondenser(EntityPlayer player, TileEntityAtmosphericCondenser tile)
	{
		super(player, tile, IIGUI.ATMOSPHERIC_CONDENSER);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.BG_ALUMINIUM, 0, 0, 176, 76)
				.withTitleBar(tile)
				.withInventorySlots(SlotStyle.IE_INPUT, container.bucketInputSlots)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.bucketOutputSlots)
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 76, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.build();

		addComponents(
				new DecoFluidTank(3, 10+4)
						.withSize(24, 48)
						.withFluidTank(tile.nitrogenTank),
				new DecoFluidTank(59, 10+4)
						.withSize(24, 48)
						.withFluidTank(tile.oxygenTank),
				new DecoFluidTank(115, 10+4)
						.withSize(24, 48)
						.withFluidTank(tile.argonTank),
				new DecoBar(164, 2)
						.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage))
		);

	}
}
