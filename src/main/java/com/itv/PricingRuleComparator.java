package com.itv;

import java.util.Comparator;

public class PricingRuleComparator implements Comparator<PricingRule> {

	@Override
	public int compare(final PricingRule rule0, final PricingRule rule1) {
		return rule0.weight() < rule1.weight() ? -1 : rule0.weight() == rule1.weight() ? 0 : 1;
	}

}
