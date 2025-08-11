package com.code.elevate.wise.catalog.app.repository;

import com.code.elevate.wise.catalog.domain.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {
    UserDetails findByLogin(String login);
}
