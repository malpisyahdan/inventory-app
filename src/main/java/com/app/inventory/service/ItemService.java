package com.app.inventory.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.inventory.model.request.CreateItemRequest;
import com.app.inventory.model.request.UpdateItemRequest;
import com.app.inventory.model.response.ItemResponse;
import com.app.inventory.persistence.entity.Item;

public interface ItemService {
	
	void add(CreateItemRequest request);
	
	void edit(UpdateItemRequest request);

	void delete(Long id);

	Optional<Item> getEntityById(Long id);

	ItemResponse getById(Long id);

	Page<ItemResponse> getAllItems(Pageable pageable);

}
