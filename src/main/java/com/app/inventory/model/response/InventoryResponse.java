package com.app.inventory.model.response;

import java.time.ZonedDateTime;

import com.app.inventory.common.type.TypeInventory;

public class InventoryResponse {

	private Long id;
	private Long itemId;
	private String itemName;
	private Integer qty;
	private TypeInventory type;

	private Long version;
	private ZonedDateTime createdAt;
	private ZonedDateTime updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public TypeInventory getType() {
		return type;
	}

	public void setType(TypeInventory type) {
		this.type = type;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public ZonedDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(ZonedDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public ZonedDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(ZonedDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

}
