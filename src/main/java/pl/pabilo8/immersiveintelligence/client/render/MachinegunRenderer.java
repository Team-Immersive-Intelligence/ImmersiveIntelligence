package pl.pabilo8.immersiveintelligence.client.render;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Machinegun;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIMachinegun;
import pl.pabilo8.immersiveintelligence.common.util.IISkinHandler;
import pl.pabilo8.immersiveintelligence.common.util.IISkinHandler.IISpecialSkin;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.weapon.ModelMachinegun;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.TmtNamedBoxGroup;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMachinegun;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class MachinegunRenderer extends Render<EntityMachinegun> implements IReloadableModelContainer<MachinegunRenderer>
{
	public static final String texture = "machinegun.png";
	public static ModelMachinegun model;
	public static HashMap<Predicate<ItemStack>, BiConsumer<ItemStack, List<TmtNamedBoxGroup>>> upgrades = new HashMap<>();
	public static List<TmtNamedBoxGroup> defaultGunParts = new ArrayList<>();
	public static List<TmtNamedBoxGroup> skinParts = new ArrayList<>();

	public MachinegunRenderer(RenderManager renderManager)
	{
		super(renderManager);
		subscribeToList("machinegun");
		reloadModels();
	}

	public static void renderMachinegun(ItemStack stack, @Nullable EntityMachinegun entity)
	{
		GlStateManager.pushMatrix();
		List<TmtNamedBoxGroup> renderParts = new ArrayList<>(defaultGunParts);
		boolean drawText = false, canApply = false;
		String specialText;
		int textColor = 0xffffff;

		String skin = IIContent.itemMachinegun.getSkinnableCurrentSkin(stack);
		if(!skin.isEmpty())
		{
			IISpecialSkin s = IISkinHandler.specialSkins.get(skin);
			if(s!=null)
			{
				ItemIIMachinegun gun = (ItemIIMachinegun)stack.getItem();
				canApply = s.doesApply(gun.getSkinnableName());
				textColor = s.textColor;
				if(s.mods.contains("skin_mg_text"))
					drawText = true;
				skinParts.forEach(tmtNamedBoxGroup -> {
					if(s.mods.contains(tmtNamedBoxGroup.getName()))
						renderParts.add(tmtNamedBoxGroup);
				});
			}

		}
		specialText = I18n.format("skin.immersiveintelligence."+skin+".name");
		skin = ((skin.isEmpty()&&!canApply)?IIContent.itemMachinegun.getSkinnableDefaultTextureLocation(): IIReference.SKIN_LOCATION+skin+"/");


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

				yaw = entity.tripod?MathHelper.clamp(yaw, -82.5F, 82.5F): MathHelper.clamp(yaw, -45.0F, 45.0F);
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
				GlStateManager.disableLighting();

				ClientUtils.font().drawString(specialText, 0, 1, MathHelper.multiplyColor(textColor, 0xffffff*(int)entity.world.getLightBrightness(entity.getPosition())));
				GlStateManager.enableLighting();
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

					switch(nmod.getName())
					{
						case "ammo":
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
							break;
						}
						case "belt_fed_loader":
							nmod.render(0.0625f);
							IBulletModel mm = AmmoRegistry.INSTANCE.registeredModels.get("mg_2bCal");
							GlStateManager.pushMatrix();
							GlStateManager.translate(0.69f, 0.65f, -0.0625f+(-0.0625f*1.5f));
							GlStateManager.rotate(180, 0, 1, 0);
							GlStateManager.rotate(90, 1, 0, 0);
							GlStateManager.scale(0.5f, 0.5f, 0.5f);

							BlockPos cratePos = entity.getPosition().offset(EnumFacing.fromAngle(entity.setYaw).getOpposite()).down();
							if(entity.getEntityWorld().getTileEntity(cratePos) instanceof TileEntityAmmunitionCrate)
							{
								TileEntityAmmunitionCrate crate = ((TileEntityAmmunitionCrate)entity.getEntityWorld().getTileEntity(cratePos));
								assert crate!=null;

								int beltLength = (int)(24+(Math.max(Math.abs(entity.gunYaw)-55, 0)/2)-(Math.max(entity.gunPitch-20, 0)/2));

								if(crate.open&&crate.hasUpgrade(IIContent.UPGRADE_MG_LOADER))
								{
									ArrayList<ItemStack> ammoStacks = new ArrayList<>();
									for(int i = 38, cc = 0; i < 50&&cc < beltLength; i++)
									{
										ItemStack bs = crate.getInventory().get(i);
										if(!bs.isEmpty())
										{
											ammoStacks.add(bs.copy());
											cc += bs.getCount();
										}
									}

									beltLength -= 4;
									float ammoDir = ((entity.gunYaw)/(entity.tripod?90f: 50f))*(1-((Math.abs(entity.gunPitch))/40f));
									float ammoTurn = entity.tripod?((Math.abs(entity.gunYaw)/90f)*(beltLength > 24?(beltLength-24)/24f: 1)): 0;

									GlStateManager.pushMatrix();
									for(int i = 0; i < 4&&ammoStacks.size() > 0; i++)
									{
										mm.renderBulletUnused(ammoStacks.get(0));
										GlStateManager.rotate(180f/4f, 0, 1, 0);
										GlStateManager.translate(0, 0, -0.1225f);

										ammoStacks.get(0).shrink(1);
										if(ammoStacks.get(0).getCount() <= 0)
											ammoStacks.remove(0);
									}
									GlStateManager.popMatrix();

									GlStateManager.translate(-0.25f, 0, 0.25);
									GlStateManager.pushMatrix();
									for(int i = 0; i < beltLength&&ammoStacks.size() > 0; i++)
									{
										mm.renderBulletUnused(ammoStacks.get(0));
										GlStateManager.translate(0f, 0, 0.125f);

										GlStateManager.rotate(ammoDir*-5f, 0, 1, 0);
										GlStateManager.rotate(ammoTurn*-8f, 1, 0, 0);

										ammoStacks.get(0).shrink(1);
										if(ammoStacks.get(0).getCount() <= 0)
											ammoStacks.remove(0);

									}
									GlStateManager.popMatrix();
								}
							}


							GlStateManager.popMatrix();
							break;
						case "second_magazine_mag":
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
							break;
						}
						case "slide":
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
							break;
						case "shield":
							ClientUtils.bindTexture(skin+nmod.getTexturePath());
							nmod.render(0.0625f);
							//TODO: add breaking progress
							break;
						default:
							nmod.render(0.0625f);
							break;
					}
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
		return new ResourceLocation(IIContent.itemMachinegun.getSkinnableDefaultTextureLocation()+texture);
	}

	@Override
	public void reloadModels()
	{
		model = new ModelMachinegun();
		defaultGunParts.clear();
		defaultGunParts.add(model.baseBox);
		defaultGunParts.add(model.barrelBox);
		defaultGunParts.add(model.sightsBox);
		defaultGunParts.add(model.triggerBox);
		defaultGunParts.add(model.ammoBox);
		defaultGunParts.add(model.slideBox);
		defaultGunParts.add(model.gripBox);
		defaultGunParts.add(model.bipodBox);

		skinParts.clear();
		skinParts.add(model.baubleBox);
	}
}