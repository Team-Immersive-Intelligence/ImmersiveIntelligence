package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoFluidTank;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoResource;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.EffectCrates;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityMedicalCrate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFiller;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerFiller;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerMedicalCrate;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.ArrayList;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 17.05.2019
 */

@DecoTemplate(name = "medicalcrate", category = DecoGuiCategory.GENERIC_TILE)
public class GuiMedicalCrate extends DecoGui<TileEntityMedicalCrate, ContainerMedicalCrate>
{

	@DecoResource
	public static final ResourceLocation TEXTURE = IIReference.RES_II.with("gui/medical_crate");

	public GuiMedicalCrate(EntityPlayer player, TileEntityMedicalCrate tile)
	{
		super(player, tile, IIGUI.MEDIC_CRATE);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(null, 0, 0, 176, 76)
				.withBox(DecoTextures.GUI_BG_WOODEN, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_ROUND_WOODEN, 0, 76, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventorySlots(SlotStyle.IE_INPUT, container.inputSlot)
				.withInventorySlots(SlotStyle.IE_INPUT, container.inputFluidSlot)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.outputSlot)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.outputSlot2)
				.withInventoryTitleBar()
				.build();


		addComponents(
				new DecoFluidTank(10, 21)
						.withSize(16, 47)
						.withFluidTank(tile.tanks[0]),

				new DecoFluidTank(54, 21)
						.withSize(16, 47)
						.withFluidTank(tile.tanks[1]),

				new DecoImage(15, 0)
						.withSize(102, 79)
						.withImageLocation(TEXTURE, true)
						.withUV(256, 102, 79, 10 ,0)
		);
	}
}
