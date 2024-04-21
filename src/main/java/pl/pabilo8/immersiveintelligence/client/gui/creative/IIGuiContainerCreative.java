package pl.pabilo8.immersiveintelligence.client.gui.creative;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.inventory.CreativeCrafting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.CreativeSettings;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.HotbarSnapshot;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IICreativeTab;
import pl.pabilo8.immersiveintelligence.common.IICreativeTab.IICreativeSubTab;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIAssemblyScheme;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

/**
 * Scary? Yes. Over-Engineered? Most likely. Did I cry making it? Absolutely. Do I want to port it? Never in my life.
 * @author GabrielV (gabriel@iiteam.net)
 */
public class IIGuiContainerCreative extends InventoryEffectRenderer
{
	public static ResourceLocation CREATIVE_INVENTORY_TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
	private static final InventoryBasic basicInventory = new InventoryBasic("tmp", true, 45);
	private static int selectedTabIndex, selectedIITabIndex;
	private float currentScroll;
	private boolean isScrolling;
	private boolean wasClicking;
	private GuiTextField searchField;
	private List<Slot> originalSlots;
	private Slot destroyItemSlot;
	private boolean clearSearch;
	private CreativeCrafting listener;
	private static int tabPage;
	private int maxPages = 0;

	public IIGuiContainerCreative(EntityPlayer player)
	{
		super(new IIContainerCreative(player));
		player.openContainer = this.inventorySlots;
		this.allowUserInput = true;
		this.ySize = 136;
		this.xSize = 195;
	}

	public void updateScreen()
	{
		if(!this.mc.playerController.isInCreativeMode())
		{
			this.mc.displayGuiScreen(new GuiInventory(this.mc.player));
		}

	}

	protected void handleMouseClick(@Nullable Slot slotIn, int slotId, int mouseButton, ClickType type)
	{
		this.clearSearch = true;
		boolean flag = type==ClickType.QUICK_MOVE;
		type = slotId==-999&&type==ClickType.PICKUP?ClickType.THROW: type;
		ItemStack itemstack5;
		InventoryPlayer inventoryplayer;
		if(slotIn==null&&selectedTabIndex!=CreativeTabs.INVENTORY.getTabIndex()&&type!=ClickType.QUICK_CRAFT)
		{
			inventoryplayer = this.mc.player.inventory;
			if(!inventoryplayer.getItemStack().isEmpty())
			{
				if(mouseButton==0)
				{
					this.mc.player.dropItem(inventoryplayer.getItemStack(), true);
					this.mc.playerController.sendPacketDropItem(inventoryplayer.getItemStack());
					inventoryplayer.setItemStack(ItemStack.EMPTY);
				}

				if(mouseButton==1)
				{
					itemstack5 = inventoryplayer.getItemStack().splitStack(1);
					this.mc.player.dropItem(itemstack5, true);
					this.mc.playerController.sendPacketDropItem(itemstack5);
				}
			}
		}
		else
		{
			if(slotIn!=null&&!slotIn.canTakeStack(this.mc.player))
			{
				return;
			}

			if(slotIn==this.destroyItemSlot&&flag)
			{
				for(int j = 0; j < this.mc.player.inventoryContainer.getInventory().size(); ++j)
				{
					this.mc.playerController.sendSlotPacket(ItemStack.EMPTY, j);
				}
			}
			else
			{
				ItemStack itemstack3;
				if(selectedTabIndex==CreativeTabs.INVENTORY.getTabIndex())
				{
					if(slotIn==this.destroyItemSlot)
					{
						this.mc.player.inventory.setItemStack(ItemStack.EMPTY);
					}
					else if(type==ClickType.THROW&&slotIn!=null&&slotIn.getHasStack())
					{
						itemstack3 = slotIn.decrStackSize(mouseButton==0?1: slotIn.getStack().getMaxStackSize());
						itemstack5 = slotIn.getStack();
						this.mc.player.dropItem(itemstack3, true);
						this.mc.playerController.sendPacketDropItem(itemstack3);
						this.mc.playerController.sendSlotPacket(itemstack5, ((IICreativeSlot)slotIn).slot.slotNumber);
					}
					else if(type==ClickType.THROW&&!this.mc.player.inventory.getItemStack().isEmpty())
					{
						this.mc.player.dropItem(this.mc.player.inventory.getItemStack(), true);
						this.mc.playerController.sendPacketDropItem(this.mc.player.inventory.getItemStack());
						this.mc.player.inventory.setItemStack(ItemStack.EMPTY);
					}
					else
					{
						this.mc.player.inventoryContainer.slotClick(slotIn==null?slotId: ((IICreativeSlot)slotIn).slot.slotNumber, mouseButton, type, this.mc.player);
						this.mc.player.inventoryContainer.detectAndSendChanges();
					}
				}
				else
				{
					ItemStack itemstack2;
					if(type!=ClickType.QUICK_CRAFT&&slotIn.inventory==basicInventory)
					{
						inventoryplayer = this.mc.player.inventory;
						itemstack5 = inventoryplayer.getItemStack();
						ItemStack itemstack7 = slotIn.getStack();
						if(type==ClickType.SWAP)
						{
							if(!itemstack7.isEmpty()&&mouseButton >= 0&&mouseButton < 9)
							{
								itemstack2 = itemstack7.copy();
								itemstack2.setCount(itemstack2.getMaxStackSize());
								this.mc.player.inventory.setInventorySlotContents(mouseButton, itemstack2);
								this.mc.player.inventoryContainer.detectAndSendChanges();
							}

							return;
						}

						if(type==ClickType.CLONE)
						{
							if(inventoryplayer.getItemStack().isEmpty()&&slotIn.getHasStack())
							{
								itemstack2 = slotIn.getStack().copy();
								itemstack2.setCount(itemstack2.getMaxStackSize());
								inventoryplayer.setItemStack(itemstack2);
							}

							return;
						}

						if(type==ClickType.THROW)
						{
							if(!itemstack7.isEmpty())
							{
								itemstack2 = itemstack7.copy();
								itemstack2.setCount(mouseButton==0?1: itemstack2.getMaxStackSize());
								this.mc.player.dropItem(itemstack2, true);
								this.mc.playerController.sendPacketDropItem(itemstack2);
							}

							return;
						}

						if(!itemstack5.isEmpty()&&!itemstack7.isEmpty()&&itemstack5.isItemEqual(itemstack7)&&ItemStack.areItemStackTagsEqual(itemstack5, itemstack7))
						{
							if(mouseButton==0)
							{
								if(flag)
								{
									itemstack5.setCount(itemstack5.getMaxStackSize());
								}
								else if(itemstack5.getCount() < itemstack5.getMaxStackSize())
								{
									itemstack5.grow(1);
								}
							}
							else
							{
								itemstack5.shrink(1);
							}
						}
						else if(!itemstack7.isEmpty()&&itemstack5.isEmpty())
						{
							inventoryplayer.setItemStack(itemstack7.copy());
							itemstack5 = inventoryplayer.getItemStack();
							if(flag)
							{
								itemstack5.setCount(itemstack5.getMaxStackSize());
							}
						}
						else if(mouseButton==0)
						{
							inventoryplayer.setItemStack(ItemStack.EMPTY);
						}
						else
						{
							inventoryplayer.getItemStack().shrink(1);
						}
					}
					else if(this.inventorySlots!=null)
					{
						itemstack3 = slotIn==null?ItemStack.EMPTY: this.inventorySlots.getSlot(slotIn.slotNumber).getStack();
						this.inventorySlots.slotClick(slotIn==null?slotId: slotIn.slotNumber, mouseButton, type, this.mc.player);

						if(Container.getDragEvent(mouseButton)==2)
						{
							for(int k = 0; k < 9; ++k)
							{
								this.mc.playerController.sendSlotPacket(this.inventorySlots.getSlot(45+k).getStack(), 36+k);
							}
						}
						else if(slotIn!=null)
						{
							itemstack5 = this.inventorySlots.getSlot(slotIn.slotNumber).getStack();
							this.mc.playerController.sendSlotPacket(itemstack5, slotIn.slotNumber-this.inventorySlots.inventorySlots.size()+9+36);
							if(type==ClickType.SWAP)
							{
								int i = 45+mouseButton;
								this.mc.playerController.sendSlotPacket(itemstack3, i-this.inventorySlots.inventorySlots.size()+9+36);
							}
							else if(type==ClickType.THROW&&!itemstack3.isEmpty())
							{
								itemstack2 = itemstack3.copy();
								itemstack2.setCount(mouseButton==0?1: itemstack2.getMaxStackSize());
								this.mc.player.dropItem(itemstack2, true);
								this.mc.playerController.sendPacketDropItem(itemstack2);
							}
							this.mc.player.inventoryContainer.detectAndSendChanges();
						}
					}
				}
			}
		}

	}

