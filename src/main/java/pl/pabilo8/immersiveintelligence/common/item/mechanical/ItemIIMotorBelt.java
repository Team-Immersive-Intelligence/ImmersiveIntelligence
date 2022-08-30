package pl.pabilo8.immersiveintelligence.common.item.mechanical;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.energy.wires.IWireCoil;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.MechanicalDevices;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.rotary.IModelMotorBelt;
import pl.pabilo8.immersiveintelligence.api.rotary.IMotorBeltConnector;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryUtils;
import pl.pabilo8.immersiveintelligence.client.model.motor_belt.ModelClothMotorBelt;
import pl.pabilo8.immersiveintelligence.client.model.motor_belt.ModelSteelMotorBelt;
import pl.pabilo8.immersiveintelligence.common.item.ItemIIBase;
import pl.pabilo8.immersiveintelligence.common.wire.IIMotorBeltType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author Pabilo8
 * @since 27-12-2019
 */
public class ItemIIMotorBelt extends ItemIIBase implements IWireCoil
{
	public ItemIIMotorBelt()
	{
		super("motor_belt", 64, MotorBelt.getNames());
	}

	@Override
	public WireType getWireType(ItemStack stack)
	{
		return MotorBelt.values()[stack.getMetadata()].type;
	}

	@Override
	public boolean canConnectCable(ItemStack stack, TileEntity targetEntity)
	{
		return targetEntity instanceof IMotorBeltConnector;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		if(stack.getTagCompound()!=null&&stack.getTagCompound().hasKey("linkingPos"))
		{
			int[] link = stack.getTagCompound().getIntArray("linkingPos");
			if(link!=null&&link.length > 3)
			{
				tooltip.add(I18n.format(Lib.DESC_INFO+"attachedToDim", link[1], link[2], link[3], link[0]));
			}
		}
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		return RotaryUtils.useCoil(this, player, world, pos, hand, side, hitX, hitY, hitZ);
	}

	/**
	 * Hey you, yes you, copying that class, <br>
	 * Please remember to call your version of this method somewhere in your ClientProxy <br>
	 * Have a nice day ^^ <br>
	 */
	public void setRenderModels()
	{
		MotorBelt.CLOTH.model = new ModelClothMotorBelt();
		MotorBelt.STEEL.model = new ModelSteelMotorBelt();
		MotorBelt.RUBBER.model = new ModelClothMotorBelt();
	}

	public enum MotorBelt implements IStringSerializable
	{
		CLOTH("light_belts", MechanicalDevices.beltLength[0], 1, 6, MechanicalDevices.beltMaxTorque[0], MechanicalDevices.beltTorqueLoss[0]),
		STEEL("heavy_belts", MechanicalDevices.beltLength[1], 1, 8, MechanicalDevices.beltMaxTorque[1], MechanicalDevices.beltTorqueLoss[1]),
		RUBBER("light_belts", MechanicalDevices.beltLength[2], 1, 6, MechanicalDevices.beltMaxTorque[2], MechanicalDevices.beltTorqueLoss[2]);

		/**
		 * Motor belt equivalent of {@link WireType#getCategory()}
		 */
		public final String category;

		/**
		 * See {@link pl.pabilo8.immersiveintelligence.Config.IIConfig.MechanicalDevices#beltMaxTorque}
		 */
		public final int maxTorque;
		/**
		 * See {@link pl.pabilo8.immersiveintelligence.Config.IIConfig.MechanicalDevices#beltTorqueLoss}
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

		public static String[] getNames()
		{
			return Arrays.stream(values()).map(MotorBelt::getName).toArray(String[]::new);
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

		@Override
		public String getName()
		{
			return this.toString().toLowerCase(Locale.ENGLISH);
		}
	}

}
