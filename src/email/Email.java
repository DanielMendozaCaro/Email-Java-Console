
package email;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Daniel Mendoza Caro
 */
public class Email {

    
    String usuarioCorreo;
    String password;
    
    String rutaArchivo;
    String nombreArchivo;
    
    String destinatario;
    String asunto;
    String mensaje;

    public Email(String usuarioCorreo, String password, String rutaArchivo, String nombreArchivo, String destinatario, String asunto, String mensaje) {
        this.usuarioCorreo = usuarioCorreo;
        this.password = password;
        this.rutaArchivo = rutaArchivo;
        this.nombreArchivo = nombreArchivo;
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.mensaje = mensaje;
    }
    
    
    public Email(String usuarioCorre, String password, String destinatario, String mensaje){
        this(usuarioCorre, password, "", "", destinatario, "", mensaje);
    }
    
    public Email(String usuarioCorre, String password, String destinatario, String asunto, String mensaje){
        this(usuarioCorre, password, "", "", destinatario, asunto, mensaje);
    }
    
    public boolean sendEmail(){
        try{
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            props.setProperty("mail.smtp.user", usuarioCorreo);
            props.setProperty("mail.smtp.auth", "true");
            
            Session session = Session.getDefaultInstance(props, null);
            BodyPart texto = new MimeBodyPart();
            texto.setText(mensaje);
            
            BodyPart adjunto = new MimeBodyPart();
            if (!rutaArchivo.equals("")){
                adjunto.setDataHandler(
                    new DataHandler(new FileDataSource(rutaArchivo)));
                adjunto.setFileName(nombreArchivo);
            }
            
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            if (!rutaArchivo.equals("")){
                multiParte.addBodyPart(adjunto);
            }
            
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usuarioCorreo));
            message.addRecipient(
                Message.RecipientType.TO,
                    new InternetAddress(destinatario));
                    message.setSubject(asunto);
                message.setContent(multiParte);
                
            Transport t = session.getTransport("smtp");
            t.connect(usuarioCorreo, password);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
            
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        String clave = "contraseña email"; 
        Email e = new Email("email_origen@gmail.com", clave, "C:\\factura.pdf" /* Ruta archivo */, "factura.pdf" /*Nombre archivo para visualización*/,
                "email_destino","email_asunto","email_mensaje");
        
        
        if(e.sendEmail()){
            System.out.println("resulto");
        }else{
                    System.out.println("no resulto");

        }
    }
}
