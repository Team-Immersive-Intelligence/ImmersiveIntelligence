package pl.pabilo8.immersiveintelligence.common.items.tools;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.tool.IDrillHead;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.items.ItemDrill;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBase;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Pabilo8 on 24-04-2020.
 */
public class ItemIIDrillHead extends ItemIIBase implements IDrillHead
{
	public DrillHeadPerm[] perms;

	public ItemIIDrillHead()
	{
		super("drillhead", 1, "tungsten", "iron_diamond_tip", "steel_diamond_tip", "tungsten_diamond_tip");
		perms = new DrillHeadPerm[this.subNames.length];
		//Maximal damage is slightly proportionate to pickaxes
		addPerm(0, new DrillHeadPerm("ingotTungsten", 5, 1, 4, 11, 10, 14000, ImmersiveIntelligence.MODID+":items/drillhead/display/tungsten"));
		addPerm(1, new DrillHeadPerm("ingotIron", 2, 1, 3, 9, 8, 8000, ImmersiveIntelligence.MODID+":items/drillhead/display/iron_tip"));
		addPerm(2, new DrillHeadPerm("ingotSteel", 3, 1, 4, 10, 9, 12000, ImmersiveIntelligence.MODID+":items/drillhead/display/steel_tip"));
		addPerm(3, new DrillHeadPerm("ingotTungsten", 5, 1, 5, 11, 12, 18000, ImmersiveIntelligence.MODID+":items/drillhead/display/tungsten_tip"));

	}

	private void addPerm(int i, DrillHeadPerm perm)
	{
		if(i < perms.length)
			perms[i] = perm;
	}

	private DrillHeadPerm getHeadPerm(ItemStack stack)
	{
		if(stack.getItemDamage() >= 0&&stack.getItemDamage() < perms.length)
			return perms[stack.getItemDamage()];
		return new DrillHeadPerm("", 0, 0, 0, 0, 0, 0, "immersiveengineering:items/drill_diesel");
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		if(stack.getItemDamage() < getSubNames().length)
		{
			list.add(I18n.format(Lib.DESC_FLAVOUR+"drillhead.size", getHeadPerm(stack).drillSize, getHeadPerm(stack).drillDepth));
			list.add(I18n.format(Lib.DESC_FLAVOUR+"drillhead.level", Utils.getHarvestLevelName(getMiningLevel(stack))));
			list.add(I18n.format(Lib.DESC_FLAVOUR+"drillhead.speed", Utils.formatDouble(getMiningSpeed(stack), "0.###")));
			list.add(I18n.format(Lib.DESC_FLAVOUR+"drillhead.damage", Utils.formatDouble(getAttackDamage(stack), "0.###")));

			int maxDmg = getMaximumHeadDamage(stack);
			int dmg = maxDmg-getHeadDamage(stack);
			float quote = dmg/(float)maxDmg;
			String status = ""+(quote < .1?TextFormatting.RED: quote < .3?TextFormatting.GOLD: quote < .6?TextFormatting.YELLOW: TextFormatting.GREEN);
			String s = status+(getMaximumHeadDamage(stack)-getHeadDamage(stack))+"/"+getMaximumHeadDamage(stack);
			list.add(I18n.format(Lib.DESC_INFO+"durability", s));
		}
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		if(this.isInCreativeTab(tab))
			for(int i = 0; i < getSubNames().length; i++)
			{
				ItemStack s = new ItemStack(this, 1, i);
				if(ApiUtils.isExistingOreName(getHeadPerm(s).repairMaterial))
					list.add(s);
			}

	}

	/**
	 * Return whether this item is repairable in an anvil.
	 *
	 * @param stack    the {@code ItemStack} being repaired
	 * @param material the {@code ItemStack} being used to perform the repair
	 */
	@Override
	public boolean getIsRepairable(ItemStack stack, ItemStack material)
	{
		return Utils.compareToOreName(material, getHeadPerm(stack).repairMaterial);
	}

	@Override
	public boolean beforeBlockbreak(ItemStack drill, ItemStack head, EntityPlayer player)
	{
		return false;
	}

	@Override
	public void afterBlockbreak(ItemStack drill, ItemStack head, EntityPlayer player)
	{
	}

	@Override
	public int getMiningLevel(ItemStack head)
	{
		return getHeadPerm(head).drillLevel;
	}

	@Override
	public float getMiningSpeed(ItemStack head)
	{
		return getHeadPerm(head).drillSpeed;
	}

