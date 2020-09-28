package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorAttachable;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ISoundTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.inventory.MultiFluidTank;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.AmmunitionFactory;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCasingType;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletComponent;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCoreType;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeString;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBullet;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class TileEntityAmmunitionFactory extends TileEntityMultiblockMetal<TileEntityAmmunitionFactory, IMultiblockRecipe> implements IDataDevice, IAdvancedCollisionBounds, IAdvancedSelectionBounds, IGuiTile, IConveyorAttachable, ISoundTile {
    public boolean active = false;

    public MultiFluidTank[] tanks = new MultiFluidTank[]{new MultiFluidTank(8000)};
    public NonNullList<ItemStack> inventory = NonNullList.withSize(8, ItemStack.EMPTY);
    public int ingredientCount1 = 0, ingredientCount2 = 0, gunpowderCount = 0;
    //gunpowder - > casing -> paint
    //core ->
    public int coreProgress = 0, gunpowderProgress = 0, casingProgress = 0, paintProgress = 0;
    public String ingredient1 = "", ingredient2 = "", name = "";
    public NBTTagCompound ingredient1NBT, ingredient2NBT;
    public float proportion = 1f;
    public int paintColour = 0xffffff;
    public int conveyorGunpowderProgress = 0, conveyorCasingProgress = 0, conveyorCoreProgress = 0, conveyorPaintProgress = 0, conveyorOutputProgress = 0;

    public List<String> plannedCoreList = new ArrayList<String>();
    public NonNullList<ItemStack> gunpowderQueue = NonNullList.withSize(2, ItemStack.EMPTY);
    public NonNullList<ItemStack> coreQueue = NonNullList.withSize(2, ItemStack.EMPTY);
    public NonNullList<ItemStack> casingQueue = NonNullList.withSize(3, ItemStack.EMPTY);
    public NonNullList<ItemStack> paintQueue = NonNullList.withSize(2, ItemStack.EMPTY);
    public NonNullList<ItemStack> outputQueue = NonNullList.withSize(1, ItemStack.EMPTY);

    IItemHandler component1Handler = new IEInventoryHandler(1, this, 2, true, false);
    IItemHandler gunpowderHandler = new IEInventoryHandler(1, this, 3, true, false);
    IItemHandler component2Handler = new IEInventoryHandler(1, this, 4, true, false);
    IItemHandler coreHandler = new IEInventoryHandler(1, this, 5, true, false);
    IItemHandler casingHandler = new IEInventoryHandler(1, this, 7, true, false);


    public TileEntityAmmunitionFactory() {
        super(MultiblockAmmunitionFactory.instance, new int[]{3, 5, 5}, AmmunitionFactory.energyCapacity, false);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.readCustomNBT(nbt, descPacket);
        if (!isDummy()) {
            if (!descPacket) {
                inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 8);
                gunpowderQueue = Utils.readInventory(nbt.getTagList("gunpowderQueue", 10), 2);
                coreQueue = Utils.readInventory(nbt.getTagList("coreQueue", 10), 2);
                casingQueue = Utils.readInventory(nbt.getTagList("casingQueue", 10), 3);
                paintQueue = Utils.readInventory(nbt.getTagList("paintQueue", 10), 2);
                outputQueue = Utils.readInventory(nbt.getTagList("outputQueue", 10), 1);
            }
            tanks[0].readFromNBT(nbt.getCompoundTag("tank"));
            active = nbt.getBoolean("active");

            ingredientCount1 = nbt.getInteger("ingredientCount1");
            ingredientCount2 = nbt.getInteger("ingredientCount2");
            gunpowderCount = nbt.getInteger("gunpowderCount");

            coreProgress = nbt.getInteger("coreProgress");
            casingProgress = nbt.getInteger("casingProgress");
            gunpowderProgress = nbt.getInteger("gunpowderProgress");
            paintProgress = nbt.getInteger("paintProgress");

            paintColour = nbt.getInteger("paintColour");
            proportion = nbt.getFloat("proportion");
            conveyorCasingProgress = nbt.getInteger("conveyorProgressCasing");
            conveyorCoreProgress = nbt.getInteger("conveyorCoreProgress");
            conveyorOutputProgress = nbt.getInteger("conveyorOutputProgress");
            conveyorPaintProgress = nbt.getInteger("conveyorPaintProgress");
            conveyorGunpowderProgress = nbt.getInteger("conveyorGunpowderProgress");

            plannedCoreList.clear();
            for (NBTBase s : nbt.getTagList("plannedCoreList", 10).tagList) {
                if (s instanceof NBTTagString) {
                    plannedCoreList.add(((NBTTagString) s).getString());
                }
            }

            ingredient1 = nbt.getString("ingredient1");
            if (nbt.hasKey("ingredient1NBT"))
                ingredient1NBT = nbt.getCompoundTag("ingredient1NBT");
            ingredient2 = nbt.getString("ingredient2");
            if (nbt.hasKey("ingredient2NBT"))
                ingredient2NBT = nbt.getCompoundTag("ingredient2NBT");
            name = nbt.getString("name");
        }
    }

    @Override
    public void receiveMessageFromClient(NBTTagCompound message) {
        super.receiveMessageFromClient(message);
    }

    @Override
    public void receiveMessageFromServer(NBTTagCompound message) {
        super.receiveMessageFromServer(message);
        if (message.hasKey("active"))
            this.active = message.getBoolean("active");
        if (message.hasKey("inventory"))
            inventory = Utils.readInventory(message.getTagList("inventory", 10), 8);

        if (message.hasKey("gunpowderQueue")) {
            gunpowderQueue = Utils.readInventory(message.getTagList("gunpowderQueue", 10), 2);
        }
        if (message.hasKey("coreQueue"))
            coreQueue = Utils.readInventory(message.getTagList("coreQueue", 10), 2);
        if (message.hasKey("casingQueue"))
            casingQueue = Utils.readInventory(message.getTagList("casingQueue", 10), 3);
        if (message.hasKey("paintQueue"))
            paintQueue = Utils.readInventory(message.getTagList("paintQueue", 10), 2);
        if (message.hasKey("outputQueue"))
            outputQueue = Utils.readInventory(message.getTagList("outputQueue", 10), 1);

        if (message.hasKey("ingredientCount1"))
            ingredientCount1 = message.getInteger("ingredientCount1");
        if (message.hasKey("ingredientCount2"))
            ingredientCount2 = message.getInteger("ingredientCount2");
        if (message.hasKey("gunpowderCount"))
            gunpowderCount = message.getInteger("gunpowderCount");
        if (message.hasKey("paintColour"))
            paintColour = message.getInteger("paintColour");
        if (message.hasKey("proportion"))
            proportion = message.getFloat("proportion");

        if (message.hasKey("ingredient1"))
            ingredient1 = message.getString("ingredient1");
        if (message.hasKey("ingredient1NBT"))
            ingredient1NBT = message.getCompoundTag("ingredient1NBT");
        if (message.hasKey("ingredient2"))
            ingredient2 = message.getString("ingredient2");
        if (message.hasKey("ingredient2NBT"))
            ingredient2NBT = message.getCompoundTag("ingredient2NBT");
        if (message.hasKey("name"))
            name = message.getString("name");

        if (message.hasKey("coreProgress"))
            coreProgress = message.getInteger("coreProgress");
        if (message.hasKey("casingProgress"))
            casingProgress = message.getInteger("casingProgress");
        if (message.hasKey("gunpowderProgress"))
            gunpowderProgress = message.getInteger("gunpowderProgress");
        if (message.hasKey("paintProgress"))
            paintProgress = message.getInteger("paintProgress");

        if (message.hasKey("conveyorCasingProgress"))
            conveyorCasingProgress = message.getInteger("conveyorProgressCasing");
        if (message.hasKey("conveyorCoreProgress"))
            conveyorCoreProgress = message.getInteger("conveyorCoreProgress");
        if (message.hasKey("conveyorOutputProgress"))
            conveyorOutputProgress = message.getInteger("conveyorOutputProgress");
        if (message.hasKey("conveyorPaintProgress"))
            conveyorPaintProgress = message.getInteger("conveyorPaintProgress");
        if (message.hasKey("conveyorGunpowderProgress"))
            conveyorGunpowderProgress = message.getInteger("conveyorGunpowderProgress");

        plannedCoreList.clear();
        for (NBTBase s : message.getTagList("plannedCoreList", 10).tagList) {
            if (s instanceof NBTTagString) {
                plannedCoreList.add(((NBTTagString) s).getString());
            }
        }
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.writeCustomNBT(nbt, descPacket);
        if (!isDummy()) {
            if (!descPacket) {
                nbt.setTag("inventory", Utils.writeInventory(inventory));
            }
            nbt.setTag("gunpowderQueue", Utils.writeInventory(gunpowderQueue));
            nbt.setTag("coreQueue", Utils.writeInventory(coreQueue));
            nbt.setTag("casingQueue", Utils.writeInventory(casingQueue));
            nbt.setTag("paintQueue", Utils.writeInventory(paintQueue));
            nbt.setTag("outputQueue", Utils.writeInventory(outputQueue));

            nbt.setTag("tank", tanks[0].writeToNBT(new NBTTagCompound()));
            nbt.setBoolean("active", active);

            nbt.setString("ingredient1", ingredient1);
            if (ingredient1NBT != null)
                nbt.setTag("ingredient1NBT", ingredient1NBT);
            nbt.setString("ingredient2", ingredient2);
            if (ingredient2NBT != null)
                nbt.setTag("ingredient2NBT", ingredient2NBT);
            nbt.setString("name", name);

            nbt.setInteger("ingredientCount1", ingredientCount1);
            nbt.setInteger("ingredientCount2", ingredientCount2);
            nbt.setInteger("gunpowderCount", gunpowderCount);

            nbt.setInteger("paintColour", paintColour);
            nbt.setFloat("proportion", proportion);

            nbt.setInteger("conveyorCasingProgress", conveyorCasingProgress);
            nbt.setInteger("conveyorCoreProgress", conveyorCoreProgress);
            nbt.setInteger("conveyorGunpowderProgress", conveyorGunpowderProgress);
            nbt.setInteger("conveyorOutputProgress", conveyorOutputProgress);
            nbt.setInteger("conveyorPaintProgress", conveyorPaintProgress);

            nbt.setInteger("paintProgress", paintProgress);
            nbt.setInteger("gunpowderProgress", gunpowderProgress);
            nbt.setInteger("casingProgress", casingProgress);
            nbt.setInteger("coreProgress", coreProgress);

            NBTTagList ls = new NBTTagList();
            for (String s : plannedCoreList)
                ls.appendTag(new NBTTagString(s));
            nbt.setTag("plannedCoreList", ls);
        }
    }

    @Override
    public void update() {
        super.update();
        NBTTagCompound tag = new NBTTagCompound();

        boolean update = productionProcess();

        if (world.isRemote || isDummy())
            return;


        if (inventory.get(0).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) && inventory.get(0).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).getTankProperties()[0].getContents() != null) {
            String fname = inventory.get(0).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).drain(1000, false).getFluid().getName();

            if (fname.equals("ink") || fname.equals("ink_cyan") || fname.equals("ink_magenta") || fname.equals("ink_yellow")) {
                int amount_prev = tanks[0].getFluidAmount();
                ItemStack emptyContainer = Utils.drainFluidContainer(tanks[0], inventory.get(0), inventory.get(1), null);
                if (amount_prev != tanks[0].getFluidAmount()) {
                    if (!inventory.get(1).isEmpty() && OreDictionary.itemMatches(inventory.get(1), emptyContainer, true))
                        inventory.get(1).grow(emptyContainer.getCount());
                    else if (inventory.get(1).isEmpty())
                        inventory.set(1, emptyContainer.copy());
                    inventory.get(0).shrink(1);
                    if (inventory.get(0).getCount() <= 0)
                        inventory.set(0, ItemStack.EMPTY);

                    update = true;
                }
            }
        }

        if (world.getTotalWorldTime() % 20 == 0) {
            if (ingredientCount1 <= 0) {
                ingredient1 = "";
                ingredient1NBT = null;
            }

            if (ingredientCount1 < AmmunitionFactory.componentCapacity) {
                if (ingredient1.equals("")) {
                    for (Map.Entry<String, IBulletComponent> b : BulletRegistry.INSTANCE.registeredComponents.entrySet()) {
                        if (b.getValue().getMaterial().matchesItemStackIgnoringSize(inventory.get(2))) {
                            int count = inventory.get(2).getCount();
                            int to_shrink = Math.max(Math.min(count, Math.min(AmmunitionFactory.componentCapacity - ingredientCount1, AmmunitionFactory.componentIntake)), 0);
                            inventory.get(2).shrink(to_shrink);
                            ingredient1 = b.getKey();
                            if (inventory.get(2).hasTagCompound())
                                ingredient1NBT = ItemNBTHelper.getTag(inventory.get(2));
                            ingredientCount1 += to_shrink;
                            update = true;
                            break;
                        }
                    }
                } else if (BulletRegistry.INSTANCE.getComponent(ingredient1).getMaterial().matchesItemStackIgnoringSize(inventory.get(2)) && (ingredient1NBT == null || ingredient1NBT.toString().equals(ItemNBTHelper.getTag(inventory.get(2)).toString()))) {
                    int count = inventory.get(2).getCount();
                    int to_shrink = Math.max(Math.min(count, Math.min(AmmunitionFactory.componentCapacity - ingredientCount1, AmmunitionFactory.componentIntake)), 0);
                    inventory.get(2).shrink(to_shrink);
                    ingredientCount1 += to_shrink;
                    update = true;
                }

            }

            if (ingredientCount2 <= 0) {
                ingredient2 = "";
                ingredient2NBT = null;
            }

            if (ingredientCount2 < AmmunitionFactory.componentCapacity) {
                if (ingredient2.equals("")) {
                    for (Map.Entry<String, IBulletComponent> b : BulletRegistry.INSTANCE.registeredComponents.entrySet()) {
                        if (b.getValue().getMaterial().matchesItemStackIgnoringSize(inventory.get(4))) {
                            int count = inventory.get(4).getCount();
                            int to_shrink = Math.max(Math.min(count, Math.min(AmmunitionFactory.componentCapacity - ingredientCount2, AmmunitionFactory.componentIntake)), 0);
                            inventory.get(4).shrink(to_shrink);
                            ingredient2 = b.getKey();
                            ingredientCount2 += to_shrink;
                            if (inventory.get(4).hasTagCompound())
                                ingredient2NBT = ItemNBTHelper.getTag(inventory.get(4));
                            update = true;
                            break;
                        }
                    }
                } else if (BulletRegistry.INSTANCE.getComponent(ingredient2).getMaterial().matchesItemStackIgnoringSize(inventory.get(4)) && (ingredient2NBT == null || ingredient2NBT.toString().equals(ItemNBTHelper.getTag(inventory.get(4)).toString()))) {
                    int count = inventory.get(4).getCount();
                    int to_shrink = Math.max(Math.min(count, Math.min(AmmunitionFactory.componentCapacity - ingredientCount2, AmmunitionFactory.componentIntake)), 0);
                    inventory.get(4).shrink(to_shrink);
                    ingredientCount2 += to_shrink;
                    update = true;
                }

            }

            if (gunpowderCount < AmmunitionFactory.componentCapacity) {
                if (Utils.compareToOreName(inventory.get(3), "gunpowder")) {
                    int count = inventory.get(3).getCount();
                    int to_shrink = Math.max(Math.min(count, Math.min(AmmunitionFactory.componentCapacity - gunpowderCount, AmmunitionFactory.componentIntake)), 0);
                    inventory.get(3).shrink(to_shrink);
                    gunpowderCount += to_shrink;
                    update = true;
                }
            }
        }

        if (update) {
            //this.markDirty();
            //this.markContainingBlockForUpdate(null);
            writeToNBT(tag);
            this.markDirty();
            this.markContainingBlockForUpdate(null);
            ImmersiveEngineering.packetHandler.sendToAllAround(new MessageTileSync(this, tag), new TargetPoint(this.world.provider.getDimension(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 32));
        }


    }

    public boolean productionProcess() {
        boolean shouldUpdate = false;

        //Input (Gunpowder)

        if (!world.isRemote && world.getTotalWorldTime() % 15 == 0) {
            check:
            if (gunpowderQueue.get(0).isEmpty() && !inventory.get(7).isEmpty() && inventory.get(7).getCount() == inventory.get(7).getMaxStackSize()) {
                ItemStack stack = inventory.get(7).copy();
                if (!(stack.getItem() instanceof IBulletCasingType)) {
                    Utils.dropStackAtPos(world, getBlockPosForPos(25).offset(facing.getOpposite()), stack, facing.getOpposite());
                    inventory.set(7, ItemStack.EMPTY);
                    break check;
                }
                plannedCoreList.add(((IBulletCasingType) stack.getItem()).getName());
                gunpowderQueue.set(0, stack.copy());
                inventory.set(7, ItemStack.EMPTY);
            }
        }

        //Output

        if (conveyorOutputProgress >= AmmunitionFactory.conveyorTime) {
            conveyorOutputProgress = 0;
            ItemStack output = outputQueue.get(0);
            TileEntity te = world.getTileEntity(getBlockPosForPos(25).offset(facing.getOpposite()));
            if (te != null && !world.isRemote) {
                output = Utils.insertStackIntoInventory(te, output, facing.getOpposite());
                outputQueue.set(0, output);
            }
            if (!output.isEmpty()) {
                if (!world.isRemote)
                    Utils.dropStackAtPos(world, getBlockPosForPos(25).offset(facing.getOpposite()), output, facing.getOpposite());
                outputQueue.set(0, ItemStack.EMPTY);
            }
            shouldUpdate = true;
        } else if (outputQueue.get(0) != ItemStack.EMPTY) {
            conveyorOutputProgress += 1;
        } else
            conveyorOutputProgress = 0;

        //Gunpowder

        if (!gunpowderQueue.get(1).isEmpty() && gunpowderCount >= ItemIIBullet.getCasing(gunpowderQueue.get(1)).getGunpowderNeeded()) {
            if (!world.isRemote && gunpowderProgress >= AmmunitionFactory.gunpowderTime) {
                if (casingQueue.get(0).isEmpty()) {
                    gunpowderCount -= ItemIIBullet.getCasing(gunpowderQueue.get(1)).getGunpowderNeeded();
                    ItemStack stack = gunpowderQueue.get(1).copy();
                    casingQueue.set(0, stack);
                    gunpowderQueue.set(1, ItemStack.EMPTY);
                    gunpowderProgress = 0;
                    shouldUpdate = true;
                }
            } else if (energyStorage.getEnergyStored() >= AmmunitionFactory.energyUsageGunpowder) {
                energyStorage.extractEnergy(AmmunitionFactory.energyUsageGunpowder, false);
                if (world.isRemote && world.getTotalWorldTime() % 4 == 0) {
                    BlockPos p = getBlockPosForPos(33).up();
                    ImmersiveEngineering.proxy.spawnRedstoneFX(world, p.getX() + 0.5f, p.getY() - 0.25, p.getZ() + 0.5f, 0f, -2, 0f, 3.5f, 0f, 0f, 0f);
                }
                gunpowderProgress += 1;
            }
        } else
            gunpowderProgress = 0;

        //Cores

        if (!plannedCoreList.isEmpty() && coreQueue.get(0).isEmpty()) {
            if (!inventory.get(5).isEmpty() && BulletRegistry.INSTANCE.getCasing(plannedCoreList.get(0)).getCoreMaterialNeeded() <= inventory.get(5).getCount()) {
                if (coreProgress < AmmunitionFactory.coreTime) {
                    if (energyStorage.getEnergyStored() >= AmmunitionFactory.energyUsageCore) {
                        energyStorage.extractEnergy(AmmunitionFactory.energyUsageCore, false);
                        coreProgress += 1;
                    }
                } else if (!world.isRemote) {
                    inventory.get(5).shrink(BulletRegistry.INSTANCE.getCasing(plannedCoreList.get(0)).getCoreMaterialNeeded());
                    String core_material = "";
                    for (IBulletCoreType t : BulletRegistry.INSTANCE.registeredBulletCores.values()) {
                        if (t.getMaterial().matchesItemStackIgnoringSize(inventory.get(5)))
                            core_material = t.getName();
                    }

                    float usage = BulletRegistry.INSTANCE.getCasing(plannedCoreList.get(0)).getComponentCapacity();

                    float stack1_usage = Math.max(0, Math.min(ingredientCount1 / usage * proportion * 2f, 1f));
                    float stack2_usage = Math.max(0, Math.min(ingredientCount2 / usage * (1f - proportion) * 2f, 1f));

                    ingredientCount1 -= Math.round(stack1_usage * 2f);
                    ingredientCount2 -= Math.round(stack2_usage * 2f);

                    ItemStack stack = new ItemIIBullet().getAmmoStack(BulletRegistry.INSTANCE.getCasing(plannedCoreList.get(0)).getStack(1).getMaxStackSize(), plannedCoreList.get(0), core_material, ingredient1, ingredient2, proportion, stack1_usage, stack2_usage);

                    if (ingredient1NBT != null)
                        ItemIIBullet.getFirstComponentNBT(stack).merge(ingredient1NBT);
                    if (ingredient2NBT != null)
                        ItemIIBullet.getSecondComponentNBT(stack).merge(ingredient2NBT);

                    coreQueue.set(0, stack);
                    plannedCoreList.remove(0);
                    coreProgress = 0;
                    shouldUpdate = true;
                }
            }
        }

        //Casings


        if (!casingQueue.get(2).isEmpty() && !coreQueue.get(1).isEmpty()) {
            if (!world.isRemote && casingProgress >= (AmmunitionFactory.casingTime * ((IBulletCasingType) casingQueue.get(2).getItem()).getSize() * casingQueue.get(2).getCount()) + AmmunitionFactory.conveyorTime) {
                if (paintQueue.get(0).isEmpty()) {
                    String casing_name = ((IBulletCasingType) casingQueue.get(2).getItem()).getName();
                    ItemStack stack = coreQueue.get(1).copy();
                    ItemNBTHelper.setString(stack, "casing", casing_name);
                    stack.setCount(casingQueue.get(2).getCount());
                    paintQueue.set(0, stack);

                    casingQueue.set(2, ItemStack.EMPTY);
                    coreQueue.set(1, ItemStack.EMPTY);

                    casingProgress = 0;
                    shouldUpdate = true;
                }
            } else if (energyStorage.getEnergyStored() >= AmmunitionFactory.energyUsageCasing) {
                energyStorage.extractEnergy(AmmunitionFactory.energyUsageCasing, false);
                casingProgress += 1;
            }
        } else
            casingProgress = 0;

        if (!paintQueue.get(1).isEmpty()) {
            if (!world.isRemote && paintProgress >= AmmunitionFactory.paintTime) {
                ItemStack stack = paintQueue.get(1).copy();

                Color col = new Color(paintColour);
                int[] amounts = pl.pabilo8.immersiveintelligence.api.Utils.rgbToCmyk(col.getRed(), col.getGreen(), col.getBlue());
                float c = 0, m = 0, y = 0, k = 0;
                Iterator<FluidStack> iter = tanks[0].fluids.iterator();

                while (iter.hasNext()) {
                    FluidStack fs = iter.next();
                    if (fs.getFluid().getName().equals("ink_cyan")) {
                        c += fs.amount;
                    } else if (fs.getFluid().getName().equals("ink_magenta")) {
                        m += fs.amount;

                    } else if (fs.getFluid().getName().equals("ink_yellow")) {
                        y += fs.amount;
                    } else if (fs.getFluid().getName().equals("ink")) {
                        k += fs.amount;
                    }
                }

                c = Math.min(c, AmmunitionFactory.paintUsage * (amounts[0] / 255f));
                m = Math.min(m, AmmunitionFactory.paintUsage * (amounts[1] / 255f));
                y = Math.min(y, AmmunitionFactory.paintUsage * (amounts[2] / 255f));
                k = Math.min(k, AmmunitionFactory.paintUsage * (amounts[3] / 255f));

                if (c > 0) {
                    c = tanks[0].drain(FluidRegistry.getFluidStack("ink_cyan", Math.round(c)), true).amount;
                } else
                    c = 0;
                if (m > 0) {
                    m = tanks[0].drain(FluidRegistry.getFluidStack("ink_magenta", Math.round(m)), true).amount;
                } else
                    m = 0;
                if (y > 0) {
                    y = tanks[0].drain(FluidRegistry.getFluidStack("ink_yellow", Math.round(y)), true).amount;
                } else
                    y = 0;
                if (k > 0) {
                    k = tanks[0].drain(FluidRegistry.getFluidStack("ink", Math.round(k)), true).amount;
                } else
                    k = 0;

                int[] rgb = pl.pabilo8.immersiveintelligence.api.Utils.cmykToRgb(Math.round(c / AmmunitionFactory.paintUsage * 255f), Math.round(m / AmmunitionFactory.paintUsage * 255f), Math.round(y / AmmunitionFactory.paintUsage * 255f), Math.round(k / AmmunitionFactory.paintUsage * 255f));

                ItemNBTHelper.setInt(stack, "colour", rgb(rgb[0], rgb[1], rgb[2]));
                if (!name.isEmpty()) {
                    stack.setStackDisplayName(name);
                }

                outputQueue.set(0, stack);
                paintQueue.set(1, ItemStack.EMPTY);


                paintProgress = 0;
                shouldUpdate = true;


            } else if (energyStorage.getEnergyStored() >= AmmunitionFactory.energyUsagePaint) {
                energyStorage.extractEnergy(AmmunitionFactory.energyUsagePaint, false);
                if (world.isRemote && world.getTotalWorldTime() % 4 == 0) {
                    BlockPos p = getBlockPosForPos(30).up();
                    float[] rbg = pl.pabilo8.immersiveintelligence.api.Utils.rgbIntToRGB(paintColour);
                    ImmersiveEngineering.proxy.spawnRedstoneFX(world, p.getX() + 0.5f, p.getY() - 0.45, p.getZ() + 0.5, 0f, -2, -2f, 2.5f, rbg[0], rbg[1], rbg[2]);
                    ImmersiveEngineering.proxy.spawnRedstoneFX(world, p.getX() + 0.25f, p.getY() - 0.65, p.getZ() + 0.25, 0f, -2, -2f, 2.5f, rbg[0], rbg[1], rbg[2]);
                    ImmersiveEngineering.proxy.spawnRedstoneFX(world, p.getX() + 0.25f, p.getY() - 0.65, p.getZ() + 0.75, 0f, -2, -2f, 2.5f, rbg[0], rbg[1], rbg[2]);
                    ImmersiveEngineering.proxy.spawnRedstoneFX(world, p.getX() + 0.75f, p.getY() - 0.65, p.getZ() + 0.25, 0f, -2, -2f, 2.5f, rbg[0], rbg[1], rbg[2]);
                    ImmersiveEngineering.proxy.spawnRedstoneFX(world, p.getX() + 0.75f, p.getY() - 0.65, p.getZ() + 0.75, 0f, -2, -2f, 2.5f, rbg[0], rbg[1], rbg[2]);
                }
                paintProgress += 1;
            }
        } else
            paintProgress = 0;

        //Gunpowder

        if (conveyorGunpowderProgress >= AmmunitionFactory.conveyorTime) {
            conveyorGunpowderProgress = 0;
            moveItemToConveyorSlot(gunpowderQueue, 0, 1);
            shouldUpdate = true;
        } else if (gunpowderQueue.get(0) != ItemStack.EMPTY && gunpowderQueue.get(1).isEmpty()) {
            conveyorGunpowderProgress += 1;
        } else
            conveyorGunpowderProgress = 0;

        //Casing

        if (conveyorCasingProgress >= AmmunitionFactory.conveyorTime) {
            conveyorCasingProgress = 0;
            moveItemToConveyorSlot(casingQueue, 1, 2);
            moveItemToConveyorSlot(casingQueue, 0, 1);
            shouldUpdate = true;
        } else if (casingQueue.get(2).isEmpty()) {
            conveyorCasingProgress += 1;
        } else
            conveyorCasingProgress = 0;

        if (conveyorCoreProgress >= AmmunitionFactory.conveyorTime) {
            conveyorCoreProgress = 0;
            moveItemToConveyorSlot(coreQueue, 0, 1);
            shouldUpdate = true;
        } else if (coreQueue.get(1).isEmpty()) {
            conveyorCoreProgress += 1;
        } else
            conveyorCoreProgress = 0;

        //ImmersiveIntelligence.logger.info(paintProgress);

        if (conveyorPaintProgress >= AmmunitionFactory.conveyorTime) {
            conveyorPaintProgress = 0;
            moveItemToConveyorSlot(paintQueue, 0, 1);
            shouldUpdate = true;
        } else if (paintQueue.get(1).isEmpty()) {
            conveyorPaintProgress += 1;
        } else
            conveyorPaintProgress = 0;

        return shouldUpdate;
    }

    /**
     * Server version of {@link MathHelper#rgb(int, int, int)}
     * @param rIn Red
     * @param gIn Green
     * @param bIn Blue
     * @return Returns a unified integer value.
     */
    private int rgb(int rIn, int gIn, int bIn) {
        int lvt_3_1_ = (rIn << 8) + gIn;
        lvt_3_1_ = (lvt_3_1_ << 8) + bIn;
        return lvt_3_1_;
    }

    @Override
    public float[] getBlockBounds() {
        return new float[]{0, 0, 0, 0, 0, 0};
    }

    @Override
    public int[] getEnergyPos() {
        return new int[]{74};
    }

    @Override
    public int[] getRedstonePos() {
        return new int[]{};
    }

    @Override
    public boolean isInWorldProcessingMachine() {
        return false;
    }

    @Override
    public void doProcessOutput(ItemStack output) {

    }

    @Override
    public void doProcessFluidOutput(FluidStack output) {
    }

    @Override
    public void onProcessFinish(MultiblockProcess<IMultiblockRecipe> process) {

    }

    @Override
    public int getMaxProcessPerTick() {
        return 1;
    }

    @Override
    public int getProcessQueueMaxLength() {
        return 1;
    }

    @Override
    public float getMinProcessDistance(MultiblockProcess<IMultiblockRecipe> process) {
        return 0;
    }

    @Override
    public NonNullList<ItemStack> getInventory() {
        return inventory;
    }

    @Override
    public boolean isStackValid(int slot, ItemStack stack) {
        if (slot == 0)
            return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
        else if (slot == 5) {
            for (IBulletCoreType t : BulletRegistry.INSTANCE.registeredBulletCores.values()) {
                if (t.getMaterial().matchesItemStackIgnoringSize(stack))
                    return true;
            }
            return false;
        } else
            return true;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public int[] getOutputSlots() {
        return new int[]{1};
    }

    @Override
    public int[] getOutputTanks() {
        return new int[0];
    }

    @Override
    public boolean additionalCanProcessCheck(MultiblockProcess<IMultiblockRecipe> process) {
        return false;
    }

    @Override
    public IFluidTank[] getInternalTanks() {
        return tanks;
    }

    @Override
    protected IFluidTank[] getAccessibleFluidTanks(EnumFacing side) {
        TileEntityAmmunitionFactory master = this.master();
        if (master != null) {
            if (pos == 27 && side.getAxis() == Axis.Y)
                return master.tanks;
        }
        return new FluidTank[0];
    }

    @Override
    protected boolean canFillTankFrom(int iTank, EnumFacing side, FluidStack resource) {
        String fname = resource.getFluid().getName();
        if (pos == 27 && side.getAxis() == Axis.Y && (fname.equals("ink") || fname.equals("ink_cyan") || fname.equals("ink_magenta") || fname.equals("ink_yellow"))) {
            TileEntityAmmunitionFactory master = this.master();
            return !(master == null || master.tanks[iTank].getFluidAmount() >= master.tanks[iTank].getCapacity());
        }
        return false;
    }

    @Override
    protected boolean canDrainTankFrom(int iTank, EnumFacing side) {
        return (side.getAxis() == Axis.Y && iTank == 0);
    }

    @Override
    public void doGraphicalUpdates(int slot) {
        this.markDirty();
    }

    @Override
    public IMultiblockRecipe findRecipeForInsertion(ItemStack inserting) {
        return null;
    }

    @Override
    protected IMultiblockRecipe readRecipeFromNBT(NBTTagCompound tag) {
        return null;
    }

    @Override
    public void onSend() {

    }

    @Override
    public void onReceive(DataPacket packet, EnumFacing side) {
        TileEntityAmmunitionFactory master = master();
        if (pos == 40 && master != null) {
            //Proportion in percents
            if (packet.getPacketVariable('p') instanceof DataPacketTypeInteger) {
                float o = (float) ((DataPacketTypeInteger) packet.getPacketVariable('p')).value;
                master.proportion = Math.min(Math.max(o / 100f, 0f), 1f);
            }

            if (packet.getPacketVariable('c') instanceof DataPacketTypeInteger) {
                master.paintColour = ((DataPacketTypeInteger) packet.getPacketVariable('c')).value;
            } else if (packet.getPacketVariable('c') instanceof DataPacketTypeString) {
                try {
                    master.paintColour = Integer.decode(packet.getPacketVariable('c').valueToString());
                } catch (NumberFormatException e) {
                    ImmersiveIntelligence.logger.info("Not a Number!");
                }

            }

            if (packet.getPacketVariable('n') instanceof DataPacketTypeString) {
                master.name = packet.getPacketVariable('n').valueToString();
            }

        }
    }

    @Override
    public List<AxisAlignedBB> getAdvancedSelectionBounds() {
        List list = new ArrayList<AxisAlignedBB>();

        list.add(new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));

        return list;
    }

    @Override
    public boolean isOverrideBox(AxisAlignedBB box, EntityPlayer player, RayTraceResult mop, ArrayList<AxisAlignedBB> list) {
        return false;
    }

    @Override
    public List<AxisAlignedBB> getAdvancedColisionBounds() {
        return getAdvancedSelectionBounds();
    }

    @Override
    public boolean canOpenGui() {
        return true;
    }

    @Override
    public int getGuiID() {
        return IIGuiList.GUI_AMMUNITION_FACTORY.ordinal();
    }

    @Nullable
    @Override
    public TileEntity getGuiMaster() {
        return master();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && (pos == 28 || pos == 29 || pos == 44 || pos == 48 || pos == 47))
            return master() != null;
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            TileEntityAmmunitionFactory master = master();
            if (master == null)
                return null;
            if (pos == 28)
                return (T) master.casingHandler;
            else if (pos == 29)
                return (T) master.gunpowderHandler;
            else if (pos == 44)
                return (T) master.coreHandler;
            else if (pos == 47)
                return (T) master.component1Handler;
            else if (pos == 48)
                return (T) master.component2Handler;


        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void onGuiOpened(EntityPlayer player, boolean clientside) {
        if (!clientside) {
            NBTTagCompound tag = new NBTTagCompound();
            ImmersiveEngineering.packetHandler.sendToAllAround(new MessageTileSync(this, tag), new TargetPoint(this.world.provider.getDimension(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 32));
        }
    }

    @Override
    public EnumFacing[] sigOutputDirections() {
        if (pos == 2)
            return new EnumFacing[]{mirrored ? facing.rotateYCCW() : facing.rotateY()};
        return new EnumFacing[0];
    }

    @Override
    public boolean shoudlPlaySound(String sound) {
        return active;
    }

    @Override
    public void onEntityCollision(World world, Entity entity) {

    }

    void moveItemToConveyorSlot(NonNullList<ItemStack> inv, int from, int to) {
        if (inv.get(to).isEmpty()) {
            ItemStack stack = inv.get(from).copy();
            inv.set(to, stack);
            inv.set(from, ItemStack.EMPTY);
        }
    }
}
