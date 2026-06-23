package org.northernarc.loanapp.service;
import org.northernarc.loanapp.model.LoanApplication;

import java.util.*;
import java.util.stream.Collectors;

public class LendingAnalytics {

    private Map<String, LoanApplication> applications =
            new HashMap<>();

    public void loadApplications(List<String> records) {

        if (records == null) return;

        Map<String, LoanApplication> temp = new HashMap<>();

        for (String record : records) {

            if (record == null || record.trim().isEmpty()) continue;

            String[] parts = record.split("\\|");

            if (parts.length != 6) continue;

            String appId = parts[0].trim();
            String customer = parts[1].trim();
            String lender = parts[2].trim();
            String loanType = parts[3].trim();
            String loanAmountStr = parts[4].trim();
            String creditScoreStr = parts[5].trim();

            if (appId.isEmpty() || customer.isEmpty() || lender.isEmpty() || loanType.isEmpty())
                continue;

            double loanAmount;
            int creditScore;

            try {
                loanAmount = Double.parseDouble(loanAmountStr);
                creditScore = Integer.parseInt(creditScoreStr);
            } catch (Exception e) {
                continue;
            }

            if (loanAmount <= 0 || creditScore < 300 || creditScore > 900)
                continue;

            LoanApplication newApp = new LoanApplication(
                    appId, customer, lender, loanType, loanAmount, creditScore
            );

            // RULE 1: duplicate resolution
            temp.merge(appId, newApp, (oldApp, newApp2) -> {

                if (newApp2.getCreditScore() != oldApp.getCreditScore()) {
                    return newApp2.getCreditScore() > oldApp.getCreditScore() ? newApp2 : oldApp;
                }

                if (newApp2.getLoanAmount() != oldApp.getLoanAmount()) {
                    return newApp2.getLoanAmount() < oldApp.getLoanAmount() ? newApp2 : oldApp;
                }

                return newApp2.getCustomerName().compareTo(oldApp.getCustomerName()) < 0
                        ? newApp2 : oldApp;
            });
        }

        this.applications = temp;
    }

    public List<LoanApplication> topCreditProfiles(int n) {
        return applications.values().stream()
                .sorted(Comparator.comparingInt(LoanApplication::getCreditScore).reversed()
                        .thenComparing(LoanApplication::getLoanAmount)
                        .thenComparing(LoanApplication::getCustomerName))
                .limit(n)
                .toList();
    }

    public Map<String, Double> averageLoanAmountByType() {

        return applications.values().stream()
                .collect(Collectors.groupingBy(
                        LoanApplication::getLoanType,
                        TreeMap::new,
                        Collectors.collectingAndThen(
                                Collectors.averagingDouble(LoanApplication::getLoanAmount),
                                avg -> Math.round(avg * 100.0) / 100.0
                        )
                ));
    }

    public Optional<LoanApplication> highestLoanApplication() {

        return applications.values().stream()
                .max(Comparator.comparingDouble(LoanApplication::getLoanAmount)
                        .thenComparing(LoanApplication::getCreditScore)
                        .thenComparing(LoanApplication::getApplicationId, Comparator.reverseOrder()));
    }

