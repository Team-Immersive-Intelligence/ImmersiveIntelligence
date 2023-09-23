package pl.pabilo8.immersiveintelligence.common.item.mechanical;

import blusunrize.immersiveengineering.api.Lib;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.MechanicalDevices;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.rotary.IModelMotorBelt;
import pl.pabilo8.immersiveintelligence.api.rotary.IMotorBeltConnector;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryUtils;
import pl.pabilo8.immersiveintelligence.client.model.motor_belt.ModelClothMotorBelt;
import pl.pabilo8.immersiveintelligence.client.model.motor_belt.ModelSteelMotorBelt;
import pl.pabilo8.immersiveintelligence.common.item.mechanical.ItemIIMotorBelt.MotorBelt;
import pl.pabilo8.immersiveintelligence.common.util.IBatchOredictRegister;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
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
public class ItemIIMotorBelt extends ItemIISubItemsBase<MotorBelt> implements IWireCoil
{
	public ItemIIMotorBelt()
	{
		super("motor_belt", 64, MotorBelt.values());
	}

	@GeneratedItemModels(itemName = "motor_belt")
	public enum MotorBelt implements IIItemEnum
	{
		CLOTH("light_belts", MechanicalDevices.beltLength[0], 1, 6,
				MechanicalDevices.beltMaxTorque[0], MechanicalDevices.beltTorqueLoss[0]),
		STEEL("heavy_belts", MechanicalDevices.beltLength[1], 1, 8,
				MechanicalDevices.beltMaxTorque[1], MechanicalDevices.beltTorqueLoss[1]),
		RUBBER("light_belts", MechanicalDevices.beltLength[2], 1, 6,
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
		 * Length - X - maximum allowed belt length<br>
		 * Thickness - Y - how thick is the belt, tracks are thicker than cloth belts<br>
		 * Width - Z - width of the belt, used to get spacing between pieces in rendering
		 */
		public final int length, thickness, width;

		public final IIMotorBeltType type;
		public final ResourceLocation res;
		@SideOnly(Side.CLIENT)
		public IModelMotorBelt model;

		MotorBelt(String category, int length, int thickness, int width, int maxTorque, float torqueLoss)
		{
			this.category = category;
			this.length = length;
			this.thickness = thickness;
			this.maxTorque = maxTorque;
			this.torqueLoss = torqueLoss;
			this.width = width;

			res = getResourceLocation(getName());
			type = new IIMotorBeltType(this);
		}

		private static ResourceLocation getResourceLocation(String name)
		{
			return new ResourceLocation(ImmersiveIntelligence.MODID, "textures/rotary/belts/"+name);
		}

		@SideOnly(Side.CLIENT)
		public IModelMotorBelt getModel()
		{
			return model;
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
		return RotaryUtils.useCoil(this, player, world, pos, hand, side, hitX, hitY, hitZ);
	}

	/**
	 * Hey you, yes you, copying that class, <br>
	 * Please remember to call your version of this method somewhere in your ClientProxy <br>
	 * Have a nice day ^^ <br>
	 */
	@SideOnly(Side.CLIENT)
	public void setRenderModels()
	{
		MotorBelt.CLOTH.model = new ModelClothMotorBelt();
		MotorBelt.STEEL.model = new ModelSteelMotorBelt();
		MotorBelt.RUBBER.model = new ModelClothMotorBelt();
	}

}
