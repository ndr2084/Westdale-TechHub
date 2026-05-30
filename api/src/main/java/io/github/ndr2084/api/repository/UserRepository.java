package io.github.ndr2084.api.repository;

import io.github.ndr2084.api.model.UserEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findById(@NonNull String Id);
}
