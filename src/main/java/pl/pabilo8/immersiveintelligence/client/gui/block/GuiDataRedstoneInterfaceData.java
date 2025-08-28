package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.DataVariable;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.TypeMetaInfo;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent.MouseButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTab;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRedstoneInterface;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerRedstoneDataInterface;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 02.02.2024
 * @since 09.02.2020
 */
@DecoTemplate(name = "data_redstone_interface", category = DecoGuiCategory.DATA_TILE)
public class GuiDataRedstoneInterfaceData extends DecoGui<TileEntityRedstoneInterface, ContainerRedstoneDataInterface>
{
	@SyncNBT
	public int scroll = 0;
	private DecoList<DataVariable> list;

	public GuiDataRedstoneInterfaceData(EntityPlayer player, TileEntityRedstoneInterface tile)
	{
		super(player, tile, IIGUI.DATA_REDSTONE_INTERFACE_DATA);
	}

	@Override
	public void onInit()
	{
		//Create background
		startBackground()
				.withBox(DecoTextures.GUI_BG_STEEL, 0, 0, 176, 128+8)
				.withTitleBar("desc.immersiveintelligence.data_to_redstone_module")
				.withBox(DecoTextures.GUI_BG_WOODEN, 0, 128+8, 176, 92)
				.withInventoryTitleBar()

				.withNextLayer()
				.withBox(DecoTextures.GUI_BG_STEEL, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_SQUARE, 0, 8, 32, 120)

				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventorySlots(SlotStyle.IE_INPUT, container.dataInput)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.dataOutput)
				.build();

		DataPacket packet = new DataPacket();
		packet.set('a', new DataTypeString("test"));
		packet.set('b', new DataTypeString("test2"));
		packet.set('e', new DataTypeString("test3"));
		packet.set('f', new DataTypeString("test4"));
		packet.set('g', new DataTypeString("test5"));
		packet.set('h', new DataTypeString("test6"));
		packet.set('i', new DataTypeString("test7"));
		packet.set('j', new DataTypeString("test8"));

		//Add components
		addComponents(
				new DecoTab()
						.withLink(IIGUI.DATA_REDSTONE_INTERFACE_DATA)
						.withIcon(IIContent.itemDataWireCoil.getStack(1))
						.withTranslatedTooltip("desc.immersiveintelligence.data_to_redstone_module"),
				new DecoTab()
						.withLink(IIGUI.DATA_REDSTONE_INTERFACE_REDSTONE)
						.withIcon(new ItemStack(IEContent.itemWireCoil, 1, 5))
						.withTranslatedTooltip("desc.immersiveintelligence.redstone_to_data_module"),

				list = new DecoList<DataVariable>(32, 8)
						.withSize(136, 120)
						.withEntries(packet.getAllVariables())
						.withCreateLaterAction(() -> changeGUI(IIGUI.DATA_REDSTONE_INTERFACE_REDSTONE))
						.withGuiSaveAction(gui -> this.scroll = gui.getScroll())
						.withDisplayFunction(new DecoEntryPanelBuilder<DataVariable>()
								.withPadding(1, 1)
								//Edit / Remove Buttons
								.withComponent(
										p -> new DecoButton(p.width-17-16+3, 2)
												.withTemplate(DecoGuiUtils.LIST_BUTTON_EDIT_TEMPLATE)
												.withOnPressed((gui, mouseButton, mouseX, mouseY) -> {
													if(mouseButton==MouseButton.LEFT)
													{
														changeGUI(IIGUI.DATA_REDSTONE_INTERFACE_REDSTONE);
														return true;
													}
													return false;
												})
								)
								.withComponent(p -> new DecoButton(p.width-17+1, 2)
										.withTemplate(DecoGuiUtils.LIST_BUTTON_REMOVE_TEMPLATE)
										.withOnPressed((gui, mouseButton, mouseX, mouseY) -> {
											if(mouseButton==MouseButton.LEFT)
											{
												p.getCurrentList().removeEntry(p.getCurrentElement());
												return true;
											}
											return false;
										})
								)
								//Type Icon, Label, and Letter
								.withComponent("image", new DecoImage(2+12, 1)
										.withSize(16, 16))
								.withLabel("typeLabel",
										new DecoLabel(fontRenderer, 2+12+16+2, 2)
												.withSize(48, 16)
												.withAlign(DecoAlignment.LEFT)
												.withText("Integer")
								)
								.withLabel("letterLabel",
										new DecoLabel(fontRenderer, 2, 2)
												.withSize(12, 16)
												.withAlign(DecoAlignment.CENTER)
								)
								.withElementApplyMethod((entry, panel) -> {
									TypeMetaInfo<?> typeMeta = entry.getValue().getTypeMeta();

									//letter label (f.e. a)
									panel.label("letterLabel")
											.withRawText(String.valueOf(entry.getName()));
									//type label (f.e. integer)
									panel.label("typeLabel")
											.withText(typeMeta.getTranslatedName())
											.withTextColor(typeMeta.color.withBrightness(0.5f));
									//type icon
									panel.component("image", DecoImage.class)
											.withImageLocation(entry.getValue().getTextureLocation());
								})
						)
						.withScroll(scroll)
		);
	}

	protected EasyNBT onSaveTileData()
	{
		//TODO: 23.02.2025 entry saving
		return super.onSaveTileData();
	}
}
