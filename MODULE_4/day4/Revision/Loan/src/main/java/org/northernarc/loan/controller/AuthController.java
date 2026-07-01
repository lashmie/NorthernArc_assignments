package org.northernarc.loan.controller;

import jakarta.validation.Valid;
import org.northernarc.loan.dto.LoginRequestDTO;
import org.northernarc.loan.dto.LoginResponseDTO;
import org.northernarc.loan.exception.BusinessRuleException;
import org.northernarc.loan.model.Customer;
import org.northernarc.loan.repository.CustomerRepository;
import org.northernarc.loan.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"", "/api/auth"})
public class AuthController {

	private final CustomerRepository customerRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public AuthController(CustomerRepository customerRepository,
						  PasswordEncoder passwordEncoder,
						  JwtUtil jwtUtil) {
		this.customerRepository = customerRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
		Customer customer = customerRepository.findByEmail(request.getEmail());
		if (customer == null) {
			throw new BusinessRuleException("Invalid email or password");
		}

		boolean passwordMatches = passwordEncoder.matches(request.getPassword(), customer.getPassword())
				|| request.getPassword().equals(customer.getPassword());

		if (!passwordMatches) {
			throw new BusinessRuleException("Invalid email or password");
		}

		String role = customer.getRole() == null ? "USER" : customer.getRole().toUpperCase();
		String token = jwtUtil.generateToken(customer.getEmail(), role);

		LoginResponseDTO response = LoginResponseDTO.builder()
				.token(token)
				.tokenType("Bearer")
				.email(customer.getEmail())
				.role(role)
				.build();

		return ResponseEntity.ok(response);
	}
}
