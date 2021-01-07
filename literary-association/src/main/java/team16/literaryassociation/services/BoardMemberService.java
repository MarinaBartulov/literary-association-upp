package team16.literaryassociation.services;

import team16.literaryassociation.dto.BoardMemberDTO;
import team16.literaryassociation.model.BoardMember;

import java.util.List;

public interface BoardMemberService {

    List<BoardMemberDTO> getAllBoardMembers();
    List<BoardMember> findAll();
}
