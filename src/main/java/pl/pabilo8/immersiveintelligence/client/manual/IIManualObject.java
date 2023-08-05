package pl.pabilo8.immersiveintelligence.client.manual;

import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.gui.GuiButtonManual;
import blusunrize.lib.manual.gui.GuiManual;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.api.utils.IAdvancedMultiblock;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 22.03.2022
 */
public abstract class IIManualObject extends GuiButtonManual
{
	//--- Setup ---//

	@Nonnull
	protected EasyNBT dataSource;
	@Nonnull
	protected final ManualInstance manual;

	public IIManualObject(ManualObjectInfo info, EasyNBT nbt)
	{
		super(info.gui, info.id, info.x, info.y, 0, 0, "");

		this.dataSource = nbt;
		this.manual = gui.getManual();
		nbt.checkSetInt("x", i -> this.x = info.x+i);
		nbt.checkSetInt("w", i -> this.width = i, getDefaultWidth());
		nbt.checkSetInt("h", i -> this.height = i, getDefaultHeight());
	}

	/**
	 * Used to add items to page (for search reference) and configure stuff based on the page
	 */
	public void postInit(IIManualPage page)
	{
		//load the data source and combine it with the provided nbt compound, if it's mentioned
		dataSource.checkSetString("source", s -> dataSource.mergeWith(page.getDataSource(s)));
	}

	@Override
	public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
	{
		if(this.visible)
		{
			this.hovered = mx >= this.x&&mx < (this.x+this.width)&&my >= this.y&&my < (this.y+this.height);
			this.mouseDragged(mc, mx, my);
		}
	}

	protected abstract int getDefaultHeight();

	protected int getDefaultWidth()
	{
		return 120;
	}

	//do not extend
	@Override
	protected final void mouseDragged(@Nonnull Minecraft mc, int mouseX, int mouseY)
	{

	}

	/**
	 * Called when mouse has been dragged across the gui
	 *
	 * @param x      gui X
	 * @param y      gui Y
	 * @param clickX the initial point of drag X
	 * @param clickY the initial point of drag Y
	 * @param mx     current mouse X
	 * @param my     current mouse Y
	 * @param lastX  mouse X from last time the method was called
	 * @param lastY  mouse Y from last time the method was called
	 * @param button mouse button ID
	 */
	public abstract void mouseDragged(int x, int y, int clickX, int clickY, int mx, int my, int lastX, int lastY, int button);

	/**
	 * Used for drawing tooltips, called after drawing buttons
	 *
	 * @return
	 */
	@Nullable
	public abstract List<String> getTooltip(Minecraft mc, int mx, int my);

	public String getText(EasyNBT nbt)
	{
		if(nbt.hasKey("text"))
			return I18n.format(nbt.getString("text"));
		if(nbt.hasKey("item"))
			return nbt.getItemStack("item").getDisplayName(); //itemstack name
		else if(nbt.hasKey("mb"))
		{
			String mbName = nbt.getString("mb");
			IMultiblock multiblock = MultiblockHandler.getMultiblocks().stream().filter(mb -> mb.getUniqueName().equals(mbName)).findFirst().orElse(null);

			if(multiblock instanceof MultiblockStuctureBase)
			{
				Vec3i offset = ((MultiblockStuctureBase<?>)multiblock).getOffset();
				return TextFormatting.DARK_GRAY+I18n.format(
						nbt.hasKey("long")?"ie.manual.entry.multiblock_forming": "ie.manual.entry.multiblock_forming_short",
						TextFormatting.BOLD+I18n.format("desc.immersiveengineering.info.multiblock."+mbName)+TextFormatting.DARK_GRAY,
						TextFormatting.GOLD.toString()+(multiblock.getClass().isAnnotationPresent(IAdvancedMultiblock.class)?
								IIContent.itemHammer.getStack(1):
								(new ItemStack(IEContent.itemTool, 1)).getDisplayName())+TextFormatting.DARK_GRAY,
						"",
						TextFormatting.BOLD+((multiblock.getStructureManual()[offset.getX()][offset.getY()][offset.getZ()]).getDisplayName())+TextFormatting.DARK_GRAY
				);
			}
			else
				return "";
		}
		else if(nbt.hasKey("fluid"))
			return nbt.getFluidStack("fluid").getLocalizedName(); //fluid name
		else
			return "missingno"; //translate automatically
	}

	public static class ManualObjectInfo
	{
		final GuiManual gui;
		final int x;
		final int y;
		final int id;

		public ManualObjectInfo(GuiManual gui, int x, int y, int id)
		{
			this.gui = gui;
			this.x = x;
			this.y = y;
			this.id = id;
		}
	}
}
