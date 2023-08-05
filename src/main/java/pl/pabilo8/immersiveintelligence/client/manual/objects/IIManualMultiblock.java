package pl.pabilo8.immersiveintelligence.client.manual.objects;

import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.lib.manual.ManualUtils;
import blusunrize.lib.manual.gui.GuiButtonManualNavigation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualObject;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPage;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Basically a copy of {@link blusunrize.immersiveengineering.api.ManualPageMultiblock} adapted for CTMB
 *
 * @author Pabilo8
 * @since 22.05.2022
 */
public class IIManualMultiblock extends IIManualObject
{
	private IMultiblock multiblock;

	private boolean canTick = true;
	private boolean showCompleted = false;
	private int tick = 0;

	private float scale = 50f;
	private float transX = 0, transY = 0;
	private float rotX = 0, rotY = 0;
	private int half;

	GuiButtonManualNavigation buttonStop, buttonPause, buttonUp, buttonDown;

	List<String> componentTooltip;
	MultiblockRenderInfo renderInfo;
	MultiblockBlockAccess blockAccess;

	//--- Setup ---//

	@SuppressWarnings("deprecation")
	public IIManualMultiblock(ManualObjectInfo info, EasyNBT nbt)
	{
		super(info, nbt);
	}

	@Override
	public void postInit(IIManualPage page)
	{
		super.postInit(page);
		String mbName = dataSource.getString("mb");
		multiblock = MultiblockHandler.getMultiblocks().stream().filter(mb -> mb.getUniqueName().equals(mbName)).findFirst().orElse(null);

		if(multiblock!=null&&multiblock.getStructureManual()!=null)
		{
			this.renderInfo = new MultiblockRenderInfo(multiblock);
			this.blockAccess = new MultiblockBlockAccess(renderInfo);
			transX = x+60+renderInfo.structureWidth/2f;
			transY = y+35+(float)Math.sqrt(renderInfo.structureHeight*renderInfo.structureHeight+renderInfo.structureWidth*renderInfo.structureWidth+renderInfo.structureLength*renderInfo.structureLength)/2;
			rotX = 25;
			rotY = -45;
			scale = multiblock.getManualScale();
			boolean canRenderFormed = multiblock.canRenderFormedStructure();

			buttonPause = new GuiButtonManualNavigation(gui, 100, x+4, (int)transY-(canRenderFormed?11: 5), 10, 10, 4);
			if(canRenderFormed)
				buttonStop = new GuiButtonManualNavigation(gui, 103, x+4, (int)transY+1, 10, 10, 6);
			if(this.renderInfo.structureHeight > 1)
			{
				buttonUp = new GuiButtonManualNavigation(gui, 101, x+4, (int)transY-(canRenderFormed?14: 8)-16, 10, 16, 3);
				buttonDown = new GuiButtonManualNavigation(gui, 102, x+4, (int)transY+(canRenderFormed?14: 8), 10, 16, 2);
			}
		}

		if(multiblock!=null)
		{
			IngredientStack[] totalMaterials = this.multiblock.getTotalMaterials();
			if(totalMaterials!=null)
			{
				componentTooltip = new ArrayList<>();
				componentTooltip.add(I18n.format("desc.immersiveengineering.info.reqMaterial"));
				int maxOff = 1;
				boolean hasAnyItems = false;
				boolean[] hasItems = new boolean[totalMaterials.length];
				for(int ss = 0; ss < totalMaterials.length; ss++)
					if(totalMaterials[ss]!=null)
					{
						IngredientStack req = totalMaterials[ss];
						int reqSize = req.inputSize;
						for(int slot = 0; slot < ManualUtils.mc().player.inventory.getSizeInventory(); slot++)
						{
							ItemStack inSlot = ManualUtils.mc().player.inventory.getStackInSlot(slot);
							if(!inSlot.isEmpty()&&req.matchesItemStackIgnoringSize(inSlot))
								if((reqSize -= inSlot.getCount()) <= 0)
									break;
						}
						if(reqSize <= 0)
						{
							hasItems[ss] = true;
							if(!hasAnyItems)
								hasAnyItems = true;
						}
						maxOff = Math.max(maxOff, (""+req.inputSize).length());
					}
				for(int ss = 0; ss < totalMaterials.length; ss++)
					if(totalMaterials[ss]!=null)
					{
						IngredientStack req = totalMaterials[ss];
						int indent = maxOff-(""+req.inputSize).length();
						StringBuilder sIndent = new StringBuilder();
						if(indent > 0)
							for(int ii = 0; ii < indent; ii++)
								sIndent.append("0");
						String s = hasItems[ss]?(TextFormatting.GREEN+TextFormatting.BOLD.toString()+"\u2713"+TextFormatting.RESET+" "): hasAnyItems?("   "): "";
						s += TextFormatting.GRAY+sIndent.toString()+req.inputSize+"x "+TextFormatting.RESET;
						ItemStack example = req.getExampleStack();
						if(!example.isEmpty())
							s += example.getRarity().rarityColor+example.getDisplayName();
						else
							s += "???";
						componentTooltip.add(s);
					}
			}
		}

		int yOffTotal = (int)(transY-y+scale*Math.sqrt(
				renderInfo.structureHeight*renderInfo.structureHeight+
						renderInfo.structureWidth*renderInfo.structureWidth+
						renderInfo.structureLength*renderInfo.structureLength)/2);
		half = yOffTotal>>1;
	}

