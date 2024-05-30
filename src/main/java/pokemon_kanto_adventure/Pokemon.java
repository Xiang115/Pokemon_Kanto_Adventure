package pokemon_kanto_adventure;

import java.util.HashMap;

public class Pokemon {
    private String name; //name of the pokemon
    private int max_level = 100; //level cap of the pokemon
    private int level; //level of the pokemon
    private int currenthp; //current hp of the pokemon
    private int maxhp; //max hp of the pokemon
    private int currentxp; //current xp until next level of the pokemon
    private String nextevolution; //the next evolution of the pokemon
    private int speed; //speed of the pokemon
    private double weight;//weight of the pokemon, for sfari zone sorting purposes
    private boolean cute;//cute status of the pokemon, for safari zone sorting purposes
    private String move1; //move 1 of the pokemon
    private String move2; //move 2 of the pokemon
    private String move3; //move 3 of the pokemon
    private String move4; //move 4 of the pokemon
    private String type1; //first type of the pokemon
    private String type2; //second type of the pokemon, of pokemon only have one type, this String will be "null"
    private boolean faint; //faint status of pokemon
    private boolean wild; //wild status of pokemon
    private boolean inbattle; //battle status of pokemon
    private int lvl_to_evolve; //level to evolve of pokemon, extra feature, pokemon evolves when it reaches this level
    private HashMap<String, Double> effectiveness; //a HashMap of effectiveness with each type as key and has a corresponding damage rate value

    public Pokemon(String n) { //to decalre a level 1 wild pokemon
        name = n; //set name to n
        level = 1; //set level to 1
        maxhp = library.pokemonhp.get(name).get(level); //set the max hp from library.pokemonhp HashMap using name as key to get a HashMap containing hp values according level, from this HashMap, use level as key to get maxhp
        currenthp = maxhp; //set currenthp to maxhp
        nextevolution = library.evolution.get(name); //set the next evolution of the pokemon with library.evolution HashMap using name as key
        weight = library.pokemon_weight.get(name); //set the weight of the pokemon with library.pokemon_weight HashMap using name as key
        speed = library.pokemon_speed.get(name).get(level);//set the speed from library.pokemon_speed HashMap using name as key to get a HashMap containing speed values according level, from this HashMap, use level as key to get speed
        cute = library.pokemon_cute.get(name); //set the cute status of the pokemon with library.pokemon_cute HashMap using name as key
        //use name as key to obtain a HashMap containing four move names from library.pokemon_moveset HashMap
        //and then use move1, move2, move3, move4 as keys to get corresponding move names
        move1 = library.pokemon_moveset.get(name).get("move1");
        move2 = library.pokemon_moveset.get(name).get("move2");
        move3 = library.pokemon_moveset.get(name).get("move3");
        move4 = library.pokemon_moveset.get(name).get("move4");
        type1 = library.pokemon_type.get(name).get("type1");
        type2 = library.pokemon_type.get(name).get("type2");
        lvl_to_evolve = library.evo_lvl.get(name); //set the level to evolve of the pokemon with library.evo_lvl HashMap using name as key
        currentxp = calcxp(); //use calcxp to obtain the currentxp until next level
        wild = true; //set the wild status to true
        inbattle = false; //set the battle status to false
        faint = false; //set faint status to false
        effectiveness = library.pokemon_effectiveness.get(name); //get the HashMap of effectiveness from library.pokemon_effectiveness HashMap using name as key
    }

    public Pokemon(String n, int l) { //to declare a wild pokemon with level and name
        //basically the same thing as Pokemon(n), so I will only put comment in the different places
        name = n;
        level = l;//set level to l
        maxhp = library.pokemonhp.get(name).get(level);
        currenthp = maxhp;
        nextevolution = library.evolution.get(name);
        weight = library.pokemon_weight.get(name);
        speed = library.pokemon_speed.get(name).get(level);
        cute = library.pokemon_cute.get(name);
        move1 = library.pokemon_moveset.get(name).get("move1");
        move2 = library.pokemon_moveset.get(name).get("move2");
        move3 = library.pokemon_moveset.get(name).get("move3");
        move4 = library.pokemon_moveset.get(name).get("move4");
        type1 = library.pokemon_type.get(name).get("type1");
        type2 = library.pokemon_type.get(name).get("type2");
        currentxp = calcxp();
        lvl_to_evolve = library.evo_lvl.get(name);
        faint = false;
        wild = true;
        inbattle = false;
        effectiveness = library.pokemon_effectiveness.get(name);
    }

