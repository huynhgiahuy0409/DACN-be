package com.example.dacn.services.impl;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;

@Service
public class ReservationMailTemplateService {
    private static final String MAIL_TEMPLATE_BASE_NAME = "mail/MailMessages";
    private static final String MAIL_TEMPLATE_PREFIX = "/templates/";
    private static final String MAIL_TEMPLATE_SUFFIX = ".html";
    private static final String UTF_8 = "UTF-8";

    private static final String TEMPLATE_NAME = "reservation-mail";

    private static TemplateEngine templateEngine;

    static {
        templateEngine = emailTemplateEngine();
    }

    private static TemplateEngine emailTemplateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(htmlTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(emailMessageSource());
        return templateEngine;
    }

    private static ResourceBundleMessageSource emailMessageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(MAIL_TEMPLATE_BASE_NAME);
        return messageSource;
    }

    private static ITemplateResolver htmlTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(MAIL_TEMPLATE_PREFIX);
        templateResolver.setSuffix(MAIL_TEMPLATE_SUFFIX);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(UTF_8);
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    public String getReservationContent(Long id, String hotelName, String location, Double price, LocalDate startDate, LocalDate endDate) {
        final Context context = new Context();

        String startDateFormatter = startDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
        String endDateFormatted = endDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));

        context.setVariable("hotelName", hotelName);
        context.setVariable("startDate", startDateFormatter);
        context.setVariable("endDate", endDateFormatted);
        context.setVariable("location", location);
        context.setVariable("phone", "0987654321");
        context.setVariable("checkin", "10:00 AM");
        context.setVariable("checkout", "09:00 AM");
        context.setVariable("days", ChronoUnit.DAYS.between(startDate, endDate));
        context.setVariable("price", price);
        context.setVariable("id", id);
        return templateEngine.process(TEMPLATE_NAME, context);
    }
}