	@Override
	public float getAttackDamage(ItemStack head)
	{
		return getHeadPerm(head).drillAttack;
	}

	@Override
	public int getHeadDamage(ItemStack head)
	{
		return ItemNBTHelper.getInt(head, "headDamage");
	}

	@Override
	public int getMaximumHeadDamage(ItemStack head)
	{
		return getHeadPerm(head).maxDamage;
	}

	@Override
	public void damageHead(ItemStack head, int dmg)
	{
		ItemNBTHelper.setInt(head, "headDamage", ItemNBTHelper.getInt(head, "headDamage")+dmg);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getDrillTexture(ItemStack drill, ItemStack head)
	{
		return getHeadPerm(head).sprite;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return (double)ItemNBTHelper.getInt(stack, "headDamage")/(double)getMaximumHeadDamage(stack);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return ItemNBTHelper.getInt(stack, "headDamage") > 0;
	}

	@Override
	public ImmutableList<BlockPos> getExtraBlocksDug(ItemStack head, World world, EntityPlayer player, RayTraceResult mop)
	{
		EnumFacing side = mop.sideHit;
		int diameter = getHeadPerm(head).drillSize;
		int depth = getHeadPerm(head).drillDepth;

		BlockPos startPos = mop.getBlockPos();
		IBlockState state = world.getBlockState(startPos);
		Block block = state.getBlock();
		float maxHardness = 1;
		if(block!=null&&!block.isAir(state, world, startPos))
			maxHardness = state.getPlayerRelativeBlockHardness(player, world, startPos)*0.6F;
		if(maxHardness < 0)
			maxHardness = 0;

		if(diameter%2==0)//even numbers
		{
			float hx = (float)mop.hitVec.x-mop.getBlockPos().getX();
			float hy = (float)mop.hitVec.y-mop.getBlockPos().getY();
			float hz = (float)mop.hitVec.z-mop.getBlockPos().getZ();
			if((side.getAxis()==Axis.Y&&hx < .5)||(side.getAxis()==Axis.Z&&hx < .5))
				startPos = startPos.add(-diameter/2, 0, 0);
			if(side.getAxis()!=Axis.Y&&hy < .5)
				startPos = startPos.add(0, -diameter/2, 0);
			if((side.getAxis()==Axis.Y&&hz < .5)||(side.getAxis()==Axis.X&&hz < .5))
				startPos = startPos.add(0, 0, -diameter/2);
		}
		else//odd numbers
		{
			startPos = startPos.add(-(side.getAxis()==Axis.X?0: diameter/2), -(side.getAxis()==Axis.Y?0: diameter/2), -(side.getAxis()==Axis.Z?0: diameter/2));
		}
		Builder<BlockPos> b = ImmutableList.builder();
		for(int dd = 0; dd < depth; dd++)
			for(int dw = 0; dw < diameter; dw++)
				for(int dh = 0; dh < diameter; dh++)
				{
					BlockPos pos = startPos.add((side.getAxis()==Axis.X?dd: dw), (side.getAxis()==Axis.Y?dd: dh), (side.getAxis()==Axis.Y?dh: side.getAxis()==Axis.X?dw: dd));
					if(pos.equals(mop.getBlockPos()))
						continue;
					state = world.getBlockState(pos);
					block = state.getBlock();
					float h = state.getPlayerRelativeBlockHardness(player, world, pos);
					boolean canHarvest = block.canHarvestBlock(world, pos, player);
					boolean drillMat = ((ItemDrill)IEContent.itemDrill).isEffective(state.getMaterial());
					boolean hardness = h > maxHardness;
					if(canHarvest&&drillMat&&hardness)
						b.add(pos);
				}
		return b.build();
	}

	public static class DrillHeadPerm
	{
		public final String texture;
		final String repairMaterial;
		final int drillSize;
		final int drillDepth;
		final int drillLevel;
		final float drillSpeed;
		final float drillAttack;
		final int maxDamage;
		@SideOnly(Side.CLIENT)
		public TextureAtlasSprite sprite;

		public DrillHeadPerm(String repairMaterial, int drillSize, int drillDepth, int drillLevel, float drillSpeed, int drillAttack, int maxDamage, String texture)
		{
			this.repairMaterial = repairMaterial;
			this.drillSize = drillSize;
			this.drillDepth = drillDepth;
			this.drillLevel = drillLevel;
			this.drillSpeed = drillSpeed;
			this.drillAttack = drillAttack;
			this.maxDamage = maxDamage;
			this.texture = texture;
		}
	}
}