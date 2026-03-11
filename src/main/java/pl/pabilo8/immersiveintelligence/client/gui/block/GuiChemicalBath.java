package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Optional.Method;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoFluidTank;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage.ImageAnimationDirection;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityChemicalBath;
import pl.pabilo8.immersiveintelligence.common.compat.jei.JEIHelper;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerChemicalBath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 10.07.2019
 */
@DecoTemplate(name = "chemical_bath", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiChemicalBath extends DecoGui<TileEntityChemicalBath, ContainerChemicalBath>
{
	@DecoResource
	public static ResLoc TEXTURE = IIReference.RES_II.with("gui/chemical_bath");
	private DecoImage imageProgress1, imageProgress2;

	public GuiChemicalBath(EntityPlayer player, TileEntityChemicalBath tile)
	{
		super(player, tile, IIGUI.CHEMICAL_BATH);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.BG_STEEL_ROUGH, DecoTextures.TEMPLATE_SQUARE, 0, 0, 176, 76)
				.withFrame(DecoTextures.FRAME_STEEL, 6, false)
				.withTitleBar(tile)
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 76, 176, 92)
				.withInventorySlots(SlotStyle.IE_INPUT, container.slotInput, container.slotBucketInput)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.slotOutput, container.slotBucketOutput)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();

		addComponents(
				new DecoBar(176-8-8-2, -2)
						.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage)),

				imageProgress1 = new DecoImage(20-4, 5+18-4+24+2+2)
						.withSize(19, 12)
						.withImageLocation(TEXTURE, true)
						.withUV(128, 0, 80, 19, 80+12),
				imageProgress2 = new DecoImage(120-4+18-2-1, 5+18-4+24+2+2)
						.withSize(21, 12)
						.withImageLocation(TEXTURE, true)
						.withUV(128, 19, 80, 19+21, 80+12),
				new DecoImage(20-4, 5+18-4+24+2+2)
						.withSize(19, 12)
						.withImageLocation(TEXTURE, true)
						.withUV(128, 0, 80-12, 19, 80-12+12)
						.withAnimation(ImageAnimationDirection.LEFT_TO_RIGHT, DecoGuiUtils.getMultiblockProductionSingleProgress(tile, 0f, 0.5f)),
				new DecoImage(120-4+18-2-1, 5+18-4+24+2+2)
						.withSize(21, 12)
						.withImageLocation(TEXTURE, true)
						.withUV(128, 19, 80-12, 19+21, 80-12+12)
						.withAnimation(ImageAnimationDirection.LEFT_TO_RIGHT, DecoGuiUtils.getMultiblockProductionSingleProgress(tile, 0.5f, 1f)),

				new DecoFluidTank(32, 39-10)
						.withFluidTank(tile.tank)
						.withTankMask(TEXTURE, 102, 32, 128, new int[]{0, 102, 0, 32}),
				new DecoImage(32-2, 39-10-2)
						.withSize(106, 36)
						.withImageLocation(TEXTURE, true)
						.withUV(128, 0, 32, 106, 32+36)

		);
	}

	@Override
	@Method(modid = "jei")
	public void onInitJEICompat()
	{
		JEIHelper.addRecipesDecoGuiLink(this.imageProgress1, "ii.bathing");
		JEIHelper.addRecipesDecoGuiLink(this.imageProgress2, "ii.bathing");
	}
}
