package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelArtilleryHowitzer;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.TileEntityArtilleryHowitzer;

/**
 * Created by Pabilo8 on 21-06-2019.
 */
public class ArtilleryHowitzerRenderer extends TileEntitySpecialRenderer<TileEntityArtilleryHowitzer>
{
	private static ModelArtilleryHowitzer model = new ModelArtilleryHowitzer();

	private static String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/artillery_howitzer.png";

	@Override
	public void render(TileEntityArtilleryHowitzer te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+4, (float)y-5, (float)z-1);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 1f, 1f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
			}

			model.getBlockRotation(te.facing, model);
			model.render();

			float loadingProgress = 0f;

			switch(te.animation)
			{
				case 1:
				{
					loadingProgress = te.animationTime/(float)te.animationTimeMax;
				}
				case 2:
				{
					loadingProgress = 1f-(te.animationTime/(float)te.animationTimeMax);
				}
			}

			GlStateManager.pushMatrix();

			ClientUtils.bindTexture("textures/atlas/blocks.png");
			GlStateManager.translate(3f, 0f, -9f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", te.facing.getOpposite());
			GlStateManager.translate(0f, 0f, 1f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", te.facing.getOpposite());
			GlStateManager.rotate(180, 0, 1, 0);
			GlStateManager.translate(-1f, 0f, -0.125f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:vertical", EnumFacing.DOWN);
			GlStateManager.translate(0f, 1f, 0f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:vertical", EnumFacing.DOWN);
			GlStateManager.translate(0f, 1f, 0f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:vertical", EnumFacing.DOWN);
			GlStateManager.translate(0f, 1f, 0f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:vertical", EnumFacing.DOWN);

			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();

			ClientUtils.bindTexture("textures/atlas/blocks.png");
			GlStateManager.translate(5f, 0f, -9f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", te.facing);
			GlStateManager.translate(0f, 0f, 1f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", te.facing);

			GlStateManager.translate(0f, 0f, -0.875f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:vertical", EnumFacing.UP);
			GlStateManager.translate(0f, 1f, 0f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:vertical", EnumFacing.UP);
			GlStateManager.translate(0f, 1f, 0f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:vertical", EnumFacing.UP);
			GlStateManager.translate(0f, 1f, 0f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:vertical", EnumFacing.UP);

			GlStateManager.popMatrix();

			ClientUtils.bindTexture(texture);

			float platform_height = te.platformHeight;
			float barrel_recoil = 0f;
			if(te.animation==3)
			{
				platform_height = Math.min(5.25f, te.platformHeight+(partialTicks/20f));
				float afloat = (float)te.animationTime/((float)te.animationTimeMax);
				barrel_recoil = afloat <= 0.25f?(afloat/0.25f): 1f-((afloat-0.25f)/0.75f);
			}


			GlStateManager.pushMatrix();

			GlStateManager.translate(0f, 5f, 0f);
			GlStateManager.pushMatrix();
			GlStateManager.translate(1.625f, 0f, -1.625f);
			GlStateManager.rotate(te.doorAngle, 1f, 0f, 0f);
			model.hatch[0].render(0.0625f);

			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.translate(1.625f, 0.125f, -7.375f);

			GlStateManager.rotate(-te.doorAngle, 1f, 0f, 0f);
			model.hatch[1].render(0.0625f);

			GlStateManager.popMatrix();

			GlStateManager.popMatrix();

			GlStateManager.translate(72F/16f, platform_height, -72F/16f);

			GlStateManager.rotate(-te.turretYaw, 0f, 1f, 0f);

			for(ModelRendererTurbo mod : model.cannon_platform)
				mod.render(0.0625f);


			//Rods
			GlStateManager.pushMatrix();

			GlStateManager.translate(0f, 1f, 0f);

			for(int i = 0; i <= platform_height; i += 1)
			{
				GlStateManager.translate(0f, -1f, 0f);
				model.platform_rod[0].render(0.0625f);
			}

			GlStateManager.popMatrix();

			GlStateManager.translate(0f, 1.625f, 0f);
			GlStateManager.rotate(te.turretPitch, 1f, 0f, 0f);

			if(Keyboard.isKeyDown(Keyboard.KEY_I)&&getWorld().getTotalWorldTime()%20==0)
				model = new ModelArtilleryHowitzer();

			for(ModelRendererTurbo mod : model.cannon)
				mod.render(0.0625f);

			for(ModelRendererTurbo mod : model.cannon_ammo_door_left)
				mod.render(0.0625f);

			for(ModelRendererTurbo mod : model.cannon_ammo_door_right)
				mod.render(0.0625f);

			GlStateManager.pushMatrix();

			GlStateManager.translate(0f, -barrel_recoil, 0f);
			for(ModelRendererTurbo mod : model.cannon_barrel)
				mod.render(0.0625f);

			GlStateManager.popMatrix();

			if(te.animation==1||te.animation==2)
			{
				//loadingProgress
				for(ModelRendererTurbo mod : model.ammo_door)
					mod.render(0.0625f);
			}


			GlStateManager.popMatrix();

		}
	}
}
