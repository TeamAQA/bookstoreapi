package pl.akademiaqa.bos.order.domain;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Optional;


public enum OrderStatus {
    NEW {
        @Override
        public UpdateStatusResult updateStatus(OrderStatus status) {
            switch (status) {
                case PAID:
                    return UpdateStatusResult.ok(PAID);
                case CANCELED:
                    return UpdateStatusResult.revoked(CANCELED);
                case ABANDONED:
                    return UpdateStatusResult.revoked(ABANDONED);
                default:
                    return super.updateStatus(status);
            }
        }
    },
    PAID {
        @Override
        public UpdateStatusResult updateStatus(OrderStatus status) {
            if (status == SHIPPED) {
                return UpdateStatusResult.ok(SHIPPED);
            }
            return super.updateStatus(status);
        }
    },
    CANCELED,
    // TODO - BUG 10 - Automatyczna zmiana statusu na ABANDONED.
    //  Po 5 minutach zmienia się status zamówienia na ABANDONED, nie ma tego w dokumentacji.
    ABANDONED,
    // TODO - BUG 8 (PATCH /orders/:id/status) - Można zmienić status zamówienia z SHIPPED na ABANDONED.
    //  Z tego statusu nie powinno się na nic zmieniać.
    SHIPPED {
        @Override
        public UpdateStatusResult updateStatus(OrderStatus status) {
            if (status == ABANDONED) {
                return UpdateStatusResult.ok(ABANDONED);
            }
            return super.updateStatus(status);
        }
    };

    public static Optional<OrderStatus> parseString(String value) {
        return Arrays.stream(values())
                .filter(it -> StringUtils.equalsIgnoreCase(it.name(), value))
                .findFirst();
    }

    public UpdateStatusResult updateStatus(OrderStatus status) {
        throw new IllegalArgumentException("Unable to mark " + this.name() + " order as " + status.name());
    }
}
