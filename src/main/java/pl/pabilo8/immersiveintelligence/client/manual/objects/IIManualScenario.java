package pl.pabilo8.immersiveintelligence.client.manual.objects;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualObject;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPage;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * Displays a scene using 3D models. Very useful for showing practical in-world examples.
 *
 * @author Pabilo8
 * @since 04.11.2022
 */
public class IIManualScenario extends IIManualObject
{
	//--- Setup ---//
	private int animationTime = 0, maxAnimationTime;
	private float displayScale;
	private float ww, hh;
	private AMT[] objects, overlay;
	private IIAnimationCompiledMap animation;
	private HoverBox[] hovers;

	private String hoveredText = null;

	public IIManualScenario(ManualObjectInfo info, EasyNBT nbt)
	{
		super(info, nbt);
	}

	@Override
	public void postInit(IIManualPage page)
	{
		super.postInit(page);

		this.objects = dataSource.getList("elements", 10).tagList.stream()
				.map(this::createAMT)
				.toArray(AMT[]::new);
		this.overlay = dataSource.getList("overlay", 10).tagList.stream()
				.map(this::createAMT)
				.toArray(AMT[]::new);
		this.hovers = dataSource.getList("hovers", 10).tagList.stream()
				.map(n -> EasyNBT.wrapNBT((NBTTagCompound)n))
				.map(HoverBox::new)
				.toArray(HoverBox[]::new);

		this.animation = IIAnimationCompiledMap.create(joinElements(), new ResourceLocation(dataSource.getString("animation")));
		this.maxAnimationTime = dataSource.getInt("duration");

		ww = width/2f;
		hh = height/2f;
		dataSource.checkSetFloat("scale", f -> displayScale = 20*f, 1f);

	}

	private AMT[] joinElements()
	{
		AMT[] amt = new AMT[this.objects.length+this.overlay.length];
		System.arraycopy(this.objects, 0, amt, 0, this.objects.length);
		System.arraycopy(this.overlay, 0, amt, this.objects.length, this.overlay.length);
		return amt;
	}

	@Nonnull
	private AMT createAMT(NBTBase tag)
	{
		// TODO: 25.11.2022 IMPROVE
		EasyNBT nbt = EasyNBT.wrapNBT((NBTTagCompound)tag);

		//create a main node for the model
		AMT amt;

		switch(nbt.getString("type"))
		{
			case "item":
				amt = new AMTItem(nbt.getString("name"), Vec3d.ZERO);
				((AMTItem)amt).setStack(nbt.getItemStack("stack"));
				break;
			case "text":
				amt = new AMTText(nbt.getString("name"), Vec3d.ZERO);
				((AMTText)amt).setText(getText(nbt));
				((AMTText)amt).setFontSize(0.0625f);
				nbt.checkSetString("font_size", ((AMTText)amt)::setText);
				nbt.checkSetColor("color", ((AMTText)amt)::setColor);
				nbt.checkSetFloat("font_size", ((AMTText)amt)::setFontSize);
				break;
			case "wire":
				amt = new AMTWire(nbt.getString("name"), Vec3d.ZERO,
						nbt.getVec3d("start"),
						nbt.getVec3d("end"),
						nbt.getInt("color"),
						nbt.getFloat("diameter")
				);
				break;
			default:
			case "model":
			{
				amt = new AMTLocator(nbt.getString("name"), Vec3d.ZERO);
				//load the model and add the parts as main node's children
				amt.setChildren(IIAnimationUtils.getAMTFromRes(new ResourceLocation(nbt.getString("model")), null));
			}
			break;
		}

		IIAnimationUtils.setModelTranslation(amt, nbt.getVec3d("pos"));
		IIAnimationUtils.setModelRotation(amt, nbt.getVec3d("rot"));

		return amt;
	}

	@Override
	protected int getDefaultHeight()
	{
		return 64;
	}

	@Override
	protected int getDefaultWidth()
	{
		return 120;
	}

	//--- Rendering, Reaction ---//

	@Override
	public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
	{
		super.drawButton(mc, mx, my, partialTicks);

		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();

		if(animation!=null&&maxAnimationTime > 0)
		{
			this.animationTime = (this.animationTime+1)%maxAnimationTime;
			animation.apply((this.animationTime+partialTicks)/(float)maxAnimationTime);
		}

		GlStateManager.enableOutlineMode(0xff000000);
		ClientUtils.drawColouredRect(x, y, width, height, 0x0f000000);
		GlStateManager.disableOutlineMode();

		GlStateManager.pushMatrix();
		GlStateManager.enableDepth();
		//center at the image's middle
		GlStateManager.translate(x+ww, y+hh, 100);
		GlStateManager.translate(0, 15, 0);

		//rotation for the base plane
		GlStateManager.rotate(-35, 0, 0, 1);
		GlStateManager.rotate(55, 0, 1, 0);
		GlStateManager.rotate(55, 1, 0, 0);
		GlStateManager.translate(-hh, -hh, 0);

		GlStateManager.enableRescaleNormal();
		RenderHelper.enableStandardItemLighting();

		//draw the base plane
		//yes, height * height, it should be a square
		ClientUtils.drawColouredRect(0, 0, height, height, 0x2c000000);
		GlStateManager.translate(hh, hh, 0);
		GlStateManager.rotate(-90, 1, 0, 0);
		GlStateManager.rotate(90, 0, 1, 0);
		ClientUtils.bindAtlas();

		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();

		//apply scale
		GlStateManager.scale(displayScale, -displayScale, displayScale);
		GlStateManager.translate(-0.5, 0, -0.5);

		//draw the scene
		for(AMT mod : this.objects)
			mod.render(tes, buf);

		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableLighting();

		GlStateManager.popMatrix();
		GlStateManager.color(1f, 1f, 1f, 1f);

		GlStateManager.pushMatrix();
		GlStateManager.translate(x+8, y+8, 0);
		GlStateManager.scale(-16, 16, -16);
		GlStateManager.rotate(180, 0, 1, 0);
		for(AMT mod : this.overlay)
			mod.render(tes, buf);
		GlStateManager.popMatrix();

		this.hoveredText = null;
		for(HoverBox hover : hovers)
			if(IIMath.isPointInRectangle(hover.x, hover.y, hover.xx, hover.yy, mx, my))
			{
				this.hoveredText = hover.text;
				break;
			}
		GlStateManager.color(1f, 1f, 1f, 1f);


	}

	@Override
	public void mouseDragged(int x, int y, int clickX, int clickY, int mx, int my, int lastX, int lastY, int button)
	{
		//TODO: 20.11.2023 clickable links
	}

	@Override
	public List<String> getTooltip(Minecraft mc, int mx, int my)
	{
		return hoveredText==null?null: Collections.singletonList(hoveredText);
	}

	private class HoverBox
	{
		final int x, y, xx, yy;
		final String text;

		public HoverBox(EasyNBT nbt)
		{
			this.x = IIManualScenario.this.x+nbt.getInt("x");
			this.y = IIManualScenario.this.y+nbt.getInt("y");
			this.xx = x+nbt.getInt("w");
			this.yy = y+nbt.getInt("h");
			this.text = getText(nbt);
		}
	}
}
