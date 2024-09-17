

import java.util.*;

public class Maze {
    // store dimensions
    private int width, height, numColors;

    // 2D arraylist to store all the tiles
    //outer arraylist stores rows
    //inner arraylist stors cols

    private ArrayList<ArrayList<Tile>> map;

    // 3D ArrayList to store whether we reached a tile
    // in a given color

    // color -- row -- col
    private ArrayList<ArrayList<ArrayList<Character>>> reached;

    // reachable collection
    private ArrayDeque<State> reachableCollection;
    private State start, finish;

    private static final char NOT_REACHED = '#';
    private static final char FROM_START = '@';

    private static final char GO_NORTH = 'N';
    private static final char GO_SOUTH = 'S';
    private static final char GO_WEST = 'W';
    private static final char GO_EAST = 'E';


    public Maze(Scanner in) {
        numColors = in.nextInt();
        height = in.nextInt();
        width = in.nextInt();

        if (numColors > 26 || numColors < 0) { // check if it's in the num color range
            System.err.println("Error : out of color range!");
            System.exit(1);
        }
        if(width < 1 || height < 1){
            System.err.println("Width or height is less than 1!");
            System.exit(1);
        }

        // read the rest of the line to avoid junk

        in.nextLine();

        // allocate enough space for height rows
        map = new ArrayList<>(height);

        // helper var for tracking how many rows we have read
        int row = 0;
        while (in.hasNextLine()) {
            if (row >= height) {
                break;
            }
            // read in line
            String line = in.nextLine();
            //check if comment and skip
            if (line.length() >= 2 && line.charAt(0) == '/' && line.charAt(1) == '/') {
                continue;
            }

            // we know we have a line of tiles to process

            ArrayList<Tile> rowOfTiles = new ArrayList<>(width);
            for (int i = 0; i < line.length(); i++) {
                char sym = line.charAt(i);
                if (sym == '@') {
                    start = new State(ColorValue.fromIndex(0), new Point(row, i));
                    //error checking for one state
                }
                if (sym == '?') {
                    finish = new State(ColorValue.fromIndex(0), new Point(row, i));
                }
                rowOfTiles.add(new Tile(line.charAt(i)));
            }

            map.add(rowOfTiles);
            row++;
        }
    }

    public void search(ConFig c) {
        // step
        // fill in our reached AL with false
        // only when in checkpoint 2
        reached = new ArrayList<>(numColors + 1);
        for (int i = 0; i < numColors + 1; i++) {
            // make an array list to store rows for this color
            // color, row, col
            ArrayList<ArrayList<Character>> rowList = new ArrayList<>(height);
            for (int row = 0; row < height; row++) {
                //array list for col values
                ArrayList<Character> colList = new ArrayList<>(width);
                for (int col = 0; col < width; col++) {
                    colList.add(NOT_REACHED); // we haven't visited
                }
                rowList.add(colList);
            }
            reached.add(rowList);
        }

        // initialize our reachable collection
        reachableCollection = new ArrayDeque<>();
        //step 2

        markReached(start, FROM_START);
        reachableCollection.addFirst(start);
        System.out.println("  adding (" + start.getColorValue() + ", (" + start.getPoint().getRow() + "," + start.getPoint().getCol() + "))");


        int i = 1; // counting
        while (!reachableCollection.isEmpty() && !isTarget()) {
            State curr;
            Point finishPoint = finish.getPoint();
            if (c.isQueueMode()) {
                curr = reachableCollection.removeLast();
            } else {
                curr = reachableCollection.removeFirst();
            }
            i++;
            if (!curr.getPoint().equals((finishPoint))) {
                System.out.println("" + i + ": processing (" + curr.getColorValue() + ", " + curr.getPoint() + ")");

            }
            if (c.isCheckpoint2()) {
                continue;
            }
            // i increments each time an item is removed
            if (isButton(curr)){ // add ur new location
                State on = new State(new ColorValue(Symbol(curr.getPoint())), curr.getPoint()); // giving u the colorvalue & point = makes new state!
                if (canBeReached(on)) {
                    //curr.setColorValue(new ColorValue(map.get(curr.getPoint().getRow()).get(curr.getPoint().getCol()).getSymbol())); // setting the new color value
                    reachableCollection.addFirst(on); // org had curr
                    markReached(on, curr.getColorValue().asButton());
                    System.out.println("adding (" + curr.getColorValue() + ", (" + curr.getPoint().getRow() + "," + curr.getPoint().getCol() + "))");


                }

            } else {

                //mark reached from our current color value
                //markReached(newColorState, curr.getColorValue().asButton()); how we can do it for our color

                State north = new State(curr.getColorValue(), curr.getPoint().north());
                if (canBeReached(north)) {
                    markReached(north, GO_NORTH);
                    reachableCollection.addFirst(north);
                    System.out.println(" adding (" + north.getColorValue() + ", (" + north.getPoint().getRow() + " ," + north.getPoint().getCol() + "))");


                }

            }
            State east = new State(curr.getColorValue(), curr.getPoint().east());
            if (canBeReached(east)) {
                markReached(east, GO_EAST);
                reachableCollection.addFirst(east);
                System.out.println(" adding (" + east.getColorValue() + ", (" + east.getPoint().getRow() + " ," + east.getPoint().getCol() + "))");

            }
            State south = new State(curr.getColorValue(), curr.getPoint().south()); // generates where we can go
            if (canBeReached(south)) {
                markReached(south, GO_SOUTH);
                reachableCollection.addFirst(south);
                System.out.println(" adding (" + south.getColorValue() + ", (" + south.getPoint().getRow() + " ," + south.getPoint().getCol() + "))");

            }

            State west = new State(curr.getColorValue(), curr.getPoint().west());
            if (canBeReached(west)) {
                markReached(west, GO_WEST);
                reachableCollection.addFirst(west);
                System.out.println(" adding (" + west.getColorValue() + ", (" + west.getPoint().getRow() + " ," + west.getPoint().getCol() + "))");

            }

        }


    }

