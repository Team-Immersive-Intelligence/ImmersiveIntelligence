package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent.MouseButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoArrows;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage.ImageAnimationDirection;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRedstoneDataInterface;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRedstoneDataInterface.ConversionMode;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRedstoneDataInterface.ConversionSetting;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerRedstoneDataInterface;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 03.04.2026
 * @since 09.02.2020
 */
@DecoTemplate(name = "data_redstone_interface", category = DecoGuiCategory.DATA_TILE)
public class GuiDataRedstoneInterface extends DecoTileGui<TileEntityRedstoneDataInterface, ContainerRedstoneDataInterface>
{
	@DecoResource
	public static ResourceLocation PROGRESS_IMAGE = ResLoc.of(IIReference.RES_II, "gui/data_input_machine");

	@SyncNBT
	public int scroll = 0;
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public EasyCollection<ConversionSetting, NBTTagCompound> dataSettings;
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public EasyCollection<ConversionSetting, NBTTagCompound> redstoneSettings;
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public boolean deactivateUnusedSignals;
	private final boolean redstoneToData;

	private GuiDataRedstoneInterface(EntityPlayer player, TileEntityRedstoneDataInterface tile, boolean redstoneToData)
	{
		super(player, tile, redstoneToData?IIGUI.DATA_REDSTONE_INTERFACE_REDSTONE: IIGUI.DATA_REDSTONE_INTERFACE_DATA);
		this.redstoneToData = redstoneToData;
	}

	public static GuiDataRedstoneInterface getRedstoneGUI(EntityPlayer player, TileEntityRedstoneDataInterface tile)
	{
		return new GuiDataRedstoneInterface(player, tile, true);
	}

	public static GuiDataRedstoneInterface getDataGUI(EntityPlayer player, TileEntityRedstoneDataInterface tile)
	{
		return new GuiDataRedstoneInterface(player, tile, false);
	}

