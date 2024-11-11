package com.app.inventory.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateOrderRequest {

	@NotNull(message = "item id cannot be null.")
	private Long itemId;

	@NotNull(message = "qty cannot be null.")
	@Min(value = 0, message = "qty must be a positive value.")
	private Integer qty;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

}
