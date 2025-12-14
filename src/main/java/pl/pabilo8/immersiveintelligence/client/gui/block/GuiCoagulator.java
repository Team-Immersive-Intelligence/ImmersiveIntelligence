package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoFluidTank;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityCoagulator;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerCoagulator;

/**)
 * @author Avalon (avalon@iiteam.net)
 * @since 12.12.2025
 */

@DecoTemplate(name = "coagulator", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiCoagulator extends DecoGui<TileEntityCoagulator, ContainerCoagulator>
{


	public GuiCoagulator(EntityPlayer player, TileEntityCoagulator tile)
	{
		super(player, tile, IIGUI.COAGULATOR);
	}


	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.GUI_BG_STEEL, 0, 0, 176, 76)
				.withTitleBar(tile)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.slotOutput)
				.withInventorySlots(SlotStyle.IE_INPUT, container.slotBucketIn)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.slotBucketOut)

				.withBox(DecoTextures.GUI_BG_WOODEN, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_ROUND_WOODEN, 0, 76, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();

		addComponents(
				new DecoBar(168, 0)
						.withTemplate(DecoGuiUtils.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage)),
				new DecoFluidTank(118-30-30, 8+4-2)
						.withSize(24, 58)
						.withFluidTank(tile.tanks[0]),
				new DecoFluidTank(118-30-30-30, 8+4-2)
						.withSize(24, 58)
						.withFluidTank(tile.tanks[1])

		);

	}

}

