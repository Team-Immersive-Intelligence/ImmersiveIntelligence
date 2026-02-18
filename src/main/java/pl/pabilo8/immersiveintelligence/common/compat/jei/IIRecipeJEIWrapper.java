package pl.pabilo8.immersiveintelligence.common.compat.jei;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.client.ClientUtils;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout.IOType;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.LayoutComponent;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.RotaryMachineRecipe;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoColors;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.05.2021
 */
public class IIRecipeJEIWrapper<T extends IIMultiblockRecipe> implements IRecipeWrapper
{
	public static final ResLoc TEXTURE = IIReference.RES_TEXTURES_GUI.with("jei_stuff", ResLoc.EXT_PNG);
	private final ItemStack machineStack;
	public final T recipe;

	public IIRecipeJEIWrapper(T recipe, ItemStack machineStack)
	{
		//Prepare thyself
		this.recipe = recipe;
		this.machineStack = machineStack;
		//Thy end is now
		recipe.getRecipeLayout();
	}

	@Override
	public void getIngredients(@Nonnull IIngredients ingredients)
	{
		IIRecipeLayout recipeLayout = recipe.getRecipeLayout();
		if(recipeLayout==null)
			return;
		//Collect ingredients from layout components
		List<List<ItemStack>> itemInputs = new ArrayList<>();
		List<List<ItemStack>> itemOutputs = new ArrayList<>();
		List<FluidStack> fluidInputs = new ArrayList<>();
		List<FluidStack> fluidOutputs = new ArrayList<>();

		for(LayoutComponent component : recipeLayout.getComponents())
		{
			switch(component.getType())
			{
				case SLOT:
				{
					assert component.getData()!=null;
					IngredientStack stack = ApiUtils.createIngredientStack(component.getData());
					if(component.getIoType()==IOType.INPUT)
						itemInputs.add(stack.getStackList());
					else
						itemOutputs.add(stack.getStackList());
				}
				break;
				case FLUID_TANK:
				{
					assert component.getData()!=null;
					FluidStack fs = (FluidStack)component.getData();
					if(component.getIoType()==IOType.INPUT)
						fluidInputs.add(fs);
					else
						fluidOutputs.add(fs);
				}
				break;
				//TODO: 07.12.2025 dust tanks
				case DUST_TANK:
					break;
				default:
					break;
			}
		}

		ingredients.setInputLists(VanillaTypes.ITEM, itemInputs);
		ingredients.setOutputLists(VanillaTypes.ITEM, itemOutputs);
		ingredients.setInputs(VanillaTypes.FLUID, fluidInputs);
		ingredients.setOutputs(VanillaTypes.FLUID, fluidOutputs);

	}

