package pl.pabilo8.immersiveintelligence.common.blocks;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.Template.BlockInfo;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Pabilo8
 * @since 31.05.2021
 */
public abstract class MultiblockStuctureBase<T extends TileEntityMultiblockPart<T>> implements IMultiblock
{
	static final TemplateManager RES_LOC_TEMPLATE_MANAGER = new TemplateManager("Blue Sunrise is a wanker", DataFixesManager.createFixer());

	//the resLoc for the .nbt file
	private final ResourceLocation loc;
	//the name generated from resLoc
	private final String name;
	//the .nbt file
	private Template template;

	//stacks for manual list
	private IngredientStack[] materials = null;
	//stacks for manual block display
	private ItemStack[][][] structure = null;
	//check array for blockstates
	private IngredientStack[][][] checkStructure = null;
	//offset for trigger block
	protected Vec3i offset = Vec3i.NULL_VECTOR;
	//multiblock dimensions
	private Vec3i size = Vec3i.NULL_VECTOR;
	//scale for manual display
	private float manualScale = 0;

	public MultiblockStuctureBase(ResourceLocation loc)
	{
		this.loc = loc;
		String[] split = loc.getResourcePath().split("/");
		this.name = "II:"+Utils.toCamelCase(split[split.length-1], false);
	}

	public void updateStructure()
	{
		template = RES_LOC_TEMPLATE_MANAGER.getTemplate(null, loc);
		size = template.getSize();

		//sets manual display scale
		this.manualScale = Math.max(Math.max(size.getX(), size.getZ()), size.getX())*(2.4f);

		structure = new ItemStack[size.getY()][size.getZ()][size.getX()];
		for(int x = 0; x < size.getX(); x++)
			for(int y = 0; y < size.getY(); y++)
				for(int z = 0; z < size.getZ(); z++)
					structure[y][z][x] = ItemStack.EMPTY;

		checkStructure = new IngredientStack[size.getY()][size.getZ()][size.getX()];
		List<BlockInfo> blocks = template.blocks;
		Set<IngredientStack> matsSet = new HashSet<>();

		for(BlockInfo info : blocks)
		{
			IngredientStack here = getIngredientStackForBlockInfo(info);
			if(!here.getExampleStack().isEmpty())
			{
				structure[info.pos.getY()][info.pos.getZ()][info.pos.getX()] = here.getExampleStack();
				checkStructure[info.pos.getY()][info.pos.getZ()][info.pos.getX()] = here;
				Optional<IngredientStack> match = matsSet.stream().filter(here::equals).findAny();
				if(match.isPresent())
					match.get().inputSize++;
				else
					matsSet.add(here);
			}
		}
		materials = matsSet.toArray(new IngredientStack[0]);
	}

	@Override
	public String getUniqueName()
	{
		return name;
	}

	@Override
	public boolean isBlockTrigger(IBlockState state)
	{
		return checkState(state, checkStructure[offset.getY()][offset.getZ()][offset.getX()]);
	}

	@Override
	public boolean createStructure(World world, BlockPos startPos, EnumFacing side, EntityPlayer player)
	{
		side = side.getOpposite();
		if(side==EnumFacing.UP||side==EnumFacing.DOWN)
		{
			side = EnumFacing.fromAngle(player.rotationYaw);
		}

		boolean mirrored = false;
		boolean b = structureCheck(world, startPos, side, false);
		if(!b)
		{
			mirrored = true;
			b = structureCheck(world, startPos, side, true);
		}
		if(!b)
			return false;

		ItemStack hammer = player.getHeldItemMainhand().getItem().getToolClasses(player.getHeldItemMainhand()).contains(Lib.TOOL_HAMMER)?player.getHeldItemMainhand(): player.getHeldItemOffhand();
		if(MultiblockHandler.fireMultiblockFormationEventPost(player, this, startPos, hammer).isCanceled())
			return false;

		for(int h = -offset.getY(); h < size.getY()-offset.getY(); h++)
			for(int l = -offset.getZ(); l < size.getZ()-offset.getZ(); l++)
				for(int w = -offset.getX(); w < size.getX()-offset.getX(); w++)
				{
					if(structure[h+offset.getY()][l+offset.getZ()][w+offset.getX()].isEmpty())
						continue;

					int ww = mirrored?-w: w;
					BlockPos pos2 = startPos.offset(side, l).offset(side.rotateY(), ww).add(0, h, 0);

					T tile = placeTile(world, pos2);
					if(tile!=null)
					{
						tile.facing = side;
						tile.mirrored = mirrored;
						tile.formed = true;

						tile.pos = (h+offset.getY())*size.getZ()*size.getX()+
								(l+offset.getZ())*size.getX()+
								w+offset.getX()
						;

						//BlockPos oPos = BlockPos.ORIGIN.offset(side, l).offset(side.rotateY(), ww).add(0, h, 0);

						tile.offset = new int[]{(side==EnumFacing.WEST?-l+1: side==EnumFacing.EAST?l-1: side==EnumFacing.NORTH?ww: -ww), h, (side==EnumFacing.NORTH?-l+1: side==EnumFacing.SOUTH?l-1: side==EnumFacing.EAST?ww: -ww)};
						tile.markDirty();
						addBlockEvent(world, pos2);
					}
				}
		return true;
	}