	@Override
	public void onInit()
	{
		//Sync data
		this.dataSettings = tile.dataSettings;
		this.redstoneSettings = tile.redstoneSettings;
		this.deactivateUnusedSignals = tile.deactivateUnusedSignals;

		//Create background
		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 176+32, 128+8)
				.withTitleBar(IIReference.DESCRIPTION_KEY+(redstoneToData?"redstone_to_data_module": "data_to_redstone_module"))
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 16, 128+8, 176, 92)
				.withFrame(DecoTextures.FRAME_WOODEN_THIN, 4, false, new boolean[]{true, false, false, false})
				.withInventoryTitleBar()

				.withNextLayer()
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 0, 8, 32, 120)

				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventorySlots(SlotStyle.IE_INPUT, container.punchtapeInput)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.punchtapeOutput)
				.build();

		addLinkTab(IIGUI.DATA_REDSTONE_INTERFACE_REDSTONE, new ItemStack(IEContent.itemWireCoil, 1, 5), "redstone_to_data_module");
		addLinkTab(IIGUI.DATA_REDSTONE_INTERFACE_DATA, IIContent.itemDataWireCoil.getStack(1), "data_to_redstone_module");

		int colorY = redstoneToData?4: 17;
		int variableY = redstoneToData?17: 4;

		//Add components
		addComponents(
				new DecoImage(4+2, 12+24+8-2-1)
						.withSize(20, 52)
						.withImageLocation(PROGRESS_IMAGE, true)
						.withUV(64, 0, 0, 20, 52),
				new DecoImage(4+2, 12+24+8-2-1)
						.withSize(20, 52)
						.withImageLocation(PROGRESS_IMAGE, true)
						.withUV(64, 20, 0, 40, 52)
						.withAnimation(ImageAnimationDirection.TOP_TO_BOTTOM, partialTicks ->
								tile.punchtapeReadProgress/100f+(partialTicks*Math.signum(tile.punchtapeReadProgress))/100f
						),

				new DecoList<ConversionSetting>(32, 8)
						.withSize(136+24+8+2, 120)
						.withEntries(redstoneToData?dataSettings: redstoneSettings)
						.withCreateAction(ConversionSetting::new)
						.withScroll(scroll)
						.withGuiSaveAction(gui -> this.scroll = gui.getScroll())
						.withDisplayFunction(new DecoEntryPanelBuilder<ConversionSetting>()
								.withHeight(32+16-6+2)
								.withLabel("from", new DecoLabel(this.fontRenderer, 4, 4)
										.withText(IIReference.GUI_LABEL_KEY+"redstone_data_interface.from."+(redstoneToData?"redstone": "data"))
										.withTranslatedTooltip(IIReference.GUI_LABEL_KEY+"redstone_data_interface.from."+(redstoneToData?"redstone": "data")+".tooltip")
								)
								.withLabel("to", new DecoLabel(this.fontRenderer, 4, 16+2)
										.withText(IIReference.GUI_LABEL_KEY+"redstone_data_interface.to."+(redstoneToData?"data": "redstone"))
										.withTranslatedTooltip(IIReference.GUI_LABEL_KEY+"redstone_data_interface.to."+(redstoneToData?"data": "redstone")+".tooltip")
								)
								.withLabel("mode", new DecoLabel(this.fontRenderer, 4, 16+14+2-1)
										.withText(IIReference.GUI_LABEL_KEY+"redstone_data_interface.mode")
										.withTranslatedTooltip(IIReference.GUI_LABEL_KEY+"redstone_data_interface.mode.tooltip")
								)

								//Color
								.withComponent("color_icon", new DecoImage(32+8+2+48-4+2+24+8, colorY)
										.withSize(8, 8)
										.withImageLocation(DecoTextures.COMPONENT_COLOR, true)
										.withUV(16, 4, 4, 12, 12)
								)
								.withLabel("color_label",
										new DecoLabel(IIClientUtils.fontRegular, 4, colorY-1)
												.withSize(136-48-8+2+24+8, 12)
												.withAlign(DecoAlignment.RIGHT)
												.withText("Dye")
								)
								.withComponent("color_arrows", builder -> new DecoArrows(32+48+16+2+24+8, colorY-2)
										.withSize(12, 12)
										.withOnArrow(arrow -> {
											builder.getCurrentElement()
													.setColor(IIUtils.cycleEnum(arrow, EnumDyeColor.class, builder.getCurrentElement().getColor()));
											if(!redstoneToData) deactivateUnusedSignals = true;
										})
								)
								//Variable
								.withLabel("variable_label",
										new DecoLabel(IIClientUtils.fontRegular, 4, variableY)
												.withSize(136-48+4+1+24+8, 12)
												.withAlign(DecoAlignment.RIGHT)
												.withText("a")
								)
								.withComponent("variable_arrows", builder -> new DecoArrows(32+32+16+24-8+2+24+8, variableY-2)
										.withSize(12, 12)
										.withOnArrow(arrow -> builder.getCurrentElement()
												.setVariable(IIUtils.cycleDataPacketChars(builder.getCurrentElement().getVariable(), arrow, false)))
								)
								//Mode
								.withLabel("mode_label",
										new DecoLabel(IIClientUtils.fontRegular, 4, 16+14+1-1)
												.withSize(136-48+4+1+24+8, 12)
												.withAlign(DecoAlignment.RIGHT)
												.withText("Mode")
								)
								.withComponent("mode_arrows", builder -> new DecoArrows(32+32+16+24-8+2+24+8, 16+12+2-1)
										.withSize(12, 12)
										.withOnArrow(arrow -> builder.getCurrentElement()
												.setMode(IIUtils.cycleEnum(arrow, ConversionMode.class, builder.getCurrentElement().getMode())))
								)
								//Edit / Remove Buttons
								.withComponent(p -> new DecoButton(p.width-17, 8+6)
										.withTemplate(DecoTemplates.ACTION_BUTTON_REMOVE)
										.withOnPressed((gui, mouseButton, mouseX, mouseY) -> {
											if(mouseButton==MouseButton.LEFT)
											{
												p.getCurrentList().removeEntry(p.getCurrentElement());
												if(!redstoneToData) deactivateUnusedSignals = true;
												return true;
											}
											return false;
										})
								)
								.withElementApplyMethod((entry, panel) -> {
									//Set dye
									EnumDyeColor dye = entry.getColor();
									panel.component("color_icon", DecoImage.class).withColor(IIColor.fromDye(dye));
									panel.label("color_label").withText("item.fireworksCharge."+dye.getUnlocalizedName());
									//Set mode
									panel.label("mode_label").withText(entry.getMode().getFullLocaleKey());
									//Set variable name
									panel.label("variable_label").withRawText(String.valueOf(entry.getVariable()));
								})
						)
		);
	}
}
