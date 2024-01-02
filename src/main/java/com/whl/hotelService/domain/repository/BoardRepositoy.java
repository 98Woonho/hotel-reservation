package com.whl.hotelService.domain.repository;

import com.whl.hotelService.domain.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepositoy extends JpaRepository<BoardEntity, Long> {

}
