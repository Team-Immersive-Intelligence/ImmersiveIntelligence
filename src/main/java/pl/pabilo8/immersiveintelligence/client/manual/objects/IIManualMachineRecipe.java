package pl.pabilo8.immersiveintelligence.client.manual.objects;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.crafting.DustStack;
import pl.pabilo8.immersiveintelligence.api.crafting.DustUtils;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout.IOType;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.LayoutComponent;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.RotaryMachineRecipe;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoColors;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualObject;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPage;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Displays a crafting recipe for item(s).
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 22.05.2022
 */
public class IIManualMachineRecipe extends IIManualObject
{
	private final List<LayoutComponent> scaledComponents = new ArrayList<>();
	private IIMultiblockRecipe recipe;
	private IIRecipeLayout layout;
	private LayoutComponent hoveredComponent = null;
	private float scaleX, scaleY;
	private float scale = -1f; // precomputed uniform scale
	private int offsetX, offsetY;

	public IIManualMachineRecipe(ManualObjectInfo info, EasyNBT nbt)
	{
		super(info, nbt);
	}

	@Override
	public void postInit(IIManualPage page)
	{
		super.postInit(page);
		Class<IIMultiblockRecipe> recipeClass = IIMultiblockRecipe.getRecipeClassFromName(dataSource.getString("type"));
		if(recipeClass==null)
			layout = null;
		else
		{
			this.recipe = IIMultiblockRecipe.getRecipe(recipeClass, dataSource.getString("recipe"));
			if(this.recipe!=null)
				this.layout = recipe.getRecipeLayout();
		}

		if(recipe==null)
			IILogger.error("Recipe "+dataSource.getString("recipe")+" not found! Cannot display manual recipe object.");
		else if(layout==null)
			IILogger.error("Recipe "+dataSource.getString("recipe")+" does not contain a valid layout! Cannot display manual recipe object.");
		else
		{
			//Clear scaled components list
			scaledComponents.clear();

			//Scale down components to fit into manual width
			//Store scaled component for tooltip detection
			for(LayoutComponent component : layout.getComponents())
				scaledComponents.add(LayoutComponent.builder(component.getType(), component.getX(), component.getY())
						.ioType(component.getIoType())
						.size(component.getWidth(), component.getHeight())
						.subtype(component.getSubtype())
						.data(component.getData())
						.build());

			//Scale down bottom bar components as well
			for(LayoutComponent component : layout.getBottomBarComponents())
				scaledComponents.add(LayoutComponent.builder(component.getType(), component.getX(), component.getY())
						.ioType(component.getIoType())
						.size(component.getWidth(), component.getHeight())
						.subtype(component.getSubtype())
						.data(component.getData())
						.build());

			// Precompute scale/offsets now that layout and components are known.
			// Note: width/height are taken from the object state; if they change later,
			// drawButton falls back to recomputing.
			computeScaleAndOffsets();
		}
	}

	// New helper: compute scale and offsets once
	private void computeScaleAndOffsets()
	{
		if(layout==null)
			return;

		int layoutWidth = layout.getGridWidth();
		int layoutHeight = layout.getGridHeight();

		float availableWidth = width-8; //4px padding on each side
		float availableHeight = height-8;

		scaleX = availableWidth/layoutWidth;
		scaleY = availableHeight/layoutHeight;
		scale = Math.min(scaleX, scaleY);

		offsetX = (int)(x+(width-layoutWidth*scale)/2);
		offsetY = (int)(y+(height-layoutHeight*scale)/2);
	}

	//--- Rendering, Reaction ---//

	@Override
	public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
	{
		super.drawButton(mc, mx, my, partialTicks);

		if(layout==null||recipe==null)
			return;

		// If object size changed or scale wasn't computed earlier, compute now.
		if(scale <= 0f)
			computeScaleAndOffsets();

		//Draw the background frame
		drawBackgroundFrame((int)(layout.getGridWidth()*scale)+8, (int)(layout.getGridHeight()*scale)+8);

		//Scale and position components
		GlStateManager.pushMatrix();
		GlStateManager.translate(offsetX, offsetY, 0);
		GlStateManager.scale(scale, scale, 1.0f);

		//Update hovered component
		hoveredComponent = null;
		int scaledMouseX = (int)((mx-offsetX)/scale);
		int scaledMouseY = (int)((my-offsetY)/scale);

		for(LayoutComponent component : scaledComponents)
		{
			//Draw component
			drawComponent(mc, component);
			//Check if mouse is hovering over this component
			if(isMouseOverComponent(scaledMouseX, scaledMouseY, component))
				hoveredComponent = component;
		}

		GlStateManager.popMatrix();
	}

