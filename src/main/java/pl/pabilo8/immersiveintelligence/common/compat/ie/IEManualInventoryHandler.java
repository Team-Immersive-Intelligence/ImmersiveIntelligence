package pl.pabilo8.immersiveintelligence.common.compat.ie;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.lib.manual.ManualInstance.ManualLink;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.clipboard.ManualPageReference;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.client.util.IIKeybind;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Overrides;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.compat.ImmersivePetroleumHelper;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIIElectricTool;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemUtils;

import java.util.Collections;

/**
 * Opens an item's manual page when the player holds a key with a manual on the cursor.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 30.10.2026
 * @since 30.10.2026
 */
@SideOnly(Side.CLIENT)
public class IEManualInventoryHandler
{
	private static final ResourceLocation RING_TEXTURE = new ResourceLocation(ImmersiveIntelligence.MODID, "textures/gui/manual/manual_lookup.png");
	private final KeyBinding keybind = new IIKeybind("manualLookup", Keyboard.KEY_Z, "key.categories.inventory")
			.withContext(KeyConflictContext.GUI).register();
	private ItemStack targetStack = ItemStack.EMPTY, sourceStack = ItemStack.EMPTY;
	private GuiContainer sourceScreen, currentScreen;
	private Slot hoveredSlot, sourceSlot;
	private double holdStart;
	private long clientTicks;
	private boolean sourceCloned;

