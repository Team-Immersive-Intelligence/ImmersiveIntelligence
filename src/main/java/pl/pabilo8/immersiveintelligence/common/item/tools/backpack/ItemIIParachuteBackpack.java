package pl.pabilo8.immersiveintelligence.common.item.tools.backpack;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.tool.IElectricEquipment;
import blusunrize.immersiveengineering.common.Config.IEConfig.Machines;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IColouredItem;
import blusunrize.immersiveengineering.common.items.ItemPowerpack;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.EnergyHelper.IIEEnergyItem;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageItemKeybind;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @author Pabilo8
 * @since 23.01.2021
 */
public class ItemIIParachuteBackpack extends ItemArmor implements ISpecialArmor
{
    public static final String CHUTE_DEPLOYED = "chuteDeployed";
	public static final String CHUTE_USES_LEFT = "chuteUsesLeft";
	public static final String CHUTE_MAX_USES = "chuteMaxUses";
    public static final int MAX_USES = 4;
	public static final boolean IS_CHUTE_DEPLOYED = false;
    public static int USES_LEFT = MAX_USES;

    public ItemIIParachuteBackpack()
	{
		super(ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.CHEST);
		setMaxDamage(0);
		String name = "parachute_backpack";
		this.setUnlocalizedName(ImmersiveIntelligence.MODID+"."+name);
		this.setCreativeTab(IIContent.II_CREATIVE_TAB);
		IIContent.ITEMS.add(this);
	}

    public void onParachuteDeploy()
    {
        // TODO implement chute deployment
    }

	//Armor should use a model instead of layered textures
	@Nullable
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
	{
		return ImmersiveIntelligence.MODID+":textures/armor/empty.png";
	}

	@Nullable
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default)
	{
		// TODO Implement Model for parachute backpack.
		// return ModelParachuteBackpack.getModel(armorSlot, itemStack);

        return null;
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
        String chuteCount = "Chutes left"+this.USES_LEFT;

		list.add(I18n.format(Lib.DESC+"info.chuteCount", TextFormatting.GOLD+chuteCount+TextFormatting.RESET));
        list.add(I18n.format(Lib.DESC+"info.isDeployed", TextFormatting.GOLD+chuteCount+TextFormatting.RESET));
	}

	@SideOnly(Side.CLIENT)
	@Nullable
	@Override
	public FontRenderer getFontRenderer(ItemStack stack)
	{
		return IIClientUtils.fontRegular;
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
	{
		if(!(player instanceof EntityLivingBase)||!((EntityLivingBase)player).getItemStackFromSlot(EntityEquipmentSlot.CHEST).equals(itemStack))
			return;

		EasyNBT nbt = getNBT(itemStack);

		boolean shouldChuteDeploy = nbt.getBoolean(CHUTE_DEPLOYED);
		int chutesleft = nbt.getInt(CHUTE_USES_LEFT);

        if(world.isRemote)
            if(!shouldChuteDeploy&&ClientProxy.keybind_deployChute.isKeyDown()&&chutesleft > 0)
			{
				IIPacketHandler.sendToServer(new MessageItemKeybind(MessageItemKeybind.KEYBIND_DEPLOY_CHUTE));
				onParachuteDeploy();
				chutesleft--;
			}

		nbt.withInt(CHUTE_USES_LEFT, MAX_USES);
		nbt.withBoolean(CHUTE_DEPLOYED, shouldChuteDeploy);
	}

	/**
	 * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
	 */
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
	{
		return HashMultimap.create();
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot)
	{
		return new ArmorProperties(0, 0, 0);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot)
	{
		return 0;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot)
	{
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		return null;
	}

	private EasyNBT getNBT(ItemStack pack)
	{
		return EasyNBT.wrapNBT(ItemNBTHelper.getTag(pack));
	}
}
