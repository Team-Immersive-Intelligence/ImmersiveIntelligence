package pl.pabilo8.immersiveintelligence.common.util.multiblock;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.ConveyorDirection;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorBelt;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.models.ModelConveyor;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.Template.BlockInfo;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.client.render.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.raytracer.AxisAlignedFacingBB;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * @author Pabilo8
 * @since 31.05.2021
 */
public abstract class MultiblockStuctureBase<T extends TileEntityMultiblockPart<T>> implements IMultiblock
{
	static final TemplateManager RES_LOC_TEMPLATE_MANAGER = new TemplateManager("Blue Sunrise is a wanker", DataFixesManager.createFixer());

	/**
	 * The resLoc for the .nbt file
	 */
	private final ResourceLocation loc;
	/**
	 * The name generated from resLoc
	 */
	private final String name;
	/**
	 * The .nbt file
	 */
	private Template template;
	/**
	 * Stacks for manual list
	 */
	private IngredientStack[] materials = new IngredientStack[0];
	/**
	 * Stacks for manual block display
	 */
	private ItemStack[][][] structure = new ItemStack[0][0][0];
	/**
	 * Check array for blockstates
	 */
	private IngredientStack[][][] checkStructure = new IngredientStack[0][0][0];
	/**
	 * Offset for trigger block
	 */
	protected Vec3i offset = Vec3i.NULL_VECTOR;
	/**
	 * Multiblock dimensions
	 */
	private Vec3i size = Vec3i.NULL_VECTOR;
	/**
	 * Scale for manual display
	 */
	private float manualScale = 0;
	/**
	 * Scheme for tactile objects
	 */
	@Nullable
	private ResLoc tactileScheme = null;

	/**
	 * Bounding boxes for collision and interaction detection at [pos] [facing (ordinal)]
	 */
	private ArrayList<ArrayList<AxisAlignedFacingBB>> AABBs = new ArrayList<>();
	//TODO: 30.07.2024 second map for caching of MultiblockPOIs
	/**
	 * Map of named Points of Interest and their local positions in this multiblock
	 */
	private HashMap<String, int[]> POIs = new HashMap<>();
	/**
	 * Whether this structure requires an infinite bounding box in rendering due to its size
	 */
	private boolean massiveStructure;

	public MultiblockStuctureBase(ResourceLocation loc)
	{
		this.loc = loc;
		String[] split = loc.getResourcePath().split("/");
		this.name = "II:"+IIStringUtil.toCamelCase(split[split.length-1], false);
	}

	public void updateStructure()
	{
		//Load structure template
		template = RES_LOC_TEMPLATE_MANAGER.getTemplate(null, loc);
		size = template.getSize();

		//Set manual display scale, based on structure size
		this.manualScale = 7f/(Math.max(Math.max(size.getX(), size.getZ()), size.getY())/12f);

		//Set structure ItemStacks to default
		structure = new ItemStack[size.getY()][size.getZ()][size.getX()];
		for(int x = 0; x < size.getX(); x++)
			for(int y = 0; y < size.getY(); y++)
				for(int z = 0; z < size.getZ(); z++)
					structure[y][z][x] = ItemStack.EMPTY;

		checkStructure = new IngredientStack[size.getY()][size.getZ()][size.getX()];
		List<BlockInfo> blocks = template.blocks;
		Set<IngredientStack> matsSet = new HashSet<>();

		//Set ItemStacks and BlockStates
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
		//Whether this multiblock has an infinite render bounding box
		massiveStructure = size.getX() > 5||size.getY() > 5||size.getZ() > 5;

		//Update AABB and POIs info from json
		updateMultiblockInfo();
	}

	public void updateMultiblockInfo()
	{
		//Collect from file
		JsonObject file = loadAABBFromJSON();
		if(file==null)
			return;

		//Load parts
		updateAABB(file);
		updatePOI(file);
		updateTactiles(file);
	}