    public void printSolution(ConFig c) {
        List<State> backtrack = backtrack();
        // output with the correct version
        if(backtrack.isEmpty()){
            System.out.println("No solution.");
            System.out.println("Reachable:");
            printNoSolution();

        } else if (c.isMapOutputMode()) {
            printMapOutput(backtrack);
        } else {
            //print the list
            for (State s : backtrack) {
                // performance of this... is it good?
                System.out.println(s + "\n");
            }
        }
    }


    public void printMapOutput(List<State> backtrack) {
        // first we need to create a duplicate map for our outputs

        ArrayList<ArrayList<ArrayList<Character>>> output = new ArrayList<>(numColors + 1);
        for (int i = 0; i < numColors + 1; i++) {
            // make an array list to store rows for this color
            ArrayList<ArrayList<Character>> rowList = new ArrayList<>(height);
            for (int row = 0; row < height; row++) {
                ArrayList<Character> colList = new ArrayList<>(width);
                for (int col = 0; col < width; col++) {
                    if (i > 0 && start.getPoint().getRow() == row && start.getPoint().getCol() == col) {
                        colList.add('.');
                    } else {
                        colList.add(map.get(row).get(col).render(ColorValue.fromIndex(i)));
                    }
                    // we haven't visited
                }
                rowList.add(colList);
            }
            output.add(rowList);
        }
        //starter map
        // walk through solution and update the characters that are stored along the path
            for (State curr : backtrack) {
                if (curr == backtrack.get(0) || curr == backtrack.get(backtrack.size() - 1)) {
                    // skip this if we are in our finish
                    continue;
                }
                Tile currTile = map.get(curr.getPoint().getRow()).get(curr.getPoint().getCol());
                if (currTile.getSymbol() == '.') {
                    output.get(curr.getColorValue().asIndex()).get(curr.getPoint().getRow()).set(curr.getPoint().getCol(), '+');
                } else if ((currTile.getSymbol() >= 'a' && currTile.getSymbol() <= 'z')) { // ur on a button
                    char temp = getBacktrack(curr);
                    if ((temp >= 'a' && temp <= 'z') || temp == '^') {
                        output.get(curr.getColorValue().asIndex()).get(curr.getPoint().getRow()).set(curr.getPoint().getCol(), '@');
                    } else {
                        output.get(curr.getColorValue().asIndex()).get(curr.getPoint().getRow()).set(curr.getPoint().getCol(), '%');
                    }
                    //starting from a button

                }
                // walking through a door
                if (currTile.getSymbol() >= 'A' && currTile.getSymbol() <= 'Z') {
                    // touched a door
                    char temp = getBacktrack(curr);
                    if (temp >= 'A' && temp <= 'Z') {
                        output.get(curr.getColorValue().asIndex()).get(curr.getPoint().getRow()).set(curr.getPoint().getCol(), '+');
                    } else {
                        output.get(curr.getColorValue().asIndex()).get(curr.getPoint().getRow()).set(curr.getPoint().getCol(), '+');
                    }

                }


            }

            for (int co = 0; co <= numColors; co++) {
                ColorValue curr = ColorValue.fromIndex(co);
                System.out.print("// color " + curr + "\n");
                for (int row = 0; row < height; row++) {
                    for (int col = 0; col < width; col++) {
                        System.out.print(output.get(co).get(row).get(col));
                    }
                    System.out.print("\n");
                }
            }
        }



    /**
     * Backtrack through the reached items to find the start
     *
     * @return a list of the states
     */

