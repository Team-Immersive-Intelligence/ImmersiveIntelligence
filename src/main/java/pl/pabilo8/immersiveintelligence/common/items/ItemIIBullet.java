package pl.pabilo8.immersiveintelligence.common.items;

import blusunrize.immersiveengineering.common.items.IEItemInterfaces.ITextureOverride;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCasingType;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletComponent;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCoreType;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.entity.EntityBullet;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Pabilo8 on 2019-05-11.
 */
public class ItemIIBullet extends ItemIIBase implements ITextureOverride
{
	public ItemIIBullet()
	{
		super("bullet", 32);
	}

	public static void makeDefault(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, "casing"))
			ItemNBTHelper.setString(stack, "casing", "artillery_8bCal");
		if(!ItemNBTHelper.hasKey(stack, "core"))
			ItemNBTHelper.setString(stack, "core", "CoreBrass");
		if(!ItemNBTHelper.hasKey(stack, "firstComponent"))
			ItemNBTHelper.setString(stack, "firstComponent", "empty");
		if(!ItemNBTHelper.hasKey(stack, "firstComponentQuantity"))
			ItemNBTHelper.setFloat(stack, "firstComponentQuantity", 0);
		if(!ItemNBTHelper.hasKey(stack, "secondComponent"))
			ItemNBTHelper.setString(stack, "secondComponent", "empty");
		if(!ItemNBTHelper.hasKey(stack, "secondComponentQuantity"))
			ItemNBTHelper.setFloat(stack, "secondComponentQuantity", 0);
		if(!ItemNBTHelper.hasKey(stack, "firstComponentNBT"))
			ItemNBTHelper.setTagCompound(stack, "firstComponentNBT", new NBTTagCompound());
		if(!ItemNBTHelper.hasKey(stack, "secondComponentNBT"))
			ItemNBTHelper.setTagCompound(stack, "secondComponentNBT", new NBTTagCompound());
		if(!ItemNBTHelper.hasKey(stack, "colour"))
			ItemNBTHelper.setInt(stack, "colour", 0);
	}

	public static IBulletCasingType getCasing(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, "casing"))
			makeDefault(stack);
		return BulletRegistry.INSTANCE.getCasing(ItemNBTHelper.getString(stack, "casing"));
	}

	public static IBulletCoreType getCore(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, "core"))
			makeDefault(stack);
		return BulletRegistry.INSTANCE.getCore(ItemNBTHelper.getString(stack, "core"));
	}

	public static boolean hasFirstComponent(ItemStack stack)
	{
		return ItemNBTHelper.hasKey(stack, "firstComponent")&&BulletRegistry.INSTANCE.getComponent(ItemNBTHelper.getString(stack, "firstComponent"))!=null;
	}

	public static boolean hasSecondComponent(ItemStack stack)
	{
		return ItemNBTHelper.hasKey(stack, "secondComponent")&&BulletRegistry.INSTANCE.getComponent(ItemNBTHelper.getString(stack, "secondComponent"))!=null;
	}

	public static boolean hasCore(ItemStack stack)
	{
		return ItemNBTHelper.hasKey(stack, "core")&&BulletRegistry.INSTANCE.getCore(ItemNBTHelper.getString(stack, "core"))!=null;
	}

	public static boolean hasColour(ItemStack stack)
	{
		return ItemNBTHelper.hasKey(stack, "colour");
	}

	public static int getColour(ItemStack stack)
	{
		return ItemNBTHelper.getInt(stack, "colour");
	}

	public static IBulletComponent getFirstComponent(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, "firstComponent"))
			makeDefault(stack);
		return BulletRegistry.INSTANCE.getComponent(ItemNBTHelper.getString(stack, "firstComponent"));
	}

	public static IBulletComponent getSecondComponent(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, "secondComponent"))
			makeDefault(stack);
		return BulletRegistry.INSTANCE.getComponent(ItemNBTHelper.getString(stack, "secondComponent"));
	}

	public static float getFirstComponentQuantity(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, "firstComponentQuantity"))
			makeDefault(stack);
		return ItemNBTHelper.getFloat(stack, "firstComponentQuantity");
	}

	public static float getSecondComponentQuantity(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, "secondComponentQuantity"))
			makeDefault(stack);
		return ItemNBTHelper.getFloat(stack, "secondComponentQuantity");
	}

	public static NBTTagCompound getFirstComponentNBT(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, "firstComponentNBT"))
			makeDefault(stack);
		return ItemNBTHelper.getTagCompound(stack, "firstComponentNBT");
	}

	public static NBTTagCompound getSecondComponentNBT(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, "secondComponentNBT"))
			makeDefault(stack);
		return ItemNBTHelper.getTagCompound(stack, "secondComponentNBT");
	}

	public static float getMass(ItemStack stack)
	{
		return getCasing(stack).getInitialMass()+
				(getCore(stack)!=null?getCore(stack).getDensity()*getCasing(stack).getInitialMass(): 0)+
				(getFirstComponent(stack)!=null?getCasing(stack).getComponentCapacity()*(getFirstComponent(stack).getDensity()*getFirstComponentQuantity(stack)): 0)+
				(getSecondComponent(stack)!=null?getCasing(stack).getComponentCapacity()*(getSecondComponent(stack).getDensity()*getSecondComponentQuantity(stack)): 0);
	}

	public static ItemStack getAmmoStack(int amount, String casing, String core, String component1, String component2, float proportion, float amount1, float amount2)
	{
		proportion = Math.min(Math.max(proportion, 0f), 1f);
		ItemStack stack = new ItemStack(CommonProxy.item_bullet, amount);
		makeDefault(stack);

		if(BulletRegistry.INSTANCE.getCasing(casing)!=null)
		{
			ItemNBTHelper.setString(stack, "casing", casing);
		}
		else
			return ItemStack.EMPTY;

		if(core!=null&&!core.equals("empty"))
		{
			ItemNBTHelper.setString(stack, "core", core);
		}
		if(!component1.equals("empty"))
		{
			ItemNBTHelper.setString(stack, "firstComponent", component1);
			ItemNBTHelper.setFloat(stack, "firstComponentQuantity", proportion*amount1);
		}
		if(!component2.equals("empty"))
		{
			ItemNBTHelper.setString(stack, "secondComponent", component2);
			ItemNBTHelper.setFloat(stack, "secondComponentQuantity", (1f-proportion)*amount2);
		}

		ItemNBTHelper.setInt(stack, "colour", Color.HSBtoRGB((float)Math.random(), 0.9f, 0.45f));

		return stack;
	}

	public static ItemStack getAmmoStack(int amount, String casing, String core, String component1, String component2, float proportion)
	{
		return getAmmoStack(amount, casing, core, component1, component2, proportion, 1f, 1f);
	}

	public static int getTrailColour(ItemStack stack)
	{
		int col1 = -1, col2 = -1;
		if(hasFirstComponent(stack)&&getFirstComponent(stack).hasTrail())
		{
			col1 = getFirstComponent(stack).getTrailColour(getFirstComponentNBT(stack));
		}
		if(hasSecondComponent(stack)&&getSecondComponent(stack).hasTrail())
		{
			col2 = getSecondComponent(stack).getTrailColour(getSecondComponentNBT(stack));
		}

		return col1!=-1?col1: col2;
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		super.onCreated(stack, worldIn, playerIn);
		makeDefault(stack);
	}

	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return getCasing(stack)!=null?getCasing(stack).getStackSize(): 32;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if(getFirstComponent(stack)!=null)
		{
			String core_name = "", comp1_name = "", comp2_name = "";

			if(getCore(stack)!=null)
			{
				core_name = I18n.format(CommonProxy.description_key+"bullet_type."+getCore(stack).getRole().getName());
				tooltip.add(I18n.format(CommonProxy.description_key+"bullets.core", I18n.format("item."+ImmersiveIntelligence.MODID+".bullet.component."+getCore(stack).getName()+".name")));
			}
			if(getFirstComponent(stack)!=null)
			{
				comp1_name = I18n.format(CommonProxy.description_key+"bullet_type."+getFirstComponent(stack).getRole().getName());
			}
			if(getSecondComponent(stack)!=null)
			{
				comp2_name = I18n.format(CommonProxy.description_key+"bullet_type."+getSecondComponent(stack).getRole().getName());
			}

			tooltip.add(core_name+(!comp1_name.isEmpty()&&!comp1_name.equals(core_name)?" - "+comp1_name: "")+(!comp2_name.isEmpty()&&!comp2_name.equals(core_name)&&!comp2_name.equals(comp1_name)?" - "+comp2_name: ""));

			tooltip.add(I18n.format(CommonProxy.description_key+"bullets.mass", getMass(stack)));

		}

		if(getCasing(stack)!=null)
		{
			tooltip.add(I18n.format(CommonProxy.description_key+"bullets.caliber", getCasing(stack).getSize()*16f));
		}
	}

	@Nullable
	@Override
	public CreativeTabs getCreativeTab()
	{
		return null;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return getCasing(stack)!=null?I18n.format("item."+ImmersiveIntelligence.MODID+".bullet."+getCasing(stack).getName()+".name"): ImmersiveIntelligence.MODID+".item.bullet.null";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getModelCacheKey(ItemStack stack)
	{
		if(ItemNBTHelper.hasKey(stack, "casing"))
		{
			return ItemNBTHelper.getString(stack, "casing").toLowerCase();
		}
		return "bullet";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List<ResourceLocation> getTextures(ItemStack stack, String key)
	{
		return Arrays.asList(
				new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/bullet_"+key+"_main"),
				new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/bullet_"+key+"_core"),
				new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/bullet_"+key+"_paint")
		);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomItemColours()
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColourForIEItem(ItemStack stack, int pass)
	{
		switch(pass)
		{
			case 1:
			{
				if(hasCore(stack))
					return getCore(stack).getColour();
			}
			break;
			case 2:
			{
				if(hasColour(stack))
					return getColour(stack);
			}
			break;
		}
		return 0xffffffff;
	}

	public boolean isGrenade(ItemStack stack)
	{
		return getCasing(stack).isThrowable();
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return isGrenade(stack)?EnumAction.BOW: EnumAction.NONE;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return Math.round(60f*getMass(stack));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if(isGrenade(player.getHeldItem(hand)))
		{
			ItemStack itemstack = player.getHeldItem(hand);
			player.setActiveHand(hand);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
		}
		ImmersiveIntelligence.logger.info("fail!");
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, player.getHeldItem(hand));
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft)
	{
		if(!world.isRemote)
		{
			double true_angle = Math.toRadians(360f-entityLiving.rotationYawHead);
			double true_angle2 = Math.toRadians(-(entityLiving.rotationPitch));

			Vec3d gun_end = pl.pabilo8.immersiveintelligence.api.Utils.offsetPosDirection(1f, true_angle, true_angle2);

			EntityBullet a = new EntityBullet(world, entityLiving.posX+gun_end.x, entityLiving.posY+entityLiving.getEyeHeight()+gun_end.y, entityLiving.posZ+gun_end.z, entityLiving, stack);
			//blocks per tick
			float distance = 0.65f*(((float)this.getMaxItemUseDuration(stack)-timeLeft)/(float)this.getMaxItemUseDuration(stack));
			a.motionX = distance*(gun_end.x);
			a.motionY = distance*(gun_end.y);
			a.motionZ = distance*(gun_end.z);
			ImmersiveIntelligence.logger.info(entityLiving.rotationPitch);
			a.world.spawnEntity(a);

			if(!(entityLiving instanceof EntityPlayer)||!((EntityPlayer)entityLiving).capabilities.isCreativeMode)
			{
				stack.shrink(1);
			}
		}
	}
}
