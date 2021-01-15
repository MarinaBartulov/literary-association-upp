package team16.literaryassociation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.MembershipApplicationDTO;
import team16.literaryassociation.dto.MembershipApplicationInfoDTO;
import team16.literaryassociation.mapper.MembershipApplicationMapper;
import team16.literaryassociation.model.LiteraryWork;
import team16.literaryassociation.model.MembershipApplication;
import team16.literaryassociation.repository.MembershipApplicationRepository;
import team16.literaryassociation.services.interfaces.LiteraryWorkService;
import team16.literaryassociation.services.interfaces.MembershipApplicationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MembershipApplicationServiceImpl implements MembershipApplicationService {

    @Autowired
    private MembershipApplicationRepository membershipApplicationRepository;

    @Autowired
    private LiteraryWorkService literaryWorkService;

    private MembershipApplicationMapper membershipApplicationMapper = new MembershipApplicationMapper();

    @Override
    public MembershipApplication save(MembershipApplication membershipApplication) {
        return membershipApplicationRepository.save(membershipApplication);
    }

    @Override
    public MembershipApplication getOne(Long id) {
        return membershipApplicationRepository.getOne(id);
    }

    @Override
    public List<MembershipApplicationDTO> getAllActiveMembershipApplicationsDTO() {
        return membershipApplicationRepository.findAllActive().stream().map(ma -> membershipApplicationMapper.toDto(ma)).collect(Collectors.toList());
    }

    @Override
    public MembershipApplicationInfoDTO getMembershipApplicationInfoDTO(Long id) {

           MembershipApplication membershipApplication = membershipApplicationRepository.getOne(id);
           List<LiteraryWork> literaryWorks = literaryWorkService.findAllWithMembershipApplicationId(id);

           return new MembershipApplicationInfoDTO(membershipApplication, literaryWorks);
    }
}
