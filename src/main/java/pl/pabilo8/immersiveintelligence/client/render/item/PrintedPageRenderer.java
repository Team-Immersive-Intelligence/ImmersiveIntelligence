package pl.pabilo8.immersiveintelligence.client.render.item;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.client.gui.item.GuiPrintedPage;

/**
 * @author Pabilo8
 * @since 30.09.2022
 */
public class PrintedPageRenderer
{
	private static ItemStack cachedStack;
	private static GuiPrintedPage cachedGui;

	private static void renderItem(ItemStack stack, EnumHand hand)
	{
		if(cachedStack==null||stack.getTagCompound()!=cachedStack.getTagCompound())
		{
			cachedStack = stack;
			cachedGui = new GuiPrintedPage(ClientUtils.mc().player, stack, hand);
		}

		final float scale = 0.25F;

		GlStateManager.pushMatrix();
		GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.scale(scale, scale, scale);
		GlStateManager.disableLighting();
		GlStateManager.translate(-0.5F, -0.5F, 0.0F);
		GlStateManager.scale(0.0078125F, 0.0078125F, 0.0078125F);

		GlStateManager.disableDepth();
		cachedGui.drawScreen(0, 0, 0f);

		GlStateManager.popMatrix();
	}

	public static void renderItemFirstPerson(ItemStack stack, EnumHand hand, float equipProgress, float swingProgress, float pitch)
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		GlStateManager.pushMatrix();
		if(hand==EnumHand.MAIN_HAND&&player.getHeldItemOffhand().isEmpty())
			renderItemFirstPersonCenter(stack, equipProgress, swingProgress, pitch);
		else
			renderItemFirstPersonSide(
					stack, hand==EnumHand.MAIN_HAND?player.getPrimaryHand(): player.getPrimaryHand().opposite(), swingProgress, equipProgress
			);
		GlStateManager.popMatrix();
	}

	/**
	 * Renders the item to one side of the player.
	 *
	 * @param stack         The stack to render
	 * @param side          The side to render on
	 * @param swingProgress The swing progress of this item
	 * @param equipProgress The equip progress of this item
	 */
	private static void renderItemFirstPersonSide(ItemStack stack, EnumHandSide side, float swingProgress, float equipProgress)
	{
		float offset = side==EnumHandSide.RIGHT?1f: -1f;
		GlStateManager.translate(offset*0.125f, -0.125f, 0f);

		// TODO: 08.10.2022 render arms

		// Setup the appropriate transformations. This is just copied from the
		// corresponding method in ItemRenderer.
		GlStateManager.pushMatrix();
		GlStateManager.translate(offset*0.51f, -0.08f+equipProgress*-1.2f, -0.75f);
		float f1 = MathHelper.sqrt(swingProgress);
		float f2 = MathHelper.sin(f1*(float)Math.PI);
		float f3 = -0.5f*f2;
		float f4 = 0.4f*MathHelper.sin(f1*((float)Math.PI*2f));
		float f5 = -0.3f*MathHelper.sin(swingProgress*(float)Math.PI);
		GlStateManager.translate(offset*f3, f4-0.3f*f2, f5);
		GlStateManager.rotate(f2*-45f, 1f, 0f, 0f);
		GlStateManager.rotate(offset*f2*-30f, 0f, 1f, 0f);

		renderItem(stack, EnumHand.OFF_HAND);

		GlStateManager.popMatrix();
	}

	/**
	 * Render an item in the middle of the screen.
	 *
	 * @param stack         The stack to render
	 * @param equipProgress The equip progress of this item
	 * @param swingProgress The swing progress of this item
	 * @param pitch         The pitch of the player
	 */
	private static void renderItemFirstPersonCenter(ItemStack stack, float equipProgress, float swingProgress, float pitch)
	{
		// Setup the appropriate transformations. This is just copied from the
		// corresponding method in ItemRenderer.
		float swingRt = MathHelper.sqrt(swingProgress);
		float tX = -0.2f*MathHelper.sin(swingProgress*(float)Math.PI);
		float tZ = -0.4f*MathHelper.sin(swingRt*(float)Math.PI);
		GlStateManager.translate(0f, -tX/2f, tZ);
		float pitchAngle = getMapAngleFromPitch(pitch);
		GlStateManager.translate(0f, 0.04f+equipProgress*-1.2f+pitchAngle*-0.5f, -0.72f);
		GlStateManager.rotate(pitchAngle*-85f, 1f, 0f, 0f);
		renderArms();
		float rX = MathHelper.sin(swingRt*(float)Math.PI);
		GlStateManager.rotate(rX*20f, 1f, 0f, 0f);
		GlStateManager.scale(2f, 2f, 2f);

		renderItem(stack, EnumHand.MAIN_HAND);
	}

	/**
	 * Return the angle to render the Map
	 */
	private static float getMapAngleFromPitch(float pitch)
	{
		float f = 1.0F-pitch/45.0F+0.1F;
		f = MathHelper.clamp(f, 0.0F, 1.0F);
		f = -MathHelper.cos(f*(float)Math.PI)*0.5F+0.5F;
		return f;
	}

	private static void renderArms()
	{
		if(!ClientUtils.mc().player.isInvisible())
		{
			GlStateManager.disableCull();
			GlStateManager.pushMatrix();
			GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
			renderArm(EnumHandSide.RIGHT);
			renderArm(EnumHandSide.LEFT);
			GlStateManager.popMatrix();
			GlStateManager.enableCull();
		}
	}

	private static void renderArm(EnumHandSide hand)
	{
		Minecraft mc = ClientUtils.mc();
		EntityPlayerSP player = mc.player;


		mc.getTextureManager().bindTexture(player.getLocationSkin());
		Render<AbstractClientPlayer> render = mc.getRenderManager().getEntityRenderObject(player);
		RenderPlayer renderplayer = (RenderPlayer)render;
		assert renderplayer!=null;

		GlStateManager.pushMatrix();
		float f = hand==EnumHandSide.RIGHT?1.0F: -1.0F;
		GlStateManager.rotate(92.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(f*-41.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.translate(f*0.3F, -1.1F, 0.45F);

		if(hand==EnumHandSide.RIGHT)
			renderplayer.renderRightArm(player);
		else
			renderplayer.renderLeftArm(player);

		GlStateManager.popMatrix();
	}
}
