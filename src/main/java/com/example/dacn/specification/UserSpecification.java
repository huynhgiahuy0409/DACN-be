package com.example.dacn.specification;

import com.example.dacn.model.UserEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification {
    public static Specification<UserEntity> hasUsernameOrEmail(String username, String email) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("username"), username));
            predicates.add(cb.equal(root.get("email"), email));
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}