    public Pokemon(String n, int l, boolean w) { //to declare a pokemon with level and name and set whether it is wild or not, this is basically only for declaring wild pokemon or trainer pokemon in battle, hence the inbattle status will always be true
        //basically the same thing as Pokemon(n), so I will only put comment in the different places
        name = n;
        level = l; //set level to l(L not 1)
        maxhp = library.pokemonhp.get(name).get(level);
        currenthp = maxhp;
        nextevolution = library.evolution.get(name);
        weight = library.pokemon_weight.get(name);
        speed = library.pokemon_speed.get(name).get(level);
        cute = library.pokemon_cute.get(name);
        move1 = library.pokemon_moveset.get(name).get("move1");
        move2 = library.pokemon_moveset.get(name).get("move2");
        move3 = library.pokemon_moveset.get(name).get("move3");
        move4 = library.pokemon_moveset.get(name).get("move4");
        type1 = library.pokemon_type.get(name).get("type1");
        type2 = library.pokemon_type.get(name).get("type2");
        currentxp = calcxp();
        lvl_to_evolve = library.evo_lvl.get(name);
        wild = w; //set wild to w, either true or false
        inbattle = true; //inbattle is true bacause it is only for wild pokemon or opponent pokemon player is battling
        faint = false;
        effectiveness = library.pokemon_effectiveness.get(name);
    }

    public Pokemon(String n, int l, boolean w, boolean b) { //to declare a pokemon with level,name,wild status and battle status, this is basically for declaring starter pokemon
        //basically the same thing as Pokemon(n), so I will only put comment in the different places
        name = n;
        level = l; //set level to l
        maxhp = library.pokemonhp.get(name).get(level);
        currenthp = maxhp;
        nextevolution = library.evolution.get(name);
        weight = library.pokemon_weight.get(name);
        speed = library.pokemon_speed.get(name).get(level);
        cute = library.pokemon_cute.get(name);
        move1 = library.pokemon_moveset.get(name).get("move1");
        move2 = library.pokemon_moveset.get(name).get("move2");
        move3 = library.pokemon_moveset.get(name).get("move3");
        move4 = library.pokemon_moveset.get(name).get("move4");
        type1 = library.pokemon_type.get(name).get("type1");
        type2 = library.pokemon_type.get(name).get("type2");
        currentxp = calcxp();
        lvl_to_evolve = library.evo_lvl.get(name);
        wild = w; //set wild to w
        inbattle = b; //set inbattle to b
        faint = false;
        effectiveness = library.pokemon_effectiveness.get(name);
    }

    public Pokemon(String n, int l, int currentHP, int currentXP) { //declare a pokemon with name, level, currenthp, currentxp , basically for reading player's team and PC pokemons, so inbattle is false
        //basically the same thing as Pokemon(n), so I will only put comment in the different places
        name = n;
        this.level = l;//set level to l
        maxhp = library.pokemonhp.get(name).get(level);
        currenthp = currentHP;
        currentxp = currentXP;
        nextevolution = library.evolution.get(name);
        weight = library.pokemon_weight.get(name);
        speed = library.pokemon_speed.get(name).get(level);
        cute = library.pokemon_cute.get(name);
        move1 = library.pokemon_moveset.get(name).get("move1");
        move2 = library.pokemon_moveset.get(name).get("move2");
        move3 = library.pokemon_moveset.get(name).get("move3");
        move4 = library.pokemon_moveset.get(name).get("move4");
        type1 = library.pokemon_type.get(name).get("type1");
        type2 = library.pokemon_type.get(name).get("type2");

        lvl_to_evolve = library.evo_lvl.get(name);
        wild = false;
        inbattle = false; //set inbattle to false
        if (currenthp == 0) { //check if currenthp is 0
            faint = true; //if yes then the pokemon is fainted
        } else {
            faint = false; //if no then the pokemon is not fainted
        }
        effectiveness = library.pokemon_effectiveness.get(name);
    }

    public int findmaxhp() { //return the max hp of the pokemon
        return maxhp;
    }

    public int findcurrenthp() { //return currenthp
        return currenthp;
    }

    public int calcxp() { //calculate the xp needed for next level
        if (level <= 10) { //if level<=10 then xp is 100
            return 100;
        } else if (level <= 30) { //if level is 11-30 then xp is 200
            return 200;
        } else if (level < max_level) { //if level > 30 but < 100 then xp is 300
            return 300;
        } else { //if level is 100 at level cap then return a very large value
            return Integer.MAX_VALUE;
        }
    }

