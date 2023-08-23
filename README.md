# My Shop

## Quick start

1. Run `./up.sh`
2. Mysql should be up. Port is 3306.
3. phpMyAdmin should be up too. Port is 8203.
4. Open http://localhost:8203
5. User is `myshop`, password is `myshopmyshop`. You can use your favorite DB client if you don't want to use phpMyAdmin.
6. Import `db/myshop.sql`
7. Open http://localhost:8080/swagger-ui/index.html to check APIs. You also can execute those APIs
   1. Create an user. POST: `/api/v1/user`
   2. Create a product. POST: `/api/v1/product`
   3. Create a cart item. POST: `/api/v1/cart`. Only `productId`, `userId`, and `amount` are reqired fields. Remember the cart item id.
   4. Place an order.
     1. Generate a token first. GET: `/api/v1/token`
     2. Place the order. POST: `/api/v1/order/{userId}/{orderNo}`. In request body, it is a cart item id array.
   5. Pay the order. PUT: `/api/v1/order/{userId}/{orderNo}`.

## APIs

Please see `postman-json/RESTED_export_myshop.json`. You can import it to Postman, or https://github.com/RESTEDClient/RESTED

Or you can just view it by your favorite json viewer.

If you have run this project, you can view its API doc site: http://localhost:8080/doc.html

Or http://localhost:8080/swagger-ui/index.html if you prefer swagger ui

## Idempotence

The API `Place an order` is idempotent, though the implementation is simple. I use a token to check whether the request is duplicated or not.
