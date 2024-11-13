package pl.pabilo8.immersiveintelligence.client.gui.elements;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 19.07.2021
 */
public class GuiDataVariableList extends GuiButton
{
	private static final ResourceLocation TEXTURE_VARIABLES = new ResourceLocation(ImmersiveIntelligence.MODID+":textures/gui/data_input_machine.png");
	private final DataPacket packet;
	private int scroll;
	private int maxScroll;
	public boolean edit = false, delete = false, add = false;
	public int selectedOption = -1;

	public GuiDataVariableList(int id, int x, int y, int w, int h, DataPacket packet)
	{
		super(id, x, y, w, h, "");
		this.packet = packet;
		recalculateEntries();
		this.scroll = maxScroll;
	}

	/**
	 * Refreshes scroll value
	 */
	public void recalculateEntries()
	{
		maxScroll = (Math.max(packet.variables.size()-4, 0))*20;
		edit = false;
		delete = false;
	}

	/**
	 * Sets and refreshes scroll value
	 */
	public void setScrollPercent(float percent)
	{
		recalculateEntries();
		this.scroll = Math.round(MathHelper.clamp(percent*maxScroll, 0, maxScroll));
	}

	/**
	 * @return scroll percent (current/max)
	 */
	public float getScrollPercent()
	{
		return scroll/(float)maxScroll;
	}

	/**
	 * Draws this list to the screen.
	 */
	@Override
	public void drawButton(@Nonnull Minecraft mc, int mx, int my, float partialTicks)
	{
		edit = false;
		delete = false;
		add = false;

		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
			delete = true;
		else if(packet.variables.size() > 0&&IIMath.isPointInRectangle(x, y, x+width, y+height, mx, my))
		{
			//scrolling
			int mouseChange = Mouse.getDWheel();
			scroll -= Integer.signum(mouseChange)*15;
			if(Mouse.isButtonDown(0)&&IIMath.isPointInRectangle(x+width-11, y, x+width, y+114, mx, my))
			{
				float v = (my-y)/(float)height;
				setScrollPercent((my-y+(v > 0.5f?v/20f: -v/20f))/114f);
			}
			scroll = MathHelper.clamp(scroll, 0, maxScroll);

			//variable selection
			selectedOption = (int)Math.floor(((my+scroll)-this.y)/20f);
			if(selectedOption >= packet.variables.size())
				selectedOption = -1;
			else
			{
				int localY = my+scroll-this.y-(selectedOption*20);

				if(localY > 4&&localY < 16)
				{
					int localX = mx-this.x-97;
					edit = localX > 1&&localX < 13;
					delete = localX > 14&&localX < 26;
				}
			}
		}
		else
			selectedOption = -1;


		//draw scrollbar
		GlStateManager.pushMatrix();
		IIClientUtils.bindTexture(TEXTURE_VARIABLES);
		drawTexturedModalRect(this.x+width-10, this.y+(int)(getScrollPercent()*100), 128, 222, 9, 14);

		scissor(x, y, width, height);

		int i = 0;

		//draw variables
		for(char c : DataPacket.varCharacters)
		{
			if(packet.variables.containsKey(c))
			{
				DataType data = packet.getPacketVariable(c);
				drawEntry(mc, this.x, this.y+(i*20)-scroll, i==selectedOption, data, c);
				i += 1;
			}
		}

		drawAddButton(mc.fontRenderer, mx, my);

		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GlStateManager.popMatrix();
	}

	private void drawAddButton(FontRenderer fr, int mx, int my)
	{
		//Draw '+' button
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		IIClientUtils.bindTexture(TEXTURE_VARIABLES);
		GlStateManager.pushMatrix();
		int length = (this.packet.variables.size()*20)-scroll;

		this.drawTexturedModalRect(x+56, y+length, 137, 222, 18, 18);
		add = IIMath.isPointInRectangle(x+56, y+length, x+56+18, y+length+18, mx, my);
		fr.drawString("+", x+62, y+5+length, add?Lib.COLOUR_I_ImmersiveOrange: 0xffffff, true);
		GL11.glPopMatrix();

	}

	void drawEntry(Minecraft mc, int x, int y, boolean hovered, DataType data, char c)
	{
		//Base
		GL11.glPushMatrix();
		GlStateManager.disableLighting();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		GlStateManager.enableAlpha();

		IIClientUtils.bindTexture(TEXTURE_VARIABLES);

		//Background
		this.drawTexturedModalRect(x, y, 0, 222, 128, 20);
		//'Edit' Button
		this.drawTexturedModalRect(x+93+6, y+4, hovered&&edit?12: 0, 242, 12, 12);
		//'Remove' Button
		this.drawTexturedModalRect(x+93+14+5, y+4, hovered&&delete?36: 24, 242, 12, 12);

		//Variable type based effects
		float[] rgb = data.getTypeColor().getFloatRGB();
		GL11.glColor4f(rgb[0], rgb[1], rgb[2], 1f);
		this.drawTexturedModalRect(x+1, y, 155, 222, 12, 20);
		this.drawTexturedModalRect(x+52+2, y, 166, 222, 22, 20);
		this.drawTexturedModalRect(x+120+2, y, 155+32+4, 222, 8, 20);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		ClientUtils.bindTexture(String.format("immersiveintelligence:textures/gui/data_types/%s.png", data.getName()));
		//Variable Type Symbol
		ClientUtils.drawTexturedRect(x+26, y+4, 12, 12, 0f, 1f, 0f, 1f);

		FontRenderer f = mc.fontRenderer;
		if(data instanceof DataTypeExpression)
			f.drawString(I18n.format(IIReference.DATA_KEY+"function."+((DataTypeExpression)data).getOperation().getMeta().name()), x+38, y+7,
					data.getTypeColor().withBrightness(0.4f).getPackedRGB(), false);
		else
			f.drawString(I18n.format(IIReference.DATA_KEY+"datatype."+data.getName()), x+38, y+7,
					data.getTypeColor().withBrightness(0.4f).getPackedRGB(), false);
		//Draw variable name (single character)
		f.drawString(String.valueOf(c), x+11, y+5, Lib.COLOUR_I_ImmersiveOrange, true);

		GL11.glPopMatrix();
	}

	/**
	 * "Cuts out" canvas, so opengl can only render stuff within a certain border
	 */
	private void scissor(int x, int y, int xSize, int ySize)
	{
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		ScaledResolution res = new ScaledResolution(ClientUtils.mc());
		x = x*res.getScaleFactor();
		ySize = ySize*res.getScaleFactor();
		y = ClientUtils.mc().displayHeight-(y*res.getScaleFactor())-ySize;
		xSize = xSize*res.getScaleFactor();
		GL11.glScissor(x, y, xSize, ySize);
	}


	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent e).
	 */
	@Override
	public boolean mousePressed(Minecraft mc, int mx, int my)
	{
		return add||(selectedOption!=-1&&(delete||edit));
	}
}
