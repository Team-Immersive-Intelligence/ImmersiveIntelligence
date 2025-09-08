package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerPrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @updated 30.80.2025
 * @since 10.07.2019
 */

@DecoTemplate(name = "precision_assembler", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiPrecisionAssembler extends DecoGui<TileEntityPrecisionAssembler, ContainerPrecisionAssembler>
{
	@DecoResource
	public static final ResourceLocation TEXTURE = IIReference.RES_II.with("gui/precision_assembler");

	public GuiPrecisionAssembler(EntityPlayer player, TileEntityPrecisionAssembler tile)
	{
		super(player, tile, IIGUI.PRECISION_ASSEMBLER);

	}

	@Override
	public void onInit()
	{

			startBackground()
					.withBox(null, 0, 0, 176, 76)
					.withBox(DecoTextures.GUI_BG_STEEL, 152, 0, 24, 76)
					.withTitleBar(tile)
					.withBox(DecoTextures.GUI_BG_WOODEN, 0, 76, 176, 92)
					.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
					.withInventorySlots(SlotStyle.IE_INPUT, container.ingredientSlot)
					.withInventorySlots(SlotStyle.IE_INPUT, container.schemeSlot)
					.withInventorySlots(SlotStyle.IE_INPUT, container.toolSlot)
					.withInventorySlots(SlotStyle.IE_OUTPUT, container.outputSlot)
					.withInventoryTitleBar()
					.build();aaaaaaaa

			addComponents(
					new DecoImage(57, 64)
							.withSize(32, 9)
							.withImageLocation(TEXTURE, true)
							.withUV(32, 0, 0, 0, 0),
					new DecoImage(57+32, 64)
							.withSize(32, 9)
							.withImageLocation(TEXTURE, true)
							.withUV(32, 0, 0, 0, 0),
					new DecoBar(161-4, -4)
							.withTemplate(DecoGuiUtils.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage))
			);
		}

	}

