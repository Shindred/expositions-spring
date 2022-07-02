package com.myproject.expo.expositions.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "themes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Theme implements Serializable {
    private static final long serialVersionUID = 3020521383107171828L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTheme;
    @Column(unique = true,nullable = false)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Theme theme = (Theme) o;
        return idTheme != null && Objects.equals(idTheme, theme.idTheme);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Theme{" +
                "idTheme=" + idTheme +
                ", name='" + name + '\'' +
                '}';
    }
}
