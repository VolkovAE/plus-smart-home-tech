package ru.practicum.smart.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.smart.model.Action;

public interface ActionRepository extends JpaRepository<Action, Long> {
}
