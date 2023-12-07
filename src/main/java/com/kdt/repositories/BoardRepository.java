package com.kdt.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdt.domain.entities.Board;

public interface BoardRepository extends JpaRepository<Board, Long>{

	List<Board> findAllByBoardTitle(String boardTitle);
	
}
