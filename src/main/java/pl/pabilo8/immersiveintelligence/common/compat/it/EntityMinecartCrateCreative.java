package pl.pabilo8.immersiveintelligence.common.compat.it;

import mctmods.immersivetechnology.common.ITContent;
import mctmods.immersivetechnology.common.blocks.wooden.types.BlockType_WoodenCrate;
import mctmods.immersivetechnology.common.util.TranslationKey;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import pl.pabilo8.immersiveintelligence.api.utils.minecart.EntityMinecartCrateBase;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;

/**
 * @author Pabilo8
 * @since 07.11.2021
 */
public class EntityMinecartCrateCreative extends EntityMinecartCrateBase implements IAdvancedTextOverlay
{
	public EntityMinecartCrateCreative(World worldIn)
	{
		super(worldIn);
	}

	public EntityMinecartCrateCreative(World worldIn, Vec3d vv)
	{
		super(worldIn, vv);
	}

	@Override
	protected Block getCarriedBlock()
	{
		return ITContent.blockWoodenCrate;
	}

	@Override
	protected int getBlockMetaID()
	{
		return BlockType_WoodenCrate.CRATE.getMeta();
	}

	@Override
	public boolean isIECrate()
	{
		return true;
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory()
	{
		return 1;
	}

	@Override
	public String getGuiID()
	{
		return "it"+":ct_crt";
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
	{
		ItemStack heldItem = player.getHeldItem(hand);
		if(heldItem.isEmpty()^this.itemHandler.getStackInSlot(0).isEmpty())
		{
			NonNullList<ItemStack> contained = ReflectionHelper.getPrivateValue(EntityMinecartContainer.class, this, "minecartContainerItems");

			if(heldItem.isEmpty())
			{
				player.setHeldItem(hand, contained.get(0).copy());
				contained.set(0, ItemStack.EMPTY);
			}
			else
			{
				contained.set(0, player.getHeldItem(hand).copy());
				player.setHeldItem(hand, ItemStack.EMPTY);
			}
			return true;
		}

		return false;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		NonNullList<ItemStack> contained = ReflectionHelper.getPrivateValue(EntityMinecartContainer.class, this, "minecartContainerItems");
		return contained.get(index).copy();
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		NonNullList<ItemStack> contained = ReflectionHelper.getPrivateValue(EntityMinecartContainer.class, this, "minecartContainerItems");
		ItemStack stack = contained.get(index).copy();
		stack.setCount(Math.min(stack.getCount(), count));
		return stack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return getStackInSlot(index);
	}


	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop)
	{
		return new String[]{TranslationKey.OVERLAY_OSD_CREATIVE_CRATE_NORMAL_FIRST_LINE.format(this.getStackInSlot(0).getDisplayName(), 0)};
	}


}
