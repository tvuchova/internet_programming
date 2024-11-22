# Fetch

https://www.geeksforgeeks.org/javascript-fetch-method/

https://javascript.info/fetch

Fetch предоставя интерфейс на JavaScript за отправяне на заявки и получаване на отговори в HTTP. Той също така
предоставя глобален метод fetch(), който осигурява лесен, логичен начин за извличане на ресурси, асинхронно от мрежата.

Този вид функционалност е постигнато преди това чрез XMLHttpRequest. Fetch предоставя по-добра алтернатива, която може
лесно да се използва.

Използване:

**Пример**

```javascript
fetch('http://example.com/movies.json')
    .then((response) => {
        return response.json();
    })
    .then((data) => {
        console.log(data);
    });
```

Примера извлича JSON файл от мрежата и го отпечатва на конзолата. Метода fetch приема един аргумент – пътят до ресурса,
който искате да извлечете.

HTTP отговора не е действителен JSON. За да се извлече съдържанието на JSON файла от отговора, трябва да се използва
метода json().

## Изпращане на заявки

```java
// Пример за заявка POST:
async function postData(url='',data={}){
        const response=await fetch(url,{
        method:'POST',
        mode:'cors',
        cache:'no-cache',
        credentials:'same-origin',
        headers:{
        'Content-Type':'application/json'
        },
        redirect:'follow',
        referrerPolicy:'no-referrer',
        body:JSON.stringify(data)
        });
        return response.json();
        }

        postData('https://example.com/answer',{answer:42})
        .then((data)=>{
        console.log(data);
        });
```

 Атрибути       | Стойност по подразбиране           | Стойности                                              
|----------------|------------------------------------|--------------------------------------------------------|
 method         | GET                                | GET, POST, PUT, DELETE, etc.                           
 mode           | cors                               | no-cors, cors, same-origin                             
 cache          | default                            | default, no-cache, reload, force-cache, only-if-cached 
 credentials    | same-origin                        | include, *same-origin, omit                            
 header         | 'Content-Type': 'application/json' | 'Content-Type': 'application/x-www-form-urlencoded'    
 redirect       | follow                             | manual, *follow, error                                 
 referrerPolicy | client                             | o-referrer, *client                                    
 body           | -                                  | body data type must match "Content-Type" header        

## Изпращане на JSON данни

```java
const data={username:'example'};

        fetch('https://example.com/profile',{
        method:'POST',
        headers:{
        'Content-Type':'application/json',
        },
        body:JSON.stringify(data),
        })
        .then((response)=>response.json())
        .then((data)=>{
        console.log('Success:',data);
        })
        .catch((error)=>{
        console.error('Error:',error);
        });
```

## Проверка дали извличането е успешно

```java
fetch('flowers.jpg')
        .then((response)=>{
        if(!response.ok){
        throw new Error('Network response was not ok');
        }
        return response.blob();
        })
        .then((myBlob)=>{
        myImage.src=URL.createObjectURL(myBlob);
        })
        .catch((error)=>{
        console.error('There has been a problem with your fetch operation:',error);
        });
  ```

## Form data to Json

```javascript
  function toJSONString(form) {
    var obj = {};
    var elements = form.querySelectorAll("input, select, textarea");
    for (var i = 0; i < elements.length; ++i) {
        var element = elements[i];
        var name = element.name;
        var value = element.value;

        if (name) {
            obj[name] = value;
        }
    }

    return JSON.stringify(obj);
}
```
