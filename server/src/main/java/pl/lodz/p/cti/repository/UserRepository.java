package pl.lodz.p.cti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.cti.models.TvModel;
import pl.lodz.p.cti.models.UserModel;

@Transactional
public interface UserRepository extends CrudRepository<UserModel, Long> {
    UserModel findByUsernameAndPasswordAndNrIndeksu(String username, String password, Long nrIndeksu);
}
