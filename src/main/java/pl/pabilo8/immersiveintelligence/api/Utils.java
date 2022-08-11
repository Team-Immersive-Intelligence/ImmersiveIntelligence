package pl.pabilo8.immersiveintelligence.api;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.RailgunHandler;
import blusunrize.immersiveengineering.api.tool.RailgunHandler.RailgunProjectileProperties;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
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
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.DamageBlockPos;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;
import pl.pabilo8.immersiveintelligence.api.utils.IWrench;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @since 22-06-2019
 */
public class Utils
{
	public static final int COLOR_POWERBAR_1 = 0xffb51500, COLOR_POWERBAR_2 = 0xff600b00;
	public static final int COLOR_ARMORBAR_1 = 0xcfcfcfcf, COLOR_ARMORBAR_2 = 0x0cfcfcfc;

	public static final int COLOR_H1 = 0x0a0a0a, COLOR_H2 = 0x1a1a1a;

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
		if(world.isBlockLoaded(newpos)&&world.getTileEntity(newpos) instanceof IDataConnector&&world.getTileEntity(newpos) instanceof IDirectionalTile)
		{
			IDirectionalTile t = (IDirectionalTile)world.getTileEntity(newpos);
			if(t.getFacing()==facing.getOpposite())
				return (IDataConnector)world.getTileEntity(newpos);
		}
		return null;
	}

	@Nullable
	public static IDataConnector findConnectorAround(BlockPos pos, World world)
	{
		BlockPos newpos = pos.add(1, 0, 0);
		if(world.isBlockLoaded(newpos)&&world.getTileEntity(newpos) instanceof IDataConnector&&world.getTileEntity(newpos) instanceof IDirectionalTile)
		{
			IDirectionalTile t = (IDirectionalTile)world.getTileEntity(newpos);
			if(t.getFacing()==EnumFacing.WEST)
				return (IDataConnector)world.getTileEntity(newpos);
		}
		newpos = pos.add(-1, 0, 0);
		if(world.isBlockLoaded(newpos)&&world.getTileEntity(newpos) instanceof IDataConnector&&world.getTileEntity(newpos) instanceof IDirectionalTile)
		{
			IDirectionalTile t = (IDirectionalTile)world.getTileEntity(newpos);
			if(t.getFacing()==EnumFacing.EAST)
				return (IDataConnector)world.getTileEntity(newpos);
		}
		newpos = pos.add(0, 0, 1);
		if(world.isBlockLoaded(newpos)&&world.getTileEntity(newpos) instanceof IDataConnector&&world.getTileEntity(newpos) instanceof IDirectionalTile)
		{
			IDirectionalTile t = (IDirectionalTile)world.getTileEntity(newpos);
			if(t.getFacing()==EnumFacing.NORTH)
				return (IDataConnector)world.getTileEntity(newpos);
		}
		newpos = pos.add(0, 0, -1);
		if(world.isBlockLoaded(newpos)&&world.getTileEntity(newpos) instanceof IDataConnector&&world.getTileEntity(newpos) instanceof IDirectionalTile)
		{
			IDirectionalTile t = (IDirectionalTile)world.getTileEntity(newpos);
			if(t.getFacing()==EnumFacing.SOUTH)
				return (IDataConnector)world.getTileEntity(newpos);
		}
		return null;
	}

	public static TargetPoint targetPointFromPos(BlockPos pos, World world, int range)
	{
		return new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), range);
	}

	public static TargetPoint targetPointFromPos(Vec3d pos, World world, int range)
	{
		return new TargetPoint(world.provider.getDimension(), pos.x, pos.y, pos.z, range);
	}

	public static TargetPoint targetPointFromEntity(Entity entity, int range)
	{
		return new TargetPoint(entity.world.provider.getDimension(), entity.getPosition().getX(), entity.getPosition().getY(), entity.getPosition().getZ(), range);
	}

	public static TargetPoint targetPointFromTile(TileEntity tile, int range)
	{
		return new TargetPoint(tile.getWorld().provider.getDimension(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), range);
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
			FluidStack out = blusunrize.immersiveengineering.common.util.Utils.copyFluidStackWithAmount(tank.getFluid(), Math.min(tank.getFluidAmount(), amount), false);
			IFluidHandler output = FluidUtil.getFluidHandler(world, pos.offset(side), side);
			if(output!=null)
			{
				int accepted = output.fill(out, false);
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
		float r = (rgb/256/256%256)/255f;
		float g = (rgb/256%256)/255f;
		float b = (rgb%256)/255f;
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
	 * @param distance
	 * @param height
	 * @param force
	 * @param gravity
	 * @param drag
	 * @param anglePrecision
	 * @return
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
		// simulate the trajectory for angles from 45 to 90 degrees,
		// returning the angle which lands the projectile closest to the target distance
//        for (double i = Math.PI * 0.25D; i < Math.PI * 0.50D; i += 0.001D) {
		for(double i = Math.PI*anglePrecision; i < Math.PI*0.5D; i += anglePrecision)
		{
			double motionX = MathHelper.cos((float)i)*force;// calculate the x component of the vector
			double motionY = MathHelper.sin((float)i)*force;// calculate the y component of the vector
			double posX = 0;
			double posY = 0;
			while(posY > height||motionY > 0)
			{ // simulate movement, until we reach the y-level required
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
		/*ImmersiveIntelligence.logger.info(String.format("Best Distance: %s/%s", (distance-bestDistance), distance));
		ImmersiveIntelligence.logger.info("Calculated Trajectory:");
		{
			double motionX = MathHelper.cos((float)bestAngle)*force;// calculate the x component of the vector
			double motionY = MathHelper.sin((float)bestAngle)*force;// calculate the y component of the vector
			double posX = 0;
			double posY = 0;
			while(posY > height||motionY > 0)
			{ // simulate movement, until we reach the y-level required
				motionX *= drag;
				motionY *= drag;
				motionY -= gravity;
				posX += motionX;
				posY += motionY;
				ImmersiveIntelligence.logger.info(posX+", "+posY);
			}
		}
		ImmersiveIntelligence.logger.info("---");*/

		return 90F-(float)(bestAngle*180D/Math.PI);
	}

	public static boolean isAdvancedHammer(ItemStack stack)
	{
		if(stack.isEmpty())
			return false;
		return stack.getItem().getToolClasses(stack).contains(CommonProxy.TOOL_ADVANCED_HAMMER);
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
		return stack.getItem().getToolClasses(stack).contains(CommonProxy.TOOL_WRENCH)&&stack.getItem() instanceof IWrench;
	}

	public static boolean isTachometer(ItemStack stack)
	{
		if(stack.isEmpty())
			return false;
		return stack.getItem().getToolClasses(stack).contains(CommonProxy.TOOL_TACHOMETER);
	}

	public static boolean isCrowbar(ItemStack stack)
	{
		if(stack.isEmpty())
			return false;
		return stack.getItem().getToolClasses(stack).contains(CommonProxy.TOOL_CROWBAR);
	}

	public static boolean isVoltmeter(ItemStack stack)
	{
		if(stack.isEmpty())
			return false;
		return OreDictionary.itemMatches(new ItemStack(IEContent.itemTool, 1, 2), stack, true);
	}

	@SideOnly(Side.CLIENT)
	public static void drawStringCentered(FontRenderer fontRenderer, String string, int x, int y, int w, int h, int colour)
	{
		fontRenderer.drawString(string, x+(w/2)-(fontRenderer.getStringWidth(string)/2), y+h, colour);
	}

	@SideOnly(Side.CLIENT)
	public static void drawStringCenteredScaled(FontRenderer fontRenderer, String string, int x, int y, int w, int h, float scale, int colour)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x+((w/2)-(fontRenderer.getStringWidth(string)*scale/2)), y+h, 0);
		GlStateManager.scale(scale, scale, 1);
		fontRenderer.drawString(string, 0, 0, colour);
		GlStateManager.popMatrix();
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

	//Cheers, Blu ^^
	@SideOnly(Side.CLIENT)
	public static void tesselateBlockBreak(Tessellator tessellatorIn, WorldClient world, DamageBlockPos blockpos, float partialTicks)
	{
		tesselateBlockBreak(tessellatorIn, world, blockpos, blockpos.damage, partialTicks);
	}

	//Cheers, Blu ^^
	@SideOnly(Side.CLIENT)
	public static void tesselateBlockBreak(Tessellator tessellatorIn, WorldClient world, DimensionBlockPos blockpos, Float value, float partialTicks)
	{
		BufferBuilder worldRendererIn = tessellatorIn.getBuffer();
		EntityPlayer player = ClientUtils.mc().player;
		double d0 = player.lastTickPosX+(player.posX-player.lastTickPosX)*(double)partialTicks;
		double d1 = player.lastTickPosY+(player.posY-player.lastTickPosY)*(double)partialTicks;
		double d2 = player.lastTickPosZ+(player.posZ-player.lastTickPosZ)*(double)partialTicks;
		TextureManager renderEngine = Minecraft.getMinecraft().renderEngine;
		int progress = 9-(int)MathHelper.clamp(value*10f, 0f, 10f); // 0-10
		if(progress < 0)
			return;
		renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		//preRenderDamagedBlocks BEGIN
		GlStateManager.pushMatrix();
		GlStateManager.tryBlendFuncSeparate(774, 768, 1, 1);
		GlStateManager.enableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
		GlStateManager.doPolygonOffset(-3.0F, -3.0F);
		GlStateManager.enablePolygonOffset();
		//GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableAlpha();


		worldRendererIn.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		worldRendererIn.setTranslation(-d0, -d1, -d2);

		Block block = world.getBlockState(blockpos).getBlock();
		TileEntity te = world.getTileEntity(blockpos);
		boolean hasBreak = block instanceof BlockChest||block instanceof BlockEnderChest
				||block instanceof BlockSign||block instanceof BlockSkull||block instanceof BlockIIMultiblock<?>;
		if(!hasBreak) hasBreak = te!=null&&te.canRenderBreaking();
		if(!hasBreak)
		{
			IBlockState iblockstate = world.getBlockState(blockpos);
			if(iblockstate.getMaterial()!=Material.AIR)
			{
				TextureAtlasSprite textureatlassprite = ClientUtils.destroyBlockIcons[progress];
				BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
				blockrendererdispatcher.renderBlockDamage(iblockstate, blockpos, textureatlassprite, world);
			}
		}
		tessellatorIn.draw();

		worldRendererIn.setTranslation(0.0D, 0.0D, 0.0D);
		// postRenderDamagedBlocks BEGIN
		GlStateManager.disableAlpha();
		GlStateManager.doPolygonOffset(0.0F, 0.0F);
		GlStateManager.disablePolygonOffset();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_COLOR, GL11.GL_DST_COLOR, 1, 1);
		GlStateManager.enableAlpha();
		GlStateManager.color(1f, 1f, 1f, 1f);

		GlStateManager.depthMask(true);
		GlStateManager.popMatrix();

	}

	public static ItemStack getStackWithMetaName(BlockIIBase block, String name)
	{
		return new ItemStack(block, 1, block.getMetaBySubname(name));
	}

	public static ItemStack getStackWithMetaName(BlockIIBase block, String name, int count)
	{
		ItemStack stack = getStackWithMetaName(block, name);
		stack.setCount(count);
		return stack;
	}

	public static ItemStack getStackWithMetaName(ItemIIBase item, String name)
	{
		return new ItemStack(item, 1, item.getMetaBySubname(name));
	}

	public static ItemStack getStackWithMetaName(ItemIIBase item, String name, int count)
	{
		ItemStack stack = getStackWithMetaName(item, name);
		stack.setCount(count);
		return stack;
	}

	public static int RGBAToRGB(int color)
	{
		return color-(color >> 24&0xFF);
	}

	//Thanks Blu, these stencil buffers look really capable
	@SideOnly(Side.CLIENT)
	public static void drawItemProgress(ItemStack stack1, ItemStack stack2, float progress, TransformType type, Tessellator tessellator, float scale)
	{
		GlStateManager.scale(scale, scale, scale);

		if(progress==0)
			ClientUtils.mc().getRenderItem().renderItem(stack1, type);
		else if(progress==1)
			ClientUtils.mc().getRenderItem().renderItem(stack2, type);
		else
		{
			float h0 = -.5f;
			float h1 = h0+progress;

			BufferBuilder worldrenderer = tessellator.getBuffer();

			GL11.glEnable(GL11.GL_STENCIL_TEST);

			GlStateManager.colorMask(false, false, false, false);
			GlStateManager.depthMask(false);

			GL11.glStencilFunc(GL11.GL_NEVER, 1, 0xFF);
			GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_KEEP, GL11.GL_KEEP);

			GL11.glStencilMask(0xFF);
			GlStateManager.clear(GL11.GL_STENCIL_BUFFER_BIT);

			GlStateManager.rotate(90.0F-ClientUtils.mc().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);

			GlStateManager.disableTexture2D();
			worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
			ClientUtils.renderBox(worldrenderer, -.5, h0, -.5, .5, h1, .5);
			tessellator.draw();
			GlStateManager.enableTexture2D();

			GlStateManager.rotate(-(90.0F-ClientUtils.mc().getRenderManager().playerViewY), 0.0F, 1.0F, 0.0F);

			GlStateManager.colorMask(true, true, true, true);
			GlStateManager.depthMask(true);

			GL11.glStencilMask(0x00);

			GL11.glStencilFunc(GL11.GL_EQUAL, 0, 0xFF);
			ClientUtils.mc().getRenderItem().renderItem(stack1, type);

			GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
			ClientUtils.mc().getRenderItem().renderItem(stack2, type);

			GL11.glDisable(GL11.GL_STENCIL_TEST);
		}

		GlStateManager.scale(1/scale, 1/scale, 1/scale);
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

	public static void drawArmorBar(int x, int y, int w, int h, float progress)
	{
		drawGradientBar(x, y, w, h, COLOR_ARMORBAR_1, COLOR_ARMORBAR_2, progress);
	}

	public static void drawPowerBar(int x, int y, int w, int h, float progress)
	{
		drawGradientBar(x, y, w, h, COLOR_POWERBAR_1, COLOR_POWERBAR_2, progress);
	}

	public static void drawGradientBar(int x, int y, int w, int h, int color1, int color2, float progress)
	{
		int stored = (int)(h*progress);
		ClientUtils.drawGradientRect(x, y+(h-stored), x+w, y+h, color1, color2);
	}

	@SuppressWarnings("rawtypes")
	public static String getPowerLevelString(TileEntityMultiblockMetal tile)
	{
		return getPowerLevelString(tile.getEnergyStored(null), tile.getMaxEnergyStored(null));
	}

	public static String getPowerLevelString(int min, int max)
	{
		return String.format("%s/%s IF", min, max);
	}

	@SideOnly(Side.CLIENT)
	public static void bindTexture(ResourceLocation path)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(path);
	}

	/**
	 * Rightfully stolen from StackOverflow
	 * <a href="https://stackoverflow.com/a/35833800/9876980">https://stackoverflow.com/a/35833800/9876980</a>
	 *
	 * @param number   to be rounded
	 * @param decimals after the separator
	 * @return a (efficiently) rounded number
	 */
	public static float roundFloat(float number, int decimals)
	{
		int pow = 10;
		for(int i = 1; i < decimals; i++)
			pow *= 10;
		float tmp = number*pow;
		return ((float)((int)((tmp-(int)tmp) >= 0.5f?tmp+1: tmp)))/pow;
	}

	@SideOnly(Side.CLIENT)
	public static ModelRendererTurbo[] createConstructionModel(@Nullable MachineUpgrade upgrade, ModelIIBase model)
	{
		int partCount = model.parts.values().stream().mapToInt(modelRendererTurbos -> modelRendererTurbos.length).sum();
		if(upgrade!=null)
			upgrade.setRequiredSteps(partCount);
		ModelRendererTurbo[] output = new ModelRendererTurbo[partCount];
		int i = 0;
		for(ModelRendererTurbo[] value : model.parts.values())
		{
			Arrays.sort(value, (o1, o2) -> (int)(o1.rotationPointY-o2.rotationPointY));
			for(ModelRendererTurbo mod : value)
			{
				output[i] = mod;
				i++;
			}
		}

		return output;
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

	@SideOnly(Side.CLIENT)
	public static void drawRope(BufferBuilder buff, double x, double y, double z, double xx, double yy, double zz, double xdiff, double zdiff)
	{
		buff.pos(x+xdiff, y, z-zdiff).tex(0f, 0f).endVertex();
		buff.pos(xx+xdiff, yy, zz-zdiff).tex(0f, 1f).endVertex();
		buff.pos(xx-xdiff, yy, zz+zdiff).tex(0.125f, 1f).endVertex();
		buff.pos(x-xdiff, y, z+zdiff).tex(0.125f, 0f).endVertex();
	}

	@SideOnly(Side.CLIENT)
	public static void drawFace(BufferBuilder buff, double x, double y, double z, double xx, double yy, double zz, double u, double uu, double v, double vv)
	{
		buff.pos(x, y, z).tex(u, v).endVertex();
		buff.pos(x, yy, z).tex(u, vv).endVertex();
		buff.pos(xx, yy, zz).tex(uu, vv).endVertex();
		buff.pos(xx, y, zz).tex(uu, v).endVertex();
	}

	@SideOnly(Side.CLIENT)
	public static void drawFluidBlock(@Nonnull FluidStack fs, boolean flowing, double x, double y, double z, double xx, double yy, double zz, boolean drawEnd)
	{
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		String tex = (flowing?fs.getFluid().getFlowing(fs): fs.getFluid().getStill(fs)).toString();
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(tex);

		if(sprite!=null)
		{
			int col = fs.getFluid().getColor(fs);
			double u = sprite.getMinU(), uu = sprite.getMaxU(), v = sprite.getMinV(), vv = v+((sprite.getMaxV()-v)*Math.min(1, (yy-y)/sprite.getIconHeight()));
			double ux = u+((uu-u)*Math.min(1, (xx-x)/sprite.getIconWidth())), uz = u+(uu*Math.min(1, (zz-z)/sprite.getIconWidth()));

			GlStateManager.color((col >> 16&255)/255.0f, (col >> 8&255)/255.0f, (col&255)/255.0f, 1);

			BufferBuilder buff = Tessellator.getInstance().getBuffer();

			buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

			drawFace(buff, x, yy, z, xx, y, z, u, ux, v, vv);
			drawFace(buff, x, yy, zz, xx, y, zz, u, ux, v, vv);
			drawFace(buff, x, yy, z, x, y, zz, u, ux, v, vv);
			drawFace(buff, xx, yy, z, xx, y, zz, u, ux, v, vv);

			Tessellator.getInstance().draw();

			GlStateManager.color(1f, 1f, 1f);
		}
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


}
