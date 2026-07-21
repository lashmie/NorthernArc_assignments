package org.northernarc.assessment4.repository;

import org.northernarc.assessment4.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Task 3: Derived Query Method
    List<Customer> findByBranch(String branch);

    @Query("""
            SELECT DISTINCT c
            FROM Customer c
            JOIN c.accounts a
            GROUP BY c
            HAVING SUM(a.balance) > :threshold
            """)
    List<Customer> findRichCustomers(@Param("threshold") double threshold);

    @Query("""
            SELECT c.branch, COALESCE(SUM(a.balance), 0)
            FROM Customer c
            LEFT JOIN c.accounts a
            GROUP BY c.branch
            """)
    List<Object[]> findTotalBalancePerBranch();

    @Query("""
            SELECT c
            FROM Customer c
            JOIN c.accounts a
            GROUP BY c
            HAVING COUNT(a) > 1
            """)
    List<Customer> findCustomersWithMultipleAccounts();

    @Query(value = """
            SELECT
                (SELECT COUNT(*) FROM test_customers) AS total_customers,
                (SELECT COUNT(*) FROM test_accounts) AS total_accounts,
                COALESCE((SELECT SUM(a.balance) FROM test_accounts a), 0) AS total_balance,
                COALESCE((
                    SELECT b.branch
                    FROM (
                        SELECT c.branch, COALESCE(SUM(a.balance), 0) AS branch_total
                        FROM test_customers c
                        LEFT JOIN test_accounts a ON a.customer_id = c.customer_id
                        GROUP BY c.branch
                        ORDER BY branch_total DESC, c.branch ASC
                        LIMIT 1
                    ) b
                ), 'N/A') AS top_branch,
                COALESCE((
                    SELECT h.customer_name
                    FROM (
                        SELECT c.customer_name, COALESCE(SUM(a.balance), 0) AS customer_total
                        FROM test_customers c
                        LEFT JOIN test_accounts a ON a.customer_id = c.customer_id
                        GROUP BY c.customer_id, c.customer_name
                        ORDER BY customer_total DESC, c.customer_name ASC
                        LIMIT 1
                    ) h
                ), 'N/A') AS highest_balance_customer
            """, nativeQuery = true)
    List<Object[]> findDashboardMetrics();


    // Security Helper
    java.util.Optional<Customer> findByEmail(String email);
}
