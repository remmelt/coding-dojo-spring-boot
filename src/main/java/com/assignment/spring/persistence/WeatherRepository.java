package com.assignment.spring.persistence;

import com.assignment.spring.persistence.model.WeatherEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends CrudRepository<WeatherEntity, Integer> {
}