	//--- Rendering, Reaction ---//

	@Override
	public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
	{
		super.drawButton(mc, mx, my, partialTicks);

		buttonStop.drawButton(mc, mx, my, partialTicks);
		buttonPause.drawButton(mc, mx, my, partialTicks);

		if(this.renderInfo.structureHeight > 1)
		{
			buttonUp.drawButton(mc, mx, my, partialTicks);
			buttonDown.drawButton(mc, mx, my, partialTicks);
		}

		manual.fontRenderer.drawString("?", x+116, y+half-4, manual.getTextColour(), false);

		int stackDepth = GL11.glGetInteger(GL11.GL_MODELVIEW_STACK_DEPTH);
		try
		{
			if(multiblock.getStructureManual()!=null)
			{
				if(canTick)
				{
					if(++tick%20==0)
						renderInfo.step();
				}

				int structureLength = renderInfo.structureLength;
				int structureWidth = renderInfo.structureWidth;
				int structureHeight = renderInfo.structureHeight;

				GlStateManager.enableRescaleNormal();
				GlStateManager.pushMatrix();
				RenderHelper.disableStandardItemLighting();

				final BlockRendererDispatcher blockRender = Minecraft.getMinecraft().getBlockRendererDispatcher();

				GlStateManager.translate(transX, transY, Math.max(structureHeight, Math.max(structureWidth, structureLength)));
				GlStateManager.scale(scale, -scale, 1);
				GlStateManager.rotate(rotX, 1, 0, 0);
				GlStateManager.rotate(90+rotY, 0, 1, 0);

				GlStateManager.translate((float)structureLength/-2f, (float)structureHeight/-2f, (float)structureWidth/-2f);

				GlStateManager.disableLighting();

				if(Minecraft.isAmbientOcclusionEnabled())
					GlStateManager.shadeModel(GL11.GL_SMOOTH);
				else
					GlStateManager.shadeModel(GL11.GL_FLAT);

				gui.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				int idx = 0;
				if(showCompleted&&multiblock.canRenderFormedStructure())
					multiblock.renderFormedStructure();
				else
				{
					Tessellator tessellator = ManualUtils.tes();
					BufferBuilder buffer = tessellator.getBuffer();

					for(int h = 0; h < structureHeight; h++)
						for(int l = 0; l < structureLength; l++)
							for(int w = 0; w < structureWidth; w++)
							{
								BlockPos pos = new BlockPos(l, h, w);
								if(!blockAccess.isAirBlock(pos))
								{
									GlStateManager.translate(l, h, w);
									boolean b = multiblock.overwriteBlockRender(renderInfo.data[h][l][w], idx++);
									GlStateManager.translate(-l, -h, -w);
									if(!b)
									{
										IBlockState state = blockAccess.getBlockState(pos);
										buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
										blockRender.renderBlock(state, pos, blockAccess, buffer);
										tessellator.draw();
									}
								}
							}
				}

				GlStateManager.popMatrix();

				GlStateManager.enableBlend();
				RenderHelper.disableStandardItemLighting();
				GlStateManager.disableRescaleNormal();
			}

		} catch(Exception e)
		{
			e.printStackTrace();
		}

		int newStackDepth = GL11.glGetInteger(GL11.GL_MODELVIEW_STACK_DEPTH);
		while(newStackDepth > stackDepth)
		{
			GlStateManager.popMatrix();
			newStackDepth--;
		}
	}

