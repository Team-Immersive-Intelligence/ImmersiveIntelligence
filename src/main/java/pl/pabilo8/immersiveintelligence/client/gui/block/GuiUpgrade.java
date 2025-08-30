package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.api.utils.upgrade.IUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.utils.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerUpgrade;

import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 10.07.2019
 */
@DecoTemplate(name = "upgrade", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiUpgrade<T extends TileEntityIEBase & IIEInventory & IUpgradableDevice> extends DecoGui<T, ContainerUpgrade<T>>
{
	public boolean info = false;
	public boolean previewInstalled = false;

	List<Upgrade> upgrades;
	Upgrade previewed = null;

	public GuiUpgrade(EntityPlayer player, T tile)
	{
		super(player, tile, IIGUI.UPGRADE);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.GUI_BG_STEEL, 0, 0, 176, 76)
				.withTitleBar("Upgrade")
				.withBox(DecoTextures.GUI_BG_WOODEN, 0, 76, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.inventorySlots)
				.withInventoryTitleBar()
				.build();
	}
}
