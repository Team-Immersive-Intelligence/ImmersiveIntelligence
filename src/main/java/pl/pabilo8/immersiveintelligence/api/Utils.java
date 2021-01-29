package pl.pabilo8.immersiveintelligence.api;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
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
import pl.pabilo8.immersiveintelligence.api.utils.IWrench;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

/**
 * @author Pabilo8
 * @since 22-06-2019
 */
public class Utils
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

	public static TargetPoint targetPointFromEntity(Entity entity, int range)
	{
		return new TargetPoint(entity.world.provider.getDimension(), entity.getPosition().getX(), entity.getPosition().getY(), entity.getPosition().getZ(), range);
	}

	public static TargetPoint targetPointFromTile(TileEntity tile, int range)
	{
		return new TargetPoint(tile.getWorld().provider.getDimension(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), range);
	}

	//https://stackoverflow.com/a/52284357/9876980
	public static double root(double num, double root)
	{
		double d = Math.pow(num, 1.0/root);
		long rounded = Math.round(d);
		return Math.abs(rounded-d) < 0.00000000000001?rounded: d;
	}

	//CMY is actually reverse RGB (i tested that out in GIMP ^^)... adding black makes colour darker (less means lighter)
	//Black is actually the limit of darkness (less value - darker) in RGB
	//But because everything is reverse, we get the color with greater value.
	public static int[] rgbToCmyk(int red, int green, int blue)
	{
		return new int[]{255-red, 255-green, 255-blue, 255-Math.min(red, Math.max(green, blue))};
	}

	public static int[] cmykToRgb(int cyan, int magenta, int yellow, int black)
	{
		return new int[]{Math.min(255-black, 255-cyan), Math.min(255-black, 255-magenta), Math.min(255-black, 255-yellow)};
	}

	//Copied from GUIContainer
	public static boolean isPointInRectangle(double x, double y, double xx, double yy, double px, double py)
	{
		return px >= x&&px < xx&&py >= y&&py < yy;
	}

	public static <T extends IFluidTank & IFluidHandler> boolean handleBucketTankInteraction(T[] tanks, NonNullList<ItemStack> inventory, int bucketInputSlot, int bucketOutputSlot, int tank)
	{
		if(inventory.get(bucketInputSlot).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)&&inventory.get(bucketInputSlot).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).getTankProperties()[0].getContents()!=null)
		{

			int amount_prev = tanks[tank].getFluidAmount();

			ItemStack emptyContainer = blusunrize.immersiveengineering.common.util.Utils.drainFluidContainer(tanks[tank], inventory.get(bucketInputSlot), inventory.get(bucketOutputSlot), null);
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

	//Based on https://stackoverflow.com/a/2262117/9876980
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

	//Pitch calculation for artillery stolen from Pneumaticcraft (https://github.com/TeamPneumatic/pnc-repressurized/blob/master/src/main/java/me/desht/pneumaticcraft/common/tileentity/TileEntityAirCannon.java)
	//Huge thanks to desht and MineMaarten for this amazing code!
	public static float calculateBallisticAngle(double distance, double height, float force, double gravity, double drag)
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
		for(double i = Math.PI*0.01D; i < Math.PI*0.5D; i += 0.01D)
		{
			double motionX = MathHelper.cos((float)i)*force;// calculate the x component of the vector
			double motionY = MathHelper.sin((float)i)*force;// calculate the y component of the vector
			double posX = 0;
			double posY = 0;
			while(posY > height||motionY > 0)
			{ // simulate movement, until we reach the y-level required
				posX += motionX;
				posY += motionY;
				motionY -= gravity;
				motionX *= drag;
				motionY *= drag;
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
				||block instanceof BlockSign||block instanceof BlockSkull;
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
		return new Vec3d((double)(f1*f2), (double)f3, (double)(f*f2));
	}
}
