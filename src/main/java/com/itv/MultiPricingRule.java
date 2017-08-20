package com.itv;


public class MultiPricingRule implements PricingRule {
	
	public static class MultiPricingRuleBuilder {
		private final Item item;
		private int spec;
		
		public MultiPricingRuleBuilder(final Item item) {
			this.item = item;
		}
		
		public MultiPricingRuleBuilder buy (final int spec) {
			this.spec = spec;
			return this;
		}
		
		public MultiPricingRule forPrice(final long price) {
			return new MultiPricingRule(price, item, spec);
		}
	}

	public static MultiPricingRuleBuilder ruleFor(Item item) {
		return new MultiPricingRuleBuilder(item);
	}
	
	private final long price;
	private final Item item;
	private final int spec;
	
	public MultiPricingRule(final long price, final Item item, final int spec) {
		this.price = price;
		this.item = item;
		this.spec = spec;
	}
	
	@Override
	public Unit apply(Long amount) {
		long ap = amount / spec;
		long remainder = amount % spec;
		return new Unit(ap * price, remainder);
	}
	
	@Override
	public Item getItem() {
		return item;
	}
	
	@Override
	public long weight() {
		return price * spec;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		result = prime * result + (int) (price ^ (price >>> 32));
		result = prime * result + spec;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MultiPricingRule other = (MultiPricingRule) obj;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		if (price != other.price)
			return false;
		if (spec != other.spec)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MultiPricingRule [price=" + price + ", item=" + item + ", spec=" + spec + "]";
	}

}
