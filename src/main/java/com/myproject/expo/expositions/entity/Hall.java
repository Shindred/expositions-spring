package com.myproject.expo.expositions.entity;

import com.myproject.expo.expositions.dto.HallDto;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "halls")
@Builder
public class Hall implements Serializable {
    private static final Long serialUID = 345678902345671567L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id_hall")
    private Long idHall;
    @Column(unique = true, nullable = false)
    private String name;
    @ManyToMany(mappedBy = "halls")
    private Set<Exposition> expositions = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Hall hall = (Hall) o;
        return idHall != null && Objects.equals(idHall, hall.idHall);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public static Hall of(HallDto hallDto) {
        return Hall.builder()
                .idHall(hallDto.getId())
                .name(hallDto.getName())
                .build();
    }

    @Override
    public String toString() {
        return "Hall{" +
                "idHall=" + idHall +
                ", name='" + name + '\'' +
                '}';
    }
}
