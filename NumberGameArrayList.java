package Project2;

import java.util.ArrayList;

public class NumberGameArrayList implements NumberSlider {
    private int [][] grid;
    private int height;
    private int width;
    private int winningValue;
    private ArrayList< int [][]> undoList;

    /**
     * Reset the game logic to handle a board of a given dimension
     *
     * @param height the number of rows in the board
     * @param width the number of columns in the board
     * @param winningValue the value that must appear on the board to
     *                     win the game
     * @throws IllegalArgumentException when the winning value is not power of two
     *  or is negative
     */
    @Override
    public void resizeBoard(int height, int width, int winningValue) {
        if(winningValue > 0 && height>0 && width>0 && (winningValue & winningValue-1) == 0) {
            this.grid = new int[height][width];
            this.height = height;
            this.width = width;
            this.winningValue = winningValue;
            undoList = new ArrayList<>();
        }else{
            throw new IllegalArgumentException();
        }
    }

    /**
     * Remove all numbered tiles from the board and place
     * TWO non-zero values at random location
     */
    @Override
    public void reset() {
        for(int rows = 0; rows < height; rows++) {
            for (int columns = 0; columns < width; columns++) {
                grid[rows][columns] = 0;
            }
        }
        placeRandomValue();
        placeRandomValue();
        undoList.clear();
    }

    /**
     * Set the game board to the desired values given in the 2D array.
     * This method should use nested loops to copy each element from the
     * provided array to your own internal array. Do not just assign the
     * entire array object to your internal array object. Otherwise, your
     * internal array may get corrupted by the array used in the JUnit
     * test file. This method is mainly used by the JUnit tester.
     * @param ref
     */
    @Override
    public void setValues(int[][] ref) {
        for (int rows = 0; rows < height; rows++){
            for(int columns = 0; columns < width; columns++){
                grid[rows][columns] = ref[rows][columns];
            }
        }
    }

    /**
     * Insert one random tile into an empty spot on the board.
     *
     * @return a Cell object with its row, column, and value attributes
     *  initialized properly
     *
     * @throws IllegalStateException when the board has no empty cell
     */
    @Override
    public Cell placeRandomValue() {
        ArrayList<int[]>empty = new ArrayList<>();
        for(int i =0;i<height;i++){
            for(int j =0; j<width;j++){
                if(grid[i][j]==0){
                    int[] a = {i,j};
                    empty.add(a);
                }
            }
        }
        if(empty.size()>0){
            int randomPower = (int)(1+(Math.random()*2));
            int randomNumber = (int)Math.pow(2,randomPower);
            int randomIndex =(int)(Math.random()*empty.size());
            int [] temp = empty.get(randomIndex);
            grid[temp[0]][temp[1]]=randomNumber;
            return new Cell (temp[0],temp[1],randomNumber);
        }else{
            throw new IllegalStateException();
        }
    }

    /**
     *
     * @return an arraylist of Cells. Each cell holds the (row,column) and
     * value of a tile
     */
    @Override
    public boolean slide(SlideDirection dir) {
        ArrayList<Integer> listTemp = new ArrayList<>();
        for (int row = 0; row < this.height; row++){
            for( int col = 0; col < this.width; col++){
                listTemp.add(grid[row][col]);
            }
        }

        if( dir.equals(SlideDirection.UP)){
            slideUp(listTemp);
        }
        if( dir.equals(SlideDirection.DOWN)){
            slideDown(listTemp);
        }
        if( dir.equals(SlideDirection.RIGHT)){
            slideRight(listTemp);
        }
        if( dir.equals(SlideDirection.LEFT)){
            slideLeft(listTemp);
        }

        boolean flag = false;

        for (int row = 0; row < this.height; row++){
            for(int col = 0; col < this.width; col++){
                if(grid[row][col] != listTemp.get((row*width)+ col)){
                    flag = true;
                    break;
                }
            }
        }

        if(flag==true){
            int[][] a = new int[height][width];
            for(int i=0;i<height;i++){
                for(int j=0;j<width;j++){
                    a[i][j]=grid[i][j];
                }
            }
            undoList.add(a);
            for (int row = 0; row < this.height; row++){
                for(int col = 0; col < this.width; col++){
                    grid[row][col] = listTemp.get((row*width)+col);
                }
            }
            placeRandomValue();
        }
        return flag;
    }

