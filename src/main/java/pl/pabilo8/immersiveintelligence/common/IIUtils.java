package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.RailgunHandler;
import blusunrize.immersiveengineering.api.tool.RailgunHandler.RailgunProjectileProperties;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import com.google.common.math.IntMath;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IWrench;
import pl.pabilo8.immersiveintelligence.common.compat.BaublesHelper;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @since 22-06-2019
 */
@SuppressWarnings("unused")
public class IIUtils
{
	public static double getDistanceBetweenPos(BlockPos pos1, BlockPos pos2, boolean center)
	{
		double deltaX = (pos1.getX()+(center?0d: 0.5d))-(pos2.getX()+(center?0d: 0.5d));
		double deltaY = (pos1.getY()+(center?0d: 0.5d))-(pos2.getY()+(center?0d: 0.5d));
		double deltaZ = (pos1.getZ()+(center?0d: 0.5d))-(pos2.getZ()+(center?0d: 0.5d));

		return Math.sqrt((deltaX*deltaX)+(deltaY*deltaY)+(deltaZ*deltaZ));
	}

	@Nullable
	public static IDataConnector findConnectorFacing(BlockPos pos, World world, EnumFacing facing)
	{
		BlockPos newpos = pos.offset(facing);
		if(!world.isBlockLoaded(newpos))
			return null;
		TileEntity te = world.getTileEntity(newpos);

		if(te instanceof IDataConnector&&te instanceof IDirectionalTile)
		{
			IDirectionalTile t = (IDirectionalTile)te;
			if(t.getFacing()==facing.getOpposite())
				return (IDataConnector)te;
		}
		return null;
	}

	@Nullable
	public static IDataConnector findConnectorAround(BlockPos pos, World world)
	{
		for(EnumFacing facing : EnumFacing.HORIZONTALS)
		{
			IDataConnector conn = findConnectorFacing(pos, world, facing);
			if(conn!=null)
				return conn;
		}
		return null;
	}

	/**
	 * <a href="https://stackoverflow.com/a/52284357/9876980">https://stackoverflow.com/a/52284357/9876980</a>
	 */
	public static double root(double num, double root)
	{
		double d = Math.pow(num, 1.0/root);
		long rounded = Math.round(d);
		return Math.abs(rounded-d) < 0.00000000000001?rounded: d;
	}

	/**
	 * CMY is actually reverse RGB (i tested that out in GIMP ^^)... adding black makes colour darker (less means lighter)<br>
	 * Black is actually the limit of darkness (less value - darker) in RGB<br>
	 * But because everything is reverse, we get the color with greater value.<br>
	 */
	public static int[] rgbToCmyk(int red, int green, int blue)
	{
		return new int[]{255-red, 255-green, 255-blue, 255-Math.min(red, Math.max(green, blue))};
	}

	/**
	 * @param r red amount (0-1)
	 * @param g green amount (0-1)
	 * @param b blue amount (0-1)
	 * @return float cmyk color array with values 0-1
	 */
	public static float[] rgbToCmyk(float r, float g, float b)
	{
		int[] cmyk = rgbToCmyk((int)(r*255), (int)(g*255), (int)(b*255));
		return new float[]{cmyk[0]/255f, cmyk[1]/255f, cmyk[2]/255f, cmyk[3]/255f};
	}

	public static float[] rgbToCmyk(float[] rgb)
	{
		return rgbToCmyk(rgb[0], rgb[1], rgb[2]);
	}

	/**
	 * @param cyan    cyan amount (0-255)
	 * @param magenta magenta amount (0-255)
	 * @param yellow  yellow amount (0-255)
	 * @param black   black amount (0-255)
	 * @return float cmyk color array with values 0-1
	 */
	public static int[] cmykToRgb(int cyan, int magenta, int yellow, int black)
	{
		return new int[]{Math.min(255-black, 255-cyan), Math.min(255-black, 255-magenta), Math.min(255-black, 255-yellow)};
	}

