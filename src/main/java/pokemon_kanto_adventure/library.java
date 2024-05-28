package pokemon_kanto_adventure;

import java.io.*;
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
            InputStream inputStream = library.class.getResourceAsStream("/pokemonhp.csv");
            if (inputStream != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                br.readLine();  // Assuming this reads the header line
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
                br.close();
            } else {
                System.out.println("File not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readpokemonevo() {
        try {
            InputStream inputStream = library.class.getResourceAsStream("/pokemon_evo.csv");
            if (inputStream != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                br.readLine();  // Assuming this reads the header line
                String line = "";
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    String pokemon_name = values[0];
                    String pokemon_evo = values[1];
                    evolution.put(pokemon_name, pokemon_evo);
                }
                br.close();
            } else {
                System.out.println("File not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readevolvelevel() {
        try {
            InputStream inputStream = library.class.getResourceAsStream("/pokemon_evo_level.csv");
            if (inputStream != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                br.readLine();  // Assuming this reads the header line
                String line = "";
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    String pokemon_name = values[0];
                    int evolve_lvl = Integer.parseInt(values[1]);
                    evo_lvl.put(pokemon_name, evolve_lvl);
                }
                br.close();
            } else {
                System.out.println("File not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readpokemonspeed() {
        try {
            InputStream inputStream = library.class.getResourceAsStream("/pokemon_speed.csv");
            if (inputStream != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                br.readLine();  // Assuming this reads the header line
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
                br.close();
            } else {
                System.out.println("File not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readpokemoneffectiveness() {
        try {
            InputStream inputStream = library.class.getResourceAsStream("/pokemon_effectiveness.csv");
            if (inputStream != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                br.readLine();  // Assuming this reads the header line
                String line;
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
                br.close();
            } else {
                System.out.println("File not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readpokemoncute() {
        try {
            InputStream inputStream = library.class.getResourceAsStream("/pokemon_cute.csv");
            if (inputStream != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                br.readLine();  // Assuming this reads the header line
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    String pokemon_name = values[0];
                    boolean cute = "T".equals(values[1]);
                    pokemon_cute.put(pokemon_name, cute);
                }
                br.close();
            } else {
                System.out.println("File not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readpokemonitems() {
        try {
            InputStream inputStream = library.class.getResourceAsStream("/pokemon_items.csv");
            if (inputStream != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                br.readLine();  // Assuming this reads the header line
                String line;
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
                    itemeff.put("pokemon_50%", Integer.valueOf(values[8]));
                    itemeff.put("pokemon_25%", Integer.valueOf(values[9]));
                    itemeff.put("revive%", Integer.valueOf(values[10]));
                    itemeff.put("price", Integer.valueOf(values[11]));
                    pokemon_items.put(item_name, itemeff);
                }
                br.close();
            } else {
                System.out.println("File not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readmovedmg() {
        try {
            InputStream inputStream = library.class.getResourceAsStream("/pokemon_move_dmg.csv");
            if (inputStream != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                br.readLine();  // Assuming this reads the header line
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    String move_name = values[0];
                    HashMap<Integer, Integer> movedmg = new HashMap<>();
                    for (int i = 1; i < values.length; i++) {
                        int dmg = Integer.parseInt(values[i]);
                        movedmg.put(i, dmg);
                    }
                    move_dmg.put(move_name, movedmg);
                }
                br.close();
            } else {
                System.out.println("File not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readmoveorder() {
        try {
            InputStream inputStream = library.class.getResourceAsStream("/pokemon_move_order.csv");
            if (inputStream != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                br.readLine();  // Assuming this reads the header line
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    String move_name = values[0];
                    int moveorder = Integer.parseInt(values[1]);
                    move_order.put(move_name, moveorder);
                }
                br.close();
            } else {
                System.out.println("File not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readmovestat() {
        try {
            InputStream inputStream = library.class.getResourceAsStream("/pokemon_move_stat.csv");
            if (inputStream != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                br.readLine();  // Assuming this reads the header line
                String line;
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
                br.close();
            } else {
                System.out.println("File not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void readmovecat() {
        try {
            InputStream inputStream = library.class.getResourceAsStream("/pokemon_movelist.csv");
            if (inputStream != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                br.readLine();  // Assuming this reads the header line
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    String move_name = values[0];
                    String category = values[1];
                    move_cat.put(move_name, category);
                }
                br.close();
            } else {
                System.out.println("File not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readpokemonmoveset() {
        try {
            InputStream inputStream = library.class.getResourceAsStream("/pokemon_moveset.csv");
            if (inputStream != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                br.readLine();  // Assuming this reads the header line
                String line;
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
                br.close();
            } else {
                System.out.println("File not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readmovetype() {
        try {
            InputStream inputStream = library.class.getResourceAsStream("/pokemon_move_type.csv");
            if (inputStream != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                br.readLine();  // Assuming this reads the header line
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    String move_name = values[0];
                    String type = values[1];
                    move_type.put(move_name, type);
                }
                br.close();
            } else {
                System.out.println("File not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readpokemontype() {
        try {
            InputStream inputStream = library.class.getResourceAsStream("/pokemon_type.csv");
            if (inputStream != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                br.readLine();  // Assuming this reads the header line
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    String pokemon_name = values[0];
                    HashMap<String, String> type = new HashMap<>();
                    type.put("type1", values[1]);
                    type.put("type2", values[2]);
                    pokemon_type.put(pokemon_name, type);
                }
                br.close();
            } else {
                System.out.println("File not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readpokemonweight() {
        try {
            InputStream inputStream = library.class.getResourceAsStream("/pokemon_weight.csv");
            if (inputStream != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                br.readLine();  // Assuming this reads the header line
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    String pokemon_name = values[0];
                    double weight = Double.parseDouble(values[1]);
                    pokemon_weight.put(pokemon_name, weight);
                }
                br.close();
            } else {
                System.out.println("File not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readmovedescription() {
        try {
            InputStream inputStream = library.class.getResourceAsStream("/move_description.csv");
            if (inputStream != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                br.readLine();  // Assuming this reads the header line
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    String move_name = values[0];
                    String description = values[1];
                    move_description.put(move_name, description);
                }
                br.close();
            } else {
                System.out.println("File not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readAllTrainers() {
        try {
            InputStream inputStream = library.class.getResourceAsStream("/trainers.csv");
            if (inputStream != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                br.readLine();  // Assuming this reads the header line
                String line;
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
                br.close();
            } else {
                System.out.println("File not found");
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
}
