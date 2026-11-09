package pl.pabilo8.immersiveintelligence.client.gui.block.inventory_interface;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.crafting.IngredientReference;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoIngredientStackPickerPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityInventoryInterface;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

/**
 * Edits the item value used by an inventory rule.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.10.2026
 */
@DecoTemplate(name = "inventory_interface_value", category = DecoGuiCategory.DATA_TILE)
public class GuiInventoryInterfaceValue extends GuiInventoryInterfaceEdit
{
	@SyncNBT
	public ItemStack value = ItemStack.EMPTY;
	@SyncNBT
	public boolean valueLoaded, matchNBT = true;
	private DecoIngredientStackPickerPanel valueEditor;

	public GuiInventoryInterfaceValue(EntityPlayer player, TileEntityInventoryInterface tile)
	{
		super(player, tile, IIGUI.INVENTORY_INTERFACE_VALUE);
	}

	@Override
	public void onInit()
	{
		changeGUIFlag = false;
		valueEditor = null;
		if(!valueLoaded)
		{
			value = draft.filter.copy();
			matchNBT = draft.matchNBT;
			valueLoaded = true;
		}
		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 240, 144)
				.withTitleBar(KEY+"rule_value")
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 32, 144, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();

		valueEditor = new DecoIngredientStackPickerPanel(8, 14)
				.withOreDictSelection(false)
				.withIngredientReference(new IngredientReference(value).setUseNBT(matchNBT));
		addComponent(valueEditor).withSize(224, 116);
		addActionButtons(240, 144, () -> {
			captureDraft();
			draft.filter = value.copy();
			draft.matchNBT = matchNBT;
			changeGUI(IIGUI.INVENTORY_INTERFACE_EDIT);
		}, IIGUI.INVENTORY_INTERFACE_EDIT);
	}

	@Override
	protected void captureDraft()
	{
		super.captureDraft();
		if(valueEditor!=null)
		{
			value = valueEditor.getItemStack();
			matchNBT = valueEditor.getIngredientReference().useNBT;
		}
	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		return EasyNBT.newNBT();
	}
}
