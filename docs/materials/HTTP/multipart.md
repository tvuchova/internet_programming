# Multipart MIME Email Guide

This is a guide on how to send a properly formatted multipart email. Multipart email strings are MIME encoded, raw text
email templates. This method of structuring an email allows for multiple versions of the same email to support different
email clients.

```
// Example Multipart Email:
From: sender@example.com
To: recipient@example.com
Subject: Multipart Email Example
Content-Type: multipart/alternative; boundary="boundary-string"

--your-boundary
Content-Type: text/plain; charset="utf-8"
Content-Transfer-Encoding: quoted-printable
Content-Disposition: inline

Plain text email goes here!
This is the fallback if email client does not support HTML

--boundary-string
Content-Type: text/html; charset="utf-8"
Content-Transfer-Encoding: quoted-printable
Content-Disposition: inline

<h1>This is the HTML Section!</h1>
<p>This is what displays in most modern email clients</p>

--boundary-string--
```

## Different `Content-Type` Sections

Multipart email strings are composed of sections for each `Content-Type` version of the email you wish to use. By
sending differently formatted versions, you can ensure your recipients are able to see whatever message you are sending,
optimized for their email clients' capabilities.

 `Content-Type`    | Description                                                           
-------------------|-----------------------------------------------------------------------
 `text/html`       | This allows the use of HTML (`<img>`, `<table>`, `<h1>`, `<p>`, etc.) 
 `text/plain`      | For sending an email as only text without formatting                  
 `text/watch-html` | Limited HTML support, similar in effect to rich text formatting       

###### Example use cases of different `Content-Type`:

* Send beautifully formatted HTML emails using `text/html` for an experience similar to that of a web browser
* For older email clients or recipients who have HTML specifically turned off, use `text/plain`
* Apple Watch does not display full HTML emails, so you may send an email without images or styles
  using `text/watch-html`
* The email client on Apple Watch is able to support a format similar to rich-text, albeit with some quirks

### Plain Text Format

The plain text format is a means to send an unformatted copy of an email, without images or styles, that will be
readable by everyone.

Although HTML makes emails beautiful, plain text makes them functional. In addition to helping prevent your emails from
getting caught in the spam filter, a plain text version of your email will allow everyone to see your message,
regardless of email client. or settings.

```
// Plain Text Example
From: sender@example.com
To: recipient@example.com
Subject: Multipart Email Example
Content-Type: multipart/alternative; boundary="boundary-string"

--your-boundary
Content-Type: text/plain; charset="utf-8"
Content-Transfer-Encoding: quoted-printable
Content-Disposition: inline

Plain text formatted content

--boundary-string--
```

### HTML Format

HTML formatting allows for the use of web languages (HTML and CSS) to style emails, display images, etc. It is important
to always include a plain text version of the email when using HTML because some email clients do not support HTML
emails, and some recipients prefer plain text format.

HTML format allow for the use of tags such as `<h1>Title</h1>` and styles such
as `<p style="color:#777777">Touch of Gray</p>` in your emaill. This format also allows for the use of remote images via
links `<img src="http://example.com/img.png">`.

```
// Example Multipart Email:
From: sender@example.com
To: recipient@example.com
Subject: Multipart Email Example
Content-Type: multipart/alternative; boundary="boundary-string"

--your-boundary
Content-Type: text/plain; charset="utf-8"
Content-Transfer-Encoding: quoted-printable
Content-Disposition: inline

Plain text formatted content

--boundary-string
Content-Type: text/html; charset="utf-8"
Content-Transfer-Encoding: quoted-printable
Content-Disposition: inline

HTML formatted content

--boundary-string--
```

### Apple Watch Format

You can send emails specifically formatted to the `text/watch-html` format to allow for Apple Watch users to view your
emails on their devices.

```
// Apple Watch Example
From: sender@example.com
To: recipient@example.com
Subject: Multipart Email Example
Content-Type: multipart/alternative; boundary="boundary-string"

--your-boundary
Content-Type: text/plain; charset="utf-8"
Content-Transfer-Encoding: quoted-printable
Content-Disposition: inline

Plain text formatted content

--boundary-string
Content-Type: text/watch-html; charset="utf-8"
Content-Transfer-Encoding: quoted-printable
Content-Disposition: inline

Apple Watch formatted content

--boundary-string--
```

The Apple Watch email client has limited support for HTML and is similar in ability to rich text format (see table).
Although images are possible in this format, they are quirky and may not display properly.

 Rich Text   | Apple Watch HTML                              | Quirks                              
-------------|-----------------------------------------------|-------------------------------------
 Bold        | `<b>ABC123</b>`                               |
 Italic      | `<i>ABC123</i>`                               |
 Underline   | `<u>ABC123</u>`                               |
 Font Color  | `<span style="color:#990000;">ABC123</span>`  | Black text will be changed to white 
 Alignment   | `<div style="text-align:right;">ABC123</div>` | `left`, `center`, `right`           
 Blockquote  | `<blockquote type="cite">ABC123</blockquote>` | Up to 7 levels deep with shading    
 Number List | `<ol><li>ABC123</li></ol>`                    |
 Bullet List | `<ul><li>ABC123</li></ul>`                    |
 Images      | `<img src="cid:...">Text</blockquote>`        | Only embedded images, buggy         

** Table
Credit: [How To Send a Hidden Version of Your Email That Only Apple Watch Will See](https://litmus.com/blog/how-to-send-hidden-version-email-apple-watch)

## Testing Your Email

You may test your email by sending it via a service such as the free [PutsMail](https://putsmail.com/tests/new) tool and
seeing how it appears in your inbox. Services such as this allow the testing of emails without the need for an email
API (Mandrill, Mailgun, Amazon SES, etc.) email service provider, or your own email server.

Although PutsMail does not allow you to send an entire multipart email as a MIME string (all of the examples in this
guide), you can send each section (HTML, plain text, watch) individually by copy/pasting the code for each type
individually without the headers.