	public static float[] cmykToRgb(float c, float m, float y, float b)
	{
		int[] dec = cmykToRgb((int)(c*255), (int)(m*255), (int)(y*255), (int)(b*255));
		return new float[]{dec[0]/255f, dec[1]/255f, dec[2]/255f};
	}

	/**
	 * stolen from MathHelper class
	 *
	 * @param hue        hue amount (NOT DEGREES) in 0-1
	 * @param saturation saturation amount in 0-1
	 * @param value      value in 0-1
	 * @return float rgb color array with values 0-1
	 */
	public static float[] hsvToRgb(float hue, float saturation, float value)
	{
		int i = (int)(hue*6.0F)%6;
		float f = hue*6.0F-(float)i;
		float f1 = value*(1.0F-saturation);
		float f2 = value*(1.0F-f*saturation);
		float f3 = value*(1.0F-(1.0F-f)*saturation);
		float r, g, b;

		switch(i)
		{
			case 0:
				r = value;
				g = f3;
				b = f1;
				break;
			case 1:
				r = f2;
				g = value;
				b = f1;
				break;
			case 2:
				r = f1;
				g = value;
				b = f3;
				break;
			case 3:
				r = f1;
				g = f2;
				b = value;
				break;
			case 4:
				r = f3;
				g = f1;
				b = value;
				break;
			case 5:
				r = value;
				g = f1;
				b = f2;
				break;
			default:
				r = 0;
				g = 0;
				b = 0;
				break;
		}

		return new float[]{r, g, b};
	}

	/**
	 * based on formula from <a href="https://www.rapidtables.com/convert/color/rgb-to-hsv.html">https://www.rapidtables.com/convert/color/rgb-to-hsv.html</a>
	 *
	 * @param r red amount (0-1)
	 * @param g green amount (0-1)
	 * @param b blue amount (0-1)
	 * @return float hsv array with values 0-1
	 */
	public static float[] rgbToHsv(float r, float g, float b)
	{
		float cMax = Math.max(Math.max(r, g), b);
		float cMin = Math.min(Math.min(r, g), b);
		float d = cMax-cMin;

		float s = cMax!=0?d/cMax: 0;
		float h;
		if(d==0)
			h = 0;
		else if(cMax==r)
			h = (((g-b)/d)%6f)/6f;
		else if(cMax==g)
			h = (((b-r)/d)+2)/6f;
		else if(cMax==b)
			h = (((r-g)/d)+4)/6f;
		else
			h = 0;

		if(h < 0)
			h = 1f+h;

		return new float[]{h, s, cMax};
	}

	//Copied from GUIContainer
	public static boolean isPointInRectangle(double x, double y, double xx, double yy, double px, double py)
	{
		return px >= x&&px < xx&&py >= y&&py < yy;
	}

	/**
	 * From StackOverflow (yet again!)
	 * <a href="https://stackoverflow.com/a/9755252/9876980">https://stackoverflow.com/a/9755252/9876980</a>
	 *
	 * @param x   point 1's x
	 * @param y   point 1's y
	 * @param xx  point 2's x
	 * @param yy  point 2's y
	 * @param xxx point 3's x
	 * @param yyy point 3's y
	 * @return whether px and py is inside the triangle
	 */
	public static boolean isPointInTriangle(int x, int y, int xx, int yy, int xxx, int yyy, int px, int py)
	{
		int as_x = px-x;
		int as_y = py-y;

		boolean s_ab = (xx-x)*as_y-(yy-y)*as_x > 0;

		if((xxx-x)*as_y-(yyy-y)*as_x > 0==s_ab) return false;
		return (xxx-xx)*(py-yy)-(yyy-yy)*(px-xx) > 0==s_ab;
	}

	public static <T extends IFluidTank & IFluidHandler> boolean handleBucketTankInteraction(T[] tanks, NonNullList<ItemStack> inventory, int bucketInputSlot, int bucketOutputSlot, int tank, boolean fillBucket)
	{
		return handleBucketTankInteraction(tanks, inventory, bucketInputSlot, bucketOutputSlot, tank, fillBucket, fluidStack -> true);
	}

