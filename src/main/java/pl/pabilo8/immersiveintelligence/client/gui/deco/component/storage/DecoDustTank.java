package pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage;

import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.api.DustTank;
import pl.pabilo8.immersiveintelligence.api.crafting.DustStack;
import pl.pabilo8.immersiveintelligence.api.crafting.DustUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 12.01.2025
 **/
public class DecoDustTank extends DecoTankBase<DecoDustTank, DustStack>
{
	private DustTank tank = null;
	private int capacity = 1;

	public DecoDustTank(int x, int y)
	{
		super(x, y);
	}

	public DecoDustTank withDustTank(DustTank tank, int capacity)
	{
		this.tank = tank;
		this.capacity = capacity;
		return this;
	}

	@Override
	protected int getResourceCapacity()
	{
		return capacity;
	}

	@Nullable
	@Override
	protected List<DustStack> getContents()
	{
		return tank==null?null: Collections.singletonList(tank.getDustStack());
	}

	@Override
	public int getResourceAmount(@Nonnull DustStack dustStack)
	{
		return dustStack.amount;
	}

	@Nonnull
	@Override
	protected IIColor getResourceColor(DustStack dustStack)
	{
		return DustUtils.getColor(dustStack);
	}

	@Override
	public ResourceLocation getResourceTexture(@Nonnull DustStack dustStack)
	{
		return IIReference.RES_TEXTURES_DECO_COMPONENT_TANK_DUST;
	}

	@Override
	protected void addResourceTooltip(List<String> tooltip, DustStack dustStack, int tankCapacity)
	{
		tooltip.add(DustUtils.getDustName(dustStack));
		tooltip.add(dustStack.amount+" mB");
	}
}
