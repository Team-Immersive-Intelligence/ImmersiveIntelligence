package pl.pabilo8.immersiveintelligence.client.gui.block.arithmetic_logic_machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.api.data.DataVariable;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTab;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoResource;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerArithmeticLogicMachine.CircuitSlot;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIFunctionalCircuit;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 03.08.2025
 * @ii-approved 0.3.1
 * @since 30.06.2019
 */
@DecoTemplate(name = "arithmetic_logic_machine", category = DecoGuiCategory.DATA_TILE)
public class GuiArithmeticLogicMachine extends DecoGui<TileEntityArithmeticLogicMachine, ContainerArithmeticLogicMachine>
{
	@DecoResource
	public static ResourceLocation ICON_STORAGE = ResLoc.of(IIReference.RES_II, "gui/tab_icons/storage");

	protected DecoList<DataVariable> list;
	boolean isStorage;

	private GuiArithmeticLogicMachine(EntityPlayer player, TileEntityArithmeticLogicMachine tile, IIGUI gui)
	{
		super(player, tile, gui);
		isStorage = gui==IIGUI.ARITHMETIC_LOGIC_MACHINE_STORAGE;
	}

	public static GuiArithmeticLogicMachine getStorageGui(EntityPlayer player, TileEntity te)
	{
		return new GuiArithmeticLogicMachine(player, (TileEntityArithmeticLogicMachine)te, IIGUI.ARITHMETIC_LOGIC_MACHINE_STORAGE);
	}

	public static GuiArithmeticLogicMachine getVariablesGui(EntityPlayer player, TileEntity te)
	{
		return new GuiArithmeticLogicMachine(player, (TileEntityArithmeticLogicMachine)te, IIGUI.ARITHMETIC_LOGIC_MACHINE_VARIABLES);
	}


	@Override
	public void onInit()
	{
		syncAnimatedParts(tile.door, true);
		syncAnimatedParts(tile.drawer, true);
		syncAnimatedParts(tile.keyboard, false);

		//Build background
		startBackground()
				.withBox(IIReference.GUI_BG_STEEL, 0, 0, 176, 128+8)
				.withTitleBar(tile)
				.withBox(IIReference.GUI_BG_WOODEN, 0, 128+8, 176, 92)
				.withInventoryTitleBar()

				.withNextLayer()
				.withBox(IIReference.GUI_BG_STEEL, IIReference.RES_TEXTURES_DECO_TEMPLATE_SQUARE, 0, 8, 32, 120)
				.conditionally(isStorage,
						b -> b
								.withBox(IIReference.GUI_BG_STEEL, IIReference.RES_TEXTURES_DECO_TEMPLATE_ROUND, 128-32+16+32, 8, 32, 120)
								.withInventorySlots(SlotStyle.IE, container.circuitSlots)
				)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.build();

		addComponent(new DecoTab()
				.withLink(IIGUI.ARITHMETIC_LOGIC_MACHINE_STORAGE)
				.withIcon(ICON_STORAGE)
				.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+"storage_module")
		);

		for(ItemStack circuit : tile.inventory)
			if(!circuit.isEmpty()&&circuit.getItem() instanceof ItemIIFunctionalCircuit)
				addComponent(new DecoTab()
						.withLink(IIGUI.DATA_INPUT_MACHINE_VARIABLES)
						.withIcon(circuit)
						.withTranslatedTooltip(circuit.getDisplayName(), IIReference.DESCRIPTION_KEY+"variables_module")
				);
	}

	@Override
	protected void handleMouseClick(@Nullable Slot slotIn, int slotId, int mouseButton, @Nullable ClickType type)
	{
		super.handleMouseClick(slotIn, slotId, mouseButton, type);
		if(slotIn instanceof CircuitSlot)
			initGui();
	}

	@Override
	public void onGuiClosed()
	{
		if(!changeGUIFlag)
		{
			syncAnimatedParts(tile.door, false);
			syncAnimatedParts(tile.drawer, false);
			syncAnimatedParts(tile.keyboard, false);
		}
		super.onGuiClosed();
	}

}
