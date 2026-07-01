package org.northernarc.loan.security;

import org.northernarc.loan.model.Customer;
import org.northernarc.loan.repository.CustomerRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final CustomerRepository customerRepository;

	public CustomUserDetailsService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Customer customer = customerRepository.findByEmail(email);
		if (customer == null) {
			throw new UsernameNotFoundException("User not found: " + email);
		}

		String role = customer.getRole() == null ? "USER" : customer.getRole().toUpperCase();
		return new User(
				customer.getEmail(),
				customer.getPassword(),
				List.of(new SimpleGrantedAuthority("ROLE_" + role))
		);
	}
}
