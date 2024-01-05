package pl.pabilo8.immersiveintelligence.common.ammo;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoComponent;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageFireworks;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class AmmoComponentFirework implements IAmmoComponent
{
	@Override
	public String getName()
	{
		return "firework";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack(new ItemStack(Items.FIREWORK_CHARGE));
	}

	@Override
	public float getDensity()
	{
		return 1f;
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, float multiplier, NBTTagCompound tag, EnumCoreTypes coreType, Entity owner)
	{
		IIPacketHandler.INSTANCE.sendToAllAround(new MessageFireworks(tag, pos), IIPacketHandler.targetPointFromPos(pos, world, 96));
		IIAmmoUtils.suppress(world, pos.x, pos.y, pos.z, 10f*multiplier, (int)(255*multiplier));
	}

	@Override
	public EnumComponentRole getRole()
	{
		return EnumComponentRole.FLARE;
	}

	@Override
	public int getColour()
	{
		return 0xcab1b1;
	}
}
