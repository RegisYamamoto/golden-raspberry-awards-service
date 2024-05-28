package com.texoit.golden_raspberry_awards_service.repository;

import com.texoit.golden_raspberry_awards_service.entity.Studios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudiosRepository extends JpaRepository<Studios, Long> {

    public List<Studios> findByName(String name);
}
