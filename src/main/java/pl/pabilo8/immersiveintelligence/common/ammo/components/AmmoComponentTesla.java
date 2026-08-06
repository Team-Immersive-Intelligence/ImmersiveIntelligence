package pl.pabilo8.immersiveintelligence.common.ammo.components;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDevice0;
import blusunrize.immersiveengineering.common.util.IESounds;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageExplosion;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 06.03.2024
 * @ii-approved 0.3.1
 * @since 10.07.2021
 */
public class AmmoComponentTesla extends AmmoComponent
{
	public AmmoComponentTesla()
	{
		super("tesla", 1f, ComponentRole.EMP, IIColor.fromPackedARGB(0x6b778a));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack(new ItemStack(IEContent.blockMetalDevice0, 1, BlockTypes_MetalDevice0.CAPACITOR_LV.getMeta()));
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float size, float multiplier, Entity owner)
	{
		if(world.isRemote)
			return;

		float radius = multiplier*10;
		BlockPos blockPos = new BlockPos(pos);
		world.playSound(null, blockPos, IISounds.explosionFlare, SoundCategory.NEUTRAL, 1, 0.5f);
		world.playSound(null, blockPos, IESounds.tesla, SoundCategory.NEUTRAL, 1, 0.5f);

		IIPacketHandler.sendToClient(MessageExplosion.createEMPMessage(
				world, pos, radius,
				IIAmmoUtils.applyEMPEffect(world, blockPos, radius, (int)(4000000*multiplier))
		));
	}
}
