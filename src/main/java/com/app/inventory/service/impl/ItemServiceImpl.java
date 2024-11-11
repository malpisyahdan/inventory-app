package com.app.inventory.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.app.inventory.common.type.TypeInventory;
import com.app.inventory.error.ErrorMessageConstant;
import com.app.inventory.model.request.CreateItemRequest;
import com.app.inventory.model.request.UpdateItemRequest;
import com.app.inventory.model.response.ItemResponse;
import com.app.inventory.persistence.entity.Inventory;
import com.app.inventory.persistence.entity.Item;
import com.app.inventory.persistence.repository.ItemRepository;
import com.app.inventory.service.InventoryService;
import com.app.inventory.service.ItemService;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;

@Service
public class ItemServiceImpl implements ItemService {

	private final ItemRepository repository;
	private final InventoryService inventoryService;

	public ItemServiceImpl(ItemRepository repository, @Lazy InventoryService inventoryService) {
		this.repository = repository;
		this.inventoryService = inventoryService;
	}

	public void validateBkNotExist(CreateItemRequest request) {
		if (repository.existsByName(request.getName())) {
			throw new ValidationException("item with " + request.getName() + ErrorMessageConstant.IS_EXISTS);
		}
	}

	@Transactional
	@Override
	public void add(CreateItemRequest request) {
		Item entity = new Item();
		validateBkNotExist(request);
		mapToEntity(entity, request);
		repository.save(entity);

	}

	@Transactional
	@Override
	public void edit(UpdateItemRequest request) {
		getEntityById(request.getId()).ifPresentOrElse(entity -> {
			mapToEntity(entity, request);
			repository.save(entity);
		}, () -> {
			throw new RuntimeException("id item " + ErrorMessageConstant.IS_NOT_EXISTS);
		});

	}

	@Transactional
	@Override
	public void delete(Long id) {
		getEntityById(id).ifPresentOrElse(entity -> {
			repository.deleteById(id);
		}, () -> {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id item " + ErrorMessageConstant.IS_NOT_EXISTS);
		});

	}

	@Override
	public Optional<Item> getEntityById(Long id) {
		return repository.findById(id);
	}

	@Override
	public ItemResponse getById(Long id) {
		Item entity = getEntityById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "item(s) " + ErrorMessageConstant.NOT_FOUND));
		return mapToResponse(entity);
	}

	@Override
	public Page<ItemResponse> getAllItems(Pageable pageable) {
		return repository.findAll(pageable).map(entity -> mapToResponse(entity));
	}

	private void mapToEntity(Item entity, CreateItemRequest request) {
		entity.setName(request.getName());
		entity.setPrice(request.getPrice());
	}

	public ItemResponse mapToResponse(Item entity) {
		ItemResponse response = new ItemResponse();
		response.setId(entity.getId());
		response.setName(entity.getName());
		response.setPrice(entity.getPrice());

		List<Inventory> items = inventoryService.getEntityByItemId(entity.getId());

		int totalStock = items.stream().mapToInt(item -> {
			if (TypeInventory.T.equals(item.getType())) {
				return item.getQty();
			} else if (TypeInventory.W.equals(item.getType())) {
				return -item.getQty();
			} else {
				return 0;
			}
		}).sum();
		response.setStock(totalStock);

		response.setCreatedAt(entity.getCreatedAt());
		response.setUpdatedAt(entity.getUpdatedAt());
		response.setVersion(entity.getVersion());

		return response;
	}

}
