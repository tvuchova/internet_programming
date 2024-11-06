**Упражнение 1 -- Въведение в TCP/IP и Сокети**

Internet cable network view\
\
• <https://bbmaps.itu.int/bbmaps/>\
• <https://www.submarinecablemap.com/>

Първоначални настройки:

Install IDE : IntelliJ IDEA Community Edition:

<https://www.jetbrains.com/idea/download/other.html>

Github:

<https://github.com/tvuchova/internet_programming>

1.IP address\
\
• На този сайт може да видите IP адреса си:\
<https://www.whatismyip.com/>\
\
• команда : **ipconfig**

Показва:**\
\
** • Ethernet adapter or wireless adapter name\
• IP address\
• Subnet mask\
• Default gateway\
• DHCP server\
• DNS servers

Пример:

Ethernet adapter Ethernet\
IP address: 192.168.1.100\
Subnet mask: 255.255.255.0\
Default gateway: 192.168.1.1\
Wireless LAN adapter WiFi\
\
IP address: 192.168.2.100\
Subnet mask: 255.255.255.0\
Default gateway: 192.168.2.1\
\
\
2. DNS\
- може да видите DNS информацията\
<https://www.nslookup.io/>

3\. Ping -- проверявате дали даден уебсайт е достъпен - Internet Control
Message Protocol (ICMP) \"echo request\"

**Част 1: Запознаване с основните класове**

1.1. InetAddress и IP адреси

**Задача 1:** Напишете по-горните команди на java използвайки
**InetAddress**:

1.  Получаване и отпечатване на IP адреса на локалния хост (текущия
    компютър)

2.  Получаване и отпечатване на loopback адреса (обикновено 127.0.0.1)

3.  Пример Reverse DNS Lookup - получаване на хостнейм от IP адрес
    (Google DNS: 8.8.8.8)

