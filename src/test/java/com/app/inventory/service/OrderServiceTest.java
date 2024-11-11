package com.app.inventory.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.app.inventory.error.ErrorMessageConstant;
import com.app.inventory.model.request.CreateOrderRequest;
import com.app.inventory.model.request.UpdateOrderRequest;
import com.app.inventory.model.response.OrderResponse;
import com.app.inventory.persistence.entity.Inventory;
import com.app.inventory.persistence.entity.Item;
import com.app.inventory.persistence.entity.Order;
import com.app.inventory.persistence.repository.OrderRepository;
import com.app.inventory.service.impl.OrderServiceImpl;

public class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private ItemService itemService;

	@Mock
	private InventoryService inventoryService;

	private OrderServiceImpl orderService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		orderService = new OrderServiceImpl(orderRepository, itemService, inventoryService);
	}

	@Test
	void testAddOrder_success() {
		CreateOrderRequest request = new CreateOrderRequest();
		request.setItemId((long) 1);
		request.setQty(5);

		Item mockItem = mock(Item.class);
		when(itemService.getEntityById((long) 1)).thenReturn(Optional.of(mockItem));
		when(mockItem.getPrice()).thenReturn(BigDecimal.valueOf(10));

		Inventory mockInventory = mock(Inventory.class);
		when(inventoryService.changeTotalStock((long) 1, 5)).thenReturn(mockInventory);

		when(mockInventory.getQty()).thenReturn(10);

		Order expectedOrder = new Order();
		expectedOrder.setItem(mockItem);
		expectedOrder.setQty(5);
		expectedOrder.setPrice(BigDecimal.valueOf(50));

		orderService.add(request);

		verify(orderRepository, times(1)).save(any(Order.class));
	}

	@Test
	void testAddOrder_itemNotFound() {
		CreateOrderRequest request = new CreateOrderRequest();
		request.setItemId((long) 1);
		request.setQty(5);

		when(itemService.getEntityById((long) 1)).thenReturn(Optional.empty());

		assertThrows(ResponseStatusException.class, () -> orderService.add(request));
	}

	@Test
	void testEditOrder_success() {
		UpdateOrderRequest request = new UpdateOrderRequest();
		request.setId("Q1");
		request.setItemId((long) 1);
		request.setQty(5);

		Order existingOrder = new Order();
		existingOrder.setOrderNo("Q1");

		Item existingItem = new Item();
		existingItem.setId((long) 1);
		existingItem.setPrice(BigDecimal.valueOf(10));
		existingOrder.setItem(existingItem);
		existingOrder.setQty(10);
		existingOrder.setPrice(BigDecimal.valueOf(100));

		Item mockItem = new Item();
		mockItem.setId((long) 1);
		mockItem.setPrice(BigDecimal.valueOf(10));
		when(itemService.getEntityById((long) 1)).thenReturn(Optional.of(mockItem));

		when(orderRepository.findById("Q1")).thenReturn(Optional.of(existingOrder));

		Inventory mockInventory = mock(Inventory.class);
		when(inventoryService.changeTotalStock((long) 1, 5)).thenReturn(mockInventory);

		when(mockInventory.getQty()).thenReturn(5);

		existingOrder.setQty(5);
		existingOrder.setPrice(BigDecimal.valueOf(50));

		orderService.edit(request);

		verify(orderRepository, times(1)).save(existingOrder);
	}

	@Test
	void testEditOrder_itemNotFound() {
		UpdateOrderRequest request = new UpdateOrderRequest();
		request.setId("Q1");
		request.setItemId((long) 1);
		request.setQty(5);

		when(orderRepository.findById("Q1")).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> orderService.edit(request));
	}

	@Test
	void testDeleteOrder_success() {
		String orderId = "Q1";

		Order existingOrder = new Order();
		existingOrder.setOrderNo(orderId);

		when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

		orderService.delete(orderId);

		verify(orderRepository, times(1)).deleteById(orderId);
	}

	@Test
	void testDeleteOrder_itemNotFound() {
		String orderId = "Q1";

		when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> orderService.delete(orderId));
	}

	@Test
	void testGetByIdSuccessfully() {
		Item item = new Item();
		item.setId((long) 1);
		item.setName("Item 1");
		item.setPrice(BigDecimal.valueOf(10000.0));

		Order order = new Order();
		order.setOrderNo("Q1");
		order.setItem(item);
		order.setQty(5);
		order.setPrice(BigDecimal.valueOf(50));

		when(orderRepository.findById("Q1")).thenReturn(Optional.of(order));

		OrderResponse response = orderService.getById("Q1");

		assert response.getOrderNo().equals(order.getOrderNo());
		assert response.getItemId().equals(order.getItem().getId());
		assert response.getItemName().equals(order.getItem().getName());
		assert response.getQty().equals(order.getQty());
		assert response.getPrice().equals(order.getPrice());
	}

	@Test
	void testGetByIdNotFound() {
		when(orderRepository.findById("Q1")).thenReturn(Optional.empty());

		try {
			itemService.getById((long) 1);
		} catch (ResponseStatusException e) {
			assert (e.getStatusCode() == HttpStatus.NOT_FOUND);
			assert (e.getReason().contains("item(s) " + ErrorMessageConstant.NOT_FOUND));
		}
	}

	@Test
	void testGetAllOrder() {
		@SuppressWarnings("unchecked")
		Page<Order> itemPage = mock(Page.class);
		when(orderRepository.findAll(any(Pageable.class))).thenReturn(itemPage);

		orderService.getAllItems(mock(Pageable.class));

		verify(orderRepository, times(1)).findAll(any(Pageable.class));
	}
}
