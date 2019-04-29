# Orders

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)


API Rest Spring boot

por defecto estan configuradas 3 Monedas (MXN esta configurada para calcular el total de las ordenes)  que se puede ver su detalle en el archivo data.sql
EUR
MXN
USD

#  Instalacion
```sh
$ mvn install dockerfile:build
$ docker run -p 8080:8080 -t javaroot/orders
```
cuenta con una interaz swagger para facilitar las pruebas y la documentacion del API.

http://localhost:8080/swagger-ui.html

#Ejemplo de request para crear una Orden
```json
{
  "registros": [
    {
      "cantidad": 2,
      "producto": {
        "id": 1
      }
    },
   {
      "cantidad": 2,
      "producto": {
        "id": 2
      }
    }
  ]
}
```