    public Set<String> lendersWithMultipleLoanTypes() {


        return applications.values().stream()
                .collect(Collectors.groupingBy(
                        LoanApplication::getLenderName,
                        Collectors.mapping(LoanApplication::getLoanType, Collectors.toSet())
                ))
                .entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public Map<String,List<LoanApplication>> groupApplicationsByLender() {
        return applications.values().stream()
                .collect(Collectors.groupingBy(
                        LoanApplication::getLenderName,
                        TreeMap::new,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparingInt(LoanApplication::getCreditScore).reversed()
                                                .thenComparing(LoanApplication::getLoanAmount))
                                        .toList()
                        )
                ));
    }

    public List<String> suspiciousApplications() {
//        Map<String, Double> avgLoanByType = applications.values().stream()
//                .collect(Collectors.groupingBy(
//                        LoanApplication::getLoanType,
//                        Collectors.averagingDouble(LoanApplication::getLoanAmount)
//                ));
//
//        Map<String, Double> avgScoreByType = applications.values().stream()
//                .collect(Collectors.groupingBy(
//                        LoanApplication::getLoanType,
//                        Collectors.averagingInt(LoanApplication::getCreditScore)
//                ));
//
//        Map<String, Long> customerLenderCount = applications.values().stream()
//                .collect(Collectors.groupingBy(
//                        LoanApplication::getCustomerName,
//                        Collectors.mapping(LoanApplication::getLenderName, Collectors.toSet())
//                ))
//                .entrySet().stream()
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        e -> (long) e.getValue().size()
//                ));
//
//        Map<String, List<LoanApplication>> sameAmountScoreGroup =
//                applications.values().stream()
//                        .collect(Collectors.groupingBy(
//                                a -> a.getLoanType() + "_" + a.getLoanAmount() + "_" + a.getCreditScore()
//                        ));
//
//        Set<String> suspicious = applications.values().stream()
//                .filter(app -> {
//
//                    String name = app.getCustomerName().trim().toLowerCase();
//                    String lender = app.getLenderName().trim().toLowerCase();
//
//                    // Condition 1: repeated consecutive words
//                    String[] words = name.split("\\s+");
//                    for (int i = 0; i < words.length - 1; i++) {
//                        if (words[i].equalsIgnoreCase(words[i + 1]))
//                            return true;
//                    }
//
//                    // Condition 2: lender inside customer name
//                    if (name.contains(lender)) return true;
//
//                    // Condition 3
//                    double avgLoan = avgLoanByType.getOrDefault(app.getLoanType(), 0.0);
//                    if (avgLoan > 0 && app.getLoanAmount() > 2.5 * avgLoan)
//                        return true;
//
//                    // Condition 4
//                    double avgScore = avgScoreByType.getOrDefault(app.getLoanType(), 0.0);
//                    if (app.getCreditScore() < avgScore &&
//                            app.getLoanAmount() > avgLoan)
//                        return true;
//
//                    // Condition 5
//                    if (app.getCustomerName().trim().split("\\s+").length > 3)
//                        return true;
//
//                    // Condition 6
//                    if (customerLenderCount.getOrDefault(app.getCustomerName(), 0L) > 3)
//                        return true;
//
//                    // Condition 7
//                    List<LoanApplication> group =
//                            sameAmountScoreGroup.get(
//                                    app.getLoanType() + "_" + app.getLoanAmount() + "_" + app.getCreditScore()
//                            );
//
//                    if (group != null && group.size() > 1) return true;
//
//                    // Condition 8 (anagram within same lender)
//                    return applications.values().stream()
//                            .filter(a -> a != app && a.getLenderName().equalsIgnoreCase(app.getLenderName()))
//                            .anyMatch(a -> {
//                                String s1 = Arrays.stream(app.getCustomerName().toLowerCase().split("\\s+"))
//                                        .sorted().collect(Collectors.joining());
//
//                                String s2 = Arrays.stream(a.getCustomerName().toLowerCase().split("\\s+"))
//                                        .sorted().collect(Collectors.joining());
//
//                                return s1.equals(s2);
//                            });
//                })
//                .map(a -> a.getCustomerName().trim())
//                .collect(Collectors.toCollection(TreeSet::new));
//
//        return new ArrayList<>(suspicious);
//    }
        // Average loan by type
        Map<String, Double> avgLoanByType =
                applications.values().stream()
                        .collect(Collectors.groupingBy(
                                LoanApplication::getLoanType,
                                Collectors.averagingDouble(LoanApplication::getLoanAmount)
                        ));

        // Average credit score by type
        Map<String, Double> avgScoreByType =
                applications.values().stream()
                        .collect(Collectors.groupingBy(
                                LoanApplication::getLoanType,
                                Collectors.averagingInt(LoanApplication::getCreditScore)
                        ));

        // Count of lenders per customer
        Map<String, Long> customerLenderCount =
                applications.values().stream()
                        .collect(Collectors.groupingBy(
                                LoanApplication::getCustomerName,
                                Collectors.mapping(
                                        LoanApplication::getLenderName,
                                        Collectors.toSet()
                                )
                        ))
                        .entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> (long) e.getValue().size()
                        ));

        // Group by (loanType + amount + score)
        Map<String, Long> duplicatePattern =
                applications.values().stream()
                        .collect(Collectors.groupingBy(
                                a -> a.getLoanType() + "|" + a.getLoanAmount() + "|" + a.getCreditScore(),
                                Collectors.counting()
                        ));

        // FINAL STREAM
        return applications.values().stream()

                .filter(app ->

                        // Condition 1: repeated consecutive words
                        Arrays.stream(app.getCustomerName().trim().toLowerCase().split("\\s+"))
                                .reduce((a, b) -> a.equals(b) ? "MATCH" : b)
                                .orElse("")
                                .equals("MATCH")

                                ||

                                // Condition 2: lender inside customer name
                                app.getCustomerName().toLowerCase()
                                        .contains(app.getLenderName().toLowerCase())

                                ||

                                // Condition 3: 2.5x average loan
                                app.getLoanAmount() >
                                        2.5 * avgLoanByType.getOrDefault(app.getLoanType(), 0.0)

                                ||

                                // Condition 4: low score + high loan
                                (app.getCreditScore() <
                                        avgScoreByType.getOrDefault(app.getLoanType(), 0.0)
                                        &&
                                        app.getLoanAmount() >
                                                avgLoanByType.getOrDefault(app.getLoanType(), 0.0))

                                ||

                                // Condition 5: more than 3 words
                                app.getCustomerName().trim().split("\\s+").length > 3

                                ||

                                // Condition 6: more than 3 lenders
                                customerLenderCount.getOrDefault(app.getCustomerName(), 0L) > 3

                                ||

                                // Condition 7: duplicate loan pattern
                                duplicatePattern.get(
                                        app.getLoanType() + "|" + app.getLoanAmount() + "|" + app.getCreditScore()
                                ) > 1

                                ||

                                // Condition 8: anagram check (STREAM ONLY)
                                applications.values().stream()
                                        .filter(other ->
                                                !other.getApplicationId().equals(app.getApplicationId())
                                                        && other.getLenderName().equalsIgnoreCase(app.getLenderName())
                                        )
                                        .anyMatch(other ->
                                                Arrays.stream(app.getCustomerName().toLowerCase().split("\\s+"))
                                                        .sorted()
                                                        .collect(Collectors.joining())
                                                        .equals(
                                                                Arrays.stream(other.getCustomerName().toLowerCase().split("\\s+"))
                                                                        .sorted()
                                                                        .collect(Collectors.joining())
                                                        )
                                        )
                )

                .map(a -> a.getCustomerName().trim())

                .collect(Collectors.toCollection(TreeSet::new))

                .stream()
                .toList();}
    }


