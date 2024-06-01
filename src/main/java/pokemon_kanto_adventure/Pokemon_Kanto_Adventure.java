package pokemon_kanto_adventure;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

//main class for the pokemon kanto adventure
public class Pokemon_Kanto_Adventure {
    // used to store the current slot number
    private static int slotNumber;
    // used to declare a Logger object
    private static final Logger logger = Logger.getLogger(Pokemon_Kanto_Adventure.class.getName());

    // main method
    public static void main(String[] args) {
        Load load = new Load();
        library.readallfiles();// load every data in all files
        while (true) {
            title();// display the pokemon title
            Player player = selectSave(load.getConnection());// load player progress into player
            if (player == null) {
                ;
            } else {
                selectionPanel(player);// if player is not null, means a valid choice is chosen, pass the player
                // containing the loaded progress to selection panel
            }
        }
    }

    // print pokemon title when boot game
    public static void title() {
        try {
            InputStream inputStream = Pokemon_Kanto_Adventure.class.getResourceAsStream("/PokemonLogo.txt");
            if (inputStream != null) {
                Scanner sc = new Scanner(inputStream);
                while (sc.hasNextLine()) {
                    System.out.println(sc.nextLine());
                }
                sc.close();
            } else {
                System.out.println("File not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //used to check for the player exist or not
    public static Player selectSave(Connection con) {
        Scanner sc = new Scanner(System.in);
        boolean[] saveExists = new boolean[3];// Tracks if save slots exist
        boolean[] containsData = new boolean[3]; // Tracks if save slots contain data

        try {
            printSaveSlots(con, saveExists, containsData);
            System.out.print("Your choice: ");
            String choice = sc.nextLine().trim();
            return processSaveChoice(con, choice, saveExists, containsData);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error selecting save", e);
        }
        return null;
    }

    // used to print the saveslot menu based on the sqlite database
    private static void printSaveSlots(Connection con, boolean[] saveExists, boolean[] containsData)
            throws SQLException {
        System.out.printf("+%s+\n", "-".repeat(90));
        System.out.println("Welcome to Pokemon - Kanto Adventures");
        System.out.printf("+%s+\n", "-".repeat(90));
        System.out.println("[1] Load Game:");

        for (int i = 0; i < 3; i++) {
            String playerName = getPlayerNameFromSlot(con, i + 1);// Get player name for the slot
            saveExists[i] = playerName != null; // Update save existence status
            containsData[i] = saveExists[i];  // Update data existence status
            System.out.printf("%c. Save %d - %-15s", 'a' + i, i + 1, playerName != null ? playerName : "empty");
        }
        System.out.println();

        System.out.println("[2] Start a new Adventure:");
        for (int i = 0; i < 3; i++) {
            String msg = containsData[i] ? "Override" : "New";
            System.out.printf("%c. Save %d - %-15s", 'a' + i, i + 1, msg);
        }
        System.out.println();
        System.out.println("[3] Exit");
        System.out.printf("+%s+\n", "-".repeat(90));
    }

    // used to get the player name based on the slot number from sqlite table
    private static String getPlayerNameFromSlot(Connection con, int slot) throws SQLException {
        String query = "SELECT player_name FROM saveslots WHERE slot_number = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, slot);// Set the slot number parameter
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("player_name");
            }
        }
        return null;
    }

    // used to display the start menu choices
    private static Player processSaveChoice(Connection con, String choice, boolean[] saveExists, boolean[] containsData)
            throws SQLException {
        if (choice.length() == 2) {
            char menuChoice = choice.charAt(0);// First character of the choice
            char slotChoice = choice.charAt(1); // Second character of the choice
            int slotIndex = slotChoice - 'a'; // Convert slot choice to index

            if (menuChoice == '1' && slotIndex >= 0 && slotIndex < 3 && saveExists[slotIndex]) {
                slotNumber = slotIndex + 1;
                return loadSave(con, slotIndex + 1);
            } else if (menuChoice == '2' && slotIndex >= 0 && slotIndex < 3) {
                slotNumber = slotIndex + 1;
                return containsData[slotIndex] ? Override(slotIndex + 1) : createNewPlayer(slotIndex + 1);
            } else {
                System.out.println("Invalid choice");
            }
        } else if ("3".equals(choice)) {
            System.exit(0);
        } else {
            System.out.println("Invalid choice! Please choose again.");
        }
        return null;
    }

    // Override after select
    public static Player Override(int slotNumber) {
        System.out.println("Overriding Save Slot " + slotNumber);
        return createNewPlayer(slotNumber);
    }

    // used to create a new player
    public static Player createNewPlayer(int slotNumber) {
        Scanner sc = new Scanner(System.in);
        System.out.printf("+%s+\n", "-".repeat(90));
        System.out.println("OAK: Hello there! Welcome to the world of Pokémon! My name is Oak!\r\n" + //
                "People call me the Pokémon Prof! This world is inhabited by creatures\r\n" + //
                "called Pokémon! For some people, Pokémon are pets. Others use them for\r\n" + //
                "fights. Myself... I study Pokémon as a profession.");
        System.out.println();
        System.out.println("OAK: First, what is your name?");
        System.out.printf("+%s+\n", "-".repeat(90));
        System.out.print("Enter your name: ");
        String playerName = sc.nextLine();
        System.out.printf("+%s+\n", "-".repeat(90));

        // Connect to the database and perform deletion and insertion
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite:pokemon.db");
            con.setAutoCommit(false);

            // Delete the existing slot
            PreparedStatement deleteStmt = con.prepareStatement("DELETE FROM saveslots WHERE slot_number = ?");
            deleteStmt.setInt(1, slotNumber);
            deleteStmt.executeUpdate();
            deleteStmt.close();

            // Delete the existing slot
            deleteStmt = con.prepareStatement("DELETE FROM badges WHERE slot_number = ?");
            deleteStmt.setInt(1, slotNumber);
            deleteStmt.executeUpdate();
            deleteStmt.close();

            deleteStmt = con.prepareStatement("DELETE FROM items WHERE slot_number = ?");
            deleteStmt.setInt(1, slotNumber);
            deleteStmt.executeUpdate();
            deleteStmt.close();

            deleteStmt = con.prepareStatement("DELETE FROM pc WHERE slot_number = ?");
            deleteStmt.setInt(1, slotNumber);
            deleteStmt.executeUpdate();
            deleteStmt.close();

            // Insert the new data for the slot
            PreparedStatement insertStmt = con
                    .prepareStatement("INSERT INTO saveslots (slot_number, player_name) VALUES (?, ?)");
            insertStmt.setInt(1, slotNumber);
            insertStmt.setString(2, playerName);
            insertStmt.executeUpdate();
            insertStmt.close();

            con.commit();
            con.close();

        } catch (SQLException e) {
            Logger.getLogger(Pokemon_Kanto_Adventure.class.getName()).log(Level.SEVERE, null, e);
        }

        Player player = new Player(playerName);
        System.out.printf("OAK: Right! So your name is %s! Welcome to the world of Pokemon.\r\n" + //
                "It's time to choose your starting pokemon.\n", player.getName());
        System.out.println("It's time to choose your starting pokemon.");
        System.out.printf("+%s+\n", "-".repeat(90));
        System.out.println("[1] Bulbasaur [Grass - Level 5]");
        System.out.println("[2] Squirtle [Water - Level 5]");
        System.out.println("[3] Charmander [Fire - Level 5]");
        System.out.printf("+%s+\n", "-".repeat(90));
        System.out.print("Your choice: ");
        String choice = sc.nextLine();
        System.out.printf("+%s+\n", "-".repeat(90));
        boolean isValid = true;
        if (choice.length() == 1) {
            int num = Integer.parseInt(choice);
            switch (num) {
                case 1:
                    Pokemon Bulbasaur = new Pokemon("Bulbasaur", 5, false, false);
                    System.out.printf("OAK: You chose %s, an amazing choice. Best of luck!\n", Bulbasaur.findname());
                    player.addPokemon(Bulbasaur);
                    break;
                case 2:
                    Pokemon Squirtle = new Pokemon("Squirtle", 5, false, false);
                    System.out.printf("OAK: You chose %s, an amazing choice. Best of luck!\n", Squirtle.findname());
                    player.addPokemon(Squirtle);
                    break;
                case 3:
                    Pokemon Charmander = new Pokemon("Charmander", 5, false, false);
                    System.out.printf("OAK: You chose %s, an amazing choice. Best of luck!\n", Charmander.findname());
                    player.addPokemon(Charmander);
                    break;
                default:
                    isValid = false;
                    System.out.println("Invalid choice");
            }
            System.out.println(
                    "OAK: Oh, and also take these 10 Poke Balls and $1000, Poke Balls can be used to catch wild pokemons and strengthen your team, and you can use money to buy items in Poke Marts!");
            player.obtainitems("Poke Ball", 10);
            player.addMoney(1000);
            save(player);
        } else {
            System.out.println("Invalid choice.");
        }
        return isValid ? player : null;
    }

    // load save after select
    public static Player loadSave(Connection con, int slotNumber) {
        try {
            // Initialize variables to store player data
            String playerName = null;
            String[] badges = new String[8];
            int numofbadge = 0;
            Pokemon[] pokemons = new Pokemon[6];
            int money = 0;
            HashMap<String, Integer> items = new HashMap<>();
            int rivalracewins = 0;
            int battlewon = 0;
            ArrayList<Pokemon> PC = new ArrayList<>();
            String currentCity = null;

            // Query saveslots table to retrieve player's basic info
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM saveslots WHERE slot_number = ?");
            stmt.setInt(1, slotNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                playerName = rs.getString("player_name");
                numofbadge = rs.getInt("numofbadge");
                money = rs.getInt("money");
                rivalracewins = rs.getInt("rivalracewins");
                battlewon = rs.getInt("battlewon");
                currentCity = rs.getString("currentCity");

                // Retrieve and set the pokemons from the result set
                for (int i = 0; i < 6; i++) {
                    String pokemonName = rs.getString("pokemon" + (i + 1));
                    int pokemonLevel = rs.getInt("pokemon" + (i + 1) + "_level");
                    if (pokemonName != null) {
                        pokemons[i] = new Pokemon(pokemonName, pokemonLevel);
                    }
                }
            }
            stmt.close();

            // Query badges table to retrieve player's badges
            stmt = con.prepareStatement("SELECT * FROM badges WHERE slot_number = ?");
            stmt.setInt(1, slotNumber);
            rs = stmt.executeQuery();
            if (rs.next()) {
                for (int i = 0; i < 8; i++) {
                    badges[i] = rs.getString("badge_name" + (i + 1));
                }
            }
            stmt.close();

            // Query items table to retrieve player's items
            stmt = con.prepareStatement("SELECT * FROM items WHERE slot_number = ?");
            stmt.setInt(1, slotNumber);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String itemName = rs.getString("item_name");
                int itemCount = rs.getInt("item_count");
                items.put(itemName, itemCount);
            }
            stmt.close();

            // Query pc table to retrieve player's Pokémon in PC
            stmt = con.prepareStatement("SELECT * FROM pc WHERE slot_number = ?");
            stmt.setInt(1, slotNumber);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String pokemonName = rs.getString("pokemon_name");
                int pokemonLevel = rs.getInt("pokemon_level");
                Pokemon pokemon = new Pokemon(pokemonName, pokemonLevel);
                PC.add(pokemon);
            }
            stmt.close();

            // Construct the Player object using the retrieved data
            Player player = new Player(playerName);
            player.setBadges(badges);
            player.setNumberofBadges(numofbadge);
            player.setPoke1(pokemons[0]);
            player.setPoke2(pokemons[1]);
            player.setPoke3(pokemons[2]);
            player.setPoke4(pokemons[3]);
            player.setPoke5(pokemons[4]);
            player.setPoke6(pokemons[5]);
            player.setMoney(money);
            player.setItems(items);
            player.setRivalwins(rivalracewins);
            player.setVictories(battlewon);
            player.setPC(PC);
            player.setCurrentCity(currentCity);

            return player;
        } catch (SQLException e) {
            Logger.getLogger(Pokemon_Kanto_Adventure.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public static void guides() { // a method that allows the player to check guides to know better about the game
        Scanner input = new Scanner(System.in);
        loop: // loop loop will not end unless player choose to go back to last selection page
        while (true) {
            System.out.println("+--------------------Guides--------------------+");// all choices that player can choose
            System.out.println("1. List of all pokemons");
            System.out.println("2. Typings and their weaknessess and resistance");
            System.out.println("3. Stats, move order and battle selections");
            System.out.println("4. Catching pokemons");
            System.out.println("5. Gym battles, trainer battles and wild pokemon battles");
            System.out.println("6. Back");
            String choice = input.nextLine();
            switch (choice) {
                case "1":// if player choose to view all pokemons' names that is in the game
                    System.out.printf("+%s+\n", "-".repeat(90));
                    System.out.println(library.pokemonhp.keySet());
                    break;
                case "2":// print the weakness and resistances of each pokemon type
                    System.out.printf("+%s+\n", "-".repeat(90));
                    System.out.println("1.normal:");
                    System.out.println("  resistance - ghost(0x damage)");
                    System.out.println("  weakness   - fighting(2x damage)");
                    System.out.println("2.fire:");
                    System.out.println("  resistance - fire,bug,grass,steel(0.5x damage)");
                    System.out.println("  weakness   - water,ground,rock(2x damage)");
                    System.out.println("3.water:");
                    System.out.println("  resistance - fire,water,steel(0.5x damage)");
                    System.out.println("  weakness   - electric,grass(2x damage)");
                    System.out.println("4.electric:");
                    System.out.println("  resistance - electric,flying,steel(0.5x damage)");
                    System.out.println("  weakness   - ground(2x damage)");
                    System.out.println("5.grass:");
                    System.out.println("  resistance - water,electric,grass,ground(0.5x damage)");
                    System.out.println("  weakness   - fire,poison,flying(2x damage)");
                    System.out.println("6.fighting:");
                    System.out.println("  resistance - bug,rock,dark(0.5x damage)");
                    System.out.println("  weakness   - flying,psychic(2x damage)");
                    System.out.println("7.poison:");
                    System.out.println("  resistance - grass,fighting,poison,bug(0.5x damage)");
                    System.out.println("  weakness   - ground,psychic(2x damage)");
                    System.out.println("8.ground:");
                    System.out.println("  resistance - poison,rock(0.5x damage); electric(0x damage)");
                    System.out.println("  weakness   - water,grass(2x damage)");
                    System.out.println("9.flying:");
                    System.out.println("  resistance - grass,fighting,bug(0.5x damage); ground(0x damage)");
                    System.out.println("  weakness   - electric,rock(2x damage)");
                    System.out.println("10.psychic:");
                    System.out.println("  resistance - fighting,psychic(0.5x damage)");
                    System.out.println("  weakness   - bug,ghost,dark(2x damage)");
                    System.out.println("11.bug:");
                    System.out.println("  resistance - grass,fighting,ground(0.5x damage)");
                    System.out.println("  weakness   - fire,flying,rock(2x damage)");
                    System.out.println("12.rock:");
                    System.out.println("  resistance - normal,fire,poison,flying(0.5x damage)");
                    System.out.println("  weakness   - water,grass,fighting,ground,steel(2x damage)");
                    System.out.println("13.steel:");
                    System.out.println(
                            "  resistance - normal,grass,flying,psychic,bug,rock,ghost,dark,steel(0.5x damage); poison(0x damage)");
                    System.out.println("  weakness   - fire,fighting,ground(2x damage)");
                    System.out.println(
                            "Tips, when a pokemon uses a move that damages its opponent that have the same move type as either one of its type, the damage will increase by 50%");
                    break;
                case "3":// display information about stats, move order and battle selections
                    System.out.printf("+%s+\n", "-".repeat(90));
                    System.out.println("Stats:");
                    System.out.println(
                            "You and your opponents' pokemons will have attack, defense, and speed stats during battles");
                    System.out.println(
                            "The higher the attack stat, the higher the damage output of the pokemon and vice versa");
                    System.out.println(
                            "The higher the defense stat, the lower the damage received from attacks and vice versa");
                    System.out.println(
                            "Speed stats determines who moves first, if the move order of both pokemons are the same");
                    System.out.println(
                            "Each pokemon have their own speed values, and this value could be altered during battles through the speed stat");
                    System.out.println(
                            "After the speed is altered, if both pokemons have the same speed, a dice roll of 50/50 will happen, which means each pokemon have a 50% chance to move first");
                    System.out.println("If a pokemon faints before its move, it will not use that move");
                    System.out.println(
                            "Whenever you or your opponent switches pokemon, all these stats will reset, so when your stats is lowered to much, try switching pokemons to clear that debuff");
                    System.out.println("");
                    System.out.println("Move Order:");
                    System.out.println(
                            "As mentioned in the 'Stats' part, there is a move order for all the moves, some moves have higher move order, which means the pokemon will use that move first");
                    System.out.println("While some move have lower move order, which mostly powerful moves.");
                    System.out.println(
                            "If a pokemon uses a move with a higher move order, the pokemon will move first regardless of both pokemons' speed stat and vice versa");
                    System.out.println("");
                    System.out.println("Battle selections:");
                    System.out.println("You could choose to use items or swap pokemons during battles");
                    System.out.println(
                            "Both actions will be executed first before your opponent uses a move, and your pokemon will not be able to move if you do any of those actions");
                    System.out.println(
                            "To be specific, if you use an item on your pokemon, your pokemon is not able to make a move while your opponent is able to do so");
                    System.out.println(
                            "If you attempt to catch a wild pokemon but it snapped out of the Poke Ball, the wild pokemon will still make a move, while your pokemon stays still");
                    System.out.println(
                            "If you switched a pokemon, your pokemon that is just switched up will be the one who takes effect of the opponents' move");
                    System.out.println(
                            "However, if you switched because of your battling pokemon is fainted, it is after the round, so this switching is safe and your opponent pokemon will not make a move");
                    break;
                case "4":// display the guide about how to catch wild pokemons and where to get those
                    // catching items
                    System.out.printf("+%s+\n", "-".repeat(90));
                    System.out.println(
                            "You can use Poke Balls, Great Balls and Ultra Balls in your bag to catch wild pokemons during battles with them");
                    System.out.println("The lower the wild pokemons' hp the higher the chance of catching it.");
                    System.out.println("However, you cannot catch another trainer's pokemon, please keep that in mind");
                    System.out.println("You can buy these balls in the Poke Mart");
                    System.out.println("Good Luck!");
                    break;
                case "5":// display information about gym battles, trainer battles and wild pokemon
                    // battles
                    System.out.printf("+%s+\n", "-".repeat(90));
                    System.out.println("Gym battles:");
                    System.out.println(
                            "There are 8 gyms in this game, with the difficulty of the gyms from low to high as below");
                    System.out.println(
                            "Pewter City Gym < Cerulean Gym < Vermilion Gym < Celadon Gym < Fuchsia City Gym = Saffron City Gym < Cinnabar Island Gym < Viridian City Gym");
                    System.out.println(
                            "To complete your journey, you have to defeat all 8 gym leaders and obtain all 8 gym badges from them");
                    System.out.println(
                            "With Giovanni in Viridian City Gym as the strongest gym leader, you have to obtain all 7 other gym badges to prove that you are a worthy opponent for the final boss");
                    System.out.println("All gyms can only be challenged once");
                    System.out.println("");
                    System.out.println("Trainer battles:");
                    System.out.println(
                            "Aside from gym battles, there are trainers that would like to battle you, you can defeat their pokemons to earn xp and money");
                    System.out.println("These trainers can be challenged more than one time");
                    System.out.println("");
                    System.out.println("Wild pokemon battles:");
                    System.out.println("You can choose to fight different wild pokemons in different areas");
                    System.out.println(
                            "The wild pokemon you encounter and their levels will be random based on the areas");
                    System.out
                            .println("You can use pokeballs to catch them or defeat them to gain xp for your pokemons");
                    System.out.println(
                            "There is a special encounter, which is Snorlax, which you can choose in some areas");
                    System.out.println(
                            "Snorlax is a super powerful pokemon, with high hp, strong moves but super slow speed");
                    System.out.println(
                            "So prepare well before you attempt to catch it as it might wipe out your whole team if not careful");
                    break;
                case "6":// player choose to go back to last selection panel, end loop
                    break loop;
                default:// if player enters an invalid choice,display message below
                    System.out.println("Invalid choice, please choose again.");
            }
        }
    }

    public static void save(Player player) {
        // SQL statements to insert or update player data, badges, items, and Pokémon in the database
        String insertSaveSlot = "REPLACE INTO saveslots (player_name, slot_number, numofbadge, pokemon1, pokemon1_level, "
                + "pokemon2, pokemon2_level, pokemon3, pokemon3_level, pokemon4, pokemon4_level, pokemon5, pokemon5_level, "
                + "pokemon6, pokemon6_level, money, rivalracewins, battlewon, currentCity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String insertBadge = "REPLACE INTO badges (slot_number, badge_name1, badge_name2, badge_name3, badge_name4, " +
                "badge_name5, badge_name6, badge_name7, badge_name8) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String insertItem = "REPLACE INTO items (slot_number, item_name, item_count) VALUES (?, ?, ?)";
        String insertPokemon = "REPLACE INTO pc (slot_number, pokemon_name, pokemon_level) VALUES (?, ?, ?)";

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:pokemon.db")) {

            // Save player data in the saveslots table
            try (PreparedStatement ps = con.prepareStatement(insertSaveSlot)) {
                ps.setString(1, player.getName());
                ps.setInt(2, slotNumber);
                ps.setInt(3, player.getNumberofBadges());

                ps.setString(4, getPokemonName(player.findPoke1()));
                ps.setInt(5, getPokemonLevel(player.findPoke1()));
                ps.setString(6, getPokemonName(player.findPoke2()));
                ps.setInt(7, getPokemonLevel(player.findPoke2()));
                ps.setString(8, getPokemonName(player.findPoke3()));
                ps.setInt(9, getPokemonLevel(player.findPoke3()));
                ps.setString(10, getPokemonName(player.findPoke4()));
                ps.setInt(11, getPokemonLevel(player.findPoke4()));
                ps.setString(12, getPokemonName(player.findPoke5()));
                ps.setInt(13, getPokemonLevel(player.findPoke5()));
                ps.setString(14, getPokemonName(player.findPoke6()));
                ps.setInt(15, getPokemonLevel(player.findPoke6()));

                ps.setInt(16, player.findMoney());// Set player's money
                ps.setInt(17, player.getrivalwins()); // Set number of rival race wins
                ps.setInt(18, player.getvictories()); // Set number of battles won
                ps.setString(19, player.findCurrentCity()); // Set current city
                ps.executeUpdate(); // Execute the update
            }

            // Save badge
            try (PreparedStatement ps = con.prepareStatement(insertBadge)) {
                int count = 2;// Initialize the count to start from the second parameter
                ps.setInt(1, slotNumber); // Set slot number
                for (int i = 0; i < player.getbadges().length; i++) {
                    if (player.getbadges()[i] != null && !player.getbadges()[i].isEmpty()) {
                        ps.setString(count, player.getbadges()[i]); // Store badge name directly
                    } else {
                        ps.setNull(count, java.sql.Types.VARCHAR); // Set badge name as null if it's empty or null
                    }
                    count++;
                }
                ps.executeUpdate();
            }

            // Save items
            try (PreparedStatement ps = con.prepareStatement(insertItem)) {
                for (String itemName : player.getItems().keySet()) {
                    ps.setInt(1, slotNumber);
                    ps.setString(2, itemName);
                    ps.setInt(3, player.getItems().get(itemName));
                    ps.executeUpdate();
                }
            }

            // Save Pokémon in the PC
            try (PreparedStatement ps = con.prepareStatement(insertPokemon)) {
                for (int i = 0; i < player.getPC().size(); i++) {
                    Pokemon pokemon = player.getPC().get(i);
                    ps.setInt(1, slotNumber);
                    ps.setString(2, pokemon.findname());
                    ps.setInt(3, pokemon.findlvl());
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(Load.class.getName()).log(Level.SEVERE, "Failed to save player data", e);
        }
    }

    private static String getPokemonName(Pokemon pokemon) {
        return (pokemon != null) ? pokemon.findname() : null;
    }

    private static int getPokemonLevel(Pokemon pokemon) {
        return (pokemon != null) ? pokemon.findlvl() : 0;
    }

    public static void selectionPanel(Player player) { // selection panel of the game
        boolean tf = true;// tf is to check whether player chooses to save and exit the selection panel
        // and go back to the save load panel
        while (tf) {// this loop will not end unless player chooses to save and exit
            System.out.printf("+%s+\n", "-".repeat(90));
            String currentCity = player.findCurrentCity();// get player's current location
            System.out.println("You are currently in: " + currentCity);// display it
            System.out.printf("+%s+\n", "-".repeat(90));
            switch (currentCity) {// the selection panel will have different selection based on which location the
                // player is at
                // every selection panel of each city will return a true if player does not save
                // and exit, or return false if player chooses to save and exit
                // updating tf to determine whether to end the selection panel loop and go back
                // to the save load panel
                case "Pallet Town":
                    tf = selectionPalletTown(player);
                    break;
                case "Viridian City":
                    tf = selectionViridianCity(player);
                    break;
                case "Pewter City":
                    tf = selectionPewterCity(player);
                    break;
                case "Cerulean City":
                    tf = selectionCeruleanCity(player);
                    break;
                case "Saffron City":
                    tf = selectionSaffronCity(player);
                    break;
                case "Celadon City":
                    tf = selectionCeladonCity(player);
                    break;
                case "Lavender Town":
                    tf = selectionLavenderTown(player);
                    break;
                case "Vermillion City":
                    tf = selectionVermillionCity(player);
                    break;
                case "Fuschia City":
                    tf = selectionFuschiaCity(player);
                    break;
                case "Cinnabar Island":
                    tf = selectionCinnabarIsland(player);
                    break;
            }
        }
    }

    public static boolean selectionPalletTown(Player player) { // Pallet Town selection panel
        Scanner input = new Scanner(System.in);
        // display all chocies
        System.out.println("[1] Move to:");// move to neighboring city
        ArrayList<String> neighboringCities = library.kantoMap.getNeighbours("Pallet Town");
        for (int i = 0; i < neighboringCities.size(); i++) {
            System.out.println((char) ('a' + i) + ". " + neighboringCities.get(i));// display all cities that player
            // could move to, the first choice of
            // every city's selection panel will
            // be this and the concept is same,
            // so I will only explain once here
        }
        System.out.println("[2] Talk to Mom(heal all pokemon to full status)");// talk to Mom to heal all pokemon to
        // full status
        System.out.println("[3] Fight Wild Pokemon [Caterpie, Rattata, Mankey][max lvl 5]");// encounter with wild
        // pokemon to fight them and
        // catch them
        System.out.println("[4] Player Options");// Player options, the player options will remain the same for every
        // city so I am only explaining it for once in Pallet Town

        System.out.println(
                "     a.Map     b.Pokemons     c.Bag     d.Badges     e.Profile     f.Guides     g.PC     h.Save and Exit");// all
        // options
        // related
        // to
        // player
        System.out.printf("+%s+\n", "-".repeat(90));

        // Get player's choice
        System.out.print("Your choice: ");
        String choice = input.nextLine();

        // Handle player's choice
        if (choice.length() != 0) {// check choice format
            if (choice.charAt(0) == '1' && choice.length() == 2) {// if choice begins with 1 and have length of 2, that
                // means player might want to move to another city
                if (choice.charAt(1) >= 'a' && choice.charAt(1) < 'a' + neighboringCities.size()) {// if the second
                    // character of
                    // choice is in
                    // wanted range
                    int cityIndex = choice.charAt(1) - 'a';// get the index of the city among the neigboring city list
                    // of the current location using the second character of
                    // choice
                    String nextCity = neighboringCities.get(cityIndex);// get the next city
                    player.movetoCity(nextCity);// move to next city
                } else { // if the second character of choice is out of range, display message below
                    System.out.println("Invalid choice! Please choose again.");
                }
                // Move the next city
            } else if (choice.equals("2")) { // if player choose to talk to Mom
                System.out.printf("+%s+\n", "-".repeat(90));
                System.out.println("You talked to Mom");// display Mom's dialogue
                System.out.println("Mom: " + player.getName()
                        + " ! Welcome home. It sounds like you had quite an experience. Maybe you should take a quick rest.");
                player.allhealup();// heal up all pokemons in player's team
                System.out.println(
                        "Mom: Oh, good! You and your Pokémon are looking great. I just heard from Prof. Oak. He said that Pokémon's energy is measured in HP. If your Pokémon lose their HP, you can restore them at any Pokémon Center. If you're going to travel far away, the smart Trainer stocks up on Potions at the Pokémon Mart. Make me proud, honey! Take care!");
                // Talk to Mom
            } else if (choice.equals("3")) { // if player choose to fight with wild pokemon
                Random r = new Random();
                String[] wilds = { "Caterpie", "Rattata", "Mankey" };// the list of wild pokemon in this area
                int wild_choice = r.nextInt(3);// randomly select a number from 0-2 to choose the wild pokemon using
                // this number as index
                int wild_lvl = r.nextInt(2, 6);// randomly select a level from 2-5
                String wild_pokemon = wilds[wild_choice];// select the wild pokemon
                System.out.println("A wild " + wild_pokemon + " appeared! [ " + wild_lvl + " ] ");// display the
                // infromation of the
                // wild pokemon
                Pokemon wild = new Pokemon(wild_pokemon, wild_lvl, true);// create a pokemon object using the data
                // given, setting wild value of the pokemon to
                // true, and this constructor will set battle
                // status of the pokemon to true
                Battle wildbattle = new Battle(player, wild); // start a battle between player and wild pokemon
            } else if (choice.charAt(0) == '4' && choice.length() == 2) {// if choice begins with 1 and have length of
                // 2, that means player might choose a player
                // option
                char player_choice = choice.charAt(1);
                switch (player_choice) {
                    case 'a': // player choose to display map and their current location on it
                        displayMap(player);
                        break;
                    case 'b':// player choose to check on all pokemons in the team
                        player.alterteam();
                        break;
                    case 'c':// player choose to check and use items in bag
                        player.bag();
                        break;
                    case 'd':// show all badges of the player
                        player.showbadges();
                        break;
                    case 'e':// show player's profile
                        player.showprofile();
                        break;
                    case 'f':// player choose to check on guides
                        guides();
                        break;
                    case 'g':// player choose to check on all pokemons in PC
                        player.alterPC(player);
                        break;
                    case 'h':// player choose save and exit
                        save(player);
                        return false;
                    default:
                        System.out.println("Invalid choice! Please choose again.");
                }
            } else {
                System.out.println("Invalid choice! Please choose again.");
            }
        } else {
            System.out.println("Invalid choice! Please choose again.");
        }
        return true;
    }

    public static boolean selectionViridianCity(Player player) { // VIridian City selection panel
        Scanner input = new Scanner(System.in);
        System.out.println("[1] Move to:"); // choice to move to neighboring city
        ArrayList<String> neighboringCities = library.kantoMap.getNeighbours("Viridian City");
        for (int i = 0; i < neighboringCities.size(); i++) {
            System.out.println((char) ('a' + i) + ". " + neighboringCities.get(i));
        }
        System.out.println("[2] Pokemon Center");// go to Pokemon Center to heal up all pokemons in the team
        System.out.println("[3] Poke Mart");// go to Poke Mart to buy or sell items
        System.out.println("[4] Fight Gym Leader [ Giovanni - Ground type ] [Recommended Pokemon Level: 50]");// fight
        // Gym
        // Leader
        // Giovanni
        System.out.println("[5] Player Options");// player options

        System.out.println(
                "     a.Map     b.Pokemons     c.Bag     d.Badges     e.Profile     f.Guides     g.PC     h.Save and Exit");
        System.out.printf("+%s+\n", "-".repeat(90));

        // Get player's choice
        System.out.print("Your choice: ");
        String choice = input.nextLine();

        // Handle player's choice
        if (choice.length() != 0) {
            if (choice.charAt(0) == '1' && choice.length() == 2) { // if player choose to move to another city
                if (choice.charAt(1) >= 'a' && choice.charAt(1) < 'a' + neighboringCities.size()) {
                    int cityIndex = choice.charAt(1) - 'a';
                    String nextCity = neighboringCities.get(cityIndex);
                    player.movetoCity(nextCity);
                } else {
                    System.out.println("Invalid choice! Please choose again.");
                }
                // Move the next city
            } else if (choice.equals("2")) {// if player choose to go to the Pokemon Center
                System.out.printf("+%s+\n", "-".repeat(90));
                System.out.println("You entered the Pokemon Center");// display message and dialogue
                System.out.println("Nurse: " + player.getName()
                        + " ! Welcome to the Pokemon Center. This is a place where you can heal up all your pokemons to their best status, let me heal up your pokemons real quick!");
                player.allhealup();// heal all pokemon in the team
                System.out.println(
                        "Nurse: Oh, good! You and your Pokémon are looking great. Good luck on your journey and take care of your pokemons!");
                // Talk to Mom
            } else if (choice.equals("3")) {// if player choose to go to the Poke Mart
                pokeMart(player);// call pokeMart() method, a method that simulates the Poke Mart
            } else if (choice.equals("4")) {// if player choose to challenge Gym Leade Giovanni
                if (!player.getbadges()[7].equals("Earth Badge")) {// if player does not have the Earth Badge
                    // check if the player have all the previous badges
                    if (player.getbadges()[0].equals("Boulder Badge") && player.getbadges()[1].equals("Cascade Badge")
                            && player.getbadges()[2].equals("Thunder Badge")
                            && player.getbadges()[3].equals("Rainbow Badge")
                            && player.getbadges()[4].equals("Soul Badge") && player.getbadges()[5].equals("Marsh Badge")
                            && player.getbadges()[6].equals("Volcano Badge")) {
                        System.out.println("You have all other gym badges! This is your final battle. Good luck!");// if
                        // yes,
                        // allow
                        // player
                        // to
                        // challenge
                        // the
                        // last
                        // gym
                        System.out.println("You are now challenging Gym Leader Giovanni!");// display battle information
                        Battle gymbattle = new Battle(player, "Giovanni");// start a battle between player and Giovanni

                        if (gymbattle.getwin()) {// when battle ends, if the player wins the battle, player earns the
                            // Earth Badge, display the message below
                            System.out.println(
                                    "Giovanni: You are sure the strongest trainer in this region, here is the Earth Badge. It is evidence of your mastery as a Pokémon Trainer.");
                            player.obtainbadge("Earth Badge");
                        }

                    } else {// if player does not have all previous badges, player is still not worthy,
                        // display message below
                        System.out.println(
                                "You have not obtained all the other badges yet, Giovanni is the strongest leader in the region.");
                        System.out.println(
                                "You are not strong enough to face him at this moment, please come back with all other badges to prove that you are a worthy opponent");
                    }
                } else {// if player have the Earth Badge, display message below
                    System.out.println("You have already challenged this gym.");
                }
            } else if (choice.charAt(0) == '5' && choice.length() == 2) {// player options
                char player_choice = choice.charAt(1);
                switch (player_choice) {
                    case 'a':
                        displayMap(player);
                        break;
                    case 'b':
                        player.alterteam();
                        break;
                    case 'c':
                        player.bag();
                        break;
                    case 'd':
                        player.showbadges();
                        break;
                    case 'e':
                        player.showprofile();
                        break;
                    case 'f':
                        guides();
                        break;
                    case 'g':
                        player.alterPC(player);
                        break;
                    case 'h':
                        save(player);
                        return false;
                    default:
                        System.out.println("Invalid choice! Please choose again.");
                }
            } else {
                System.out.println("Invalid choice! Please choose again.");
            }
        } else {
            System.out.println("Invalid choice! Please choose again.");
        }
        return true;
    }

    public static boolean selectionPewterCity(Player player) {// Pewter City selection panel
        Scanner input = new Scanner(System.in);
        System.out.println("[1] Move to:"); // choice to move to neighborinf cities
        ArrayList<String> neighboringCities = library.kantoMap.getNeighbours("Pewter City");
        for (int i = 0; i < neighboringCities.size(); i++) {
            System.out.println((char) ('a' + i) + ". " + neighboringCities.get(i));
        }
        System.out.println("[2] Pokemon Center");// Pokemon Center to heal up all pokemons
        System.out.println("[3] Poke Mart");// Poke Mart to buy/sell items
        System.out.println("[4] Fight Gym Leader [ Brock - Rock type ] [Recommended Pokemon Level: 14]");// challenge
        // Gym Leader
        // Brock
        System.out.println("[5] Fight Wild Pokemon [Caterpie, Metapod, Pikachu][max lvl 6]");// fight wild pokemon
        System.out.println("[6] Fight other trainers");// fight other trainers
        System.out.println(
                "     a.Rick[Bug type][lvl 6]    b.Anthony[Bug type][lvl 9]     c.Charlie[Electric type][lvl 8]");// all
        // the
        // trainers
        // that
        // could
        // be
        // fought
        System.out.println("[7] Player Options");// player options

        System.out.println(
                "     a.Map     b.Pokemons     c.Bag     d.Badges     e.Profile     f.Guides     g.PC     h.Save and Exit");
        System.out.printf("+%s+\n", "-".repeat(90));

        // Get player's choice
        System.out.print("Your choice: ");
        String choice = input.nextLine();

        // Handle player's choice
        if (choice.length() != 0) {
            if (choice.charAt(0) == '1' && choice.length() == 2) {// player choose to move to other location
                if (choice.charAt(1) >= 'a' && choice.charAt(1) < 'a' + neighboringCities.size()) {
                    int cityIndex = choice.charAt(1) - 'a';
                    String nextCity = neighboringCities.get(cityIndex);
                    player.movetoCity(nextCity);
                } else {
                    System.out.println("Invalid choice! Please choose again.");
                }
                // Move the next city
            } else if (choice.equals("2")) { // player choose to enter Pokemon Center and heal up all pokemons
                System.out.printf("+%s+\n", "-".repeat(90));
                System.out.println("You entered the Pokemon Center");
                System.out.println("Nurse: " + player.getName()
                        + " ! Welcome to the Pokemon Center. This is a place where you can heal up all your pokemons to their best status, let me heal up your pokemons real quick!");
                player.allhealup();
                System.out.println(
                        "Nurse: Oh, good! You and your Pokémon are looking great. Good luck on your journey and take care of your pokemons!");
                // Poke Center
            } else if (choice.equals("3")) {// player choose to enter Poke Mart
                pokeMart(player);
            } else if (choice.equals("4")) {// player choose to challenge Gym Leader Brock
                if (!player.getbadges()[0].equals("Boulder Badge")) {// if player does not have the Boulder Badge
                    System.out.println("You are now challenging Gym Leader Brock!");// display battle information
                    Battle gymbattle = new Battle(player, "Brock");// start battle between player and Brock
                    if (gymbattle.getwin()) {// if player won the battle
                        player.obtainbadge("Boulder Badge");// player obtains Boulder Badge
                        System.out.println(
                                "Brock: I took you for granted, and so I lost. As proof of your victory, I confer on you this...the official Boulder Badge.");
                    }
                } else {// if player have Boulder Badge, display message below
                    System.out.println("You have already challenged this gym.");
                }
            } else if (choice.equals("5")) {// player choose to fight wild pokemon
                Random r = new Random();// similar to Pallet Town, the wild pokemon is randomly picked from the wilds[]
                // array, and level is randomly chosen between a range
                String[] wilds = { "Caterpie", "Metapod", "Pikachu" };
                int wild_choice = r.nextInt(3);
                int wild_lvl = r.nextInt(3, 7);// level between 3-6
                String wild_pokemon = wilds[wild_choice];
                System.out.println("A wild " + wild_pokemon + " appeared!");// display wild pokemon information
                Pokemon wild = new Pokemon(wild_pokemon, wild_lvl, true);// create Pokemon object for wild pokemon
                Battle wildbattle = new Battle(player, wild);// start Battle between player and wild pokemon
                // all other wild pokemon fight in other cities will have similar code structure
                // with this, only changing the wild pokemon that mgiht appear and the level
                // range
            } else if (choice.charAt(0) == '6' && choice.length() == 2) {// player chooses a trainer to fight
                char player_choice = choice.charAt(1);
                switch (player_choice) {
                    case 'a':
                        // fight rick - caterpie lvl 6, caterpie lvl 6
                        Battle trainerbattle = new Battle(player, "Rick");
                        break;
                    case 'b':
                        // fight anthony - caterpie lvl 9
                        trainerbattle = new Battle(player, "Anthony");
                        break;
                    case 'c':
                        // fight Charlie - pikachu lvl 9
                        trainerbattle = new Battle(player, "Charlie");
                        break;
                    default:
                        System.out.println("Invalid choice! Please choose again.");
                }
            } else if (choice.charAt(0) == '7' && choice.length() == 2) {// player options
                char player_choice = choice.charAt(1);
                switch (player_choice) {
                    case 'a':
                        displayMap(player);
                        break;
                    case 'b':
                        player.alterteam();
                        break;
                    case 'c':
                        player.bag();
                        break;
                    case 'd':
                        player.showbadges();
                        break;
                    case 'e':
                        player.showprofile();
                        break;
                    case 'f':
                        guides();
                        break;
                    case 'g':
                        player.alterPC(player);
                        break;
                    case 'h':
                        save(player);
                        return false;
                    default:
                        System.out.println("Invalid choice! Please choose again.");
                }
            } else {
                System.out.println("Invalid choice! Please choose again.");
            }
        } else {
            System.out.println("Invalid choice! Please choose again");
        }
        return true;
    }

    public static boolean selectionCeruleanCity(Player player) {// Cerulean City selection panel
        Scanner input = new Scanner(System.in);
        System.out.println("[1] Move to:");// move to neighboring city
        ArrayList<String> neighboringCities = library.kantoMap.getNeighbours("Cerulean City");
        for (int i = 0; i < neighboringCities.size(); i++) {
            System.out.println((char) ('a' + i) + ". " + neighboringCities.get(i));
        }
        System.out.println("[2] Pokemon Center");// explained
        System.out.println("[3] Poke Mart");// explained
        System.out.println("[4] Fight Gym Leader [ Misty - Water type ] [Recommended Pokemon Level: 21]");// fight gym
        // Leader
        // Misty
        System.out.println("[5] Fight Wild Pokemon [Sandshrew, Geodude, Onix][max lvl 12]");// fight wild pokemon
        System.out.println("[6] Fight other trainers");// fight other trainers
        System.out.println(
                "     a.Rocket Grunt[Ground, Fighting type][lvl 17]    b.Marcos[Rock type][lvl 11]     c.Jovan[Electric type][lvl 14]");
        System.out.println("[7] Player Options");// player options

        System.out.println(
                "     a.Map     b.Pokemons     c.Bag     d.Badges     e.Profile     f.Guides     g.PC     h.Save and Exit");
        System.out.printf("+%s+\n", "-".repeat(90));

        // Get player's choice
        System.out.print("Your choice: ");
        String choice = input.nextLine();

        // Handle player's choice
        if (choice.length() != 0) {
            if (choice.charAt(0) == '1' && choice.length() == 2) {// move to another city
                if (choice.charAt(1) >= 'a' && choice.charAt(1) < 'a' + neighboringCities.size()) {
                    int cityIndex = choice.charAt(1) - 'a';
                    String nextCity = neighboringCities.get(cityIndex);
                    player.movetoCity(nextCity);
                } else {
                    System.out.println("Invalid choice! Please choose again.");
                }
                // Move the next city
            } else if (choice.equals("2")) {
                System.out.printf("+%s+\n", "-".repeat(90));
                System.out.println("You entered the Pokemon Center");
                System.out.println("Nurse: " + player.getName()
                        + " ! Welcome to the Pokemon Center. This is a place where you can heal up all your pokemons to their best status, let me heal up your pokemons real quick!");
                player.allhealup();
                System.out.println(
                        "Nurse: Oh, good! You and your Pokémon are looking great. Good luck on your journey and take care of your pokemons!");
                // Poke Center
            } else if (choice.equals("3")) {
                pokeMart(player);
                // Poke Mart
            } else if (choice.equals("4")) {// challenge Gy Leader Misty
                if (!player.getbadges()[1].equals("Cascade Badge")) {// if player does not have Cascade Badge
                    System.out.println("You are now challenging Gym Leader Misty!");// display challenge information
                    Battle gymbattle = new Battle(player, "Misty");// start battle between player and Misty
                    if (gymbattle.getwin()) {// if player won
                        player.obtainbadge("Cascade Badge");// player earns Cascade Badge
                        System.out.println(
                                "Misty: Wow! You're too much, all right! You can have the Cascade Badge to show that you beat me.");
                    }
                } else {// if player have Cascade Badge
                    System.out.println("You have already challenged this gym");
                }
            } else if (choice.equals("5")) {// fight wild pokemon
                Random r = new Random();
                String[] wilds = { "Sandshrew", "Geodude", "Onix" };// wild pokemons that might appear
                int wild_choice = r.nextInt(3);
                int wild_lvl = r.nextInt(8, 13);// level range
                String wild_pokemon = wilds[wild_choice];
                System.out.println("A wild " + wild_pokemon + " appeared!");
                Pokemon wild = new Pokemon(wild_pokemon, wild_lvl, true);
                Battle wildbattle = new Battle(player, wild);
            } else if (choice.charAt(0) == '6' && choice.length() == 2) {// trainer battle
                char player_choice = choice.charAt(1);
                switch (player_choice) {
                    case 'a':
                        // fight rocket grunt - machop lvl 17, sandshrew lvl 17
                        Battle trainerbattle = new Battle(player, "Rocket Grunt");
                        break;
                    case 'b':
                        // fight marcos - 2x geodude lvl 11, onix lvl 11
                        trainerbattle = new Battle(player, "Marcos");
                        break;
                    case 'c':
                        // fight Jovan - voltorb lvl 14, magnemite lvl 14
                        trainerbattle = new Battle(player, "Jovan");
                        break;
                    default:
                        System.out.println("Invalid choice! Please choose again.");
                }
            } else if (choice.charAt(0) == '7' && choice.length() == 2) { // player options
                char player_choice = choice.charAt(1);
                switch (player_choice) {
                    case 'a':
                        displayMap(player);
                        break;
                    case 'b':
                        player.alterteam();
                        break;
                    case 'c':
                        player.bag();
                        break;
                    case 'd':
                        player.showbadges();
                        break;
                    case 'e':
                        player.showprofile();
                        break;
                    case 'f':
                        guides();
                        break;
                    case 'g':
                        player.alterPC(player);
                        break;
                    case 'h':
                        save(player);
                        return false;
                    default:
                        System.out.println("Invalid choice! Please choose again.");
                }
            } else {
                System.out.println("Invalid choice! Please choose again.");
            }
        } else {
            System.out.println("Invalid choice! Please choose again.");
        }
        return true;
    }

    public static boolean selectionSaffronCity(Player player) {// Saffron City selection panel
        Scanner input = new Scanner(System.in);
        System.out.println("[1] Move to:");// move to neighboring city
        ArrayList<String> neighboringCities = library.kantoMap.getNeighbours("Saffron City");
        for (int i = 0; i < neighboringCities.size(); i++) {
            System.out.println((char) ('a' + i) + ". " + neighboringCities.get(i));
        }
        System.out.println("[2] Pokemon Center");// explained
        System.out.println("[3] Poke Mart");// explained
        System.out.println("[4] Fight Gym Leader [ Sabrina - Psychic type ] [Recommended Pokemon Level: 43]");// challenge
        // Gym
        // Leader
        // Sabrina
        System.out.println("[5] Fight Wild Pokemon [Oddish, Bellsprout, Growlithe, Abra][max lvl 16]");// fight wild
        // pokemon
        System.out.println("[6] Fight other trainers");// fight other trainers
        System.out.println(
                "     a.Ricky[Water type][lvl 30]    b.Jeff[Normal type][lvl 29]     c.Elijah[Bug type][lvl 30]");
        System.out.println("[7] Rival Race");// start a rival race
        System.out.println("[8] Player Options");// player options

        System.out.println(
                "     a.Map     b.Pokemons     c.Bag     d.Badges     e.Profile     f.Guides     g.PC     h.Save and Exit");
        System.out.printf("+%s+\n", "-".repeat(90));

        // Get player's choice
        System.out.print("Your choice: ");
        String choice = input.nextLine();

        // Handle player's choice
        if (choice.length() != 0) {
            if (choice.charAt(0) == '1' && choice.length() == 2) {// player move to another city
                if (choice.charAt(1) >= 'a' && choice.charAt(1) < 'a' + neighboringCities.size()) {
                    int cityIndex = choice.charAt(1) - 'a';
                    String nextCity = neighboringCities.get(cityIndex);
                    player.movetoCity(nextCity);
                } else {
                    System.out.println("Invalid choice! Please choose again.");
                }
                // Move the next city
            } else if (choice.equals("2")) {
                System.out.printf("+%s+\n", "-".repeat(90));
                System.out.println("You entered the Pokemon Center");
                System.out.println("Nurse: " + player.getName()
                        + " ! Welcome to the Pokemon Center. This is a place where you can heal up all your pokemons to their best status, let me heal up your pokemons real quick!");
                player.allhealup();
                System.out.println(
                        "Nurse: Oh, good! You and your Pokémon are looking great. Good luck on your journey and take care of your pokemons!");
                // Poke Center
            } else if (choice.equals("3")) {
                pokeMart(player);
                // Poke Mart
            } else if (choice.equals("4")) {// player challenges Gym Leader Sabrina
                if (!player.getbadges()[5].equals("Marsh Badge")) {// if player does not have the Marsh Badge
                    System.out.println("You are now challenging Gym Leader Sabrina!");// display challenge information
                    Battle gymbattle = new Battle(player, "Sabrina");// start battle between player and Sabrina
                    if (gymbattle.getwin()) {// if player wins
                        player.obtainbadge("Marsh Badge");// player obtaines Marsh Badge
                        System.out.println(
                                "Sabrina: This loss shocks me! But a loss is a loss. I admit I didn't work hard enough to win. You earned the Marsh Badge.");
                    }
                } else {// if player have the Marsh Badge
                    System.out.println("You have already challenged this gym.");
                }
            } else if (choice.equals("5")) {// fight wild pokemon
                Random r = new Random();
                String[] wilds = { "Oddish", "Bellsprout", "Growlithe", "Abra" };// wild pokemon in the area
                int wild_choice = r.nextInt(4);
                int wild_lvl = r.nextInt(11, 17);// level range
                String wild_pokemon = wilds[wild_choice];
                System.out.println("A wild " + wild_pokemon + " appeared!");
                Pokemon wild = new Pokemon(wild_pokemon, wild_lvl, true);
                Battle wildbattle = new Battle(player, wild);
            } else if (choice.charAt(0) == '6' && choice.length() == 2) {// fight other trainers
                char player_choice = choice.charAt(1);
                switch (player_choice) {
                    case 'a':
                        // fight ricky - wartortle lvl 30
                        Battle trainerbattle = new Battle(player, "Ricky");
                        break;
                    case 'b':
                        // fight Jeff - 2x raticate lvl 29
                        trainerbattle = new Battle(player, "Jeff");
                        break;
                    case 'c':
                        // fight Elijah - Butterfree lvl 30
                        trainerbattle = new Battle(player, "Elijah");
                        break;
                    default:
                        System.out.println("Invalid choice! Please choose again.");
                }
            } else if (choice.equals("7")) {// start a rival race with rival
                player.startrivalrace();// call the startrivalrace() method to initiate a rival race
            } else if (choice.charAt(0) == '8' && choice.length() == 2) {// player options
                char player_choice = choice.charAt(1);
                switch (player_choice) {
                    case 'a':
                        displayMap(player);
                        break;
                    case 'b':
                        player.alterteam();
                        break;
                    case 'c':
                        player.bag();
                        break;
                    case 'd':
                        player.showbadges();
                        break;
                    case 'e':
                        player.showprofile();
                        break;
                    case 'f':
                        guides();
                        break;
                    case 'g':
                        player.alterPC(player);
                        break;
                    case 'h':
                        save(player);
                        return false;
                    default:
                        System.out.println("Invalid choice! Please choose again.");
                }
            } else {
                System.out.println("Invalid choice! Please choose again.");
            }
        } else {
            System.out.println("Invalid choice! Please choose again.");
        }
        return true;
    }

    public static boolean selectionCeladonCity(Player player) {// Celadon City selection panel
        Scanner input = new Scanner(System.in);
        System.out.println("[1] Move to:");// player move to another city
        ArrayList<String> neighboringCities = library.kantoMap.getNeighbours("Celadon City");
        for (int i = 0; i < neighboringCities.size(); i++) {
            System.out.println((char) ('a' + i) + ". " + neighboringCities.get(i));
        }
        System.out.println("[2] Pokemon Center");// explained
        System.out.println("[3] Poke Mart");// explained
        System.out.println("[4] Fight Gym Leader [ Erika - Grass type ] [Recommended Pokemon Level: 29]");// challenge
        // Gym Leader
        // Erika
        System.out.println("[5] Fight Wild Pokemon [Koffing, Grimer, Machop, Ponyta][max lvl 23]");// fight wild pokemon
        System.out.println("[6] Fight Snorlax [lvl 30]");// special encounter, wild Snorlax, a very stong and bulky
        // pokemon
        System.out.println("[7] Fight other trainers");// fight other trainers
        System.out.println(
                "     a.Lao[Poison type][lvl 27]    b.Koji[Fighting type][lvl 27]     c.Lea[Fire type][lvl 27]");
        System.out.println("[8] Player Options");// player options

        System.out.println(
                "     a.Map     b.Pokemons     c.Bag     d.Badges     e.Profile     f.Guides     g.PC     h.Save and Exit");
        System.out.printf("+%s+\n", "-".repeat(90));

        // Get player's choice
        System.out.print("Your choice: ");
        String choice = input.nextLine();

        // Handle player's choice
        if (choice.length() != 0) {
            if (choice.charAt(0) == '1' && choice.length() == 2) {// move to another city
                if (choice.charAt(1) >= 'a' && choice.charAt(1) < 'a' + neighboringCities.size()) {
                    int cityIndex = choice.charAt(1) - 'a';
                    String nextCity = neighboringCities.get(cityIndex);
                    player.movetoCity(nextCity);
                } else {
                    System.out.println("Invalid choice! Please choose again.");
                }
                // Move the next city
            } else if (choice.equals("2")) {
                System.out.printf("+%s+\n", "-".repeat(90));
                System.out.println("You entered the Pokemon Center");
                System.out.println("Nurse: " + player.getName()
                        + " ! Welcome to the Pokemon Center. This is a place where you can heal up all your pokemons to their best status, let me heal up your pokemons real quick!");
                player.allhealup();
                System.out.println(
                        "Nurse: Oh, good! You and your Pokémon are looking great. Good luck on your journey and take care of your pokemons!");
                // Poke Center
            } else if (choice.equals("3")) {
                pokeMart(player);
                // Poke Mart
            } else if (choice.equals("4")) {// player challenges Gym Leader Erika
                if (!player.getbadges()[3].equals("Rainbow Badge")) {// if player does not have the Rainbow Badge
                    System.out.println("You are now challenging Gym Leader Erika!");// display challenge information
                    Battle gymbattle = new Battle(player, "Erika");// start battle between player and erika
                    if (gymbattle.getwin()) {// if player wins
                        player.obtainbadge("Rainbow Badge");// player obtains Rainbow Badge
                        System.out.println(
                                "Erika: Oh! I concede defeat. You are remarkably strong. I must confer on you the Rainbow Badge.");
                    }
                } else {// if player have Rainbow Badge
                    System.out.println("You have already challenged this gym.");
                }
            } else if (choice.equals("5")) {// fight wild pokemon
                Random r = new Random();
                String[] wilds = { "Koffing", "Grimer", "Machop", "Ponyta" };// wild pokemon in the area
                int wild_choice = r.nextInt(4);
                int wild_lvl = r.nextInt(20, 24);// level range
                String wild_pokemon = wilds[wild_choice];
                System.out.println("A wild " + wild_pokemon + " appeared!");
                Pokemon wild = new Pokemon(wild_pokemon, wild_lvl, true);
                Battle wildbattle = new Battle(player, wild);
            } else if (choice.equals("6")) {// fight snorlax
                System.out.println("A wild Snorlax is blocking the road!");// display Snorlax message
                Pokemon wild = new Pokemon("Snorlax", 30, true);// create a Pokemon object for Snorlax with level 30 and
                // wild status
                Battle wildbattle = new Battle(player, wild);// start a battle between Snorlax and player
            } else if (choice.charAt(0) == '7' && choice.length() == 2) {// fight other trainers
                char player_choice = choice.charAt(1);
                switch (player_choice) {
                    case 'a':
                        // fight lao - grimer lvl 27, koffing lvl 27
                        Battle trainerbattle = new Battle(player, "Lao");
                        break;
                    case 'b':
                        // fight koji - machop lvl 27, mankey lvl 27
                        trainerbattle = new Battle(player, "Koji");
                        break;
                    case 'c':
                        // fight lea - rapidash lvl 27
                        trainerbattle = new Battle(player, "Lea");
                        break;
                    default:
                        System.out.println("Invalid choice! Please choose again.");
                }
            } else if (choice.charAt(0) == '8' && choice.length() == 2) {// player options
                char player_choice = choice.charAt(1);
                switch (player_choice) {
                    case 'a':
                        displayMap(player);
                        break;
                    case 'b':
                        player.alterteam();
                        break;
                    case 'c':
                        player.bag();
                        break;
                    case 'd':
                        player.showbadges();
                        break;
                    case 'e':
                        player.showprofile();
                        break;
                    case 'f':
                        guides();
                        break;
                    case 'g':
                        player.alterPC(player);
                        break;
                    case 'h':
                        save(player);
                        return false;
                    default:
                        System.out.println("Invalid choice! Please choose again.");
                }
            } else {
                System.out.println("Invalid choice! Please choose again.");
            }
        } else {
            System.out.println("Invalid choice! Please choose again.");
        }
        return true;
    }

    public static boolean selectionLavenderTown(Player player) {// Lavender Town selection panel
        Scanner input = new Scanner(System.in);
        System.out.println("[1] Move to:");// move to neighboring city
        ArrayList<String> neighboringCities = library.kantoMap.getNeighbours("Lavender Town");
        for (int i = 0; i < neighboringCities.size(); i++) {
            System.out.println((char) ('a' + i) + ". " + neighboringCities.get(i));
        }
        System.out.println("[2] Pokemon Center");
        System.out.println("[3] Poke Mart");
        System.out.println("[4] Pokemon Tower - Poke Maze");// play Poke Maze at Pokemon Tower
        System.out.println("[5] Fight Wild Pokemon [Magnemite, Voltorb, Nidoran-M, Nidoran-F, Venonat][max lvl 20]");// fight
        // wild
        // pokemon
        System.out.println("[6] Fight Snorlax [lvl 30]");// special encounter, Snorlax
        System.out.println("[7] Fight other trainers");// fight other trainers
        System.out.println(
                "     a.Luca[Electric type][lvl 29]    b.Justin[Poison type][lvl 29]     c.Tower Grunt[Normal type][lvl 27]");
        System.out.println("[8] Player Options");

        System.out.println(
                "     a.Map     b.Pokemons     c.Bag     d.Badges     e.Profile     f.Guides     g.PC     h.Save and Exit");
        System.out.printf("+%s+\n", "-".repeat(90));

        // Get player's choice
        System.out.print("Your choice: ");
        String choice = input.nextLine();

        // Handle player's choice
        if (choice.length() != 0) {
            if (choice.charAt(0) == '1' && choice.length() == 2) {// player move to another city
                if (choice.charAt(1) >= 'a' && choice.charAt(1) < 'a' + neighboringCities.size()) {
                    int cityIndex = choice.charAt(1) - 'a';
                    String nextCity = neighboringCities.get(cityIndex);
                    player.movetoCity(nextCity);
                } else {
                    System.out.println("Invalid choice! Please choose again.");
                }
                // Move the next city
            } else if (choice.equals("2")) {
                System.out.printf("+%s+\n", "-".repeat(90));
                System.out.println("You entered the Pokemon Center");
                System.out.println("Nurse: " + player.getName()
                        + " ! Welcome to the Pokemon Center. This is a place where you can heal up all your pokemons to their best status, let me heal up your pokemons real quick!");
                player.allhealup();
                System.out.println(
                        "Nurse: Oh, good! You and your Pokémon are looking great. Good luck on your journey and take care of your pokemons!");
                // Poke Center
            } else if (choice.equals("3")) {
                pokeMart(player);
                // Poke Mart
            } else if (choice.equals("4")) {// player choose to play Poke Maze
                PokeMaze maze = new PokeMaze();// create a Maze object
                maze.simulation(player);// let player play the Maze
            } else if (choice.equals("5")) {// fight wild pokemon
                Random r = new Random();
                String[] wilds = { "Magnemite", "Voltorb", "Nidoran-M", "Nidoran-F", "Venonat" };// wild pokemon in the
                // area
                int wild_choice = r.nextInt(5);
                int wild_lvl = r.nextInt(14, 21);// level range
                String wild_pokemon = wilds[wild_choice];
                System.out.println("A wild " + wild_pokemon + " appeared!");
                Pokemon wild = new Pokemon(wild_pokemon, wild_lvl, true);
                Battle wildbattle = new Battle(player, wild);
            } else if (choice.equals("6")) {// fight snorlax, same explanation as in Celadon City selection panel
                System.out.println("A wild Snorlax is blocking the road!");
                Pokemon wild = new Pokemon("Snorlax", 30);
                Battle wildbattle = new Battle(player, wild);
            } else if (choice.charAt(0) == '7' && choice.length() == 2) {// fight other trainers
                char player_choice = choice.charAt(1);
                switch (player_choice) {
                    case 'a':
                        // fight luca - voltorb, electrode lvl 29
                        Battle trainerbattle = new Battle(player, "Luca");
                        break;
                    case 'b':
                        // fight justin - Nidoran-M,Nidoran-F, lvl 29
                        trainerbattle = new Battle(player, "Justin");
                        break;
                    case 'c':
                        // fight tower grunt - rattata, raticate lvl 27
                        trainerbattle = new Battle(player, "Tower Grunt");
                        break;
                    default:
                        System.out.println("Invalid choice! Please choose again.");
                }
            } else if (choice.charAt(0) == '8' && choice.length() == 2) {// player options
                char player_choice = choice.charAt(1);
                switch (player_choice) {
                    case 'a':
                        displayMap(player);
                        break;
                    case 'b':
                        player.alterteam();
                        break;
                    case 'c':
                        player.bag();
                        break;
                    case 'd':
                        player.showbadges();
                        break;
                    case 'e':
                        player.showprofile();
                        break;
                    case 'f':
                        guides();
                        break;
                    case 'g':
                        player.alterPC(player);
                        break;
                    case 'h':
                        save(player);
                        return false;
                    default:
                        System.out.println("Invalid choice! Please choose again.");
                }
            } else {
                System.out.println("Invalid choice! Please choose again.");
            }
        } else {
            System.out.println("Invalid choice! Please choose again.");
        }
        return true;
    }

    public static boolean selectionVermillionCity(Player player) {// Vermillion City selection Panel
        Scanner input = new Scanner(System.in);
        System.out.println("[1] Move to:");// move to neighboring city
        ArrayList<String> neighboringCities = library.kantoMap.getNeighbours("Vermillion City");
        for (int i = 0; i < neighboringCities.size(); i++) {
            System.out.println((char) ('a' + i) + ". " + neighboringCities.get(i));
        }
        System.out.println("[2] Pokemon Center");
        System.out.println("[3] Poke Mart");
        System.out.println("[4] Fight Gym Leader [ Lt. Surge - Electric type ] [Recommended Pokemon Level: 24]");// challege
        // Gym
        // Leader
        // Lt.
        // Surge
        System.out.println("[5] Fight Wild Pokemon [Diglett, Jigglypuff, Eevee][max lvl 22]");// fight wild pokemon
        System.out.println("[6] Fight other trainers");// fight other trainers
        System.out.println(
                "     a.Yasu[Normal type][lvl 17]    b.Dave[Poison type][lvl 18]     c.Bernie[Electric type][lvl 18]");
        System.out.println("[7] Player Options");// player options

        System.out.println(
                "     a.Map     b.Pokemons     c.Bag     d.Badges     e.Profile     f.Guides     g.PC     h.Save and Exit");
        System.out.printf("+%s+\n", "-".repeat(90));

        // Get player's choice
        System.out.print("Your choice: ");
        String choice = input.nextLine();

        // Handle player's choice
        if (choice.length() != 0) {
            if (choice.charAt(0) == '1' && choice.length() == 2) {
                if (choice.charAt(1) >= 'a' && choice.charAt(1) < 'a' + neighboringCities.size()) {
                    int cityIndex = choice.charAt(1) - 'a';
                    String nextCity = neighboringCities.get(cityIndex);
                    player.movetoCity(nextCity);
                } else {
                    System.out.println("Invalid choice! Please choose again.");
                }
                // Move the next city
            } else if (choice.equals("2")) {
                System.out.printf("+%s+\n", "-".repeat(90));
                System.out.println("You entered the Pokemon Center");
                System.out.println("Nurse: " + player.getName()
                        + " ! Welcome to the Pokemon Center. This is a place where you can heal up all your pokemons to their best status, let me heal up your pokemons real quick!");
                player.allhealup();
                System.out.println(
                        "Nurse: Oh, good! You and your Pokémon are looking great. Good luck on your journey and take care of your pokemons!");
                // Poke Center
            } else if (choice.equals("3")) {
                pokeMart(player);
                // Poke Mart
            } else if (choice.equals("4")) {// challenge Gym Leader Lt. Surge
                if (!player.getbadges()[2].equals("Thunder Badge")) {// if player does not have Thunder Badge
                    System.out.println("You are now challenging Gym Leader Lt. Surge!");// display challenge message
                    Battle gymbattle = new Battle(player, "Lt. Surge");// start battle between player and Lt. Surge
                    if (gymbattle.getwin()) {// if player wins
                        player.obtainbadge("Thunder Badge");// player obtains Thunder Badge
                        System.out.println(
                                "Lt. Surge: Now that's a shocker! You're the real deal, kid! Fine, then, take the Thunder Badge!");
                    }
                } else {// if player have Thunder Badge
                    System.out.println("You have already challenged this gym");
                }

            } else if (choice.equals("5")) {// fight wild pokemon
                Random r = new Random();
                String[] wilds = { "Diglett", "Jigglypuff", "Eevee" };// wild pokemon in the area
                int wild_choice = r.nextInt(3);
                int wild_lvl = r.nextInt(15, 23);// level range
                String wild_pokemon = wilds[wild_choice];
                System.out.println("A wild " + wild_pokemon + " appeared!");
                Pokemon wild = new Pokemon(wild_pokemon, wild_lvl, true);
                Battle wildbattle = new Battle(player, wild);
            } else if (choice.charAt(0) == '6' && choice.length() == 2) {// fight other trainers
                char player_choice = choice.charAt(1);
                switch (player_choice) {
                    case 'a':
                        // fight yasu - 2x rattata, raticate lvl 17
                        Battle trainerbattle = new Battle(player, "Yasu");
                        break;
                    case 'b':
                        // fight dave - nidoran-m,nidorino lvl 18
                        trainerbattle = new Battle(player, "Dave");
                        break;
                    case 'c':
                        // fight bernie - 2x magnemite, magneton lvl 18
                        trainerbattle = new Battle(player, "Bernie");
                        break;
                    default:
                        System.out.println("Invalid choice! Please choose again.");
                }
            } else if (choice.charAt(0) == '7' && choice.length() == 2) {// player options
                char player_choice = choice.charAt(1);
                switch (player_choice) {
                    case 'a':
                        displayMap(player);
                        break;
                    case 'b':
                        player.alterteam();
                        break;
                    case 'c':
                        player.bag();
                        break;
                    case 'd':
                        player.showbadges();
                        break;
                    case 'e':
                        player.showprofile();
                        break;
                    case 'f':
                        guides();
                        break;
                    case 'g':
                        player.alterPC(player);
                        break;
                    case 'h':
                        save(player);
                        return false;
                    default:
                        System.out.println("Invalid choice! Please choose again.");
                }
            } else {
                System.out.println("Invalid choice! Please choose again.");
            }
        } else {
            System.out.println("Invalid choice! Please choose again.");
        }
        return true;
    }

    public static boolean selectionFuschiaCity(Player player) {// Fuschia City selection panel
        Scanner input = new Scanner(System.in);
        System.out.println("[1] Move to:");
        ArrayList<String> neighboringCities = library.kantoMap.getNeighbours("Fuschia City");
        for (int i = 0; i < neighboringCities.size(); i++) {
            System.out.println((char) ('a' + i) + ". " + neighboringCities.get(i));
        }
        System.out.println("[2] Pokemon Center");
        System.out.println("[3] Poke Mart");
        System.out.println("[4] Fight Gym Leader [ Koga - Poison type ] [Recommended Pokemon Level: 43]");// fight Gym
        // Leader Koga
        System.out.println("[5] Fight Wild Pokemon [Grimer, Rattata, Raticate][max lvl 29]");
        System.out.println("[6] Fight other trainers");
        System.out.println(
                "     a.Charles[Poison type][lvl 39]    b.Jacob[Fire type][lvl 39]     c.Connie[Water type][lvl 33]");
        System.out.println("[7] Safari Zone");// go into Safari Zone
        System.out.println("[8] Player Options");

        System.out.println(
                "     a.Map     b.Pokemons     c.Bag     d.Badges     e.Profile     f.Guides     g.PC     h.Save and Exit");
        System.out.printf("+%s+\n", "-".repeat(90));

        // Get player's choice
        System.out.print("Your choice: ");
        String choice = input.nextLine();

        // Handle player's choice
        if (choice.length() != 0) {
            if (choice.charAt(0) == '1' && choice.length() == 2) {
                if (choice.charAt(1) >= 'a' && choice.charAt(1) < 'a' + neighboringCities.size()) {
                    int cityIndex = choice.charAt(1) - 'a';
                    String nextCity = neighboringCities.get(cityIndex);
                    player.movetoCity(nextCity);
                } else {
                    System.out.println("Invalid choice! Please choose again.");
                }
                // Move the next city
            } else if (choice.equals("2")) {
                System.out.printf("+%s+\n", "-".repeat(90));
                System.out.println("You entered the Pokemon Center");
                System.out.println("Nurse: " + player.getName()
                        + " ! Welcome to the Pokemon Center. This is a place where you can heal up all your pokemons to their best status, let me heal up your pokemons real quick!");
                player.allhealup();
                System.out.println(
                        "Nurse: Oh, good! You and your Pokémon are looking great. Good luck on your journey and take care of your pokemons!");
                // Poke Center
            } else if (choice.equals("3")) {
                pokeMart(player);
                // Poke Mart
            } else if (choice.equals("4")) {// challgeng Gym Leader Koga
                if (!player.getbadges()[4].equals("Soul Badge")) {// if player does not have the Soul Badge
                    System.out.println("You are now challenging Gym Leader Koga!");
                    Battle gymbattle = new Battle(player, "Koga");// start a battle between player and Koga
                    if (gymbattle.getwin()) {// if player wins
                        player.obtainbadge("Soul Badge");// player obtains Soul Badge
                        System.out.println("Koga: Humph! You have proven your worth! Here! Take the Soul Badge!");
                    }
                } else {// if player have Soul Badge
                    System.out.println("You have already challenged this gym.");
                }
            } else if (choice.equals("5")) {// fight wild pokemon
                Random r = new Random();
                String[] wilds = { "Grimer", "Rattata", "Raticate" };// wild pokemon in the area
                int wild_choice = r.nextInt(3);
                int wild_lvl = r.nextInt(22, 30);// level range
                String wild_pokemon = wilds[wild_choice];
                System.out.println("A wild " + wild_pokemon + " appeared!");
                Pokemon wild = new Pokemon(wild_pokemon, wild_lvl, true);
                Battle wildbattle = new Battle(player, wild);
            } else if (choice.charAt(0) == '6' && choice.length() == 2) {// fight other trainers
                char player_choice = choice.charAt(1);
                switch (player_choice) {
                    case 'a':
                        // fight charles - koffing, weezing lvl 39
                        Battle trainerbattle = new Battle(player, "Charles");
                        break;
                    case 'b':
                        // fight jacob - charmeleon lvl 39
                        trainerbattle = new Battle(player, "Jacob");
                        break;
                    case 'c':
                        // fight connie - 3x staryu lvl 33
                        trainerbattle = new Battle(player, "Connie");
                        break;
                    default:
                        System.out.println("Invalid choice! Please choose again.");
                }
            } else if (choice.equals("7")) {// player enters safari zone
                safari.SafariZone();// call SafariZone() method from safari class
            } else if (choice.charAt(0) == '8' && choice.length() == 2) {// player optoins
                char player_choice = choice.charAt(1);
                switch (player_choice) {
                    case 'a':
                        displayMap(player);
                        break;
                    case 'b':
                        player.alterteam();
                        break;
                    case 'c':
                        player.bag();
                        break;
                    case 'd':
                        player.showbadges();
                        break;
                    case 'e':
                        player.showprofile();
                        break;
                    case 'f':
                        guides();
                        break;
                    case 'g':
                        player.alterPC(player);
                        break;
                    case 'h':
                        save(player);
                        return false;
                    default:
                        System.out.println("Invalid choice! Please choose again.");
                }
            } else {
                System.out.println("Invalid choice! Please choose again.");
            }
        } else {
            System.out.println("Invalid choice! Please choose again.");
        }
        return true;
    }

    public static boolean selectionCinnabarIsland(Player player) {// Cinnabar Island selection panel
        Scanner input = new Scanner(System.in);
        System.out.println("[1] Move to:");
        ArrayList<String> neighboringCities = library.kantoMap.getNeighbours("Cinnabar Island");
        for (int i = 0; i < neighboringCities.size(); i++) {
            System.out.println((char) ('a' + i) + ". " + neighboringCities.get(i));
        }
        System.out.println("[2] Pokemon Center");
        System.out.println("[3] Poke Mart");
        System.out.println("[4] Fight Gym Leader [ Blaine - Fire type ] [Recommended Pokemon Level: 47]");// challenge
        // Gym Leader
        // Blaine
        System.out.println("[5] Fight Wild Pokemon [Staryu, Tangela][max lvl 28]");
        System.out.println("[6] Fight other trainers");
        System.out.println(
                "     a.Lil[Water type][lvl 33]    b.Jack[Water type][lvl 37]     c.Jerome[Water type][lvl 33]");
        System.out.println("[7] Player Options");

        System.out.println(
                "     a.Map     b.Pokemons     c.Bag     d.Badges     e.Profile     f.Guides     g.PC     h.Save and Exit");
        System.out.printf("+%s+\n", "-".repeat(90));

        // Get player's choice
        System.out.print("Your choice: ");
        String choice = input.nextLine();

        // Handle player's choice
        if (choice.length() != 0) {
            if (choice.charAt(0) == '1' && choice.length() == 2) {
                if (choice.charAt(1) >= 'a' && choice.charAt(1) < 'a' + neighboringCities.size()) {
                    int cityIndex = choice.charAt(1) - 'a';
                    String nextCity = neighboringCities.get(cityIndex);
                    player.movetoCity(nextCity);
                } else {
                    System.out.println("Invalid choice! Please choose again.");
                }
                // Move the next city
            } else if (choice.equals("2")) {
                System.out.printf("+%s+\n", "-".repeat(90));
                System.out.println("You entered the Pokemon Center");
                System.out.println("Nurse: " + player.getName()
                        + " ! Welcome to the Pokemon Center. This is a place where you can heal up all your pokemons to their best status, let me heal up your pokemons real quick!");
                player.allhealup();
                System.out.println(
                        "Nurse: Oh, good! You and your Pokémon are looking great. Good luck on your journey and take care of your pokemons!");
                // Poke Center
            } else if (choice.equals("3")) {
                pokeMart(player);
                // Poke Mart
            } else if (choice.equals("4")) { // player challenges Gym Leader Blaine
                if (!player.getbadges()[6].equals("Volcano Badge")) {// if player does not have Volcano Badge
                    System.out.println("You are now challenging Gym Leader Blaine!");
                    Battle gymbattle = new Battle(player, "Blaine");// start a battle between player and Blaine
                    if (gymbattle.getwin()) {// if player wins
                        player.obtainbadge("Volcano Badge");// player obtains Volcano Badge
                        System.out.println(
                                "Blaine: I have burned down to nothing! Not even ashes remain! You have earned the Volcano Badge.");
                    }
                } else {// if player have Volcano Badge
                    System.out.println("You have already challenged this gym.");
                }

            } else if (choice.equals("5")) {// fight wild pokemon
                Random r = new Random();
                String[] wilds = { "Staryu", "Tangela" };// wild pokemon in the area
                int wild_choice = r.nextInt(2);
                int wild_lvl = r.nextInt(17, 29);// level range
                String wild_pokemon = wilds[wild_choice];
                System.out.println("A wild " + wild_pokemon + " appeared!");
                Pokemon wild = new Pokemon(wild_pokemon, wild_lvl, true);
                Battle wildbattle = new Battle(player, wild);
            } else if (choice.charAt(0) == '6' && choice.length() == 2) {// fight other trainers
                char player_choice = choice.charAt(1);
                switch (player_choice) {
                    case 'a':
                        // fight lil - starmie lvl 33
                        Battle trainerbattle = new Battle(player, "Lil");
                        break;
                    case 'b':
                        // fight jack - starmie lvl 37
                        trainerbattle = new Battle(player, "Jack");
                        break;
                    case 'c':
                        // fight jerome - staryu lvl 33, wartortle lvl 33
                        trainerbattle = new Battle(player, "Jerome");
                        break;
                    default:
                        System.out.println("Invalid choice! Please choose again.");
                }
            } else if (choice.charAt(0) == '7' && choice.length() == 2) {// player options
                char player_choice = choice.charAt(1);
                switch (player_choice) {
                    case 'a':
                        displayMap(player);
                        break;
                    case 'b':
                        player.alterteam();
                        break;
                    case 'c':
                        player.bag();
                        break;
                    case 'd':
                        player.showbadges();
                        break;
                    case 'e':
                        player.showprofile();
                        break;
                    case 'f':
                        guides();
                        break;
                    case 'g':
                        player.alterPC(player);
                        break;
                    case 'h':
                        save(player);
                        return false;
                    default:
                        System.out.println("Invalid choice! Please choose again.");
                }
            } else {
                System.out.println("Invalid choice! Please choose again.");
            }
        } else {
            System.out.println("Invalid choice! Please choose again.");
        }
        return true;
    }

    public static void pokeMart(Player player) {// Poke Mart

        Scanner input = new Scanner(System.in);
        System.out.println("You entered the Poke Mart");// display message
        while (true) {// loop will not end unless player choose to exit
            System.out.println("Hello trainer, welcome to the PokeMart.How can I help you?");// display dialogue
            System.out.println("1. Buy");
            System.out.println("2. Sell");
            System.out.println("3. Exit");
            System.out.print("Choose 1 action: ");
            String action = input.nextLine();
            if (action.equals("1")) {// player choose to buy items
                buy: // buy loop will not end unless player choose Back
                while (true) {
                    System.out.println("+----------------------Buy-----------------------+");
                    System.out.println("You have: $" + player.findMoney());// show player money
                    System.out.println("1. Poke Ball    - $ " + library.pokemon_items.get("Poke Ball").get("price"));// show
                    // all
                    // items
                    // and
                    // their
                    // prices
                    System.out.println("2. Great Ball   - $ " + library.pokemon_items.get("Great Ball").get("price"));
                    System.out.println("3. Ultra Ball   - $ " + library.pokemon_items.get("Ultra Ball").get("price"));
                    System.out.println("4. Potion       - $ " + library.pokemon_items.get("Potion").get("price"));
                    System.out.println("5. Super Potion - $ " + library.pokemon_items.get("Super Potion").get("price"));
                    System.out.println("6. Hyper Potion - $ " + library.pokemon_items.get("Hyper Potion").get("price"));
                    System.out.println("7. Max Potion   - $ " + library.pokemon_items.get("Max Potion").get("price"));
                    System.out.println("8. X Attack     - $ " + library.pokemon_items.get("X Attack").get("price"));
                    System.out.println("9. X Defend     - $ " + library.pokemon_items.get("X Defend").get("price"));
                    System.out.println("10. X Speed     - $ " + library.pokemon_items.get("X Speed").get("price"));
                    System.out.println("11. Revive      - $ " + library.pokemon_items.get("Revive").get("price"));
                    System.out.println("12. Back");
                    System.out.println("Select items(1-11) to buy/12 to go back: ");// get player choice
                    String choiceitem_st = input.nextLine();
                    if (player.isNum(choiceitem_st)) {// check choice format
                        int choiceitem = Integer.parseInt(choiceitem_st);// turn choice to integer
                        switch (choiceitem) {
                            case 1:// Player chooses Poke Ball
                                System.out.println("+-----Poke Ball-----+");// display item name
                                System.out.println("You have: " + player.getItems().get("Poke Ball"));// display number
                                // of items in
                                // player's bag
                                System.out
                                        .println("Buy Price: $ " + library.pokemon_items.get("Poke Ball").get("price"));// display
                                // the
                                // price
                                System.out.println("How many would you like to buy?");// ask player for the buying
                                // number
                                System.out.print("Enter a number: ");
                                String number_st = input.nextLine();// receive buying number
                                if (player.isNum(number_st)) {// check buying number format
                                    int number = Integer.parseInt(number_st);// turn buying number into integer
                                    int price = number * library.pokemon_items.get("Poke Ball").get("price");// calculate
                                    // the
                                    // total
                                    // price
                                    if (price > player.findMoney()) {// if player does not have enogh money
                                        System.out.println("You don't have enough money.");
                                    } else {// if player have enough or more money
                                        System.out.println("That will be $ " + price);// display the total price
                                        System.out.println(
                                                "Would you like to buy the item(s) you selected?(yes-y/no-any other input)");// ask
                                        // player
                                        // to
                                        // confirm
                                        // purchase
                                        String ccc = input.nextLine();// receive choice
                                        if (ccc.equals("y")) {// if choice is y
                                            player.deductMoney(price);// deduct the total price from player money
                                            player.obtainitems("Poke Ball", number);// player obtains entered number of
                                            // items
                                        }
                                    }
                                } else {
                                    System.out.println("Invalid input");
                                }
                                break;
                            case 2:// case 1 and case2-11 is similar only changing the item name according to what
                                // item the case number represents
                                System.out.println("+-----Great Ball-----+");
                                System.out.println("You have: " + player.getItems().get("Great Ball"));
                                System.out.println(
                                        "Buy Price: $ " + library.pokemon_items.get("Great Ball").get("price"));
                                System.out.println("How many would you like to buy?");
                                System.out.print("Enter a number: ");
                                number_st = input.nextLine();
                                if (player.isNum(number_st)) {
                                    int number = Integer.parseInt(number_st);
                                    int price = number * library.pokemon_items.get("Great Ball").get("price");
                                    if (price > player.findMoney()) {
                                        System.out.println("You don't have enough money.");
                                    } else {
                                        System.out.println("That will be $ " + price);
                                        System.out.println(
                                                "Would you like to buy the item(s) you selected?(yes-y/no-any other input)");
                                        String ccc = input.nextLine();
                                        if (ccc.equals("y")) {
                                            player.deductMoney(price);
                                            player.obtainitems("Great Ball", number);
                                        }
                                    }
                                } else {
                                    System.out.println("Invalid input");
                                }
                                break;
                            case 3:
                                System.out.println("+-----Ultra Ball-----+");
                                System.out.println("You have: " + player.getItems().get("Ultra Ball"));
                                System.out.println(
                                        "Buy Price: $ " + library.pokemon_items.get("Ultra Ball").get("price"));
                                System.out.println("How many would you like to buy?");
                                System.out.print("Enter a number: ");
                                number_st = input.nextLine();
                                if (player.isNum(number_st)) {
                                    int number = Integer.parseInt(number_st);
                                    int price = number * library.pokemon_items.get("Ultra Ball").get("price");
                                    if (price > player.findMoney()) {
                                        System.out.println("You don't have enough money.");
                                    } else {
                                        System.out.println("That will be $ " + price);
                                        System.out.println(
                                                "Would you like to buy the item(s) you selected?(yes-y/no-any other input)");
                                        String ccc = input.nextLine();
                                        if (ccc.equals("y")) {
                                            player.deductMoney(price);
                                            player.obtainitems("Ultra Ball", number);
                                        }
                                    }
                                } else {
                                    System.out.println("Invalid input");
                                }
                                break;
                            case 4:
                                System.out.println("+-----Potion-----+");
                                System.out.println("You have: " + player.getItems().get("Potion"));
                                System.out.println("Buy Price: $ " + library.pokemon_items.get("Potion").get("price"));
                                System.out.println("How many would you like to buy?");
                                System.out.print("Enter a number: ");
                                number_st = input.nextLine();
                                if (player.isNum(number_st)) {
                                    int number = Integer.parseInt(number_st);
                                    int price = number * library.pokemon_items.get("Potion").get("price");
                                    if (price > player.findMoney()) {
                                        System.out.println("You don't have enough money.");
                                    } else {
                                        System.out.println("That will be $ " + price);
                                        System.out.println(
                                                "Would you like to buy the item(s) you selected?(yes-y/no-any other input)");
                                        String ccc = input.nextLine();
                                        if (ccc.equals("y")) {
                                            player.deductMoney(price);
                                            player.obtainitems("Potion", number);
                                        }
                                    }
                                } else {
                                    System.out.println("Invalid input");
                                }
                                break;
                            case 5:
                                System.out.println("+-----Super Potion-----+");
                                System.out.println("You have: " + player.getItems().get("Super Potion"));
                                System.out
                                        .println("Buy Price: $ "
                                                + library.pokemon_items.get("Super Potion").get("price"));
                                System.out.println("How many would you like to buy?");
                                System.out.print("Enter a number: ");
                                number_st = input.nextLine();
                                if (player.isNum(number_st)) {
                                    int number = Integer.parseInt(number_st);
                                    int price = number * library.pokemon_items.get("Super Potion").get("price");
                                    if (price > player.findMoney()) {
                                        System.out.println("You don't have enough money.");
                                    } else {
                                        System.out.println("That will be $ " + price);
                                        System.out.println(
                                                "Would you like to buy the item(s) you selected?(yes-y/no-any other input)");
                                        String ccc = input.nextLine();
                                        if (ccc.equals("y")) {
                                            player.deductMoney(price);
                                            player.obtainitems("Super Potion", number);
                                        }
                                    }
                                } else {
                                    System.out.println("Invalid input");
                                }
                                break;
                            case 6:
                                System.out.println("+-----Hyper Potion-----+");
                                System.out.println("You have: " + player.getItems().get("Hyper Potion"));
                                System.out
                                        .println("Buy Price: $ "
                                                + library.pokemon_items.get("Hyper Potion").get("price"));
                                System.out.println("How many would you like to buy?");
                                System.out.print("Enter a number: ");
                                number_st = input.nextLine();
                                if (player.isNum(number_st)) {
                                    int number = Integer.parseInt(number_st);
                                    int price = number * library.pokemon_items.get("Hyper Potion").get("price");
                                    if (price > player.findMoney()) {
                                        System.out.println("You don't have enough money.");
                                    } else {
                                        System.out.println("That will be $ " + price);
                                        System.out.println(
                                                "Would you like to buy the item(s) you selected?(yes-y/no-any other input)");
                                        String ccc = input.nextLine();
                                        if (ccc.equals("y")) {
                                            player.deductMoney(price);
                                            player.obtainitems("Hyper Potion", number);
                                        }
                                    }
                                } else {
                                    System.out.println("Invalid input");
                                }
                                break;
                            case 7:
                                System.out.println("+-----Max Potion-----+");
                                System.out.println("You have: " + player.getItems().get("Max Potion"));
                                System.out.println(
                                        "Buy Price: $ " + library.pokemon_items.get("Max Potion").get("price"));
                                System.out.println("How many would you like to buy?");
                                System.out.print("Enter a number: ");
                                number_st = input.nextLine();
                                if (player.isNum(number_st)) {
                                    int number = Integer.parseInt(number_st);
                                    int price = number * library.pokemon_items.get("Max Potion").get("price");
                                    if (price > player.findMoney()) {
                                        System.out.println("You don't have enough money.");
                                    } else {
                                        System.out.println("That will be $ " + price);
                                        System.out.println(
                                                "Would you like to buy the item(s) you selected?(yes-y/no-any other input)");
                                        String ccc = input.nextLine();
                                        if (ccc.equals("y")) {
                                            player.deductMoney(price);
                                            player.obtainitems("Max Potion", number);
                                        }
                                    }
                                } else {
                                    System.out.println("Invalid input");
                                }
                                break;
                            case 8:
                                System.out.println("+-----X Attack-----+");
                                System.out.println("You have: " + player.getItems().get("X Attack"));
                                System.out
                                        .println("Buy Price: $ " + library.pokemon_items.get("X Attack").get("price"));
                                System.out.println("How many would you like to buy?");
                                System.out.print("Enter a number: ");
                                number_st = input.nextLine();
                                if (player.isNum(number_st)) {
                                    int number = Integer.parseInt(number_st);
                                    int price = number * library.pokemon_items.get("X Attack").get("price");
                                    if (price > player.findMoney()) {
                                        System.out.println("You don't have enough money.");
                                    } else {
                                        System.out.println("That will be $ " + price);
                                        System.out.println(
                                                "Would you like to buy the item(s) you selected?(yes-y/no-any other input)");
                                        String ccc = input.nextLine();
                                        if (ccc.equals("y")) {
                                            player.deductMoney(price);
                                            player.obtainitems("X Attack", number);
                                        }
                                    }
                                } else {
                                    System.out.println("Invalid input");
                                }
                                break;
                            case 9:
                                System.out.println("+-----X Defend-----+");
                                System.out.println("You have: " + player.getItems().get("X Defend"));
                                System.out
                                        .println("Buy Price: $ " + library.pokemon_items.get("X Defend").get("price"));
                                System.out.println("How many would you like to buy?");
                                System.out.print("Enter a number: ");
                                number_st = input.nextLine();
                                if (player.isNum(number_st)) {
                                    int number = Integer.parseInt(number_st);
                                    int price = number * library.pokemon_items.get("X Defend").get("price");
                                    if (price > player.findMoney()) {
                                        System.out.println("You don't have enough money.");
                                    } else {
                                        System.out.println("That will be $ " + price);
                                        System.out.println(
                                                "Would you like to buy the item(s) you selected?(yes-y/no-any other input)");
                                        String ccc = input.nextLine();
                                        if (ccc.equals("y")) {
                                            player.deductMoney(price);
                                            player.obtainitems("X Defend", number);
                                        }
                                    }
                                } else {
                                    System.out.println("Invalid input");
                                }
                                break;
                            case 10:
                                System.out.println("+-----X Speed-----+");
                                System.out.println("You have: " + player.getItems().get("X Speed"));
                                System.out.println("Buy Price: $ " + library.pokemon_items.get("X Speed").get("price"));
                                System.out.println("How many would you like to buy?");
                                System.out.print("Enter a number: ");
                                number_st = input.nextLine();
                                if (player.isNum(number_st)) {
                                    int number = Integer.parseInt(number_st);
                                    int price = number * library.pokemon_items.get("X Speed").get("price");
                                    if (price > player.findMoney()) {
                                        System.out.println("You don't have enough money.");
                                    } else {
                                        System.out.println("That will be $ " + price);
                                        System.out.println(
                                                "Would you like to buy the item(s) you selected?(yes-y/no-any other input)");
                                        String ccc = input.nextLine();
                                        if (ccc.equals("y")) {
                                            player.deductMoney(price);
                                            player.obtainitems("X Speed", number);
                                        }
                                    }
                                } else {
                                    System.out.println("Invalid input");
                                }
                                break;
                            case 11:
                                System.out.println("+-----Revive-----+");
                                System.out.println("You have: " + player.getItems().get("Revive"));
                                System.out.println("Buy Price: $ " + library.pokemon_items.get("Revive").get("price"));
                                System.out.println("How many would you like to buy?");
                                System.out.print("Enter a number: ");
                                number_st = input.nextLine();
                                if (player.isNum(number_st)) {
                                    int number = Integer.parseInt(number_st);
                                    int price = number * library.pokemon_items.get("Revive").get("price");
                                    if (price > player.findMoney()) {
                                        System.out.println("You don't have enough money.");
                                    } else {
                                        System.out.println("That will be $ " + price);
                                        System.out.println(
                                                "Would you like to buy the item(s) you selected?(yes-y/no-any other input)");
                                        String ccc = input.nextLine();
                                        if (ccc.equals("y")) {
                                            player.deductMoney(price);
                                            player.obtainitems("Revive", number);
                                        }
                                    }
                                } else {
                                    System.out.println("Invalid input");
                                }
                                break;
                            case 12:// player choose to go back to last selection page
                                break buy;// end buy loop
                            default:
                                System.out.println("Invalid choice");
                        }
                    } else {
                        System.out.println("Invalid choice");
                    }
                }
            } else if (action.equals("2")) {// Sell items
                sell: // sell loop will not end unless player chooses Back
                while (true) {
                    System.out.println("+-----------------------Sell-----------------------+");
                    System.out.println("Your items: ");// display all the items and their numbers in player's bag
                    player.showitems();
                    System.out.println("12. Back");
                    System.out.println("Note: Sell price of each items is 70% of their buy price.");
                    System.out.println("Select items(1-11) to sell/12 to go back: ");// get player's choice
                    String choiceitem_st = input.nextLine();
                    if (player.isNum(choiceitem_st)) {// check choice format
                        int choiceitem = Integer.parseInt(choiceitem_st);// turn choice into integer
                        switch (choiceitem) {
                            case 1:// layer chooses Poke Ball
                                System.out.println("+-----Poke Ball-----+");// display item name
                                System.out.println("You have: " + player.getItems().get("Poke Ball"));// display number
                                // of item in
                                // player's bag
                                System.out.println(
                                        "Sell Price: $ "
                                                + library.pokemon_items.get("Poke Ball").get("price") * 7 / 10);// display
                                // sell
                                // price
                                System.out.println("How many would you like to sell?");// ask player how many to sell
                                System.out.print("Enter a number: ");// get sell number
                                String number_st = input.nextLine();// check sell number format
                                if (player.isNum(number_st)) {
                                    int number = Integer.parseInt(number_st);// turn sell number into integer
                                    int price = number * library.pokemon_items.get("Poke Ball").get("price") * 7 / 10;// calculate
                                    // total
                                    // sell
                                    // price
                                    if (number > player.getItems().get("Poke Ball")) {// if the sell number is more than
                                        // what player have
                                        System.out.println("You don't have enough Poke Balls.");
                                    } else {// if player have more or same items than sell number
                                        System.out.println("That will be $ " + price); // display total price
                                        System.out.println(
                                                "Would you like to sell the item(s) you selected?(yes-y/no-any other input)");// ask
                                        // player
                                        // to
                                        // confirm
                                        // sell
                                        String ccc = input.nextLine();// get player's choice
                                        if (ccc.equals("y")) {// if player confirms sell
                                            player.addMoney(price);// player earns money equal to sell price
                                            player.deditems("Poke Ball", number);// player loses the number of items
                                            // sold
                                        }
                                    }
                                } else {
                                    System.out.println("Invalid input");
                                }
                                break;
                            case 2:// case 1 is similar to case2-11 only changing the item name according to what
                                // item the case number represents
                                System.out.println("+-----Great Ball-----+");
                                System.out.println("You have: " + player.getItems().get("Great Ball"));
                                System.out.println(
                                        "Sell Price: $ "
                                                + library.pokemon_items.get("Great Ball").get("price") * 7 / 10);
                                System.out.println("How many would you like to sell?");
                                System.out.print("Enter a number: ");
                                number_st = input.nextLine();
                                if (player.isNum(number_st)) {
                                    int number = Integer.parseInt(number_st);
                                    int price = number * library.pokemon_items.get("Great Ball").get("price") * 7 / 10;
                                    if (number > player.getItems().get("Great Ball")) {
                                        System.out.println("You don't have enough Great Balls.");
                                    } else {
                                        System.out.println("That will be $ " + price);
                                        System.out.println(
                                                "Would you like to sell the item(s) you selected?(yes-y/no-any other input)");
                                        String ccc = input.nextLine();
                                        if (ccc.equals("y")) {
                                            player.addMoney(price);
                                            player.deditems("Great Ball", number);
                                        }
                                    }
                                } else {
                                    System.out.println("Invalid input");
                                }
                                break;
                            case 3:
                                System.out.println("+-----Ultra Ball-----+");
                                System.out.println("You have: " + player.getItems().get("Ultra Ball"));
                                System.out.println(
                                        "Sell Price: $ "
                                                + library.pokemon_items.get("Ultra Ball").get("price") * 7 / 10);
                                System.out.println("How many would you like to sell?");
                                System.out.print("Enter a number: ");
                                number_st = input.nextLine();
                                if (player.isNum(number_st)) {
                                    int number = Integer.parseInt(number_st);
                                    int price = number * library.pokemon_items.get("Ultra Ball").get("price") * 7 / 10;
                                    if (number > player.getItems().get("Ultra Ball")) {
                                        System.out.println("You don't have enough Ultra Balls.");
                                    } else {
                                        System.out.println("That will be $ " + price);
                                        System.out.println(
                                                "Would you like to sell the item(s) you selected?(yes-y/no-any other input)");
                                        String ccc = input.nextLine();
                                        if (ccc.equals("y")) {
                                            player.addMoney(price);
                                            player.deditems("Ultra Ball", number);
                                        }
                                    }
                                } else {
                                    System.out.println("Invalid input");
                                }
                                break;
                            case 4:
                                System.out.println("+-----Potion-----+");
                                System.out.println("You have: " + player.getItems().get("Potion"));
                                System.out.println(
                                        "Sell Price: $ " + library.pokemon_items.get("Potion").get("price") * 7 / 10);
                                System.out.println("How many would you like to sell?");
                                System.out.print("Enter a number: ");
                                number_st = input.nextLine();
                                if (player.isNum(number_st)) {
                                    int number = Integer.parseInt(number_st);
                                    int price = number * library.pokemon_items.get("Potion").get("price") * 7 / 10;
                                    if (number > player.getItems().get("Potion")) {
                                        System.out.println("You don't have enough Potions.");
                                    } else {
                                        System.out.println("That will be $ " + price);
                                        System.out.println(
                                                "Would you like to sell the item(s) you selected?(yes-y/no-any other input)");
                                        String ccc = input.nextLine();
                                        if (ccc.equals("y")) {
                                            player.addMoney(price);
                                            player.deditems("Potion", number);
                                        }
                                    }
                                } else {
                                    System.out.println("Invalid input");
                                }
                                break;
                            case 5:
                                System.out.println("+-----Super Potion-----+");
                                System.out.println("You have: " + player.getItems().get("Super Potion"));
                                System.out.println(
                                        "Sell Price: $ "
                                                + library.pokemon_items.get("Super Potion").get("price") * 7 / 10);
                                System.out.println("How many would you like to sell?");
                                System.out.print("Enter a number: ");
                                number_st = input.nextLine();
                                if (player.isNum(number_st)) {
                                    int number = Integer.parseInt(number_st);
                                    int price = number * library.pokemon_items.get("Super Potion").get("price") * 7
                                            / 10;
                                    if (number > player.getItems().get("Super Potion")) {
                                        System.out.println("You don't have enough Super Potions.");
                                    } else {
                                        System.out.println("That will be $ " + price);
                                        System.out.println(
                                                "Would you like to sell the item(s) you selected?(yes-y/no-any other input)");
                                        String ccc = input.nextLine();
                                        if (ccc.equals("y")) {
                                            player.addMoney(price);
                                            player.deditems("Super Potion", number);
                                        }
                                    }
                                } else {
                                    System.out.println("Invalid input");
                                }
                                break;
                            case 6:
                                System.out.println("+-----Hyper Potion-----+");
                                System.out.println("You have: " + player.getItems().get("Hyper Potion"));
                                System.out.println(
                                        "Sell Price: $ "
                                                + library.pokemon_items.get("Hyper Potion").get("price") * 7 / 10);
                                System.out.println("How many would you like to sell?");
                                System.out.print("Enter a number: ");
                                number_st = input.nextLine();
                                if (player.isNum(number_st)) {
                                    int number = Integer.parseInt(number_st);
                                    int price = number * library.pokemon_items.get("Hyper Potion").get("price") * 7
                                            / 10;
                                    if (number > player.getItems().get("Hyper Potion")) {
                                        System.out.println("You don't have enough Hyper Potions.");
                                    } else {
                                        System.out.println("That will be $ " + price);
                                        System.out.println(
                                                "Would you like to sell the item(s) you selected?(yes-y/no-any other input)");
                                        String ccc = input.nextLine();
                                        if (ccc.equals("y")) {
                                            player.addMoney(price);
                                            player.deditems("Hyper Potion", number);
                                        }
                                    }
                                } else {
                                    System.out.println("Invalid input");
                                }
                                break;
                            case 7:
                                System.out.println("+-----Max Potion-----+");
                                System.out.println("You have: " + player.getItems().get("Max Potion"));
                                System.out.println(
                                        "Sell Price: $ "
                                                + library.pokemon_items.get("Max Potion").get("price") * 7 / 10);
                                System.out.println("How many would you like to sell?");
                                System.out.print("Enter a number: ");
                                number_st = input.nextLine();
                                if (player.isNum(number_st)) {
                                    int number = Integer.parseInt(number_st);
                                    int price = number * library.pokemon_items.get("Max Potion").get("price") * 7 / 10;
                                    if (number > player.getItems().get("Max Potion")) {
                                        System.out.println("You don't have enough Max Potions.");
                                    } else {
                                        System.out.println("That will be $ " + price);
                                        System.out.println(
                                                "Would you like to sell the item(s) you selected?(yes-y/no-any other input)");
                                        String ccc = input.nextLine();
                                        if (ccc.equals("y")) {
                                            player.addMoney(price);
                                            player.deditems("Max Potion", number);
                                        }
                                    }
                                } else {
                                    System.out.println("Invalid input");
                                }
                                break;
                            case 8:
                                System.out.println("+-----X Attack-----+");
                                System.out.println("You have: " + player.getItems().get("X Attack"));
                                System.out.println(
                                        "Sell Price: $ " + library.pokemon_items.get("X Attack").get("price") * 7 / 10);
                                System.out.println("How many would you like to sell?");
                                System.out.print("Enter a number: ");
                                number_st = input.nextLine();
                                if (player.isNum(number_st)) {
                                    int number = Integer.parseInt(number_st);
                                    int price = number * library.pokemon_items.get("X Attack").get("price") * 7 / 10;
                                    if (number > player.getItems().get("X Attack")) {
                                        System.out.println("You don't have enough X Attacks.");
                                    } else {
                                        System.out.println("That will be $ " + price);
                                        System.out.println(
                                                "Would you like to sell the item(s) you selected?(yes-y/no-any other input)");
                                        String ccc = input.nextLine();
                                        if (ccc.equals("y")) {
                                            player.addMoney(price);
                                            player.deditems("X Attack", number);
                                        }
                                    }
                                } else {
                                    System.out.println("Invalid input");
                                }
                                break;
                            case 9:
                                System.out.println("+-----X Defend-----+");
                                System.out.println("You have: " + player.getItems().get("X Defend"));
                                System.out.println(
                                        "Sell Price: $ " + library.pokemon_items.get("X Defend").get("price") * 7 / 10);
                                System.out.println("How many would you like to sell?");
                                System.out.print("Enter a number: ");
                                number_st = input.nextLine();
                                if (player.isNum(number_st)) {
                                    int number = Integer.parseInt(number_st);
                                    int price = number * library.pokemon_items.get("X Defend").get("price") * 7 / 10;
                                    if (number > player.getItems().get("X Defend")) {
                                        System.out.println("You don't have enough X Defends.");
                                    } else {
                                        System.out.println("That will be $ " + price);
                                        System.out.println(
                                                "Would you like to sell the item(s) you selected?(yes-y/no-any other input)");
                                        String ccc = input.nextLine();
                                        if (ccc.equals("y")) {
                                            player.addMoney(price);
                                            player.deditems("X Defend", number);
                                        }
                                    }
                                } else {
                                    System.out.println("Invalid input");
                                }
                                break;
                            case 10:
                                System.out.println("+-----X Speed-----+");
                                System.out.println("You have: " + player.getItems().get("X Speed"));
                                System.out.println(
                                        "Sell Price: $ " + library.pokemon_items.get("X Speed").get("price") * 7 / 10);
                                System.out.println("How many would you like to sell?");
                                System.out.print("Enter a number: ");
                                number_st = input.nextLine();
                                if (player.isNum(number_st)) {
                                    int number = Integer.parseInt(number_st);
                                    int price = number * library.pokemon_items.get("X Speed").get("price") * 7 / 10;
                                    if (number > player.getItems().get("X Speed")) {
                                        System.out.println("You don't have enough X Speeds.");
                                    } else {
                                        System.out.println("That will be $ " + price);
                                        System.out.println(
                                                "Would you like to sell the item(s) you selected?(yes-y/no-any other input)");
                                        String ccc = input.nextLine();
                                        if (ccc.equals("y")) {
                                            player.addMoney(price);
                                            player.deditems("X Speed", number);
                                        }
                                    }
                                } else {
                                    System.out.println("Invalid input");
                                }
                                break;
                            case 11:
                                System.out.println("+-----Revive-----+");
                                System.out.println("You have: " + player.getItems().get("Revive"));
                                System.out.println(
                                        "Sell Price: $ " + library.pokemon_items.get("Revive").get("price") * 7 / 10);
                                System.out.println("How many would you like to sell?");
                                System.out.print("Enter a number: ");
                                number_st = input.nextLine();
                                if (player.isNum(number_st)) {
                                    int number = Integer.parseInt(number_st);
                                    int price = number * library.pokemon_items.get("Revive").get("price") * 7 / 10;
                                    if (number > player.getItems().get("Revive")) {
                                        System.out.println("You don't have enough Revives.");
                                    } else {
                                        System.out.println("That will be $ " + price);
                                        System.out.println(
                                                "Would you like to sell the item(s) you selected?(yes-y/no-any other input)");
                                        String ccc = input.nextLine();
                                        if (ccc.equals("y")) {
                                            player.addMoney(price);
                                            player.deditems("Revive", number);
                                        }
                                    }
                                } else {
                                    System.out.println("Invalid input");
                                }
                                break;
                            case 12:// if player choose to go back to last selection page
                                break sell;// end the sell loop
                            default:
                                System.out.println("Invalid choice");
                        }
                    } else {
                        System.out.println("Invalid choice");
                    }
                }
            } else if (action.endsWith("3")) {// if player choose to exit Poke Mart
                System.out.println("Thank you for coming! Hope you have a nice day!");
                break;// end the loop
            } else {
                System.out.println("Invalid choice");
            }
        }
    }

    public static void displayMap(Player player) {// display the Map of the region and also the player's curent location
        // on the map
        // the location of the player will have ** surrounding the name of the
        // location(e.g: [**Pallet Town**])
        switch (player.findCurrentCity()) {
            case "Pallet Town":
                System.out.println("Map of Kanto:");
                System.out.println("[Pewter City]-----------------------[Cerulean City]-----------------|");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |            [Celadon City]----[Saffron City]-----[Lavender Town]");
                System.out.println("       |                   |                |                       |");
                System.out.println("[Viridian City]            |                |                       |");
                System.out.println("       |                   |                |                       |");
                System.out.println("       |                   |                |                       |");
                System.out.println("       |                   |        [Vermillion City]---------------|");
                System.out.println("       |                   |                                        |");
                System.out.println("[**Pallet Town**]          |                                        |");
                System.out.println("       |                   |                                        |");
                System.out.println("       |             [Fuchsia City]---------------------------------|");
                System.out.println("       |                   |");
                System.out.println("       |                   |");
                System.out.println("[Cinnabar Island]----------|");
                System.out.printf("+%s+\n", "-".repeat(90));
                break;
            case "Viridian City":
                System.out.println("Map of Kanto:");
                System.out.println("[Pewter City]-----------------------[Cerulean City]-----------------|");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |            [Celadon City]----[Saffron City]-----[Lavender Town]");
                System.out.println("       |                   |                |                       |");
                System.out.println("[**Viridian City**]        |                |                       |");
                System.out.println("       |                   |                |                       |");
                System.out.println("       |                   |                |                       |");
                System.out.println("       |                   |        [Vermillion City]---------------|");
                System.out.println("       |                   |                                        |");
                System.out.println("[Pallet Town]              |                                        |");
                System.out.println("       |                   |                                        |");
                System.out.println("       |             [Fuchsia City]---------------------------------|");
                System.out.println("       |                   |");
                System.out.println("       |                   |");
                System.out.println("[Cinnabar Island]----------|");
                System.out.printf("+%s+\n", "-".repeat(90));
                break;
            case "Pewter City":
                System.out.println("Map of Kanto:");
                System.out.println("[**Pewter City**]-------------------[Cerulean City]-----------------|");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |            [Celadon City]----[Saffron City]-----[Lavender Town]");
                System.out.println("       |                   |                |                       |");
                System.out.println("[Viridian City]            |                |                       |");
                System.out.println("       |                   |                |                       |");
                System.out.println("       |                   |                |                       |");
                System.out.println("       |                   |        [Vermillion City]---------------|");
                System.out.println("       |                   |                                        |");
                System.out.println("[Pallet Town]              |                                        |");
                System.out.println("       |                   |                                        |");
                System.out.println("       |             [Fuchsia City]---------------------------------|");
                System.out.println("       |                   |");
                System.out.println("       |                   |");
                System.out.println("[Cinnabar Island]----------|");
                System.out.printf("+%s+\n", "-".repeat(90));
                break;
            case "Cerulean City":
                System.out.println("Map of Kanto:");
                System.out.println("[Pewter City]---------------------[**Cerulean City**]---------------|");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |            [Celadon City]----[Saffron City]-----[Lavender Town]");
                System.out.println("       |                   |                |                       |");
                System.out.println("[Viridian City]            |                |                       |");
                System.out.println("       |                   |                |                       |");
                System.out.println("       |                   |                |                       |");
                System.out.println("       |                   |        [Vermillion City]---------------|");
                System.out.println("       |                   |                                        |");
                System.out.println("[Pallet Town]              |                                        |");
                System.out.println("       |                   |                                        |");
                System.out.println("       |             [Fuchsia City]---------------------------------|");
                System.out.println("       |                   |");
                System.out.println("       |                   |");
                System.out.println("[Cinnabar Island]----------|");
                System.out.printf("+%s+\n", "-".repeat(90));
                break;
            case "Saffron City":
                System.out.println("Map of Kanto:");
                System.out.println("[Pewter City]-----------------------[Cerulean City]-----------------|");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |            [Celadon City]--[**Saffron City**]---[Lavender Town]");
                System.out.println("       |                   |                |                       |");
                System.out.println("[Viridian City]            |                |                       |");
                System.out.println("       |                   |                |                       |");
                System.out.println("       |                   |                |                       |");
                System.out.println("       |                   |        [Vermillion City]---------------|");
                System.out.println("       |                   |                                        |");
                System.out.println("[Pallet Town]              |                                        |");
                System.out.println("       |                   |                                        |");
                System.out.println("       |             [Fuchsia City]---------------------------------|");
                System.out.println("       |                   |");
                System.out.println("       |                   |");
                System.out.println("[Cinnabar Island]----------|");
                System.out.printf("+%s+\n", "-".repeat(90));
                break;
            case "Celadon City":
                System.out.println("Map of Kanto:");
                System.out.println("[Pewter City]-----------------------[Cerulean City]-----------------|");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |          [**Celadon City**]--[Saffron City]-----[Lavender Town]");
                System.out.println("       |                   |                |                       |");
                System.out.println("[Viridian City]            |                |                       |");
                System.out.println("       |                   |                |                       |");
                System.out.println("       |                   |                |                       |");
                System.out.println("       |                   |        [Vermillion City]---------------|");
                System.out.println("       |                   |                                        |");
                System.out.println("[Pallet Town]              |                                        |");
                System.out.println("       |                   |                                        |");
                System.out.println("       |             [Fuchsia City]---------------------------------|");
                System.out.println("       |                   |");
                System.out.println("       |                   |");
                System.out.println("[Cinnabar Island]----------|");
                System.out.printf("+%s+\n", "-".repeat(90));
                break;
            case "Lavender Town":
                System.out.println("Map of Kanto:");
                System.out.println("[Pewter City]-----------------------[Cerulean City]-----------------|");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |            [Celadon City]----[Saffron City]---[**Lavender Town**]");
                System.out.println("       |                   |                |                       |");
                System.out.println("[Viridian City]            |                |                       |");
                System.out.println("       |                   |                |                       |");
                System.out.println("       |                   |                |                       |");
                System.out.println("       |                   |        [Vermillion City]---------------|");
                System.out.println("       |                   |                                        |");
                System.out.println("[Pallet Town]              |                                        |");
                System.out.println("       |                   |                                        |");
                System.out.println("       |             [Fuchsia City]---------------------------------|");
                System.out.println("       |                   |");
                System.out.println("       |                   |");
                System.out.println("[Cinnabar Island]----------|");
                System.out.printf("+%s+\n", "-".repeat(90));
                break;
            case "Vermillion City":
                System.out.println("Map of Kanto:");
                System.out.println("[Pewter City]-----------------------[Cerulean City]-----------------|");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |            [Celadon City]----[Saffron City]-----[Lavender Town]");
                System.out.println("       |                   |                |                       |");
                System.out.println("[Viridian City]            |                |                       |");
                System.out.println("       |                   |                |                       |");
                System.out.println("       |                   |                |                       |");
                System.out.println("       |                   |      [**Vermillion City**]-------------|");
                System.out.println("       |                   |                                        |");
                System.out.println("[Pallet Town]              |                                        |");
                System.out.println("       |                   |                                        |");
                System.out.println("       |             [Fuchsia City]---------------------------------|");
                System.out.println("       |                   |");
                System.out.println("       |                   |");
                System.out.println("[Cinnabar Island]----------|");
                System.out.printf("+%s+\n", "-".repeat(90));
                break;
            case "Fuschia City":
                System.out.println("Map of Kanto:");
                System.out.println("[Pewter City]-----------------------[Cerulean City]-----------------|");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |            [Celadon City]----[Saffron City]-----[Lavender Town]");
                System.out.println("       |                   |                |                       |");
                System.out.println("[Viridian City]            |                |                       |");
                System.out.println("       |                   |                |                       |");
                System.out.println("       |                   |                |                       |");
                System.out.println("       |                   |        [Vermillion City]---------------|");
                System.out.println("       |                   |                                        |");
                System.out.println("[Pallet Town]              |                                        |");
                System.out.println("       |                   |                                        |");
                System.out.println("       |           [**Fuchsia City**]-------------------------------|");
                System.out.println("       |                   |");
                System.out.println("       |                   |");
                System.out.println("[Cinnabar Island]----------|");
                System.out.printf("+%s+\n", "-".repeat(90));
                break;
            case "Cinnabar Island":
                System.out.println("Map of Kanto:");
                System.out.println("[Pewter City]-----------------------[Cerulean City]-----------------|");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |                                    |                       |");
                System.out.println("       |            [Celadon City]----[Saffron City]-----[Lavender Town]");
                System.out.println("       |                   |                |                       |");
                System.out.println("[Viridian City]            |                |                       |");
                System.out.println("       |                   |                |                       |");
                System.out.println("       |                   |                |                       |");
                System.out.println("       |                   |        [Vermillion City]---------------|");
                System.out.println("       |                   |                                        |");
                System.out.println("[Pallet Town]              |                                        |");
                System.out.println("       |                   |                                        |");
                System.out.println("       |             [Fuchsia City]---------------------------------|");
                System.out.println("       |                   |");
                System.out.println("       |                   |");
                System.out.println("[**Cinnabar Island**]------|");
                System.out.printf("+%s+\n", "-".repeat(90));
                break;
        }
    }
}
