package lk.dimuthu.autohive.repository;

import lk.dimuthu.autohive.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    // Find all vehicles belonging to a specific user by their user ID
    List<Vehicle> findByUser_Id(String userId);
}