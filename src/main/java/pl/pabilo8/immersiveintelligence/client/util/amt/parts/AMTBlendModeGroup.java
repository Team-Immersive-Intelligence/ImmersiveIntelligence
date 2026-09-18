package pl.pabilo8.immersiveintelligence.client.util.amt.parts;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.09.2026
 */
public class AMTBlendModeGroup extends AMT
{
	private SourceFactor sourceFactor = SourceFactor.SRC_ALPHA;
	private DestFactor destFactor = DestFactor.ONE_MINUS_SRC_ALPHA;

	public AMTBlendModeGroup(String name, AMTModelHeader header)
	{
		super(name, header);
	}

	public AMTBlendModeGroup(String name, Vec3d originPos)
	{
		super(name, originPos);
	}

	@Override
	protected void draw(Tessellator tes, BufferBuilder buf)
	{

	}

	@Override
	protected void preDraw()
	{
		//Apply custom blending
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(sourceFactor, destFactor);
	}

	@Override
	protected void postDraw()
	{
		//Apply default blending
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
	}

	@Override
	protected AMT renamedCopy(String newName)
	{
		return new AMTBlendModeGroup(newName, originPos);
	}

	@Override
	public void disposeOf()
	{

	}

	@Override
	@Nonnull
	public AxisAlignedBB getBoundingBox()
	{
		return new AxisAlignedBB(originPos, originPos);
	}

	@Override
	public void applyProperties(EasyNBT nbt)
	{
		super.applyProperties(nbt);
		nbt.checkSetEnum("source_factor", SourceFactor.class, result -> this.sourceFactor = result);
		nbt.checkSetEnum("dest_factor", DestFactor.class, result -> this.destFactor = result);
	}
}
