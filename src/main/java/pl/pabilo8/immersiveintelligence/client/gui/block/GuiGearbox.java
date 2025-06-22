package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBarGroup;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityGearbox;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerGearbox;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 10.07.2019
 */
@DecoTemplate(name = "wooden_gearbox")
public class GuiGearbox extends DecoGui<TileEntityGearbox, ContainerGearbox>
{
	public GuiGearbox(EntityPlayer player, TileEntityGearbox tile)
	{
		super(player, tile, IIGUI.GEARBOX);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(IIReference.GUI_BG_WOODEN, 0, 0, 176, 76)
				.withStandaloneFrame(24, 12, 128, 64-8, IIReference.GUI_FRAME_CORNERS_BRASS, 4, true)
				.withTitleBar(tile)
				.withBox(IIReference.GUI_BG_WOODEN, 0, 76, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.inventorySlots)
				.withInventoryTitleBar()
				.build();

		addComponents(
				new DecoBarGroup(-4, 0)
						.withBar(b -> b.withTemplate(DecoGuiUtils.BAR_MECH_TORQUE_INPUT.apply(tile.rotation)))
						.withBar(b -> b.withTemplate(DecoGuiUtils.BAR_MECH_SPEED_INPUT.apply(tile.rotation))),

				new DecoBarGroup(128+28, 0)
						.withBar(b -> b.withTemplate(DecoGuiUtils.BAR_MECH_TORQUE_OUTPUT.apply(tile.rotation)))
						.withBar(b -> b.withTemplate(DecoGuiUtils.BAR_MECH_SPEED_OUTPUT.apply(tile.rotation)))
		);

		addLabel("Gear Ratio: 4:1", 24, 54)
				.withSize(128, 11)
				.withTextColor(IIColor.fromPackedRGB(0xd99747))
				.withAlign(DecoAlignment.CENTER);
	}
}
