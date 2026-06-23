import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MySpringConfiguration {
    @Bean("credit")
    public PaymentService cc() {
        return new CreditCard();
    }
    @Bean("debit")
    public PaymentService dd() {
        return new DebitCard();
    }

    @Bean("wa")
    public NotificationService wa(){
        return new Wa();
    }
    @Bean("email")
    public NotificationService e(){
        return new Email();
    }
}
