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
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticles;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTBullet.BulletState;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIItemRendererAMT.RegisteredItemRenderer;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.AssaultRifle;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIGunBase;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIISubmachinegun;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IISkinHandler;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * @author Pabilo8
 * @updated 23.12.2023
 * @since 13-10-2019
 */
@RegisteredItemRenderer(name = "items/weapons/submachinegun")
public class SubmachinegunRenderer extends IIUpgradableItemRendererAMT<ItemIISubmachinegun> implements ISpecificHandRenderer
{
	//Animations
	IIAnimationCachedMap fire, load, unload, handAngle, offHandAngle, foldingStock;
	IIAnimationCachedMap loadBottom, loadBottomDrum, unloadBottom, unloadBottomDrum;

	//Model Utils
	private MTLTextureRemapper skinRemapper;

	//Parts
	private AMTCrossVariantReference<AMT> hand, offHand;
	private AMTCrossVariantReference<AMTParticle> muzzleFlash;
	private AMTCrossVariantReference<AMTBullet> casingFired;

	public SubmachinegunRenderer()
	{
		super(IIContent.itemSubmachinegun, ResLoc.of(RES_MODEL_WEAPON, "submachinegun"));
	}

	@Override
	protected ItemModelReplacement setTransforms(ItemModelReplacement_OBJ model)
	{
		Matrix4 tpp = new Matrix4()
				.scale(0.425, 0.425, 0.425)
				.rotate(Math.toRadians(-20.5f), 0, 1, 0)
				.translate(0.625f, -1.125, 0.65f);
		Matrix4 tppOffhand = new Matrix4()
				.scale(0.385, 0.385, 0.385)
				.rotate(Math.toRadians(75f), 1, 0, 0)
				.rotate(Math.toRadians(20.5f), 0, 0, 1)
				.rotate(Math.toRadians(90f), 0, 1, 0)
				.translate(-0.5f, -.25, .125);

		Matrix4 fpp = new Matrix4()
				.scale(0.75, 0.75, 0.75)
				.translate(1f-0.25f, -1f, 0)
				.rotate(Math.toRadians(7.5f), 0, 1, 0)
				.rotate(Math.toRadians(5), 1, 0, 0)
				.translate(-0.125f, 0, 0.125f);
		Matrix4 fppOffhand = new Matrix4()
				.scale(0.55, 0.55, 0.55)
				.translate(1f-0.25f, -1f, 0)
				.rotate(Math.toRadians(82.5), 0, 1, 0)
				.rotate(Math.toRadians(2.5), 1, 0, 0)
				.translate(0, 0, -0.5f);

		return model
				.setTransformations(TransformType.GROUND, new Matrix4()
						.scale(0.425, 0.425, 0.425)
						.translate(0.5, -0.75, 1))
				.setTransformations(TransformType.THIRD_PERSON_RIGHT_HAND, tpp)
				.setTransformations(TransformType.THIRD_PERSON_LEFT_HAND, tppOffhand)
				.setTransformations(TransformType.FIXED, new Matrix4()
						.rotate(Math.toRadians(-3.5), 1, 0, 0)
						.rotate(Math.toRadians(-75), 0, 1, 0)
						.translate(0.125, -0.25, -0.125)
						.scale(0.425, 0.425, 0.425))
				.setTransformations(TransformType.GUI, new Matrix4()
						.translate(0, -0.25, 0)
						.scale(0.5, 0.5, 0.5)
						.rotate(Math.toRadians(35), 1, 0, 0)
						.rotate(Math.toRadians(135), 0, 1, 0)
						.translate(0, 0, 0.325)
				)
				.setTransformations(TransformType.FIRST_PERSON_RIGHT_HAND, fpp)
				.setTransformations(TransformType.FIRST_PERSON_LEFT_HAND, fppOffhand);
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		super.registerSprites(map);
		IISkinHandler.registerSprites(map, IIContent.itemSubmachinegun.getSkinnableName());
	}

