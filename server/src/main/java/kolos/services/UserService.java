package kolos.services;

import kolos.models.UserModel;
import kolos.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserModel save(UserModel userModel) {
        return userRepository.save(userModel);
    }

    public UserModel findByUserNameAndPasswordAndNrIndeksu(String username, String password, Long nrIndeksu) {
        return userRepository.findByUsernameAndPasswordAndNrIndeksu(username, password, nrIndeksu);
    }
}
