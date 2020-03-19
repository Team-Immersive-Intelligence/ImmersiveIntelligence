package pl.pabilo8.immersiveintelligence.common.items.mechanical;

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
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBase;
import pl.pabilo8.immersiveintelligence.common.wire.IIMotorBeltType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Pabilo8 on 27-12-2019.
 */
public class ItemIIMotorBelt extends ItemIIBase implements IWireCoil
{
	public ItemIIMotorBelt()
	{
		super("motor_belt", 8, MotorBelt.getNames());
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
		return RotaryUtils.doCoilUse(this, player, world, pos, hand, side, hitX, hitY, hitZ);
	}

	//Hey you, yes you, copying that class,
	//Please remember to call your version of this method somewhere in your ClientProxy
	//Have a nice day ^^
	public void setRenderModels()
	{
		MotorBelt.CLOTH.model = new ModelClothMotorBelt();
		MotorBelt.STEEL.model = new ModelSteelMotorBelt();
	}

	public enum MotorBelt implements IStringSerializable
	{
		CLOTH("light_belts", MechanicalDevices.belt_length[0], 1, 6, MechanicalDevices.belt_max_torque[0], MechanicalDevices.belt_max_rpm[0], MechanicalDevices.belt_torque_loss[0]),
		STEEL("heavy_belts", MechanicalDevices.belt_length[1], 1, 8, MechanicalDevices.belt_max_torque[1], MechanicalDevices.belt_max_rpm[1], MechanicalDevices.belt_torque_loss[1]);

		public ResourceLocation res;
		public String category;
		public int thickness;
		public float torqueLoss;
		public int maxTorque, maxRPM, length, width;
		public IIMotorBeltType type;
		@SideOnly(Side.CLIENT)
		public IModelMotorBelt model;

		MotorBelt(String category, int length, int thickness, int width, int maxTorque, int maxRPM, float torqueLoss)
		{
			res = getResourceLocation(getName());
			this.category = category;
			this.length = length;
			this.thickness = thickness;
			this.maxTorque = maxTorque;
			this.maxRPM = maxRPM;
			this.torqueLoss = torqueLoss;
			this.width = width;
			type = new IIMotorBeltType(this);
		}

		public static String[] getNames()
		{
			ArrayList<String> list = new ArrayList<>();
			String[] a = new String[list.size()];
			for(MotorBelt belt : values())
				list.add(belt.getName());
			return list.toArray(a);
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
