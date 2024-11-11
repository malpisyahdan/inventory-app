package com.app.inventory.model.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateItemRequest{

	@NotBlank(message = "name cannot be empty.")
	private String name;

	@NotNull(message = "Price cannot be null.")
	@DecimalMin(value = "0.0", inclusive = false, message = "Price must be a positive value.")
	private BigDecimal price;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
