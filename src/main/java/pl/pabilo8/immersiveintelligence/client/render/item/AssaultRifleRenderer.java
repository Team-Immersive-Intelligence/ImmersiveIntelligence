package pl.pabilo8.immersiveintelligence.client.render.item;

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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticles;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTBullet.BulletState;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIItemRendererAMT.RegisteredItemRenderer;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.AssaultRifle;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIAssaultRifle;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIGunBase;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IISkinHandler;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * @author Pabilo8
 * @since 18.09.2022
 */
@RegisteredItemRenderer(name = "items/weapons/assault_rifle")
public class AssaultRifleRenderer extends IIUpgradableItemRendererAMT<ItemIIAssaultRifle> implements ISpecificHandRenderer
{
	IIAnimationCachedMap load, unload, modeSwitch, fire, handAngle, offHandAngle;
	IIAnimationCachedMap loadGrenade, fireGrenade, stabilizer;
	private MTLTextureRemapper skinRemapper;
	private AMTCrossVariantReference<AMT> magazine, hand;
	private AMTCrossVariantReference<AMTParticle> muzzleFlash;
	private AMTCrossVariantReference<AMTText> nixie1, nixie2;
	private AMTCrossVariantReference<AMTBullet> grenade, casingFired;

	public AssaultRifleRenderer()
	{
		super(IIContent.itemAssaultRifle, ResLoc.of(RES_MODEL_WEAPON, "assault_rifle"));
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
		IISkinHandler.registerSprites(map, IIContent.itemAssaultRifle.getSkinnableName());
	}

