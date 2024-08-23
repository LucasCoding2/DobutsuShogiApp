# Dependencies, Building, and Running
## Dependency Instructions

Download JavaFX
https://gluonhq.com/products/javafx/

## Building
### Windows
`javac --module-path "Location of JavaFX Library" --add-modules javafx.controls,javafx.fxml DobutsuShogi.java`

## Running
### Windows
`java --module-path "Location of JavaFX Library" --add-modules javafx.controls,javafx.fxml DobutsuShogi`

Choose a player setting from the dropdown menu.
1 v 1 is for player vs player
The rest are for player vs AI, where the number next to the option is the depth the AI will search to.
Then play the game.
