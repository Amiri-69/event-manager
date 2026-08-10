package com.eventmanager.event.specification;

import com.eventmanager.event.dto.request.EventFilterRequest;
import com.eventmanager.event.entity.Event;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EventSpecification {

    public static Specification<Event> filter(
            EventFilterRequest request
    ) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (request.getTitle() != null &&
                    !request.getTitle().isBlank()) {

                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("title")),
                                "%" + request.getTitle().toLowerCase() + "%"
                        )
                );
            }

            if (request.getCategoryId() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("category").get("id"),
                                request.getCategoryId()
                        )
                );
            }

            if (request.getLocationId() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("location").get("id"),
                                request.getLocationId()
                        )
                );
            }

            if (request.getStatus() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("status"),
                                request.getStatus()
                        )
                );
            }

            if (request.getStartFrom() != null) {

                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("startAt"),
                                request.getStartFrom()
                        )
                );
            }

            if (request.getEndTo() != null) {

                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("endAt"),
                                request.getEndTo()
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}