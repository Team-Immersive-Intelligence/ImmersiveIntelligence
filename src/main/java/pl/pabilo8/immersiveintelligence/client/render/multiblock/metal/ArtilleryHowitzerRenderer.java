package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.ArtilleryHowitzer;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCasingType;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelBullet;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelArtilleryHowitzer;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.TileEntityArtilleryHowitzer;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBullet;

/**
 * @author Pabilo8
 * @since 21-06-2019
 */
public class ArtilleryHowitzerRenderer extends TileEntitySpecialRenderer<TileEntityArtilleryHowitzer> implements IReloadableModelContainer<ArtilleryHowitzerRenderer>
{
	private static ModelArtilleryHowitzer model;
	private static ModelArtilleryHowitzer modelFlipped;
	private static ModelBullet modelBullet = new ModelBullet();

	private static String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/artillery_howitzer.png";
	private static String textureBullet = ImmersiveIntelligence.MODID+":textures/entity/bullet.png";

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

			ModelArtilleryHowitzer modelCurrent = te.mirrored?modelFlipped: model;
			GlStateManager.pushMatrix();
			modelCurrent.getBlockRotation(te.facing, te.mirrored);
			modelCurrent.render();
			GlStateManager.popMatrix();
			modelCurrent.getBlockRotation(te.facing, false);

			float loadingProgress = 0f;

			switch(te.animation)
			{
				case 1:
				{
					loadingProgress = Math.max((te.animationTime*0.8f)/((float)te.animationTimeMax*0.8f), 0f);
				}
				case 2:
				{
					loadingProgress = 1f-Math.max((te.animationTime*0.8f)/((float)te.animationTimeMax*0.8f), 0f);
				}
			}

			float progress2 = (loadingProgress < 0.5?loadingProgress/0.5f: 1f-((loadingProgress-0.5f)/0.5f));
			GlStateManager.pushMatrix();
			if(te.animation==1||te.animation==2)
			{
				//loadingProgress
				GlStateManager.translate(0f, 0, 2.5f*progress2);
				for(ModelRendererTurbo mod : model.ammo_door)
					mod.render(0.0625f);
				GlStateManager.translate(4.125f, 0.5, -6);

				if(te.animation==1&&loadingProgress > 0.5&&!te.inventory.get(5).isEmpty())
					modelBullet.renderBullet(ItemIIBullet.getCore(te.inventory.get(5)).getColour(), ItemIIBullet.getColour(te.inventory.get(5)));
				else if(te.animation==2&&loadingProgress < 0.5)
				{
					if(te.bullet.getItem() instanceof ItemIIBullet)
						modelBullet.renderBullet(ItemIIBullet.getCore(te.bullet).getColour(), ItemIIBullet.getColour(te.bullet));
					else if(te.bullet.getItem() instanceof IBulletCasingType)
						modelBullet.renderCasing(false, -1, 0);
				}
			}
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();

			GlStateManager.translate(3f, 0f, -9f);

			GlStateManager.pushMatrix();
			GlStateManager.rotate(90, 1f, 0f, 0f);
			GlStateManager.rotate(90, 0f, 0f, 1f);
			GlStateManager.translate(0f, -1f, 0f);


			//te.shellLoadTime/(float)Machines.artilleryHowitzer.conveyorTime

			GlStateManager.pushMatrix();

			boolean[] shell_can_move = new boolean[]{te.inventoryHandler.getStackInSlot(0).isEmpty(), te.inventoryHandler.getStackInSlot(1).isEmpty(), te.inventoryHandler.getStackInSlot(2).isEmpty(), te.inventoryHandler.getStackInSlot(3).isEmpty(), te.inventoryHandler.getStackInSlot(4).isEmpty(), te.inventoryHandler.getStackInSlot(5).isEmpty(), te.inventoryHandler.getStackInSlot(6).isEmpty(), te.inventoryHandler.getStackInSlot(7).isEmpty(), te.inventoryHandler.getStackInSlot(8).isEmpty(), te.inventoryHandler.getStackInSlot(9).isEmpty(), te.inventoryHandler.getStackInSlot(10).isEmpty(), te.inventoryHandler.getStackInSlot(11).isEmpty()};
			boolean is_moved = false;

			GlStateManager.translate(0f, 0f, 1f);

			float prgrs;
			if(te.animation==1)
				prgrs = Math.min((float)te.animationTime/((float)te.animationTimeMax*0.2f), 1f);
			else
				prgrs = te.shellLoadTime/(float)ArtilleryHowitzer.conveyorTime;

			if(!shell_can_move[6])
			{
				GlStateManager.pushMatrix();
				if(te.animation==2)
				{
					GlStateManager.translate(0f, 1f-prgrs, 0f);
					is_moved = true;
				}
				GlStateManager.translate(1f, -2f, -0.875f);
				ItemStack stack = te.inventoryHandler.getStackInSlot(6);
				if(stack.getItem() instanceof ItemIIBullet)
					modelBullet.renderBullet(ItemIIBullet.getCore(stack).getColour(), ItemIIBullet.getColour(stack));
				else
					modelBullet.renderCasing(false, -1, 0);


				GlStateManager.popMatrix();
			}

