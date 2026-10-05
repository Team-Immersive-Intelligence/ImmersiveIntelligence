package pl.pabilo8.immersiveintelligence.client.gui.block.emplacement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBarGroup;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoScrollableItemSlots;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerEmplacement;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * Displays Base and Platform storage for the installed Emplacement weapon.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 27.09.2026
 * @since 16.07.2021
 */
@DecoTemplate(name = "emplacement_storage", category = DecoGuiCategory.TERRITORY_CONTROL_TILE)
public class GuiEmplacementStorage extends GuiEmplacement
{
	private static final String KEY = IIReference.GUI_LABEL_KEY+"emplacement.";
	private static final int SLOTS_PER_ROW = 8;
	private static final int SLOT_SIZE = 18;

	public GuiEmplacementStorage(EntityPlayer player, TileEntityEmplacement tile)
	{
		super(player, tile, IIGUI.EMPLACEMENT_STORAGE);
	}

	@Override
	public void onInit()
	{
		super.onInit();
		DecoPanel panelPlatform, panelBase;
		addComponents(
				panelPlatform = new DecoPanel(4, 8+8-4)
						.withSize(152+96-8, 76-8)
						.withBackground(null),
				panelBase = new DecoPanel(4, 76+4+8-4)
						.withSize(152+96-8, 76-8)
						.withBackground(null)
		);

		//Base and Weapon hitpoints
		panelBase.addComponents(
				new DecoBarGroup(panelBase.width-8-24+4, 4+2)
						.withBar(decoBar -> decoBar
								.withTemplate(DecoTemplates.BAR_STRUCTURAL_INTEGRITY_BASE)
								.withLimits(0, Emplacement.baseHealth, () -> (int)tile.baseHealth.getHealth())
								.withHeight(panelBase.height-8)
						)
						.withBar(decoBar -> decoBar
								.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage))
								.withHeight(panelBase.height-8)
						)
		);

		if(tile.currentWeapon!=null)
		{
			//Scrollable slots for inventory
			addScrollableSlots(panelPlatform, container.slotsPlatformAmmo, container.slotsPlatformCasings);
			addScrollableSlots(panelBase, container.slotsBaseAmmo, container.slotsBaseCasings);
			if(container.slotsPlatformAmmo.length > 0)
				panelPlatform.addComponent(new DecoImage(1, container.slotsPlatformAmmo[0].yPos-panelPlatform.y)
						.withSize(16, 16)
						.withImageLocation(DecoTextures.ICON_AMMO, true)
				);
			if(container.slotsPlatformCasings.length > 0)
				panelPlatform.addComponent(new DecoImage(1, container.slotsPlatformCasings[0].yPos-panelPlatform.y)
						.withSize(16, 16)
						.withImageLocation(DecoTextures.ICON_CASING_OUTPUT, true)
				);
			if(container.slotsBaseAmmo.length > 0)
				panelBase.addComponent(new DecoImage(1, container.slotsBaseAmmo[0].yPos-panelBase.y)
						.withSize(16, 16)
						.withImageLocation(DecoTextures.ICON_AMMO_INPUT, true)
				);

			//Energy bar
			panelPlatform.addComponent(
					new DecoBar(panelPlatform.width-8-12+4, 4+2)
							.withTemplate(DecoTemplates.BAR_ARMOR_INTEGRITY)
							.withLimits(0, tile.currentWeapon.getMaxHealth(), () -> (int)tile.currentWeapon.getHealth())
							.withHeight(panelPlatform.height-8)
			);
			tile.currentWeapon.initializeGUI(panelBase, panelPlatform);
		}
		else
		{
			//No weapon labels
			panelPlatform.addLabel(KEY+"no_weapon", 8, 18)
					.withSize(panelPlatform.width-16, 12)
					.withAlign(DecoAlignment.CENTER)
					.withTextColor(DecoColors.H2);
			panelBase.addLabel(KEY+"no_weapon", 8, 18)
					.withSize(panelBase.width-16, 12)
					.withAlign(DecoAlignment.CENTER)
					.withTextColor(DecoColors.H2);
		}

	}

	private void addScrollableSlots(DecoPanel panel, Slot[] ammo, Slot[] casings)
	{
		if(ContainerEmplacement.shouldScrollAmmo(ammo.length, casings.length))
			addScrollableSlots(panel, ammo, ContainerEmplacement.getScrollableRows(casings.length));
		if(ContainerEmplacement.shouldScrollCasings(ammo.length, casings.length))
			addScrollableSlots(panel, casings, ContainerEmplacement.getScrollableRows(ammo.length));
	}

	private void addScrollableSlots(DecoPanel panel, Slot[] slots, int visibleRows)
	{
		Slot first = slots[0];
		panel.addComponent(new DecoScrollableItemSlots(first.xPos-panel.x-1, first.yPos-panel.y-1)
				.withSlots(slots)
				.withColumns(SLOTS_PER_ROW)
				.withHeight(visibleRows*SLOT_SIZE));
	}
}
