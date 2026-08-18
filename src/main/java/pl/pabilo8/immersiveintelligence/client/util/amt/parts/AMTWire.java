package pl.pabilo8.immersiveintelligence.client.util.amt.parts;

import blusunrize.immersiveengineering.api.energy.wires.WireApi;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * AMT type for drawing IE wiring, using AMTQuadsBuilder for geometry.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.08.2022
 */
public class AMTWire extends AMT
{
	private AMTQuads wireModel;
	private IIColor color = IIColor.WHITE;
	private float diameter = 0.0625f;
	private float slack = 1.02f;
	private Vec3d start, end;

	public AMTWire(String name, Vec3d originPos)
	{
		super(name, originPos);
		this.start = this.end = originPos;
	}

	public AMTWire(String name, AMTModelHeader header)
	{
		this(name, header.getOffset(name));
	}

	public AMTWire(String name, Vec3d originPos, Vec3d start, Vec3d end, IIColor color, float diameter)
	{
		this(name, originPos);
		this.color = color;
		this.diameter = diameter;
		setConnection(start, end);
	}

	public AMTWire(String name, AMTModelHeader header, Vec3d start, Vec3d end, IIColor color, float diameter)
	{
		this(name, header.getOffset(name), start, end, color, diameter);
	}

	public void setConnection(Vec3d start, Vec3d end)
	{
		this.start = start;
		this.end = end;
		disposeOf();
	}

	@Override
	protected void draw(Tessellator tes, BufferBuilder buf)
	{
		if(wireModel==null)
		{
			wireModel = new AMTQuadsBuilder(null)
					.withWireSegment(start, end, diameter, slack)
					.build("wire_"+name, new Vec3d(0, 0, 0))
					.recolor(color);
		}

		IIClientUtils.bindTexture(new ResourceLocation("immersiveengineering:textures/blocks/wire.png"));
		GlStateManager.disableCull();
		wireModel.draw(tes, buf);
		GlStateManager.color(1f, 1f, 1f, 1f);
		IIClientUtils.bindAtlas();
		GlStateManager.enableCull();
	}

	@Override
	public void disposeOf()
	{
		this.wireModel = AMTUtils.disposeOf(this.wireModel);
	}

	@Override
	public void applyProperties(EasyNBT nbt)
	{
		super.applyProperties(nbt);

		nbt.checkSetString("wire_type", wireType -> {
			for(WireType wire : WireApi.INFOS.keySet())
				if(wire.getUniqueName().equalsIgnoreCase(wireType))
					this.color = IIColor.fromPackedRGB(wire.getColour(null));
		});
		nbt.checkSetColor("color", color -> this.color = color);
		nbt.checkSetFloat("diameter", diameter -> this.diameter = diameter);
		nbt.checkSetFloat("slack", slack -> this.slack = slack);

		Vec3d start = nbt.getVec3d("start");
		Vec3d end = nbt.getVec3d("end");
		if(start!=null&&end!=null)
			setConnection(start.scale(0.0625f), end.scale(0.0625f));
	}

	@Override
	protected AMT renamedCopy(String newName)
	{
		AMTWire copy = new AMTWire(newName, originPos);
		copy.color = this.color;
		copy.diameter = this.diameter;
		copy.slack = this.slack;
		copy.start = this.start;
		copy.end = this.end;
		return copy;
	}
}
