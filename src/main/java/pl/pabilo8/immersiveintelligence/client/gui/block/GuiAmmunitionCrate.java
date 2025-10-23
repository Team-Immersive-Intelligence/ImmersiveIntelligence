package pl.pabilo8.immersiveintelligence.client.gui.block;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoResource;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 17.05.2019
 */
@DecoTemplate(name = "ammunitioncrate", category = DecoGuiCategory.GENERIC_TILE)
public class GuiAmmunitionCrate extends DecoGui<TileEntityAmmunitionCrate, ContainerAmmunitionCrate>
{

	@DecoResource
	public static final ResourceLocation TEXTURE_AMMO = IIReference.RES_II.with("gui/ammunition_crate");

	public GuiAmmunitionCrate(EntityPlayer player, TileEntityAmmunitionCrate tile)
	{
		super(player, tile, IIGUI.AMMUNITION_CRATE);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(null, 0, 0, 176, 76)
				.withBox(DecoTextures.GUI_BG_WOODEN, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_ROUND_WOODEN, 0, 89, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventorySlots(SlotStyle.IE_CUSTOM1, container.slotsInputrevolver)
				.withInventorySlots(SlotStyle.IE_CUSTOM1, container.slotsInputbullet)
				.withInventorySlots(SlotStyle.IE_CUSTOM1, container.slotsInputshell)
				.withInventoryTitleBar()
				.build();


		addComponents(

				new DecoImage(0, -45)
						.withSize(175, 132)
						.withImageLocation(TEXTURE_AMMO, true)
						.withUV(256, 0, 0, 175, 132)
		);
	}
}
