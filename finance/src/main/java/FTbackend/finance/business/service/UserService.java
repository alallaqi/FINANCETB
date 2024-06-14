package FTbackend.finance.business.service;

import FTbackend.finance.data.domain.*;
import FTbackend.finance.data.repository.*;
import FTbackend.finance.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RoleRepository roleRepository;

    private final EmergencyFundRepository emergencyFundRepository;
    private final InvestmentRepository investmentRepository;
    private final LoanRepository loanRepository;
    private final MortgageRepository mortgageRepository;
    private final RetirementPlanRepository retirementPlanRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, RoleRepository roleRepository,
                       EmergencyFundRepository emergencyFundRepository, InvestmentRepository investmentRepository,
                       LoanRepository loanRepository, MortgageRepository mortgageRepository, RetirementPlanRepository retirementPlanRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.roleRepository = roleRepository;
        this.emergencyFundRepository = emergencyFundRepository;
        this.investmentRepository = investmentRepository;
        this.loanRepository = loanRepository;
        this.mortgageRepository = mortgageRepository;
        this.retirementPlanRepository = retirementPlanRepository;
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    public User registerNewUser(String username, String email, String password) {
        return registerNewUser(username, email, password, Set.of("ROLE_USER"));
    }

    @Transactional
    public User registerNewUser(String username, String email, String password, Set<String> roles) {
        userRepository.findByUsername(username)
                .or(() -> userRepository.findByEmail(email))
                .ifPresent(s -> {
                    throw new IllegalArgumentException("User already exists");
                });

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));

        Set<Role> userRoles = roles.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseGet(() -> roleRepository.save(new Role(roleName))))
                .collect(Collectors.toSet());

        newUser.setRoles(userRoles);

        return userRepository.save(newUser);
    }

    public void addAdminUser() {
        String username = "admin";
        String email = "admin@example.com";
        String password = passwordEncoder.encode("adminpassword"); // change to a secure password

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

        User adminUser = new User();
        adminUser.setUsername(username);
        adminUser.setEmail(email);
        adminUser.setPassword(password);
        adminUser.setRoles(Set.of(adminRole));

        userRepository.save(adminUser);
    }

    @Transactional
    public User updateUserProfile(Long id, String newUsername, String newEmail) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UsernameNotFoundException("User not found with id: " + id)
        );

        // Check if the username or email needs to be updated and isn't taken by another user
        if (!user.getUsername().equals(newUsername)) {
            userRepository.findByUsername(newUsername).ifPresent(s -> {
                throw new IllegalArgumentException("Username already exists");
            });
            user.setUsername(newUsername);
        }

        if (!user.getEmail().equals(newEmail)) {
            userRepository.findByEmail(newEmail).ifPresent(s -> {
                throw new IllegalArgumentException("Email already exists");
            });
            user.setEmail(newEmail);
        }

        return userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        String[] roles = user.getRoles().stream().map(Role::getName).toArray(String[]::new);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                true, true, true, true,
                AuthorityUtils.createAuthorityList(roles));
    }

    public Map<String, Object> loginUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        if (passwordEncoder.matches(password, user.getPassword())) {
            Map<String, Object> response = new HashMap<>();
            String token = jwtUtil.generateToken(user.getUsername());
            response.put("token", token);
            response.put("user", Map.of("id", user.getId(), "username", user.getUsername(), "email", user.getEmail()));
            return response;
        } else {
            throw new IllegalArgumentException("Incorrect username or password");
        }
    }

    @Transactional
    public void clearUserData(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getEmergencyFunds().clear(); // Clear the user's emergency funds
            user.getInvestments().clear(); // Clear the user's investments
            user.getLoans().clear(); // Clear the user's loans
            user.getMortgages().clear(); // Clear the user's mortgages
            user.getRetirementPlans().clear(); // Clear the user's retirement plans
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public Map<String, List<?>> getAllUserCalculations(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
        Map<String, List<?>> calculations = new HashMap<>();
        calculations.put("emergencyFunds", emergencyFundRepository.findByUserId(userId));
        calculations.put("investments", investmentRepository.findByUserId(userId));
        calculations.put("loans", loanRepository.findByUserId(userId));
        calculations.put("mortgages", mortgageRepository.findByUserId(userId));
        calculations.put("retirementPlans", retirementPlanRepository.findByUserId(userId));
        return calculations;
    }
}