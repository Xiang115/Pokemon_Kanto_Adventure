package pokemon_kanto_adventure;

import java.io.*;
import java.util.*;

/*
For the Safari Zone, there are 7 pokemons want to be arranged with conditions. In order to arrange the lineup
more systematic and effectively, the conditions will be fulfilled based on the priority. The level of the priorities
are as below:
i) The highest priority: Eevee, Pikachu, Snorlax
ii) Second highest priority: Bulbasaur, Charmander and Machop (Bulbasaur and Charmander must be in the same list)
iii) Third highest priority: Jigglypuff
iv) The lowest priority: Other Pokemon
 */
public class safari {

    public static void SafariZone() {
        Scanner sc = new Scanner(System.in);
        System.out.printf("+%s+\n", "-".repeat(90));
        System.out.println("Welcome to the Safari Zone! Today's challenge: Sort the Pokemon!");
        System.out.printf("+%s+\n", "-".repeat(90));
        //The system prompt user to enter the pokemons.
        System.out.print("Enter the Pokemon in your party (separated by a comma): ");
        String input = sc.nextLine();
        //Each pokemon entered are stored in an array
        String[] pokemonArray = input.split(", ");
        //The array is transferred into arraylist
        ArrayList<String> list = new ArrayList<>(Arrays.asList(pokemonArray));

        /*
        The system check the input entered, if the input entered is invalid, the user need to enter again until a valid input obtained
        Make sure every pokemon can only be entered once and the can only the pokemon that available in the game
         */
        while (true) {
            if (CheckInput(list)) {
                break;
            } else {
                //The system will ask the user to enter the correct input if the user don't follow the appropriate format
                System.out.println("Enter correct input (Example: Pikachu, Eevee, Snorlax), no duplicates(every pokemon can only be entered once), and only pokemons that are available in the game, which you can check in the guides.");
                System.out.print("Enter the Pokemon in your party (separated by a comma): ");
                input = sc.nextLine();
                pokemonArray = input.split(", ");
                list = new ArrayList<>(Arrays.asList(pokemonArray));
            }
        }
        System.out.println("");
        //display the current sorted list
        System.out.println("You entered: " + displayList(list));
        System.out.println("");
        System.out.println("Sorting your Pokemon according to their unique preferences...");
        System.out.println("");
        int step = 1; //This is used to initialize the step and display the number of step during the sorting

        //Arrange Eevee to the first position of the lineup
        if (list.contains("Eevee")) {
            System.out.println("Step " + step + " : Eevee insists on being positioned either at the beginning of the lineup to showcase its adaptability.");
            //Interchange the position of the pokemon at the beginning of the lineup and the position of Eevee
            String temp = list.get(0);
            int indexEevee = list.indexOf("Eevee");
            list.set(0, "Eevee");
            list.set(indexEevee, temp);
            //display the current sorted list
            System.out.println("Partial sort: " + displayList(list));
            System.out.println("");
            step++;
        }

        //Arrange Pikachu to the center of the lineup
        if (list.contains("Pikachu")) {
            System.out.println("Step " + step + " : Pikachu demands to be placed at the center of the arrangement.");
            //Interchange the position of the pokemon at the center of the lineup and the position of Pikachu
            String temp = list.get(list.size() / 2);
            int indexPikachu = list.indexOf("Pikachu");
            list.set(list.size() / 2, "Pikachu");
            list.set(indexPikachu, temp);
            //display the current sorted list
            System.out.println("Partial sort: " + displayList(list));
            System.out.println("");
            step++;
        }

        //Arrange Snorlax to the end of the lineup
        if (list.contains("Snorlax")) {
            System.out.println("Step " + step + " : Snorlax insists on being positioned at the end of the lineup to ensure maximum relaxation.");
            //Interchange the position of the pokemon at the end of the lineup and the position of Snorlax
            String temp = list.get(list.size() - 1);
            int indexSnorlax = list.indexOf("Snorlax");
            list.set(list.size() - 1, "Snorlax");
            list.set(indexSnorlax, temp);
            //display the current sorted list
            System.out.println("Partial sort: " + displayList(list));
            System.out.println("");
            step++;
        }

        boolean success = false; //a booelan to check whether a sort pattern that fulfills all requirements is found

        //If only Machop is found in the list, then set the success variable to be true
        if (list.size() == 1 && list.contains("Machop")) {
            success = true;
        }

        //Arrange Machop to the position which is placed next to the heaviest pokemon
        ArrayList<String> initialList = list;
        //initialize the variable for heaviest Pokemon and its weight
        String heaviestPokemon = "";
        double maxWeight = 0;
        //If the list has more than 1 Pokemon and involves Machop, then proceed to the code below
        if (list.contains("Machop") && list.size() > 1) {
            //Use for loop to check which pokemon has the greatest weight (Excluding Machop)
            for (int i = 0; i < list.size(); i++) {
                if (library.pokemon_weight.get(list.get(i)) > maxWeight && !(list.get(i).equals("Machop"))) {
                    maxWeight = library.pokemon_weight.get(list.get(i));
                    heaviestPokemon = list.get(i);
                }
            }
            int indexMax = list.indexOf(heaviestPokemon);

            if (indexMax - 1 >= 0) { //check if there is a slot at the left of the heaviest pokemon
                String temp = list.get(indexMax - 1);
                /*Since the condition of Machop is in the second highest priority, so need to make sure the pokemons at the left
                  of the heaviest pokemon are not the pokemon with higher priority*/
                if (!(temp.equals("Eevee") || temp.equals("Pikachu") || temp.equals("Snorlax"))) {
                    int indexMachop = list.indexOf("Machop");
                    //Add a condition that the initial position of Machop is not at the left of the position of the heaviest Pokemon
                    if (indexMachop != indexMax - 1) {
                        System.out.println("The heaviest pokemon is: " + heaviestPokemon);
                        //Interchange the position of the pokemon at the left of the heaviest pokemon and the position of Machop
                        list.set(indexMax - 1, "Machop");
                        list.set(indexMachop, temp);
                        System.out.println("Step " + step + " : Machop demands to be placed next to the heaviest Pokemon in the lineup to show off its strength");
                        //display the current sorted list
                        System.out.println("Partial sort: " + displayList(list));
                        System.out.println("");
                        step++;
                    }
                    ArrayList<String> initialListMachop = list; //Initialize an arraylist for initial list after sorting Machop
                    //After sorting Machop, then check for Bulbasaur and Charmander if these 2 Pokemon are in the same list
                    if (list.contains("Bulbasaur") && list.contains("Charmander")) {
                        int indexB = list.indexOf("Bulbasaur");
                        int indexC = list.indexOf("Charmander");
                        //Check whether Bulbasaur and Charmander are next to each other or not
                        if (Math.abs(indexB - indexC) == 1) {
                            //Use for loop to check the pokemon with lower priority
                            for (int i = 0; i < list.size(); i++) {
                                reverse(initialListMachop, list);//reset the list until after machop is swapped everytime loop starts
                                if (!(list.get(i).equals("Eevee") || list.get(i).equals("Pikachu") || list.get(i).equals("Snorlax") || i == indexB || i == indexC)) {
                                    //If the pokemon with lower priority is coming after Bulbasaur and Charmander, then the code execution is as below
                                    if (i > indexB && i > indexC) {
                                        if (indexB > indexC) {
                                            list.set(indexB, list.get(i));
                                            list.set(i, "Bulbasaur");
                                        } else {
                                            list.set(indexC, list.get(i));
                                            list.set(i, "Charmander");
                                        }
                                        System.out.println("Step " + step + " : Bulbasaur refuses to be placed next to Charmander, his fire burns too hot.");
                                        //display the current sorted list
                                        System.out.println("Partial sort: " + displayList(list));
                                        System.out.println("");
                                        step++;
                                    }
                                    //If the pokemon with lower priority is coming before Bulbasaur and Charmander, then the code execution is as below
                                    else if (i < indexB && i < indexC) {
                                        if (indexB < indexC) {
                                            list.set(indexB, list.get(i));
                                            list.set(i, "Bulbasaur");
                                        } else {
                                            list.set(indexC, list.get(i));
                                            list.set(i, "Charmander");
                                        }
                                        System.out.println("Step " + step + " : Bulbasaur refuses to be placed next to Charmander, his fire burns too hot.");
                                        System.out.println("Partial sort: " + displayList(list));
                                        System.out.println("");
                                        step++;
                                    }

                                    //After sorting Bulbasaur and Charmander, then do the arrangement for Jigglypuff if this Pokemon exist in the list
                                    if (list.contains("Jigglypuff")) {
                                        //Use for loop to check "cute" Pokemon
                                        for (int j = 0; j < list.size(); j++) {
                                            if (library.pokemon_cute.get(list.get(j)) && !(list.get(j).equals("Jigglypuff"))) {
                                                //If the "cute" pokemon is found, then check whether the pokemon at the left of the "cute" pokemon exist or not
                                                if (j - 1 >= 0) {
                                                    temp = list.get(j - 1);
                                                    /*Since the condition of Jigglypuff is in the lowest priority, so need to make sure the pokemons at the left
                                                      of the "cute" pokemon are not the pokemon with higher priority*/
                                                    if (!(temp.equals("Eevee") || temp.equals("Pikachu") || temp.equals("Snorlax") || temp.equals("Machop") || temp.equals("Bulbasaur") || temp.equals("Charmander"))) {
                                                        //Interchange the position of the pokemon at the left of the "cute" pokemon and the position of Jigglypuff
                                                        int indexJigglypuff = list.indexOf("Jigglypuff");
                                                        list.set(j - 1, "Jigglypuff");
                                                        list.set(indexJigglypuff, temp);
                                                        System.out.println("Step " + step + " :  Jigglypuff prefers to be surrounded by other \"cute\" Pokémon for morale purposes.");
                                                        //display the current sorted list
                                                        System.out.println("Partial sort: " + displayList(list));
                                                        System.out.println("");
                                                        step++;
                                                        success = true;
                                                        break;//If solution is found break the iteration and no need to find next cute pokemon;
                                                    }
                                                }

                                                /*If the pokemon which is at the left of the "cute" Pokemon is not exist and the solution is not found,
                                                  then proceed to find the pokemon which is at the right of the "cute" Pokemon*/
                                                if (j + 1 <= list.size() - 1 && !success) {
                                                    temp = list.get(j + 1);
                                                    /*Since the condition of Jigglypuff is in the lowest priority, so need to make sure the pokemons at the right
                                                      of the "cute" pokemon are not the pokemon with higher priority*/
                                                    if (!(temp.equals("Eevee") || temp.equals("Pikachu") || temp.equals("Snorlax") || temp.equals("Machop") || temp.equals("Bulbasaur") || temp.equals("Charmander"))) {
                                                        //Interchange the position of the pokemon at the right of the "cute" pokemon and the position of Jigglypuff
                                                        int indexJigglypuff = list.indexOf("Jigglypuff");
                                                        list.set(j + 1, "Jigglypuff");
                                                        list.set(indexJigglypuff, temp);
                                                        System.out.println("Step " + step + " :  Jigglypuff prefers to be surrounded by other \"cute\" Pokémon for morale purposes.");
                                                        //display the current sorted list
                                                        System.out.println("Partial sort: " + displayList(list));
                                                        System.out.println("");
                                                        step++;
                                                        success = true;
                                                        break;//if solution is found, break the iteration and no need to find next cute pokemon
                                                    }
                                                }
                                            }

                                        }
                                        if (success) {
                                            break;//if solution is found, break the iteration and no need to find next combination of charmander and bulbasaur placement
                                        }
                                    } else {
                                        /*If Jigglypuff is not in the list, check again Bulbasaur and Charmander are not next to each other and Machop is besides of the heaviest Pokemon or not
                                          if the 2 conditions are fulfilled, then the success becomes true
                                        */
                                        if (Math.abs(list.indexOf("Bulbasaur") - list.indexOf("Charmander")) != 1 && Math.abs(list.indexOf("Machop") - list.indexOf(heaviestPokemon)) == 1) {
                                            success = true;
                                            break;//if solution is found, break the iteration and no need to find next combination of charmander and bulbasaur placement
                                        }
                                    }
                                }
                            }
                        } else {
                            //If Bulbasaur and Charmander fulfilled the condition, then the list contains Jigglypuff or not
                            if (list.contains("Jigglypuff")) {
                                //Use for loop to check "cute" Pokemon
                                for (int j = 0; j < list.size(); j++) {
                                    if (library.pokemon_cute.get(list.get(j)) && !(list.get(j).equals("Jigglypuff"))) {
                                        //If the "cute" pokemon is found, then check whether the pokemon at the left of the "cute" pokemon exist or not
                                        if (j - 1 >= 0) {
                                            temp = list.get(j - 1);
                                            /*Since the condition of Jigglypuff is in the lowest priority, so need to make sure the pokemons at the left
                                              of the "cute" pokemon are not the pokemon with higher priority*/
                                            if (!(temp.equals("Eevee") || temp.equals("Pikachu") || temp.equals("Snorlax") || temp.equals("Machop") || temp.equals("Bulbasaur") || temp.equals("Charmander"))) {
                                                //Interchange the position of the pokemon at the left of the "cute" pokemon and the position of Jigglypuff
                                                int indexJigglypuff = list.indexOf("Jigglypuff");
                                                list.set(j - 1, "Jigglypuff");
                                                list.set(indexJigglypuff, temp);
                                                System.out.println("Step " + step + " :  Jigglypuff prefers to be surrounded by other \"cute\" Pokémon for morale purposes.");
                                                //display current sorted list
                                                System.out.println("Partial sort: " + displayList(list));
                                                System.out.println("");
                                                step++;
                                                success = true;
                                            }
                                        }
                                         /*If the pokemon which is at the left of the "cute" Pokemon is not exist and the solution is not found,
                                           then proceed to find the pokemon which is at the right of the "cute" Pokemon*/
                                        if (j + 1 <= list.size() - 1 && !success) {
                                            temp = list.get(j + 1);
                                            /*Since the condition of Jigglypuff is in the lowest priority, so need to make sure the pokemons at the right
                                              of the "cute" pokemon are not the pokemon with higher priority*/
                                            if (!(temp.equals("Eevee") || temp.equals("Pikachu") || temp.equals("Snorlax") || temp.equals("Machop") || temp.equals("Bulbasaur") || temp.equals("Charmander"))) {
                                                //Interchange the position of the pokemon at the right of the "cute" pokemon and the position of Jigglypuff
                                                int indexJigglypuff = list.indexOf("Jigglypuff");
                                                list.set(j + 1, "Jigglypuff");
                                                list.set(indexJigglypuff, temp);
                                                System.out.println("Step " + step + " :  Jigglypuff prefers to be surrounded by other \"cute\" Pokémon for morale purposes.");
                                                //display the current sorted list
                                                System.out.println("Partial sort: " + displayList(list));
                                                System.out.println("");
                                                step++;
                                                success = true;
                                            }
                                        }
                                    }
                                    if (success) {
                                        break; //if solution is found, break the iteration and no need to find next cute pokemon
                                    }
                                }
                            } else {
                                //If Jigglypuff is not in the list, then the variable success becomes true
                                success = true;
                            }
                        }
                    } else {
                        // If the list is not contains Bulbasaur and Charmander, then check Jigglypuff is in the list or not
                        if (list.contains("Jigglypuff")) {
                            //Use for loop to check "cute" Pokemon
                            for (int j = 0; j < list.size(); j++) {
                                if (library.pokemon_cute.get(list.get(j)) && !(list.get(j).equals("Jigglypuff"))) {
                                    //If the "cute" pokemon is found, then check whether the pokemon at the left of the "cute" pokemon exist or not
                                    if (j - 1 >= 0) {
                                        temp = list.get(j - 1);
                                        /*Since the condition of Jigglypuff is in the lowest priority, so need to make sure the pokemons at the left
                                          of the "cute" pokemon are not the pokemon with higher priority*/
                                        if (!(temp.equals("Eevee") || temp.equals("Pikachu") || temp.equals("Snorlax") || temp.equals("Machop"))) {
                                            //Interchange the position of the pokemon at the left of the "cute" pokemon and the position of Jigglypuff
                                            int indexJigglypuff = list.indexOf("Jigglypuff");
                                            list.set(j - 1, "Jigglypuff");
                                            list.set(indexJigglypuff, temp);
                                            System.out.println("Step " + step + " :  Jigglypuff prefers to be surrounded by other \"cute\" Pokémon for morale purposes.");
                                            //display the current sorted list
                                            System.out.println("Partial sort: " + displayList(list));
                                            System.out.println("");
                                            step++;
                                            success = true;
                                        }
                                    }
                                    /*If the pokemon which is at the left of the "cute" Pokemon is not exist and the solution is not found,
                                      then proceed to find the pokemon which is at the right of the "cute" Pokemon*/
                                    if (j + 1 <= list.size() - 1 && !success) {
                                        temp = list.get(j + 1);
                                        /*Since the condition of Jigglypuff is in the lowest priority, so need to make sure the pokemons at the right
                                          of the "cute" pokemon are not the pokemon with higher priority*/
                                        if (!(temp.equals("Eevee") || temp.equals("Pikachu") || temp.equals("Snorlax") || temp.equals("Machop"))) {
                                            //Interchange the position of the pokemon at the right of the "cute" pokemon and the position of Jigglypuff
                                            int indexJigglypuff = list.indexOf("Jigglypuff");
                                            list.set(j + 1, "Jigglypuff");
                                            list.set(indexJigglypuff, temp);
                                            System.out.println("Step " + step + " :  Jigglypuff prefers to be surrounded by other \"cute\" Pokémon for morale purposes.");
                                            //display the current sorted list
                                            System.out.println("Partial sort: " + displayList(list));
                                            System.out.println("");
                                            step++;
                                            success = true;
                                        }
                                    }
                                }
                                if (success) {
                                    break; //if solution is found, break the iteration and no need to find next cute pokemon
                                }
                            }
                        } else {
                            //If Jigglypuff is not in the list, then the variable success becomes true
                            success = true;
                        }
                    }
                }
            }
            /*if all cases where machop is placed at the left of the heaviest pokemon does not contain a solution or there is no slot
              at the left of the heaviest pokemon(when heaviest pokemon is eevee), check whether there is a slot at the right of the heaviest pokemon*/
            if (indexMax + 1 < list.size() && !success){
                reverse(initialList, list); //reset the list becomes the initial list
                String temp = list.get(indexMax - 1);
                /*Since the condition of Machop is in the second highest priority, so need to make sure the pokemons at the left
                  of the heaviest pokemon are not the pokemon with higher priority*/
                if (!(temp.equals("Eevee") || temp.equals("Pikachu") || temp.equals("Snorlax"))) {
                    //Interchange the position of the pokemon at the left of the heaviest pokemon and the position of Machop
                    int indexMachop = list.indexOf("Machop");
                    //Add a condition that the initial position of Machop is not at the right of the position of the heaviest Pokemon
                    if (indexMachop != indexMax + 1) {
                        System.out.println("The heaviest pokemon is: " + heaviestPokemon);
                        //Interchange the position of the pokemon at the right of the "cute" pokemon and the position of Jigglypuff
                        list.set(indexMax + 1, "Machop");
                        list.set(indexMachop, temp);
                        System.out.println("Step " + step + " : Machop demands to be placed next to the heaviest Pokemon in the lineup to show off its strength");
                        //display the current sorted list
                        System.out.println("Partial sort: " + displayList(list));
                        System.out.println("");
                        step++;
                    }

                    ArrayList<String> initialListMachop = list;

                    if (list.contains("Bulbasaur") && list.contains("Charmander")) {
                        int indexB = list.indexOf("Bulbasaur");
                        int indexC = list.indexOf("Charmander");
                        //Check whether Bulbasaur and Charmander are next to each other or not
                        if (Math.abs(indexB - indexC) == 1) {
                            //Use for loop to check the pokemon with lower priority
                            for (int i = 0; i < list.size(); i++) {
                                reverse(initialListMachop, list);//reset the list until after machop is swapped everytime loop starts
                                if (!(list.get(i).equals("Eevee") || list.get(i).equals("Pikachu") || list.get(i).equals("Snorlax") || i == indexB || i == indexC)) {
                                    //If the pokemon with lower priority is coming after Bulbasaur and Charmander, then the code execution is as below
                                    if (i > indexB && i > indexC) {
                                        if (indexB > indexC) {
                                            list.set(indexB, list.get(i));
                                            list.set(i, "Bulbasaur");
                                        } else {
                                            list.set(indexC, list.get(i));
                                            list.set(i, "Charmander");
                                        }
                                        System.out.println("Step " + step + " : Bulbasaur refuses to be placed next to Charmander, his fire burns too hot.");
                                        //display the current sorted list
                                        System.out.println("Partial sort: " + displayList(list));
                                        System.out.println("");
                                        step++;
                                    }
                                    //If the pokemon with lower priority is coming before Bulbasaur and Charmander, then the code execution is as below
                                    else if (i < indexB && i < indexC) {
                                        if (indexB < indexC) {
                                            list.set(indexB, list.get(i));
                                            list.set(i, "Bulbasaur");
                                        } else {
                                            list.set(indexC, list.get(i));
                                            list.set(i, "Charmander");
                                        }
                                        System.out.println("Step " + step + " : Bulbasaur refuses to be placed next to Charmander, his fire burns too hot.");
                                        //display the current sorted list
                                        System.out.println("Partial sort: " + displayList(list));
                                        System.out.println("");
                                        step++;
                                    }

                                    if (list.contains("Jigglypuff")) {
                                        //use for loop to check "cute" Pokemon
                                        for (int j = 0; j < list.size(); j++) {
                                            if (library.pokemon_cute.get(list.get(j)) && !(list.get(j).equals("Jigglypuff"))) {
                                                //If the "cute" pokemon is found, then check whether the pokemon at the left of the "cute" pokemon exist or not
                                                if (j - 1 >= 0) {
                                                    temp = list.get(j - 1);
                                                    /*Since the condition of Jigglypuff is in the lowest priority, so need to make sure the pokemons at the left
                                                      of the "cute" pokemon are not the pokemon with higher priority*/
                                                    if (!(temp.equals("Eevee") || temp.equals("Pikachu") || temp.equals("Snorlax") || temp.equals("Machop") || temp.equals("Bulbasaur") || temp.equals("Charmander"))) {
                                                        //Interchange the position of the pokemon at the left of the "cute" pokemon and the position of Jigglypuff
                                                        int indexJigglypuff = list.indexOf("Jigglypuff");
                                                        list.set(j - 1, "Jigglypuff");
                                                        list.set(indexJigglypuff, temp);
                                                        System.out.println("Step " + step + " :  Jigglypuff prefers to be surrounded by other \"cute\" Pokémon for morale purposes.");
                                                        //display the current sorted list
                                                        System.out.println("Partial sort: " + displayList(list));
                                                        System.out.println("");
                                                        step++;
                                                        success = true;
                                                        break;//if solution is found, break the iteration and no need to find next cute pokemon
                                                    }
                                                }
                                                /*If the pokemon which is at the left of the "cute" Pokemon is not exist and the solution is not found,
                                                  then proceed to find the pokemon which is at the right of the "cute" Pokemon*/
                                                if (j + 1 <= list.size() - 1 && !success) {
                                                    temp = list.get(j + 1);
                                                    /*Since the condition of Jigglypuff is in the lowest priority, so need to make sure the pokemons at the right
                                                      of the "cute" pokemon are not the pokemon with higher priority*/
                                                    if (!(temp.equals("Eevee") || temp.equals("Pikachu") || temp.equals("Snorlax") || temp.equals("Machop") || temp.equals("Bulbasaur") || temp.equals("Charmander"))) {
                                                        //Interchange the position of the pokemon at the right of the "cute" pokemon and the position of Jigglypuff
                                                        int indexJigglypuff = list.indexOf("Jigglypuff");
                                                        list.set(j + 1, "Jigglypuff");
                                                        list.set(indexJigglypuff, temp);
                                                        System.out.println("Step " + step + " :  Jigglypuff prefers to be surrounded by other \"cute\" Pokémon for morale purposes.");
                                                        //display the current sorted list
                                                        System.out.println("Partial sort: " + displayList(list));
                                                        System.out.println("");
                                                        step++;
                                                        success = true;
                                                        break;//if solution is found, break the iteration and no need to find next cute pokemon
                                                    }
                                                }
                                            }
                                        }
                                        if (success) {
                                            break;//if solution is found, break the iteration and no need to find next combination of charmander and bulbasaur placement
                                        }
                                    } else {
                                        if (Math.abs(list.indexOf("Bulbasaur") - list.indexOf("Charmander")) != 1 && Math.abs(list.indexOf("Machop") - list.indexOf(heaviestPokemon)) == 1) {
                                            success = true;
                                            break;//if solution is found, break the iteration and no need to find next combination of charmander and bulbasaur placement
                                        }
                                    }
                                }
                            }
                        } else {
                            if (list.contains("Jigglypuff")) {
                                //Use for loop to check "cute" Pokemon
                                for (int j = 0; j < list.size(); j++) {
                                    if (library.pokemon_cute.get(list.get(j)) && !(list.get(j).equals("Jigglypuff"))) {
                                        //If the "cute" pokemon is found, then check whether the pokemon at the left of the "cute" pokemon exist or not
                                        if (j - 1 >= 0) {
                                            temp = list.get(j - 1);
                                            /*Since the condition of Jigglypuff is in the lowest priority, so need to make sure the pokemons at the left
                                              of the "cute" pokemon are not the pokemon with higher priority*/
                                            if (!(temp.equals("Eevee") || temp.equals("Pikachu") || temp.equals("Snorlax") || temp.equals("Machop") || temp.equals("Bulbasaur") || temp.equals("Charmander"))) {
                                                //Interchange the position of the pokemon at the left of the "cute" pokemon and the position of Jigglypuff
                                                int indexJigglypuff = list.indexOf("Jigglypuff");
                                                list.set(j - 1, "Jigglypuff");
                                                list.set(indexJigglypuff, temp);
                                                System.out.println("Step " + step + " :  Jigglypuff prefers to be surrounded by other \"cute\" Pokémon for morale purposes.");
                                                //display the current sorted list
                                                System.out.println("Partial sort: " + displayList(list));
                                                System.out.println("");
                                                step++;
                                                success = true;
                                            }
                                        }
                                        /*If the pokemon which is at the left of the "cute" Pokemon is not exist and the solution is not found,
                                          then proceed to find the pokemon which is at the right of the "cute" Pokemon*/
                                        if (j + 1 <= list.size() - 1 && !success) {
                                            temp = list.get(j + 1);
                                            /*Since the condition of Jigglypuff is in the lowest priority, so need to make sure the pokemons at the right
                                              of the "cute" pokemon are not the pokemon with higher priority*/
                                            if (!(temp.equals("Eevee") || temp.equals("Pikachu") || temp.equals("Snorlax") || temp.equals("Machop") || temp.equals("Bulbasaur") || temp.equals("Charmander"))) {
                                                //Interchange the position of the pokemon at the right of the "cute" pokemon and the position of Jigglypuff
                                                int indexJigglypuff = list.indexOf("Jigglypuff");
                                                list.set(j + 1, "Jigglypuff");
                                                list.set(indexJigglypuff, temp);
                                                System.out.println("Step " + step + " :  Jigglypuff prefers to be surrounded by other \"cute\" Pokémon for morale purposes.");
                                                //display the current sorted list
                                                System.out.println("Partial sort: " + displayList(list));
                                                System.out.println("");
                                                step++;
                                                success = true;
                                            }
                                        }
                                    }
                                    if (success) {
                                        break; //if solution is found, break the iteration and no need to find next cute pokemon
                                    }
                                }
                            } else {
                                //If Jigglypuff is not in the list, then the variable success becomes true
                                success = true;
                            }
                        }
                    } else {
                        if (list.contains("Jigglypuff")) {
                            //Use for loop to check "cute" Pokemon
                            for (int j = 0; j < list.size(); j++) {
                                if (library.pokemon_cute.get(list.get(j)) && !(list.get(j).equals("Jigglypuff"))) {
                                    //If the "cute" pokemon is found, then check whether the pokemon at the left of the "cute" pokemon exist or not
                                    if (j - 1 >= 0) {
                                        temp = list.get(j - 1);
                                        /*Since the condition of Jigglypuff is in the lowest priority, so need to make sure the pokemons at the left
                                    of the "cute" pokemon are not the pokemon with higher priority*/
                                        if (!(temp.equals("Eevee") || temp.equals("Pikachu") || temp.equals("Snorlax") || temp.equals("Machop"))) {
                                            //Interchange the position of the pokemon at the left of the "cute" pokemon and the position of Jigglypuff
                                            int indexJigglypuff = list.indexOf("Jigglypuff");
                                            list.set(j - 1, "Jigglypuff");
                                            list.set(indexJigglypuff, temp);
                                            System.out.println("Step " + step + " :  Jigglypuff prefers to be surrounded by other \"cute\" Pokémon for morale purposes.");
                                            //display the current sorted list
                                            System.out.println("Partial sort: " + displayList(list));
                                            System.out.println("");
                                            step++;
                                            success = true;
                                        }
                                    }
                                    /*If the pokemon which is at the left of the "cute" Pokemon is not exist and the solution is not found,
                                      then proceed to find the pokemon which is at the right of the "cute" Pokemon*/
                                    if (j + 1 <= list.size() - 1 && !success) {
                                        temp = list.get(j + 1);
                                        /*Since the condition of Jigglypuff is in the lowest priority, so need to make sure the pokemons at the right
                                          of the "cute" pokemon are not the pokemon with higher priority*/
                                        if (!(temp.equals("Eevee") || temp.equals("Pikachu") || temp.equals("Snorlax") || temp.equals("Machop"))) {
                                            //Interchange the position of the pokemon at the right of the "cute" pokemon and the position of Jigglypuff
                                            int indexJigglypuff = list.indexOf("Jigglypuff");
                                            list.set(j + 1, "Jigglypuff");
                                            list.set(indexJigglypuff, temp);
                                            System.out.println("Step " + step + " :  Jigglypuff prefers to be surrounded by other \"cute\" Pokémon for morale purposes.");
                                            //display the current sorted list
                                            System.out.println("Partial sort: " + displayList(list));
                                            System.out.println("");
                                            step++;
                                            success = true;
                                        }
                                    }
                                }
                                if (success) {
                                    break; //if solution is found, break the iteration and no need to find next cute pokemon
                                }
                            }

                        } else {
                            //If Jigglypuff is not in the list, then the variable success becomes true
                            success = true;
                        }
                    }
                }
            }
        } else {
            //If Machop is not in the list, then check Bulbasaur and Charmander is in the same list or not
            if (list.contains("Bulbasaur") && list.contains("Charmander")) {
                int indexB = list.indexOf("Bulbasaur");
                int indexC = list.indexOf("Charmander");
                String temp = "";
                //Check whether Bulbasaur and Charmander are next to each other or not
                if (Math.abs(indexB - indexC) == 1) {
                    //Use for loop to check the pokemon with lower priority
                    for (int i = 0; i < list.size(); i++) {
                        if (!(list.get(i).equals("Eevee") || list.get(i).equals("Pikachu") || list.get(i).equals("Snorlax") || i == indexB || i == indexC)) {
                            //If the pokemon with lower priority is coming after Bulbasaur and Charmander, then the code execution is as below
                            if (i > indexB && i > indexC) {
                                if (indexB > indexC) {
                                    list.set(indexB, list.get(i));
                                    list.set(i, "Bulbasaur");
                                } else {
                                    list.set(indexC, list.get(i));
                                    list.set(i, "Charmander");
                                }
                                System.out.println("Step " + step + " : Bulbasaur refuses to be placed next to Charmander, his fire burns too hot.");
                                //display the current sorted list
                                System.out.println("Partial sort: " + displayList(list));
                                System.out.println("");
                            } //If the pokemon with lower priority is coming before Bulbasaur and Charmander, then the code execution is as below
                            else if (i < indexB && i < indexC) {
                                if (indexB < indexC) {
                                    list.set(indexB, list.get(i));
                                    list.set(i, "Bulbasaur");
                                } else {
                                    list.set(indexC, list.get(i));
                                    list.set(i, "Charmander");
                                }
                                System.out.println("Step " + step + " : Bulbasaur refuses to be placed next to Charmander, his fire burns too hot.");
                                //display the current sorted list
                                System.out.println("Partial sort: " + displayList(list));
                                System.out.println("");
                            }
                        }
                        if (list.contains("Jigglypuff")) {
                            //Use for loop to check "cute" Pokemon
                            for (int j = 0; j < list.size(); j++) {
                                if (library.pokemon_cute.get(list.get(j)) && !(list.get(j).equals("Jigglypuff"))) {
                                    //If the "cute" pokemon is found, then check whether the pokemon at the left of the "cute" pokemon exist or not
                                    if (j - 1 >= 0) {
                                        temp = list.get(j - 1);
                                        /*Since the condition of Jigglypuff is in the lowest priority, so need to make sure the pokemons at the left
                                      of the "cute" pokemon are not the pokemon with higher priority*/
                                        if (!(temp.equals("Eevee") || temp.equals("Pikachu") || temp.equals("Snorlax") || temp.equals("Bulbasaur") || temp.equals("Charmander"))) {
                                            //Interchange the position of the pokemon at the left of the "cute" pokemon and the position of Jigglypuff
                                            int indexJigglypuff = list.indexOf("Jigglypuff");
                                            list.set(j - 1, "Jigglypuff");
                                            list.set(indexJigglypuff, temp);
                                            System.out.println("Step " + step + " :  Jigglypuff prefers to be surrounded by other \"cute\" Pokémon for morale purposes.");
                                            //display the current sorted list
                                            System.out.println("Partial sort: " + displayList(list));
                                            System.out.println("");
                                            step++;
                                            success = true;
                                        }
                                    }
                                    /*If the pokemon which is at the left of the "cute" Pokemon is not exist and the solution is not found,
                                      then proceed to find the pokemon which is at the right of the "cute" Pokemon*/
                                    if (j + 1 <= list.size() - 1 && !success) {
                                        temp = list.get(j + 1);
                                        /*Since the condition of Jigglypuff is in the lowest priority, so need to make sure the pokemons at the right
                                          of the "cute" pokemon are not the pokemon with higher priority*/
                                        if (!(temp.equals("Eevee") || temp.equals("Pikachu") || temp.equals("Snorlax") || temp.equals("Bulbasaur") || temp.equals("Charmander"))) {
                                            //Interchange the position of the pokemon at the right of the "cute" pokemon and the position of Jigglypuff
                                            int indexJigglypuff = list.indexOf("Jigglypuff");
                                            list.set(j + 1, "Jigglypuff");
                                            list.set(indexJigglypuff, temp);
                                            System.out.println("Step " + step + " :  Jigglypuff prefers to be surrounded by other \"cute\" Pokémon for morale purposes.");
                                            //display the current sorted list
                                            System.out.println("Partial sort: " + displayList(list));
                                            System.out.println("");
                                            step++;
                                            success = true;
                                        }
                                    }
                                }
                                if (success) {
                                    break; //if solution is found, break the iteration and no need to find next cute pokemon
                                }
                            }
                            if (success) { //if solution is found, break the iteration and no need to find next combination of charmander and bulbasaur placement
                                break;
                            }
                        } else {
                            //If Jigglypuff is not in the list and Bulbasaur and Charmander are not next to each other, then the solution is found
                            if (Math.abs(indexB - indexC) != 1) {
                                success = true;
                            }
                        }
                    }
                } else {
                    //If Bulbasaur and Charmander are not next to each other, then check Jigglypuff is in the list or not
                    if (list.contains("Jigglypuff")) {
                        //Use for loop to check "cute" Pokemon
                        for (int j = 0; j < list.size(); j++) {
                            if (library.pokemon_cute.get(list.get(j)) && !(list.get(j).equals("Jigglypuff"))) {
                                //If the "cute" pokemon is found, then check whether the pokemon at the left of the "cute" pokemon exist or not
                                if (j - 1 >= 0) {
                                    temp = list.get(j - 1);
                                    /*Since the condition of Jigglypuff is in the lowest priority, so need to make sure the pokemons at the left
                                    of the "cute" pokemon are not the pokemon with higher priority*/
                                    if (!(temp.equals("Eevee") || temp.equals("Pikachu") || temp.equals("Snorlax") || temp.equals("Bulbasaur") || temp.equals("Charmander"))) {
                                        //Interchange the position of the pokemon at the left of the "cute" pokemon and the position of Jigglypuff
                                        int indexJigglypuff = list.indexOf("Jigglypuff");
                                        list.set(j - 1, "Jigglypuff");
                                        list.set(indexJigglypuff, temp);
                                        System.out.println("Step " + step + " :  Jigglypuff prefers to be surrounded by other \"cute\" Pokémon for morale purposes.");
                                        //display the current sorted list
                                        System.out.println("Partial sort: " + displayList(list));
                                        System.out.println("");
                                        step++;
                                        success = true;
                                    }
                                }
                                 /*If the pokemon which is at the left of the "cute" Pokemon is not exist and the solution is not found,
                                   then proceed to find the pokemon which is at the right of the "cute" Pokemon*/
                                if (j + 1 <= list.size() - 1 && !success) {
                                    temp = list.get(j + 1);
                                    /*Since the condition of Jigglypuff is in the lowest priority, so need to make sure the pokemons at the right
                                    of the "cute" pokemon are not the pokemon with higher priority*/
                                    if (!(temp.equals("Eevee") || temp.equals("Pikachu") || temp.equals("Snorlax") || temp.equals("Bulbasaur") || temp.equals("Charmander"))) {
                                        //Interchange the position of the pokemon at the right of the "cute" pokemon and the position of Jigglypuff
                                        int indexJigglypuff = list.indexOf("Jigglypuff");
                                        list.set(j + 1, "Jigglypuff");
                                        list.set(indexJigglypuff, temp);
                                        System.out.println("Step " + step + " :  Jigglypuff prefers to be surrounded by other \"cute\" Pokémon for morale purposes.");
                                        //display the current sorted list
                                        System.out.println("Partial sort: " + displayList(list));
                                        System.out.println("");
                                        step++;
                                        success = true;
                                    }
                                }
                            }
                            if (success) {
                                break; //if solution is found, break the iteration and no need to find next cute pokemon
                            }
                        }
                    } else {
                        //If Jigglypuff is not in the list, then the variable success becomes true
                        success = true;
                    }
                }
            } else {
                // If the list is not contains Bulbasaur and Charmander, then check Jigglypuff is in the list or not
                if (list.contains("Jigglypuff")) {
                    String temp = "";
                    //Use for loop to check "cute" Pokemon
                    for (int j = 0; j < list.size(); j++) {
                        if (library.pokemon_cute.get(list.get(j)) && !(list.get(j).equals("Jigglypuff"))) {
                            //If the "cute" pokemon is found, then check whether the pokemon at the left of the "cute" pokemon exist or not
                            if (j - 1 >= 0) {
                                temp = list.get(j - 1);
                                /*Since the condition of Jigglypuff is in the lowest priority, so need to make sure the pokemons at the left
                                    of the "cute" pokemon are not the pokemon with higher priority*/
                                if (!(temp.equals("Eevee") || temp.equals("Pikachu") || temp.equals("Snorlax"))) {
                                    //Interchange the position of the pokemon at the left of the "cute" pokemon and the position of Jigglypuff
                                    int indexJigglypuff = list.indexOf("Jigglypuff");
                                    list.set(j - 1, "Jigglypuff");
                                    list.set(indexJigglypuff, temp);
                                    System.out.println("Step " + step + " :  Jigglypuff prefers to be surrounded by other \"cute\" Pokémon for morale purposes.");
                                    //display the current sorted list
                                    System.out.println("Partial sort: " + displayList(list));
                                    System.out.println("");
                                    step++;
                                    success = true;
                                }
                            }
                             /*If the pokemon which is at the left of the "cute" Pokemon is not exist and the solution is not found,
                                then proceed to find the pokemon which is at the right of the "cute" Pokemon*/
                            if (j + 1 <= list.size() - 1 && !success) {
                                temp = list.get(j + 1);
                                /*Since the condition of Jigglypuff is in the lowest priority, so need to make sure the pokemons at the right
                                    of the "cute" pokemon are not the pokemon with higher priority*/
                                if (!(temp.equals("Eevee") || temp.equals("Pikachu") || temp.equals("Snorlax"))) {
                                    //Interchange the position of the pokemon at the right of the "cute" pokemon and the position of Jigglypuff
                                    int indexJigglypuff = list.indexOf("Jigglypuff");
                                    list.set(j + 1, "Jigglypuff");
                                    list.set(indexJigglypuff, temp);
                                    System.out.println("Step " + step + " :  Jigglypuff prefers to be surrounded by other \"cute\" Pokémon for morale purposes.");
                                    //display the current sorted list
                                    System.out.println("Partial sort: " + displayList(list));
                                    System.out.println("");
                                    step++;
                                    success = true;
                                }
                            }
                        }
                        if (success) {
                            break; //if solution is found, break the iteration and no need to find next cute pokemon
                        }
                    }

                } else {
                    //If Jigglypuff is not in the list, then the variable success becomes true
                    success = true;
                }
            }
        }
        //Display the final sorted list
        if (success) {
            System.out.printf("+%s+\n", "-".repeat(90));
            System.out.println("Final Sorted List: " + displayList(list));
            System.out.printf("+%s+\n", "-".repeat(90));
            System.out.println("Your Pokemon are now sorted! Enjoy your adventure in the Safari Zone!");
        } else {
            System.out.println("This combination of pokemons cannot be sorted!");
        }

    }

    // Reset the current list becomes the initial list again
    public static void reverse(ArrayList<String> initialList, ArrayList<String> list) {
        list = initialList;
    }

    /*This method is used to check the input entered is following the format or not, also check whether all of the pokemon
      entered are existed or not and also check the same pokemon is entered more than one or not*/
    public static boolean CheckInput(ArrayList<String> list) {
        Set<String> pokemonSet = new HashSet<>();
        boolean tf = true;
        for (int i = 0; i < list.size(); i++) {
            String currentPokemon = list.get(i);
            // Check for duplicate Pokémon
            if (pokemonSet.contains(currentPokemon)) {
                System.out.println("The same pokemon cannot be entered twice.");
                tf = false; // Duplicate found, return false
            } else {
                pokemonSet.add(currentPokemon); // Add to set if not already present
            }
            if (!library.pokemonhp.containsKey(currentPokemon)) {
                System.out.println("You entered a pokemon that is not available in the game. ");
                tf = false;
            }
        }
        return tf;
    }

    //This method is used to display the current lineup of the pokemon
    public static String displayList(ArrayList<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size() - 1; i++) {
            sb.append(list.get(i)).append(", ");
        }
        sb.append(list.get(list.size() - 1));
        return sb.toString();
    }
}