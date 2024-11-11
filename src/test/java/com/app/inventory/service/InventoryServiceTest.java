package com.app.inventory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.app.inventory.common.type.TypeInventory;
import com.app.inventory.error.ErrorMessageConstant;
import com.app.inventory.model.request.CreateInventoryRequest;
import com.app.inventory.model.request.UpdateInventoryRequest;
import com.app.inventory.model.response.InventoryResponse;
import com.app.inventory.persistence.entity.Inventory;
import com.app.inventory.persistence.entity.Item;
import com.app.inventory.persistence.repository.InventoryRepository;
import com.app.inventory.service.impl.InventoryServiceImpl;

class InventoryServiceTest {

	@Mock
	private InventoryRepository inventoryRepository;

	@Mock
	private ItemService itemService;

	private InventoryService inventoryService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		inventoryService = new InventoryServiceImpl(inventoryRepository, itemService);
	}

	@Test
	void testAddInventory_validRequest() {
		CreateInventoryRequest request = new CreateInventoryRequest();
		request.setItemId((long) 1);
		request.setQty(10);
		request.setType("T");

		Item mockItem = new Item();
		mockItem.setId((long) 1);
		when(itemService.getEntityById((long) 1)).thenReturn(Optional.of(mockItem));

		Inventory mockInventory = new Inventory();
		mockInventory.setId((long) 1);
		when(inventoryRepository.save(any(Inventory.class))).thenReturn(mockInventory);

		inventoryService.add(request);

		verify(inventoryRepository, times(1)).save(any(Inventory.class));
	}

	@Test
	void testAddInventory_invalidItem() {
		CreateInventoryRequest request = new CreateInventoryRequest();
		request.setItemId((long) 1);
		request.setQty(10);
		request.setType("T");

		when(itemService.getEntityById((long) 1)).thenReturn(Optional.empty());

		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			inventoryService.add(request);
		});

		assertEquals("404 NOT_FOUND \"item id is not exists\"", exception.getMessage());
	}

	@Test
	void testEditInventory_validRequest() {
		UpdateInventoryRequest request = new UpdateInventoryRequest();
		request.setId((long) 1);
		request.setItemId((long) 1);
		request.setQty(15);
		request.setType("T");

		Item mockItem = new Item();
		mockItem.setId((long) 1);
		when(itemService.getEntityById((long) 1)).thenReturn(Optional.of(mockItem));

		Inventory existingInventory = new Inventory();
		existingInventory.setId((long) 1);
		when(inventoryRepository.findById((long) 1)).thenReturn(Optional.of(existingInventory));

		inventoryService.edit(request);

		verify(inventoryRepository, times(1)).save(existingInventory);
	}

	@Test
	void testEditInventory_notFound() {
		UpdateInventoryRequest request = new UpdateInventoryRequest();
		request.setId((long) 1);
		request.setItemId((long) 1);
		request.setQty(15);
		request.setType("T");

		Item mockItem = new Item();
		mockItem.setId((long) 1);
		when(itemService.getEntityById((long) 1)).thenReturn(Optional.of(mockItem));

		when(inventoryRepository.findById((long) 1)).thenReturn(Optional.empty());

		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			inventoryService.edit(request);
		});

		assertEquals("404 NOT_FOUND \"id item is not exists\"", exception.getMessage());
	}

	@Test
	void testDeleteInventory_validRequest() {
		Long inventoryId = (long) 1;

		Inventory existingInventory = new Inventory();
		existingInventory.setId(inventoryId);

		when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(existingInventory));

		inventoryService.delete(inventoryId);

		verify(inventoryRepository, times(1)).deleteById(inventoryId);
	}

	@Test
	void testDeleteInventory_notFound() {
		Long inventoryId = (long) 1;

		when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.empty());

		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			inventoryService.delete(inventoryId);
		});

		assertEquals("404 NOT_FOUND \"id itemis not exists\"", exception.getMessage());
	}

	@Test
	void testChangeTotalStock_success() {
		Long itemId = (long) 1;
		Integer qty = 15;

		Inventory inventory1 = new Inventory();
		inventory1.setId((long) 1);
		inventory1.setQty(10);

		Inventory inventory2 = new Inventory();
		inventory2.setId((long) 1);
		inventory2.setQty(10);

		List<Inventory> inventories = Arrays.asList(inventory1, inventory2);

		when(inventoryRepository.findAllByItemId(itemId)).thenReturn(inventories);

		when(inventoryRepository.save(any(Inventory.class))).thenAnswer(invocation -> {
			Inventory savedInventory = invocation.getArgument(0);
			return savedInventory;
		});

		inventoryService.changeTotalStock(itemId, qty);

		verify(inventoryRepository, times(2)).save(any(Inventory.class));
	}

	@Test
	void testChangeTotalStock_notEnoughStock() {
		Long itemId = (long) 1;
		int qtyToReduce = 20;

		Inventory inventory1 = new Inventory();
		inventory1.setQty(5);

		List<Inventory> inventories = Collections.singletonList(inventory1);
		when(inventoryRepository.findAllByItemId(itemId)).thenReturn(inventories);

		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			inventoryService.changeTotalStock(itemId, qtyToReduce);
		});

		assertEquals("400 BAD_REQUEST \"Not enough stock available for item\"", exception.getMessage());
	}

	@Test
	void testGetByIdSuccessfully() {
		Item item = new Item();
		item.setId((long) 1);
		item.setName("Item 1");
		item.setPrice(BigDecimal.valueOf(10000.0));

		Inventory inventory = new Inventory();
		inventory.setId((long) 1);
		inventory.setItem(item);
		inventory.setQty(5);
		inventory.setType(TypeInventory.T);

		when(inventoryRepository.findById((long) 1)).thenReturn(Optional.of(inventory));

		InventoryResponse response = inventoryService.getById((long) 1);

		assert response.getId().equals(inventory.getId());
		assert response.getItemId().equals(inventory.getItem().getId());
		assert response.getItemName().equals(inventory.getItem().getName());
		assert response.getQty().equals(inventory.getQty());
		assert response.getType().equals(inventory.getType());
	}

	@Test
	void testGetByIdNotFound() {
		when(inventoryRepository.findById((long) 1)).thenReturn(Optional.empty());

		try {
			inventoryService.getById((long) 1);
		} catch (ResponseStatusException e) {
			assert (e.getStatusCode() == HttpStatus.NOT_FOUND);
			assert (e.getReason().contains("item(s) " + ErrorMessageConstant.NOT_FOUND));
		}
	}

	@Test
	void testGetAllInventory() {
		@SuppressWarnings("unchecked")
		Page<Inventory> itemPage = mock(Page.class);
		when(inventoryRepository.findAll(any(Pageable.class))).thenReturn(itemPage);

		inventoryService.getAllItems(mock(Pageable.class));

		verify(inventoryRepository, times(1)).findAll(any(Pageable.class));
	}
}
