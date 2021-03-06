package com.thoughtworks.ddd.order.application;

import com.thoughtworks.ddd.order.domain.order.Order;
import com.thoughtworks.ddd.order.domain.order.OrderRepository;
import com.thoughtworks.ddd.order.domain.order.OrderStatus;
import com.thoughtworks.ddd.order.domain.payment.Payment;
import com.thoughtworks.ddd.order.domain.payment.PaymentRepository;
import com.thoughtworks.ddd.order.domain.payment.PaymentStatus;
import com.thoughtworks.ddd.order.domain.payment.PayOrderService;
import org.hamcrest.CustomMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderApplicationServiceTest {
    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PayOrderService paymentService;

    @InjectMocks
    private OrderApplicationService orderApplicationService;


    @Test
    public void should_be_able_to_pay_the_created_order() throws Exception {
        final long orderId = 123456L;
        Order order = new Order();
        order.setId(orderId);

        when(orderRepository.findBy(orderId)).thenReturn(order);
        when(paymentService.payOrder(order)).thenReturn(new Payment(orderId, PaymentStatus.PAID));

        orderApplicationService.payOrder(orderId);

        verify(paymentRepository).save(argThat(new CustomMatcher<Payment>("has paid.") {
            @Override
            public boolean matches(Object item) {
                Payment payment = (Payment) item;
                return payment.getPaymentStatus() == PaymentStatus.PAID;
            }
        }));

        verify(orderRepository).save(argThat(new CustomMatcher<Order>("Order has been paid.") {
            @Override
            public boolean matches(Object o) {
                Order order = (Order) o;
                return order.getOrderStatus() == OrderStatus.PAID;
            }
        }));
    }
}