	private void drawBackgroundFrame(int width, int height)
	{
		GlStateManager.pushMatrix();
		GlStateManager.color(1f, 1f, 1f, 1f);
		ClientUtils.bindAtlas();
		GlStateManager.disableTexture2D();

		GlStateManager.enableOutlineMode(0xff000000);
		ClientUtils.drawColouredRect(x, y, width, height+4, 0x0f000000);

		GlStateManager.disableOutlineMode();
		GlStateManager.enableTexture2D();
		GlStateManager.popMatrix();
	}

	private void drawComponent(Minecraft mc, LayoutComponent component)
	{
		int x = component.getX();
		int y = component.getY();

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.color(1f, 1f, 1f);

		switch(component.getType())
		{
			case SLOT:
				drawSlot(mc, x, y, component);
				break;
			case INFO_DISPLAY:
				drawInfoDisplay(mc, x, y, component);
				break;
			case DUST_TANK:
				drawDustTank(mc, x, y, component);
				break;
			case FLUID_TANK:
				drawFluidTank(mc, x, y, component);
				break;
			case MULTIBLOCK_MODEL:
				break;
			default:
				break;
		}

		GlStateManager.popMatrix();
	}

	private void drawSlot(Minecraft mc, int x, int y, LayoutComponent component)
	{
		int width = component.getWidth();
		int height = component.getHeight();
		String subtype = component.getSubtype();

		//Draw slot background
		GlStateManager.enableBlend();
		/*GlStateManager.blendFunc(SourceFactor.DST_COLOR, DestFactor.SRC_COLOR);
		ClientUtils.drawSlot(x+1, y+1, width, height, 128);
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);*/

		IIDrawUtils.startTexturedColored()
				.drawConnectedTexColorRect(x-1, y-1, width+2, height+2, IIColor.WHITE, DecoTextures.SLOT_IE_MANUAL,
						32, 32, 8, 8)
				.finish();

		//If this is a labeled slot (with frame subtype)
		if(subtype.contains("frame"))
			drawFrameLabel(mc, x, y, component);

		//Draw stack in slot
		ItemStack stack = null;
		Object data = component.getData();
		if(data instanceof ItemStack)
			stack = (ItemStack)data;
		else if(data instanceof IngredientStack)
			stack = ((IngredientStack)data).getRandomizedExampleStack(mc.world.getTotalWorldTime());

		if(stack!=null)
		{
			GlStateManager.pushMatrix();
			GlStateManager.color(1f, 1f, 1f);
			GlStateManager.enableDepth();
			RenderHelper.enableGUIStandardItemLighting();
			mc.getRenderItem().renderItemIntoGUI(stack, x+1, y+1);
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableDepth();
			GlStateManager.popMatrix();
		}
		else
			IILogger.error("Incorrect data for layout component. Slot data must be an ItemStack or IngredientStack.");
	}

	private void drawFrameLabel(Minecraft mc, int x, int y, LayoutComponent component)
	{
		//Similar to JEI implementation but simplified for manual
		String subtype = component.getSubtype();
		subtype = subtype.replace("frame", "").replace("_", "");

		ClientUtils.bindAtlas();
		TextureAtlasSprite label = ClientUtils.getSprite(DecoTextures.SLOT_IE_MANUAL_MARKER);

		int labelI = getLabelIndex(subtype, component.getIoType());

		float labelU = label.getInterpolatedU(labelI%4*4);
		float labelUU = label.getInterpolatedU(labelI%4*4+4);
		float labelV = label.getInterpolatedV((labelI/4)*3);
		float labelVV = label.getInterpolatedV((labelI/4)*3+3);

		IIDrawUtils.startTexturedColored()
				.drawTexColorRect(x-2+18*0.5f, y-1-2, 4, 3, IIColor.WHITE,
						labelU, labelUU, labelV, labelVV)
				.finish();
	}

