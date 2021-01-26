package pl.pabilo8.immersiveintelligence.common.items;

import blusunrize.immersiveengineering.common.items.IEItemInterfaces.ITextureOverride;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.IBullet;
import pl.pabilo8.immersiveintelligence.common.IIContent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 01-11-2019
 */
public class ItemIIBulletMagazine extends ItemIIBase implements ITextureOverride
{
	public ItemIIBulletMagazine()
	{
		super("bullet_magazine", 1, "machinegun", "submachinegun", "automatic_revolver", "submachinegun_drum", "assault_rifle");
	}

	public static void makeDefault(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, "bullets"))
		{
			NonNullList<ItemStack> l = NonNullList.withSize(getBulletCapactity(stack), ItemStack.EMPTY);
			ItemNBTHelper.getTag(stack).setTag("bullets", Utils.writeInventory(l));

			if(!ItemNBTHelper.hasKey(stack, "colour0"))
				ItemNBTHelper.setInt(stack, "colour0", -1);
			if(!ItemNBTHelper.hasKey(stack, "colour1"))
				ItemNBTHelper.setInt(stack, "colour1", -1);
			if(!ItemNBTHelper.hasKey(stack, "colour2"))
				ItemNBTHelper.setInt(stack, "colour2", -1);
			if(!ItemNBTHelper.hasKey(stack, "colour3"))
				ItemNBTHelper.setInt(stack, "colour3", -1);
		}
		else
		{
			int bc = getBulletCapactity(stack);
			NonNullList<ItemStack> cartridge = Utils.readInventory(ItemNBTHelper.getTag(stack).getTagList("bullets", 10), bc);
			ArrayList<ItemStack> already = new ArrayList<>();
			int i = 0;
			for(ItemStack bullet : cartridge)
			{
				if(bullet.isEmpty()||i > 3)
					break;

				boolean contains = false;
				for(ItemStack s : already)
				{
					if(ItemStack.areItemStacksEqual(bullet, s))
					{
						contains = true;
						break;
					}
				}
				if(!contains)
				{
					already.add(bullet);
					ItemNBTHelper.setInt(stack, "colour"+i, ((IBullet)bullet.getItem()).getPaintColor(bullet));
					ItemNBTHelper.setTagCompound(stack, "bullet"+i, bullet.serializeNBT());
					i += 1;
				}
			}

			if(already.size() < 1)
			{
				ItemNBTHelper.remove(stack, "bullet0");
				ItemNBTHelper.setInt(stack, "colour0", -1);
			}
			if(already.size() < 2)
			{
				ItemNBTHelper.remove(stack, "bullet1");
				ItemNBTHelper.setInt(stack, "colour1", -1);
			}
			if(already.size() < 3)
			{
				ItemNBTHelper.remove(stack, "bullet2");
				ItemNBTHelper.setInt(stack, "colour2", -1);
			}
			if(already.size() < 4)
			{
				ItemNBTHelper.remove(stack, "bullet3");
				ItemNBTHelper.setInt(stack, "colour3", -1);
			}
		}
	}

	public static ItemStack takeBullet(ItemStack stack)
	{
		if(ItemNBTHelper.hasKey(stack, "bullets"))
		{
			int bc = getBulletCapactity(stack);
			NonNullList<ItemStack> cartridge = Utils.readInventory(ItemNBTHelper.getTag(stack).getTagList("bullets", 10), bc);
			ItemStack ammo = cartridge.get(0).copy();
			cartridge.set(0, ItemStack.EMPTY);
			for(int i = 1; i < bc; i += 1)
			{
				if(cartridge.get(i-1).isEmpty())
				{
					cartridge.set(i-1, cartridge.get(i));
					cartridge.set(i, ItemStack.EMPTY);
				}
			}
			NBTTagList list = Utils.writeInventory(cartridge);
			ItemNBTHelper.getTag(stack).setTag("bullets", list);
			return ammo;
		}
		else
		{
			makeDefault(stack);
			return ItemStack.EMPTY;
		}
	}

	public static boolean hasNoBullets(ItemStack stack)
	{
		int bc = getBulletCapactity(stack);
		NonNullList<ItemStack> cartridge = Utils.readInventory(ItemNBTHelper.getTag(stack).getTagList("bullets", 10), bc);
		return cartridge.get(0).isEmpty();
	}

	public static int getBulletCapactity(ItemStack stack)
	{
		switch(stack.getMetadata())
		{
			case 0:
				return 48;
			case 1:
				return 24;
			case 2:
				return 16;
			case 3:
				return 64;
			case 4:
				return 32;
		}
		return 0;
	}

	public static IBullet getMatchingType(ItemStack stack)
	{
		switch(stack.getMetadata())
		{
			case 0:
			default:
				return IIContent.itemAmmoMachinegun;
			case 1:
			case 3:
				return IIContent.itemAmmoSubmachinegun;
			case 2:
				return IIContent.itemAmmoRevolver;
			case 4:
				return IIContent.itemAmmoStormRifle;
		}
	}

	public static ItemStack getMagazine(String type, ItemStack... bullets)
	{
		ItemStack stack = new ItemStack(IIContent.itemBulletMagazine, 1, IIContent.itemBulletMagazine.getMetaBySubname(type));
		NonNullList<ItemStack> l = NonNullList.withSize(getBulletCapactity(stack), ItemStack.EMPTY);
		for(int i = 0; i < l.size(); i++)
		{
			int t = i%bullets.length;
			l.set(i, bullets[t]);
		}
		NBTTagList list = Utils.writeInventory(l);
		ItemNBTHelper.getTag(stack).setTag("bullets", list);
		makeDefault(stack);
		return stack;
	}

	@Override
	public String getModelCacheKey(ItemStack stack)
	{
		return subNames[stack.getMetadata()]+"_"+checkColours(stack);
	}

	public int checkColours(ItemStack stack)
	{
		makeDefault(stack);
		int num = 0;
		num += ItemNBTHelper.getInt(stack, "colour0")!=-1?1: 0;
		num += ItemNBTHelper.getInt(stack, "colour1")!=-1?1: 0;
		num += ItemNBTHelper.getInt(stack, "colour2")!=-1?1: 0;
		num += ItemNBTHelper.getInt(stack, "colour3")!=-1?1: 0;
		return num;
	}

	@Override
	public List<ResourceLocation> getTextures(ItemStack stack, String key)
	{
		ArrayList<ResourceLocation> l = new ArrayList<>();

		String name = getSubNames()[stack.getMetadata()];

		l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/main"));

		if(ItemNBTHelper.getInt(stack, "colour0")!=-1)
		{
			if(getMetadata(stack)==1)
			{
				l.remove(l.size()-1);
				l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/main_disp"));
			}
			l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/bullet0"));
			l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/paint0"));
			if(ItemNBTHelper.getInt(stack, "colour1")!=-1)
			{
				l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/bullet1"));
				l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/paint1"));
				if(ItemNBTHelper.getInt(stack, "colour2")!=-1)
				{
					l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/bullet2"));
					l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/paint2"));
					if(ItemNBTHelper.getInt(stack, "colour3")!=-1)
					{
						l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/bullet3"));
						l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/paint3"));
					}
				}
			}
		}
		return l;
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
		if(!stack.hasTagCompound())
			makeDefault(stack);
		switch(pass)
		{
			case 2:
				return ItemNBTHelper.getInt(stack, "colour0");
			case 4:
				return ItemNBTHelper.getInt(stack, "colour1");
			case 6:
				return ItemNBTHelper.getInt(stack, "colour2");
			case 8:
				return ItemNBTHelper.getInt(stack, "colour3");
		}
		return 0xffffffff;
	}

	public static int getRemainingBulletCount(ItemStack stack)
	{
		NonNullList<ItemStack> cartridge = Utils.readInventory(ItemNBTHelper.getTag(stack).getTagList("bullets", 10), getBulletCapactity(stack));
		int count = 0;
		for(ItemStack s : cartridge)
			if(!s.isEmpty())
				count++;
		return count;
	}
}
