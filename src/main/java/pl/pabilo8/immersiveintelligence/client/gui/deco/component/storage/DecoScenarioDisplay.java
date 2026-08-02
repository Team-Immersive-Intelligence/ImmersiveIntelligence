package pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoSprite;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 18.08.2025
 */
public class DecoScenarioDisplay extends DecoComponent<DecoScenarioDisplay>
{
	private AMTModel scene;

	//--- View settings ---//
	private float scale = 1;
	private float rotationPitch = 0, rotationYaw = 0;
	private float pitchRotationTicks = 0, yawRotationTicks = 0;
	private Vec3d origin = Vec3d.ZERO, translation = Vec3d.ZERO;

	//--- Interaction state ---//
	private boolean interactionAllowed = true, zoomAllowed = false;
	private int lastMouseX;
	private int lastMouseY;

	//Optional background texture
	@Nullable
	private DecoSprite background;
	@Nullable
	private IIColor backgroundColor;

	//Lighting settings
	private boolean useStandardGUILighting = true;
	private int lightColor = 0xffffff;

	public DecoScenarioDisplay(int x, int y)
	{
		super(x, y);
		withOnScroll(this::handleMouseScroll);
		withOnPressed(this::handleMousePress);
		withOnDragged(this::handleMouseDrag);
	}

	@Override
	protected boolean initialize()
	{
		return true;
	}

	/**
	 * Sets the model to display
	 */
	public DecoScenarioDisplay withModel(boolean join, AMTModel model)
	{
		this.scene = join&&this.scene!=null?new AMTModel(scene, model): model;
		this.initialized = false;
		return this;
	}

	/**
	 * Sets the model to display
	 */
	public DecoScenarioDisplay withModel(boolean join, AMT... model)
	{
		return this.withModel(join, new AMTModel(model));
	}

	/**
	 * Sets whether the model can be rotated using mouse
	 */
	public DecoScenarioDisplay withInteractionAllowed(boolean interactionAllowed)
	{
		this.interactionAllowed = interactionAllowed;
		return this;
	}

	/**
	 * Sets whether zooming using mouse scroll is allowed
	 */
	public DecoScenarioDisplay withZoomAllowed(boolean zoomAllowed)
	{
		this.zoomAllowed = zoomAllowed;
		return this;
	}

	/**
	 * Sets the initial rotation of the model
	 */
	public DecoScenarioDisplay withRotation(float rotX, float rotY)
	{
		this.rotationPitch = rotX;
		this.rotationYaw = rotY;
		return this;
	}

	/**
	 * Sets the initial scale of the model
	 */
	public DecoScenarioDisplay withScale(float scale)
	{
		this.scale = scale;
		return this;
	}

	/**
	 * Sets the origin point for rotation
	 */
	public DecoScenarioDisplay withOrigin(double x, double y, double z)
	{
		this.origin = new Vec3d(x, y, z);
		return this;
	}

	/**
	 * Sets the translation of the model
	 */
	public DecoScenarioDisplay withTranslation(double x, double y, double z)
	{
		this.translation = new Vec3d(x, y, z);
		return this;
	}

	/**
	 * Sets a background texture for the scenario display
	 */
	public DecoScenarioDisplay withBackground(@Nullable DecoSprite background)
	{
		this.background = background;
		return this;
	}

	/**
	 * Sets a background color for the scenario display
	 */
	public DecoScenarioDisplay withBackgroundColor(@Nullable IIColor backgroundColor)
	{
		this.backgroundColor = backgroundColor;
		return this;
	}

	/**
	 * Whether to use standard GUI lighting
	 */
	public DecoScenarioDisplay withUseStandardGUILighting(boolean useStandardLighting)
	{
		this.useStandardGUILighting = useStandardLighting;
		return this;
	}

	/**
	 * Sets the light color for the model
	 */
	public DecoScenarioDisplay withLightColor(int lightColor)
	{
		this.lightColor = lightColor;
		return this;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		bindAtlas();
		GlStateManager.enableBlend();
		//Draw textured background if set
		if(background!=null)
		{
			IIDrawUtils draw = IIDrawUtils.startTexturedColored();
			draw.drawConnectedTexColorRect(x, y, width, height, backgroundColor==null?IIColor.WHITE: backgroundColor,
					background.getSizeX(), background.getSizeY(), background.getSizeX()/4, background.getSizeY()/4,
					background.getMapUV());
			draw.finish();
		}
		//Draw color only
		else if(backgroundColor!=null)
		{
			//Disable textures
			GlStateManager.disableTexture2D();
			GlStateManager.disableAlpha();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
			//Draw color background
			IIDrawUtils.startColored()
					.drawColorRect(x, y, width, height, backgroundColor)
					.finish();
			//Re-enable textures
			GlStateManager.enableTexture2D();
			GlStateManager.shadeModel(GL11.GL_FLAT);
			GlStateManager.disableBlend();
			GlStateManager.enableAlpha();
		}

		if(scene==null)
			return;

		//Set up OpenGL state for rendering 3D model
		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableDepth();
		GlStateManager.enableBlend();
		GlStateManager.translate(x+((float)width/2), y+((float)height/2), 100);
		float scale = this.scale*Math.min(width, height);
		GlStateManager.scale(-scale, -scale, -scale);

		//Apply rotation and translation
		GlStateManager.translate(translation.x, translation.y, translation.z);

		GlStateManager.translate(origin.x, origin.y, origin.z);
		GlStateManager.rotate(rotationPitch, 1, 0, 0);
		if(pitchRotationTicks > 0)
			GlStateManager.rotate((float)(AMTUtils.getDebugProgress(pitchRotationTicks, partialTicks)*360d), 1, 0, 0);
		GlStateManager.rotate(rotationYaw, 0, 1, 0);
		if(yawRotationTicks > 0)
			GlStateManager.rotate((float)(AMTUtils.getDebugProgress(yawRotationTicks, partialTicks)*360d), 0, 1, 0);

		GlStateManager.translate(-origin.x, -origin.y, -origin.z);

		//Set up lighting
		RenderHelper.enableStandardItemLighting();

		//Render the model
		Tessellator tes = Tessellator.getInstance();
		scene.render(tes, tes.getBuffer());

		//Restore OpenGL state
		RenderHelper.disableStandardItemLighting();

		GlStateManager.disableRescaleNormal();
		GlStateManager.disableDepth();
		GlStateManager.popMatrix();
	}

	private boolean handleMouseScroll(DecoScenarioDisplay gui, int mouseScroll, int mouseX, int mouseY)
	{
		if(zoomAllowed)
			scale = MathHelper.clamp(scale+mouseScroll*0.05f, 0.5f, 2f);
		return false;
	}

	private boolean handleMouseDrag(DecoScenarioDisplay gui, MouseButton mouseButton, int mouseX, int mouseY)
	{
		if(this.interactionAllowed)
		{
			this.rotationYaw += mouseX-lastMouseX;
			this.rotationPitch += mouseY-lastMouseY;

			lastMouseX = mouseX;
			lastMouseY = mouseY;
			return true;
		}
		return false;
	}

	private boolean handleMousePress(DecoScenarioDisplay gui, MouseButton mouseButton, int mouseX, int mouseY)
	{
		return false;
	}

	@Override
	public void cleanup()
	{
		if(scene!=null)
			scene.disposeOf();
	}

	public DecoScenarioDisplay withRotationAnimation(int yawRotationTicks, int pitchRotationTicks)
	{
		this.interactionAllowed = false;
		this.yawRotationTicks = yawRotationTicks;
		this.pitchRotationTicks = pitchRotationTicks;
		return this;
	}
}
