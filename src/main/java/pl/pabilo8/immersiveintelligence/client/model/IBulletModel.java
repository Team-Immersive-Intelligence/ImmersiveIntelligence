package pl.pabilo8.immersiveintelligence.client.model;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.IBullet;

/**
 * @author Pabilo8
 * @since 04-10-2019
 */
public interface IBulletModel
{
	default void renderBulletUnused(ItemStack stack)
	{
		IBullet b = (IBullet)stack.getItem();
		renderBulletUnused(b.getCore(stack).getColour(), b.getCoreType(stack), b.getPaintColor(stack));
	}

	default void renderBulletUnused(int coreColour, EnumCoreTypes coreType, int paintColour)
	{
		renderCasing(1f, paintColour);
		renderCore(coreColour, coreType);
	}


	default void renderBulletUsed(int coreColour, EnumCoreTypes coreType, int paintColour)
	{
		//default for bullets rendering, override it when rendering grenades (so the stick/casing would be rendered too)
		renderCore(coreColour, coreType);
	}

	void renderCasing(float gunpowderPercentage, int paintColour);

	void renderCore(int coreColour, EnumCoreTypes coreType);
}
