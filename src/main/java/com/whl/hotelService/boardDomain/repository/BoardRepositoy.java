package com.whl.hotelService.boardDomain.repository;

import com.whl.hotelService.boardDomain.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepositoy extends JpaRepository<BoardEntity, Long> {
    @Modifying //update, delete시 필수로 붙여야함
    @Query(value = "update board_table set board_hits = board_hits+1 where id=?", nativeQuery = true)
    void updateHits(@Param("id") Long id);
}
