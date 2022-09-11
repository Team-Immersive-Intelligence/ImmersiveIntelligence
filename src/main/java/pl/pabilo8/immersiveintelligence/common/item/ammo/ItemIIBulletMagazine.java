package pl.pabilo8.immersiveintelligence.common.item.ammo;

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
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Pabilo8
 * @since 01-11-2019
 */
public class ItemIIBulletMagazine extends ItemIISubItemsBase<Magazines> implements ITextureOverride
{
	public ItemIIBulletMagazine()
	{
		super("bullet_magazine", 1, Magazines.values());
	}

	public enum Magazines implements IIItemEnum
	{
		MACHINEGUN(48, IIContent.itemAmmoMachinegun),
		SUBMACHINEGUN(24, IIContent.itemAmmoSubmachinegun),
		@IIItemProperties(hidden = true)
		AUTOMATIC_REVOLVER(16, IIContent.itemAmmoRevolver),
		SUBMACHINEGUN_DRUM(64, IIContent.itemAmmoSubmachinegun),
		ASSAULT_RIFLE(32, IIContent.itemAmmoAssaultRifle),
		AUTOCANNON(16, IIContent.itemAmmoAutocannon),
		CPDS_DRUM(128, IIContent.itemAmmoMachinegun);

		public final int capacity;
		public final IAmmo ammo;

		Magazines(int capacity, IAmmo ammo)
		{
			this.capacity = capacity;
			this.ammo = ammo;
		}
	}

	public void defaultize(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, "bullets"))
			return;

		//defaultize, -1 means an uncolored or no bullet
		for(int j = 0; j <= 3; j++)
			ItemNBTHelper.setInt(stack, "colour"+j, -1);

		NonNullList<ItemStack> cartridge = readInventory(stack);
		ArrayList<ItemStack> already = new ArrayList<>();
		int i = 0;
		for(ItemStack bullet : cartridge)
		{
			if(bullet.isEmpty()||i > 3)
				break;

			boolean contains = false;
			for(ItemStack s : already)
				if(ItemStack.areItemStacksEqual(bullet, s))
				{
					contains = true;
					break;
				}
			if(!contains)
			{
				already.add(bullet);
				ItemNBTHelper.setInt(stack, "colour"+i, ((IAmmo)bullet.getItem()).getPaintColor(bullet));
				ItemNBTHelper.setBoolean(stack, "bullet"+i, true);
				i += 1;
			}
		}

		//remove tags if not enough bullet types to display
		for(int j = 0; j <= 3; j++)
			if(already.size() < j+1)
			{
				ItemNBTHelper.remove(stack, "bullet"+j);
				ItemNBTHelper.setInt(stack, "colour"+j, -1);
			}
	}

	//--- ITextureOverride ---//

	@Override
	public String getModelCacheKey(ItemStack stack)
	{
		return stackToSub(stack).getName()+"_"+checkBullets(stack)+"_"+checkColors(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List<ResourceLocation> getTextures(ItemStack stack, String key)
	{
		ArrayList<ResourceLocation> l = new ArrayList<>();
		Magazines subItem = stackToSub(stack);
		String name = subItem.getName();

		if(ItemNBTHelper.hasKey(stack, "bullet0")&&(subItem==Magazines.SUBMACHINEGUN||subItem==Magazines.ASSAULT_RIFLE))
			l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/main_disp"));
		else
			l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/main"));

		if(stack.getTagCompound()==null||stack.getTagCompound().hasNoTags())
			return l;

		for(int i = 0; i < 3; i++)
			if(ItemNBTHelper.hasKey(stack, "bullet"+i))
			{
				l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/bullet"+i));
				if(ItemNBTHelper.hasKey(stack, "colour"+i)&&ItemNBTHelper.getInt(stack, "colour"+i)!=-1)
					l.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"/paint"+i));
			}

		return l;
	}

	public int checkBullets(ItemStack stack)
	{
		defaultize(stack);

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

		for(int i = 0; i < 3; i++)
			if(ItemNBTHelper.hasKey(stack, "colour"+i)&&ItemNBTHelper.getInt(stack, "colour"+i)!=-1)
				ss.append(i);

		return ss.toString();
	}

	//--- IColouredItem ---//

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

	public int getRemainingBulletCount(ItemStack stack)
	{
		//xaxaxa, trick! optimization device!
		NBTTagCompound nbt = ItemNBTHelper.getTagCompound(stack, "bullets");
		return nbt.getTagList("inventory", 3).tagCount();
	}

	//--- Inventory Storage ---//

	/**
	 * Slower, but allows storing more of nbt-rich bullets
	 */
	public NonNullList<ItemStack> readInventory(ItemStack stack)
	{
		Magazines magazine = stackToSub(stack);
		NBTTagCompound nbt = ItemNBTHelper.getTagCompound(stack, "bullets");

		NonNullList<ItemStack> inv = NonNullList.withSize(magazine.capacity, ItemStack.EMPTY);
		ArrayList<ItemStack> dictionary = new ArrayList<>();

		NBTTagList listDict = nbt.getTagList("dictionary", 10);
		NBTTagList listInventory = nbt.getTagList("inventory", 3);

		for(NBTBase tag : listDict)
			if(tag instanceof NBTTagCompound)
				dictionary.add(new ItemStack(((NBTTagCompound)tag)));

		if(dictionary.size()==0)
			return inv;

		int max = listInventory.tagCount();
		for(int i = 0; i < Math.min(max, magazine.capacity); i++)
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
	public ItemStack writeInventory(ItemStack magazine, NonNullList<ItemStack> inv)
	{
		ArrayList<ItemStack> dictionary = new ArrayList<>();
		NBTTagList invList = new NBTTagList();

		//compile magazine item dictionary
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

		//create compound
		NBTTagCompound output = new NBTTagCompound();
		output.setTag("dictionary", dictList);
		output.setTag("inventory", invList);

		//set to stack
		ItemNBTHelper.setTagCompound(magazine, "bullets", output);

		return magazine;
	}

	public NonNullList<ItemStack> takeAll(ItemStack stack)
	{
		NonNullList<ItemStack> bullets = readInventory(stack);
		writeInventory(stack, bullets);

		defaultize(stack);
		return bullets;
	}


	public ItemStack takeBullet(ItemStack stack, boolean doTake)
	{
		NonNullList<ItemStack> bullets = readInventory(stack);
		ItemStack ammo = bullets.get(0).copy();
		if(!doTake)
			return ammo;

		//actually take the bullet
		bullets.set(0, ItemStack.EMPTY);
		writeInventory(stack, bullets);
		defaultize(stack);
		return ammo;
	}

	public boolean hasNoBullets(ItemStack stack)
	{
		//safety(tm)
		NBTTagCompound nbt = ItemNBTHelper.getTagCompound(stack, "bullets");
		return nbt.hasNoTags()||nbt.getTagList("inventory", 3).tagCount()==0;
	}

	public ItemStack getMagazine(Magazines type, ItemStack... bullets)
	{
		ItemStack stack = getStack(type, 1);

		NonNullList<ItemStack> l = NonNullList.withSize(type.capacity, ItemStack.EMPTY);
		//fill with bullets
		for(int i = 0; i < l.size(); i++)
			l.set(i, bullets[i%bullets.length]);
		//parse with optimized method
		writeInventory(stack, l);
		//apply colors, etc.
		defaultize(stack);
		return stack;
	}
}
