package pl.pabilo8.immersiveintelligence.common.item;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.tools.ISkycrateMount;
import pl.pabilo8.immersiveintelligence.client.render.SkyCrateRenderer;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler;
import pl.pabilo8.immersiveintelligence.common.item.ItemIISkycrateMount.SkycrateMounts;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;

/**
 * @author Pabilo8
 * @since 27-12-2019
 */
public class ItemIISkycrateMount extends ItemIISubItemsBase<SkycrateMounts> implements ISkycrateMount
{
	public ItemIISkycrateMount()
	{
		super("skycrate_mount", 1, SkycrateMounts.values());
	}

	public enum SkycrateMounts implements IIItemEnum
	{
		MECHANICAL(IIConfigHandler.IIConfig.Tools.SkycrateMounts.mechEnergy, IIConfigHandler.IIConfig.Tools.SkycrateMounts.mechSpeed, IIConfigHandler.IIConfig.Tools.SkycrateMounts.mechSpeed, false),
		ELECTRIC(0, IIConfigHandler.IIConfig.Tools.SkycrateMounts.electricEnergy, IIConfigHandler.IIConfig.Tools.SkycrateMounts.electricSpeed, true);

		private final float mountEnergy, mountMaxEnergy, electricEnergy;
		private final boolean isTeslaCharged;

		SkycrateMounts(float mountEnergy, float mountMaxEnergy, float electricEnergy, boolean isTeslaCharged)
		{
			this.mountEnergy = mountEnergy;
			this.mountMaxEnergy = mountMaxEnergy;
			this.electricEnergy = electricEnergy;
			this.isTeslaCharged = isTeslaCharged;
		}
	}

	// TODO: 01.09.2022 replace with capabilities
	@Override
	public double getMountEnergy(ItemStack stack)
	{
		return stackToSub(stack).mountEnergy;
	}

	@Override
	public double getMountMaxEnergy(ItemStack stack)
	{
		return stackToSub(stack).mountMaxEnergy;
	}

	@Override
	public double getPoweredSpeed(ItemStack stack)
	{
		return stackToSub(stack).electricEnergy;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void render(ItemStack stack, World world, float partialTicks, double energy)
	{
		switch(stackToSub(stack))
		{
			case MECHANICAL:
				ClientUtils.bindTexture(SkyCrateRenderer.texture_mechanical);
				SkyCrateRenderer.model_mechanical.render();
				break;
			case ELECTRIC:
				ClientUtils.bindTexture(SkyCrateRenderer.texture_electric);
				SkyCrateRenderer.model_electric.render();
				break;
		}
	}

	@Override
	public boolean isTesla(ItemStack stack)
	{
		return stackToSub(stack).isTeslaCharged;
	}
}
