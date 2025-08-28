package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.ListUtils;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.utils.tools.ISawblade;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.RotaryMachineRecipe;
import pl.pabilo8.immersiveintelligence.common.util.sound.IISoundAnimation;

import java.util.HashMap;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.06.2025
 * @ii-approved 0.3.1
 * @since 14.04.2020
 */
public class SawmillRecipe extends IIMultiblockRecipe implements RotaryMachineRecipe
{
	private static final IIColor DEFAULT_COLOR = IIColor.fromFloatRGB(0.22392157f, 0.21372549019607842f, 0.15176470588235294f);
	public static HashMap<String, ISawblade> toolMap = new HashMap<>();
	public final IngredientStack itemInput;
	public final ItemStack itemOutput, itemSecondaryOutput;
	private final int torque;
	//The tier of the saw required, 1 for cutting wood (bronze), 2 iron, 3 steel, 4 tungsten
	private final int hardness;
	private final IIColor dustColor;
	IISoundAnimation soundAnimation;

	public SawmillRecipe(ItemStack itemOutput, Object itemInput, ItemStack itemSecondaryOutput, int torque, int time, int hardness, IIColor dustColor)
	{
		super(ApiUtils.createIngredientStack(itemInput));
		this.itemOutput = itemOutput;
		this.itemSecondaryOutput = itemSecondaryOutput;
		this.itemInput = ApiUtils.createIngredientStack(itemInput);
		this.torque = torque;
		this.setTimeAndEnergy(time, 0);
		this.hardness = hardness;

		this.inputList = Lists.newArrayList(this.itemInput);
		this.outputList = ListUtils.fromItems(this.itemOutput, this.itemSecondaryOutput);
		this.dustColor = dustColor;
	}

	@Override
	protected void loadClientSideContent()
	{
		//0 - 0.1 - grabbing sound
		//0.1 - 1 - cutting,
		//sections each through 0.4/3-1.5/3
		//rolling each 1.5/3 - 2.5/3, landing 3/3

		int time = getTotalProcessTime();
		double cuttingTimeStart = time*0.1;
		double cuttingSection = (time*0.9)/itemOutput.getCount();

		this.soundAnimation = new IISoundAnimation(time)
				.withSound(0.05, IISounds.sawmillInserterStart);

		for(int i = 0; i < itemOutput.getCount(); i++)
			this.soundAnimation
					.withSound(cuttingTimeStart+cuttingSection*i, IISounds.sawmillInserterStart)
					.withRepeatedSound(cuttingTimeStart+cuttingSection*(i+0.13),
							cuttingTimeStart+cuttingSection*(i+0.5), IISounds.sawmillLoop)
					.withSound(cuttingTimeStart+cuttingSection*(i+0.76), IISounds.sawmillWoodTumble)
					.withSound(cuttingTimeStart+cuttingSection*(i+0.83), IISounds.sawmillWoodTumble)
					.withSound(cuttingTimeStart+cuttingSection*(i+0.85), IISounds.sawmillInserterEnd)
					.withSound(cuttingTimeStart+cuttingSection*(i+0.9), IISounds.sawmillWoodTumble);
		this.soundAnimation.compile(time);
	}

	public SawmillRecipe(ItemStack itemOutput, Object itemInput, ItemStack itemSecondaryOutput, int torque, int time, int hardness)
	{
		this(itemOutput, itemInput, itemSecondaryOutput, torque, time, hardness, DEFAULT_COLOR);
	}

	public static void registerSawblade(String name, ISawblade blade)
	{
		toolMap.putIfAbsent(name, blade);
	}

	public IISoundAnimation getSoundAnimation()
	{
		return soundAnimation;
	}

	public int getTorque()
	{
		return torque;
	}

	public int getHardness()
	{
		return hardness;
	}

	public IIColor getDustColor()
	{
		return dustColor;
	}
}
