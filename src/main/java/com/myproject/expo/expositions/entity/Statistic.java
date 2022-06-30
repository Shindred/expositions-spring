package com.myproject.expo.expositions.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Statistic implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ColumnDefault("0")
    @Digits(integer = 15, fraction = 0, message = "{err.sold_input}")
    @NotNull(message = "{err.sold_input}")
    private Long sold;
    @Digits(integer = 15, fraction = 0, message = "{err.tickets_input}")
    @NotNull(message = "{err.tickets_input}")
    private Long tickets;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Statistic statistic = (Statistic) o;
        return Objects.equals(getId(), statistic.getId())
                && Objects.equals(getSold(), statistic.getSold())
                && Objects.equals(getTickets(), statistic.getTickets());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSold(), getTickets());
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "id=" + id +
                ", sold=" + sold +
                ", tickets=" + tickets +
                '}';
    }
}
