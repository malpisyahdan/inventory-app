package com.app.inventory.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.app.inventory.error.ErrorMessageConstant;
import com.app.inventory.model.request.CreateItemRequest;
import com.app.inventory.model.request.UpdateItemRequest;
import com.app.inventory.model.response.ItemResponse;
import com.app.inventory.persistence.entity.Item;
import com.app.inventory.persistence.repository.ItemRepository;
import com.app.inventory.service.impl.ItemServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

	@Mock
	private ItemRepository itemRepository;

	@Mock
	private InventoryService inventoryService;

	@InjectMocks
	private ItemServiceImpl itemService;

	private Item item;
	private CreateItemRequest createItemRequest;
	private UpdateItemRequest updateItemRequest;

	@BeforeEach
	public void setUp() {
		item = new Item();
		item.setId((long) 1);
		item.setName("Item 1");
		item.setPrice(BigDecimal.valueOf(10000.0));

		createItemRequest = new CreateItemRequest();
		createItemRequest.setName("Item 1");
		createItemRequest.setPrice(BigDecimal.valueOf(10000.0));

		updateItemRequest = new UpdateItemRequest();
		updateItemRequest.setId((long) 1);
		updateItemRequest.setName("Updated Item");
		updateItemRequest.setPrice(BigDecimal.valueOf(10000.0));
	}

	@Test
	void testAddItemSuccessfully() {
		when(itemRepository.existsByName(createItemRequest.getName())).thenReturn(false);
		when(itemRepository.save(any(Item.class))).thenReturn(item);

		itemService.add(createItemRequest);

		verify(itemRepository, times(1)).save(any(Item.class));
	}

	@Test
	void testAddItemWithDuplicateName() {
		when(itemRepository.existsByName(createItemRequest.getName())).thenReturn(true);

		try {
			itemService.add(createItemRequest);
		} catch (Exception e) {
			assert (e instanceof ResponseStatusException);
			assert (e.getMessage().contains("item with Item 1 is exists"));
		}
	}

	@Test
	void testEditItemSuccessfully() {
		when(itemRepository.findById(updateItemRequest.getId())).thenReturn(Optional.of(item));
		when(itemRepository.save(any(Item.class))).thenReturn(item);

		itemService.edit(updateItemRequest);

		verify(itemRepository, times(1)).save(any(Item.class));
	}

	@Test
	void testEditItemNotFound() {
		when(itemRepository.findById(updateItemRequest.getId())).thenReturn(Optional.empty());

		try {
			itemService.edit(updateItemRequest);
		} catch (Exception e) {
			assert (e instanceof RuntimeException);
			assert (e.getMessage().contains("id item " + ErrorMessageConstant.IS_NOT_EXISTS));
		}
	}

	@Test
	void testDeleteItemSuccessfully() {
		when(itemRepository.findById((long) 1)).thenReturn(Optional.of(item));

		itemService.delete((long) 1);

		verify(itemRepository, times(1)).deleteById((long) 1);
	}

	@Test
	void testDeleteItemNotFound() {
		when(itemRepository.findById((long) 1)).thenReturn(Optional.empty());

		try {
			itemService.delete((long) 1);
		} catch (ResponseStatusException e) {
			assert (e.getStatusCode() == HttpStatus.NOT_FOUND);
			assert (e.getReason().contains("id item " + ErrorMessageConstant.IS_NOT_EXISTS));
		}
	}

	@Test
	void testGetByIdSuccessfully() {
		when(itemRepository.findById((long) 1)).thenReturn(Optional.of(item));

		ItemResponse response = itemService.getById((long) 1);

		assert response.getId().equals(item.getId());
		assert response.getName().equals(item.getName());
		assert response.getPrice().equals(item.getPrice());
	}

	@Test
	void testGetByIdNotFound() {
		when(itemRepository.findById((long) 1)).thenReturn(Optional.empty());

		try {
			itemService.getById((long) 1);
		} catch (ResponseStatusException e) {
			assert (e.getStatusCode() == HttpStatus.NOT_FOUND);
			assert (e.getReason().contains("item(s) " + ErrorMessageConstant.NOT_FOUND));
		}
	}

	@Test
	void testGetAllItems() {
		@SuppressWarnings("unchecked")
		Page<Item> itemPage = mock(Page.class);
		when(itemRepository.findAll(any(Pageable.class))).thenReturn(itemPage);

		itemService.getAllItems(mock(Pageable.class));

		verify(itemRepository, times(1)).findAll(any(Pageable.class));
	}
}