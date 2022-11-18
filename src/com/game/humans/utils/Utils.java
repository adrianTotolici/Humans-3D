package com.game.humans.utils;

import eu.enties.Entity;
import eu.enties.Player;
import eu.toolBox.EngineConstants;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Utils {

    private static final String LOG = "Humans";
    private static StringBuilder stringBuilder = new StringBuilder();

    /**
     * Method used to write log messages
     *
     * @param msg , message  to be writen
     * @param classUsage , class path from usage
     */
    public static void logWhitTime(String msg, String classUsage){

        if (EngineConstants.ACTIVE_LOG) {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat DATE_FORMAT_SIMPLE = new SimpleDateFormat("HH:mm:ss.SSS");

            String x = DATE_FORMAT_SIMPLE.format(date) + " " + LOG + " (" + classUsage + ") " + msg;
            System.out.println(x);
            stringBuilder.append(x+"\n");
        }
    }

    /**
     * Method used detect if a player is in a certain aria of another entity
     *
     * @param player , dynamic player model on the map
     * @param entity , static entity on the map
     * @return boolean value if it is or not in that aria
     */
    public static boolean entityWhitenParams(Player player, Entity entity){
        Vector3f positionPlayer = player.getPosition();
        Vector3f positionEntity = entity.getPosition();

        boolean xPoz = checkParam((int) positionEntity.getX(), (int) positionPlayer.getX());
        boolean zPoz = checkParam((int) positionEntity.getZ(), (int) positionPlayer.getZ());
        boolean yPoz = checkParam((int) positionEntity.getY(), (int) positionPlayer.getY());

        return xPoz && zPoz && yPoz;

    }

    /**
     * Method used to load a custom font into Font class
     *
     * @param fontPath , path to font resources
     * @param fontSize , size of the font
     * @param bold , if font has bold property
     * @param italic , if font has italic property
     * @return unicodeFont object for GUI to render
     */
    public static UnicodeFont loadFont(String fontPath, int fontSize, boolean bold, boolean italic){

        try {
            UnicodeFont font = new UnicodeFont(fontPath,fontSize, bold, italic);
            font.addAsciiGlyphs();
            font.addGlyphs(400,600);
            font.getEffects().add(new ColorEffect(Color.yellow));
            font.loadGlyphs();

            return font;
        } catch (SlickException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method used to send mail in case of exception.
     * @param exception exception thron by application.
     */
    public static void sendMail(Exception exception){
        String msg = getStackTrace(exception);
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("gamehumans1","adrian88");
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("gamehumans1@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("totolici.adrian@gmail.com"));
            message.setSubject("Exception");
            message.setText(stringBuilder.toString() + "\n" + msg);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to get basic openGl version
     * @param detailOpenGlVersion detail openGL version
     * @return string of openGl version
     */
    public static String parseOpenGLVersion(String detailOpenGlVersion){
        return detailOpenGlVersion.split("\\.")[0]+"."+detailOpenGlVersion.split("\\.")[1];
    }

    private static boolean checkParam(int x, int playerPoz){
        int x1 = x-1;
        int x2 = x+1;

        if (playerPoz >= x1){
            if (playerPoz <= x2){
                return true;
            }
        }
        return false;
    }

    private static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