    /**
     * Return the current state of the game
     * @return one of the possible values of GameStatus enum
     */
    @Override
    public ArrayList<Cell> getNonEmptyTiles() {
        ArrayList<Cell> list = new ArrayList<Cell>();
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                if(grid[i][j]!=0){
                list.add(new Cell(i,j,grid[i][j]));}
            }
        }
        return list;
    }

    /**
     * Return the current state of the game
     * @return one of the possible values of GameStatus enum
     */
    @Override
    public GameStatus getStatus() {
        // return USER_WON if the value in grid reach the winningValue
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[i].length; j++){
                if(grid[i][j] == this.winningValue) {
                    return GameStatus.USER_WON;
                }
            }
        }

        // return IN_PROGRESS if there is still an empty tiles
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[i].length; j++){
                if(grid[i][j] == 0){
                    return GameStatus.IN_PROGRESS;
                }
            }
        }

        // return IN_PROGRESS if 2 titles of the same value is next to each other in vertical
        for(int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length-1; j++) {
                if(grid[i][j] == grid[i][j+1]){
                    return GameStatus.IN_PROGRESS;
                }
            }
        }

        // return IN_PROGRESS if 2 titles of the same value is next to each other in horizontal
        for(int i = 0; i < grid[0].length; i++){
            for (int j = 0; j < grid.length-1; j++){
                if(grid[j][i] == grid[j+1][i]){
                    return GameStatus.IN_PROGRESS;
                }
            }
        }

        // return .USER_LOST if it doesn't match any cases above
        return GameStatus.USER_LOST;
    }

    /**
     * Undo the most recent action, i.e. restore the board to its previous
     * state. Calling this method multiple times will ultimately restore
     * the game to the very first initial state of the board holding two
     * random values. Further attempt to undo beyond this state will throw
     * an IllegalStateException.
     *
     * @throws IllegalStateException when undo is not possible
     */
   
        @Override
        public void undo() {
            //create temp array to hold last move
            int[][] temp = new int[this.height][this.width];
            if(undoList.size()-1<0){
                throw new IllegalStateException();
            }
            //copy last move to board
            temp = undoList.get((undoList.size())-1);
            for(int i=0;i<temp.length;i++){
                for(int j=0;j<temp[i].length;j++){
                    grid[i][j]=temp[i][j];
                }
            }
            //remove last move
            undoList.remove(undoList.size() -1);
        }
    

    /**
     * slide the board up
     */
    public void slideUp(ArrayList<Integer> listTemp){
        // move all the tiles with value up
        for(int col = 0; col < this.width; col++){
            int num = 0;
            for(int row = 0; row < this.height; row++){
                if(listTemp.get((row*width)+col) != 0) {
                    listTemp.set(((num * width) + col), listTemp.get((row * width) + col));
                    if (num != row) {
                        listTemp.set(((row * width) + col), 0);
                    }
                    num++;
                }
            }
        }

        for(int col = 0; col < width; col++){
            for(int row = 0; row < height-1; row++) {
                if (listTemp.get((row * width) + col).equals(listTemp.get((row + 1) * width + col))) {
                    listTemp.set(((row * width) + col), listTemp.get((row * width) + col)*2);
                    listTemp.set((((row+1) * width) + col), 0);
                }
            }
        }

        for(int col = 0; col < this.width; col++){
            int num = 0;
            for(int row = 0; row < this.height; row++){
                if(listTemp.get((row*width)+col) != 0) {
                    listTemp.set(((num * width) + col), listTemp.get((row * width) + col));
                    if (num != row) {
                        listTemp.set(((row * width) + col), 0);
                    }
                    num++;
                }
            }
        }

    }

    /**
     * slide the board down
     */
    public void slideDown(ArrayList<Integer> listTemp){
        // move all the tiles with value down
        for(int col = 0; col < this.width ; col++){
            int num = height-1;
            for(int row = height-1; row >= 0; row--){
                if(listTemp.get((row*width)+col) != 0){
                    listTemp.set(((num*width)+col),listTemp.get((row*width)+col));
                    if(num != row){
                        listTemp.set(((row*width)+col),0);
                    }
                    num--;
                }
            }
        }

        // if 2 tiles with the same number is next to each other
        // double the value of the tiles at the bottom
        // set the top tiles value to 0
        for(int col = 0; col < this.width; col++){
            for(int row = this.height -1; row > 0; row--) {
                if (listTemp.get(row * width + col).equals(listTemp.get((row - 1) * width + col))) {
                    listTemp.set(((row * width) + col), listTemp.get((row * width) + col)*2);
                    listTemp.set((((row-1) * width) + col), 0);
                }
            }
        }


        // move the rest to the bottom after adding all the same value tiles
        for(int col = 0; col < this.width ; col++){
            int num = height-1;
            for(int row = height-1; row >= 0; row--){
                if(listTemp.get((row*width)+col) != 0){
                    listTemp.set(((num*width)+col),listTemp.get((row*width)+col));
                    if(num != row){
                        listTemp.set(((row*width)+col),0);
                    }
                    num--;
                }
            }
        }

    }

    /**
     * slide the board right
     */
    public void slideRight(ArrayList<Integer> listTemp){
        // move all the tiles with value to the right
        for(int row = 0; row < this.height; row++){
            int num = this.width -1;
            for(int col = this.width -1; col >= 0 ; col--){
                if(listTemp.get((row*width)+col) != 0){
                    listTemp.set(((row*width)+num),listTemp.get((row*width)+col));

                    if(num != col){
                        listTemp.set(((row*width)+col),0);

                    }
                    num--;
                }
            }
        }

        // if 2 tiles with the same number is next to each other
        // double the value of the tiles on the right
        // set the left tiles value to 0
        for(int row = 0; row < this.height; row++){

            for(int col = this.width -1; col > 0; col--) {
                 if (listTemp.get(row * width + col).equals(listTemp.get(row * width + (col - 1)))) {
                    listTemp.set(((row * width) + col), listTemp.get((row * width) + col)*2);
                    listTemp.set(((row * width) + col - 1), 0);
                }
            }
        }

        // move the rest to the right after adding all the same value tiles
        for(int row = 0; row < this.height; row++){
            int num = this.width -1;
            for(int col = this.width -1; col >= 0 ; col--){
                if(listTemp.get((row*width)+col) != 0){
                    listTemp.set(((row*width)+num),listTemp.get((row*width)+col));

                    if(num != col){
                        listTemp.set(((row*width)+col),0);
                    }
                    num--;
                }
            }
        }
    }

    /**
     * slide the board to the left
     */
    public void slideLeft(ArrayList<Integer> listTemp){
        for(int row = 0; row < height; row++){
            int num = 0;
            for(int col = 0; col < width; col++){
                if(listTemp.get((row*width)+col) != 0){
                    listTemp.set(((row*width)+num),listTemp.get((row*width)+col));

                    if(num!=col){
                        listTemp.set(((row*width)+col),0);
                    }
                    num++;
                }
            }
        }

        for(int row = 0; row < this.height; row++){

            for(int col = 0; col < this.width -1; col++) {
                if (listTemp.get(row * width + col).equals(listTemp.get(row * width + col + 1))) {
                    listTemp.set(((row * width) + col), listTemp.get((row * width) + col)*2);
                    listTemp.set(((row * width) + col + 1), 0);
                }
            }
        }

        for(int row = 0; row < height; row++){
            int num = 0;
            for(int col = 0; col < width; col++){
                if(listTemp.get((row*width)+col) != 0){
                    listTemp.set(((row*width)+num),listTemp.get((row*width)+col));

                    if(num!=col){
                        listTemp.set(((row*width)+col),0);
                    }
                    num++;
                }
            }
        }


    }
}
