package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.simple.tileentity.TileEntitySmallCrate;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerIICrate;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class GuiSmallCrate extends DecoGui<TileEntitySmallCrate, ContainerIICrate<TileEntitySmallCrate>>
{
	public GuiSmallCrate(EntityPlayer player, TileEntitySmallCrate tile)
	{
		super(player, tile, IIGUI.SMALL_CRATE);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(tile.isWooden()?IIReference.GUI_BG_WOODEN: IIReference.GUI_BG_STEEL, 0, 0, 176, 76)
				.withTitleBar(tile)
				.withBox(IIReference.GUI_BG_WOODEN, 0, 76, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.inventorySlots)
				.withInventoryTitleBar()
				.build();
	}
}