    public List<State> backtrack() {
        // start from the end point
        State curr = null;
        ArrayList<State> backtrack = new ArrayList<>();
        for (int color = 0; color < numColors + 1; color++) {
            // check backtrack to see if reached
            char temp = reached.get(color).get(finish.getPoint().getRow()).get(finish.getPoint().getCol());
            //System.out.println(reached);
            if (temp != NOT_REACHED) {
                //this was the color we reached the finish in
                curr = new State(ColorValue.fromIndex(color), finish.getPoint());
                //stop looping
                break;
            }
        }

        // at this point, we should have a value in curr
        //if we value(null) --> no solution

        if (curr == null) {
            return backtrack;
        }


        //commence backtracking
        backtrack.add(curr);

        //Keep looping while our current state is not the starting point. while backtrack for current is not '#'
        while (getBacktrack(curr) != FROM_START) {
            //get one more state and add to our list
            char dir = getBacktrack(curr);
            if (dir == GO_NORTH) {
                // backtrack south
                curr = new State(curr.getColorValue(), curr.getPoint().south());
            } else if (dir == GO_SOUTH) {
                curr = new State(curr.getColorValue(), curr.getPoint().north());
            } else if (dir == GO_EAST) {
                curr = new State(curr.getColorValue(), curr.getPoint().west());
            } else if (dir == GO_WEST) {
                curr = new State(curr.getColorValue(), curr.getPoint().east());
            } else if ((dir >= 'a' && dir <= 'z') || dir == '^') {
                //pressed a button
                curr = new State(new ColorValue(dir), curr.getPoint());
            } else {
                throw new IllegalStateException("reached a backtrack location with no place to go" + curr);
            }
            // new location
            backtrack.add(curr);

        }
        //REVERSE THIS HERE
        Collections.reverse(backtrack); // reverses the order, so we get it in a normal order as well
        return backtrack;
    }


    private char getBacktrack(State curr) {
        return reached.get(curr.getColorValue().asIndex()).get(curr.getPoint().getRow()).get(curr.getPoint().getCol());
    }


    private static boolean colorMatch(State curr, ColorValue match) {
        // this is to check if were on a button does the button match the current value of the state
        if (!curr.getColorValue().equals(match)) { // return the curr value and check if its ==.
            return true;
        }
        return false;
    }

    /**
     * Mark that a state was reached and from when we come
     *
     * @param st   the state to visit
     * @param from the direction or button color
     */
    private void markReached(State st, char from) {
        int colorIdx = st.getColorValue().asIndex();
        Point p = st.getPoint();
        reached.get(colorIdx).get(p.getRow()).set(p.getCol(), from);
    }


    private boolean canBeReached(State stFrom) { // state from and state to
        int colorIdx = stFrom.getColorValue().asIndex();
        Point p = stFrom.getPoint();

        if (p.getCol() < 0 || p.getCol() >= width || p.getRow() < 0 || p.getRow() >= height) {
            //outside the bounds
            return false;
        } else if (reached.get(colorIdx).get(p.getRow()).get(p.getCol()) != NOT_REACHED) {
            return false;
        }
        Tile curr = map.get(p.getRow()).get(p.getCol());
        if (curr.getSymbol() == '#' || stFrom.getColorValue().asIndex() != colorIdx) {
            return false;
        }
        if (curr.isDoor() && !stFrom.getColorValue().equals(curr.colorValueTile())) {
            return false;
        }
        if(curr.isButton()) {
            return true;
        }
        return true;
    }

    public  char Symbol(Point p) {
        // returning symbol from point
        return map.get(p.getRow()).get(p.getCol()).getSymbol();
    }


    public  boolean isButton(State st) {
        char sym = map.get(st.getPoint().getRow()).get(st.getPoint().getCol()).getSymbol();
        return ((sym >= 'a' && sym <= 'z') || sym == '^') && sym != st.getColorValue().asButton();
    }

    public  boolean isTarget(){ // finish point check!
        for(int i = 0; i < numColors + 1; i++ ){
            if(reached.get(i).get(finish.getPoint().getRow()).get(finish.getPoint().getCol()) != NOT_REACHED){
                return true;
            }

        }
        return false;

    }

    private void printNoSolution(){
        // numColors + 1; all closed + each of the colors
            for (int row = 0; row < height; row++) {
                for (int col = 0; col<width; col++ ){
                    Tile curr = map.get(row).get(col);
                    boolean pounds = true;
                    for(int co = 0; co < numColors+1; co++){
                        char sym = reached.get(co).get(row).get(col);
                        if(sym != NOT_REACHED){
                            pounds = false;
                            break;
                        }

                    }
                    if(pounds){
                        System.out.println("#");
                    }else{
                        System.out.println(curr);
                    }
                }
                System.out.println("\n");
            }

    }



    public void printMap() {
        // numColors + 1; all closed + each of the colors
        for (int i = 0; i < numColors + 1; i++) {
            ColorValue curr = ColorValue.fromIndex(i);
            System.out.print("// color " + curr + "\n");
            for (ArrayList<Tile> row : map) {
                StringBuilder sb = new StringBuilder(width + 1);
                for (Tile col : row) {
                    sb.append(col.render(curr));
                }
                // row is output
                sb.append("\n");
                System.out.print(sb);


            }
        }
    }
}


