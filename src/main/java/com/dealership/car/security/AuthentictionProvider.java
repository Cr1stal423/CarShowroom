package com.dealership.car.security;

import com.dealership.car.model.Person;
import com.dealership.car.model.Roles;
import com.dealership.car.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * AuthenticationProvider implementation that authenticates a user against a PersonRepository.
 * This class is annotated with @Component to be discovered by Spring's component scanning.
 * It implements the authenticate method of the AuthenticationProvider interface to provide
 * a custom authentication mechanism.
 */
@Component
public class AuthentictionProvider implements AuthenticationProvider {
    @Autowired
    private PersonRepository personRepository;
    /**
     * Authenticates a user based on their username and password.
     * This method retrieves the user from the repository, checks the credentials,
     * and returns an authentication token if the credentials are valid.
     *
     * @param authentication The authentication request containing the username and password.
     * @return An authentication token if the credentials are valid.
     * @throws AuthenticationException If the authentication fails due to invalid credentials.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        Person person = personRepository.findByUsername(username);
        if (person != null && person.getPersonId() > 0 && person.getKeys().getPassword().equals(pwd)){
            return new UsernamePasswordAuthenticationToken(username,null, getGrantedAuthority(person.getRoles()));
        } else {
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    /**
     * Determines if this AuthenticationProvider supports the specified authentication token type.
     *
     * @param authentication the class of the authentication token
     * @return true if the authentication token is of type UsernamePasswordAuthenticationToken, false otherwise
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    /**
     * Creates a list of granted authorities based on the provided roles.
     *
     * @param roles the roles associated with the user
     * @return a list of GrantedAuthority objects corresponding to the roles
     */
    private List<GrantedAuthority> getGrantedAuthority(Roles roles){
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+roles.getRoleName()));
        return grantedAuthorities;
    }


}
