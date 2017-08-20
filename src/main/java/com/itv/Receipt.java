package com.itv;

import java.util.Map;

public class Receipt {

	private final long totalPrice;
	private final Map<Item, Long> unpricedItems;
	private final long numberOfPricedItems;
	
	public Receipt(final long totalPrice, final Map<Item, Long> unpricedItems, final long numberOfPricedItems) {
		this.totalPrice = totalPrice;
		this.unpricedItems = unpricedItems;
		this.numberOfPricedItems = numberOfPricedItems;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (numberOfPricedItems ^ (numberOfPricedItems >>> 32));
		result = prime * result + (int) (totalPrice ^ (totalPrice >>> 32));
		result = prime * result + ((unpricedItems == null) ? 0 : unpricedItems.hashCode());
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
		Receipt other = (Receipt) obj;
		if (numberOfPricedItems != other.numberOfPricedItems)
			return false;
		if (totalPrice != other.totalPrice)
			return false;
		if (unpricedItems == null) {
			if (other.unpricedItems != null)
				return false;
		} else if (!unpricedItems.equals(other.unpricedItems))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Receipt [totalPrice=" + totalPrice + ", numberOfPricedItems=" + numberOfPricedItems 
				+ ", unpricedItems=" + unpricedItems + "]";
	}
	
	
	
}
