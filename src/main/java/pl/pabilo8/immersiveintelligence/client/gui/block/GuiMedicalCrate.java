package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonState;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoFluidTank;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
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
	public static final ResourceLocation TEXTURE_MED = IIReference.RES_II.with("gui/medical_crate");
	public static final ResourceLocation SPEED = IIReference.RES_II.with("deco/icons/icon_speed");
	public static final ResourceLocation HEAL = IIReference.RES_II.with("deco/icons/icon_progress");

	public GuiMedicalCrate(EntityPlayer player, TileEntityMedicalCrate tile)
	{
		super(player, tile, IIGUI.MEDIC_CRATE);
	}


	/**
	@Override
	protected void actionPerformed(GuiButton button)
	{
		if(button==buttonHealing)
		{
			tile.shouldHeal = !tile.shouldHeal;
			buttonHealing.state =   tile.shouldHeal;
			IIPacketHandler.sendToServer(new MessageBooleanAnimatedPartsSync(1, tile.shouldHeal, tile.getPos()));
		}
		else if(button==buttonBoost)
		{
			tile.shouldBoost = !tile.shouldBoost;
			buttonBoost.state = tile.shouldBoost;
			IIPacketHandler.sendToServer(new MessageBooleanAnimatedPartsSync(2, tile.shouldBoost, tile.getPos()));
		}
	}
**/
	boolean upgraded = tile.isUpgradeInstalled(IIContent.UPGRADE_INSERTER);


	@Override
	public void onInit()
	{

		if (upgraded) {

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
							.withImageLocation(TEXTURE_MED, false)
							.withUV(256, 102, 79, 10 ,0),

					new DecoButton(20,20)
							.withIcon(SPEED, 16)
							.withText("Speed Boost"),

					new DecoButton(20, 40)
							.withIcon(HEAL, 16)
							.withText("Healing")

					//new DecoBar(161, -4)
				  			//.withTemplate(DecoGuiUtils.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage))

					//putting notes here becuase storm may kill power while out
					//need to have the buttons do the thing when pressed
					//power bar for the upgrade (Do this for the ammo and repair crate (upgrade thing) if it works
			);

		}

		startBackground()
				.withBox(null, 0, 0, 176, 76)
				.withBox(DecoTextures.GUI_BG_WOODEN, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_ROUND_WOODEN, 0, 76, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventorySlots(SlotStyle.IE_INPUT, container.inputSlot)
				.withInventorySlots(SlotStyle.IE_INPUT, container.inputFluidSlot)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.outputSlot)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.outputSlot2)
				.build();


		addComponents(
				new DecoFluidTank(54, 20)
						.withSize(16, 47)
						.withFluidTank(tile.tanks[0]),

				new DecoFluidTank(108, 20)
						.withSize(16, 47)
						.withFluidTank(tile.tanks[1]),

				new DecoImage(36, -2)
						.withSize(101, 78)
						.withImageLocation(TEXTURE_MED, false)
						.withUV(256, 9, 0, 110 ,78)
		);
	}
}
