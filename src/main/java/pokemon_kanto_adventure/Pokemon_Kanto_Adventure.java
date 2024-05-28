package pokemon_kanto_adventure;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

public class Pokemon_Kanto_Adventure {
    private static int slotNumber;
    private static final Logger logger = Logger.getLogger(Pokemon_Kanto_Adventure.class.getName());

    public static void main(String[] args) {
        Load load = new Load();
        library.readallfiles();
        while (true) {
            title();
            Player player = selectSave(load.getConnection());
            if (player == null) {
                break;
            } else {
                selectionPanel(player);
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

    public static Player selectSave(Connection con) {
        Scanner sc = new Scanner(System.in);
        boolean[] saveExists = new boolean[3];
        boolean[] containsData = new boolean[3];

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

    private static void printSaveSlots(Connection con, boolean[] saveExists, boolean[] containsData)
            throws SQLException {
        System.out.printf("+%s+\n", "-".repeat(90));
        System.out.println("Welcome to Pokemon - Kanto Adventures");
        System.out.printf("+%s+\n", "-".repeat(90));
        System.out.println("[1] Load Game:");

        for (int i = 0; i < 3; i++) {
            String playerName = getPlayerNameFromSlot(con, i + 1);
            saveExists[i] = playerName != null;
            containsData[i] = saveExists[i];
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

    private static String getPlayerNameFromSlot(Connection con, int slot) throws SQLException {
        String query = "SELECT player_name FROM saveslots WHERE slot_number = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, slot);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("player_name");
            }
        }
        return null;
    }

    private static Player processSaveChoice(Connection con, String choice, boolean[] saveExists, boolean[] containsData)
            throws SQLException {
        if (choice.length() == 2) {
            char menuChoice = choice.charAt(0);
            char slotChoice = choice.charAt(1);
            int slotIndex = slotChoice - 'a';

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
            return null;
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
                    Pokemon Bulbasaur = new Pokemon("Bulbasaur", 5);
                    System.out.printf("OAK: You chose %s, an amazing choice. Best of luck!\n", Bulbasaur.findname());
                    player.addPokemon(Bulbasaur);
                    break;
                case 2:
                    Pokemon Squirtle = new Pokemon("Squirtle", 5);
                    System.out.printf("OAK: You chose %s, an amazing choice. Best of luck!\n", Squirtle.findname());
                    player.addPokemon(Squirtle);
                    break;
                case 3:
                    Pokemon Charmander = new Pokemon("Charmander", 5);
                    System.out.printf("OAK: You chose %s, an amazing choice. Best of luck!\n", Charmander.findname());
                    player.addPokemon(Charmander);
                    break;
                default:
                    isValid = false;
                    System.out.println("Invalid choice");
            }
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

    public static void guides() {
        Scanner sc = new Scanner(System.in);
        String choice;
        System.out.printf("+%s+\n","-".repeat(90));
        System.out.printf("|%s%s%s|\n"," ".repeat(39),"Game Guides:"," ".repeat(39));
        System.out.printf("+%s+\n","-".repeat(90));
        System.out.printf("|%-90s|\n"," 1. How to move to different cities");
        System.out.printf("|%-90s|\n"," 2. How to use the Pokémon Center");
        System.out.printf("|%-90s|\n"," 3. How to shop at the Poké Mart ");
        System.out.printf("|%-90s|\n"," 4. How to battle wild Pokémon");
        System.out.printf("|%-90s|\n"," 5. How to manage your Pokémon team");
        System.out.printf("|%-90s|\n"," 6. How to access your bag ");
        System.out.printf("|%-90s|\n"," 7. How to view your badges ");
        System.out.printf("|%-90s|\n"," 8. How to view your profile");
        System.out.printf("|%-90s|\n"," 9. How to save and exit the game");
        System.out.printf("+%s+\n","-".repeat(90));
        System.out.print("Your choice (Enter 0 to exit): ");
        choice = sc.nextLine();
        System.out.printf("+%s+\n","-".repeat(90));
    }

    public static void save(Player player) {
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

                ps.setInt(16, player.findMoney());
                ps.setInt(17, player.getrivalwins());
                ps.setInt(18, player.getvictories());
                ps.setString(19, player.findCurrentCity());
                ps.executeUpdate();
            }

            // Save badge
            try (PreparedStatement ps = con.prepareStatement(insertBadge)) {
                int count = 2;
                ps.setInt(1, slotNumber);
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

    public static void selectionPanel(Player player) {
        boolean tf = true;
        while (tf) {
            System.out.printf("+%s+\n", "-".repeat(90));
            String currentCity = player.findCurrentCity();
            System.out.println("You are currently in: " + currentCity);
            System.out.printf("+%s+\n", "-".repeat(90));
            switch (currentCity) {
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

    public static boolean selectionPalletTown(Player player) {
        displayMap(player);
        Scanner input = new Scanner(System.in);
        System.out.println("[1] Move to:");
        ArrayList<String> neighboringCities = library.kantoMap.getNeighbours("Pallet Town");
        for (int i = 0; i < neighboringCities.size(); i++) {
            System.out.println((char) ('a' + i) + ". " + neighboringCities.get(i));
        }
        System.out.println("[2] Talk to Mom(heal all pokemon to full status)");
        System.out.println("[3] Fight Wild Pokemon [Caterpie, Rattata, Mankey][max lvl 5]");
        System.out.println("[4] Player Options");

        System.out.println(
                "     a.Map     b.Pokemons     c.Bag     d.Badges     e.Profile     f.Guides     g.Save and Exit");
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
                    System.out.println("Invalid choice");
                }
                // Move the next city
            } else if (choice.equals("2")) {
                System.out.printf("+%s+\n", "-".repeat(90));
                System.out.println("You talked to Mom");
                System.out.println("Mom: " + player.getName()
                        + " ! Welcome home. It sounds like you had quite an experience. Maybe you should take a quick rest.");
                player.allhealup();
                System.out.println(
                        "Mom: Oh, good! You and your Pokémon are looking great. I just heard from Prof. Oak. He said that Pokémon's energy is measured in HP. If your Pokémon lose their HP, you can restore them at any Pokémon Center. If you're going to travel far away, the smart Trainer stocks up on Potions at the Pokémon Mart. Make me proud, honey! Take care!");
                // Talk to Mom
            } else if (choice.equals("3")) {
                Random r = new Random();
                String[] wilds = { "Caterpie", "Rattata", "Mankey" };
                int wild_choice = r.nextInt(3);
                int wild_lvl = r.nextInt(2, 6);
                String wild_pokemon = wilds[wild_choice];
                System.out.println("A wild " + wild_pokemon + " appeared! [ " + wild_lvl + " ] ");
                Pokemon wild = new Pokemon(wild_pokemon, wild_lvl);
                Battle wildbattle = new Battle(player, wild);
            } else if (choice.charAt(0) == '4' && choice.length() == 2) {
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

    public static boolean selectionViridianCity(Player player) {
        displayMap(player);
        Scanner input = new Scanner(System.in);
        System.out.println("[1] Move to:");
        ArrayList<String> neighboringCities = library.kantoMap.getNeighbours("Viridian City");
        for (int i = 0; i < neighboringCities.size(); i++) {
            System.out.println((char) ('a' + i) + ". " + neighboringCities.get(i));
        }
        System.out.println("[2] Pokemon Center");
        System.out.println("[3] Poke Mart");
        System.out.println("[4] Fight Gym Leader [ Giovanni - Ground type ] [Recommended Pokemon Level: 50]");
        System.out.println("[5] Player Options");

        System.out.println(
                "     a.Map     b.Pokemons     c.Bag     d.Badges     e.Profile     f.Guides     g.Save and Exit");
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
                    System.out.println("Invalid choice");
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
                // Talk to Mom
            } else if (choice.equals("3")) {
                pokeMart(player);
            } else if (choice.equals("4")) {
                if (!player.getbadges()[7].equals("Earth Badge")) {
                    if (player.getbadges()[0].equals("Boulder Badge") && player.getbadges()[1].equals("Cascade Badge")
                            && player.getbadges()[2].equals("Thunder Badge")
                            && player.getbadges()[3].equals("Rainbow Badge")
                            && player.getbadges()[4].equals("Soul Badge") && player.getbadges()[5].equals("Marsh Badge")
                            && player.getbadges()[6].equals("Volcano Badge")) {
                        System.out.println("You have all other gym badges! This is your final battle. Good luck!");
                        System.out.println("You are now challenging Gym Leader Giovanni!");
                        Battle gymbattle = new Battle(player, "Giovanni");

                        if (gymbattle.getwin()) {
                            System.out.println(
                                    "Giovanni: You are sure the strongest trainer in this region, here is the Earth Badge. It is evidence of your mastery as a Pokémon Trainer.");
                            player.obtainbadge("Earth Badge");
                        }

                    } else {
                        System.out.println(
                                "You have not obtained all the other badges yet, Giovanni is the strongest leader in the region.");
                        System.out.println(
                                "You are not strong enough to face him at this moment, please come back with all other badges to prove that you are a worthy opponent");
                    }
                } else {
                    System.out.println("You have already challenged this gym.");
                }
            } else if (choice.charAt(0) == '5' && choice.length() == 2) {
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

    public static boolean selectionPewterCity(Player player) {
        displayMap(player);
        Scanner input = new Scanner(System.in);
        System.out.println("[1] Move to:");
        ArrayList<String> neighboringCities = library.kantoMap.getNeighbours("Pewter City");
        for (int i = 0; i < neighboringCities.size(); i++) {
            System.out.println((char) ('a' + i) + ". " + neighboringCities.get(i));
        }
        System.out.println("[2] Pokemon Center");
        System.out.println("[3] Poke Mart");
        System.out.println("[4] Fight Gym Leader [ Brock - Rock type ] [Recommended Pokemon Level: 14]");
        System.out.println("[5] Fight Wild Pokemon [Caterpie, Metapod, Pikachu][max lvl 6]");
        System.out.println("[6] Fight other trainers");
        System.out.println(
                "     a.Rick[Bug type][lvl 6]    b.Anthony[Bug type][lvl 9]     c.Charlie[Electric type][lvl 8]");
        System.out.println("[7] Player Options");

        System.out.println(
                "     a.Map     b.Pokemons     c.Bag     d.Badges     e.Profile     f.Guides     g.Save and Exit");
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
                    System.out.println("Invalid choice");
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
            } else if (choice.equals("4")) {
                if (!player.getbadges()[0].equals("Boulder Badge")) {
                    System.out.println("You are now challenging Gym Leader Brock!");
                    Battle gymbattle = new Battle(player, "Brock");
                    if (gymbattle.getwin()) {
                        player.obtainbadge("Boulder Badge");
                        System.out.println(
                                "Brock: I took you for granted, and so I lost. As proof of your victory, I confer on you this...the official Boulder Badge.");
                    }
                } else {
                    System.out.println("You have already challenged this gym.");
                }
            } else if (choice.equals("5")) {
                Random r = new Random();
                String[] wilds = { "Caterpie", "Metapod", "Pikachu" };
                int wild_choice = r.nextInt(3);
                int wild_lvl = r.nextInt(3, 7);
                String wild_pokemon = wilds[wild_choice];
                System.out.println("A wild " + wild_pokemon + " appeared!");
                Pokemon wild = new Pokemon(wild_pokemon, wild_lvl);
                Battle wildbattle = new Battle(player, wild);
            } else if (choice.charAt(0) == '6' && choice.length() == 2) {
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
            } else if (choice.charAt(0) == '7' && choice.length() == 2) {
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

    public static boolean selectionCeruleanCity(Player player) {
        displayMap(player);
        Scanner input = new Scanner(System.in);
        System.out.println("[1] Move to:");
        ArrayList<String> neighboringCities = library.kantoMap.getNeighbours("Cerulean City");
        for (int i = 0; i < neighboringCities.size(); i++) {
            System.out.println((char) ('a' + i) + ". " + neighboringCities.get(i));
        }
        System.out.println("[2] Pokemon Center");
        System.out.println("[3] Poke Mart");
        System.out.println("[4] Fight Gym Leader [ Misty - Water type ] [Recommended Pokemon Level: 21]");
        System.out.println("[5] Fight Wild Pokemon [Sandshrew, Geodude, Onix][max lvl 12]");
        System.out.println("[6] Fight other trainers");
        System.out.println(
                "     a.Rocket Grunt[Ground, Fighting type][lvl 17]    b.Marcos[Rock type][lvl 11]     c.Jovan[Electric type][lvl 14]");
        System.out.println("[7] Player Options");

        System.out.println(
                "     a.Map     b.Pokemons     c.Bag     d.Badges     e.Profile     f.Guides     g.Save and Exit");
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
                    System.out.println("Invalid choice");
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
            } else if (choice.equals("4")) {
                if (!player.getbadges()[1].equals("Cascade Badge")) {
                    System.out.println("You are now challenging Gym Leader Misty!");
                    Battle gymbattle = new Battle(player, "Misty");
                    if (gymbattle.getwin()) {
                        player.obtainbadge("Cascade Badge");
                        System.out.println(
                                "Misty: Wow! You're too much, all right! You can have the Cascade Badge to show that you beat me.");
                    }
                } else {
                    System.out.println("You have already challenged this gym");
                }
            } else if (choice.equals("5")) {
                Random r = new Random();
                String[] wilds = { "Sandshrew", "Geodude", "Onix" };
                int wild_choice = r.nextInt(3);
                int wild_lvl = r.nextInt(8, 13);
                String wild_pokemon = wilds[wild_choice];
                System.out.println("A wild " + wild_pokemon + " appeared!");
                Pokemon wild = new Pokemon(wild_pokemon, wild_lvl);
                Battle wildbattle = new Battle(player, wild);
            } else if (choice.charAt(0) == '6' && choice.length() == 2) {
                char player_choice = choice.charAt(1);
                switch (player_choice) {
                    case 'a':
                        // fight grunt - machop lvl 17, sandshrew lvl 17
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
            } else if (choice.charAt(0) == '7' && choice.length() == 2) {
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

    public static boolean selectionSaffronCity(Player player) {
        displayMap(player);
        Scanner input = new Scanner(System.in);
        System.out.println("[1] Move to:");
        ArrayList<String> neighboringCities = library.kantoMap.getNeighbours("Saffron City");
        for (int i = 0; i < neighboringCities.size(); i++) {
            System.out.println((char) ('a' + i) + ". " + neighboringCities.get(i));
        }
        System.out.println("[2] Pokemon Center");
        System.out.println("[3] Poke Mart");
        System.out.println("[4] Fight Gym Leader [ Sabrina - Psychic type ] [Recommended Pokemon Level: 43]");
        System.out.println("[5] Fight Wild Pokemon [Oddish, Bellsprout, Growlithe, Abra][max lvl 16]");
        System.out.println("[6] Fight other trainers");
        System.out.println(
                "     a.Ricky[Water type][lvl 30]    b.Jeff[Normal type][lvl 29]     c.Elijah[Bug type][lvl 30]");
        System.out.println("[7] Rival Race");
        System.out.println("[8] Player Options");

        System.out.println(
                "     a.Map     b.Pokemons     c.Bag     d.Badges     e.Profile     f.Guides     g.Save and Exit");
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
                    System.out.println("Invalid choice");
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
            } else if (choice.equals("4")) {
                if (!player.getbadges()[5].equals("Marsh Badge")) {
                    System.out.println("You are now challenging Gym Leader Sabrina!");
                    Battle gymbattle = new Battle(player, "Sabrina");
                    if (gymbattle.getwin()) {
                        player.obtainbadge("Marsh Badge");
                        System.out.println(
                                "Sabrina: This loss shocks me! But a loss is a loss. I admit I didn't work hard enough to win. You earned the Marsh Badge.");
                    }
                } else {
                    System.out.println("You have already challenged this gym.");
                }
            } else if (choice.equals("5")) {
                Random r = new Random();
                String[] wilds = { "Oddish", "Bellsprout", "Growlithe", "Abra" };
                int wild_choice = r.nextInt(4);
                int wild_lvl = r.nextInt(11, 17);
                String wild_pokemon = wilds[wild_choice];
                System.out.println("A wild " + wild_pokemon + " appeared!");
                Pokemon wild = new Pokemon(wild_pokemon, wild_lvl);
                Battle wildbattle = new Battle(player, wild);
            } else if (choice.charAt(0) == '6' && choice.length() == 2) {
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
            } else if (choice.equals("7")) {
                player.startrivalrace();
            } else if (choice.charAt(0) == '8' && choice.length() == 2) {
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

    public static boolean selectionCeladonCity(Player player) {
        displayMap(player);
        Scanner input = new Scanner(System.in);
        System.out.println("[1] Move to:");
        ArrayList<String> neighboringCities = library.kantoMap.getNeighbours("Celadon City");
        for (int i = 0; i < neighboringCities.size(); i++) {
            System.out.println((char) ('a' + i) + ". " + neighboringCities.get(i));
        }
        System.out.println("[2] Pokemon Center");
        System.out.println("[3] Poke Mart");
        System.out.println("[4] Fight Gym Leader [ Erika - Grass type ] [Recommended Pokemon Level: 29]");
        System.out.println("[5] Fight Wild Pokemon [Koffing, Grimer, Machop, Ponyta][max lvl 23]");
        System.out.println("[6] Fight Snorlax [lvl 30]");
        System.out.println("[7] Fight other trainers");
        System.out.println(
                "     a.Lao[Poison type][lvl 27]    b.Koji[Fighting type][lvl 27]     c.Lea[Fire type][lvl 27]");
        System.out.println("[8] Player Options");

        System.out.println(
                "     a.Map     b.Pokemons     c.Bag     d.Badges     e.Profile     f.Guides     g.Save and Exit");
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
                    System.out.println("Invalid choice");
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
            } else if (choice.equals("4")) {
                if (!player.getbadges()[3].equals("Rainbow Badge")) {
                    System.out.println("You are now challenging Gym Leader Erika!");
                    Battle gymbattle = new Battle(player, "Erika");
                    if (gymbattle.getwin()) {
                        player.obtainbadge("Rainbow Badge");
                        System.out.println(
                                "Erika: Oh! I concede defeat. You are remarkably strong. I must confer on you the Rainbow Badge.");
                    }
                } else {
                    System.out.println("You have already challenged this gym.");
                }
            } else if (choice.equals("5")) {
                Random r = new Random();
                String[] wilds = { "Koffing", "Grimer", "Machop", "Ponyta" };
                int wild_choice = r.nextInt(4);
                int wild_lvl = r.nextInt(20, 24);
                String wild_pokemon = wilds[wild_choice];
                System.out.println("A wild " + wild_pokemon + " appeared!");
                Pokemon wild = new Pokemon(wild_pokemon, wild_lvl);
                Battle wildbattle = new Battle(player, wild);
            } else if (choice.equals("6")) {
                System.out.println("A wild Snorlax is blocking the road!");
                Pokemon wild = new Pokemon("Snorlax", 30);
                Battle wildbattle = new Battle(player, wild);
            } else if (choice.charAt(0) == '7' && choice.length() == 2) {
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
            } else if (choice.charAt(0) == '8' && choice.length() == 2) {
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

    public static boolean selectionLavenderTown(Player player) {
        displayMap(player);
        Scanner input = new Scanner(System.in);
        System.out.println("[1] Move to:");
        ArrayList<String> neighboringCities = library.kantoMap.getNeighbours("Lavender Town");
        for (int i = 0; i < neighboringCities.size(); i++) {
            System.out.println((char) ('a' + i) + ". " + neighboringCities.get(i));
        }
        System.out.println("[2] Pokemon Center");
        System.out.println("[3] Poke Mart");
        System.out.println("[4] Pokemon Tower - Poke Maze");
        System.out.println("[5] Fight Wild Pokemon [Magnemite, Voltorb, Nidoran-M, Nidoran-F, Venonat][max lvl 20]");
        System.out.println("[6] Fight Snorlax [lvl 30]");
        System.out.println("[7] Fight other trainers");
        System.out.println(
                "     a.Luca[Electric type][lvl 29]    b.Justin[Poison type][lvl 29]     c.Tower Grunt[Normal type][lvl 27]");
        System.out.println("[8] Player Options");

        System.out.println(
                "     a.Map     b.Pokemons     c.Bag     d.Badges     e.Profile     f.Guides     g.Save and Exit");
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
                    System.out.println("Invalid choice");
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
            } else if (choice.equals("4")) {
                PokeMaze maze = new PokeMaze();
                maze.simulation(player);
            } else if (choice.equals("5")) {
                Random r = new Random();
                String[] wilds = { "Magnemite", "Voltorb", "Nidoran-M", "Nidoran-F", "Venonat" };
                int wild_choice = r.nextInt(5);
                int wild_lvl = r.nextInt(14, 21);
                String wild_pokemon = wilds[wild_choice];
                System.out.println("A wild " + wild_pokemon + " appeared!");
                Pokemon wild = new Pokemon(wild_pokemon, wild_lvl);
                Battle wildbattle = new Battle(player, wild);
            } else if (choice.equals("6")) {
                System.out.println("A wild Snorlax is blocking the road!");
                Pokemon wild = new Pokemon("Snorlax", 30);
                Battle wildbattle = new Battle(player, wild);
            } else if (choice.charAt(0) == '7' && choice.length() == 2) {
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
            } else if (choice.charAt(0) == '8' && choice.length() == 2) {
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

    public static boolean selectionVermillionCity(Player player) {
        displayMap(player);
        Scanner input = new Scanner(System.in);
        System.out.println("[1] Move to:");
        ArrayList<String> neighboringCities = library.kantoMap.getNeighbours("Vermillion City");
        for (int i = 0; i < neighboringCities.size(); i++) {
            System.out.println((char) ('a' + i) + ". " + neighboringCities.get(i));
        }
        System.out.println("[2] Pokemon Center");
        System.out.println("[3] Poke Mart");
        System.out.println("[4] Fight Gym Leader [ Lt. Surge - Electric type ] [Recommended Pokemon Level: 24]");
        System.out.println("[5] Fight Wild Pokemon [Diglett, Jigglypuff, Eevee][max lvl 22]");
        System.out.println("[6] Fight other trainers");
        System.out.println(
                "     a.Yasu[Normal type][lvl 17]    b.Dave[Poison type][lvl 18]     c.Bernie[Electric type][lvl 18]");
        System.out.println("[7] Player Options");

        System.out.println(
                "     a.Map     b.Pokemons     c.Bag     d.Badges     e.Profile     f.Guides     g.Save and Exit");
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
                    System.out.println("Invalid choice");
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
            } else if (choice.equals("4")) {
                if (!player.getbadges()[2].equals("Thunder Badge")) {
                    System.out.println("You are now challenging Gym Leader Lt. Surge!");
                    Battle gymbattle = new Battle(player, "Lt. Surge");
                    if (gymbattle.getwin()) {
                        player.obtainbadge("Thunder Badge");
                        System.out.println(
                                "Lt. Surge: Now that's a shocker! You're the real deal, kid! Fine, then, take the Thunder Badge!");
                    }
                } else {
                    System.out.println("You have already challenged this gym");
                }

            } else if (choice.equals("5")) {
                Random r = new Random();
                String[] wilds = { "Diglett", "Jigglypuff", "Eevee" };
                int wild_choice = r.nextInt(3);
                int wild_lvl = r.nextInt(15, 23);
                String wild_pokemon = wilds[wild_choice];
                System.out.println("A wild " + wild_pokemon + " appeared!");
                Pokemon wild = new Pokemon(wild_pokemon, wild_lvl);
                Battle wildbattle = new Battle(player, wild);
            } else if (choice.charAt(0) == '6' && choice.length() == 2) {
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
            } else if (choice.charAt(0) == '7' && choice.length() == 2) {
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

    public static boolean selectionFuschiaCity(Player player) {
        displayMap(player);
        Scanner input = new Scanner(System.in);
        System.out.println("[1] Move to:");
        ArrayList<String> neighboringCities = library.kantoMap.getNeighbours("Fuschia City");
        for (int i = 0; i < neighboringCities.size(); i++) {
            System.out.println((char) ('a' + i) + ". " + neighboringCities.get(i));
        }
        System.out.println("[2] Pokemon Center");
        System.out.println("[3] Poke Mart");
        System.out.println("[4] Fight Gym Leader [ Koga - Poison type ] [Recommended Pokemon Level: 43]");
        System.out.println("[5] Fight Wild Pokemon [Grimer, Rattata, Raticate][max lvl 29]");
        System.out.println("[6] Fight other trainers");
        System.out.println(
                "     a.Charles[Poison type][lvl 39]    b.Jacob[Fire type][lvl 39]     c.Connie[Water type][lvl 33]");
        System.out.println("[7] Safari Zone");
        System.out.println("[8] Player Options");

        System.out.println(
                "     a.Map     b.Pokemons     c.Bag     d.Badges     e.Profile     f.Guides     g.Save and Exit");
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
                    System.out.println("Invalid choice");
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
            } else if (choice.equals("4")) {
                if (!player.getbadges()[4].equals("Soul Badge")) {
                    System.out.println("You are now challenging Gym Leader Koga!");
                    Battle gymbattle = new Battle(player, "Koga");
                    if (gymbattle.getwin()) {
                        player.obtainbadge("Soul Badge");
                        System.out.println("Koga: Humph! You have proven your worth! Here! Take the Soul Badge!");
                    }
                } else {
                    System.out.println("You have already challenged this gym.");
                }
            } else if (choice.equals("5")) {
                Random r = new Random();
                String[] wilds = { "Grimer", "Rattata", "Raticate" };
                int wild_choice = r.nextInt(3);
                int wild_lvl = r.nextInt(22, 30);
                String wild_pokemon = wilds[wild_choice];
                System.out.println("A wild " + wild_pokemon + " appeared!");
                Pokemon wild = new Pokemon(wild_pokemon, wild_lvl);
                Battle wildbattle = new Battle(player, wild);
            } else if (choice.charAt(0) == '6' && choice.length() == 2) {
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
            } else if (choice.equals("7")) {
                safari.SafariZone();
            } else if (choice.charAt(0) == '8' && choice.length() == 2) {
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

    public static boolean selectionCinnabarIsland(Player player) {
        displayMap(player);
        Scanner input = new Scanner(System.in);
        System.out.println("[1] Move to:");
        ArrayList<String> neighboringCities = library.kantoMap.getNeighbours("Cinnabar Island");
        for (int i = 0; i < neighboringCities.size(); i++) {
            System.out.println((char) ('a' + i) + ". " + neighboringCities.get(i));
        }
        System.out.println("[2] Pokemon Center");
        System.out.println("[3] Poke Mart");
        System.out.println("[4] Fight Gym Leader [ Blaine - Fire type ] [Recommended Pokemon Level: 47]");
        System.out.println("[5] Fight Wild Pokemon [Staryu, Tangela][max lvl 28]");
        System.out.println("[6] Fight other trainers");
        System.out.println(
                "     a.Lil[Water type][lvl 33]    b.Jack[Water type][lvl 37]     c.Jerome[Water type][lvl 33]");
        System.out.println("[7] Player Options");

        System.out.println(
                "     a.Map     b.Pokemons     c.Bag     d.Badges     e.Profile     f.Guides     g.Save and Exit");
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
                    System.out.println("Invalid choice");
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
            } else if (choice.equals("4")) {
                if (!player.getbadges()[6].equals("Volcano Badge")) {
                    System.out.println("You are now challenging Gym Leader Blaine!");
                    Battle gymbattle = new Battle(player, "Blaine");
                    if (gymbattle.getwin()) {
                        player.obtainbadge("Volcano Badge");
                        System.out.println(
                                "Blaine: I have burned down to nothing! Not even ashes remain! You have earned the Volcano Badge.");
                    }
                } else {
                    System.out.println("You have already challenged this gym.");
                }

            } else if (choice.equals("5")) {
                Random r = new Random();
                String[] wilds = { "Staryu", "Tangela" };
                int wild_choice = r.nextInt(2);
                int wild_lvl = r.nextInt(17, 29);
                String wild_pokemon = wilds[wild_choice];
                System.out.println("A wild " + wild_pokemon + " appeared!");
                Pokemon wild = new Pokemon(wild_pokemon, wild_lvl);
                Battle wildbattle = new Battle(player, wild);
            } else if (choice.charAt(0) == '6' && choice.length() == 2) {
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
            } else if (choice.charAt(0) == '7' && choice.length() == 2) {
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

    public static void pokeMart(Player player) {
        displayMap(player);
        Scanner input = new Scanner(System.in);
        System.out.println("You entered the Poke Mart");
        while (true) {
            System.out.println("Hello trainer, welcome to the PokeMart.How can I help you?");
            System.out.println("1. Buy");
            System.out.println("2. Sell");
            System.out.println("3. Exit");
            System.out.print("Choose 1 action: ");
            String action = input.nextLine();
            if (action.equals("1")) {
                System.out.println("+----------------------Buy-----------------------+");
                System.out.println("You have: $" + player.findMoney());
                System.out.println("1. Poke Ball    - $ " + library.pokemon_items.get("Poke Ball").get("price"));
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
                System.out.println("Select items(1-11) to buy/12 to go back: ");
                String choiceitem_st = input.nextLine();
                if (player.isNum(choiceitem_st)) {
                    int choiceitem = Integer.parseInt(choiceitem_st);
                    switch (choiceitem) {
                        case 1: // this is supposed to be checking team while not in battle, hence all pokeballs
                                // used will output "No effect"
                            System.out.println("+-----Poke Ball-----+");
                            System.out.println("You have: " + player.getItems().get("Poke Ball"));
                            System.out.println("Buy Price: $ " + library.pokemon_items.get("Poke Ball").get("price"));
                            System.out.println("How many would you like to buy?");
                            System.out.print("Enter a number: ");
                            String number_st = input.nextLine();
                            if (player.isNum(number_st)) {
                                int number = Integer.parseInt(number_st);
                                int price = number * library.pokemon_items.get("Poke Ball").get("price");
                                if (price > player.findMoney()) {
                                    System.out.println("You don't have enough money.");
                                } else {
                                    System.out.println("That will be $ " + price);
                                    System.out.println(
                                            "Would you like to buy the item(s) you selected?(yes-y/no-any other input)");
                                    String ccc = input.nextLine();
                                    if (ccc.equals("y")) {
                                        player.deductMoney(price);
                                        player.obtainitems("Poke Ball", number);
                                    }
                                }
                            } else {
                                System.out.println("Invalid input");
                            }
                            break;
                        case 2:
                            System.out.println("+-----Great Ball-----+");
                            System.out.println("You have: " + player.getItems().get("Great Ball"));
                            System.out.println("Buy Price: $ " + library.pokemon_items.get("Great Ball").get("price"));
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
                            System.out.println("Buy Price: $ " + library.pokemon_items.get("Ultra Ball").get("price"));
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
                                    .println("Buy Price: $ " + library.pokemon_items.get("Super Potion").get("price"));
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
                                    .println("Buy Price: $ " + library.pokemon_items.get("Hyper Potion").get("price"));
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
                            System.out.println("Buy Price: $ " + library.pokemon_items.get("Max Potion").get("price"));
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
                            System.out.println("Buy Price: $ " + library.pokemon_items.get("X Attack").get("price"));
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
                            System.out.println("Buy Price: $ " + library.pokemon_items.get("X Defend").get("price"));
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
                        case 12:
                            break;
                        default:
                            System.out.println("Invalid choice");
                    }
                } else {
                    System.out.println("Invalid choice");
                }
            } else if (action.equals("2")) {
                System.out.println("+-----------------------Sell-----------------------+");
                System.out.println("Your items: ");
                player.showitems();
                System.out.println("12. Back");
                System.out.println("Note: Sell price of each items is 70% of their buy price.");
                System.out.println("Select items(1-11) to sell/12 to go bak: ");
                String choiceitem_st = input.nextLine();
                if (player.isNum(choiceitem_st)) {
                    int choiceitem = Integer.parseInt(choiceitem_st);
                    switch (choiceitem) {
                        case 1: // this is supposed to be checking team while not in battle, hence all pokeballs
                                // used will output "No effect"
                            System.out.println("+-----Poke Ball-----+");
                            System.out.println("You have: " + player.getItems().get("Poke Ball"));
                            System.out.println(
                                    "Sell Price: $ " + library.pokemon_items.get("Poke Ball").get("price") * 7 / 10);
                            System.out.println("How many would you like to sell?");
                            System.out.print("Enter a number: ");
                            String number_st = input.nextLine();
                            if (player.isNum(number_st)) {
                                int number = Integer.parseInt(number_st);
                                int price = number * library.pokemon_items.get("Poke Ball").get("price") * 7 / 10;
                                if (number > player.getItems().get("Poke Ball")) {
                                    System.out.println("You don't have enough Poke Balls.");
                                } else {
                                    System.out.println("That will be $ " + price);
                                    System.out.println(
                                            "Would you like to sell the item(s) you selected?(yes-y/no-any other input)");
                                    String ccc = input.nextLine();
                                    if (ccc.equals("y")) {
                                        player.addMoney(price);
                                        player.deditems("Poke Ball", number);
                                    }
                                }
                            } else {
                                System.out.println("Invalid input");
                            }
                            break;
                        case 2:
                            System.out.println("+-----Great Ball-----+");
                            System.out.println("You have: " + player.getItems().get("Great Ball"));
                            System.out.println(
                                    "Sell Price: $ " + library.pokemon_items.get("Great Ball").get("price") * 7 / 10);
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
                                    "Sell Price: $ " + library.pokemon_items.get("Ultra Ball").get("price") * 7 / 10);
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
                                    "Sell Price: $ " + library.pokemon_items.get("Super Potion").get("price") * 7 / 10);
                            System.out.println("How many would you like to sell?");
                            System.out.print("Enter a number: ");
                            number_st = input.nextLine();
                            if (player.isNum(number_st)) {
                                int number = Integer.parseInt(number_st);
                                int price = number * library.pokemon_items.get("Super Potion").get("price") * 7 / 10;
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
                                    "Sell Price: $ " + library.pokemon_items.get("Hyper Potion").get("price") * 7 / 10);
                            System.out.println("How many would you like to sell?");
                            System.out.print("Enter a number: ");
                            number_st = input.nextLine();
                            if (player.isNum(number_st)) {
                                int number = Integer.parseInt(number_st);
                                int price = number * library.pokemon_items.get("Hyper Potion").get("price") * 7 / 10;
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
                                    "Sell Price: $ " + library.pokemon_items.get("Max Potion").get("price") * 7 / 10);
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
                        case 12:
                            break;
                        default:
                            System.out.println("Invalid choice");
                    }
                } else {
                    System.out.println("Invalid choice");
                }
            } else if (action.endsWith("3")) {
                System.out.println("Thank you for coming! Hope you have a nice day!");
                break;
            } else {
                System.out.println("Invalid choice");
            }
        }
    }

    public static String displayList(ArrayList<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size() - 1; i++) {
            sb.append(list.get(i)).append(", ");
        }
        sb.append(list.get(list.size() - 1));
        return sb.toString();
    }

    public static void displayMap(Player player) {
        String currentCity = player.findCurrentCity();
        String map = generateMap(currentCity);
        System.out.println("Map of Kanto:");
        System.out.println(map);
        System.out.printf("+%s+\n", "-".repeat(90));
    }

    private static String generateMap(String currentCity) {
        String template = """
            [Pewter City]-----------------------[Cerulean City]-----------------|
                   |                                    |                       |
                   |                                    |                       |
                   |                                    |                       |
                   |                                    |                       |
                   |            [Celadon City]----[Saffron City]-----[Lavender Town]
                   |                   |                |                       |
            [Viridian City]            |                |                       |
                   |                   |                |                       |
                   |                   |                |                       |
                   |                   |        [Vermillion City]---------------|
                   |                   |                                        |
            [Pallet Town]              |                                        |
                   |                   |                                        |
                   |             [Fuchsia City]---------------------------------|
                   |                   |
                   |                   |
            [Cinnabar Island]----------|""";

        return template.replace("[" + currentCity + "]", "[**" + currentCity + "**]");
    }
}
