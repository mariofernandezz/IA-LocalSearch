# Práctica de Búsqueda Local (IA-UPC) 
## Guía para la ejecución y replicación de resultados
### por Mario Fernández, Héctor Fortuño y Aina Luis

El objetivo de este documento es facilitar al usuario la creación de ejecuciones y el diseño de experimentos para validar los resultados extraídos de la práctica.

## Tabla de contenidos
- [Creación de ejecuciones simples](#creación-de-ejecuciones-simples)
- [Diseño de experimentos](#diseño-de-experimentos)
- [Modificación de otros elementos del problema](#modificación-de-otros-elementos-del-problema)

## Creación de ejecuciones simples

Para poder realizar una ejecución del problema, es tan sencillo como ejecutar el fichero `Lab1Demo.java` desde la terminal: 

```
$ java Lab1Demo
```

Otra forma de hacerlo es desde tu editor de código favorito (e.g. Visual Studio Code), en el caso que tenga una opción de ejecutar un archivo abierto.

:bangbang: Es imprescindible que este fichero se encuentre en el mismo directorio que todos los archivos `.java` del proyecto y que los archivos `AIMA.jar` y `Comparticion.jar` para que la ejecución sea posible.

Una vez ejecutado, irán apareciendo por la terminal diferentes ordenes para diseñar la ejecución. Por orden de aparición, se os pedirá lo siguiente:

1. El número de Usuarios `N` que deseáis.
2. El número de conductores `M` que deseáis (M debe ser menor que N).
3. La `seed` de generación aleatoria de Usuarios.
4. El algoritmo con el que deseáis resolver el problema. Escribir (1) para usar `Hill Climbing` y (2) para usar `Simulated Annealing`.
5. La heurística que deseáis usar. Escribir (1) para `minimizar la distancia` y (2) para `minimizar la distancia y los conductores`.

## Diseño de experimentos

Para poder realizar experimentos a lo largo de la práctica de forma más rápida y sencilla se ha diseñado unos archivos especiales. Estos permiten realizar 10 ejecuciones de una serie de experimentos descritos por sus características en un fichero llamado `input.txt` y guardar los resultados en un fichero llamado `output.txt`.

Para el correcto funcionamiento del sistema, los experimentos deben estar descritos en líneas diferentes del archivo de texto `input.txt` y deben incluir, con separaciones de un espacio, los mismos valores que se piden en la ejecución simple. Es decir, deben incluir la **N**, la **M**, la  **seed**, el **número de algoritmo** y el **número de función heurística**, con los valores descritos arriba.

Un ejemplo de fichero con dos experimentos podría ser el siguiente:
```
200 100 1234 1 1
300 120 33 1 2
```

Para ejecutarlos, debéis escoger el fichero en función del sistema operativo:

* Si tenéis Windows :window: debéis usar el fichero `ejecuciones_windows.bat` y ejecutarlo desde el directorio de la siguiente forma:
```
$ .\ejecuciones_windows.bat
```

* En caso de usar un sistema Linux :penguin: o macOS :apple:, debéis usar el fichero `ejecuciones_unix.sh` y ejecutarlo desde el directorio de la siguiente forma:
```
$ .\ejecuciones_unix.sh
```

:bangbang: Es imprescindible que estos ficheros se encuentre en el mismo directorio que todos los archivos `.java` del proyecto y que los archivos `AIMA.jar` y `Comparticion.jar` para que la ejecución sea posible.

:bangbang::bangbang: Antes de ejecutarlos, debéis entrar al código y añadir la **ruta del directorio** donde se encuentran todos vuestros archivos del proyecto, incluido este mismo, en la **tercera** línea del archivo.

## Modificación de otros elementos del problema

De cara a poder explorar y verificar ciertos resultados de la práctica, podéis modificar algunos de los elementos del problema. A continuación se especifican cuales son y la forma en que se pueden modificar:
- **Generador de la solución inicial:** para cambiar el algoritmo que genera la solución inicial se debe hacer desde los ficheros `Lab1Demo.java` (línea 31) o `Lab1Experimentos.java` (línea 31). Las opciones disponibles son: la generación más sencilla, *solucionInicial1()*; la generación sofisticada *solucionInicial2()* (default); y la sofisticada con aleatoriedad *solucionInicial3(seed)*.
- **Operadores:** se puede añadir el operador de *añadir_conductor(conductor)* si se des-comenta su implementación en los archivos `Lab1SuccessorFunction.java` y `Lab1SASuccessorFunction.java`. Está indicado qué parte del código es cada operador.
- **Parámetros del algoritmo de Simulated Annealing:** se pueden modificar dichos parámetros desde los ficheros `Lab1Demo.java` (líneas 77-80) o `Lab1Experimentos.java` (líneas 77-80).
