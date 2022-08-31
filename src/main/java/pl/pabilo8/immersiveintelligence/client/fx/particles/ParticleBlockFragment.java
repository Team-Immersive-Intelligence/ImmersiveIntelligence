package pl.pabilo8.immersiveintelligence.client.fx.particles;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticle;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleRenderer.DrawingStages;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 04.01.2021
 */
public class ParticleBlockFragment extends IIParticle
{
	private final IBlockState sourceState;
	private BlockPos sourcePos;

	public ParticleBlockFragment(World world, Vec3d pos, Vec3d motion, float scale, IBlockState state)
	{
		super(world, pos, motion);
		this.sourceState = state;
		this.particleScale = scale;
		this.setParticleTexture(Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state).getParticleTexture());
		this.particleMaxAge = (int)(100+(60*rand.nextFloat()));
		this.particleGravity = state.getBlock().blockParticleGravity*0.85f;
		this.particleRed = 0.6F;
		this.particleGreen = 0.6F;
		this.particleBlue = 0.6F;

	}

	/**
	 * Sets the position of the block that this particle came from. Used for calculating texture and color multiplier.
	 */
	public ParticleBlockFragment setBlockPos(BlockPos pos)
	{
		this.sourcePos = pos;

		if(this.sourceState.getBlock()==Blocks.GRASS)
		{
			return this;
		}
		else
		{
			this.multiplyColor(pos);
			return this;
		}
	}

	public ParticleBlockFragment init()
	{
		this.sourcePos = new BlockPos(this.posX, this.posY, this.posZ);
		Block block = this.sourceState.getBlock();

		if(block==Blocks.GRASS)
		{
			return this;
		}
		else
		{
			this.multiplyColor(this.sourcePos);
			return this;
		}
	}

	protected void multiplyColor(@Nullable BlockPos p_187154_1_)
	{
		int i = Minecraft.getMinecraft().getBlockColors().colorMultiplier(this.sourceState, this.world, p_187154_1_, 0);
		this.particleRed *= (float)(i >> 16&255)/255.0F;
		this.particleGreen *= (float)(i >> 8&255)/255.0F;
		this.particleBlue *= (float)(i&255)/255.0F;
	}

	/**
	 * Retrieve what effect layer (what texture) the particle should be rendered with. 0 for the particle sprite sheet,
	 * 1 for the main Texture atlas, and 3 for a custom texture
	 */
	public int getFXLayer()
	{
		return 1;
	}

	/**
	 * Renders the particle
	 */
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		double f;
		double f1;
		double f2;
		double f3;
		float f4 = 0.1F*this.particleScale;
		this.setParticleTexture(Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(sourceState).getParticleTexture());

		f = this.particleTexture.getInterpolatedU(this.particleTextureJitterX/4.0F*16.0F);
		f1 = this.particleTexture.getInterpolatedU((this.particleTextureJitterX+1.0F)/4.0F*16.0F);
		f2 = this.particleTexture.getInterpolatedV(this.particleTextureJitterY/4.0F*16.0F);
		f3 = this.particleTexture.getInterpolatedV((this.particleTextureJitterY+1.0F)/4.0F*16.0F);

		float f5 = (float)(this.prevPosX+(this.posX-this.prevPosX)*(double)partialTicks-interpPosX);
		float f6 = (float)(this.prevPosY+(this.posY-this.prevPosY)*(double)partialTicks-interpPosY);
		float f7 = (float)(this.prevPosZ+(this.posZ-this.prevPosZ)*(double)partialTicks-interpPosZ);
		int i = this.getBrightnessForRender(partialTicks);
		int j = i >> 16&65535;
		int k = i&65535;

		buffer.pos(f5-rotationX*f4-rotationXY*f4, f6-rotationZ*f4, f7-rotationYZ*f4-rotationXZ*f4).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
		buffer.pos(f5-rotationX*f4+rotationXY*f4, f6+rotationZ*f4, f7-rotationYZ*f4+rotationXZ*f4).tex(f, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
		buffer.pos(f5+rotationX*f4+rotationXY*f4, f6+rotationZ*f4, f7+rotationYZ*f4+rotationXZ*f4).tex(f1, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
		buffer.pos(f5+rotationX*f4-rotationXY*f4, f6-rotationZ*f4, f7+rotationYZ*f4-rotationXZ*f4).tex(f1, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
	}

	@Override
	public int getBrightnessForRender(float p_189214_1_)
	{
		int i = super.getBrightnessForRender(p_189214_1_);
		int j = 0;

		if(this.world.isBlockLoaded(this.sourcePos))
		{
			j = this.world.getCombinedLight(this.sourcePos, 0);
		}

		return i==0?j: i;
	}

	@Override
	@Nonnull
	public DrawingStages getDrawStage()
	{
		return DrawingStages.CUSTOM;
	}
}
