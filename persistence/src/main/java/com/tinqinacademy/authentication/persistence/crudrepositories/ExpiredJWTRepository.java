package com.tinqinacademy.authentication.persistence.crudrepositories;

import com.tinqinacademy.authentication.persistence.models.ExpiredJWT;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpiredJWTRepository extends CrudRepository<ExpiredJWT,String> {
}