	/**
	 * Counts client ticks, including ticks while an inventory pauses the world.
	 */
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		if(event.phase==TickEvent.Phase.END)
			clientTicks++;
	}

	/**
	 * Records the slot before the player picks up the manual.
	 */
	@SubscribeEvent
	public void onMouseInput(GuiScreenEvent.MouseInputEvent.Pre event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(!Overrides.enableManualLookup||!(event.getGui() instanceof GuiContainer gui)||mc.player==null
				||!Mouse.getEventButtonState())
			return;
		if(!mc.player.inventory.getItemStack().isEmpty())
		{
			if(!IIItemUtils.isEngineersManual(mc.player.inventory.getItemStack()))
				clearSource();
			return;
		}

		clearSource();
		reset();
		int mouseX = Mouse.getEventX()*gui.width/mc.displayWidth-gui.getGuiLeft();
		int mouseY = gui.height-Mouse.getEventY()*gui.height/mc.displayHeight-1-gui.getGuiTop();
		for(Slot slot : gui.inventorySlots.inventorySlots)
			if(slot.isEnabled()&&IIMath.isPointInRectangle(slot.xPos-1, slot.yPos-1, slot.xPos+17, slot.yPos+17, mouseX, mouseY))
			{
				if(IIItemUtils.isEngineersManual(slot.getStack()))
				{
					sourceScreen = gui;
					sourceSlot = slot;
					sourceStack = slot.getStack().copy();
					if(gui instanceof GuiContainerCreative)
					{
						sourceCloned = slot.inventory!=mc.player.inventory
								||mc.gameSettings.keyBindPickBlock.isActiveAndMatches(Mouse.getEventButton()-100);
						if(!sourceCloned)
							//Creative wrappers can use a different slot index. Record the real slot.
							sourceSlot = mc.player.inventoryContainer.inventorySlots.stream()
									.filter(candidate -> slot.isHere(candidate.inventory, candidate.getSlotIndex()))
									.findFirst().orElse(null);
					}
				}
				break;
			}
	}

	/**
	 * Resets the hold when the screen changes.
	 */
	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event)
	{
		reset();
		clearSource();
	}

	/**
	 * Draws the hold indicator after the container and its carried item.
	 */
	@SubscribeEvent
	public void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event)
	{
		Minecraft mc = ClientUtils.mc();
		if(!Overrides.enableManualLookup)
		{
			reset();
			clearSource();
			return;
		}

		//Containers do not render item tooltips while an item is hovered over
		if(!(event.getGui() instanceof GuiContainer gui)||mc.currentScreen!=gui||mc.player==null
				||!keybind.getKeyModifier().isActive(keybind.getKeyConflictContext()))
		{
			reset();
			return;
		}

		ItemStack carried = mc.player.inventory.getItemStack();
		Slot slot = gui.getSlotUnderMouse();
		if(!IIItemUtils.isEngineersManual(carried)||sourceScreen!=gui||sourceSlot==null
				||!ItemStack.areItemsEqual(carried, sourceStack)||!ItemStack.areItemStackTagsEqual(carried, sourceStack))
		{
			reset();
			clearSource();
			return;
		}

		if(slot==null||!slot.isEnabled()||!slot.getHasStack())
		{
			reset();
			return;
		}

		ItemStack slotStack = slot.getStack();
		ManualLink link = ManualHelper.getManual().getManualLink(slotStack);
		if(slotStack.getItem() instanceof ItemIIElectricTool)
			link = ManualHelper.getManual().getManualLink(new ItemStack(slotStack.getItem()));
		else if(IICompatModule.petroleum&&ImmersivePetroleumHelper.isProjector(slotStack))
			link = ImmersivePetroleumHelper.getManualLinkForProjector(slotStack);
		else if(link==null&&slotStack.hasTagCompound())
		{
			//Keep exact links first, then ignore tags such as stored energy or custom names.
			ItemStack untagged = slotStack.copy();
			untagged.setTagCompound(null);
			if(!slotStack.getItem().getHasSubtypes())
				untagged.setItemDamage(0);
			link = ManualHelper.getManual().getManualLink(untagged);

		}
		if(link==null)
		{
			reset();
			return;
		}

		if(!GameSettings.isKeyDown(keybind))
		{
			//Draw tooltip
			GuiUtils.drawHoveringText(Collections.singletonList(
							TextFormatting.GOLD+I18n.format(IIReference.DESCRIPTION_KEY+"info.holdAny", keybind.getDisplayName())+TextFormatting.GRAY+" - "
									+I18n.format(keybind.getKeyDescription())
					), event.getMouseX(), event.getMouseY(),
					gui.width, gui.height, 255, IIClientUtils.fontRegular);
			reset();
			return;
		}

		double now = (double)clientTicks+mc.getRenderPartialTicks();
		if(currentScreen!=gui||hoveredSlot!=slot||!ItemStack.areItemStacksEqual(targetStack, slotStack))
		{
			currentScreen = gui;
			hoveredSlot = slot;
			targetStack = slotStack.copy();
			holdStart = now;
		}

		//Save previous GL state
		boolean lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
		boolean depth = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
		boolean alpha = GL11.glIsEnabled(GL11.GL_ALPHA_TEST);
		int texture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
		GlStateManager.pushMatrix();
		GlStateManager.translate(event.getMouseX()-11, event.getMouseY()-11, 400);
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.enableAlpha();
		GlStateManager.color(1, 1, 1, 1);

		float progress = (float)MathHelper.clamp((now-holdStart)/Math.max(1, Overrides.manualLookupTime), 0, 1);

		IIClientUtils.bindTexture(RING_TEXTURE);

		//Draw the ring
		float length = progress*80;
		float topRight = MathHelper.clamp(length, 0, 11);
		float right = MathHelper.clamp(length-11, 0, 20);
		float bottom = MathHelper.clamp(length-31, 0, 20);
		float left = MathHelper.clamp(length-51, 0, 20);
		float topLeft = MathHelper.clamp(length-71, 0, 9);
		IIDrawUtils.startTexturedColored()
				//Background
				.drawTexColorRect(0, 0, 22, 22, IIColor.MC_DARK_GRAY, 0, .6875f, 0, .6875f)
				//Top-Right
				.drawTexColorRect(11, 0, topRight, 2, IIReference.COLOR_IMMERSIVE_ORANGE, .34375f, (11+topRight)/32, 0, .0625f)
				//Right
				.drawTexColorRect(20, 2, 2, right, IIReference.COLOR_IMMERSIVE_ORANGE, .625f, .6875f, .0625f, (2+right)/32)
				//Bottom
				.drawTexColorRect(20-bottom, 20, bottom, 2, IIReference.COLOR_IMMERSIVE_ORANGE, (20-bottom)/32, .625f, .625f, .6875f)
				//Left
				.drawTexColorRect(0, 20-left, 2, left, IIReference.COLOR_IMMERSIVE_ORANGE, 0, .0625f, (20-left)/32, .625f)
				//Top-Left
				.drawTexColorRect(2, 0, topLeft, 2, IIReference.COLOR_IMMERSIVE_ORANGE, .0625f, (2+topLeft)/32, 0, .0625f)
				.finish();


		//Load previous GL state
		GlStateManager.bindTexture(texture);
		IIColor.WHITE.glColor();
		if(lighting)
			GlStateManager.enableLighting();
		if(depth)
			GlStateManager.enableDepth();
		if(!alpha)
			GlStateManager.disableAlpha();
		GlStateManager.popMatrix();

		if(progress < 1)
			return;

		//Cannot open the manual because the source slot is empty
		if(!returnManual(mc, gui, carried))
			return;

		//Open the manual page
		mc.player.closeScreen();
		new ManualPageReference(link.getKey(), link.getPage(), "").open();
	}

	private void reset()
	{
		currentScreen = null;
		hoveredSlot = null;
		targetStack = ItemStack.EMPTY;
	}

	private void clearSource()
	{
		sourceScreen = null;
		sourceSlot = null;
		sourceStack = ItemStack.EMPTY;
		sourceCloned = false;
	}

	private boolean returnManual(Minecraft mc, GuiContainer gui, ItemStack manual)
	{
		boolean creative = gui instanceof GuiContainerCreative;
		Container container = creative?mc.player.inventoryContainer: gui.inventorySlots;
		Slot slot = sourceSlot;
		if(sourceCloned)
		{
			if(!creative||!ItemStack.areItemStacksEqual(sourceStack, slot.getStack()))
				return false;
			//A cloned manual leaves its source intact. Remove only the carried copy.
			mc.player.inventory.setItemStack(ItemStack.EMPTY);
			return true;
		}

		if(slot==null||!container.inventorySlots.contains(slot)||!slot.isEnabled()||!slot.isItemValid(manual)
				||slot.getItemStackLimit(manual) < sourceStack.getCount())
			return false;
		ItemStack remaining = slot.getStack();
		if((!remaining.isEmpty()&&(!ItemStack.areItemsEqual(remaining, manual)||!ItemStack.areItemStackTagsEqual(remaining, manual)))
				||remaining.getCount()+manual.getCount()!=sourceStack.getCount())
			return false;

		if(creative)
		{
			slot.putStack(sourceStack.copy());
			mc.playerController.sendSlotPacket(slot.getStack(), slot.slotNumber);
			mc.player.inventory.setItemStack(ItemStack.EMPTY);
		}
		else
			mc.playerController.windowClick(container.windowId, slot.slotNumber, 0, ClickType.PICKUP, mc.player);
		boolean matches = mc.player.inventory.getItemStack().isEmpty()&&ItemStack.areItemStacksEqual(slot.getStack(), sourceStack);

		//Reset the source slot
		reset();
		return matches;
	}
}
