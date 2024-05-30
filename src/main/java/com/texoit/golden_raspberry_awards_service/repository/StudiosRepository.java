package com.texoit.golden_raspberry_awards_service.repository;

import com.texoit.golden_raspberry_awards_service.entity.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudiosRepository extends JpaRepository<Studio, Long> {

    public List<Studio> findByName(String name);
}
