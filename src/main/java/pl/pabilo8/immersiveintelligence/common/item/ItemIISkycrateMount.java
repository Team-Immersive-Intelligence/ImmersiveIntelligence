package pl.pabilo8.immersiveintelligence.common.item;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Tools.SkycrateMounts;
import pl.pabilo8.immersiveintelligence.api.utils.ISkycrateMount;
import pl.pabilo8.immersiveintelligence.client.render.SkyCrateRenderer;

/**
 * @author Pabilo8
 * @since 27-12-2019
 */
public class ItemIISkycrateMount extends ItemIIBase implements ISkycrateMount
{
	public ItemIISkycrateMount()
	{
		super("skycrate_mount", 1, "mechanical", "electric");
	}

	@Override
	public double getMountEnergy(ItemStack stack)
	{
		if(stack.getMetadata()==0)
		{
			return SkycrateMounts.mechEnergy;
		}
		return 0;
	}

	@Override
	public double getMountMaxEnergy(ItemStack stack)
	{
		switch(stack.getMetadata())
		{
			case 0:
				return SkycrateMounts.mechSpeed;
			case 1:
				return SkycrateMounts.electricEnergy;
		}
		return 0;
	}

	@Override
	public double getPoweredSpeed(ItemStack stack)
	{
		switch(stack.getMetadata())
		{
			case 0:
				return SkycrateMounts.mechSpeed;
			case 1:
				return SkycrateMounts.electricSpeed;
		}
		return 0;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void render(ItemStack stack, World world, float partialTicks, double energy)
	{
		switch(stack.getMetadata())
		{
			case 0:
				ClientUtils.bindTexture(SkyCrateRenderer.texture_mechanical);
				SkyCrateRenderer.model_mechanical.render();
				break;
			case 1:
				ClientUtils.bindTexture(SkyCrateRenderer.texture_electric);
				SkyCrateRenderer.model_electric.render();
				break;
		}
	}

	@Override
	public boolean isTesla(ItemStack stack)
	{
		return stack.getMetadata()==1;
	}
}
