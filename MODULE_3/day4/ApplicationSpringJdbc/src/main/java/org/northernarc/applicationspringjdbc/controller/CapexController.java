package org.northernarc.applicationspringjdbc.controller;

import org.northernarc.applicationspringjdbc.model.Capex;
import org.northernarc.applicationspringjdbc.service.CapexDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/capex")
public class CapexController {

    @Autowired
    public CapexDaoService capexDaoService;
    @PostMapping
    public ResponseEntity<Capex> addCapex(@RequestBody Capex capex) {
        return ResponseEntity.status(HttpStatus.CREATED).body(capexDaoService.addCapex(capex));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Capex> findById(@PathVariable Long id) {
        return ResponseEntity.ok(capexDaoService.findCapexById(id));
    }
    @GetMapping
    public ResponseEntity<Map<Long, Capex>> getAll() {
        return ResponseEntity.ok(capexDaoService.showCapex());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Capex> update(@PathVariable Long id, @RequestBody Capex capex) {
        return ResponseEntity.ok(capexDaoService.updateCapexDetails(id, capex));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        capexDaoService.deleteCapexById(id);
        return ResponseEntity.noContent().build();
    }
}