			if(!shell_can_move[7])
			{
				GlStateManager.pushMatrix();

				if(shell_can_move[8])
					is_moved = true;

				GlStateManager.translate(1f-(0.5f*((is_moved)?prgrs: 0f)), -2f, -0.875f);

				ItemStack stack = te.inventoryHandler.getStackInSlot(7);
				if(stack.getItem() instanceof ItemIIBullet)
					modelBullet.renderBullet(ItemIIBullet.getCore(stack).getColour(), ItemIIBullet.getColour(stack));
				else
					modelBullet.renderCasing(false, -1, 0);

				GlStateManager.popMatrix();
			}

			if(!shell_can_move[8])
			{
				GlStateManager.pushMatrix();

				if(shell_can_move[9])
					is_moved = true;

				GlStateManager.translate((0.5f*((is_moved)?1f-prgrs: 0f)), -2f, -0.875f);
				ItemStack stack = te.inventoryHandler.getStackInSlot(8);
				if(stack.getItem() instanceof ItemIIBullet)
					modelBullet.renderBullet(ItemIIBullet.getCore(stack).getColour(), ItemIIBullet.getColour(stack));
				else
					modelBullet.renderCasing(false, -1, 0);

				GlStateManager.popMatrix();
			}

			if(!shell_can_move[9])
			{
				GlStateManager.pushMatrix();

				if(shell_can_move[10])
					is_moved = true;

				GlStateManager.translate(0f, -2f, -1.385+(0.5f*((is_moved)?1f-prgrs: 0f)));
				ItemStack stack = te.inventoryHandler.getStackInSlot(9);
				if(stack.getItem() instanceof ItemIIBullet)
					modelBullet.renderBullet(ItemIIBullet.getCore(stack).getColour(), ItemIIBullet.getColour(stack));
				else
					modelBullet.renderCasing(false, -1, 0);

				GlStateManager.popMatrix();
			}

			if(!shell_can_move[10])
			{
				GlStateManager.pushMatrix();

				if(shell_can_move[11])
					is_moved = true;

				GlStateManager.translate(0f, -2f, -2.185+(0.5f*((is_moved)?1f-prgrs: 0f)));
				ItemStack stack = te.inventoryHandler.getStackInSlot(10);
				if(stack.getItem() instanceof ItemIIBullet)
					modelBullet.renderBullet(ItemIIBullet.getCore(stack).getColour(), ItemIIBullet.getColour(stack));
				else
					modelBullet.renderCasing(false, -1, 0);

				GlStateManager.popMatrix();
			}

			if(!shell_can_move[11])
			{
				GlStateManager.pushMatrix();

				if(shell_can_move[1])
					is_moved = true;

				GlStateManager.translate(0f, -2f, -2.985-(1.5f*((is_moved)?prgrs: 0f)));
				ItemStack stack = te.inventoryHandler.getStackInSlot(11);
				if(stack.getItem() instanceof ItemIIBullet)
					modelBullet.renderBullet(ItemIIBullet.getCore(stack).getColour(), ItemIIBullet.getColour(stack));
				else
					modelBullet.renderCasing(false, -1, 0);

				GlStateManager.popMatrix();
			}

			//

			if(!shell_can_move[5])
			{
				GlStateManager.pushMatrix();
				if(te.animation==1)
				{
					GlStateManager.translate(0f, -prgrs, 0f);
					is_moved = true;
				}
				GlStateManager.translate(1f, 0f, -0.875f);
				ItemStack stack = te.inventoryHandler.getStackInSlot(5);
				modelBullet.renderBullet(ItemIIBullet.getCore(stack).getColour(), ItemIIBullet.getColour(stack));

				GlStateManager.popMatrix();
			}

			if(!shell_can_move[4])
			{
				GlStateManager.pushMatrix();

				if(shell_can_move[5])
					is_moved = true;

				GlStateManager.translate(0.5f+(0.5f*((is_moved)?prgrs: 0f)), 0f, -0.875f);
				ItemStack stack = te.inventoryHandler.getStackInSlot(4);
				modelBullet.renderBullet(ItemIIBullet.getCore(stack).getColour(), ItemIIBullet.getColour(stack));

				GlStateManager.popMatrix();
			}

			if(!shell_can_move[3])
			{
				GlStateManager.pushMatrix();

				if(shell_can_move[4])
					is_moved = true;

				GlStateManager.translate((0.5f*((is_moved)?prgrs: 0f)), 0f, -0.875f);
				ItemStack stack = te.inventoryHandler.getStackInSlot(3);
				modelBullet.renderBullet(ItemIIBullet.getCore(stack).getColour(), ItemIIBullet.getColour(stack));

				GlStateManager.popMatrix();
			}

			if(!shell_can_move[2])
			{
				GlStateManager.pushMatrix();

				if(shell_can_move[3])
					is_moved = true;

				GlStateManager.translate(0f, 0f, -1.385+(0.5f*((is_moved)?prgrs: 0f)));
				ItemStack stack = te.inventoryHandler.getStackInSlot(2);
				modelBullet.renderBullet(ItemIIBullet.getCore(stack).getColour(), ItemIIBullet.getColour(stack));

				GlStateManager.popMatrix();
			}

