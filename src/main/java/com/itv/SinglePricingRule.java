package com.itv;


public class SinglePricingRule implements PricingRule {
	
	public static class SinglePricingRuleBuilder {
		private final Item item;

		public SinglePricingRuleBuilder(final Item item) {
			this.item = item;
		}
		
		public SinglePricingRule atPrice(final long price) {
			return new SinglePricingRule(item, price);
		}
	}

	public static SinglePricingRuleBuilder ruleFor(Item item) {
		return new SinglePricingRuleBuilder(item);
	}
	
	private final Item item;
	private final long price;
	
	public SinglePricingRule(final Item item, final long price) {
		this.item = item;
		this.price = price;
	}
	
	@Override
	public Unit apply(Long amount) {
		return new Unit(price * amount, 0);
	}
	
	@Override
	public Item getItem() {
		return item;
	}
	
	@Override
	public long weight() {
		return price;
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
		SinglePricingRule other = (SinglePricingRule) obj;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		if (price != other.price)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SinglePricingRule [item=" + item + ", price=" + price + "]";
	}

}
