package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoCheckbox;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoFluidTank;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityMedicalCrate;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerMedicalCrate;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

/**
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @implNote Class so breaking-proof, that it survived a magnetic storm during one of the edits.
 * @updated 13.08.2026
 * @ii-approved 0.3.1
 * @since 17.05.2019
 */

@DecoTemplate(name = "medical_crate", category = DecoGuiCategory.GENERIC_TILE)
public class GuiMedicalCrate extends DecoTileGui<TileEntityMedicalCrate, ContainerMedicalCrate>
{
	@DecoResource
	public static final ResourceLocation TEXTURE = IIReference.RES_II.with("gui/medical_crate");
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public boolean shouldHeal;
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public boolean shouldBoost;

	public GuiMedicalCrate(EntityPlayer player, TileEntityMedicalCrate tile)
	{
		super(player, tile, IIGUI.MEDIC_CRATE);
	}

	@Override
	public void onInit()
	{
		boolean upgrade = tile.isUpgradeInstalled(IIContent.UPGRADE_INSERTER);
		final IIColor backgroundColor = IIColor.fromHex("a86465");
		startBackground()
				.withBox(DecoTextures.BG_STEEL_ROUGH, DecoTextures.TEMPLATE_ROUND, 32, 0, 176-64, 76+12, backgroundColor)
				.withFrame(DecoTextures.FRAME_STEEL, 6, false)

				.withInventorySlots(SlotStyle.IE_INPUT, container.inputSlot)
				.withInventorySlots(SlotStyle.IE_INPUT, container.inputFluidSlot)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.outputSlot)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.outputSlot2)

				.withTitleBar(tile)
				.withNextLayer()

				.conditionally(upgrade, builder -> builder
						.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 0, 87+8, 176, 24, backgroundColor)
				)

				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 87+8+(upgrade?24: 0), 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();

		addComponents(
				//Display tanks
				new DecoFluidTank(46-2, 19-2+4)
						.withSize(16+4, 47+4+1)
						.withFluidTank(tile.tanks[0]),

				new DecoFluidTank(90-2, 19-2+4)
						.withSize(16+4, 47+4+1)
						.withFluidTank(tile.tanks[1]),

				//Syringe tops
				new DecoImage(46-2, 19-2-12+4)
						.withSize(20, 12)
						.withImageLocation(TEXTURE, true)
						.withUV(32, 0, 0, 20, 12),
				new DecoImage(90-2, 19-2-12+4)
						.withSize(20, 12)
						.withImageLocation(TEXTURE, true)
						.withUV(32, 0, 0, 20, 12),

				//Syringe bottoms
				new DecoImage(46-2, 19-2-12+4+48+4+12)
						.withSize(20, 8)
						.withImageLocation(TEXTURE, true)
						.withUV(32, 0, 24, 20, 24+8),
				new DecoImage(90-2, 19-2-12+4+48+4+12)
						.withSize(20, 8)
						.withImageLocation(TEXTURE, true)
						.withUV(32, 0, 24, 20, 24+8)
		);

		if(upgrade)
			addComponents(
					//Energy bar
					new DecoBar(168-32-1, 4)
							.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage)),
					new DecoCheckbox(3, 87+8+1)
							.withText(IIReference.GUI_LABEL_KEY+"medical_crate.heal")
							.withTextColor(IIReference.COLOR_GUI_BRASS, IIColor.WHITE)
							.withChecked(this.shouldHeal = tile.shouldHeal)
							.withOnToggle(value -> shouldHeal = value),
					new DecoCheckbox(3, 87+8+11)
							.withText(IIReference.GUI_LABEL_KEY+"medical_crate.boost")
							.withTextColor(IIReference.COLOR_GUI_BRASS, IIColor.WHITE)
							.withChecked(this.shouldBoost = tile.shouldBoost)
							.withOnToggle(value -> shouldBoost = value)
			);
	}
}