	/**
	 * @param file
	 */
	private void updateAABB(@Nonnull JsonObject file)
	{
		//Set AABB
		AABBs.clear();
		if(!file.has("bounds"))
			return;

		//Collect AABB dictionary
		Map<String, AxisAlignedFacingBB> allBounds = file.get("bounds").getAsJsonObject()
				.entrySet().stream()
				.filter(e -> e.getValue() instanceof JsonArray)
				.map(e -> new Tuple<>(e.getKey(), new AxisAlignedFacingBB(e.getValue().getAsJsonArray())))
				.collect(Collectors.toMap(Tuple::getFirst, Tuple::getSecond));

		//Map JSON positions
		JsonObject positionsJSON = file.get("positions").getAsJsonObject();
		HashMap<Integer, ArrayList<AxisAlignedFacingBB>> mapped = new HashMap<>();

		for(Entry<String, JsonElement> entry : positionsJSON.entrySet())
		{
			//Get position IDs
			Integer[] IDs = Arrays.stream(entry.getKey().split(","))
					.map(Integer::parseInt)
					.toArray(Integer[]::new);

			//Gather all bounding boxes
			ArrayList<AxisAlignedFacingBB> group = new ArrayList<>();

			//Array - can contain multiple AABBs
			if(entry.getValue() instanceof JsonArray)
			{
				JsonArray array = entry.getValue().getAsJsonArray();
				for(JsonElement element : array)
				{
					AxisAlignedFacingBB aabb = allBounds.get(element.getAsString());
					if(aabb!=null)
						group.add(aabb);
				}
			}
			//String - only a single AABB
			else if(entry.getValue() instanceof JsonPrimitive)
			{
				//Get assigned aabb name
				AxisAlignedFacingBB aabb = allBounds.get(entry.getValue().getAsString());
				if(aabb!=null)
					group.add(aabb);
			}

			//Add elements to map
			for(Integer id : IDs)
				mapped.compute(id, (k, v) -> {
					//Create new list with elements, or add them to an existing list
					if(v==null)
						return new ArrayList<>(group);
					v.addAll(group);
					return v;
				});
		}

		//Finally, turn them into a list!
		int tiles = size.getX()*size.getY()*size.getZ();
		for(int i = 0; i < tiles; i++)
			AABBs.add(mapped.getOrDefault(i, new ArrayList<>()));
	}

	private void updatePOI(@Nonnull JsonObject file)
	{
		//Load POIs
		POIs.clear();
		if(!file.has("poi"))
			return;

		JsonObject poiJSON = file.get("poi").getAsJsonObject();
		for(Entry<String, JsonElement> poi : poiJSON.entrySet())
		{
			//Multiple Values
			if(poi.getValue() instanceof JsonArray)
			{
				JsonArray arr = poi.getValue().getAsJsonArray();
				int[] posArray = new int[arr.size()];
				for(int i = 0; i < arr.size(); i++)
					posArray[i] = arr.get(i).getAsInt();

				POIs.put(poi.getKey(), posArray);
			}
			//Single Value
			else if(poi.getValue() instanceof JsonPrimitive)
				POIs.put(poi.getKey(), new int[]{poi.getValue().getAsInt()});
		}

		//Sorting needed for binary search to work
		POIs.values().forEach(Arrays::sort);
	}

	/**
	 * Does not actually load tactiles info. It's handled by {@link pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileHandler}
	 *
	 * @param file json file
	 */
	private void updateTactiles(@Nonnull JsonObject file)
	{

	}

	private JsonObject loadAABBFromJSON()
	{
		ResLoc res = getAABBFileLocation();
		try
		{
			InputStream stream = MinecraftServer.class.getResourceAsStream("/assets/"+res.getResourceDomain()+"/"+res.getResourcePath());
			assert stream!=null;
			JsonElement object = new JsonStreamParser(new InputStreamReader(stream)).next();
			return object.getAsJsonObject();
		} catch(Exception ignored) {} //
		return null;
	}

	@Override
	public String getUniqueName()
	{
		return name;
	}

