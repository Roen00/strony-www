package kolos.repositories;

import kolos.models.UserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends CrudRepository<UserModel, Long> {
    UserModel findByUsernameAndPasswordAndNrIndeksu(String username, String password, Long nrIndeksu);

    UserModel findByPasswordAndNrIndeksu(String password, Long nrIndeksu);

}
