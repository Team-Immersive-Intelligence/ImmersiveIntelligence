package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent.MouseButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRedstoneDataInterface;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRedstoneDataInterface.ConversionSetting;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerRedstoneDataInterface;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 09.03.2026
 * @ii-approved 0.3.1
 * @since 09.02.2020
 */
@DecoTemplate(name = "data_redstone_interface_redstone", category = DecoGuiCategory.DATA_TILE)
public class GuiDataRedstoneInterfaceRedstone extends DecoGui<TileEntityRedstoneDataInterface, ContainerRedstoneDataInterface>
{
	@DecoResource
	public static ResourceLocation PROGRESS_IMAGE = ResLoc.of(IIReference.RES_II, "gui/data_input_machine");

	@SyncNBT
	public int scroll = 0;
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public EasyCollection<ConversionSetting, NBTTagCompound> redstoneSettings;
	private DecoList<ConversionSetting> list;

	public GuiDataRedstoneInterfaceRedstone(EntityPlayer player, TileEntityRedstoneDataInterface tile)
	{
		super(player, tile, IIGUI.DATA_REDSTONE_INTERFACE_REDSTONE);
	}

	@Override
	public void onInit()
	{
		this.redstoneSettings = tile.redstoneSettings;
		//Create background
		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 176, 128+8)
				.withTitleBar("desc.immersiveintelligence.redstone_to_data_module")
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 128+8, 176, 92)
				.withInventoryTitleBar()

				.withNextLayer()
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 0, 8, 32, 120)

				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventorySlots(SlotStyle.IE_INPUT, container.punchtapeInput)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.punchtapeOutput)
				.build();

		addLinkTab(IIGUI.DATA_REDSTONE_INTERFACE_REDSTONE, new ItemStack(IEContent.itemWireCoil, 1, 5), "redstone_to_data_module");
		addLinkTab(IIGUI.DATA_REDSTONE_INTERFACE_DATA, IIContent.itemDataWireCoil.getStack(1), "data_to_redstone_module");

		//Add components
		addComponents(
				new DecoImage(4+2, 12+24+8-2-1)
						.withSize(20, 52)
						.withImageLocation(PROGRESS_IMAGE, true)
						.withUV(64, 0, 0, 20, 52),
				new DecoImage(4+2, 12+24+8-2-1)
						.withSize(20, 52)
						.withImageLocation(PROGRESS_IMAGE, true)
						.withUV(64, 20, 0, 40, 52),

				list = new DecoList<ConversionSetting>(32, 8)
						.withSize(136, 120)
						.withEntries(redstoneSettings)
						.withCreateAction(ConversionSetting::new)
						.withGuiSaveAction(gui -> this.scroll = gui.getScroll())
						.withScroll(scroll)
						.withDisplayFunction(new DecoEntryPanelBuilder<ConversionSetting>()
								.withPadding(1, 1)
								//Edit / Remove Buttons
								.withComponent(p -> new DecoButton(p.width-17+1, 2)
										.withTemplate(DecoTemplates.ACTION_BUTTON_REMOVE)
										.withOnPressed((gui, mouseButton, mouseX, mouseY) -> {
											if(mouseButton==MouseButton.LEFT)
											{
												p.getCurrentList().removeEntry(p.getCurrentElement());
												return true;
											}
											return false;
										})
								)
								.withElementApplyMethod((entry, panel) -> {

								})
						)
		);
	}
}
