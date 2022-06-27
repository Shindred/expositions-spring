package com.myproject.expo.expositions.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Entity
@Table(name = "expos")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Exposition implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_expo")
    private Long idExpo;
    @Column(unique = true)
    private String name;
    @Column(nullable = false)
    private LocalDate expoDate;
    @Column(nullable = false)
    private LocalTime expoTime;
    private BigDecimal price;
    @ColumnDefault("1")
    private int statusId;
    @ManyToMany
    @JoinTable(name = "expo_hall",
        joinColumns = @JoinColumn(name = "exp_id",referencedColumnName = "id_expo"),
       inverseJoinColumns = @JoinColumn(name = "hall_id",referencedColumnName = "id_hall"))
    private Set<Hall> halls = new HashSet<>();
    @OneToOne(targetEntity = Theme.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "theme_id")
    private Theme theme;
    @ManyToMany(mappedBy = "expos",fetch = FetchType.EAGER)
    private List<User> users = new ArrayList<>();
    @OneToOne(targetEntity = Statistic.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "statistic_id", referencedColumnName = "id")
    private Statistic statistic;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Exposition that = (Exposition) o;
        return idExpo != null && Objects.equals(idExpo, that.idExpo);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Exposition{" +
                "idExpo=" + idExpo +
                ", name='" + name + '\'' +
                ", expoDate=" + expoDate +
                ", expoTime=" + expoTime +
                ", price=" + price +
                ", statusId=" + statusId +
                ", halls=" + halls +
                ", theme=" + theme +
                ", statistic=" + statistic +
                '}';
    }
}
