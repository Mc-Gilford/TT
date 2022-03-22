# TT
 Aplicacion para profesionales
## Usuario
El usuario es aquel que interectuara con la aplicacion para poder realizar cualquier accion dependiendo
del perfil que este se encuentre, los perfiles a cumplir seran 2  **Cliete y Profesional**.

1. **Cliente.** Es aquel que hace la adquision de un servicio para satisfacer sus necesidades mediante un pago, este debe de cumplir ciertas normas de informacion personal para poder adquirir los servicios.
2. **Profesional.** Es aquella persona que esta dispuesta a brindar un servicio dentro de la plataforma con proposito comercial, para poder obtener nuevos clientes y poder recibir nuevos ingresos, este debe de cumplir de ciertas normas mas estrictas que el Cliente.

Dado a que ciertos datos son informacion que comparten en comun tanto **Cliente** como **Profesional**, dentro de la entidad Usuario estos seran los puntos que lo definiran. Alguno de estos seran bloqueados en caso de que el usuario no dependa del uso de alguno de estos. En el caso de la cedula profesional y los servicios.

``` java
public class Usuario
{
    private Integer id;
    private String email;
    private String password;
    private String typeUser; /* Aqui va un ENUM para seleccionar el tipo de usario Cliente o Profesional, con esto se habilitaran unos campos*/
    private Person person;
    private Account account;
    private List<Service> services;
    private String idJob;/*La cedula profesional del profesional*/ 
    private Boolean idActive;/*Indica si el usuario tiene su perfil activo */
    private Double coordX;
    private Double coordY;
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