/******************************************************************************************
 * StaffViewController.java
 *
 *      Provides an "endpoint" so that all user_types utilizing the "view staff" feature can
 *      a list of the staff.
 *
 * @author Matthew Kiyono
 * @since 12/7/2025
 * @version 1.0
 ******************************************************************************************/
package com.appointmentProject.backend.controller;

import com.appointmentProject.backend.model.Nurse;
import com.appointmentProject.backend.model.Provider;
import com.appointmentProject.backend.service.NurseService;
import com.appointmentProject.backend.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/staff-view")
@CrossOrigin(origins = "*")
public class StaffViewController {

    @Autowired
    private ProviderService providerService;

    @Autowired
    private NurseService nurseService;

    /**
     * Returns a combined list of providers and nurses.
     * Each row has: firstName, lastName, phone, email, specialty, type.
     * For nurses, specialty is always "Nurse".
     */
    @GetMapping("/all")
    public ResponseEntity<List<Map<String, String>>> getAllStaff() {

        List<Map<String, String>> response = new ArrayList<>();

        // --- Providers ---
        for (Provider p : providerService.getAllProviders()) {
            Map<String, String> row = new HashMap<>();
            row.put("firstName", p.getFirstName());
            row.put("lastName", p.getLastName());
            row.put("phone", p.getPhone());
            row.put("email", p.getEmail());
            row.put("specialty", p.getSpecialty());
            row.put("type", "PROVIDER");
            response.add(row);
        }

        // --- Nurses ---
        for (Nurse n : nurseService.getAllNurses()) {
            Map<String, String> row = new HashMap<>();
            row.put("firstName", n.getFirstName());
            row.put("lastName", n.getLastName());
            row.put("phone", n.getPhone());
            row.put("email", n.getEmail());
            row.put("specialty", "Nurse");   // <– requirement: show "Nurse" as specialty
            row.put("type", "NURSE");
            response.add(row);
        }

        return ResponseEntity.ok(response);
    }
}
