package pokemon_kanto_adventure;

public class Move {
    private String movename; //the name of the move
    private String type; //the type of the move, if have
    private String category; //the category of the move
    private int order; //the order of the move
    private double atk; //the atk stat change of the move, if have
    private double def; //the def stat change of the move, if have
    private double sp; //the sp stat change of the move, if have
    private double dmghealratio; //the damage heal ratio of the move, if have
    private double foe_atk;//the foe_atk stat change of the move, if have
    private double foe_def;//the foe_def stat change of the move, if have
    private double foe_sp; //the fe_sp stat chagne of the move, if have
    private int power; //the base power of the move, if have
    private double hphealratio; //the hp heal ratio of the move, if have
    private String description; //the description of the move

    public Move(String n, int lvl) { //create a move using the name of the move, and the level of the pokemon
        movename = n; //set the name of the move to n
        category = library.move_cat.get(movename); //set the category of the move with library.move_cat Hashamp using movename as key
        order = library.move_order.get(movename); //set the order of the move with library.move_order Hashamp using movename as key
        description = library.move_description.get(movename); //set the description of the move with library.move_description Hashamp using movename as key
        if (category.equals("dmg")) { //if the category of the move is 'dmg'
            type = library.move_type.get(movename); //set the type of the move with library.move_type Hashamp using movename as key
            power = library.move_dmg.get(movename).get(lvl);//set the power of the move with library.move_dmg Hashamp using movename as key 
            //we get another HashMap that contains the power of the move according to level, and use level as key to get the power
        } else if (category.equals("stat")) { //if the category of the move is 'stat'
            //set the stat changes of the moves with library.move_stat using movename as a key to get a HashMap that contains each stat changes
            //and then use atk, def, sp, foe_atk, foe_def and foe_sp as keys to get the stat changes respectively
            atk = library.move_stat.get(movename).get("atk");
            def = library.move_stat.get(movename).get("def");
            sp = library.move_stat.get(movename).get("sp");
            foe_atk = library.move_stat.get(movename).get("foe_atk");
            foe_def = library.move_stat.get(movename).get("foe_def");
            foe_sp = library.move_stat.get(movename).get("foe_sp");
        } else if (category.equals("heal")) { //if the category of the move is 'heal'
            hphealratio = 0.5; //set the hp heal ratio to 0.5
        } else if (category.equals("sp")) { //if the category of the move is 'sp'

            power = library.move_dmg.get(movename).get(lvl); //get the power of the move with the same way when category is 'dmg'
            if (power != 0) { //if power is not 0
                type = library.move_type.get(movename); //get the type of the move with the same way when category is 'dmg'
            }
            //set the stat changes of the moves with library.move_stat using movename as a key to get a HashMap that contains each stat changes
            //and then use atk, def, sp, foe_atk, foe_def and foe_sp as keys to get the stat changes respectively
            //and get dmghealratio from the same way using healratio as the key instead of atk,def,sp...
            atk = library.move_stat.get(movename).get("atk");
            def = library.move_stat.get(movename).get("def");
            sp = library.move_stat.get(movename).get("sp");
            dmghealratio = library.move_stat.get(movename).get("healratio");
            foe_atk = library.move_stat.get(movename).get("foe_atk");
            foe_def = library.move_stat.get(movename).get("foe_def");
            foe_sp = library.move_stat.get(movename).get("foe_sp");
        }
    }

    public String getName() { //return the name of the move
        return movename;
    }

    public int getPower() { //return the power of the move
        return power;
    }

    public String getType() { //return the type of the move
        return type;
    }

    public int getOrder() { //return the order of the move
        return order;
    }

    public String getCategory() { //return the category of the move
        return category;
    }

    public double getAtk() { //return the atk stat change of the move
        return atk;
    }

    public double getDef() { //return the def stat change of the move
        return def;
    }

    public double getSp() { //return the sp stat change of the move
        return sp;
    }

    public double getdmgheal() { //return the damage heal ratio of the move
        return dmghealratio;
    }

    public double gethpheal() { //return the hp heal ratio of the move
        return hphealratio;
    }

    public double getFAtk() {//return the foe_atk stat change of the move
        return foe_atk;
    }

    public double getFDef() {//return the foe_def stat change of the move
        return foe_def;
    }

    public double getFSp() {//return the foe_sp stat change of the move
        return foe_sp;
    }

    public String description() { //return the description of the move
        return description;
    }

    public void showmovdetail() { //display the details of the move
        System.out.println("+-------------------Move Details-------------------+");
        System.out.println(movename);
        System.out.println("Category: " + category);
        System.out.println("Move order: " + order);
        if (category.equals("dmg")) {
            System.out.println("Move type: " + type);
            System.out.println("Move Power: " + power);
        } else if (category.equals("heal")) {
            System.out.println("This move will heal your pokemon for 50% of its max hp");
        } else if (category.equals("stat")) {
            if (atk != 0.0) {
                System.out.println("This move alters this pokemon's attack by " + (int) atk + " stages");
            }
            if (def != 0.0) {
                System.out.println("This move alters this pokemon's defense by " + (int) def + " stages");
            }
            if (sp != 0.0) {
                System.out.println("This move alters this pokemon's speed by " + (int) sp + " stages");
            }
            if (foe_atk != 0.0) {
                System.out.println("This move alters foe's pokemon's attack by " + (int) foe_atk + " stages");
            }
            if (foe_def != 0.0) {
                System.out.println("This move alters foe's pokemon's defense by " + (int) foe_def + " stages");
            }
            if (foe_sp != 0.0) {
                System.out.println("This move alters foe's pokemon's speed by " + (int) foe_sp + " stages");
            }
        } else if (category.equals("sp")) {
            if (power != 0) {
                System.out.println("Move type: " + type);
                System.out.println("Move Power: " + power);
            }
            if (atk != 0.0) {
                System.out.println("This move alters this pokemon's attack by " + (int) atk + " stages");
            }
            if (def != 0.0) {
                System.out.println("This move alters this pokemon's defense by " + (int) def + " stages");
            }
            if (sp != 0.0) {
                System.out.println("This move alters this pokemon's speed by " + (int) def + " stages");
            }
            if (foe_atk != 0.0) {
                System.out.println("This move alters foe's pokemon's attack by " + (int) foe_atk + " stages");
            }
            if (foe_def != 0.0) {
                System.out.println("This move alters foe's pokemon's defense by " + (int) foe_def + " stages");
            }
            if (foe_sp != 0.0) {
                System.out.println("This move alters foe's pokemon's speed by " + (int) foe_sp + " stages");
            }
            if (dmghealratio != 0.0) {
                System.out.println("This move heals your pokemon by " + dmghealratio * 100 + " % of damage dealt");
            }
        }
        System.out.println(description());
    }
}