			if(!shell_can_move[1])
			{
				GlStateManager.pushMatrix();

				if(shell_can_move[2])
					is_moved = true;

				GlStateManager.translate(0f, 0f, -2.185+(0.5f*((is_moved)?prgrs: 0f)));
				ItemStack stack = te.inventoryHandler.getStackInSlot(1);
				modelBullet.renderBullet(ItemIIBullet.getCore(stack).getColour(), ItemIIBullet.getColour(stack));

				GlStateManager.popMatrix();
			}

			if(!shell_can_move[0])
			{
				GlStateManager.pushMatrix();

				if(shell_can_move[1])
					is_moved = true;

				GlStateManager.translate(0f, 0f, -2.985+(0.5f*((is_moved)?prgrs: 0f)));
				ItemStack stack = te.inventoryHandler.getStackInSlot(0);
				modelBullet.renderBullet(ItemIIBullet.getCore(stack).getColour(), ItemIIBullet.getColour(stack));

				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();

			//GlStateManager.translate(0f,0f,-0.6f);


			GlStateManager.popMatrix();

			ClientUtils.bindTexture("textures/atlas/blocks.png");
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", EnumFacing.SOUTH);
			GlStateManager.translate(0f, 0f, 1f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", EnumFacing.SOUTH);
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
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", EnumFacing.NORTH);
			GlStateManager.translate(0f, 0f, 1f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", EnumFacing.NORTH);

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

			model.getModelCounterRotation(te.facing);

			GlStateManager.translate(72F/16f, platform_height, -72F/16f);

			GlStateManager.rotate(-(te.turretYaw+90), 0f, 1f, 0f);

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

			for(ModelRendererTurbo mod : model.cannon)
				mod.render(0.0625f);

			GlStateManager.pushMatrix();

			if(te.animation==1||te.animation==2)
			{
				GlStateManager.pushMatrix();

				GlStateManager.translate(-0.4075f, -1.15f, 0.65f);
				if(te.animation==1&&loadingProgress < 0.5&&!te.inventory.get(5).isEmpty())
					modelBullet.renderBullet(ItemIIBullet.getCore(te.inventory.get(5)).getColour(), ItemIIBullet.getColour(te.inventory.get(5)));
				else if(te.animation==2&&loadingProgress > 0.5)
				{
					if(te.bullet.getItem() instanceof ItemIIBullet)
						modelBullet.renderBullet(ItemIIBullet.getCore(te.bullet).getColour(), ItemIIBullet.getColour(te.bullet));
					else if(te.bullet.getItem() instanceof IBulletCasingType)
						modelBullet.renderCasing(false, -1, 0);
				}

				GlStateManager.popMatrix();

				GlStateManager.translate(-0.375f, -1f, 0f);
				GlStateManager.rotate(-90*progress2, 1, 0, 0);
				GlStateManager.translate(0.375f, 1f, 0f);
			}
			ClientUtils.bindTexture(texture);
			for(ModelRendererTurbo mod : model.cannon_ammo_door_left)
				mod.render(0.0625f);

			GlStateManager.popMatrix();
			ClientUtils.bindTexture(texture);

			GlStateManager.pushMatrix();

			if(te.animation==1||te.animation==2)
			{
				GlStateManager.translate(-0.375f, -1f, 0f);
				GlStateManager.rotate(-90*progress2, 1, 0, 0);
				GlStateManager.translate(0.375f, 1f, 0f);
			}

			for(ModelRendererTurbo mod : model.cannon_ammo_door_right)
				mod.render(0.0625f);

			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();

			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();

			GlStateManager.translate(0f, -barrel_recoil, 0f);
			for(ModelRendererTurbo mod : model.cannon_barrel)
				mod.render(0.0625f);

			GlStateManager.popMatrix();

			GlStateManager.popMatrix();

		}
		else if(te==null)
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+1.5, (float)y+0.55, (float)z+1.5);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			GlStateManager.scale(0.5, 0.5, 0.5);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			for(ModelRendererTurbo mod : model.cannon_platform)
				mod.render(0.0625f);

			GlStateManager.translate(0f, 1.625f, 0f);
			GlStateManager.rotate(-45, 1f, 0f, 0f);

			for(ModelRendererTurbo mod : model.cannon)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.cannon_barrel)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.cannon_ammo_door_left)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.cannon_ammo_door_right)
				mod.render(0.0625f);

			GlStateManager.popMatrix();

		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelArtilleryHowitzer(false);
		modelFlipped = new ModelArtilleryHowitzer(true);
		for(ModelRendererTurbo[] mod : modelFlipped.parts.values())
		{

			for(ModelRendererTurbo m : mod)
			{
				if(!m.field_1402_i)
				{
					m.doMirror(true, false, false);
					m.setRotationPoint(-m.rotationPointX, m.rotationPointY, m.rotationPointZ);
					m.rotateAngleY *= -1;
					m.rotateAngleZ *= -1;
				}
				else
				{
					m.rotationPointX -= 144f;
					m.field_1402_i = false;
				}
			}

		}
	}
}
