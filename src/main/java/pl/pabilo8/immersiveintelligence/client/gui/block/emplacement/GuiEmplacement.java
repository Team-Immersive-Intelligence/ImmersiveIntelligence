package pl.pabilo8.immersiveintelligence.client.gui.block.emplacement;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerEmplacement;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 18.08.2026
 * @since 16.07.2021
 */
public abstract class GuiEmplacement extends DecoTileGui<TileEntityEmplacement, ContainerEmplacement>
{
	public GuiEmplacement(EntityPlayer player, TileEntityEmplacement tile, IIGUI gui)
	{
		super(player, tile, gui);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.BG_STEEL_ROUGH, 0, 0, 152+96, 152+8)
				.withTitleBar(tile)
				.withNextLayer()
				//Bad practicle, I know, but it was a must
				.conditionally(this instanceof GuiEmplacementPageStorage, builder -> builder
						.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 4, 8+8, 152+96-8, 76-8-8)
						.withTitleBar(IIReference.GUI_LABEL_KEY+"emplacement.platform_inventory", DecoAlignment.TOP_LEFT)
						.withInventorySlots(SlotStyle.VANILLA, container.slotsPlatformAmmo)
						.withInventorySlots(SlotStyle.VANILLA, container.slotsPlatformCasings)
						.withNextLayer()
						.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 4, 76+4+8, 152+96-8, 76-8-8)
						.withTitleBar(IIReference.GUI_LABEL_KEY+"emplacement.base_inventory", DecoAlignment.TOP_LEFT)
						.withInventorySlots(SlotStyle.VANILLA, container.slotsBaseAmmo)
						.withInventorySlots(SlotStyle.VANILLA, container.slotsBaseCasings))
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 32, 152+8, 176, 92)
				.withFrame(DecoTextures.FRAME_WOODEN_THIN, 4, false, new boolean[]{true, false, false, false})
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();

		//Refresh GUI on weapon change
		addValueListener(() -> tile.currentWeapon)
				.withObserver(emplacementWeapon -> this.refreshGUI());

		//Tabs
		addLinkTab(IIGUI.EMPLACEMENT_STORAGE, DecoTextures.ICON_STORAGE, "storage_module");
		addLinkTab(IIGUI.EMPLACEMENT_CONFIG, DecoTextures.ICON_CONFIG, "configuration_module");
		addLinkTab(IIGUI.EMPLACEMENT_TARGET_FILTERS, DecoTextures.ICON_TARGETS, "targets_module");
		addLinkTab(IIGUI.EMPLACEMENT_FIRE_MISSIONS, DecoTextures.ICON_FIRE_MISSIONS, "fire_missions_module");

		if(tile.currentWeapon!=null)
			tile.currentWeapon.init(tile);
	}
}
