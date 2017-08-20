package com.itv;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CheckoutTest {

	private static final Item A = new Item("A");
	private static final Item B = new Item("B");
	private static final Item C = new Item("C");
	private static final Item D = new Item("D");
	
	private Set<PricingRule> pricingRules;
	
	@Before
	public void setUp() throws Exception {
		pricingRules = new HashSet<PricingRule>() {{
			add(SinglePricingRule.ruleFor(A).atPrice(50));
			add(SinglePricingRule.ruleFor(B).atPrice(30));
			add(SinglePricingRule.ruleFor(C).atPrice(20));
			add(SinglePricingRule.ruleFor(D).atPrice(15));
			add(MultiPricingRule.ruleFor(A).buy(3).forPrice(130));
			add(MultiPricingRule.ruleFor(B).buy(2).forPrice(45));
		}};
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSinglePricedItems() throws Exception {
		Checkout checkout = Checkout.newRules(pricingRules)
			.scan(A)
			.scan(B);
			
		Receipt receipt = checkout.totalPrice();
		assertEquals(new Receipt(80, Collections.emptyMap(), 2), receipt);
	}
	
	@Test
	public void testDuplicateSinglePricedItem_willUseTheRuleWithLargerWeight() throws Exception {
		pricingRules = new HashSet<PricingRule>() {{
			add(SinglePricingRule.ruleFor(A).atPrice(50));
			add(SinglePricingRule.ruleFor(A).atPrice(60));
			add(SinglePricingRule.ruleFor(B).atPrice(30));
			add(SinglePricingRule.ruleFor(C).atPrice(20));
			add(MultiPricingRule.ruleFor(A).buy(3).forPrice(130));
			add(MultiPricingRule.ruleFor(B).buy(2).forPrice(45));
		}};
		
		Checkout checkout = Checkout.newRules(pricingRules)
				.scan(A)
				.scan(B);
				
			Receipt receipt = checkout.totalPrice();
			assertEquals(new Receipt(90, Collections.emptyMap(), 2), receipt);
	}
	
	@Test
	public void testMultiPricedItems() throws Exception {
		Checkout checkout = Checkout.newRules(pricingRules)
				.scan(A)
				.scan(A)
				.scan(A)
				.scan(B);
				
		Receipt receipt = checkout.totalPrice();
		assertEquals(new Receipt(160, Collections.emptyMap(), 4), receipt);
	}
	
	@Test
	public void testMultiPricedItemsWithRemainder() throws Exception {
		Checkout checkout = Checkout.newRules(pricingRules)
				.scan(A)
				.scan(A)
				.scan(A)
				.scan(A)
				.scan(B);
				
		Receipt receipt = checkout.totalPrice();
		assertEquals(new Receipt(210, Collections.emptyMap(), 5), receipt);
	}
	
	@Test
	public void testMultiPricedItemsScannedInDifferentOrder() throws Exception {
		Checkout checkout = Checkout.newRules(pricingRules)
				.scan(A)
				.scan(A)
				.scan(B)
				.scan(A)
				.scan(B);
				
		Receipt receipt = checkout.totalPrice();
		assertEquals(new Receipt(175, Collections.emptyMap(), 5), receipt);
	}
	
	@Test
	public void testIdempotencyOfTotalPriceCalculation() throws Exception {
		Checkout checkout = Checkout.newRules(pricingRules)
				.scan(A)
				.scan(A)
				.scan(B)
				.scan(A)
				.scan(B);
				
		Receipt receipt = checkout.totalPrice();
		assertEquals(new Receipt(175, Collections.emptyMap(), 5), receipt);
		
		receipt = checkout.totalPrice();
		assertEquals(new Receipt(175, Collections.emptyMap(), 5), receipt);
	}

	@Test
	public void testReCalculationOfTotalPriceWithAdditionalScan() throws Exception {
		Checkout checkout = Checkout.newRules(pricingRules)
				.scan(A)
				.scan(A)
				.scan(B)
				.scan(A)
				.scan(B);
				
		Receipt receipt = checkout.totalPrice();
		assertEquals(new Receipt(175, Collections.emptyMap(), 5), receipt);
		checkout.scan(C).scan(D);
		receipt = checkout.totalPrice();
		assertEquals(new Receipt(210, Collections.emptyMap(), 7), receipt);
	}
	
	@Test
	public void testCalculationOfTotalPriceForItemWithoutPricingRule() throws Exception {
		pricingRules = new HashSet<PricingRule>() {{
			add(SinglePricingRule.ruleFor(A).atPrice(50));
			add(SinglePricingRule.ruleFor(B).atPrice(30));
			add(SinglePricingRule.ruleFor(C).atPrice(20));
			add(MultiPricingRule.ruleFor(A).buy(3).forPrice(130));
			add(MultiPricingRule.ruleFor(B).buy(2).forPrice(45));
		}};
		
		Checkout checkout = Checkout.newRules(pricingRules)
				.scan(D);
				
		Receipt receipt = checkout.totalPrice();
		Map<Item, Long> unpricedItems = new HashMap<>();
		unpricedItems.put(D, 1l);
		assertEquals(new Receipt(0, unpricedItems, 0), receipt);
	}
	
	@Test
	public void testItemWithMultiPricingRuleButWithoutSinglePricingRule() throws Exception {
		pricingRules = new HashSet<PricingRule>() {{
			add(SinglePricingRule.ruleFor(B).atPrice(30));
			add(SinglePricingRule.ruleFor(C).atPrice(20));
			add(SinglePricingRule.ruleFor(D).atPrice(15));
			add(MultiPricingRule.ruleFor(A).buy(3).forPrice(130));
			add(MultiPricingRule.ruleFor(B).buy(2).forPrice(45));
		}};
		
		Checkout checkout = Checkout.newRules(pricingRules)
				.scan(A)
				.scan(A)
				.scan(B)
				.scan(A)
				.scan(B)
				.scan(A);
				
		Receipt receipt = checkout.totalPrice();
		Map<Item, Long> unpricedItems = new HashMap<>();
		unpricedItems.put(A, 1l);
		//System.out.println(receipt);
		assertEquals(new Receipt(175, unpricedItems, 5), receipt);
	}
	
	@Test
	public void testMoreThanOneMultiPricingRuleForOneItem() throws Exception {
		pricingRules = new HashSet<PricingRule>() {{
			add(SinglePricingRule.ruleFor(A).atPrice(50));
			add(SinglePricingRule.ruleFor(B).atPrice(30));
			add(SinglePricingRule.ruleFor(C).atPrice(20));
			add(SinglePricingRule.ruleFor(D).atPrice(15));
			add(MultiPricingRule.ruleFor(A).buy(3).forPrice(130));
			add(MultiPricingRule.ruleFor(B).buy(2).forPrice(45));
			add(MultiPricingRule.ruleFor(A).buy(5).forPrice(200));
		}};
		
		Checkout checkout = Checkout.newRules(pricingRules)
				.scan(A)
				.scan(A)
				.scan(B)
				.scan(A)
				.scan(B)
				.scan(C)
				.scan(A)
				.scan(A);
				
		Receipt receipt = checkout.totalPrice();
		
		assertEquals(new Receipt(265, Collections.emptyMap(), 8), receipt);
	}
	
}
