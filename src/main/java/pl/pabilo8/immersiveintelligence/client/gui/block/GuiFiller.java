package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoDustTank;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Filler;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFiller;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerFiller;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 18.08.2025
 * @ii-approved 0.3.1
 * @since 10.07.2019
 */
@DecoTemplate(name = "filler", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiFiller extends DecoGui<TileEntityFiller, ContainerFiller>
{
	@DecoResource
	public static final ResourceLocation TEXTURE = IIReference.RES_II.with("gui/filler");

	public GuiFiller(EntityPlayer player, TileEntityFiller tile)
	{
		super(player, tile, IIGUI.FILLER);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(null, 0, 0, 176, 76)
				.withBox(DecoTextures.GUI_BG_STEEL, 152, 0, 24, 76)
				//.withTitleBar(tile)
				.withBox(DecoTextures.GUI_BG_WOODEN, 0, 76, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventorySlots(SlotStyle.IE_INPUT, container.inputSlot)
				.withInventoryTitleBar()
				.build();

		addComponents(
				new DecoImage(54, 64-4)
						.withSize(64, 15)
						.withImageLocation(TEXTURE, true)
						.withUV(64, 0, 0, 64, 15),

				new DecoDustTank(54, -4)
						.withSize(64, 64)
						.withDustTank(tile.dustStorage, Filler.dustCapacity),
				new DecoImage(120-4, 5+18-4)
						.withSize(22, 22)
						.withImageLocation(TEXTURE, true)
						.withUV(64, 0, 15, 22, 37),

				new DecoBar(161-4, -4)
						.withTemplate(DecoGuiUtils.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage))

		);
	}
}
