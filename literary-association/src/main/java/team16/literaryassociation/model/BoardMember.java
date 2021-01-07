package team16.literaryassociation.model;

import lombok.NoArgsConstructor;
import team16.literaryassociation.dto.BoardMemberDTO;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@DiscriminatorValue("BoardMember")
public class BoardMember extends User {

    public BoardMember(BoardMemberDTO dto)
    {
        super(dto);
    }
}