	@Override
	protected int getDefaultHeight()
	{
		return 80;
	}

	@Override
	public List<String> getTooltip(Minecraft mc, int mx, int my)
	{
		if(componentTooltip!=null)
		{
			if(IIUtils.isPointInRectangle(x+116, y+half-4, x+122, y+half+4, mx, my))
				return componentTooltip;
		}
		return null;
	}

	@Override
	public boolean mousePressed(@Nonnull Minecraft mc, int mouseX, int mouseY)
	{
		if(super.mousePressed(mc, mouseX, mouseY))
		{
			if(buttonStop.mousePressed(mc, mouseX, mouseY))
				showCompleted = !showCompleted;
			else if(buttonPause.mousePressed(mc, mouseX, mouseY))
			{
				canTick = !canTick;
				buttonPause.type = buttonPause.type==4?5: 4;
			}
			else if(buttonUp.mousePressed(mc, mouseX, mouseY))
				this.renderInfo.setShowLayer(Math.min(renderInfo.showLayer+1, renderInfo.structureHeight-1));
			else if(buttonDown.mousePressed(mc, mouseX, mouseY))
				this.renderInfo.setShowLayer(Math.max(renderInfo.showLayer-1, -1));
			else
				return false;
			return true;
		}
		return false;
	}

	@Override
	public void mouseDragged(int x, int y, int clickX, int clickY, int mx, int my, int lastX, int lastY, int button)
	{
		if(isMouseOver())
		{
			int dx = mx-lastX;
			int dy = my-lastY;
			rotY = rotY+(dx/104f)*80;
			rotX = rotX+(dy/100f)*80;
		}
	}

	//--- Storage Classes ---//

	@ParametersAreNonnullByDefault
	static class MultiblockBlockAccess implements IBlockAccess
	{
		private final MultiblockRenderInfo data;
		private final IBlockState[][][] structure;

		public MultiblockBlockAccess(MultiblockRenderInfo data)
		{
			this.data = data;
			final int[] index = {0};//Nasty workaround, but IDEA suggested it =P
			this.structure = Arrays.stream(data.data)
					.map(layer -> Arrays.stream(layer)
							.map(row -> Arrays.stream(row)
									.map(itemstack -> convert(index[0]++, itemstack)).collect(Collectors.toList())
									.toArray(new IBlockState[0])).collect(Collectors.toList())
							.toArray(new IBlockState[0][]))
					.toArray(IBlockState[][][]::new);
		}

		private IBlockState convert(int index, @Nullable ItemStack itemstack)
		{
			if(itemstack==null)
				return Blocks.AIR.getDefaultState();
			IBlockState state = data.multiblock.getBlockstateFromStack(index, itemstack);
			if(state!=null)
				return state;
			return Blocks.AIR.getDefaultState();
		}

		@Nullable
		@Override
		public TileEntity getTileEntity(BlockPos pos)
		{
			return null;
		}

