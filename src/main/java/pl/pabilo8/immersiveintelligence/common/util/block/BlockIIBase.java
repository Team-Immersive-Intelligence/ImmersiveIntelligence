package pl.pabilo8.immersiveintelligence.common.util.block;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

/**
 * Created by Pabilo8 on 2019-05-07 with help of Alternating Flux by AntiBlueQuirk.<br>
 * Reworked on 04.09.2022
 */
public class BlockIIBase<E extends Enum<E> & IIBlockEnum> extends Block implements IIIStateMappings<E>
{
	//--- BlockIIBase ---//

	/**
	 * The name, used to create the ID of this block
	 */
	public final String name;

	/**
	 * Properties passed to Block in construction
	 */
	protected static IProperty<?>[] tempProperties;
	protected static IUnlistedProperty<?>[] tempUnlistedProperties;

	/**
	 * Enum property, main property defining SubBlocks
	 */
	public final PropertyEnum<E> property;
	/**
	 * Properties actually used
	 */
	public final IProperty<?>[] additionalProperties;
	public final IUnlistedProperty<?>[] additionalUnlistedProperties;
	/**
	 * Enum, marking SubBlocks of this block
	 */
	public final E[] enumValues;

	/**
	 * Hiding block's ItemBlock by meta<br>
	 * Whether SubBlocks are not full cubes
	 */
	protected final boolean[] hidden, fullCubes;
	/**
	 * Language key for tooltip description in ItemBlock
	 */
	protected final IICategory[] category;
	protected final int[] stackAmounts, opaqueness;
	protected final float[] hardness, blastResistance;
	protected final String[] description;
	protected final Material[] materials;
	protected final SoundType[] soundTypes;
	protected final HashMap<E, Set<String>> toolTypes;
	protected final HashMap<E, Set<BlockRenderLayer>> renderLayers;
	protected EnumPushReaction[] mobilityFlags;

	/**
	 * ItemBlock of this block
	 */
	@Nullable
	public final ItemBlockIIBase itemBlock;

	/**
	 * List of SubBlocks using {@link net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer} for legacy TMT based models.
	 */
	@Deprecated
	public List<E> tesrList = new ArrayList<>();

	public BlockIIBase(String name, PropertyEnum<E> mainProperty, Material material, Function<BlockIIBase<E>, ItemBlockIIBase> itemBlock, Object... additionalProperties)
	{
		super(setTempProperties(material, mainProperty, additionalProperties));

		//set values
		this.name = name;
		this.property = mainProperty;
		this.enumValues = mainProperty.getValueClass().getEnumConstants();

		//prepare properties
		ArrayList<IProperty<?>> propList = new ArrayList<>();
		ArrayList<IUnlistedProperty<?>> unlistedPropList = new ArrayList<>();
		for(Object o : additionalProperties)
		{
			if(o instanceof IProperty) propList.add((IProperty<?>)o);
			if(o instanceof IProperty[]) Collections.addAll(propList, ((IProperty<?>[])o));
			if(o instanceof IUnlistedProperty) unlistedPropList.add((IUnlistedProperty<?>)o);
			if(o instanceof IUnlistedProperty[]) Collections.addAll(unlistedPropList, ((IUnlistedProperty<?>[])o));
		}

		this.additionalProperties = propList.toArray(new IProperty[0]);
		this.additionalUnlistedProperties = unlistedPropList.toArray(new IUnlistedProperty[0]);

		//prepare for registration
		this.setDefaultState(getInitDefaultState());
		this.setUnlocalizedName(ImmersiveIntelligence.MODID+"."+name);
		this.setCreativeTab(IIContent.II_CREATIVE_TAB);

		//SubBlock data arrays
		int sub = this.enumValues.length;

		//defaultize
		this.hidden = new boolean[sub];
		Arrays.fill(this.hidden, false);
		this.fullCubes = new boolean[sub];
		Arrays.fill(this.fullCubes, false);

		this.stackAmounts = new int[sub];
		Arrays.fill(this.stackAmounts, 64);
		this.opaqueness = new int[sub];
		Arrays.fill(this.opaqueness, material.isOpaque()?0: 255);

		this.blastResistance = new float[sub];
		Arrays.fill(this.blastResistance, blockResistance = 2);
		this.hardness = new float[sub];
		Arrays.fill(this.hardness, blockHardness = 1);

		this.description = new String[sub];
		Arrays.fill(this.description, "");

		this.category = new IICategory[sub];
		Arrays.fill(this.category, IICategory.NULL);

		this.materials = new Material[sub];
		Arrays.fill(this.materials, material);
		this.soundTypes = new SoundType[sub];
		Arrays.fill(this.soundTypes, this.blockSoundType = adjustSound(material));

		this.toolTypes = new HashMap<>();
		this.renderLayers = new HashMap<>(sub);
		this.mobilityFlags = new EnumPushReaction[sub];
		Arrays.fill(this.mobilityFlags, EnumPushReaction.NORMAL);

		IIContent.BLOCKS.add(this);
		IIContent.ITEMS.add(this.itemBlock = itemBlock.apply(this));

		lightOpacity = 255;
	}

