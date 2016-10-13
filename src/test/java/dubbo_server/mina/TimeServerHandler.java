package dubbo_server.mina;

//这个类中一般有exceptionCaught, messageReceived 和 sessionIdle这几个方法。
//exceptionCaught 应该总是在handler 中定义，来处理一些异常情况，否则异常信息将无法捕捉。
//exceptionCaught 方法简单地打印了错误的堆栈跟踪和关闭会话。对于大多数程序，这将是标准的做法，除非处理程序可以从异常状态中恢复。
//messageReceived 方法来处理从客户端接收到的数据，这里是将当前时间返回给客户端，当收到quit时，会话将被关闭，也会返回一个当前时间给客户端。
//根据所使用的协议编解码器，object 这个参数传递的类型有所不同，以及返回的数据时的session.write(Object) 也不同。
//如果不指定协议的编解码器，你将收到一个类型为IoBuffer 的对象，返回的数据也要求是IoBuffer。
//sessionIdle 方法将定时调用一次会话，保持空闲状态。通过调用acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 10 );来设定时间间隔。
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.util.Date;

/**
 * Created by jghkjh on 2016/9/6.
 */
public class TimeServerHandler extends IoHandlerAdapter {
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		String str = message.toString();
		if (str.trim().equalsIgnoreCase("quit")) {
			session.close();
			return;
		}

		Date date = new Date();
		session.write(date.toString());
		System.out.println("Message written...");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		System.out.println("IDLE " + session.getIdleCount(status));
	}
}
