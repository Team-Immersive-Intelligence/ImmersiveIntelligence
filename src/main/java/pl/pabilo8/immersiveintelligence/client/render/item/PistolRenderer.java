package pl.pabilo8.immersiveintelligence.client.render.item;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.ImmersiveModelRegistry.ItemModelReplacement;
import blusunrize.immersiveengineering.client.ImmersiveModelRegistry.ItemModelReplacement_OBJ;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticles;
import pl.pabilo8.immersiveintelligence.client.util.ResLoc;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTBullet.BulletState;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.AssaultRifle;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Pistol;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIPistol;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIGunBase;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler.AmmoHandler;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IISkinHandler;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * @author Pabilo8
 * @since 18.09.2022
 */
public class PistolRenderer extends IIUpgradableItemRendererAMT<ItemIIPistol> implements ISpecificHandRenderer
{
	IIAnimationCachedMap load, unload, modeSwitch, fire;
	private MTLTextureRemapper skinRemapper;
	private AMTCrossVariantReference<AMT> magazine;
	private AMTCrossVariantReference<AMTParticle> muzzleFlash;
	private AMTCrossVariantReference<AMTBullet> casingFired;
	private IIAnimationCachedMap handVisibility, offhandVisibility, handAngle;

	public PistolRenderer()
	{
		super(IIContent.itemPistol, ResLoc.of(RES_MODEL_WEAPON, "pistol"));
	}

	@Override
	protected ItemModelReplacement setTransforms(ItemModelReplacement_OBJ model)
	{
		//TODO: 22.12.2023 transforms from .amt
		Matrix4 tpp = new Matrix4()
				.scale(0.385, 0.385, 0.385)
				.rotate(Math.toRadians(-20.5f), 0, 1, 0)
				.translate(0.625f, -1.25, -0.25f);
		Matrix4 tppOffhand = new Matrix4()
				.scale(0.385, 0.385, 0.385)
				.rotate(Math.toRadians(75f), 1, 0, 0)
				.rotate(Math.toRadians(20.5f), 0, 0, 1)
				.rotate(Math.toRadians(90f), 0, 1, 0)
				.translate(-0.5f, -.25, .125);

		Matrix4 fpp = new Matrix4()
				.scale(0.625, 0.625, 0.625)
				.translate(1f-0.25f, -1f, 0)
				.rotate(Math.toRadians(7.5f), 0, 1, 0)
				.rotate(Math.toRadians(5), 1, 0, 0)
				.translate(0, 0, -0.5f);
		Matrix4 fppOffhand = new Matrix4()
				.scale(0.55, 0.55, 0.55)
				.translate(1f-0.25f, -1f, 0)
				.rotate(Math.toRadians(82.5), 0, 1, 0)
				.rotate(Math.toRadians(2.5), 1, 0, 0)
				.translate(0, 0, -0.5f);

		return model
				.setTransformations(TransformType.GROUND, new Matrix4()
						.scale(0.325, 0.325, 0.325)
						.translate(0.5, -0.75, 0.5))
				.setTransformations(TransformType.THIRD_PERSON_RIGHT_HAND, tpp)
				.setTransformations(TransformType.THIRD_PERSON_LEFT_HAND, tppOffhand)
				.setTransformations(TransformType.FIXED, new Matrix4()
						.rotate(Math.toRadians(-3.5), 1, 0, 0)
						.rotate(Math.toRadians(-75), 0, 1, 0)
						.translate(0.125, -0.25, -0.125)
						.scale(0.425, 0.425, 0.425))
				.setTransformations(TransformType.GUI, new Matrix4()
						.translate(0, -0.25, 0)
						.scale(0.325, 0.325, 0.325)
						.rotate(Math.toRadians(35), 1, 0, 0)
						.rotate(Math.toRadians(135), 0, 1, 0)
				)
				.setTransformations(TransformType.FIRST_PERSON_RIGHT_HAND, fpp)
				.setTransformations(TransformType.FIRST_PERSON_LEFT_HAND, fppOffhand);
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		super.registerSprites(map);
		IISkinHandler.registerSprites(map, IIContent.itemPistol.getSkinnableName());
	}

