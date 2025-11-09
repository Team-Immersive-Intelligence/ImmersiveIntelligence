package pl.pabilo8.immersiveintelligence.client.gui.block.ammunition_production;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage.ImageAnimationDirection;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPrintingPress;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityChemicalPainter;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerPrintingPress;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 10.07.2019
 * @since 29.10.2025
 */

@DecoTemplate(name = "ammunition_assembler", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiAmmunitionAssembler extends DecoGui<TileEntityAmmunitionAssembler, ContainerAmmunitionAssembler>
{

	@DecoResource
	public static final ResourceLocation TEXTURE_AMMOASS = IIReference.RES_II.with("gui/ammunition_assembler");

	public GuiAmmunitionAssembler(EntityPlayer player, TileEntityAmmunitionAssembler tile)
	{
		super(player, tile, IIGUI.AMMUNITION_ASSEMBLER);
	}


	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.GUI_BG_STEEL_ROUGH, 0, 0, 176, 76)
				.withTitleBar(tile)
				.withInventorySlots(SlotStyle.IE_INPUT, container.inputSlot)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.outputSlot)
				.withBox(DecoTextures.GUI_BG_WOODEN, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_ROUND_WOODEN, 0, 76, 176, 92)
				.withInventoryTitleBar()
				.withBox(DecoTextures.GUI_BG_PAPER, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_PAPER, 100, 0, 76, 70)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.build();

		addComponents(
				new DecoBar(150+11, 0)
						.withTemplate(DecoGuiUtils.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage)),

				new DecoButton(100,10)
						.withIcon(ResLoc.of(IIReference.RES_II,"gui/deco/icons/icon_proximity"))
						.withTranslatedTooltip("PROXIMITY"),

				new DecoButton(120,10)
						.withIcon(ResLoc.of(IIReference.RES_II,"gui/deco/icons/icon_contact"))
						.withTranslatedTooltip("CONTACT"),

				new DecoButton(140,10)
						.withIcon(ResLoc.of(IIReference.RES_II,"gui/deco/icons/icon_time"))
						.withTranslatedTooltip("TIMED"),


				new DecoImage(20, 20)
						.withSize(118, 33)
						.withImageLocation(TEXTURE_AMMOASS, false)
						.withUV(256, 127, 177, 122, 209),

				new DecoImage(20, 20)
						.withSize(118, 33)
						.withImageLocation(TEXTURE_AMMOASS, false)
						.withUV(256, 0, 177, 245, 209)
						.withAnimation(ImageAnimationDirection.LEFT_TO_RIGHT, DecoGuiUtils.getMultiblockProductionMultiProgress(tile))
		);
	}
}
