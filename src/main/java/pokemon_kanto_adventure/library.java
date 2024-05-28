package pokemon_kanto_adventure;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class library {
    protected static Map<String, Integer> kantoMap = new Map<>();
    protected static HashMap<String, HashMap<Integer, Integer>> pokemonhp = new HashMap<>();
    protected static HashMap<String, String> evolution = new HashMap<>();
    protected static HashMap<String, Integer> evo_lvl = new HashMap<>();
    protected static HashMap<String, HashMap<Integer, Integer>> pokemon_speed = new HashMap<>();
    protected static HashMap<String, HashMap<String, Double>> pokemon_effectiveness = new HashMap<>();
    protected static HashMap<String, Boolean> pokemon_cute = new HashMap<>();
    protected static HashMap<String, HashMap<String, Integer>> pokemon_items = new HashMap<>();
    protected static HashMap<String, HashMap<Integer, Integer>> move_dmg = new HashMap<>();
    protected static HashMap<String, Integer> move_order = new HashMap<>();
    protected static HashMap<String, HashMap<String, Double>> move_stat = new HashMap<>();
    protected static HashMap<String, String> move_cat = new HashMap<>();
    protected static HashMap<String, HashMap<String, String>> pokemon_moveset = new HashMap<>();
    protected static HashMap<String, String> move_type = new HashMap<>();
    protected static HashMap<String, HashMap<String, String>> pokemon_type = new HashMap<>();
    protected static HashMap<String, Double> pokemon_weight = new HashMap<>();
    protected static HashMap<String, String> move_description = new HashMap<>();
    protected static HashMap<String, ArrayList<Pokemon>> Trainers = new HashMap<>();
    protected static HashMap<String, Integer> TrainerReward = new HashMap<>();

    public static void readallfiles() {
        readpokemonhp();
        readpokemonevo();
        readevolvelevel();
        readpokemonspeed();
        readpokemoneffectiveness();
        readpokemoncute();
        readpokemonitems();
        readmovedmg();
        readmoveorder();
        readmovestat();
        readmovecat();
        readpokemonmoveset();
        readmovetype();
        readpokemontype();
        readpokemonweight();
        readmovedescription();
        initializeMap();
        readAllTrainers();
    }

    public static void readpokemonhp() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Pokemon_Kanto_Adventure/Files/pokemonhp.csv"));
            br.readLine();
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String pokemon_name = values[0];
                HashMap<Integer, Integer> pokehp = new HashMap<>();
                for (int i = 1; i <= 100; i++) {
                    int hp = Integer.parseInt(values[i]);
                    pokehp.put(i, hp);
                }
                pokemonhp.put(pokemon_name, pokehp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readpokemonevo() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Pokemon_Kanto_Adventure/Files/pokemon_evo.csv"));
            br.readLine();
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String pokemon_name = values[0];
                String pokemon_evo = values[1];
                evolution.put(pokemon_name, pokemon_evo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readevolvelevel() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Pokemon_Kanto_Adventure/Files/pokemon_evo_level.csv"));
            br.readLine();
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String pokemon_name = values[0];
                int evolve_lvl = Integer.parseInt(values[1]);
                evo_lvl.put(pokemon_name, evolve_lvl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readpokemonspeed() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Pokemon_Kanto_Adventure/Files/pokemon_speed.csv"));
            br.readLine();
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String pokemon_name = values[0];
                HashMap<Integer, Integer> pokespeed = new HashMap<>();
                for (int i = 1; i <= 100; i++) {
                    int speed = Integer.parseInt(values[i]);
                    pokespeed.put(i, speed);
                }
                pokemon_speed.put(pokemon_name, pokespeed);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readpokemoneffectiveness() {
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("Pokemon_Kanto_Adventure/Files/pokemon_effectiveness.csv"));
            br.readLine();
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String pokemon_name = values[0];
                HashMap<String, Double> pokeeff = new HashMap<>();
                pokeeff.put("normal", Double.valueOf(values[1]));
                pokeeff.put("fire", Double.valueOf(values[2]));
                pokeeff.put("water", Double.valueOf(values[3]));
                pokeeff.put("electric", Double.valueOf(values[4]));
                pokeeff.put("grass", Double.valueOf(values[5]));
                pokeeff.put("fighting", Double.valueOf(values[6]));
                pokeeff.put("poison", Double.valueOf(values[7]));
                pokeeff.put("ground", Double.valueOf(values[8]));
                pokeeff.put("flying", Double.valueOf(values[9]));
                pokeeff.put("psychic", Double.valueOf(values[10]));
                pokeeff.put("bug", Double.valueOf(values[11]));
                pokeeff.put("rock", Double.valueOf(values[12]));
                pokeeff.put("ghost", Double.valueOf(values[13]));
                pokeeff.put("dark", Double.valueOf(values[14]));
                pokeeff.put("steel", Double.valueOf(values[15]));
                pokeeff.put("sound", Double.valueOf(values[16]));
                pokemon_effectiveness.put(pokemon_name, pokeeff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readpokemoncute() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Pokemon_Kanto_Adventure/Files/pokemon_cute.csv"));
            br.readLine();
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String pokemon_name = values[0];
                boolean cute = false;
                if (values[1].equals("T")) {
                    cute = true;
                } else {
                    cute = false;
                }
                pokemon_cute.put(pokemon_name, cute);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readpokemonitems() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Pokemon_Kanto_Adventure/Files/pokemon_items.csv"));
            br.readLine();
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String item_name = values[0];
                HashMap<String, Integer> itemeff = new HashMap<>();
                itemeff.put("heal", Integer.valueOf(values[1]));
                itemeff.put("atk", Integer.valueOf(values[2]));
                itemeff.put("def", Integer.valueOf(values[3]));
                itemeff.put("sp", Integer.valueOf(values[4]));
                itemeff.put("catch", Integer.valueOf(values[5]));
                itemeff.put("base_catch_rate", Integer.valueOf(values[6]));
                itemeff.put("pokemon_75%", Integer.valueOf(values[7]));
                itemeff.put("pokemon_50&", Integer.valueOf(values[8]));
                itemeff.put("pokemon_25%", Integer.valueOf(values[9]));
                itemeff.put("revive%", Integer.valueOf(values[10]));
                itemeff.put("price", Integer.valueOf(values[11]));
                pokemon_items.put(item_name, itemeff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readmovedmg() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Pokemon_Kanto_Adventure/Files/pokemon_move_dmg.csv"));
            br.readLine();
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String move_name = values[0];
                HashMap<Integer, Integer> movedmg = new HashMap<>();
                for (int i = 1; i <= 100; i++) {
                    int dmg = Integer.parseInt(values[i]);
                    movedmg.put(i, dmg);
                }
                move_dmg.put(move_name, movedmg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readmoveorder() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Pokemon_Kanto_Adventure/Files/pokemon_move_order.csv"));
            br.readLine();
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String move_name = values[0];
                int moveorder = Integer.parseInt(values[1]);
                move_order.put(move_name, moveorder);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readmovestat() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Pokemon_Kanto_Adventure/Files/pokemon_move_stat.csv"));
            br.readLine();
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String move_name = values[0];
                HashMap<String, Double> movstat = new HashMap<>();
                movstat.put("atk", Double.valueOf(values[1]));
                movstat.put("def", Double.valueOf(values[2]));
                movstat.put("sp", Double.valueOf(values[3]));
                movstat.put("healratio", Double.valueOf(values[4]));
                movstat.put("foe_atk", Double.valueOf(values[5]));
                movstat.put("foe_def", Double.valueOf(values[6]));
                movstat.put("foe_sp", Double.valueOf(values[7]));
                move_stat.put(move_name, movstat);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readmovecat() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Pokemon_Kanto_Adventure/Files/pokemon_movelist.csv"));
            br.readLine();
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String move_name = values[0];
                String category = values[1];
                move_cat.put(move_name, category);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readpokemonmoveset() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Pokemon_Kanto_Adventure/Files/pokemon_moveset.csv"));
            br.readLine();
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String move_name = values[0];
                HashMap<String, String> movset = new HashMap<>();
                movset.put("move1", values[1]);
                movset.put("move2", values[2]);
                movset.put("move3", values[3]);
                movset.put("move4", values[4]);
                pokemon_moveset.put(move_name, movset);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readmovetype() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Pokemon_Kanto_Adventure/Files/pokemon_move_type.csv"));
            br.readLine();
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String move_name = values[0];
                String type = values[1];
                move_type.put(move_name, type);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readpokemontype() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Pokemon_Kanto_Adventure/Files/pokemon_type.csv"));
            br.readLine();
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String pokemon_name = values[0];
                HashMap<String, String> type = new HashMap<>();
                type.put("type1", values[1]);
                type.put("type2", values[2]);
                pokemon_type.put(pokemon_name, type);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readpokemonweight() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Pokemon_Kanto_Adventure/Files/pokemon_weight.csv"));
            br.readLine();
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String pokemon_name = values[0];
                double weight = Double.valueOf(values[1]);
                pokemon_weight.put(pokemon_name, weight);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readmovedescription() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Pokemon_Kanto_Adventure/Files/move_description.csv"));
            br.readLine();
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String move_name = values[0];
                String desc = values[1];
                move_description.put(move_name, desc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initializeMap() {
        kantoMap.addCity("Pallet Town");
        kantoMap.addCity("Viridian City");
        kantoMap.addCity("Pewter City");
        kantoMap.addCity("Cerulean City");
        kantoMap.addCity("Saffron City");
        kantoMap.addCity("Celadon City");
        kantoMap.addCity("Lavender Town");
        kantoMap.addCity("Vermillion City");
        kantoMap.addCity("Fuschia City");
        kantoMap.addCity("Cinnabar Island");
        kantoMap.addPath("Pallet Town", "Viridian City", 5);
        kantoMap.addPath("Pallet Town", "Cinnabar Island", 7);
        kantoMap.addPath("Viridian City", "Pewter City", 8);
        kantoMap.addPath("Pewter City", "Cerulean City", 12);
        kantoMap.addPath("Cerulean City", "Saffron City", 6);
        kantoMap.addPath("Cerulean City", "Lavender Town", 9);
        kantoMap.addPath("Saffron City", "Lavender Town", 3);
        kantoMap.addPath("Saffron City", "Celadon City", 4);
        kantoMap.addPath("Saffron City", "Vermillion City", 3);
        kantoMap.addPath("Vermillion City", "Lavender Town", 5);
        kantoMap.addPath("Fuschia City", "Lavender Town", 11);
        kantoMap.addPath("Vermillion City", "Fuschia City", 7);
        kantoMap.addPath("Celadon City", "Fuschia City", 10);
        kantoMap.addPath("Fuschia City", "Cinnabar Island", 5);
    }

    public static void readAllTrainers() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Pokemon_Kanto_Adventure/Files/trainers.csv"));
            br.readLine();
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String trainer_name = values[0];
                ArrayList<Pokemon> pokemons = new ArrayList<>();
                for (int i = 1; i < values.length - 1; i++) {
                    if (!values[i].equals("null")) {
                        String[] poke_desc = values[i].split("\\|");
                        String pokemon_Name = poke_desc[0];
                        int pokemon_Level = Integer.parseInt(poke_desc[1]);
                        boolean wild = false;
                        Pokemon pokepoke = new Pokemon(pokemon_Name, pokemon_Level, wild);
                        pokemons.add(pokepoke);
                    }
                }
                int money = Integer.parseInt(values[values.length - 1]);
                Trainers.put(trainer_name, pokemons);
                TrainerReward.put(trainer_name, money);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