	@Override
	public void draw(ItemStack stack, TransformType transform, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		if(isScopeZooming(transform, stack))
			return;

		EasyNBT nbt = EasyNBT.wrapNBT(stack);

		//TODO: 12.04.2023 skins and shaders
		model.getVariant(nbt.getString("contributorSkin"), stack);
		model.forEach(AMT::defaultize);

		//Make upgrade AMTs visible
		//showUpgrades(stack, nbt);

		//magazine stack
		ItemStack magazine = nbt.getItemStack(ItemIIPistol.MAGAZINE);
		IIAnimationUtils.setModelVisibility(this.magazine.get(), !magazine.isEmpty());

		int firing = nbt.getInt(ItemIIPistol.FIRE_DELAY);
		int firingDelay = item.getFireDelay(stack, nbt);
		int reloading = nbt.getInt(ItemIIPistol.RELOADING);

		//switch between auto/manual/railgun fire modes
		int lastMode = nbt.getInt(ItemIIPistol.LAST_FIRE_MODE), fireMode = nbt.getInt(ItemIIPistol.FIRE_MODE);
		int modeTimer = nbt.getInt(ItemIIPistol.FIRE_MODE_TIMER);

		//Whether hand should be rendered
		boolean handRender = is1stPerson(transform);

		AmmoHandler ammoHandler = item.getAmmoHandler(stack);

		boolean gui = transform==TransformType.GUI;

		//Make upgrade AMTs visible
		//showUpgrades(stack, nbt);

		handVisibility.apply(handRender?1: 0);

		if(handRender)
		{
			int aiming = nbt.getInt(ItemIIPistol.AIMING);
			float preciseAim = IIAnimationUtils.getAnimationProgress(aiming, item.getAimingTime(stack, nbt),
					true, !Minecraft.getMinecraft().player.isSneaking(),
					1, 3,
					partialTicks);

			if(preciseAim > 0)
			{
				//gun "push" towards player
				float recoil = Math.min(
						(nbt.getFloat(ItemIIPistol.RECOIL_V)+nbt.getFloat(ItemIIPistol.RECOIL_H))
								/(AssaultRifle.maxRecoilHorizontal+AssaultRifle.maxRecoilVertical),
						1f);

				GlStateManager.translate(-preciseAim*(1-0.0625-0.0625/2+0.0078125), 0.25*preciseAim, 0);
				GlStateManager.rotate(preciseAim*-7.75f, 0, 1, 0);
				GlStateManager.rotate(preciseAim*-5f, 1, 0, 0);

				if(item.hasIIUpgrades(stack, WeaponUpgrade.SCOPE))
				{
					GlStateManager.translate(0, preciseAim*-0.1, preciseAim*1.5);
					GlStateManager.rotate(5*preciseAim, 1, 0, 0);
				}
				else
					GlStateManager.translate(0, 0, preciseAim*0.35);

				if(recoil > 0)
					GlStateManager.translate(0, 0, recoil*0.25);


			}

			handAngle.apply(preciseAim);
		}

		//Don't show muzzle flash GUI
		if(transform==TransformType.GUI)
		{
			IIAnimationUtils.setModelVisibility(muzzleFlash.get(), false);
			IIAnimationUtils.setModelVisibility(casingFired.get(), false);
		}

		IIAnimationUtils.setModelVisibility(this.magazine.get(), !magazine.isEmpty());
		if(reloading > 0)
		{
			float v = IIAnimationUtils.getAnimationProgress(
					reloading,
					(float)item.getReloadTime(stack, ItemStack.EMPTY, EasyNBT.wrapNBT(item.getUpgrades(stack))),
					false,
					partialTicks
			);

			float rpart = v <= 0.33?v/0.33f: v <= 0.66?1f: 1f-(v-0.66f)/0.33f;
			switch(fireMode)
			{
				//Regular Rifle
				case 0:
				case 1:
				{
					(magazine.isEmpty()?load: unload).apply(v);
					//Rotate the gun held 80 degrees during reload
					if(handRender)
					{
						GlStateManager.rotate(rpart*80f, 1, 0, 0);
						GlStateManager.translate(0, -rpart*0.75, -rpart*1.5);
					}
				}
				break;

				default:
					break;
			}

		}

		//Animate fire mode switching
		this.modeSwitch.apply(fireMode*0.5f); //0 or 1
		if(modeTimer > 0)
		{
			float modeProgress = 1f-MathHelper.clamp((nbt.getInt(ItemIIPistol.FIRE_MODE_TIMER)-partialTicks)/6f, 0f, 1f);
			this.modeSwitch.apply(
					((float)MathHelper.clampedLerp(lastMode, fireMode, modeProgress))*0.5f
			);
		}

		//Animate Stereoscopic Rangefinder's nixie tube distance meter
		if(handRender)
		{
			int value = 0;
			//if(item.hasIIUpgrade(stack, WeaponUpgrade.STEREOSCOPIC_RANGEFINDER))
			//{
				//RayTraceResult mop = ClientUtils.mc().player.rayTrace(60, partialTicks);
				//if(mop!=null)
					//value = (int)ClientUtils.mc().player.getPositionVector().distanceTo(mop.hitVec);
			//}
			//else
			//{
				if(fireMode==2)
					value = (int)MathHelper.clamp((1f-((firing-partialTicks)/(float)(firingDelay)))*99, 0, 99);
			//}


		}

		//Finally, render
		for(AMT amt : model)
			amt.render(tes, buf);

	}

