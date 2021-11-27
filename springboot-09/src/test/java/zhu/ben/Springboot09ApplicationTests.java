package zhu.ben;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@SpringBootTest
class Springboot09ApplicationTests {


    @Autowired
    JavaMailSenderImpl mailSender;


    @Test
    void contextLoads() {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("");
        mail.setTo("");
        mail.setText("");
        mail.setSubject("");
        mailSender.send(mail);
    }

    @Test
    void contestloads() throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        helper.setSubject("sdaasds");
        helper.setText("jbz",true);
        helper.addAttachment("banner.text", new File("/home/stl/Desktop/banner.txt"));
        helper.setTo("L18249290950@126.com");
        helper.setFrom("1012074120@qq.com");
        mailSender.send(message);
    }

    public void SendMail(Boolean html,String title,String text,File file,String sendTo,String sendform) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,html);
        helper.setText(text);
        helper.setSubject(title);
        helper.setFrom(sendform);
        helper.setTo(sendTo);
        helper.addAttachment("jbz",file);
        mailSender.send(message);

    }

}
