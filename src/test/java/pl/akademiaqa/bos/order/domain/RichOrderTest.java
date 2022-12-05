package pl.akademiaqa.bos.order.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.akademiaqa.bos.books.domain.Book;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class RichOrderTest {

    @Test
    public void shouldCalculateTotalPriceForEmptyOrder() {
        // given
        RichOrder order = new RichOrder(
                1L,
                OrderStatus.NEW,
                Collections.EMPTY_SET,
                Recipient.builder().build(),
                LocalDateTime.now()
        );

        // when
        BigDecimal totalPrice = order.totalPrice();

        // then
        Assertions.assertThat(totalPrice).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void shouldCalculateTotalPriceForOneBook() {
        // given
        Book b1 = new Book();
        b1.setPrice(new BigDecimal("12.50"));

        Book b2 = new Book();
        b2.setPrice(new BigDecimal("15.50"));

        Set<OrderItem> items = new HashSet<>(
                Arrays.asList(
                        new OrderItem(b1, 1),
                        new OrderItem(b2, 1)
                )
        );

        RichOrder order = new RichOrder(
                1L,
                OrderStatus.NEW,
                items,
                Recipient.builder().build(),
                LocalDateTime.now()
        );

        // when
        BigDecimal totalPrice = order.totalPrice();

        // then
         Assertions.assertThat(totalPrice).isEqualTo(new BigDecimal("28.00"));
    }

    @Test
    public void shouldCalculateTotalPriceForMultipleBooks() {
        // given
        Book b1 = new Book();
        b1.setPrice(new BigDecimal("12.50"));

        Book b2 = new Book();
        b2.setPrice(new BigDecimal("15.50"));

        Set<OrderItem> items = new HashSet<>(
                Arrays.asList(
                        new OrderItem(b1, 2),
                        new OrderItem(b2, 2)
                )
        );

        RichOrder order = new RichOrder(
                1L,
                OrderStatus.NEW,
                items,
                Recipient.builder().build(),
                LocalDateTime.now()
        );

        // when
        BigDecimal totalPrice = order.totalPrice();

        // then
        Assertions.assertThat(totalPrice).isEqualTo(new BigDecimal("56.00"));
    }
}