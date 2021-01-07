package team16.literaryassociation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.Role;
import team16.literaryassociation.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findByName(String name) {
        return this.roleRepository.findByName(name);
    }
}
