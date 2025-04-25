package com.sparta.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.demo.model.Board;

public interface BoardRepository extends JpaRepository<Board, Long>{

}
