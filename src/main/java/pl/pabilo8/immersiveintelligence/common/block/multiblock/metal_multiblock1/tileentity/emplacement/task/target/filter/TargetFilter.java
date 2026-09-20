package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetEvaluationContext;

import javax.annotation.Nonnull;

/**
 * Tests one entity condition in an Emplacement target decision tree.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.08.2026
 */
public interface TargetFilter extends INBTSerializable<NBTTagCompound>
{
	/**
	 * @return true if the entity satisfies this condition
	 */
	boolean matches(@Nonnull TargetEvaluationContext context);

	/**
	 * @return the filter category
	 */
	@Nonnull
	TargetFilterType getType();

	/**
	 * @return a short value summary for the tree editor
	 */
	@Nonnull
	String getSummary();
}
