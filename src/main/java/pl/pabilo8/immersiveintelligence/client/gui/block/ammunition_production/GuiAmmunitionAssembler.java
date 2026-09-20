package pl.pabilo8.immersiveintelligence.client.gui.block.ammunition_production;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoItemStackDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextFilter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage.ImageAnimationDirection;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

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
	public static ResourceLocation TEXTURE = ResLoc.of(IIReference.RES_II.with("gui/ammunition_assembler"));
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public FuseType fuseType;
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public int fuseConfig;

	public GuiAmmunitionAssembler(EntityPlayer player, TileEntityAmmunitionAssembler tile)
	{
		super(player, tile, IIGUI.AMMUNITION_ASSEMBLER);
	}


	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.BG_STEEL_ROUGH, 0, 0, 176, 76+24)
				.withTitleBar(tile)
				.withInventorySlots(SlotStyle.IE_INPUT, container.coreSlot, container.casingSlot)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.outputSlot)
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 76+24, 176, 92)
				.withInventoryTitleBar()
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.build();

		//Set synced variables
		this.fuseType = tile.fuseType;
		this.fuseConfig = tile.fuseConfig;
		syncAnimatedParts(tile.hatch, true);

		DecoPanel configPanel;
		addComponents(
				//Energy bar
				new DecoBar(150+11, 0)
						.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage)),

				//Progress
				new DecoImage(8+64+8+2+1-48-2-4+24-1, 28+4+4+4+1-16)
						.withSize(64, 32)
						.withImageLocation(TEXTURE, true)
						.withUV(64, 0, 0, 64, 32),
				new DecoImage(8+64+8+2+1-48-2-4+1+24-1, 28+4+4+4+1-16+1)
						.withSize(64, 30)
						.withImageLocation(TEXTURE, true)
						.withUV(64, 0, 34, 64, 34+30)
						.withAnimation(ImageAnimationDirection.LEFT_TO_RIGHT, DecoGuiUtils.getMultiblockProductionMultiProgress(tile)),

				new DecoItemStackDisplay(8+64+8+2+1-48-2-4+64+2, 28+4+4+4+1-16+8),

				//Config bar
				configPanel = new DecoPanel(0, 76-2)
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
						.withSize(176, 24)
		);

		DecoLabel configLabel = configPanel.addLabel("pole wspaniale", 4+80+2, 2)
				.withAlign(DecoAlignment.RIGHT)
				.withSize(64-12, 18);

		DecoTextField textField;
		configPanel.addComponents(
				(textField = new DecoTextField(176-32-4, 2))
						.withSize(32, 18)
						.withTextColor(IIColor.WHITE)
						.withFilter(TextFilter.DECIMAL)
						.withText(this.fuseConfig)
						.withOnTextChanged(newValue -> {
							//Contact fuse has no config value
							if(this.fuseType==FuseType.CONTACT)
								textField.withText(newValue = "0");
							this.fuseConfig = IIStringUtil.parseInt(newValue);
						}),
				new DecoDropdown<FuseType>(4, 2)
						.withEntries(FuseType.values())
						.withDisplayFunction(new DecoEntryPanelBuilder<FuseType>()
								//Type Icon, Label, and Letter
								.withComponent("icon", p -> new DecoImage(2, 2)
										.withSize(16, 16)
								)
								.withLabel("label", p ->
										new DecoLabel(fontRenderer, 20, 2)
												.withSize(48, 18)
												.withAlign(DecoAlignment.LEFT)
												.withText("Proximity")
								)
								.withElementApplyMethod(this::drawFuseEntry)
						)
						.withScrollBarBackground(DecoTextures.COMPONENT_SLIDER_PAPER)
						.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
						.withOnSelectedEntry((oldFuse, newFuse) -> {
							textField.withDisabled(newFuse==FuseType.CONTACT);
							textField.visible = newFuse!=FuseType.CONTACT;
							configLabel.visible = newFuse!=FuseType.CONTACT;
							this.fuseType = newFuse;
							configLabel.withText(I18n.format(IIReference.GUI_LABEL_KEY+"ammunition_assembler.fuse_config."+newFuse.getName()))
									.withTranslatedTooltip(IIReference.GUI_LABEL_KEY+"ammunition_assembler.fuse_config."+newFuse.getName()+".tooltip");
						})
						.withSelectedEntry(this.fuseType)
						.withSize(80, 20)
		);
	}

	private void drawFuseEntry(FuseType fuseType, DecoEntryPanelBuilder<FuseType> builder)
	{
		builder.label("label").withRawText(fuseType.getLocalizedName());
		builder.component("icon", DecoImage.class)
				.withImageLocation(ResLoc.of(IIReference.RES_II, "gui/deco/icons/icon_fuse_"+fuseType.getName()), true);
	}

	@Override
	protected void onGuiClosedWithoutTransition()
	{
		super.onGuiClosedWithoutTransition();
		syncAnimatedParts(tile.hatch, false);
	}
}
