package pl.pabilo8.immersiveintelligence.client.util.amt.parts;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

import javax.annotation.Nonnull;

/**
 * An empty AMT type, used to group models
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.08.2022
 */
public class AMTLocator extends AMT
{
	public AMTLocator(String name, Vec3d originPos)
	{
		super(name, originPos);
	}

	public AMTLocator(String name, AMTModelHeader header)
	{
		super(name, header);
	}

	@Override
	protected void draw(Tessellator tes, BufferBuilder buf)
	{

	}

	@Override
	protected AMT renamedCopy(String newName)
	{
		return new AMTLocator(newName, originPos);
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


}
