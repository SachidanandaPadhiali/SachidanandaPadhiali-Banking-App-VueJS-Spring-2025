package com.banking.bank_app_java.service;

import com.banking.bank_app_java.dto.CreditDebitEmail;
import com.banking.bank_app_java.dto.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.username")
	private String senderEmail;

	@Override
	public void sendSignUpEmail(EmailDetails emailDetails) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setFrom(senderEmail);
			helper.setTo(emailDetails.getRecipient());

			helper.setSubject(emailDetails.getSubject());

			String htmlContent = "<div style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 40px;\">"
					+ "<div style=\"max-width: 600px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
					+ "<div style=\"text-align: center;\">"
					+ "<img src='https://brandripe.com/storage/media_files/Blog%2074/image%202.jpg' alt='MyBank Logo' style='max-height: 50px; margin-bottom: 20px;'>"
					+ "<h2 style=\"color: #134B70;\">Welcome to MyBank</h2>" + "</div>"
					+ "<p style=\"font-size: 16px; color: #333;\">Dear <strong>"
					+ emailDetails.getSavedUser().getFirstName() + "</strong>,</p>"
					+ "<p style=\"font-size: 16px; color: #333;\">We are pleased to inform you that your new bank account has been successfully created.</p>"
					+ "<div style=\"background-color: #60A5FA; padding: 15px; border-left: 5px solid #2E86C1; margin: 20px 0; font-size: 15px;\">"
					+ "<strong>Account Details:</strong><br>" + "Account Holder: "
					+ emailDetails.getSavedUser().getFirstName() + "<br>" + "Account Number: "
					+ emailDetails.getSavedUserBank().getAccNo() + "<br>" + "Account Type: Savings Account<br>"
					+ "Status: Active<br>" + "Address: " + emailDetails.getSavedUser().getAddressLine1() + ", "
					+ emailDetails.getSavedUser().getAddressLine2() + ", " + emailDetails.getSavedUser().getCity()
					+ ", " + emailDetails.getSavedUser().getState() + ", " + emailDetails.getSavedUser().getPin()
					+ "e<br>Linked Phone Number: " + emailDetails.getSavedUser().getPhoneNumber() + "</div>"
					+ "<p style=\"font-size: 15px; color: #333;\">You can now access your account through our online banking platform or mobile app.</p>"
					+ "<div style=\"text-align: center; margin-top: 30px;\">"
					+ "<a href=\"https://sachidanandapadhiali-banking-app-spring.onrender.com:8080/Sign-in\" style=\"background-color: #134B70; color: #ffffff; padding: 12px 25px; border-radius: 5px; text-decoration: none; font-weight: bold;\">Log In to Your Account</a>"
					+ "</div>"
					+ "<p style=\"margin-top: 30px; font-size: 14px; color: #888;\">For your security, please do not share your account number or credentials with anyone.</p>"
					+ "<p style=\"font-size: 14px; color: #888;\">If you did not request this account or have any questions, contact our support team immediately.</p>"
					+ "<hr style=\"margin-top: 40px;\">"
					+ "<p style=\"font-size: 12px; color: #bbb; text-align: center;\">This is an automated message from YourBank. Please do not reply.</p>"
					+ "</div>" + "</div>";
			helper.setText(htmlContent, true);

			mailSender.send(message);
			System.out.println("Email sent Successfully");
		} catch (MailException | MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void sendBalanceUpdateEmail(CreditDebitEmail emailDetails) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setFrom(senderEmail);
			helper.setTo(emailDetails.getRecipient());

			helper.setSubject(emailDetails.getSubject());

			LocalDateTime now = LocalDateTime.now();
			String formatted = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

			String creditHtmlContent = "<body style='font-family: Arial, sans-serif; color: #333; background-color: #f9f9f9; padding: 20px;'>"
					+
					"  <div style='max-width: 600px; margin: auto; background: #ffffff; padding: 20px; border-radius: 8px;'>"
					+
					"    <h2 style='color: #4CAF50; margin-top: 0;'>🎉 Good News! Your Account Has Been Credited</h2>" +
					"    <p>Hi " + emailDetails.getUser().getFirstName() + ",</p>" +
					"    <p>We’re happy to inform you that your account has been successfully credited.</p>" +

					"    <div style='margin-top: 15px;'>" +
					"      <span style='display: block; margin: 4px 0;'><strong>Amount Credited:</strong> ₹"
					+ emailDetails.getAmount() + "</span>"
					+
					"      <span style='display: block; margin: 4px 0;'><strong>Transaction ID:</strong> 1234</span>"
					+
					"      <span style='display: block; margin: 4px 0;'><strong>Date & Time:</strong> " + formatted
					+ "</span>"
					+
					"      <span style='display: block; margin: 4px 0;'><strong>Your Current Balance:</strong> "
					+ emailDetails.getAccount().getAccBalance() + "</span>" +
					"    </div>" +

					"    <p>You can log into your account anytime to view the updated balance.</p>" +
					"    <p>If you have any questions or need assistance, feel free to reach out—we’re here to help.</p>"
					+
					"    <p>Thanks for choosing us!</p>" +
					"    <div style='margin-top: 20px; font-size: 12px; color: #777;'>— My Bank | mybank@mybank.com</div>"
					+
					"  </div>" +
					"</body>";

			String debitHtmlContent = "<body style='font-family: Arial, sans-serif; color: #333; background-color: #f9f9f9; padding: 20px;'>"
					+ "  <div style='max-width: 600px; margin: auto; background: #ffffff; padding: 20px; border-radius: 8px;'>"
					+ "    <h2 style='color: #F44336; margin-top: 0;'>🔔 Alert: A Debit Transaction Has Occurred</h2>"
					+ "    <p>Hi " + emailDetails.getUser().getFirstName() + ",</p>"
					+ "    <p>We’re writing to notify you that a debit transaction has been made from your account.</p>"

					+ "    <div style='margin-top: 15px;'>"
					+ "      <span style='display: block; margin: 4px 0;'><strong>Amount Debited:</strong> ₹"
					+ emailDetails.getAmount() + "</span>"
					+ "      <span style='display: block; margin: 4px 0;'><strong>Transaction ID:</strong> 5678</span>"
					+ "      <span style='display: block; margin: 4px 0;'><strong>Date & Time:</strong> " + formatted
					+ "</span>"
					+ "      <span style='display: block; margin: 4px 0;'><strong>Remaining Balance:</strong> "
					+ emailDetails.getAccount().getAccBalance() + "</span>"
					+ "    </div>"

					+ "    <p>Please review this transaction. If you have any concerns or did not authorize it, contact us immediately.</p>"
					+ "    <p>Thank you for trusting us with your banking needs.</p>"
					+ "    <div style='margin-top: 20px; font-size: 12px; color: #777;'>— My Bank | mybank@mybank.com</div>"
					+ "  </div>"
					+ "</body>";

			if (emailDetails.getIsCredit() == 1) {
				helper.setText(creditHtmlContent, true);
			} else {
				helper.setText(debitHtmlContent, true);
			}

			mailSender.send(message);
			System.out.println("Email sent Successfully");
		} catch (MailException | MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void sendEmail(EmailDetails emailDetails) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(senderEmail);
			message.setTo(emailDetails.getRecipient());
			message.setSubject(emailDetails.getSubject());
			message.setText(emailDetails.getMsgBody());
			mailSender.send(message);
			System.out.println("Email sent Successfully");
		} catch (MailException e) {
			throw new RuntimeException(e);
		}
	}
}
