package pl.pabilo8.immersiveintelligence.common.item.weapons;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.entity.mounted_weapon.EntityMortar;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.IItemEntityPlacer;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;

import javax.annotation.Nonnull;

/**
 * Places a mortar with the user's yaw direction.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 03.10.2026
 * @ii-approved 0.3.1
 * @since 23.01.2021
 */
@IIItemProperties(category = IICategory.WARFARE)
public class ItemIIMortar extends ItemIIBase implements IItemEntityPlacer<EntityMortar>
{
	public ItemIIMortar()
	{
		super("mortar", 1);
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Nonnull
	public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing,
									  float hitX, float hitY, float hitZ)
	{
		return IItemEntityPlacer.super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Nonnull
	@Override
	public AxisAlignedBB getPlacedSpace(BlockPos pos)
	{
		return new AxisAlignedBB(pos).expand(0, 0.5, 0);
	}

	/**
	 * Creates a mortar with its body and aim set to the player yaw.
	 */
	@Nonnull
	@Override
	public EntityMortar getPlacedEntity(World world, double x, double y, double z, ItemStack stack, float playerYaw, float playerPitch)
	{
		EntityMortar mortar = new EntityMortar(world);
		mortar.aim.withCenterYaw(playerYaw).withCurrentAngles(playerYaw, mortar.aim.getPitch(0));
		mortar.setLocationAndAngles(x, y, z, mortar.aim.getYaw(0), mortar.aim.getPitch(0));
		mortar.prevRotationYaw = mortar.rotationYaw;
		mortar.prevRotationPitch = mortar.rotationPitch;
		return mortar;
	}
}
