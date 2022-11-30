package ru.skypro.homework.components.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.skypro.homework.components.validation.Violation;

import java.util.List;

@Getter
    @RequiredArgsConstructor
    public class ValidationErrorResponse {

        private final List<Violation> violations;

    }

