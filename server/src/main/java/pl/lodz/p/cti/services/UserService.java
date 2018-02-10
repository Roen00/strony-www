package pl.lodz.p.cti.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.models.UserModel;
import pl.lodz.p.cti.models.CollectionObjectModel;
import pl.lodz.p.cti.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserModel save(UserModel userModel) {
        return userRepository.save(userModel);
    }

    public UserModel findByUserNameAndPasswordAndNrIndeksu(String username, String password, Long nrIndeksu) {
        return userRepository.findByUsernameAndPasswordAndNrIndeksu(username,password,nrIndeksu);
    }
}