		@Override
		public int getCombinedLight(BlockPos pos, int lightValue)
		{
			// full brightness always
			return 15<<20|15<<4;
		}

		@Override
		@Nonnull
		public IBlockState getBlockState(BlockPos pos)
		{
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();

			if(y >= 0&&y < structure.length)
				if(x >= 0&&x < structure[y].length)
					if(z >= 0&&z < structure[y][x].length)
					{
						int index = y*(data.structureLength*data.structureWidth)+x*data.structureWidth+z;
						if(index <= data.getLimiter())
							return structure[y][x][z];
					}
			return Blocks.AIR.getDefaultState();
		}

		/**
		 * Checks to see if an air block exists at the provided location. Note that this only checks to see if the blocks
		 * material is set to air, meaning it is possible for non-vanilla blocks to still pass this check.
		 */
		@Override
		public boolean isAirBlock(BlockPos pos)
		{
			return getBlockState(pos).getBlock()==Blocks.AIR;
		}

		@Override
		@Nonnull
		public Biome getBiome(BlockPos pos)
		{
			World world = Minecraft.getMinecraft().world;
			if(world!=null)
				return world.getBiome(pos);
			else
				return Biomes.BIRCH_FOREST;
		}

		@Override
		public int getStrongPower(BlockPos pos, EnumFacing direction)
		{
			return 0;
		}

		@Override
		@Nonnull
		public WorldType getWorldType()
		{

			World world = Minecraft.getMinecraft().world;
			if(world!=null)
				return world.getWorldType();
			else
				return WorldType.DEFAULT;
		}

		@Override
		public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default)
		{
			return false;
		}
	}

	//Stolen back from boni's StructureInfo
	static class MultiblockRenderInfo
	{
		public final IMultiblock multiblock;
		public ItemStack[][][] data;
		public int[] countPerLevel;
		public int structureHeight = 0;
		public int structureLength = 0;
		public int structureWidth = 0;
		public int showLayer = -1;

		private int blockIndex;
		private final int maxBlockIndex;

		public MultiblockRenderInfo(IMultiblock multiblock)
		{
			this.multiblock = multiblock;
			init(multiblock.getStructureManual());
			maxBlockIndex = blockIndex = structureHeight*structureLength*structureWidth;
		}

		public void init(ItemStack[][][] structure)
		{
			data = structure;
			structureHeight = structure.length;
			structureWidth = 0;
			structureLength = 0;

			countPerLevel = new int[structureHeight];
			for(int h = 0; h < structure.length; h++)
			{
				if(structure[h].length > structureLength)
					structureLength = structure[h].length;
				int perLvl = 0;
				for(int l = 0; l < structure[h].length; l++)
				{
					if(structure[h][l].length > structureWidth)
						structureWidth = structure[h][l].length;
					for(ItemStack ss : structure[h][l])
						if(ss!=null&&!ss.isEmpty())
							perLvl++;
				}
				countPerLevel[h] = perLvl;
			}
		}

		public void setShowLayer(int layer)
		{
			showLayer = layer;
			if(layer < 0)
				reset();
			else
				blockIndex = (layer+1)*(structureLength*structureWidth)-1;
		}

		public void reset()
		{
			blockIndex = maxBlockIndex;
		}

		public void step()
		{
			int start = blockIndex;
			do
			{
				if(++blockIndex >= maxBlockIndex)
					blockIndex = 0;
			}
			while(isEmpty(blockIndex)&&blockIndex!=start);
		}

		private boolean isEmpty(int index)
		{
			int y = index/(structureLength*structureWidth);
			int r = index%(structureLength*structureWidth);
			int x = r/structureWidth;
			int z = r%structureWidth;

			ItemStack stack = data[y][x][z];
			return stack==null||stack.isEmpty();
		}

		public int getLimiter()
		{
			return blockIndex;
		}
	}
}
