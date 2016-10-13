package dubbo_server.mina;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Created by Loftor on 2014/8/15.
 */
public class MinaTimeClient {

    public static void main(String[] args) throws Exception {
        IoConnector connector = new NioSocketConnector();
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new PrefixedStringCodecFactory(Charset.forName("UTF-8"))));
        connector.setHandler(new TimeClientHander());
        IoSession session = null;
        for (; ; ) {
            try {
                ConnectFuture future = connector.connect(new InetSocketAddress("127.0.0.1", 9123));
                future.awaitUninterruptibly();
                session = future.getSession();
                System.out.println("连接成功");
                break;
            } catch (RuntimeIoException e) {
                System.err.println("连接失败");
                e.printStackTrace();
                Thread.sleep(5000);
            }
    }
//        ConnectFuture connectFuture = connector.connect(new InetSocketAddress("127.0.0.1",BaseConfig.PORT));
//        //等待建立连接
//        connectFuture.awaitUninterruptibly();
//        System.out.println("连接成功");
//
//        IoSession session = connectFuture.getSession();

            Scanner sc = new Scanner(System.in);

            boolean quit = false;

            while (!quit) {

                String str = sc.next();
                if (str.equalsIgnoreCase("quit")) {
                    quit = true;
                }
                session.write(str);
            }

            //关闭
            if (session != null) {
                if (session.isConnected()) {
                    session.getCloseFuture().awaitUninterruptibly();
                }
                connector.dispose(true);
            }


        }

    }