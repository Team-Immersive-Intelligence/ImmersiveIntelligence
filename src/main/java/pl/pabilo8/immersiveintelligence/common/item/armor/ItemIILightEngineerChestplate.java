package pl.pabilo8.immersiveintelligence.common.item.armor;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.tool.IElectricEquipment;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IAdvancedFluidItem;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
import blusunrize.immersiveengineering.common.util.IEItemFluidHandler;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import com.google.common.collect.Multimap;
import net.minecraft.block.material.Material;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.armor.IInfraredProtectionEquipment;
import pl.pabilo8.immersiveintelligence.client.model.armor.ModelLightEngineerArmor;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.LightEngineerArmor;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.util.item.IIArmorItemStackHandler;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @author Pabilo8
 * @since 13.09.2020
 */
@IIItemProperties(category = IICategory.WARFARE)
public class ItemIILightEngineerChestplate extends ItemIILightEngineerArmorBase implements IElectricEquipment, IInfraredProtectionEquipment, IAdvancedFluidItem
{
	public ItemIILightEngineerChestplate()
	{
		super(EntityEquipmentSlot.CHEST, "LIGHT_ENGINEER_CHESTPLATE");
	}


	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		if(!stack.isEmpty())
			return new IIArmorItemStackHandler(stack)
			{
				final IEItemFluidHandler fluids = new IEItemFluidHandler(stack, 0);

				@Override
				public boolean hasCapability(Capability<?> capability, EnumFacing facing)
				{
					return capability==CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY||
							super.hasCapability(capability, facing);
				}

				@Override
				public <T> T getCapability(Capability<T> capability, EnumFacing facing)
				{
					if(capability==CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
						return (T)fluids;
					return super.getCapability(capability, facing);
				}
			};
		return null;
	}

	@Override
	public void finishUpgradeRecalculation(ItemStack stack)
	{
		FluidStack fs = getFluid(stack);
		if(fs!=null&&fs.amount > getCapacity(stack, 0))
		{
			fs.amount = getCapacity(stack, 0);
			ItemNBTHelper.setFluidStack(stack, "Fluid", fs);
		}
	}

	private boolean hasReinforcement(EntityPlayer player)
	{
		// Get the boots specifically
		ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);

		// Check if the boots are not empty and if the "reinforcement" upgrade is present
		if (!boots.isEmpty())
		{
			NBTTagCompound upgrades = getUpgrades(boots);
			if (upgrades != null && upgrades.hasKey("boot_reinforcement"))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack)
	{

		boolean hasHeatCoat = getUpgrades(stack).hasKey("heatcoat");
		boolean hasReinforcement = hasReinforcement(player);


		if(hasHeatCoat && hasReinforcement)
		{
			player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 20, 0, true, false)); // Give fire resistance for 1 second
			return;
		}

		super.onArmorTick(world, player, stack);
		if(player.getAir()!=300&&hasUpgrade(stack, "scuba")&&hasUpgrade(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD), "gasmask"))
		{
			IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(stack);
			if(fluidHandler!=null&&world.getTotalWorldTime()%20==0)
			{
				FluidStack fs = fluidHandler.drain(LightEngineerArmor.scubaTankUsage, true);
				if(fs!=null&&fs.amount > 0)
					player.setAir((int)Math.min(player.getAir()+40, 300*(fs.amount/(float)LightEngineerArmor.scubaTankUsage)));
			}
		}
		if(getUpgrades(stack).hasKey("camo_mesh")&&world.getTotalWorldTime()%10==0)
		{
			// Get the player's current position, below it, and the surrounding blocks
			Material materialCurrent = world.getBlockState(player.getPosition()).getMaterial();
			Material materialBelow = world.getBlockState(player.getPosition().down()).getMaterial();
			Material materialNorth = world.getBlockState(player.getPosition().north()).getMaterial();
			Material materialSouth = world.getBlockState(player.getPosition().south()).getMaterial();
			Material materialEast = world.getBlockState(player.getPosition().east()).getMaterial();
			Material materialWest = world.getBlockState(player.getPosition().west()).getMaterial();

			// Check if any of the blocks around the player or at their position match grass, leaves, or vine
			if (player.isSneaking() && (
					materialCurrent == Material.GRASS || materialCurrent == Material.LEAVES || materialCurrent == Material.VINE ||
							materialBelow == Material.GRASS || materialBelow == Material.LEAVES || materialBelow == Material.VINE ||
							materialNorth == Material.GRASS || materialNorth == Material.LEAVES || materialNorth == Material.VINE ||
							materialSouth == Material.GRASS || materialSouth == Material.LEAVES || materialSouth == Material.VINE ||
							materialEast == Material.GRASS || materialEast == Material.LEAVES || materialEast == Material.VINE ||
							materialWest == Material.GRASS || materialWest == Material.LEAVES || materialWest == Material.VINE
			))
			{
				player.addPotionEffect(new PotionEffect(IIPotions.concealed, 15, 0, true, false));
				player.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 15, 0, true, false));
			}
		}
		if(getUpgrades(stack).hasKey("heatcoat"))
		{
			if(player.isBurning())
			{
				player.attackEntityFrom(DamageSource.IN_FIRE, 0.75F);
				player.attackEntityFrom(DamageSource.ON_FIRE, 0.5F);
			}
		}
	}

	@Nullable
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default)
	{
		return ModelLightEngineerArmor.getModel(armorSlot, itemStack);
	}

	@Override
	protected String getMaterialName(ArmorMaterial material)
	{
		return "light_engineer_armor";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<String> list, @Nonnull ITooltipFlag flag)
	{
		super.addInformation(stack, world, list, flag);
		if(hasUpgrade(stack, "scuba"))
		{
			int amount = getFluid(stack)==null?0: getFluid(stack).amount;
			list.add(I18n.format(Lib.DESC_FLAVOUR+"drill.fuel")+" "+amount+"/"+getCapacity(stack, 0)+"mB");
		}
	}

	@Override
	public float getXpRepairRatio(ItemStack stack)
	{
		return 0.1f;
	}

	@Nonnull
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EntityEquipmentSlot equipmentSlot, @Nonnull ItemStack stack)
	{
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot, stack);

		if(equipmentSlot==this.armorType)
		{
			//multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Power Armor Movement Speed Debuff", -.03, 1));
		}
		return multimap;
	}

	@Override
	public void onStrike(ItemStack s, EntityEquipmentSlot eqSlot, EntityLivingBase p, Map<String, Object> cache,
						 @Nullable DamageSource dSource, ElectricSource eSource)
	{
		if(!(dSource instanceof ElectricDamageSource))
			return;
		if(!hasUpgrade(s, "anti_static_mesh"))
			return;

		ElectricDamageSource dmg = (ElectricDamageSource)dSource;
		dmg.dmg = (hasUpgrade(s, "anti_static_mesh")&&p.isInWater())?(float)(dmg.dmg*LightEngineerArmor.antiStaticMeshWaterDamageMod): 0;
	}

	@Override
	public int getSlotCount()
	{
		return 4;
	}

	@Override
	public boolean invisibleToInfrared(ItemStack stack)
	{
		return hasUpgrade(stack, "ir_mesh");
	}

	@Override
	public int getCapacity(ItemStack stack, int baseCapacity)
	{
		return hasUpgrade(stack, "scuba")?LightEngineerArmor.scubaTankCapacity: 0;
	}

	@Override
	public boolean allowFluid(ItemStack container, FluidStack fluid)
	{
		return fluid.getFluid()==IIContent.gasOxygen;
	}
}
