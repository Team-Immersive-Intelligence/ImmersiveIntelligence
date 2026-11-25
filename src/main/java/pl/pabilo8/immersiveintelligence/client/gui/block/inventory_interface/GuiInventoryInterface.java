package pl.pabilo8.immersiveintelligence.client.gui.block.inventory_interface;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoCheckbox;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityInventoryInterface;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.inventory_interface.InventoryInterfaceRule;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerInventoryInterface;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

/**
 * Inventory notification rules and connector channels.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 27.10.2026
 */
@DecoTemplate(name = "inventory_interface", category = DecoGuiCategory.DATA_TILE)
public class GuiInventoryInterface extends DecoTileGui<TileEntityInventoryInterface, ContainerInventoryInterface>
{
	protected static final String KEY = IIReference.GUI_LABEL_KEY+"inventory_interface.";
	protected static final int MAX_RULES = TileEntityInventoryInterface.MAX_RULES;

	@SyncNBT
	public EasyCollection<InventoryInterfaceRule, NBTTagCompound> rules = new EasyCollection<>(InventoryInterfaceRule::new);
	@SyncNBT
	public int colorIn, colorOut = 1;
	@SyncNBT
	public int editedRule = -1;
	@SyncNBT
	public int ruleScroll;

	private boolean navigationLoaded;
	private DecoList<InventoryInterfaceRule> ruleList;

	public GuiInventoryInterface(EntityPlayer player, TileEntityInventoryInterface tile)
	{
		this(player, tile, IIGUI.INVENTORY_INTERFACE);
	}

	protected GuiInventoryInterface(EntityPlayer player, TileEntityInventoryInterface tile, IIGUI gui)
	{
		super(player, tile, gui);
		if(tile!=null)
		{
			rules = tile.rules.clone();
			colorIn = tile.colorIn;
			colorOut = tile.colorOut;
		}
	}

	@Override
	protected EasyNBT loadGuiData()
	{
		if(navigationLoaded)
		{
			captureDraft();
			return EasyNBT.newNBT();
		}
		navigationLoaded = true;
		return super.loadGuiData();
	}

	@Override
	public void onInit()
	{
		changeGUIFlag = false;
		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 320, 144)
				.withTitleBar(KEY+"title")
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 72, 144, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();

		addLabel(KEY+"channel_in", 8, 12+2);
		addComponent(new DecoDropdown<EnumDyeColor>(68, 10+2)
				.withTemplate(DecoTemplates.DYE_COLOR_DROPDOWN)
				.withSize(84, 12).withDropdownWidth(100)
				.withSelectedEntry(EnumDyeColor.byMetadata(colorIn))
				.withOnSelectedEntry((oldColor, newColor) -> {
					colorIn = newColor.getMetadata();
					if(colorIn==colorOut)
						colorOut = oldColor.getMetadata();
					refreshGUI();
				}));
		addLabel(KEY+"channel_out", 164, 12+2);
		addComponent(new DecoDropdown<EnumDyeColor>(224, 10+2)
				.withTemplate(DecoTemplates.DYE_COLOR_DROPDOWN)
				.withSize(88, 12).withDropdownWidth(100)
				.withSelectedEntry(EnumDyeColor.byMetadata(colorOut))
				.withOnSelectedEntry((oldColor, newColor) -> {
					colorOut = newColor.getMetadata();
					if(colorOut==colorIn)
						colorIn = oldColor.getMetadata();
					refreshGUI();
				}));

		ruleList = addComponent(new DecoList<InventoryInterfaceRule>(8, 30)
				.withSize(304, 106)
				.withEntries(rules)
				.withCreateLaterAction(rules.size() < MAX_RULES?() -> {
					editedRule = -1;
					changeGUI(IIGUI.INVENTORY_INTERFACE_EDIT);
				}: null)
				.withDisplayFunction(new DecoEntryPanelBuilder<InventoryInterfaceRule>().withHeight(20)
						.withBackground(DecoTextures.BG_PAPER)
						.withComponent(p -> new DecoCheckbox(3, 3)
								.withSize(14, 14)
								.withChecked(p.getCurrentElement().enabled)
								.withTranslatedTooltip(KEY+"enabled")
								.withOnToggle(value -> p.getCurrentElement().enabled = value))
						.withLabel("summary", p -> new DecoLabel(fontRenderer, 21, 5)
								.withSize(p.width-62, 12).withAlign(DecoAlignment.LEFT))
						.withComponent(p -> new DecoButton(p.width-34, 2)
								.withTemplate(DecoTemplates.ACTION_BUTTON_EDIT)
								.withOnLMBPressed(() -> {
									editedRule = rules.indexOf(p.getCurrentElement());
									if(editedRule >= 0)
										changeGUI(IIGUI.INVENTORY_INTERFACE_EDIT);
								}))
						.withComponent(p -> new DecoButton(p.width-18, 2)
								.withTemplate(DecoTemplates.ACTION_BUTTON_REMOVE)
								.withOnLMBPressed(() -> {
									rules.remove(p.getCurrentElement());
									refreshGUI();
								}))
						.withElementApplyMethod((rule, panel) -> panel.label("summary")
								.withRawText(fontRenderer.trimStringToWidth(I18n.format(KEY+"rule_summary", rule.trigger.getLocalizedName(), rule.outputs.size()), panel.width-62))))
				.withScroll(ruleScroll));
	}

	protected void captureDraft()
	{
		if(ruleList!=null)
			ruleScroll = ruleList.getScroll();
	}

	@Override
	protected EasyNBT saveGuiData()
	{
		captureDraft();
		return super.saveGuiData();
	}

	@Override
	protected void cleanupDecoGui()
	{
		captureDraft();
		super.cleanupDecoGui();
	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		return EasyNBT.newNBT().withSerializable("rules", rules)
				.withInt("colorIn", colorIn).withInt("colorOut", colorOut);
	}
}
