package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage.ImageAnimationDirection;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.compat.jei.JEIHelper;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerPrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;


/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @updated 30.80.2025
 * @since 10.07.2019
 */

@DecoTemplate(name = "precision_assembler", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiPrecisionAssembler extends DecoTileGui<TileEntityPrecisionAssembler, ContainerPrecisionAssembler>
{
	@DecoResource
	public static final ResourceLocation TEXTURE_PRE = IIReference.RES_II.with("gui/precision_assembler");
	private MultiblockInteractablePart openedDrawer;
	private DecoImage recipeLink;

	public GuiPrecisionAssembler(EntityPlayer player, TileEntityPrecisionAssembler tile)
	{
		super(player, tile, IIGUI.PRECISION_ASSEMBLER);
	}

	@Override
	public void onInit()
	{
		//Open a selected drawer
		this.syncAnimatedParts(openedDrawer = Utils.RAND.nextBoolean()?tile.drawer1: tile.drawer2, true);

		//Initialize the background
		startBackground()
				.withBox(DecoTextures.BG_STEEL_ROUGH, DecoTextures.TEMPLATE_SQUARE, 0, 0, 176, 78)
				.withFrame(DecoTextures.FRAME_STEEL, 6, false)
				.withTitleBar(tile)
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 78, 176, 92-4)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.withInventorySlots(SlotStyle.IE_INPUT, container.ingredientSlots[0])
				.withInventorySlots(SlotStyle.IE, container.ingredientSlots[1], container.ingredientSlots[2], container.ingredientSlots[3])
				.withInventorySlots(SlotStyle.VANILLA, container.schemeSlot)
				.withInventorySlots(SlotStyle.IE_CUSTOM1, container.toolSlots[0])
				.withInventorySlots(SlotStyle.IE_CUSTOM2, container.toolSlots[1])
				.withInventorySlots(SlotStyle.IE_CUSTOM3, container.toolSlots[2])

				.withInventorySlots(SlotStyle.IE_OUTPUT, container.outputSlots[0])
				.withInventorySlots(SlotStyle.IE, container.outputSlots[1])

				.build();

		//Draw the energy bar and progress bar
		addComponents(
				new DecoBar(161-4, 5-6)
						.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage)),
				new DecoImage(48+16-1, 8+4+2)
						.withSize(48, 25)
						.withImageLocation(TEXTURE_PRE, true)
						.withUV(128, 14, 0, 48+14, 25),
				this.recipeLink = new DecoImage(48+16-1-4-10, 8+4+2+10+10)
						.withSize(85, 17-3)
						.withImageLocation(TEXTURE_PRE, true)
						.withUV(128, 0, 25, 85, 17+25-3),
				new DecoImage(48+16-1-4-10, 8+4+2+10+10)
						.withSize(85, 17-3)
						.withImageLocation(TEXTURE_PRE, true)
						.withUV(128, 0, 42, 85, 17+42-3)
						.withAnimation(ImageAnimationDirection.LEFT_TO_RIGHT, DecoGuiUtils.getMultiblockProductionSingleProgress(this.tile))
		);
	}

	@Override
	public void onInitJEICompat()
	{
		super.onInitJEICompat();
		JEIHelper.addRecipesDecoGuiLink(this.recipeLink, "ii.precision_assembler");
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		if(openedDrawer!=null)
			syncAnimatedParts(openedDrawer, false);
	}

}

