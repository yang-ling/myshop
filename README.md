# My Shop

## Quick start

1. Run `./up.sh`
2. Mysql should be up. Port is 3306.
3. phpMyAdmin should be up too. Port is 8203.
4. Open http://localhost:8203
5. User is `myshop`, password is `myshopmyshop`. You can use your favorite DB client if you don't want to use phpMyAdmin.
6. Import `db/myshop.sql`

## Run

`./gradlew bootRun`

## APIs

Please see `postman-json/RESTED_export_myshop.json`. You can import it to Postman, or https://github.com/RESTEDClient/RESTED

Or you can just view it by your favorite json viewer.

If you have run this project, you can view its API doc site: http://localhost:8080/doc.html

Or http://localhost:8080/swagger-ui/index.html if you prefer swagger ui

## TODOs

- [x] Global exception handling. Currently I just throw runtime exception. It's not good. I could use `ExceptionHandler` and `ControllerAdvice` to achieve global exception handling if I have more time.
