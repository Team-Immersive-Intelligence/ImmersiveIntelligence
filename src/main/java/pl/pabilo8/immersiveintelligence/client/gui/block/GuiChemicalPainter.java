package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional.Method;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoColorPickerPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoFluidTank;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage.ImageAnimationDirection;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityChemicalPainter;
import pl.pabilo8.immersiveintelligence.common.compat.jei.JEIHelper;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerChemicalPainter;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 10.07.2019
 */
@DecoTemplate(name = "chemical_painter", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiChemicalPainter extends DecoTileGui<TileEntityChemicalPainter, ContainerChemicalPainter>
{
	@DecoResource
	public static final ResourceLocation TEXTURE = IIReference.RES_II.with("gui/chemical_painter");
	private DecoImage imageProgress;

	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public IIColor color;

	public GuiChemicalPainter(EntityPlayer player, TileEntityChemicalPainter tile)
	{
		super(player, tile, IIGUI.CHEMICAL_PAINTER);
	}

	@Override
	public void onInit()
	{
		this.color = tile.color;
		startBackground()
				//Machine GUI
				.withBox(DecoTextures.BG_STEEL, 0, 0, 176, 128)
				.withTitleBar(tile)
				.withInventorySlots(SlotStyle.IE_INPUT, container.inputSlot, container.inputFluidSlot)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.outputSlot, container.outputFluidSlot)
				//Player inventory
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 128, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				//Color panel
				.withNextLayer()
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 0, 80, 176, 48)
				.build();

		addComponents(
				//Energy Bar
				new DecoBar(168, 0)
						.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage)),
				//Ink Tanks
				new DecoFluidTank(25, 12)
						.withFluidTank(tile.tankCyan)
						.withColorMarker(IIColor.fromPackedRGB(0x4d7373)),
				new DecoFluidTank(51, 12)
						.withFluidTank(tile.tankMagenta)
						.withColorMarker(IIColor.fromPackedRGB(0x734d73)),
				new DecoFluidTank(77, 12)
						.withFluidTank(tile.tankYellow)
						.withColorMarker(IIColor.fromPackedRGB(0x73734d)),
				new DecoFluidTank(103, 12)
						.withFluidTank(tile.tankBlack)
						.withColorMarker(IIColor.fromPackedRGB(0x1a1a1a)),
				//Production Progress
				new DecoImage(131, 20)
						.withSize(12, 51)
						.withImageLocation(TEXTURE, true)
						.withUV(64, 0, 0, 12, 51),
				new DecoImage(131+12, 20+13)
						.withSize(12, 17)
						.withImageLocation(TEXTURE, true)
						.withUV(64, 12, 14, 12+12, 29),

				this.imageProgress = new DecoImage(131, 20)
						.withSize(24, 51)
						.withImageLocation(TEXTURE, true)
						.withUV(64, 24, 0, 48, 51)
						.withAnimation(ImageAnimationDirection.TOP_TO_BOTTOM, DecoGuiUtils.getMultiblockProductionSingleProgress(tile)),
				//Color Picker
				new DecoColorPickerPanel(0, 80)
						.withOnColorChanged((oldColor, newColor) -> {
							this.color = newColor;
							IIPacketHandler.sendToServer(new MessageIITileSync(tile, onSaveTileData()));
						})
						.withColor(color)
						.withSize(176, 48)
		);
	}

	@Override
	@Method(modid = "jei")
	public void onInitJEICompat()
	{
		JEIHelper.addRecipesDecoGuiLink(this.imageProgress, "ii.painting");
	}
}
