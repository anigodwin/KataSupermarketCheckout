package com.itv;


public interface PricingRule {

	Item getItem();

	Unit apply(Long value);

	long weight();

}
