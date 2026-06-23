
\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\package org.northernarc.capexwithapi.controller;

import org.northernarc.capexwithapi.dao.CapexDao;
import org.northernarc.capexwithapi.model.Capex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
@RequestMapping("/api/capex")
public class CapexController {
    @Autowired
    CapexDao capexe;

    @PostMapping
    public ResponseEntity save(@RequestBody Capex capex) {
        capexe.save(capex);
        return ResponseEntity
                .status(HttpStatus.CREATED) // 201
                .body("Capex created successfully");
    }
    @PutMapping
    public ResponseEntity<Capex> update(@PathVariable int id, @RequestBody Capex capex) {
        capexe.update(id, capex);
        return ResponseEntity.ok(capex);
    }

    @GetMapping
    public ResponseEntity findById(@PathVariable int id) {
        Capex capex = capexe.findById(id);
        return ResponseEntity.ok(capex);
    }

    @DeleteMapping("/capex/{id}")
    public ResponseEntity<String> deleteCapex(@PathVariable int id) {
        capexe.deleteByFinancialId(id);
        return ResponseEntity
                .ok("Capex deleted successfully");
    }

}
