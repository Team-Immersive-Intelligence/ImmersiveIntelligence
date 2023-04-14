package pl.pabilo8.immersiveintelligence.common.ammo;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoUtils;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmoComponent;
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
	public void onEffect(float amount, EnumCoreTypes coreType, NBTTagCompound tag, Vec3d pos, Vec3d dir, World world)
	{
		IIPacketHandler.INSTANCE.sendToAllAround(new MessageFireworks(tag, pos), IIPacketHandler.targetPointFromPos(pos, world, 96));
		AmmoUtils.suppress(world, pos.x, pos.y, pos.z, 10f*amount, (int)(255*amount));
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
