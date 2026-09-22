package pl.pabilo8.immersiveintelligence.client.render.item;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticles;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.MTLTextureRemapper;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModelBuilder;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCrossVariantReference;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet.BulletState;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTHand;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTParticle;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIItemRendererAMT.RegisteredItemRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIUpgradableItemRendererAMT;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIGunBase;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIRifle;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler.AmmoHandler;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IISkinHandler;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 18.09.2022
 */
@RegisteredItemRenderer(name = "items/weapons/rifle")
public class RifleRenderer extends IIUpgradableItemRendererAMT<ItemIIRifle> implements ISpecificHandRenderer
{
	private MTLTextureRemapper handmadeRemapper;
	private MTLTextureRemapper skinRemapper;
	private AMTCrossVariantReference<AMTBullet> bullet, casing;

	private IIAnimationCachedMap loadBullet, loadMag, unloadMag, fireBoltAction, fireSemiAutomatic, handAngle, handVisibility, offhandVisibility;
	private float swingProgress = 0;

	public RifleRenderer()
	{
		super(IIContent.itemRifle, ResLoc.of(RES_MODEL_WEAPON, "rifle"));
	}

	@Override
	public void draw(ItemStack stack, TransformType transform, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		if(isScopeZooming(transform, stack))
			return;

		EasyNBT nbt = EasyNBT.wrapNBT(stack);
		EasyNBT upgradeNBT = EasyNBT.wrapNBT(item.getUpgrades(stack));
		AmmoHandler ammoHandler = item.getAmmoHandler(stack);

		//Set model variant
		model.getVariant(stack, nbt.hasKey("handmade")?"diy": nbt.getString(IISkinHandler.NBT_ENTRY));
		model.forEach(AMT::defaultize);

		int firing = nbt.getInt(ItemIIRifle.FIRE_DELAY);
		int reloading = nbt.getInt(ItemIIRifle.RELOADING);
		boolean handRender = is1stPerson(transform);
		boolean semiAuto = item.hasIIUpgrade(stack, WeaponUpgrade.SEMI_AUTOMATIC);
		boolean gui = transform==TransformType.GUI;

		//Make upgrade AMTs visible
		showUpgrades(stack, nbt);

		handVisibility.apply(handRender?1: 0);

		if(handRender)
		{
			int aiming = nbt.getInt(ItemIIRifle.AIMING);
			float preciseAim = AMTUtils.getAnimationProgress(aiming, item.getAimingTime(stack, upgradeNBT),
					true, !Minecraft.getMinecraft().player.isSneaking(),
					1, 3,
					partialTicks);

			if(preciseAim > 0)
			{
				//gun "push" towards player
				float maxRecoil = item.getMaxHorizontalRecoil(stack, upgradeNBT)+item.getMaxVerticalRecoil(stack, upgradeNBT);
				float recoil = maxRecoil > 0?
						Math.min((nbt.getFloat(ItemIIRifle.RECOIL_V)+nbt.getFloat(ItemIIRifle.RECOIL_H))/maxRecoil, 1f): 0;

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
			else if(swingProgress > 0&&upgradeNBT.hasKey("melee"))
			{
				GlStateManager.translate(0, 0, -0.5f*(1f-Math.abs((swingProgress-0.5f)/0.5f)));
			}

			handAngle.apply(preciseAim);
		}

		(semiAuto?fireSemiAutomatic: fireBoltAction).apply(gui?0: (1f-((firing-partialTicks)/(item.getFireDelay(stack, nbt)))));

		float v = AMTUtils.getAnimationProgress(
				reloading,
				(float)item.getReloadTime(stack, ItemStack.EMPTY, upgradeNBT),
				reloading > 0,
				false,
				1,
				0,
				partialTicks
		);

		if(semiAuto)
		{
			ItemStack magazine = nbt.getItemStack(ItemIIRifle.MAGAZINE);
			(magazine.isEmpty()?loadMag: unloadMag).apply(v);
		}

		if(reloading > 0)
		{
			if(!semiAuto)
			{
				this.bullet.get().withStack(nbt.getItemStack("found"), BulletState.BULLET_UNUSED);
				loadBullet.apply(v);
			}
		}
		else if(handRender)
		{
			ItemStack b = ammoHandler.getNextAmmo(stack, nbt, false);
			this.bullet.get().withStack(b, BulletState.BULLET_UNUSED);
			this.casing.get().withStack(b, BulletState.CASING);
		}

		if(gui)
			this.casing.get().setVisible(false);

		if(transform==TransformType.FIRST_PERSON_LEFT_HAND)
			offhandVisibility.apply(0);

		//Finally, render
		model.render(tes, buf);
	}

	@Override
	public void compileModels(OBJModel model, AMTModelHeader header)
	{
		this.handmadeRemapper = new MTLTextureRemapper(model, ResLoc.of(directoryRes, "rifle_handmade").withExtension(ResLoc.EXT_MTL));

		this.model = AMTCachedModelBuilder.startItemModel()
				.withModel(model)
				.withModels(listUpgradeModels())
				.withHeader(header)
				.withHeader(AMTLoader.loadHeader(new ResourceLocation(ImmersiveIntelligence.MODID, "models/item/weapons/rifle/rifle_upgrades.obj.amt")))
				.withModelProvider(
						(stack, combinedHeader) -> new AMT[]{
								new AMTBullet("bullet", combinedHeader, AmmoRegistry.getModel(IIContent.itemAmmoMachinegun))
										.withState(BulletState.BULLET_UNUSED)
										.withProperties(IIContent.ammoCoreSteel, CoreType.PIERCING, null),
								new AMTBullet("casing_fired", combinedHeader, AmmoRegistry.getModel(IIContent.itemAmmoMachinegun))
										.withState(BulletState.CASING),
								new AMTParticle("muzzle_flash", combinedHeader)
										.setParticle(IIParticles.PARTICLE_GUNFIRE),
								new AMTHand("hand", combinedHeader, EnumHand.OFF_HAND),
								new AMTHand("hand_right", combinedHeader, EnumHand.MAIN_HAND)
						}
				)
				.withTextureProvider(
						(res, stack) ->
						{
							String skin = IIContent.itemRifle.getSkinnableCurrentSkin(stack);
							if(IISkinHandler.isValidSkin(skin))
							{
								this.skinRemapper = new MTLTextureRemapper(model, ResLoc.of(IIReference.RES_TEXTURES_SKIN, skin, "/rifle").withExtension(ResLoc.EXT_MTL));
								return ClientUtils.getSprite(this.skinRemapper.apply(res));
							}

							if(ItemNBTHelper.hasKey(stack, ItemIIRifle.HANDMADE))
								return ClientUtils.getSprite(handmadeRemapper.apply(res));
							return ClientUtils.getSprite(res);
						}
				)
				.build();

		this.bullet = new AMTCrossVariantReference<>("bullet", this.model);
		this.casing = new AMTCrossVariantReference<>("casing_fired", this.model);

		//add upgrade visibility animations
		loadUpgrades(model, ResLoc.of(animationRes, "upgrades/"));

		loadBullet = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "load_bullet"));
		loadMag = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "load_magazine"));
		unloadMag = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "unload_magazine"));

		fireBoltAction = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "fire_bolt_action"));
		fireSemiAutomatic = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "fire_semiauto"));
		handAngle = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "hand"));
		handVisibility = IIAnimationCachedMap.create(this.model, new ResourceLocation(ImmersiveIntelligence.MODID, "gun_hand_visibility"));
		offhandVisibility = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "offhand"));
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		super.registerSprites(map);
		ApiUtils.getRegisterSprite(map, new ResourceLocation(ImmersiveIntelligence.MODID, "items/weapons/rifle_handmade"));
		// Register skins
		IISkinHandler.registerSprites(map, IIContent.itemRifle.getSkinnableName());
	}

	@Override
	public boolean doHandRender(ItemStack stack, EnumHand hand, ItemStack otherHand, float swingProgress, float partialTicks)
	{
		//Render bayonet attack
		this.swingProgress = swingProgress;
		return hand==EnumHand.OFF_HAND&&otherHand.getItem() instanceof ItemIIGunBase;
	}

	@Override
	public boolean shouldCancelCrosshair(ItemStack stack, EnumHand hand)
	{
		if(item.hasIIUpgrade(stack, WeaponUpgrade.SCOPE))
			return false;

		return ItemNBTHelper.getInt(stack, ItemIIRifle.AIMING) > item.getAimingTime(stack, EasyNBT.wrapNBT(item.getUpgrades(stack)))*0.85;
	}
}
