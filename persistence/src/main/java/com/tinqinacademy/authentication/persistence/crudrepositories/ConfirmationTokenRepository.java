package com.tinqinacademy.authentication.persistence.crudrepositories;

import com.tinqinacademy.authentication.persistence.models.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String>  {
}
