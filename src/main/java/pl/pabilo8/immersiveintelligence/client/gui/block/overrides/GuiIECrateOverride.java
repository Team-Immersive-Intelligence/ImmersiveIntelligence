package pl.pabilo8.immersiveintelligence.client.gui.block.overrides;

import blusunrize.immersiveengineering.common.blocks.wooden.TileEntityWoodenCrate;
import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.compat.ImmersiveEngineeringHelper;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerIICrate;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 17.05.2019
 */
@DecoTemplate(name = "ie_crate_override", category = DecoGuiCategory.GENERIC_TILE)
public class GuiIECrateOverride extends DecoTileGui<TileEntityWoodenCrate, ContainerIICrate<TileEntityWoodenCrate>>
{
	public GuiIECrateOverride(EntityPlayer player, TileEntityWoodenCrate tile)
	{
		super(player, tile, ImmersiveEngineeringHelper.GUI_IE_CRATE_OVERRIDE);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.BG_WOODEN, 0, 0, 176, 76)
				.withTitleBar(tile)
				.conditionally(tile.getBlockMetadata()!=0, builder ->
						builder.withFrame(DecoTextures.FRAME_STEEL_THIN, 4, true)
				)
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 76, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.inventorySlots)
				.withInventoryTitleBar()
				.build();
	}
}
