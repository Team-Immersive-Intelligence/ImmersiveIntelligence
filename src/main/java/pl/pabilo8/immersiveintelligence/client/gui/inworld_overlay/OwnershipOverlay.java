package pl.pabilo8.immersiveintelligence.client.gui.inworld_overlay;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.IOwnableProperty;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 29.05.2023
 */
public class OwnershipOverlay extends InWorldOverlayBase
{
	@Override
	public void draw(EntityPlayer player, World world, RayTraceResult mouseOver, float partialTicks)
	{


		if(mouseOver.typeOfHit!=Type.BLOCK)
			return;
		TileEntity te = world.getTileEntity(mouseOver.getBlockPos());
		if(!(te instanceof IOwnableProperty))
			return;


		//get rendering centre position
		double posX = player.lastTickPosX+(player.posX-player.lastTickPosX)*(double)partialTicks;
		double posY = player.lastTickPosY+(player.posY-player.lastTickPosY)*(double)partialTicks;
		double posZ = player.lastTickPosZ+(player.posZ-player.lastTickPosZ)*(double)partialTicks;

		//Start Draw
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buf = tessellator.getBuffer();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		buf.begin(7, DefaultVertexFormats.POSITION_COLOR);
		buf.setTranslation(-posX, -posY, -posZ);

		//Draw chunks


		//Finish Draw
		buf.setTranslation(0, 0, 0);
		tessellator.draw();
		GlStateManager.enableTexture2D();
	}
}
