# Пренасочване между Servlet и JSP

## Интерфеийсът RequestDispatcher

Интерфейсът ```RequestDispatcher``` предоставя възможност за изпращане на заявката до друг сървурен ресурс, той може да бъде html, servlet или jsp. Този интерфейс може да се използва и за включване на съдържанието на друг ресурс. Това е един от начините на връзка между servlet.

Има два метода, дефинирани в интерфейса RequestDispatcher:

- ```forward(request, response)``` - Препраща заявка от сервлет към друг ресурс (servlet, JSP файл или HTML файл) на сървъра.
- ```include(request, response)``` - Към отговора се включва съдържанието на друг ресурс (servlet, JSP страница или HTML файл).
 
## Използване на обекта RequestDispatcher

- ```getRequestDispatcher()``` на интерфейса ServletRequest връща обекта на RequestDispatcher. Приема адреса до ресурса към който ще се пренасочи.

```
//Извлича RequestDispatcher обект за пренасочване към друг ресурс
RequestDispatcher rd=request.getRequestDispatcher("servlet2");  
//препраща заявката от сервлет към друг ресурс
rd.forward(request, response);
```

```
//Извлича RequestDispatcher обект за пренасочване към друг ресурс
RequestDispatcher rd=request.getRequestDispatcher("servlet2");  
//добавя резултата от заявката към друг ресурс
rd.inclue(request, response);
```

## Интерфеийсът SendRedirect

- ```sendRedirect()``` - на HttpServletResponse интерфейсa може да се използва за пренасочване на отговор към друг ресурс, може да бъде servlet, jsp или html файл. 
 
Приема относителen, както и абсолютен URL адрес.

Той работи от страна на клиента, защото използва URL лентата на браузъра, за да направи друга заявка. Така че, може да се пренасочи към ресурс на сървъра или ресурс на друг сървър.

```
public class GoogleRedirectServlet extends HttpServlet{  
public void doGet(HttpServletRequest request,HttpServletResponse response)  throws ServletException IOException  
{  
    response.setContentType("text/html");  
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter pw=res.getWriter();  
  
    response.sendRedirect("http://www.google.com");  
  
pw.close();  
}} 
```

## Разлика между метода forward() и sendRedirect()

| forward() метод |	sendRedirect() метод |
-|-|-|
forward() работи от страна на сървъра. | sendRedirect() работи от страна на клиента.
Изпраща същите обекти на request и response на друг сървлет. | Винаги се изпраща нов request.
Работи само в рамките на сървъра. | Използва се и извън сървъра.
Пример: request.getRequestDispacher("servlet2").forward(request,response); |	Пример: response.sendRedirect("servlet2");
