package com.app.inventory.persistence.entity;

import java.math.BigDecimal;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@SuppressWarnings("deprecation")
@Entity
@Table(name = "orders")
@SQLDelete(sql = "UPDATE orders SET deleted_at = now() WHERE id=? AND version =?")
@Where(clause = "deleted_at IS NULL")
public class Order extends MasterEntity {

	@Id
	@Column(name = "order_no", nullable = false, updatable = false)
	private String orderNo;

	@ManyToOne
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	@Column(name = "qty", nullable = false)
	private Integer qty;

	@Column(name = "price", nullable = false)
	private BigDecimal price;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}