package com.example.springbatchdemo.repositories;

import com.example.springbatchdemo.entities.FooBar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdpRepository extends JpaRepository<FooBar, Long> {
}
