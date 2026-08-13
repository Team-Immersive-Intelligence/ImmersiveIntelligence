package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.api.ammocrate.AmmunitionCrateMode;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoElementDisplays;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoTitleLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

/**
 * Displays mode-dependent ammunition, casing, magazine, and revolver-pattern slots.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 13.08.2026
 * @since 17.05.2019
 */
@DecoTemplate(name = "ammunition_crate", category = DecoGuiCategory.GENERIC_TILE)
public class GuiAmmunitionCrate extends DecoTileGui<TileEntityAmmunitionCrate, ContainerAmmunitionCrate>
{
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public AmmunitionCrateMode mode;

	public GuiAmmunitionCrate(EntityPlayer player, TileEntityAmmunitionCrate tile)
	{
		super(player, tile, IIGUI.AMMUNITION_CRATE);
	}

	@Override
	public void onInit()
	{
		this.mode = tile.mode;
		this.container.refreshSlots(mode);

		final IIColor backgroundColor = IIColor.fromPackedRGB(0xd0ebc2);
		startBackground()
				.withBox(DecoTextures.BG_STEEL_ROUGH, DecoTextures.TEMPLATE_ROUND, 0, 0, 224, 160, backgroundColor)
				.withTitleBar(tile)
				.withInventorySlots(SlotStyle.VANILLA, container.inputAmmunition)
				.withInventorySlots(SlotStyle.VANILLA, container.revolverPattern)
				.withInventorySlots(SlotStyle.VANILLA, container.spentCasings)
				.withInventorySlots(SlotStyle.VANILLA, container.spentMagazines)
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 24, 160, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();

		boolean separateMagazines = mode!=AmmunitionCrateMode.REVOLVER&&mode.hasSpentMagazineSlots();
		addLabel(new DecoTitleLabel(this.fontRenderer, mode==AmmunitionCrateMode.REVOLVER?8: 58, 30-2)
				.withBackgroundLocation(DecoTextures.LABEL_WOODEN)
				.withText(IIReference.GUI_LABEL_KEY+"ammunition_crate.section.ammunition")
				.pack()
		);
		addLabel(new DecoTitleLabel(this.fontRenderer, separateMagazines?11: (mode==AmmunitionCrateMode.REVOLVER?8: 22), 106-2)
				.withBackgroundLocation(DecoTextures.LABEL_WOODEN)
				.withText(IIReference.GUI_LABEL_KEY+"ammunition_crate.section.spent_casings")
				.pack()
		);
		if(separateMagazines)
			addLabel(new DecoTitleLabel(this.fontRenderer, xSize/2+6, 106-2)
					.withBackgroundLocation(DecoTextures.LABEL_WOODEN)
					.withText(IIReference.GUI_LABEL_KEY+"ammunition_crate.section.spent_magazines")
					.pack()
			);

		DecoLabel modeLabel = addLabel(IIReference.GUI_LABEL_KEY+"ammunition_crate.mode", 6, 10+3).pack();
		int labelWidth = modeLabel.getWidth();
		addComponent(new DecoDropdown<AmmunitionCrateMode>(6+labelWidth+2, 10)
				.withSize(xSize-6-labelWidth-2-6, 16)
				.withEntries(AmmunitionCrateMode.values())
				.withSelectedEntry(mode)
				.withDisplayFunction(DecoElementDisplays.getSimpleTextDisplay(AmmunitionCrateMode::getLocalizedName))
				.withOnSelectedEntry((oldMode, newMode) -> {
					if(newMode==null||newMode==mode)
						return;

					mode = newMode;
					tile.mode = newMode;
					refreshGUI();
				}));
	}
}
