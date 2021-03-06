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

> - [X] Agregar documentación de código

    @Respuesta: Se adicionó documentación en métodos mas importantes*



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
Para ejecutar el programa, primero ubicarse en la raíz del proyecto, donde esta el archivo pom.xml.

Ejemplo en linux:
```bash
$ls -l
  LICENSE
  pom.xml
  README.md
  src
``` 

Y ejecutar el siguiente comando:
```bash 
$ mvn clean install

``` 

La ejecucion del comando anterior puede tardar algunos minutos (si es necesario descargar las dependencias). Las dependencias son:
 - junit
 - org.mockito
 - slf4j
 
### Resultado esperado (test unit)

Las pruebas unitarias tardan aproximadamente 30 segundos dependiendo la cantidad de procesaodres y memoria. 
1. test01: La primera prueba envia 10 hilos y se evalúa que sean atendidos primero por los 6 operadores luego por los tres supervisores y un director.
2. test02: La segunda prueba envia 10 hilos en paralelo y el resultado esperado es que se crean 10 llamadas al tiempo con una duración total cercana a los 10 segundos. Cada llamada puede durar entre 5 y 10 segundos cada una.
3. test03 En esta prueba se envian 22 hilos en paralelo y se valida que sean creadas 22 llamadas.
4. test04 En la última prueba se envian 2 hilos uno detrás del otro y se evalúa que sean creadas 2 llamadas.

***NOTA: La cantidad de agentes del callcenter es configurable. El tamaño del pool también. Las pruebas estan con un pool de 10 hilos.***

Ejemplo del resultado esperado:
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

Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 39.675 sec - in callce...


....

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 47.902 s

.....


```

## Diagrama de clases

![https://github.com/sumelio/CallCenterApp/blob/master/src/resources/img/callcenterApp.png](https://github.com/sumelio/CallCenterApp/blob/master/src/resources/img/callcenterApp.png)

La clase abstracta **Agent** implementa la interfaz **Receiver** y extiende o es hija de **Person**. Tiene el método answer para dar respuesta a la persona que llama (caller).
Además tiene cuatro clases hijas: Operator, Supervisor, Director y AgentQueue. **AgentQueue** sobreescribe el método answer para encolar un **Caller** y atenderlo cuando los demas agentes esten disponible. Los demás agentes atienden a los *callers* creando la instancia **AttendingCall**.

La clase **Caller** implementa a la interfaz **Sender**, también es hija de **Person** y representa a la persona que realiza la llamada entrante.

La clase **AttendingCall** extiende de **Call** y es la entidad que ejecuta la llamada, para poder crear una instancia de esta, se necesita un Agente (**Agent**) disponible y la persona que realiza la solicitud de llamada (**Caller**).

En la clase **Dispatcher** está la lógica de negocio que implementa el método **dispatchCall**  y el bucle **loopCallCenter**.

El método **dispatchCall**: Encolar las solicitudes de los emisores **Callers** .

El método **loopCallCenter**: Lee de la cola, asigna a un agente Operator, Supervisor, Director o AgentQueue para responder y crea la instancia  **AttendingCall** la cual es ejecutada en un hilo dentro del pool del ExecutorServices.


![https://github.com/sumelio/CallCenterApp/blob/master/src/resources/img/clases.png](https://github.com/sumelio/CallCenterApp/blob/master/src/resources/img/clases.png)



:+1:
