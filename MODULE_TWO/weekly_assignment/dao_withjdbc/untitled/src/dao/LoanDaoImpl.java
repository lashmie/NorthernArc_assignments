package dao;

import Connection.DBmanager;
import entity.Loan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoanDaoImpl implements LoanDao{
    @Override
    public void save(Loan loan) {

        try(Connection conn = DBmanager.getConnection()) {

            String sql =
                    "INSERT INTO loan(loan_id, customer_name, loan_amount, interest_rate, tenure_months) VALUES(?,?,?,?,?)";

            PreparedStatement stmt =
                    conn.prepareStatement(sql);

            stmt.setInt(1, loan.getLoanId());
            stmt.setString(2, loan.getCustomerName());
            stmt.setDouble(3, loan.getLoanAmount());
            stmt.setDouble(4, loan.getInterestRate());
            stmt.setInt(5, loan.getTenureMonths());

            stmt.executeUpdate();

            System.out.println("Loan saved successfully");

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Loan> findAll() {

        List<Loan> loans = new ArrayList<>();

        try(Connection conn = DBmanager.getConnection()) {

            String sql = "SELECT * FROM loan";

            PreparedStatement stmt =
                    conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {

                Loan loan = new Loan(
                        rs.getInt("loan_id"),
                        rs.getString("customer_name"),
                        rs.getDouble("loan_amount"),
                        rs.getDouble("interest_rate"),
                        rs.getInt("tenure_months")
                );

                loans.add(loan);
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return loans;
    }

    @Override
    public void update(Loan loan) {

        try(Connection conn = DBmanager.getConnection()) {

            String sql =
                    "UPDATE loan SET customer_name=?, loan_amount=?, interest_rate=?, tenure_months=? WHERE loan_id=?";

            PreparedStatement stmt =
                    conn.prepareStatement(sql);

            stmt.setString(1, loan.getCustomerName());
            stmt.setDouble(2, loan.getLoanAmount());
            stmt.setDouble(3, loan.getInterestRate());
            stmt.setInt(4, loan.getTenureMonths());
            stmt.setInt(5, loan.getLoanId());

            stmt.executeUpdate();

            System.out.println("Loan updated successfully");

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteById(int loanId) {

        try(Connection conn = DBmanager.getConnection()) {

            String sql =
                    "DELETE FROM loan WHERE loan_id=?";

            PreparedStatement stmt =
                    conn.prepareStatement(sql);

            stmt.setInt(1, loanId);

            stmt.executeUpdate();

            System.out.println("Loan deleted successfully");

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteAll() {

        try (Connection conn = DBmanager.getConnection()) {

            String sql = "DELETE FROM loan";

            PreparedStatement stmt = conn.prepareStatement(sql);

            int rows = stmt.executeUpdate();

            System.out.println(rows + " loans deleted successfully");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