	@Override
	public void draw(ItemStack stack, TransformType transform, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		if(isScopeZooming(transform, stack))
			return;

		EasyNBT nbt = EasyNBT.wrapNBT(stack);

		model.getVariant(nbt.getString(IISkinHandler.NBT_ENTRY), stack);
		model.forEach(AMT::defaultize);

		//Make upgrade AMTs visible
		showUpgrades(stack, nbt);

		//magazine stack
		ItemStack magazine = nbt.getItemStack(ItemIIAssaultRifle.MAGAZINE);
		ItemStack grenade = nbt.getItemStack(ItemIIAssaultRifle.LOADED_GRENADE);
		IIAnimationUtils.setModelVisibility(this.magazine.get(), !magazine.isEmpty());

		int firing = nbt.getInt(ItemIIAssaultRifle.FIRE_DELAY);
		int firingDelay = item.getFireDelay(stack, nbt);
		int reloading = nbt.getInt(ItemIIAssaultRifle.RELOADING);

		//switch between auto/manual/railgun fire modes
		int lastMode = nbt.getInt(ItemIIAssaultRifle.LAST_FIRE_MODE), fireMode = nbt.getInt(ItemIIAssaultRifle.FIRE_MODE);
		int modeTimer = nbt.getInt(ItemIIAssaultRifle.FIRE_MODE_TIMER);

		//Whether hand should be rendered
		boolean handRender = is1stPerson(transform);

		//hand should be visible only in 1st person mode
		IIAnimationUtils.setModelVisibility(hand.get(), handRender);
		if(handRender)
		{
			int aiming = nbt.getInt(ItemIIAssaultRifle.AIMING);
			boolean scoped = item.isScoped(stack);
			float preciseAim = IIAnimationUtils.getAnimationProgress(aiming, item.getAimingTime(stack, nbt),
					true, !Minecraft.getMinecraft().player.isSneaking(),
					1, 3,
					partialTicks);

			if(preciseAim > 0)
			{
				//gun "push" towards player
				float recoil = Math.min((nbt.getFloat(ItemIIAssaultRifle.RECOIL_V)+nbt.getFloat(ItemIIAssaultRifle.RECOIL_H))/(AssaultRifle.maxRecoilHorizontal+AssaultRifle.maxRecoilVertical), 1f);

				GlStateManager.translate(-preciseAim*1.03125, 0.225*preciseAim, 0);
				GlStateManager.rotate(preciseAim*-8f, 0, 1, 0);
				GlStateManager.rotate(preciseAim*-5f, 1, 0, 0);
				if(scoped)
				{
					GlStateManager.translate(0, preciseAim*-0.1, preciseAim*0.85);
					GlStateManager.rotate(5*preciseAim, 1, 0, 0);
				}
				else
					GlStateManager.translate(0, 0, preciseAim*0.25);
				if(recoil > 0)
					GlStateManager.translate(0, -recoil*(0.155-0.1*preciseAim), recoil*0.25);
			}
			(transform==TransformType.FIRST_PERSON_RIGHT_HAND?handAngle: offHandAngle).apply(preciseAim);
		}

		//fireGrenade.apply(0);
		//Choose and apply firing animation

		(fireMode==2?fireGrenade: fire).apply((1f-((firing-partialTicks)/firingDelay)));
		//Don't show muzzle flash GUI
		if(transform==TransformType.GUI)
		{
			IIAnimationUtils.setModelVisibility(muzzleFlash.get(), false);
			IIAnimationUtils.setModelVisibility(casingFired.get(), false);
		}

		//Display the grenade mounted
		this.grenade.get().withStack(grenade, BulletState.BULLET_USED);

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
				//Grenade Launcher
				case 2:
				{
					loadGrenade.apply(v);
					this.grenade.get().withStack(nbt.getItemStack("found"), BulletState.BULLET_UNUSED);
					//Rotate the gun held 35 degrees during reload
					if(handRender)
					{
						GlStateManager.rotate(rpart*35f, 1, 0, 0);
						GlStateManager.translate(0, -rpart*0.35, 0);
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
			float modeProgress = 1f-MathHelper.clamp((nbt.getInt(ItemIIAssaultRifle.FIRE_MODE_TIMER)-partialTicks)/6f, 0f, 1f);
			this.modeSwitch.apply(
					((float)MathHelper.clampedLerp(lastMode, fireMode, modeProgress))*0.5f
			);
		}

		//Animate Stereoscopic Rangefinder's nixie tube distance meter
		if(handRender)
		{
			int value = 0;
			if(item.hasIIUpgrade(stack, WeaponUpgrade.STEREOSCOPIC_RANGEFINDER))
			{
				RayTraceResult mop = ClientUtils.mc().player.rayTrace(60, partialTicks);
				if(mop!=null)
					value = (int)ClientUtils.mc().player.getPositionVector().distanceTo(mop.hitVec);
			}
			else
			{
				if(fireMode==2)
					value = (int)MathHelper.clamp((1f-((firing-partialTicks)/(float)(firingDelay)))*99, 0, 99);
			}
			if(item.hasIIUpgrade(stack, WeaponUpgrade.GYROSCOPIC_STABILIZER))
				stabilizer.apply(IIAnimationUtils.getDebugProgress(30, partialTicks));

			nixie1.get().setText(String.valueOf(value/10));
			nixie2.get().setText(String.valueOf(value%10));
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
				.withModel(ResLoc.of(this.directoryRes, "upgrades/common.obj"))
				.withModels(listUpgradeModels())
				.withHeader(header)
				.withHeader(ResLoc.of(this.directoryRes, "assault_rifle_upgrades.obj.amt"))
				.withModelProvider(
						(stack, combinedHeader) -> new AMT[]{
								//Main Model
								new AMTParticle("muzzle_flash", combinedHeader).setParticle(IIParticles.PARTICLE_GUNFIRE),
								new AMTHand("hand", combinedHeader, EnumHand.OFF_HAND),

								//Ammo
								new AMTBullet("casing_fired", combinedHeader, AmmoRegistry.getModel(IIContent.itemAmmoAssaultRifle))
										.withState(BulletState.CASING),
								new AMTBullet("grenade", combinedHeader, AmmoRegistry.getModel(IIContent.itemRailgunGrenade))
										.withState(BulletState.CASING),

								//Upgrades
								new AMTText("nixie_text1", combinedHeader)
										.setText("0")
										.setFontSize(0.015625f)
										.setColor(IIReference.COLOR_NIXIE_ORANGE),
								new AMTText("nixie_text2", combinedHeader)
										.setText("0")
										.setFontSize(0.015625f)
										.setColor(IIReference.COLOR_NIXIE_ORANGE)
						}
				).withTextureProvider(
						(res, stack) ->
						{
							String skin = IIContent.itemAssaultRifle.getSkinnableCurrentSkin(stack);
							if(IISkinHandler.isValidSkin(skin))
							{
								this.skinRemapper = new MTLTextureRemapper(model, ResLoc.of(IIReference.RES_TEXTURES_SKIN, skin, "/assault_rifle").withExtension(ResLoc.EXT_MTL));
								return ClientUtils.getSprite(this.skinRemapper.apply(res));
							}

							return ClientUtils.getSprite(res);
						}
				)
				.build();

		this.magazine = new AMTCrossVariantReference<>("magazine", this.model);
		this.hand = new AMTCrossVariantReference<>("hand", this.model);
		this.muzzleFlash = new AMTCrossVariantReference<>("muzzle_flash", this.model);

		this.nixie1 = new AMTCrossVariantReference<>("nixie_text1", this.model);
		this.nixie2 = new AMTCrossVariantReference<>("nixie_text2", this.model);
		this.grenade = new AMTCrossVariantReference<>("grenade", this.model);
		this.casingFired = new AMTCrossVariantReference<>("casing_fired", this.model);

		//Add upgrade visibility animations
		loadUpgrades(model, ResLoc.of(animationRes, "upgrades/"));

		load = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "load"));
		unload = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "unload"));
		modeSwitch = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "mode_manual"));
		fire = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "fire"));
		handAngle = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "hand"));
		offHandAngle = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "offhand"));

		loadGrenade = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "load_grenade"));
		fireGrenade = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "fire_grenade"));
		stabilizer = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "stabilizer"));

	}

	@Override
	protected void nullifyModels()
	{
		IIAnimationUtils.disposeOf(model);
	}

	@Override
	public boolean doHandRender(ItemStack stack, EnumHand hand, ItemStack otherHand, float swingProgress, float partialTicks)
	{
		return hand==EnumHand.OFF_HAND&&otherHand.getItem() instanceof ItemIIGunBase;
	}

	@Override
	public boolean shouldCancelCrosshair(ItemStack stack, EnumHand hand)
	{
		if(item.isScoped(stack))
			return false;

		return ItemNBTHelper.getInt(stack, ItemIIAssaultRifle.AIMING) > AssaultRifle.aimTime*0.85;
	}

}
