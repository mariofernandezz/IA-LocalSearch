@echo off

cd C:\Users\hecto\Desktop\Práctica1\IA

REM Lee la lista de valores desde el archivo inputs.txt
setlocal ENABLEDELAYEDEXPANSION
set /p inputs=<inputs.txt

REM Crea o sobrescribe el archivo output.txt
echo. > output.txt

javac -cp AIMA.jar;Comparticion.jar *.java

REM Itera a través de cada línea en la lista de valores
for /F "tokens=1,2,3,4,5" %%a in ("%inputs%") do (
    set "N=%%a"
    set "M=%%b"
    set "seed=%%c"
    set "alg=%%d"
    set "h=%%e"

    echo ++++++++++++++++++++++++++++++++++++++++++++++++++ >> output.txt
    echo Ejeciones con los valores de entrada: >> output.txt
    echo N = !N! >> output.txt
    echo M = !M! >> output.txt
    echo seed = !seed! >> output.txt
    if !alg! == 1 (
        echo alg = Hill Climbing >> output.txt
    ) else (
        echo alg = Simulated Annealing >> output.txt
    )
    if !h! == 1 (
        echo h = Heuristica 1 >> output.txt
    ) else (
        echo h = Heuristica 2 >> output.txt
    )
    echo ++++++++++++++++++++++++++++++++++++++++++++++++++ >> output.txt
    echo. >> output.txt

    for /l %%i in (1,1,10) do (
        echo. >> output.txt
        echo ------------Ejecución %%i------------ >> output.txt
        echo. >> output.txt
        REM Ejecuta el programa con los valores como argumentos
        java -cp .;AIMA.jar;Comparticion.jar Lab1Demo !N! !M! !seed! !alg! !h! >> output.txt 

    )
)
