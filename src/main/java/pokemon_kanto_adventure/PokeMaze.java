package pokemon_kanto_adventure;

import java.util.Stack;
import java.util.Scanner;

public class PokeMaze {
    // row = 9, cloumn = 17
    private static char[][] graph = { //the maze pattern
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
            {'#', 'S', '.', '.', '.', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
            {'#', '#', '#', '#', '#', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
            {'#', '.', '.', '.', '#', '.', '#', '#', '#', '.', '.', '.', '.', '.', '#', '#', '#'},
            {'#', '#', '#', '.', '#', '.', '#', '#', '#', 'G', '#', '.', '#', '.', '#', '#', '#'},
            {'#', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '#'},
            {'#', '#', '#', '#', '#', 'G', '#', '.', '#', '.', '#', '.', '#', '#', '#', '.', '#'},
            {'#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '#'},
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', 'E', '#'},
    };
    private static Stack<Integer> row;//stack containing passed row index for bactracking
    private static Stack<Integer> col;//stack containing passed column index for backtracking
    //row + col at same index together represent the position history
    private static Stack<String> movement; //stack conatining movement history for backtracking

    public PokeMaze() {
        row = new Stack<>(); //create new Stacks
        col = new Stack<>();
        movement = new Stack<>();
        System.out.println("Welcome to the PokeMaze Challenge"); //display welcome to the maze message
        System.out.println("Find your way through the maze using stacks");
        System.out.println("Legend: # - Wall, . - Path, S - Start, E - End, G - Ghastly");//display maze legend
        row.push(1);//push the initial position
        col.push(1);
        InitializeGraph();
    }

    public void InitializeGraph() { //Initialize the Maze and draw it using the maze pattern above
        for (char[] Gcol : graph) {
            for (char Grow : Gcol) {
                System.out.print(Grow + "  ");
            }
            System.out.println();
        }
    }

    public void PrintGraph() { //print the maze and the player's position everytime player makes a move
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                char elem = (row.peek().compareTo(i) == 0 && col.peek().compareTo(j) == 0) ? 'Y' : graph[i][j];
                System.out.print(elem + "  ");
            }
            System.out.println();
        }
    }

    public void simulation(Player player) { //method that allows player walk through the maze
        Scanner sc = new Scanner(System.in);
        while (true) { //while player does not get caught by Ghastly or reach the end of the maze the loop will not end
            boolean G = false, E = false, W = false;//G is the state of getting caught by Ghastly, E is the state of reaching the end of the maze, W is the state of bumping into a wall
            System.out.print("Enter direction (up, down, left, right): ");//get direction from user
            String choice = sc.nextLine();//receive direction
            move(choice);//move the player according to the direction
            char pointer = graph[row.peek()][col.peek()];//check the player's position
            if (pointer == 'G') {//if the player's position is on a Ghastly
                G = true; //player gets caught by Ghastly
                PrintGraph();//display the maze and player's position
                System.out.println("Oh no! You encountered a Ghastly and got caught.");//display get caught message
                System.out.println("Game Over.");//display game over message
                break;//end loop
            } else if (pointer == 'E') {//if player's position is the end of hte maze
                E = true;//player reaches the end of the maze
                PrintGraph();//display the maze and the player's position
                System.out.println("Congratulations! You've reached the end of the maze.");//display win message
                System.out.println("You found $1000 in the maze!");
                player.addMoney(1000);//player earns $1000 as reward
                break;//end loop
            } else if (pointer == '#') {//if player's position is on a wall
                W = true;//player bumps into a wall
                System.out.println("Invalid move.");//display invalid move message
                backmove(choice);//let player go back one step, before bumping on the wall
                PrintGraph();//display maze and player position
            }
            if (!G && !E && !W) {//if neither of the three mentioned states is true, that means player is on a path, continue the maze game
                PrintGraph();//print the maze and the player's loaction
            }
        }
        System.out.println("Total steps: " + movement.size());//if maze is ended, no matter win or lost, display the total steps
        System.out.println("Your path:");//backtrack the path taken and print it out
        StringBuilder sb = new StringBuilder();
        while (!movement.isEmpty()) {
            sb.insert(0, (" --> " + movement.pop() + " --> | " + col.pop() + " , " + row.pop() + " |"));
        }
        sb.insert(0, ("| " + row.pop() + " , " + col.pop() + " |"));
        System.out.println(sb);
    }

    public void move(String choice) {//move the player according to movement

        switch (choice) {
            case "right"://if player moves right
                movement.push(choice);//push the movement into movement history stack
                col.push(col.peek() + 1);//push the new coordinates of the player into col and row, col+1 because moving right
                row.push(row.peek());
                break;
            case "left"://if player moves left
                movement.push(choice);//push the movement into movement history stack
                col.push(col.peek() - 1);//push the new coordinates of the player into col and row, col-1 because moving right
                row.push(row.peek());
                break;
            case "up"://if the player moves up
                movement.push(choice);//push the movement into movement history stack
                row.push(row.peek() - 1);//push the new coordinates of the player into col and row, row-1 because moving up
                col.push(col.peek());
                break;
            case "down"://if the player moves down
                movement.push(choice);//push the movement into movement history stack
                row.push(row.peek() + 1);//push the new coordinates of the player into col and row, row+1 because moving down
                col.push(col.peek());
                break;
            default:
                System.out.println("Inavailable choice");//if invalid direction print this
        }
    }

    public void backmove(String choice) {//move back to last coordinates

        switch (choice) {
            case "right"://all cases are the same, pop top element in movement, row, col
                movement.pop();
                col.pop();
                row.pop();
                break;
            case "left":
                movement.pop();
                col.pop();
                row.pop();
                break;
            case "up":
                movement.pop();
                row.pop();
                col.pop();
                break;
            case "down":
                movement.pop();
                row.pop();
                col.pop();
                break;
            default://if direction choice is invalid, print message below
                System.out.println("Inavailable choice");
        }
    }
}