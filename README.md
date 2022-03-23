# TT
 Aplicacion para profesionales
## Usuario
El usuario es aquel que interectuara con la aplicacion para poder realizar cualquier accion dependiendo
del perfil que este se encuentre, los perfiles a cumplir seran 2  **Cliente y Profesional**.

1. **Cliente.** Es aquel que hace la adquision de un servicio para satisfacer sus necesidades mediante un pago, este debe de cumplir ciertas normas de informacion personal para poder adquirir los servicios.
2. **Profesional.** Es aquella persona que esta dispuesta a brindar un servicio dentro de la plataforma con proposito comercial, para poder obtener nuevos clientes y poder recibir nuevos ingresos, este debe de cumplir de ciertas normas mas estrictas que el Cliente.

Dado a que ciertos datos son informacion que comparten en comun tanto **Cliente** como **Profesional**, dentro de la entidad Usuario estos seran los puntos que lo definiran. Por lo cual estas dos clases usaran herencia a hacia esta clase.

``` java
public class User
{
    private Integer id;
    private String email;
    private String password;
    private Person person;
    private Account account;
    private Boolean idActive;/*Indica si el usuario tiene su perfil activo */
    private Double coordX;
    private Double coordY;
}
```

## Cliente
La clase cliente hereda las caracteristicas de **User**, en cambio tendra un campo adicional a su clase padre, la cual sera una lista donde puede guardar a los profesionales que mas le gustaron.
``` java
public class Client extends User
{
    private List<Professional> professionals; /*Este sirve para guardar a los profesionales que le gustaron su servicio*/
}
```

## Profesional
Esta clase muy similar a su clase hermana Cliente agrega nuevos atributos para su funcionamiento de esta como una lista de sus servicios, un campo para la cedula profesional y su puntaje para el sistema de calificaciones.
``` java
public class Professional extends User
{
    private List<Service> services;
    private String idJob;/*La cedula profesional del profesional*/
    private Float score;
}
```

## Persona
La entidad **Person** es donde tendra la mayor de la parte de la informacion personal, como el nombre, apellido paterno, apellido materno, telefono, domicilio y archivos personales.

``` java
public class Person
{
    private Integer id;
    private String name;
    private String lastname;
    private String secondname;
    private String phone;
    private Address address;
    private List<File> data;
}
```

## Direccion
La entidad de **Address** es donde contendra la mayor parte de la informacion de la direccion del usuario, como pais, estado, ciudad, colonia, calle, codigo postal, numero.

``` java
public class Address
{
    private String id;
    private String country;
    private String state;
    private String city;
    private String colony;
    private String street;
    private String postalCode;
    private Integer number;
}
```
## Cuenta
Esta entidad **Account** representa los datos del numero de cuenta bancaria, como el numero de la tarjeta, el tipo de tarjeta, numero de cuenta y la cantidad que se le depositara a su cuenta.

``` java
private class Account
{
    private String cardNumber;
    private String typeCard;
    private String accounNumber;
    private Double amount;/* Este es opcional dependiendo si queremos que tenga una vista de sus ingresos*/
}
```

## Servicio 
La entidad **Service** esta representa por el nombre del tipo de servicio, el precio del servicio y los detalles de este.

``` java
private class Service
{
    private String id;
    private String name;
    private Double cost;
    private String details;
    private Time time;
}
```