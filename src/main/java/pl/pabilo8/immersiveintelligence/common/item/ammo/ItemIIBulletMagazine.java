package pl.pabilo8.immersiveintelligence.common.item.ammo;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IAdvancedTooltipItem;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIIItemTextureOverride;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;
import pl.pabilo8.modworks.annotations.item.ItemModelType;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Pabilo8
 * @since 01-11-2019
 */
@IIItemProperties(category = IICategory.WARFARE)
public class ItemIIBulletMagazine extends ItemIISubItemsBase<Magazines> implements IIIItemTextureOverride, IAdvancedTooltipItem
{
	//--- Textures ---//
	private final ResLoc magazineTexture = ResLoc.of(IIReference.RES_II, "items/bullets/magazines/");
	private final ResLoc bulletTexture = ResLoc.of(IIReference.RES_II, "items/bullets/magazines/common/bullet");
	private final ResLoc paintTexture = ResLoc.of(IIReference.RES_II, "items/bullets/magazines/common/paint");

	public ItemIIBulletMagazine()
	{
		super("bullet_magazine", 1, Magazines.values());
	}


	@GeneratedItemModels(itemName = "bullet_magazine", type = ItemModelType.ITEM_SIMPLE_AUTOREPLACED)
	public enum Magazines implements IIItemEnum
	{
		MACHINEGUN(48, IIContent.itemAmmoMachinegun),
		SUBMACHINEGUN(24, IIContent.itemAmmoSubmachinegun, true),
		RIFLE(12, IIContent.itemAmmoMachinegun),
		SUBMACHINEGUN_DRUM(64, IIContent.itemAmmoSubmachinegun),
		ASSAULT_RIFLE(32, IIContent.itemAmmoAssaultRifle, true),
		AUTOCANNON(16, IIContent.itemAmmoAutocannon),
		CPDS_DRUM(128, IIContent.itemAmmoMachinegun),
		@IIItemProperties(hidden = true)
		AUTOMATIC_REVOLVER(16, IIContent.itemAmmoRevolver);

		public final int capacity;
		public final IAmmoTypeItem ammo;
		public final boolean hasDisplayTexture;

		Magazines(int capacity, IAmmoTypeItem ammo)
		{
			this(capacity, ammo, false);
		}

		Magazines(int capacity, IAmmoTypeItem ammo, boolean hasDisplayTexture)
		{
			this.capacity = capacity;
			this.ammo = ammo;
			this.hasDisplayTexture = hasDisplayTexture;
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
				IIColor color = ((IAmmoTypeItem<?, ?>)bullet.getItem()).getPaintColor(bullet);
				ItemNBTHelper.setInt(stack, "colour"+i, color!=null?color.getPackedRGB(): -1);
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


	@SideOnly(Side.CLIENT)
	@Override
	@ParametersAreNonnullByDefault
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		int bullets = getRemainingBulletCount(stack);

		tooltip.add(IIStringUtil.getItalicString(I18n.format(IIReference.DESCRIPTION_KEY+(bullets==0?"bullet_magazine.empty": "bullet_magazine.remaining"), bullets)));
		NBTTagList listDict = ItemNBTHelper.getTagCompound(stack, "bullets").getTagList("dictionary", NBT.TAG_COMPOUND);

		if(ItemNBTHelper.getTag(stack).hasKey("bullet0"))
			tooltip.add("   "+TextFormatting.GOLD+new ItemStack(listDict.getCompoundTagAt(0)).getDisplayName());
		if(ItemNBTHelper.getTag(stack).hasKey("bullet1"))
			tooltip.add("   "+TextFormatting.GOLD+new ItemStack(listDict.getCompoundTagAt(1)).getDisplayName());
		if(ItemNBTHelper.getTag(stack).hasKey("bullet2"))
			tooltip.add("   "+TextFormatting.GOLD+new ItemStack(listDict.getCompoundTagAt(2)).getDisplayName());
		if(ItemNBTHelper.getTag(stack).hasKey("bullet3"))
			tooltip.add("   "+TextFormatting.GOLD+new ItemStack(listDict.getCompoundTagAt(3)).getDisplayName());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addAdvancedInformation(ItemStack stack, int offsetX, List<Integer> offsetsY)
	{
		if(!hasNoBullets(stack))
			ItemTooltipHandler.drawItemList(offsetX, offsetsY.get(0),
					ItemNBTHelper.getTagCompound(stack, "bullets").getTagList("dictionary", 10));
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

		if(ItemNBTHelper.hasKey(stack, "bullet0")&&(subItem.hasDisplayTexture))
			l.add(ResLoc.of(magazineTexture, subItem.getName(), "_disp"));
		else
			l.add(ResLoc.of(magazineTexture, subItem.getName()));

		if(stack.getTagCompound()==null||stack.getTagCompound().hasNoTags())
			return l;

		for(int i = 0; i < 3; i++)
			if(ItemNBTHelper.hasKey(stack, "bullet"+i))
			{
				l.add(ResLoc.of(bulletTexture, i));
				if(ItemNBTHelper.hasKey(stack, "colour"+i)&&ItemNBTHelper.getInt(stack, "colour"+i)!=-1)
					l.add(ResLoc.of(paintTexture, i));
			}

		return l;
	}


	@SideOnly(Side.CLIENT)
	@Override
	public void registerSprites(TextureMap map)
	{
		for(int i = 0; i < 4; i++)
		{
			ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/magazines/common/bullet"+i);
			ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/magazines/common/paint"+i);
		}
		for(Magazines magazine : IIContent.itemBulletMagazine.getSubItems())
		{
			String name = magazine.getName();
			ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name);
			if(magazine.hasDisplayTexture)
				ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"_disp");
		}
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
		return nbt.getTagList("inventory", NBT.TAG_INT).tagCount();
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

		NBTTagList listDict = nbt.getTagList("dictionary", NBT.TAG_COMPOUND);
		NBTTagList listInventory = nbt.getTagList("inventory", NBT.TAG_INT);

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
		writeInventory(stack, NonNullList.create());
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
		return nbt.hasNoTags()||nbt.getTagList("inventory", NBT.TAG_INT).tagCount()==0;
	}

	public ItemStack getMagazine(Magazines type, ItemStack... bullets)
	{
		ItemStack stack = getStack(type, 1);

		NonNullList<ItemStack> l = NonNullList.withSize(type.capacity, ItemStack.EMPTY);
		//fill with bullets
		if(bullets.length > 0)
			for(int i = 0; i < l.size(); i++)
				l.set(i, bullets[i%bullets.length]);
		//parse with optimized method
		writeInventory(stack, l);
		//apply colors, etc.
		defaultize(stack);
		return stack;
	}
}
