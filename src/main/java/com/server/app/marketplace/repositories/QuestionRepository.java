package com.server.app.marketplace.repositories;

import com.server.app.marketplace.domain.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByProductId(Long productId);
}