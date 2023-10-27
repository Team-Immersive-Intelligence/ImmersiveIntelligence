package pl.pabilo8.immersiveintelligence.common.item.ammo;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
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
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumFuseTypes;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IItemScrollable;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelNavalMine;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityNavalMine;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityNavalMineAnchor;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoCasing.Casings;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class ItemIINavalMine extends ItemIIAmmoBase implements IItemScrollable
{
	public ItemIINavalMine()
	{
		super("naval_mine", "naval_mine", Casings.NAVAL_MINE);
		this.setUnlocalizedName(ImmersiveIntelligence.MODID+"."+this.itemName);
	}

	@Override
	public float getComponentMultiplier()
	{
		return 0.55f;
	}

	@Override
	public int getCoreMaterialNeeded()
	{
		return 6;
	}

	@Override
	public float getInitialMass()
	{
		return 1f;
	}

	@Override
	public float getDefaultVelocity()
	{
		return 0f;
	}

	@Override
	public float getCaliber()
	{
		return 16f;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public @Nonnull
	Class<? extends IBulletModel> getModel()
	{
		return ModelNavalMine.class;
	}

	@Override
	public float getDamage()
	{
		return 5;
	}

	@Override
	public ItemStack getCasingStack(int amount)
	{
		return IIContent.itemAmmoCasing.getStack(Casings.NAVAL_MINE, amount);
	}

	@Override
	public EnumCoreTypes[] getAllowedCoreTypes()
	{
		return new EnumCoreTypes[]{EnumCoreTypes.CANISTER};
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


	/**
	 * Called when the equipped item is right clicked.
	 * Boat code
	 */
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		float f = 1.0F;
		float f1 = playerIn.prevRotationPitch+(playerIn.rotationPitch-playerIn.prevRotationPitch);
		float f2 = playerIn.prevRotationYaw+(playerIn.rotationYaw-playerIn.prevRotationYaw);
		double d0 = playerIn.prevPosX+(playerIn.posX-playerIn.prevPosX);
		double d1 = playerIn.prevPosY+(playerIn.posY-playerIn.prevPosY)+(double)playerIn.getEyeHeight();
		double d2 = playerIn.prevPosZ+(playerIn.posZ-playerIn.prevPosZ);
		Vec3d vec3d = new Vec3d(d0, d1, d2);
		float f3 = MathHelper.cos(-f2*0.017453292F-(float)Math.PI);
		float f4 = MathHelper.sin(-f2*0.017453292F-(float)Math.PI);
		float f5 = -MathHelper.cos(-f1*0.017453292F);
		float f6 = MathHelper.sin(-f1*0.017453292F);
		float f7 = f4*f5;
		float f8 = f3*f5;
		Vec3d vec3d1 = vec3d.addVector((double)f7*5.0D, (double)f6*5.0D, (double)f8*5.0D);
		RayTraceResult raytraceresult = worldIn.rayTraceBlocks(vec3d, vec3d1, true);

		if(raytraceresult==null)
		{
			return new ActionResult<>(EnumActionResult.PASS, itemstack);
		}
		else
		{
			Vec3d vec3d2 = playerIn.getLook(1.0F);
			boolean flag = false;
			List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(playerIn, playerIn.getEntityBoundingBox().expand(vec3d2.x*5.0D, vec3d2.y*5.0D, vec3d2.z*5.0D).grow(1.0D));

			for(Entity entity : list)
			{
				if(entity.canBeCollidedWith())
				{
					AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().grow(entity.getCollisionBorderSize());

					if(axisalignedbb.contains(vec3d))
					{
						flag = true;
					}
				}
			}

			if(flag)
			{
				return new ActionResult<>(EnumActionResult.PASS, itemstack);
			}
			else if(raytraceresult.typeOfHit!=RayTraceResult.Type.BLOCK)
			{
				return new ActionResult<>(EnumActionResult.PASS, itemstack);
			}
			else
			{
				Block block = worldIn.getBlockState(raytraceresult.getBlockPos()).getBlock();
				boolean flag1 = block==Blocks.WATER||block==Blocks.FLOWING_WATER;
				EntityNavalMine mine = new EntityNavalMine(worldIn, itemstack, raytraceresult.hitVec.x, flag1?raytraceresult.hitVec.y-0.12D: raytraceresult.hitVec.y, raytraceresult.hitVec.z);

				if(!worldIn.getCollisionBoxes(mine, mine.getEntityBoundingBox().grow(-0.1D)).isEmpty())
				{
					return new ActionResult<>(EnumActionResult.FAIL, itemstack);
				}
				else
				{
					if(!worldIn.isRemote)
					{
						EntityNavalMineAnchor anchor = new EntityNavalMineAnchor(worldIn);
						anchor.setPosition(raytraceresult.hitVec.x, flag1?raytraceresult.hitVec.y-0.12D: raytraceresult.hitVec.y-1.5, raytraceresult.hitVec.z);
						mine.setMaxLength(ItemNBTHelper.hasKey(itemstack, "length")?ItemNBTHelper.getInt(itemstack, "length"): 5);
						worldIn.spawnEntity(anchor);
						mine.setPosition(anchor.posX, anchor.posY-0.5, anchor.posZ);
						worldIn.spawnEntity(mine);
						mine.startRiding(anchor);
						worldIn.playSound(null, mine.posX, mine.posY, mine.posZ, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCKS, 0.75F, 0.8F);
					}

					if(!playerIn.capabilities.isCreativeMode)
					{
						itemstack.shrink(1);
					}

					playerIn.addStat(StatList.getObjectUseStats(this));
					return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
				}
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
	public EnumFuseTypes[] getAllowedFuseTypes()
	{
		return new EnumFuseTypes[]{EnumFuseTypes.CONTACT, EnumFuseTypes.PROXIMITY};
	}

	@Override
	public boolean isProjectile()
	{
		return false;
	}
}
