package ru.practicum.smart.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ru.practicum.smart.util.StringConstants.*;

@Entity
@Table(name = TABLE_NAME_ENTITY_BOOKING, schema = SHEMA_NAME)
@Getter
@Setter
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class OrderBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = COLUMN_NAME_BOOKING_ID)
    UUID bookingId;

    @Column(name = COLUMN_NAME_ORDER_ID)
    UUID orderId;

    @Column(name = COLUMN_NAME_DELIVERY_ID)
    UUID deliveryId;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    List<BookingProduct> bookingProducts = new ArrayList<>();

    @Column(name = COLUMN_NAME_DELIVERY_WEIGHT)
    BigDecimal deliveryWeight;

    @Column(name = COLUMN_NAME_DELIVERY_VOLUME)
    BigDecimal deliveryVolume;

    @Column(name = COLUMN_NAME_ENTITY_PRODUCT_FRAGILE)
    Boolean fragile;

    public void addProduct(QuantityProductInWarehouse product, Integer quantity) {
        BookingProduct bookingProduct = new BookingProduct();
        bookingProduct.setBooking(this);
        bookingProduct.setProduct(product);
        bookingProduct.setQuantity(quantity);

        this.bookingProducts.add(bookingProduct);
    }
}