	@Override
	public void drawInfo(@Nonnull Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
	{
		IIRecipeLayout recipeLayout = recipe.getRecipeLayout();
		if(recipeLayout==null)
			return;

		//Draw 3D multiblock model in the background
		//drawMultiblock(mc, recipeWidth, recipeHeight);
		for(LayoutComponent component : recipeLayout.getComponents())
			if(component.getType()==IIRecipeLayout.ComponentType.MULTIBLOCK_MODEL)
				drawMultiblock(mc, component);

		//Draw components
		for(LayoutComponent component : recipeLayout.getComponents())
		{
			int x = component.getX();
			int y = component.getY();
			int width = component.getWidth();
			int height = component.getHeight();
			String subtype = component.getSubtype();

			GlStateManager.pushMatrix();
			GlStateManager.color(1f, 1f, 1f);
			switch(component.getType())
			{
				case SLOT:
					drawSlot(mc, component, x, y, width, height, subtype);
					break;
				case INFO_DISPLAY:
					drawInfoDisplay(mc, component, x, y, width, height, subtype);
					break;
				case DUST_TANK:
				case FLUID_TANK:
					drawTank(x, y, width, height);
					break;
				default:
					break;
			}
			GlStateManager.popMatrix();
		}

		//Draw bottom bar for components
		drawBottomBar(mc, recipeWidth, recipeHeight, recipeLayout.getBottomBarComponents());
	}

	private void drawBottomBar(Minecraft mc, int recipeWidth, int recipeHeight, List<LayoutComponent> bottomBarComponents)
	{
		if(bottomBarComponents.isEmpty())
			return;

		//Draw the bar
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		TextureAtlasSprite label = mc.getTextureMapBlocks()
				.getAtlasSprite((recipe.getRecipeLayout().isEarlyGame()?DecoTextures.LABEL_WOODEN: DecoTextures.LABEL_STEEL).toString());
		IIDrawUtils.startTexturedColored()
				.drawConnectedTexColorRect(-4, recipeHeight+2-16+1, recipeWidth+8, 16, IIColor.WHITE,
						32, 16, 8, 4, label.getMinU(), label.getMaxU(), label.getMinV(), label.getInterpolatedV(8))
				.finish();
		GlStateManager.popMatrix();

		//Draw components
		for(LayoutComponent component : bottomBarComponents)
		{
			int x = component.getX();
			int y = component.getY();
			int width = component.getWidth();
			int height = component.getHeight();
			String subtype = component.getSubtype();
			GlStateManager.pushMatrix();
			GlStateManager.color(1f, 1f, 1f);
			drawInfoDisplay(mc, component, x, y, width, height, subtype);
			GlStateManager.popMatrix();
		}
	}

	private void drawMultiblock(Minecraft mc, LayoutComponent component)
	{
		if(this.machineStack.isEmpty())
			return;
		float scale = Math.min(component.getWidth(), component.getHeight());

		GlStateManager.pushMatrix();
		GlStateManager.translate(component.getX()+(component.getWidth()/2f), component.getY()+(component.getHeight()/2f), scale);
		GlStateManager.enableDepth();
		GlStateManager.scale(scale, -scale, scale);
		mc.getRenderItem().renderItem(this.machineStack, TransformType.GUI);
		GlStateManager.disableDepth();
		GlStateManager.popMatrix();
	}

	private void drawSlot(Minecraft mc, LayoutComponent component, int x, int y, int width, int height, String subtype)
	{
		//Draw vanilla slot
		if(!subtype.contains("frame"))
		{
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(SourceFactor.DST_COLOR, DestFactor.SRC_COLOR);
			ClientUtils.drawSlot(x+1, y+1, width, height, 128);
			GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			return;
		}
		//Replace subtype
		subtype = subtype.replace("frame", "")
				.replace("_", "");

		//Draw an IE styled slot
		ClientUtils.bindAtlas();
		TextureAtlasSprite label = mc.getTextureMapBlocks()
				.getAtlasSprite(DecoTextures.SLOT_IE_MARKER.toString());
		int labelI = 0;
		switch(subtype)
		{
			case "red":
			case "0":
				labelI = 4;
				break;
			case "blue":
			case "1":
				labelI = 5;
				break;
			case "green":
			case "2":
				labelI = 6;
				break;
			case "yellow":
			case "3":
				labelI = 7;
				break;
			case "purple":
			case "4":
				labelI = 8;
				break;
			case "cyan":
			case "5":
				labelI = 9;
				break;
			case "black":
			case "6":
				labelI = 10;
				break;
			case "white":
			case "7":
				labelI = 11;
				break;
			case "none":
				//noinspection DataFlowIssue
				labelI = 0;
				break;
			default:
				switch(component.getIoType())
				{
					case INPUT:
						labelI = 2;
						break;
					case OUTPUT:
						labelI = 3;
						break;
					default:
						break;
				}
				break;
		}

		float labelU = label.getInterpolatedU(labelI%4*4), labelUU = label.getInterpolatedU(labelI%4*4+4);
		//noinspection IntegerDivisionInFloatingPointContext
		float labelV = label.getInterpolatedV((labelI/4)*3), labelVV = label.getInterpolatedV((labelI/4)*3+3);
		IIDrawUtils.startTexturedColored()
				.drawConnectedTexColorRect(x-1, y-1, width+2, height+2, IIColor.WHITE, DecoTextures.SLOT_IE, 32, 32, 4, 4)
				.drawTexColorRect(x-2+width*0.5f, y-1-2, 4, 3, IIColor.WHITE, labelU, labelUU, labelV, labelVV)
				.finish();
	}

	private void drawInfoDisplay(Minecraft mc, LayoutComponent component,
								 int x, int y, int width, int height, String subtype)
	{
		switch(subtype)
		{
			case "arrow":
				drawProgressArrow(mc, x, y, width, height);
				break;
			case "time":
				drawTimeInfo(x, y);
				break;
			case "power":
				drawPowerInfo(x, y);
				break;
			case "mechanical_power":
				if(recipe instanceof RotaryMachineRecipe)
					drawMechanicalPowerInfo(x, y, (RotaryMachineRecipe)recipe);
				break;
		}
	}

	private void drawProgressArrow(Minecraft mc, int x, int y, int width, int height)
	{
		//Draw arrow background
		mc.getTextureManager().bindTexture(TEXTURE);
		ClientUtils.drawTexturedRect(x, y, width, height,
				0/256f, width/256f, 0/256f, height/256f);

		//Draw progress overlay (animated)
		long worldTime = mc.world.getTotalWorldTime();
		float progress = (worldTime%100)/100f; //Simple animation
		int progressWidth = (int)(width*progress);

		ClientUtils.drawTexturedRect(x, y, progressWidth, height,
				width/256f, (width+progressWidth)/256f, 0/256f, height/256f);
	}

	private void drawTimeInfo(int x, int y)
	{
		//Draw time icon
		ClientUtils.bindAtlas();
		IIDrawUtils.startTextured()
				.drawTexSprite(x-2, y-2, 16, 16, DecoTextures.ICON_TIME)
				.finish();

		//Draw time text
		String timeStr = GuiScreen.isShiftKeyDown()?"1 t": recipe.getTotalProcessTime()+" t";
		ClientUtils.mc().fontRenderer.drawString(timeStr, x+14, y+3, DecoColors.H1.getPackedRGB());
	}

	private void drawPowerInfo(int x, int y)
	{
		//Draw power icon
		ClientUtils.bindAtlas();
		IIDrawUtils.startTextured()
				.drawTexSprite(x-2, y-2, 16, 16, DecoTextures.ICON_ENERGY_INPUT)
				.finish();

		//Draw power text
		String powerStr = GuiScreen.isShiftKeyDown()?recipe.getTotalProcessEnergy()+" IF": String.format("%d IF/t", recipe.getEnergyPerTick());
		ClientUtils.mc().fontRenderer.drawString(powerStr, x+14, y+3, DecoColors.H1.getPackedRGB());
	}

	private void drawMechanicalPowerInfo(int x, int y, RotaryMachineRecipe recipe)
	{
		ClientUtils.bindAtlas();
		IIDrawUtils.startTextured()
				.drawTexSprite(x-2, y-1, 16, 16, DecoTextures.ICON_MECH_TORQUE_INPUT)
				.drawTexSprite(x-2+54, y-1, 16, 16, DecoTextures.ICON_MECH_SPEED_INPUT)
				.finish();

		IIClientUtils.fontRegular.drawString(recipe.getTorque()+" IT", x+16, y+3, DecoColors.H1.getPackedRGB());
		IIClientUtils.fontRegular.drawString((GuiScreen.isShiftKeyDown()?recipe.getMaxSpeed(): recipe.getMinSpeed())+" D/t", x+16+54, y+3,
				DecoColors.H1.getPackedRGB());
	}

	private void drawTank(int x, int y, int width, int height)
	{
		//Draw dust tank background
		ClientUtils.bindAtlas();
		IIDrawUtils.startTexturedColored()
				.drawConnectedTexColorRect(x-1, y-1, width+2, height+2, IIColor.WHITE,
						DecoTextures.BG_DARK_TANK, 64, 64, 8, 8)
				.drawConnectedTexColorRect(x-1, y-1, width+2, height+2, IIColor.WHITE,
						DecoTextures.COMPONENT_TANK, 64, 64, 16, 16)
				.finish();
	}
}
