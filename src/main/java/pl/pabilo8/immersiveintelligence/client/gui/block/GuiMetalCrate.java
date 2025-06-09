package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityMetalCrate;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerIICrate;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 17.05.2019
 */
public class GuiMetalCrate extends DecoGui<TileEntityMetalCrate, ContainerIICrate<TileEntityMetalCrate>>
{
	public GuiMetalCrate(EntityPlayer player, TileEntityMetalCrate tile)
	{
		super(player, tile, IIGUI.METAL_CRATE);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(IIReference.GUI_BG_STEEL, 0, 0, 176, 76)
				.withTitleBar(tile)
				.withBox(IIReference.GUI_BG_WOODEN, 0, 76, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.inventorySlots)
				.withInventoryTitleBar()
				.build();
	}
}