package pl.pabilo8.immersiveintelligence.client.gui.block.ammunition_production;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import static pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures.*;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 10.07.2019
 * @since 29.10.2025
 */

@DecoTemplate(name = "ammunition_assembler", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiAmmunitionAssembler extends DecoTileGui<TileEntityAmmunitionAssembler, ContainerAmmunitionAssembler>
{

	@DecoResource
	public static ResourceLocation TEXTURE_AMMOAS = ResLoc.of(IIReference.RES_II.with("gui/ammunition_assembler"));

	private DecoButton btnProximity, btnContact, btnTime;
	private DecoTextField fuseTextField;

	public GuiAmmunitionAssembler(EntityPlayer player, TileEntityAmmunitionAssembler tile)
	{
		super(player, tile, IIGUI.AMMUNITION_ASSEMBLER);
	}


	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.BG_STEEL_ROUGH, 0, 0, 176, 76)
				.withTitleBar(tile)
				.withInventorySlots(SlotStyle.IE_INPUT, container.inputSlot)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.outputSlot)
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 76, 176, 92)
				.withInventoryTitleBar()
				.withBox(DecoTextures.BG_BLUEPRINT, 100, 0, 76, 70)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.build();

		addComponents(
				new DecoBar(150+11, 0)
						.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage)),

				new DecoButton(100, 10)
						.withIcon(ICON_PROXIMITY)
						.withTranslatedTooltip("PROXIMITY"),

				new DecoButton(120, 10)
						.withIcon(ICON_CONTACT)
						.withTranslatedTooltip("CONTACT"),

				new DecoButton(140, 10)
						.withIcon(ICON_TIME)
						.withTranslatedTooltip("TIMED"),

				new DecoTextField(96, 40)
						.withSize(65, 18)
						.withTextColor(IIColor.WHITE)
						.withText("Fuze Time")

/**

 new DecoImage(20, 20)
 .withSize(118, 33)
 .withImageLocation(TEXTURE_AMMOAS, false)
 .withUV(256, 127, 177, 122, 209),

 new DecoImage(20, 20)
 .withSize(118, 33)
 .withImageLocation(TEXTURE_AMMOAS, false)
 .withUV(256, 0, 177, 245, 209)
 .withAnimation(ImageAnimationDirection.LEFT_TO_RIGHT, DecoGuiUtils.getMultiblockProductionMultiProgress(tile))
 **/
		);
	}

}