    public boolean findcute() { //return cute
        return cute;
    }

    public double findweight() { //return weight
        return weight;
    }

    public int findspeed() { //return speed
        return speed;
    }

    public String findname() { //return name
        return name;
    }

    public int findlvl() { //return level
        return level;
    }

    public String findmov1() { //return move1
        return move1;
    }

    public String findmov2() { //return move2
        return move2;
    }

    public String findmov3() { //return move3
        return move3;
    }

    public String findmov4() { //return move4
        return move4;
    }

    public String findtype1() { //return type1
        return type1;
    }

    public String findtype2() { //return type2
        return type2;
    }

    public int findcurrentxp() { //return currentxp
        return currentxp;
    }

    public HashMap<String, Double> findEffectiveness() { //return HashMap of effectiveness
        return effectiveness;
    }

    public double findEffectiveness(String t) { //return effectiveness of a specific type
        return effectiveness.get(t);
    }

    public void showPokemonInfo() { //show pokemon info
        System.out.println("+--------------------Pokemon Info--------------------+");
        System.out.println(findname());
        System.out.println("Lvl: " + findlvl());
        System.out.printf("HP: %d/%d\n", findcurrenthp(), findmaxhp());
        System.out.printf("Type1: %s  Type2: %s\n", findtype1(), findtype2());
        System.out.println("xp needed to level up: " + findcurrentxp());
        System.out.println("Speed: " + findspeed());
        System.out.println("Weight: " + findweight());
        System.out.println("Cute?: " + findcute());
        System.out.println("Moves: ");
        System.out.println(findmov1());
        System.out.println(findmov2());
        System.out.println(findmov3());
        System.out.println(findmov4());
        System.out.println("Weak against: ");
        String[] types = {"normal", "fire", "water", "electric", "grass", "fighting", "poison", "ground", "flying", "psychic", "bug", "rock", "ghost", "dark", "steel", "sound"};
        for (int i = 0; i < types.length; i++) {
            if (effectiveness.get(types[i]) > 1.0) {
                System.out.println(" - " + types[i]);
            }
        }
        System.out.println("Resists: ");
        for (int i = 0; i < types.length; i++) {
            if (effectiveness.get(types[i]) < 1.0) {
                System.out.println(" - " + types[i]);
            }
        }
        System.out.println("moves");
    }

    public void showWeakness() { //show pokemon's weakness
        System.out.println(name + " is weak against: ");
        String[] types = {"normal", "fire", "water", "electric", "grass", "fighting", "poison", "ground", "flying", "psychic", "bug", "rock", "ghost", "dark", "steel", "sound"};
        for (int i = 0; i < types.length; i++) {
            if (effectiveness.get(types[i]) > 1.0) {
                System.out.println(" - " + types[i]);
            }
        }
        System.out.println("moves");
        System.out.println("");
    }

    public void showResistance() { //show pokemon's resistance
        System.out.println(name + " resists: ");
        String[] types = {"normal", "fire", "water", "electric", "grass", "fighting", "poison", "ground", "flying", "psychic", "bug", "rock", "ghost", "dark", "steel", "sound"};
        for (int i = 0; i < types.length; i++) {
            if (effectiveness.get(types[i]) < 1.0) {
                System.out.println(" - " + types[i]);
            }
        }
        System.out.println("moves");
        System.out.println("");
    }

    public void levelup() { //when a pokemon level up
        if (level != max_level) { //if pokemon is not at max level
            level++;//increase level by 1
            int hploss = maxhp - currenthp;//maintain the hp loss
            maxhp = library.pokemonhp.get(name).get(level);//get the new maxhp value
            currenthp = maxhp - hploss;//calculate currenthp using hploss and maxhp
            speed = library.pokemon_speed.get(name).get(level);//get new speed
            currentxp = calcxp(); //calculate xp needed for next level
            System.out.printf("+%s+\n", "-".repeat(90)); //display message that tells player pokemon leveled up
            System.out.println(name + " leveled up!");
            if (level >= lvl_to_evolve) {//if level >= level to evolve
                evolve();//pokemon evolves
            }
        }
    }

