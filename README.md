## SK example serverside task
### Overview
Spring application designed to handle a POST request at the 
'**/modify**' endpoint, which should contain a JSON object with 
'**id**' and '**add**' fields. The application provides atomic 
increment of the '**current**' field value of the '**obj**' column 
of the row identified by the '**id**' value in the '**sk_example_table**' 
table by the specified '**add**' value, and returns the resulting 
value via the API.

### Data schema 
PostgreSQL database, 
which contains a role '***sk_example_user***', 
a database '***sk_example_db***' owned by that role,
and, inside the database, a table '***sk_example_table***':  

<table>
  <tr>
    <th colspan="3">sk_example_table</th>
  </tr>
  <tr>
    <td><b>PK</b></td>
    <td><b>id</b></td>
    <td><b>SERIAL</b></td>
  </tr>
  <tr>
    <td></td>
    <td>obj</td>
    <td>JSONB NOT NULL</td>
  </tr>
</table>


### Endpoint
- POST **/modify**  

Request and response JSONs have the following formats:  

**Request JSON:**
```json
{
  "id": <number>,
  "add": <number>
}
```

**Response JSON:**
```json
{
  "current": <number>
}
```

Upon receiving a POST request, application checks it against the JSON schema. 
Then, the '**current**' value of the JSONB column of the row 
identified by the '**id**' value is atomically incremented 
by the specified '**add**' value. 
In case of success it returns a JSON with one '**current**' 
field containing the resulting value.
If operation fails for any reason it returns HTTP status 418.

### Tests
Application comes with Unit and Integration tests.  
Please note, that Integration tests require Docker environment to be installed.

### Technology stack
- Java 17
- Spring (Boot, Data, Test, MVC)
- PostgreSQL
- Junit 5, Mockito
- testcontainers, Docker
- Maven
