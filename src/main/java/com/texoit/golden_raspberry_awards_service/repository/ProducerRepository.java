package com.texoit.golden_raspberry_awards_service.repository;

import com.texoit.golden_raspberry_awards_service.entity.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Long> {

    public List<Producer> findByName(String name);
}
