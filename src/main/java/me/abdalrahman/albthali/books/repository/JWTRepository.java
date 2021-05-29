package me.abdalrahman.albthali.books.repository;

import me.abdalrahman.albthali.books.entity.JWTEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JWTRepository extends CrudRepository<JWTEntity, String> {
}
