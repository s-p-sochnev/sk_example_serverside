package org.spsochnev.example.task;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Can use {@link org.hibernate.annotations.Type @Type} with @TypeDef for Hibernate 4/5
 * Also can use a proper implementation for 'obj' field
 * when it's necessary to work with this field in the app code
 */

@Entity
@Table(name="${sk.example.table.name}")
public class ExampleObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Object obj;
}
