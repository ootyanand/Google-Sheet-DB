## Obtaining google Credentials using **gapi.jar**
First follow [this](https://developers.google.com/sheets/api/quickstart/java) instructions to create and download a **_client_secret.json_** file for your account. This file is necessary to access your google drive files from any application.

1. Once you have downloaded the **_client_secret.json_** file, you can pass this file in **_getRefreshToken()_** in **GApi** to obtain a **refresh token** from google. This process will open a web page by google to authenticate yourself. You can persist this **refresh token** and use it in future by directly going to step2. (if you don't want this authentication process each time).

2. Once you have the **refresh token**, you can now create **Credential** Object using **_getCredential()_** in **gapi.jar** by passing **_client_secret.json_** and the **refresh token**. 

3. This **Credential** Object can now be used in any Google Api Srvices like, google drive, google sheets, google docs, etc...

## Sheet Id
A sheet id can be found in the url when you open that sheet in web browser...

Example: The sheet id would be **X0x0x0x0x0x0x0x0x0x0x0x0** in the following URL.
https://docs.google.com/spreadsheets/d/X0x0x0x0x0x0x0x0x0x0x0x0/edit#gid=0

## Using gdb.jar
Once you have Credentials, you can now directly create a database (Google Sheet) on google drive using **_GDatabase.createDatabase()_**  or open an existing database(Google Sheet) using **_GDatabase.Open()**

**Please Note: The idea behind using google sheet as a database is to, use the first row as column names and each consecutive rows as records.**

You will find all other function in **_GDatabase_** more obvious.

I have written a sample database application "GoogleDatabaseApp", which shows how to use this gdb.jar. Please see the image below.

![gdb1](https://user-images.githubusercontent.com/6127328/30237189-c242c79c-94e1-11e7-8b3d-f23dc09b2345.JPG)

And the changes will be reflected immediately in the google sheet.
![gdb2](https://user-images.githubusercontent.com/6127328/30237204-f3a1f47a-94e1-11e7-822d-f64d41f7aa73.JPG)


## Using gsheet.jar
You can use **gsheet.jar** to directly access shpreadsheet functionality!

