package com.whl.hotelService.boardDomain.service;

import com.whl.hotelService.boardDomain.dto.BoardDto;
import com.whl.hotelService.boardDomain.entity.BoardEntity;
import com.whl.hotelService.boardDomain.repository.BoardRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(rollbackFor = Exception.class)
    public void updateHits(Long id) throws Exception {
        boardRepositoy.updateHits(id);
    }

    public BoardDto findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepositoy.findById(id); //Id 로 조회를 해서
        if (optionalBoardEntity.isPresent()){ //만약 있으면
            BoardEntity boardEntity = optionalBoardEntity.get(); //반환하고
            BoardDto boardDto = BoardDto.EntityToDto(boardEntity); //엔티티를 Dto로 변환해서 컨트롤러에 리턴
            return boardDto;
        } else {
            return null;
        }
    }
}

