package pl.pabilo8.immersiveintelligence.common.items;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCasingType;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletComponent;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Pabilo8 on 2019-05-11.
 */
public class ItemIIBullet extends ItemIIBase
{
	public ItemIIBullet()
	{
		super("bullet", 32);
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		super.onCreated(stack, worldIn, playerIn);
		makeDefault(stack);
		//TODO: TEISR
		//setTileEntityItemStackRenderer();
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
			tooltip.add(
					I18n.format(ImmersiveIntelligence.proxy.description_key+"bullet_type."+getFirstComponent(stack).getRole().getName())+
							(getSecondComponent(stack)!=null&&getSecondComponent(stack)!=getFirstComponent(stack)?" - "+I18n.format(ImmersiveIntelligence.proxy.description_key+"bullet_type."+getSecondComponent(stack).getRole().getName()): "")
			);

			tooltip.add(I18n.format(ImmersiveIntelligence.proxy.description_key+"bullets.mass", getMass(stack)));

		}

		if(getCasing(stack)!=null)
		{
			tooltip.add(I18n.format(ImmersiveIntelligence.proxy.description_key+"bullets.caliber", getCasing(stack).getSize()));
		}
	}

	public static void makeDefault(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, "casing"))
			ItemNBTHelper.setString(stack, "casing", "artillery_8bCal");
		if(!ItemNBTHelper.hasKey(stack, "firstComponent"))
			ItemNBTHelper.setString(stack, "firstComponent", "empty");
		if(!ItemNBTHelper.hasKey(stack, "firstComponentQuantity"))
			ItemNBTHelper.setString(stack, "firstComponentQuantity", "0");
		if(!ItemNBTHelper.hasKey(stack, "secondComponent"))
			ItemNBTHelper.setString(stack, "secondComponent", "empty");
		if(!ItemNBTHelper.hasKey(stack, "secondComponentQuantity"))
			ItemNBTHelper.setString(stack, "secondComponentQuantity", "0");
		if(!ItemNBTHelper.hasKey(stack, "firstComponentNBT"))
			ItemNBTHelper.setTagCompound(stack, "firstComponentNBT", new NBTTagCompound());
		if(!ItemNBTHelper.hasKey(stack, "secondComponentNBT"))
			ItemNBTHelper.setTagCompound(stack, "secondComponentNBT", new NBTTagCompound());
	}

	public static IBulletCasingType getCasing(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, "casing"))
			makeDefault(stack);
		return BulletRegistry.INSTANCE.getCasing(ItemNBTHelper.getString(stack, "casing"));
	}

	public static boolean hasFirstComponent(ItemStack stack)
	{
		return ItemNBTHelper.hasKey(stack, "firstComponent")&&BulletRegistry.INSTANCE.getComponent(ItemNBTHelper.getString(stack, "firstComponent"))!=null;
	}

	public static boolean hasSecondComponent(ItemStack stack)
	{
		return ItemNBTHelper.hasKey(stack, "secondComponent")&&BulletRegistry.INSTANCE.getComponent(ItemNBTHelper.getString(stack, "secondComponent"))!=null;
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
				(getFirstComponent(stack)!=null?(getFirstComponent(stack).getDensity()*getFirstComponentQuantity(stack)): 0)+
				(getSecondComponent(stack)!=null?(getSecondComponent(stack).getDensity()*getSecondComponentQuantity(stack)): 0);
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
		return getCasing(stack)!=null?I18n.format(ImmersiveIntelligence.MODID+".bullet."+getCasing(stack).getName()+".name"): ImmersiveIntelligence.MODID+".item.bullet.null";
	}

	public static ItemStack getAmmoStack(int amount, String casing, String component1, String component2, float proportion)
	{
		proportion = Math.min(Math.max(proportion, 0f), 1f);
		ItemStack stack = new ItemStack(ImmersiveIntelligence.proxy.item_bullet, amount);
		makeDefault(stack);

		if(BulletRegistry.INSTANCE.getCasing(casing)!=null)
		{
			ItemNBTHelper.setString(stack, "casing", casing);
		}
		else
			return ItemStack.EMPTY;

		if(!component1.equals("empty"))
		{
			ItemNBTHelper.setString(stack, "firstComponent", component1);
			ItemNBTHelper.setFloat(stack, "firstComponentQuantity", proportion);
		}
		if(!component2.equals("empty"))
		{
			ItemNBTHelper.setString(stack, "secondComponent", component2);
			ItemNBTHelper.setFloat(stack, "secondComponentQuantity", 1f-proportion);
		}

		return stack;
	}

}
