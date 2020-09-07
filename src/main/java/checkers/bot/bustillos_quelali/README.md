# Checkers
### - Vanessa Bustillos 
### - Alejandra Quelali 

Para el desarrollo del proyecto se ha implementado el algoritmo MiniMax 
y el algoritmo Alpha-Beta para su optimización.

Para determinar si un tablero representa un estado terminal, se ha considerado dos aspectos: 

* Si el número de piezas restantes un jugador es igual a 0, significa que uno de ellos ha ganado la partida.

* Si ningún jugador puede realizar ningún movimiento ni captura, se determina un empate.

Para generar los estados sucesores, se verifica si el tablero actual es terminal o no. En caso de serlo, ya no se generan suscesores. Caso contrario, se devuelve la lista de capturas posibles, si estas no existieran, los movimientos posibles.
Además, se registra en una lista los estados hijo generados y el movimiento que realizó su padre para llegar a dicho estado. 

De ese modo, la heurística empleada se basa en el cálculo de las piezas de cada jugador, estos valores sirven para asignarle un valor utilitario a cada tablero explorado.
Al realizar la resta de ambos valores (myPieces - opponentPieces) se determina qué movimiento será el indicado para obtener la mayor ventaja frente al oponente.

Para obtener el mejor movimiento a realizar en cada jugada, se ha implementado el algoritmo MiniMax; en el cual, si es turno del oponente, se selecciona el "peor movimiento", es decir, el movimiento con la menor utilidad. En caso de ser el turno del jugador local, selecciona el movimiento con la mayor utilidad. Esta operación se realiza, analizando todos los nodos sucesores del tablero actual.
