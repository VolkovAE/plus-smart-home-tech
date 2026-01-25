package ru.practicum.smart.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.smart.model.Condition;

public interface ConditionRepository extends JpaRepository<Condition, Long> {
}
