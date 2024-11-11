package com.app.inventory.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateInventoryRequest {

	@NotNull(message = "item id cannot be null.")
	private Long itemId;

	@NotNull(message = "qty cannot be null.")
	@Min(value = 0, message = "qty must be a positive value.")
	private Integer qty;

	@NotBlank(message = "type cannot be empty.")
	private String type;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