	public void parseSubBlocks()
	{
		for(int i = 0; i < enumValues.length; i++)
		{
			IIBlockProperties properties = enumValues[i].getProperties();
			if(properties==null) continue;

			if(properties.hidden().isSet()) hidden[i] = properties.hidden().isTrue();
			if(properties.fullCube().isSet()) fullCubes[i] = properties.fullCube().isTrue();

			if(properties.stackSize()!=-1) stackAmounts[i] = properties.stackSize();
			if(properties.opacity()!=-1) opaqueness[i] = properties.opacity();
			if(properties.blastResistance()!=-1) blastResistance[i] = properties.blastResistance();

			if(properties.hardness()!=-1) hardness[i] = properties.hardness();

			if(!properties.descKey().isEmpty()) description[i] = properties.descKey();
			if(properties.category()!=IICategory.NULL) category[i] = properties.category();
		}
	}

	private SoundType adjustSound(Material material)
	{
		if(material==Material.ANVIL) return SoundType.ANVIL;
		else if(material==Material.CARPET||material==Material.CLOTH) return SoundType.CLOTH;
		else if(material==Material.GLASS||material==Material.ICE) return SoundType.GLASS;
		else if(material==Material.GRASS||material==Material.LEAVES||material==Material.TNT||material==Material.PLANTS||material==Material.VINE)
			return SoundType.PLANT;
		else if(material==Material.GROUND) return SoundType.GROUND;
		else if(material==Material.IRON) return SoundType.METAL;
		else if(material==Material.SAND) return SoundType.SAND;
		else if(material==Material.SNOW) return SoundType.SNOW;
		else if(material==Material.ROCK) return SoundType.STONE;
		else if(material==Material.WOOD||material==Material.CACTUS) return SoundType.WOOD;

		//default
		return SoundType.STONE;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	@Nonnull
	public SoundType getSoundType(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nullable Entity entity)
	{
		int meta = state.getValue(property).getMeta();
		return soundTypes[meta%enumValues.length];
	}

	private static Material setTempProperties(Material material, PropertyEnum<?> property, Object... additionalProperties)
	{
		ArrayList<IProperty<?>> propList = new ArrayList<>();
		ArrayList<IUnlistedProperty<?>> unlistedPropList = new ArrayList<>();
		propList.add(property);
		for(Object o : additionalProperties)
		{
			if(o instanceof IProperty) propList.add((IProperty<?>)o);
			if(o instanceof IProperty[]) Collections.addAll(propList, ((IProperty<?>[])o));
			if(o instanceof IUnlistedProperty) unlistedPropList.add((IUnlistedProperty<?>)o);
			if(o instanceof IUnlistedProperty[]) Collections.addAll(unlistedPropList, ((IUnlistedProperty<?>[])o));
		}
		tempProperties = propList.toArray(new IProperty[0]);
		tempUnlistedProperties = unlistedPropList.toArray(new IUnlistedProperty[0]);
		return material;
	}

	//--- TMT TESR Registration ---//

	public void addToTESRMap(E... id)
	{
		Collections.addAll(tesrList, id);
	}

	//--- IAdvancedStateMappings ---//

	@Override
	public String getMappingsName()
	{
		return this.name;
	}

	@Nullable
	@Override
	public E[] getMappingsEnum()
	{
		return enumValues;
	}

	@Nullable
	@Override
	public List<E> getLegacyTESR()
	{
		return tesrList;
	}

	/**
	 * Override, if you're using non-standard meta values
	 */
	@Override
	@Nullable
	public String getMappingsExtension(int meta, boolean itemBlock)
	{
		if(meta > enumValues.length-1)
			return null;

		IIBlockProperties properties = enumValues[meta%enumValues.length].getProperties();
		if(properties!=null&&properties.needsCustomState()) return enumValues[meta%enumValues.length].getName();
		return null;
	}

	//--- Utilities ---//

	public String getTranslationKey(ItemStack stack)
	{
		return super.getUnlocalizedName()+"."+enumValues[stack.getMetadata()%enumValues.length].getName();
	}

	protected static Object[] combineProperties(Object[] currentProperties, Object... addedProperties)
	{
		Object[] array = new Object[currentProperties.length+addedProperties.length];
		System.arraycopy(currentProperties, 0, array, 0, currentProperties.length);
		System.arraycopy(addedProperties, 0, array, currentProperties.length, addedProperties.length);
		return array;
	}

	//--- Block ---//

	protected BlockStateContainer createNotTempBlockState()
	{
		IProperty<?>[] array = new IProperty[1+this.additionalProperties.length];
		array[0] = this.property;
		System.arraycopy(this.additionalProperties, 0, array, 1, this.additionalProperties.length);
		if(this.additionalUnlistedProperties.length > 0)
			return new ExtendedBlockState(this, array, additionalUnlistedProperties);
		return new BlockStateContainer(this, array);
	}

	protected IBlockState getInitDefaultState()
	{
		IBlockState state = this.blockState.getBaseState().withProperty(this.property, enumValues[0]);
		for(IProperty<?> additionalProperty : this.additionalProperties)
			if(additionalProperty!=null&&!additionalProperty.getAllowedValues().isEmpty())
				state = applyProperty(state, additionalProperty, additionalProperty.getAllowedValues().iterator().next());
		return state;
	}

	protected <V extends Comparable<V>> IBlockState applyProperty(IBlockState in, IProperty<V> prop, Object val)
	{
		return in.withProperty(prop, (V)val);
	}

	public void onIEBlockPlacedBy(World world, BlockPos pos, IBlockState state, EnumFacing side, float hitX, float hitY, float hitZ, EntityLivingBase placer, ItemStack stack)
	{

	}

	public boolean canIEBlockBePlaced(World world, BlockPos pos, IBlockState newState, EnumFacing side, float hitX, float hitY, float hitZ, EntityPlayer player, ItemStack stack)
	{
		return true;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		if(this.property!=null) return createNotTempBlockState();
		if(tempUnlistedProperties.length > 0)
			return new ExtendedBlockState(this, tempProperties, tempUnlistedProperties);

		return new BlockStateContainer(this, tempProperties);
	}

	/**
	 * Called by ItemBlocks after a block is set in the world, to allow post-place logic
	 */
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	/**
	 * Called on server when World#addBlockEvent is called. If server returns true, then also called on the client. On the
	 * Server, this may perform additional changes to the world, like pistons replacing the block with an extended base. On
	 * the client, the update may involve replacing tile entities or effects such as sounds or particles
	 */
	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int eventID, int eventParam)
	{
		if(worldIn.isRemote&&eventID==255)
		{
			worldIn.notifyBlockUpdate(pos, state, state, 3);
			return true;
		}
		return super.eventReceived(state, worldIn, pos, eventID, eventParam);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(@Nullable IBlockState state)
	{
		if(state==null||enumValues==null||!this.equals(state.getBlock())) return 0;
		return state.getValue(this.property).getMeta();
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@SuppressWarnings("deprecation")
	@Override
	@Nonnull
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(this.property, enumValues[meta%enumValues.length]);
	}

	/**
	 * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It returns
	 * the metadata of the dropped item based on the old metadata of the block.
	 */
	@Override
	public int damageDropped(IBlockState state)
	{
		return getMetaFromState(state);
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for(E type : this.enumValues)
			if(!this.hidden[type.getMeta()])
				list.add(new ItemStack(this, 1, type.getMeta()));
	}

	//--- SubBlocks ---//

	public BlockIIBase<E> setSubMaterial(E subBlock, Material material)
	{
		this.materials[subBlock.getMeta()] = material;
		this.soundTypes[subBlock.getMeta()] = adjustSound(material);
		return this;
	}

	public BlockIIBase<E> setBlockLayer(BlockRenderLayer... layer)
	{
		for(E subBlock : enumValues)
			this.renderLayers.put(subBlock, new HashSet<>(Arrays.asList(layer)));
		return this;
	}

	public BlockIIBase<E> setSubBlockLayer(E subBlock, BlockRenderLayer... layer)
	{
		this.renderLayers.put(subBlock, new HashSet<>(Arrays.asList(layer)));
		return this;
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
	{
		E subBlock = state.getValue(property);
		if(renderLayers.containsKey(subBlock))
			return renderLayers.get(subBlock).contains(layer);
		return layer==BlockRenderLayer.SOLID;
	}

	@Override
	public BlockIIBase<E> setLightOpacity(int opacity)
	{
		this.lightOpacity = opacity;
		Arrays.fill(opaqueness, this.lightOpacity);
		return this;
	}

	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		if(enumValues==null||!this.equals(state.getBlock()))
			return 0;

		int meta = state.getValue(property).getMeta();
		return opaqueness[meta%enumValues.length];
	}

	@Override
	public Block setHardness(float hardness)
	{
		this.blockHardness = hardness;
		Arrays.fill(this.hardness, this.blockHardness);
		return this;
	}

	@Override
	public float getBlockHardness(IBlockState state, World world, BlockPos pos)
	{
		if(enumValues==null||!this.equals(state.getBlock()))
			return 0;

		int meta = state.getValue(property).getMeta();
		return hardness[meta%enumValues.length];
	}

	@Override
	public Block setResistance(float resistance)
	{
		this.blockResistance = resistance;
		Arrays.fill(this.blastResistance, this.blockResistance);
		return this;
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		int meta = getMetaFromState(world.getBlockState(pos));
		return blastResistance[meta%enumValues.length];
	}

	public Block setCategory(IICategory category)
	{
		for(E subBlock : enumValues)
			this.category[subBlock.getMeta()] = category;
		return this;
	}

	public boolean isHidden(int meta)
	{
		return hidden[meta%enumValues.length];
	}

	public IICategory getCategory(int meta)
	{
		return category[meta%enumValues.length];
	}

	//--- Mobility Flags (Piston Pushing) ---//

	public BlockIIBase<E> setMetaMobilityFlag(E subType, EnumPushReaction flag)
	{
		mobilityFlags[subType.ordinal()] = flag;
		return this;
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state)
	{
		int meta = state.getValue(property).getMeta();
		if(mobilityFlags[meta%enumValues.length]==null) return EnumPushReaction.NORMAL;
		return mobilityFlags[meta%enumValues.length];
	}

	//--- Opaqueness ---//

	/**
	 * Used for setting opaqueness for the entire block
	 */
	public final BlockIIBase<E> setFullCube(boolean fullCube)
	{
		this.fullBlock = fullCube;
		Arrays.fill(fullCubes, fullCube);
		return this;
	}

	protected boolean normalBlockCheck(@Nonnull IBlockState state)
	{
		if(enumValues==null||!this.equals(state.getBlock())) return true;
		int meta = state.getValue(property).getMeta();
		return fullCubes[meta%enumValues.length];
	}


	@Override
	@SuppressWarnings("deprecation")
	public boolean isBlockNormalCube(@Nonnull IBlockState state)
	{
		return normalBlockCheck(state);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isNormalCube(@Nonnull IBlockState state)
	{
		return normalBlockCheck(state);
	}

	/**
	 * @return true if the state occupies all of its 1x1x1 cube
	 */
	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullBlock(@Nonnull IBlockState state)
	{
		return normalBlockCheck(state);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube(@Nonnull IBlockState state)
	{
		return normalBlockCheck(state);
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(@Nonnull IBlockState state)
	{
		if(enumValues==null||!this.equals(state.getBlock()))
			return true;
		return normalBlockCheck(state);
	}

	@Override
	public boolean isNormalCube(@Nonnull IBlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos)
	{
		return normalBlockCheck(state);
	}

	//--- Tool Types ---//

	public void setToolTypes(String... toolTypes)
	{
		for(E subBlock : enumValues)
			this.toolTypes.put(subBlock, new HashSet<>(Arrays.asList(toolTypes)));
	}

	/**
	 * Used to add special tool types, such as hammer, wirecutters, wrench, etc.
	 */
	@Override
	public boolean isToolEffective(@Nonnull String type, @Nonnull IBlockState state)
	{
		E subBlock = state.getValue(this.property);
		if(toolTypes.containsKey(subBlock))
			return toolTypes.get(subBlock).contains(type);

		return super.isToolEffective(type, state);
	}

	//--- Crafting Utilities ---//

	public ItemStack getStack(E subBlock)
	{
		return getStack(subBlock, 1);
	}

	public ItemStack getStack(E subBlock, int amount)
	{
		return new ItemStack(this, amount, subBlock.ordinal());
	}

	public IngredientStack getIngredientStack(E subBlock, int amount)
	{
		return new IngredientStack(getStack(subBlock, amount));
	}
}