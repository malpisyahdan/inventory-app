package com.app.inventory.persistence.entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.app.inventory.common.type.TypeInventory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@SuppressWarnings("deprecation")
@Entity
@Table(name = "inventory")
@SQLDelete(sql = "UPDATE inventory SET deleted_at = now() WHERE id=? AND version =?")
@Where(clause = "deleted_at IS NULL")
public class Inventory extends MasterEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	@Column(name = "qty", nullable = false)
	private Integer qty;

	@Column(name = "type", nullable = false)
	@Enumerated(value = EnumType.STRING)
	private TypeInventory type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public TypeInventory getType() {
		return type;
	}

	public void setType(TypeInventory type) {
		this.type = type;
	}

}