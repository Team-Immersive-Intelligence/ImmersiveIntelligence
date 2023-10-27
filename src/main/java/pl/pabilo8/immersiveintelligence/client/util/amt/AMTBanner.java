package pl.pabilo8.immersiveintelligence.client.util.amt;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;

/**
 * @author Pabilo8
 * @since 09.10.2023
 */
public class AMTBanner extends AMT
{
	public AMTBanner(String name, IIModelHeader header)
	{
		super(name, header);
	}

	public AMTBanner(String name, Vec3d originPos)
	{
		super(name, originPos);
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
