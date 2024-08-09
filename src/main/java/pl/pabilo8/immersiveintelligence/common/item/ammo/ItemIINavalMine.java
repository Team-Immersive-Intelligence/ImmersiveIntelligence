package pl.pabilo8.immersiveintelligence.common.item.ammo;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.network.play.server.SPacketTitle.Type;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PropellantType;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoFactory;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IItemScrollable;
import pl.pabilo8.immersiveintelligence.client.model.builtin.IAmmoModel;
import pl.pabilo8.immersiveintelligence.client.model.builtin.ModelAmmoNavalMine;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.naval_mine.EntityNavalMine;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.naval_mine.EntityNavalMineAnchor;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoCasing.Casing;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
@IIItemProperties(category = IICategory.WARFARE)
public class ItemIINavalMine extends ItemIIAmmoBase<EntityNavalMine> implements IItemScrollable
{
	public ItemIINavalMine()
	{
		super("naval_mine", "naval_mine", Casing.NAVAL_MINE);
		this.setUnlocalizedName(ImmersiveIntelligence.MODID+"."+this.itemName);
	}

	@Override
	public float getComponentMultiplier()
	{
		return 0.55f;
	}

	@Override
	public PropellantType getAllowedPropellants()
	{
		return PropellantType.NONE;
	}

	@Override
	public int getCoreMaterialNeeded()
	{
		return 6;
	}

	@Override
	public float getCasingMass()
	{
		return 1f;
	}

	@Override
	public float getVelocity()
	{
		return 0f;
	}

	@Override
	public int getCaliber()
	{
		return 16;
	}

	@SideOnly(Side.CLIENT)
	@Nonnull
	@Override
	public Function<ItemIIAmmoBase<EntityNavalMine>, IAmmoModel<ItemIIAmmoBase<EntityNavalMine>, EntityNavalMine>> get3DModel()
	{
		return ModelAmmoNavalMine::createNavalMineModel;
	}

	@Nonnull
	@Override
	public EntityNavalMine getAmmoEntity(World world)
	{
		return new EntityNavalMine(world);
	}

	@Override
	public float getDamage()
	{
		return 5;
	}

	@Override
	public ItemStack getCasingStack(int amount)
	{
		return IIContent.itemAmmoCasing.getStack(Casing.NAVAL_MINE, amount);
	}

	@Override
	public CoreType[] getAllowedCoreTypes()
	{
		return new CoreType[]{CoreType.CANISTER};
	}

	@Override
	public void registerSprites(TextureMap map)
	{

	}

