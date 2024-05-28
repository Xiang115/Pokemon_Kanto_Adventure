package pokemon_kanto_adventure;

import java.util.HashMap;

public class Pokemon {
    private String name;
    private int max_level = 100;
    private int level;
    private int currenthp;
    private int maxhp;
    private int currentxp;
    private String nextevolution;
    private int speed;
    private double weight;
    private boolean cute;
    private String move1;
    private String move2;
    private String move3;
    private String move4;
    private String type1;
    private String type2;
    private boolean faint;
    private boolean wild;
    private boolean inbattle;
    private int lvl_to_evolve;
    private HashMap<String, Double> effectiveness;

    public Pokemon(String n) { // to decalre a level 1 pokemon
        name = n;
        level = 1;
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
        lvl_to_evolve = library.evo_lvl.get(name);
        currentxp = calcxp();
        wild = true;
        inbattle = false;
        faint = false;
        effectiveness = library.pokemon_effectiveness.get(name);
    }

    public Pokemon(String n, int l, boolean w) { // to declare a pokemon with level and name and set whether it is wild
                                                 // or not
        name = n;
        level = l;
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
        wild = w;
        inbattle = true;
        faint = false;
        effectiveness = library.pokemon_effectiveness.get(name);
    }

    public Pokemon(String n, int l) { // to declare a pokemon with level and name
        name = n;
        level = l;
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

    public int findmaxhp() {
        return maxhp;
    }

    public int findcurrenthp() {
        return currenthp;
    }

    public int calcxp() {
        if (level <= 10) {
            return 100;
        } else if (level <= 30) {
            return 200;
        } else if (level < max_level) {
            return 300;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public boolean findcute() {
        return cute;
    }

    public double findweight() {
        return weight;
    }

    public int findspeed() {
        return speed;
    }

    public String findname() {
        return name;
    }

    public int findlvl() {
        return level;
    }

    public String findmov1() {
        return move1;
    }

    public String findmov2() {
        return move2;
    }

    public String findmov3() {
        return move3;
    }

    public String findmov4() {
        return move4;
    }

    public String findtype1() {
        return type1;
    }

    public String findtype2() {
        return type2;
    }

    public int findcurrentxp() {
        return currentxp;
    }

    public HashMap<String, Double> findEffectiveness() {
        return effectiveness;
    }

    public double findEffectiveness(String t) {
        return effectiveness.get(t);
    }

    public void showPokemonInfo() {
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
        String[] types = { "normal", "fire", "water", "electric", "grass", "fighting", "poison", "ground", "flying",
                "psychic", "bug", "rock", "ghost", "dark", "steel", "sound" };
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
    }

    public void showWeakness() {
        System.out.println(name + " is weak against: ");
        String[] types = { "normal", "fire", "water", "electric", "grass", "fighting", "poison", "ground", "flying",
                "psychic", "bug", "rock", "ghost", "dark", "steel", "sound" };
        for (int i = 0; i < types.length; i++) {
            if (effectiveness.get(types[i]) > 1.0) {
                System.out.println(" - " + types[i]);
            }
        }
        System.out.println("moves");
        System.out.println("");
    }

    public void showResistance() {
        System.out.println(name + " resists: ");
        String[] types = { "normal", "fire", "water", "electric", "grass", "fighting", "poison", "ground", "flying",
                "psychic", "bug", "rock", "ghost", "dark", "steel", "sound" };
        for (int i = 0; i < types.length; i++) {
            if (effectiveness.get(types[i]) < 1.0) {
                System.out.println(" - " + types[i]);
            }
        }
        System.out.println("moves");
        System.out.println("");
    }

    public void levelup() {
        if (level != max_level) {
            level++;
            int hploss = maxhp - currenthp;
            maxhp = library.pokemonhp.get(name).get(level);
            currenthp = maxhp - hploss;
            speed = library.pokemon_speed.get(name).get(level);
            currentxp = calcxp();
            System.out.printf("+%s+\n", "-".repeat(90));
            System.out.println(name + " leveled up!");
            if (level >= lvl_to_evolve) {
                evolve();
            }
        }
    }

    public void evolve() {
        System.out.printf("+%s+\n", "-".repeat(90));
        System.out.printf("What?! %s is evolving!\n", name);
        name = nextevolution;
        int hploss = maxhp - currenthp;
        maxhp = library.pokemonhp.get(name).get(level);
        currenthp = maxhp - hploss;
        speed = library.pokemon_speed.get(name).get(level);
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
        System.out.println("It evolved into " + name + " !");
    }

    public boolean inBattle() {
        return inbattle;
    }

    public boolean wild() {
        return wild;
    }

    public void caught() {
        wild = false;
    }

    public void changebattlestatus(boolean s) {
        inbattle = s;
    }

    public boolean isFaint() {
        return faint;
    }

    public void setcurrenthp(int h) {
        currenthp = h;
    }

    public void setxp(int x) {
        currentxp = x;
    }

    public void takedmg(int d) {
        System.out.printf("+%s+\n", "-".repeat(90));
        System.out.println(name + " took " + d + " damage");
        if (d < currenthp) {
            currenthp = currenthp - d;
        } else {
            currenthp = 0;
            faint = true;
            System.out.printf("+%s+\n", "-".repeat(90));
            System.out.println(name + " has fainted");
        }
    }

    public void obtainxp(int e) {
        if (level != max_level) {
            System.out.printf("+%s+\n", "-".repeat(90));

            if (e < currentxp) {
                currentxp = currentxp - e;
                System.out.println(name + " obtained " + e + " xp!");
            } else {
                System.out.println(name + " obtained " + currentxp + " xp!");
                e = e - currentxp;
                currentxp = 0;
                levelup();
                obtainxp(e);
            }
        } else {
            System.out.println("This pokemon is at max level and will not obatin any more xp");
        }
    }

    public void heal(int h) {
        System.out.printf("+%s+\n", "-".repeat(90));
        System.out.println(name + " is healed for " + h + " hp");

        if ((currenthp + h) >= maxhp) {
            currenthp = maxhp;
        } else {
            currenthp = currenthp + h;
        }
        System.out.println("Current hp: " + currenthp + " / " + maxhp);
    }

    public void fullheal() {
        System.out.printf("+%s+\n", "-".repeat(90));
        currenthp = maxhp;
        System.out.println(name + " is healed to full hp");
    }

    public void fullres() {
        faint = false;
        currenthp = maxhp;
    }

    public void revive() {
        System.out.printf("+%s+\n", "-".repeat(90));
        System.out.println(name + " is revived");

        faint = false;
        currenthp = maxhp / 2;
        System.out.println("Current hp: " + currenthp + " / " + maxhp);
    }
}