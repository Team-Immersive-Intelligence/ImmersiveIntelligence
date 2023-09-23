package pl.pabilo8.immersiveintelligence.common.item.ammo;

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
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Ammunition;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Grenade;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumFuseTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoUtils;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.bullet.ModelGrenade;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase.AmmoParts;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;
import pl.pabilo8.modworks.annotations.item.ItemModelType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
@GeneratedItemModels(itemName = "bullet_grenade_5bcal", type = ItemModelType.ITEM_SIMPLE_AUTOREPLACED, valueSet = AmmoParts.class)
public class ItemIIAmmoGrenade extends ItemIIAmmoBase
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
	public int getCoreMaterialNeeded()
	{
		return 2;
	}

	@Override
	public float getInitialMass()
	{
		return 0.25f;
	}

	@Override
	public float getDefaultVelocity()
	{
		return Ammunition.grenadeVelocity;
	}

	@Override
	public float getCaliber()
	{
		return 5f;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public @Nonnull
	Class<? extends IBulletModel> getModel()
	{
		return ModelGrenade.class;
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
	public EnumCoreTypes[] getAllowedCoreTypes()
	{
		return new EnumCoreTypes[]{EnumCoreTypes.CANISTER};
	}

	@Override
	public EnumFuseTypes[] getAllowedFuseTypes()
	{
		return new EnumFuseTypes[]{EnumFuseTypes.CONTACT, EnumFuseTypes.TIMED};
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
		return String.format("%s%s_%s%s", stackToSub(stack).getMeta(), NAME, getPaintColor(stack)==-1?"no_": "paint_", Grenade.classicGrenades);
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
				if(getPaintColor(stack)!=-1)
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
		if(!world.isRemote)
		{
			world.playSound(null, entity.posX, entity.posY, entity.posZ, IISounds.grenadeThrow, SoundCategory.PLAYERS, 1f, 1f);

			Vec3d vec = IIUtils.getVectorForRotation(entity.rotationPitch, entity.getRotationYawHead());
			Vec3d vv = entity.getPositionVector().addVector(0, (double)entity.getEyeHeight()-0.10000000149011612D, 0);

			EntityBullet a = AmmoUtils.createBullet(world, stack, vv, vec, Math.min((((float)this.getMaxItemUseDuration(stack)-timeLeft)/(float)this.getMaxItemUseDuration(stack)), 35)*Grenade.throwSpeedModifier);
			a.setShooters(entity);
			a.fuse = (int)(60f/EntityBullet.DEV_SLOMO);
			world.spawnEntity(a);

			if(!(entity instanceof EntityPlayer)||!((EntityPlayer)entity).capabilities.isCreativeMode)
			{
				stack.shrink(1);
			}
		}
	}
}
