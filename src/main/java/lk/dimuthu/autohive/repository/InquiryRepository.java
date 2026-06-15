package lk.dimuthu.autohive.repository;

import lk.dimuthu.autohive.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, String> {
    // පාරිභෝගිකයාගේ ID එකෙන් ඔහුගේ සියලුම Inquiries හොයාගන්න
    List<Inquiry> findByUserId(String userId);

    // අලුතින් එකතු කරන එක: Open සහ Pending තත්ත්වයේ ඇති සියලුම ඉල්ලුම් ලබා ගැනීමට
    List<Inquiry> findByInquiryTypeAndStatus(String inquiryType, String status);
}