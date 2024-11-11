package com.app.inventory.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.inventory.persistence.entity.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
	
	List<Inventory> findAllByItemId(Long itemId);
}
