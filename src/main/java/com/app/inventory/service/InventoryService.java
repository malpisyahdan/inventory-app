package com.app.inventory.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.inventory.model.request.CreateInventoryRequest;
import com.app.inventory.model.request.UpdateInventoryRequest;
import com.app.inventory.model.response.InventoryResponse;
import com.app.inventory.persistence.entity.Inventory;

public interface InventoryService {

	void add(CreateInventoryRequest request);

	void edit(UpdateInventoryRequest request);

	void delete(Long id);
	
	Inventory changeTotalStock(Long itemId, Integer qty);

	Optional<Inventory> getEntityById(Long id);
	
	List<Inventory> getEntityByItemId(Long itemId);

	InventoryResponse getById(Long id);

	Page<InventoryResponse> getAllItems(Pageable pageable);

}
