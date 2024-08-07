package com.tinqinacademy.authentication.core.processors;

import com.tinqinacademy.authentication.api.errors.ErrorOutput;
import com.tinqinacademy.authentication.api.operations.sendconfirmemail.SendConfirmEmail;
import com.tinqinacademy.authentication.api.operations.sendconfirmemail.SendConfirmEmailInput;
import com.tinqinacademy.authentication.api.operations.sendconfirmemail.SendConfirmEmailOutput;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;
import java.util.Map;

import static io.vavr.API.Match;

@Service
@Slf4j
public class SendConfirmEmailProcessor extends BaseProcessor implements SendConfirmEmail {
    private final JavaMailSender sender;
    private final TemplateEngine engine;
    @Value("${email.from}")
    private String emailSender;


    public SendConfirmEmailProcessor(ConversionService conversionService, Validator validator,
                                     JavaMailSender sender, TemplateEngine engine) {
        super(conversionService, validator);
        this.sender = sender;
        this.engine = engine;
    }

    @Override
    public Either<ErrorOutput, SendConfirmEmailOutput> process(SendConfirmEmailInput input) {
        log.info("Start sendConfirmEmail {}", input);
        return Try.of(() -> {
            Context context = new Context(Locale.ENGLISH, Map.of("firstName",input.getRecipient(),
                    "code", input.getCode()));
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setFrom(emailSender);
            helper.setText(engine.process("email-confirmation", context), true);
            helper.setTo(input.getRecipient());
            helper.setSubject(input.getSubject());
            sender.send(message);
            SendConfirmEmailOutput output = SendConfirmEmailOutput.builder().build();
            log.info("End sendConfirmEmail {}", output);
            return output;
        }).toEither()
                .mapLeft(throwable -> Match(throwable).of(
                        defaultCase(throwable)
                ));
    }


}
