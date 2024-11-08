# Форма

## Тага form

form e HTML елемент за създаване на формуляри, който се използва за събиране на въведени от потребителя данни

|атрибути | стойност| значение| 
|-|-|-|
action | URL| Указва къде да се изпратят данните от формуляра
method | get, post | Указва HTTP метода, който да се използва при изпращане на данни на формуляра
enctype | application/x-www-form-urlencoded, multipart/form-data, text/plain| Определя как трябва да се кодират данните на формуляра при подаването им на сървъра (само за метод = "post")
name | текст| специфично име на формуляра

```html
<form>
    <!--.
    form elements
    .-->
</form>
``` 

## Тага input

input елемента оказва данните който ще се въвеждат във формата и изпращат към сървъра.

|атрибути | стойност| значение|
|-|-|-|
type | button
checkbox
color
date
datetime-local
email
file
hidden
image
month
number
password
radio
range
reset
search
submit
tel
text
time
url
week | специфицира типа на данните на input полето
name | текст | името на input полето което се използва като ключ за достъп до данните на сървъра
value | текст | специфичната стойност на input полето
checked | checked | Специфицира стойноста на input полета от type="checkbox" или type="radio"

### Типове input полета

|Тип | Стоиност| значение
button | String| бутон за изпращане на данните към сървъра
checkbox | bool/String[]| True или False показва дали е избран или не елемента 
color | String| цвят
date | String| дата
datetime-local | String| локално време
email | String| текст в специфичен формат emeil
file | binary| изпращане на файл към сървъра
hidden | String| скрито поле с данни
image | binary| изпращане на файл към сървъра
month | int| изпращане на специфично число към сървъра месец
number | month | int| изпращане на число към сървъра
password | String| текстови данни за парола
radio | bool/string[]/ True или False показва дали е избран или не елемента 
range | int| изпращане на специфично число към сървъра
reset | String | бутон за почистване на формата
search | String| поле за въвеждане на текст за търсене
submit | String| бутон за изпращане на данните към сървъра
tel | String| текст в специфичен формат 
text | String| изпращане текст към сървъра
time | String| текст в специфичен формат 
url | String| текст в специфичен формат 
week | String| текст в специфичен формат 

## Тага select

select е HTML елемент за списъчни данни

|атрибути | стойност| значение|
|-|-|-|
name | текст| името на input полето което се използва като ключ за достъп до данните на сървъра

Тага option са стойностите на списъка

|атрибути | стойност| значение|
|-|-|-|
value | текст| стойноста на полето

```html
<select name="cars">
  <option value="volvo">Volvo</option>
  <option value="saab">Saab</option>
  <option value="fiat">Fiat</option>
  <option value="audi">Audi</option>
</select>
```

## Тага textarea

textarea е HTML таг който служи за въвеждане на многоредов текст

|атрибути | стойност| значение|
|-|-|-|
name | текст| името на input полето което се използва като ключ за достъп до данните на сървъра
row | число| броя на редовете за въвеждане
cols | число| броя на колоните за въвеждане

