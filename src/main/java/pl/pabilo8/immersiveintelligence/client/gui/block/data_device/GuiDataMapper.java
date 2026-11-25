package pl.pabilo8.immersiveintelligence.client.gui.block.data_device;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.data_device.DataDeviceUtils;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataMapper;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataMapper.MappingRule;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerSmallDataDevice;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

/**
 * Lists typed variable mappings and opens their rule editors.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 25.10.2026
 * @since 24.10.2026
 */
@DecoTemplate(name = "data_mapper", category = DecoGuiCategory.DATA_TILE)
public class GuiDataMapper extends DecoTileGui<TileEntityDataMapper, ContainerSmallDataDevice<TileEntityDataMapper>>
{
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public EasyCollection<MappingRule, NBTTagCompound> mappings = new EasyCollection<>(MappingRule::new);
	@SyncNBT
	public int editedRule = -1;

	public GuiDataMapper(EntityPlayer player, TileEntityDataMapper tile)
	{
		super(player, tile, IIGUI.DATA_MAPPER);
		if(tile!=null)
			mappings = tile.mappings.clone();
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_ROUND, 0, 0, 176, 8)
				.withTitleBar(tile)
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 0, 8, 176, 160)
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 168, 176, 92)
				.withFrame(DecoTextures.FRAME_WOODEN_THIN, 4, false, new boolean[]{true, false, false, false})
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();
		addComponent(new DecoList<MappingRule>(4, 12-2-2).withSize(160+4+4, 144+8-4+4+4).withEntries(mappings)
				.withCreateLaterAction(() -> {
					if(mappings.size() < DataDeviceUtils.MAX_RULES)
					{
						editedRule = mappings.size();
						changeGUI(IIGUI.DATA_MAPPER_EDIT);
					}
				})
				.withDisplayFunction(new DecoEntryPanelBuilder<MappingRule>().withHeight(20)
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_TICKET)
						.withComponent(p -> new DecoImage(4, 2).withSize(16, 16)
								.withImageLocation(p.getCurrentElement().inputType.getTextureLocation(), true)
								.withTranslatedTooltip(p.getCurrentElement().inputType.getName())
						)
						.withLabel("route", p -> new DecoLabel(fontRenderer, 24, 6).withSize(40, 10)
								.withAlign(DecoAlignment.LEFT)
								.withRawText(p.getCurrentElement().input+" ->"))
						.withComponent(p -> new DecoImage(48, 2).withSize(16, 16)
								.withImageLocation(p.getCurrentElement().outputType.getTextureLocation(), true)
								.withTranslatedTooltip(p.getCurrentElement().outputType.getName())
						)
						.withLabel("result", p -> new DecoLabel(fontRenderer, 48+20, 6).withSize(40, 10)
								.withAlign(DecoAlignment.LEFT)
								.withRawText(String.valueOf(p.getCurrentElement().output)))
						.withComponent(p -> new DecoButton(p.width-17-14-14, 3).withTemplate(DecoTemplates.ACTION_BUTTON_EDIT)
								.withOnLMBPressed(() -> {
									editedRule = mappings.indexOf(p.getCurrentElement());
									changeGUI(IIGUI.DATA_MAPPER_EDIT);
								}))
						.withComponent(p -> new DecoButton(p.width-17-14, 3).withTemplate(DecoTemplates.ACTION_BUTTON_DUPLICATE)
								.withOnLMBPressed(() -> {
									if(mappings.size() < DataDeviceUtils.MAX_RULES)
										mappings.add(mappings.copyEntry(p.getCurrentElement()));
									refreshGUI();
								}))
						.withComponent(p -> new DecoButton(p.width-17, 3).withTemplate(DecoTemplates.ACTION_BUTTON_REMOVE)
								.withOnLMBPressed(() -> {
									mappings.remove(p.getCurrentElement());
									refreshGUI();
								}))));
	}
}