	public static <T extends IFluidTank & IFluidHandler> boolean handleBucketTankInteraction(T[] tanks, NonNullList<ItemStack> inventory, int bucketInputSlot, int bucketOutputSlot, int tank, boolean fillBucket, Predicate<FluidStack> filter)
	{
		if(inventory.get(bucketInputSlot).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
		{
			IFluidHandlerItem capability = inventory.get(bucketInputSlot).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			if(!filter.test(capability.getTankProperties()[0].getContents()))
				return false;

			int amount_prev = tanks[tank].getFluidAmount();
			ItemStack emptyContainer;

			if(fillBucket)
			{
				if(tanks[tank].getTankProperties()[0].getContents()==null)
					return false;
				emptyContainer = blusunrize.immersiveengineering.common.util.Utils.fillFluidContainer(tanks[tank], inventory.get(bucketInputSlot), inventory.get(bucketOutputSlot), null);
			}
			else
			{
				if(capability.getTankProperties()[0].getContents()==null)
					return false;
				emptyContainer = blusunrize.immersiveengineering.common.util.Utils.drainFluidContainer(tanks[tank], inventory.get(bucketInputSlot), inventory.get(bucketOutputSlot), null);
			}

			if(amount_prev!=tanks[tank].getFluidAmount())
			{
				if(!inventory.get(bucketOutputSlot).isEmpty()&&OreDictionary.itemMatches(inventory.get(bucketOutputSlot), emptyContainer, true))
					inventory.get(bucketOutputSlot).grow(emptyContainer.getCount());
				else if(inventory.get(bucketOutputSlot).isEmpty())
					inventory.set(bucketOutputSlot, emptyContainer.copy());
				inventory.get(bucketInputSlot).shrink(1);
				if(inventory.get(bucketInputSlot).getCount() <= 0)
					inventory.set(bucketInputSlot, ItemStack.EMPTY);

				return true;
			}
		}
		return false;
	}

	public static boolean outputFluidToTank(FluidTank tank, int amount, BlockPos pos, World world, EnumFacing side)
	{
		if(tank.getFluidAmount() > 0)
		{

			FluidStack out = tank.drain(Math.min(tank.getFluidAmount(), amount), false);
			IFluidHandler output = FluidUtil.getFluidHandler(world, pos.offset(side), side.getOpposite());
			if(output!=null)
			{
				int accepted = output.fill(out, true);
				if(accepted > 0)
				{
					int drained = output.fill(blusunrize.immersiveengineering.common.util.Utils.copyFluidStackWithAmount(out, Math.min(out.amount, accepted), false), true);
					tank.drain(drained, true);
					return true;
				}
			}
		}
		return false;
	}


	/**
	 * @param offset (length) of the vector
	 * @param yaw    of the vector (in radians)
	 * @param pitch  of the vector (in radians)
	 * @return direction transformed position
	 * @author Pabilo8
	 * <p>
	 * Used to calculate 3D vector offset in a direction
	 * </p>
	 */
	public static Vec3d offsetPosDirection(float offset, double yaw, double pitch)
	{
		if(offset==0)
			return new Vec3d(0, 0, 0);

		double yy = (MathHelper.sin((float)pitch)*offset);
		double true_offset = (MathHelper.cos((float)pitch)*offset);

		double xx = (MathHelper.sin((float)yaw)*true_offset);
		double zz = (MathHelper.cos((float)yaw)*true_offset);

		return new Vec3d(xx, yy, zz);
	}

	/**
	 * based on <a href="https://stackoverflow.com/a/2262117/9876980">https://stackoverflow.com/a/2262117/9876980</a>
	 *
	 * @param rgb color in rgbInt
	 * @return color in rgbFloats format
	 */
	public static float[] rgbIntToRGB(int rgb)
	{
		float r = ((rgb>>16)&0x0ff)*0.003921f;
		float g = ((rgb>>8)&0x0ff)*0.003921f;
		float b = (rgb&0x0ff)*0.003921f;
		return new float[]{r, g, b};
	}

	public static char cycleDataPacketChars(char current, boolean forward, boolean hasEmpty)
	{
		if(hasEmpty)
		{
			if(current==' ')
			{
				if(forward)
					current = DataPacket.varCharacters[0];
				else
					current = DataPacket.varCharacters[DataPacket.varCharacters.length-1];
			}
			else
			{
				int current_char;

				current_char = ArrayUtils.indexOf(DataPacket.varCharacters, current);
				current_char += forward?1: -1;

				if(current_char >= DataPacket.varCharacters.length||current_char < 0)
					current = ' ';
				else
					current = DataPacket.varCharacters[current_char];
			}
		}
		else
		{
			int current_char;

			current_char = ArrayUtils.indexOf(DataPacket.varCharacters, current);
			current_char += forward?1: -1;

			if(current_char >= DataPacket.varCharacters.length)
				current = DataPacket.varCharacters[0];
			else if(current_char < 0)
				current = DataPacket.varCharacters[DataPacket.varCharacters.length-1];
			else
				current = DataPacket.varCharacters[current_char];
		}
		return current;
	}

	public static char cyclePacketCharsAvoiding(char current, boolean forward, boolean hasEmpty, DataPacket packet)
	{
		int current_char = ArrayUtils.indexOf(DataPacket.varCharacters, current);
		int repeats = DataPacket.varCharacters.length+(hasEmpty?1: 0);

		for(int i = 0; i < repeats; i++)
		{
			current_char += forward?1: -1;
			if(current_char >= repeats)
				current_char = 0;
			if(current_char < 0)
				current_char = repeats-1;

			char c = (hasEmpty&&current_char==DataPacket.varCharacters.length)?' ': DataPacket.varCharacters[current_char];

			if(!packet.hasVariable(c))
			{
				return c;
			}
		}
		return current; //¯\_(ツ)_/¯
	}

	/**
	 * Works™
	 */
	public static float clampedLerp3Par(float e1, float e2, float e3, float percent)
	{
		return (float)MathHelper.clampedLerp(MathHelper.clampedLerp(e1, e2, percent*2), e3, Math.max(percent-0.5f, 0)*2);
	}

	/**
	 * Pitch calculation for artillery stolen from Pneumaticcraft. Huge thanks to desht and MineMaarten for this amazing code!
	 * <a href="https://github.com/TeamPneumatic/pnc-repressurized/blob/master/src/main/java/me/desht/pneumaticcraft/common/tileentity/TileEntityAirCannon.java">https://github.com/TeamPneumatic/pnc-repressurized/blob/master/src/main/java/me/desht/pneumaticcraft/common/tileentity/TileEntityAirCannon.java</a>
	 *
	 * @param distance       distance to target
	 * @param height         height difference between the gun and target
	 * @param force          speed (blocks/s) of the bullet
	 * @param gravity        gravity of the bullet
	 * @param drag           drag factor of the bullet
	 * @param anglePrecision precision with which the angle will be searched, the lower the number, the higher the precision
	 * @return optimal ballistic shooting angle
	 * @author desht
	 * @author MineMaarten
	 */
	public static float calculateBallisticAngle(double distance, double height, float force, double gravity, double drag, double anglePrecision)
	{
		double bestAngle = 0;
		double bestDistance = Float.MAX_VALUE;
		if(gravity==0D)
		{
			return 90F-(float)(Math.atan(height/distance)*180F/Math.PI);
		}
		/*
			simulate the trajectory for angles from 45 to 90 degrees,
			returning the angle which lands the projectile closest to the target distance
		*/
		for(double i = Math.PI*anglePrecision; i < Math.PI*0.5D; i += anglePrecision)
		{
			double motionX = MathHelper.cos((float)i)*force;// calculate the x component of the vector
			double motionY = MathHelper.sin((float)i)*force;// calculate the y component of the vector
			double posX = 0;
			double posY = 0;
			while(posY > height||motionY > 0)
			{
				// simulate movement, until we reach the y-level required
				motionX *= drag;
				motionY *= drag;
				motionY -= gravity;
				posX += motionX;
				posY += motionY;
			}
			double distanceToTarget = Math.abs(distance-posX);
			if(distanceToTarget < bestDistance)
			{
				bestDistance = distanceToTarget;
				bestAngle = i;
			}
		}

		return 90F-(float)(bestAngle*180D/Math.PI);
	}

	public static boolean isAdvancedHammer(ItemStack stack)
	{
		if(stack.isEmpty())
			return false;
		return stack.getItem().getToolClasses(stack).contains(IIReference.TOOL_ADVANCED_HAMMER);
	}

	public static boolean isAABBContained(@Nonnull AxisAlignedBB compared, @Nonnull AxisAlignedBB comparedTo)
	{
		Vec3d c0, c1, c2, c3, c4, c5, c6, c7;
		c0 = new Vec3d(compared.minX, compared.minY, compared.minZ);
		c1 = new Vec3d(compared.maxX, compared.minY, compared.minZ);
		c2 = new Vec3d(compared.minX, compared.maxY, compared.minZ);
		c3 = new Vec3d(compared.maxX, compared.maxY, compared.minZ);
		c4 = new Vec3d(compared.minX, compared.minY, compared.maxZ);
		c5 = new Vec3d(compared.maxX, compared.minY, compared.maxZ);
		c6 = new Vec3d(compared.minX, compared.maxY, compared.maxZ);
		c7 = new Vec3d(compared.maxX, compared.maxY, compared.maxZ);

		AxisAlignedBB comp2 = comparedTo.grow(0.1f);

		return comp2.contains(c0)&&comp2.contains(c1)&&comp2.contains(c2)&&comp2.contains(c3)
				&&comp2.contains(c4)&&comp2.contains(c5)&&comp2.contains(c6)&&comp2.contains(c7);
	}

	//Converts snake_case to camelCase or CamelCase
	//Copy as you wish
	public static String toCamelCase(String string, boolean startSmall)
	{
		StringBuilder result = new StringBuilder();
		String[] all = string.split("_");
		for(String s : all)
		{
			result.append(Character.toUpperCase(s.charAt(0)));
			result.append(s.substring(1));
		}
		if(startSmall)
			result.setCharAt(0, Character.toLowerCase(result.charAt(0)));
		return result.toString();
	}

	public static void unlockIIAdvancement(EntityPlayer player, String name)
	{
		if(player instanceof EntityPlayerMP)
		{
			//Can't unlock the same advancement twice
			if(hasUnlockedIIAdvancement(player, name))
				return;

			PlayerAdvancements advancements = ((EntityPlayerMP)player).getAdvancements();
			AdvancementManager manager = ((WorldServer)player.getEntityWorld()).getAdvancementManager();
			Advancement advancement = manager.getAdvancement(new ResourceLocation(ImmersiveIntelligence.MODID, name));
			if(advancement!=null)
				advancements.grantCriterion(advancement, "code_trigger");
		}
	}

	public static boolean hasUnlockedIIAdvancement(EntityPlayer player, String name)
	{
		if(player instanceof EntityPlayerMP)
		{
			PlayerAdvancements advancements = ((EntityPlayerMP)player).getAdvancements();
			AdvancementManager manager = ((WorldServer)player.getEntityWorld()).getAdvancementManager();
			Advancement advancement = manager.getAdvancement(new ResourceLocation(ImmersiveIntelligence.MODID, name));
			if(advancement!=null)
				return advancements.getProgress(advancement).isDone();
		}
		return false;
	}

	public static boolean isWrench(ItemStack stack)
	{
		if(stack.isEmpty())
			return false;
		return stack.getItem().getToolClasses(stack).contains(IIReference.TOOL_WRENCH)&&stack.getItem() instanceof IWrench;
	}

	public static boolean isTachometer(ItemStack stack)
	{
		if(stack.isEmpty())
			return false;
		return stack.getItem().getToolClasses(stack).contains(IIReference.TOOL_TACHOMETER);
	}

	public static boolean isCrowbar(ItemStack stack)
	{
		if(stack.isEmpty())
			return false;
		return stack.getItem().getToolClasses(stack).contains(IIReference.TOOL_CROWBAR);
	}

	public static boolean isVoltmeter(ItemStack stack)
	{
		if(stack.isEmpty())
			return false;
		return OreDictionary.itemMatches(new ItemStack(IEContent.itemTool, 1, 2), stack, true);
	}

	public static int cycleInt(boolean forward, int current, int min, int max)
	{
		current += forward?1: -1;
		if(current > max)
			return min;
		else if(current < min)
			return max;
		return current;
	}

	public static int cycleIntAvoid(boolean forward, int current, int min, int max, int avoid)
	{
		int i = cycleInt(forward, current, min, max);
		if(i==avoid)
			return cycleInt(forward, i, min, max);
		else
			return i;
	}

	public static <E extends Enum<E>> E cycleEnum(boolean forward, Class<E> enumType, E current)
	{
		return enumType.getEnumConstants()[cycleInt(forward, current.ordinal(), 0, enumType.getEnumConstants().length-1)];
	}

	public static boolean compareBlockstateOredict(IBlockState state, String oreName)
	{
		ItemStack stack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
		return blusunrize.immersiveengineering.common.util.Utils.compareToOreName(stack, oreName);
	}

	public static int RGBAToRGB(int color)
	{
		return color-(color>>24&0xFF);
	}

	static double colorDistance(int a, int b)
	{
		float[] f1 = rgbIntToRGB(a);
		float[] f2 = rgbIntToRGB(b);
		return colorDistance(f1, f2);
	}

	static double colorDistance(float[] f1, float[] f2)
	{
		int deltaR = (int)(f1[0]*255-f2[0]*255);
		int deltaG = (int)(f1[1]*255-f2[1]*255);
		int deltaB = (int)(f1[2]*255-f2[2]*255);
		return Math.abs((deltaR*deltaR+deltaG*deltaG+deltaB*deltaB)/3.0);
	}

	/**
	 * @param color color in rgbInt
	 * @return closest dye color
	 */
	public static EnumDyeColor getRGBTextFormatting(int color)
	{
		float[] cc = rgbIntToRGB(color);
		Optional<EnumDyeColor> min = Arrays.stream(EnumDyeColor.values()).min(Comparator.comparingDouble(value -> colorDistance(value.getColorComponentValues(), cc)));
		return min.orElse(EnumDyeColor.BLACK);
	}

	/**
	 * Creates a Vec3 using the pitch and yaw of the entities rotation.
	 */
	public static Vec3d getVectorForRotation(float pitch, float yaw)
	{
		float f = MathHelper.cos(-yaw*0.017453292F-(float)Math.PI);
		float f1 = MathHelper.sin(-yaw*0.017453292F-(float)Math.PI);
		float f2 = -MathHelper.cos(-pitch*0.017453292F);
		float f3 = MathHelper.sin(-pitch*0.017453292F);
		return new Vec3d(f1*f2, f3, f*f2);
	}

	public static float getMaxClientProgress(float current, float required, int parts)
	{
		return current-(current%(required/parts));
	}

	/**
	 * Makes an integer color from the given red, green, and blue float (0-1) values
	 * Stolen from MathHelper because of Side=Client annotation
	 */
	public static int rgb(float rIn, float gIn, float bIn)
	{
		return rgb(MathHelper.floor(rIn*255.0F), MathHelper.floor(gIn*255.0F), MathHelper.floor(bIn*255.0F));
	}

	/**
	 * Makes a single int color with the given red, green, and blue (0-255) values.
	 * Stolen from MathHelper because of Side=Client annotation
	 */
	public static int rgb(int rIn, int gIn, int bIn)
	{
		int lvt_3_1_ = (rIn<<8)+gIn;
		lvt_3_1_ = (lvt_3_1_<<8)+bIn;
		return lvt_3_1_;
	}

	/**
	 * @param colour1    in 3 float array format
	 * @param colour2    in 3 float array format
	 * @param proportion how much of second color is mixed to the first one
	 * @return color in between
	 */
	public static float[] medColour(float[] colour1, float[] colour2, float proportion)
	{
		float rev = 1f-proportion;
		return new float[]{
				(colour1[0]*rev+colour2[0]*proportion),
				(colour1[1]*rev+colour2[1]*proportion),
				(colour1[2]*rev+colour2[2]*proportion)
		};
	}

	public static boolean inRange(int value, int maxValue, double min, double max)
	{
		double vv = value/(double)maxValue;
		return vv >= min&&vv <= max;
	}

	@Deprecated
	public static String getPowerLevelString(TileEntityMultiblockMetal<?, ?> tile)
	{
		return getPowerLevelString(tile.getEnergyStored(null), tile.getMaxEnergyStored(null));
	}

	public static String getPowerLevelString(EnergyStorage storage)
	{
		return getPowerLevelString(storage.getEnergyStored(), storage.getMaxEnergyStored());
	}

	public static String getPowerLevelString(int min, int max)
	{
		return String.format("%s/%s IF", min, max);
	}

	public static String getItalicString(String string)
	{
		return TextFormatting.ITALIC+string+TextFormatting.RESET;
	}

	public static String getHexCol(int color, String text)
	{
		return getHexCol(Integer.toHexString(color), text);
	}

	public static String getHexCol(String color, String text)
	{
		return String.format("<hexcol=%s:%s>", color, text);
	}

	/**
	 * @param number   to be rounded
	 * @param decimals after the separator
	 * @return a (efficiently) rounded number
	 */
	public static float roundFloat(float number, int decimals)
	{
		int pow = 1;
		for(int i = 0; i < decimals; i++)
			pow *= 10;
		float tmp = number*pow;

		return (float)Math.round(tmp)/pow;
	}

	public static String toSnakeCase(String value)
	{
		return value.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
	}

	public static float getDirectFireAngle(float initialForce, float mass, Vec3d toTarget)
	{
		float force = initialForce;
		double dist = toTarget.distanceTo(new Vec3d(0, toTarget.y, 0));
		double gravityMotionY = 0, motionY = 0, baseMotionY = toTarget.normalize().y, baseMotionYC;

		while(dist > 0)
		{
			force -= EntityBullet.DRAG*force*EntityBullet.DEV_SLOMO;
			gravityMotionY -= EntityBullet.GRAVITY*mass*EntityBullet.DEV_SLOMO;
			baseMotionYC = baseMotionY*(force/(initialForce));
			motionY += (baseMotionYC+gravityMotionY)*EntityBullet.DEV_SLOMO;
			dist -= EntityBullet.DEV_SLOMO*force;
		}

		toTarget = toTarget.addVector(0, motionY-baseMotionY, 0).normalize();

		return (float)Math.toDegrees((Math.atan2(toTarget.y, toTarget.distanceTo(new Vec3d(0, toTarget.y, 0)))));
	}

	public static float getIEDirectRailgunAngle(ItemStack ammo, Vec3d toTarget)
	{
		RailgunProjectileProperties p = RailgunHandler.getProjectileProperties(ammo);
		if(p!=null)
		{
			float force = 20;
			float gravity = (float)p.gravity;

			double gravityMotionY = 0, motionY = 0, baseMotionY = toTarget.normalize().y, baseMotionYC = baseMotionY;
			double dist = toTarget.distanceTo(new Vec3d(0, toTarget.y, 0));
			while(dist > 0)
			{
				dist -= force;
				force *= 0.99;
				baseMotionYC *= 0.99f;
				gravityMotionY -= gravity/force;
				motionY += (baseMotionYC+gravityMotionY);
			}

			toTarget = toTarget.addVector(0, motionY-baseMotionY, 0).normalize();
		}

		return (float)Math.toDegrees((Math.atan2(toTarget.y, toTarget.distanceTo(new Vec3d(0, toTarget.y, 0)))));
	}

	public static Vec3d getEntityCenter(Entity entity)
	{
		return entity.getPositionVector().addVector(-entity.width/2f, entity.height/2f, -entity.width/2f);
	}

	@Nonnull
	public static EnumFacing getFacingBetweenPos(@Nonnull BlockPos fromPos, @Nonnull BlockPos toPos)
	{
		Vec3d vv = new Vec3d(fromPos.subtract(toPos)).normalize();
		return EnumFacing.getFacingFromVector((float)vv.x, (float)vv.y, (float)vv.x);
	}

	public static IngredientStack ingredientFromData(IDataType dataType)
	{
		if(dataType instanceof DataTypeItemStack)
			return new IngredientStack((((DataTypeItemStack)dataType).value.copy()));
		else if(dataType instanceof DataTypeString)
			return new IngredientStack(dataType.valueToString());
		else
			return new IngredientStack("*");
	}

	public static DataPacket getSimpleCallbackMessage(DataPacket packet, String parameter, IDataType value)
	{
		packet.setVariable('c', new DataTypeString(parameter));
		packet.setVariable('g', value);
		return packet;
	}

	public static void giveOrDropCasingStack(@Nonnull Entity entity, ItemStack stack)
	{
		//attempt to give the item
		if(entity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
		{
			IItemHandler capability = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if(capability!=null)
			{
				for(int i = 0; i < 10; i++)
				{
					if(stack.isEmpty())
						break;

					ItemStack pouchStack;
					if(i==0)
						pouchStack = (IICompatModule.baubles&&entity instanceof EntityPlayer)?
								BaublesHelper.getWornPouch(((EntityPlayer)entity)):
								ItemStack.EMPTY;
					else
						pouchStack = capability.getStackInSlot(i-1);

					if(!pouchStack.getItem().equals(IIContent.itemCasingPouch))
						continue;

					IItemHandler pouchCap = pouchStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
					if(pouchCap==null)
						continue;

					stack = ItemHandlerHelper.insertItem(pouchCap, stack, false);
					if(entity instanceof EntityPlayer)
						((EntityPlayer)entity).inventoryContainer.detectAndSendChanges();
				}
			}
		}

		if(!stack.isEmpty())
			giveOrDropStack(entity, stack);
	}

	public static void giveOrDropStack(@Nonnull Entity entity, ItemStack stack)
	{
		//attempt to give the item
		if(entity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
		{
			IItemHandler capability = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			stack = ItemHandlerHelper.insertItem(capability, stack, false);
		}
		//if can't do that, drop it on the entity's position
		if(!stack.isEmpty())
			Utils.dropStackAtPos(entity.world, entity.getPosition(), stack);
	}

	/**
	 * No idea why make this client-side only...
	 */
	public static void setEntityVelocity(Entity entity, double motionX, double motionY, double motionZ)
	{
		entity.motionX = motionX;
		entity.motionY = motionY;
		entity.motionZ = motionZ;
		entity.velocityChanged = true;
	}

	public static Vec3d getEntityMotion(Entity entity)
	{
		return new Vec3d(entity.motionX, entity.motionY, entity.motionZ);
	}

	/**
	 * <i>Trust me, I'm an Engineer!</i><br>
	 * Returns a value of an annotation for an enum extending {@link ISerializableEnum}<br>
	 * Generally safe to use, but slow. Cache the results.
	 */
	@Nullable
	public static <T extends Annotation> T getEnumAnnotation(Class<T> annotationClass, ISerializableEnum e)
	{
		try
		{
			Field field = e.getClass().getDeclaredField(e.getName().toUpperCase());
			if(field.isAnnotationPresent(annotationClass))
				return field.getAnnotation(annotationClass);
		} catch(NoSuchFieldException ignored) {}
		return null;
	}

	/**
	 * @param numbers array of numbers, must contain at least two
	 * @return Greatest Common Divisor of multiple numbers
	 */
	public static int gcd(int... numbers)
	{
		int gcd = numbers[0];
		for(int i = 1; i < numbers.length; i++)
			gcd = IntMath.gcd(gcd, numbers[i]);

		return gcd;
	}
}
