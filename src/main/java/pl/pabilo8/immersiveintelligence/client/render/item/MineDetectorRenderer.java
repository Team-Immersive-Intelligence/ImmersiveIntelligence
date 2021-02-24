package pl.pabilo8.immersiveintelligence.client.render.item;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.item.ModelMineDetector;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 28.01.2021
 */
public class MineDetectorRenderer extends TileEntityItemStackRenderer implements IReloadableModelContainer<MineDetectorRenderer>
{
	public static MineDetectorRenderer instance = new MineDetectorRenderer().subscribeToList("mine_detector");
	@SideOnly(Side.CLIENT)
	private static ModelMineDetector model;
	private static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/items/tools/mine_detector.png";

	@Override
	public void renderByItem(ItemStack itemStackIn, float partialTicks)
	{
		GlStateManager.pushMatrix();

		ClientUtils.bindTexture(TEXTURE);
		for(ModelRendererTurbo mod : model.baseModel)
			mod.render();
		for(ModelRendererTurbo mod : model.poleModel)
		{
			mod.rotateAngleZ=-0.95993109F;
			mod.render();
		}

		GlStateManager.popMatrix();
	}

	@Override
	public void reloadModels()
	{
		model = new ModelMineDetector();
	}

	public void renderBase(EntityLivingBase player, float distance, boolean flipY)
	{
		GlStateManager.pushMatrix();
		int i = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
		ClientUtils.bindTexture(TEXTURE);

		if(flipY)
		{
			distance*=2f;

			float v = 0.5f;
			RayTraceResult traceResult = player.rayTrace(distance, 0);
			if(traceResult!=null&&traceResult.typeOfHit!=null)
			{
				v= (float)traceResult.hitVec.subtract(player.getPositionVector()).distanceTo(Vec3d.ZERO);
				if(player.world.getBlockState(traceResult.getBlockPos()).equals(Blocks.AIR.getDefaultState()))
					v=0.5f;
			}
			v= (float)MathHelper.clamp(v,0.5,distance*0.5f);

			//GlStateManager.rotate(player.cameraPitch,1,0,0);
			GlStateManager.translate(0,1,-v);
			GlStateManager.rotate(90,0,1,0);
			GlStateManager.scale(-1,-1,1);
			//GlStateManager.rotate(player.rotationYawHead,0,1,0);
			for(ModelRendererTurbo mod : model.baseModel)
				mod.render();

			float angle = -0.125F-(1.9f*Math.max((v*6)/distance,1f));

			for(ModelRendererTurbo mod : model.poleModel)
			{
				mod.rotateAngleZ = angle;
				mod.render();
			}
		}
		else
		{
			GlStateManager.translate(0, -11/16f, -1.25+2/16f);
			GlStateManager.rotate(2f, 1, 0, 0);
			GlStateManager.rotate(8.5f, 0, 1, 0);
			for(ModelRendererTurbo mod : model.baseModel)
				mod.render();
		}

		GlStateManager.bindTexture(i);
		GlStateManager.popMatrix();
	}
}
