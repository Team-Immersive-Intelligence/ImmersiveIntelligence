package pl.pabilo8.immersiveintelligence.client.gui.block;


import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 17.05.2019
 */
@DecoTemplate(name = "ammunition_crate", category = DecoGuiCategory.GENERIC_TILE)
public class GuiAmmunitionCrate extends DecoTileGui<TileEntityAmmunitionCrate, ContainerAmmunitionCrate>
{
	public GuiAmmunitionCrate(EntityPlayer player, TileEntityAmmunitionCrate tile)
	{
		super(player, tile, IIGUI.AMMUNITION_CRATE);
	}

	@Override
	public void onInit()
	{
		final IIColor backgroundColor = IIColor.fromPackedRGB(0xd0ebc2);
		startBackground()
				.withBox(DecoTextures.BG_STEEL_ROUGH, DecoTextures.TEMPLATE_ROUND, 0, 0, 176, 126+8, backgroundColor)
				.withTitleBar(tile)
				.conditionally(tile.isUpgradeInstalled(IIContent.UPGRADE_MG_LOADER), builder -> builder
						.withNextLayer()
						.withBox(DecoTextures.BG_STEEL_ROUGH, DecoTextures.TEMPLATE_ROUND, 176, 0, 48, 126+8, backgroundColor)
						.withInventorySlots(SlotStyle.VANILLA, container.inputMG)
				)
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 126+8, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventorySlots(SlotStyle.VANILLA, container.inputRevolver)
				.withInventorySlots(SlotStyle.VANILLA, container.inputBullet)
				.withInventorySlots(SlotStyle.VANILLA, container.inputShell)
				.withInventoryTitleBar()
				.build();



		/*addComponents(

				new DecoImage(0, -45)
						.withSize(175, 132)
						.withImageLocation(TEXTURE_AMMO, true)
						.withUV(256, 0, 0, 175, 132)
		);*/
	}
}
