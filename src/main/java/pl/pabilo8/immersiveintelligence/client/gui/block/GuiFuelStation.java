package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoFluidTank;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFuelStation;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerFuelStation;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 10.07.2019
 */
@DecoTemplate(name = "fuel_station", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiFuelStation extends DecoGui<TileEntityFuelStation, ContainerFuelStation>
{
	@DecoResource
	public static final ResourceLocation TEXTURE = IIReference.RES_II.with("gui/fuel_station");

	public GuiFuelStation(EntityPlayer player, TileEntityFuelStation tile)
	{
		super(player, tile, IIGUI.FUEL_STATION);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(null, 0, 0, 176, 76)
				.withBox(DecoTextures.GUI_BG_STEEL, 152, 0, 24, 76)
				//.withTitleBar(tile)
				.withBox(DecoTextures.GUI_BG_WOODEN, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_ROUND_WOODEN, 0, 76, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventorySlots(SlotStyle.IE_INPUT, container.inputFluidSlot)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.outputFluidSlot)
				.withInventoryTitleBar()
				.build();

		addComponents(
				new DecoImage(57, 64)
						.withSize(32, 9)
						.withImageLocation(TEXTURE, true)
						.withUV(32, 0, 0, 32, 10),
				new DecoImage(57+32, 64)
						.withSize(32, 9)
						.withImageLocation(TEXTURE, true)
						.withUV(32, 0, 10, 32, 20),
				new DecoFluidTank(57, 0)
						.withSize(64, 64)
						.withFluidTank(tile.tank),
				new DecoBar(161-4, -4)
						.withTemplate(DecoGuiUtils.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage))
		);
	}
}
