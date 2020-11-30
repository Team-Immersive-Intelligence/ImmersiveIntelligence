/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package pl.pabilo8.immersiveintelligence.client.render;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Weapons.Machinegun;
import pl.pabilo8.immersiveintelligence.CustomSkinHandler;
import pl.pabilo8.immersiveintelligence.CustomSkinHandler.SpecialSkin;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.weapon.ModelMachinegun;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.tmt.TmtNamedBoxGroup;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMachinegun;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class MachinegunRenderer extends Render<EntityMachinegun>
{
	public static final String texture = "machinegun.png";
	public static ModelMachinegun model = new ModelMachinegun();
	public static HashMap<Predicate<ItemStack>, BiConsumer<ItemStack, List<TmtNamedBoxGroup>>> upgrades = new HashMap<>();
	public static List<TmtNamedBoxGroup> defaultGunParts = new ArrayList<>();
	public static List<TmtNamedBoxGroup> skinParts = new ArrayList<>();

	public MachinegunRenderer(RenderManager renderManager)
	{
		super(renderManager);
		defaultGunParts.add(model.baseBox);
		defaultGunParts.add(model.barrelBox);
		defaultGunParts.add(model.sightsBox);
		defaultGunParts.add(model.triggerBox);
		defaultGunParts.add(model.ammoBox);
		defaultGunParts.add(model.slideBox);
		defaultGunParts.add(model.gripBox);
		defaultGunParts.add(model.bipodBox);

		skinParts.add(model.baubleBox);
	}

	public static void renderMachinegun(ItemStack stack, @Nullable EntityMachinegun entity)
	{
		GlStateManager.pushMatrix();
		List<TmtNamedBoxGroup> renderParts = new ArrayList<>(defaultGunParts);
		boolean drawText = false;

		String skin = CommonProxy.item_machinegun.getSkinnableCurrentSkin(stack);
		if(!skin.isEmpty())
		{
			SpecialSkin s = CustomSkinHandler.specialSkins.get(skin);
			if(s!=null)
			{
				if(s.mods.contains("skin_mg_text"))
					drawText = true;
				skinParts.forEach(tmtNamedBoxGroup -> {
					if(s.mods.contains(tmtNamedBoxGroup.getName()))
						renderParts.add(tmtNamedBoxGroup);
				});
			}

		}
		skin = (skin.isEmpty()?CommonProxy.item_machinegun.getSkinnableDefaultTextureLocation(): CommonProxy.SKIN_LOCATION+skin+"/");


		ClientUtils.bindTexture(skin+texture);


		for(Entry<Predicate<ItemStack>, BiConsumer<ItemStack, List<TmtNamedBoxGroup>>> s : upgrades.entrySet())
		{
			if(s.getKey()!=null&&s.getValue()!=null&&s.getKey().test(stack))
				s.getValue().accept(stack, renderParts);
		}

		if(entity!=null)
		{
			float yaw = entity.gunYaw, pitch = entity.gunPitch;
			if(entity.setupTime < 1&&entity.getPassengers().size() > 0&&entity.getPassengers().get(0) instanceof EntityLivingBase)
			{
				EntityLivingBase psg = (EntityLivingBase)entity.getPassengers().get(0);

				float true_head_angle = MathHelper.wrapDegrees(psg.prevRotationYawHead-entity.setYaw);
				float true_head_angle2 = MathHelper.wrapDegrees(psg.rotationPitch);

				if(entity.gunYaw < true_head_angle)
					yaw += ClientUtils.mc().getRenderPartialTicks()*2f;
				else if(entity.gunYaw > true_head_angle)
					yaw -= ClientUtils.mc().getRenderPartialTicks()*2f;

				if(Math.ceil(entity.gunYaw) <= Math.ceil(true_head_angle)+1f&&Math.ceil(entity.gunYaw) >= Math.ceil(true_head_angle)-1f)
					yaw = true_head_angle;

				if(entity.gunPitch < true_head_angle2)
					pitch += ClientUtils.mc().getRenderPartialTicks();
				else if(entity.gunPitch > true_head_angle2)
					pitch -= ClientUtils.mc().getRenderPartialTicks();

				yaw = MathHelper.clamp(yaw, -45, 45);
				pitch = MathHelper.clamp(pitch, -20, 20);

				yaw += entity.recoilYaw;
				pitch += entity.recoilPitch;

				pitch = MathHelper.clamp(pitch, -20, 20);
			}

			GlStateManager.translate(0f, -0.34375, 0f);
			float setup = Math.max(entity.setupTime-ClientUtils.mc().getRenderPartialTicks(), 0);
			GlStateManager.rotate(-25f*(setup/(float)entity.maxSetupTime), 1, 0, 0);
			GlStateManager.translate(0, 0.25*(setup/(float)entity.maxSetupTime), 0);

			if(drawText)
			{
				GlStateManager.pushMatrix();
				GlStateManager.scale(0.85, 0.85, 0.85);
				GlStateManager.rotate(180-entity.setYaw, 0f, 1f, 0f);
				GlStateManager.rotate(-yaw, 0f, 1f, 0f);
				GlStateManager.rotate(-pitch, 1, 0, 0);
				GlStateManager.translate(-0.5f, 0.34375, 1.65625+(pitch/20*0.25));

				GlStateManager.rotate(90, 0, 1, 0);
				GlStateManager.scale(-1, -1, 1);
				GlStateManager.translate(-0.93, -0.7, 0.44);
				GlStateManager.rotate(-45, 1, 0, 0);
				GlStateManager.scale(1/96f, 1/96f, 1/96f);

				ClientUtils.font().drawString("Eisenheim", 0, 1, 0xce953c);
				GlStateManager.color(1, 1, 1);
				GlStateManager.popMatrix();
			}

			for(TmtNamedBoxGroup nmod : renderParts)
			{
				ClientUtils.bindTexture(skin+nmod.getTexturePath());
				if(nmod.getName().equals("bipod"))
				{
					GlStateManager.pushMatrix();
					GlStateManager.scale(0.85, 0.85, 0.85);
					GlStateManager.rotate(180-entity.setYaw, 0f, 1f, 0f);
					GlStateManager.translate(-0.5f, 0.34375+0.0625, 1.65625);
					nmod.render(0.0625f, setup/(float)entity.maxSetupTime);
					GlStateManager.popMatrix();
				}
				else
				{
					GlStateManager.pushMatrix();
					GlStateManager.scale(0.85, 0.85, 0.85);
					GlStateManager.rotate(180-entity.setYaw, 0f, 1f, 0f);
					GlStateManager.rotate(-yaw, 0f, 1f, 0f);
					GlStateManager.rotate(-pitch, 1, 0, 0);
					GlStateManager.translate(-0.5f, 0.34375, 1.65625+(pitch/20*0.25));

					if(nmod.getName().equals("ammo"))
					{
						boolean should_render = false;
						if(entity.currentlyLoaded==1)
						{

							float progress = entity.magazine1.isEmpty()?1f-Math.min(2*(float)entity.clipReload/(float)Machinegun.clipReloadTime, 1): (float)entity.clipReload/(float)Machinegun.clipReloadTime;
							GlStateManager.translate(0f, 0.375f*progress, 0f);
							should_render = true;
						}
						else if(!entity.magazine1.isEmpty())
							should_render = true;

						if(should_render)
							nmod.render(0.0625f);
					}
					else if(nmod.getName().equals("belt_fed_loader"))
					{
						nmod.render(0.0625f);
						IBulletModel mm = BulletRegistry.INSTANCE.registeredModels.get("mg_2bCal");
						GlStateManager.pushMatrix();
						GlStateManager.translate(0.725f, 0.65f, -0.25f+(-0.0625f*1.5f));
						GlStateManager.rotate(180, 0, 1, 0);
						GlStateManager.rotate(90, 1, 0, 0);
						GlStateManager.scale(0.5f, 0.5f, 0.5f);
						GlStateManager.pushMatrix();
						for(int i = 0; i < 3; i++)
						{
							mm.renderBulletUnused(0xbbbbbb, EnumCoreTypes.PIERCING, MathHelper.hsvToRGB(((i*15)%255)/255f, 0.65f, 0.45f));
							GlStateManager.rotate(22.5f*3f, 0, 1, 0);
							GlStateManager.translate(0, 0, -0.0625f/2f);
						}
						GlStateManager.popMatrix();

						GlStateManager.translate(-0.25f, 0, 0.25);
						GlStateManager.pushMatrix();
						for(int i = 0; i < 20; i++)
						{
							mm.renderBulletUnused(0xbbbbbb, EnumCoreTypes.PIERCING, MathHelper.hsvToRGB(((i*15)%255)/255f, 0.65f, 0.45f));
							GlStateManager.translate(0f, 0, 0.165f);
							GlStateManager.rotate(i%15 < 5?5f: -5f, 0, 1, 0);

						}
						GlStateManager.popMatrix();

						GlStateManager.popMatrix();
					}
					else if(nmod.getName().equals("second_magazine_mag"))
					{
						boolean should_render = false;
						if(entity.currentlyLoaded==2)
						{

							float progress = entity.magazine2.isEmpty()?1f-Math.min(2*(float)entity.clipReload/(float)Machinegun.clipReloadTime, 1): (float)entity.clipReload/(float)Machinegun.clipReloadTime;
							GlStateManager.translate(0f, 0.375f*progress, 0f);
							should_render = true;
						}
						else if(!entity.magazine2.isEmpty())
							should_render = true;

						if(should_render)
							nmod.render(0.0625f);
					}
					else if(nmod.getName().equals("slide"))
					{
						if(((entity.currentlyLoaded==1&&entity.magazine1.isEmpty())||(entity.currentlyLoaded==2&&entity.magazine2.isEmpty()))&&((float)entity.clipReload/(float)Machinegun.clipReloadTime) > 0.5)
						{
							float curr = (((float)entity.clipReload/(float)Machinegun.clipReloadTime)-0.5f)/0.5f;
							float progress;
							if(curr > 0.65)
								progress = 1f-((curr-0.65f)/0.35f);
							else
								progress = (curr/0.65f);
							GlStateManager.translate(0f, 0f, progress*0.375);
						}
						nmod.render(0.0625f);
					}
					else if(nmod.getName().equals("shield"))
					{
						ClientUtils.bindTexture(skin+nmod.getTexturePath());
						nmod.render(0.0625f);
						//TODO: add breaking progress
					}
					else
						nmod.render(0.0625f);
					GlStateManager.popMatrix();
				}
			}
		}
		else
		{
			for(TmtNamedBoxGroup nmod : renderParts)
			{
				ClientUtils.bindTexture(skin+nmod.getTexturePath());

				if(nmod.getName().equals("bipod"))
				{
					nmod.render(0.0625f, 1f);
					continue;
				}
				if(nmod.getName().equals("ammo")&&!(ItemNBTHelper.hasKey(stack, "magazine1")&&!(new ItemStack(ItemNBTHelper.getTagCompound(stack, "magazine1")).isEmpty())))
					continue;
				if(nmod.getName().equals("second_magazine_mag")&&!(ItemNBTHelper.hasKey(stack, "magazine2")&&!(new ItemStack(ItemNBTHelper.getTagCompound(stack, "magazine2")).isEmpty())))
					continue;

				for(ModelRendererTurbo m : nmod.getModel())
					m.render(0.0625f);
			}
		}

		GlStateManager.popMatrix();
	}

	public static void drawBulletsList(ItemStack stack)
	{
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.enableDepth();

		RenderItem ir = ClientUtils.mc().getRenderItem();

		if(ItemNBTHelper.hasKey(stack, "bullet0"))
		{
			ir.renderItemIntoGUI(new ItemStack(ItemNBTHelper.getTagCompound(stack, "bullet0")), 0, 0);
		}
		if(ItemNBTHelper.hasKey(stack, "bullet1"))
		{
			ir.renderItemIntoGUI(new ItemStack(ItemNBTHelper.getTagCompound(stack, "bullet1")), 0, 22);
		}
		if(ItemNBTHelper.hasKey(stack, "bullet2"))
		{
			ir.renderItemIntoGUI(new ItemStack(ItemNBTHelper.getTagCompound(stack, "bullet2")), 0, 44);
		}
		if(ItemNBTHelper.hasKey(stack, "bullet3"))
		{
			ir.renderItemIntoGUI(new ItemStack(ItemNBTHelper.getTagCompound(stack, "bullet3")), 0, 66);
		}

		GlStateManager.disableDepth();
	}

	/**
	 * Renders the desired {@code T} type Entity.
	 */
	@Override
	public void doRender(EntityMachinegun entity, double x, double y, double z, float f0, float f1)
	{

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderHelper.enableStandardItemLighting();

		if(entity.gun!=null&&!entity.gun.isEmpty())
			renderMachinegun(entity.gun, entity);


		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityMachinegun entity)
	{
		return new ResourceLocation(CommonProxy.item_machinegun.getSkinnableDefaultTextureLocation()+texture);
	}

}