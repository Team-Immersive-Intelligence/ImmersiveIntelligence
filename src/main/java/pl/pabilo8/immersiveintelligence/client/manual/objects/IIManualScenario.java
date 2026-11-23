package pl.pabilo8.immersiveintelligence.client.manual.objects;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualObject;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPage;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTLocator;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Displays animated manual scenes from AMT model headers.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 27.10.2026
 * @since 04.11.2022
 */
public class IIManualScenario extends IIManualObject
{
	//--- Setup ---//
	private int maxAnimationTime;
	private long animationStart;
	private float displayScale, displayOffsetY;
	private float ww, hh;
	private AMTModel objects, overlay;
	private IIAnimationCompiledMap animation;
	private HoverBox[] hovers;

	public IIManualScenario(ManualObjectInfo info, EasyNBT nbt)
	{
		super(info, nbt);
	}

	@Override
	public void postInit(IIManualPage page)
	{
		super.postInit(page);

		AMTModel scene = new AMTModel();
		if(dataSource.hasKey("header"))
		{
			AMTModelHeader header = AMTLoader.loadHeader(new ResourceLocation(dataSource.getString("header")));
			AMT[] parts = header.createParts((name, properties) -> createModel(name, header, properties));
			header.applyHierarchy(Arrays.asList(parts));
			scene = new AMTModel(Arrays.stream(parts).filter(part -> !part.isChild()).toArray(AMT[]::new));
		}
		this.objects = new AMTModel(scene.stream()
				.filter(part -> !part.getName().equals("overlay")).toArray(AMT[]::new));
		this.overlay = new AMTModel(scene.stream()
				.filter(part -> part.getName().equals("overlay")).toArray(AMT[]::new));
		this.hovers = dataSource.streamList(NBTTagCompound.class, "hovers")
				.map(EasyNBT::wrapNBT)
				.map(HoverBox::new)
				.toArray(HoverBox[]::new);

		this.animation = dataSource.hasKey("animation")?
				IIAnimationCompiledMap.create(scene, new ResourceLocation(dataSource.getString("animation"))): null;
		this.maxAnimationTime = dataSource.getInt("duration");
		this.animationStart = Minecraft.getSystemTime();

		ww = width/2f;
		hh = height/2f;
		dataSource.checkSetFloat("scale", f -> displayScale = 20*f, 1f);
		dataSource.checkSetFloat("offset_y", f -> displayOffsetY = f, 15f);
	}

	@Nullable
	private static AMT createModel(String name, AMTModelHeader header, EasyNBT nbt)
	{
		if(!nbt.hasKey("model")&&!nbt.hasKey("models"))
			return null;

		AMTLocator part = new AMTLocator(name, header);
		List<AMT> children = new ArrayList<>();
		List<String> models = new ArrayList<>();
		nbt.checkSetString("model", models::add);
		nbt.streamList(NBTTagString.class, "models").map(NBTTagString::getString).forEach(models::add);
		for(String model : models)
		{
			//Embedded models keep their own coordinate system and use scene properties on the parent.
			AMTModel embedded = new AMTModel(DefaultVertexFormats.BLOCK, new ResourceLocation(model),
					new AMTModelHeader(), null);
			int index = children.size();
			for(AMT child : embedded.getChildrenRecursive())
				child.rename(name+"/"+index+"/"+child.getName());
			Collections.addAll(children, embedded.getParts());
		}
		part.setChildren(children.toArray(new AMT[0]));
		return part;
	}

	@Override
	protected int getDefaultHeight()
	{
		return 64;
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
			float animationTime = ((Minecraft.getSystemTime()-animationStart)/50f)%maxAnimationTime;
			animation.apply(animationTime/maxAnimationTime);
		}

		GlStateManager.enableOutlineMode(0xff000000);
		ClientUtils.drawColouredRect(x, y, width, height, 0x0f000000);
		GlStateManager.disableOutlineMode();

		GlStateManager.pushMatrix();
		GlStateManager.enableDepth();
		//center at the image's middle
		GlStateManager.translate(x+ww, y+hh+displayOffsetY, 100);

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
		this.objects.render(tes, buf);

		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableLighting();

		GlStateManager.popMatrix();
		GlStateManager.color(1f, 1f, 1f, 1f);

		GlStateManager.disableDepth();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 0);
		GlStateManager.scale(16, -16, 16);
		this.overlay.render(tes, buf);
		GlStateManager.popMatrix();

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
		for(HoverBox hover : hovers)
			if(IIMath.isPointInRectangle(hover.x, hover.y, hover.xx, hover.yy, mx, my))
				return Collections.singletonList(hover.text);
		return null;
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
