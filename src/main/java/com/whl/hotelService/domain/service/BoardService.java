package com.whl.hotelService.domain.service;

import com.whl.hotelService.domain.dto.BoardDto;
import com.whl.hotelService.domain.entity.BoardEntity;
import com.whl.hotelService.domain.repository.BoardRepositoy;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
// DTO -> Entity변환 작업은 (Entity class)컨트롤러가 서비스로 데이터를 넘겨줄 땐 DTO 객체를 반환해야함 반대로 서비스에서 컨트롤러에 데이터를 넘겨줄 땐 DTO 객체를 반환
// Entity -> DTO변환 작업은 (DTO class)서비스가 레파지토리로 데이터를 넘겨줄 땐 Entity 객체를 반환해야함 반대로 레파지토리에서 서비스로 데이터를 넘겨줄때도 Enitiy 객체를 반환
@Service
public class BoardService {
    @Autowired
    private BoardRepositoy boardRepositoy;

    public void save(BoardDto boardDto) {
        // Contoller에서 받은 Dto객체를 엔티티로 변환해서 boardRepositoydp 저장해야함
        BoardEntity boardEntity = BoardEntity.DtoToEntity(boardDto);
        boardRepositoy.save(boardEntity);
    }

    public List<BoardDto> findAll() {
        // boardRepositoy로부터 Entity로 넘어온 객체를 Dto로 담아서 컨트롤러에 전달해야함.
        List<BoardEntity> boardEntityList = boardRepositoy.findAll();
        List<BoardDto> boardDtoList = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntityList){
            boardDtoList.add(BoardDto.EntityToDto(boardEntity));
        }
        return boardDtoList;
    }
}

