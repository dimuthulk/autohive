package lk.dimuthu.autohive.repository;

import lk.dimuthu.autohive.entity.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, String> {
    // Inquiry ID එකෙන් ඒකට ලැබිලා තියෙන සියලුම Quotes හොයාගන්න
    List<Quote> findByInquiryId(String inquiryId);

    List<Quote> findBySellerId(String sellerId);
    Optional<Quote> findByInquiryIdAndSellerId(String inquiryId, String sellerId);
}