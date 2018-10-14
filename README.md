# URL Shortener

### Installation
There is no external dependencies, therefore no additional installation is required.

### Running
Clone the project using <br />

`git clone https://github.com/aomerovic/Url-Shortener.git` <br />

or simply download the project.

Two ways to run the project:
1. Using command prompt
2. Using intelliJ IDEA

#### Command Prompt
Use cmd to navigate to downloaded folder. Open the "target" folder. <br />
`cd URLSHORT\target`
<br />

In cmd window run the command <br />
`java -jar demo-0.0.1-SNAPSHOT.jar`

Application is up and running.  <br />

Use POSTMAN (or similar) for requests.

#### IntelliJ IDEA
Open IntelliJ IDEA. Open downloaded project. Run the project.

Use POSTMAN (or similar) for requests.
### Problem

##### Task
Create HTTP service used to shorten URLs, with following functionalities:
- Web address registration (API)
- Redirect client according to shorten URL
- Visits statistic
<br>

#### Description of the task

##### 1. Basic architecture
Service has two parts: configuration and client part.

##### 1.1 Configuration part
Configuration part is called using REST call with JSON parameters, and it is used for:
- Opening an account
- Registering an URL in "Shortener" service
- Fetching the statistics

###### 1.1.1 Opening an account
Parameter | Description 
--- | ---
HTTP method | POST
URI | /account
Request Type | application/json
Request Body | JSON object with following parameters:<ul><li>`accountId (String, required)`</li></ul>Example: {"accountId" : "myAccountId"}
Response Type | application/json
Response | There is a difference between successful and unsuccessful registration. Unsuccessful registration happens only when account ID already exists. Parameters are the following: <ul><li>`success:true\|false`</li><li>`description: Status description, e.g. Account with that ID already exists`</li><li>`password: Returned only when account is successfully opened. Automatically generated password 8 alphanumeric characters long.`<li></ul> Example {"success":"true", "description":"Your account is opened", password: "xC345Fc"}

###### 1.1.2 Registeration of URLs
Parameter | Description
--- | ---
HTTP method | POST
URI | /register
Request Type | application/json
Request Headers | Set the Authorization header, and authorize the user
Request Body | JSON object with following parameters:	<ul><li>`url (required, url to be shorten)`</li><li>`redirectType : 301 \| 302 (not required, default 302)`</li></ul> Example: {"url": "http://stackoverflow.com/questions/1567929/website-safe-data-access-architecture-question?rq=1", "redirectType":"301"}
Response Type | application/json
Response | Response parameters in case of successful registration are the following: <ul><li>`shortUrl (shorten URL)`</li></ul> Example: {"shortUrl":"http://short.com/xYswlE"}

###### 1.1.3 Fetching the statistic
Parameter | Description
--- | ---
HTTP method | GET
URI | /statistic/{accountId}
Request Headers | Set the Authorization header, and authorize the user
Response Type | application/json
Response | Server responds with JSON object, that is map key:value, where the key is registered URL, and value is a number of calls to it. Example: {"http://myweb.com/someverylongdirectory/someotherdirectory/":"10", "http://myweb.com/someverylongdirectory2/someotherdirectory2/":"4", "http://myweb.com/someverylongdirectory3/someotherdirectory3/":"91"}

##### 1.2	Redirect
Redirect client to configured address with configured HTTP status.

##### 2. General requirements
-	Use Java programing language
-	Pay attention that HTTP statuses of response are according to REST standards (list of statuses http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html)
- Service has to be written as executable jar or WAR (can be chosen).
- Application must not require any additional configuration, which means that it must not contain any external dependencies. It must work on first run.
- According to the previous request, it is not allowed to use databases, unless they are embedded in application itself.
- Create help page `(uri: /help)` containing run, setup and use instructions
- Deliver source code preferably as Maven project





