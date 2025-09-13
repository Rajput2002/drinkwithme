package com.ecom.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.ecom.model.OrderAddress;
import com.ecom.model.ProductOrder;
import com.ecom.model.UserDtls;
import com.ecom.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommonUtil {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    public Boolean sendMail(String url, String reciepentEmail) throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("thescholarsbay@gmail.com", "Drink With Me");
        helper.setTo(reciepentEmail);

        String content = "<p>Hello,</p>" 
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + url + "\">Change my password</a></p>";

        helper.setSubject("Password Reset");
        helper.setText(content, true);
        mailSender.send(message);
        return true;
    }

    public static String generateUrl(HttpServletRequest request) {
        String siteUrl = request.getRequestURL().toString();
        return siteUrl.replace(request.getServletPath(), "");
    }

    String msg = null;

    public Boolean sendMailForProductOrder(ProductOrder order, String status) throws Exception {
        msg = "<p>Hello [[name]],</p>"
                + "<p>Thank you for your order. Your order has been <b>[[orderStatus]]</b>.</p>"
                + "<p><b>Product Details:</b></p>"
                + "<p>Name : [[productName]]</p>"
                + "<p>Category : [[category]]</p>"
                + "<p>Quantity : [[quantity]]</p>"
                + "<p>Price : [[price]]</p>"
                + "<p>Payment Type : [[paymentType]]</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("thescholarsbay@gmail.com", "Drink With Me");
        helper.setTo(order.getOrderAddress().getEmail());

        msg = msg.replace("[[name]]", order.getOrderAddress().getFirstName());
        msg = msg.replace("[[orderStatus]]", status);
        msg = msg.replace("[[productName]]", order.getProduct().getTitle());
        msg = msg.replace("[[category]]", order.getProduct().getCategory());
        msg = msg.replace("[[quantity]]", order.getQuantity().toString());
        msg = msg.replace("[[price]]", order.getPrice().toString());
        msg = msg.replace("[[paymentType]]", order.getPaymentType());

        helper.setSubject("Product Order Status");
        helper.setText(msg, true);
        mailSender.send(message);
        return true;
    }

    public Boolean sendOrderConfirmationMail(List<ProductOrder> orders, String status) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        OrderAddress address = orders.get(0).getOrderAddress();

        helper.setFrom("thescholarsbay@gmail.com", "Drink With Me");
        helper.setTo(address.getEmail());
        helper.setSubject("Your Order has been " + status);

        StringBuilder htmlContent = new StringBuilder();

        double grandTotal = 0.0;
        for (ProductOrder order : orders) {
            grandTotal += order.getQuantity() * order.getPrice();
        }

        // Modern HTML Email Template
        htmlContent.append("<!DOCTYPE html>");
        htmlContent.append("<html><head>");
        htmlContent.append("<meta charset='UTF-8'>");
        htmlContent.append("</head><body style='background-color:#f8f9fa; font-family: Arial, sans-serif; margin:0; padding:20px;'>");

        // Card Wrapper
        htmlContent.append("<div style='max-width:600px; margin:auto; background:#fff; border-radius:10px; box-shadow:0 4px 12px rgba(0,0,0,0.1); overflow:hidden;'>");

        // Header with Logo
        htmlContent.append("<div style='background:#ffffff; padding:15px 20px; text-align:center;'>");
        htmlContent.append("<img src='cid:drinkLogo' alt='Drink With Me' style='height:90px; margin:0;'>");
        htmlContent.append("</div>");

        // Body Content
        htmlContent.append("<div style='padding:10px;'>");
        htmlContent.append("<h2 style='color:#333; text-align:center; margin:5px 0 0 0;'>Order Confirmation</h2>");
        htmlContent.append("<p>Hello <b>").append(address.getFirstName()).append("</b>,</p>");
        htmlContent.append("<p>Thank you for your purchase! Your order <b>#")
                .append(orders.get(0).getOrderId())
                .append("</b> has been placed successfully.</p>");

        // Table
        htmlContent.append("<table style='width:100%; border-collapse:collapse; margin-top:20px;'>");
        htmlContent.append("<thead><tr style='background:#f2f2f2;'>");
        htmlContent.append("<th style='padding:12px; text-align:left;'>Product</th>");
        htmlContent.append("<th style='padding:12px; text-align:center;'>Quantity</th>");
        htmlContent.append("<th style='padding:12px; text-align:right;'>Price</th>");
        htmlContent.append("<th style='padding:12px; text-align:right;'>Subtotal</th>");
        htmlContent.append("</tr></thead><tbody>");

        for (ProductOrder order : orders) {
            double subtotal = order.getQuantity() * order.getPrice();
            htmlContent.append("<tr style='border-bottom:1px solid #eee;'>");
            htmlContent.append("<td style='padding:12px;'>").append(order.getProduct().getTitle()).append("</td>");
            htmlContent.append("<td style='padding:12px; text-align:center;'>").append(order.getQuantity()).append("</td>");
            htmlContent.append("<td style='padding:12px; text-align:right;'>Rs. ").append(String.format("%.2f", order.getPrice())).append("</td>");
            htmlContent.append("<td style='padding:12px; text-align:right;'>Rs. ").append(String.format("%.2f", subtotal)).append("</td>");
            htmlContent.append("</tr>");
        }

        htmlContent.append("</tbody></table>");

        // Grand Total
        htmlContent.append("<p style='text-align:right; font-size:1.2em; font-weight:bold; margin-top:20px;'>Grand Total: Rs. ")
                .append(String.format("%.2f", grandTotal))
                .append("</p>");

        // Billing Info
        htmlContent.append("<h4 style='margin-top:30px;'>Billing Address:</h4>");
        htmlContent.append("<p>")
                .append(address.getFirstName()).append(" ").append(address.getLastName()).append("<br>")
                .append(address.getAddress()).append("<br>")
                .append(address.getCity()).append(", ").append(address.getState()).append(" - ").append(address.getPincode()).append("<br>")
                .append("Mobile: ").append(address.getMobileNo()).append("</p>");

        // Footer
        htmlContent.append("<div style='margin-top:30px; text-align:center; font-size:0.9em; color:#777;'>");
        htmlContent.append("<p>We'll make sure to deliver the product asap.</p>");
        htmlContent.append("</div>");

        htmlContent.append("</div>"); // end body
        htmlContent.append("</div>"); // end card
        htmlContent.append("</body></html>");

        helper.setText(htmlContent.toString(), true);

        // Attach inline logo from static resources
        File logoFile = new File("src/main/resources/static/img/drinklogocpy.png");
        if (logoFile.exists()) {
            helper.addInline("drinkLogo", logoFile);
        }

        mailSender.send(message);
        return true;
    }

    public UserDtls getLoggedInUserDetails(Principal p) {
        String email = p.getName();
        UserDtls userDtls = userService.getUserByEmail(email);
        return userDtls;
    }
}
