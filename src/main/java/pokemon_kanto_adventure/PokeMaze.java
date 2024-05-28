package pokemon_kanto_adventure;

import java.util.Stack;
import java.util.Scanner;

public class PokeMaze {
    // row = 9, cloumn = 17
    private static char[][] graph = {
            { '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' },
            { '#', 'S', '.', '.', '.', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' },
            { '#', '#', '#', '#', '#', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' },
            { '#', '.', '.', '.', '#', '.', '#', '#', '#', '.', '.', '.', '.', '.', '#', '#', '#' },
            { '#', '#', '#', '.', '#', '.', '#', '#', '#', 'G', '#', '.', '#', '.', '#', '#', '#' },
            { '#', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '#' },
            { '#', '#', '#', '#', '#', 'G', '#', '.', '#', '.', '#', '.', '#', '#', '#', '.', '#' },
            { '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '#' },
            { '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', 'E', '#' },
    };
    private static Stack<Integer> row;
    private static Stack<Integer> col;
    private static Stack<String> movement;

    public PokeMaze() {
        row = new Stack<>();
        col = new Stack<>();
        movement = new Stack<>();
        System.out.println("Welcome to the PokeMaze Challenge");
        System.out.println("Find your way through the maze using stacks");
        System.out.println("Legend: # - Wall, . - Path, S - Start, E - End, G - Ghastly");
        row.push(1);
        col.push(1);
        InitializeGraph();
    }

    public void InitializeGraph() {
        for (char[] Gcol : graph) {
            for (char Grow : Gcol) {
                System.out.print(Grow + "  ");
            }
            System.out.println();
        }
    }

    public void PrintGraph() {
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                char elem = (row.peek().compareTo(i) == 0 && col.peek().compareTo(j) == 0) ? 'Y' : graph[i][j];
                System.out.print(elem + "  ");
            }
            System.out.println();
        }
    }

    public void simulation(Player player) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            boolean G = false, E = false, W = false;
            System.out.print("Enter direction (up, down, left, right): ");
            String choice = sc.nextLine();
            move(choice);
            char pointer = graph[row.peek()][col.peek()];
            if (pointer == 'G') {
                G = true;
                PrintGraph();
                System.out.println("Oh no! You encountered a Ghastly and got caught.");
                System.out.println("Game Over.");
                break;
            } else if (pointer == 'E') {
                E = true;
                PrintGraph();
                System.out.println("Congratulations! You've reached the end of the maze.");
                System.out.println("You found $1000 in the maze!");
                player.addMoney(1000);
                break;
            } else if (pointer == '#') {
                W = true;
                System.out.println("Invalid move.");
                backmove(choice);
                PrintGraph();
            }
            if (!G && !E && !W) {
                PrintGraph();
            }
        }
        System.out.println("Total steps: " + movement.size());
        System.out.println("Your path:");
        StringBuilder sb = new StringBuilder();
        while (!movement.isEmpty()) {
            sb.insert(0, (" --> " + movement.pop() + " --> | " + col.pop() + " , " + row.pop() + " |"));
        }
        sb.insert(0, ("| " + row.pop() + " , " + col.pop() + " |"));
        System.out.println(sb);
    }

    public void move(String choice) {

        switch (choice) {
            case "right":
                movement.push(choice);
                col.push(col.peek() + 1);
                row.push(row.peek());
                break;
            case "left":
                movement.push(choice);
                col.push(col.peek() - 1);
                row.push(row.peek());
                break;
            case "up":
                movement.push(choice);
                row.push(row.peek() - 1);
                col.push(col.peek());
                break;
            case "down":
                movement.push(choice);
                row.push(row.peek() + 1);
                col.push(col.peek());
                break;
            default:
                System.out.println("Inavailable choice");
        }
    }

    public void backmove(String choice) {

        switch (choice) {
            case "right":
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
            default:
                System.out.println("Inavailable choice");
        }
    }
}