	protected void updateActivePotionEffects()
	{
		int i = this.guiLeft;
		super.updateActivePotionEffects();
		if(this.searchField!=null&&this.guiLeft!=i)
		{
			this.searchField.x = this.guiLeft+82;
		}

	}

	public void initGui()
	{
		if(this.mc.playerController.isInCreativeMode())
		{
			super.initGui();
			this.buttonList.clear();
			Keyboard.enableRepeatEvents(true);
			this.searchField = new GuiTextField(0, this.fontRenderer, this.guiLeft+82, this.guiTop+6, 80, this.fontRenderer.FONT_HEIGHT);
			this.searchField.setMaxStringLength(50);
			this.searchField.setEnableBackgroundDrawing(false);
			this.searchField.setVisible(false);
			this.searchField.setTextColor(16777215);
			int i = selectedTabIndex;
			selectedTabIndex = -1;
			selectedIITabIndex = -1;
			this.setCurrentCreativeTab(CreativeTabs.CREATIVE_TAB_ARRAY[i]);
			this.listener = new CreativeCrafting(this.mc);
			this.mc.player.inventoryContainer.addListener(this.listener);
			int tabCount = CreativeTabs.CREATIVE_TAB_ARRAY.length;
			if(tabCount > 12)
			{
				this.buttonList.add(new GuiButton(101, this.guiLeft, this.guiTop-50, 20, 20, "<"));
				this.buttonList.add(new GuiButton(102, this.guiLeft+this.xSize-20, this.guiTop-50, 20, 20, ">"));
				this.maxPages = (int)Math.ceil((double)(tabCount-12)/10.0);
			}
		}
		else
		{
			this.mc.displayGuiScreen(new GuiInventory(this.mc.player));
		}

	}

