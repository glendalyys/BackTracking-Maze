/**
 * Represents a single spot on the maze
 */

public class Tile {
    private char symbol;

    public Tile(char sym){
        symbol = sym;
    }

    public char getSymbol(){
        return symbol;
    }

    public char render(ColorValue v){
        if(symbol == v.asButton() || symbol == v.asDoor()){ // changes it to a button
            return '.';
        }
        return symbol;
    }


    public ColorValue colorValueTile(){
        ColorValue newColor;
        newColor = null;
        if(symbol >= 'A' && symbol <= 'Z'){
            newColor = new ColorValue(Character.toLowerCase(symbol));
        }
        return newColor;
    }

    public boolean isDoor(){
        if(symbol >= 'A' && symbol <= 'Z' ){
            return true;
        }
        return false;
    }
    public boolean isButton(){
        if(symbol >= 'a' && symbol <= 'z'){
            return true;
        }
           return false;
    }

    @Override
    public String toString(){
        return String.valueOf(symbol);
    }

    public boolean isTrap() {
        if (symbol == '^'){
            return true;
        }
        return false;
    }
}
