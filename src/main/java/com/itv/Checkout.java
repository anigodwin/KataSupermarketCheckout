package com.itv;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class Checkout {

	private final Set<PricingRule> pricingRules;
	private final Map<Item, Long> basket;
	
	
	private Checkout(final Set<PricingRule> pricingRules) {
		this.pricingRules = pricingRules;
		this.basket = new HashMap<Item, Long>();
	}

	public static Checkout newRules(final Set<PricingRule> pricingRules) {
		return new Checkout(pricingRules);
	}

	public Checkout scan(final Item item) {
		basket.compute(item, (k, v) -> (v == null) ? 1 : ++v);
		return this;
	}

	public Receipt totalPrice() {
		long total = 0;
		long count = 0;
		final Map<Item, Long>unpricedItems = new HashMap<>();
		for (Entry<Item, Long> entry : basket.entrySet()) {
			Long amount = entry.getValue();
			count += amount;
			List<PricingRule> relevantRules = selectRelevantRules(pricingRules, entry.getKey());
			for (PricingRule rule : relevantRules) {
				Unit unit = rule.apply(amount);
				total += unit.price();
				amount = unit.remainder();
			}
			if (amount > 0) {
				count -= amount;
				unpricedItems.put(entry.getKey(), amount);
			}
		}
		return new Receipt(total, unpricedItems, count);
	}

	private List<PricingRule> selectRelevantRules(Set<PricingRule> pricingRules, Item item) {
		return pricingRules.stream()
			.filter(r -> item.equals(r.getItem()))
			.sorted(Collections.reverseOrder(new PricingRuleComparator()))
			.collect(Collectors.toList());
	}

}