	public void onGuiClosed()
	{
		super.onGuiClosed();
		if(this.mc.player!=null&&this.mc.player.inventory!=null)
		{
			this.mc.player.inventoryContainer.removeListener(this.listener);
		}

		Keyboard.enableRepeatEvents(false);
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(!CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex].hasSearchBar())
		{
			if(GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat))
			{
				this.setCurrentCreativeTab(CreativeTabs.SEARCH);
			}
			else
			{
				super.keyTyped(typedChar, keyCode);
			}
		}
		else
		{
			if(this.clearSearch)
			{
				this.clearSearch = false;
				this.searchField.setText("");
			}

			if(!this.checkHotbarKeys(keyCode))
			{
				if(this.searchField.textboxKeyTyped(typedChar, keyCode))
				{
					this.updateCreativeSearch();
				}
				else
				{
					super.keyTyped(typedChar, keyCode);
				}
			}
		}

	}

	private void updateCreativeSearch()
	{
		IIContainerCreative guicontainercreative$containercreative = (IIContainerCreative)this.inventorySlots;
		guicontainercreative$containercreative.itemList.clear();
		CreativeTabs tab = CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex];
		if(tab.hasSearchBar()&&tab!=CreativeTabs.SEARCH)
		{
			tab.displayAllRelevantItems(guicontainercreative$containercreative.itemList);
			if(!this.searchField.getText().isEmpty())
			{
				String search = this.searchField.getText().toLowerCase(Locale.ROOT);
				Iterator<ItemStack> itr = guicontainercreative$containercreative.itemList.iterator();

				while(itr.hasNext())
				{
					ItemStack stack = (ItemStack)itr.next();
					boolean matches = false;
					Iterator var7 = stack.getTooltip(this.mc.player, this.mc.gameSettings.advancedItemTooltips?TooltipFlags.ADVANCED: TooltipFlags.NORMAL).iterator();

					while(var7.hasNext())
					{
						String line = (String)var7.next();
						if(TextFormatting.getTextWithoutFormattingCodes(line).toLowerCase(Locale.ROOT).contains(search))
						{
							matches = true;
							break;
						}
					}

					if(!matches)
					{
						itr.remove();
					}
				}
			}

			this.currentScroll = 0.0F;
			guicontainercreative$containercreative.scrollTo(0.0F);
		}
		else
		{
			if(this.searchField.getText().isEmpty())
			{
				Iterator var3 = Item.REGISTRY.iterator();

				while(var3.hasNext())
				{
					Item item = (Item)var3.next();
					item.getSubItems(CreativeTabs.SEARCH, guicontainercreative$containercreative.itemList);
				}
			}
			else
			{
				guicontainercreative$containercreative.itemList.addAll(this.mc.getSearchTree(SearchTreeManager.ITEMS).search(this.searchField.getText().toLowerCase(Locale.ROOT)));
			}

			this.currentScroll = 0.0F;
			guicontainercreative$containercreative.scrollTo(0.0F);
		}
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		CreativeTabs creativetabs = CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex];
		if(creativetabs!=null&&creativetabs.drawInForegroundOfTab())
		{
			GlStateManager.disableBlend();
			this.fontRenderer.drawString(I18n.format(creativetabs.getTranslatedTabLabel(), new Object[0]), 8, 6, creativetabs.getLabelColor());
		}

	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if(mouseButton==0)
		{
			int i = mouseX-this.guiLeft;
			int j = mouseY-this.guiTop;
			CreativeTabs[] var6 = CreativeTabs.CREATIVE_TAB_ARRAY;
			int var7 = var6.length;

			for(int var8 = 0; var8 < var7; ++var8)
			{
				CreativeTabs creativetabs = var6[var8];
				if(this.isMouseOverTab(creativetabs, i, j))
				{
					return;
				}
			}

			IICreativeSubTab[] var9 = IICreativeSubTab.CREATIVE_SUB_TABS;
			for(int var10 = 0; var10 < var9.length; ++var10)
			{
				IICreativeSubTab tab = var9[var10];
				if(this.isMouseOverSubTab(tab, mouseX, mouseY))
				{
					return;
				}
			}
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	protected void mouseReleased(int mouseX, int mouseY, int state)
	{
		if(state==0)
		{
			int i = mouseX-this.guiLeft;
			int j = mouseY-this.guiTop;
			CreativeTabs[] var6 = CreativeTabs.CREATIVE_TAB_ARRAY;
			int var7 = var6.length;

			for(int var8 = 0; var8 < var7; ++var8)
			{
				CreativeTabs creativetabs = var6[var8];
				if(creativetabs!=null&&this.isMouseOverTab(creativetabs, i, j))
				{
					this.setCurrentCreativeTab(creativetabs);
					return;
				}
			}

			IICreativeSubTab[] var9 = IICreativeSubTab.CREATIVE_SUB_TABS;
			for(int var10 = 0; var10 < var9.length; ++var10)
			{
				IICreativeSubTab tab = var9[var10];
				if(this.isMouseOverSubTab(tab, mouseX, mouseY))
				{
					this.setCurrentIITab(tab);
					return;
				}
			}
		}

		super.mouseReleased(mouseX, mouseY, state);
	}

	private boolean needsScrollBars()
	{
		if(CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex]==null)
		{
			return false;
		}
		else
		{
			return selectedTabIndex!=CreativeTabs.INVENTORY.getTabIndex()&&CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex].shouldHidePlayerInventory()&&((IIContainerCreative)this.inventorySlots).canScroll();
		}
	}

	private void setCurrentCreativeTab(CreativeTabs tab)
	{
		if(tab==null) return;

		int i = selectedTabIndex;
		selectedTabIndex = tab.getTabIndex();
		IIContainerCreative guicontainercreative$containercreative = (IIContainerCreative)this.inventorySlots;
		this.dragSplittingSlots.clear();
		guicontainercreative$containercreative.itemList.clear();
		if(tab==CreativeTabs.HOTBAR)
		{
			for(int j = 0; j < 9; ++j)
			{
				HotbarSnapshot hotbarsnapshot = this.mc.creativeSettings.getHotbarSnapshot(j);
				if(hotbarsnapshot.isEmpty())
				{
					for(int k = 0; k < 9; ++k)
					{
						if(k==j)
						{
							ItemStack itemstack = new ItemStack(Items.PAPER);
							itemstack.getOrCreateSubCompound("CustomCreativeLock");
							String s = GameSettings.getKeyDisplayString(this.mc.gameSettings.keyBindsHotbar[j].getKeyCode());
							String s1 = GameSettings.getKeyDisplayString(this.mc.gameSettings.keyBindSaveToolbar.getKeyCode());
							itemstack.setStackDisplayName((new TextComponentTranslation("inventory.hotbarInfo", new Object[]{s1, s})).getUnformattedText());
							guicontainercreative$containercreative.itemList.add(itemstack);
						}
						else
						{
							guicontainercreative$containercreative.itemList.add(ItemStack.EMPTY);
						}
					}
				}
				else
				{
					guicontainercreative$containercreative.itemList.addAll(hotbarsnapshot);
				}
			}
		}
		else if(tab!=CreativeTabs.SEARCH&&tab!=IIContent.II_CREATIVE_TAB)
		{
			tab.displayAllRelevantItems(guicontainercreative$containercreative.itemList);
		}
		else if(tab==IIContent.II_CREATIVE_TAB)
		{
			this.setCurrentIITab(IICreativeSubTab.CREATIVE_SUB_TABS[Math.max(0, selectedIITabIndex)]);
		}

		if(tab==CreativeTabs.INVENTORY)
		{
			Container container = this.mc.player.inventoryContainer;
			if(this.originalSlots==null)
			{
				this.originalSlots = guicontainercreative$containercreative.inventorySlots;
			}

			guicontainercreative$containercreative.inventorySlots = Lists.newArrayList();

			for(int l = 0; l < container.inventorySlots.size(); ++l)
			{
				Slot slot = new IICreativeSlot((Slot)container.inventorySlots.get(l), l);
				guicontainercreative$containercreative.inventorySlots.add(slot);
				int i1;
				int k1;
				int i2;
				if(l >= 5&&l < 9)
				{
					i1 = l-5;
					k1 = i1/2;
					i2 = i1%2;
					slot.xPos = 54+k1*54;
					slot.yPos = 6+i2*27;
				}
				else if(l >= 0&&l < 5)
				{
					slot.xPos = -2000;
					slot.yPos = -2000;
				}
				else if(l==45)
				{
					slot.xPos = 35;
					slot.yPos = 20;
				}
				else if(l < container.inventorySlots.size())
				{
					i1 = l-9;
					k1 = i1%9;
					i2 = i1/9;
					slot.xPos = 9+k1*18;
					if(l >= 36)
					{
						slot.yPos = 112;
					}
					else
					{
						slot.yPos = 54+i2*18;
					}
				}
			}

			this.destroyItemSlot = new Slot(basicInventory, 0, 173, 112);
			guicontainercreative$containercreative.inventorySlots.add(this.destroyItemSlot);
		}
		else if(i==CreativeTabs.INVENTORY.getTabIndex())
		{
			guicontainercreative$containercreative.inventorySlots = this.originalSlots;
			this.originalSlots = null;
		}

		if(this.searchField!=null)
		{
			if(tab.hasSearchBar())
			{
				this.searchField.setVisible(true);
				this.searchField.setCanLoseFocus(false);
				this.searchField.setFocused(true);
				this.searchField.setText("");
				this.searchField.width = tab.getSearchbarWidth();
				this.searchField.x = this.guiLeft+171-this.searchField.width;
				this.updateCreativeSearch();
			}
			else
			{
				this.searchField.setVisible(false);
				this.searchField.setCanLoseFocus(true);
				this.searchField.setFocused(false);
			}
		}

		this.currentScroll = 0.0F;
		guicontainercreative$containercreative.scrollTo(0.0F);
	}

	private void getSubTabItems(IICreativeSubTab tab, @Nonnull NonNullList<ItemStack> list)
	{
		for (IIItemStack stack : IIContent.CATEGORY_ITEM_MAP)
		{
			if (stack.category()==tab.getCategory())
			{
				if (stack.stack().getItem() instanceof ItemIIAssemblyScheme)
				{
					stack.stack().getItem().getSubItems(IIContent.II_CREATIVE_TAB, list);
				} else {
					list.add(stack.stack());
				}
			}
		}

		switch(tab.getCategory())
		{
			case WARFARE:
			{
				IICreativeTab.addArmor(list);
				IICreativeTab.addGuns(list);
				IICreativeTab.addExampleBullets(list);
				break;
			}
			case RESOURCE:
			{
				for(Fluid fluid : IICreativeTab.fluidBucketMap) IICreativeTab.addFluidBucket(fluid, list);
				break;
			}
		}

	}

	private void setCurrentIITab(IICreativeSubTab tab)
	{
		if(tab==null) return;
		selectedIITabIndex = tab.tabIndex;
		IIContainerCreative guicontainercreative$containercreative = (IIContainerCreative)this.inventorySlots;
		this.dragSplittingSlots.clear();
		guicontainercreative$containercreative.itemList.clear();
		this.getSubTabItems(tab, guicontainercreative$containercreative.itemList);
		this.currentScroll = 0.0F;
		guicontainercreative$containercreative.scrollTo(0.0F);
	}

	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();
		if(i!=0&&this.needsScrollBars())
		{
			int j = (((IIContainerCreative)this.inventorySlots).itemList.size()+9-1)/9-5;
			if(i > 0)
			{
				i = 1;
			}

			if(i < 0)
			{
				i = -1;
			}

			this.currentScroll = (float)((double)this.currentScroll-(double)i/(double)j);
			this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
			((IIContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
		}

	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		boolean flag = Mouse.isButtonDown(0);
		int i = this.guiLeft;
		int j = this.guiTop;
		int k = i+175;
		int l = j+18;
		int i1 = k+14;
		int j1 = l+112;
		if(!this.wasClicking&&flag&&mouseX >= k&&mouseY >= l&&mouseX < i1&&mouseY < j1)
		{
			this.isScrolling = this.needsScrollBars();
		}

		if(!flag)
		{
			this.isScrolling = false;
		}

		this.wasClicking = flag;
		if(this.isScrolling)
		{
			this.currentScroll = ((float)(mouseY-l)-7.5F)/((float)(j1-l)-15.0F);
			this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
			((IIContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
		int start = tabPage*10;
		int end = Math.min(CreativeTabs.CREATIVE_TAB_ARRAY.length, (tabPage+1)*10+2);
		if(tabPage!=0)
		{
			start += 2;
		}

		boolean rendered = false;
		CreativeTabs[] var14 = Arrays.copyOfRange(CreativeTabs.CREATIVE_TAB_ARRAY, start, end);
		int width = var14.length;

		for(int var16 = 0; var16 < width; ++var16)
		{
			CreativeTabs creativetabs = var14[var16];
			if(creativetabs!=null&&this.renderCreativeInventoryHoveringText(creativetabs, mouseX, mouseY))
			{
				rendered = true;
				break;
			}
		}

		IICreativeSubTab[] var17 = Arrays.copyOfRange(IICreativeSubTab.CREATIVE_SUB_TABS, 0, IICreativeSubTab.CREATIVE_SUB_TABS.length);
		for(int var18 = 0; var18 < var17.length; ++var18)
		{
			IICreativeSubTab tab = var17[var18];
			if(tab!=null&&this.renderCreativeInventorySubTabText(tab, mouseX, mouseY))
			{
				break;
			}
		}

		if(!rendered&&!this.renderCreativeInventoryHoveringText(CreativeTabs.SEARCH, mouseX, mouseY))
		{
			this.renderCreativeInventoryHoveringText(CreativeTabs.INVENTORY, mouseX, mouseY);
		}

		if(this.destroyItemSlot!=null&&selectedTabIndex==CreativeTabs.INVENTORY.getTabIndex()&&this.isPointInRegion(this.destroyItemSlot.xPos, this.destroyItemSlot.yPos, 16, 16, mouseX, mouseY))
		{
			this.drawHoveringText(I18n.format("inventory.binSlot"), mouseX, mouseY);
		}

		if(this.maxPages!=0)
		{
			String page = String.format("%d / %d", tabPage+1, this.maxPages+1);
			width = this.fontRenderer.getStringWidth(page);
			GlStateManager.disableLighting();
			this.zLevel = 300.0F;
			this.itemRender.zLevel = 300.0F;
			this.fontRenderer.drawString(page, this.guiLeft+this.xSize/2-width/2, this.guiTop-44, -1);
			this.zLevel = 0.0F;
			this.itemRender.zLevel = 0.0F;
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableLighting();
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	protected void renderToolTip(ItemStack stack, int x, int y)
	{
		if(selectedTabIndex==CreativeTabs.SEARCH.getTabIndex())
		{
			List<String> list = stack.getTooltip(this.mc.player, this.mc.gameSettings.advancedItemTooltips?TooltipFlags.ADVANCED: TooltipFlags.NORMAL);
			CreativeTabs creativetabs = stack.getItem().getCreativeTab();
			if(creativetabs==null&&stack.getItem()==Items.ENCHANTED_BOOK)
			{
				Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
				if(map.size()==1)
				{
					Enchantment enchantment = (Enchantment)map.keySet().iterator().next();
					CreativeTabs[] var8 = CreativeTabs.CREATIVE_TAB_ARRAY;
					int var9 = var8.length;

					for(int var10 = 0; var10 < var9; ++var10)
					{
						CreativeTabs creativetabs1 = var8[var10];
						if(creativetabs1.hasRelevantEnchantmentType(enchantment.type))
						{
							creativetabs = creativetabs1;
							break;
						}
					}
				}
			}

			if(creativetabs!=null)
			{
				list.add(1, ""+TextFormatting.BOLD+TextFormatting.BLUE+I18n.format(creativetabs.getTranslatedTabLabel(), new Object[0]));
			}

			for(int i = 0; i < list.size(); ++i)
			{
				if(i==0)
				{
					list.set(i, stack.getItem().getForgeRarity(stack).getColor()+(String)list.get(i));
				}
				else
				{
					list.set(i, TextFormatting.GRAY+(String)list.get(i));
				}
			}

			FontRenderer font = stack.getItem().getFontRenderer(stack);
			GuiUtils.preItemToolTip(stack);
			this.drawHoveringText(list, x, y, font==null?this.fontRenderer: font);
			GuiUtils.postItemToolTip();
		}
		else
		{
			super.renderToolTip(stack, x, y);
		}

	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.enableGUIStandardItemLighting();
		CreativeTabs creativetabs = CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex];
		int start = tabPage*10;
		int end = Math.min(CreativeTabs.CREATIVE_TAB_ARRAY.length, (tabPage+1)*10+2);
		if(tabPage!=0)
		{
			start += 2;
		}

		CreativeTabs[] var7 = Arrays.copyOfRange(CreativeTabs.CREATIVE_TAB_ARRAY, start, end);
		int j = var7.length;

		int k;
		for(k = 0; k < j; ++k)
		{
			CreativeTabs creativetabs1 = var7[k];
			if(creativetabs1!=null&&creativetabs1.getTabIndex()!=selectedTabIndex)
			{
				this.drawTab(creativetabs1);
			}
		}

		if(tabPage!=0)
		{
			if(creativetabs!=CreativeTabs.SEARCH)
			{
				this.drawTab(CreativeTabs.SEARCH);
			}

			if(creativetabs!=CreativeTabs.INVENTORY)
			{
				this.drawTab(CreativeTabs.INVENTORY);

			}
		}

		if(creativetabs instanceof IICreativeTab||creativetabs==IIContent.II_CREATIVE_TAB)
		{
			drawSubTabs((IICreativeTab)creativetabs);
			IICreativeSubTab tab = IICreativeSubTab.CREATIVE_SUB_TABS[selectedIITabIndex];
			this.mc.getTextureManager().bindTexture(tab.getBackgroundImage());
		}
		else
		{
			this.mc.getTextureManager().bindTexture(creativetabs.getBackgroundImage());
		}

		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		this.searchField.drawTextBox();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int i = this.guiLeft+175;
		j = this.guiTop+18;
		k = j+112;
		if(creativetabs.shouldHidePlayerInventory())
		{
			this.mc.getTextureManager().bindTexture(CREATIVE_INVENTORY_TABS);
			this.drawTexturedModalRect(i, j+(int)((float)(k-j-17)*this.currentScroll), 232+(this.needsScrollBars()?0: 12), 0, 12, 15);
		}

		if(creativetabs!=null&&creativetabs.getTabPage()==tabPage||creativetabs==CreativeTabs.SEARCH||creativetabs==CreativeTabs.INVENTORY)
		{
			this.drawTab(creativetabs);
			if(creativetabs==CreativeTabs.INVENTORY)
			{
				GuiInventory.drawEntityOnScreen(this.guiLeft+88, this.guiTop+45, 20, (float)(this.guiLeft+88-mouseX), (float)(this.guiTop+45-30-mouseY), this.mc.player);
			}

		}
	}

	protected boolean isMouseOverTab(CreativeTabs tab, int mouseX, int mouseY)
	{
		if(tab.getTabPage()!=tabPage&&tab!=CreativeTabs.SEARCH&&tab!=CreativeTabs.INVENTORY)
		{
			return false;
		}
		else
		{
			int i = tab.getTabColumn();
			int j = 28*i;
			int k = 0;
			if(tab.isAlignedRight())
			{
				j = this.xSize-28*(6-i)+2;
			}
			else if(i > 0)
			{
				j += i;
			}

			if(tab.isTabInFirstRow())
			{
				k -= 32;
			}
			else
			{
				k += this.ySize;
			}

			return mouseX >= j&&mouseX <= j+28&&mouseY >= k&&mouseY <= k+32;
		}
	}

	protected boolean isMouseOverSubTab(IICreativeSubTab tab, int mouseX, int mouseY)
	{
		if(CreativeTabs.CREATIVE_TAB_ARRAY[getSelectedTabIndex()].getTabPage()!=tabPage&&CreativeTabs.CREATIVE_TAB_ARRAY[getSelectedTabIndex()]!=IIContent.II_CREATIVE_TAB)
			return false;

		// x, y - staring positions. width, height - size of sub tab
		int y = this.guiTop+12, x = this.guiLeft-28, width = 28, height = 24;

		// This is a very bad method of doing it since we are hardcoding the positions of the tabs but for now we will go with that ~GabrielV
		int[][] areas = {
				// First sub tab
				new int[]{x, y},
				new int[]{x+width, y+height},
				// Second sub tab
				new int[]{x, y+height},
				new int[]{x+width, y+height+24},
				// Third sub tab
				new int[]{x, y+height+24},
				new int[]{x+width, y+height+24*2},
				// Fourth sub tab
				new int[]{x, y+height+24*2},
				new int[]{x+width, y+height+24*3},
				// Fifth sub tab
				new int[]{x, y+height+24*3},
				new int[]{x+width, y+height+24*4}
		};

		int[] min = new int[0], max = new int[0];
		if(tab==IICreativeSubTab.ELECTRONICS)
		{
			min = areas[0];
			max = areas[1];
		}
		else if(tab==IICreativeSubTab.LOGISTICS)
		{
			min = areas[2];
			max = areas[3];
		}
		else if(tab==IICreativeSubTab.WARFARE)
		{
			min = areas[4];
			max = areas[5];
		}
		else if(tab==IICreativeSubTab.INTELLIGENCE)
		{
			min = areas[6];
			max = areas[7];
		}
		else
		{ // Resources
			min = areas[8];
			max = areas[9];
		}

		return (mouseX > min[0]&&mouseX < max[0]&&mouseY > min[1]&&mouseY < max[1]);
	}

	protected boolean renderCreativeInventorySubTabText(IICreativeSubTab tab, int mouseX, int mouseY)
	{
		if(this.isMouseOverSubTab(tab, mouseX, mouseY)&&CreativeTabs.CREATIVE_TAB_ARRAY[getSelectedTabIndex()]==IIContent.II_CREATIVE_TAB)
		{
			this.drawHoveringText(tab.tabLabel, mouseX, mouseY);
			return true;
		}
		else
		{
			return false;
		}
	}

	protected boolean renderCreativeInventoryHoveringText(CreativeTabs tab, int mouseX, int mouseY)
	{
		int i = tab.getTabColumn();
		int j = 28*i;
		int k = 0;
		if(tab.isAlignedRight())
		{
			j = this.xSize-28*(6-i)+2;
		}
		else if(i > 0)
		{
			j += i;
		}

		if(tab.isTabInFirstRow())
		{
			k -= 32;
		}
		else
		{
			k += this.ySize;
		}

		if(this.isPointInRegion(j+3, k+3, 23, 27, mouseX, mouseY))
		{
			this.drawHoveringText(I18n.format(tab.getTranslatedTabLabel()), mouseX, mouseY);
			return true;
		}
		else
		{
			return false;
		}
	}

	private void drawSubTabs(IICreativeTab tab)
	{
		int y = this.guiTop+12, x = this.guiLeft-28;
		for(IICreativeSubTab subTab : IICreativeSubTab.CREATIVE_SUB_TABS)
		{
			this.mc.getTextureManager().bindTexture(IICreativeSubTab.SUB_TAB_TEXTURE);
			GlStateManager.disableLighting();
			GlStateManager.color(1.0F, 1.0F, 1.0F);
			GlStateManager.enableBlend();
			if (subTab.tabIndex==selectedIITabIndex)
				this.drawTexturedModalRect(x, y, (int)subTab.getSelectedTabUV().x, (int)subTab.getSelectedTabUV().y, 28, 24);
			else
				this.drawTexturedModalRect(x, y, (int)subTab.getTabUV().x, (int)subTab.getTabUV().y, 28, 24);
			this.drawTexturedModalRect(x, y, (int)subTab.getIconUV().x, (int)subTab.getIconUV().y, 28, 24);
			y += 24;
		}
	}

	protected void drawTab(CreativeTabs tab)
	{
		if(tab==IIContent.II_CREATIVE_TAB)
		{
			IICreativeSubTab subTab = IICreativeSubTab.CREATIVE_SUB_TABS[Math.max(0, selectedIITabIndex)];
			this.mc.getTextureManager().bindTexture(subTab.getTabImage());
		}
		else
			this.mc.getTextureManager().bindTexture(CREATIVE_INVENTORY_TABS);
		boolean flag = tab.getTabIndex()==selectedTabIndex;
		boolean flag1 = tab.isTabInFirstRow();
		int i = tab.getTabColumn();
		int j = i*28;
		int k = 0;
		int l = this.guiLeft+28*i;
		int i1 = this.guiTop;
		if(flag)
		{
			k += 32;
		}

		if(tab.isAlignedRight())
		{
			l = this.guiLeft+this.xSize-28*(6-i);
		}
		else if(i > 0)
		{
			l += i;
		}

		if(flag1)
		{
			i1 -= 28;
		}
		else
		{
			k += 64;
			i1 += this.ySize-4;
		}

		GlStateManager.disableLighting();
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		GlStateManager.enableBlend();
		this.drawTexturedModalRect(l, i1, j, k, 28, 32);
		this.zLevel = 100.0F;
		this.itemRender.zLevel = 100.0F;
		l += 6;
		i1 = i1+8+(flag1?1: -1);
		GlStateManager.enableLighting();
		GlStateManager.enableRescaleNormal();
		ItemStack itemstack = tab.getIconItemStack();
		this.itemRender.renderItemAndEffectIntoGUI(itemstack, l, i1);
		this.itemRender.renderItemOverlays(this.fontRenderer, itemstack, l, i1);
		GlStateManager.disableLighting();
		this.itemRender.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}

	protected void actionPerformed(GuiButton button) throws IOException
	{
		if(button.id==1)
		{
			this.mc.displayGuiScreen(new GuiStats(this, this.mc.player.getStatFileWriter()));
		}

		if(button.id==101)
		{
			tabPage = Math.max(tabPage-1, 0);
		}
		else if(button.id==102)
		{
			tabPage = Math.min(tabPage+1, this.maxPages);
		}

	}

	public int getSelectedTabIndex()
	{
		return selectedTabIndex;
	}

	public static void handleHotbarSnapshots(Minecraft p_192044_0_, int p_192044_1_, boolean p_192044_2_, boolean p_192044_3_)
	{
		EntityPlayerSP entityplayersp = p_192044_0_.player;
		CreativeSettings creativesettings = p_192044_0_.creativeSettings;
		HotbarSnapshot hotbarsnapshot = creativesettings.getHotbarSnapshot(p_192044_1_);
		int j;
		if(p_192044_2_)
		{
			for(j = 0; j < InventoryPlayer.getHotbarSize(); ++j)
			{
				ItemStack itemstack = ((ItemStack)hotbarsnapshot.get(j)).copy();
				entityplayersp.inventory.setInventorySlotContents(j, itemstack);
				p_192044_0_.playerController.sendSlotPacket(itemstack, 36+j);
			}

			entityplayersp.inventoryContainer.detectAndSendChanges();
		}
		else if(p_192044_3_)
		{
			for(j = 0; j < InventoryPlayer.getHotbarSize(); ++j)
			{
				hotbarsnapshot.set(j, entityplayersp.inventory.getStackInSlot(j).copy());
			}

			String s = GameSettings.getKeyDisplayString(p_192044_0_.gameSettings.keyBindsHotbar[p_192044_1_].getKeyCode());
			String s1 = GameSettings.getKeyDisplayString(p_192044_0_.gameSettings.keyBindLoadToolbar.getKeyCode());
			p_192044_0_.ingameGUI.setOverlayMessage(new TextComponentTranslation("inventory.hotbarSaved", new Object[]{s1, s}), false);
			creativesettings.write();
		}

	}

	static
	{
		selectedTabIndex = CreativeTabs.BUILDING_BLOCKS.getTabIndex();
		selectedIITabIndex = IICreativeSubTab.ELECTRONICS.tabIndex;
		tabPage = 0;
	}

	@SideOnly(Side.CLIENT)
	static class IILockedSlot extends Slot
	{
		public IILockedSlot(IInventory p_i47453_1_, int p_i47453_2_, int p_i47453_3_, int p_i47453_4_)
		{
			super(p_i47453_1_, p_i47453_2_, p_i47453_3_, p_i47453_4_);
		}

		public boolean canTakeStack(EntityPlayer playerIn)
		{
			if(super.canTakeStack(playerIn)&&this.getHasStack())
			{
				return this.getStack().getSubCompound("CustomCreativeLock")==null;
			}
			else
			{
				return !this.getHasStack();
			}
		}
	}

	@SideOnly(Side.CLIENT)
	class IICreativeSlot extends Slot
	{
		private final Slot slot;

		public IICreativeSlot(Slot p_i46313_2_, int index)
		{
			super(p_i46313_2_.inventory, index, 0, 0);
			this.slot = p_i46313_2_;
		}

		public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
		{
			this.slot.onTake(thePlayer, stack);
			return stack;
		}

		public boolean isItemValid(ItemStack stack)
		{
			return this.slot.isItemValid(stack);
		}

		public ItemStack getStack()
		{
			return this.slot.getStack();
		}

		public boolean getHasStack()
		{
			return this.slot.getHasStack();
		}

		public void putStack(ItemStack stack)
		{
			this.slot.putStack(stack);
		}

		public void onSlotChanged()
		{
			this.slot.onSlotChanged();
		}

		public int getSlotStackLimit()
		{
			return this.slot.getSlotStackLimit();
		}

		public int getItemStackLimit(ItemStack stack)
		{
			return this.slot.getItemStackLimit(stack);
		}

		@Nullable
		public String getSlotTexture()
		{
			return this.slot.getSlotTexture();
		}

		public ItemStack decrStackSize(int amount)
		{
			return this.slot.decrStackSize(amount);
		}

		public boolean isHere(IInventory inv, int slotIn)
		{
			return this.slot.isHere(inv, slotIn);
		}

		public boolean isEnabled()
		{
			return this.slot.isEnabled();
		}

		public boolean canTakeStack(EntityPlayer playerIn)
		{
			return this.slot.canTakeStack(playerIn);
		}

		public ResourceLocation getBackgroundLocation()
		{
			return this.slot.getBackgroundLocation();
		}

		public void setBackgroundLocation(ResourceLocation texture)
		{
			this.slot.setBackgroundLocation(texture);
		}

		public void setBackgroundName(@Nullable String name)
		{
			this.slot.setBackgroundName(name);
		}

		@Nullable
		public TextureAtlasSprite getBackgroundSprite()
		{
			return this.slot.getBackgroundSprite();
		}

		public int getSlotIndex()
		{
			return this.slot.getSlotIndex();
		}

		public boolean isSameInventory(Slot other)
		{
			return this.slot.isSameInventory(other);
		}
	}

	@SideOnly(Side.CLIENT)
	public static class IIContainerCreative extends Container
	{
		public NonNullList<ItemStack> itemList = NonNullList.create();

		public IIContainerCreative(EntityPlayer player)
		{
			InventoryPlayer inventoryplayer = player.inventory;

			int k;
			for(k = 0; k < 5; ++k)
			{
				for(int j = 0; j < 9; ++j)
				{
					this.addSlotToContainer(new IILockedSlot(IIGuiContainerCreative.basicInventory, k*9+j, 9+j*18, 18+k*18));
				}
			}

			for(k = 0; k < 9; ++k)
			{
				this.addSlotToContainer(new Slot(inventoryplayer, k, 9+k*18, 112));
			}

			this.scrollTo(0.0F);
		}

		public boolean canInteractWith(EntityPlayer playerIn)
		{
			return true;
		}

		public void scrollTo(float pos)
		{
			int i = (this.itemList.size()+9-1)/9-5;
			int j = (int)((double)(pos*(float)i)+0.5);
			if(j < 0)
			{
				j = 0;
			}

			for(int k = 0; k < 5; ++k)
			{
				for(int l = 0; l < 9; ++l)
				{
					int i1 = l+(k+j)*9;
					if(i1 >= 0&&i1 < this.itemList.size())
					{
						IIGuiContainerCreative.basicInventory.setInventorySlotContents(l+k*9, (ItemStack)this.itemList.get(i1));
					}
					else
					{
						IIGuiContainerCreative.basicInventory.setInventorySlotContents(l+k*9, ItemStack.EMPTY);
					}
				}
			}

		}

		public boolean canScroll()
		{
			return this.itemList.size() > 45;
		}

		public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
		{
			if(index >= this.inventorySlots.size()-9&&index < this.inventorySlots.size())
			{
				Slot slot = (Slot)this.inventorySlots.get(index);
				if(slot!=null&&slot.getHasStack())
				{
					slot.putStack(ItemStack.EMPTY);
				}
			}

			return ItemStack.EMPTY;
		}

		public boolean canMergeSlot(ItemStack stack, Slot slotIn)
		{
			return slotIn.yPos > 90;
		}

		public boolean canDragIntoSlot(Slot slotIn)
		{
			return slotIn.inventory instanceof InventoryPlayer||slotIn.yPos > 90&&slotIn.xPos <= 162;
		}
	}
}
