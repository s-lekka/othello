# othello
------
| New Game | Move & Hint | Rules & Exit |
| -------- | ----------- | ------------ |
| ![riversi new game](/gifs/riversi-new-game.gif) | ![riversi move and hint](/gifs/riversi-move-hint.gif) | ![riversi rules and exit](/gifs/riversi-rules-exit.gif) |

------
## requirements
- openjdk 15.0.1

## instructions
compile and run program with following commands
on command promt


```
javac Main.java
java Main
```
![riversi compile and run](/gifs/riversi-compile-run.gif)

Alternatively, build & run
on any IDE.

------
## additional notes
On IntelliJ IDE better appearance of board is possible using text color codes.  
To use the above mentioned presentation a change must be made inside the code, 
before building, of file `Board.java line:681` in function String colored(char c) 
following the instuctions in the comments.   