	@Override
	public void compileModels(OBJModel model, IIModelHeader header)
	{
		this.model = AMTModelCacheBuilder.startItemModel()
				.withModel(model)
				.withModels(listUpgradeModels())
				.withHeader(header)
				.withModelProvider(
						(stack, combinedHeader) -> new AMT[]{
								new AMTBullet("bullet", combinedHeader, AmmoRegistry.getModel(IIContent.itemAmmoPistol))
										.withState(BulletState.BULLET_UNUSED)
										.withProperties(IIContent.ammoCoreSteel, CoreType.SHAPED, -1),
								new AMTBullet("casing_fired", combinedHeader, AmmoRegistry.getModel(IIContent.itemAmmoPistol))
										.withState(BulletState.CASING),
								new AMTParticle("muzzle_flash", combinedHeader)
										.setParticle(IIParticles.PARTICLE_GUNFIRE),
								new AMTHand("hand_off", combinedHeader, EnumHand.OFF_HAND),
								new AMTHand("hand_main", combinedHeader, EnumHand.MAIN_HAND)
						}
				)
				.withTextureProvider(
						(res, stack) ->
						{
							String skin = IIContent.itemPistol.getSkinnableCurrentSkin(stack);
							if(IISkinHandler.isValidSkin(skin))
							{
								this.skinRemapper = new MTLTextureRemapper(model, ResLoc.of(IIReference.RES_TEXTURES_SKIN, skin, "/pistol").withExtension(ResLoc.EXT_MTL));
								return ClientUtils.getSprite(this.skinRemapper.apply(res));
							}
							return ClientUtils.getSprite(res);
						}
				)
				.build();

		this.magazine = new AMTCrossVariantReference<>("magazine", this.model);
		handVisibility = IIAnimationCachedMap.create(this.model, new ResourceLocation(ImmersiveIntelligence.MODID, "gun_hand_visibility"));
		offhandVisibility = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "offhand"));
		this.muzzleFlash = new AMTCrossVariantReference<>("muzzle_flash", this.model);
		this.casingFired = new AMTCrossVariantReference<>("casing_fired", this.model);

		//Add upgrade visibility animations
		//loadUpgrades(model, ResLoc.of(animationRes, "upgrades/"));

		load = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "load"));
		unload = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "unload"));
		modeSwitch = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "mode_manual"));
		fire = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "fire"));
		handAngle = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "hand"));
	}

	@Override
	public boolean doHandRender(ItemStack stack, EnumHand hand, ItemStack otherHand, float swingProgress, float partialTicks)
	{
		return hand==EnumHand.OFF_HAND&&otherHand.getItem() instanceof ItemIIGunBase;
	}

	@Override
	public boolean renderCrosshair(ItemStack stack, EnumHand hand)
	{
		return ItemNBTHelper.getInt(stack, ItemIIPistol.AIMING) > Pistol.aimTime*0.85;
	}

}
