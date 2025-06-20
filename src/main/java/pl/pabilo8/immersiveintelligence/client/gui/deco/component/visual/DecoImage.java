package pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.GuiComponentDecoBase;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A class for displaying a single image in the Deco GUI system
 * with a default width and height of 16x16.
 *
 * @since 10.01.2025
 */
public class DecoImage extends GuiComponentDecoBase<DecoImage>
{
	@Nullable
	private ResourceLocation imageLocation;
	private boolean usesBlockAtlas;
	private TextureAtlasSprite sprite;
	private float[] uv;

	@Nullable
	private ImageAnimationDirection animationDirection;
	/**
	 * Calculates animation progress, passed partial ticks.
	 */
	private Function<Float, Float> animationProgress;

	public DecoImage(int x, int y)
	{
		super(x, y);
	}

	/**
	 * Sets the image location for this DecoImage, uses block atlas by default.
	 *
	 * @param imageLocation the resource location of the image, a null value indicates no image
	 * @return this DecoImage instance for method chaining
	 */
	public DecoImage withImageLocation(@Nullable ResourceLocation imageLocation)
	{
		return withImageLocation(imageLocation, false);
	}

	/**
	 * Sets the image location and whether it uses the block atlas.
	 *
	 * @param imageLocation  the resource location of the image, a null value indicates no image
	 * @param usesBlockAtlas if true, the image will be sourced from the block atlas,
	 * @return this DecoImage instance for method chaining
	 */
	public DecoImage withImageLocation(@Nullable ResourceLocation imageLocation, boolean usesBlockAtlas)
	{
		this.imageLocation = imageLocation;
		this.usesBlockAtlas = usesBlockAtlas;
		return this.withUV(16, 0, 0, 16, 16);
	}

	/**
	 * Sets the UV coordinates for the image.
	 *
	 * @param textureSize the size of the texture atlas, used to scale the UV coordinates
	 * @param u           the starting U coordinate (x-axis)
	 * @param v           the starting V coordinate (y-axis)
	 * @param uu          the ending U coordinate (x-axis)
	 * @param vv          the ending V coordinate (y-axis)
	 * @return this DecoImage instance for method chaining
	 */
	public DecoImage withUV(float textureSize, float u, float v, float uu, float vv)
	{
		this.uv = new float[4];
		if(usesBlockAtlas)
		{
			assert imageLocation!=null;
			this.sprite = ClientUtils.getSprite(imageLocation);
			this.uv[0] = sprite.getInterpolatedU(u*16f/textureSize);
			this.uv[1] = sprite.getInterpolatedU(uu*16f/textureSize);
			this.uv[2] = sprite.getInterpolatedV(v*16f/textureSize);
			this.uv[3] = sprite.getInterpolatedV(vv*16f/textureSize);
		}
		else
		{
			this.uv[0] = u/textureSize;
			this.uv[1] = uu/textureSize;
			this.uv[2] = v/textureSize;
			this.uv[3] = vv/textureSize;
		}

		return this;
	}

	/**
	 * Sets the animation direction and progress function for this DecoImage.
	 *
	 * @param direction         the direction of the animation, can be null for no animation
	 * @param animationProgress a function that takes the partial ticks and returns a float value
	 * @return this DecoImage instance for method chaining
	 */
	public DecoImage withAnimation(ImageAnimationDirection direction, Function<Float, Float> animationProgress)
	{
		this.animationDirection = direction;
		this.animationProgress = animationProgress;

		return this;
	}

	@Override
	protected boolean initialize()
	{
		return uv!=null;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		//Draw the image
		GlStateManager.enableBlend();
		IIDrawUtils draw = IIDrawUtils.startTexturedColored();

		//Use the block atlas if specified, otherwise use the texture directly, like older GUIs do
		if(usesBlockAtlas)
			bindAtlas();
		else
			IIClientUtils.bindTexture(imageLocation);

		//Draw the image with the specified UV coordinates
		if(animationDirection==null)
			draw.drawTexColorRect(x, y, width, height, IIColor.WHITE, uv);
		else
		{
			float[] drawnUV = animationDirection.animate(uv, animationProgress.apply(partialTicks));
			float[] drawnDimensions = animationDirection.animate(new float[]{0, width, 0, height}, animationProgress.apply(partialTicks));
			draw.drawTexColorRect(
					x+drawnDimensions[0], y+drawnDimensions[2], drawnDimensions[1], drawnDimensions[3],
					IIColor.WHITE,
					drawnUV[0], drawnUV[1], drawnUV[2], drawnUV[3]
			);
		}

		draw.finish();
		GlStateManager.disableBlend();
	}

	@Override
	public void cleanup()
	{

	}

	/**
	 * Enum representing the direction of image animation, provides the uv calculation based on the progress.
	 */
	public enum ImageAnimationDirection
	{
		LEFT_TO_RIGHT(
				(uv, progress) -> new float[]{
						uv[0],
						uv[0]+(uv[1]-uv[0])*progress,
						uv[2],
						uv[3]
				}
		),
		RIGHT_TO_LEFT(
				(uv, progress) -> new float[]{
						uv[1]-(uv[1]-uv[0])*(1-progress),
						uv[1],
						uv[2],
						uv[3]
				}
		);

		/**
		 * Function to animate the UV coordinates based on the progress.
		 * The progress is a float value between 0 and 1, where 0 is the start and 1 is the end of the animation.
		 */
		private final BiFunction<float[], Float, float[]> animateFunction;

		ImageAnimationDirection(BiFunction<float[], Float, float[]> animateFunction)
		{
			this.animateFunction = animateFunction;
		}

		/**
		 * Animates coordinates (dimensions, UV) based on the progress.
		 *
		 * @param uv       the original coordinates
		 * @param progress the progress of the animation, a float value between 0 and 1
		 * @return the animated UV coordinates
		 */
		public float[] animate(float[] uv, float progress)
		{
			return animateFunction.apply(uv, progress);
		}
	}
}