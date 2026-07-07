package paymentDemo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MySpringConfiguration {
    @Bean("creditcard")
    public PaymentService creditcard(){
        return new CreditCard();
    }
    @Bean("debitcard")
    public PaymentService debitcard(){
        return new DebitCard();
    }
    @Bean("upi")
    public PaymentService upi(){
        return new upi();
    }
    @Bean("email")
    public NotificationService email(){
        return new Email();
    }
    @Bean("wa")
    public NotificationService wa(){
        return new wa();
    }
    @Bean("textmessage")
    public NotificationService textmessage(){
        return  new TextMessage();
    }
    //autowiring - auto dependency injection
    @Bean
    public ExpenseManager expenseManager(@Qualifier("upi") PaymentService pay,@Qualifier("wa") NotificationService ns){
        return new ExpenseManager(pay,ns);
    }

}
