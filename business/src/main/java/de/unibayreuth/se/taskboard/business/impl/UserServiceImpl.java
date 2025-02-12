package de.unibayreuth.se.taskboard.business.impl;

import de.unibayreuth.se.taskboard.business.domain.User;
import de.unibayreuth.se.taskboard.business.exceptions.DuplicateNameException;
import de.unibayreuth.se.taskboard.business.ports.UserPersistenceService;
import de.unibayreuth.se.taskboard.business.ports.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserPersistenceService userPersistenceService;

    @Override
    public void clear() {
        userPersistenceService.clear();
    }

    @Override
    @NonNull
    public List<User> getAll() {
        return userPersistenceService.getAll();
    }

    @Override
    @NonNull
    public Optional<User> getById(@NonNull UUID id) {
        return userPersistenceService.getById(id);
    }

    @NonNull
    @Override
    public User create(@NonNull User user) throws DuplicateNameException {
        if (user.getId() != null) {
            throw new DuplicateNameException("Cannot create a user with an existing ID.");
        }

        return userPersistenceService.upsert(user);
    }

}