	@Override
	public void draw(ItemStack stack, TransformType transform, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		EasyNBT nbt = EasyNBT.wrapNBT(stack);

		model.getVariant(nbt.getString(IISkinHandler.NBT_ENTRY), stack);
		model.forEach(AMT::defaultize);

		//Make upgrade AMTs visible
		showUpgrades(stack, nbt);

		//magazine stack
		ItemStack magazine = nbt.getItemStack(ItemIISubmachinegun.MAGAZINE);

		int firing = nbt.getInt(ItemIISubmachinegun.FIRE_DELAY);
		int firingDelay = item.getFireDelay(stack, nbt);
		int reloading = nbt.getInt(ItemIISubmachinegun.RELOADING);
		fire.apply((1f-(Math.max(0, firing-partialTicks)/firingDelay)));

		//Whether hand should be rendered
		boolean handRender = is1stPerson(transform);

		//hand should be visible only in 1st person mode
		IIAnimationUtils.setModelVisibility(hand.get(), handRender);
		IIAnimationUtils.setModelVisibility(offHand.get(), handRender);


		//Aiming animation
		int aiming = nbt.getInt(ItemIISubmachinegun.AIMING);
		EasyNBT upgradeNBT = EasyNBT.wrapNBT(item.getUpgrades(stack));
		float preciseAim = IIAnimationUtils.getAnimationProgress(aiming, item.getAimingTime(stack, upgradeNBT),
				true, !Minecraft.getMinecraft().player.isSneaking(),
				1, 3,
				partialTicks);
		//first-person perspective
		if(handRender)
		{
			if(preciseAim > 0)
			{
				//gun "push" towards player
				float recoil = Math.min((nbt.getFloat(ItemIISubmachinegun.RECOIL_V)+nbt.getFloat(ItemIISubmachinegun.RECOIL_H))/(AssaultRifle.maxRecoilHorizontal+AssaultRifle.maxRecoilVertical), 1f);

				GlStateManager.translate(-preciseAim*(1-0.125-0.0625/3), 0.15*preciseAim, 0);
				GlStateManager.rotate(preciseAim*-7.75f, 0, 1, 0);
				GlStateManager.rotate(preciseAim*-5f, 1, 0, 0);

				GlStateManager.translate(0, 0, preciseAim*0.25);

				if(recoil > 0)
					GlStateManager.translate(0, -recoil*(0.155-0.1*preciseAim), recoil*0.25);
			}
			(transform==TransformType.FIRST_PERSON_RIGHT_HAND?handAngle: offHandAngle).apply(preciseAim);
		}
		if(item.hasIIUpgrade(stack, WeaponUpgrade.FOLDING_STOCK)&&preciseAim > 0)
			foldingStock.apply(preciseAim);


		//Don't show muzzle flash GUI
		if(transform==TransformType.GUI)
		{
			IIAnimationUtils.setModelVisibility(muzzleFlash.get(), false);
			IIAnimationUtils.setModelVisibility(casingFired.get(), false);
		}

		float v = IIAnimationUtils.getAnimationProgress(
				reloading,
				(float)item.getReloadTime(stack, ItemStack.EMPTY, EasyNBT.wrapNBT(item.getUpgrades(stack))),
				false,
				reloading > 0?partialTicks: 0
		);

		if(item.hasIIUpgrade(stack, WeaponUpgrade.BOTTOM_LOADING))
		{
			if(nbt.hasKey("isDrum"))
				(magazine.isEmpty()?loadBottomDrum: unloadBottomDrum).apply(v);
			else
				(magazine.isEmpty()?loadBottom: unloadBottom).apply(v);
		}
		else
			(magazine.isEmpty()?load: unload).apply(v);

		//Reloading animation
		if(reloading > 0)
		{
			float rpart = v <= 0.33?v/0.33f: v <= 0.66?1f: 1f-(v-0.66f)/0.33f;
			//Rotate the gun held 80 degrees during reload
			if(handRender)
			{
				GlStateManager.rotate(rpart*80f, 0, 1, 0);
				GlStateManager.rotate(rpart*40f, 0, 0, 1);
				GlStateManager.translate(rpart, -rpart*0.75, -rpart*0.25);
			}
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
				.withHeader(ResLoc.of(this.directoryRes, "submachinegun_upgrades.obj.amt"))
				.withModelProvider(
						(stack, combinedHeader) -> new AMT[]{
								//Main Model
								new AMTParticle("muzzle_flash", combinedHeader)
										.setParticle(IIParticles.PARTICLE_GUNFIRE),
								new AMTHand("hand_main", combinedHeader, EnumHand.OFF_HAND),
								new AMTHand("hand_off", combinedHeader, EnumHand.OFF_HAND),

								new AMTBullet("casing_fired", combinedHeader, AmmoRegistry.getModel(IIContent.itemAmmoSubmachinegun))
										.withState(BulletState.CASING),
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

		this.muzzleFlash = new AMTCrossVariantReference<>("muzzle_flash", this.model);
		this.hand = new AMTCrossVariantReference<>("hand_main", this.model);
		this.offHand = new AMTCrossVariantReference<>("hand_off", this.model);
		this.casingFired = new AMTCrossVariantReference<>("casing_fired", this.model);


		//Add upgrade visibility animations
		loadUpgrades(model, ResLoc.of(animationRes, "upgrades/"));

		load = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "load"));
		unload = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "unload"));
		fire = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "fire"));

		loadBottom = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "load_bottom"));
		unloadBottom = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "unload_bottom"));
		loadBottomDrum = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "load_bottom_drum"));
		unloadBottomDrum = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "unload_bottom_drum"));

		handAngle = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "hand"));
		offHandAngle = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "offhand"));

		foldingStock = IIAnimationCachedMap.create(this.model, ResLoc.of(animationRes, "folding_stock"));
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
		return ItemNBTHelper.getInt(stack, ItemIISubmachinegun.AIMING) >= item.getAimingTime(stack, EasyNBT.wrapNBT(item.getUpgrades(stack)))*0.85;
	}
}
