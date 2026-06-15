package lk.dimuthu.autohive.repository;

import lk.dimuthu.autohive.entity.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, String> {
}