package pl.pabilo8.immersiveintelligence.client.render.entity.weapon;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammocrate.AmmunitionCrateHandler;
import pl.pabilo8.immersiveintelligence.client.model.builtin.IAmmoModel;
import pl.pabilo8.immersiveintelligence.client.model.weapon.ModelMachinegun;
import pl.pabilo8.immersiveintelligence.client.render.IPassengerAnimationsRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.TmtNamedBoxGroup;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice.IIBlockTypes_MetalDevice;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.entity.mounted_weapon.EntityMachinegun;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIMachinegun;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IISkinHandler;
import pl.pabilo8.immersiveintelligence.common.util.IISkinHandler.IISpecialSkin;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class MachinegunRenderer extends Render<EntityMachinegun> implements IReloadableModelContainer<MachinegunRenderer>, IPassengerAnimationsRenderer<EntityMachinegun>
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

	public static void renderMachinegun(ItemStack stack, @Nullable EntityMachinegun entity, float partialTicks)
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
		skin = skin.isEmpty()?IIContent.itemMachinegun.getSkinnableDefaultTextureLocation(): IIReference.SKIN_LOCATION+skin+"/";
		ClientUtils.bindTexture(skin+texture);

		for(Entry<Predicate<ItemStack>, BiConsumer<ItemStack, List<TmtNamedBoxGroup>>> s : upgrades.entrySet())
		{
			if(s.getKey()!=null&&s.getValue()!=null&&s.getKey().test(stack))
				s.getValue().accept(stack, renderParts);
		}

		if(entity!=null)
		{
			float yaw = entity.aim.getRelativeYaw(partialTicks), pitch = entity.aim.getPitch(partialTicks);
			boolean tripod = entity.upgrades.contains(WeaponUpgrade.TRIPOD);
			if(tripod)
				GlStateManager.translate(0f, 0.5, 0f);

			GlStateManager.translate(0f, -0.34375, 0f);
			float setup = AMTUtils.getAnimationProgress(entity.setupTime, entity.maxSetupTime, true, partialTicks);
			GlStateManager.rotate(-25f*setup, 1, 0, 0);
			GlStateManager.translate(0, 0.25*setup, 0);

			if(drawText)
			{
				GlStateManager.pushMatrix();
				GlStateManager.scale(0.85, 0.85, 0.85);
				GlStateManager.rotate(180-entity.aim.getCenterYaw(), 0f, 1f, 0f);
				GlStateManager.rotate(-yaw, 0f, 1f, 0f);
				GlStateManager.rotate(-pitch, 1, 0, 0);
				GlStateManager.translate(-0.5f, 0.34375, 1.65625+pitch/20*0.25);

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
					GlStateManager.rotate(180-entity.aim.getCenterYaw(), 0f, 1f, 0f);
					GlStateManager.translate(-0.5f, 0.34375+0.0625, 1.65625);
					nmod.render(0.0625f, setup);
					GlStateManager.popMatrix();
				}
				else
				{
					GlStateManager.pushMatrix();
					GlStateManager.scale(0.85, 0.85, 0.85);
					GlStateManager.rotate(180-entity.aim.getCenterYaw(), 0f, 1f, 0f);
					GlStateManager.rotate(-yaw, 0f, 1f, 0f);
					GlStateManager.rotate(-pitch, 1, 0, 0);
					GlStateManager.translate(-0.5f, 0.34375, 1.65625+pitch/20*0.25);

					switch(nmod.getName())
					{
						case "ammo":
						{
							boolean should_render = entity.loadingMagazine1.getLoadingProgress(partialTicks) > 0;
							GlStateManager.translate(0f, 0.375f*(1f-entity.loadingMagazine1.getLoadingProgress(partialTicks)), 0f);
							if(should_render)
								nmod.render(0.0625f);
							break;
						}
						case "belt_fed_loader":
							nmod.render(0.0625f);
							IAmmoModel<ItemIIAmmoBase<EntityAmmoProjectile>, EntityAmmoProjectile> mm = AmmoRegistry.getModel(IIContent.itemAmmoMachinegun);
							GlStateManager.pushMatrix();
							GlStateManager.translate(0.69f, 0.65f, -0.0625f+-0.0625f*1.5f);
							GlStateManager.rotate(180, 0, 1, 0);
							GlStateManager.rotate(90, 1, 0, 0);
							GlStateManager.scale(0.5f, 0.5f, 0.5f);

							ArrayList<ItemStack> ammoStacks = new ArrayList<>(AmmunitionCrateHandler.getMountedAmmunition(entity));
							if(!ammoStacks.isEmpty())
							{
								float relativeYaw = entity.aim.getRelativeYaw(0f);
								int beltLength = (int)(24+Math.max(Math.abs(relativeYaw)-55, 0)/2-Math.max(entity.aim.getPitch(partialTicks)-20, 0)/2);

								beltLength -= 4;
								float ammoDir = relativeYaw/(tripod?90f: 50f)*(1-Math.abs(entity.aim.getPitch(partialTicks))/40f);
								float ammoTurn = tripod?Math.abs(relativeYaw)/90f*(beltLength > 24?(beltLength-24)/24f: 1): 0;

								GlStateManager.pushMatrix();
								for(int i = 0; i < 4&&!ammoStacks.isEmpty(); i++)
								{
									mm.renderAmmoComplete(false, ammoStacks.get(0));
									GlStateManager.rotate(180f/4f, 0, 1, 0);
									GlStateManager.translate(0, 0, -0.1225f);

									ammoStacks.get(0).shrink(1);
									if(ammoStacks.get(0).getCount() <= 0)
										ammoStacks.remove(0);
								}
								GlStateManager.popMatrix();

								GlStateManager.translate(-0.25f, 0, 0.25);
								GlStateManager.pushMatrix();
								for(int i = 0; i < beltLength&&!ammoStacks.isEmpty(); i++)
								{
									mm.renderAmmoComplete(false, ammoStacks.get(0));
									GlStateManager.translate(0f, 0, 0.125f);

									GlStateManager.rotate(ammoDir*-5f, 0, 1, 0);
									GlStateManager.rotate(ammoTurn*-8f, 1, 0, 0);

									ammoStacks.get(0).shrink(1);
									if(ammoStacks.get(0).getCount() <= 0)
										ammoStacks.remove(0);

								}
								GlStateManager.popMatrix();
							}


							GlStateManager.popMatrix();
							break;
						case "second_magazine_mag":
						{
							boolean should_render = entity.loadingMagazine2.getLoadingProgress(partialTicks) > 0;
							GlStateManager.translate(0f, 0.375f*(entity.loadingMagazine2.getLoadingProgress(partialTicks)), 0f);
							if(should_render)
								nmod.render(0.0625f);
							break;
						}
						case "slide":
							//if(entity.currentlyLoaded==1)
						{
							float curr = (entity.gunHandler.getLoadingProgress(partialTicks)-0.5f)/0.5f;
							float progress;
							if(curr > 0.65)
								progress = 1f-(curr-0.65f)/0.35f;
							else
								progress = curr/0.65f;
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
				if(nmod.getName().equals("ammo")&&!(ItemNBTHelper.hasKey(stack, "magazine1")&&!new ItemStack(ItemNBTHelper.getTagCompound(stack, "magazine1")).isEmpty()))
					continue;
				if(nmod.getName().equals("second_magazine_mag")&&!(ItemNBTHelper.hasKey(stack, "magazine2")&&!new ItemStack(ItemNBTHelper.getTagCompound(stack, "magazine2")).isEmpty()))
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
	public void doRender(EntityMachinegun entity, double x, double y, double z, float entityYaw, float partialTicks)
	{

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderHelper.enableStandardItemLighting();

		ItemStack stack = entity.getOriginStack();
		if(!stack.isEmpty())
			renderMachinegun(stack, entity, partialTicks);


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

	@Override
	public boolean handleBipedRotations(ModelBiped model, EntityMachinegun mg, EntityLivingBase passenger, float partialTicks)
	{
		float ff = (float)(-1.35f-Math.toRadians(mg.aim.getPitch(partialTicks))*1.25);
		float setYaw = mg.aim.getCenterYaw();
		float true_head_angle = MathHelper.wrapDegrees(passenger.prevRotationYawHead-setYaw);
		float wtime;

		mg.applyOrientationToEntity(passenger);
		model.bipedHead.rotateAngleX *= -0.35f;
		model.bipedHeadwear.rotateAngleX *= -0.35f;
		model.bipedLeftArm.rotateAngleY = .08726f+3.14f/6f;

		IBlockState state = passenger.world.getBlockState(passenger.getPosition());
		boolean tripod = mg.upgrades.contains(WeaponUpgrade.TRIPOD);
		float aimYaw = mg.aim.getRelativeYaw(0f);
		if(!tripod&&state.getMaterial().isSolid()&&!(state.getBlock()==IIContent.blockMetalDevice&&state.getValue(IIContent.blockMetalDevice.property)==IIBlockTypes_MetalDevice.AMMUNITION_CRATE))
		{
			float headDelta = MathHelper.wrapDegrees(aimYaw-true_head_angle);
			if(Math.abs(headDelta) > 5)
			{
				wtime = Math.abs((mg.getEntityWorld().getTotalWorldTime()+partialTicks)%20/20f-0.5f)/0.5f;
				wtime *= 0.25f;

				if(!mg.isSetupComplete())
					wtime = 0;
				if(headDelta < 0)
				{
					model.bipedRightLeg.rotateAngleY = -wtime*2f;
					model.bipedLeftLeg.rotateAngleY = wtime*2f;
				}
				else
				{
					model.bipedRightLeg.rotateAngleY = wtime*2f;
					model.bipedLeftLeg.rotateAngleY = -wtime*2f;
				}
			}

			model.bipedBody.rotateAngleX += 1.5f;
			model.bipedRightLeg.rotateAngleX += 1.5f;
			model.bipedLeftLeg.rotateAngleX += 1.5f;

			model.bipedRightArm.rotateAngleX += ff-0.5f;
			model.bipedLeftArm.rotateAngleX += ff-0.5f;

			model.bipedRightLeg.rotationPointY = 0f;
			model.bipedLeftLeg.rotationPointY = 0f;

			model.bipedRightLeg.rotationPointZ = 12f;
			model.bipedLeftLeg.rotationPointZ = 12f;

			float maxRotation = 45.0F;

			model.bipedRightLeg.rotateAngleY += aimYaw/maxRotation;
			model.bipedLeftLeg.rotateAngleY += aimYaw/maxRotation;

			//model.bipedLeftLeg.rotateAngleY += ff+1f;

		}
		else
		{
			wtime = Math.abs((mg.getEntityWorld().getTotalWorldTime()+partialTicks)%40/40f-0.5f)/0.5f-0.5f;
			wtime *= 0.65f;
			if(!mg.isSetupComplete())
				wtime = 0;
			model.bipedBody.rotateAngleX -= 0.0625f;
			float headDelta = MathHelper.wrapDegrees(aimYaw-true_head_angle);
			if(Math.abs(headDelta) > 5)
				if(headDelta < 0)
				{
					model.bipedRightLeg.rotateAngleX = wtime*2f;
					model.bipedLeftLeg.rotateAngleX = -wtime*2f;
				}
				else
				{
					model.bipedRightLeg.rotateAngleX = -wtime*2f;
					model.bipedLeftLeg.rotateAngleX = wtime*2f;
				}

			model.bipedRightArm.rotateAngleX = ff;
			model.bipedLeftArm.rotateAngleX = ff;
		}
		return true;
	}
}