4.  Получаване на IP адрес за дадено домейн име (пример:
    [www.yahoo.com](http://www.yahoo.com))

5.  Симулиране на nslookup за домейна google.com - връщане на всички
    свързани IP адреси(hostname, ipadress)

6.  Симулиране на \"ping\" - проверка дали хост с даден IP адрес е
    достъпен. Проверка дали хостът е достъпен в рамките на 5000
    милисекунди (5 секунди)

използвайте класа:

**public class
InetAddress **extends **Object** implements **Serializable:**

Тук може да видите документация и методите му:

<https://docs.oracle.com/javase/8/docs/api/java/net/InetAddress.html>

методите, които могат да помогнат при реализация на задачата ви:

+-----------------------------------------------+----------------------+
| public static InetAddress                     | instance на          |
| getLocalHost() *throws UnknownHostException*  | InetAddress          |
|                                               | съдържаща local      |
|                                               | hostname and         |
|                                               | address.             |
+===============================================+======================+
| public static InetAddress getByName( String   | instance на          |
| host ) *throws UnknownHostException*          | InetAddress          |
|                                               | съдържаща IP and     |
|                                               | Host name на хоста.  |
+-----------------------------------------------+----------------------+
| public static InetAddress\[\] getAllByName(   | array от инстанции   |
| String hostName ) *throws                     | InetAddress class    |
| UnknownHostException*                         | който съдържат IP    |
|                                               | addresses.           |
+-----------------------------------------------+----------------------+
| public static InetAddress getByAddress( byte  | InetAddress обекта   |
| IPAddress\[\] ) *throws UnknownHostException* | създаден от raw IP   |
|                                               | address.             |
+-----------------------------------------------+----------------------+
| public static InetAddress getByAddress(       | Създава и връща      |
| String hostName, byte IPAddress\[\] ) *throws | InetAddress обекта   |
| UnknownHostException*                         | създаден от          |
|                                               | съответния hostname  |
|                                               | and IP address.      |
+-----------------------------------------------+----------------------+
| InetAddress.getLoopbackAddress()              | The loopback address |
|                                               | е специален IP       |
|                                               | address което се     |
|                                               | използва да тества   |
|                                               | network connections  |
|                                               | на локалната machine |
|                                               | без да изпраща       |
|                                               | пакети по реална     |
|                                               | мрежа                |
|                                               |                      |
|                                               | refers to the IP     |
|                                               | address 127.0.0.1 in |
|                                               | IPv4 or ::1 in IPv6. |
|                                               |                      |
|                                               | • It is used to      |
|                                               | refer to             |
|                                               | \"localhost\",       |
|                                               | meaning the local    |
|                                               | machine itself.      |
|                                               |                      |
|                                               | • Using the loopback |
|                                               | address allows you   |
|                                               | to send and receive  |
|                                               | network traffic from |
|                                               | your own computer    |
|                                               | without requiring an |
|                                               | external network     |
|                                               | connection.          |
+-----------------------------------------------+----------------------+

**Example output:**

IP of Local Host : R59RM22RCV/192.168.43.72

Loopback InetAddress - Local Host : localhost/127.0.0.1

Hostname for IP 8.8.8.8: dns.google

IP of www.yahoo.com is: 212.82.116.204

NSLookup for google.com:

Host Name: google.com

IP Address: 142.250.187.142

Host Name: google.com

IP Address: 2a00:1450:4017:80e:0:0:0:200e

Sending Ping Request to 133.192.31.42

Sorry ! We can\'t reach to this host

**1.2 Class NetworkInterface --** този класни позволява програмно да
вземем информация за нашите мрежови интерфейси

Методи:

<https://docs.oracle.com/javase/8/docs/api/java/net/NetworkInterface.html>

**Задача 2.** Вие сте системен администратор и искате да разберете какви
мрежови интерфейси са налични на сървъра, който поддържате. Освен това,
искате да получите повече информация за тях, като IP адреси, MAC адреси,
статус на връзките и други. Вашата задача е да създадете програма,
която:

1.  Изброява всички налични мрежови интерфейси на машината.

2.  За всеки интерфейс показва:

    -   Името на интерфейса

    -   MAC адрес

    -   Всички IP адреси (IPv4 и IPv6)

    -   Дали интерфейсът е активен

    -   Дали поддържа multicast

    -   Скорост на интерфейса (ако е налична)

Искат от вас да използвате java и клас NetworkInterface

Полезни методи:

NetworkInterface.*getNetworkInterfaces*()

networkInterface.getHardwareAddress()

networkInterface.getInetAddresses();

networkInterface.isUp()

networkInterface.supportsMulticast()

networkInterface.isLoopback()

Примерен изход:

Интерфейс: eth0

Описание: Ethernet интерфейс

MAC адрес: 00-14-22-01-23-45

IP адрес: 192.168.1.10

Интерфейсът е активен: true

Поддържа multicast: true

Loopback интерфейс: false

\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\--

Интерфейс: lo

Описание: Loopback интерфейс

MAC адрес: Няма информация.

IP адрес: 127.0.0.1

Интерфейсът е активен: true

Поддържа multicast: false

Loopback интерфейс: true

**Част 2: Въвеждане в потоково предаване на данни (Streams)**

**2.1.** Работа с входни и изходни потоци (I/O)

Основната комуникация в сокетите често използва потоци за четене и
запис**.**

**Задача 3.** Управление на оценки на студенти чрез файлова система (I/O
операции)

**Описание:**

Напишете програма, която управлява база данни с оценки на студенти, като
използва файлове за съхранение на информацията. Програмата трябва да
може да чете, записва и обработва информация за студенти и техните
оценки, използвайки I/O операции (входно-изходни потоци).

Функционалности:

1.  Добавяне на студент и оценка:

Програмата трябва да позволява добавяне на студент с неговото име и
оценка.

Информацията трябва да бъде записана във файл (например
\"students.txt\").

2.  Преглед на оценки:

Програмата трябва да чете данни от файла и да показва списък с всички
студенти и техните оценки.

Изчисляване на средна оценка:

Програмата трябва да може да изчислява и показва средната оценка на
всички студенти, използвайки данните от файла.

Запис във файл:

Всички промени в информацията за студенти трябва да се запазват във
файла.

Изисквания:

Файлът \"students.txt\" трябва да съхранява данните в следния формат:
всяка линия представлява студент и оценката му, разделени със запетая
(например: Иван Иванов,5.50).

Забележка:

Използвайте try-with-resources за работа с файлови потоци, за да
осигурите автоматичното им затваряне.

**Част 3: SOCKET -- CLIENT- SERVER**

![A diagram of a computer program Description automatically
generated](./image1.png){width="6.527047244094488in"
height="4.88841426071741in"}

**Socket class**

java.net.Socket позволява да създадем сокет свързан към порт и адрес:

*public Socket(InetAddress address, int port)*

Четене и записване на данни:

+-----------------------------------------------------------------------+
| **Method**                                                            |
+=======================================================================+
| 1.  public InputStream getInputStream() - InputStream атачнат към     |
|     този сокет                                                        |
+-----------------------------------------------------------------------+
| 2.  public OutputStream getOutputStream() - връща OutputStream        |
|     атачнат към тоя сокет                                             |
+-----------------------------------------------------------------------+
| 3.  public synchronized void close() - затваря сокета                 |
+-----------------------------------------------------------------------+

**ServerSocket class**

The ServerSocket class може да използваме за да създадем server socket.
Този обект се използва за да осъществи комуникация с клиента.

+-----------------------------------------------------------------------+
| **Method**                                                            |
+=======================================================================+
| 1)  public Socket accept() -- връща сокет и установява връзка между   |
|     клиент и сървър                                                   |
+-----------------------------------------------------------------------+
| 2)  public synchronized void close() -- затваря сокета                |
+-----------------------------------------------------------------------+

**Задача 4**.Напишете ехо програма: Клиента прочита текст от
клавиатурата праща го към сървъра и сървъра му връща ехо същия текст

Спецификация:

<https://docs.oracle.com/javase/tutorial/networking/sockets/readingWriting.html>

<https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/net/Socket.html>

Примерен изход:

**Сървър:**

Hello World.I am Server!

Connection established

**Client:**

Hello World.I am Client!

Enter string

Hello from a client

Server echo says: Hello from a client
