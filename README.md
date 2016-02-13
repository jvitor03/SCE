# SCE (Elevator Control System)
A small system to simulate the working of elevators where each them work concurrently.

> "9. Be original, XGH hasn't design pattern." - XGH teachings

---

## Quick usage

```bash
$ make && java -jar bin/SCE.jar < INPUT_EXAMPLE
```

## Input

Program waits positive integer numbers on standard input. Respectively, floors' number N, elevators' number M, elevator's capacity of persons C. Next, the starting floor for each elevator Ei. Then, for each floor, waits the number of people in this floor Ri following of the destination floor to them.

```
N       M       C
E0      E1      ...     E(M-1)      EM
R0      (N-2)   N
...
R(N-1)  0       (N-3)
```

Troubles? See an example file in the `INPUT_EXAMPLE`.

## Output

Program will create a directory `./output`. Each elevator will create a file `Elevator-<ID>.txt` in the output directory. These files store the working of its owner.

## Reporting bugs

Feel free to send a message to {jvitor03,nettinhorama}@gmail.com.
