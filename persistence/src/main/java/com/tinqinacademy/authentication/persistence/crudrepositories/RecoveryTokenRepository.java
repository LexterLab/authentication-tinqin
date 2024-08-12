package com.tinqinacademy.authentication.persistence.crudrepositories;

import com.tinqinacademy.authentication.persistence.models.RecoveryToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecoveryTokenRepository  extends CrudRepository<RecoveryToken, String> {
}
