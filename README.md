## sb-rest-validation

A Spring Boot REST style web service to handle CRUD operations from a H2 In-memory database.

A REST controller to create the following REST API endpoints :

HTTP 	Method	URI			Description
GET			/books			List all books.
POST		/books			Save a book.
GET			/books/{id}		Find a book where id = {:id}.
PUT			/books/{id}		Update a book where id = {:id}, or save it.
PATCH		/books/{id}		Update a single field where id = {:id}.
DELETE		/books/{id}		Delete a book where id = {:id}.

 
### Test
Start Spring Boot application, and test the REST API endpoints with curl command.
Find all books

```
curl -v localhost:8080/books
```

Find One - /books/1

```
curl -v localhost:8080/books/1
```

Test 404 - /books/5

```
curl -v localhost:8080/books/5
```

Save - /books -d {json}

```
curl -v -X POST localhost:8080/books -H "Content-type:application/json" -d "{\"name\":\"Spring REST tutorials\",\"author\":\"Javawiz\",\"price\":\"9.99\"}"
```

Test Update - /books/4 -d {json}

```
curl -v -X PUT localhost:8080/books/4 -H "Content-type:application/json" -d "{\"name\":\"Spring Forever\",\"author\":\"pivotal\",\"price\":\"9.99\"}"
```

Test Update a 'author' field - /books/4 -d {json}

```
curl -v -X PATCH localhost:8080/books/4 -H "Content-type:application/json" -d "{\"author\":\"oracle\"}"
```

Test delete - /books/4

```
curl -v -X DELETE localhost:8080/books/4
```


## Bean Validation (Hibernate Validator)
The bean validation will be enabled automatically if any JSR-303 implementation (like Hibernate Validator) is available on the classpath. By default, Spring Boot will get and download the Hibernate Validator automatically.
The below POST request will be passed, we need to implement the bean validation on the book object to make sure fields like name, author and price are not empty.
```
@PostMapping("/books")
@ResponseStatus(HttpStatus.CREATED)
Book newBook(@RequestBody Book newBook) {
    return repository.save(newBook);
}
```
Annotate the Book's properties with javax.validation.constraints.* annotations and try to send a POST request to the REST endpoint again. If the bean validation is failed, it will trigger a MethodArgumentNotValidException. By default, Spring will send back an HTTP status 400 Bad Request, but no error detail.
```
@PostMapping("/books")
@ResponseStatus(HttpStatus.CREATED)
Book newBook(@Valid @RequestBody Book newBook) {
    return repository.save(newBook);
}

public class Book {
    @Id
    @GeneratedValue
    private Long id;
    @NotEmpty(message = "Please provide a name")
    private String name;
    @NotEmpty(message = "Please provide a author")
    private String author;
    @NotNull(message = "Please provide a price")
    @DecimalMin("1.00")
    private BigDecimal price;

    // ...
}
```
```
curl -v -X POST localhost:8080/books -H "Content-type:application/json" -d "{\"name\":\"Spring REST tutorials\"}"
Note: Unnecessary use of -X or --request, POST is already inferred.
*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 8080 (#0)
> POST /books HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.55.1
> Accept: */*
> Content-type:application/json
> Content-Length: 32
>
* upload completely sent off: 32 out of 32 bytes
< HTTP/1.1 400
< Content-Length: 0
< Date: Wed, 12 Aug 2020 11:45:57 GMT
< Connection: close
<
* Closing connection 0
```
The above error response is not friendly, we can catch the MethodArgumentNotValidException and override the response like this in our CustomGlobalExceptionHandler :
```
 @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);

    }
```
Try again. Done.
```
>curl -v -X POST localhost:8080/books -H "Content-type:application/json" -d "{\"name\":\"Spring REST tutorials\"}"
Note: Unnecessary use of -X or --request, POST is already inferred.
*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 8080 (#0)
> POST /books HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.55.1
> Accept: */*
> Content-type:application/json
> Content-Length: 32
>
* upload completely sent off: 32 out of 32 bytes
< HTTP/1.1 400
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Wed, 12 Aug 2020 14:53:12 GMT
< Connection: close
<
{"timestamp":"2020-08-12T14:53:12.524+00:00","status":400,"errors":["Please provide a price","Please provide a author"]}* Closing connection 0
```