	protected boolean structureCheck(World world, BlockPos startPos, EnumFacing dir, boolean mirror)
	{
		for(int h = -offset.getY(); h < size.getY()-offset.getY(); h++)
			for(int l = -offset.getZ(); l < size.getZ()-offset.getZ(); l++)
				for(int w = -offset.getX(); w < size.getX()-offset.getX(); w++)
				{
					if(structure[h+offset.getY()][l+offset.getZ()][w+offset.getX()].isEmpty())
						continue;

					int ww = mirror?-w: w;
					BlockPos pos = startPos.offset(dir, l).offset(dir.rotateY(), ww).add(0, h, 0);

					if(!checkState(world.getBlockState(pos), checkStructure[h+offset.getY()][l+offset.getZ()][w+offset.getX()]))
						return false;
				}
		return true;
	}

	protected abstract void addBlockEvent(World world, BlockPos pos);

	@Nullable
	protected abstract T placeTile(World world, BlockPos pos);

	@Override
	public ItemStack[][][] getStructureManual()
	{
		return structure;
	}

	@Override
	public IngredientStack[] getTotalMaterials()
	{
		return materials;
	}

	@Override
	public boolean overwriteBlockRender(ItemStack stack, int iterator)
	{
		return false;
	}

	@Override
	public float getManualScale()
	{
		return manualScale;
	}

	@Override
	public boolean canRenderFormedStructure()
	{
		return true;
	}

	private T te;

	@Override
	public void renderFormedStructure()
	{
		if(te==null)
		{
			te = getMBInstance();
			te.facing = EnumFacing.NORTH;
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(-1, 2, 0);
		ImmersiveIntelligence.proxy.renderTile(te);
		GlStateManager.popMatrix();
	}

	protected abstract T getMBInstance();

	/**
	 * Checks state using IngredientStack
	 *
	 * @param state blockstate
	 * @param stack to be compared to, uses stack's logic (ore/itemstack)
	 * @return whether is equal
	 */
	private boolean checkState(IBlockState state, IngredientStack stack)
	{
		return stack.matchesItemStackIgnoringSize(new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)));
	}

	private IngredientStack getIngredientStackForBlockInfo(BlockInfo info)
	{
		IBlockState state = info.blockState;
		int meta = state.getBlock().getMetaFromState(state);
		ItemStack stack = new ItemStack(state.getBlock(), 1, meta);

		try
		{
			int[] oids = OreDictionary.getOreIDs(stack);
			if(oids.length > 0)
				return new IngredientStack(OreDictionary.getOreName(oids[0]));
		} catch(Exception ignored)
		{

		}

		return new IngredientStack(stack);

		/*
		if(state.getBlock()==IEContent.blockSheetmetal)
			return new IngredientStack(Utils.toCamelCase("block_sheetmetal_"+BlockTypes_MetalsAll.values()[meta].getName().toLowerCase(), true));
		if(state.getBlock()==IEContent.blockSheetmetalSlabs)
			return new IngredientStack(Utils.toCamelCase("slab_sheetmetal_"+BlockTypes_MetalsAll.values()[meta].getName().toLowerCase(), true));
		if(state.getBlock()==IIContent.blockSheetmetal)
			return new IngredientStack(Utils.toCamelCase("block_sheetmetal_"+IIBlockTypes_Metal.values()[meta].getName().toLowerCase(), true));
		if(state.getBlock()==IIContent.blockSheetmetalSlabs)
			return new IngredientStack(Utils.toCamelCase("slab_sheetmetal_"+IIBlockTypes_Metal.values()[meta].getName().toLowerCase(), true));

		if(state.getBlock()==IEContent.blockStorage)
			return new IngredientStack(Utils.toCamelCase("block_"+BlockTypes_MetalsIE.values()[meta].getName().toLowerCase(), true));
		if(state.getBlock()==IEContent.blockStorageSlabs)
			return new IngredientStack(Utils.toCamelCase("slab_"+BlockTypes_MetalsIE.values()[meta].getName().toLowerCase(), true));
		if(state.getBlock()==IIContent.blockMetalStorage)
			return new IngredientStack(Utils.toCamelCase("block_"+BlockTypes_MetalsIE.values()[meta].getName().toLowerCase(), true));
		if(state.getBlock()==IIContent.blockMetalSlabs)
			return new IngredientStack(Utils.toCamelCase("slab_"+BlockTypes_MetalsIE.values()[meta].getName().toLowerCase(), true));

			return new IngredientStack(new ItemStack(state.getBlock(), 1, meta));
		 */
	}
}
