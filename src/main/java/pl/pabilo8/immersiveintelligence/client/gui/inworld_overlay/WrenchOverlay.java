package pl.pabilo8.immersiveintelligence.client.gui.inworld_overlay;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.utils.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 29.05.2023
 */
public class WrenchOverlay extends InWorldOverlayBase
{
	@Override
	public void draw(EntityPlayer player, World world, RayTraceResult mouseOver, float partialTicks)
	{
		ItemStack stack = player.getHeldItemMainhand();
		if(!stack.getItem().getToolClasses(stack).contains(IIReference.TOOL_WRENCH))
			return;

		AxisAlignedBB full = new AxisAlignedBB(player.getPosition()).grow(16);
		ArrayList<AxisAlignedBB> aabb = new ArrayList<>();

		//get rendering centre position
		double posX = player.lastTickPosX+(player.posX-player.lastTickPosX)*(double)partialTicks;
		double posY = player.lastTickPosY+(player.posY-player.lastTickPosY)*(double)partialTicks;
		double posZ = player.lastTickPosZ+(player.posZ-player.lastTickPosZ)*(double)partialTicks;

		BlockPos base = player.getPosition();

		//Scan for tile positions
		for(int x = -16; x <= 16; x++)
			for(int z = -16; z <= 16; z++)
				for(int y = -16; y <= 16; y++)
				{
					BlockPos pos = base.add(x, y, z);
					TileEntity te = world.getTileEntity(pos);

					if(!(te instanceof IUpgradableMachine))
						continue;
					IUpgradableMachine machine = (IUpgradableMachine)te;
					if(machine.getUpgradeMaster().getCurrentlyInstalled()!=null)
						continue;

					//TODO: 29.05.2023 check
					world.getBlockState(pos).addCollisionBoxToList(world, pos, full, aabb, player, true);

				}

		//Start Draw
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buf = tessellator.getBuffer();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		buf.begin(7, DefaultVertexFormats.POSITION_COLOR);
		buf.setTranslation(-posX, -posY, -posZ);

		final float[] rgb = IIReference.COLOR_IMMERSIVE_ORANGE.getFloatRGB();
		final float alpha = 0.25f;

		//Draw
		for(AxisAlignedBB box : aabb)
		{
			box = box.grow(0.0625/4);
			//TODO: 30.05.2023 Draw Utils?
			buf.pos(box.minX, box.maxY, box.minZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
			buf.pos(box.maxX, box.maxY, box.minZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
			buf.pos(box.maxX, box.minY, box.minZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
			buf.pos(box.minX, box.minY, box.minZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
			buf.pos(box.minX, box.minY, box.maxZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
			buf.pos(box.maxX, box.minY, box.maxZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
			buf.pos(box.maxX, box.maxY, box.maxZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
			buf.pos(box.minX, box.maxY, box.maxZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
			buf.pos(box.minX, box.minY, box.minZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
			buf.pos(box.maxX, box.minY, box.minZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
			buf.pos(box.maxX, box.minY, box.maxZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
			buf.pos(box.minX, box.minY, box.maxZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
			buf.pos(box.minX, box.maxY, box.maxZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
			buf.pos(box.maxX, box.maxY, box.maxZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
			buf.pos(box.maxX, box.maxY, box.minZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
			buf.pos(box.minX, box.maxY, box.minZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
			buf.pos(box.minX, box.minY, box.maxZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
			buf.pos(box.minX, box.maxY, box.maxZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
			buf.pos(box.minX, box.maxY, box.minZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
			buf.pos(box.minX, box.minY, box.minZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
			buf.pos(box.maxX, box.minY, box.minZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
			buf.pos(box.maxX, box.maxY, box.minZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
			buf.pos(box.maxX, box.maxY, box.maxZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
			buf.pos(box.maxX, box.minY, box.maxZ).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
		}

		//Finish Draw
		buf.setTranslation(0, 0, 0);
		tessellator.draw();
		GlStateManager.enableTexture2D();
	}
}
