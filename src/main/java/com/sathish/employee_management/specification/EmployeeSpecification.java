package com.sathish.employee_management.specification;


import com.sathish.employee_management.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {

    public static Specification<Employee> hasSearch(String search) {
        return ((root, query, criteriaBuilder) -> {
            if(search == null || search.isBlank()){
                return null;
            }

            String searchValue = "%" + search.trim().toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), searchValue),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), searchValue),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchValue),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("department")), searchValue)
            );
        });
    }

    public static Specification<Employee> hasDepartment(String department) {


        return ((root, query, criteriaBuilder) -> {
            if(department == null || department.isBlank()){
                return null;
            }
            return criteriaBuilder.equal(criteriaBuilder.lower(root.get("department")), department.trim().toLowerCase());
        });
    }

    public static Specification<Employee> salaryGreaterThanOrEqual(Double minSalary) {
        return ((root, query, criteriaBuilder) -> {
            if (minSalary == null) {
                return null;
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("salary"), minSalary);
        });
    }

    public static Specification<Employee> salaryLessThanOrEqual(Double maxSalary) {
        return ((root, query, criteriaBuilder) -> {
            if (maxSalary == null) {
                return null;
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("salary"), maxSalary);
        });
    }
}