package lk.dimuthu.autohive.controller;

import lk.dimuthu.autohive.dto.request.VehicleRequest;
import lk.dimuthu.autohive.dto.response.VehicleResponse;
import lk.dimuthu.autohive.entity.User;
import lk.dimuthu.autohive.entity.Vehicle;
import lk.dimuthu.autohive.repository.UserRepository;
import lk.dimuthu.autohive.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<String> addVehicle(@RequestBody VehicleRequest request) {
        // Validate if the provided user ID exists in the database
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        if(optionalUser.isEmpty()){
            return ResponseEntity.badRequest().body("Error: This User ID is not valid!");
        }

        // Create a new Vehicle entity and associate it with the user
        Vehicle vehicle = new Vehicle();
        vehicle.setUser(optionalUser.get());
        vehicle.setMake(request.getMake());
        vehicle.setModel(request.getModel());
        vehicle.setYear(request.getYear());

        // Save the vehicle to the database
        vehicleRepository.save(vehicle);
        return ResponseEntity.ok("Vehicle successfully added to the system!");
    }

    @GetMapping("/my-vehicles/{userId}")
    public ResponseEntity<List<VehicleResponse>> getMyVehicles(@PathVariable String userId) {
        // Retrieve all vehicles belonging to the specified user
        List<Vehicle> vehicles = vehicleRepository.findByUser_Id(userId);

        // Convert each Vehicle entity to a VehicleResponse DTO
        List<VehicleResponse> responses = vehicles.stream().map(v -> new VehicleResponse(
                v.getId(),
                v.getUser().getId(),
                v.getMake(),
                v.getModel(),
                v.getYear()
        )).toList();

        return ResponseEntity.ok(responses);
    }
}