	@Override
	public String getModelCacheKey(ItemStack stack)
	{
		return "";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List<ResourceLocation> getTextures(ItemStack stack, String key)
	{
		return new ArrayList<>();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		int length = ItemNBTHelper.hasKey(stack, "length")?ItemNBTHelper.getInt(stack, "length"): 5;
		tooltip.add(I18n.format(IIReference.DESCRIPTION_KEY+"naval_mine_chain_length",
				TextFormatting.GOLD+Integer.toString(length)));
	}

	/**
	 * Called when the equipped item is right clicked.
	 * Boat code
	 *
	 * @param world  The world the item is in
	 * @param player The player using the item
	 * @param hand   The hand the item is in
	 * @return A new ItemStack to be put in the player's hand
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);

		//Get player's position vector
		float dirLookPitch = player.prevRotationPitch+(player.rotationPitch-player.prevRotationPitch);
		float dirLookYaw = player.prevRotationYaw+(player.rotationYaw-player.prevRotationYaw);
		Vec3d playerVec = new Vec3d(
				player.prevPosX+(player.posX-player.prevPosX),
				player.prevPosY+(player.posY-player.prevPosY)+(double)player.getEyeHeight(),
				player.prevPosZ+(player.posZ-player.prevPosZ)
		);

		//Get player's look vector
		float dX = MathHelper.cos(-dirLookYaw*0.017453292F-(float)Math.PI);
		float dZ = MathHelper.sin(-dirLookYaw*0.017453292F-(float)Math.PI);
		float dY = -MathHelper.cos(-dirLookPitch*0.017453292F);
		Vec3d lookVec = playerVec.addVector(
				dZ*dY*5.0D,
				MathHelper.sin(-dirLookPitch*0.017453292F)*5.0D,
				dX*dY*5.0D
		);
		RayTraceResult raytraceresult = world.rayTraceBlocks(playerVec, lookVec, true);

		//Try to place the mine on look position
		if(raytraceresult==null)
			return new ActionResult<>(EnumActionResult.PASS, stack);
		else
		{
			List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox()
					.expand(lookVec.x, lookVec.y, lookVec.z)
					.grow(1.0D));

			boolean flag = false;
			for(Entity entity : list)
				if(entity.canBeCollidedWith())
				{
					AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().grow(entity.getCollisionBorderSize());
					if(axisalignedbb.contains(playerVec))
						flag = true;
				}

			if(flag)
				return new ActionResult<>(EnumActionResult.PASS, stack);
			else if(raytraceresult.typeOfHit!=RayTraceResult.Type.BLOCK)
				return new ActionResult<>(EnumActionResult.PASS, stack);
			else
			{
				//Whether the mine is inside a fluid block
				IBlockState state = world.getBlockState(raytraceresult.getBlockPos());
				Block block = state.getBlock();
				float liquidHeight = block.getBlockLiquidHeight(world, raytraceresult.getBlockPos(), state, null);

				if(!world.isRemote)
				{
					EntityNavalMine mine = new AmmoFactory<EntityNavalMine>(world)
							.setStack(stack)
							.setPosition(raytraceresult.hitVec)
							.setOwner(player)
							.create();

					EntityNavalMineAnchor anchor = new EntityNavalMineAnchor(world);
					anchor.setPosition(raytraceresult.hitVec.x, raytraceresult.hitVec.y-liquidHeight, raytraceresult.hitVec.z);
					mine.setMaxLength(ItemNBTHelper.hasKey(stack, "length")?ItemNBTHelper.getInt(stack, "length"): 5);
					world.spawnEntity(anchor);
					mine.setPosition(anchor.posX, anchor.posY+0.5-liquidHeight, anchor.posZ);
					world.spawnEntity(mine);
					mine.startRiding(anchor);

					world.playSound(null, mine.posX, mine.posY, mine.posZ,
							liquidHeight > 0?SoundEvents.ENTITY_GENERIC_SPLASH: block.getSoundType(state, world, raytraceresult.getBlockPos(), mine).getPlaceSound(),
							SoundCategory.BLOCKS, 0.75F, 0.8F);
				}

				if(!player.capabilities.isCreativeMode)
					stack.shrink(1);

				player.addStat(StatList.getObjectUseStats(this));
				return new ActionResult<>(EnumActionResult.SUCCESS, stack);
			}
		}
	}

	@Override
	public void onScroll(ItemStack stack, boolean forward, EntityPlayerMP player)
	{
		if(!ItemNBTHelper.hasKey(stack, "length"))
			ItemNBTHelper.setInt(stack, "length", 5);
		ItemNBTHelper.setInt(stack, "length", MathHelper.clamp(ItemNBTHelper.getInt(stack, "length")+(forward?1: -1), 0, 16));
		SPacketTitle packet = new SPacketTitle(Type.ACTIONBAR, new TextComponentTranslation(IIReference.DESCRIPTION_KEY+"naval_mine_chain_length", ItemNBTHelper.getInt(stack, "length")), 0, 20, 0);
		player.connection.sendPacket(packet);
	}

	@Override
	public FuseType[] getAllowedFuseTypes()
	{
		return new FuseType[]{FuseType.CONTACT, FuseType.PROXIMITY};
	}
}
