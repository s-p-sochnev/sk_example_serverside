package org.spsochnev.example.task;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface ExampleObjectRepository extends CrudRepository<ExampleObject, Integer> {

    @Query(value = "UPDATE sk_example_table " +
            "SET obj = obj || CONCAT('{\"current\":', (obj->>'current')\\:\\:INT + :add, '}')\\:\\:JSONB " +
            "WHERE id=:id " +
            "RETURNING (obj->>'current')\\:\\:INT AS \"current\"",
            nativeQuery = true)
    Map<String, Integer> incrementCurrentValue(@Param("id") Integer id, @Param("add") Integer add);
}
