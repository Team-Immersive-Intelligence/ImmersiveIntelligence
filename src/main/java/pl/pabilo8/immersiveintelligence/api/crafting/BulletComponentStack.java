package pl.pabilo8.immersiveintelligence.api.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class BulletComponentStack implements INBTSerializable<NBTTagCompound>
{
	@Nullable
	public AmmoComponent component;
	public String name;
	public int amount;
	@Nonnull
	public NBTTagCompound tagCompound;

	private BulletComponentStack(String name, int amount, @Nonnull NBTTagCompound tag)
	{
		this.name = name;
		this.amount = amount;
		this.tagCompound = tag.copy();

		Optional<AmmoComponent> first = AmmoRegistry.getAllComponents().stream()
				.filter(comp -> this.name.equals(comp.getName()))
				.findFirst();

		component = first.orElse(null);
	}

	public BulletComponentStack(AmmoComponent component, @Nullable NBTTagCompound tag)
	{
		this(component, 16, tag);
	}

	public BulletComponentStack(AmmoComponent component, int amount, @Nullable NBTTagCompound tag)
	{
		this(component.getName(), amount, tag==null?new NBTTagCompound(): tag);
	}

	public BulletComponentStack()
	{
		this("", 0, new NBTTagCompound());
	}

	public BulletComponentStack copy()
	{
		return new BulletComponentStack(name, amount, tagCompound);
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("name", name);
		nbt.setInteger("amount", amount);
		nbt.setTag("nbt", tagCompound.copy());

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		name = nbt.getString("name");
		amount = nbt.getInteger("amount");
		tagCompound = nbt.getCompoundTag("nbt").copy();

		Optional<AmmoComponent> first = AmmoRegistry.getAllComponents().stream()
				.filter(comp -> this.name.equals(comp.getName()))
				.findFirst();

		component = first.orElse(null);
	}

	public boolean isEmpty()
	{
		return name.isEmpty()||amount==0||component==null;
	}

	public boolean isFluid()
	{
		return component!=null&&component.getMaterial().fluid!=null;
	}

	public boolean matches(ItemStack stack)
	{
		if(isEmpty())
			return true;
		if(stack.isEmpty()||component==null)
			return false;

		NBTTagCompound stackTag = stack.getTagCompound();
		if(stackTag==null)
			stackTag = new NBTTagCompound();

		return component.getMaterial().matchesItemStackIgnoringSize(stack)&&this.tagCompound.equals(stackTag);
	}

	public boolean matches(FluidStack fs)
	{
		if(isEmpty())
			return true;
		if(fs==null||component==null||component.getMaterial().fluid==null)
			return false;

		NBTTagCompound stackTag = fs.tag==null?new NBTTagCompound(): fs.tag;

		return fs.isFluidEqual(component.getMaterial().fluid)&&this.tagCompound.equals(stackTag);
	}

	public void subtract(int amount)
	{
		this.amount = Math.max(0, this.amount-amount);
		if(this.amount==0)
		{
			tagCompound = new NBTTagCompound();
			name = "";
			component = null;
		}
	}

	public boolean matches(IAmmoTypeItem<?, ?> bullet)
	{
		return component!=null&&component.matchesBullet(bullet);
	}

	public IIColor getColor()
	{
		return component!=null?component.getColor(tagCompound): IIColor.WHITE;
	}

	public float getAmountPercentage()
	{
		return amount/(float)ProjectileWorkshop.componentCapacity;
	}

	@SideOnly(Side.CLIENT)
	public String getTranslatedName()
	{
		return component!=null?component.getTranslatedName(): "";
	}

	public AmmoComponent getComponent()
	{
		return component;
	}
}
