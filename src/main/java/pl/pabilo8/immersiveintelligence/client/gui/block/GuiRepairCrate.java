package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoCheckbox;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityRepairCrate;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerRepairCrate;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @updated 13.08.2026
 * @ii-approved 0.3.1
 * @since 17.05.2019
 */
@DecoTemplate(name = "repair_crate", category = DecoGuiCategory.GENERIC_TILE)
public class GuiRepairCrate extends DecoTileGui<TileEntityRepairCrate, ContainerRepairCrate>
{
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public boolean shouldRepairArmor;
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public boolean shouldRepairVehicles;

	public GuiRepairCrate(EntityPlayer player, TileEntityRepairCrate tile)
	{
		super(player, tile, IIGUI.REPAIR_CRATE);
	}

	@Override
	public void onInit()
	{
		boolean upgrade = tile.isUpgradeInstalled(IIContent.UPGRADE_INSERTER);
		startBackground()
				.withBox(DecoTextures.BG_STEEL_ROUGH, 32, 0, 176-64, 76+12)
				.withFrame(DecoTextures.FRAME_STEEL, 6, false)
				.withInventorySlots(SlotStyle.VANILLA, container.slots)
				.withTitleBar(tile)
				.withNextLayer()

				.conditionally(upgrade, builder -> builder
						.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 0, 87+8, 176, 24)
				)

				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 87+8+(upgrade?24: 0), 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();

		if(upgrade)
			addComponents(
					//Energy bar
					new DecoBar(168-32-1, 4)
							.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage)),
					new DecoCheckbox(3, 87+8+1)
							.withText(IIReference.GUI_LABEL_KEY+"repair_crate.repair_armor")
							.withChecked(this.shouldRepairArmor = tile.shouldRepairArmor)
							.withOnToggle(value -> shouldRepairArmor = value),
					new DecoCheckbox(3, 87+8+11)
							.withText(IIReference.GUI_LABEL_KEY+"repair_crate.repair_vehicles")
							.withChecked(this.shouldRepairVehicles = tile.shouldRepairVehicles)
							.withOnToggle(value -> shouldRepairVehicles = value)
			);
	}
}
