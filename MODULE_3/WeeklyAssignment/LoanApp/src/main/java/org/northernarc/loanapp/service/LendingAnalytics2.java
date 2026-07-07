package org.northernarc.loanapp.service;

import org.northernarc.loanapp.model.LoanApplication;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LendingAnalytics2 {

    private Map<String, LoanApplication> applications =
            new HashMap<>();

    public void loadApplications(List<String> records) {

        if(records == null) {
            return;
        }

        records.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .forEach(record -> {

                    String[] p = record.split("\\|");

                    if(p.length != 6) {
                        return;
                    }

                    String applicationId = p[0].trim();
                    String customerName = p[1].trim();
                    String lenderName = p[2].trim();
                    String loanType = p[3].trim();

                    double loanAmount;
                    int creditScore;

                    try {
                        loanAmount = Double.parseDouble(p[4].trim());
                        creditScore = Integer.parseInt(p[5].trim());
                    }
                    catch(Exception e) {
                        return;
                    }

                    if(applicationId.isEmpty()
                            || customerName.isEmpty()
                            || lenderName.isEmpty()
                            || loanType.isEmpty()
                            || loanAmount <= 0
                            || creditScore < 300
                            || creditScore > 900) {
                        return;
                    }

                    LoanApplication current =
                            new LoanApplication(
                                    applicationId,
                                    customerName,
                                    lenderName,
                                    loanType,
                                    loanAmount,
                                    creditScore
                            );

                    applications.merge(
                            applicationId,
                            current,
                            (oldApp,newApp) -> {

                                if(newApp.getCreditScore() > oldApp.getCreditScore()) {
                                    return newApp;
                                }

                                if(newApp.getCreditScore() < oldApp.getCreditScore()) {
                                    return oldApp;
                                }

                                if(newApp.getLoanAmount() < oldApp.getLoanAmount()) {
                                    return newApp;
                                }

                                if(newApp.getLoanAmount() > oldApp.getLoanAmount()) {
                                    return oldApp;
                                }

                                return newApp.getCustomerName()
                                        .compareTo(oldApp.getCustomerName()) < 0
                                        ? newApp
                                        : oldApp;
                            });
                });
    }

    public List<LoanApplication> topCreditProfiles(int n) {

        return applications.values()
                .stream()
                .sorted(
                        Comparator
                                .comparingInt(
                                        LoanApplication::getCreditScore)
                                .reversed()
                                .thenComparingDouble(
                                        LoanApplication::getLoanAmount)
                                .thenComparing(
                                        LoanApplication::getCustomerName))
                .limit(n)
                .collect(Collectors.toList());
    }

    public Map<String, Double> averageLoanAmountByType() {

        return applications.values()
                .stream()
                .collect(Collectors.groupingBy(
                        LoanApplication::getLoanType,
                        TreeMap::new,
                        Collectors.collectingAndThen(
                                Collectors.averagingDouble(
                                        LoanApplication::getLoanAmount),
                                avg -> Math.round(avg * 100.0) / 100.0
                        )));
    }

    public Optional<LoanApplication> highestLoanApplication() {

        return applications.values()
                .stream()
                .max(
                        Comparator
                                .comparingDouble(
                                        LoanApplication::getLoanAmount)
                                .thenComparingInt(
                                        LoanApplication::getCreditScore)
                                .thenComparing(
                                        LoanApplication::getApplicationId,
                                        Comparator.reverseOrder()
                                )
                );
    }

    public Set<String> lendersWithMultipleLoanTypes() {

        return applications.values()
                .stream()
                .collect(Collectors.groupingBy(
                        LoanApplication::getLenderName,
                        Collectors.mapping(
                                LoanApplication::getLoanType,
                                Collectors.toSet()
                        )))
                .entrySet()
                .stream()
                .filter(e -> e.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public Map<String,List<LoanApplication>> groupApplicationsByLender() {

        return applications.values()
                .stream()
                .collect(Collectors.groupingBy(
                        LoanApplication::getLenderName,
                        TreeMap::new,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(
                                                Comparator
                                                        .comparingInt(
                                                                LoanApplication::getCreditScore)
                                                        .reversed()
                                                        .thenComparingDouble(
                                                                LoanApplication::getLoanAmount))
                                        .collect(Collectors.toList())
                        )))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a,b)->a,
                        LinkedHashMap::new
                ));
    }

    public List<String> suspiciousApplications() {

        Collection<LoanApplication> apps =
                applications.values();

        Map<String, Double> avgAmountByType =
                apps.stream()
                        .collect(Collectors.groupingBy(
                                LoanApplication::getLoanType,
                                Collectors.averagingDouble(
                                        LoanApplication::getLoanAmount)));

        Map<String, Double> avgCreditByType =
                apps.stream()
                        .collect(Collectors.groupingBy(
                                LoanApplication::getLoanType,
                                Collectors.averagingInt(
                                        LoanApplication::getCreditScore)));

        Set<String> condition6Customers =
                apps.stream()
                        .collect(Collectors.groupingBy(
                                a -> a.getCustomerName()
                                        .trim()
                                        .toLowerCase(),
                                Collectors.mapping(
                                        LoanApplication::getLenderName,
                                        Collectors.toSet())))
                        .entrySet()
                        .stream()
                        .filter(e -> e.getValue().size() > 3)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet());

        Set<String> condition7Customers =
                apps.stream()
                        .collect(Collectors.groupingBy(
                                a ->
                                        a.getLoanType().toLowerCase()
                                                + "|"
                                                + a.getLoanAmount()
                                                + "|"
                                                + a.getCreditScore()))
                        .values()
                        .stream()
                        .filter(list ->
                                list.stream()
                                        .map(a -> a.getCustomerName()
                                                .toLowerCase())
                                        .distinct()
                                        .count() > 1)
                        .flatMap(List::stream)
                        .map(LoanApplication::getCustomerName)
                        .collect(Collectors.toSet());

        Set<String> condition8Customers =
                apps.stream()
                        .collect(Collectors.groupingBy(
                                LoanApplication::getLenderName))
                        .values()
                        .stream()
                        .flatMap(list ->
                                list.stream()
                                        .collect(Collectors.groupingBy(
                                                a ->
                                                        a.getCustomerName()
                                                                .replaceAll("\\s+","")
                                                                .toLowerCase()
                                                                .chars()
                                                                .sorted()
                                                                .mapToObj(
                                                                        c -> String.valueOf((char)c))
                                                                .collect(Collectors.joining())
                                        ))
                                        .values()
                                        .stream()
                                        .filter(g -> g.size() > 1)
                                        .flatMap(List::stream))
                        .map(LoanApplication::getCustomerName)
                        .collect(Collectors.toSet());

        return apps.stream()
                .filter(app -> {

                    String customer =
                            app.getCustomerName().trim();

                    String lender =
                            app.getLenderName().trim();

                    String[] words =
                            customer.toLowerCase()
                                    .split("\\s+");

                    boolean condition1 =
                            java.util.stream.IntStream
                                    .range(0, words.length - 1)
                                    .anyMatch(i ->
                                            words[i]
                                                    .equals(words[i + 1]));

                    boolean condition2 =
                            customer.toLowerCase()
                                    .contains(
                                            lender.toLowerCase());

                    boolean condition3 =
                            app.getLoanAmount()
                                    > avgAmountByType.getOrDefault(
                                            app.getLoanType(),
                                            0.0) * 2.5;

                    boolean condition4 =
                            app.getCreditScore()
                                    < avgCreditByType.getOrDefault(
                                            app.getLoanType(),
                                            0.0)
                                    &&
                                    app.getLoanAmount()
                                            > avgAmountByType.getOrDefault(
                                            app.getLoanType(),
                                            0.0);

                    boolean condition5 =
                            words.length > 3;

                    boolean condition6 =
                            condition6Customers.contains(
                                    customer.toLowerCase());

                    boolean condition7 =
                            condition7Customers.contains(
                                    customer);

                    boolean condition8 =
                            condition8Customers.contains(
                                    customer);

                    return condition1
                            || condition2
                            || condition3
                            || condition4
                            || condition5
                            || condition6
                            || condition7
                            || condition8;
                })
                .map(LoanApplication::getCustomerName)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public Map<String,
            Map<String,
                    Optional<LoanApplication>>>
    loanTypeWiseTopApplicantByLender() {

        return applications.values()
                .stream()
                .collect(Collectors.groupingBy(
                        LoanApplication::getLoanType,
                        Collectors.groupingBy(
                                LoanApplication::getLenderName,
                                Collectors.collectingAndThen(
                                        Collectors.maxBy(
                                                Comparator
                                                        .comparingInt(
                                                                LoanApplication::getCreditScore)
                                                        .thenComparingDouble(
                                                                LoanApplication::getLoanAmount)
                                        ),
                                        Function.identity()
                                )
                        )
                ));
    }
}

