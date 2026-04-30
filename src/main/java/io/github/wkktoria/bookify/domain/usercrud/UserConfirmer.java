package io.github.wkktoria.bookify.domain.usercrud;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserConfirmer {

    private final MailSender mailSender;
    private final UserRepository userRepository;

    public void sendConfirmationEmail(final User user) {
        String to = user.getEmail();
        String subject = "Complete your registration";
        String text = "To confirm your account, please click here: "
                + "https://localhost:8443/users/confirm?token=" + user.getConfirmationToken();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    @Transactional
    public boolean confirmUser(final String confirmationToken) {
        User user = userRepository.findByConfirmationToken(confirmationToken)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.confirm();
    }

}
