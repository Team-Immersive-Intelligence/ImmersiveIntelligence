package pl.pabilo8.immersiveintelligence.client.gui.deco;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import pl.pabilo8.immersiveintelligence.api.style.IStyleCustomizable;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.widget.DecoManualWidget;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.widget.DecoStyleWidget;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * Deco GUI backed by an Entity.
 * <p>
 * The concrete entity GUI supplies its container directly, because IIGUI currently has tile and item factories only.
 * Entity sync is intentionally left to concrete subclasses until a project-wide entity GUI packet exists.
 * </p>
 */
public abstract class DecoEntityGui<E extends Entity, C extends Container> extends DecoGui<E, C>
{
	protected final E entity;

	public DecoEntityGui(EntityPlayer player, E entity, IIGUI iigui)
	{
		//noinspection unchecked
		super(player, (C)iigui.containerFromEntity.apply(player, entity), entity, iigui);
		this.entity = entity;
	}

	@Override
	protected void onInitStandardAddons()
	{
		if(category==DecoGuiCategory.VEHICLE_ENTITY)
			addWidget(new DecoManualWidget());
		if(entity instanceof IStyleCustomizable)
			addWidget(new DecoStyleWidget(((IStyleCustomizable)entity)));
	}

	@Override
	protected boolean isStoredGuiDataValid(EasyNBT nbt)
	{
		if(!super.isStoredGuiDataValid(nbt)||entity==null)
			return false;
		final boolean[] valid = {false};
		nbt.checkSetString("entity", s -> valid[0] = getEntityGuiKey().equals(s));
		return valid[0];
	}

	@Override
	protected EasyNBT createGuiDataTag()
	{
		EasyNBT nbt = super.createGuiDataTag();
		if(entity!=null)
			nbt.withString("entity", getEntityGuiKey());
		return nbt;
	}

	protected String getEntityGuiKey()
	{
		return entity.dimension+":"+entity.getEntityId();
	}

	public E getEntity()
	{
		return entity;
	}
}