	private int getLabelIndex(String subtype, IOType ioType)
	{
		switch(subtype)
		{
			case "red":
				return 4;
			case "blue":
				return 5;
			case "green":
				return 6;
			case "yellow":
				return 7;
			case "purple":
				return 8;
			case "cyan":
				return 9;
			case "black":
				return 10;
			case "white":
				return 11;
			default:
				switch(ioType)
				{
					case INPUT:
						return 2;
					case OUTPUT:
						return 3;
					default:
						return 0;
				}
		}
	}

	private void drawInfoDisplay(Minecraft mc, int x, int y, LayoutComponent component)
	{
		String subtype = component.getSubtype();
		switch(subtype)
		{
			case "arrow":
				drawProgressArrow(mc, x, y, component.getWidth(), component.getHeight());
				break;
			case "time":
				drawTimeInfo(mc, x, y);
				break;
			case "power":
				drawPowerInfo(mc, x, y);
				break;
			case "mechanical_power":
				if(recipe instanceof RotaryMachineRecipe)
					drawMechanicalPowerInfo(mc, x, y, (RotaryMachineRecipe)recipe);
				break;
		}
	}

	private void drawProgressArrow(Minecraft mc, int x, int y, int width, int height)
	{
		//TODO: 08.12.2025 implement for JEI and here
	}

	private void drawTimeInfo(Minecraft mc, int x, int y)
	{
		//Draw time icon
		ClientUtils.bindAtlas();
		IIDrawUtils.startTextured()
				.drawTexSprite(x-2, y-2, 16, 16, DecoTextures.ICON_TIME)
				.finish();

		//Draw time text
		String timeStr = recipe.getTotalProcessTime()+" t";
		mc.fontRenderer.drawString(timeStr, x+14, y+3, DecoColors.H1.getPackedRGB());
	}

	private void drawPowerInfo(Minecraft mc, int x, int y)
	{
		//Draw power icon
		ClientUtils.bindAtlas();
		IIDrawUtils.startTextured()
				.drawTexSprite(x-2, y-2, 16, 16, DecoTextures.ICON_ENERGY_INPUT)
				.finish();

		//Draw power text
		String powerStr = recipe.getTotalProcessEnergy()+" IF";
		mc.fontRenderer.drawString(powerStr, x+14, y+3, DecoColors.H1.getPackedRGB());
	}

	private void drawMechanicalPowerInfo(Minecraft mc, int x, int y, RotaryMachineRecipe recipe)
	{
		ClientUtils.bindAtlas();
		IIDrawUtils.startTextured()
				.drawTexSprite(x-2, y-1, 16, 16, DecoTextures.ICON_MECH_TORQUE_INPUT)
				.drawTexSprite(x-2+54, y-1, 16, 16, DecoTextures.ICON_MECH_SPEED_INPUT)
				.finish();

		IIClientUtils.fontRegular.drawString(recipe.getTorque()+" IT", x+16, y+3, DecoColors.H1.getPackedRGB());
		IIClientUtils.fontRegular.drawString(recipe.getMinSpeed()+" D/t", x+16+54, y+3,
				DecoColors.H1.getPackedRGB());
	}

	private void drawDustTank(Minecraft mc, int x, int y, LayoutComponent component)
	{
		int width = component.getWidth();
		int height = component.getHeight();

		//Draw tank frame
		ClientUtils.bindAtlas();
		IIDrawUtils draw = IIDrawUtils.startTexturedColored();

		//Draw dust fill if data is available
		Object data = component.getData();
		if(data instanceof DustStack)
		{
			DustStack dust = (DustStack)data;
			draw.drawRepeatedTexColorRect(x, (int)(y+(height*0.5f)), width, (int)(height*0.5f), DustUtils.getColor(dust),
					DecoTextures.COMPONENT_TANK_DUST, 16);
		}
		draw.drawConnectedTexColorRect(x-1, y-1, width+2, height+2, IIColor.WHITE,
				DecoTextures.COMPONENT_TANK_PAPER, 32, 32, 8, 8).finish();
	}

