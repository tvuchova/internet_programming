[назад](/../..)

# Обработване на клиентски заявки. Методите `doGet()` и `doPost()`

## Обработване на GET заявка

HTML форма
```html
<form action="HelloForm" method="GET" >
    <label for="first">First Name:</label> 
    <input id="first" type= "text" name= "first_name" >
    <br/>
    <label for="last">Last Name:</label> 
    <input id="last" type="text" name= "last_name" />
    <input type= "submit" value= "Submit" />
</form>
```

Сървлет, обработващ заявката от формата чрез метода doGet()

```java
@WebServlet("/HelloForm")
public class HelloForm extends HttpServlet {
    
    private String docType;
    private String title;
    private String format;

    public void init() throws ServletException{
        docType = "<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">\n";
        title = "Using GET Method to Read Form Data";
        format = "%s <html>\n<head><title>%s</title></head>\n<body bgcolor=\"#ccc\">\n<h1 align=\"center\">%s</h1>\n<ul>\n<li><b>First Name</b>:%s\n<li><b>Last Name</b>:%s\n</ul>\n</body></html>";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
        response.setContentType("text/html");

        String fName = request.getParameter("first_name");
        String lName = request.getParameter("last_name");

        PrintWriter out = response.getWriter();
        
        out.println(String.format(format, docType, title, title, fName, lName));
    }
}
```
## Обработване на POST заявка

HTML форма
```html
<form action="HelloForm" method="POST" >
    <label for="first">First Name:</label> 
    <input id="first" type= "text" name= "first_name" >
    <br/>
    <label for="last">Last Name:</label> 
    <input id="last" type="text" name= "last_name" />
    <input type= "submit" value= "Submit" />
</form>
```

Сървлет, обработващ заявката от формата чрез метода doPoat()

```java
@WebServlet("/HelloForm")
public class HelloForm extends HttpServlet {
    
    private String docType;
    private String title;
    private String format;

    public void init() throws ServletException{
        docType = "<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">\n";
        title = "Using POST Method to Read Form Data";
        format = "%s <html>\n<head><title>%s</title></head>\n<body bgcolor=\"#ccc\">\n<h1 align=\"center\">%s</h1>\n<ul>\n<li><b>First Name</b>:%s\n<li><b>Last Name</b>:%s\n</ul>\n</body></html>";
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
        response.setContentType("text/html");

        String fName = request.getParameter("first_name");
        String lName = request.getParameter("last_name");

        PrintWriter out = response.getWriter();
        
        out.println(String.format(format, docType, title, title, fName, lName));
    }
}
```

[назад](/../..)
