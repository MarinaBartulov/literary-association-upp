package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.BoardMemberDTO;

import java.util.List;

@Service
public class GetAllBoardMembersService  implements JavaDelegate {

    @Autowired
    private BoardMemberService boardMemberService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Usao u GetAllBoardMembers Service");
        List<BoardMemberDTO> boardMembersDTO = boardMemberService.getAllBoardMembers();

        System.out.println("Velicina boardMembersDTO niza: " + boardMembersDTO.size());

        execution.setVariable("boardMembers", boardMembersDTO);
        execution.setVariable("cycles", 0);
        System.out.println("U get all board members: " + execution.getVariable("boardMembers"));
    }

}
