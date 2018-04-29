# CallCenterApp

Esta aplicación buscar cumplir con la siguiente consigna:

> *Existe un call center donde hay 3 tipos de empleados: operador, supervisor
y director. El proceso de la atención de una llamada telefónica en primera
instancia debe ser atendida por un operador, si no hay ninguno libre debe
ser atendida por un supervisor, y de no haber tampoco supervisores libres
debe ser atendida por un director.

> ##### - Requerimientos

> - [x] Debe existir una clase Dispatcher encargada de manejar las
llamadas, y debe contener el método dispatchCall para que las
asigne a los empleados disponibles.
El método dispatchCall puede invocarse por varios hilos al mismo
tiempo.

> - [x] La clase Dispatcher debe tener la capacidad de poder procesar 10
llamadas al mismo tiempo (de modo concurrente).
> - [x] Cada llamada puede durar un tiempo aleatorio entre 5 y 10
segundos.
> - [x] Debe tener un test unitario donde lleguen 10 llamadas.

> ##### - Extras/Plus

> - [x] Dar alguna solución sobre qué pasa con una llamada cuando no hay
ningún empleado libre. 

     @Respuesta:Se inserta en una cola todos los remitentes que no han sido atentidos, 
     luego en un loop se atiendes los remitentes encolados.*

> - [x] Dar alguna solución sobre qué pasa con una llamada cuando entran
más de 10 llamadas concurrentes. 

    @Respuesta: Se usa el ExecuterService con un pool configurable de hilos, el cual permite 
    también encolar los procesos que no entrarón en el pool la primera vez*

> - [x] Agregar los tests unitarios que se crean convenientes. 

    @Respuesta: Se adicionarón tres pruebas*

> - [ ] Agregar documentación de código

    @Respuesta: Se adicionó solo la documentación necesaria*



## Requisitos para ejecutar la aplicación
### 1. Tener instalado java 8

```bash 
$ java -version
java version "1.8.0_171"
Java(TM) SE Runtime Environment (build 1.8.0_171-b11)

``` 

### 2. Tener instalado y configurado maven
```bash 
$ mvn -version 
Apache Maven 3.3.9
....

``` 



## Ejecutar maven
Este proyecto usa Maven para la gestión de librerías o dependencias por lo tanto se debe  ejecutar el comando:

```bash 
$ mvn install

``` 
### Resultado esperado

```bash 
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running callcenter.com.co.app.DispatchCallTest
Start call Operator_1 -> Caller_7
Start call Operator_2 -> Caller_9
Start call Operator_3 -> Caller_10
...

Results :

Tests run: 3, Failures: 0, Errors: 0, Skipped: 0

....

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 47.902 s
[INFO] Finished at: 2018-04-29T02:35:45-05:00
[INFO] Final Memory: 17M/179M
[INFO] ------------------------------------------------------------------------

.....


```

## Diagrama de class

![https://github.com/sumelio/CallCenterApp/blob/master/src/resources/img/callcenter_App.png](https://github.com/sumelio/CallCenterApp/blob/master/src/resources/img/callcenter_App.png)

![https://github.com/sumelio/CallCenterApp/blob/master/src/resources/img/clases.png](https://github.com/sumelio/CallCenterApp/blob/master/src/resources/img/clases.png)



:+1:
