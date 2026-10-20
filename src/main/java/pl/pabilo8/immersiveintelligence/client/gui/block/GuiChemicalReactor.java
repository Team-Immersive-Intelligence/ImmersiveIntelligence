package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoFluidTank;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityChemicalReactor;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerChemicalReactor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Avalon (avalon@iiteam.net)
 * @since 17.10.2026
 */
@DecoTemplate(name = "chemicalreactor", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiChemicalReactor extends DecoTileGui<TileEntityChemicalReactor, ContainerChemicalReactor>
{
	@DecoResource
	public static final ResourceLocation TEXTURE = ResLoc.of(IIReference.RES_II, "gui/chemicalreactor");

	public GuiChemicalReactor(EntityPlayer player, TileEntityChemicalReactor tile)
	{
		super(player, tile, IIGUI.CHEMICAL_REACTOR);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.BG_STEEL_ROUGH, 0, 0, 176, 76)
				.withTitleBar(tile)
				.withInventorySlots(SlotStyle.IE_INPUT, container.slotsSet1)
				.withInventorySlots(SlotStyle.IE_INPUT, container.slotsSet2)
				.withInventorySlots(SlotStyle.IE_INPUT, container.slotsSet3)

				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 76, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();

		addComponents(
				//Set 1 Fluid Tanks
				new DecoFluidTank(12, 12)
						.withSize(16, 16)
						.withFluidTank(tile.tankSet1Input1),
				new DecoFluidTank(22, 12)
						.withSize(16, 16)
						.withFluidTank(tile.tankSet1Input2),
				new DecoFluidTank(32, 12)
						.withSize(16, 16)
						.withFluidTank(tile.tankSet1Output),

				//Set 2 Fluid Tanks
				new DecoFluidTank(52, 12)
						.withSize(16, 16)
						.withFluidTank(tile.tankSet2Input1),
				new DecoFluidTank(62, 12)
						.withSize(16, 16)
						.withFluidTank(tile.tankSet2Input2),
				new DecoFluidTank(72, 12)
						.withSize(16, 16)
						.withFluidTank(tile.tankSet2Output),

				//Set 3 Fluid Tanks
				new DecoFluidTank(92, 12)
						.withSize(16, 16)
						.withFluidTank(tile.tankSet3Input1),
				new DecoFluidTank(102, 12)
						.withSize(16, 16)
						.withFluidTank(tile.tankSet3Input2),
				new DecoFluidTank(112, 12)
						.withSize(16, 16)
						.withFluidTank(tile.tankSet3Output),

				//Energy bar
				new DecoBar(168, 0)
						.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage))

		);
	}
}
