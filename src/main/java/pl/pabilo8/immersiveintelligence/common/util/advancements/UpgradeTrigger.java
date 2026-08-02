package pl.pabilo8.immersiveintelligence.common.util.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.common.util.advancements.UpgradeTrigger.UpgradeCriterionInstance;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Advancement criterion fired after a machine upgrade finishes installing.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 12.07.2026
 */
public class UpgradeTrigger implements ICriterionTrigger<UpgradeCriterionInstance>
{
	private static final ResourceLocation ID = new ResourceLocation(ImmersiveIntelligence.MODID, "upgrade_installed");
	public static final UpgradeTrigger INSTANCE = CriteriaTriggers.register(new UpgradeTrigger());

	private final Map<PlayerAdvancements, UpgradeListeners> listeners = Maps.newHashMap();

	private UpgradeTrigger()
	{

	}

	@Nonnull
	@Override
	public ResourceLocation getId()
	{
		return ID;
	}

	@Override
	public void addListener(@Nonnull PlayerAdvancements playerAdvancements, @Nonnull ICriterionTrigger.Listener<UpgradeCriterionInstance> listener)
	{
		UpgradeListeners listeners = this.listeners.get(playerAdvancements);
		if(listeners==null)
		{
			listeners = new UpgradeListeners(playerAdvancements);
			this.listeners.put(playerAdvancements, listeners);
		}
		listeners.add(listener);
	}

	@Override
	public void removeListener(@Nonnull PlayerAdvancements playerAdvancements, @Nonnull ICriterionTrigger.Listener<UpgradeCriterionInstance> listener)
	{
		UpgradeListeners listeners = this.listeners.get(playerAdvancements);
		if(listeners!=null)
		{
			listeners.remove(listener);
			if(listeners.isEmpty())
				this.listeners.remove(playerAdvancements);
		}
	}

	@Override
	public void removeAllListeners(@Nonnull PlayerAdvancements playerAdvancements)
	{
		this.listeners.remove(playerAdvancements);
	}

	@Nonnull
	@Override
	public UpgradeCriterionInstance deserializeInstance(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context)
	{
		ResourceLocation upgrade = new ResourceLocation(JsonUtils.getString(json, "upgrade"));
		ItemPredicate item = ItemPredicate.deserialize(json.get("item"));
		return new UpgradeCriterionInstance(upgrade, item);
	}

	/**
	 * Fires the criterion for a completed upgrade installation.
	 *
	 * @param upgrade installed upgrade
	 * @param player  player who completed the installation
	 * @param wrench  wrench used for the final installation action
	 */
	public static void trigger(Upgrade upgrade, EntityPlayer player, ItemStack wrench)
	{
		if(upgrade==null||!(player instanceof EntityPlayerMP))
			return;

		UpgradeListeners listeners = INSTANCE.listeners.get(((EntityPlayerMP)player).getAdvancements());
		if(listeners!=null)
			listeners.trigger(upgrade, wrench);
	}

	public static class UpgradeCriterionInstance extends AbstractCriterionInstance
	{
		private final ResourceLocation upgrade;
		private final ItemPredicate item;

		public UpgradeCriterionInstance(ResourceLocation upgrade, ItemPredicate item)
		{
			super(ID);
			this.upgrade = upgrade;
			this.item = item;
		}

		public boolean test(Upgrade upgrade, ItemStack wrench)
		{
			return this.upgrade.equals(upgrade.getId())&&this.item.test(wrench);
		}
	}

	static class UpgradeListeners
	{
		private final PlayerAdvancements playerAdvancements;
		private final Set<ICriterionTrigger.Listener<UpgradeCriterionInstance>> listeners = Sets.newHashSet();

		UpgradeListeners(PlayerAdvancements playerAdvancements)
		{
			this.playerAdvancements = playerAdvancements;
		}

		boolean isEmpty()
		{
			return listeners.isEmpty();
		}

		void add(ICriterionTrigger.Listener<UpgradeCriterionInstance> listener)
		{
			listeners.add(listener);
		}

		void remove(ICriterionTrigger.Listener<UpgradeCriterionInstance> listener)
		{
			listeners.remove(listener);
		}

		void trigger(Upgrade upgrade, ItemStack wrench)
		{
			List<ICriterionTrigger.Listener<UpgradeCriterionInstance>> matched = null;
			for(ICriterionTrigger.Listener<UpgradeCriterionInstance> listener : listeners)
				if(listener.getCriterionInstance().test(upgrade, wrench))
				{
					if(matched==null)
						matched = Lists.newArrayList();
					matched.add(listener);
				}

			if(matched!=null)
				for(ICriterionTrigger.Listener<UpgradeCriterionInstance> listener : matched)
					listener.grantCriterion(playerAdvancements);
		}
	}
}
