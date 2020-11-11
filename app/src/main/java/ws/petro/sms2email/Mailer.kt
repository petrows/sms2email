package ws.petro.sms2email

import com.github.tntkhang.gmailsenderlibrary.GmailListener
import com.github.tntkhang.gmailsenderlibrary.JSSEProvider
import com.github.tntkhang.gmailsenderlibrary.SendMailTask
import java.security.Security
import java.util.*
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Session

class GMailSenderAuthenticator internal constructor(
    private val user: String,
    private val password: String,
    private val listener: GmailListener
) :
    Authenticator() {
    companion object {
        init {
            Security.addProvider(JSSEProvider())
        }
    }

    private val session: Session
    override fun getPasswordAuthentication(): PasswordAuthentication {
        return PasswordAuthentication(user, password)
    }

    @Synchronized
    fun sendMail(subject: String?, body: String?, sender: String?, recipients: String?) {
//        val sendMailTask = SendMailTask(
//            subject, body, sender, recipients,
//            listener, session
//        )
//        sendMailTask.execute()
    }

    init {
        val props = Properties()
        props.setProperty("mail.transport.protocol", "smtp")
        props.setProperty("mail.host", "smtp.gmail.com")
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.port"] = "465"
        props["mail.smtp.socketFactory.port"] = "465"
        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        props["mail.smtp.socketFactory.fallback"] = "false"
        props.setProperty("mail.smtp.quitwait", "false")
        session = Session.getDefaultInstance(props, this)
    }
}
