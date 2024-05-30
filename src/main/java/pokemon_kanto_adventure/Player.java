package pokemon_kanto_adventure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Player {
    //System.out.printf("+%s+\n", "-".repeat(90)); is the code for displaying a separating line
    private String name; //name of the player
    private String[] badges = new String[8]; //badges of the player
    private int numofbadge; //numberofbadge of the player
    private Pokemon pokemon1; //pokemon1-6 of the player
    private Pokemon pokemon2;
    private Pokemon pokemon3;
    private Pokemon pokemon4;
    private Pokemon pokemon5;
    private Pokemon pokemon6;
    private int money; //money of the player
    private HashMap<String, Integer> items;//items of the player, contained in a HashMap with name of items as keys and number of items as values
    private int rivalracewins; //number of rivalrace won
    private int battlewon; //number of battle won
    private ArrayList<Pokemon> PC; //pokemons in PC
    private String currentCity;//current location of player

    public Player(String n) { //create a new player with name
        name = n; //set name to n
        for (int i = 0; i < 8; i++) { //set all badges to ---
            badges[i] = "---";
        }
        pokemon1 = null;//set all pokemon in team to nothing
        pokemon2 = null;
        pokemon3 = null;
        pokemon4 = null;
        pokemon5 = null;
        pokemon6 = null;
        items = new HashMap<>(); //set up items HashMap with keys, and all values to 0
        items.put("Poke Ball", 0);
        items.put("Great Ball", 0);
        items.put("Ultra Ball", 0);
        items.put("Potion", 0);
        items.put("Super Potion", 0);
        items.put("Hyper Potion", 0);
        items.put("Max Potion", 0);
        items.put("X Attack", 0);
        items.put("X Defend", 0);
        items.put("X Speed", 0);
        items.put("Revive", 0);
        numofbadge = 0;//set number of badges to 0
        PC = new ArrayList<Pokemon>();//initialize PC with an empty ArrayList of Pokemons
        currentCity = "Pallet Town";//set currentCity to Pallet Town
    }

    public void addPokemon(Pokemon a) { //add a pokemon
        if (pokemon1 == null) { //if pokemon1 is empty, set pokemon1 to that pokemon
            pokemon1 = a;
        } else if (pokemon2 == null) {//if pokemon1 not empty but pokemon2 is empty, set pokemon2 to that pokemon
            pokemon2 = a;
        } else if (pokemon3 == null) {//if pokemon2 not empty but pokemon3 is empty, set pokemon3 to that pokemon
            pokemon3 = a;
        } else if (pokemon4 == null) {//if pokemon3 not empty but pokemon4 is empty, set pokemon4 to that pokemon
            pokemon4 = a;
        } else if (pokemon5 == null) {//if pokemon4 not empty but pokemon5 is empty, set pokemon5 to that pokemon
            pokemon5 = a;
        } else if (pokemon6 == null) {//if pokemon5 not empty but pokemon6 is empty, set pokemon6 to that pokemon
            pokemon6 = a;
        } else {//if player team is full, display message below and add the pokemon to PC
            System.out.println("Team is full, " + a.findname() + " is moved to PC");
            PC.add(a);
        }
    }

    public int findMoney() {//return money
        return money;
    }

    public void deductMoney(int ded) {//deduct money
        money -= ded;
    }

    public void addMoney(int add) {//add money
        money += add;
    }

    public Pokemon findPoke1() {//return pokemon1
        return pokemon1;
    }

    public Pokemon findPoke2() {//return pokemon2
        return pokemon2;
    }

    public Pokemon findPoke3() {//return pokemon3
        return pokemon3;
    }

    public Pokemon findPoke4() {//return pokemon4
        return pokemon4;
    }

    public Pokemon findPoke5() {//return pokemon5
        return pokemon5;
    }

    public Pokemon findPoke6() {//return pokemon6
        return pokemon6;
    }

    public String findCurrentCity() {//return currentCity
        return currentCity;
    }

    public void setcurrentcity(String city) {//set currentCity
        currentCity = city;
    }

    public void movetoCity(String city) {//move to new city
        System.out.printf("+%s+\n", "-".repeat(90)); //display moving to the city message
        System.out.println("Moving to " + city + "......");
        currentCity = city;//set currentCity to new city
    }

    public void obtainbadge(String badge) {//obtain a new badge
        if (badge.equals("Boulder Badge")) {//if a badge is obtained, put them to their corresponding locations
            badges[0] = badge;
        } else if (badge.equals("Cascade Badge")) {
            badges[1] = badge;
        } else if (badge.equals("Thunder Badge")) {
            badges[2] = badge;
        } else if (badge.equals("Rainbow Badge")) {
            badges[3] = badge;
        } else if (badge.equals("Soul Badge")) {
            badges[4] = badge;
        } else if (badge.equals("Marsh Badge")) {
            badges[5] = badge;
        } else if (badge.equals("Volcano Badge")) {
            badges[6] = badge;
        } else if (badge.equals("Earth Badge")) {
            badges[7] = badge;
        }
        numofbadge++;//increase number of badges by 1
        if (numofbadge == 8) {//if number of badges is 8, display win message
            System.out.println("Congrats!! you have finished the game and won against all the gym leaders, you are now the new Champion of the Kanto Region!!");
        }
    }

    public String[] getbadges() { //return badges
        return badges;
    }

    public void showbadges() { //show player badges
        System.out.println("Badges: ");
        for (int i = 0; i < badges.length; i++) {
            System.out.println(badges[i] + " ");
        }
    }

    public void startrivalrace() { //start a rival race
        RivalRace race = new RivalRace(); //create a new RivalRace() object
        race.simulation(); //find the best path

        while (!currentCity.equals(race.getDestination())) { //while player have not arrive at the destination of the race
            ArrayList<String> neighboringCities = library.kantoMap.getNeighbours(currentCity);//get the list of neighboring cities of the current city of the player
            System.out.printf("+%s+\n", "-".repeat(90));//show the current city player is at
            System.out.println("You are now at: " + currentCity);
            System.out.println("Move to the next correct location: ");//print all the choices to go to
            for (int i = 0; i < neighboringCities.size(); i++) {
                System.out.println((i + 1) + ". " + neighboringCities.get(i));
            }
            Scanner sc = new Scanner(System.in);
            System.out.print("Select a location: ");//prompt player to enter the choice
            String selection = sc.nextLine();//receive choice
            if (isNum(selection)) {//check choice format
                int choice = Integer.parseInt(selection) - 1;//turn choice to integer
                if (choice < neighboringCities.size()) {
                    System.out.println("You selected " + neighboringCities.get(choice));//display message of player's choice
                    String newCity = neighboringCities.get(choice); //set new city to the city in the list corresponding to that choice
                    movetoCity(newCity);//move to the new city
                    if (!currentCity.equals(race.getStack().pop())) { //if the new city is not the next destination of the shortest path
                        System.out.printf("+%s+\n", "-".repeat(90));//player loses the rival race because they did not chose the correct location for the shortest path
                        System.out.println("Oops, you went the wrong way! You lost this race, better luck next time!");//display lose message
                        break;//end the loop
                    }
                } else {
                    System.out.printf("+%s+\n", "-".repeat(90));
                    System.out.println("Invalid choice! Please choose again.");
                }
            } else {
                System.out.printf("+%s+\n", "-".repeat(90));
                System.out.println("Invalid choice! Please choose again.");
            }
        }
        if (currentCity.equals(race.getDestination())) {//when loop ends, if player is at the destination of the rival race, player wins the rival race
            System.out.printf("+%s+\n", "-".repeat(90));//display win message
            System.out.println("Congratulations, you have reach the finish line and won the race! You got $1000 for winning!");
            addMoney(1000);//obtain money
            rivalracewins++;//increase rival race wins by 1
        }
    }

    public void obtainitems(String n, int i) { //obtain a number of items
        int old = items.get(n);
        int neww = old + i;
        items.replace(n, old, neww);
    }

    public void deditems(String n, int i) { //deduct a number of items
        int old = items.get(n);
        int neww = old - i;
        items.replace(n, old, neww);
    }

    public HashMap<String, Integer> getItems() { //get the HashMap of items and their numbers
        return items;
    }

    public void showitems() { //show all the items and their numbers
        System.out.println("1. Poke Ball: " + items.get("Poke Ball"));
        System.out.println("2. Great Ball: " + items.get("Great Ball"));
        System.out.println("3. Ultra Ball: " + items.get("Ultra Ball"));
        System.out.println("4. Potion: " + items.get("Potion"));
        System.out.println("5. Super Potion: " + items.get("Super Potion"));
        System.out.println("6. Hyper Potion: " + items.get("Hyper Potion"));
        System.out.println("7. Max Potion: " + items.get("Max Potion"));
        System.out.println("8. X Attack: " + items.get("X Attack"));
        System.out.println("9. X Defend: " + items.get("X Defend"));
        System.out.println("10. X Speed: " + items.get("X Speed"));
        System.out.println("11. Revive: " + items.get("Revive"));
    }

    public void setPoke1(Pokemon poke) {//set pokemon1 to poke
        pokemon1 = poke;
    }

    public void setPoke2(Pokemon poke) {//set pokemon2 to poke
        pokemon2 = poke;
    }

    public void setPoke3(Pokemon poke) {//set pokemon3 to poke
        pokemon3 = poke;
    }

    public void setPoke4(Pokemon poke) {//set pokemon4 to poke
        pokemon4 = poke;
    }

    public void setPoke5(Pokemon poke) {//set pokemon5 to poke
        pokemon5 = poke;
    }

    public void setPoke6(Pokemon poke) {//set pokemon6 to poke
        pokemon6 = poke;
    }

    public int getrivalwins() {//return rivalracewins
        return rivalracewins;
    }

    public int getvictories() {//return battlewon
        return battlewon;
    }

    public ArrayList<Pokemon> getPC() {//return PC
        return PC;
    }

    public void wonbattle() {//player wins a trainer battle
        battlewon++;
    }

    public void showPC() {//show all pokemons in PC
        for (int i = 0; i < PC.size(); i++) {
            System.out.println(PC.get(i).findname());
        }
    }

    public void showteam() {//show all pokemons in the team
        System.out.printf("+%s+\n", "-".repeat(90));
        System.out.println("+--------------------Pokemons--------------------+");
        if (pokemon1 != null)
            System.out.println("1. " + pokemon1.findname() + " Lvl: " + pokemon1.findlvl() + " HP: " + pokemon1.findcurrenthp() + " / " + pokemon1.findmaxhp());
        else
            System.out.printf("+%s+\n", "-".repeat(90));
        if (pokemon2 != null)
            System.out.println("2. " + pokemon2.findname() + " Lvl: " + pokemon2.findlvl() + " HP: " + pokemon2.findcurrenthp() + " / " + pokemon2.findmaxhp());
        else
            System.out.printf("+%s+\n", "-".repeat(90));
        if (pokemon3 != null)
            System.out.println("3. " + pokemon3.findname() + " Lvl: " + pokemon3.findlvl() + " HP: " + pokemon3.findcurrenthp() + " / " + pokemon3.findmaxhp());
        else
            System.out.printf("+%s+\n", "-".repeat(90));
        if (pokemon4 != null)
            System.out.println("4. " + pokemon4.findname() + " Lvl: " + pokemon4.findlvl() + " HP: " + pokemon4.findcurrenthp() + " / " + pokemon4.findmaxhp());
        else
            System.out.printf("+%s+\n", "-".repeat(90));
        if (pokemon5 != null)
            System.out.println("5. " + pokemon5.findname() + " Lvl: " + pokemon5.findlvl() + " HP: " + pokemon5.findcurrenthp() + " / " + pokemon5.findmaxhp());
        else
            System.out.printf("+%s+\n", "-".repeat(90));
        if (pokemon6 != null)
            System.out.println("6. " + pokemon6.findname() + " Lvl: " + pokemon6.findlvl() + " HP: " + pokemon6.findcurrenthp() + " / " + pokemon6.findmaxhp());
        else
            System.out.printf("+%s+\n", "-".repeat(90));
        System.out.println("+--------------------End of pokemon list--------------------+");
    }

    public String getName() {//return name
        return name;
    }

    public void setMoney(int money) { //set money
        this.money = money;
    }

    public void showprofile() { //show player profile
        System.out.printf("+%s+\n", "-".repeat(90));
        System.out.println("+-----------------Player Profile-----------------+");
        System.out.println("Player Name: " + name);
        System.out.println("Money: $ " + money);
        showbadges();
        showteam();
        System.out.println("Rival race wins: " + getrivalwins());
        System.out.println("Trainer battles won: " + battlewon);
        System.out.println("+------------------End of Player Profile------------------+");
    }

    public boolean teamfaint() { //check if the whole team is fainted
        if (pokemon1 != null) { // check if pokemon1 is nothing, if is nothing then player does not have pokemon, return false, else player has at least one pokemon
            if (pokemon1.isFaint()) { //check if the first pokemon is fainted
                if (pokemon2 != null) { //if first pokemon is fainted, check if player have second pokemon, if no, means player only have one pokemon and it is fainted, hence all pokemons are fainted and return true
                    if (pokemon2.isFaint()) { //check if the 2nd pokemon is fainted
                        if (pokemon3 != null) { //if 2nd pokemon is fainted, check if player have 3rd pokemon, if no, means player only have 2 pokemons, hence all pokemons are fainted and return true
                            if (pokemon3.isFaint()) { //check if 3rd pokemon is fainted
                                if (pokemon4 != null) { //if 3rd pokemon is fainted, check if player have 4th pokemon, if no, means player only have 3 pokemons, hence all pokemons are fainted and return true
                                    if (pokemon4.isFaint()) { //check if 4th pokemon is fainted
                                        if (pokemon5 != null) { //if 4th pokemon is fainted, check if player have 5th pokemon, if no, means player only have 4 pokemons, hence all pokemons are fainted and return true
                                            if (pokemon5.isFaint()) { //check if 5th pokemon is fainted
                                                if (pokemon6 != null) { //if 5th pokemon is fainted, check if player have 6th pokemon, if no, means player only have 5 pokemons, hence all pokemons are fainted and return true
                                                    if (pokemon6.isFaint()) { //check if 6th pokemon is fainted
                                                        if (currentCity.equals("Pallet Town")) { //if yes, if player is at Pallet Town
                                                            System.out.println("Uh Oh, all your pokemons have fainted, you whited out and is sent to Mom"); //player will be sent to Mum to heal up and no penalty
                                                            allhealup();
                                                            System.out.println("Mom: Looks like you really had a harsh battle, but don't give up, keep on working hard, good luck!");
                                                        } else { //if player is not at Pallet Town, player loses 200 money as penalty and is sent to Pokemon Center to heal up
                                                            int lostmoney = 0;
                                                            if (money < 200) {
                                                                lostmoney = money;
                                                                money = 0;
                                                            } else {
                                                                lostmoney = 200;
                                                                money -= 200;
                                                            }
                                                            System.out.println("Uh Oh, all your pokemons have fainted, you whited out and was sent to the Pokemon Center, you lost $ " + lostmoney);
                                                            allhealup();
                                                            System.out.println("Nurse: Now your pokemons are all healed up, have a nice day!");
                                                        }
                                                        return true; //if true, then all pokemons in the team is fainted and return true;
                                                    }
                                                } else {
                                                    if (currentCity.equals("Pallet Town")) {//if player is at Pallet Town
                                                        System.out.println("Uh Oh, all your pokemons have fainted, you whited out and is sent to Mom");//player will be sent to Mum to heal up and no penalty
                                                        allhealup();
                                                        System.out.println("Mom: Looks like you really had a harsh battle, but don't give up, keep on working hard, good luck!");
                                                    } else {//if player is not at Pallet Town, player loses 200 money as penalty and is sent to Pokemon Center to heal up
                                                        int lostmoney = 0;
                                                        if (money < 200) {
                                                            lostmoney = money;
                                                            money = 0;
                                                        } else {
                                                            lostmoney = 200;
                                                            money -= 200;
                                                        }
                                                        System.out.println("Uh Oh, all your pokemons have fainted, you whited out and was sent to the Pokemon Center, you lost $ " + lostmoney);
                                                        allhealup();
                                                        System.out.println("Nurse: Now your pokemons are all healed up, have a nice day!");
                                                    }
                                                    return true;
                                                }
                                            }
                                        } else {
                                            if (currentCity.equals("Pallet Town")) {//if player is at Pallet Town
                                                System.out.println("Uh Oh, all your pokemons have fainted, you whited out and is sent to Mom");//player will be sent to Mum to heal up and no penalty
                                                allhealup();
                                                System.out.println("Mom: Looks like you really had a harsh battle, but don't give up, keep on working hard, good luck!");
                                            } else {//if player is not at Pallet Town, player loses 200 money as penalty and is sent to Pokemon Center to heal up
                                                int lostmoney = 0;
                                                if (money < 200) {
                                                    lostmoney = money;
                                                    money = 0;
                                                } else {
                                                    lostmoney = 200;
                                                    money -= 200;
                                                }
                                                System.out.println("Uh Oh, all your pokemons have fainted, you whited out and was sent to the Pokemon Center, you lost $ " + lostmoney);
                                                allhealup();
                                                System.out.println("Nurse: Now your pokemons are all healed up, have a nice day!");
                                            }
                                            return true;
                                        }
                                    }
                                } else {
                                    if (currentCity.equals("Pallet Town")) {//if player is at Pallet Town
                                        System.out.println("Uh Oh, all your pokemons have fainted, you whited out and is sent to Mom");//player will be sent to Mum to heal up and no penalty
                                        allhealup();
                                        System.out.println("Mom: Looks like you really had a harsh battle, but don't give up, keep on working hard, good luck!");
                                    } else {//if player is not at Pallet Town, player loses 200 money as penalty and is sent to Pokemon Center to heal up
                                        int lostmoney = 0;
                                        if (money < 200) {
                                            lostmoney = money;
                                            money = 0;
                                        } else {
                                            lostmoney = 200;
                                            money -= 200;
                                        }
                                        System.out.println("Uh Oh, all your pokemons have fainted, you whited out and was sent to the Pokemon Center, you lost $ " + lostmoney);
                                        allhealup();
                                        System.out.println("Nurse: Now your pokemons are all healed up, have a nice day!");
                                    }
                                    return true;
                                }
                            }
                        } else {
                            if (currentCity.equals("Pallet Town")) {//if player is at Pallet Town
                                System.out.println("Uh Oh, all your pokemons have fainted, you whited out and is sent to Mom");//player will be sent to Mum to heal up and no penalty
                                allhealup();
                                System.out.println("Mom: Looks like you really had a harsh battle, but don't give up, keep on working hard, good luck!");
                            } else {//if player is not at Pallet Town, player loses 200 money as penalty and is sent to Pokemon Center to heal up
                                int lostmoney = 0;
                                if (money < 200) {
                                    lostmoney = money;
                                    money = 0;
                                } else {
                                    lostmoney = 200;
                                    money -= 200;
                                }
                                System.out.println("Uh Oh, all your pokemons have fainted, you whited out and was sent to the Pokemon Center, you lost $ " + lostmoney);
                                allhealup();
                                System.out.println("Nurse: Now your pokemons are all healed up, have a nice day!");
                            }
                            return true;
                        }
                    }
                } else {
                    if (currentCity.equals("Pallet Town")) {//if player is at Pallet Town
                        System.out.println("Uh Oh, all your pokemons have fainted, you whited out and is sent to Mom");//player will be sent to Mum to heal up and no penalty
                        allhealup();
                        System.out.println("Mom: Looks like you really had a harsh battle, but don't give up, keep on working hard, good luck!");
                    } else {//if player is not at Pallet Town, player loses 200 money as penalty and is sent to Pokemon Center to heal up
                        int lostmoney = 0;
                        if (money < 200) {
                            lostmoney = money;
                            money = 0;
                        } else {
                            lostmoney = 200;
                            money -= 200;
                        }
                        System.out.println("Uh Oh, all your pokemons have fainted, you whited out and was sent to the Pokemon Center, you lost $ " + lostmoney);
                        allhealup();
                        System.out.println("Nurse: Now your pokemons are all healed up, have a nice day!");
                    }
                    return true;
                }
            }
        }
        return false;//if there is at least one pokemon not fainted, return false
    }

    public void alterteam() { //player choose to alter team
        Scanner input = new Scanner(System.in);
        all: //all loop will not end until player choose to go back to last selection page
        while (true) {
            showteam(); //display all the pokemons in the team
            System.out.println("Choose a pokemon(1-6)/7 to exit: "); //prompt user to eenter choice
            String choice_st = input.nextLine();//receive choice
            if (isNum(choice_st)) {//check choice format
                int choice = Integer.parseInt(choice_st); //turn choice into integer
                switch (choice) {
                    case 1:
                        if (pokemon1 == null) { //check if pokemon1 exists
                            System.out.printf("+%s+\n", "-".repeat(90)); //if not then tell player this is a empty slot
                            System.out.println("There is no pokemon in this slot");
                        } else {
                            pokechoice(pokemon1, 1);//if pokemon1 exists call the pokechoice() method, which is a method that allows the player to make actions on a pokemon
                        }
                        break;
                    case 2:
                        if (pokemon2 == null) { //things happened in case 1 will happen in case2-6,only changing pokemon1 to pokemon2-6
                            System.out.printf("+%s+\n", "-".repeat(90));
                            System.out.println("There is no pokemon in this slot");
                        } else {
                            pokechoice(pokemon2, 2);
                        }
                        break;
                    case 3:
                        if (pokemon3 == null) {
                            System.out.printf("+%s+\n", "-".repeat(90));
                            System.out.println("There is no pokemon in this slot");
                        } else {
                            pokechoice(pokemon3, 3);
                        }
                        break;
                    case 4:
                        if (pokemon4 == null) {
                            System.out.printf("+%s+\n", "-".repeat(90));
                            System.out.println("There is no pokemon in this slot");
                        } else {
                            pokechoice(pokemon4, 4);
                        }
                        break;
                    case 5:
                        if (pokemon5 == null) {
                            System.out.printf("+%s+\n", "-".repeat(90));
                            System.out.println("There is no pokemon in this slot");
                        } else {
                            pokechoice(pokemon5, 5);
                        }
                        break;
                    case 6:
                        if (pokemon6 == null) {
                            System.out.printf("+%s+\n", "-".repeat(90));
                            System.out.println("There is no pokemon in this slot");
                        } else {
                            pokechoice(pokemon6, 6);
                        }
                        break;
                    case 7://if player choose 7 to go back to last selection page
                        break all; //end the all loop
                    default://if invalid choice is entered, display the message below
                        System.out.println("Invalid choice! Please choose again.");
                }
            } else {
                System.out.println("Invalid choice! Please choose again.");
            }
        }
    }

    public void pokechoice(Pokemon poke, int pos) { //a method that allows user to make actions on a pokemon, either check, heal or swap slots
        all://the all loop will not end unless player make a swap or choose to go back to last selection page
        while (true) {
            System.out.printf("+%s+\n", "-".repeat(90));
            System.out.println(poke.findname()); //display the pokemon's name
            System.out.println("1. Show details"); //choices the player can choose
            System.out.println("2. Use items");
            System.out.println("3. Swap slots");
            System.out.println("4. Back");
            System.out.println("Select one from 1-4: "); //prompt user to enter choice
            Scanner input = new Scanner(System.in);
            String choice_st = input.nextLine();//receive choice
            if (isNum(choice_st)) { //check choice format
                int choice = Integer.parseInt(choice_st); //turn choice into integer
                switch (choice) {
                    case 1: //if player choose to view pokemon's details
                        poke.showPokemonInfo(); //show the pokemon's info
                        moves: //the moves loop will not end unless the player choose to not check the details of the move and go back to last selection page
                        while (true) {
                            System.out.printf("+%s+\n", "-".repeat(90)); //print all the moves of the pokemon
                            System.out.println("1. " + poke.findmov1());
                            System.out.println("2. " + poke.findmov2());
                            System.out.println("3. " + poke.findmov3());
                            System.out.println("4. " + poke.findmov4());
                            System.out.println("Check moves(1-4)?5 to exit: ");//prompt user to enter choice
                            String choicemove_st = input.nextLine(); //receive choice
                            if (isNum(choicemove_st)) { //check choice format
                                int choicemove = Integer.parseInt(choicemove_st);//turn choice into integer
                                switch (choicemove) {
                                    case 1://if player choose to check move 1
                                        System.out.printf("+%s+\n", "-".repeat(90));//display move1 details
                                        Move x1 = new Move(poke.findmov1(), poke.findlvl());
                                        x1.showmovdetail();
                                        break;
                                    case 2://if player choose to check move2
                                        System.out.printf("+%s+\n", "-".repeat(90));//display move2 details
                                        Move x2 = new Move(poke.findmov2(), poke.findlvl());
                                        x2.showmovdetail();
                                        break;
                                    case 3://if player choose to check move3
                                        System.out.printf("+%s+\n", "-".repeat(90));//display move3 details
                                        Move x3 = new Move(poke.findmov3(), poke.findlvl());
                                        x3.showmovdetail();
                                        break;
                                    case 4://if player choose to check move4
                                        System.out.printf("+%s+\n", "-".repeat(90)); //display move4 details
                                        Move x4 = new Move(poke.findmov4(), poke.findlvl());
                                        x4.showmovdetail();
                                        break;
                                    case 5://if user choose to go back to last selection page by entering 5
                                        break moves;//end the moves loop
                                    default://if choice is invalid, display message below
                                        System.out.println("Invalid choice! Please choose again.");
                                }
                            } else {
                                System.out.println("Invalid choice! Please choose again.");
                            }
                        }
                        break;
                    case 2://if player choose to use item on pokemon
                        itemmm://itemmm loop will not end if user choose to go back to last selection page
                        while (true) {
                            System.out.printf("+%s+\n", "-".repeat(90));
                            showitems(); //display all the items and their numbers in the player's bag
                            System.out.println("12. Exit");//choice to go back to last selection page
                            System.out.println("Select items(1-11)/12 to exit: ");//prompt user to enter choice
                            String choiceitem_st = input.nextLine();//receive choice
                            if (isNum(choiceitem_st)) { //check choice format
                                int choiceitem = Integer.parseInt(choiceitem_st);//turn choice into integer
                                switch (choiceitem) {
                                    case 1: //this is supposed to be checking team while not in battle, hence all balls(Poke Ball, Great Ball, Ultra Ball) used will output "No effect"
                                        System.out.printf("+%s+\n", "-".repeat(90));
                                        System.out.println("This item has no effect on this pokemon");
                                        break;
                                    case 2:
                                        System.out.printf("+%s+\n", "-".repeat(90));
                                        System.out.println("This item has no effect on this pokemon");
                                        break;
                                    case 3:
                                        System.out.printf("+%s+\n", "-".repeat(90));
                                        System.out.println("This item has no effect on this pokemon");
                                        break;
                                    case 4://if Potion is selected
                                        System.out.printf("+%s+\n", "-".repeat(90));
                                        if (items.get("Potion") != 0) {//if there is at least one potion in the player's bag
                                            if (poke.findcurrenthp() == poke.findmaxhp() || poke.isFaint()) { //if the pokemon is either at full hp or fainted
                                                System.out.println("This item has no effect on this pokemon"); //then potion will not have any effect on the pokemon
                                            } else {//if the pokemon is not fainted and not at full hp
                                                System.out.println("You used a Potion on " + poke.findname()); //display potion is used message
                                                poke.heal(library.pokemon_items.get("Potion").get("heal")); //heal the pokemon according to the Potion's healing amount
                                                int old = items.get("Potion"); //deduct the number of Potions by 1
                                                items.replace("Potion", old, old - 1);
                                            }

                                        } else { //if there are no more Potions in the player's bag, then display the message below
                                            System.out.println("You do not have any Potions left");
                                        }
                                        break;
                                    case 5://same thing in case 4 will happen at case5 and 6,replacing Potion with Super Potion and Hyper Potion
                                        System.out.printf("+%s+\n", "-".repeat(90));
                                        if (items.get("Super Potion") != 0) {
                                            if (poke.findcurrenthp() == poke.findmaxhp() || poke.isFaint()) {
                                                System.out.println("This item has no effect on this pokemon");
                                            } else {
                                                System.out.println("You used a Super Potion on " + poke.findname());
                                                poke.heal(library.pokemon_items.get("Super Potion").get("heal"));
                                                int old = items.get("Super Potion");
                                                items.replace("Super Potion", old, old - 1);
                                            }

                                        } else {
                                            System.out.println("You do not have any Super Potions left");
                                        }

                                        break;
                                    case 6:
                                        System.out.printf("+%s+\n", "-".repeat(90));
                                        if (items.get("Hyper Potion") != 0) {
                                            if (poke.findcurrenthp() == poke.findmaxhp() || poke.isFaint()) {
                                                System.out.println("This item has no effect on this pokemon");
                                            } else {
                                                System.out.println("You used a Hyper Potion on " + poke.findname());
                                                poke.heal(library.pokemon_items.get("Hyper Potion").get("heal"));
                                                int old = items.get("Hyper Potion");
                                                items.replace("Hyper Potion", old, old - 1);
                                            }

                                        } else {
                                            System.out.println("You do not have any Hyper Potions left");
                                        }
                                        break;
                                    case 7: //same thing in case 4 happens, replacing Potion to Max Potion, and heal() method to fullheal() method as full heal method heals pokemon to full health, rather than a fixed amount in heal()
                                        System.out.printf("+%s+\n", "-".repeat(90));
                                        if (items.get("Max Potion") != 0) {
                                            if (poke.findcurrenthp() == poke.findmaxhp() || poke.isFaint()) {
                                                System.out.println("This item has no effect on this pokemon");
                                            } else {
                                                System.out.println("You used a Max Potion on " + poke.findname());
                                                poke.fullheal();
                                                int old = items.get("Max Potion");
                                                items.replace("Max Potion", old, old - 1);
                                            }

                                        } else {
                                            System.out.println("You do not have any Max Potions left");
                                        }
                                        break;
                                    case 8: //X Attack,X Defend and X Speed have no effect outside of battle
                                        System.out.printf("+%s+\n", "-".repeat(90));
                                        System.out.println("This item has no effect on this pokemon");
                                        break;
                                    case 9:
                                        System.out.printf("+%s+\n", "-".repeat(90));
                                        System.out.println("This item has no effect on this pokemon");
                                        break;
                                    case 10:
                                        System.out.printf("+%s+\n", "-".repeat(90));
                                        System.out.println("This item has no effect on this pokemon");
                                        break;
                                    case 11://if Revive is chosen
                                        System.out.printf("+%s+\n", "-".repeat(90));
                                        if (items.get("Revive") != 0) {//if there is at least one revive in the player's bag
                                            if (poke.isFaint()) { //if the pokemon is fainted
                                                System.out.println("You used a Revive on " + poke.findname());//print revive message
                                                poke.revive();//the pokemon is revived with half hp
                                                int old = items.get("Revive");//reduce the number of Revive in the player's bag by 1
                                                items.replace("Revive", old, old - 1);
                                            } else { //if pokemon is not fainted, then Revive will have no effect
                                                System.out.println("This item has no effect on this pokemon");
                                            }
                                        } else {//if there are no more Revives, print the message below
                                            System.out.println("You do not have any Revive left");
                                        }
                                        break;
                                    case 12://if player chooses 12 to go back to the last selection page
                                        break itemmm;//end the itemmm loop
                                    default://if choice entered is invalid, display the message below
                                        System.out.println("Invalid choice! Please choose again.");
                                }
                            } else {
                                System.out.println("Invalid choice! Please choose again.");
                            }
                        }
                        break;
                    case 3://if player choose to swap pokemon slots
                        swap://the swap loop will not end unless a swap is made or player chooses to cancel the swap
                        while(true) {
                            System.out.printf("+%s+\n", "-".repeat(90));
                            System.out.println("Choose a slot to swap with(1-6)/7 to cancel: "); //prompt player to enter choice
                            String choiceswap_st = input.nextLine(); //receive choice
                            if (isNum(choiceswap_st)) {//check choice format
                                int choiceswap = Integer.parseInt(choiceswap_st);//turn choice into integer
                                switch (choiceswap) {
                                    case 1: //if player choose to swap chosen pokemon slot with pokemon1
                                        if (pokemon1 != null && pos != 1) {//if pokemon1 exists and chosen pokemon is not pokemon1
                                            Pokemon temp = pokemon1; //hold original pokemon1 at temp
                                            pokemon1 = poke;//set pokemon1 to chosen pokemon
                                            if (pos == 2) {//if chosen pokemon's original slot is pokemon2, then set pokemon2 to temp
                                                pokemon2 = temp;
                                            } else if (pos == 3) {//if chosen pokemon's original slot is pokemon3, then set pokemon3 to temp
                                                pokemon3 = temp;
                                            } else if (pos == 4) {//if chosen pokemon's original slot is pokemon4, then set pokemon4 to temp
                                                pokemon4 = temp;
                                            } else if (pos == 5) {//if chosen pokemon's original slot is pokemon5, then set pokemon5 to temp
                                                pokemon5 = temp;
                                            } else if (pos == 6) {//if chosen pokemon's original slot is pokemon6, then set pokemon6 to temp
                                                pokemon6 = temp;
                                            }
                                            System.out.printf("+%s+\n", "-".repeat(90));//print position swapped message
                                            System.out.println("Position swapped");
                                            break all;//end the all loop
                                        } else if (pokemon1 != null && pos == 1) {//pokemon1 exists but the pokemon chosen is pokemon1
                                            System.out.printf("+%s+\n", "-".repeat(90));//display invalid swap message and reason
                                            System.out.println("This swap is invalid beacuse this slot is its original slot");
                                            break;
                                        } else {//if pokemon1 slot is empty
                                            System.out.printf("+%s+\n", "-".repeat(90));//print invalid swap message and reason
                                            System.out.println("This swap is invalid because this slot does not have a pokemon");
                                            break;
                                        }
                                    case 2://if player choose to swap chosen pokemon slot with pokemon2
                                        if (pokemon2 != null && pos != 2) {//if pokemon2 exists and chosen pokemon is not pokemon2
                                            Pokemon temp = pokemon2;//hold original pokemon2 at temp
                                            pokemon2 = poke;//set pokemon2 to chosen pokemon
                                            if (pos == 1) {//if chosen pokemon's original slot is pokemon1, then set pokemon1 to temp
                                                pokemon1 = temp;
                                            } else if (pos == 3) {//if chosen pokemon's original slot is pokemon3, then set pokemon3 to temp
                                                pokemon3 = temp;
                                            } else if (pos == 4) {//if chosen pokemon's original slot is pokemon4, then set pokemon4 to temp
                                                pokemon4 = temp;
                                            } else if (pos == 5) {//if chosen pokemon's original slot is pokemon5, then set pokemon5 to temp
                                                pokemon5 = temp;
                                            } else if (pos == 6) {//if chosen pokemon's original slot is pokemon6, then set pokemon6 to temp
                                                pokemon6 = temp;
                                            }
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("Position swapped");//print position swapped message
                                            break all;//edn the all loop
                                        } else if (pokemon2 != null && pos == 2) {//pokemon2 exists but the pokemon chosen is pokemon2
                                            System.out.printf("+%s+\n", "-".repeat(90));//display invalid swap message and reason
                                            System.out.println("This swap is invalid beacuse this slot is its original slot");
                                            break;
                                        } else {//if pokemon2 slot is empty
                                            System.out.printf("+%s+\n", "-".repeat(90));//display invalid swap message and reason
                                            System.out.println("This swap is invalid because this slot does not have a pokemon");
                                            break;
                                        }
                                    case 3://if player choose to swap chosen pokemon slot with pokemon3
                                        if (pokemon3 != null && pos != 3) {//if pokemon3 exists and chosen pokemon is not pokemon3
                                            Pokemon temp = pokemon3; //hold original pokemon3 at temp
                                            pokemon3 = poke;//set pokemon3 to chosen pokemon
                                            if (pos == 1) {//if chosen pokemon's original slot is pokemon1, then set pokemon1 to temp
                                                pokemon1 = temp;
                                            } else if (pos == 2) {//if chosen pokemon's original slot is pokemon2, then set pokemon2 to temp
                                                pokemon2 = temp;
                                            } else if (pos == 4) {//if chosen pokemon's original slot is pokemon4, then set pokemon4 to temp
                                                pokemon4 = temp;
                                            } else if (pos == 5) {//if chosen pokemon's original slot is pokemon5, then set pokemon5 to temp
                                                pokemon5 = temp;
                                            } else if (pos == 6) {//if chosen pokemon's original slot is pokemon6, then set pokemon6 to temp
                                                pokemon6 = temp;
                                            }
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("Position swapped");//print position swapped message
                                            break all;//end the all loop
                                        } else if (pokemon3 != null && pos == 3) {//pokemon3 exists but the pokemon chosen is pokemon3
                                            System.out.printf("+%s+\n", "-".repeat(90));//display invalid swap message and reason
                                            System.out.println("This swap is invalid beacuse this slot is its original slot");
                                            break;
                                        } else {//if pokemon3 slot is empty
                                            System.out.printf("+%s+\n", "-".repeat(90));//display invalid swap message and reason
                                            System.out.println("This swap is invalid because this slot does not have a pokemon");
                                            break;
                                        }
                                    case 4://if player choose to swap chosen pokemon slot with pokemon4
                                        if (pokemon4 != null && pos != 4) {//if pokemon4 exists and chosen pokemon is not pokemon4
                                            Pokemon temp = pokemon4;//hold original pokemon4 at temp
                                            pokemon4 = poke;//set pokemon4 to chosen pokemon
                                            if (pos == 1) {//if chosen pokemon's original slot is pokemon1, then set pokemon1 to temp
                                                pokemon1 = temp;
                                            } else if (pos == 2) {//if chosen pokemon's original slot is pokemon2, then set pokemon2 to temp
                                                pokemon2 = temp;
                                            } else if (pos == 3) {//if chosen pokemon's original slot is pokemon3, then set pokemon3 to temp
                                                pokemon3 = temp;
                                            } else if (pos == 5) {//if chosen pokemon's original slot is pokemon5, then set pokemon5 to temp
                                                pokemon5 = temp;
                                            } else if (pos == 6) {//if chosen pokemon's original slot is pokemon6, then set pokemon6 to temp
                                                pokemon6 = temp;
                                            }
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("Position swapped");//print position swapped message
                                            break all;//end the all loop
                                        } else if (pokemon4 != null && pos == 4) {//pokemon4 exists but the pokemon chosen is pokemon4
                                            System.out.printf("+%s+\n", "-".repeat(90));//display invalid swap message and reason
                                            System.out.println("This swap is invalid beacuse this slot is its original slot");
                                            break;
                                        } else {//if pokemon4 slot is empty
                                            System.out.printf("+%s+\n", "-".repeat(90));//display invalid swap message and reason
                                            System.out.println("This swap is invalid because this slot does not have a pokemon");
                                            break;
                                        }
                                    case 5://if player choose to swap chosen pokemon slot with pokemon5
                                        if (pokemon5 != null && pos != 5) {//if pokemon5 exists and chosen pokemon is not pokemon5
                                            Pokemon temp = pokemon5;//hold original pokemon5 at temp
                                            pokemon5 = poke;//set pokemon5 to chosen pokemon
                                            if (pos == 1) {//if chosen pokemon's original slot is pokemon1, then set pokemon1 to temp
                                                pokemon1 = temp;
                                            } else if (pos == 2) {//if chosen pokemon's original slot is pokemon2, then set pokemon2 to temp
                                                pokemon2 = temp;
                                            } else if (pos == 3) {//if chosen pokemon's original slot is pokemon3, then set pokemon3 to temp
                                                pokemon3 = temp;
                                            } else if (pos == 4) {//if chosen pokemon's original slot is pokemon4, then set pokemon4 to temp
                                                pokemon4 = temp;
                                            } else if (pos == 6) {//if chosen pokemon's original slot is pokemon6, then set pokemon6 to temp
                                                pokemon6 = temp;
                                            }
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("Position swapped");//print position swapped message
                                            break all;//end the all loop
                                        } else if (pokemon5 != null && pos == 5) {//pokemon5 exists but the pokemon chosen is pokemon5
                                            System.out.printf("+%s+\n", "-".repeat(90));//display invalid swap message and reason
                                            System.out.println("This swap is invalid beacuse this slot is its original slot");
                                            break;
                                        } else {//if pokemon5 slot is empty
                                            System.out.printf("+%s+\n", "-".repeat(90));//display invalid swap message and reason
                                            System.out.println("This swap is invalid because this slot does not have a pokemon");
                                            break;
                                        }
                                    case 6://if player choose to swap chosen pokemon slot with pokemon6
                                        if (pokemon6 != null && pos != 6) {//if pokemon6 exists and chosen pokemon is not pokemon6
                                            Pokemon temp = pokemon6;//hold original pokemon6 at temp
                                            pokemon6 = poke;//set pokemon6 to chosen pokemon
                                            if (pos == 1) {//if chosen pokemon's original slot is pokemon1, then set pokemon1 to temp
                                                pokemon1 = temp;
                                            } else if (pos == 2) {//if chosen pokemon's original slot is pokemon2, then set pokemon2 to temp
                                                pokemon2 = temp;
                                            } else if (pos == 3) {//if chosen pokemon's original slot is pokemon3, then set pokemon3 to temp
                                                pokemon3 = temp;
                                            } else if (pos == 4) {//if chosen pokemon's original slot is pokemon4, then set pokemon4 to temp
                                                pokemon4 = temp;
                                            } else if (pos == 5) {//if chosen pokemon's original slot is pokemon5, then set pokemon5 to temp
                                                pokemon5 = temp;
                                            }
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("Position swapped");//print position swapped message
                                            break all;//end the all loop
                                        } else if (pokemon6 != null && pos == 6) {//pokemon6 exists but the pokemon chosen is pokemon6
                                            System.out.printf("+%s+\n", "-".repeat(90));//display invalid swap message and reason
                                            System.out.println("This swap is invalid beacuse this slot is its original slot");
                                            break;
                                        } else {//if pokemon6 slot is empty
                                            System.out.printf("+%s+\n", "-".repeat(90));//display invalid swap message and reason
                                            System.out.println("This swap is invalid because this slot does not have a pokemon");
                                            break;
                                        }
                                    case 7://cancel swap
                                        break swap;//end the swap loop
                                    default://if invalid choice is entered, display the message below
                                        System.out.println("Invalid choice! Please choose again.");
                                }
                            } else {
                                System.out.println("Invalid choice! Please choose again.");
                            }
                        }
                        break;
                    case 4://if player choose to go back to last selection page
                        break all;//end the all loop
                    default://if invalid choice is entered, display the message below
                        System.out.println("Invalid choice! Please choose again.");
                }
            } else {
                System.out.println("Invalid choice! Please choose again.");
            }
        }
    }

    public void bag() { //a method to enable player check and use items in bag
        Scanner input = new Scanner(System.in);
        all://the all loop will not end unless player choose to stop checking and using items
        while (true) {
            System.out.printf("+%s+\n", "-".repeat(90)); //display all items and their numbers in the player's bag
            System.out.println("+--------------------Bag--------------------+");
            showitems();
            System.out.println("+--------------------End of Bag--------------------+");
            System.out.println("Choose an item(1-11)/12 to exit");//prompt user to enter choice
            String choice_st = input.nextLine();//receive choice
            if (isNum(choice_st)) {//check choice format
                int choice = Integer.parseInt(choice_st);//turn choice into integer
                switch (choice) {
                    case 1: //call choiceitem(String s) method and put the names of each choice's corresponding items as parameter, the method is to check and use a specific item
                        choiceitem("Poke Ball");
                        break;
                    case 2:
                        choiceitem("Great Ball");
                        break;
                    case 3:
                        choiceitem("Ultra Ball");
                        break;
                    case 4:
                        choiceitem("Potion");
                        break;
                    case 5:
                        choiceitem("Super Potion");
                        break;
                    case 6:
                        choiceitem("Hyper Potion");
                        break;
                    case 7:
                        choiceitem("Max Potion");
                        break;
                    case 8:
                        choiceitem("X Attack");
                        break;
                    case 9:
                        choiceitem("X Defend");
                        break;
                    case 10:
                        choiceitem("X Speed");
                        break;
                    case 11:
                        choiceitem("Revive");
                        break;
                    case 12://if user chooses 12 to go back to last selection page
                        break all;//end the all loop
                    default:
                        System.out.println("Invalid choice! Please choose again.");
                }
            } else {
                System.out.println("Invalid choice! Please choose again.");
            }
        }
    }

    public void choiceitem(String it) { //a method to let player check and use a specific item
        System.out.printf("+%s+\n", "-".repeat(90));
        Scanner input = new Scanner(System.in);
        switch (it) {
            case "Poke Ball": //this is supposed to be checking team while not in battle, hence all balls(Poke Ball, Great Ball, Ultra Ball) used will output "No effect"
                System.out.println("An item that can catch a wild pokemon, can be only used in battle, against wild pokemons");
                System.out.println("You have: " + items.get("Poke Ball"));
                break;
            case "Great Ball":
                System.out.println("An item that can catch a wild pokemon at a high rate, can be only used in battle, against wild pokemons");
                System.out.println("You have: " + items.get("Great Ball"));
                break;
            case "Ultra Ball":
                System.out.println("An item that can catch a wild pokemon at a very high rate, can be only used in battle, against wild pokemons");
                System.out.println("You have: " + items.get("Ultra Ball"));
                break;
            case "Potion"://if Potion is the chosen item
                System.out.println("An item that can heal a pokemon for " + library.pokemon_items.get(it).get("heal") + " hp");//show the item detail
                System.out.println("You have: " + items.get("Potion"));//show number of Potion left
                if (items.get("Potion") != 0) { //if the number of Potion is not 0
                    while (true && items.get("Potion") != 0) { //while there are still Potions left, this loop enables player to use multiple Potions until Potions is 0 or cancel the use
                        System.out.printf("+%s+\n", "-".repeat(90));
                        System.out.println("You have: " + items.get("Potion") + " Potions"); //show number of Potions left
                        System.out.println("Do you want to use it(y/n)? On which pokemon(1-6)? e.g: y1 to use at pokemon 1,n to not use and exit"); //prompt user to enter choice
                        showteam();//show all pokemons in team
                        String line = input.nextLine();//receive choice
                        if (line.length() != 0) { //check line format
                            if (line.charAt(0) == 'y' && line.length() == 2) { //if line starts with y and have a length of 2, that means it is an acceptable format
                                int poke = line.charAt(1) - '0'; //determine the slot of the chosen pokemon
                                System.out.println("Selected pokemon on slot: " + poke); //display message of which pokemon is selected
                                switch (poke) {
                                    case 1://pokemon1 is selected
                                        if (pokemon1 != null) {//if pokemon1 exist
                                            if (pokemon1.findcurrenthp() == pokemon1.findmaxhp()) {//if pokemon1 is at full hp, Potion, Super Potion, Hyper Potion and Max Potion will not have any effect on the pokemon
                                                System.out.printf("+%s+\n", "-".repeat(90));//display the message below
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon1.isFaint()) {//if pokemon1 is fainted, Potion, Super Potion, Hyper Potion and Max Potion will not have any effect on the pokemon
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else { //if pokemon1 is not fainted and not at fullhp
                                                    System.out.println("You used a Potion on " + pokemon1.findname());//display item used message
                                                    pokemon1.heal(library.pokemon_items.get(it).get("heal"));//heal the pokemon with the fixed amount of healing Potion provides
                                                    int old = items.get("Potion");//reduce the number of potion in the player's bag
                                                    items.replace("Potion", old, old - 1);
                                                }
                                            }
                                        } else { //if pokemon1 slot is empty
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 2://same thing happens in case 1 happens in case2-6, changing pokemon1 to pokemon2-6 respectively
                                        if (pokemon2 != null) {
                                            if (pokemon2.findcurrenthp() == pokemon2.findmaxhp()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon2.isFaint()) {
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else {
                                                    System.out.println("You used a Potion on " + pokemon2.findname());
                                                    pokemon2.heal(library.pokemon_items.get(it).get("heal"));
                                                    int old = items.get("Potion");
                                                    items.replace("Potion", old, old - 1);
                                                }
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 3:
                                        if (pokemon3 != null) {
                                            if (pokemon3.findcurrenthp() == pokemon3.findmaxhp()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon3.isFaint()) {
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else {
                                                    System.out.println("You used a Potion on " + pokemon3.findname());
                                                    pokemon3.heal(library.pokemon_items.get(it).get("heal"));
                                                    int old = items.get("Potion");
                                                    items.replace("Potion", old, old - 1);
                                                }
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 4:
                                        if (pokemon4 != null) {
                                            if (pokemon4.findcurrenthp() == pokemon4.findmaxhp()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon4.isFaint()) {
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else {
                                                    System.out.println("You used a Potion on " + pokemon4.findname());
                                                    pokemon4.heal(library.pokemon_items.get(it).get("heal"));
                                                    int old = items.get("Potion");
                                                    items.replace("Potion", old, old - 1);
                                                }
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 5:
                                        if (pokemon5 != null) {
                                            if (pokemon5.findcurrenthp() == pokemon5.findmaxhp()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon5.isFaint()) {
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else {
                                                    System.out.println("You used a Potion on " + pokemon5.findname());
                                                    pokemon5.heal(library.pokemon_items.get(it).get("heal"));
                                                    int old = items.get("Potion");
                                                    items.replace("Potion", old, old - 1);
                                                }
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 6:
                                        if (pokemon6 != null) {
                                            if (pokemon6.findcurrenthp() == pokemon6.findmaxhp()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon6.isFaint()) {
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else {
                                                    System.out.println("You used a Potion on " + pokemon6.findname());
                                                    pokemon6.heal(library.pokemon_items.get(it).get("heal"));
                                                    int old = items.get("Potion");
                                                    items.replace("Potion", old, old - 1);
                                                }
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    default:
                                        System.out.println("Invalid choice! Please choose again.");
                                        break;
                                }
                            } else if (line.charAt(0) == 'n') { //if player enters n to cancel the use
                                break;//end the loop
                            } else { //if choice is invalid, display message below
                                System.out.println("Invalid choice! Please choose again.");
                            }
                        } else {
                            System.out.println("Invalid choice! Please choose again.");
                        }
                    }
                    if (items.get("Potion") == 0) { //if there are no more Potion in the player's bag, display the message below
                        System.out.printf("+%s+\n", "-".repeat(90));
                        System.out.println("You don't have any Potions left");
                    }
                }
                break;
            case "Super Potion": //same thing happened in case "Potion" will happen in case "Super Potion" and "Hyper Potion", only changing Potion to Super Potion and Hyper Potion respectively
                System.out.println("An item that can heal a pokemon for " + library.pokemon_items.get(it).get("heal") + " hp");
                System.out.println("You have: " + items.get("Super Potion"));
                if (items.get("Super Potion") != 0) {
                    while (true && items.get("Super Potion") != 0) {
                        System.out.printf("+%s+\n", "-".repeat(90));
                        System.out.println("You have: " + items.get("Super Potion") + " Super Potions");
                        System.out.println("Do you want to use it(y/n)? On which pokemon(1-6)? e.g: y1 to use at pokemon 1,n to not use and exit");
                        showteam();
                        String line = input.nextLine();
                        if (line.length() != 0) {
                            if (line.charAt(0) == 'y' && line.length() == 2) {
                                int poke = line.charAt(1) - '0';
                                System.out.println("Selected pokemon on slot: " + poke);
                                switch (poke) {
                                    case 1:
                                        if (pokemon1 != null) {
                                            if (pokemon1.findcurrenthp() == pokemon1.findmaxhp()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon1.isFaint()) {
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else {
                                                    System.out.println("You used a Super Potion on " + pokemon1.findname());
                                                    pokemon1.heal(library.pokemon_items.get(it).get("heal"));
                                                    int old = items.get("Super Potion");
                                                    items.replace("Super Potion", old, old - 1);
                                                }
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 2:
                                        if (pokemon2 != null) {
                                            if (pokemon2.findcurrenthp() == pokemon2.findmaxhp()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon2.isFaint()) {
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else {
                                                    System.out.println("You used a Super Potion on " + pokemon2.findname());
                                                    pokemon2.heal(library.pokemon_items.get(it).get("heal"));
                                                    int old = items.get("Super Potion");
                                                    items.replace("Super Potion", old, old - 1);
                                                }
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 3:
                                        if (pokemon3 != null) {
                                            if (pokemon3.findcurrenthp() == pokemon3.findmaxhp()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon3.isFaint()) {
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else {
                                                    System.out.println("You used a Super Potion on " + pokemon3.findname());
                                                    pokemon3.heal(library.pokemon_items.get(it).get("heal"));
                                                    int old = items.get("Super Potion");
                                                    items.replace("Super Potion", old, old - 1);
                                                }
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 4:
                                        if (pokemon4 != null) {
                                            if (pokemon4.findcurrenthp() == pokemon4.findmaxhp()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon4.isFaint()) {
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else {
                                                    System.out.println("You used a Super Potion on " + pokemon4.findname());
                                                    pokemon4.heal(library.pokemon_items.get(it).get("heal"));
                                                    int old = items.get("Super Potion");
                                                    items.replace("Super Potion", old, old - 1);
                                                }
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 5:
                                        if (pokemon5 != null) {
                                            if (pokemon5.findcurrenthp() == pokemon5.findmaxhp()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon5.isFaint()) {
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else {
                                                    System.out.println("You used a Super Potion on " + pokemon5.findname());
                                                    pokemon5.heal(library.pokemon_items.get(it).get("heal"));
                                                    int old = items.get("Super Potion");
                                                    items.replace("Super Potion", old, old - 1);
                                                }
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 6:
                                        if (pokemon6 != null) {
                                            if (pokemon6.findcurrenthp() == pokemon6.findmaxhp()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon6.isFaint()) {
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else {
                                                    System.out.println("You used a Super Potion on " + pokemon6.findname());
                                                    pokemon6.heal(library.pokemon_items.get(it).get("heal"));
                                                    int old = items.get("Super Potion");
                                                    items.replace("Super Potion", old, old - 1);
                                                }
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    default:
                                        System.out.println("Invalid choice! Please choose again.");
                                        break;
                                }
                            } else if (line.charAt(0) == 'n') {
                                break;
                            } else {
                                System.out.println("Invalid choice! Please choose again.");
                            }
                        } else {
                            System.out.println("Invalid choice! Please choose again.");
                        }
                    }
                    if (items.get("Super Potion") == 0) {
                        System.out.printf("+%s+\n", "-".repeat(90));
                        System.out.println("You don't have any Super Potions left");
                    }
                }
                break;
            case "Hyper Potion":
                System.out.println("An item that can heal a pokemon for " + library.pokemon_items.get(it).get("heal") + " hp");
                System.out.println("You have: " + items.get("Hyper Potion"));
                if (items.get("Hyper Potion") != 0) {
                    while (true && items.get("Hyper Potion") != 0) {
                        System.out.printf("+%s+\n", "-".repeat(90));
                        System.out.println("You have: " + items.get("Hyper Potion") + " Hyper Potions");
                        System.out.println("Do you want to use it(y/n)? On which pokemon(1-6)? e.g: y1 to use at pokemon 1,n to not use and exit");
                        showteam();
                        String line = input.nextLine();
                        if (line.length() != 0) {
                            if (line.charAt(0) == 'y' && line.length() == 2) {
                                int poke = line.charAt(1) - '0';
                                System.out.println("Selected pokemon on slot: " + poke);
                                switch (poke) {
                                    case 1:
                                        if (pokemon1 != null) {
                                            if (pokemon1.findcurrenthp() == pokemon1.findmaxhp()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon1.isFaint()) {
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else {
                                                    System.out.println("You used a Hyper Potion on " + pokemon1.findname());
                                                    pokemon1.heal(library.pokemon_items.get(it).get("heal"));
                                                    int old = items.get("Hyper Potion");
                                                    items.replace("Hyper Potion", old, old - 1);
                                                }
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 2:
                                        if (pokemon2 != null) {
                                            if (pokemon2.findcurrenthp() == pokemon2.findmaxhp()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon2.isFaint()) {
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else {
                                                    System.out.println("You used a Hyper Potion on " + pokemon2.findname());
                                                    pokemon2.heal(library.pokemon_items.get(it).get("heal"));
                                                    int old = items.get("Hyper Potion");
                                                    items.replace("Hyper Potion", old, old - 1);
                                                }
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 3:
                                        if (pokemon3 != null) {
                                            if (pokemon3.findcurrenthp() == pokemon3.findmaxhp()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon3.isFaint()) {
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else {
                                                    System.out.println("You used a Hyper Potion on " + pokemon3.findname());
                                                    pokemon3.heal(library.pokemon_items.get(it).get("heal"));
                                                    int old = items.get("Hyper Potion");
                                                    items.replace("Hyper Potion", old, old - 1);
                                                }
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 4:
                                        if (pokemon4 != null) {
                                            if (pokemon4.findcurrenthp() == pokemon4.findmaxhp()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon4.isFaint()) {
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else {
                                                    System.out.println("You used a Hyper Potion on " + pokemon4.findname());
                                                    pokemon4.heal(library.pokemon_items.get(it).get("heal"));
                                                    int old = items.get("Hyper Potion");
                                                    items.replace("Hyper Potion", old, old - 1);
                                                }
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 5:
                                        if (pokemon5 != null) {
                                            if (pokemon5.findcurrenthp() == pokemon5.findmaxhp()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon5.isFaint()) {
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else {
                                                    System.out.println("You used a Hyper Potion on " + pokemon5.findname());
                                                    pokemon5.heal(library.pokemon_items.get(it).get("heal"));
                                                    int old = items.get("Hyper Potion");
                                                    items.replace("Hyper Potion", old, old - 1);
                                                }
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 6:
                                        if (pokemon6 != null) {
                                            if (pokemon6.findcurrenthp() == pokemon6.findmaxhp()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon6.isFaint()) {
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else {
                                                    System.out.println("You used a Hyper Potion on " + pokemon6.findname());
                                                    pokemon6.heal(library.pokemon_items.get(it).get("heal"));
                                                    int old = items.get("Hyper Potion");
                                                    items.replace("Hyper Potion", old, old - 1);
                                                }
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    default:
                                        System.out.println("Invalid choice! Please choose again.");
                                        break;
                                }
                            } else if (line.charAt(0) == 'n') {
                                break;
                            } else {
                                System.out.println("Invalid choice! Please choose again.");
                            }
                        } else {
                            System.out.println("Invalid choice, please choose again");
                        }
                    }
                    if (items.get("Hyper Potion") == 0) {
                        System.out.printf("+%s+\n", "-".repeat(90));
                        System.out.println("You don't have any Hyper Potions left");
                    }
                }
                break;
            case "Max Potion"://same thing happened in case "Potion" also happens in this case, changing Potion to Max Potion and heal() method to fullheal() method for reasons's explained in pokechoice() method
                System.out.println("An item that can heal a pokemon until its full hp");
                System.out.println("You have: " + items.get("Max Potion"));
                if (items.get("Max Potion") != 0) {
                    while (true && items.get("Max Potion") != 0) {
                        System.out.printf("+%s+\n", "-".repeat(90));
                        System.out.println("You have: " + items.get("Max Potion") + " Max Potions");
                        System.out.println("Do you want to use it(y/n)? On which pokemon(1-6)? e.g: y1 to use at pokemon 1,n to not use and exit");
                        showteam();
                        String line = input.nextLine();
                        if (line.length() != 0) {
                            if (line.charAt(0) == 'y' && line.length() == 2) {
                                int poke = line.charAt(1) - '0';
                                System.out.println("Selected pokemon on slot: " + poke);
                                switch (poke) {
                                    case 1:
                                        if (pokemon1 != null) {
                                            if (pokemon1.findcurrenthp() == pokemon1.findmaxhp()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon1.isFaint()) {
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else {
                                                    System.out.println("You used a Max Potion on " + pokemon1.findname());
                                                    pokemon1.fullheal();
                                                    int old = items.get("Max Potion");
                                                    items.replace("Max Potion", old, old - 1);
                                                }
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 2:
                                        if (pokemon2 != null) {
                                            if (pokemon2.findcurrenthp() == pokemon2.findmaxhp()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon2.isFaint()) {
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else {
                                                    System.out.println("You used a Max Potion on " + pokemon2.findname());
                                                    pokemon2.fullheal();
                                                    int old = items.get("Max Potion");
                                                    items.replace("Max Potion", old, old - 1);
                                                }
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 3:
                                        if (pokemon3 != null) {
                                            if (pokemon3.findcurrenthp() == pokemon3.findmaxhp()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon3.isFaint()) {
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else {
                                                    System.out.println("You used a Max Potion on " + pokemon3.findname());
                                                    pokemon3.fullheal();
                                                    int old = items.get("Max Potion");
                                                    items.replace("Max Potion", old, old - 1);
                                                }
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 4:
                                        if (pokemon4 != null) {
                                            if (pokemon4.findcurrenthp() == pokemon4.findmaxhp()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon4.isFaint()) {
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else {
                                                    System.out.println("You used a Max Potion on " + pokemon4.findname());
                                                    pokemon4.fullheal();
                                                    int old = items.get("Max Potion");
                                                    items.replace("Max Potion", old, old - 1);
                                                }
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 5:
                                        if (pokemon5 != null) {
                                            if (pokemon5.findcurrenthp() == pokemon5.findmaxhp()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon5.isFaint()) {
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else {
                                                    System.out.println("You used a Max Potion on " + pokemon5.findname());
                                                    pokemon5.fullheal();
                                                    int old = items.get("Max Potion");
                                                    items.replace("Max Potion", old, old - 1);
                                                }
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 6:
                                        if (pokemon6 != null) {
                                            if (pokemon6.findcurrenthp() == pokemon6.findmaxhp()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon with full hp");
                                            } else {
                                                if (pokemon6.isFaint()) {
                                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                                } else {
                                                    System.out.println("You used a Max Potion on " + pokemon6.findname());
                                                    pokemon6.fullheal();
                                                    int old = items.get("Max Potion");
                                                    items.replace("Max Potion", old, old - 1);
                                                }
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    default:
                                        System.out.println("Invalid choice! Please choose again.");
                                        break;
                                }
                            } else if (line.charAt(0) == 'n') {
                                break;
                            } else {
                                System.out.println("Invalid choice! Please choose again.");
                            }
                        } else {
                            System.out.println("Invalid choice! Please choose again.");
                        }
                    }
                    if (items.get("Max Potion") == 0) {
                        System.out.printf("+%s+\n", "-".repeat(90));
                        System.out.println("You don't have any Max Potions left");
                    }
                }
                break;
            case "X Attack": //X Attack, X Defend, X Speed will not have any effect outside of battle
                System.out.println("An item that can increase a pokemon's attack by 1 stage in battle, can be only used in battle");
                System.out.println("You have: " + items.get("X Attack"));
                break;
            case "X Defend":
                System.out.println("An item that can increase a pokemon's defense by 1 stage in battle, can be only used in battle");
                System.out.println("You have: " + items.get("X Defend"));
                break;
            case "X Speed":
                System.out.println("An item that can increase a pokemon's speed by 1 stage in battle, can be only used in battle");
                System.out.println("You have: " + items.get("X Speed"));
                break;
            case "Revive": //if Revive is chosen
                System.out.println("An item that can revive a fainted pokemon with half of its hp"); //display description of Revive
                System.out.println("You have: " + items.get("Revive"));//show the number of Revive in player's bag
                if (items.get("Revive") != 0) { //if the number of Revive is not 0
                    while (true && items.get("Revive") != 0) {//while Revive is not 0, same purpose as while(items.get("Potion")!=0)
                        System.out.printf("+%s+\n", "-".repeat(90)); 
                        System.out.println("You have: " + items.get("Revive") + " Revives");//display the number of Revive
                        System.out.println("Do you want to use it(y/n)? On which pokemon(1-6)? e.g: y1 to use at pokemon 1,n to not use and exit");//prompt user to enter choice
                        showteam();//dislay all the pokemon's in the team
                        String line = input.nextLine();//receive choice
                        if (line.length() != 0) { //check choice format
                            if (line.charAt(0) == 'y' && line.length() == 2) { //if choice starts with y and length is 2
                                int poke = line.charAt(1) - '0'; //determine the slot of the pokemon chosen using second character of choice
                                System.out.println("Selected pokemon on slot: " + poke);//display the selected pokemon
                                switch (poke) {
                                    case 1:
                                        if (pokemon1 != null) {//if pokemon1 exists
                                            if (!pokemon1.isFaint()) {//if pokemon1 is not fainted, Revive will not have any effect on it
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon that is not fainted");
                                            } else {//if pokemon1 is fainted
                                                System.out.println("You used a Revive on " + pokemon1.findname());//display Revive is used message
                                                pokemon1.revive(); //pokemon1 is revived
                                                int old = items.get("Revive");//reduce the number of Revive by 1
                                                items.replace("Revive", old, old - 1);
                                            }
                                        } else {//if pokemon1 does not exist
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 2://same thing happened in case1 will happen in case2-6, changing pokemon1 to pokemon2-6 respectively
                                        if (pokemon2 != null) {
                                            if (!pokemon2.isFaint()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon that is not fainted");
                                            } else {
                                                System.out.println("You used a Revive on " + pokemon2.findname());
                                                pokemon2.revive();
                                                int old = items.get("Revive");
                                                items.replace("Revive", old, old - 1);
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 3:
                                        if (pokemon3 != null) {
                                            if (!pokemon3.isFaint()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon that is not fainted");
                                            } else {
                                                System.out.println("You used a Revive on " + pokemon3.findname());
                                                pokemon3.revive();
                                                int old = items.get("Revive");
                                                items.replace("Revive", old, old - 1);
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 4:
                                        if (pokemon4 != null) {
                                            if (!pokemon4.isFaint()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon that is not fainted");
                                            } else {
                                                System.out.println("You used a Revive on " + pokemon4.findname());
                                                pokemon4.revive();
                                                int old = items.get("Revive");
                                                items.replace("Revive", old, old - 1);
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 5:
                                        if (pokemon5 != null) {
                                            if (!pokemon5.isFaint()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon that is not fainted");
                                            } else {
                                                System.out.println("You used a Revive on " + pokemon5.findname());
                                                pokemon5.revive();
                                                int old = items.get("Revive");
                                                items.replace("Revive", old, old - 1);
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 6:
                                        if (pokemon6 != null) {
                                            if (!pokemon6.isFaint()) {
                                                System.out.printf("+%s+\n", "-".repeat(90));
                                                System.out.println("This item has no effect on a pokemon that is not fainted");
                                            } else {
                                                System.out.println("You used a Revive on " + pokemon6.findname());
                                                pokemon6.revive();
                                                int old = items.get("Revive");
                                                items.replace("Revive", old, old - 1);
                                            }
                                        } else {
                                            System.out.printf("+%s+\n", "-".repeat(90));
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    default://if choice is invalid, display message below
                                        System.out.println("Invalid choice! Please choose again.");
                                        break;
                                }
                            } else if (line.charAt(0) == 'n') {//if player chooses n to cancel the use of item
                                break;//end the loop
                            } else {
                                System.out.println("Invalid choice! Please choose again.");
                            }
                        } else {
                            System.out.println("Invalid choice! Please choose again.");
                        }
                    }
                    if (items.get("Revive") == 0) {//if there are no more Revives left
                        System.out.printf("+%s+\n", "-".repeat(90));
                        System.out.println("You don't have any Revives left");
                    }
                }
                break;
        }
    }

    public boolean isNum(String s) { //a method to check whether a String object can be turned into integer
        try { //try turning
            int ss = Integer.parseInt(s);
            return true; //if could return true
        } catch (NumberFormatException nfe) {//if could not and error is caught
            return false;//return false
        }
    }

    public void allhealup() { //heal all pokemons in the team to full hp and recover from fainted to not fainted
        System.out.println("All your pokemons have been healed to their best status"); //display all heal message
        if (pokemon1 != null) {//full restore all existing pokemons in the team
            pokemon1.fullres(); 
        }
        if (pokemon2 != null) {
            pokemon2.fullres();
        }
        if (pokemon3 != null) {
            pokemon3.fullres();
        }
        if (pokemon4 != null) {
            pokemon4.fullres();
        }
        if (pokemon5 != null) {
            pokemon5.fullres();
        }
        if (pokemon6 != null) {
            pokemon6.fullres();
        }
    }

    public void alterPC(Player player) { //method that allows player to check pokemons in PC
        Scanner input = new Scanner(System.in);
        if (PC.isEmpty()) { //if there is nothing in the PC
            System.out.println("Your PC is empty.");
        } else {//if there is Pokemons in the PC
            all://all loop will not end unless player cancel the use of PC and go back to last selection page
            while (true) {
                System.out.println("+--------------------PC--------------------+");
                for (int i = 0; i < PC.size(); i++) {
                    Pokemon poke = PC.get(i);//display all pokemons in the PC and their levels
                    System.out.println((i + 1) + ". " + poke.findname() + " [ level " + poke.findlvl() + " ] ");
                }
                System.out.println((PC.size() + 1) + ". Back");//display a Back choice
                System.out.print("Enter your choice: ");//prompt user to enter choice
                String choice_st = input.nextLine();//receive choice
                if (isNum(choice_st)) {//check choice format
                    int choice = Integer.parseInt(choice_st); //turn choice into integer
                    if (choice <= PC.size() && choice > 0) { //if choice range is within PC's size, which is the number of Pokemon in PC
                        Pokemon poke = PC.get(choice - 1);//set poke to the pokemon chosen
                        System.out.println("You chose: " + poke.findname() + " [ level " + poke.findlvl() + " ] ");//print out the chosen pokemon
                        PCpokeChoice(poke);//call PCpokeChoice method that allows player to make actions on the selected Pokemon
                    } else if (choice == PC.size() + 1) {//if user choose to go back to last selection page
                        break all; //end the all loop
                    } else { //if choice is invalid, display the message below
                        System.out.println("Invalid choice, please choose again");
                    }
                } else {
                    System.out.println("Invalid choice, please choose again");
                }
            }
        }
    }

    public void PCpokeChoice(Pokemon poke) { //a method that allows player to make actions on chosen pokemon in PC
        Scanner input = new Scanner(System.in);
        System.out.println("+--------------------Pokemon info--------------------+");//display the info of the Pokemon
        poke.showPokemonInfo();
        System.out.println("Do you want to withdraw this pokemon(y-yes/other input-no and go back)?");//ask if player wants to withdraw the pokemon
        String choice = input.nextLine();//receive choice
        if (choice.equals("y")) { //if choice is y
            if (pokemon1 == null) { //if pokemon1 is empty
                System.out.println("You successfully withdrew this pokemon to pokemon slot 1.");
                pokemon1 = poke;//withdrew to pokemon to pokemon1
                PC.remove(poke);
            } else if (pokemon2 == null) {//if pokemon2 is empty
                System.out.println("You successfully withdrew this pokemon to pokemon slot 2.");
                pokemon2 = poke;//withdrew the pokemon to pokemon2
                PC.remove(poke);
            } else if (pokemon3 == null) {//if pokemon3 is empty
                System.out.println("You successfully withdrew this pokemon to pokemon slot 3.");
                pokemon3 = poke;//withdrew the pokemon to pokemon3
                PC.remove(poke);
            } else if (pokemon4 == null) {//if pokemon4 is empty
                System.out.println("You successfully withdrew this pokemon to pokemon slot 4.");
                pokemon4 = poke;//withdrew the pokemon to pokemon4
                PC.remove(poke);
            } else if (pokemon5 == null) {//if pokemon5 is empty
                System.out.println("You successfully withdrew this pokemon to pokemon slot 5.");
                pokemon5 = poke;//withdrew the pokemon to pokemon5
                PC.remove(poke);
            } else if (pokemon6 == null) {//if pokemon6 is empty
                System.out.println("You successfully withdrew this pokemon to pokemon slot 6.");
                pokemon6 = poke;//withdrew the pokemon to pokemon6
                PC.remove(poke);
            } else {//if team is true
                swapp://the swapp loop will not end unless player make a swap between the pokemon in PC and a pokemon in the team, or cancel the swap
                while (true) {
                    System.out.println("Your team is full. Which pokemon do you want to swap with?(1-6 to swap)/(7 to cancel)"); //prompt user to enter a choice
                    String swap_st = input.nextLine();//receive choice
                    if (isNum(swap_st)) {//check choice format
                        int swap = Integer.parseInt(swap_st);//turn choice into integer
                        switch (swap) { 
                            case 1://if player choose to swap with pokemon1
                                //display the swap information
                                System.out.println("You swapped " + poke.findname() + " [ level " + poke.findlvl() + " ] with " + pokemon1.findname() + " [ level " + pokemon1.findlvl() + " ] ");
                                Pokemon temp = pokemon1;//set temp to hold pokemon1
                                pokemon1 = poke;//set pokemon1 to the chosen PC pokemon
                                PC.set(PC.indexOf(poke), temp);//put the original pokemon1 into the original position of the PC pokemon
                                break swapp;//end the swapp loop
                            case 2://case2-6 is same as case1 only changing pokemon 1 to pokemon2-6 respectively
                                System.out.println("You swapped " + poke.findname() + " [ level " + poke.findlvl() + " ] with " + pokemon2.findname() + " [ level " + pokemon2.findlvl() + " ] ");
                                temp = pokemon2;
                                pokemon2 = poke;
                                PC.set(PC.indexOf(poke), temp);
                                break swapp;
                            case 3:
                                System.out.println("You swapped " + poke.findname() + " [ level " + poke.findlvl() + " ] with " + pokemon3.findname() + " [ level " + pokemon3.findlvl() + " ] ");
                                temp = pokemon3;
                                pokemon3 = poke;
                                PC.set(PC.indexOf(poke), temp);
                                break swapp;
                            case 4:
                                System.out.println("You swapped " + poke.findname() + " [ level " + poke.findlvl() + " ] with " + pokemon4.findname() + " [ level " + pokemon4.findlvl() + " ] ");
                                temp = pokemon4;
                                pokemon4 = poke;
                                PC.set(PC.indexOf(poke), temp);
                                break swapp;
                            case 5:
                                System.out.println("You swapped " + poke.findname() + " [ level " + poke.findlvl() + " ] with " + pokemon5.findname() + " [ level " + pokemon5.findlvl() + " ] ");
                                temp = pokemon5;
                                pokemon5 = poke;
                                PC.set(PC.indexOf(poke), temp);
                                break swapp;
                            case 6:
                                System.out.println("You swapped " + poke.findname() + " [ level " + poke.findlvl() + " ] with " + pokemon6.findname() + " [ level " + pokemon6.findlvl() + " ] ");
                                temp = pokemon6;
                                pokemon6 = poke;
                                PC.set(PC.indexOf(poke), temp);
                                break swapp;
                            case 7://if player enters 7 to cancel the swap
                                break swapp;//end the swapp loop
                            default:
                                System.out.println("Invalid choice! Please choose again.");
                        }
                    } else {
                        System.out.println("Invalid choice, please choose again");
                    }
                }
            }
        }
    }

    public int getNumberofBadges() { //return numofbadge
        return numofbadge;
    }

    public void setBadges(String[] badges) { //set badges[]
        this.badges = badges;
    }

    public void setNumberofBadges(int numofbadge) {//set numofbadge
        this.numofbadge = numofbadge;
    }


    public void setItems(HashMap<String, Integer> items) { //set HashMap of items
        this.items = items;
    }

    public void setRivalwins(int rivalracewins) {//set rivalracewins
        this.rivalracewins = rivalracewins;
    }

    public void setVictories(int battlewon) {//set battlewon
        this.battlewon = battlewon;
    }

    public void setPC(ArrayList<Pokemon> pc) { //set PC
        this.PC = pc;
    }


    public void setCurrentCity(String currentCity) {//set currentCity
        this.currentCity = currentCity;
    }
}