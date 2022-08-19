package pl.pabilo8.immersiveintelligence.common.items.ammunition;

import blusunrize.immersiveengineering.common.items.IEItemInterfaces.ITextureOverride;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.IBullet;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Pabilo8
 * @since 01-11-2019
 */
public class ItemIIBulletMagazine extends ItemIIBase implements ITextureOverride
{
	public ItemIIBulletMagazine()
	{
		super("bullet_magazine", 1, "machinegun", "submachinegun", "automatic_revolver", "submachinegun_drum", "assault_rifle", "autocannon", "cpds_drum");
		setMetaHidden(2); //Hide automatic revolver
	}

	public static void makeDefault(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, "bullets"))
			return;


		if(!ItemNBTHelper.hasKey(stack, "colour0"))
			ItemNBTHelper.setInt(stack, "colour0", -1);
		if(!ItemNBTHelper.hasKey(stack, "colour1"))
			ItemNBTHelper.setInt(stack, "colour1", -1);
		if(!ItemNBTHelper.hasKey(stack, "colour2"))
			ItemNBTHelper.setInt(stack, "colour2", -1);
		if(!ItemNBTHelper.hasKey(stack, "colour3"))
			ItemNBTHelper.setInt(stack, "colour3", -1);

		int bc = getBulletCapactity(stack);
		NonNullList<ItemStack> cartridge = readInventory(ItemNBTHelper.getTag(stack).getCompoundTag("bullets"), bc);
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
				ItemNBTHelper.setBoolean(stack, "bullet"+i, true);
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


	public static NonNullList<ItemStack> takeAll(ItemStack stack)
	{
		if(ItemNBTHelper.hasKey(stack, "bullets"))
		{
			int bc = getBulletCapactity(stack);
			NonNullList<ItemStack> cartridge = readInventory(ItemNBTHelper.getTag(stack).getCompoundTag("bullets"), bc);

			ItemNBTHelper.getTag(stack).setTag("bullets", writeInventory(NonNullList.withSize(getBulletCapactity(stack), ItemStack.EMPTY)));

			return cartridge;
		}
		else
		{
			makeDefault(stack);
			return NonNullList.create();
		}
	}


	public static ItemStack takeBullet(ItemStack stack, boolean doTake)
	{
		if(ItemNBTHelper.hasKey(stack, "bullets"))
		{
			int bc = getBulletCapactity(stack);
			NonNullList<ItemStack> cartridge = readInventory(ItemNBTHelper.getTag(stack).getCompoundTag("bullets"), bc);
			ItemStack ammo = cartridge.get(0).copy();
			if(!doTake)
				return ammo;
			cartridge.set(0, ItemStack.EMPTY);
			for(int i = 1; i < bc; i += 1)
			{
				if(cartridge.get(i-1).isEmpty())
				{
					cartridge.set(i-1, cartridge.get(i));
					cartridge.set(i, ItemStack.EMPTY);
				}
			}
			NBTTagCompound list = writeInventory(cartridge);
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
		if(stack.getTagCompound()==null||stack.getTagCompound().hasNoTags())
			return true;

		int bc = getBulletCapactity(stack);
		NonNullList<ItemStack> cartridge = readInventory(ItemNBTHelper.getTag(stack).getCompoundTag("bullets"), bc);
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
			case 5:
				return 16;
			case 3:
				return 64;
			case 4:
				return 32;
			case 6:
				return 128;
		}
		return 0;
	}

	public static IBullet getMatchingType(ItemStack stack)
	{
		switch(stack.getMetadata())
		{
			case 0:
			case 6:
			default:
				return IIContent.itemAmmoMachinegun;
			case 1:
			case 3:
				return IIContent.itemAmmoSubmachinegun;
			case 2:
				return IIContent.itemAmmoRevolver;
			case 4:
				return IIContent.itemAmmoAssaultRifle;
			case 5:
				return IIContent.itemAmmoAutocannon;
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
		NBTTagCompound list = writeInventory(l);
		ItemNBTHelper.getTag(stack).setTag("bullets", list);
		makeDefault(stack);
		return stack;
	}

	@Override
	public String getModelCacheKey(ItemStack stack)
	{
		return subNames[stack.getMetadata()]+"_"+checkBullets(stack)+"_"+checkColors(stack);
	}

	public int checkBullets(ItemStack stack)
	{
		makeDefault(stack);

		if(stack.getTagCompound()==null||stack.getTagCompound().hasNoTags())
			return 0;

		int num = ItemNBTHelper.hasKey(stack, "bullet0")?1: 0;
		num += ItemNBTHelper.hasKey(stack, "bullet1")?1: 0;
		num += ItemNBTHelper.hasKey(stack, "bullet2")?1: 0;
		num += ItemNBTHelper.hasKey(stack, "bullet3")?1: 0;
		return num;
	}

	public String checkColors(ItemStack stack)
	{
		if(stack.getTagCompound()==null||stack.getTagCompound().hasNoTags())
			return "";

		StringBuilder ss = new StringBuilder();
		if(ItemNBTHelper.hasKey(stack, "colour0")&&ItemNBTHelper.getInt(stack, "colour0")!=-1)
			ss.append("0");
		if(ItemNBTHelper.hasKey(stack, "colour1")&&ItemNBTHelper.getInt(stack, "colour1")!=-1)
			ss.append("1");
		if(ItemNBTHelper.hasKey(stack, "colour2")&&ItemNBTHelper.getInt(stack, "colour2")!=-1)
			ss.append("2");
		if(ItemNBTHelper.hasKey(stack, "colour3")&&ItemNBTHelper.getInt(stack, "colour3")!=-1)
			ss.append("3");
		return ss.toString();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List<ResourceLocation> getTextures(ItemStack stack, String key)
	{
		//ItemNBTHelper.getInt(stack, "bullet1")!=-1
		ArrayList<ResourceLocation> l = new ArrayList<>();

		String name = getSubNames()[stack.getMetadata()];

		l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/main"));

		if(stack.getTagCompound()==null||stack.getTagCompound().hasNoTags())
			return l;

		if(ItemNBTHelper.hasKey(stack, "bullet0"))
		{
			if(getMetadata(stack)==1||getMetadata(stack)==4)
			{
				l.remove(0);
				l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/main_disp"));
			}

			l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/bullet0"));
			if(ItemNBTHelper.hasKey(stack, "colour0")&&ItemNBTHelper.getInt(stack, "colour0")!=-1)
				l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/paint0"));
			else
				l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":blocks/empty"));

			if(ItemNBTHelper.hasKey(stack, "bullet1"))
			{
				l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/bullet1"));
				if(ItemNBTHelper.hasKey(stack, "colour1")&&ItemNBTHelper.getInt(stack, "colour1")!=-1)
					l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/paint1"));
				else
					l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":blocks/empty"));

				if(ItemNBTHelper.hasKey(stack, "bullet2"))
				{
					l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/bullet2"));
					if(ItemNBTHelper.hasKey(stack, "colour2")&&ItemNBTHelper.getInt(stack, "colour2")!=-1)
						l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/paint2"));
					else
						l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":blocks/empty"));

					if(ItemNBTHelper.hasKey(stack, "bullet3"))
					{
						l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/bullet3"));
						if(ItemNBTHelper.hasKey(stack, "colour3")&&ItemNBTHelper.getInt(stack, "colour3")!=-1)
							l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/paint3"));
						else
							l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":blocks/empty"));
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
		NonNullList<ItemStack> cartridge = readInventory(ItemNBTHelper.getTag(stack).getCompoundTag("bullets"), getBulletCapactity(stack));
		int count = 0;
		for(ItemStack s : cartridge)
			if(!s.isEmpty())
				count++;
		return count;
	}

	/**
	 * Slower, but allows storing more of nbt-rich bullets
	 */
	public static NonNullList<ItemStack> readInventory(NBTTagCompound nbt, int size)
	{
		NonNullList<ItemStack> inv = NonNullList.withSize(size, ItemStack.EMPTY);
		ArrayList<ItemStack> dictionary = new ArrayList<>();

		NBTTagList listDict = nbt.getTagList("dictionary", 10);
		NBTTagList listInventory = nbt.getTagList("inventory", 3);

		for(NBTBase tag : listDict)
			if(tag instanceof NBTTagCompound)
				dictionary.add(new ItemStack(((NBTTagCompound)tag)));

		if(dictionary.size()==0)
			return inv;

		int max = listInventory.tagCount();
		for(int i = 0; i < Math.min(max, size); i++)
		{
			int id = listInventory.getIntAt(i);
			if(id >= 0&&id < dictionary.size())
				inv.set(i, dictionary.get(id).copy());
		}
		return inv;
	}

	/**
	 * Slower, but more efficient in case of magazines filled with nbt-rich bullets
	 */
	public static NBTTagCompound writeInventory(NonNullList<ItemStack> inv)
	{
		ArrayList<ItemStack> dictionary = new ArrayList<>();
		NBTTagList invList = new NBTTagList();

		for(ItemStack stack : inv)
		{
			if(stack.isEmpty())
				continue;
			int id;

			Optional<ItemStack> found = dictionary.stream().filter(stack1 -> ItemHandlerHelper.canItemStacksStackRelaxed(stack1, stack)).findFirst();
			if(!found.isPresent())
			{
				dictionary.add(stack);
				id = dictionary.size()-1;
			}
			else
				id = dictionary.indexOf(found.get());

			invList.appendTag(new NBTTagInt(id));
		}

		NBTTagList dictList = new NBTTagList();
		dictionary.forEach(stack -> dictList.appendTag(stack.serializeNBT()));

		NBTTagCompound output = new NBTTagCompound();
		output.setTag("dictionary", dictList);
		output.setTag("inventory", invList);

		return output;
	}
}
