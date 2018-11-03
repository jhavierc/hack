/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.logica;

import co.com.ccepeda.siigo.dto.CorreoDTO;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.ejb.Stateless;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author CarlosJavier
 */
@Stateless
public class EnvioCorreoLogica {

    private static final Properties properties = new Properties();
    private static Session session;
    private static final Logger LOGGER = Logger.getLogger(EnvioCorreoLogica.class.getSimpleName());

    public static void sendEmail(final CorreoDTO correo) {

        try {
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.port", 587);
            properties.put("mail.smtp.user", correo.getUserCorreo());
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.from.alias", "Siigo");

            session = Session.getDefaultInstance(properties);

            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setText(correo.getMensaje());

            BodyPart adjunto = new MimeBodyPart();

            if (correo.getRutaArchivo() != null && correo.getRutaArchivo().length() > 0) {
                adjunto.setDataHandler(new DataHandler(new FileDataSource(correo.getRutaArchivo())));
                adjunto.setFileName(correo.getNombreArchivo());
            }
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(bodyPart);
            if (correo.getRutaArchivo() != null) {
                mimeMultipart.addBodyPart(adjunto);
            }

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(correo.getUserCorreo()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(correo.getDestino()));
            message.setSubject(correo.getAsunto());
            message.setContent(mimeMultipart);

            Transport t = session.getTransport("smtp");
            t.connect(correo.getUserCorreo(), correo.getPassword());
            t.sendMessage(message, message.getAllRecipients());
            t.close();
            LOGGER.log(Level.INFO, "Notificación de correo enviada exitosamente!!!");
        } catch (MessagingException me) {
            LOGGER.log(Level.SEVERE, "Error al enviar el correo {0}", me);
        }
    }

    private String crearHtmlCorreo(String titulo, String user, String mensaje, String footer) {
        String contenido = "﻿<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <head>\n"
                + "        <meta charset=\"utf-8\">\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "        <meta name=\"description\" content=\"LtCoin - Lottery\">\n"
                + "        <title>LtCoin - Lottery</title>\n"
                + "        <link rel=\"stylesheet\" href=\"http://www.monkeysoft.com.co/images/ltcoin/vendor.bundle.css\">\n"
                + "        <link rel=\"stylesheet\" href=\"http://www.monkeysoft.com.co/images/ltcoin/style.css?ver=1.0\">\n"
                + "        <link rel=\"stylesheet\" href=\"http://www.monkeysoft.com.co/images/ltcoin/theme.css?ver=1.0\">\n"
                + "        <link rel=\"stylesheet\" type=\"text/css\" href=\"http://www.monkeysoft.com.co/images/ltcoin/widget.css?38\" media=\"all\">\n"
                + "         <link rel=\"stylesheet\" type=\"text/css\" href=\"http://www.monkeysoft.com.co/images/ltcoin/numbers.css?38\" media=\"all\">"
                + "    </head>\n"
                + "    <body>\n"
                + "        <div class=\"section section-pad\">\n"
                + "            <div class=\"container\">\n"
                + "                <div class=\"section-head\">\n"
                + "                    <div class=\"row\">\n"
                + "                        Logo\n"
                + "                    </div>\n"
                + "                </div>\n"
                + "                <div class=\"gaps size-2x\"></div>\n"
                + "                <div class=\"row\">\n"
                + "                    <div class=\"col-md-6 col-md-offset-3\">\n"
                + "                        <div class=\"testimonial-carousel has-carousel owl-carousel owl-theme owl-loaded\">\n"
                + "                            <div class=\"owl-stage-outer\">\n"
                + "                                <div class=\"owl-stage\">\n"
                + "                                    <div class=\"owl-item cloned\">\n"
                + "                                        <h3><span>" + titulo + "</span></h3>   \n"
                + "                                        <div class=\"client-info\">\n"
                + "                                            <h6>" + user + "</h6>\n"
                + "                                        </div>\n"
                + "                                        <br>\n"
                + "                                        <p>" + mensaje + "</p>\n"
                + " "
                + "										<p>" + footer + "</p>\n"
                + "										<br>\n"
                + "                                        <div class=\"contact-information\">\n"
                + "                                            <div class=\"row\">\n"
                + "                                                <div class=\"col-sm-12 res-m-bttm\">\n"
                + "                                                    <div class=\"contact-entry\">\n"
                + "                                                        <h6>Contact <span>Information</span></h6>\n"
                + "                                                        <p>carlos.cepeda</p>\n"
                + "														<p>monday - friday at 8:30am - 5:00pm</p>\n"
                + "                                                    </div>\n"
                + "                                                </div>\n"
                + "                                            </div>\n"
                + "                                        </div>\n"
                + "                                    </div>\n"
                + "                                </div>\n"
                + "                            </div>\n"
                + "                        </div>\n"
                + "                    </div>\n"
                + "                </div>\n"
                + "            </div>\n"
                + "       	</div>\n"
                + "    </body>\n"
                + "</html>";
        return contenido;
    }

       public static void main(String[] args) {

        CorreoDTO correoDTO = new CorreoDTO();
        correoDTO.setAsunto("Siigo Notifications");
        correoDTO.setDestino("cepeda_357@hotmail.com");
        correoDTO.setMensaje("Notificacion Siigo");
        correoDTO.setUsuario("Carlos Cepeda");
        correoDTO.setTitulo("Test");
        correoDTO.setFooter("Para mayor información contactenos en : ");
        correoDTO.setPassword("Contraseña app");
        correoDTO.setUserCorreo("contraseña administracion");    

        //correoDTO.setRutaArchivo("C:\\developer.jpeg");
        //correoDTO.setNombreArchivo("developer.jpeg");
        correoDTO.setUserCorreo("cepeda_357@hotmail.com");
        sendEmail(correoDTO);
    }
}
