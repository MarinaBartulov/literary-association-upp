package team16.literaryassociation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.MembershipApplication;
import team16.literaryassociation.repository.MembershipApplicationRepository;
import team16.literaryassociation.services.MembershipApplicationService;

@Service
public class MembershipApplicationServiceImpl implements MembershipApplicationService {

    @Autowired
    private MembershipApplicationRepository membershipApplicationRepository;

    @Override
    public MembershipApplication save(MembershipApplication membershipApplication) {
        return membershipApplicationRepository.save(membershipApplication);
    }
}
