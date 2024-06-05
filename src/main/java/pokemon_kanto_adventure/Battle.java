package pokemon_kanto_adventure;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Battle {
    private Player player;
    private String opponent;
    private boolean win;
    private Pokemon your_pokemon; //your pokemon in battle
    private Pokemon foe_pokemon; //opponent's pokemon in battle
    private double atk; //attack stat of your pokemon in battle
    private double def; //defense stat of your pokemon in battle
    private double sp; //speed stat of your pokemon in battle
    private double foe_atk; //attack stat of opponent's pokemon in battle
    private double foe_def; // defense stat of opponent's pokemon in battle
    private double foe_sp; // speed stat of opponent's pokemon in battle
    public Battle(Player p,String opp){ //constructor and method for trainer battles
        library.readAllTrainers();//reset all trainer's pokemon
        Random r = new Random(); //declare and initialize Random object to be used later
        win = false; //declare and initialize boolean win for outside the class uses
        player = p; //take player object from outside the class so that when the player object's datas are altered here the changes could be passed back to the method that calls this method
        opponent = opp; //String opponent is declared to symbolize the name of the trainer you are fighting and could find their pokemon datas through the library.Trainers Hashmap by getting the Arraylist of their pokemons using their names as key
        ArrayList<Pokemon> opponent_pokemons = library.Trainers.get(opp); //get the ArrayList of the opposing trainer's pokemon with their name as key
        //find a pokemon in your team to battle, the default pokemon to sent out when the battle starts is the pokemon in the first slot, if first slot pokemon is fainted and could not battle, then check if second pokemon exists or not and is fainted or not,
        //if second pokemon exists and is not fainted, send out second pokemon, if second pokemon is also fainted, then check third pokemon exists and fainted or not, the same thing will be done if a not fainted pokemon is not found until pokemon 6
        //at least 1 pokemon is not fainted, if pokemon 1-5 is all fainted then pokemon 6 will be sent out
        //because in our program, when all your pokemons in the team is fainted, we have a teamfaint() method that checks whether all pokemons in the team is fainted or not, if true, then all pokemons will be sent to the Pokemon Center or Mum to heal up
        if(!p.findPoke1().isFaint()){ //pokemon1 will never be null, as player will choose a starter pokemon when a new Save is created.
            your_pokemon = p.findPoke1(); //p is player, findPoke1 will return the Pokemon object that contains the data of the pokemon in the first slot, same for findPoke2-6
        }else if(p.findPoke2()!=null&&!p.findPoke2().isFaint()){
            your_pokemon = p.findPoke2();
        }else if(p.findPoke3()!=null&&!p.findPoke3().isFaint()){
            your_pokemon = p.findPoke3();
        }else if(p.findPoke4()!=null&&!p.findPoke4().isFaint()){
            your_pokemon = p.findPoke4();
        }else if(p.findPoke5()!=null&&!p.findPoke5().isFaint()){
            your_pokemon = p.findPoke5();
        }else if(p.findPoke6()!=null){ //at least last pokemon is not fainted
            your_pokemon = p.findPoke6();
        }
        your_pokemon.changebattlestatus(true); //change player's pokemon's battle status to true
        foe_pokemon = opponent_pokemons.get(0); //opponent_pokemons is an ArrayList that contains the data of the opponents' pokemon, get(0) means the first pokemon in the opponent's team
        //counter of how many pokemons in the opponent's team have fainted, when a pokemon of the opponent is defeated, this counter increases by 1,
        //and when this counter is equal to the size of the opponent's team's size, opponent loses and player wins, which will be shown in the codes later
        int foe_fainted_pokemons = 0;
        System.out.println("You sent out " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]"); //display the information of player sending out pokemon and the pokemon's level
        System.out.println(opponent + " sent out " + foe_pokemon.findname() + "[ level " + foe_pokemon.findlvl() + " ]"); //display the information of opponent sending out pokemon and the pokemon's level
        System.out.println(your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ] [ " + your_pokemon.findcurrenthp() + "/" + your_pokemon.findmaxhp() + " ] "); //display the basic information of the player's pokemon in battle
        System.out.println("VS");//display a VS
        System.out.println(foe_pokemon.findname() + "[ level " + foe_pokemon.findlvl() + " ] [ " + foe_pokemon.findcurrenthp() + "/" + foe_pokemon.findmaxhp() + " ] ");//display the basic information of the opponents pokemon in battle
        resetstat(); //reset all stats of both pokemons
        System.out.println("");
        System.out.println("Tips: "); //show tips of opponent's pokemon's weakness and resistance
        foe_pokemon.showWeakness(); //a method in Pokemon class that displays a pokemon's weakness
        foe_pokemon.showResistance(); // a method in Pokemon class that shown a pokemon's resistance
        System.out.println("");
        Scanner input = new Scanner(System.in); //declare and initialize a Scanner objact to be used later
        all: //label the loop so that it could be ended by the codes in it when necessary
        while(!player.teamfaint()||foe_fainted_pokemons!=opponent_pokemons.size()){ //check if all of the player's pokemons are fainted or opponent's pokemons are fainted
            boolean foe_faint = false; //a boolean that symbolizes the opponent's pokemon in battle is fainted or not
            boolean your_faint = false; // a boolean that symbolizes the player's pokemon in battle is fainted or not
            boolean move = false; //a boolean that checks if a player chose a pokemon's move or not in a turn
            Move your_move = null; //the Move object of the player's pokemon is declared initialized to null, if player uses a pokemon a move, the your_move will be set to the move used
            boolean alter_pokemons = false; // a boolean that checks if a player switches a pokemon or not in a turn
            boolean bag_items = false; // a boolean that checks if a player uses an item in a turn or not
            //the decision loop, when a decision that will end the player's turn is not made, the loop will not end
            decision:
            while(true){

                //-move -- choose a move to use, ends the decision loop if a move is chosen
                //-pokemon -- check all pokemons in the team, can be used to check the status of all pokemons in the team, use items on them, or switch them into battle,
                //            if switch is done or a pokemon is switched into battle, then end the decision loop
                //-bag -- display the items in the bag of the player, player can choose an item to use or not, if an item is used, then end the decision loop
                //check status -- check the status of the pokemons,will not end decision loop
                //-run -- apparently player cannot run from player battles, this is just for fun and salute to the original game will not end decision loop
                System.out.printf("+%s+\n","-".repeat(90)); //display the basic informations about both pokemons every loop
                System.out.println(your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ] [ " + your_pokemon.findcurrenthp() + "/" + your_pokemon.findmaxhp() + " ] ");
                System.out.println("VS");
                System.out.println(foe_pokemon.findname() + "[ level " + foe_pokemon.findlvl() + " ] [ " + foe_pokemon.findcurrenthp() + "/" + foe_pokemon.findmaxhp() + " ] ");
                System.out.println("1. Fight"); //display all the choices that the playe can choose
                System.out.println("2. Pokemons");
                System.out.println("3. Bag");
                System.out.println("4. Check status");
                System.out.println("5. Run");
                System.out.println("Your move(Choose 1-5)");
                String choice_st = input.nextLine(); //receive input
                if(isNum(choice_st)){ //check whether the string input can be converted to integer, by this way, the program will not end even when invalid inputs are accepted
                    int choice = Integer.parseInt(choice_st);
                    switch(choice){
                        case 1:
                            your_move = chooseMove(); //call chooseMove() method that will return a Move object if a move is chosen
                            if(your_move!=null){
                                move = true; //if a move is made, then set boolean move to true
                                break decision; //and break the decision loop
                            }
                            break;
                        case 2:
                            alter_pokemons = pokemons(); //call pokemons() method that will return a true value if a pokemon is switched out or an item is used on a pokemon
                            if(alter_pokemons){ //when alter_pokemons is true, then break the decision loop
                                break decision;
                            }
                            break;
                        case 3:
                            bag_items = bag(); //call bag() method that will return a true value if an item in the bag is used
                            if(bag_items){ //when bag_items is true, break the decision loop
                                break decision;
                            }
                        case 4:
                            //display all stats of both pokemon
                            System.out.println("Your atk: " + atk);
                            System.out.println("Your def: " + def);
                            System.out.println("Your sp: " + sp);
                            System.out.println("Foe atk: " + foe_atk);
                            System.out.println("Foe def: " + foe_def);
                            System.out.println("Foe sp: " + foe_sp);
                            break;
                        case 5:
                            //display a player could not run away from trainer battle message
                            System.out.println("There is no running away from a true battle between trainers!");
                            break;
                        default:
                            //pop invalid choice mssage if the input does not fulfill the format or number as a valid choice, same thing will occur everytime an invalid input is accepted
                            System.out.println("Invalid choice! Please choose again");
                    }
                }else{
                    System.out.println("Invalid choice! Please choose again.");
                }
            }

            //enemy decision
            Move foe_move = null; //declare a Move object of the opponent's pokemon's move
            int foe_decs = r.nextInt(4)+1; //the move will be randomly chosen from the four moves the opponent's pokemon
            switch(foe_decs){
                case 1: //if the random integer generated is 1, then use move 1, if it is 2, then use move 2 etc
                    foe_move = new Move(foe_pokemon.findmov1(),foe_pokemon.findlvl());
                    break;
                case 2:
                    foe_move = new Move(foe_pokemon.findmov2(),foe_pokemon.findlvl());
                    break;
                case 3:
                    foe_move = new Move(foe_pokemon.findmov3(),foe_pokemon.findlvl());
                    break;
                case 4:
                    foe_move = new Move(foe_pokemon.findmov4(),foe_pokemon.findlvl());
                    break;
            }


            //if swap and use items then your pokemon cannot move
            if(alter_pokemons||bag_items){
                opponentmove(foe_move); //only the opponent can move
            }else if(move){ //if player chose a pokemon move
                //calculate move order, every move has its own move order, with some move faster than the other moves
                int your_move_order = your_move.getOrder(); //when a move is declared and initialized using a constructor, the move order is set, by using getOrder() method, the move order can be obtained
                int foe_move_order = foe_move.getOrder();
                //calculate speed, if move order is same, the pokemon with higher speed will move first
                double your_speed = your_pokemon.findspeed(); //get the speed value of both pokemons using findspeed() method in pokemon class
                double foe_speed = foe_pokemon.findspeed();
                double sp_rate = 1; //declare and initialize sp_rate and foe_sp_rate which is the rate of multiplication of both pokemons' speed to obtain the temporal speed in battle
                double foe_sp_rate = 1;
                //sp_rate and foe_sp rate will be altered by sp stat and foe_sp stat
                //the formula of calculation is like this:
                //if sp stat>0
                //sp_rate = (2+n)/2
                //if sp stat<0
                //sp_rate = 2/(-n+2)
                //with n being the value of the sp stat and same goes with foe_sp_rate using foe_sp stat
                if(sp>0){
                    sp_rate = (1.0*(2+sp))/(2*1.0);
                }else if(sp<0){
                    sp_rate = (1.0*2)/(2+(-1*sp));
                }

                if(foe_sp>0){
                    foe_sp_rate = (1.0*(2+foe_sp))/(2*1.0);
                }else if(foe_sp<0){
                    foe_sp_rate = (1.0*2)/(2+(-1*foe_sp));
                }
                //after obtaining the rates, multiply them with pokemons' own speed, obtaining the temporal speed to determine who moves first if move order is the same
                your_speed = your_speed*sp_rate;
                foe_speed = foe_speed*foe_sp_rate;

                //determine who moves first
                if(your_move_order>foe_move_order){ //if player's pokemon's move order is higher
                    //player pokemon move first
                    yourmove(your_move);
                    if(foe_pokemon.isFaint()){ //if opponent pokemon is fainted, then set foe_faint to true, while opponent's battling pokemon cannot perform the move
                        foe_faint = true;
                    }else{ //if opponent's pokemon is not fainted after player's pokemon's move, then it gets to move after player's pokemon
                        opponentmove(foe_move);
                    }
                }else if(foe_move_order>your_move_order){ //vice versa of last case
                    //foe move first
                    opponentmove(foe_move);
                    if(your_pokemon.isFaint()){ //if player's pokemon is fainted, set your_faint to true
                        your_faint = true;
                    }else{
                        yourmove(your_move);
                    }
                }else{ //same move order
                    if(your_speed>foe_speed){ //if player's pokemon's speed is higher, same thing as if player's pokemons's move order is higher
                        //player move first
                        yourmove(your_move);
                        if(foe_pokemon.isFaint()){
                            foe_faint = true;
                        }else{
                            opponentmove(foe_move);
                        }
                    }else if(foe_speed>your_speed){ //vice versa of last case
                        //foe move first
                        opponentmove(foe_move);
                        if(your_pokemon.isFaint()){
                            your_faint = true;
                        }else{
                            yourmove(your_move);
                        }
                    }else{ //same speed
                        int rando = r.nextInt(2); //coin flip to determine who move first 50 50
                        if(rando == 0){ //if coin flip says player's pokemon move first, same thing as player's pokemon's speed is faster
                            //player move first
                            yourmove(your_move);
                            if(foe_pokemon.isFaint()){
                                foe_faint = true;
                            }else{
                                opponentmove(foe_move);
                            }
                        }else{ //vice versa of last case
                            //foe move first
                            opponentmove(foe_move);
                            if(your_pokemon.isFaint()){
                                your_faint = true;
                            }else{
                                yourmove(your_move);
                            }
                        }
                    }
                }




            }

            if(foe_faint||foe_pokemon.isFaint()){ //foe's pokemon is fainted
                your_pokemon.obtainxp(foe_pokemon.findlvl()*5); //player's pokemon in battle will earn xp based on the level of the pokemon it defeated times 5
                foe_fainted_pokemons++; //increase the counter of opponent's fainted pokemon by 1
                if(foe_fainted_pokemons!=opponent_pokemons.size()){ //check if the counter of opponent's whole team is fainted
                    foe_pokemon = opponent_pokemons.get(foe_fainted_pokemons); //if not, then opponent switches to next pokemon, using the counter to track the index of the next pokemon in the ArrayList
                    System.out.printf("+%s+\n","-".repeat(90));
                    System.out.println(opponent + " sent out " + foe_pokemon.findname() + "[ level " + foe_pokemon.findlvl() + " ]"); //display message of opponent sending out pokemon
                    System.out.println("");
                    System.out.println("Tips: "); //display weaknesses and resistance of the opponent's pokemon
                    foe_pokemon.showWeakness();
                    foe_pokemon.showResistance();
                    System.out.println("");
                    resetfoestatus(); //reset all the stats of opponent's pokemon as opponent switched in a new pokemon
                }else{ //if all the opponent's pokemon is fainted
                    System.out.printf("+%s+\n","-".repeat(90));
                    System.out.println("Congratulations! You have defeated " + opponent + ", you earned $ " + library.TrainerReward.get(opponent)); //display message of winning the battle
                    player.addMoney(library.TrainerReward.get(opponent)); //player obtains money according to trainer, this value can be obtained from the library.TrainerReward Hashmap using opponent's name as key
                    resetfoestatus(); //reset all the stats of opponent's pokemon status
                    win = true; //set win to true
                    player.wonbattle(); //wonbattle method increases the battleswon value in the Player object by 1
                    break all; //end the whole loop
                }
            }



            if(your_faint||your_pokemon.isFaint()){ //if player's pokemon is fainted
                if(player.teamfaint()){ //if all pokemon's in player's team is fainted, end the whole loop and display player has whited out message
                    break all;
                }else{//if there are still pokemon not fainted
                    switchpoke: //the switchpoke loop will never end if the player does not switch to next pokemon that is not fainted
                    while(true){
                        player.showteam(); //show the pokemons in player's team
                        System.out.println("Choose a pokemon(1-6) to switch: "); //prompt player to choose which pokemon to switch
                        String choice_st = input.nextLine(); // receive player input
                        if(isNum(choice_st)){ //check if input is correct format
                            int choice = Integer.parseInt(choice_st);//change input into integer
                            switch(choice){
                                case 1: //pokemon1 is always not null as mentioned before
                                    if(player.findPoke1().equals(your_pokemon)){ //check if the pokemon chosen is the pokemon in battle or not
                                        System.out.println("This pokemon is already in battle and has fainted!");//if yes, display already in battle and fainted message
                                    }else if(player.findPoke1().isFaint()){ //if pokemon chosen is not in battle, but has already fainted
                                        System.out.println("This pokemon has already fainted!");//display is fainted message
                                    }else{ //pokemon1 is not in battle and not fainted
                                        System.out.print("You withdrew " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]"); //display switching pokemon message
                                        your_pokemon.changebattlestatus(false); //change last pokemon's battle status to false
                                        your_pokemon = player.findPoke1(); //switch pokemon1 to pokemon in battle
                                        your_pokemon.changebattlestatus(true); // change pokemon in battle, which is pokemon1's battle status to true
                                        System.out.println(" and sent out " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                        resetyourstatus(); //reset player's pokemon in battle status
                                        break switchpoke; // end switchpoke loop
                                    }
                                    break;
                                case 2:
                                    if(player.findPoke2()!=null){ //check if pokemon2 exists
                                        if(player.findPoke2().equals(your_pokemon)){ //same thing that happens when pokemon1 is chosen happens onto pokemon2,check if pokemon2 is already in battle,is fainted or not, if not switch out
                                            System.out.println("This pokemon is already in battle and has fainted!");
                                        }else if(player.findPoke2().isFaint()){
                                            System.out.println("This pokemon has already fainted!");
                                        }else{
                                            System.out.print("You withdrew " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                            your_pokemon.changebattlestatus(false);
                                            your_pokemon = player.findPoke2();
                                            your_pokemon.changebattlestatus(true);
                                            System.out.println(" and sent out " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                            resetyourstatus();
                                            break switchpoke;
                                        }
                                    }else{//if pokemon2 does not exist, then display slot is empty message
                                        System.out.println("This pokemon slot is empty");
                                    }
                                    break;
                                case 3: //everything that happens when pokemon2 is chosen will happen onto pokemon3-6 when they are chosen
                                    if(player.findPoke3()!=null){
                                        if(player.findPoke3().equals(your_pokemon)){
                                            System.out.println("This pokemon is already in battle and has fainted!");
                                        }else if(player.findPoke3().isFaint()){
                                            System.out.println("This pokemon has already fainted!");
                                        }else{
                                            System.out.print("You withdrew " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                            your_pokemon.changebattlestatus(false);
                                            your_pokemon = player.findPoke3();
                                            your_pokemon.changebattlestatus(true);
                                            System.out.println(" and sent out " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                            resetyourstatus();
                                            break switchpoke;
                                        }
                                    }else{
                                        System.out.println("This pokemon slot is empty");
                                    }
                                    break;
                                case 4:
                                    if(player.findPoke4()!=null){
                                        if(player.findPoke4().equals(your_pokemon)){
                                            System.out.println("This pokemon is already in battle and has fainted!");
                                        }else if(player.findPoke4().isFaint()){
                                            System.out.println("This pokemon has already fainted!");
                                        }else{
                                            System.out.print("You withdrew " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                            your_pokemon.changebattlestatus(false);
                                            your_pokemon = player.findPoke4();
                                            your_pokemon.changebattlestatus(true);
                                            System.out.println(" and sent out " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                            resetyourstatus();
                                            break switchpoke;
                                        }
                                    }else{
                                        System.out.println("This pokemon slot is empty");
                                    }
                                    break;
                                case 5:
                                    if(player.findPoke5()!=null){
                                        if(player.findPoke5().equals(your_pokemon)){
                                            System.out.println("This pokemon is already in battle and has fainted!");
                                        }else if(player.findPoke5().isFaint()){
                                            System.out.println("This pokemon has already fainted!");
                                        }else{
                                            System.out.print("You withdrew " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                            your_pokemon.changebattlestatus(false);
                                            your_pokemon = player.findPoke5();
                                            your_pokemon.changebattlestatus(true);
                                            System.out.println(" and sent out " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                            resetyourstatus();
                                            break switchpoke;
                                        }
                                    }else{
                                        System.out.println("This pokemon slot is empty");
                                    }
                                    break;
                                case 6:
                                    if(player.findPoke6()!=null){
                                        if(player.findPoke6().equals(your_pokemon)){
                                            System.out.println("This pokemon is already in battle and has fainted!");
                                        }else if(player.findPoke6().isFaint()){
                                            System.out.println("This pokemon has already fainted!");
                                        }else{
                                            System.out.print("You withdrew " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                            your_pokemon.changebattlestatus(false);
                                            your_pokemon = player.findPoke6();
                                            your_pokemon.changebattlestatus(true);
                                            System.out.println(" and sent out " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                            resetyourstatus();
                                            break switchpoke;
                                        }
                                    }else{
                                        System.out.println("This pokemon slot is empty");
                                    }
                                    break;
                                default: //if player enters invalid choice then display invalid choice message
                                    System.out.println("Invalid choice! Please choose again!");
                            }
                        }else{
                            System.out.println("Invalid choice! Please choose again!");
                        }
                    }

                }
            }
        }
    }
    public boolean getwin(){ //a method that returns the win value
        return win;
    }
    public Battle(Player p,Pokemon wild){ //battle constructor for wild pokemons
        //almost everthing in this constructor is the same with the constructor in trainer battles, we just do not have check if there is any pokemon's left when the wild pokemon is fainted and can run away
        Random r = new Random(); //declare a random objact to be used later
        player = p; //same explanation as another constructor for this line
        //for these if else if statements, the explanation is as that in the another constructor
        if(!p.findPoke1().isFaint()){ //pokemon1 will never be null, as player will choose a starter pokemon when a new Save is created.
            your_pokemon = p.findPoke1(); //p is player, findPoke1 will return the Pokemon object that contains the data of the pokemon in the first slot, same for findPoke2-6
        }else if(p.findPoke2()!=null&&!p.findPoke2().isFaint()){
            your_pokemon = p.findPoke2();
        }else if(p.findPoke3()!=null&&!p.findPoke3().isFaint()){
            your_pokemon = p.findPoke3();
        }else if(p.findPoke4()!=null&&!p.findPoke4().isFaint()){
            your_pokemon = p.findPoke4();
        }else if(p.findPoke5()!=null&&!p.findPoke5().isFaint()){
            your_pokemon = p.findPoke5();
        }else if(p.findPoke6()!=null){ //at least last pokemon is not fainted
            your_pokemon = p.findPoke6();
        }
        your_pokemon.changebattlestatus(true);//change layer's pokemon's battle status to true
        System.out.println("You sent out " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]"); //display player sent ou pokemon message
        foe_pokemon = wild;
        //display basic information of both pokemons
        System.out.println(your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ] [ " + your_pokemon.findcurrenthp() + "/" + your_pokemon.findmaxhp() + " ] ");
        System.out.println("VS");
        System.out.println(foe_pokemon.findname() + "[ level " + foe_pokemon.findlvl() + " ] [ " + foe_pokemon.findcurrenthp() + "/" + foe_pokemon.findmaxhp() + " ] ");
        resetstat(); //reset all the stats
        System.out.println("");
        System.out.println("Tips: "); //display weakness and resistance of wild pokemon
        foe_pokemon.showWeakness();
        foe_pokemon.showResistance();
        System.out.println("");
        Scanner input = new Scanner(System.in); //declare Scanner object to be used later
        all:
        while(!player.teamfaint()||foe_pokemon.isFaint()){ //check if the all the player's pokemon is fainted
            //all these variable and object functions as the same as the another constructor but for wild pokemons instead of opponent's pokemon in battle
            boolean foe_faint = false;
            boolean your_faint = false;
            boolean move = false;
            Move your_move = null;
            boolean alter_pokemons = false;
            boolean bag_items = false;
            //the decision loop, when a decision that will end the player's turn is not made, the loop will not end
            decision:
            while(true){
                //-move -- choose a move to use, ends the decision loop if a move is chosen
                //-pokemon -- check all pokemons in the team, can be used to check the status of all pokemons in the team, use items on them, or switch them into battle,
                //            if switch is done or a pokemon is switched into battle, then end the decision loop
                //-bag -- display the items in the bag of the player, player can choose an item to use or not, if an item is used, then end the decision loop
                //check status -- check the status of the pokemons,will not end decision loop
                //-run -- run away from wild pokemon and end the all loop
                System.out.printf("+%s+\n","-".repeat(90));
                System.out.println(your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ] [ " + your_pokemon.findcurrenthp() + "/" + your_pokemon.findmaxhp() + " ] ");
                System.out.println("VS");
                System.out.println(foe_pokemon.findname() + "[ level " + foe_pokemon.findlvl() + " ] [ " + foe_pokemon.findcurrenthp() + "/" + foe_pokemon.findmaxhp() + " ] ");
                System.out.println("1. Fight");
                System.out.println("2. Pokemons");
                System.out.println("3. Bag");
                System.out.println("4. Check status");
                System.out.println("5. Run");
                System.out.println("Your move(Choose 1-5)");
                String choice_st = input.nextLine(); //receive user input
                if(isNum(choice_st)){ //check input format
                    int choice = Integer.parseInt(choice_st);//turn input into integer
                    switch(choice){
                        case 1:
                            your_move = chooseMove(); //same thing as another constructor
                            if(your_move!=null){
                                move = true;
                                break decision;
                            }
                            break;
                        case 2:
                            alter_pokemons = pokemons(); //same thing as another constructor
                            if(alter_pokemons){
                                break decision;
                            }
                            break;
                        case 3:
                            bag_items = bag(); //same thing as another constructor
                            if(bag_items){
                                break decision;
                            }
                        case 4: //display stats
                            System.out.println("Your atk: " + atk);
                            System.out.println("Your def: " + def);
                            System.out.println("Your sp: " + sp);
                            System.out.println("Foe atk: " + foe_atk);
                            System.out.println("Foe def: " + foe_def);
                            System.out.println("Foe sp: " + foe_sp);
                            break;
                        case 5: //display run away message and break all loop
                            System.out.println("You ran away safely!");
                            break all;
                        default: //display invalid choice message if invalid choice in entered
                            System.out.println("Invalid choice! Please choose again");
                    }
                }else{
                    System.out.println("Invalid choice! Please choose again.");
                }
            }

            //enemy decision
            Move foe_move = null; //wild pokemon's move is decided the same way as opponent's pokemon's move in the another constructor
            int foe_decs = r.nextInt(4)+1;
            switch(foe_decs){
                case 1:
                    foe_move = new Move(foe_pokemon.findmov1(),foe_pokemon.findlvl());
                    break;
                case 2:
                    foe_move = new Move(foe_pokemon.findmov2(),foe_pokemon.findlvl());
                    break;
                case 3:
                    foe_move = new Move(foe_pokemon.findmov3(),foe_pokemon.findlvl());
                    break;
                case 4:
                    foe_move = new Move(foe_pokemon.findmov4(),foe_pokemon.findlvl());
                    break;
            }
            //if wild pokemon is caught by any type of Poke Ball, as wild pokemons can be catched unlike trainer's pokemons
            //break the all loop and end battle
            if(!foe_pokemon.wild()){
                break all;
            }

            //if swap and use items then your pokemon cannot move, same thing as another constructor, only change is opponent's pokemon to wild pokemon
            if(alter_pokemons||bag_items){
                opponentmove(foe_move);
            }else if(move){
                //calculate move order, same thing as another constructor, only change is opponent's pokemon to wild pokemon
                int your_move_order = your_move.getOrder();
                int foe_move_order = foe_move.getOrder();
                //calculate speed, same thing as another constructor, only change is opponent's pokemon to wild pokemon
                double your_speed = your_pokemon.findspeed();
                double foe_speed = foe_pokemon.findspeed();
                double sp_rate = 1;
                double foe_sp_rate = 1;
                if(sp>0){
                    sp_rate = (1.0*(2+sp))/(2*1.0);
                }else if(sp<0){
                    sp_rate = (1.0*2)/(2+(-1*sp));
                }

                if(foe_sp>0){
                    foe_sp_rate = (1.0*(2+foe_sp))/(2*1.0);
                }else if(foe_sp<0){
                    foe_sp_rate = (1.0*2)/(2+(-1*foe_sp));
                }
                your_speed = your_speed*sp_rate;
                foe_speed = foe_speed*foe_sp_rate;

                //determine who moves first, same thing as another constructor, only change is opponent's pokemon to wild pokemon
                if(your_move_order>foe_move_order){
                    //player move first
                    yourmove(your_move);
                    if(foe_pokemon.isFaint()){
                        foe_faint = true;
                    }else{
                        opponentmove(foe_move);
                    }
                }else if(foe_move_order>your_move_order){
                    //foe move first
                    opponentmove(foe_move);
                    if(your_pokemon.isFaint()){
                        your_faint = true;
                    }else{
                        yourmove(your_move);
                    }
                }else{ //same move order
                    if(your_speed>foe_speed){
                        //player move first
                        yourmove(your_move);
                        if(foe_pokemon.isFaint()){
                            foe_faint = true;
                        }else{
                            opponentmove(foe_move);
                        }
                    }else if(foe_speed>your_speed){
                        //foe move first
                        opponentmove(foe_move);
                        if(your_pokemon.isFaint()){
                            your_faint = true;
                        }else{
                            yourmove(your_move);
                        }
                    }else{ //same speed
                        int rando = r.nextInt(2); //coin flip to determine who move first 50 50
                        if(rando == 0){
                            //player move first
                            yourmove(your_move);
                            if(foe_pokemon.isFaint()){
                                foe_faint = true;
                            }else{
                                opponentmove(foe_move);
                            }
                        }else{
                            //foe move first
                            opponentmove(foe_move);
                            if(your_pokemon.isFaint()){
                                your_faint = true;
                            }else{
                                yourmove(your_move);
                            }
                        }
                    }
                }




            }
            //if the wild is fainted
            if(foe_faint||foe_pokemon.isFaint()){
                your_pokemon.obtainxp(foe_pokemon.findlvl()*5); //then player's pokemon obtains xp according to the wild pokemon's level
                break all; //break the all loop
            }

            if(your_faint||your_pokemon.isFaint()){ //if player's pokemon is fainted
                if(player.teamfaint()){ //check if all pokemon in player's team is fainted, if yes display player white out message and end the all loop and battle
                    break all;
                }else{ //if the player still have pokemon not fainted
                    System.out.println("Do you want to switch pokemon(y) or run away(any other input)?"); //check if player wants to choose another pokemon to fight, or just run away as the wild pokemon might be too strong
                    String choice_sr = input.nextLine(); //choice to switch or run
                    if(choice_sr.equals("y")){ //if choice is yes
                        switchpoke: //then start the switchpoke loop
                        while(true){
                            player.showteam();
                            System.out.println("Choose a pokemon(1-6)/7 to run: "); //same thing as another constructor if palyer enters 1-6, the only difference is if player enters 7 they can cancel the switch and just run away
                            String choice_st = input.nextLine();
                            if(isNum(choice_st)){
                                int choice = Integer.parseInt(choice_st);
                                switch(choice){
                                    case 1:
                                        if(player.findPoke1().equals(your_pokemon)){
                                            System.out.println("This pokemon is already in battle and has fainted!");
                                        }else if(player.findPoke1().isFaint()){
                                            System.out.println("This pokemon has already fainted!");
                                        }else{
                                            System.out.print("You withdrew " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                            your_pokemon.changebattlestatus(false);
                                            your_pokemon = player.findPoke1();
                                            your_pokemon.changebattlestatus(true);
                                            System.out.println(" and sent out " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                            resetyourstatus();
                                            break switchpoke;
                                        }
                                        break;
                                    case 2:
                                        if(player.findPoke2()!=null){
                                            if(player.findPoke2().equals(your_pokemon)){
                                                System.out.println("This pokemon is already in battle and has fainted!");
                                            }else if(player.findPoke2().isFaint()){
                                                System.out.println("This pokemon has already fainted!");
                                            }else{
                                                System.out.print("You withdrew " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                                your_pokemon.changebattlestatus(false);
                                                your_pokemon = player.findPoke2();
                                                your_pokemon.changebattlestatus(true);
                                                System.out.println(" and sent out " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                                resetyourstatus();
                                                break switchpoke;
                                            }
                                        }else{
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 3:
                                        if(player.findPoke3()!=null){
                                            if(player.findPoke3().equals(your_pokemon)){
                                                System.out.println("This pokemon is already in battle and has fainted!");
                                            }else if(player.findPoke3().isFaint()){
                                                System.out.println("This pokemon has already fainted!");
                                            }else{
                                                System.out.print("You withdrew " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                                your_pokemon.changebattlestatus(false);
                                                your_pokemon = player.findPoke3();
                                                your_pokemon.changebattlestatus(true);
                                                System.out.println(" and sent out " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                                resetyourstatus();
                                                break switchpoke;
                                            }
                                        }else{
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 4:
                                        if(player.findPoke4()!=null){
                                            if(player.findPoke4().equals(your_pokemon)){
                                                System.out.println("This pokemon is already in battle and has fainted!");
                                            }else if(player.findPoke4().isFaint()){
                                                System.out.println("This pokemon has already fainted!");
                                            }else{
                                                System.out.print("You withdrew " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                                your_pokemon.changebattlestatus(false);
                                                your_pokemon = player.findPoke4();
                                                your_pokemon.changebattlestatus(true);
                                                System.out.println(" and sent out " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                                resetyourstatus();
                                                break switchpoke;
                                            }
                                        }else{
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 5:
                                        if(player.findPoke5()!=null){
                                            if(player.findPoke5().equals(your_pokemon)){
                                                System.out.println("This pokemon is already in battle and has fainted!");
                                            }else if(player.findPoke5().isFaint()){
                                                System.out.println("This pokemon has already fainted!");
                                            }else{
                                                System.out.print("You withdrew " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                                your_pokemon.changebattlestatus(false);
                                                your_pokemon = player.findPoke5();
                                                your_pokemon.changebattlestatus(true);
                                                System.out.println(" and sent out " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                                resetyourstatus();
                                                break switchpoke;
                                            }
                                        }else{
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 6:
                                        if(player.findPoke6()!=null){
                                            if(player.findPoke6().equals(your_pokemon)){
                                                System.out.println("This pokemon is already in battle and has fainted!");
                                            }else if(player.findPoke6().isFaint()){
                                                System.out.println("This pokemon has already fainted!");
                                            }else{
                                                System.out.print("You withdrew " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                                your_pokemon.changebattlestatus(false);
                                                your_pokemon = player.findPoke6();
                                                your_pokemon.changebattlestatus(true);
                                                System.out.println(" and sent out " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                                resetyourstatus();
                                                break switchpoke;
                                            }
                                        }else{
                                            System.out.println("This pokemon slot is empty");
                                        }
                                        break;
                                    case 7: //if player enter 7 to run away, display run away safely message
                                        System.out.println("You ran away safely!");
                                        break all;
                                    default:
                                        System.out.println("Invalid choice! Please choose again!");
                                }
                            }else{
                                System.out.println("Invalid choice! Please choose again!");
                            }
                        }
                    }else{ //if player does not enter y to run away, display run away safely message
                        System.out.println("You ran away safely!");
                        break all;
                    }
                }
            }
        }
    }
    public boolean isNum(String s){ //a method to check if the input can be turned into integer or not
        try{
            int ss = Integer.parseInt(s);
            return true;
        }catch(NumberFormatException nfe){
            return false;
        }
    }
    public void resetstat(){ // a method to reset all stats
        atk = 0;
        def = 0;
        sp = 0;
        foe_atk = 0;
        foe_def = 0;
        foe_sp = 0;
    }
    public void resetyourstatus(){ //a method to reset only the player's pokemon's stats
        atk = 0;
        def = 0;
        sp = 0;
    }
    public void resetfoestatus(){ //a method to reset only the opponent's or wild pokemon's stats
        foe_atk = 0;
        foe_def = 0;
        foe_sp = 0;
    }
    public Move chooseMove(){ //a method that returns a Move object if the player choses one
        Scanner input = new Scanner(System.in); //declare a Scanner object to be used later
        while(true){ //while the player does not make a decision, this loop will not end
            //display all the moves of the pokemon, their category, their type and power if have
            System.out.printf("+%s+\n","-".repeat(90));
            System.out.println("1. " + your_pokemon.findmov1() + " [ " + library.move_cat.get(your_pokemon.findmov1()) + " move ]");
            Move move1 = new Move(your_pokemon.findmov1(),your_pokemon.findlvl()); //declare and initialize a Move object using Move constuctor, same for the other three moves, so that every one of them could be returned if chosen
            if(move1.getPower()!=0){
                System.out.println("type: " + move1.getType());
                System.out.println("power: " + move1.getPower());
            }
            System.out.println("2. " + your_pokemon.findmov2() + " [ " + library.move_cat.get(your_pokemon.findmov2()) + " move ]");
            Move move2 = new Move(your_pokemon.findmov2(),your_pokemon.findlvl());
            if(move2.getPower()!=0){
                System.out.println("type: " + move2.getType());
                System.out.println("power: " + move2.getPower());
            }
            System.out.println("3. " + your_pokemon.findmov3() + " [ " + library.move_cat.get(your_pokemon.findmov3()) + " move ]");
            Move move3 = new Move(your_pokemon.findmov3(),your_pokemon.findlvl());
            if(move3.getPower()!=0){
                System.out.println("type: " + move3.getType());
                System.out.println("power: " + move3.getPower());
            }
            System.out.println("4. " + your_pokemon.findmov4() + " [ " + library.move_cat.get(your_pokemon.findmov4()) + " move ]");
            Move move4 = new Move(your_pokemon.findmov4(),your_pokemon.findlvl());
            if(move4.getPower()!=0){
                System.out.println("type: " + move4.getType());
                System.out.println("power: " + move4.getPower());
            }
            System.out.println("5. Back"); //give option to not choose any move and go back
            System.out.println("Choose a move to use(1-4) or 5 to go back to previous selection page"); //prompt user to enter choice
            String choice_st = input.nextLine(); //receive user choice input
            if(isNum(choice_st)){ //check choice input format
                int choice = Integer.parseInt(choice_st); //turn choice input into integer
                switch(choice){
                    //return Move object according to choice
                    case 1:
                        return move1;
                    case 2:
                        return move2;
                    case 3:
                        return move3;
                    case 4:
                        return move4;
                    case 5:
                        return null;
                }
            }else{
                System.out.println("Invalid choice! Please choose again!");
            }
        }
    }
    public boolean pokemons(){ //a method that returns true if player swithes pokemon or uses an item on a pokemon
        Scanner input = new Scanner(System.in); //declare Scanner object
        all: //the loop will not end until player swithes pokemon or uses an item on a pokemon or choose to go back
        while(true){
            boolean decision_made = false; //this boolean variable will be returned when the method ends
            player.showteam(); //display all pokemons in the team
            System.out.println("Choose a pokemon(1-6)/7 to exit: "); //prompt player to enter choice
            String choice_st = input.nextLine(); //receive input
            if(isNum(choice_st)){ //check input format
                int choice = Integer.parseInt(choice_st); //change choice input into integer
                switch(choice){
                    case 1: //pokemon 1 is chosen
                        decision_made = pokechoice(player.findPoke1(),1); //call pokechoice method that will return true if an action is made on pokemon 1, this method receives a pokemon object and an integer value representing the pokemon's position in the team
                        if(decision_made){
                            return decision_made;
                        }
                        break;
                    case 2:
                        if(player.findPoke2()==null){ //check if pokemon 2 exists or not
                            System.out.printf("+%s+\n","-".repeat(90));
                            System.out.println("There is no pokemon in this slot");
                        }else{ //if exists, then call pokechoice but using pokemon2 and 2 as the parameters
                            decision_made = pokechoice(player.findPoke2(),2);
                            if(decision_made){
                                return decision_made;
                            }
                        }
                        break;
                    case 3: //same thing happens to pokemon2 will happen to pokemon3-6 is they were chosen
                        if(player.findPoke3()==null){
                            System.out.printf("+%s+\n","-".repeat(90));
                            System.out.println("There is no pokemon in this slot");
                        }else{
                            decision_made = pokechoice(player.findPoke3(),3);
                            if(decision_made){
                                return decision_made;
                            }
                        }
                        break;
                    case 4:
                        if(player.findPoke4()==null){
                            System.out.printf("+%s+\n","-".repeat(90));
                            System.out.println("There is no pokemon in this slot");
                        }else{
                            decision_made = pokechoice(player.findPoke4(),4);
                            if(decision_made){
                                return decision_made;
                            }
                        }
                        break;
                    case 5:
                        if(player.findPoke5()==null){
                            System.out.printf("+%s+\n","-".repeat(90));
                            System.out.println("There is no pokemon in this slot");
                        }else{
                            decision_made = pokechoice(player.findPoke5(),5);
                            if(decision_made){
                                return decision_made;
                            }
                        }
                        break;
                    case 6:
                        if(player.findPoke6()==null){
                            System.out.printf("+%s+\n","-".repeat(90));
                            System.out.println("There is no pokemon in this slot");
                        }else{
                            decision_made = pokechoice(player.findPoke6(),6);
                            if(decision_made){
                                return decision_made;
                            }
                        }
                        break;
                    case 7: //if player choose to go back to last selection page, then return false indicating the player's decision is not done
                        return false;
                    default: //if choice input is invalid then display this message
                        System.out.println("Invalid choice! Please choose again|");
                }
            }else{
                System.out.println("Invalid choice! Please choose again!");
            }
        }
    }
    public boolean pokechoice(Pokemon poke,int pos){ //this method will return true if an action is done on the Pokemon object it received
        all: //the all loop will not end if player does not make a valid action on the pokemon or go back to last selection page
        while(true){
            System.out.printf("+%s+\n","-".repeat(90));
            System.out.println(poke.findname()); //show the name of the pokemon
            System.out.println("1. Show details"); //display all the choices
            System.out.println("2. Use items");
            System.out.println("3. Swap");
            System.out.println("4. Back");
            System.out.println("Select one from 1-4: "); //prompt user to enter choice input
            Scanner input = new Scanner(System.in);
            String choice_st = input.nextLine(); //receive choice input
            if(isNum(choice_st)){ //check choice input format
                int choice = Integer.parseInt(choice_st); //turn choice input into integer
                switch(choice){
                    case 1: //player choose the show the pokemon's information
                        poke.showPokemonInfo(); //show pokemon info, and does not break the all loop as it is just checking the information of the pokemon
                        moves://moves loop will not end unless player choose to go back to last selection page
                        while(true){
                            System.out.printf("+%s+\n","-".repeat(90)); //display all the moves of the pokemon
                            System.out.println("1. " + poke.findmov1());
                            System.out.println("2. " + poke.findmov2());
                            System.out.println("3. " + poke.findmov3());
                            System.out.println("4. " + poke.findmov4());
                            System.out.println("Check moves(1-4)?5 to exit: "); //prompt user to enter choice input
                            String choicemove_st = input.nextLine(); //receive choice input
                            if(isNum(choicemove_st)){ //check choice input format
                                int choicemove = Integer.parseInt(choicemove_st); //turn choice input into integer
                                switch(choicemove){
                                    case 1: //display the info of the chosen move, if 1 - 4 is the input
                                        System.out.printf("+%s+\n","-".repeat(90));
                                        Move x1 = new Move(poke.findmov1(),poke.findlvl());
                                        x1.showmovdetail();
                                        break;
                                    case 2:
                                        System.out.printf("+%s+\n","-".repeat(90));
                                        Move x2 = new Move(poke.findmov2(),poke.findlvl());
                                        x2.showmovdetail();
                                        break;
                                    case 3:
                                        System.out.printf("+%s+\n","-".repeat(90));
                                        Move x3 = new Move(poke.findmov3(),poke.findlvl());
                                        x3.showmovdetail();
                                        break;
                                    case 4:
                                        System.out.printf("+%s+\n","-".repeat(90));
                                        Move x4 = new Move(poke.findmov4(),poke.findlvl());
                                        x4.showmovdetail();
                                        break;
                                    case 5: //if user enters 5 to go back
                                        break moves; //end the moves loop
                                    default: //if input is invalid display this message
                                        System.out.println("Invalid choice! Please choose again.");
                                }
                            }else{
                                System.out.println("Invalid choice! Please choose again.");
                            }
                        }
                        break;
                    case 2: //the user choose to use an item
                        itemmm: //the itemmm loop will nt end unless user uses an item or chooose to go back to last selection page
                        while(true){
                            System.out.printf("+%s+\n","-".repeat(90));
                            player.showitems(); //show all the items in the player's bag
                            System.out.println("12. Exit"); //exit choice
                            System.out.println("Select items(1-11)/12 to exit: "); //prompt player to enter choice
                            String choiceitem_st = input.nextLine(); //receive choice
                            if(isNum(choiceitem_st)){//check choice format
                                int choiceitem = Integer.parseInt(choiceitem_st); //turn choice into integer
                                switch(choiceitem){
                                    case 1: //this method's function is supposed to be using an item on the player's chosen pokemon, apparently you cannot use any type of pokeball onto your pokemon as they are already caught and there is no point of doing that
                                        System.out.printf("+%s+\n","-".repeat(90)); //so when Poke Ball, Great Ball and Ultra Ball is chosen, a message will display to indicate that the item has no effect on the pokemon and the itemmm loop will not end
                                        System.out.println("This item has no effect on this pokemon");
                                        break;
                                    case 2:
                                        System.out.printf("+%s+\n","-".repeat(90));
                                        System.out.println("This item has no effect on this pokemon");
                                        break;
                                    case 3:
                                        System.out.printf("+%s+\n","-".repeat(90));
                                        System.out.println("This item has no effect on this pokemon");
                                        break;
                                    case 4:
                                        System.out.printf("+%s+\n","-".repeat(90));
                                        if(player.getItems().get("Potion")!=0){ //if Potion is choosed, then check if the number of the Potions left in the player's bag is 0 or not
                                            if(poke.findcurrenthp()==poke.findmaxhp()||poke.isFaint()){ //if there is still Potions left, then check if the pokemon if at full hp or already fainted
                                                System.out.println("This item has no effect on this pokemon"); //if the pokemon is at full hp or fainted, then potions cannot be used to heal, the potion is not used, the itemmm loop is not ended
                                            }else{ //if pokemon is not at ful hp and is not fainted
                                                System.out.println("You used a Potion on " + poke.findname()); //then display a message to tell the player they have used a potion
                                                poke.heal(library.pokemon_items.get("Potion").get("heal")); //pokemon heals for the amount of healing a Potion can do, which could be found through library.pokemon_items HashMap using "Potion" as the key
                                                //to get a HashMap about Potion's details and use "heal" as the key to get the healing amount
                                                //heal(int h) is a method that increases the pokemon's current hp but not exceeding its max hp
                                                player.deditems("Potion",1); //deditems() is a method to reduce the items in the player's bag by a specific amount
                                                return true; //return true as a decision is made, itemmm loop ends
                                            }

                                        }else{ //if there is no more potion, then display there are no more Potions, itemmm loop does not end
                                            System.out.println("You do not have any Potions left");
                                        }
                                        break;
                                    case 5:
                                        System.out.printf("+%s+\n","-".repeat(90));
                                        if(player.getItems().get("Super Potion")!=0){ //the same thing that happens if Potion is choosed will happen to Super Potion, Hyper Potion and Max Potion, changing the item from potion the the chosen item, and Max Heal will heal the pokemon until full hp instead of a fixed amount
                                            if(poke.findcurrenthp()==poke.findmaxhp()||poke.isFaint()){
                                                System.out.println("This item has no effect on this pokemon");
                                            }else{
                                                System.out.println("You used a Super Potion on " + poke.findname());
                                                poke.heal(library.pokemon_items.get("Super Potion").get("heal"));
                                                player.deditems("Super Potion",1);
                                                return true;
                                            }

                                        }else{
                                            System.out.println("You do not have any Super Potions left");
                                        }

                                        break;
                                    case 6:
                                        System.out.printf("+%s+\n","-".repeat(90));
                                        if(player.getItems().get("Hyper Potion")!=0){
                                            if(poke.findcurrenthp()==poke.findmaxhp()||poke.isFaint()){
                                                System.out.println("This item has no effect on this pokemon");
                                            }else{
                                                System.out.println("You used a Hyper Potion on " + poke.findname());
                                                poke.heal(library.pokemon_items.get("Hyper Potion").get("heal"));
                                                player.deditems("Hyper Potion",1);
                                                return true;
                                            }

                                        }else{
                                            System.out.println("You do not have any Hyper Potions left");
                                        }
                                        break;
                                    case 7:
                                        System.out.printf("+%s+\n","-".repeat(90));
                                        if(player.getItems().get("Max Potion")!=0){
                                            if(poke.findcurrenthp()==poke.findmaxhp()||poke.isFaint()){
                                                System.out.println("This item has no effect on this pokemon");
                                            }else{
                                                System.out.println("You used a Max Potion on " + poke.findname());
                                                poke.fullheal(); //just as mentioned before, Max Potion heals a pokemon to its full hp, fullheal() is a method that increases the amount of current hp to the pokemon's max hp
                                                player.deditems("Max Potion",1);
                                                return true;
                                            }

                                        }else{
                                            System.out.println("You do not have any Max Potions left");
                                        }
                                        break;
                                    case 8:
                                        System.out.printf("+%s+\n","-".repeat(90));
                                        if(player.getItems().get("X Attack")!=0){ //if X Attack, X Defend or X Speed is chosen, check whether there is any left in the player's bag
                                            if(poke.inBattle()){ //if yes, then check if the chosen pokemon is in battle
                                                if(atk==6){ //if chosen pokemon is in battle,then check if the attack stat is at highest value ,which is 6
                                                    System.out.println("Your battling pokemon's attack cannot be raised any higher!"); //if yes, then display a message telling user that the X Attack cannot be used, the itemmm loop does not end
                                                }else{ //if attack stat is not highest
                                                    System.out.println("You used a X Attack!"); //display message an X Attack is used
                                                    System.out.println(poke.findname() + "'s attack rose by 1 stage"); //the battling pokemon's attack increased by one stage
                                                    atk++; //increase the atk value by 1
                                                    player.deditems("X Attack", 1); //reduce the number of X Attack in the players bag by 1
                                                    return true; //return true as a decision is made
                                                }
                                            }else{ //if the pokemon is not in battle, X Attack has no effect on it
                                                System.out.println("This item has no effect on this pokemon");
                                            }
                                        }else{ //if there are no more X Attacks, tell the player
                                            System.out.println("You do not have any X Attacks left");
                                        }
                                        break;
                                    case 9: //same thing that happens on X Attack will happen X Defend or X Speed if one of them is chosen, only changing X Attack and atk to X Defend and def or to X Speed and sp
                                        System.out.printf("+%s+\n","-".repeat(90));
                                        if(player.getItems().get("X Defend")!=0){
                                            if(poke.inBattle()){
                                                if(def==6){
                                                    System.out.println("Your battling pokemon's defense cannot be raised any higher!");
                                                }else{
                                                    System.out.println("You used a X Defend!");
                                                    System.out.println(poke.findname() + "'s defense rose by 1 stage");
                                                    atk++;
                                                    player.deditems("X Defend", 1);
                                                    return true;
                                                }
                                            }else{
                                                System.out.println("This item has no effect on this pokemon");
                                            }
                                        }else{
                                            System.out.println("You do not have any X Defends left");
                                        }
                                        break;
                                    case 10:
                                        System.out.printf("+%s+\n","-".repeat(90));
                                        if(player.getItems().get("X Speed")!=0){
                                            if(poke.inBattle()){
                                                if(sp==6){
                                                    System.out.println("Your battling pokemon's speed cannot be raised any higher!");
                                                }else{
                                                    System.out.println("You used a X Speed!");
                                                    System.out.println(poke.findname() + "'s speed rose by 1 stage");
                                                    atk++;
                                                    player.deditems("X Defend", 1);
                                                    return true;
                                                }
                                            }else{
                                                System.out.println("This item has no effect on this pokemon");
                                            }
                                        }else{
                                            System.out.println("You do not have any X Defends left");
                                        }
                                        break;
                                    case 11:
                                        System.out.printf("+%s+\n","-".repeat(90)); //if Revive is chosen
                                        if(player.getItems().get("Revive")!=0){//check if there is any Revives left in the player's bag
                                            if(poke.isFaint()){  //if yes,the ncheck if the pokemon chosen is fainted
                                                System.out.println("You used a Revive on " + poke.findname()); //if pokemon is fainted, then the Revive is successfully used and display this message
                                                poke.revive(); //revive() is a method that increases the current hp of the pokemon to half of its max hp and changes the fainted attribute in Pokemon object to false ,indicating the pokemon is not fainted anymore
                                                player.deditems("Revive",1); //reduce the number of Revives in the player's bag by 1
                                                return true; //return true as a decision is ,ae
                                            }else{ //if pokemon chosen is not fainted then Revive will have no effect on it, display a meesage to tell the player
                                                System.out.println("This item has no effect on this pokemon");
                                            }
                                        }else{ //if there is no more Revives, tell the player
                                            System.out.println("You do not have any Revive left");
                                        }
                                        break;
                                    case 12: //the user chose to go back to last selection page, end the itemmm loop
                                        break itemmm;
                                    default: //if choice is invalid, display the following message
                                        System.out.println("Invalid choice! Please choose again!");
                                }
                            }else{
                                System.out.println("Invalid choice! Please choose again!");
                            }
                        }
                        break;
                    case 3: //player chose to swap the chosen pokemon with battling pokemon
                        System.out.printf("+%s+\n","-".repeat(90));
                        if(pos==1){  //if the position of the chosen pokemon is 1
                            if(your_pokemon.equals(player.findPoke1())){ //check if the battling pokemon is equal to the pokemon at pokemon slot 1
                                System.out.println("This pokemon is already in battle!"); //if yes then tell the user that this pokemon is already in batle
                            }else if(player.findPoke1().isFaint()){ //if not, but the pokemon is fainted, tell the user the pokemon has already fainted and cannot be swapped into battle
                                System.out.println("This pokemon is already fainted");
                            }else{ //if the battling pokemon is not the chosen pokemon and the chosen pokemon is not fainted
                                System.out.print("You withdrew " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]"); //display message of withdrewing battling pokemon
                                your_pokemon.changebattlestatus(false);//change the battlestatus of the withdrewed pokemon to false, indicating that it is not in battle anymore
                                your_pokemon = player.findPoke1(); //set the battling pokemon to the chosen pokemon
                                your_pokemon.changebattlestatus(true); //set the battlestatus of the new battling pokemon to true
                                System.out.println(" and sent out " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]"); //display message of sending out the chosen pokemon
                                resetyourstatus(); //reset the player's battle pokemon's stats
                                return true;
                            }
                        }else if(pos == 2){ //the same thing happens to pos==1 condition will happen to pos==2 - pos==6 conditions only changing findPoke1() to findPoke2() - findPoke6()
                            if(your_pokemon.equals(player.findPoke2())){
                                System.out.println("This pokemon is already in battle!");
                            }else if(player.findPoke2().isFaint()){
                                System.out.println("This pokemon is already fainted");
                            }else{
                                System.out.print("You withdrew " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                your_pokemon.changebattlestatus(false);
                                your_pokemon = player.findPoke2();
                                your_pokemon.changebattlestatus(true);
                                System.out.println(" and sent out " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                resetyourstatus();
                                return true;
                            }
                        }else if(pos == 3){
                            if(your_pokemon.equals(player.findPoke3())){
                                System.out.println("This pokemon is already in battle!");
                            }else if(player.findPoke3().isFaint()){
                                System.out.println("This pokemon is already fainted");
                            }else{
                                System.out.print("You withdrew " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                your_pokemon.changebattlestatus(false);
                                your_pokemon = player.findPoke3();
                                your_pokemon.changebattlestatus(true);
                                System.out.println(" and sent out " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                resetyourstatus();
                                return true;
                            }
                        }else if(pos == 4){
                            if(your_pokemon.equals(player.findPoke4())){
                                System.out.println("This pokemon is already in battle!");
                            }else if(player.findPoke4().isFaint()){
                                System.out.println("This pokemon is already fainted");
                            }else{
                                System.out.print("You withdrew " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                your_pokemon.changebattlestatus(false);
                                your_pokemon = player.findPoke4();
                                your_pokemon.changebattlestatus(true);
                                System.out.println(" and sent out " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                resetyourstatus();
                                return true;
                            }
                        }else if(pos == 5){
                            if(your_pokemon.equals(player.findPoke5())){
                                System.out.println("This pokemon is already in battle!");
                            }else if(player.findPoke5().isFaint()){
                                System.out.println("This pokemon is already fainted");
                            }else{
                                System.out.print("You withdrew " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                your_pokemon.changebattlestatus(false);
                                your_pokemon = player.findPoke5();
                                your_pokemon.changebattlestatus(true);
                                System.out.println(" and sent out " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                resetyourstatus();
                                return true;
                            }
                        }else if(pos == 6){
                            if(your_pokemon.equals(player.findPoke6())){
                                System.out.println("This pokemon is already in battle!");
                            }else if(player.findPoke6().isFaint()){
                                System.out.println("This pokemon is already fainted");
                            }else{
                                System.out.print("You withdrew " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                your_pokemon.changebattlestatus(false);
                                your_pokemon = player.findPoke6();
                                your_pokemon.changebattlestatus(true);
                                System.out.println(" and sent out " + your_pokemon.findname() + "[ level " + your_pokemon.findlvl() + " ]");
                                resetyourstatus();
                                return true;
                            }
                        }
                        break;
                    case 4: //player chose to go back to last selection page
                        return false; //return false as no decision is made
                    default: //if choice is invalid, the display the message below
                        System.out.println("Invalid choice! Please choose again!");
                }
            }else{
                System.out.println("Invalid choice! Please choose again!");
            }
        }
    }
    public boolean bag(){ //a method that returns true if an item is used
        Scanner input = new Scanner(System.in);
        all://all loop will not end unless an item is used or the player chooses to go back to last selection page
        while(true){
            boolean decision_made = false; //this boolean will be returned when the method ends
            System.out.printf("+%s+\n","-".repeat(90));
            System.out.println("+--------------------Bag--------------------+"); //display all the items in the bag
            player.showitems();
            System.out.println("+----------------End of Bag-----------------+");
            System.out.println("Choose an item(1-11)/12 to exit"); //prompt the player to neter choice
            String choice_st = input.nextLine(); //receive choice
            if(isNum(choice_st)){ //check choice format
                int choice = Integer.parseInt(choice_st); //change choice into integer
                switch(choice){
                    case 1:
                        decision_made = choiceitem("Poke Ball"); //choiceitem(String item) is a method that will return true if the chosen item is used
                        if(decision_made){ //if a decision is made, return true, if decision made == true, then if(decision_made) == if(true) and will return a true
                            return decision_made;
                        }
                        break;
                    case 2: //same thing happens to all other items when choice that symbolizes them is entered
                        decision_made = choiceitem("Great Ball");
                        if(decision_made){
                            return decision_made;
                        }
                        break;
                    case 3:
                        decision_made = choiceitem("Ultra Ball");
                        if(decision_made){
                            return decision_made;
                        }
                        break;
                    case 4:
                        decision_made = choiceitem("Potion");
                        if(decision_made){
                            return decision_made;
                        }
                        break;
                    case 5:
                        decision_made = choiceitem("Super Potion");
                        if(decision_made){
                            return decision_made;
                        }
                        break;
                    case 6:
                        decision_made = choiceitem("Hyper Potion");
                        if(decision_made){
                            return decision_made;
                        }
                        break;
                    case 7:
                        decision_made = choiceitem("Max Potion");
                        if(decision_made){
                            return decision_made;
                        }
                        break;
                    case 8:
                        decision_made = choiceitem("X Attack");
                        if(decision_made){
                            return decision_made;
                        }
                        break;
                    case 9:
                        decision_made = choiceitem("X Defend");
                        if(decision_made){
                            return decision_made;
                        }
                        break;
                    case 10:
                        decision_made = choiceitem("X Speed");
                        if(decision_made){
                            return decision_made;
                        }
                        break;
                    case 11:
                        decision_made = choiceitem("Revive");
                        if(decision_made){
                            return decision_made;
                        }
                        break;
                    case 12://if player enters 12 to go back to last selection page
                        return false; //return false as no decision is made
                    default: //if choice entered is invalid, display the message below
                        System.out.println("Invalid choice! Please choose again");
                }
            }else{
                System.out.println("Invalid choice! Please choose again");
            }
        }
    }

    public boolean choiceitem(String item){ //a method that returns true if the chosen item is used
        System.out.printf("+%s+\n","-".repeat(90));
        Random r = new Random();
        Scanner input = new Scanner(System.in);
        double foe_hp_percentage = 1.0*foe_pokemon.findcurrenthp()/foe_pokemon.findmaxhp(); //get foe_hp_percentage to get the catch_rate for balls later
        switch(item){
            case "Poke Ball": //if Poke Ball is chosen
                System.out.println("An item that can catch a wild pokemon, can be only used in battle, against wild pokemons"); //display info of chosen item
                System.out.println("You have: " + player.getItems().get("Poke Ball")); //display how many this item does the player have
                if(player.getItems().get("Poke Ball")!=0){ //if the number of the item is not 0
                    System.out.println("Do you want to use it(y to use/any other input to not use)?");//then ask player to confirm they want to use it
                    String use = input.nextLine(); //receive choice
                    if(use.equals("y")){ //if choice is y
                        if(foe_pokemon.wild()){ //if the opposing pokemon is a wild pokemon not a trainer's pokemon
                            System.out.println("You used a Poke Ball!"); //the Poke Ball is used and display a message about that
                            int roll = r.nextInt(101); //roll a number
                            int success = 0; //this is the catch rate, initialize the success integer to 0, roll must be lower than this number to successfully catch a pokemon
                            if(foe_hp_percentage<=0.25){ //if wild pokemon's current hp is less than 25% of max hp
                                success = library.pokemon_items.get("Poke Ball").get("pokemon_25%"); //get the catch rate of Poke Ball when wild pokemon's current hp is lower than 25% of max hp
                            }else if(foe_hp_percentage<=0.5){ //if wild pokemon's current hp is between 25 and 50% of max hp
                                success = library.pokemon_items.get("Poke Ball").get("pokemon_50%"); //get the catch rate of Poke Ball when wild pokemon's current hp is between 25 and 50% of max hp
                            }else if(foe_hp_percentage<=0.75){ //if wild pokemon's current hp is between 50 and 75% of max hp
                                success = library.pokemon_items.get("Poke Ball").get("pokemon_75%"); //get the catch rate of Poke Ball when wild pokemon's current hp is between 50 and 75% of max hp
                            }else{ //if wild pokemon's current hp is above 75%
                                success = library.pokemon_items.get("Poke Ball").get("base_catch_rate"); //get the catch rate of PokeBall when wild pokemon's current hp is above 75%
                            }
                            if(success>roll){ //if success is higher than roll, pokemon is successully caught
                                foe_pokemon.caught(); //caught() is a method that changes the wild attribute and battle status of a pokemon to false indicating that it is not wild anymore
                                System.out.println("You caught " + foe_pokemon.findname() + " !"); //display caught message
                                player.addPokemon(foe_pokemon); //add the caught pokemon to player's team or PC

                            }else{ //if success is lower than roll, pokemon is not caught, display the message below
                                System.out.println("Oh no! " + foe_pokemon.findname() + " snapped out of the Poke Ball");
                            }
                            player.deditems("Poke Ball",1);//reduce the number of Poke Ball in the layer's bag by 1
                            return true;
                        }else{ //if player tries to use a Poke Ball on a trainer's pokemon, this will happen
                            System.out.println("You cannot catch another trainer's pokemon!");
                            return false; //return false the as item is not used
                        }
                    }else{ //if player does not want to use the Poke Ball, then go back to last selection page
                        return false;//return false as the item is not used
                    }
                }else{ //if player does not have any Poke Ball then just go back to last selection page
                    return false; //item is not used
                }
            case "Great Ball": //same thing happens to Poke Ball will happen to Great Ball or Ultra Ball if one of them is the chosen item, just change Poke Ball to Great Ball or Ultra Ball, and these two balls have higher catch rate compared to Poke Ball
                System.out.println("An item that can catch a wild pokemon at a high rate, can be only used in battle, against wild pokemons");
                System.out.println("You have: " + player.getItems().get("Great Ball"));
                if(player.getItems().get("Great Ball")!=0){
                    System.out.println("Do you want to use it(y to use/any other input to not use)?");
                    String use = input.nextLine();
                    if(use.equals("y")){
                        if(foe_pokemon.wild()){
                            System.out.println("You used a Great Ball!");
                            int roll = r.nextInt(101);
                            int success = 0;
                            if(foe_hp_percentage<=0.25){
                                success = library.pokemon_items.get("Great Ball").get("pokemon_25%");
                            }else if(foe_hp_percentage<=0.5){
                                success = library.pokemon_items.get("Great Ball").get("pokemon_50%");
                            }else if(foe_hp_percentage<=0.75){
                                success = library.pokemon_items.get("Great Ball").get("pokemon_75%");
                            }else{
                                success = library.pokemon_items.get("Great Ball").get("base_catch_rate");
                            }
                            if(success>roll){
                                foe_pokemon.caught();
                                System.out.println("You caught " + foe_pokemon.findname() + " !");
                                player.addPokemon(foe_pokemon);
                            }else{
                                System.out.println("Oh no! " + foe_pokemon.findname() + " snapped out of the Great Ball");
                            }
                            player.deditems("Great Ball",1);
                            return true;
                        }else{
                            System.out.println("You cannot catch another trainer's pokemon!");
                            return false;
                        }
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            case "Ultra Ball":
                System.out.println("An item that can catch a wild pokemon at a very high rate, can be only used in battle, against wild pokemons");
                System.out.println("You have: " + player.getItems().get("Ultra Ball"));
                if(player.getItems().get("Ultra Ball")!=0){
                    System.out.println("Do you want to use it(y to use/any other input to not use)?");
                    String use = input.nextLine();
                    if(use.equals("y")){
                        if(foe_pokemon.wild()){
                            System.out.println("You used a Ultra Ball!");
                            int roll = r.nextInt(101);
                            int success = 0;
                            if(foe_hp_percentage<=0.25){
                                success = library.pokemon_items.get("Ultra Ball").get("pokemon_25%");
                            }else if(foe_hp_percentage<=0.5){
                                success = library.pokemon_items.get("Ultra Ball").get("pokemon_50%");
                            }else if(foe_hp_percentage<=0.75){
                                success = library.pokemon_items.get("Ultra Ball").get("pokemon_75%");
                            }else{
                                success = library.pokemon_items.get("Ultra Ball").get("base_catch_rate");
                            }
                            if(success>roll){
                                foe_pokemon.caught();
                                System.out.println("You caught " + foe_pokemon.findname() + " !");
                                player.addPokemon(foe_pokemon);
                            }else{
                                System.out.println("Oh no! " + foe_pokemon.findname() + " snapped out of the Ultra Ball");
                            }
                            player.deditems("Ultra Ball",1);
                            return true;
                        }else{
                            System.out.println("You cannot catch another trainer's pokemon!");
                            return false;
                        }
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            case "Potion": //if Potion is chosen
                System.out.println("An item that can heal a pokemon for " + library.pokemon_items.get("Potion").get("heal") + " hp"); //display item information
                System.out.println("You have: " + player.getItems().get("Potion")); //display the number of the item in player's bag
                if(player.getItems().get("Potion")!=0){ //if the number of Poion in player's bag is not 0
                    System.out.printf("+%s+\n","-".repeat(90));
                    System.out.println("You have: " + player.getItems().get("Potion") + " Potions");
                    System.out.println("Do you want to use it(y to use/any other input to not use)?"); //ask the player to confirm the use
                    String use = input.nextLine(); //receive choice
                    if(use.equals("y")){ //if choice is y
                        return healchoice("Potion"); //call healchoice(), a method that receive a healing item's name and make the player choose on which pokemon they want to use it on or cancel the use
                    }else{ //otherwise the player does not want to use the item, so go back to last selection page
                        return false; //no decision is made
                    }
                }else{ //player does not have any Potions left
                    return false;//no decision is made
                }
            case "Super Potion"://same thing that happens to Potion will happen when a healing item(Super Potion, Hyper Potion, Max Potion, Revive) is chosen, just changing Potion to the name of the item
                System.out.println("An item that can heal a pokemon for " + library.pokemon_items.get("Super Potion").get("heal") + " hp");
                System.out.println("You have: " + player.getItems().get("Super Potion"));
                if(player.getItems().get("Super Potion")!=0){
                    System.out.printf("+%s+\n","-".repeat(90));
                    System.out.println("You have: " + player.getItems().get("Super Potion") + "Super Potions");
                    System.out.println("Do you want to use it(y to use/any other input to not use)?");
                    String use = input.nextLine();
                    if(use.equals("y")){
                        return healchoice("Super Potion");
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            case "Hyper Potion":
                System.out.println("An item that can heal a pokemon for " + library.pokemon_items.get("Hyper Potion").get("heal") + " hp");
                System.out.println("You have: " + player.getItems().get("Hyper Potion"));
                if(player.getItems().get("Hyper Potion")!=0){
                    System.out.printf("+%s+\n","-".repeat(90));
                    System.out.println("You have: " + player.getItems().get("Hyper Potion") + "Hyper Potions");
                    System.out.println("Do you want to use it(y to use/any other input to not use)?");
                    String use = input.nextLine();
                    if(use.equals("y")){
                        return healchoice("Hyper Potion");
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            case "Max Potion":
                System.out.println("An item that can heal a pokemon for " + library.pokemon_items.get("Max Potion").get("heal") + " hp");
                System.out.println("You have: " + player.getItems().get("Max Potion"));
                if(player.getItems().get("Max Potion")!=0){
                    System.out.printf("+%s+\n","-".repeat(90));
                    System.out.println("You have: " + player.getItems().get("Max Potion") + "Max Potions");
                    System.out.println("Do you want to use it(y to use/any other input to not use)?");
                    String use = input.nextLine();
                    if(use.equals("y")){
                        return healchoice("Max Potion");
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            case "X Attack": //if X Attack is chosen
                System.out.println("An item that can increase a pokemon's attack by 1 stage in battle, can be only used in battle"); //display item information
                System.out.println("You have: " + player.getItems().get("X Attack"));//display the number of item in player's bag
                if(player.getItems().get("X Attack")!=0){ //if the number of item in player's bag is not 0
                    System.out.println("Do you want to use it(y to use/any other input to not use)?"); //ask player to confirm the use
                    String use = input.nextLine(); //receive choice
                    if(use.equals("y")){ //if choice is y
                        if(atk==6){ //check if atk is at highest level
                            System.out.println("Your battling pokemon's attack cannot be raised any higher!"); //if yes then the item cannot be used
                            return false;//return false as item is not used
                        }
                        System.out.println("You used a X Attack!"); //if atk is not at highest level, then the item is used, display message about that
                        System.out.println("Your " + your_pokemon.findname() + "'s attack rose by 1 stage"); //display message that tells player that the battling pokemon's atk stat has increased for 1 stage
                        atk++; //increase atk by 1
                        player.deditems("X Attack", 1);//reduce the number of X Attack in player's bag by 1
                        return true; //return true as the item is used
                    }else{ //if user does not want to use item
                        return false;//return false as item is not used
                    }
                }else{ //if user does not have any X Attack left
                    return false;//return false as the item is not used
                }
            case "X Defend": //same thing that happens when X Attack is chosen will happen onto X Defend or X Speed when one of them is chosen, just changing X Attack and atk to X Defend and def of to X Speed and sp
                System.out.println("An item that can increase a pokemon's defense by 1 stage in battle, can be only used in battle");
                System.out.println("You have: " + player.getItems().get("X Defend"));
                if(player.getItems().get("X Defend")!=0){
                    System.out.println("Do you want to use it(y to use/any other input to not use)?");
                    String use = input.nextLine();
                    if(use.equals("y")){
                        if(def==6){
                            System.out.println("Your battling pokemon's defense cannot be raised any higher!");
                            return false;
                        }
                        System.out.println("You used a X Defend!");
                        System.out.println("Your " + your_pokemon.findname() + "'s defense rose by 1 stage");
                        def++;
                        player.deditems("X Defend", 1);
                        return true;
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            case "X Speed":
                System.out.println("An item that can increase a pokemon's speed by 1 stage in battle, can be only used in battle");
                System.out.println("You have: " + player.getItems().get("X Speed"));;
                if(player.getItems().get("X Speed")!=0){
                    System.out.println("Do you want to use it(y to use/any other input to not use)?");
                    String use = input.nextLine();
                    if(use.equals("y")){
                        if(sp==6){
                            System.out.println("Your battling pokemon's speed cannot be raised any higher!");
                            return false;
                        }
                        System.out.println("You used a X Speed!");
                        System.out.println("Your " + your_pokemon.findname() + "'s speed rose by 1 stage");
                        sp++;
                        player.deditems("X Speed", 1);
                        return true;
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            case "Revive":
                System.out.println("An item that can revive a fainted pokemon with half of its hp");
                System.out.println("You have: " + player.getItems().get("Revive"));
                if(player.getItems().get("Revive")!=0){
                    System.out.printf("+%s+\n","-".repeat(90));
                    System.out.println("You have: " + player.getItems().get("Revive") + " Revives");
                    System.out.println("Do you want to use it(y to use/any other input to not use)?");
                    String use = input.nextLine();
                    if(use.equals("y")){
                        return healchoice("Revive");
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
        }
        return false;
    }

    public boolean healchoice(String item){ //a method that returns true if the item is used on a pokemon(only for heal item)
        Scanner input = new Scanner(System.in);
        if(item.equals("Potion")||item.equals("Super Potion")||item.equals("Hyper Potion")){ //if the item received is one of these three
            while(true){ //this loop will not end unless user does use this item on a Pokemon or choose to go back
                player.showteam(); //show all the player's pokemon in the team
                System.out.println("Choose a pokemon to use this item(1-6)/7 to cancel use"); //prompt user to enter choice
                String choice_st = input.nextLine(); //receive choice
                if(isNum(choice_st)){ //check choice format
                    int choice = Integer.parseInt(choice_st);//turn choice into integer
                    switch(choice){
                        case 1: //if pokemon1 is chosen
                            //because is in battle hence must have at least 1 pokemon, hence no need to check whether poke1 is null
                            if(player.findPoke1().findcurrenthp()==player.findPoke1().findmaxhp()){ //check if pokemon1 is at full hp
                                System.out.printf("+%s+\n","-".repeat(90)); //if yes then the item has no effect
                                System.out.println("This item has no effect on a pokemon with full hp"); //tell the player
                            }else{ //if not at full hp
                                if(player.findPoke1().isFaint()){ //check if pokemon1 is fainted, if yes then the item also has no effect
                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first"); //display a message to inform the player and give tips to the player
                                }else{ //if pokemon1 is not fainted and not at fullhp
                                    System.out.println("You used a " + item + " on " + player.findPoke1().findname()); //the item is used, display a message about that
                                    player.findPoke1().heal(library.pokemon_items.get(item).get("heal")); //the pokemon heals for the amount of healing provided by the item
                                    player.deditems(item, 1); //decrease the number of the item in the player's bag by 1
                                    return true; //return true as the item is used and a decision is made
                                }
                            }
                            break;
                        case 2: //if pokemon2 is chosen
                            if(player.findPoke2()!=null){ //check if pokemon2 exists or not, if yes, what happens to pokemon1 when it is chosen will happen to pokemon2, just change findPoke1() to findPoke2()
                                if(player.findPoke2().findcurrenthp()==player.findPoke2().findmaxhp()){
                                    System.out.printf("+%s+\n","-".repeat(90));
                                    System.out.println("This item has no effect on a pokemon with full hp");
                                }else{
                                    if(player.findPoke2().isFaint()){
                                        System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                    }else{
                                        System.out.println("You used a " + item + " on " + player.findPoke2().findname());
                                        player.findPoke2().heal(library.pokemon_items.get(item).get("heal"));
                                        player.deditems(item, 1);
                                        return true;
                                    }
                                }
                            }else{ //if pokemon2 does not exist
                                System.out.printf("+%s+\n","-".repeat(90));
                                System.out.println("This pokemon slot is empty");//tell the player this is an empty pokemon slot
                            }
                            break;
                        case 3: //the same thing that happens when pokemon2 is chosen will happen to pokemon3-6 if one of them is chosen, just changing findPoke2() to findPoke3() - findPoke6()
                            if(player.findPoke3()!=null){
                                if(player.findPoke3().findcurrenthp()==player.findPoke3().findmaxhp()){
                                    System.out.printf("+%s+\n","-".repeat(90));
                                    System.out.println("This item has no effect on a pokemon with full hp");
                                }else{
                                    if(player.findPoke3().isFaint()){
                                        System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                    }else{
                                        System.out.println("You used a " + item + " on " + player.findPoke3().findname());
                                        player.findPoke3().heal(library.pokemon_items.get(item).get("heal"));
                                        player.deditems(item, 1);
                                        return true;
                                    }
                                }
                            }else{
                                System.out.printf("+%s+\n","-".repeat(90));
                                System.out.println("This pokemon slot is empty");
                            }
                            break;
                        case 4:
                            if(player.findPoke4()!=null){
                                if(player.findPoke4().findcurrenthp()==player.findPoke4().findmaxhp()){
                                    System.out.printf("+%s+\n","-".repeat(90));
                                    System.out.println("This item has no effect on a pokemon with full hp");
                                }else{
                                    if(player.findPoke4().isFaint()){
                                        System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                    }else{
                                        System.out.println("You used a " + item + " on " + player.findPoke4().findname());
                                        player.findPoke4().heal(library.pokemon_items.get(item).get("heal"));
                                        player.deditems(item, 1);
                                        return true;
                                    }
                                }
                            }else{
                                System.out.printf("+%s+\n","-".repeat(90));
                                System.out.println("This pokemon slot is empty");
                            }
                            break;
                        case 5:
                            if(player.findPoke5()!=null){
                                if(player.findPoke5().findcurrenthp()==player.findPoke5().findmaxhp()){
                                    System.out.printf("+%s+\n","-".repeat(90));
                                    System.out.println("This item has no effect on a pokemon with full hp");
                                }else{
                                    if(player.findPoke5().isFaint()){
                                        System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                    }else{
                                        System.out.println("You used a " + item + " on " + player.findPoke5().findname());
                                        player.findPoke5().heal(library.pokemon_items.get(item).get("heal"));
                                        player.deditems(item, 1);
                                        return true;
                                    }
                                }
                            }else{
                                System.out.printf("+%s+\n","-".repeat(90));
                                System.out.println("This pokemon slot is empty");
                            }
                            break;
                        case 6:
                            if(player.findPoke6()!=null){
                                if(player.findPoke6().findcurrenthp()==player.findPoke6().findmaxhp()){
                                    System.out.printf("+%s+\n","-".repeat(90));
                                    System.out.println("This item has no effect on a pokemon with full hp");
                                }else{
                                    if(player.findPoke6().isFaint()){
                                        System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                    }else{
                                        System.out.println("You used a " + item + " on " + player.findPoke6().findname());
                                        player.findPoke6().heal(library.pokemon_items.get(item).get("heal"));
                                        player.deditems(item, 1);
                                        return true;
                                    }
                                }
                            }else{
                                System.out.printf("+%s+\n","-".repeat(90));
                                System.out.println("This pokemon slot is empty");
                            }
                            break;
                        case 7: //if player enters 7 to cancel the use and go back to last selection page
                            return false; //return false as the item is not used
                        default:
                            System.out.println("Invalid choice! Please choose again.");
                    }
                }else{
                    System.out.println("Invalid choice! Please choose again.");
                }
            }
        }else if(item.equals("Max Potion")){ //if the item is Max Potion, same thing when item is Potion, Super Potion or Hyper Potion, only change is heal() method to fullheal() method, reasons have been explained before
            while(true){
                player.showteam();
                System.out.println("Choose a pokemon to use this item(1-6)/7 to cancel use");
                String choice_st = input.nextLine();
                if(isNum(choice_st)){
                    int choice = Integer.parseInt(choice_st);
                    switch(choice){
                        case 1:
                            //because is in battle hence must have at least 1 pokemon, hence no need to check whether poke1 is null
                            if(player.findPoke1().findcurrenthp()==player.findPoke1().findmaxhp()){
                                System.out.printf("+%s+\n","-".repeat(90));
                                System.out.println("This item has no effect on a pokemon with full hp");
                            }else{
                                if(player.findPoke1().isFaint()){
                                    System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                }else{
                                    System.out.println("You used a " + item + " on " + player.findPoke1().findname());
                                    player.findPoke1().fullheal();
                                    player.deditems(item, 1);
                                    return true;
                                }
                            }
                            break;
                        case 2:
                            if(player.findPoke2()!=null){
                                if(player.findPoke2().findcurrenthp()==player.findPoke2().findmaxhp()){
                                    System.out.printf("+%s+\n","-".repeat(90));
                                    System.out.println("This item has no effect on a pokemon with full hp");
                                }else{
                                    if(player.findPoke2().isFaint()){
                                        System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                    }else{
                                        System.out.println("You used a " + item + " on " + player.findPoke2().findname());
                                        player.findPoke2().fullheal();
                                        player.deditems(item, 1);
                                        return true;
                                    }
                                }
                            }else{
                                System.out.printf("+%s+\n","-".repeat(90));
                                System.out.println("This pokemon slot is empty");
                            }
                            break;
                        case 3:
                            if(player.findPoke3()!=null){
                                if(player.findPoke3().findcurrenthp()==player.findPoke3().findmaxhp()){
                                    System.out.printf("+%s+\n","-".repeat(90));
                                    System.out.println("This item has no effect on a pokemon with full hp");
                                }else{
                                    if(player.findPoke3().isFaint()){
                                        System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                    }else{
                                        System.out.println("You used a " + item + " on " + player.findPoke3().findname());
                                        player.findPoke3().fullheal();
                                        player.deditems(item, 1);
                                        return true;
                                    }
                                }
                            }else{
                                System.out.printf("+%s+\n","-".repeat(90));
                                System.out.println("This pokemon slot is empty");
                            }
                            break;
                        case 4:
                            if(player.findPoke4()!=null){
                                if(player.findPoke4().findcurrenthp()==player.findPoke4().findmaxhp()){
                                    System.out.printf("+%s+\n","-".repeat(90));
                                    System.out.println("This item has no effect on a pokemon with full hp");
                                }else{
                                    if(player.findPoke4().isFaint()){
                                        System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                    }else{
                                        System.out.println("You used a " + item + " on " + player.findPoke4().findname());
                                        player.findPoke4().fullheal();
                                        player.deditems(item, 1);
                                        return true;
                                    }
                                }
                            }else{
                                System.out.printf("+%s+\n","-".repeat(90));
                                System.out.println("This pokemon slot is empty");
                            }
                            break;
                        case 5:
                            if(player.findPoke5()!=null){
                                if(player.findPoke5().findcurrenthp()==player.findPoke5().findmaxhp()){
                                    System.out.printf("+%s+\n","-".repeat(90));
                                    System.out.println("This item has no effect on a pokemon with full hp");
                                }else{
                                    if(player.findPoke5().isFaint()){
                                        System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                    }else{
                                        System.out.println("You used a " + item + " on " + player.findPoke5().findname());
                                        player.findPoke5().fullheal();
                                        player.deditems(item, 1);
                                        return true;
                                    }
                                }
                            }else{
                                System.out.printf("+%s+\n","-".repeat(90));
                                System.out.println("This pokemon slot is empty");
                            }
                            break;
                        case 6:
                            if(player.findPoke6()!=null){
                                if(player.findPoke6().findcurrenthp()==player.findPoke6().findmaxhp()){
                                    System.out.printf("+%s+\n","-".repeat(90));
                                    System.out.println("This item has no effect on a pokemon with full hp");
                                }else{
                                    if(player.findPoke6().isFaint()){
                                        System.out.println("This item has no effect on a fainted pokemon, please revive it first");
                                    }else{
                                        System.out.println("You used a " + item + " on " + player.findPoke6().findname());
                                        player.findPoke6().fullheal();
                                        player.deditems(item, 1);
                                        return true;
                                    }
                                }
                            }else{
                                System.out.printf("+%s+\n","-".repeat(90));
                                System.out.println("This pokemon slot is empty");
                            }
                            break;
                        case 7:
                            return false;
                        default:
                            System.out.println("Invalid choice! Please choose again.");
                    }
                }else{
                    System.out.println("Invalid choice! Please choose again.");
                }
            }
        }else if(item.equals("Revive")){ //if item chosen is revive
            while(true){ //loop not end unless player uses the item on a pokemon or cancel the use the item and go back to last selection page
                player.showteam(); //show all pokemon in player's team
                System.out.println("Choose a pokemon to use this item(1-6)/7 to cancel use"); //prompt user to enter choice
                String choice_st = input.nextLine();//receive choice
                if(isNum(choice_st)){ //check choice format
                    int choice = Integer.parseInt(choice_st); //turn choice into integer
                    switch(choice){
                        case 1://if pokemon1 is chosen
                            if(!player.findPoke1().isFaint()){ //check if pokemon1 is fainted, if not
                                System.out.printf("+%s+\n","-".repeat(90)); //revive will not ahve any effect on the pokemon
                                System.out.println("This item has no effect on a pokemon that is not fainted");
                            }else{ //if pokemon1 is fainted
                                System.out.println("You used a Revive on " + player.findPoke1().findname()); //Revive is used, tell player
                                player.findPoke1().revive();//pokemon1 is Revived
                                player.deditems(item, 1);//reduce the number of Revives in player's bag by 1
                                return true; //return true as item is used
                            }
                            break;
                        case 2: //if pokemon2 is chosen
                            if(player.findPoke2()!=null){ //check if pokemon2 exists, if yes, then same thing as pokemon1 is chosen happens to pokemon2, change findPoke1() to findPoke2()
                                if(!player.findPoke2().isFaint()){
                                    System.out.printf("+%s+\n","-".repeat(90));
                                    System.out.println("This item has no effect on a pokemon that is not fainted");
                                }else{
                                    System.out.println("You used a Revive on " + player.findPoke2().findname());
                                    player.findPoke2().revive();
                                    player.deditems(item, 1);
                                    return true;
                                }
                            }else{ //pokemon2 does not exist, tell user this is an empty pokemon slot
                                System.out.printf("+%s+\n","-".repeat(90));
                                System.out.println("This pokemon slot is empty");
                            }
                            break;
                        case 3://same thing when pokemon2 is chosen happen to pokemon3-6 if one of them is chosen, just change findPoke2() to findPoke3() - findPoke6()
                            if(player.findPoke3()!=null){
                                if(!player.findPoke3().isFaint()){
                                    System.out.printf("+%s+\n","-".repeat(90));
                                    System.out.println("This item has no effect on a pokemon that is not fainted");
                                }else{
                                    System.out.println("You used a Revive on " + player.findPoke3().findname());
                                    player.findPoke3().revive();
                                    player.deditems(item, 1);
                                    return true;
                                }
                            }else{
                                System.out.printf("+%s+\n","-".repeat(90));
                                System.out.println("This pokemon slot is empty");
                            }
                            break;
                        case 4:
                            if(player.findPoke4()!=null){
                                if(!player.findPoke4().isFaint()){
                                    System.out.printf("+%s+\n","-".repeat(90));
                                    System.out.println("This item has no effect on a pokemon that is not fainted");
                                }else{
                                    System.out.println("You used a Revive on " + player.findPoke4().findname());
                                    player.findPoke4().revive();
                                    player.deditems(item, 1);
                                    return true;
                                }
                            }else{
                                System.out.printf("+%s+\n","-".repeat(90));
                                System.out.println("This pokemon slot is empty");
                            }
                            break;
                        case 5:
                            if(player.findPoke5()!=null){
                                if(!player.findPoke5().isFaint()){
                                    System.out.printf("+%s+\n","-".repeat(90));
                                    System.out.println("This item has no effect on a pokemon that is not fainted");
                                }else{
                                    System.out.println("You used a Revive on " + player.findPoke5().findname());
                                    player.findPoke5().revive();
                                    player.deditems(item, 1);
                                    return true;
                                }
                            }else{
                                System.out.printf("+%s+\n","-".repeat(90));
                                System.out.println("This pokemon slot is empty");
                            }
                            break;
                        case 6:
                            if(player.findPoke6()!=null){
                                if(!player.findPoke6().isFaint()){
                                    System.out.printf("+%s+\n","-".repeat(90));
                                    System.out.println("This item has no effect on a pokemon that is not fainted");
                                }else{
                                    System.out.println("You used a Revive on " + player.findPoke6().findname());
                                    player.findPoke6().revive();
                                    player.deditems(item, 1);
                                    return true;
                                }
                            }else{
                                System.out.printf("+%s+\n","-".repeat(90));
                                System.out.println("This pokemon slot is empty");
                            }
                            break;
                        case 7: //if player enters 7 to cancel the use
                            return false; //return false as item is not used
                        default:
                            System.out.println("Invalid choice! Please choose again.");
                    }
                }else{
                    System.out.println("Invalid choice! Please choose again.");
                }
            }
        }
        return false;
    }

    public void opponentmove(Move foe_move){ //opponent's pokemon uses a move
        System.out.printf("+%s+\n","-".repeat(90));
        System.out.println("Opponent " + foe_pokemon.findname() + " used " + foe_move.getName() + " !"); //display message of opponent's pokemon using a move
        double def_ratio = 1; //defense ratio of player's pokemon
        double foe_atk_ratio = 1; //attack ratio of opponent's pokemon
        if(def<0){ //use def stat to determine the def ratio, the formula is same as how to calculate sp_ratio but using def instead of sp
            def_ratio = (1.0*2)/(2+(-1*def));
        }else if(def>0){
            def_ratio = (1.0*2+def)/(2*1.0);
        }
        if(foe_atk<0){ //use foe atk stat to determine the foe atk ratio, same formula as calculate sp_ratio but using foe_atk
            foe_atk_ratio = (1.0*2)/(2+(-1*foe_atk));
        }else if(foe_atk>0){
            foe_atk_ratio = (1.0*2+foe_atk)/(2*1.0);
        }
        if(foe_move.getCategory().equals("dmg")){ //if the pokemon uses a damage move
            int base_power = foe_move.getPower(); //get the base power of the move by using getPower() method
            String foe_move_type = foe_move.getType(); //get the type of the move
            double same_type = 1; //this ratio is the same type damage boost when the move used is the same type as one of pokemon's type
            if(foe_pokemon.findtype1().equals(foe_move.getType())||foe_pokemon.findtype2().equals(foe_move.getType())){ //check if the move's type is same as one of the pokemon's type
                same_type = 1.5; // if yes then set same_type to 1.5 indicating the move will do 50% more damage
            }
            double effectiveness = your_pokemon.findEffectiveness(foe_move_type); //determine the effectiveness of the move on player's pokemon base on the move type and the effectiveness of that type on player's pokemon, using findEffectiveness() method
            //the formula to calculate the damage done is as below, when def and foe_atk is at same stage, then foe_atk_ratio/def_ratio would be 1
            int damage = (int)(base_power*same_type*effectiveness*foe_atk_ratio/def_ratio);

            if(effectiveness>1.0){ //tell the player whether the move is super effective, not very efective, does not have any effect or just normal damage
                System.out.println("It's super effective!");
            }else if(effectiveness<1.0&&effectiveness>0){
                System.out.println("It's not very effective...");
            }else if(effectiveness==0.0){
                System.out.println("This move does not have any effect on your " + your_pokemon.findname());
            }
            your_pokemon.takedmg(damage); //player's pokemon takes damage
        }else if(foe_move.getCategory().equals("stat")){//if opponent's pokemon uses a stat move
            if(foe_move.getFAtk()<0){ //the stat move lowers player's pokemon's atk stat
                if(atk>-6){ //if atk is higher than lowest stage
                    System.out.println("Your " + your_pokemon.findname() + "'s Attack fell by " + -1*foe_move.getFAtk() + " stages"); //display message to tell the change
                    atk+=foe_move.getFAtk(); //atk is altered by the value of getFAtk() of the move,you are the foe of your foe, so FAtk is your atk change,same with FDef and Fsp
                }else{ //else display the message below
                    System.out.println("Your " + your_pokemon.findname() + "'s Attack cannot be lowered anymore!"); //display message to tell the change
                }
            }else if(foe_move.getFAtk()>0){//the stat move increases player's pokemon's atk stat
                if(atk<6){ //if atk is lower than highest stage
                    System.out.println("Your " + your_pokemon.findname() + "'s Attack rose by " + foe_move.getFAtk() + " stages");
                    atk+=foe_move.getFAtk(); //atk is altered by the value of getFAtk() of the move
                }else{ //else display the message below
                    System.out.println("Your " + your_pokemon.findname() + "'s Attack cannot be raised anymore!");
                }
            }
            //the codes above will repeat twice, but changing getFAtk() to getFDef() and getFSp(), atk to def and sp
            if(foe_move.getFDef()<0){
                if(def>-6){
                    System.out.println("Your " + your_pokemon.findname() + "'s Defense fell by " + -1*foe_move.getFDef() + " stages");
                    def+=foe_move.getFDef();
                }else{
                    System.out.println("Your " + your_pokemon.findname() + "'s Defense cannot be lowered anymore!");
                }
            }else if(foe_move.getFDef()>0){
                if(def<6){
                    System.out.println("Your " + your_pokemon.findname() + "'s Defense rose by " + foe_move.getFDef() + " stages");
                    def+=foe_move.getFDef();
                }else{
                    System.out.println("Your " + your_pokemon.findname() + "'s Defense cannot be raised anymore!");
                }
            }
            if(foe_move.getFSp()<0){
                if(sp>-6){
                    System.out.println("Your " + your_pokemon.findname() + "'s Speed fell by " + -1*foe_move.getFSp() + " stages");
                    sp+=foe_move.getFSp();
                }else{
                    System.out.println("Your " + your_pokemon.findname() + "'s Speed cannot be lowered anymore!");
                }
            }else if(foe_move.getFSp()>0){
                if(sp<6){
                    System.out.println("Your " + your_pokemon.findname() + "'s Speed rose by " + foe_move.getFSp() + " stages");
                    sp+=foe_move.getFSp();
                }else{
                    System.out.println("Your " + your_pokemon.findname() + "'s Speed cannot be raised anymore!");
                }
            }

            if(foe_move.getAtk()<0){ //if the move lowers the opponent's pokemon's attack stat
                if(foe_atk>-6){ //if the foe_atk is higher than lowest stage
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Attack fell by " + -1*foe_move.getAtk() + " stages");//display message to tell the change
                    foe_atk+=foe_move.getAtk(); //atk is altered by the value of getAtk() of the move
                }else{ //else display the message below
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Attack cannot be lowered anymore!");
                }
            }else if(foe_move.getAtk()>0){ //if the move increases the opponent's pokemon's attack stat
                if(foe_atk<6){ //if foe_atk is lower than highest stage
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Attack rose by " + foe_move.getAtk() + " stages");//display message to tell the change
                    foe_atk+=foe_move.getAtk(); //atk is altered by the value of getAtk() of the move
                }else{ //else display the message below
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Attack cannot be raised anymore!");
                }
            }
            //the code above(starting from if(foe_move.getAtk()<0)) will repeat twice, changing getAtk() to getDef() and getSp(), foe_atk to foe_def and foe_sp
            if(foe_move.getDef()<0){
                if(foe_def>-6){
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Defense fell by " + -1*foe_move.getDef() + " stages");
                    foe_def+=foe_move.getDef();
                }else{
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Defense cannot be lowered anymore!");
                }
            }else if(foe_move.getDef()>0){
                if(foe_def<6){
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Defense rose by " + foe_move.getDef() + " stages");
                    foe_def+=foe_move.getDef();
                }else{
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Defense cannot be raised anymore!");
                }
            }
            if(foe_move.getSp()<0){
                if(foe_sp>-6){
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Speed fell by " + -1*foe_move.getSp() + " stages");
                    foe_sp+=foe_move.getSp();
                }else{
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Speed cannot be lowered anymore!");
                }
            }else if(foe_move.getSp()>0){
                if(foe_sp<6){
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Speed rose by " + foe_move.getSp() + " stages");
                    foe_sp+=foe_move.getSp();
                }else{
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Speed cannot be raised anymore!");
                }
            }
        }else if(foe_move.getCategory().equals("heal")){ //if the opponent's pokemon uses a heal move
            int healing = (int)(foe_pokemon.findmaxhp()*foe_move.gethpheal()); //healing amount of the move will be determined using max hp of the pokemon times the hp heal ratio of the move, using gethpheal() method to get the ratio
            foe_pokemon.heal(healing); //opponent's pokemon heals for the healing amount
        }else if(foe_move.getCategory().equals("sp")){ //if the opponent's pokemon uses a sp move
            //sp move is a hybrid of dmg moves and stat moves, which are dmg moves that have a secondary effect
            int damage = 0; //declare and initialize damage just as in dmg move
            int yourcurrenthp = your_pokemon.findcurrenthp();//get the currenthp of player's pokemon in battle
            if(foe_move.getPower()!=0){ //to check whether the move have base_power, basically every move have, this is just to prevent bugs and allow expansion
                int base_power = foe_move.getPower(); //everything here is just the same as in dmg
                String foe_move_type = foe_move.getType();
                double effectiveness = your_pokemon.findEffectiveness(foe_move_type);
                double same_type = 1;
                if(foe_pokemon.findtype1().equals(foe_move.getType())||foe_pokemon.findtype2().equals(foe_move.getType())){
                    same_type = 1.5;
                }
                damage = (int)(base_power*same_type*effectiveness*foe_atk_ratio/def_ratio);
                if(effectiveness>1.0){
                    System.out.println("It's super effective!");
                }else if(effectiveness<1.0&&effectiveness>0){
                    System.out.println("It's not very effective...");
                }else if(effectiveness==0.0){
                    System.out.println("This move does not have any effect on " + your_pokemon.findname());
                }
                your_pokemon.takedmg(damage);
            }

            //everything here is just as same as in stat
            if(foe_move.getFAtk()<0){
                if(atk>-6){
                    System.out.println("Your " + your_pokemon.findname() + "'s Attack fell by " + -1*foe_move.getFAtk() + " stages");
                    atk+=foe_move.getFAtk(); //you are the foe of your foe, so FAtk is your atk change,same with FDef and Fsp
                }else{
                    System.out.println("Your " + your_pokemon.findname() + "'s Attack cannot be lowered anymore!");
                }
            }else if(foe_move.getFAtk()>0){
                if(atk<6){
                    System.out.println("Your " + your_pokemon.findname() + "'s Attack rose by " + foe_move.getFAtk() + " stages");
                    atk+=foe_move.getFAtk();
                }else{
                    System.out.println("Your " + your_pokemon.findname() + "'s Attack cannot be raised anymore!");
                }
            }
            if(foe_move.getFDef()<0){
                if(def>-6){
                    System.out.println("Your " + your_pokemon.findname() + "'s Defense fell by " + -1*foe_move.getFDef() + " stages");
                    def+=foe_move.getFDef(); //you are the foe of your foe, so FAtk is your atk change,same with FDef and Fsp
                }else{
                    System.out.println("Your " + your_pokemon.findname() + "'s Defense cannot be lowered anymore!");
                }
            }else if(foe_move.getFDef()>0){
                if(def<6){
                    System.out.println("Your " + your_pokemon.findname() + "'s Defense rose by " + foe_move.getFDef() + " stages");
                    def+=foe_move.getFDef();
                }else{
                    System.out.println("Your " + your_pokemon.findname() + "'s Defense cannot be raised anymore!");
                }
            }
            if(foe_move.getFSp()<0){
                if(sp>-6){
                    System.out.println("Your " + your_pokemon.findname() + "'s Speed fell by " + -1*foe_move.getFSp() + " stages");
                    sp+=foe_move.getFSp(); //you are the foe of your foe, so FAtk is your atk change,same with FDef and Fsp
                }else{
                    System.out.println("Your " + your_pokemon.findname() + "'s Speed cannot be lowered anymore!");
                }
            }else if(foe_move.getFSp()>0){
                if(sp<6){
                    System.out.println("Your " + your_pokemon.findname() + "'s Speed rose by " + foe_move.getFSp() + " stages");
                    sp+=foe_move.getFSp();
                }else{
                    System.out.println("Your " + your_pokemon.findname() + "'s Speed cannot be raised anymore!");
                }
            }

            if(foe_move.getAtk()<0){ //print output to show change in stats
                if(foe_atk>-6){
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Attack fell by " + -1*foe_move.getAtk() + " stages");
                    foe_atk+=foe_move.getAtk();
                }else{
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Attack cannot be lowered anymore!");
                }
            }else if(foe_move.getAtk()>0){
                if(foe_atk<6){
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Attack rose by " + foe_move.getAtk() + " stages");
                    foe_atk+=foe_move.getAtk();
                }else{
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Attack cannot be raised anymore!");
                }
            }
            if(foe_move.getDef()<0){
                if(foe_def>-6){
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Defense fell by " + -1*foe_move.getDef() + " stages");
                    foe_def+=foe_move.getDef();
                }else{
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Defense cannot be lowered anymore!");
                }
            }else if(foe_move.getDef()>0){
                if(foe_def<6){
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Defense rose by " + foe_move.getDef() + " stages");
                    foe_def+=foe_move.getDef();
                }else{
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Defense cannot be raised anymore!");
                }
            }
            if(foe_move.getSp()<0){
                if(foe_sp>-6){
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Speed fell by " + -1*foe_move.getSp() + " stages");
                    foe_sp+=foe_move.getSp();
                }else{
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Speed cannot be lowered anymore!");
                }
            }else if(foe_move.getSp()>0){
                if(foe_sp<6){
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Speed rose by " + foe_move.getSp() + " stages");
                    foe_sp+=foe_move.getSp();
                }else{
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Speed cannot be raised anymore!");
                }
            }
            //only addition is this
            if(foe_move.getdmgheal()!=0.0){ //if the sp move can heal the user pokemon according to the damage done
                if(damage>yourcurrenthp){ //if damage is higher than the player's pokemon current hp, then healing will base on the currenthp of that pokemon before taking damage
                    int healing = (int)(yourcurrenthp*foe_move.getdmgheal());//healing amount = yourcurrenthp x the dmg heal ratio of the move obtained using getdmgheal() method
                    foe_pokemon.heal(healing);//opponent's pokemon heals for that healing amount
                }else{ //if damage is lower than the player's pokemon current hp
                    int healing = (int)(damage*foe_move.getdmgheal());//healing amount = damage x the dmg heal ratio of the move
                    foe_pokemon.heal(healing);//opponent's pokemon heals for that healing amount
                }
            }
        }
        //in some occasion stats can be >6 and <-6 so adjust them back to 6 or -6
        if(atk>6){
            atk=6;
        }
        if(atk<-6){
            atk=-6;
        }
        if(def>6){
            def=6;
        }
        if(def<-6){
            def=-6;
        }
        if(sp>6){
            sp=6;
        }
        if(sp<-6){
            sp=-6;
        }

        if(foe_atk>6){
            foe_atk=6;
        }
        if(foe_atk<-6){
            foe_atk=-6;
        }
        if(foe_def>6){
            foe_def=6;
        }
        if(foe_def<-6){
            foe_def=-6;
        }
        if(foe_sp>6){
            foe_sp=6;
        }
        if(foe_sp<-6){
            foe_sp=-6;
        }
    }

    public void yourmove(Move your_move){ //player's pokemon uses a move, basically this is just the mirror of the last method, changing sides, your_pokemon with opponent_pokemon
        System.out.printf("+%s+\n","-".repeat(90));
        System.out.println("Your " + your_pokemon.findname() + " used " + your_move.getName() + " !"); //display message of player's pokemon using a move
        double atk_ratio = 1; //attack ratio of player's pokemon
        double foe_def_ratio = 1;//defense ratio of opponent's pokemon
        if(atk<0){ //use atk stat to determine the atk ratio, the formula is same as how to calculate sp_ratio but using atk instead of sp
            atk_ratio = (1.0*2)/(2+(-1*atk));
        }else if(atk>0){
            atk_ratio = (1.0*(2+atk))/(1.0*2);
        }
        if(foe_def<0){ //use foe_def stat to determine the foe def ratio, the formula is same as how to calculate sp_ratio but using foe_def instead of sp
            foe_def_ratio = (2*1.0)/(2+(-1*foe_def));
        }else if(foe_def>0){
            foe_def_ratio = (1.0*(2+foe_def))/(2*1.0);
        }

        if(your_move.getCategory().equals("dmg")){//if the pokemon uses a damage move
            int base_power = your_move.getPower();//get the base power of the move by using getPower() method
            String your_move_type = your_move.getType();//get the type of the move
            double effectiveness = foe_pokemon.findEffectiveness(your_move_type);//this ratio is the same type damage boost when the move used is the same type as one of pokemon's type
            double same_type = 1;//determine the effectiveness of the move on player's pokemon base on the move type and the effectiveness of that type on player's pokemon, using findEffectiveness() method
            if(your_pokemon.findtype1().equals(your_move.getType())||your_pokemon.findtype2().equals(your_move.getType())){ //check if the move's type is same as one of the pokemon's type
                same_type = 1.5;// if yes then set same_type to 1.5 indicating the move will do 50% more damage
            }
            //the formula to calculate the damage done is as below, when def and foe_atk is at same stage, then foe_atk_ratio/def_ratio would be 1
            int damage = (int)(base_power*same_type*effectiveness*atk_ratio/foe_def_ratio);
            //show effectiveness of the move used
            if(effectiveness>1.0){
                System.out.println("It's super effective!");
            }else if(effectiveness<1.0&&effectiveness>0){
                System.out.println("It's not very effective...");
            }else if(effectiveness==0.0){
                System.out.println("This move does not have any effect on " + foe_pokemon.findname());
            }
            foe_pokemon.takedmg(damage);//opponent's pokemon take damage
        }else if(your_move.getCategory().equals("stat")){ //if player's pokemon used a stat move
            if(your_move.getAtk()<0){ //the stat move lowers player's pokemon's atk stat
                if(atk>-6){//if atk is higher than lowest stage
                    System.out.println("Your " + your_pokemon.findname() + "'s Attack fell by " + -1*your_move.getAtk() + " stages");//display message to tell the change
                    atk+=your_move.getAtk();//atk is altered by the value of getAtk() of the move
                }else{//else display the message below
                    System.out.println("Your " + your_pokemon.findname() + "'s Attack cannot be lowered anymore");
                }
            }else if(your_move.getAtk()>0){//the stat move increases player's pokemon's atk stat
                if(atk<6){//if atk is lower than highest stage
                    System.out.println("Your " + your_pokemon.findname() + "'s Attack rose by " + your_move.getAtk() + " stages");//display message to tell the change
                    atk+=your_move.getAtk();//atk is altered by the value of getAtk() of the move
                }else{//else display the message below
                    System.out.println("Your " + your_pokemon.findname() + "'s Attack cannot be raised anymore");
                }
            }
            //the codes above will repeat twice, but changing getAtk() to getDef() and getSp(), atk to def and sp
            if(your_move.getDef()<0){
                if(def>-6){
                    System.out.println("Your " + your_pokemon.findname() + "'s Defense fell by " + -1*your_move.getDef() + " stages");
                    def+=your_move.getDef();
                }else{
                    System.out.println("Your " + your_pokemon.findname() + "'s Defense cannot be lowered anymore");
                }
            }else if(your_move.getDef()>0){
                if(def<6){
                    System.out.println("Your " + your_pokemon.findname() + "'s Defense rose by " + your_move.getDef() + " stages");
                    def+=your_move.getDef();
                }else{
                    System.out.println("Your " + your_pokemon.findname() + "'s Defense cannot be raised anymore");
                }
            }
            if(your_move.getSp()<0){
                if(sp>-6){
                    System.out.println("Your " + your_pokemon.findname() + "'s Speed fell by " + -1*your_move.getSp() + " stages");
                    sp+=your_move.getSp();
                }else{
                    System.out.println("Your " + your_pokemon.findname() + "'s Speed cannot be lowered anymore");
                }
            }else if(your_move.getSp()>0){
                if(sp<6){
                    System.out.println("Your " + your_pokemon.findname() + "'s Speed rose by " + your_move.getSp() + " stages");
                    sp+=your_move.getSp();
                }else{
                    System.out.println("Your " + your_pokemon.findname() + "'s Speed cannot be raised anymore");
                }
            }

            if(your_move.getFAtk()<0){ //
                if(foe_atk>-6){ //if foe_atk is higher than lowest stage
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Attack fell by " + -1*your_move.getFAtk() + " stages");//display message to tell the change
                    foe_atk+=your_move.getFAtk();//foe_atk is altered by the value of getFAtk() of the move
                }else{//else display the message below
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Attack cannot be lowered anymore");
                }
            }else if(your_move.getFAtk()>0){//
                if(foe_atk<6){//if foe_atk is lower than highest stage
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Attack rose by " + your_move.getFAtk() + " stages");//display message to tell the change
                    foe_atk+=your_move.getFAtk();//foe_atk is altered by the value of getFAtk() of the move
                }else{//else display the message below
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Attack cannot be raised anymore");
                }
            }
            //the codes above will repeat twice, but changing getFAtk() to getFDef() and getFSp(), foe_atk to foe_def and foe_sp
            if(your_move.getFDef()<0){
                if(foe_def>-6){
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Defense fell by " + -1*your_move.getFDef() + " stages");
                    foe_def+=your_move.getFDef();
                }else{
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Defense cannot be lowered anymore");
                }
            }else if(your_move.getFDef()>0){
                if(foe_def<6){
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Defense rose by " + your_move.getFDef() + " stages");
                    foe_def+=your_move.getFDef();
                }else{
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Defense cannot be raised anymore");
                }
            }
            if(your_move.getFSp()<0){
                if(foe_sp>-6){
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Speed fell by " + -1*your_move.getFSp() + " stages");
                    foe_sp+=your_move.getFSp();
                }else{
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Speed cannot be lowered anymore");
                }
            }else if(your_move.getFSp()>0){
                if(foe_sp<6){
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Speed rose by " + your_move.getFSp() + " stages");
                    foe_sp+=your_move.getFSp();
                }else{
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Speed cannot be raised anymore");
                }
            }
        }else if(your_move.getCategory().equals("heal")){//if the player's pokemon uses a heal move
            int healing = (int)(your_pokemon.findmaxhp()*your_move.gethpheal()); //healing amount of the move will be determined using max hp of the pokemon times the hp heal ratio of the move, using gethpheal() method to get the ratio
            your_pokemon.heal(healing);//opponent's pokemon heals for the healing amount
        }else if(your_move.getCategory().equals("sp")){//if the player's pokemon uses a sp move
            //sp move is a hybrid of dmg moves and stat moves, which are dmg moves that have a secondary effect
            int damage = 0;//declare and initialize damage just as in dmg move
            int currentfoehp = foe_pokemon.findcurrenthp();//get the currenthp of opponent's pokemon in battle
            if(your_move.getPower()!=0){ //to check whether the move have base_power, basically every move have, this is just to prevent bugs and allow expansion
                int base_power = your_move.getPower(); //everything here is just the same as in dmg
                String your_move_type = your_move.getType();
                double effectiveness = foe_pokemon.findEffectiveness(your_move_type);
                double same_type = 1;
                if(your_pokemon.findtype1().equals(your_move.getType())||your_pokemon.findtype2().equals(your_move.getType())){
                    same_type = 1.5;
                }
                damage = (int)(base_power*same_type*effectiveness*atk_ratio/foe_def_ratio);
                if(effectiveness>1.0){
                    System.out.println("It's super effective!");
                }else if(effectiveness<1.0&&effectiveness>0){
                    System.out.println("It's not very effective...");
                }else if(effectiveness==0.0){
                    System.out.println("This move does not have any effect on " + foe_pokemon.findname());
                }
                foe_pokemon.takedmg(damage);
            }
            //everything here is same as in stat
            if(your_move.getAtk()<0){
                if(atk>-6){
                    System.out.println("Your " + your_pokemon.findname() + "'s Attack fell by " + -1*your_move.getAtk() + " stages");
                    atk+=your_move.getAtk();
                }else{
                    System.out.println("Your " + your_pokemon.findname() + "'s Attack cannot be lowered anymore");
                }
            }else if(your_move.getAtk()>0){
                if(atk<6){
                    System.out.println("Your " + your_pokemon.findname() + "'s Attack rose by " + your_move.getAtk() + " stages");
                    atk+=your_move.getAtk();
                }else{
                    System.out.println("Your " + your_pokemon.findname() + "'s Attack cannot be raised anymore");
                }
            }
            if(your_move.getDef()<0){
                if(def>-6){
                    System.out.println("Your " + your_pokemon.findname() + "'s Defense fell by " + -1*your_move.getDef() + " stages");
                    def+=your_move.getDef();
                }else{
                    System.out.println("Your " + your_pokemon.findname() + "'s Defense cannot be lowered anymore");
                }
            }else if(your_move.getDef()>0){
                if(def<6){
                    System.out.println("Your " + your_pokemon.findname() + "'s Defense rose by " + your_move.getDef() + " stages");
                    def+=your_move.getDef();
                }else{
                    System.out.println("Your " + your_pokemon.findname() + "'s Defense cannot be raised anymore");
                }
            }
            if(your_move.getSp()<0){
                if(sp>-6){
                    System.out.println("Your " + your_pokemon.findname() + "'s Speed fell by " + -1*your_move.getSp() + " stages");
                    sp+=your_move.getSp();
                }else{
                    System.out.println("Your " + your_pokemon.findname() + "'s Speed cannot be lowered anymore");
                }
            }else if(your_move.getSp()>0){
                if(sp<6){
                    System.out.println("Your " + your_pokemon.findname() + "'s Speed rose by " + your_move.getSp() + " stages");
                    sp+=your_move.getSp();
                }else{
                    System.out.println("Your " + your_pokemon.findname() + "'s Speed cannot be raised anymore");
                }
            }

            if(your_move.getFAtk()<0){ //print output to show change in stats
                if(foe_atk>-6){
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Attack fell by " + -1*your_move.getFAtk() + " stages");
                    foe_atk+=your_move.getFAtk();
                }else{
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Attack cannot be lowered anymore");
                }
            }else if(your_move.getFAtk()>0){
                if(foe_atk<6){
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Attack rose by " + your_move.getFAtk() + " stages");
                    foe_atk+=your_move.getFAtk();
                }else{
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Attack cannot be raised anymore");
                }
            }
            if(your_move.getFDef()<0){
                if(foe_def>-6){
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Defense fell by " + -1*your_move.getFDef() + " stages");
                    foe_def+=your_move.getFDef();
                }else{
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Defense cannot be lowered anymore");
                }
            }else if(your_move.getFDef()>0){
                if(foe_def<6){
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Defense rose by " + your_move.getFDef() + " stages");
                    foe_def+=your_move.getFDef();
                }else{
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Defense cannot be raised anymore");
                }
            }
            if(your_move.getFSp()<0){
                if(foe_sp>-6){
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Speed fell by " + -1*your_move.getFSp() + " stages");
                    foe_sp+=your_move.getFSp();
                }else{
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Speed cannot be lowered anymore");
                }
            }else if(your_move.getFSp()>0){
                if(foe_sp<6){
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Speed rose by " + your_move.getFSp() + " stages");
                    foe_sp+=your_move.getFSp();
                }else{
                    System.out.println("Opponent " + foe_pokemon.findname() + "'s Speed cannot be raised anymore");
                }
            }
            //only addition is this
            if(your_move.getdmgheal()!=0.0){//if the sp move can heal the user pokemon accorfing to the damage done
                if(damage>currentfoehp){ //if damage is higher than the opponent's pokemon current hp, then healing will base on the currenthp of that pokemon before taking damage
                    //this to prevent overhealing from overkill, etc opponent have 10hp left, you hit 1000 and heal rate is 50% you are supposed to leech only 5 hp from your attack and not 500
                    int healing = (int)(currentfoehp*your_move.getdmgheal()); //healing amount = currentfoehp x the dmg heal ratio of the move obtained using getdmgheal() method
                    your_pokemon.heal(healing);//player's pokemon heals for that healing amount
                }else{//if damage is lower than the opponent's pokemon current hp
                    int healing = (int)(damage*your_move.getdmgheal());//healing amount = damage x the dmg heal ratio of the move
                    your_pokemon.heal(healing);//player's pokemon heals for that healing amount
                }
            }
        }
        //in some occasion stats can be >6 and <-6 so adjust them back to 6 or -6
        if(atk>6){
            atk=6;
        }
        if(atk<-6){
            atk=-6;
        }
        if(def>6){
            def=6;
        }
        if(def<-6){
            def=-6;
        }
        if(sp>6){
            sp=6;
        }
        if(sp<-6){
            sp=-6;
        }

        if(foe_atk>6){
            foe_atk=6;
        }
        if(foe_atk<-6){
            foe_atk=-6;
        }
        if(foe_def>6){
            foe_def=6;
        }
        if(foe_def<-6){
            foe_def=-6;
        }
        if(foe_sp>6){
            foe_sp=6;
        }
        if(foe_sp<-6){
            foe_sp=-6;
        }
    }
}