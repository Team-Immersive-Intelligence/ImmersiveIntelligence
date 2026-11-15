package pl.pabilo8.immersiveintelligence.client.gui.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoItemGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerBackpack;

/**
 * Displays the storage slots of a backpack.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.11.2026
 */
@DecoTemplate(name = "backpack", category = DecoGuiCategory.GENERIC_ITEM)
public class GuiBackpack extends DecoItemGui<ContainerBackpack>
{
	public GuiBackpack(EntityPlayer player, ItemStack stack, EnumHand hand)
	{
		super(player, stack, hand, IIGUI.BACKPACK);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.BG_STEEL_ROUGH, DecoTextures.TEMPLATE_ROUND, 0, 0, 176, 70)
				.withTitleBar(itemStack)
				.withInventorySlots(SlotStyle.VANILLA_STEEL, container.backpackSlots)
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 70, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();
	}
}