	@Override
	public boolean isBlockTrigger(IBlockState state)
	{
		return checkState(state, checkStructure[offset.getY()][offset.getZ()][offset.getX()], null, null);
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
						tile.offset = useNewOffset()?
								new int[]{(side==EnumFacing.WEST?-l+1: side==EnumFacing.EAST?l-1: side==EnumFacing.NORTH?ww: -ww), h, (side==EnumFacing.NORTH?-l+1: side==EnumFacing.SOUTH?l-1: side==EnumFacing.EAST?ww: -ww)}:
								new int[]{(side==EnumFacing.WEST?-l: side==EnumFacing.EAST?l: side==EnumFacing.NORTH?ww: -ww), h, (side==EnumFacing.NORTH?-l: side==EnumFacing.SOUTH?l: side==EnumFacing.EAST?ww: -ww)};
						tile.markDirty();
						addBlockEvent(world, pos2);
					}
				}
		return true;
	}

	/**
	 * Whether the master block should have 1 block offset
	 */
	protected boolean useNewOffset()
	{
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

					if(!checkState(world.getBlockState(pos), checkStructure[h+offset.getY()][l+offset.getZ()][w+offset.getX()], world, pos))
						return false;
				}
		return true;
	}

	protected abstract BlockIIMultiblock<?> getBlock();

	protected abstract int getMeta();

	protected void addBlockEvent(World world, BlockPos pos)
	{
		world.addBlockEvent(pos, getBlock(), 255, 0);
	}

	@Nullable
	protected T placeTile(World world, BlockPos pos)
	{
		world.setBlockState(pos, getBlock().getStateFromMeta(getMeta()));
		return (T)world.getTileEntity(pos);
	}

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
		if(stack.getItem() instanceof ItemBlockIEBase&&((ItemBlockIEBase)stack.getItem()).getBlock()==IEContent.blockConveyor)
		{
			renderConveyor(stack);
			return true;
		}
		return false;
	}

	/**
	 * *Actually* renders a conveyor
	 *
	 * @param stack conveyor ItemStack
	 */
	@SideOnly(Side.CLIENT)
	public void renderConveyor(ItemStack stack)
	{
		GlStateManager.pushMatrix();

		Tuple<ResourceLocation, EnumFacing> entry = getConveyorKey(stack, EnumFacing.WEST);
		EnumFacing facing = entry.getSecond();
		IConveyorBelt conv = ConveyorHandler.functionRegistry.get(entry.getFirst()).apply(null);

		List<BakedQuad> quads = ModelConveyor.getBaseConveyor(facing, 1, new Matrix4(facing), ConveyorDirection.HORIZONTAL,
				ClientUtils.getSprite(conv.getActiveTexture()), new boolean[]{true, true}, new boolean[]{true, true}, null, 0);

		quads = conv.modifyQuads(quads, null, facing);
		ClientUtils.renderQuads(quads, 1, 1, 1, 1);

		GlStateManager.popMatrix();
	}

	@Override
	public float getManualScale()
	{
		return 6*(1f/(Math.max(Math.max(size.getX(), size.getZ()), size.getY())/10f));
	}

	@Override
	public boolean canRenderFormedStructure()
	{
		return true;
	}

	private T te;
	@SideOnly(Side.CLIENT)
	private TileEntitySpecialRenderer<T> tesr;

	@Override
	public void renderFormedStructure()
	{
		if(te==null)
		{
			te = getMBInstance();
			te.facing = EnumFacing.NORTH;
			tesr = TileEntityRendererDispatcher.instance.getRenderer(te);
			if(tesr instanceof IIMultiblockRenderer)
				((IIMultiblockRenderer)tesr).setFastMultiblockState(this, getBlock().getStateFromMeta(getMeta()));
		}

		if(tesr==null)
			return;

		GlStateManager.pushMatrix();
		tesr.render(te,
				Math.floor(size.getX()/2f)-offset.getX()+1,
				offset.getY(),
				Math.floor(size.getZ()/2f)-offset.getZ()-(size.getZ()%2==1?0: 1),
				0, 0, 0);
		GlStateManager.popMatrix();
	}

	protected abstract T getMBInstance();

	@Override
	public IBlockState getBlockstateFromStack(int index, ItemStack stack)
	{
		/*int blocksPerLevel = size.getY()*size.getZ();
		// dist = target position - current position
		int distH = (index/blocksPerLevel);
		int distL = (index%blocksPerLevel/size.getZ());
		int distW = (index%size.getZ());*/

		if(!stack.isEmpty()&&stack.getItem() instanceof ItemBlock)
		{
			Block block = ((ItemBlock)stack.getItem()).getBlock();
			return block.getStateFromMeta(stack.getItemDamage());
		}

		return null;
	}

	/**
	 * Checks state using IngredientStack
	 *
	 * @param state blockstate
	 * @param stack to be compared to, uses stack's logic (ore/itemstack)
	 * @param world
	 * @param pos
	 * @return whether is equal
	 */
	private boolean checkState(IBlockState state, IngredientStack stack, @Nullable World world, @Nullable BlockPos pos)
	{
		//air blocks
		if(stack==null)
			return true;

		if(stack.stack.getItem() instanceof ItemBlockIEBase&&((ItemBlockIEBase)stack.stack.getItem()).getBlock()==IEContent.blockConveyor)
		{
			if(world!=null)
				return ConveyorHandler.isConveyor(world, pos, ItemNBTHelper.getString(stack.stack, "conveyorType"), null);
			else
				return state.getBlock()==IEContent.blockConveyor;
		}

		return stack.matchesItemStackIgnoringSize(new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)));
	}

	private IngredientStack getIngredientStackForBlockInfo(BlockInfo info)
	{
		IBlockState state = info.blockState;

		if(state.getBlock()==IEContent.blockConveyor)
		{
			ItemStack conveyorStack = ConveyorHandler.getConveyorStack(info.tileentityData.getString("conveyorBeltSubtype"));
			ItemNBTHelper.setInt(conveyorStack, "conveyorFacing", info.tileentityData.getInteger("facing"));
			return new IngredientStack(conveyorStack).setUseNBT(true);
		}

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
	}

	/**
	 * @return size in int array, values are swapped to HLW
	 */
	public int[] getSize()
	{
		return new int[]{size.getY(), size.getZ(), size.getX()};
	}

	/**
	 * @return the multiblock XYZ offset vector
	 */
	public Vec3i getOffset()
	{
		return offset;
	}

	/**
	 * @param h height (y)
	 * @param l length (z)
	 * @param w width (x)
	 * @return resource location in string format if tile is a conveyor or empty string
	 */
	public Tuple<ResourceLocation, EnumFacing> getConveyorKey(int h, int l, int w, EnumFacing facing)
	{
		IngredientStack is = checkStructure[h][l][w];
		return getConveyorKey(is.stack, facing);
	}

	public Tuple<ResourceLocation, EnumFacing> getConveyorKey(ItemStack stack, EnumFacing facing)
	{
		ResourceLocation rl = new ResourceLocation(ItemNBTHelper.getString(stack, "conveyorType"));
		EnumFacing sf = EnumFacing.getFront(ItemNBTHelper.getInt(stack, "conveyorFacing"));
		EnumFacing ff = EnumFacing.getHorizontal(sf.getHorizontalIndex()+facing.getHorizontalIndex());//
		return new Tuple<>(rl, ff);
	}

	//--- AABB ---//

	public ResLoc getAABBFileLocation()
	{
		return ResLoc.of(IIReference.RES_AABB, loc.getResourcePath().replace("multiblocks", "multiblock"))
				.withExtension(ResLoc.EXT_JSON);
	}

	/**
	 * @param pos      position of the block in a multiblock
	 * @param blockPos position of the block in the world
	 * @param facing   facing of the block
	 * @param mirrored whether the multiblock is mirrored
	 * @return a list of AABBs for the block
	 */
	public List<AxisAlignedBB> getAABB(int pos, BlockPos blockPos, EnumFacing facing, boolean mirrored)
	{
		return AABBs.get(pos).stream()
				.map(aafbb -> aafbb.getFacing(facing, mirrored))
				.map(aafbb -> aafbb.offset(blockPos))
				.collect(Collectors.toList());
	}

	//--- POIs ---//

	/**
	 * @param pos  position of the block in multiblock
	 * @param name of the POI
	 * @return whether this position is a POI of that type
	 */
	public boolean isPointOfInterest(int pos, String name)
	{
		return Arrays.binarySearch(POIs.get(name), pos) >= 0;
	}

	public int getPointOfInterest(String name)
	{
		int[] arr = POIs.get(name);
		return arr.length==0?0: arr[0];
	}

	public int[] getPointsOfInterest(String name)
	{
		return POIs.getOrDefault(name, new int[0]);
	}

	public boolean isMassiveStructure()
	{
		return massiveStructure;
	}
}