    public void evolve() { //pokemon evolves
        System.out.printf("+%s+\n", "-".repeat(90)); //tell player pokemon is evovling
        System.out.printf("What?! %s is evolving!\n", name);
        name = nextevolution; //change the name to its next evolution's name
        int hploss = maxhp - currenthp;//maintain hp loss
        maxhp = library.pokemonhp.get(name).get(level);//get new maxhp value
        currenthp = maxhp - hploss;//calculate currenthp using hploss and maxhp
        speed = library.pokemon_speed.get(name).get(level);//get new speed
        nextevolution = library.evolution.get(name);//get new nexevolution
        weight = library.pokemon_weight.get(name);//get new weight
        speed = library.pokemon_speed.get(name).get(level);//get new Speed
        cute = library.pokemon_cute.get(name);//get new cute
        move1 = library.pokemon_moveset.get(name).get("move1");//get new move1-4
        move2 = library.pokemon_moveset.get(name).get("move2");
        move3 = library.pokemon_moveset.get(name).get("move3");
        move4 = library.pokemon_moveset.get(name).get("move4");
        type1 = library.pokemon_type.get(name).get("type1");//get new type1
        type2 = library.pokemon_type.get(name).get("type2");//get new type2
        currentxp = calcxp(); //calculate xp to next level
        lvl_to_evolve = library.evo_lvl.get(name);//get new lvl_to_evolve
        System.out.println("It evolved into " + name + " !");//tell player what pokemon has evolved to
    }

    public boolean inBattle() { //return inbattle
        return inbattle;
    }

    public boolean wild() {//return wild
        return wild;
    }

    public void caught() {//wild pokemon is caught
        wild = false; //so not wild anymore
        inbattle = false; //not in battle anymore
    }

    public void changebattlestatus(boolean s) { //change battle status
        inbattle = s;
    }

    public boolean isFaint() { //return faint
        return faint;
    }

    public void setcurrenthp(int h) { //set currenthp
        currenthp = h;
    }

    public void setxp(int x) { //set currentxp
        currentxp = x;
    }

    public void takedmg(int d) {//pokemon takes damage
        System.out.printf("+%s+\n", "-".repeat(90));//display how much damage took
        System.out.println(name + " took " + d + " damage");
        if (d < currenthp) {//if damage < currenthp
            currenthp = currenthp - d;
        } else { //if damage>=currenthp
            currenthp = 0;
            faint = true;//pokemon faints
            System.out.printf("+%s+\n", "-".repeat(90));
            System.out.println(name + " has fainted");//display message to tell player
        }
    }

    public void obtainxp(int e) { //pokemon obtains xp
        if (level != max_level) {//if pokemon is not max level
            if (e != 0) { //if e is not 0
                System.out.printf("+%s+\n", "-".repeat(90));

                if (e < currentxp) {//if e<currentxp
                    currentxp = currentxp - e;
                    System.out.println(name + " obtained " + e + " xp!");//display how much xp obtained
                } else {//if e>=currentxp
                    System.out.println(name + " obtained " + currentxp + " xp!");//display how much xp obtained
                    e = e - currentxp;
                    currentxp = 0;
                    levelup();//pokemon levels up
                    obtainxp(e);//call own method until e becomes 0 or less than currentxp
                }
            }
        } else { //if pokemon is at max level, display message below
            System.out.println("This pokemon is at max level and will not obatin any more xp");
        }
    }

    public void heal(int h) {//when pokemon heals
        System.out.printf("+%s+\n", "-".repeat(90));//display the heal amount
        System.out.println(name + " is healed for " + h + " hp");

        if ((currenthp + h) >= maxhp) {//if overhealed
            currenthp = maxhp;//set currenthp back to maxhp
        } else {//if not overhealed
            currenthp = currenthp + h;
        }
        System.out.println("Current hp: " + currenthp + " / " + maxhp);//display hp status
    }

    public void fullheal() {//pokemon full heals
        System.out.printf("+%s+\n", "-".repeat(90));
        currenthp = maxhp;
        System.out.println(name + " is healed to full hp");//display full heal message
    }

    public void fullres() {//pokemon full restores
        faint = false; //pokemon is revived
        currenthp = maxhp; //pokemon is full healed
    }

    public void revive() { //pokemon is revived
        System.out.printf("+%s+\n", "-".repeat(90));
        System.out.println(name + " is revived");//display revive message

        faint = false; //pokemon is not fainted anymore
        currenthp = maxhp / 2; //pokemon heals up to half of max hp
        System.out.println("Current hp: " + currenthp + " / " + maxhp);//display current hp status
    }
}