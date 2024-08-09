package pl.pabilo8.immersiveintelligence.common.item.mechanical;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.energy.wires.IWireCoil;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.rotary.IIRotaryUtils;
import pl.pabilo8.immersiveintelligence.api.rotary.IMotorBeltConnector;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.MechanicalDevices;
import pl.pabilo8.immersiveintelligence.common.item.mechanical.ItemIIMotorBelt.MotorBelt;
import pl.pabilo8.immersiveintelligence.common.util.IBatchOredictRegister;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.immersiveintelligence.common.wire.IIMotorBeltType;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 27-12-2019
 */
@IBatchOredictRegister(oreDict = "motorBelt")
@IIItemProperties(category = IICategory.ROTARY)
public class ItemIIMotorBelt extends ItemIISubItemsBase<MotorBelt> implements IWireCoil
{
	public ItemIIMotorBelt()
	{
		super("motor_belt", 64, MotorBelt.values());
	}

	@GeneratedItemModels(itemName = "motor_belt")
	public enum MotorBelt implements IIItemEnum
	{
		CLOTH(IIRotaryUtils.BELT_CATEGORY, new IngredientStack("leather"), MechanicalDevices.beltLength[0], 6,
				MechanicalDevices.beltMaxTorque[0], MechanicalDevices.beltTorqueLoss[0]),
		STEEL(IIRotaryUtils.TRACK_CATEGORY, new IngredientStack("plateSteel"), MechanicalDevices.beltLength[1], 8,
				MechanicalDevices.beltMaxTorque[1], MechanicalDevices.beltTorqueLoss[1]),
		RUBBER(IIRotaryUtils.BELT_CATEGORY, new IngredientStack("beltRubber"), MechanicalDevices.beltLength[2], 8,
				MechanicalDevices.beltMaxTorque[2], MechanicalDevices.beltTorqueLoss[2]);

		/**
		 * Motor belt equivalent of {@link WireType#getCategory()}
		 */
		public final String category;

		/**
		 * See {@link IIConfigHandler.IIConfig.MechanicalDevices#beltMaxTorque}
		 */
		public final int maxTorque;
		/**
		 * See {@link IIConfigHandler.IIConfig.MechanicalDevices#beltTorqueLoss}
		 */
		public final float torqueLoss;
		/**
		 * Maximum allowed belt length in blocks
		 */
		public final int length;
		/**
		 * Width of the belt, used for damaging colliding entities
		 */
		public final int width;
		public final IIMotorBeltType type;
		/**
		 * The Item dropped when the belt is broken
		 */
		public IngredientStack dropItem;

		MotorBelt(String category, IngredientStack dropItem, int length, int width, int maxTorque, float torqueLoss)
		{
			this.category = category;
			this.dropItem = dropItem;
			this.length = length;
			this.width = width;
			this.maxTorque = maxTorque;
			this.torqueLoss = torqueLoss;

			type = new IIMotorBeltType(this);
		}
	}

	@Override
	public WireType getWireType(ItemStack stack)
	{
		return stackToSub(stack).type;
	}

	@Override
	public boolean canConnectCable(ItemStack stack, TileEntity targetEntity)
	{
		return targetEntity instanceof IMotorBeltConnector;
	}

	@Override
	public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flagIn)
	{
		int[] link = ItemNBTHelper.getIntArray(stack, "linkingPos");
		if(link.length > 3)
			tooltip.add(I18n.format(Lib.DESC_INFO+"attachedToDim", link[1], link[2], link[3], link[0]));
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUseFirst(@Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ, @Nonnull EnumHand hand)
	{
		return IIRotaryUtils.useCoil(this, player, world, pos, hand, side, hitX, hitY, hitZ);
	}
}
