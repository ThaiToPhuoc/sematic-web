package vn.tinhoc.repository;

import org.springframework.stereotype.Repository;

import vn.lanhoang.ontology.repository.OntologyRepository;
import vn.tinhoc.domain.dto.User;

@Repository
public class UserRepository extends OntologyRepository<User> {

}
