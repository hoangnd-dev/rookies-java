package nashtech.rookies.jpa.service.impl;

import org.springframework.stereotype.Service;

import nashtech.rookies.jpa.entity.Author;
import nashtech.rookies.jpa.repository.AuthorRepository;
import nashtech.rookies.jpa.service.AuthorService;

@Service
public class AuthorServiceImpl extends ServiceImpl<Author, Long> implements AuthorService {

    public AuthorServiceImpl (AuthorRepository repository) {
        super(repository);
    }
}
