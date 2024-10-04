package pl.pabilo8.immersiveintelligence.client.util;

import blusunrize.immersiveengineering.client.IECustomStateMapper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.block.IIIStateMappings;

/**
 * Custom {@link IBlockState} location mapper that allows to use meta-IDs
 *
 * @param <E>
 */
public class IICustomStateMapper<E extends Enum<E>> extends IECustomStateMapper
{
	public static <E extends Enum<E>> StateMapperBase getStateMapper(IIIStateMappings<E> mappings)
	{
		return stateMappers.computeIfAbsent("ii_"+mappings.getMappingsName(), s -> new IICustomStateMapper<E>());
	}

	@Override
	@SuppressWarnings("unchecked")
	protected ModelResourceLocation getModelResourceLocation(IBlockState state)
	{
		try
		{
			assert state.getBlock() instanceof IIIStateMappings;
			IIIStateMappings<E> mappings = (IIIStateMappings<E>)state.getBlock();
			ResourceLocation rl = new ResourceLocation(Block.REGISTRY.getNameForObject(state.getBlock()).getResourceDomain(),
					mappings.getMappingsName());

			String custom = mappings.getMappingsExtension(state.getBlock().getMetaFromState(state), false);
			if(custom!=null)
				rl = new ResourceLocation(rl+"/"+custom);
			return new ModelResourceLocation(rl, this.getPropertyString(state.getProperties()));
		} catch(Exception e)
		{
			IILogger.error("Failed to get ModelResourceLocation for state %s, cause: %s", state, e);
			ResourceLocation rl = Block.REGISTRY.getNameForObject(state.getBlock());
			return new ModelResourceLocation(rl, this.getPropertyString(state.getProperties()));
		}
	}
}