package pl.pabilo8.immersiveintelligence.common.item.ammo.grenade;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PropellantType;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoFactory;
import pl.pabilo8.immersiveintelligence.client.model.builtin.IAmmoModel;
import pl.pabilo8.immersiveintelligence.client.model.builtin.ModelAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Ammunition;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Grenade;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoGrenade;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase.AmmoParts;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;
import pl.pabilo8.modworks.annotations.item.ItemModelType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
@GeneratedItemModels(itemName = "bullet_grenade_5bcal", type = ItemModelType.ITEM_SIMPLE_AUTOREPLACED, valueSet = AmmoParts.class)
@IIItemProperties(category = IICategory.WARFARE)
public class ItemIIAmmoGrenade extends ItemIIAmmoBase<EntityAmmoGrenade>
{
	public ItemIIAmmoGrenade()
	{
		super("grenade_5bCal", null);
		setMaxStackSize(8);
	}

	@Override
	public float getComponentMultiplier()
	{
		return 0.45f;
	}

	@Override
	public PropellantType getAllowedPropellants()
	{
		return PropellantType.NONE;
	}

	@Override
	public int getCoreMaterialNeeded()
	{
		return 2;
	}

	@Override
	public float getCasingMass()
	{
		return 0.2f;
	}

	@Override
	public float getVelocity()
	{
		return Ammunition.grenadeVelocity;
	}

	@Override
	public int getCaliber()
	{
		return 5;
	}

	@Override
	public float getPenetrationDepth()
	{
		return 1;
	}

	@SideOnly(Side.CLIENT)
	@Nonnull
	@Override
	public Function<ItemIIAmmoBase<EntityAmmoGrenade>, IAmmoModel<ItemIIAmmoBase<EntityAmmoGrenade>, EntityAmmoGrenade>> get3DModel()
	{
		return ModelAmmoProjectile::createGrenadeModel;
	}

	@Override
	public float getDamage()
	{
		return 5;
	}

	@Override
	public ItemStack getCasingStack(int amount)
	{
		//stick
		return new ItemStack(IEContent.itemMaterial, amount, 0);
	}

	@Override
	public CoreType[] getAllowedCoreTypes()
	{
		return new CoreType[]{CoreType.CANISTER};
	}

	@Override
	public FuseType[] getAllowedFuseTypes()
	{
		return new FuseType[]{FuseType.CONTACT, FuseType.TIMED};
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/base");
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/core");
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/core_classic");
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/core_disp");
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/paint");
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/core_disp_classic");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColourForIEItem(ItemStack stack, int pass)
	{
		if(pass!=1)
			return super.getColourForIEItem(stack, pass);
		else
			return Grenade.classicGrenades > 1?0xffffffff: super.getColourForIEItem(stack, pass);
	}

	@Override
	public String getModelCacheKey(ItemStack stack)
	{
		return String.format("%s%s_%s%s", stackToSub(stack).getMeta(), NAME, getPaintColor(stack)==null?"no_": "paint_", Grenade.classicGrenades);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List<ResourceLocation> getTextures(ItemStack stack, String key)
	{
		ArrayList<ResourceLocation> a = new ArrayList<>();
		switch(stackToSub(stack))
		{
			case CORE:
			{
				if(Grenade.classicGrenades < 2)
					a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/core"));
				else
					a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/core_classic"));
			}
			break;
			case BULLET:
			{
				a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/base"));
				if(Grenade.classicGrenades < 2)
					a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/core_disp"));
				else
					a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/core_disp_classic"));
				if(getPaintColor(stack)!=null)
					a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/paint"));
			}
			break;
		}
		return a;
	}

	@Override
	@Nonnull
	public EnumAction getItemUseAction(@Nonnull ItemStack stack)
	{
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(@Nonnull ItemStack stack)
	{
		return 60;
	}

	@Override
	@Nonnull
	public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand)
	{
		ItemStack itemstack = player.getHeldItem(hand);
		player.setActiveHand(hand);
		return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
	}

	@Override
	public void onPlayerStoppedUsing(@Nonnull ItemStack stack, World world, @Nonnull EntityLivingBase entity, int timeLeft)
	{
		if(world.isRemote)
			return;

		//throwing sound
		world.playSound(null, entity.posX, entity.posY, entity.posZ, IISounds.grenadeThrow, SoundCategory.PLAYERS, 1f, 1f);

		//calculate the position and direction
		Vec3d vec = IIMath.getVectorForRotation(entity.rotationPitch, entity.getRotationYawHead());
		Vec3d vv = entity.getPositionVector().addVector(0, (double)entity.getEyeHeight()-0.10000000149011612D, 0);

		//spawn the grenade
		new AmmoFactory<>(world)
				.setStack(stack)
				.setOwner(entity)
				.setPosition(vv)
				.setDirection(vec)
				.setVelocityModifier(
						Math.min(((float)this.getMaxItemUseDuration(stack)-timeLeft)/(float)this.getMaxItemUseDuration(stack), 35)*Grenade.throwSpeedModifier
				).create();

		//take ammo from player if not in creative mode
		if(!(entity instanceof EntityPlayer)||!((EntityPlayer)entity).capabilities.isCreativeMode)
			stack.shrink(1);
	}

	@Nonnull
	@Override
	public EntityAmmoGrenade getAmmoEntity(World world)
	{
		return new EntityAmmoGrenade(world);
	}
}
