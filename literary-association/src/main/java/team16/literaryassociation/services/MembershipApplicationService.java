package team16.literaryassociation.services;

import team16.literaryassociation.dto.MembershipApplicationDTO;
import team16.literaryassociation.dto.MembershipApplicationInfoDTO;
import team16.literaryassociation.model.MembershipApplication;

import java.util.List;

public interface MembershipApplicationService {

    MembershipApplication save(MembershipApplication membershipApplication);
    MembershipApplication getOne(Long id);
    List<MembershipApplicationDTO> getAllActiveMembershipApplicationsDTO();
    MembershipApplicationInfoDTO getMembershipApplicationInfoDTO(Long id);
 }
