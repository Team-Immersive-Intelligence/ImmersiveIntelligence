package pl.pabilo8.immersiveintelligence.api.utils;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 22.09.2023
 */
public class ItemTooltipHandler
{
	public interface IAdvancedTooltipItem
	{
		@SideOnly(Side.CLIENT)
		void addAdvancedInformation(ItemStack stack, int offsetX, List<Integer> offsetsY);
	}

	public interface IItemScrollable
	{
		void onScroll(ItemStack stack, boolean forward, EntityPlayerMP player);
	}

	public interface IGuiItem extends IEItemInterfaces.IGuiItem
	{
		@Override
		default int getGuiID(ItemStack stack)
		{
			return getGUI(stack).ordinal();
		}

		IIGuiList getGUI(ItemStack stack);
	}

	//--- Utility Methods ---//


	@SideOnly(Side.CLIENT)
	public static boolean addExpandableTooltip(int key, String message, @Nullable List<String> tooltip)
	{
		String keyName;
		IIColor keyColor;
		switch(key)
		{
			case Keyboard.KEY_LCONTROL:
				if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)||Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))
					return true;
				keyName = IIReference.DESC_HOLD_CTRL;
				keyColor = IIReference.COLORS_STANDARD[0];
				break;
			case Keyboard.KEY_LSHIFT:
				if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
					return true;
				keyName = IIReference.DESC_HOLD_SHIFT;
				keyColor = IIReference.COLORS_STANDARD[1];
				break;
			case Keyboard.KEY_LMENU:
				if(Keyboard.isKeyDown(Keyboard.KEY_LMENU)||Keyboard.isKeyDown(Keyboard.KEY_RMENU))
					return true;
				keyName = IIReference.DESC_HOLD_ALT;
				keyColor = IIReference.COLORS_STANDARD[2];
				break;
			case Keyboard.KEY_TAB:
				if(Keyboard.isKeyDown(key))
					return true;
				keyName = IIReference.DESC_HOLD_TAB;
				keyColor = IIReference.COLORS_STANDARD[3];
				break;
			default:
				return true;
		}

		//format the "button icon"
		String buttonIcon = I18n.format(keyName)
				.replace("[", "<hexcol="+keyColor.getHexRGB()+":[")
				.replace("]", "]>")+TextFormatting.GRAY;
		//add the tooltip
		if(tooltip!=null)
			tooltip.add(I18n.format(message, buttonIcon));
		return false;
	}

	@SideOnly(Side.CLIENT)
	public static boolean canExpandTooltip(int key)
	{
		return addExpandableTooltip(key, "", null);
	}

	@SideOnly(Side.CLIENT)
	public static ArrayList<Integer> findTooltipY(RenderTooltipEvent.PostText event)
	{
		return findTooltipY(event.getLines(), event.getY(), event.getFontRenderer());
	}

	@SideOnly(Side.CLIENT)
	public static ArrayList<Integer> findTooltipY(List<String> lines, int y, FontRenderer font)
	{
		ArrayList<Integer> list = new ArrayList<>();
		final int size = lines.size();
		boolean lastLine = false;

		for(int i = 0; i < size; i++)
			if(StringUtils.stripControlCodes(lines.get(i)).startsWith("   "))
			{
				if(!lastLine)
				{
					list.add(y+i*10+2); //height+spacing
					lastLine = true;
				}
			}
			else if(lastLine)
				lastLine = false;

		return list;
	}

	@SideOnly(Side.CLIENT)
	public static void drawItemList(int x, int y, NBTTagList stacks)
	{
		ItemStack[] array = new ItemStack[stacks.tagCount()];
		for(int i = 0; i < stacks.tagCount(); i++)
			array[i] = new ItemStack(stacks.getCompoundTagAt(i));
		drawItemList(x, y, array);
	}

	@SideOnly(Side.CLIENT)
	public static void drawItemList(int x, int y, ItemStack[] stacks)
	{
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.enableRescaleNormal();
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.enableDepth();
		GlStateManager.translate(x, y, 700);
		GlStateManager.scale(.5f, .5f, 1);

		RenderItem renderItem = ClientUtils.mc().getRenderItem();
		for(int i = 0; i < stacks.length; i++)
			renderItem.renderItemIntoGUI(stacks[i], 0, i*20);

		GlStateManager.disableDepth();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}
}
