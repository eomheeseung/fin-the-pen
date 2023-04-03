package project.fin_the_pen.codefAPI.repository;

import org.springframework.stereotype.Repository;
import project.fin_the_pen.data.token.Token;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class TokenRepository {
    @PersistenceContext
    EntityManager entityManager;

    public void init(String accessToken) {
        Token token = new Token();
        token.setAccessToken(accessToken);

        entityManager.persist(token);
    }

    public List<Token> findToken() {
        List<Token> list = entityManager.createQuery("select t from Token t", Token.class).getResultList();
        return list;
    }

    public Token findOneToken() {
        Token token = entityManager.createQuery("select t from Token t", Token.class)
                .getSingleResult();
        return token;
    }
}
