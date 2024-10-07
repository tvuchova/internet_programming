import java.net.*;
import java.io.IOException;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args) throws SocketException {
        firstExample();
        secondExample();
    }

    public static void firstExample() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            System.out.println("Local Host IP Address: " + localHost);

            InetAddress loopbackAddress = InetAddress.getLoopbackAddress();
            System.out.println("Loopback Address: " + loopbackAddress);

            InetAddress googleDNS = InetAddress.getByName("8.8.8.8");
            String hostName = googleDNS.getHostName();
            System.out.println("Hostname for IP 8.8.8.8: " + hostName);

            InetAddress yahooIP = InetAddress.getByName("www.yahoo.com");
            System.out.println("IP address of www.yahoo.com: " + yahooIP.getHostAddress());

            InetAddress[] googleAddresses = InetAddress.getAllByName("google.com");
            System.out.println("NSLookup for google.com:");
            for (InetAddress addr : googleAddresses) {
                System.out.println("Host Name: " + addr.getHostName() + ", IP Address: " + addr.getHostAddress());
            }

            InetAddress pingTest = InetAddress.getByName("133.192.31.42");
            boolean isReachable = pingTest.isReachable(5000);
            if (isReachable) {
                System.out.println("Ping success! Host is reachable.");
            } else {
                System.out.println("Sorry! Can't reach this host.");
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void secondExample() throws SocketException {
        System.out.println("Network Interfaces: ");
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while(interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while(addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                if (address instanceof Inet4Address) {
                    System.out.println("IP Address: " + address.getHostAddress());
                }
            }
        }
    }

    public static void thirdExample() throws SocketException {
        
    }
}