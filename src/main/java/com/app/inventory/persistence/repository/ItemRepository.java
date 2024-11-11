package com.app.inventory.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.inventory.persistence.entity.Item;
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
	
	boolean existsByName(String name);
}
