package pl.pabilo8.immersiveintelligence.common.ammo;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageFireworks;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @updated 06.03.2024
 * @ii-approved 0.3.1
 * @since 10.07.2021
 */
public class AmmoComponentFirework extends AmmoComponent
{
	public AmmoComponentFirework()
	{
		super("firework", 1f, ComponentRole.FLARE, IIColor.fromPackedRGB(0xcab1b1));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack(new ItemStack(Items.FIREWORK_CHARGE));
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, CoreTypes coreType, NBTTagCompound tag, float componentAmount, float multiplier, Entity owner)
	{
		IIPacketHandler.INSTANCE.sendToAllAround(new MessageFireworks(tag, pos), IIPacketHandler.targetPointFromPos(pos, world, 96));
		IIAmmoUtils.suppress(world, pos.x, pos.y, pos.z, 10f*multiplier, (int)(255*multiplier));
	}
}