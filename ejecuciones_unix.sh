#!/bin/bash

cd # Añadir aquí el directorio donde se encuentran vuestros archivos 

# Lee la lista de valores desde el archivo inputs.txt
inputs=$(cat inputs.txt)

# Crea o sobrescribe el archivo output.txt
echo "" > output.txt

javac -cp AIMA.jar:Comparticion.jar *.java

# Itera a través de cada línea en la lista de valores
while read -r N M seed alg h; do
    echo "++++++++++++++++++++++++++++++++++++++++++++++++++" >> output.txt
    echo "Ejeciones con los valores de entrada:" >> output.txt
    echo "N = $N" >> output.txt
    echo "M = $M" >> output.txt
    echo "seed = $seed" >> output.txt
    if [[ $alg -eq 1 ]]; then
        echo "alg = Hill Climbing" >> output.txt
    else
        echo "alg = Simulated Annealing" >> output.txt
    fi
    if [[ $h -eq 1 ]]; then
        echo "h = Heuristica 1" >> output.txt
    else
        echo "h = Heuristica 2" >> output.txt
    fi
    echo "++++++++++++++++++++++++++++++++++++++++++++++++++" >> output.txt
    echo "" >> output.txt

    for i in {1..10}; do
        echo "" >> output.txt
        echo "------------Ejecución $i------------" >> output.txt
        echo "" >> output.txt
        # Ejecuta el programa con los valores como argumentos
        java -cp .:AIMA.jar:Comparticion.jar Lab1Experimentos $N $M $seed $alg $h >> output.txt
    done
done <<< "$inputs"
