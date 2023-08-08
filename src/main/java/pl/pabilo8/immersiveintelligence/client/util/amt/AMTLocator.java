package pl.pabilo8.immersiveintelligence.client.util.amt;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;

/**
 * An empty AMT type, used to group models
 *
 * @author Pabilo8
 * @since 21.08.2022
 */
public class AMTLocator extends AMT
{
	public AMTLocator(String name, Vec3d originPos)
	{
		super(name, originPos);
	}

	public AMTLocator(String name, IIModelHeader header)
	{
		super(name, header);
	}

	@Override
	protected void draw(Tessellator tes, BufferBuilder buf)
	{

	}

	@Override
	public void disposeOf()
	{

	}
}
