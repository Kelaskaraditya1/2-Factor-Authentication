package com.starkIndustries.RoleBasedAuthorization.auth.repository;

import com.starkIndustries.RoleBasedAuthorization.auth.modles.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    public Employee findByUsername(String username);

}
