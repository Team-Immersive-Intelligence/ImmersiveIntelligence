package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional.Method;
import pl.pabilo8.immersiveintelligence.api.crafting.VulcanizerRecipe;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoItemStackDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityVulcanizer;
import pl.pabilo8.immersiveintelligence.common.compat.jei.JEIHelper;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerVulcanizer;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase.IIMultiblockProcess;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 10.07.2019
 */
@DecoTemplate(name = "vulcanizer", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiVulcanizer extends DecoGui<TileEntityVulcanizer, ContainerVulcanizer>
{
	@DecoResource
	public static final ResourceLocation TEXTURE = IIReference.RES_II.with("gui/vulcanizer");
	private DecoImage imageRotato;
	private DecoItemStackDisplay produced1, produced2;

	public GuiVulcanizer(EntityPlayer player, TileEntityVulcanizer tile)
	{
		super(player, tile, IIGUI.VULCANIZER);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(null, 0, 0, 176, 76)
				.withBox(DecoTextures.BG_STEEL, 0, 0, 32, 76)
				.withInventorySlots(SlotStyle.IE_INPUT, container.slotInput[0])
				.withInventorySlots(SlotStyle.IE_CUSTOM1, container.slotInput[1])
				.withInventorySlots(SlotStyle.IE_CUSTOM3, container.slotInput[2])
				.withBox(DecoTextures.BG_STEEL, 152, 0, 24, 76)
				//.withTitleBar(tile)
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 76, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();

		addComponents(
				new DecoBar(157, -4)
						.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage)),
				new DecoImage(56, 0)
						.withSize(64, 71)
						.withImageLocation(TEXTURE, true)
						.withUV(128, 0, 0, 64, 71),
				imageRotato = new DecoImage(56, 8)
						.withSize(64, 48)
						.withImageLocation(TEXTURE, true)
						.withUV(128, 0, 71, 64, 119),
				produced1 = new DecoItemStackDisplay(48, 24)
						.withBackgroundTexture(DecoSprite.atlasSprite(DecoTextures.SLOT_IE, 32, true))
						.withStack(ItemStack.EMPTY)
						.withProgressBar(partialTicks -> tile.getProductionProgress(tile.processQueue.isEmpty()?null: tile.processQueue.get(0), partialTicks),
								IIReference.COLOR_GUI_BRASS, IIReference.COLOR_IMMERSIVE_ORANGE),
				produced2 = new DecoItemStackDisplay(112, 24)
						.withBackgroundTexture(DecoSprite.atlasSprite(DecoTextures.SLOT_IE, 32, true))
						.withStack(ItemStack.EMPTY)
						.withProgressBar(partialTicks -> tile.getProductionProgress(tile.processQueue.size() < 2?null: tile.processQueue.get(1), partialTicks),
								IIReference.COLOR_GUI_BRASS, IIReference.COLOR_IMMERSIVE_ORANGE)
		);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

		produced1.withStack(ItemStack.EMPTY);
		produced2.withStack(ItemStack.EMPTY);

		if(!tile.processQueue.isEmpty())
		{
			IIMultiblockProcess<VulcanizerRecipe> recipe = tile.processQueue.get(0);
			produced1.withStack(recipe.recipe.output);
			float progress = tile.getProductionProgress(recipe, partialTicks);
			float angle = AMTUtils.getAnimationOffsetProgress(progress, 0.78f, 0.84f, partialTicks/recipe.maxTicks)*180f;
			imageRotato.withRotation(angle);
			produced1.x = (int)(guiLeft+(56+32)+Math.cos(Math.toRadians(-angle))*-32-12);
			produced1.y = (int)(guiTop+(8+24)-Math.sin(Math.toRadians(-angle))*-32-8);

			produced2.x = (int)(guiLeft+(56+32)+Math.cos(Math.toRadians(-angle))*32-12);
			produced2.y = (int)(guiTop+(8+24)-Math.sin(Math.toRadians(-angle))*32-8);

			if(tile.processQueue.size() > 1)
				produced2.withStack(tile.processQueue.get(1).recipe.output);
		}

		//Set rotation and progress for produced items
		//imageRotato.withRotation(AMTUtils.getDebugProgress(80, partialTicks)*360f);
	}


	@Override
	@Method(modid = "jei")
	public void onInitJEICompat()
	{
		JEIHelper.addRecipesDecoGuiLink(this.imageRotato, "ii.vulcanizer");
	}
}