	private void drawFluidTank(Minecraft mc, int x, int y, LayoutComponent component)
	{
		int width = component.getWidth();
		int height = component.getHeight();

		//Draw tank frame
		ClientUtils.bindAtlas();
		IIDrawUtils draw = IIDrawUtils.startTexturedColored();

		Object data = component.getData();
		if(data instanceof FluidStack)
		{
			FluidStack fs = (FluidStack)data;
			Fluid fluid = fs.getFluid();
			draw.drawRepeatedTexColorRect(x, (int)(y+(height*0.5f)), width, (int)(height*0.5f),
					IIColor.fromPackedRGB(fluid.getColor(fs)), fluid.getStill(fs), 16);
		}
		draw.drawConnectedTexColorRect(x-1, y-1, width+2, height+2, IIColor.WHITE,
						DecoTextures.COMPONENT_TANK_PAPER, 32, 32, 8, 8)
				.finish();
	}

	private boolean isMouseOverComponent(int mouseX, int mouseY, LayoutComponent component)
	{
		return mouseX >= component.getX()&&
				mouseX <= component.getX()+component.getWidth()&&
				mouseY >= component.getY()&&
				mouseY <= component.getY()+component.getHeight();
	}

	@Override
	protected int getDefaultHeight()
	{
		//Auto-size based on layout if available
		//Add padding for frame
		if(layout!=null)
			return layout.getGridHeight()+20;
		return 60;
	}

	@Override
	public void mouseDragged(int x, int y, int clickX, int clickY, int mx, int my, int lastX, int lastY, int button)
	{
		//Manual recipes typically don't need dragging
	}

	@Override
	@Nonnull
	public List<String> getTooltip(Minecraft mc, int mx, int my)
	{
		List<String> tooltip = new ArrayList<>();

		if(layout==null||recipe==null||hoveredComponent==null)
			return tooltip;

		Object data = hoveredComponent.getData();
		switch(hoveredComponent.getType())
		{
			case SLOT:
			{
				ItemStack stack = ItemStack.EMPTY;
				if(data instanceof ItemStack)
					stack = (ItemStack)data;
				else if(data instanceof IngredientStack)
					stack = ((IngredientStack)data).getRandomizedExampleStack(mc.world.getTotalWorldTime());
				if(!stack.isEmpty())
					return this.gui.getItemToolTip(stack);
			}
			break;
			case FLUID_TANK:
			{
				if(data instanceof FluidStack)
				{
					FluidStack fluid = (FluidStack)data;
					ClientUtils.addFluidTooltip(fluid, tooltip, 1000);
				}
			}
			break;
			case DUST_TANK:
			{
				if(data instanceof DustStack)
				{
					DustStack dust = (DustStack)data;
					tooltip.add(TextFormatting.GRAY+DustUtils.getDustName(dust));
					tooltip.add(TextFormatting.GRAY+""+dust.amount+" mB");
				}
			}
			break;
			case INFO_DISPLAY:
			{
				switch(hoveredComponent.getSubtype())
				{
					case "time":
						tooltip.add("Processing Time");
						tooltip.add(TextFormatting.GRAY+""+recipe.getTotalProcessTime()+" ticks");
						tooltip.add(TextFormatting.GRAY+Utils.formatDouble(recipe.getTotalProcessTime()/20000f, "0.###")+" days");
						break;
					case "power":
						tooltip.add("Energy Required");
						tooltip.add(TextFormatting.GRAY+""+recipe.getTotalProcessEnergy()+" IF total");
						tooltip.add(TextFormatting.GRAY+""+recipe.getEnergyPerTick()+" IF/tick");
						break;
					case "mechanical_power":
						if(recipe instanceof RotaryMachineRecipe)
						{
							RotaryMachineRecipe rotary = (RotaryMachineRecipe)recipe;
							tooltip.add("Mechanical Power");
							tooltip.add("Torque: "+rotary.getTorque()+" IT");
							tooltip.add("Speed: "+rotary.getMinSpeed()+" to "+rotary.getMaxSpeed()+" D/t");
							tooltip.add("Total Torque: "+rotary.getTotalProcessTorque()+" IT·t");
						}
						break;
				}
			}
			break;
			default:
				break;
		}
		return tooltip;
	}
}
